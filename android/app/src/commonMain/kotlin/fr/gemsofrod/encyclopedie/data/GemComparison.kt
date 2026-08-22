package fr.gemsofrod.encyclopedie.data

/**
 * Valeurs numériques brutes extraites des fiches [Gem]/[GemDiagnostic] pour
 * une gemme donnée, utilisées par l'outil de comparaison (graphique radar).
 * `null` signifie que la donnée est absente ou n'a pas pu être interprétée
 * comme un nombre.
 */
data class GemComparisonProfile(
    val dureteValue: Double?,
    val indiceValue: Double?,
    val densiteValue: Double?,
    val fluorescenceValue: Double?,
    val pleochroismeValue: Double?
) {
    val dureteNorm: Float get() = normalize(dureteValue, 0.0, 10.0)
    val indiceNorm: Float get() = normalize(indiceValue, 1.3, 2.6)
    val densiteNorm: Float get() = normalize(densiteValue, 1.8, 6.6)
    val fluorescenceNorm: Float get() = normalize(fluorescenceValue, 0.0, 3.0)
    val pleochroismeNorm: Float get() = normalize(pleochroismeValue, 0.0, 3.0)

    private fun normalize(value: Double?, min: Double, max: Double): Float {
        if (value == null) return 0f
        return ((value - min) / (max - min)).toFloat().coerceIn(0f, 1f)
    }
}

/**
 * Calcule le profil comparatif d'une gemme à partir de ses champs texte
 * (dureté, indice de réfraction, densité, fluorescence, pléochroïsme),
 * pour alimenter un graphique radar comparant deux pierres entre elles.
 */
object GemComparison {
    fun profile(gem: Gem): GemComparisonProfile {
        val diagnostic = GemDiagnostics.data[gem.id]
        return GemComparisonProfile(
            dureteValue = parseNumericAverage(gem.durete),
            indiceValue = parseNumericAverage(gem.indiceRefraction),
            densiteValue = diagnostic?.let { parseNumericAverage(it.densite) },
            fluorescenceValue = diagnostic?.let { parseIntensity(it.fluorescence) },
            pleochroismeValue = diagnostic?.let { parseIntensity(it.pleochroisme) }
        )
    }

    /**
     * Moyenne des nombres décimaux (virgule française) trouvés dans le
     * texte avant toute parenthèse explicative, ex. "2,5 - 3,5 (7 en gangue
     * de quartz)" → moyenne de 2,5 et 3,5, en ignorant le "7" entre
     * parenthèses.
     */
    private fun parseNumericAverage(text: String): Double? {
        val relevant = text.substringBefore('(').trim()
        val numbers = Regex("""\d+(?:,\d+)?""")
            .findAll(relevant)
            .map { it.value.replace(',', '.').toDouble() }
            .toList()
        return if (numbers.isEmpty()) null else numbers.average()
    }

    /**
     * Convertit un texte d'intensité rédigé dans le vocabulaire contrôlé
     * français (Aucun(e), Faible, Modéré(e), Fort(e), Variable) en une
     * valeur 0..3. Les formulations composées ("Modérée à forte") sont
     * moyennées entre les niveaux repérés.
     */
    private fun parseIntensity(text: String): Double? {
        val lower = text.lowercase()
        if (lower.isBlank()) return null
        val levels = mutableListOf<Double>()
        if (Regex("""aucun""").containsMatchIn(lower)) levels += 0.0
        if (Regex("""faible""").containsMatchIn(lower)) levels += 1.0
        if (Regex("""mod[ée]r""").containsMatchIn(lower)) levels += 2.0
        if (Regex("""fort""").containsMatchIn(lower)) levels += 3.0
        if (levels.isNotEmpty()) return levels.average()
        return if (lower.contains("variable")) 1.5 else null
    }
}
