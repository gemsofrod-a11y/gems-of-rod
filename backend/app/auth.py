import secrets

from fastapi import Header, HTTPException

from app import config


def require_token(authorization: str = Header(default="")) -> None:
    if not config.ASSISTANT_API_TOKEN:
        raise HTTPException(500, "ASSISTANT_API_TOKEN n'est pas configuré côté serveur.")
    prefix = "Bearer "
    token = authorization[len(prefix):] if authorization.startswith(prefix) else ""
    if not token or not secrets.compare_digest(token, config.ASSISTANT_API_TOKEN):
        raise HTTPException(401, "Token invalide.")
