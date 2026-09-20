"""Boucle agentique (tool-use Anthropic) qui répond aux commandes vocales de
Sébastien depuis l'app téléphone. Le texte renvoyé est destiné à être lu à
voix haute (TextToSpeech) : phrases courtes, naturelles, sans markdown.
"""
import anthropic

from app import config, db, gmail_client, images, weather

TOOLS: list[dict] = [
    # Outil serveur Anthropic : la recherche s'exécute côté Anthropic, les
    # résultats reviennent déjà intégrés dans la même réponse — aucun
    # dispatch de notre côté n'est nécessaire (voir handle_turn_stream).
    {"type": "web_search_20260209", "name": "web_search", "max_uses": 3},
    {
        "name": "get_weather",
        "description": "Donne la météo actuelle (température, conditions, humidité, vent) "
                       "pour un lieu donné. À utiliser pour toute question sur le temps qu'il "
                       "fait, où que ce soit dans le monde.",
        "input_schema": {
            "type": "object",
            "properties": {
                "location": {"type": "string",
                             "description": "Nom du lieu, ex: 'Paris', 'Lyon, France'."},
            },
            "required": ["location"],
        },
    },
    {
        "name": "search_images",
        "description": "Cherche des images libres de droits (licence Creative Commons) sur un "
                       "sujet donné et les affiche dans l'app. Utilise ceci quand Sébastien "
                       "demande une photo, une image ou un visuel de quelque chose (ex. \"montre-"
                       "moi une photo de saphir brut\"). Ne génère pas d'image : cherche des "
                       "photos déjà existantes.",
        "input_schema": {
            "type": "object",
            "properties": {
                "query": {"type": "string", "description": "Sujet de l'image recherchée."},
                "count": {"type": "integer", "default": 3,
                          "description": "Nombre d'images à montrer (1 à 6)."},
            },
            "required": ["query"],
        },
    },
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

_SYSTEM_PROMPT = """Tu es Saphir, l'assistant vocal personnel de Sébastien, fondateur de Gems \
of Rod (maison française de pierres précieuses et bijoux d'exception). C'est ton prénom : si \
Sébastien te demande comment tu t'appelles, réponds simplement « Saphir », sans plus \
d'explication à moins qu'il n'en redemande. Il te parle à voix haute depuis son téléphone et \
tes réponses sont lues à voix haute : réponds en français, avec des phrases courtes et \
naturelles, sans markdown, sans listes à puces.

Tu ne te limites pas à Gmail : tu es aussi un assistant généraliste. Pour la météo, utilise \
get_weather. Pour l'actualité, les infos récentes, ou toute question de culture générale dont \
tu n'es pas certain, utilise web_search plutôt que de répondre de mémoire — n'invente jamais \
un chiffre ou un fait qui pourrait avoir changé. Pour une photo ou un visuel, utilise \
search_images : elle cherche des images déjà existantes, elle ne génère rien — dis-le si \
Sébastien demande une image "générée" ou "inventée", ce n'est pas encore possible. Résume \
toujours la réponse en une ou deux phrases parlées, jamais une liste de résultats bruts.

Personnalité : inspire-toi de J.A.R.V.I.S., l'assistant de confiance calme, précis et \
discrètement spirituel. Pas de familiarité excessive ni d'exclamations : une élégance sobre, \
une pointe d'humour fin de temps en temps, jamais au détriment de la clarté. Vouvoie \
Sébastien. Sois proactif quand c'est pertinent (signaler quelque chose qu'il devrait savoir) \
sans jamais dépasser les règles d'autonomie ci-dessous.

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

Conditions de service taille lapidaire (à connaître pour répondre aux clients qui demandent \
un délai ou les conditions d'intervention — partage cette information factuelle directement, \
mais un chiffrage ou un engagement de prix reste soumis à la règle d'autonomie ci-dessus) :
- Délai standard : 2 à 4 semaines selon la charge d'atelier, confirmé à la commande.
- Les chutes et refus éventuels sont retournés avec la pierre taillée.
- Clause de risque de casse : la taille lapidaire implique un risque inhérent de bris ou \
d'éclat, notamment sur les pierres très incluses, fragiles ou présentant des clivages \
naturels. Gems of Rod met en œuvre tous les soins nécessaires lors de chaque intervention. Sa \
responsabilité ne saurait être engagée en cas de casse liée aux caractéristiques naturelles \
de la pierre. Le client est informé de ce risque et l'accepte formellement avant toute \
intervention, par signature d'une clause de risque. Si un client interroge sur ce risque, \
transmets cette clause fidèlement plutôt que de la résumer de mémoire.
"""


def _dispatch(
    name: str,
    tool_input: dict,
    referenced_emails: list[dict],
    draft_replies: list[dict],
    found_images: list[dict],
) -> str:
    """referenced_emails est enrichi en effet de bord quand un outil de
    lecture consulte des emails, pour que le client web puisse les présenter
    dans son carrousel visuel au moment précis où l'assistant les annonce.
    found_images est enrichi de la même façon par search_images, pour que le
    client web affiche les images trouvées.
    draft_replies est enrichi de la même façon quand une réponse est rédigée
    (brouillon ou envoi), pour que le client web puisse l'afficher en train
    de "s'écrire" à l'écran sans que Sébastien ait besoin d'ouvrir Gmail.
    """
    try:
        if name == "search_emails":
            results = gmail_client.search_messages(
                tool_input["query"], tool_input.get("max_results", 10)
            )
            referenced_emails.extend(
                {"message_id": r["id"], "subject": r.get("subject") or "(sans objet)"}
                for r in results
            )
            return str(results)
        if name == "get_email":
            email = gmail_client.get_message(tool_input["message_id"])
            referenced_emails.append(
                {"message_id": email["id"], "subject": email.get("subject") or "(sans objet)"}
            )
            return str(email)
        if name == "send_reply":
            email = gmail_client.get_message(tool_input["message_id"])
            subject = email.get("subject") or "(sans objet)"
            sent_id = gmail_client.send_reply(tool_input["message_id"], tool_input["body_text"])
            db.log_action(tool_input["message_id"], "voice_send_reply", tool_input["body_text"])
            draft_replies.append({
                "message_id": tool_input["message_id"],
                "subject": subject,
                "body_text": tool_input["body_text"],
                "draft_id": None,
                "sent": True,
            })
            return f"Réponse envoyée (id {sent_id})."
        if name == "create_draft":
            email = gmail_client.get_message(tool_input["message_id"])
            subject = email.get("subject") or "(sans objet)"
            draft_id = gmail_client.create_draft_reply(
                tool_input["message_id"], tool_input["body_text"]
            )
            db.log_action(tool_input["message_id"], "voice_create_draft", tool_input["body_text"])
            draft_replies.append({
                "message_id": tool_input["message_id"],
                "subject": subject,
                "body_text": tool_input["body_text"],
                "draft_id": draft_id,
                "sent": False,
            })
            return f"Brouillon créé (id {draft_id})."
        if name == "archive_email":
            email = gmail_client.get_message(tool_input["message_id"])
            subject = email.get("subject") or "(sans objet)"
            gmail_client.archive_message(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_archive", subject)
            return f"Email « {subject} » archivé."
        if name == "label_email":
            gmail_client.add_label(tool_input["message_id"], tool_input["label"])
            db.log_action(tool_input["message_id"], "voice_label", tool_input["label"])
            return f"Étiquette « {tool_input['label']} » appliquée."
        if name == "mark_spam":
            gmail_client.mark_spam(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_spam", "")
            return "Email marqué comme spam."
        if name == "trash_email":
            email = gmail_client.get_message(tool_input["message_id"])
            subject = email.get("subject") or "(sans objet)"
            gmail_client.trash_message(tool_input["message_id"])
            db.log_action(tool_input["message_id"], "voice_trash", subject)
            return f"Email « {subject} » mis à la corbeille."
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
        if name == "get_weather":
            return str(weather.get_weather(tool_input["location"]))
        if name == "search_images":
            results = images.search_images(
                tool_input["query"], tool_input.get("count", 3)
            )
            found_images.extend(r for r in results if "url" in r)
            return str(results)
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


_CONTEXT_WINDOW = 20  # échanges texte envoyés à Claude à chaque tour (coût/contexte)


def _context_from_history(full_history: list[dict], window: int) -> list[dict]:
    """Reconstruit un contexte propre pour l'API à partir de l'historique
    persistant : uniquement les échanges texte (jamais les blocs
    tool_use/tool_result des tours précédents — un tool_use ne peut être
    "rejoué" en toute sécurité que dans le tour où il a été créé ; les
    relire depuis la base après coup a causé des 400 Anthropic à répétition,
    l'API exigeant un appariement strict tool_use/tool_result). Ceux du tour
    EN COURS sont ajoutés à part, par handle_turn lui-même.
    """
    clean = []
    for msg in full_history:
        role, content = msg.get("role"), msg.get("content")
        if role == "user" and isinstance(content, str):
            clean.append({"role": "user", "content": content})
        elif role == "assistant" and isinstance(content, list):
            text = "".join(
                b.get("text", "") for b in content if isinstance(b, dict) and b.get("type") == "text"
            ).strip()
            if text:
                clean.append({"role": "assistant", "content": text})
    return clean[-window:]


# Outils de simple lecture : leur résultat peut être un dump brut volumineux
# (ex. search_emails renvoie la liste complète des métadonnées) et n'a rien
# d'une "action effectuée" à confirmer — Claude en tient déjà compte dans sa
# réponse en langage naturel, inutile de le réafficher tel quel au client web.
_READONLY_TOOLS = {"search_emails", "get_email", "list_pending_confirmations", "get_today_digest",
                    "get_weather", "search_images"}


def handle_turn_stream(text: str, session_id: str = "default"):
    """Générateur équivalent à l'ancien handle_turn (même boucle d'outils,
    même persistance), mais qui utilise l'API de streaming d'Anthropic et
    cède le texte de la réponse au fil de sa génération (évènements
    {"type": "text_delta", "text": ...}), plutôt que d'attendre la réponse
    complète. Le client web peut ainsi commencer à parler dès la première
    phrase générée au lieu d'attendre la fin du tour — et surtout la fin de
    tous les appels d'outils (recherche/lecture Gmail) qui le précèdent —
    pour une lecture beaucoup plus fluide et immédiate.
    Le dernier évènement cédé est toujours {"type": "done", ...} avec la
    même forme que l'ancien dict de retour (reply/actions/emails/drafts/media),
    pour que le reste de l'interface (carrousel, fiches de réponse, etc.)
    continue de fonctionner à l'identique.
    """
    full_history = db.load_conversation(session_id)
    full_history.append({"role": "user", "content": text})
    context = _context_from_history(full_history, _CONTEXT_WINDOW)

    client = anthropic.Anthropic(api_key=config.ANTHROPIC_API_KEY)
    actions: list[str] = []
    referenced_emails: list[dict] = []
    draft_replies: list[dict] = []
    found_images: list[dict] = []

    for _ in range(6):
        with client.messages.stream(
            model=config.ASSISTANT_MODEL,
            max_tokens=1024,
            system=_SYSTEM_PROMPT,
            tools=TOOLS,
            messages=context,
        ) as stream:
            for delta in stream.text_stream:
                yield {"type": "text_delta", "text": delta}
            resp = stream.get_final_message()

        assistant_entry = {"role": "assistant", "content": [b.model_dump() for b in resp.content]}
        full_history.append(assistant_entry)
        context.append(assistant_entry)

        if resp.stop_reason != "tool_use":
            reply = "".join(b.text for b in resp.content if b.type == "text").strip()
            db.save_conversation(session_id, full_history)
            yield {"type": "done", "reply": reply, "actions": actions,
                   "emails": referenced_emails, "drafts": draft_replies, "media": found_images}
            return

        tool_results = []
        for block in resp.content:
            if block.type != "tool_use":
                continue
            result = _dispatch(block.name, block.input, referenced_emails, draft_replies, found_images)
            if block.name not in _READONLY_TOOLS:
                actions.append(f"{block.name}: {result}")
            tool_results.append({
                "type": "tool_result",
                "tool_use_id": block.id,
                "content": result,
            })
        tool_entry = {"role": "user", "content": tool_results}
        full_history.append(tool_entry)
        context.append(tool_entry)

    db.save_conversation(session_id, full_history)
    yield {"type": "done",
           "reply": "Je n'ai pas réussi à terminer cette action, peux-tu reformuler ?",
           "actions": actions, "emails": referenced_emails, "drafts": draft_replies,
           "media": found_images}


def get_display_history(session_id: str = "default") -> list[dict]:
    """Historique simplifié (texte uniquement) pour l'affichage dans le client
    web : ne montre que les messages de Sébastien et les réponses finales de
    l'assistant, pas les appels d'outils intermédiaires.
    """
    display = []
    for msg in db.load_conversation(session_id):
        role, content = msg.get("role"), msg.get("content")
        if role == "user" and isinstance(content, str):
            display.append({"role": "user", "text": content})
        elif role == "assistant" and isinstance(content, list):
            text = "".join(
                b.get("text", "") for b in content if isinstance(b, dict) and b.get("type") == "text"
            ).strip()
            if text:
                display.append({"role": "assistant", "text": text})
    return display


def reset_conversation(session_id: str = "default") -> None:
    db.clear_conversation(session_id)
