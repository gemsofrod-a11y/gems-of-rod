"""Cycle de triage automatique : parcourt les nouveaux emails, les fait
classer par le classificateur, et applique l'action correspondant à
l'autonomie choisie (voir classifier.py). Conçu pour être lancé
périodiquement (cron / systemd timer / scripts/run_triage_loop.py).
"""
from app import classifier, config, db, gmail_client


def run_triage_cycle() -> dict:
    db.init_db()
    summary = {"auto_triage": 0, "auto_reply": 0, "needs_confirmation": 0, "ignore": 0,
               "skipped_already_processed": 0, "errors": []}

    try:
        messages = gmail_client.search_messages(config.TRIAGE_QUERY, config.TRIAGE_MAX_RESULTS)
    except Exception as e:
        summary["errors"].append(f"Recherche Gmail impossible : {e}")
        return summary

    for meta in messages:
        message_id = meta["id"]
        if db.is_processed(message_id):
            summary["skipped_already_processed"] += 1
            continue
        try:
            category = _process_one(message_id)
            summary[category] = summary.get(category, 0) + 1
        except Exception as e:
            summary["errors"].append(f"{message_id}: {e}")
            continue

    return summary


def _process_one(message_id: str) -> str:
    email = gmail_client.get_message(message_id)
    decision = classifier.classify_email(email)
    category = decision["category"]

    if category == "ignore":
        gmail_client.mark_spam(message_id) if _looks_like_spam(email) else gmail_client.mark_read(message_id)
        db.log_action(message_id, "auto_ignore", decision.get("reasoning", ""))

    elif category == "auto_triage":
        if decision.get("suggested_label"):
            gmail_client.add_label(message_id, decision["suggested_label"])
        gmail_client.add_label(message_id, config.LABEL_AUTO_TRAITE)
        gmail_client.archive_message(message_id)
        db.log_action(message_id, "auto_triage", decision.get("reasoning", ""))

    elif category == "auto_reply":
        reply = decision.get("suggested_reply")
        if reply:
            gmail_client.send_reply(message_id, reply)
            gmail_client.add_label(message_id, config.LABEL_AUTO_REPONDU)
            gmail_client.archive_message(message_id)
            db.log_action(message_id, "auto_reply", reply)
        else:
            category = "needs_confirmation"

    if category == "needs_confirmation":
        gmail_client.add_label(message_id, config.LABEL_EN_ATTENTE)
        db.create_pending(
            message_id=message_id,
            thread_id=email.get("thread_id"),
            from_addr=email.get("from", ""),
            subject=email.get("subject", ""),
            snippet=email.get("snippet", ""),
            category=category,
            reasoning=decision.get("reasoning", ""),
            suggested_reply=decision.get("suggested_reply"),
        )
        db.log_action(message_id, "queued_for_confirmation", decision.get("reasoning", ""))

    db.mark_processed(message_id, category)
    return category


def _looks_like_spam(email: dict) -> bool:
    return "unsubscribe" in email.get("body_text", "").lower() and \
        "SPAM" not in email.get("labels", [])
