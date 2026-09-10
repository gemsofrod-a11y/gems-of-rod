package fr.gemsofrod.encyclopedie.ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Lecteur vidéo intégré à l'application, sans quitter l'écran ni ouvrir
 * l'app YouTube : charge le lecteur officiel YouTube (iframe embed) dans une
 * WebView au format 16:9. La lecture démarre au geste de l'utilisateur
 * (pas d'autoplay), pour ne pas déclencher de trafic de données à
 * l'ouverture de l'écran.
 *
 * L'iframe est chargée via une page HTML minimale avec `loadDataWithBaseURL`
 * en fixant l'origine à https://www.youtube.com : charger l'URL d'embed
 * directement (`loadUrl`) fait échouer le lecteur avec l'erreur YouTube 153
 * (origine non reconnue), car la WebView n'a alors aucune origine web valide.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeEmbedPlayer(youtubeId: String, modifier: Modifier = Modifier) {
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
            }
        },
        update = { webView ->
            if (webView.tag != youtubeId) {
                webView.tag = youtubeId
                val html = """
                    <html><body style="margin:0;padding:0;background:#000;">
                    <iframe width="100%" height="100%"
                        src="https://www.youtube.com/embed/$youtubeId?rel=0&modestbranding=1&playsinline=1"
                        frameborder="0"
                        allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen></iframe>
                    </body></html>
                """.trimIndent()
                webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
            }
        }
    )
}
