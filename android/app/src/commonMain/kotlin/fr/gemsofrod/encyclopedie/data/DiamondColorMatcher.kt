package fr.gemsofrod.encyclopedie.data

/**
 * Un cran de l'échelle de couleur du diamant blanc (D à Z, GIA et
 * équivalents) : lettre de grade, catégorie regroupant plusieurs lettres, et
 * intensité de teinte jaune indicative (0 = D, incolore ; 1 = Z, teinte la
 * plus marquée), utilisée pour le rendu visuel et le rapprochement au
 * curseur. Ne couvre que l'échelle « blanche » standard, pas les diamants de
 * couleur fantaisie (fancy colors), qui suivent un système distinct.
 */
data class DiamondColorGrade(
    val letter: String,
    val category: String,
    val tintIntensity: Float
)

/**
 * Trouve, parmi une liste de grades de référence, celui dont l'intensité de
 * teinte est la plus proche d'une valeur observée (curseur de comparaison
 * visuelle — repère indicatif, pas une mesure colorimétrique certifiée).
 */
object DiamondColorMatcher {
    fun nearest(intensity: Float, grades: List<DiamondColorGrade>): DiamondColorGrade? {
        if (grades.isEmpty()) return null
        return grades.minByOrNull { kotlin.math.abs(it.tintIntensity - intensity) }
    }
}
