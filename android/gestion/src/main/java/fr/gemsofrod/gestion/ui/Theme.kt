package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Gold = Color(0xFFB08A3E)
private val GoldLight = Color(0xFFD9BC7F)
private val Ink = Color(0xFF1F2A2E)

private val LightColors = lightColorScheme(
    primary = Ink,
    onPrimary = Color.White,
    secondary = Gold,
    onSecondary = Color.White,
    tertiary = Color(0xFF2E6F5E),
    background = Color(0xFFFAF9F6),
    onBackground = Ink,
    surface = Color(0xFFFAF9F6),
    onSurface = Ink,
    surfaceVariant = Color(0xFFEFEDE7),
    onSurfaceVariant = Color(0xFF5B6366),
    surfaceContainer = Color(0xFFF3F1EC),
    surfaceContainerHigh = Color(0xFFEFEDE7),
    secondaryContainer = Color(0xFFF1E6CF),
    onSecondaryContainer = Ink,
    outline = Color(0xFFD5D2CA),
    outlineVariant = Color(0xFFE4E1DA),
    error = Color(0xFFB3261E),
)

private val DarkColors = darkColorScheme(
    primary = GoldLight,
    onPrimary = Ink,
    secondary = GoldLight,
    onSecondary = Ink,
    tertiary = Color(0xFF7FC4AE),
    background = Color(0xFF141A1C),
    onBackground = Color(0xFFE8E6E1),
    surface = Color(0xFF141A1C),
    onSurface = Color(0xFFE8E6E1),
    surfaceVariant = Color(0xFF232B2E),
    onSurfaceVariant = Color(0xFFA9B0B2),
    surfaceContainer = Color(0xFF1B2224),
    surfaceContainerHigh = Color(0xFF232B2E),
    secondaryContainer = Color(0xFF3B3322),
    onSecondaryContainer = Color(0xFFE8E6E1),
    outline = Color(0xFF3A4447),
    outlineVariant = Color(0xFF2C3538),
    error = Color(0xFFF2B8B5),
)

/** Couleurs des séries de graphiques (catégories), lisibles en clair et en sombre. */
val ChartColors = listOf(
    Color(0xFFB08A3E),
    Color(0xFF2E6F8E),
    Color(0xFF2E8E6A),
    Color(0xFF9A4E7A),
    Color(0xFF7A7F84),
)

@Composable
fun GestionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
