package fr.gemsofrod.encyclopedie.ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Lecteur vidéo intégré à l'application, sans quitter l'écran ni ouvrir
 * l'app YouTube : charge le lecteur officiel YouTube (IFrame Player API)
 * dans une WebView au format 16:9. La lecture démarre au geste de
 * l'utilisateur (pas d'autoplay), pour ne pas déclencher de trafic de
 * données à l'ouverture de l'écran.
 *
 * Utilise l'API JS officielle (`iframe_api`), et pas une simple balise
 * `<iframe src="…/embed/…">`, pour deux raisons :
 * - `loadDataWithBaseURL` avec une origine à https://www.youtube.com est
 *   nécessaire dans les deux cas (sinon erreur YouTube 153, origine non
 *   reconnue par une WebView chargée sans page hôte) ;
 * - l'API expose un évènement `onError` que la balise `<iframe>` seule
 *   n'expose pas : certaines vidéos tierces ont l'intégration désactivée
 *   par leur auteur, et YouTube affiche alors sa propre carte de repli
 *   (fond blanc, bouton « Ouvrir l'app ») au lieu de jouer la vidéo — un
 *   rendu qui détonne dans notre thème sombre. En écoutant `onError`, on
 *   bascule vers [onUnavailable] pour afficher notre propre carte de
 *   repli, cohérente avec le reste de l'appli, plutôt que celle de
 *   YouTube.
 *
 * Certaines restrictions (région, âge, vidéo privée…) ne déclenchent pas
 * toujours `onError` et laissent le lecteur silencieusement vide (rectangle
 * noir figé) : un minuteur JS déclenche le même repli si ni `onReady` ni
 * `onError` ne se sont produits après quelques secondes.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeEmbedPlayer(
    youtubeId: String,
    onUnavailable: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnUnavailable = rememberUpdatedState(onUnavailable)

    AndroidView(
        modifier = modifier
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = true
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                setBackgroundColor(android.graphics.Color.BLACK)
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onPlayerError() {
                            post { currentOnUnavailable.value() }
                        }
                    },
                    "GemsOfRodPlayerBridge"
                )
            }
        },
        update = { webView ->
            if (webView.tag != youtubeId) {
                webView.tag = youtubeId
                val html = """
                    <html><body style="margin:0;padding:0;background:#000;">
                    <div id="player" style="width:100%;height:100%;"></div>
                    <script src="https://www.youtube.com/iframe_api"></script>
                    <script>
                        var player;
                        var settled = false;
                        function reportUnavailable() {
                            if (!settled) {
                                settled = true;
                                GemsOfRodPlayerBridge.onPlayerError();
                            }
                        }
                        var readyTimeout = setTimeout(reportUnavailable, 7000);
                        function onYouTubeIframeAPIReady() {
                            player = new YT.Player('player', {
                                width: '100%',
                                height: '100%',
                                videoId: '$youtubeId',
                                playerVars: { rel: 0, modestbranding: 1, playsinline: 1 },
                                events: {
                                    'onReady': function(e) { settled = true; clearTimeout(readyTimeout); },
                                    'onError': function(e) { clearTimeout(readyTimeout); reportUnavailable(); }
                                }
                            });
                        }
                    </script>
                    </body></html>
                """.trimIndent()
                webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
            }
        }
    )
}
