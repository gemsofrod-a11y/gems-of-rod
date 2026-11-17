package fr.gemsofrod.encyclopedie

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.encyclopedie.data.FacetDesign
import fr.gemsofrod.encyclopedie.data.FacetGeometry
import fr.gemsofrod.encyclopedie.data.FacetLighting
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetRenderSettings
import fr.gemsofrod.encyclopedie.data.FacetRule
import fr.gemsofrod.encyclopedie.data.FacetSide
import fr.gemsofrod.encyclopedie.ui.screens.FacetDesignScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetLibraryScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetRenderScreen
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
import org.junit.Assume
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Vidéo explicative de l'atelier (bouton « Démo ») filmée image par image dans
 * l'app compilée : écrans réels, curseurs qui bougent, doigt qui touche les
 * boutons. 12 images/s ; le workflow « Facet demo video » les assemble en MP4.
 * Lancé seulement quand FACET_VIDEO=1 (long : plusieurs centaines d'images).
 *
 * Chapitres (début en images, à garder en phase avec FacetDemoScreen.CHAPTERS_MS) :
 * bibliothèque 0, cube brut 36, coupe 66, outils 256, inclinaison 286,
 * pierres 334, teinte 390, éclairage 426, fin 482 (total 494).
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w412dp-h915dp-xhdpi")
class FacetDemoVideoTest {

    private val out = File("build/outputs/facet-video").apply { mkdirs() }
    private var n = 0

    /** Doigt : position en dp dans la fenêtre, appui de 0 (survol) à 1 (touché). */
    private data class Finger(val x: Float, val y: Float, val press: Float = 0f)

    private lateinit var activity: MainActivity

    private fun frame(finger: Finger? = null, content: @Composable () -> Unit) {
        activity.setContent {
            GemsEncyclopedieTheme {
                Box(Modifier.fillMaxSize()) {
                    Surface(modifier = Modifier.fillMaxSize(), content = content)
                    if (finger != null) androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                        val c = Offset(finger.x.dp.toPx(), finger.y.dp.toPx())
                        val r = 17.dp.toPx()
                        if (finger.press > 0f) drawCircle(Color(0xFFD6B872).copy(alpha = 0.5f * (1 - finger.press)), radius = r * (1 + 1.4f * finger.press), center = c, style = Stroke(3.dp.toPx()))
                        drawCircle(Color.White.copy(alpha = 0.30f + 0.25f * finger.press), radius = r * (1 - 0.15f * finger.press), center = c)
                        drawCircle(Color(0xFF1E2E24).copy(alpha = 0.75f), radius = r * (1 - 0.15f * finger.press), center = c, style = Stroke(2.dp.toPx()))
                    }
                }
            }
        }
        repeat(3) { shadowOf(Looper.getMainLooper()).idle() }
        val root: View = activity.window.decorView
        val dm = activity.resources.displayMetrics
        root.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY))
        root.layout(0, 0, dm.widthPixels, dm.heightPixels)
        shadowOf(Looper.getMainLooper()).idle()
        val bmp = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888)
        root.draw(Canvas(bmp))
        FileOutputStream(File(out, "f%04d.png".format(n++))).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    private fun ease(t: Double) = 0.5 - 0.5 * cos(PI * t.coerceIn(0.0, 1.0))
    private fun lerp(a: Float, b: Float, t: Double) = a + (b - a) * t.toFloat()

    /** Approche du doigt puis appui (dernières images). */
    private fun tap(from: Finger, to: Finger, frames: Int, i: Int): Finger {
        val move = frames - 5
        return if (i < move) Finger(lerp(from.x, to.x, ease(i / move.toDouble())), lerp(from.y, to.y, ease(i / move.toDouble())))
        else Finger(to.x, to.y, ((i - move + 1) / 5f).coerceAtMost(1f))
    }

    private fun design(d: FacetDesign, startEditing: Int? = null, f: Finger? = null) = frame(f) {
        FacetDesignScreen(d, {}, {}, {}, {}, allow3d = false, startEditing = startEditing)
    }

    private fun render(d: FacetDesign, s: FacetRenderSettings, f: Finger? = null, scroll: Int = 0) = frame(f) {
        FacetRenderScreen(d, {}, synchronous = true, initial = s, syncSize = 400, syncSamples = 2, initialScroll = scroll)
    }

    // repères mesurés sur les captures (dp) : curseurs des réglages du rendu
    private fun sliderX(frac: Double) = (136 + 193 * frac).toFloat()
    private fun hueX(h: Double) = (135 + 173 * h / 360).toFloat()
    private val depthY = 432f
    private fun depthX(cut: Double, span: Double) = (74 + 213 * cut / span).toFloat()
    private val toolY = 887f
    private val rest = Finger(300f, 760f)

    private fun span(side: FacetSide) = when (side) { FacetSide.PAVILION -> 3.2; FacetSide.GIRDLE -> 1.65; FacetSide.CROWN -> 3.4 }
    private fun rangeEnd(side: FacetSide) = when (side) { FacetSide.PAVILION -> 2.6; FacetSide.GIRDLE -> 1.75; FacetSide.CROWN -> 3.4 }

    @Test
    fun video() {
        Assume.assumeTrue(System.getenv("FACET_VIDEO") == "1")
        activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        val bril = FacetPresets.brilliantRound

        // 1. bibliothèque → « Nouvelle taille » (36)
        for (i in 0 until 36) frame(if (i >= 10) tap(rest, Finger(106f, 514f), 26, i - 10) else null) {
            FacetLibraryScreen(designs = emptyList(), onOpenPreset = {}, onOpen = {}, onNew = {}, onBack = {})
        }
        // 2. cube brut → outil « Couper » (30)
        val blank = FacetPresets.blank("Ma taille")
        for (i in 0 until 30) design(blank, f = if (i >= 8) tap(rest, Finger(377f, toolY), 22, i - 8) else null)
        // 3. coupe passe par passe, le curseur de profondeur en mouvement (190)
        bril.tiers.forEachIndexed { k, tier ->
            val sub = bril.copy(tiers = bril.tiers.take(k + 1))
            val depth = FacetGeometry.build(sub).depths.getValue(k)
            val end = rangeEnd(tier.side); val sp = span(tier.side)
            val finalCut = end - depth; val startCut = finalCut * 0.35
            val anim = if (k == 0) 24 else 14
            for (i in 0 until anim) {
                val cut = startCut + (finalCut - startCut) * ease(i / (anim - 1.0))
                val d = sub.copy(tiers = sub.tiers.mapIndexed { j, t -> if (j == k) t.copy(rule = FacetRule.VALUE, value = end - cut) else t })
                design(d, k, Finger(depthX(cut, sp), depthY, if (i < 2) 0.6f else 1f))
            }
            for (i in 0 until 6) design(sub, k)
        }
        // 4. pierre finie → outil « Rendu » (30)
        for (i in 0 until 30) design(bril, f = if (i >= 6) tap(rest, Finger(308f, toolY), 24, i - 6) else null)
        // 5. rendu : inclinaison avant / arrière au curseur (48)
        val dia = FacetRenderSettings.forMaterial(FacetMaterial.DIAMANT)
        for (i in 0 until 48) {
            val t = i / 47.0
            val tilt = when { t < 0.4 -> -28 * ease(t / 0.4); t < 0.8 -> -28 + 40 * ease((t - 0.4) / 0.4); else -> 12 - 12 * ease((t - 0.8) / 0.2) }
            render(bril, dia.copy(tiltX = tilt), Finger(sliderX((tilt + 40) / 80), 873f, 1f))
        }
        // 6. liste des pierres : diamant → rubis, saphir, émeraude, tanzanite (56)
        for (m in listOf(FacetMaterial.RUBIS, FacetMaterial.CORINDON, FacetMaterial.EMERAUDE, FacetMaterial.TANZANITE)) {
            val s = FacetRenderSettings.forMaterial(m).copy(tiltX = -10.0)
            for (i in 0 until 14) {
                val f = if (i < 4) Finger(300f, 541f, (i + 1) / 4f) else null
                render(bril.copy(material = m), s, f)
            }
        }
        // 7. teinte au curseur, sur le saphir (36)
        val saphir = bril.copy(material = FacetMaterial.CORINDON)
        val base = FacetRenderSettings.forMaterial(FacetMaterial.CORINDON).copy(tiltX = -10.0)
        for (i in 0 until 36) {
            val t = i / 35.0
            val hue = if (t < 0.5) 222 + 123 * ease(t / 0.5) else 345 - 205 * ease((t - 0.5) / 0.5)
            render(saphir, base.copy(hue = hue), Finger(hueX(hue), 681f, 1f))
        }
        // 8. éclairages : défilement puis soleil, lumière douce, studio (56)
        val emerald = base.copy(hue = 140.0)
        for (i in 0 until 8) render(saphir, emerald, scroll = (ease(i / 7.0) * 160 * 2).toInt())
        val chipsY = 991f - 160f
        for ((light, x) in listOf(FacetLighting.SUN to 141f, FacetLighting.SOFT to 247f, FacetLighting.STUDIO to 62f)) {
            for (i in 0 until 16) {
                val f = if (i < 4) Finger(x, chipsY, (i + 1) / 4f) else null
                render(saphir, emerald.copy(lighting = light), f, scroll = 320)
            }
        }
        // fin (12)
        for (i in 0 until 12) render(saphir, emerald, scroll = 320)
        File(out, "frames.txt").writeText("$n\n")
    }
}
