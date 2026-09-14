package fr.gemsofrod.encyclopedie.data

/**
 * Estimation indicative du poids en carats d'une pierre montée (donc non
 * pesable directement) à partir de ses dimensions visibles et du poids
 * spécifique de son espèce. Approximation par volume de la boîte
 * englobante : profondeur ≈ 0,62 × moyenne(largeur, hauteur) (proportion
 * standard d'une pierre à facettes), facteur de remplissage 0,48 (une
 * pierre facettée n'occupe pas tout son volume englobant).
 */
object RingCaratEstimator {

    /**
     * @param specificGravity Poids spécifique (densité) de l'espèce.
     * @param widthMm Largeur (ou diamètre pour une forme symétrique), en mm.
     * @param heightMm Hauteur, en mm — égale à [widthMm] pour une forme symétrique.
     */
    fun estimateCarats(specificGravity: Double, widthMm: Double, heightMm: Double): Double {
        require(widthMm > 0.0) { "widthMm doit être strictement positif." }
        require(heightMm > 0.0) { "heightMm doit être strictement positif." }
        require(specificGravity > 0.0) { "specificGravity doit être strictement positif." }

        val depthMm = ((widthMm + heightMm) / 2.0) * 0.62
        val fillFactor = 0.48
        return widthMm * heightMm * depthMm * fillFactor * specificGravity / 200.0
    }
}
