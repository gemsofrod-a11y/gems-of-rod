package fr.gemsofrod.gestion.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Palette « maison de joaillerie » : fond ivoire, cartes crème, or champagne,
 * panneau noir onyx, couleurs d'état douces (émeraude, terracotta, ambre).
 * Les noms historiques (Accent, Navy…) sont gardés pour ne pas toucher aux écrans.
 */
object Palette {
    val Background = Color(0xFFF6F2EA)
    val Card = Color(0xFFFFFDF8)
    val Ink = Color(0xFF1E1B17)
    val Muted = Color(0xFF8B8478)
    val Line = Color(0xFFECE5D8)
    val Accent = Color(0xFF9C7A42)
    val AccentSoft = Color(0xFFD8C39A)
    val AccentPale = Color(0xFFF2EADB)
    val Navy = Color(0xFF1F1C19)
    val NavyRow = Color(0xFF2B2823)
    val NavyMuted = Color(0xFFA89F90)
    val Red = Color(0xFFA9533F)
    val RedPale = Color(0xFFF4E4DE)
    val Green = Color(0xFF4C7559)
    val GreenPale = Color(0xFFE4EDE5)
    val Orange = Color(0xFFA9742C)
    val OrangePale = Color(0xFFF4EAD8)

    /** Or patiné (boutons, pilule active, carte « Reste à encaisser »). */
    val AccentGradient = Brush.linearGradient(listOf(Color(0xFFBF9B62), Color(0xFF8C6A37)))
    /** Onyx (fiche commande façon facture). */
    val PanelGradient = Brush.linearGradient(listOf(Color(0xFF2E2A25), Color(0xFF171513)))
}

/** Police d'affichage (titres, montants) : à empattements, façon joaillerie. */
val Display = FontFamily.Serif

/** Couleurs des séries de graphiques : or, émeraude, saphir, grenat, taupe. */
val ChartColors = listOf(
    Color(0xFFA8844C),
    Color(0xFF4C7559),
    Color(0xFF4A6585),
    Color(0xFF8E4A5A),
    Color(0xFFA89F90),
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
    outline = Color(0xFFDDD4C4),
    outlineVariant = Palette.Line,
    error = Palette.Red,
)

private val Type = Typography().let { t ->
    t.copy(
        headlineLarge = t.headlineLarge.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Medium, fontSize = 30.sp),
        headlineMedium = t.headlineMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
        headlineSmall = t.headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.3).sp),
        titleLarge = t.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        titleMedium = t.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        titleSmall = t.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        labelLarge = t.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    )
}

/** Gros montant des cartes (« $ 24,850.00 » de la maquette). */
val AmountStyle = TextStyle(fontFamily = FontFamily.Serif, fontSize = 30.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp, color = Palette.Ink)

@Composable
fun GestionTheme(content: @Composable () -> Unit) {
    // Thème clair uniquement : le design repose sur des cartes blanches sur fond clair.
    MaterialTheme(colorScheme = Colors, typography = Type, content = content)
}
