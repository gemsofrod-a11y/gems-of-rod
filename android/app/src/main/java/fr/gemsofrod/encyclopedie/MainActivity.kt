package fr.gemsofrod.encyclopedie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.ui.navigation.GemsNavGraph
import fr.gemsofrod.encyclopedie.ui.theme.GemsEncyclopedieTheme

class MainActivity : ComponentActivity() {
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
