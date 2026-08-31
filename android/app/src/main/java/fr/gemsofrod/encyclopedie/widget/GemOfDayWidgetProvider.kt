package fr.gemsofrod.encyclopedie.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.RemoteViews
import fr.gemsofrod.encyclopedie.MainActivity
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.GemLocalization
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.LanguageRepository
import java.time.LocalDate
import java.util.Locale

/**
 * Widget d'écran d'accueil « Gemme du jour » : affiche une pierre choisie de
 * façon déterministe selon le jour de l'année (même pierre pour tout le
 * monde le même jour, sans état à synchroniser). Implémenté en
 * [AppWidgetProvider]/[RemoteViews] classiques (API du framework Android,
 * aucune dépendance ajoutée) plutôt qu'avec Glance.
 */
class GemOfDayWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        /**
         * Contexte dont les ressources sont résolues dans la langue choisie
         * par l'utilisateur dans l'application (indépendante de la langue
         * système), même patron que `MainActivity.attachBaseContext` — un
         * widget est inflaté hors du cycle de vie de l'Activity, donc sans
         * ce contournement ses textes statiques suivraient la langue système
         * plutôt que la préférence in-app.
         */
        private fun localizedContext(context: Context): Context {
            val locale = Locale(LanguageRepository.getLanguage(context).code)
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val gems = GemsRepository.gems
            if (gems.isEmpty()) return

            val gemOfDay = gems[LocalDate.now().dayOfYear % gems.size]
            val languageCode = LanguageRepository.getLanguage(context).code
            val localizedGem = GemLocalization.localize(gemOfDay, languageCode)
            val labelContext = localizedContext(context)

            val views = RemoteViews(context.packageName, R.layout.widget_gem_of_day).apply {
                setTextViewText(R.id.widget_label, labelContext.getString(R.string.widget_gem_of_day_label))
                setTextViewText(R.id.widget_gem_name, localizedGem.nom)
                setTextViewText(R.id.widget_gem_family, localizedGem.famille)
            }

            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
