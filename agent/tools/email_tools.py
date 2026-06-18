import uuid
from datetime import datetime
from agent import config


def _build_summary_html(
    actions_effectuees: list[str],
    elements_en_attente: list[str],
    questions: list[str],
    prochaines_etapes: list[str],
) -> str:
    def items_html(items: list[str]) -> str:
        if not items:
            return "<p style='color:#999; font-style:italic;'>Aucun</p>"
        return "<ul style='margin:8px 0; padding-left:20px;'>" + "".join(
            f"<li style='margin:6px 0; color:#333;'>{item}</li>" for item in items
        ) + "</ul>"

    now = datetime.now()
    return f"""<!DOCTYPE html>
<html lang="fr">
<head><meta charset="UTF-8" /></head>
<body style="margin:0; font-family:'Helvetica Neue',Arial,sans-serif; background:#fff; color:#111;">
  <div style="max-width:600px; margin:0 auto; padding:40px 24px;">

    <div style="text-align:center; margin-bottom:32px; border-bottom:1px solid #e5e5e5; padding-bottom:24px;">
      <p style="font-size:11px; letter-spacing:4px; text-transform:uppercase; color:#c9a96e; margin:0 0 4px;">Gems of Rod — Agent IA</p>
      <h1 style="font-weight:300; font-size:22px; margin:0;">Récapitulatif du {now.strftime('%d/%m/%Y')}</h1>
      <p style="color:#999; font-size:13px; margin:8px 0 0;">Rapport généré à {now.strftime('%H:%M')}</p>
    </div>

    <div style="margin:24px 0;">
      <h2 style="font-size:13px; letter-spacing:2px; text-transform:uppercase; color:#c9a96e; border-bottom:1px solid #f0e8d8; padding-bottom:8px;">✓ Actions effectuées</h2>
      {items_html(actions_effectuees)}
    </div>

    <div style="margin:24px 0;">
      <h2 style="font-size:13px; letter-spacing:2px; text-transform:uppercase; color:#e5a830; border-bottom:1px solid #f0e8d8; padding-bottom:8px;">⏳ En attente de validation</h2>
      {items_html(elements_en_attente)}
    </div>

    <div style="margin:24px 0;">
      <h2 style="font-size:13px; letter-spacing:2px; text-transform:uppercase; color:#d46b4b; border-bottom:1px solid #f0e8d8; padding-bottom:8px;">❓ Questions pour vous</h2>
      {items_html(questions)}
    </div>

    <div style="margin:24px 0;">
      <h2 style="font-size:13px; letter-spacing:2px; text-transform:uppercase; color:#555; border-bottom:1px solid #f0e8d8; padding-bottom:8px;">→ Prochaines étapes</h2>
      {items_html(prochaines_etapes)}
    </div>

    <div style="background:#faf7f2; border:1px solid #e5ddd0; padding:16px; margin-top:32px; text-align:center; font-size:12px; color:#777;">
      Gems of Rod AI Agent · {now.strftime('%d/%m/%Y %H:%M')}
    </div>
  </div>
</body>
</html>"""


def generate_daily_summary(
    actions_effectuees: list[str],
    elements_en_attente: list[str] | None = None,
    questions: list[str] | None = None,
    prochaines_etapes: list[str] | None = None,
) -> dict:
    summary_id = f"SUM-{datetime.now().strftime('%Y%m%d')}-{uuid.uuid4().hex[:4].upper()}"
    html = _build_summary_html(
        actions_effectuees,
        elements_en_attente or [],
        questions or [],
        prochaines_etapes or [],
    )

    summary = {
        "id": summary_id,
        "type": "daily_summary",
        "to_email": config.USER_EMAIL,
        "subject": f"[Gems of Rod] Récapitulatif agent IA — {datetime.now().strftime('%d/%m/%Y')}",
        "html_content": html,
        "actions_effectuees": actions_effectuees,
        "elements_en_attente": elements_en_attente or [],
        "questions": questions or [],
        "prochaines_etapes": prochaines_etapes or [],
        "status": "draft",
        "created_at": datetime.now().isoformat(),
    }

    path = config.EMAIL_DIRS["emails"] / f"{summary_id}.json"
    config.save_json(path, summary)

    html_path = config.EMAIL_DIRS["emails"] / f"{summary_id}.html"
    html_path.write_text(html, encoding="utf-8")

    return {
        "success": True,
        "summary_id": summary_id,
        "summary": summary,
        "html_file": str(html_path),
        "to_email": config.USER_EMAIL,
        "subject": summary["subject"],
        "next_step": "Utiliser mcp__Gmail__create_draft pour créer le brouillon du récapitulatif",
    }


def list_email_drafts() -> dict:
    drafts = []
    for f in config.EMAIL_DIRS["emails"].glob("*.json"):
        draft = config.load_json(f)
        drafts.append({
            "id": draft.get("id"),
            "type": draft.get("type"),
            "to_email": draft.get("to_email") or draft.get("to_email"),
            "subject": draft.get("subject") or draft.get("sujet"),
            "status": draft.get("status"),
            "created_at": draft.get("created_at"),
        })
    return {"drafts": drafts, "total": len(drafts)}


def get_email_draft(draft_id: str) -> dict:
    for f in config.EMAIL_DIRS["emails"].glob("*.json"):
        draft = config.load_json(f)
        if draft.get("id") == draft_id:
            return {"draft": draft}
    return {"error": f"Brouillon {draft_id} introuvable"}
