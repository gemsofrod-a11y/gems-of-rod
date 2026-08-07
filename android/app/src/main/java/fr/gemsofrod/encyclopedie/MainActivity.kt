package fr.gemsofrod.encyclopedie

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.data.LanguageRepository
import fr.gemsofrod.encyclopedie.ui.navigation.GemsNavGraph
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme
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
        super.onCreate(savedInstanceState)
        FavoritesRepository.init(this)
        enableEdgeToEdge()
        setContent {
            GemsEncyclopedieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GemsNavGraph()
                }
            }
        }
    }
}
