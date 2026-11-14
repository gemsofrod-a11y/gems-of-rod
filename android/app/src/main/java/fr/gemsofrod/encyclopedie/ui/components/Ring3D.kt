package fr.gemsofrod.encyclopedie.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject

/**
 * Rendu 3D réaliste de la bague (demande de Sébastien du 14/11/2026,
 * « étape 2 ») : une vraie bague modélisée en 3D par le moteur embarqué
 * `assets/ring3d` (three.js, aucun accès réseau), affichée dans une
 * WebView qu'on fait tourner au doigt. Le même moteur compose la fiche
 * technique 4 vues (dessus, perspective, face, côté) cotée, reprise dans
 * le PDF de simulation. Si WebGL n'est pas disponible sur le téléphone,
 * [onUnavailable] est appelé et l'écran retombe sur l'aperçu 2D.
 */

/** Composition au format attendu par le moteur 3D (voir android/ring3d/src/engine.js). */
fun ringConfigJson(config: RingConfiguration): String {
    fun hex(c: androidx.compose.ui.graphics.Color) = String.format("#%06X", c.toArgb() and 0xFFFFFF)
    val c = config.central
    val json = JSONObject()
        .put("metal", config.metal.name)
        .put("grams", config.gramsMetal)
        .put(
            "center", JSONObject()
                .put("cut", c.cut.name).put("w", c.widthMm).put("h", c.heightMm)
                .put("rot", c.orientationDeg.toDouble()).put("color", hex(c.color)).put("sert", c.sertissage.name)
        )
    val side = config.cote
    if (side.nombre > 0) {
        val s = side.stone
        json.put(
            "side", JSONObject()
                .put("n", side.nombre).put("cut", s.cut.name).put("w", s.widthMm).put("h", s.heightMm)
                .put("color", hex(s.color)).put("sert", s.sertissage.name)
                .put("esp", side.espacement.toDouble()).put("off", side.orientationOffsetDeg.toDouble())
        )
    } else {
        json.put("side", JSONObject.NULL)
    }
    return json.toString()
}

/** Pont JavaScript → app : les rappels arrivent sur un fil du moteur, on les ramène sur le fil principal. */
private class RingBridge(
    private val onReady: () -> Unit,
    private val onSheet: (Bitmap) -> Unit,
    private val onError: (String) -> Unit
) {
    private val main = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun onReady() { main.post { onReady.invoke() } }

    @JavascriptInterface
    fun onSheet(dataUrl: String) {
        val bitmap = runCatching {
            val bytes = Base64.decode(dataUrl.substringAfter(','), Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }.getOrNull()
        main.post { if (bitmap != null) onSheet.invoke(bitmap) else onError.invoke("sheet") }
    }

    @JavascriptInterface
    fun onError(message: String) { main.post { onError.invoke(message) } }
}

/**
 * Bague 3D interactive. [sheetRequest] : quand il change (et n'est pas
 * null), le moteur compose la fiche 4 vues avec ces lignes de texte et la
 * renvoie via [onSheet].
 */
@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
@Composable
fun Ring3DView(
    config: RingConfiguration,
    modifier: Modifier = Modifier,
    sheetRequest: List<String>? = null,
    onSheet: (Bitmap) -> Unit = {},
    onUnavailable: () -> Unit = {}
) {
    var ready by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    val json = remember(config) { ringConfigJson(config) }

    Box(modifier) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    settings.javaScriptEnabled = true
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    webViewClient = WebViewClient()
                    addJavascriptInterface(
                        RingBridge(
                            onReady = { ready = true },
                            onSheet = { onSheet(it) },
                            onError = { onUnavailable() }
                        ),
                        "AndroidRing"
                    )
                    // la bague se fait tourner au doigt : on empêche la page de défiler pendant le geste
                    setOnTouchListener { v, event ->
                        if (event.actionMasked == MotionEvent.ACTION_DOWN) v.parent?.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                    loadUrl("file:///android_asset/ring3d/viewer.html")
                    webView = this
                }
            },
            modifier = Modifier.matchParentSize()
        )
    }

    LaunchedEffect(ready, json) {
        // petit délai : pendant qu'on fait glisser un curseur, on ne reconstruit la bague qu'une fois le geste posé
        delay(90)
        if (ready) webView?.evaluateJavascript("window.ring.setConfig(${JSONObject.quote(json)})", null)
    }
    LaunchedEffect(ready, json, sheetRequest) {
        val lines = sheetRequest ?: return@LaunchedEffect
        if (ready) {
            val linesJson = JSONArray(lines).toString()
            webView?.evaluateJavascript("window.ring.renderSheet(${JSONObject.quote(json)}, ${JSONObject.quote(linesJson)})", null)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            webView?.apply {
                removeJavascriptInterface("AndroidRing")
                destroy()
            }
        }
    }
}
