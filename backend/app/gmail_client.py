"""Client Gmail : authentification OAuth (voir scripts/gmail_oauth_setup.py) et
actions (lecture, réponse, archivage, étiquetage, spam, corbeille, désabonnement)
via l'API Gmail officielle.

Scope unique utilisé : gmail.modify (lecture, envoi, gestion des libellés,
archivage/corbeille — pas de suppression définitive/irréversible possible avec
ce scope : un message mis à la corbeille reste récupérable 30 jours, comme
dans l'interface Gmail).
"""
import base64
import re
from email.mime.text import MIMEText

import requests
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

from app import config

_service = None
_label_cache: dict[str, str] = {}


class GmailNotAuthenticated(RuntimeError):
    pass


def get_service():
    global _service
    if _service is not None:
        return _service
    if not config.TOKEN_PATH.exists():
        raise GmailNotAuthenticated(
            "Aucun token.json trouvé. Lancez d'abord "
            "`python -m backend.scripts.gmail_oauth_setup`."
        )
    creds = Credentials.from_authorized_user_file(str(config.TOKEN_PATH), config.GMAIL_SCOPES)
    if creds.expired and creds.refresh_token:
        creds.refresh(Request())
        config.TOKEN_PATH.write_text(creds.to_json(), encoding="utf-8")
    _service = build("gmail", "v1", credentials=creds)
    return _service


def _header(headers: list[dict], name: str) -> str:
    for h in headers:
        if h["name"].lower() == name.lower():
            return h["value"]
    return ""


def _extract_body_text(payload: dict) -> str:
    if payload.get("mimeType") == "text/plain" and payload.get("body", {}).get("data"):
        return base64.urlsafe_b64decode(payload["body"]["data"]).decode("utf-8", "replace")
    for part in payload.get("parts", []) or []:
        text = _extract_body_text(part)
        if text:
            return text
    if payload.get("body", {}).get("data"):
        return base64.urlsafe_b64decode(payload["body"]["data"]).decode("utf-8", "replace")
    return ""


def parse_message(raw: dict) -> dict:
    headers = raw.get("payload", {}).get("headers", [])
    return {
        "id": raw["id"],
        "thread_id": raw.get("threadId"),
        "message_id_header": _header(headers, "Message-Id"),
        "from": _header(headers, "From"),
        "to": _header(headers, "To"),
        "subject": _header(headers, "Subject"),
        "date": _header(headers, "Date"),
        "snippet": raw.get("snippet", ""),
        "body_text": _extract_body_text(raw.get("payload", {})),
        "labels": raw.get("labelIds", []),
        "list_unsubscribe": _header(headers, "List-Unsubscribe"),
        "list_unsubscribe_post": _header(headers, "List-Unsubscribe-Post"),
    }


def search_messages(query: str, max_results: int = 10) -> list[dict]:
    service = get_service()
    try:
        resp = service.users().messages().list(
            userId="me", q=query, maxResults=max_results
        ).execute()
    except HttpError as e:
        raise RuntimeError(f"Erreur recherche Gmail: {e}") from e
    results = []
    for m in resp.get("messages", []):
        raw = service.users().messages().get(userId="me", id=m["id"], format="metadata",
                                              metadataHeaders=["From", "To", "Subject", "Date"]).execute()
        headers = raw.get("payload", {}).get("headers", [])
        results.append({
            "id": raw["id"],
            "thread_id": raw.get("threadId"),
            "from": _header(headers, "From"),
            "subject": _header(headers, "Subject"),
            "date": _header(headers, "Date"),
            "snippet": raw.get("snippet", ""),
            "labels": raw.get("labelIds", []),
        })
    return results


def get_message(message_id: str) -> dict:
    service = get_service()
    raw = service.users().messages().get(userId="me", id=message_id, format="full").execute()
    return parse_message(raw)


def _get_or_create_label(name: str) -> str:
    if name in _label_cache:
        return _label_cache[name]
    service = get_service()
    labels = service.users().labels().list(userId="me").execute().get("labels", [])
    for lbl in labels:
        if lbl["name"] == name:
            _label_cache[name] = lbl["id"]
            return lbl["id"]
    created = service.users().labels().create(
        userId="me",
        body={"name": name, "labelListVisibility": "labelShow", "messageListVisibility": "show"},
    ).execute()
    _label_cache[name] = created["id"]
    return created["id"]


def add_label(message_id: str, label_name: str) -> None:
    label_id = _get_or_create_label(label_name)
    get_service().users().messages().modify(
        userId="me", id=message_id, body={"addLabelIds": [label_id]}
    ).execute()


def archive_message(message_id: str) -> None:
    get_service().users().messages().modify(
        userId="me", id=message_id, body={"removeLabelIds": ["INBOX"]}
    ).execute()


def mark_read(message_id: str) -> None:
    get_service().users().messages().modify(
        userId="me", id=message_id, body={"removeLabelIds": ["UNREAD"]}
    ).execute()


def mark_spam(message_id: str) -> None:
    get_service().users().messages().modify(
        userId="me", id=message_id, body={"addLabelIds": ["SPAM"], "removeLabelIds": ["INBOX"]}
    ).execute()


def trash_message(message_id: str) -> None:
    get_service().users().messages().trash(userId="me", id=message_id).execute()


_LIST_UNSUB_ENTRY = re.compile(r"<([^>]+)>")


def _parse_list_unsubscribe(header_value: str) -> list[str]:
    return _LIST_UNSUB_ENTRY.findall(header_value or "")


def unsubscribe(message_id: str) -> dict:
    """Tente de se désabonner d'un email via son en-tête List-Unsubscribe
    (RFC 8058 : POST one-click quand disponible, sinon GET, sinon mailto:).
    Best-effort : de nombreux expéditeurs exigent une confirmation manuelle
    que cette méthode ne peut pas franchir ; le résultat le signale.
    """
    original = get_message(message_id)
    candidates = _parse_list_unsubscribe(original.get("list_unsubscribe", ""))
    if not candidates:
        return {"attempted": False, "method": "none", "detail": "Aucun en-tête List-Unsubscribe."}

    https_links = [c for c in candidates if c.lower().startswith(("http://", "https://"))]
    mailto_links = [c for c in candidates if c.lower().startswith("mailto:")]
    one_click = "one-click" in original.get("list_unsubscribe_post", "").lower()

    if https_links and one_click:
        url = https_links[0]
        try:
            resp = requests.post(
                url,
                data={"List-Unsubscribe": "One-Click"},
                timeout=10,
            )
            return {
                "attempted": True,
                "method": "one-click",
                "success": resp.ok,
                "detail": f"POST {url} -> {resp.status_code}",
            }
        except requests.RequestException as e:
            return {"attempted": True, "method": "one-click", "success": False, "detail": str(e)}

    if https_links:
        url = https_links[0]
        try:
            resp = requests.get(url, timeout=10)
            return {
                "attempted": True,
                "method": "get",
                "success": resp.ok,
                "detail": f"GET {url} -> {resp.status_code} (peut nécessiter une confirmation manuelle)",
            }
        except requests.RequestException as e:
            return {"attempted": True, "method": "get", "success": False, "detail": str(e)}

    if mailto_links:
        mailto = mailto_links[0][len("mailto:"):]
        to_addr, _, query = mailto.partition("?")
        subject = "unsubscribe"
        for part in query.split("&"):
            key, _, value = part.partition("=")
            if key.lower() == "subject" and value:
                subject = requests.utils.unquote(value)
        msg = MIMEText("", _charset="utf-8")
        msg["To"] = to_addr
        msg["Subject"] = subject
        raw = base64.urlsafe_b64encode(msg.as_bytes()).decode("utf-8")
        get_service().users().messages().send(userId="me", body={"raw": raw}).execute()
        return {"attempted": True, "method": "mailto", "success": True, "detail": f"Email envoyé à {to_addr}"}

    return {"attempted": False, "method": "none", "detail": "Aucune méthode de désabonnement exploitable."}


def _build_reply_mime(original: dict, body_text: str) -> dict:
    to_addr = original["from"]
    subject = original["subject"]
    if not subject.lower().startswith("re:"):
        subject = f"Re: {subject}"
    msg = MIMEText(body_text, _charset="utf-8")
    msg["To"] = to_addr
    msg["Subject"] = subject
    if original.get("message_id_header"):
        msg["In-Reply-To"] = original["message_id_header"]
        msg["References"] = original["message_id_header"]
    raw = base64.urlsafe_b64encode(msg.as_bytes()).decode("utf-8")
    return {"raw": raw, "threadId": original["thread_id"]}


def send_reply(message_id: str, body_text: str) -> str:
    """Envoie une réponse — toujours en créant d'abord le brouillon Gmail puis
    en l'envoyant (jamais un envoi direct sans brouillon, voir CLAUDE.md :
    « Jamais envoyer d'email sans sauvegarder le brouillon d'abord »). Le
    brouillon disparaît de la boîte de brouillons une fois envoyé, exactement
    comme si Sébastien avait cliqué "Envoyer" sur ce brouillon lui-même.
    """
    original = get_message(message_id)
    body = _build_reply_mime(original, body_text)
    draft = get_service().users().drafts().create(userId="me", body={"message": body}).execute()
    sent = get_service().users().drafts().send(userId="me", body={"id": draft["id"]}).execute()
    return sent["id"]


def create_draft_reply(message_id: str, body_text: str) -> str:
    original = get_message(message_id)
    body = _build_reply_mime(original, body_text)
    draft = get_service().users().drafts().create(userId="me", body={"message": body}).execute()
    return draft["id"]


def send_draft(draft_id: str) -> str:
    """Envoie un brouillon déjà créé (ex. après relecture dans l'app, quand
    Sébastien décide d'envoyer un brouillon proposé par create_draft)."""
    sent = get_service().users().drafts().send(userId="me", body={"id": draft_id}).execute()
    return sent["id"]
