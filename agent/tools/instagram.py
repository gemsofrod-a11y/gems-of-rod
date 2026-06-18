import json
import uuid
from datetime import datetime
from agent import config


HASHTAGS_BASE = [
    "#gemsofrod",
    "#pierresprécieuses",
    "#gemmes",
    "#joaillerie",
    "#luxe",
    "#pierresfines",
    "#bijoux",
    "#gemmologie",
]

HASHTAGS_BY_CATEGORY = {
    "pierre_precieuse": ["#saphir", "#rubis", "#émeraude", "#diamant", "#preciousstones"],
    "pierre_fine": ["#alexandrite", "#tanzanite", "#spinelle", "#tourmaline", "#coloredjewels"],
    "metal_precieux": ["#or18carats", "#platine", "#investissement", "#gold"],
    "bijou": ["#bijousurcommande", "#handcraftedjewelry", "#artisanat"],
}


def create_instagram_post(
    caption: str,
    hashtags: list[str],
    visual_brief: str,
    product_id: str | None = None,
    tone: str = "luxueux",
    canva_template_id: str | None = None,
) -> dict:
    post_id = f"IG-{uuid.uuid4().hex[:8].upper()}"
    post = {
        "id": post_id,
        "caption": caption,
        "hashtags": hashtags,
        "hashtags_string": " ".join(hashtags),
        "visual_brief": visual_brief,
        "product_id": product_id,
        "tone": tone,
        "canva_template_id": canva_template_id,
        "status": "draft",
        "created_at": datetime.now().isoformat(),
        "full_post": f"{caption}\n\n{' '.join(hashtags)}",
    }

    path = config.EMAIL_DIRS["instagram"] / f"{post_id}.json"
    config.save_json(path, post)
    return {
        "success": True,
        "post_id": post_id,
        "post": post,
        "file": str(path),
        "next_step": "Utiliser Canva MCP pour créer le visuel avec ce brief",
    }


def list_instagram_drafts() -> dict:
    drafts = []
    for f in config.EMAIL_DIRS["instagram"].glob("*.json"):
        drafts.append(config.load_json(f))
    return {"drafts": drafts, "total": len(drafts)}


def get_suggested_hashtags(category: str | None = None) -> dict:
    hashtags = list(HASHTAGS_BASE)
    if category and category in HASHTAGS_BY_CATEGORY:
        hashtags.extend(HASHTAGS_BY_CATEGORY[category])
    return {"hashtags": hashtags}
