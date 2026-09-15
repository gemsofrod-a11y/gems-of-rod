from fastapi import Depends, FastAPI, HTTPException
from pydantic import BaseModel

from app import config, db, triage, voice_agent
from app.auth import require_token

app = FastAPI(title="Gems of Rod — Assistant vocal Gmail")


@app.on_event("startup")
def _startup() -> None:
    db.init_db()


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


@app.post("/api/triage/run", dependencies=[Depends(require_token)])
def run_triage() -> dict:
    return triage.run_triage_cycle()


@app.get("/api/digest/today", dependencies=[Depends(require_token)])
def digest_today() -> dict:
    return db.count_today_actions()
