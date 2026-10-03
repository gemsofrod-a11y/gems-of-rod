package fr.gemsofrod.gestion.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Palette « fintech » : fond gris très clair, cartes blanches, violet indigo, panneau marine. */
object Palette {
    val Background = Color(0xFFF3F4F9)
    val Card = Color.White
    val Ink = Color(0xFF16172B)
    val Muted = Color(0xFF8A8CA3)
    val Line = Color(0xFFE9EAF2)
    val Accent = Color(0xFF5546E8)
    val AccentSoft = Color(0xFFB8B1F7)
    val AccentPale = Color(0xFFEDEBFE)
    val Navy = Color(0xFF1C1D33)
    val NavyRow = Color(0xFF28294A)
    val NavyMuted = Color(0xFF9A9BBE)
    val Red = Color(0xFFE5484D)
    val RedPale = Color(0xFFFDECEC)
    val Green = Color(0xFF16A34A)
    val GreenPale = Color(0xFFE6F6EC)
    val Orange = Color(0xFFE38A1E)
    val OrangePale = Color(0xFFFDF1E1)

    val AccentGradient = Brush.linearGradient(listOf(Color(0xFF6D5EF5), Color(0xFF4335D6)))
    val PanelGradient = Brush.linearGradient(listOf(Color(0xFF6A5CF0), Color(0xFF8B7FF6)))
}

/** Couleurs des séries de graphiques (catégories). */
val ChartColors = listOf(
    Color(0xFF5546E8),
    Color(0xFF9B8CFF),
    Color(0xFF22B8CF),
    Color(0xFFF59E0B),
    Color(0xFF8A8CA3),
)

private val Colors = lightColorScheme(
    primary = Palette.Accent,
    onPrimary = Color.White,
    primaryContainer = Palette.AccentPale,
    onPrimaryContainer = Palette.Accent,
    secondary = Palette.Accent,
    onSecondary = Color.White,
    secondaryContainer = Palette.AccentPale,
    onSecondaryContainer = Palette.Ink,
    background = Palette.Background,
    onBackground = Palette.Ink,
    surface = Palette.Card,
    onSurface = Palette.Ink,
    surfaceVariant = Palette.Background,
    onSurfaceVariant = Palette.Muted,
    surfaceContainer = Palette.Card,
    surfaceContainerHigh = Palette.Card,
    surfaceContainerHighest = Palette.Card,
    surfaceContainerLow = Palette.Card,
    outline = Color(0xFFD9DAE6),
    outlineVariant = Palette.Line,
    error = Palette.Red,
)

private val Type = Typography().let { t ->
    t.copy(
        headlineLarge = t.headlineLarge.copy(fontWeight = FontWeight.Bold, fontSize = 30.sp, letterSpacing = (-0.5).sp),
        headlineMedium = t.headlineMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
        headlineSmall = t.headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp),
        titleLarge = t.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        titleMedium = t.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        titleSmall = t.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        labelLarge = t.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    )
}

/** Gros montant des cartes (« $ 24,850.00 » de la maquette). */
val AmountStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp, color = Palette.Ink)

@Composable
fun GestionTheme(content: @Composable () -> Unit) {
    // Thème clair uniquement : le design repose sur des cartes blanches sur fond clair.
    MaterialTheme(colorScheme = Colors, typography = Type, content = content)
}
