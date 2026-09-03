package fr.gemsofrod.encyclopedie.data

import android.content.Context

private const val PREFS_NAME = "gems_of_rod_stock_backup"
private const val KEY_LAST_BACKUP_MILLIS = "lastBackupMillis"

/**
 * Horodatage de la dernière sauvegarde du stock (export partagé ou enregistré
 * via le sélecteur de fichiers système, qui permet de choisir Google Drive
 * comme destination sans intégration OAuth) — le stock n'est autrement
 * stocké que localement sur l'appareil, sans synchronisation automatique.
 * Sert uniquement à rappeler périodiquement à l'utilisateur de sauvegarder
 * (voir [fr.gemsofrod.encyclopedie.ui.screens.StockDashboardScreen]).
 */
object StockBackupPrefs {
    fun lastBackupMillis(context: Context): Long? {
        val value = prefs(context).getLong(KEY_LAST_BACKUP_MILLIS, -1L)
        return if (value == -1L) null else value
    }

    fun recordBackupNow(context: Context) {
        prefs(context).edit().putLong(KEY_LAST_BACKUP_MILLIS, System.currentTimeMillis()).apply()
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
