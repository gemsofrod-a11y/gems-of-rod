package fr.gemsofrod.assistant.ai

import org.json.JSONArray
import org.json.JSONObject

/** Définitions des outils de Saphir, au format JSON brut de l'API Messages
 * (voir ai/AnthropicClient.kt) — port fidèle de la liste TOOLS de
 * backend/app/voice_agent.py. */
object SaphirTools {

    private fun schema(properties: JSONObject, required: List<String>): JSONObject = JSONObject()
        .put("type", "object")
        .put("properties", properties)
        .put("required", JSONArray(required))

    private fun stringProp(description: String? = null): JSONObject =
        JSONObject().put("type", "string").also { if (description != null) it.put("description", description) }

    private fun integerProp(description: String, default: Int? = null): JSONObject =
        JSONObject().put("type", "integer").put("description", description)
            .also { if (default != null) it.put("default", default) }

    private fun enumProp(values: List<String>): JSONObject =
        JSONObject().put("type", "string").put("enum", JSONArray(values))

    private fun tool(name: String, description: String, properties: JSONObject, required: List<String>): JSONObject =
        JSONObject()
            .put("name", name)
            .put("description", description)
            .put("input_schema", schema(properties, required))

    /** Outil serveur Anthropic (web_search) : la recherche s'exécute côté
     * Anthropic, les résultats reviennent déjà intégrés dans la même
     * réponse — aucun dispatch de notre côté n'est nécessaire. */
    private fun webSearchTool(): JSONObject = JSONObject()
        .put("type", "web_search_20260209")
        .put("name", "web_search")
        .put("max_uses", 3)

    fun buildAll(): JSONArray {
        val tools = JSONArray()
        tools.put(webSearchTool())
        tools.put(
            tool(
                "get_weather",
                "Donne la météo actuelle (température, conditions, humidité, vent) pour un lieu " +
                    "donné. À utiliser pour toute question sur le temps qu'il fait, où que ce soit " +
                    "dans le monde.",
                JSONObject().put("location", stringProp("Nom du lieu, ex: 'Paris', 'Lyon, France'.")),
                listOf("location"),
            )
        )
        tools.put(
            tool(
                "search_images",
                "Cherche des images libres de droits (licence Creative Commons) sur un sujet " +
                    "donné et les affiche dans l'app. Utilise ceci quand Sébastien demande une " +
                    "photo, une image ou un visuel de quelque chose. Ne génère pas d'image : " +
                    "cherche des photos déjà existantes.",
                JSONObject()
                    .put("query", stringProp("Sujet de l'image recherchée."))
                    .put("count", integerProp("Nombre d'images à montrer (1 à 6).", 3)),
                listOf("query"),
            )
        )
        tools.put(
            tool(
                "search_emails",
                "Recherche des emails dans la boîte Gmail (syntaxe de recherche Gmail : from:, " +
                    "subject:, is:unread, newer_than:7d, etc.).",
                JSONObject()
                    .put("query", stringProp("Requête de recherche Gmail."))
                    .put("max_results", integerProp("Nombre maximum de résultats.", 10)),
                listOf("query"),
            )
        )
        tools.put(
            tool(
                "get_email",
                "Récupère le contenu complet d'un email (corps du message inclus), avec la " +
                    "liste de ses pièces jointes éventuelles (nom, type, attachment_id) — " +
                    "utilise read_pdf_attachment pour en ouvrir une.",
                JSONObject().put("message_id", stringProp()),
                listOf("message_id"),
            )
        )
        tools.put(
            tool(
                "read_pdf_attachment",
                "Ouvre une pièce jointe PDF d'un email (ex. un devis fournisseur, une fiche " +
                    "technique, un certificat) : en extrait le texte pour que tu puisses le lire " +
                    "et en discuter, et l'affiche à l'écran du téléphone pour que Sébastien " +
                    "puisse le lire lui aussi en parallèle. Utilise get_email d'abord pour " +
                    "connaître l'attachment_id d'une pièce jointe.",
                JSONObject()
                    .put("message_id", stringProp())
                    .put("attachment_id", stringProp())
                    .put("filename", stringProp("Nom du fichier (pour l'affichage à l'écran).")),
                listOf("message_id", "attachment_id"),
            )
        )
        tools.put(
            tool(
                "send_reply",
                "Envoie immédiatement une réponse à un email. À utiliser seulement quand " +
                    "Sébastien a donné une instruction claire et explicite par la voix.",
                JSONObject().put("message_id", stringProp()).put("body_text", stringProp()),
                listOf("message_id", "body_text"),
            )
        )
        tools.put(
            tool(
                "create_draft",
                "Crée un brouillon de réponse dans Gmail sans l'envoyer, pour relecture ultérieure.",
                JSONObject().put("message_id", stringProp()).put("body_text", stringProp()),
                listOf("message_id", "body_text"),
            )
        )
        tools.put(
            tool(
                "archive_email",
                "Archive un email (le retire de la boîte de réception).",
                JSONObject().put("message_id", stringProp()),
                listOf("message_id"),
            )
        )
        tools.put(
            tool(
                "label_email",
                "Applique une étiquette Gmail à un email (créée si elle n'existe pas).",
                JSONObject().put("message_id", stringProp()).put("label", stringProp()),
                listOf("message_id", "label"),
            )
        )
        tools.put(
            tool(
                "mark_spam",
                "Marque un email comme spam.",
                JSONObject().put("message_id", stringProp()),
                listOf("message_id"),
            )
        )
        tools.put(
            tool(
                "trash_email",
                "Met un email à la corbeille (récupérable 30 jours, comme dans Gmail). À " +
                    "utiliser pour supprimer une newsletter, une publicité, ou tout email que " +
                    "Sébastien demande explicitement de supprimer.",
                JSONObject().put("message_id", stringProp()),
                listOf("message_id"),
            )
        )
        tools.put(
            tool(
                "unsubscribe_email",
                "Tente de se désabonner de l'expéditeur d'un email (newsletter) via son " +
                    "en-tête List-Unsubscribe. Best-effort : certains expéditeurs exigent une " +
                    "confirmation manuelle que ce n'est pas possible de franchir " +
                    "automatiquement, le résultat le précise. À combiner avec trash_email si " +
                    "Sébastien veut aussi supprimer l'email.",
                JSONObject().put("message_id", stringProp()),
                listOf("message_id"),
            )
        )
        tools.put(
            tool(
                "list_pending_confirmations",
                "Liste les emails complexes en attente d'une décision de Sébastien " +
                    "(négociation, VIP, réclamation, ambigu), avec la réponse suggérée.",
                JSONObject(),
                emptyList(),
            )
        )
        tools.put(
            tool(
                "resolve_pending",
                "Résout un email en attente : approuve (envoie la réponse suggérée ou une " +
                    "version modifiée) ou rejette (n'envoie rien, laisse l'email tel quel).",
                JSONObject()
                    .put("pending_id", stringProp())
                    .put("decision", enumProp(listOf("approve", "reject")))
                    .put(
                        "edited_reply",
                        stringProp("Texte de réponse modifié, si Sébastien veut changer la suggestion avant envoi."),
                    ),
                listOf("pending_id", "decision"),
            )
        )
        tools.put(
            tool(
                "get_today_digest",
                "Résumé des actions automatiques effectuées aujourd'hui et du nombre d'emails " +
                    "en attente de décision.",
                JSONObject(),
                emptyList(),
            )
        )
        return tools
    }

    /** Outils qui ne modifient rien (résultat pas digne d'une "action
     * effectuée" à confirmer dans le journal). */
    val READONLY_TOOLS = setOf(
        "search_emails", "get_email", "list_pending_confirmations", "get_today_digest",
        "get_weather", "search_images", "read_pdf_attachment",
    )
}
