import uuid
from datetime import datetime
from agent import config


def _build_html(
    titre_principal: str,
    introduction: str,
    sections: list[dict],
    offre_speciale: str = "",
    call_to_action: str = "Découvrir la collection",
) -> str:
    sections_html = ""
    for section in sections:
        sections_html += f"""
        <div style="margin:32px 0; border-left:3px solid #c9a96e; padding-left:16px;">
          <h3 style="font-weight:400; color:#111; margin:0 0 8px;">{section.get('titre', '')}</h3>
          <p style="color:#555; margin:0; line-height:1.7;">{section.get('contenu', '')}</p>
        </div>"""

    offre_html = ""
    if offre_speciale:
        offre_html = f"""
        <div style="background:#faf7f2; border:1px solid #c9a96e; padding:24px; margin:32px 0; text-align:center;">
          <p style="font-size:14px; letter-spacing:2px; text-transform:uppercase; color:#c9a96e; margin:0 0 8px;">Offre exclusive</p>
          <p style="font-size:18px; font-weight:300; color:#111; margin:0;">{offre_speciale}</p>
        </div>"""

    return f"""<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
</head>
<body style="margin:0; font-family:'Helvetica Neue',Arial,sans-serif; background:#fff; color:#111;">
  <div style="max-width:600px; margin:0 auto; padding:40px 24px;">

    <div style="text-align:center; margin-bottom:40px; border-bottom:1px solid #e5e5e5; padding-bottom:32px;">
      <p style="font-size:11px; letter-spacing:4px; text-transform:uppercase; color:#999; margin:0 0 8px;">Gems of Rod</p>
      <h1 style="font-weight:300; font-size:28px; margin:0; color:#111;">{titre_principal}</h1>
    </div>

    <p style="font-size:16px; line-height:1.8; color:#333;">{introduction}</p>

    {sections_html}
    {offre_html}

    <div style="text-align:center; margin:48px 0;">
      <a href="https://gems-of-rod.fr" style="display:inline-block; padding:14px 36px; border:1px solid #111; color:#111; text-decoration:none; font-size:11px; letter-spacing:3px; text-transform:uppercase;">{call_to_action}</a>
    </div>

    <div style="border-top:1px solid #e5e5e5; padding-top:24px; text-align:center; font-size:11px; color:#999;">
      <p style="margin:0;">© {datetime.now().year} Gems of Rod – France</p>
      <p style="margin:8px 0 0;"><a href="#" style="color:#999;">Se désinscrire</a></p>
    </div>
  </div>
</body>
</html>"""


def create_newsletter(
    sujet: str,
    titre_principal: str,
    introduction: str,
    sections: list[dict] | None = None,
    produits_en_vedette: list[str] | None = None,
    offre_speciale: str = "",
    call_to_action: str = "Découvrir la collection",
) -> dict:
    newsletter_id = f"NL-{uuid.uuid4().hex[:8].upper()}"
    html_content = _build_html(
        titre_principal, introduction, sections or [], offre_speciale, call_to_action
    )

    newsletter = {
        "id": newsletter_id,
        "sujet": sujet,
        "titre_principal": titre_principal,
        "introduction": introduction,
        "sections": sections or [],
        "produits_en_vedette": produits_en_vedette or [],
        "offre_speciale": offre_speciale,
        "call_to_action": call_to_action,
        "html_content": html_content,
        "status": "draft",
        "created_at": datetime.now().isoformat(),
        "sent_at": None,
        "recipients_count": 0,
    }

    path = config.EMAIL_DIRS["newsletters"] / f"{newsletter_id}.json"
    config.save_json(path, newsletter)

    html_path = config.EMAIL_DIRS["newsletters"] / f"{newsletter_id}.html"
    html_path.write_text(html_content, encoding="utf-8")

    return {
        "success": True,
        "newsletter_id": newsletter_id,
        "newsletter": newsletter,
        "html_file": str(html_path),
        "next_step": "Newsletter prête. Utiliser mcp__Gmail__create_draft pour créer le brouillon.",
    }


def list_newsletters(status: str = "all") -> dict:
    newsletters = []
    for f in config.EMAIL_DIRS["newsletters"].glob("*.json"):
        nl = config.load_json(f)
        if status == "all" or nl.get("status") == status:
            newsletters.append({
                "id": nl.get("id"),
                "sujet": nl.get("sujet"),
                "status": nl.get("status"),
                "created_at": nl.get("created_at"),
            })
    return {"newsletters": newsletters, "total": len(newsletters)}


def get_newsletter(newsletter_id: str) -> dict:
    path = config.EMAIL_DIRS["newsletters"] / f"{newsletter_id}.json"
    if not path.exists():
        return {"error": f"Newsletter {newsletter_id} introuvable"}
    return {"newsletter": config.load_json(path)}
