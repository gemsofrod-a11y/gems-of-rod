package fr.gemsofrod.encyclopedie

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetRenderSettings
import fr.gemsofrod.encyclopedie.ui.screens.AnalyseScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetDesignScreen
import fr.gemsofrod.encyclopedie.ui.screens.FacetRenderScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemComparisonScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GlossaryScreen
import fr.gemsofrod.encyclopedie.ui.screens.HomeScreen
import fr.gemsofrod.encyclopedie.ui.screens.QuizScreen
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
import fr.gemsofrod.encyclopedie.ui.theme.GorBodyFont
import fr.gemsofrod.encyclopedie.ui.theme.GorDisplayFont
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream

/**
 * Captures « vitrine » pour la fiche Play Store : l'écran réel de l'app, rendu
 * par l'app compilée (Robolectric, dans MainActivity), posé dans un cadre aux
 * couleurs de la maison avec un titre. 1440 × 2560 px (9:16), dans les limites
 * du Play Store (320 à 3840 px, côté long au plus 2× le court).
 * Workflow « Store screenshots » → branche store-screenshots.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xxxhdpi")
class StoreScreenshotTest {

    private val out = File("build/outputs/store-screenshots").apply { mkdirs() }

    private val Night = Color(0xFF141D17)
    private val Forest = Color(0xFF1E2E24)
    private val Gold = Color(0xFFD6B872)
    private val Mist = Color(0xFFB9C9BD)

    /** Écran de l'app à sa taille réelle (412 × 820 dp), réduit dans un cadre de téléphone. */
    @Composable
    private fun Phone(content: @Composable () -> Unit) {
        val scale = 0.66f
        Box(
            Modifier
                .size((412 * scale).dp + 12.dp, (820 * scale).dp + 12.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF0B100D))
                .border(1.5.dp, Gold.copy(alpha = 0.55f), RoundedCornerShape(30.dp))
                .padding(6.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.TopStart
        ) {
            Box(
                Modifier
                    .wrapContentSize(Alignment.TopStart, unbounded = true)
                    .requiredSize(412.dp, 820.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale, transformOrigin = TransformOrigin(0f, 0f))
            ) {
                GemsEncyclopedieTheme { Surface(Modifier.fillMaxSize(), content = content) }
            }
        }
    }

    private fun shot(name: String, title: String, subtitle: String, content: @Composable () -> Unit) {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        activity.setContent {
            key(name) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Forest, Night)))
                        .padding(horizontal = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(30.dp))
                    Text("GEMS OF ROD", color = Gold.copy(alpha = 0.85f), fontFamily = GorBodyFont, fontSize = 10.sp, letterSpacing = 3.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        title, color = Color(0xFFF4EBD6), fontFamily = GorDisplayFont, fontSize = 27.sp,
                        lineHeight = 31.sp, textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.width(36.dp).height(1.dp).background(Gold))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        subtitle, color = Mist, fontFamily = GorBodyFont, fontSize = 12.5.sp,
                        lineHeight = 17.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(18.dp))
                    // le téléphone déborde en bas de l'image, comme sur une vitrine
                    Box(Modifier.fillMaxWidth().weight(1f).wrapContentSize(Alignment.TopCenter, unbounded = true)) { Phone(content) }
                }
            }
        }
        repeat(3) { shadowOf(Looper.getMainLooper()).idle() }
        val root: View = activity.window.decorView
        val dm = activity.resources.displayMetrics
        root.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY))
        root.layout(0, 0, dm.widthPixels, dm.heightPixels)
        repeat(2) { shadowOf(Looper.getMainLooper()).idle() }
        val bmp = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888)
        root.draw(Canvas(bmp))
        FileOutputStream(File(out, "$name.png")).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun s1_home() = shot("01_accueil", "L'encyclopédie\ndes pierres précieuses", "Gemmes, minéraux, météorites, fossiles : tout l'univers des pierres, hors ligne.") {
        HomeScreen(
            onGemmologieClick = {}, onLanguageClick = {}, onThemeClick = {}, onFavoritesClick = {},
            onMeteoritesClick = {}, onFossilesClick = {}, onCoquillagesClick = {}, onGemmesOrganiquesClick = {},
            onLithotherapieClick = {}, onLearningPathClick = {}, onAchievementsClick = {}, onLabClick = {},
            onLapidaireClick = {}, onFacetAtelierClick = {}, onSearchClick = {}
        )
    }

    @Test
    fun s2_gem() = shot("02_fiche_rubis", "Des fiches\nde gemmologue", "Propriétés, origines, traitements et conseils d'achat, pierre par pierre.") {
        GemDetailScreen(gemId = "rubis", onBackClick = {}, onCertificateClick = {})
    }

    @Test
    fun s3_atelier() = shot("03_atelier_facettage", "Concevez\nvos tailles", "L'atelier de facettage : un cube brut taillé passe par passe, instructions écrites pour vous.") {
        FacetDesignScreen(FacetPresets.brilliantRound, {}, {}, {}, {}, allow3d = false)
    }

    @Test
    fun s4_render() = shot("04_rendu_realiste", "Voyez la lumière\navant de tailler", "Rendu réaliste par lancer de rayons : reflets, feu et couleur de 49 pierres facettables.") {
        FacetRenderScreen(
            FacetPresets.brilliantRound.copy(material = FacetMaterial.RUBIS), {}, synchronous = true,
            initial = FacetRenderSettings.forMaterial(FacetMaterial.RUBIS).copy(tiltX = -12.0), syncSize = 520, syncSamples = 2
        )
    }

    @Test
    fun s5_compare() = shot("05_comparer", "Comparez\ndeux pierres", "Rubis ou saphir ? Indices, densités et propriétés côte à côte.") {
        GemComparisonScreen(onBackClick = {}, initialGemAId = "rubis", initialGemBId = "saphir-bleu")
    }

    @Test
    fun s6_analyse() = shot("06_analyse", "Identifiez\nune pierre", "Instruments, mesures et méthode pas à pas du gemmologue.") {
        AnalyseScreen(onGemClick = {}, onInstrumentsClick = {}, onGlossaireClick = {}, onBackClick = {})
    }

    @Test
    fun s7_quiz() = shot("07_quiz", "Apprenez\nen jouant", "Quiz, parcours d'apprentissage et trophées pour progresser.") {
        QuizScreen(onBackClick = {})
    }

    @Test
    fun s8_lexique() = shot("08_lexique", "Le vocabulaire\ndu métier", "Un lexique de gemmologie et de lapidairerie, en 9 langues.") {
        GlossaryScreen(onBackClick = {})
    }
}
