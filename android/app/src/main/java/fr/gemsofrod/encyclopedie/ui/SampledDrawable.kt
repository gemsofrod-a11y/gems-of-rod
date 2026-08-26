package fr.gemsofrod.encyclopedie.ui

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * Résout un drawable par nom (voir [rememberDrawableResId]) et le décode à une
 * résolution proche de sa taille d'affichage, plutôt qu'à sa résolution
 * intrinsèque comme le ferait painterResource(). Les photos de gemmes vivent
 * dans drawable-nodpi (aucun sous-échantillonnage par densité) et proviennent
 * pour beaucoup de téléchargements Wikimedia Commons non redimensionnés —
 * certaines décodent à ~45 Mo en mémoire pleine résolution alors qu'elles ne
 * s'affichent jamais qu'en vignette de quelques dizaines de dp. Renvoie null
 * si le nom ne résout à aucune ressource.
 */
@Composable
fun rememberSampledDrawablePainter(name: String?, targetSize: Dp): Painter? {
    val context = LocalContext.current
    val density = LocalDensity.current
    val resId = rememberDrawableResId(name)
    val targetPx = with(density) { targetSize.roundToPx() }
    return remember(resId, targetPx) {
        if (resId == 0) return@remember null
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(context.resources, resId, bounds)
        // Facteur 2 de marge par rapport à la taille cible : les vignettes
        // sont souvent recadrées (ContentScale.Crop) sur un appareil à forte
        // densité, une marge évite un flou perceptible tout en restant très
        // loin de la résolution source.
        var sampleSize = 1
        while (bounds.outWidth / sampleSize > targetPx * 2 || bounds.outHeight / sampleSize > targetPx * 2) {
            sampleSize *= 2
        }
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            resId,
            BitmapFactory.Options().apply { inSampleSize = sampleSize }
        ) ?: return@remember null
        BitmapPainter(bitmap.asImageBitmap())
    }
}
