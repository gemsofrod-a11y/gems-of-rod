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
import fr.gemsofrod.encyclopedie.ui.screens.AnalyseScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemComparisonScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GlossaryScreen
import fr.gemsofrod.encyclopedie.ui.screens.HomeScreen
import fr.gemsofrod.encyclopedie.ui.screens.QuizScreen
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
 * Génère des captures d'écran des principaux écrans de l'app pour la fiche
 * Play Store, sans dépendre d'un émulateur : rendu Compose sur JVM via
 * Robolectric en mode graphique natif, capturé pixel par pixel. Chaque écran
 * est rendu directement avec des callbacks neutres plutôt que via une
 * navigation réelle, pour rester indépendant du reste du graphe de nav.
 *
 * Les PNG produits sont écrits dans build/outputs/screenshots/ et
 * récupérés comme artifact par le workflow "Screenshots".
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w412dp-h892dp-xxhdpi")
class ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val outputDir = File("build/outputs/screenshots").apply { mkdirs() }

    private fun capture(name: String, content: @Composable () -> Unit) {
        composeTestRule.setContent {
            GemsEncyclopedieTheme {
                Surface(modifier = Modifier.fillMaxSize(), content = content)
            }
        }
        // `waitForIdle()` s'appuie sur un mécanisme d'idling-resource qui
        // reste bloqué sous Robolectric (LooperMode.PAUSED par défaut,
        // aucun pompage automatique de la file d'attente du looper) :
        // `setContent` a déjà réalisé la composition/mesure/dessin initiaux
        // de façon synchrone, il suffit de vider les tâches en attente
        // (ripple, focus...) via le looper Robolectric directement.
        shadowOf(Looper.getMainLooper()).idle()
        val bitmap = composeTestRule.onRoot().captureToImage().asAndroidBitmap()
        FileOutputStream(File(outputDir, "$name.png")).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }

    @Test
    fun home() = capture("01_accueil") {
        HomeScreen(
            onGemmologieClick = {},
            onFamillesClick = {},
            onLithotherapieClick = {},
            onLanguageClick = {},
            onFavoritesClick = {},
            onMeteoritesClick = {},
            onFossilesClick = {},
            onCoquillagesClick = {},
            onQuizClick = {},
            onAchievementsClick = {},
            onLabClick = {}
        )
    }

    @Test
    fun gemDetail() = capture("02_fiche_gemme") {
        GemDetailScreen(gemId = "rubis", onBackClick = {}, onCertificateClick = {})
    }

    @Test
    fun analyse() = capture("03_analyse") {
        AnalyseScreen(onGemClick = {}, onInstrumentsClick = {}, onGlossaireClick = {}, onBackClick = {})
    }

    @Test
    fun quiz() = capture("04_quiz") {
        QuizScreen(onBackClick = {})
    }

    @Test
    fun glossaire() = capture("05_lexique") {
        GlossaryScreen(onBackClick = {})
    }

    @Test
    fun comparer() = capture("06_comparer") {
        GemComparisonScreen(onBackClick = {}, initialGemAId = "rubis", initialGemBId = "saphir-bleu")
    }
}
