package fr.gemsofrod.encyclopedie

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetWheels
import fr.gemsofrod.encyclopedie.ui.screens.FacetDesignScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetLibraryScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetRenderScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetDemoScreen
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream

/**
 * Captures de l'Atelier de facettage rendues par l'app compilée (Robolectric),
 * pour vérifier les écrans avant publication (workflow « Facet screenshots »).
 *
 * Les écrans sont affichés dans la vraie activité de l'app (MainActivity) :
 * createComposeRule() lance une activité de test que Robolectric ne trouve pas
 * dans ce projet multiplateforme (« Unable to resolve activity », la raison
 * pour laquelle le workflow « Screenshots » n'a jamais abouti).
 * La vue 3D (WebView) ne se dessine pas ici : on montre le dessin des facettes,
 * le même que le repli de l'app quand WebGL manque.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w412dp-h1500dp-xxhdpi")
class FacetAtelierScreenshotTest {

    private val outputDir = File("build/outputs/facet-screenshots").apply { mkdirs() }

    private fun capture(name: String, content: @Composable () -> Unit) {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        activity.setContent {
            GemsEncyclopedieTheme { Surface(modifier = Modifier.fillMaxSize(), content = content) }
        }
        repeat(3) { shadowOf(Looper.getMainLooper()).idle() }
        val root: View = activity.window.decorView
        val res = activity.resources.displayMetrics
        val w = res.widthPixels; val h = res.heightPixels
        root.measure(View.MeasureSpec.makeMeasureSpec(w, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY))
        root.layout(0, 0, w, h)
        shadowOf(Looper.getMainLooper()).idle()
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        root.draw(Canvas(bitmap))
        FileOutputStream(File(outputDir, "$name.png")).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun library() = capture("atelier_01_bibliotheque") {
        FacetLibraryScreen(
            designs = listOf(FacetPresets.brilliantRound.copy(name = "Mon brillant"), FacetPresets.blank("Ma taille")),
            onOpenPreset = {}, onOpen = {}, onNew = {}, onBack = {}
        )
    }

    @Test
    fun blankCube() = capture("atelier_02_cube_brut") {
        FacetDesignScreen(FacetPresets.blank("Ma taille"), {}, {}, {}, {}, allow3d = false)
    }

    @Test
    fun brilliant() = capture("atelier_03_brillant") {
        FacetDesignScreen(FacetPresets.brilliantRound, {}, {}, {}, {}, allow3d = false)
    }

    @Test
    fun cushion() = capture("atelier_04_coussin") {
        FacetDesignScreen(FacetPresets.cushionGor, {}, {}, {}, {}, allow3d = false)
    }

    @Test
    fun cutMeetingPoint() = capture("atelier_05_couper_rencontre") {
        val d = FacetPresets.brilliantRound
        FacetDesignScreen(d, {}, {}, {}, {}, allow3d = false, startEditing = d.tiers.indexOfFirst { it.name == "C3" })
    }

    @Test
    fun cutSlider() = capture("atelier_06_couper_curseur") {
        val d = FacetPresets.cushionGor
        val i = d.tiers.indexOfFirst { it.name == "P3" }
        // curseur réglé un peu plus profond que « niveler le rondiste »
        val depth = fr.gemsofrod.encyclopedie.data.FacetGeometry.build(d).depths.getValue(i)
        val slid = d.copy(tiers = d.tiers.mapIndexed { k, t -> if (k == i) t.copy(rule = fr.gemsofrod.encyclopedie.data.FacetRule.VALUE, value = depth - 0.01) else t })
        FacetDesignScreen(slid, {}, {}, {}, {}, allow3d = false, startEditing = i)
    }

    @Test
    fun problems() = capture("atelier_07_alertes") {
        // pavillon sous l'angle critique et facettes recoupées : l'atelier le signale
        val d = FacetPresets.cushionGor
        val bad = d.copy(tiers = d.tiers.map {
            when (it.name) {
                "P1" -> it.copy(angleDeg = 38.0)
                "P3" -> it.copy(indices = FacetWheels.indicesFor(3, 16, 96))
                else -> it
            }
        })
        FacetDesignScreen(bad, {}, {}, {}, {}, allow3d = false)
    }

    @Test
    fun renderDiamond() = capture("atelier_08_rendu_diamant") {
        FacetRenderScreen(FacetPresets.brilliantRound, {}, synchronous = true)
    }

    @Test
    fun renderCitrine() = capture("atelier_09_rendu_citrine") {
        FacetRenderScreen(FacetPresets.cushionGor.copy(material = FacetMaterial.QUARTZ), {}, synchronous = true)
    }

    @Test
    fun renderRuby() = capture("atelier_10_rendu_rubis") {
        FacetRenderScreen(FacetPresets.brilliantRound.copy(material = FacetMaterial.RUBIS), {}, synchronous = true)
    }

    @Test
    fun demo() = capture("atelier_11_demo") {
        // la vidéo elle-même ne se lit pas sous Robolectric : on vérifie la page et ses textes
        FacetDemoScreen {}
    }
}
