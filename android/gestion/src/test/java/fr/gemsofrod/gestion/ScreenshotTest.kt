package fr.gemsofrod.gestion

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.gestion.data.SampleData
import fr.gemsofrod.gestion.ui.ClientsScreen
import fr.gemsofrod.gestion.ui.DashboardScreen
import fr.gemsofrod.gestion.ui.GestionTheme
import fr.gemsofrod.gestion.ui.OrderDetail
import fr.gemsofrod.gestion.ui.OrderEditor
import fr.gemsofrod.gestion.ui.OrdersScreen
import fr.gemsofrod.gestion.ui.StockScreen
import fr.gemsofrod.gestion.ui.SumUpScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import java.io.FileOutputStream

/**
 * Captures d'écran de l'app avec le jeu d'exemple, sans émulateur (rendu
 * Compose sur JVM via Robolectric, même méthode que l'encyclopédie).
 * Les PNG sont écrits dans gestion/screenshots/ par le workflow
 * « Gestion screenshots ».
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w412dp-h892dp-xxhdpi")
class ScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val outputDir = File("build/outputs/screenshots").apply { mkdirs() }
    private val data = SampleData.build()

    private fun capture(name: String, content: @Composable () -> Unit) {
        composeTestRule.setContent { GestionTheme(content) }
        shadowOf(Looper.getMainLooper()).idle()
        // captureToImage() attend un rendu matériel qui n'arrive jamais sous
        // Robolectric (délai dépassé) : on dessine la vue racine nous-mêmes
        // dans un bitmap, en rendu logiciel.
        val root = composeTestRule.activity.window.decorView.rootView
        val bitmap = Bitmap.createBitmap(root.width, root.height, Bitmap.Config.ARGB_8888)
        root.draw(Canvas(bitmap))
        FileOutputStream(File(outputDir, "$name.png")).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }

    @Composable
    private fun Dashboard() = AppFrame(Tab.ACCUEIL, {}) {
        DashboardScreen(data, {}, {}, {}, {}, {}, {})
    }

    @Test
    fun apercu() = capture("01_apercu") { Dashboard() }

    /** Tout le tableau de bord d'un coup, sur un écran très haut. */
    @Test
    @Config(qualifiers = "w412dp-h2600dp-xxhdpi")
    fun apercuComplet() = capture("02_apercu_complet") { Dashboard() }

    @Test
    fun commandes() = capture("03_commandes") {
        AppFrame(Tab.COMMANDES, {}) { OrdersScreen(data, {}, {}) }
    }

    @Test
    fun ficheCommande() = capture("04_fiche_commande") {
        val order = data.orders.first { it.isUnpaid && it.lines.size > 1 }
        OrderDetail(order, data, {}, {}, {}, {})
    }

    @Test
    fun nouvelleCommande() = capture("05_saisie_commande") {
        OrderEditor(data.orders.first { it.isUnpaid }, data, {}, {}, {})
    }

    @Test
    fun stock() = capture("06_stock") {
        AppFrame(Tab.STOCK, {}) { StockScreen(data, {}, {}) }
    }

    @Test
    fun clients() = capture("07_clients") {
        AppFrame(Tab.CLIENTS, {}) { ClientsScreen(data, {}, {}) }
    }

    @Test
    fun sumup() = capture("08_sumup") {
        SumUpScreen(
            data,
            SumUpUi(configured = true, merchantCode = "MEXEMPLE", message = "2 nouveaux paiements récupérés.", lastSync = System.currentTimeMillis()),
            { _, _ -> }, {}, {}, { _, _ -> }, {},
        )
    }

    @Test
    fun sumupConnexion() = capture("09_sumup_connexion") {
        SumUpScreen(data.copy(sumupPayments = emptyList()), SumUpUi(), { _, _ -> }, {}, {}, { _, _ -> }, {})
    }

    @Test
    fun detailChiffreAffaires() = capture("10_detail_chiffre_affaires") {
        fr.gemsofrod.gestion.ui.InsightScreen(fr.gemsofrod.gestion.ui.Insight.REVENUE, data, {}, {}, {})
    }

    @Test
    fun detailResteAEncaisser() = capture("11_detail_reste_a_encaisser") {
        fr.gemsofrod.gestion.ui.InsightScreen(fr.gemsofrod.gestion.ui.Insight.TO_COLLECT, data, {}, {}, {})
    }
}
