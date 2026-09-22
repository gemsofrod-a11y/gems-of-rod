package fr.gemsofrod.assistant.ai

import fr.gemsofrod.assistant.gmail.ParsedEmail
import org.json.JSONArray
import org.json.JSONObject

data class ClassificationDecision(
    val category: String,
    val reasoning: String,
    val suggestedLabel: String?,
    val suggestedReply: String?,
)

/** Classification des emails entrants et rédaction de réponses dans le ton
 * Gems of Rod — port Kotlin de backend/app/classifier.py.
 *
 * Catégories possibles, du plus autonome au moins autonome :
 * - "ignore"             : spam évident, notification sans action possible.
 * - "auto_delete"        : newsletters et publicités -> désabonnement tenté
 *                          puis suppression (corbeille).
 * - "auto_triage"        : informatif et légitime, pas une newsletter/pub ->
 *                          étiqueté et archivé, jamais supprimé.
 * - "auto_reply"         : question simple et récurrente d'un contact non-VIP
 *                          -> réponse courte envoyée automatiquement.
 * - "needs_confirmation" : prix/devis, négociation, rendez-vous, VIP,
 *                          réclamation ou ambigu -> réponse proposée, jamais
 *                          envoyée sans validation.
 */
object Classifier {
    private val CATEGORIES = setOf("ignore", "auto_delete", "auto_triage", "auto_reply", "needs_confirmation")

    // Contexte de marque de Gems of Rod (voir CLAUDE.md — Voix de marque & Ton).
    // L'app ne dispose pas du fichier agent/knowledge/company.json du backend
    // Python ; ce texte en est la copie figée.
    private const val BRAND_CONTEXT = """Société : Gems of Rod — maison française indépendante fondée en 2020, spécialisée dans la sélection, la vente et la valorisation de pierres précieuses, pierres fines, métaux précieux et bijoux d'exception.
Registre : luxueux, authentique, expert, chaleureux mais formel
Formule d'ouverture pro : Madame, Monsieur,
Formule de clôture : Avec mes sincères salutations,
L'équipe Gems of Rod
À éviter : Langage commercial agressif, fausses promesses, hyperboles vides"""

    private fun systemPrompt(): String = """
Tu es l'assistant de tri email de Gems of Rod, maison française de pierres précieuses et bijoux d'exception. Tu analyses un email reçu et tu décides comment il doit être traité, en respectant strictement l'autonomie suivante :

- "ignore" : spam évident ou notification sans aucune action possible.
- "auto_delete" : newsletter (à laquelle Sébastien est abonné, avec ou sans lien de désabonnement visible) ou publicité/promotion d'un expéditeur commercial. Ces emails seront désabonnés automatiquement (si possible) puis définitivement supprimés (corbeille) — Sébastien l'a explicitement demandé, ne pas se contenter d'archiver.
- "auto_triage" : email informatif et légitime mais qui n'est PAS une newsletter/publicité (confirmation de commande, notification d'un service que Sébastien utilise réellement, accusé automatique d'une administration). Étiqueté et archivé, jamais supprimé, aucune réponse à rédiger.
- "auto_reply" : demande simple et récurrente venant d'un contact non-VIP, sans négociation ni sujet sensible (accusé de réception, question de disponibilité/horaires, question générale déjà couverte par les informations connues de la maison). Rédige une réponse courte, dans le ton de la maison, prête à être envoyée telle quelle.
- "needs_confirmation" : tout ce qui touche à un prix (y compris une simple demande de devis), une négociation, une prise de rendez-vous, un client VIP, une réclamation, un sujet juridique/financier, ou tout email ambigu ou dont tu n'es pas sûr. Rédige une proposition de réponse mais elle ne sera jamais envoyée sans validation humaine — Sébastien veut être consulté avant toute décision sur ces sujets-là. Pour une demande de devis, utilise la grille tarifaire ci-dessous pour proposer un chiffrage indicatif précis dans suggested_reply (au lieu de rester vague ou de renvoyer le client à un futur devis) — présente-le clairement comme une proposition à valider par Sébastien, jamais comme un prix ferme déjà engagé.

En cas de doute entre "auto_delete" et "auto_triage" pour un email automatisé, choisis "auto_triage" (on ne supprime que ce qui ressemble clairement à une newsletter ou une pub).

Contexte de marque :
$BRAND_CONTEXT

Grille tarifaire de référence (pour chiffrer une demande de devis) :
${PricingReference.TARIFF_REFERENCE}

Réponds UNIQUEMENT avec un objet JSON valide, sans texte autour, au format exact :
{"category": "ignore|auto_delete|auto_triage|auto_reply|needs_confirmation", "reasoning": "raison courte en français", "suggested_label": "étiquette Gmail courte optionnelle ou null", "suggested_reply": "réponse complète prête à envoyer si auto_reply ou needs_confirmation, sinon null"}
""".trimIndent()

    fun classifyEmail(apiKey: String, model: String, email: ParsedEmail): ClassificationDecision {
        val prompt = buildString {
            append("De : ${email.from}\n")
            append("Sujet : ${email.subject}\n")
            append("Extrait : ${email.snippet}\n\n")
            append("Corps du message :\n${email.bodyText.take(4000)}")
        }
        val messages = JSONArray().put(
            JSONObject().put("role", "user").put("content", prompt)
        )
        val response = AnthropicClient.createMessage(
            apiKey = apiKey, model = model, system = systemPrompt(), messages = messages, maxTokens = 1024,
        )

        val content = response.optJSONArray("content") ?: JSONArray()
        var text = buildString {
            for (i in 0 until content.length()) {
                val block = content.getJSONObject(i)
                if (block.optString("type") == "text") append(block.optString("text"))
            }
        }.trim()
        text = text.removePrefix("```json").removePrefix("```").removeSuffix("```").trim()

        val parsed = try {
            JSONObject(text)
        } catch (e: Exception) {
            return ClassificationDecision(
                category = "needs_confirmation",
                reasoning = "Réponse du classificateur illisible, soumis par précaution.",
                suggestedLabel = null,
                suggestedReply = null,
            )
        }

        val category = parsed.optString("category", "needs_confirmation")
            .takeIf { it in CATEGORIES } ?: "needs_confirmation"
        return ClassificationDecision(
            category = category,
            reasoning = parsed.optString("reasoning", ""),
            suggestedLabel = parsed.optStringOrNull("suggested_label"),
            suggestedReply = parsed.optStringOrNull("suggested_reply"),
        )
    }

    private fun JSONObject.optStringOrNull(key: String): String? =
        if (!has(key) || isNull(key)) null else getString(key)
}
