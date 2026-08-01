package fr.gemsofrod.encyclopedie.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Résout un nom de ressource drawable (ex. généré par le workflow de récupération
 * des photos de gemmes) en identifiant de ressource, ou 0 si absent.
 */
@Composable
fun rememberDrawableResId(name: String?): Int {
    val context = LocalContext.current
    return remember(name) {
        if (name.isNullOrBlank()) {
            0
        } else {
            context.resources.getIdentifier(name, "drawable", context.packageName)
        }
    }
}
