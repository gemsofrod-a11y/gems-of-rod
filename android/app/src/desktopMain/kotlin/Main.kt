import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Point d'entrée du build bureau (Windows/Linux/macOS), pour l'instant un
 * placeholder minimal. Voir le plan de portage
 * /root/.claude/plans/humble-scribbling-island.md : le contenu réel de
 * l'appli (catalogue, écrans) migre vers commonMain au fil des phases
 * suivantes, ce fichier ne fait que prouver que la cible desktop compile et
 * se lance.
 */
fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Gems of Rod Encyclopédie") {
        MaterialTheme {
            Text("Gems of Rod Encyclopédie — build bureau (en cours de portage)")
        }
    }
}
