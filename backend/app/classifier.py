"""Classification des emails entrants et rédaction de réponses dans le ton
Gems of Rod (voir CLAUDE.md — Voix de marque & Ton).

Catégories possibles, du plus autonome au moins autonome :
- "ignore"             : spam évident, notification sans action possible.
- "auto_delete"        : newsletters et publicités → désabonnement tenté
                          (voir gmail_client.unsubscribe) puis suppression
                          (corbeille). Demandé explicitement par Sébastien :
                          pas d'archivage pour ces emails-là, une vraie
                          suppression.
- "auto_triage"        : informatif et légitime mais pas une newsletter/pub
                          (confirmation de commande, notification d'un service
                          réellement utilisé, accusé automatique) → étiqueté
                          et archivé, jamais supprimé, aucune réponse requise.
- "auto_reply"         : question simple et récurrente (accusé de réception,
                          demande de disponibilité/horaires, question FAQ générique
                          d'un expéditeur non-VIP) → réponse courte générée et
                          envoyée automatiquement dans le ton de la maison.
- "needs_confirmation" : tout ce qui touche à la négociation, au prix (y compris
                          une demande de devis), à une prise de rendez-vous, à un
                          client VIP, à une réclamation, ou reste ambigu →
                          une réponse est proposée mais jamais envoyée seule.
"""
import json

import anthropic

from app import config

_CATEGORIES = ["ignore", "auto_delete", "auto_triage", "auto_reply", "needs_confirmation"]


def _client() -> anthropic.Anthropic:
    return anthropic.Anthropic(api_key=config.ANTHROPIC_API_KEY)


_DEFAULT_CLOTURE = "Avec mes sincères salutations,\nL'équipe Gems of Rod"


def _brand_context() -> str:
    company = config.get_company_info()
    tone = company.get("tone_of_voice", {})
    formules = tone.get("formules_email", {})
    cloture = formules.get("cloture", _DEFAULT_CLOTURE)
    return (
        f"Société : {company.get('nom', 'Gems of Rod')} — {company.get('description', '')}\n"
        f"Registre : {tone.get('registre', 'luxueux, authentique, expert')}\n"
        f"Formule d'ouverture pro : {formules.get('ouverture_pro', 'Madame, Monsieur,')}\n"
        f"Formule de clôture : {cloture}\n"
        f"À éviter : {', '.join(tone.get('a_eviter', []))}"
    )


_SYSTEM_PROMPT = """Tu es l'assistant de tri email de Gems of Rod, maison française de \
pierres précieuses et bijoux d'exception. Tu analyses un email reçu et tu décides comment \
il doit être traité, en respectant strictement l'autonomie suivante :

- "ignore" : spam évident ou notification sans aucune action possible.
- "auto_delete" : newsletter (à laquelle Sébastien est abonné, avec ou sans lien de \
désabonnement visible) ou publicité/promotion d'un expéditeur commercial. Ces emails seront \
désabonnés automatiquement (si possible) puis définitivement supprimés (corbeille) — \
Sébastien l'a explicitement demandé, ne pas se contenter d'archiver.
- "auto_triage" : email informatif et légitime mais qui n'est PAS une newsletter/publicité \
(confirmation de commande, notification d'un service que Sébastien utilise réellement, \
accusé automatique d'une administration). Étiqueté et archivé, jamais supprimé, aucune \
réponse à rédiger.
- "auto_reply" : demande simple et récurrente venant d'un contact non-VIP, sans négociation \
ni sujet sensible (accusé de réception, question de disponibilité/horaires, question \
générale déjà couverte par les informations connues de la maison). Rédige une réponse \
courte, dans le ton de la maison, prête à être envoyée telle quelle.
- "needs_confirmation" : tout ce qui touche à un prix (y compris une simple demande de \
devis), une négociation, une prise de rendez-vous, un client VIP, une réclamation, un sujet \
juridique/financier, ou tout email ambigu ou dont tu n'es pas sûr. Rédige une proposition de \
réponse mais elle ne sera jamais envoyée sans validation humaine — Sébastien veut être \
consulté avant toute décision sur ces sujets-là.

En cas de doute entre "auto_delete" et "auto_triage" pour un email automatisé, choisis \
"auto_triage" (on ne supprime que ce qui ressemble clairement à une newsletter ou une pub).

Contexte de marque :
{brand}

Réponds UNIQUEMENT avec un objet JSON valide, sans texte autour, au format exact :
{{"category": "ignore|auto_delete|auto_triage|auto_reply|needs_confirmation", \
"reasoning": "raison courte en français", \
"suggested_label": "étiquette Gmail courte optionnelle ou null", \
"suggested_reply": "réponse complète prête à envoyer si auto_reply ou needs_confirmation, sinon null"}}
"""


def classify_email(email: dict) -> dict:
    prompt = (
        f"De : {email.get('from', '')}\n"
        f"Sujet : {email.get('subject', '')}\n"
        f"Extrait : {email.get('snippet', '')}\n\n"
        f"Corps du message :\n{email.get('body_text', '')[:4000]}"
    )
    resp = _client().messages.create(
        model=config.ASSISTANT_MODEL,
        max_tokens=1024,
        system=_SYSTEM_PROMPT.format(brand=_brand_context()),
        messages=[{"role": "user", "content": prompt}],
    )
    text = "".join(block.text for block in resp.content if block.type == "text").strip()
    text = text.removeprefix("```json").removeprefix("```").removesuffix("```").strip()
    try:
        data = json.loads(text)
    except json.JSONDecodeError:
        return {
            "category": "needs_confirmation",
            "reasoning": "Réponse du classificateur illisible, soumis par précaution.",
            "suggested_label": None,
            "suggested_reply": None,
        }
    if data.get("category") not in _CATEGORIES:
        data["category"] = "needs_confirmation"
    return data
