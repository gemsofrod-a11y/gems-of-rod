package fr.gemsofrod.encyclopedie.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemLocalization
import fr.gemsofrod.encyclopedie.data.LabelLocalization
import fr.gemsofrod.encyclopedie.data.Meteorite
import fr.gemsofrod.encyclopedie.data.MeteoriteLocalization

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

/**
 * Renvoie cette météorite avec son nom et ses textes descriptifs traduits
 * dans la langue d'interface actuelle, ou telle quelle si aucune traduction
 * n'est disponible pour cette langue.
 */
@Composable
fun Meteorite.localized(): Meteorite {
    val languageCode = LocalConfiguration.current.locales[0].language
    return MeteoriteLocalization.localize(this, languageCode)
}

/**
 * Renvoie le texte des inclusions typiques de la gemme d'identifiant [gemId]
 * traduit dans la langue d'interface actuelle (ou en français si aucune
 * traduction n'est disponible), ou `null` si la gemme n'a pas de texte
 * d'inclusions (gemmes translucides à opaques ou opaques).
 */
@Composable
fun localizedInclusions(gemId: String): String? {
    val languageCode = LocalConfiguration.current.locales[0].language
    return GemLocalization.localizeInclusions(gemId, languageCode)
}

/**
 * Traduit un libellé utilisé aussi comme clé interne (famille minérale,
 * chakra, bienfait, signe astrologique, mois de naissance) pour l'affichage
 * dans la langue d'interface actuelle. Le libellé français d'origine doit
 * continuer à être utilisé pour le routage et les recherches par clé.
 */
@Composable
fun localizedLabel(label: String): String {
    val languageCode = LocalConfiguration.current.locales[0].language
    return LabelLocalization.localize(label, languageCode)
}
