package fr.gemsofrod.encyclopedie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.gemsofrod.encyclopedie.data.ThemeMode
import fr.gemsofrod.encyclopedie.data.ThemeRepository

private val GorDarkColors = darkColorScheme(
    primary = GorGold,
    onPrimary = GorInk,
    secondary = GorVerdigris,
    onSecondary = GorInk,
    tertiary = GorGarnet,
    onTertiary = GorIvory,
    background = GorBottleGreen,
    onBackground = GorIvory,
    surface = GorBottleGreenSurface,
    onSurface = GorIvory,
    surfaceVariant = GorBottleGreenSurfaceVariant,
    onSurfaceVariant = GorSage
)

private val GorLightColors = lightColorScheme(
    primary = GorGold,
    onPrimary = GorInk,
    secondary = GorBottleGreen,
    onSecondary = GorIvory,
    tertiary = GorGarnet,
    onTertiary = GorIvory,
    background = GorParchment,
    onBackground = GorInk,
    surface = Color(0xFFFFFBF2),
    onSurface = GorInk,
    surfaceVariant = GorParchmentVariant,
    onSurfaceVariant = GorInk
)

@Composable
fun GemsEncyclopedieTheme(
    content: @Composable () -> Unit
) {
    val darkTheme = when (ThemeRepository.mode) {
        ThemeMode.SYSTEME -> isSystemInDarkTheme()
        ThemeMode.CLAIR -> false
        ThemeMode.SOMBRE -> true
    }
    val colors = if (darkTheme) GorDarkColors else GorLightColors
    MaterialTheme(
        colorScheme = colors,
        typography = GorTypography,
        content = content
    )
}
