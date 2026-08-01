package fr.gemsofrod.encyclopedie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GorDarkColors = darkColorScheme(
    primary = GorGold,
    onPrimary = GorAnthracite,
    secondary = GorGoldLight,
    background = GorAnthracite,
    onBackground = GorCream,
    surface = Color(0xFF242426),
    onSurface = GorCream,
    surfaceVariant = Color(0xFF2F2F32),
    onSurfaceVariant = GorCream
)

private val GorLightColors = lightColorScheme(
    primary = GorGold,
    onPrimary = Color(0xFFFFFFFF),
    secondary = GorAnthracite,
    background = GorCream,
    onBackground = GorCharcoalText,
    surface = Color(0xFFFFFFFF),
    onSurface = GorCharcoalText,
    surfaceVariant = Color(0xFFEDE6D8),
    onSurfaceVariant = GorCharcoalText
)

@Composable
fun GemsEncyclopedieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) GorDarkColors else GorLightColors
    MaterialTheme(
        colorScheme = colors,
        typography = GorTypography,
        content = content
    )
}
