package fr.gemsofrod.encyclopedie.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.MineralFormationStone
import fr.gemsofrod.encyclopedie.data.MineralFormationVideos
import fr.gemsofrod.encyclopedie.ui.components.YouTubeEmbedPlayer

/**
 * Ouvre la vidéo dans l'app YouTube si elle est installée, sinon dans le
 * navigateur. Essaie d'abord le schéma `vnd.youtube:` (ouverture directe
 * dans l'app), puis retombe sur l'URL web ; n'importe quel échec de
 * résolution est absorbé plutôt que de faire planter l'appli — un identifiant
 * de vidéo supprimée ou invalide ne doit jamais provoquer de crash.
 */
private fun openYoutubeExternally(context: Context, youtubeId: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeId")))
        return
    } catch (_: ActivityNotFoundException) {
        // Pas d'app YouTube installée : on retombe sur le navigateur.
    } catch (_: Exception) {
        // Ignore toute autre erreur de résolution et retombe aussi sur le navigateur.
    }
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$youtubeId")))
    } catch (_: Exception) {
        // Aucune app ne peut ouvrir de lien web : rien de plus à faire côté appli.
    }
}

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
    val context = LocalContext.current
    val video = MineralFormationVideos.video(stone, languageCode)
    val isFallback = !MineralFormationVideos.hasNativeVideo(stone, languageCode)
    var playbackUnavailable by rememberSaveable(video.youtubeId) { mutableStateOf(false) }

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
            if (playbackUnavailable) {
                VideoUnavailableFallback(
                    youtubeId = video.youtubeId,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                YouTubeEmbedPlayer(
                    youtubeId = video.youtubeId,
                    onUnavailable = { playbackUnavailable = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = stringResource(R.string.mineral_formation_video_credit, video.title),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!playbackUnavailable) {
                TextButton(
                    onClick = { openYoutubeExternally(context, video.youtubeId) },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(stringResource(R.string.mineral_formation_watch_on_youtube))
                }
            }
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

/**
 * Carte de repli affichée à la place du lecteur quand YouTube signale que la
 * vidéo ne peut pas être jouée en intégration (intégration désactivée par
 * son auteur, la cause la plus fréquente) — plutôt que de laisser YouTube
 * afficher sa propre carte de repli, au fond blanc détonnant dans notre
 * thème sombre.
 */
@Composable
private fun VideoUnavailableFallback(youtubeId: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                Icons.Filled.OndemandVideo,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.mineral_formation_video_unavailable),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(onClick = { openYoutubeExternally(context, youtubeId) }) {
                Text(stringResource(R.string.mineral_formation_watch_on_youtube))
            }
        }
    }
}
