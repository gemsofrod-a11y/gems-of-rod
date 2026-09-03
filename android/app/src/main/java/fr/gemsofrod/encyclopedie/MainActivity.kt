package fr.gemsofrod.encyclopedie

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import fr.gemsofrod.encyclopedie.data.ClientRepository
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.data.LabNotebookRepository
import fr.gemsofrod.encyclopedie.data.LanguageRepository
import fr.gemsofrod.encyclopedie.data.ReflectivityCalibrationRepository
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.ui.navigation.GemsNavGraph
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
import fr.gemsofrod.encyclopedie.widget.GemOfDayWidgetProvider
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale(LanguageRepository.getLanguage(newBase).code)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FavoritesRepository.init(this)
        AchievementsRepository.init(this)
        LabNotebookRepository.init(this)
        ReflectivityCalibrationRepository.init(this)
        StockRepository.init(this)
        ClientRepository.init(this)
        refreshGemOfDayWidgets()
        enableEdgeToEdge()
        setContent {
            GemsEncyclopedieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GemsNavGraph()
                }
            }
        }
    }

    /**
     * Un widget déjà posé sur l'écran d'accueil ne reçoit pas d'appel à
     * `onUpdate` lors d'une mise à jour de l'app — il garde l'affichage
     * poussé par l'ancienne version jusqu'à son prochain cycle
     * (`updatePeriodMillis`, 24h) ou tant qu'il n'est pas retiré puis
     * reposé. Ouvrir l'app est le déclencheur le plus naturel après une
     * mise à jour : on en profite pour forcer le rafraîchissement de
     * toutes les instances existantes du widget.
     */
    private fun refreshGemOfDayWidgets() {
        runCatching {
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this, GemOfDayWidgetProvider::class.java))
            for (widgetId in widgetIds) {
                GemOfDayWidgetProvider.updateWidget(this, appWidgetManager, widgetId)
            }
        }
    }
}
