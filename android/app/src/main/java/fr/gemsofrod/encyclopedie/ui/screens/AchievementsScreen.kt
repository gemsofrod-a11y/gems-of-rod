package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Achievements
import fr.gemsofrod.encyclopedie.data.AchievementStats
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import fr.gemsofrod.encyclopedie.data.Badge
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemsRepository

/**
 * Liste des succès à débloquer en explorant le catalogue, en ajoutant des
 * favoris et en jouant au quiz gemmologique. Purement local (aucun compte,
 * aucun classement) : la progression sert à donner envie d'explorer
 * l'app, pas à comparer les utilisateurs entre eux.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(onBackClick: () -> Unit) {
    val stats = AchievementsRepository.stats()
    val unlockedCount = Achievements.BADGES.count { it.isUnlocked(stats) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.achievements_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.achievements_progress_summary, unlockedCount, Achievements.BADGES.size),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Achievements.BADGES.forEach { badge -> BadgeCard(badge, stats) }
        }
    }
}

@Composable
private fun BadgeCard(badge: Badge, stats: AchievementStats) {
    val unlocked = badge.isUnlocked(stats)
    val progress = badge.progress(stats).coerceAtMost(badge.threshold)
    val containerColor = if (unlocked) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (unlocked) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = badge.emoji,
                fontSize = 32.sp,
                modifier = Modifier.alpha(if (unlocked) 1f else 0.35f)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badgeTitle(badge),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
                Text(
                    text = badgeDescription(badge),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (unlocked) contentColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!unlocked) {
                    Text(
                        text = stringResource(R.string.achievement_progress_format, progress, badge.threshold),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (unlocked) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = contentColor)
            } else {
                Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun badgeTitle(badge: Badge): String = when (badge.id) {
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
    else -> badge.id
}

@Composable
private fun badgeDescription(badge: Badge): String = when (badge.id) {
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
    else -> ""
}
