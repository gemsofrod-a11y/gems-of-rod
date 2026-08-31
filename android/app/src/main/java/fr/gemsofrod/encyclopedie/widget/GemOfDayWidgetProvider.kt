package fr.gemsofrod.encyclopedie.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Bundle
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import fr.gemsofrod.encyclopedie.MainActivity
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
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
 *
 * Le fond du widget est une photo de la pierre quand une existe (crédits
 * [GemImages]), sinon un aplat de sa couleur dominante ([androidx.compose.ui.graphics.Color]
 * — la même que celle utilisée dans le reste de l'application) — les deux
 * cas sont composés en un seul bitmap arrondi (mêmes coins que l'ancien
 * fond statique) car `RemoteViews` ne peut pas découper les coins d'une
 * `ImageView` selon la forme d'une vue voisine.
 */
class GemOfDayWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        // Redessine le fond à la nouvelle taille lors d'un redimensionnement
        // du widget (resizeMode="horizontal|vertical" dans le manifeste).
        updateWidget(context, appWidgetManager, appWidgetId)
    }

    companion object {
        private const val DEFAULT_WIDTH_DP = 180
        private const val DEFAULT_HEIGHT_DP = 90
        private const val CORNER_RADIUS_DP = 16f
        private const val BORDER_WIDTH_DP = 1f
        private const val BORDER_COLOR = 0x33C9A227
        private const val SCRIM_COLOR = 0x59000000

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

            val density = context.resources.displayMetrics.density
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val widthPx = (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, DEFAULT_WIDTH_DP) * density)
                .toInt().coerceAtLeast(1)
            val heightPx = (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, DEFAULT_HEIGHT_DP) * density)
                .toInt().coerceAtLeast(1)

            val photo = loadGemPhoto(context, gemOfDay.id, widthPx, heightPx)
            val background = buildBackgroundBitmap(
                density = density,
                widthPx = widthPx,
                heightPx = heightPx,
                photo = photo,
                fallbackColorArgb = gemOfDay.couleur.swatch.toArgb()
            )

            val views = RemoteViews(context.packageName, R.layout.widget_gem_of_day).apply {
                setTextViewText(R.id.widget_label, labelContext.getString(R.string.widget_gem_of_day_label))
                setTextViewText(R.id.widget_gem_name, localizedGem.nom)
                setTextViewText(R.id.widget_gem_family, localizedGem.famille)
                setImageViewBitmap(R.id.widget_gem_image, background)
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

        /**
         * Charge la photo « facettée » de la pierre si elle existe (même
         * priorité que [fr.gemsofrod.encyclopedie.ui.screens.FavoritesScreen] :
         * FACETTEE d'abord, sinon la première photo disponible), sous-échantillonnée
         * à la taille cible pour limiter la mémoire utilisée. `null` si la
         * pierre n'a aucune photo créditée.
         */
        private fun loadGemPhoto(context: Context, gemId: String, targetWidthPx: Int, targetHeightPx: Int): Bitmap? {
            val credit = GemImages.creditsFor(gemId).let { credits ->
                credits.firstOrNull { it.type == GemImageType.FACETTEE } ?: credits.firstOrNull()
            } ?: return null

            val resId = context.resources.getIdentifier(credit.drawableName, "drawable", context.packageName)
            if (resId == 0) return null

            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeResource(context.resources, resId, bounds)
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

            var sampleSize = 1
            while (bounds.outWidth / sampleSize > targetWidthPx * 2 || bounds.outHeight / sampleSize > targetHeightPx * 2) {
                sampleSize *= 2
            }
            return BitmapFactory.decodeResource(
                context.resources,
                resId,
                BitmapFactory.Options().apply { inSampleSize = sampleSize }
            )
        }

        /**
         * Compose le fond du widget en un seul bitmap : la photo de la pierre
         * (recadrée en « center crop ») si elle existe, sinon un aplat de sa
         * couleur ; un voile semi-transparent par-dessus pour garder le texte
         * lisible dans les deux cas ; le tout découpé aux coins arrondis avec
         * une bordure dorée, pour reprendre exactement l'apparence de l'ancien
         * fond statique (drawable/widget_background, désormais inutile).
         */
        private fun buildBackgroundBitmap(
            density: Float,
            widthPx: Int,
            heightPx: Int,
            photo: Bitmap?,
            fallbackColorArgb: Int
        ): Bitmap {
            val cornerRadiusPx = CORNER_RADIUS_DP * density
            val borderWidthPx = BORDER_WIDTH_DP * density

            val result = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            val rect = RectF(0f, 0f, widthPx.toFloat(), heightPx.toFloat())

            // Masque aux coins arrondis : tout ce qui est dessiné ensuite avec
            // ce même Paint (mode SRC_IN) reste confiné à cette forme.
            val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, maskPaint)
            maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            if (photo != null) {
                val scale = maxOf(widthPx.toFloat() / photo.width, heightPx.toFloat() / photo.height)
                val scaledWidth = photo.width * scale
                val scaledHeight = photo.height * scale
                val left = (widthPx - scaledWidth) / 2f
                val top = (heightPx - scaledHeight) / 2f
                canvas.drawBitmap(photo, null, RectF(left, top, left + scaledWidth, top + scaledHeight), maskPaint)
            } else {
                canvas.drawRect(rect, Paint(maskPaint).apply { color = fallbackColorArgb })
            }

            canvas.drawRect(rect, Paint(maskPaint).apply { color = SCRIM_COLOR })

            val inset = borderWidthPx / 2f
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = borderWidthPx
                color = BORDER_COLOR
            }
            canvas.drawRoundRect(
                RectF(inset, inset, widthPx - inset, heightPx - inset),
                cornerRadiusPx,
                cornerRadiusPx,
                borderPaint
            )

            return result
        }
    }
}
