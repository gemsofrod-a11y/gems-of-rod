package fr.gemsofrod.encyclopedie.ui

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import fr.gemsofrod.encyclopedie.data.StockPhotoStorage

/**
 * Charge la photo d'une fiche de stock (voir [StockPhotoStorage]) sous-
 * échantillonnée à la taille d'affichage — même principe que
 * [rememberSampledDrawablePainter], mais depuis un fichier du stockage
 * interne plutôt qu'une ressource drawable.
 */
@Composable
fun rememberStockPhotoBitmap(fileName: String?, targetSize: Dp): Bitmap? {
    val context = LocalContext.current
    val density = LocalDensity.current
    val targetPx = with(density) { targetSize.roundToPx() }
    return remember(fileName, targetPx) {
        StockPhotoStorage.loadSampledBitmap(context, fileName, targetPx)
    }
}
