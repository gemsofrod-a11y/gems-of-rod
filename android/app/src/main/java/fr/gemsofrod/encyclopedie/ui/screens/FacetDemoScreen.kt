package fr.gemsofrod.encyclopedie.ui.screens

import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fr.gemsofrod.encyclopedie.R

/**
 * Vidéo explicative de l'atelier (res/raw/facet_demo.mp4, sans son) : l'app elle-même
 * filmée image par image (FacetDemoVideoTest) — coupe au curseur, outils, rendu,
 * liste des pierres, teinte, éclairages. Le sous-titre du passage en cours s'affiche
 * sous la vidéo dans la langue de l'app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacetDemoScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    var player by remember { mutableStateOf<VideoView?>(null) }
    var chapter by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.facet_demo_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back)) }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        setVideoURI(Uri.parse("android.resource://${context.packageName}/${R.raw.facet_demo}"))
                        setOnPreparedListener { mp -> mp.isLooping = true; mp.setVolume(0f, 0f); start() }
                        // une vidéo qui ne s'ouvre pas ne doit jamais faire planter l'atelier
                        setOnErrorListener { _, _, _ -> true }
                    }.also { player = it }
                },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(412f / 915f, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF141D17))
            )
            // sous-titre du passage en cours, dans la langue de l'app
            Text(
                stringResource(CAPTIONS[chapter]),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                minLines = 3,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
    LaunchedEffect(player) {
        while (true) {
            val ms = runCatching { player?.currentPosition ?: 0 }.getOrDefault(0)
            chapter = CHAPTERS_MS.indexOfLast { ms >= it }.coerceAtLeast(0)
            delay(200)
        }
    }
    DisposableEffect(Unit) { onDispose { player?.stopPlayback() } }
}

/** Début de chaque passage de la vidéo (ms), en phase avec FacetDemoVideoTest (12 images/s). */
private val CHAPTERS_MS = listOf(0, 3000, 5500, 21333, 23833, 27833, 32500, 35500)
private val CAPTIONS = listOf(
    R.string.facet_demo_c1, R.string.facet_demo_c2, R.string.facet_demo_c3, R.string.facet_demo_c4,
    R.string.facet_demo_c5, R.string.facet_demo_c6, R.string.facet_demo_c7, R.string.facet_demo_c8
)
