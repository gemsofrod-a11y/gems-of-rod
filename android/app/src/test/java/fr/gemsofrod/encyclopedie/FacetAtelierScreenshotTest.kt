package fr.gemsofrod.encyclopedie

import android.graphics.Bitmap
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetWheels
import fr.gemsofrod.encyclopedie.ui.screens.FacetDesignScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetLibraryScreen
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream

/**
 * Captures de l'Atelier de facettage rendues par l'app compilée (Robolectric),
 * pour vérifier les écrans avant publication (workflow « Facet screenshots »).
 * La vue 3D (WebView) ne se dessine pas ici : on montre le dessin des facettes,
 * le même que le repli de l'app quand WebGL manque.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w412dp-h1600dp-xxhdpi")
class FacetAtelierScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val outputDir = File("build/outputs/facet-screenshots").apply { mkdirs() }

    private fun capture(name: String, content: @Composable () -> Unit) {
        composeTestRule.setContent {
            GemsEncyclopedieTheme { Surface(modifier = Modifier.fillMaxSize(), content = content) }
        }
        shadowOf(Looper.getMainLooper()).idle()
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
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
        val slid = d.copy(tiers = d.tiers.mapIndexed { k, t -> if (k == i) t.copy(rule = fr.gemsofrod.encyclopedie.data.FacetRule.VALUE, value = 0.43) else t })
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
}
