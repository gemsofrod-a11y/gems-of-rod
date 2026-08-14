package fr.gemsofrod.encyclopedie.ui.screens

import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import kotlinx.coroutines.launch

/** Clé du pays correct dans le GeoJSON embarqué (`legendary_map/leaflet/world_data.js`) : la Birmanie, seul gisement connu de painite au monde. */
private const val LEGENDARY_ANSWER_KEY = "myanmar"

/**
 * Étape finale de l'énigme légendaire : une carte du monde interactive
 * (Leaflet + OpenStreetMap, embarquée dans une WebView) où l'utilisateur
 * doit toucher le pays déduit des indices de [LegendaryRiddleScreen]. Les
 * contours de pays sont embarqués localement (aucune donnée à télécharger
 * pour l'interaction elle-même) ; seul le fond de carte visuel (tuiles
 * OpenStreetMap) nécessite une connexion, contrairement au reste de l'app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendaryMapScreen(
    onGemClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var solved by remember { mutableStateOf(AchievementsRepository.stats().hasSolvedLegendaryRiddle) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val wrongMessage = stringResource(R.string.legendary_wrong_country)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.legendary_map_title)) },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (solved) {
            LegendarySuccess(onGemClick = onGemClick, modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Text(
                    text = stringResource(R.string.legendary_map_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
                AndroidView(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    factory = { context ->
                        WebView(context).apply {
                            val webView = this
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            addJavascriptInterface(
                                object {
                                    @JavascriptInterface
                                    fun onCountrySelected(key: String) {
                                        webView.post {
                                            if (key == LEGENDARY_ANSWER_KEY) {
                                                AchievementsRepository.recordLegendaryRiddleSolved()
                                                solved = true
                                            } else {
                                                scope.launch { snackbarHostState.showSnackbar(wrongMessage) }
                                            }
                                        }
                                    }
                                },
                                "Android"
                            )
                            loadUrl("file:///android_asset/legendary_map/legendary_map.html")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LegendarySuccess(onGemClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.legendary_success_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.legendary_success_body),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)
        )
        Button(onClick = { onGemClick("painite") }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.legendary_success_view_gem_button))
        }
    }
}
