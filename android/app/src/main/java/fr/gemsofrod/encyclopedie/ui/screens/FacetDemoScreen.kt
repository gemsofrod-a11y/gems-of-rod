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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fr.gemsofrod.encyclopedie.R

/**
 * Petite vidéo de démonstration de l'atelier (res/raw/facet_demo.mp4, sans son ni
 * texte : les explications sont dans la langue de l'app, sous la vidéo). Elle est
 * calculée avec le vrai moteur de rendu de l'atelier (FacetRender) : un cube brut
 * taillé passe par passe en brillant, le feu du diamant, puis plusieurs pierres.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacetDemoScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    var player by remember { mutableStateOf<VideoView?>(null) }
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
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(12.dp)
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
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF141D17))
            )
            Text(
                stringResource(R.string.facet_demo_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
    DisposableEffect(Unit) { onDispose { player?.stopPlayback() } }
}
