"""
Orchestration entre l'agent Python et les outils MCP (Gmail, Canva, Drive).
Ce module fournit les données formatées pour chaque outil MCP.
"""
import json
from datetime import datetime
from pathlib import Path
from agent import config


def get_pending_email_drafts() -> list[dict]:
    """Retourne tous les brouillons email prêts à envoyer via Gmail MCP."""
    drafts = []
    for f in config.EMAIL_DIRS["emails"].glob("*.json"):
        draft = config.load_json(f)
        if draft.get("status") == "draft":
            drafts.append({
                "file": str(f),
                "draft_id": draft.get("id"),
                "to": draft.get("to_email"),
                "subject": draft.get("subject") or draft.get("sujet", ""),
                "body_html": draft.get("html_content") or draft.get("body", ""),
                "type": draft.get("type"),
            })
    return drafts


def get_pending_instagram_drafts() -> list[dict]:
    """Retourne tous les posts Instagram prêts à illustrer via Canva MCP."""
    drafts = []
    for f in config.EMAIL_DIRS["instagram"].glob("*.json"):
        post = config.load_json(f)
        if post.get("status") == "draft":
            drafts.append({
                "file": str(f),
                "post_id": post.get("id"),
                "caption": post.get("caption"),
                "hashtags": post.get("hashtags", []),
                "visual_brief": post.get("visual_brief"),
                "tone": post.get("tone", "luxueux"),
                "product_id": post.get("product_id"),
                "canva_template_id": post.get("canva_template_id"),
            })
    return drafts


def get_pending_articles() -> list[dict]:
    """Retourne les articles validés prêts à sauvegarder sur Google Drive."""
    articles = []
    for f in config.EMAIL_DIRS["articles"].glob("*.json"):
        article = config.load_json(f)
        if article.get("status") == "approved":
            articles.append({
                "file": str(f),
                "article_id": article.get("id"),
                "titre": article.get("titre"),
                "resume": article.get("resume"),
                "contenu": article.get("contenu_complet"),
                "categorie": article.get("categorie"),
                "mots_cles_seo": article.get("mots_cles_seo", []),
                "word_count": article.get("word_count"),
            })
    return articles


def get_pending_newsletters() -> list[dict]:
    """Retourne les newsletters prêtes à envoyer via Gmail MCP."""
    newsletters = []
    for f in config.EMAIL_DIRS["newsletters"].glob("*.json"):
        nl = config.load_json(f)
        if nl.get("status") == "draft":
            newsletters.append({
                "file": str(f),
                "newsletter_id": nl.get("id"),
                "sujet": nl.get("sujet"),
                "html_content": nl.get("html_content"),
                "html_file": str(config.EMAIL_DIRS["newsletters"] / f"{nl.get('id')}.html"),
            })
    return newsletters


def mark_draft_sent(file_path: str, mcp_ref: str = "") -> bool:
    """Marque un brouillon comme envoyé après traitement MCP."""
    path = Path(file_path)
    if not path.exists():
        return False
    draft = config.load_json(path)
    draft["status"] = "sent"
    draft["sent_at"] = datetime.now().isoformat()
    if mcp_ref:
        draft["mcp_ref"] = mcp_ref
    config.save_json(path, draft)
    return True


def get_orchestration_status() -> dict:
    """Résumé de l'état de l'orchestration — tous les brouillons en attente."""
    emails = get_pending_email_drafts()
    instagram = get_pending_instagram_drafts()
    articles = get_pending_articles()
    newsletters = get_pending_newsletters()

    return {
        "emails_en_attente": len(emails),
        "posts_instagram_en_attente": len(instagram),
        "articles_approuves": len(articles),
        "newsletters_en_attente": len(newsletters),
        "total": len(emails) + len(instagram) + len(articles) + len(newsletters),
        "details": {
            "emails": emails,
            "instagram": instagram,
            "articles": articles,
            "newsletters": newsletters,
        },
    }


if __name__ == "__main__":
    status = get_orchestration_status()
    print(json.dumps(status, ensure_ascii=False, indent=2))
