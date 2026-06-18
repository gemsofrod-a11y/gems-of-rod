import uuid
from datetime import datetime
from agent import config


def create_article_proposal(
    titre: str,
    resume: str,
    contenu_complet: str,
    categorie: str = "gemmologie",
    mots_cles_seo: list[str] | None = None,
    tags: list[str] | None = None,
) -> dict:
    article_id = f"ART-{uuid.uuid4().hex[:8].upper()}"
    article = {
        "id": article_id,
        "titre": titre,
        "resume": resume,
        "contenu_complet": contenu_complet,
        "categorie": categorie,
        "mots_cles_seo": mots_cles_seo or [],
        "tags": tags or [],
        "status": "pending",
        "created_at": datetime.now().isoformat(),
        "validated_at": None,
        "published_at": None,
        "word_count": len(contenu_complet.split()),
    }

    path = config.EMAIL_DIRS["articles"] / f"{article_id}.json"
    config.save_json(path, article)
    return {
        "success": True,
        "article_id": article_id,
        "article": article,
        "file": str(path),
        "next_step": "Article en attente de votre validation. Répondez 'valider' ou 'rejeter'.",
    }


def list_article_proposals(status: str = "all") -> dict:
    articles = []
    for f in config.EMAIL_DIRS["articles"].glob("*.json"):
        article = config.load_json(f)
        if status == "all" or article.get("status") == status:
            articles.append({
                "id": article.get("id"),
                "titre": article.get("titre"),
                "resume": article.get("resume"),
                "categorie": article.get("categorie"),
                "status": article.get("status"),
                "created_at": article.get("created_at"),
                "word_count": article.get("word_count"),
            })
    return {"articles": articles, "total": len(articles)}


def get_article(article_id: str) -> dict:
    path = config.EMAIL_DIRS["articles"] / f"{article_id}.json"
    if not path.exists():
        return {"error": f"Article {article_id} introuvable"}
    return {"article": config.load_json(path)}


def validate_article(article_id: str, decision: str, commentaire: str = "") -> dict:
    path = config.EMAIL_DIRS["articles"] / f"{article_id}.json"
    if not path.exists():
        return {"error": f"Article {article_id} introuvable"}

    article = config.load_json(path)
    article["status"] = "approved" if decision == "valider" else "rejected"
    article["validated_at"] = datetime.now().isoformat()
    article["commentaire_validation"] = commentaire
    config.save_json(path, article)
    return {"success": True, "article_id": article_id, "status": article["status"]}
