import uuid
from datetime import datetime
from agent import config


def list_suppliers(type_filter: str = "tous") -> dict:
    suppliers = config.get_suppliers()
    if type_filter and type_filter != "tous":
        suppliers = [s for s in suppliers if s.get("type") == type_filter]
    return {"suppliers": suppliers, "total": len(suppliers)}


def get_supplier(supplier_id: str) -> dict:
    suppliers = config.get_suppliers()
    found = next((s for s in suppliers if s.get("id") == supplier_id), None)
    if not found:
        return {"error": f"Fournisseur {supplier_id} introuvable"}
    return {"supplier": found}


def compose_supplier_email(
    supplier_id: str,
    email_type: str,
    sujet: str,
    corps: str,
    produits_concernes: list[str] | None = None,
) -> dict:
    supplier = get_supplier(supplier_id)
    if "error" in supplier:
        return supplier
    sup = supplier["supplier"]

    draft_id = f"EMAIL-{uuid.uuid4().hex[:8].upper()}"
    draft = {
        "id": draft_id,
        "type": "supplier_email",
        "email_type": email_type,
        "to_name": sup["contact"],
        "to_email": sup["email"],
        "supplier_id": supplier_id,
        "supplier_name": sup["nom"],
        "subject": sujet,
        "body": corps,
        "produits_concernes": produits_concernes or [],
        "status": "draft",
        "created_at": datetime.now().isoformat(),
    }

    path = config.EMAIL_DIRS["emails"] / f"{draft_id}.json"
    config.save_json(path, draft)
    return {
        "success": True,
        "draft_id": draft_id,
        "draft": draft,
        "file": str(path),
        "next_step": "Utiliser mcp__Gmail__create_draft pour créer le brouillon Gmail",
    }


def request_jeweler_quote(
    joaillier_nom: str,
    joaillier_email: str,
    description_projet: str,
    specifications_techniques: str = "",
    budget_indicatif: str = "",
    delai_souhaite: str = "",
    pieces_jointes: list[str] | None = None,
) -> dict:
    company = config.get_company_info()
    draft_id = f"DEVIS-{uuid.uuid4().hex[:8].upper()}"

    subject = f"Demande de devis – {description_projet[:50]} | Gems of Rod"
    body = f"""Madame, Monsieur,

Je me permets de vous contacter au nom de {company.get('nom', 'Gems of Rod')}, maison française spécialisée dans les pierres précieuses et bijoux d'exception.

Nous souhaiterions obtenir un devis pour le projet suivant :

**Description du projet :**
{description_projet}
"""
    if specifications_techniques:
        body += f"\n**Spécifications techniques :**\n{specifications_techniques}\n"
    if budget_indicatif:
        body += f"\n**Budget indicatif :** {budget_indicatif}\n"
    if delai_souhaite:
        body += f"\n**Délai souhaité :** {delai_souhaite}\n"

    default_cloture = "Avec nos sincères salutations,\nL'équipe Gems of Rod"
    cloture = company.get("tone_of_voice", {}).get("formules_email", {}).get("cloture", default_cloture)
    body += f"\nNous restons à votre disposition pour tout complément d'information.\n\n{cloture}\n"

    draft = {
        "id": draft_id,
        "type": "jeweler_quote",
        "to_name": joaillier_nom,
        "to_email": joaillier_email,
        "subject": subject,
        "body": body,
        "description_projet": description_projet,
        "specifications_techniques": specifications_techniques,
        "budget_indicatif": budget_indicatif,
        "delai_souhaite": delai_souhaite,
        "pieces_jointes": pieces_jointes or [],
        "status": "draft",
        "created_at": datetime.now().isoformat(),
    }

    path = config.EMAIL_DIRS["emails"] / f"{draft_id}.json"
    config.save_json(path, draft)
    return {
        "success": True,
        "draft_id": draft_id,
        "draft": draft,
        "file": str(path),
        "next_step": "Utiliser mcp__Gmail__create_draft pour envoyer ce devis",
    }
