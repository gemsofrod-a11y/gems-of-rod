package fr.gemsofrod.encyclopedie.ui.components

import android.webkit.WebView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.viewinterop.AndroidView
import fr.gemsofrod.encyclopedie.data.FacetDesign
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetSide
import fr.gemsofrod.encyclopedie.data.FacetStone
import org.json.JSONObject
import kotlin.math.max

/**
 * Atelier de facettage : la pierre calculée par FacetGeometry, affichée par
 * le moteur 3D embarqué (même WebView que la bague, aucun accès réseau).
 * [meshJson] vient de FacetGeometry.meshJson. Si WebGL manque, [onUnavailable].
 */
@Composable
fun FacetStone3DView(
    meshJson: String,
    material: FacetMaterial,
    modifier: Modifier = Modifier,
    onUnavailable: () -> Unit = {}
) {
    var ready by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    val payload = remember(meshJson, material) {
        // on ajoute la couleur de la matière au JSON de géométrie
        meshJson.dropLast(1) + ",\"color\":\"" + facetMaterialColor(material) + "\"}"
    }

    Box(modifier) {
        AndroidView(
            factory = { context ->
                createEngineWebView(context, onReady = { ready = true }, onError = { onUnavailable() })
                    .also { webView = it }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
    // pas de délai : la pierre suit le curseur de profondeur en direct
    LaunchedEffect(ready, payload) {
        if (ready) webView?.evaluateJavascript("window.ring.setStone(${JSONObject.quote(payload)})", null)
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

/** Teinte de rendu par matière (le blanc donne le rendu « diamant » du moteur). */
fun facetMaterialColor(material: FacetMaterial): String = when (material) {
    FacetMaterial.CORINDON -> "#2F5DB8"
    FacetMaterial.GRENAT -> "#8E1F2E"
    FacetMaterial.SPINELLE -> "#C23A5A"
    FacetMaterial.PERIDOT -> "#8DB33A"
    FacetMaterial.TANZANITE -> "#5B4BB0"
    FacetMaterial.TOURMALINE -> "#2E8A62"
    FacetMaterial.TOPAZE -> "#7FB6DA"
    FacetMaterial.BERYL -> "#7EC8D2"
    FacetMaterial.QUARTZ -> "#9A6FC4"
    FacetMaterial.CHRYSOBERYL -> "#A9B53C"
    FacetMaterial.ZIRCON -> "#5BB7C9"
    else -> "#FFFFFF"
}

/**
 * Dessin des facettes vu de dessus (couronne) ou de dessous (pavillon,
 * vu en miroir comme on le regarde sur la machine). Les facettes de la passe
 * [highlightTier] sont remplies de [highlight]. Sert d'aperçu dans la saisie
 * manuelle et de repli quand la 3D est indisponible.
 */
@Composable
fun FacetDiagram(
    design: FacetDesign,
    stone: FacetStone,
    side: FacetSide,
    modifier: Modifier = Modifier,
    highlightTier: Int? = null,
    line: Color = Color(0xFFB9C9BD),
    highlight: Color = Color(0xFFD6B872),
    background: Color = Color(0xFF1E2E24)
) {
    Canvas(modifier) {
        val faces = stone.faces.filter { f ->
            val t = f.plane?.let { design.tiers.getOrNull(it.tier) } ?: return@filter false
            if (side == FacetSide.CROWN) t.side == FacetSide.CROWN else t.side == FacetSide.PAVILION
        }
        val pts = stone.faces.flatMap { it.points }
        if (pts.isEmpty()) return@Canvas
        val ext = max(pts.maxOf { kotlin.math.abs(it.x) }, pts.maxOf { kotlin.math.abs(it.z) })
        val r = size.minDimension / 2f * 0.94f
        val k = (r / ext).toFloat()
        val c = Offset(size.width / 2f, size.height / 2f)
        drawCircle(background, radius = size.minDimension / 2f, center = c)
        for (f in faces) {
            val path = Path()
            f.points.forEachIndexed { i, p ->
                val x = c.x + p.x.toFloat() * k
                val y = c.y + (if (side == FacetSide.CROWN) p.z else -p.z).toFloat() * k
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            if (highlightTier != null && f.plane?.tier == highlightTier) drawPath(path, highlight)
            drawPath(path, line, style = Stroke(width = 1.4f))
        }
    }
}
