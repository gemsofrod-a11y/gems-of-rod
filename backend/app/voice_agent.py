"""Boucle agentique (tool-use Anthropic) qui répond aux commandes vocales de
Sébastien depuis l'app téléphone. Le texte renvoyé est destiné à être lu à
voix haute (TextToSpeech) : phrases courtes, naturelles, sans markdown.
"""
import anthropic

from app import config, db, gmail_client

TOOLS: list[dict] = [
    {
        "name": "search_emails",
        "description": "Recherche des emails dans la boîte Gmail (syntaxe de recherche Gmail : "
                       "from:, subject:, is:unread, newer_than:7d, etc.).",
        "input_schema": {
            "type": "object",
            "properties": {
                "query": {"type": "string", "description": "Requête de recherche Gmail."},
                "max_results": {"type": "integer", "default": 10},
            },
            "required": ["query"],
        },
    },
    {
        "name": "get_email",
        "description": "Récupère le contenu complet d'un email (corps du message inclus).",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}},
            "required": ["message_id"],
        },
    },
    {
        "name": "send_reply",
        "description": "Envoie immédiatement une réponse à un email. À utiliser seulement "
                       "quand Sébastien a donné une instruction claire et explicite par la voix.",
        "input_schema": {
            "type": "object",
            "properties": {
                "message_id": {"type": "string"},
                "body_text": {"type": "string"},
            },
            "required": ["message_id", "body_text"],
        },
    },
    {
        "name": "create_draft",
        "description": "Crée un brouillon de réponse dans Gmail sans l'envoyer, pour "
                       "relecture ultérieure.",
        "input_schema": {
            "type": "object",
            "properties": {
                "message_id": {"type": "string"},
                "body_text": {"type": "string"},
            },
            "required": ["message_id", "body_text"],
        },
    },
    {
        "name": "archive_email",
        "description": "Archive un email (le retire de la boîte de réception).",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}},
            "required": ["message_id"],
        },
    },
    {
        "name": "label_email",
        "description": "Applique une étiquette Gmail à un email (créée si elle n'existe pas).",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}, "label": {"type": "string"}},
            "required": ["message_id", "label"],
        },
    },
    {
        "name": "mark_spam",
        "description": "Marque un email comme spam.",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}},
            "required": ["message_id"],
        },
    },
    {
        "name": "trash_email",
        "description": "Met un email à la corbeille (récupérable 30 jours, comme dans "
                       "Gmail). À utiliser pour supprimer une newsletter, une publicité, "
                       "ou tout email que Sébastien demande explicitement de supprimer.",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}},
            "required": ["message_id"],
        },
    },
    {
        "name": "unsubscribe_email",
        "description": "Tente de se désabonner de l'expéditeur d'un email (newsletter) via "
                       "son en-tête List-Unsubscribe. Best-effort : certains expéditeurs "
                       "exigent une confirmation manuelle que ce n'est pas possible de "
                       "franchir automatiquement, le résultat le précise. À combiner avec "
                       "trash_email si Sébastien veut aussi supprimer l'email.",
        "input_schema": {
            "type": "object",
            "properties": {"message_id": {"type": "string"}},
            "required": ["message_id"],
        },
    },
    {
        "name": "list_pending_confirmations",
        "description": "Liste les emails complexes en attente d'une décision de Sébastien "
                       "(négociation, VIP, réclamation, ambigu), avec la réponse suggérée.",
        "input_schema": {"type": "object", "properties": {}, "required": []},
    },
    {
        "name": "resolve_pending",
        "description": "Résout un email en attente : approuve (envoie la réponse suggérée ou "
                       "une version modifiée) ou rejette (n'envoie rien, laisse l'email tel quel).",
        "input_schema": {
            "type": "object",
            "properties": {
                "pending_id": {"type": "string"},
                "decision": {"type": "string", "enum": ["approve", "reject"]},
                "edited_reply": {
                    "type": "string",
                    "description": "Texte de réponse modifié, si Sébastien veut changer la "
                                   "suggestion avant envoi.",
                },
            },
            "required": ["pending_id", "decision"],
        },
    },
    {
        "name": "get_today_digest",
        "description": "Résumé des actions automatiques effectuées aujourd'hui et du nombre "
                       "d'emails en attente de décision.",
        "input_schema": {"type": "object", "properties": {}, "required": []},
    },
]

_SYSTEM_PROMPT = """Tu es l'assistant vocal personnel de Sébastien, fondateur de Gems of Rod \
(maison française de pierres précieuses et bijoux d'exception). Il te parle à voix haute \
depuis son téléphone et tes réponses sont lues à voix haute : réponds en français, avec des \
phrases courtes et naturelles, sans markdown, sans listes à puces.

Règles d'autonomie :
- Tâches simples (chercher, lire, classer, étiqueter, archiver un email, supprimer/désabonner \
une newsletter ou une publicité, ou envoyer une réponse quand Sébastien te donne une \
instruction claire et explicite) : agis directement.
- Pour tout ce qui touche à un prix ou une demande de devis, une prise de rendez-vous, un \
client VIP, une réclamation ou un sujet sensible, ne décide jamais seul : utilise \
list_pending_confirmations pour les lui présenter et resolve_pending seulement après qu'il a \
donné sa décision à voix haute.
- Si une demande est ambiguë, pose une question courte avant d'agir plutôt que de deviner.
- Confirme toujours brièvement ce que tu viens de faire (« C'est fait, j'ai archivé le mail \
de... », « Désabonné et supprimé. »).
"""


def _dispatch(name: str, tool_input: dict) -> str:
    try:
        if name == "search_emails":
            results = gmail_client.search_messages(
                tool_input["query"], tool_input.get("max_results", 10)
            )
            return str(results)
        if name == "get_email":
            return str(gmail_client.get_message(tool_input["message_id"]))
        if name == "send_reply":
            sent_id = gmail_client.send_reply(tool_input["message_id"], tool_input["body_text"])
            db.log_action(tool_input["message_id"], "voice_send_reply", tool_input["body_text"])
            return f"Réponse envoyée (id {sent_id})."
        if name == "create_draft":
            draft_id = gmail_client.create_draft_reply(
                tool_input["message_id"], tool_input["body_text"]
            )
            db.log_action(tool_input["message_id"], "voice_create_draft", tool_input["body_text"])
            return f"Brouillon créé (id {draft_id})."
        if name == "archive_email":
            gmail_client.archive_message(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_archive", "")
            return "Email archivé."
        if name == "label_email":
            gmail_client.add_label(tool_input["message_id"], tool_input["label"])
            db.log_action(tool_input["message_id"], "voice_label", tool_input["label"])
            return f"Étiquette « {tool_input['label']} » appliquée."
        if name == "mark_spam":
            gmail_client.mark_spam(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_spam", "")
            return "Email marqué comme spam."
        if name == "trash_email":
            gmail_client.trash_message(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_trash", "")
            return "Email mis à la corbeille."
        if name == "unsubscribe_email":
            result = gmail_client.unsubscribe(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_unsubscribe", result.get("detail", ""))
            return str(result)
        if name == "list_pending_confirmations":
            return str(db.list_pending())
        if name == "resolve_pending":
            return resolve_pending_action(
                tool_input["pending_id"],
                tool_input["decision"],
                tool_input.get("edited_reply"),
            )
        if name == "get_today_digest":
            return str(db.count_today_actions())
        return f"Outil inconnu : {name}"
    except Exception as e:
        return f"Erreur lors de l'exécution de {name} : {e}"


def resolve_pending_action(pending_id: str, decision: str, edited_reply: str | None) -> str:
    pending = db.get_pending(pending_id)
    if not pending:
        return "Cet email en attente est introuvable."
    if decision == "reject":
        db.resolve_pending(pending_id, "rejected")
        db.log_action(pending["message_id"], "voice_reject_pending", "")
        return "Compris, je n'envoie rien."
    reply_text = edited_reply or pending["suggested_reply"]
    if not reply_text:
        return "Aucune réponse suggérée à envoyer pour cet email."
    gmail_client.send_reply(pending["message_id"], reply_text)
    db.resolve_pending(pending_id, "approved")
    db.log_action(pending["message_id"], "voice_approve_pending", reply_text)
    return "Réponse envoyée."


_sessions: dict[str, list[dict]] = {}


def handle_turn(text: str, session_id: str = "default") -> dict:
    history = _sessions.setdefault(session_id, [])
    history.append({"role": "user", "content": text})

    client = anthropic.Anthropic(api_key=config.ANTHROPIC_API_KEY)
    actions: list[str] = []

    for _ in range(6):
        resp = client.messages.create(
            model=config.ASSISTANT_MODEL,
            max_tokens=1024,
            system=_SYSTEM_PROMPT,
            tools=TOOLS,
            messages=history,
        )
        history.append({"role": "assistant", "content": resp.content})

        if resp.stop_reason != "tool_use":
            reply = "".join(b.text for b in resp.content if b.type == "text").strip()
            history[:] = history[-20:]
            return {"reply": reply, "actions": actions}

        tool_results = []
        for block in resp.content:
            if block.type != "tool_use":
                continue
            result = _dispatch(block.name, block.input)
            actions.append(f"{block.name}: {result}")
            tool_results.append({
                "type": "tool_result",
                "tool_use_id": block.id,
                "content": result,
            })
        history.append({"role": "user", "content": tool_results})

    history[:] = history[-20:]
    return {"reply": "Je n'ai pas réussi à terminer cette action, peux-tu reformuler ?",
            "actions": actions}
