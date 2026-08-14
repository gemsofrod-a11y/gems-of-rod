package fr.gemsofrod.encyclopedie.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Badge
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemsRepository

/**
 * Résout le titre et la description d'un succès ([Badge]) dans la langue
 * d'interface courante. Partagé entre [fr.gemsofrod.encyclopedie.ui.screens.AchievementsScreen]
 * (liste des succès) et la notification affichée au déblocage.
 */
@Composable
fun localizedBadgeTitle(badge: Badge): String = when (badge.id) {
    "first_gem" -> stringResource(R.string.achievement_first_gem_title)
    "curious" -> stringResource(R.string.achievement_curious_title)
    "collector" -> stringResource(R.string.achievement_collector_title)
    "encyclopedist" -> stringResource(R.string.achievement_encyclopedist_title)
    "master" -> stringResource(R.string.achievement_master_title)
    "rainbow" -> stringResource(R.string.achievement_rainbow_title)
    "first_favorite" -> stringResource(R.string.achievement_first_favorite_title)
    "fan" -> stringResource(R.string.achievement_fan_title)
    "big_collector" -> stringResource(R.string.achievement_big_collector_title)
    "first_quiz" -> stringResource(R.string.achievement_first_quiz_title)
    "quiz_regular" -> stringResource(R.string.achievement_quiz_regular_title)
    "perfect_score" -> stringResource(R.string.achievement_perfect_score_title)
    "legendary" -> stringResource(R.string.achievement_legendary_title)
    else -> badge.id
}

@Composable
fun localizedBadgeDescription(badge: Badge): String = when (badge.id) {
    "first_gem" -> stringResource(R.string.achievement_first_gem_desc)
    "curious" -> stringResource(R.string.achievement_curious_desc)
    "collector" -> stringResource(R.string.achievement_collector_desc)
    "encyclopedist" -> stringResource(R.string.achievement_encyclopedist_desc)
    "master" -> stringResource(R.string.achievement_master_desc, GemsRepository.gems.size)
    "rainbow" -> stringResource(R.string.achievement_rainbow_desc, GemColorCategory.entries.size)
    "first_favorite" -> stringResource(R.string.achievement_first_favorite_desc)
    "fan" -> stringResource(R.string.achievement_fan_desc)
    "big_collector" -> stringResource(R.string.achievement_big_collector_desc)
    "first_quiz" -> stringResource(R.string.achievement_first_quiz_desc)
    "quiz_regular" -> stringResource(R.string.achievement_quiz_regular_desc)
    "perfect_score" -> stringResource(R.string.achievement_perfect_score_desc)
    "legendary" -> stringResource(R.string.achievement_legendary_desc)
    else -> ""
}
