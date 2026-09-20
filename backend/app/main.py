import threading
import time
from pathlib import Path

from fastapi import Depends, FastAPI, HTTPException
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel

from app import config, db, gmail_client, triage, voice_agent
from app.auth import require_token

app = FastAPI(title="Gems of Rod — Assistant vocal Gmail")

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
def voice(req: VoiceRequest) -> dict:
    if not req.text.strip():
        raise HTTPException(400, "Texte vide.")
    return voice_agent.handle_turn(req.text, req.session_id)


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


# Client web (voix depuis le téléphone, voir backend/static/) : monté en
# dernier pour ne jamais intercepter les routes /api/* déclarées ci-dessus.
_STATIC_DIR = Path(__file__).parent.parent / "static"
if _STATIC_DIR.exists():
    app.mount("/", StaticFiles(directory=_STATIC_DIR, html=True), name="static")
