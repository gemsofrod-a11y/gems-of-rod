package fr.gemsofrod.encyclopedie.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemLocalization

/**
 * Renvoie cette gemme avec son nom et ses textes descriptifs traduits dans la
 * langue d'interface actuelle (celle appliquée à `Configuration` par
 * `MainActivity.attachBaseContext`), ou telle quelle si aucune traduction
 * n'est disponible pour cette langue.
 */
@Composable
fun Gem.localized(): Gem {
    val languageCode = LocalConfiguration.current.locales[0].language
    return GemLocalization.localize(this, languageCode)
}
