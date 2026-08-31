package fr.gemsofrod.encyclopedie.data

/** Formes de taille couvertes par l'estimateur de poids, avec leur
 * coefficient volumique (précision indicative 10-15%, nécessite le poids
 * spécifique réel de l'espèce). [Rond] et [Ovale] suivent chacune leur
 * propre formule ; les autres formes suivent la formule standard
 * longueur × largeur × hauteur × poids spécifique × coefficient. */
enum class LapidaireCutShape(val coefficient: Double, val requiresTwoDimensions: Boolean) {
    ROND(0.0018, requiresTwoDimensions = false),
    OVALE(0.0020, requiresTwoDimensions = true),
    COUSSIN_CARRE(0.0018, requiresTwoDimensions = false),
    COUSSIN_RECTANGULAIRE(0.0022, requiresTwoDimensions = true),
    CARRE_A_GRADIN(0.0022, requiresTwoDimensions = false),
    RECTANGLE_A_GRADINS(0.0025, requiresTwoDimensions = true),
    COUSSIN_CARRE_GRADIN(0.0024, requiresTwoDimensions = false),
    COUSSIN_RECTANGULAIRE_GRADIN(0.0026, requiresTwoDimensions = true),
    MARQUISE(0.0017, requiresTwoDimensions = true),
    POIRE(0.0018, requiresTwoDimensions = true),
    TRIANGLE_BOMBE(0.0018, requiresTwoDimensions = false),
    TRIANGLE(0.00162, requiresTwoDimensions = false),
    TRAPEZE(0.0026, requiresTwoDimensions = true),
    COEUR(0.0021, requiresTwoDimensions = false)
}

/** Estimateur de poids par le volume, pour une pierre déjà taillée et
 * montée (impossible à peser directement). Mesures au pied à coulisse ou au
 * micromètre (précision 1/100 mm), poids spécifique (P.S.) de l'espèce
 * requis. [Rond] utilise diamètre² ; [Ovale] utilise (largeur + longueur)² /
 * 2 ; toutes les autres formes utilisent longueur × largeur. */
object LapidaireWeightEstimator {

    /**
     * @param dimension1Mm Diamètre (rond) ou longueur (autres formes), en mm.
     * @param dimension2Mm Largeur en mm — obligatoire si [LapidaireCutShape.requiresTwoDimensions],
     *                      ignorée sinon (la forme est alors traitée comme carrée/symétrique).
     * @param heightMm Hauteur totale (table à culet), en mm.
     * @param specificGravity Poids spécifique (densité) de l'espèce.
     */
    fun estimateWeightCarats(
        shape: LapidaireCutShape,
        dimension1Mm: Double,
        dimension2Mm: Double?,
        heightMm: Double,
        specificGravity: Double
    ): Double {
        require(dimension1Mm > 0.0) { "dimension1Mm doit être strictement positif." }
        require(!shape.requiresTwoDimensions || (dimension2Mm != null && dimension2Mm > 0.0)) {
            "Cette forme nécessite une seconde dimension (largeur) strictement positive."
        }
        require(heightMm > 0.0) { "heightMm doit être strictement positif." }
        require(specificGravity > 0.0) { "specificGravity doit être strictement positif." }

        val effectiveDim2 = dimension2Mm?.takeIf { it > 0.0 } ?: dimension1Mm
        val base = if (shape == LapidaireCutShape.OVALE) {
            val sum = dimension1Mm + effectiveDim2
            (sum * sum) / 2.0
        } else {
            dimension1Mm * effectiveDim2
        }
        return base * heightMm * specificGravity * shape.coefficient
    }
}
