package fr.gemsofrod.encyclopedie.data

/**
 * Appellation commerciale de couleur reconnue dans le commerce de la
 * gemmologie (ex. « London Blue » pour la topaze, « Paraíba » pour la
 * tourmaline), avec une teinte HSV représentative pour la comparaison
 * visuelle. [nom] est un nom commercial international, utilisé tel quel
 * dans toutes les langues du commerce de la pierre précieuse — seul
 * [espece] (l'espèce/variété associée) est localisé.
 */
data class NuancierCouleur(
    val nom: String,
    val espece: String,
    val hueDeg: Float,
    val saturation: Float,
    val valeur: Float
)

/**
 * Trouve, parmi une liste d'appellations de référence, celle dont la teinte
 * HSV est la plus proche d'une couleur observée — outil de comparaison
 * visuelle indicatif, pas une mesure colorimétrique certifiée (l'écran du
 * téléphone et l'éclairage ambiant influencent fortement la perception des
 * couleurs).
 */
object NuancierColorMatcher {
    /** En dessous de ce seuil de saturation, la couleur est jugée trop
     * neutre (gris/blanc/noir) pour être rapprochée d'une appellation. */
    const val ACHROMATIC_SATURATION_THRESHOLD = 0.12f

    /** Distance maximale (métrique interne, sans unité) au-delà de laquelle
     * aucune appellation n'est jugée assez proche pour être proposée. */
    const val MAX_MATCH_DISTANCE = 0.35f

    /** Distance angulaire entre deux teintes (0-360°), en tenant compte du
     * bouclage du cercle chromatique (ex. 350° et 10° sont proches de 20°,
     * pas de 340°). */
    fun hueDistance(a: Float, b: Float): Float {
        val diff = kotlin.math.abs(a - b) % 360f
        return if (diff > 180f) 360f - diff else diff
    }

    /**
     * @return l'appellation la plus proche de la couleur (h,s,v) donnée,
     * ou `null` si la couleur est trop neutre (achromatique) ou trop
     * éloignée de toutes les appellations connues.
     */
    fun match(hueDeg: Float, saturation: Float, valeur: Float, colors: List<NuancierCouleur>): NuancierCouleur? {
        if (saturation < ACHROMATIC_SATURATION_THRESHOLD || colors.isEmpty()) return null

        var best: NuancierCouleur? = null
        var bestDistance = Float.MAX_VALUE
        for (color in colors) {
            val dh = hueDistance(hueDeg, color.hueDeg) / 180f
            val ds = saturation - color.saturation
            val dv = valeur - color.valeur
            val distance = dh * dh * 3f + ds * ds + dv * dv
            if (distance < bestDistance) {
                bestDistance = distance
                best = color
            }
        }
        return if (bestDistance <= MAX_MATCH_DISTANCE) best else null
    }
}
