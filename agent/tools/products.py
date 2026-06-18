import json
import uuid
from datetime import date
from pathlib import Path
from agent import config


def list_products(category: str | None = None) -> dict:
    products = config.get_products()
    if category:
        products = [p for p in products if p.get("categorie") == category]
    return {"products": products, "total": len(products)}


def get_product(product_id: str) -> dict:
    products = config.get_products()
    for p in products:
        if p.get("id") == product_id:
            return {"product": p}
    return {"error": f"Produit {product_id} introuvable"}


def create_product(
    nom: str,
    categorie: str,
    description_courte: str,
    description_longue: str = "",
    prix_eur: float = 0,
    poids_carats: float | None = None,
    origine: str = "",
    couleur: str = "",
    purete: str = "",
    traitement: str = "Non traité",
    certification: str = "",
    tags: list[str] | None = None,
) -> dict:
    product_id = f"{categorie[:3].upper()}-{str(uuid.uuid4())[:6].upper()}"
    product = {
        "id": product_id,
        "nom": nom,
        "categorie": categorie,
        "description_courte": description_courte,
        "description_longue": description_longue,
        "prix_eur": prix_eur,
        "poids_carats": poids_carats,
        "origine": origine,
        "couleur": couleur,
        "purete": purete,
        "traitement": traitement,
        "certification": certification,
        "tags": tags or [],
        "statut": "disponible",
        "date_ajout": date.today().isoformat(),
    }
    path = config.PRODUCTS_DIR / f"{product_id}.json"
    config.save_json(path, product)
    return {"success": True, "product_id": product_id, "product": product}


def update_product(product_id: str, updates: dict) -> dict:
    path = config.PRODUCTS_DIR / f"{product_id}.json"
    if path.exists():
        product = config.load_json(path)
    else:
        products = config.get_products()
        found = next((p for p in products if p.get("id") == product_id), None)
        if not found:
            return {"error": f"Produit {product_id} introuvable"}
        product = found

    product.update(updates)
    product["date_modification"] = date.today().isoformat()
    config.save_json(config.PRODUCTS_DIR / f"{product_id}.json", product)
    return {"success": True, "product": product}
