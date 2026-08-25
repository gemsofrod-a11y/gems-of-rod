package fr.gemsofrod.encyclopedie.data

import kotlin.math.pow
import kotlin.math.sqrt

/** Réflectance de Fresnel à incidence normale à l'interface air/gemme, et son
 * inverse. R = ((n - 1) / (n + 1))², d'où n = (1 + √R) / (1 - √R).
 *
 * Base physique du mode "réflectomètre" (Phase A du plan) : contrairement à
 * un réfractomètre classique, aucun hémicylindre ni liquide de contact n'est
 * nécessaire, seule la brillance réfléchie par la facette compte. En
 * contrepartie la précision est nettement inférieure — voir
 * [ReflectivityCalibration.estimate].
 */
object Fresnel {
    /** Réflectance R (0..1) pour un indice de réfraction n ≥ 1. */
    fun reflectance(refractiveIndex: Double): Double {
        require(refractiveIndex >= 1.0) { "L'indice de réfraction doit être ≥ 1 (reçu $refractiveIndex)." }
        val ratio = (refractiveIndex - 1.0) / (refractiveIndex + 1.0)
        return ratio * ratio
    }

    /** Indice de réfraction correspondant à une réflectance R (0..1 exclu). */
    fun refractiveIndexFromReflectance(reflectance: Double): Double {
        require(reflectance >= 0.0 && reflectance < 1.0) {
            "La réflectance doit être dans [0, 1[ (reçue $reflectance)."
        }
        val sqrtR = sqrt(reflectance)
        return (1.0 + sqrtR) / (1.0 - sqrtR)
    }
}

/** Un point de calibration : brillance mesurée (unité arbitraire, ex. moyenne
 * de pixels 0..255) pour une pierre-étalon d'indice de réfraction connu. */
data class CalibrationPoint(
    val knownRefractiveIndex: Double,
    val measuredBrightness: Double
)

/** Résultat d'une estimation par réflectivité : une plage plutôt qu'une
 * valeur unique, pour ne jamais afficher une fausse précision. */
data class ReflectivityEstimate(
    val refractiveIndexLow: Double,
    val refractiveIndexCenter: Double,
    val refractiveIndexHigh: Double
)

/** Calibration à deux points pour une session de mesure donnée (même
 * téléphone, même éclairage, même capot). On photographie deux pierres-étalon
 * d'indice de réfraction connu et on ajuste une correspondance linéaire entre
 * brillance mesurée et réflectance de Fresnel théorique — cela absorbe le
 * gain de l'appareil, la lumière parasite et la géométrie du capot, qu'une
 * conversion directe de la formule de Fresnel ne pourrait pas corriger.
 *
 * [measurementUncertainty] est un écart en réflectance (pas en indice, la
 * relation n'étant pas linéaire) appliqué symétriquement autour de la
 * réflectance mesurée pour produire la plage de [ReflectivityEstimate]. Sa
 * valeur par défaut est un point de départ raisonnable ; elle devra être
 * recalibrée à partir de mesures réelles lors de la validation sur device
 * (Phase F du plan) plutôt que de rester une constante théorique.
 */
class ReflectivityCalibration(
    private val pointA: CalibrationPoint,
    private val pointB: CalibrationPoint,
    private val measurementUncertainty: Double = DEFAULT_MEASUREMENT_UNCERTAINTY
) {
    private val gain: Double
    private val offset: Double

    init {
        require(pointA.measuredBrightness != pointB.measuredBrightness) {
            "Les deux points de calibration ont la même brillance mesurée : impossible d'ajuster une correspondance."
        }
        require(measurementUncertainty > 0.0) { "measurementUncertainty doit être strictement positif." }

        val reflectanceA = Fresnel.reflectance(pointA.knownRefractiveIndex)
        val reflectanceB = Fresnel.reflectance(pointB.knownRefractiveIndex)

        // Ajustement linéaire brillance = gain * réflectance + offset à partir
        // des deux points (brillance_i, réflectance_i) connus.
        gain = (pointA.measuredBrightness - pointB.measuredBrightness) / (reflectanceA - reflectanceB)
        offset = pointA.measuredBrightness - gain * reflectanceA
    }

    /** Estime l'indice de réfraction (sous forme de plage) à partir d'une
     * brillance mesurée dans les mêmes conditions que la calibration. */
    fun estimate(measuredBrightness: Double): ReflectivityEstimate {
        val reflectance = (measuredBrightness - offset) / gain
        val reflectanceLow = (reflectance - measurementUncertainty).coerceIn(0.0, MAX_VALID_REFLECTANCE)
        val reflectanceHigh = (reflectance + measurementUncertainty).coerceIn(0.0, MAX_VALID_REFLECTANCE)

        return ReflectivityEstimate(
            refractiveIndexLow = Fresnel.refractiveIndexFromReflectance(reflectanceLow),
            refractiveIndexCenter = Fresnel.refractiveIndexFromReflectance(
                reflectance.coerceIn(0.0, MAX_VALID_REFLECTANCE)
            ),
            refractiveIndexHigh = Fresnel.refractiveIndexFromReflectance(reflectanceHigh)
        )
    }

    companion object {
        const val DEFAULT_MEASUREMENT_UNCERTAINTY: Double = 0.01

        // Marge sous 1.0 pour éviter que l'inversion de Fresnel ne diverge
        // vers l'infini sur une mesure bruitée en borne haute.
        private const val MAX_VALID_REFLECTANCE: Double = 0.999
    }
}

/** Calibration persistée pour une session de mesure (Phase B du plan) :
 * les deux points bruts tels que saisis/mesurés, plus la date de calibration
 * pour proposer une recalibration périodique — la lumière ambiante et le
 * capot changent d'une séance à l'autre.
 *
 * [calibration] est calculé une seule fois à la construction plutôt qu'à
 * chaque accès : cela valide aussi les deux points immédiatement (utile pour
 * détecter des préférences corrompues rechargées depuis le disque, avant même
 * de tenter une estimation).
 */
data class ReflectivityCalibrationRecord(
    val pointA: CalibrationPoint,
    val pointB: CalibrationPoint,
    val calibratedAtEpochMillis: Long
) {
    val calibration: ReflectivityCalibration = ReflectivityCalibration(pointA, pointB)

    /** Vrai si la calibration date de plus de [maxAgeMillis]. Valeur de
     * départ à 7 jours, à ajuster lors de la validation terrain (Phase F). */
    fun isStale(nowEpochMillis: Long, maxAgeMillis: Long = DEFAULT_MAX_AGE_MILLIS): Boolean =
        nowEpochMillis - calibratedAtEpochMillis > maxAgeMillis

    companion object {
        const val DEFAULT_MAX_AGE_MILLIS: Long = 7L * 24 * 60 * 60 * 1000
    }
}
