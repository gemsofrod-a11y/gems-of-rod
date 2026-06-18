import json
import sys
from datetime import datetime, date
from pathlib import Path

import anthropic
from rich.console import Console
from rich.panel import Panel
from rich.text import Text

from agent import config
from agent.tools import products, instagram, suppliers, articles, newsletter, email_tools

console = Console()


TOOLS: list[dict] = [
    # ── Connaissance société ──
    {
        "name": "get_company_info",
        "description": "Récupère les informations complètes sur la société Gems of Rod : mission, valeurs, tone of voice, catégories produits.",
        "input_schema": {"type": "object", "properties": {}, "required": []},
    },
    # ── Produits ──
    {
        "name": "list_products",
        "description": "Liste tous les produits du catalogue. Filtre optionnel par catégorie.",
        "input_schema": {
            "type": "object",
            "properties": {
                "category": {
                    "type": "string",
                    "description": "pierre_precieuse | pierre_fine | metal_precieux | bijou",
                    "enum": ["pierre_precieuse", "pierre_fine", "metal_precieux", "bijou"],
                }
            },
            "required": [],
        },
    },
    {
        "name": "get_product",
        "description": "Récupère la fiche complète d'un produit par son ID.",
        "input_schema": {
            "type": "object",
            "properties": {"product_id": {"type": "string"}},
            "required": ["product_id"],
        },
    },
    {
        "name": "create_product",
        "description": "Crée une nouvelle fiche produit dans le catalogue.",
        "input_schema": {
            "type": "object",
            "properties": {
                "nom": {"type": "string"},
                "categorie": {
                    "type": "string",
                    "enum": ["pierre_precieuse", "pierre_fine", "metal_precieux", "bijou"],
                },
                "description_courte": {"type": "string"},
                "description_longue": {"type": "string"},
                "prix_eur": {"type": "number"},
                "poids_carats": {"type": "number"},
                "origine": {"type": "string"},
                "couleur": {"type": "string"},
                "purete": {"type": "string"},
                "traitement": {"type": "string"},
                "certification": {"type": "string"},
                "tags": {"type": "array", "items": {"type": "string"}},
            },
            "required": ["nom", "categorie", "description_courte"],
        },
    },
    {
        "name": "update_product",
        "description": "Met à jour une fiche produit existante.",
        "input_schema": {
            "type": "object",
            "properties": {
                "product_id": {"type": "string"},
                "updates": {"type": "object", "description": "Champs à mettre à jour"},
            },
            "required": ["product_id", "updates"],
        },
    },
    # ── Instagram ──
    {
        "name": "create_instagram_post",
        "description": "Crée un post Instagram complet (légende, hashtags, brief visuel pour Canva) et le sauvegarde en brouillon.",
        "input_schema": {
            "type": "object",
            "properties": {
                "caption": {"type": "string", "description": "Texte principal du post"},
                "hashtags": {"type": "array", "items": {"type": "string"}},
                "visual_brief": {
                    "type": "string",
                    "description": "Description du visuel souhaité pour Canva",
                },
                "product_id": {"type": "string"},
                "tone": {
                    "type": "string",
                    "enum": ["luxueux", "educatif", "storytelling", "promotionnel"],
                },
                "canva_template_id": {"type": "string"},
            },
            "required": ["caption", "hashtags", "visual_brief"],
        },
    },
    {
        "name": "list_instagram_drafts",
        "description": "Liste les posts Instagram en attente.",
        "input_schema": {"type": "object", "properties": {}, "required": []},
    },
    {
        "name": "get_suggested_hashtags",
        "description": "Retourne les hashtags recommandés pour une catégorie de produit.",
        "input_schema": {
            "type": "object",
            "properties": {"category": {"type": "string"}},
            "required": [],
        },
    },
    # ── Fournisseurs ──
    {
        "name": "list_suppliers",
        "description": "Liste tous les fournisseurs : lapidaires, négociants, joailliers.",
        "input_schema": {
            "type": "object",
            "properties": {
                "type_filter": {
                    "type": "string",
                    "enum": ["lapidaire", "negociant", "joaillier", "mineur", "tous"],
                }
            },
            "required": [],
        },
    },
    {
        "name": "get_supplier",
        "description": "Récupère les détails d'un fournisseur par son ID.",
        "input_schema": {
            "type": "object",
            "properties": {"supplier_id": {"type": "string"}},
            "required": ["supplier_id"],
        },
    },
    {
        "name": "compose_supplier_email",
        "description": "Compose et sauvegarde un email professionnel à destination d'un fournisseur (demande de prix, disponibilité, commande, relance).",
        "input_schema": {
            "type": "object",
            "properties": {
                "supplier_id": {"type": "string"},
                "email_type": {
                    "type": "string",
                    "enum": [
                        "demande_prix",
                        "demande_disponibilite",
                        "commande",
                        "relance",
                        "general",
                    ],
                },
                "sujet": {"type": "string"},
                "corps": {"type": "string"},
                "produits_concernes": {"type": "array", "items": {"type": "string"}},
            },
            "required": ["supplier_id", "email_type", "sujet", "corps"],
        },
    },
    {
        "name": "request_jeweler_quote",
        "description": "Rédige et sauvegarde une demande de devis formelle à destination d'un joaillier ou bijoutier.",
        "input_schema": {
            "type": "object",
            "properties": {
                "joaillier_nom": {"type": "string"},
                "joaillier_email": {"type": "string"},
                "description_projet": {"type": "string"},
                "specifications_techniques": {"type": "string"},
                "budget_indicatif": {"type": "string"},
                "delai_souhaite": {"type": "string"},
                "pieces_jointes": {"type": "array", "items": {"type": "string"}},
            },
            "required": ["joaillier_nom", "joaillier_email", "description_projet"],
        },
    },
    # ── Articles web ──
    {
        "name": "create_article_proposal",
        "description": "Rédige et sauvegarde une proposition d'article pour le site. Statut 'pending' jusqu'à validation.",
        "input_schema": {
            "type": "object",
            "properties": {
                "titre": {"type": "string"},
                "resume": {"type": "string"},
                "contenu_complet": {"type": "string"},
                "categorie": {"type": "string"},
                "mots_cles_seo": {"type": "array", "items": {"type": "string"}},
                "tags": {"type": "array", "items": {"type": "string"}},
            },
            "required": ["titre", "resume", "contenu_complet"],
        },
    },
    {
        "name": "list_article_proposals",
        "description": "Liste les articles proposés, avec filtre par statut.",
        "input_schema": {
            "type": "object",
            "properties": {
                "status": {
                    "type": "string",
                    "enum": ["pending", "approved", "rejected", "all"],
                }
            },
            "required": [],
        },
    },
    {
        "name": "get_article",
        "description": "Récupère le contenu complet d'un article par son ID.",
        "input_schema": {
            "type": "object",
            "properties": {"article_id": {"type": "string"}},
            "required": ["article_id"],
        },
    },
    {
        "name": "validate_article",
        "description": "Valide ou rejette un article proposé.",
        "input_schema": {
            "type": "object",
            "properties": {
                "article_id": {"type": "string"},
                "decision": {"type": "string", "enum": ["valider", "rejeter"]},
                "commentaire": {"type": "string"},
            },
            "required": ["article_id", "decision"],
        },
    },
    # ── Newsletter ──
    {
        "name": "create_newsletter",
        "description": "Crée une newsletter complète avec contenu HTML prêt à envoyer.",
        "input_schema": {
            "type": "object",
            "properties": {
                "sujet": {"type": "string"},
                "titre_principal": {"type": "string"},
                "introduction": {"type": "string"},
                "sections": {
                    "type": "array",
                    "items": {
                        "type": "object",
                        "properties": {
                            "titre": {"type": "string"},
                            "contenu": {"type": "string"},
                        },
                    },
                },
                "produits_en_vedette": {"type": "array", "items": {"type": "string"}},
                "offre_speciale": {"type": "string"},
                "call_to_action": {"type": "string"},
            },
            "required": ["sujet", "titre_principal", "introduction"],
        },
    },
    {
        "name": "list_newsletters",
        "description": "Liste les newsletters créées.",
        "input_schema": {
            "type": "object",
            "properties": {
                "status": {"type": "string", "enum": ["draft", "sent", "all"]}
            },
            "required": [],
        },
    },
    # ── Clients ──
    {
        "name": "list_clients",
        "description": "Liste les clients Gems of Rod avec leurs préférences et historique.",
        "input_schema": {
            "type": "object",
            "properties": {
                "segment": {
                    "type": "string",
                    "enum": ["vip", "regulier", "prospect", "tous"],
                }
            },
            "required": [],
        },
    },
    # ── Communication & rapports ──
    {
        "name": "ask_user",
        "description": "Pose une question à l'utilisateur et attend sa réponse avant de poursuivre. Utiliser pour toute décision importante.",
        "input_schema": {
            "type": "object",
            "properties": {
                "question": {"type": "string"},
                "contexte": {"type": "string"},
                "options": {
                    "type": "array",
                    "items": {"type": "string"},
                    "description": "Options suggérées (optionnel)",
                },
            },
            "required": ["question"],
        },
    },
    {
        "name": "generate_daily_summary",
        "description": "Génère l'email récapitulatif HTML de la journée et le sauvegarde en brouillon.",
        "input_schema": {
            "type": "object",
            "properties": {
                "actions_effectuees": {
                    "type": "array",
                    "items": {"type": "string"},
                },
                "elements_en_attente": {
                    "type": "array",
                    "items": {"type": "string"},
                },
                "questions": {
                    "type": "array",
                    "items": {"type": "string"},
                },
                "prochaines_etapes": {
                    "type": "array",
                    "items": {"type": "string"},
                },
            },
            "required": ["actions_effectuees"],
        },
    },
    {
        "name": "list_email_drafts",
        "description": "Liste tous les brouillons d'emails générés (fournisseurs, devis, newsletters, récapitulatifs).",
        "input_schema": {"type": "object", "properties": {}, "required": []},
    },
    {
        "name": "get_email_draft",
        "description": "Récupère le contenu complet d'un brouillon email.",
        "input_schema": {
            "type": "object",
            "properties": {"draft_id": {"type": "string"}},
            "required": ["draft_id"],
        },
    },
    {
        "name": "log_action",
        "description": "Enregistre une action dans le journal de l'agent.",
        "input_schema": {
            "type": "object",
            "properties": {
                "type": {"type": "string"},
                "description": {"type": "string"},
                "statut": {"type": "string", "enum": ["success", "pending", "error"]},
                "details": {"type": "object"},
            },
            "required": ["type", "description", "statut"],
        },
    },
]


def _execute_tool(name: str, tool_input: dict) -> dict:
    try:
        match name:
            case "get_company_info":
                return config.get_company_info()
            case "list_products":
                return products.list_products(tool_input.get("category"))
            case "get_product":
                return products.get_product(tool_input["product_id"])
            case "create_product":
                return products.create_product(**tool_input)
            case "update_product":
                return products.update_product(tool_input["product_id"], tool_input["updates"])
            case "create_instagram_post":
                return instagram.create_instagram_post(**tool_input)
            case "list_instagram_drafts":
                return instagram.list_instagram_drafts()
            case "get_suggested_hashtags":
                return instagram.get_suggested_hashtags(tool_input.get("category"))
            case "list_suppliers":
                return suppliers.list_suppliers(tool_input.get("type_filter", "tous"))
            case "get_supplier":
                return suppliers.get_supplier(tool_input["supplier_id"])
            case "compose_supplier_email":
                return suppliers.compose_supplier_email(**tool_input)
            case "request_jeweler_quote":
                return suppliers.request_jeweler_quote(**tool_input)
            case "create_article_proposal":
                return articles.create_article_proposal(**tool_input)
            case "list_article_proposals":
                return articles.list_article_proposals(tool_input.get("status", "all"))
            case "get_article":
                return articles.get_article(tool_input["article_id"])
            case "validate_article":
                return articles.validate_article(**tool_input)
            case "create_newsletter":
                return newsletter.create_newsletter(**tool_input)
            case "list_newsletters":
                return newsletter.list_newsletters(tool_input.get("status", "all"))
            case "list_clients":
                clients = config.get_clients()
                seg = tool_input.get("segment", "tous")
                if seg != "tous":
                    clients = [c for c in clients if c.get("segment") == seg]
                return {"clients": clients, "total": len(clients)}
            case "ask_user":
                return _ask_user_interactive(tool_input)
            case "generate_daily_summary":
                return email_tools.generate_daily_summary(**tool_input)
            case "list_email_drafts":
                return email_tools.list_email_drafts()
            case "get_email_draft":
                return email_tools.get_email_draft(tool_input["draft_id"])
            case "log_action":
                return _log_action(tool_input)
            case _:
                return {"error": f"Outil inconnu : {name}"}
    except Exception as exc:
        return {"error": str(exc)}


def _ask_user_interactive(tool_input: dict) -> dict:
    question = tool_input["question"]
    contexte = tool_input.get("contexte", "")
    options = tool_input.get("options", [])

    console.print()
    console.print(Panel(
        Text.from_markup(
            f"[bold yellow]{'Contexte : ' + contexte + chr(10) if contexte else ''}"
            f"[/bold yellow][white]{question}[/white]"
            + (f"\n\n[dim]Options : {' | '.join(options)}[/dim]" if options else "")
        ),
        title="[bold gold1]❓ Question de l'agent[/bold gold1]",
        border_style="gold1",
    ))
    answer = console.input("[bold cyan]Votre réponse : [/bold cyan]").strip()
    return {"question": question, "reponse": answer}


def _log_action(tool_input: dict) -> dict:
    config.ensure_dirs()
    log_path = config.LOGS_DIR / f"agent_{date.today().isoformat()}.json"
    logs: list = json.loads(log_path.read_text()) if log_path.exists() else []
    entry = {
        "timestamp": datetime.now().isoformat(),
        **tool_input,
    }
    logs.append(entry)
    log_path.write_text(json.dumps(logs, ensure_ascii=False, indent=2))
    return {"success": True, "logged": entry}


def _get_system_prompt() -> str:
    company = config.get_company_info()
    prods = config.get_products()
    sups = config.get_suppliers()
    clients = config.get_clients()
    today = datetime.now().strftime("%A %d %B %Y, %H:%M")

    return f"""Tu es l'agent IA autonome de Gems of Rod, une maison française spécialisée dans les pierres précieuses et bijoux d'exception.

Date et heure actuelles : {today}

## Connaissance de la société
{json.dumps(company, ensure_ascii=False, indent=2)}

## Catalogue produits ({len(prods)} produits)
{json.dumps(prods, ensure_ascii=False, indent=2)}

## Fournisseurs ({len(sups)} partenaires)
{json.dumps(sups, ensure_ascii=False, indent=2)}

## Clients ({len(clients)} contacts)
{json.dumps(clients, ensure_ascii=False, indent=2)}

## Tes responsabilités
1. **Fiches produits** : Créer, enrichir et mettre à jour le catalogue de pierres et bijoux
2. **Instagram** : Créer des posts luxueux avec légende, hashtags et brief visuel pour Canva
3. **Fournisseurs** : Composer des emails professionnels (demandes de prix, disponibilités, relances)
4. **Joailliers** : Rédiger des demandes de devis précises et formelles
5. **Articles web** : Proposer des articles de blog gemmologiques (soumis à validation)
6. **Newsletter** : Créer et préparer des newsletters HTML pour les clients
7. **Questions** : Utiliser l'outil `ask_user` pour les décisions importantes
8. **Récapitulatif** : Terminer chaque session par `generate_daily_summary`

## Règles absolues
- Toujours `log_action` pour chaque action significative
- Toujours terminer par `generate_daily_summary`
- Ne jamais inventer de données sur les produits ou certificats
- Maintenir le ton luxueux et expert de la marque
- Poser des questions plutôt que prendre des décisions arbitraires importantes
- Les articles doivent être soumis à validation, pas publiés directement
"""


def run_agent(task: str, interactive: bool = True) -> None:
    config.ensure_dirs()
    client = anthropic.Anthropic()

    messages: list[dict] = [{"role": "user", "content": task}]

    console.print()
    console.print(Panel(
        f"[bold white]{task}[/bold white]",
        title="[bold magenta]✦ Gems of Rod AI Agent[/bold magenta]",
        border_style="magenta",
    ))

    iteration = 0
    while True:
        iteration += 1

        with console.status(f"[dim]Réflexion en cours (itération {iteration})…[/dim]"):
            response = client.messages.create(
                model=config.AGENT_MODEL,
                max_tokens=16000,
                thinking={"type": "enabled", "budget_tokens": config.THINKING_BUDGET},
                system=_get_system_prompt(),
                tools=TOOLS,
                messages=messages,
            )

        tool_use_blocks = [b for b in response.content if b.type == "tool_use"]
        text_blocks = [b for b in response.content if b.type == "text" and b.text.strip()]

        for block in text_blocks:
            console.print()
            console.print(Panel(
                block.text,
                title="[bold cyan]Agent[/bold cyan]",
                border_style="cyan",
            ))

        if not tool_use_blocks:
            break

        messages.append({"role": "assistant", "content": response.content})

        tool_results = []
        for tool_use in tool_use_blocks:
            console.print(f"\n  [bold yellow]→[/bold yellow] [yellow]{tool_use.name}[/yellow]", end="")
            result = _execute_tool(tool_use.name, tool_use.input)
            status = "✓" if "error" not in result else "✗"
            console.print(f" [{('green' if status == '✓' else 'red')}]{status}[/]")

            tool_results.append({
                "type": "tool_result",
                "tool_use_id": tool_use.id,
                "content": json.dumps(result, ensure_ascii=False, default=str),
            })

        messages.append({"role": "user", "content": tool_results})

    console.print()
    console.print("[bold green]✦ Session terminée.[/bold green]")
