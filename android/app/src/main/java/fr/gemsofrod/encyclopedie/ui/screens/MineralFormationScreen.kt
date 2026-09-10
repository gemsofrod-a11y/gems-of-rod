package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.MineralFormationStone
import fr.gemsofrod.encyclopedie.data.MineralFormationVideos
import fr.gemsofrod.encyclopedie.ui.components.YouTubeEmbedPlayer

/**
 * Une vidéo pédagogique par pierre (diamant, émeraude, saphir & rubis),
 * lue directement dans l'appli, dans la langue d'interface quand une source
 * fiable existe (repli sur l'anglais sinon — voir [MineralFormationVideos]).
 * Contenu tiers hébergé sur YouTube, non produit par Gems of Rod.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineralFormationScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.mineral_formation_title)) },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.mineral_formation_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            MineralFormationCard(
                stone = MineralFormationStone.DIAMANT,
                languageCode = languageCode,
                title = stringResource(R.string.mineral_formation_diamant_title),
                description = stringResource(R.string.mineral_formation_diamant_desc)
            )
            MineralFormationCard(
                stone = MineralFormationStone.EMERAUDE,
                languageCode = languageCode,
                title = stringResource(R.string.mineral_formation_emeraude_title),
                description = stringResource(R.string.mineral_formation_emeraude_desc)
            )
            MineralFormationCard(
                stone = MineralFormationStone.SAPHIR_RUBIS,
                languageCode = languageCode,
                title = stringResource(R.string.mineral_formation_saphir_rubis_title),
                description = stringResource(R.string.mineral_formation_saphir_rubis_desc)
            )

            Text(
                text = stringResource(R.string.mineral_formation_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MineralFormationCard(
    stone: MineralFormationStone,
    languageCode: String,
    title: String,
    description: String
) {
    val video = MineralFormationVideos.video(stone, languageCode)
    val isFallback = !MineralFormationVideos.hasNativeVideo(stone, languageCode)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            YouTubeEmbedPlayer(
                youtubeId = video.youtubeId,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.mineral_formation_video_credit, video.title),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isFallback) {
                Text(
                    text = stringResource(R.string.mineral_formation_fallback_note),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
