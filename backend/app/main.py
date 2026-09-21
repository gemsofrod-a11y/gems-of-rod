import json
import threading
import time
from pathlib import Path

from fastapi import Depends, FastAPI, HTTPException
from fastapi.responses import Response, StreamingResponse
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel
from starlette.middleware.base import BaseHTTPMiddleware

from app import config, db, gmail_client, triage, voice_agent
from app.auth import require_token

app = FastAPI(title="Gems of Rod — Assistant vocal Gmail")


class _NoCacheStaticMiddleware(BaseHTTPMiddleware):
    """Le client web (index.html) est réinstallé sur le téléphone comme une
    app (icône "Ajouter à l'écran d'accueil") : sans cet en-tête, mobile
    Safari/Chrome peuvent continuer à servir une version en cache pendant
    longtemps après un déploiement, donnant l'impression qu'un changement
    livré n'est jamais arrivé. On force donc le navigateur à toujours
    revalider le fichier statique auprès du serveur.
    """

    async def dispatch(self, request, call_next):
        response = await call_next(request)
        if not request.url.path.startswith("/api/"):
            response.headers["Cache-Control"] = "no-store, must-revalidate"
        return response


app.add_middleware(_NoCacheStaticMiddleware)

_triage_thread_started = False


def _triage_loop() -> None:
    while True:
        try:
            triage.run_triage_cycle()
        except Exception as e:  # ne jamais laisser mourir la boucle de fond
            print(f"Erreur cycle de triage : {e}")
        time.sleep(config.TRIAGE_INTERVAL_SECONDS)


@app.on_event("startup")
def _startup() -> None:
    global _triage_thread_started
    db.init_db()
    if config.TRIAGE_AUTOSTART and not _triage_thread_started:
        threading.Thread(target=_triage_loop, daemon=True).start()
        _triage_thread_started = True


@app.get("/api/health")
def health() -> dict:
    return {"status": "ok"}


class VoiceRequest(BaseModel):
    text: str
    session_id: str = "default"


@app.post("/api/voice", dependencies=[Depends(require_token)])
def voice(req: VoiceRequest) -> StreamingResponse:
    """Répond en streaming (Server-Sent Events) : des évènements
    {"type": "text_delta", ...} au fil de la génération de la réponse, puis
    un dernier {"type": "done", ...} avec la forme complète attendue par le
    reste de l'interface (actions/emails/drafts). Voir
    voice_agent.handle_turn_stream : permet au client de commencer à parler
    dès la première phrase plutôt que d'attendre la réponse entière."""
    if not req.text.strip():
        raise HTTPException(400, "Texte vide.")

    def event_stream():
        for chunk in voice_agent.handle_turn_stream(req.text, req.session_id):
            yield f"data: {json.dumps(chunk)}\n\n"

    return StreamingResponse(event_stream(), media_type="text/event-stream")


@app.get("/api/voice/history", dependencies=[Depends(require_token)])
def voice_history(session_id: str = "default") -> list[dict]:
    return voice_agent.get_display_history(session_id)


@app.post("/api/voice/reset", dependencies=[Depends(require_token)])
def voice_reset(session_id: str = "default") -> dict:
    voice_agent.reset_conversation(session_id)
    return {"message": "Conversation réinitialisée."}


@app.get("/api/pending", dependencies=[Depends(require_token)])
def pending() -> list[dict]:
    return db.list_pending()


class ResolveRequest(BaseModel):
    edited_reply: str | None = None


@app.post("/api/pending/{pending_id}/approve", dependencies=[Depends(require_token)])
def approve(pending_id: str, req: ResolveRequest) -> dict:
    result = voice_agent.resolve_pending_action(pending_id, "approve", req.edited_reply)
    return {"message": result}


@app.post("/api/pending/{pending_id}/reject", dependencies=[Depends(require_token)])
def reject(pending_id: str) -> dict:
    result = voice_agent.resolve_pending_action(pending_id, "reject", None)
    return {"message": result}


@app.post("/api/drafts/{draft_id}/send", dependencies=[Depends(require_token)])
def send_draft(draft_id: str) -> dict:
    """Envoie un brouillon déjà créé par create_draft, après relecture dans
    l'app (voir renderReplyCard côté client : le brouillon est montré en
    train de "s'écrire" pour que Sébastien puisse décider de l'envoyer sans
    avoir à ouvrir Gmail)."""
    sent_id = gmail_client.send_draft(draft_id)
    db.log_action(None, "voice_send_draft", draft_id)
    return {"message": "Réponse envoyée.", "sent_id": sent_id}


@app.post("/api/triage/run", dependencies=[Depends(require_token)])
def run_triage() -> dict:
    return triage.run_triage_cycle()


@app.get("/api/digest/today", dependencies=[Depends(require_token)])
def digest_today() -> dict:
    return db.count_today_actions()


@app.get("/api/actions/recent", dependencies=[Depends(require_token)])
def actions_recent(since: str = "") -> list[dict]:
    return db.list_actions_since(since)


@app.get("/api/attachment/{message_id}/{attachment_id}", dependencies=[Depends(require_token)])
def get_attachment(message_id: str, attachment_id: str, filename: str = "document.pdf") -> Response:
    """Sert le contenu brut d'une pièce jointe Gmail (PDF), pour affichage
    dans le fil de discussion du client web (voir voice_agent.read_pdf_attachment
    et index.html/renderDocumentCard). Le client récupère ces octets avec le
    même jeton Bearer que les autres appels /api/*, puis les affiche via une
    URL blob locale — le jeton ne transite donc jamais dans l'URL elle-même."""
    data = gmail_client.get_attachment_bytes(message_id, attachment_id)
    safe_filename = "".join(c for c in filename if c not in '"\r\n') or "document.pdf"
    return Response(
        content=data,
        media_type="application/pdf",
        headers={"Content-Disposition": f'inline; filename="{safe_filename}"'},
    )


# Client web (voix depuis le téléphone, voir backend/static/) : monté en
# dernier pour ne jamais intercepter les routes /api/* déclarées ci-dessus.
_STATIC_DIR = Path(__file__).parent.parent / "static"
if _STATIC_DIR.exists():
    app.mount("/", StaticFiles(directory=_STATIC_DIR, html=True), name="static")
