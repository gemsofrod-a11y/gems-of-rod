package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

private const val PREFS_NAME = "gems_of_rod_reflectivity_calibration"
private const val KEY_POINT_A_RI = "point_a_ri"
private const val KEY_POINT_A_BRIGHTNESS = "point_a_brightness"
private const val KEY_POINT_B_RI = "point_b_ri"
private const val KEY_POINT_B_BRIGHTNESS = "point_b_brightness"
private const val KEY_CALIBRATED_AT = "calibrated_at_millis"

/**
 * Calibration du mode réflectivité (voir ReflectivityMeter.kt), persistée
 * localement. Contrairement à [FavoritesRepository], une seule calibration
 * est conservée à la fois : recalibrer remplace la précédente plutôt que
 * d'en accumuler plusieurs.
 */
object ReflectivityCalibrationRepository {
    private var prefs: SharedPreferences? = null
    private val record = mutableStateOf<ReflectivityCalibrationRecord?>(null)

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        record.value = loadFrom(sharedPrefs)
    }

    fun current(): ReflectivityCalibrationRecord? = record.value

    fun save(pointA: CalibrationPoint, pointB: CalibrationPoint, calibratedAtEpochMillis: Long) {
        // Valide les deux points avant toute écriture : on ne veut jamais
        // persister une calibration invalide (ex. brillances identiques).
        val newRecord = ReflectivityCalibrationRecord(pointA, pointB, calibratedAtEpochMillis)
        record.value = newRecord
        prefs?.edit()
            ?.putFloat(KEY_POINT_A_RI, pointA.knownRefractiveIndex.toFloat())
            ?.putFloat(KEY_POINT_A_BRIGHTNESS, pointA.measuredBrightness.toFloat())
            ?.putFloat(KEY_POINT_B_RI, pointB.knownRefractiveIndex.toFloat())
            ?.putFloat(KEY_POINT_B_BRIGHTNESS, pointB.measuredBrightness.toFloat())
            ?.putLong(KEY_CALIBRATED_AT, calibratedAtEpochMillis)
            ?.apply()
    }

    fun clear() {
        record.value = null
        prefs?.edit()?.clear()?.apply()
    }

    private fun loadFrom(sharedPrefs: SharedPreferences): ReflectivityCalibrationRecord? {
        if (!sharedPrefs.contains(KEY_CALIBRATED_AT)) return null
        val pointA = CalibrationPoint(
            knownRefractiveIndex = sharedPrefs.getFloat(KEY_POINT_A_RI, 0f).toDouble(),
            measuredBrightness = sharedPrefs.getFloat(KEY_POINT_A_BRIGHTNESS, 0f).toDouble()
        )
        val pointB = CalibrationPoint(
            knownRefractiveIndex = sharedPrefs.getFloat(KEY_POINT_B_RI, 0f).toDouble(),
            measuredBrightness = sharedPrefs.getFloat(KEY_POINT_B_BRIGHTNESS, 0f).toDouble()
        )
        val calibratedAt = sharedPrefs.getLong(KEY_CALIBRATED_AT, 0L)
        // Protège contre des préférences corrompues (ex. les deux brillances
        // identiques après une écriture partielle) : ReflectivityCalibrationRecord
        // valide les points à la construction et lève une exception sinon.
        return runCatching { ReflectivityCalibrationRecord(pointA, pointB, calibratedAt) }.getOrNull()
    }
}
