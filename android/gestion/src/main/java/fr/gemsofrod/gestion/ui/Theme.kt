package fr.gemsofrod.gestion.ui

import android.app.Activity
import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

/**
 * Thèmes de couleurs au choix (menu ⋮ → Thème), demandés par Sébastien le
 * 04/10/2026 : il n'aimait pas le blanc. « Rubis nuit » (cartes vieux rose
 * foncé, variante C de ses maquettes) est le thème par défaut.
 */
enum class AppTheme(
    val label: String,
    val dark: Boolean,
    val background: Color,
    val card: Color,
    val ink: Color,
    val muted: Color,
    val line: Color,
    val accent: Color,
    val accentSoft: Color,
    val accentPale: Color,
    /** Texte et icônes posés sur l'or. */
    val onAccent: Color,
    /** Panneaux sombres (« À suivre », barre de navigation). */
    val navy: Color,
    val navyRow: Color,
    val navyMuted: Color,
    val red: Color,
    val redPale: Color,
    val green: Color,
    val greenPale: Color,
    val orange: Color,
    val orangePale: Color,
    val gold: Pair<Color, Color>,
    val panel: Pair<Color, Color>,
    val chart: List<Color>,
) {
    RUBIS(
        "Rubis nuit", true,
        Color(0xFF1A0D11), Color(0xFF4A2C33), Color(0xFFF7EDE9), Color(0xFFC7AAAF), Color(0xFF5C3640),
        Color(0xFFDDB87C), Color(0xFFE9CE9C), Color(0xFF5E4236), Color(0xFF2A1218),
        Color(0xFF2E1820), Color(0xFF3E232B), Color(0xFFB99CA1),
        Color(0xFFEDA391), Color(0xFF63363B), Color(0xFF97C9A5), Color(0xFF34473C), Color(0xFFE6B46E), Color(0xFF5E4232),
        Color(0xFFE6C48C) to Color(0xFFB8925A), Color(0xFF3E222A) to Color(0xFF221016),
        listOf(Color(0xFFDDB87C), Color(0xFF7FB894), Color(0xFF8EA6CC), Color(0xFFD98C9C), Color(0xFFB49CA1)),
    ),
    ONYX(
        "Onyx et or", true,
        Color(0xFF121110), Color(0xFF2A2724), Color(0xFFF3EBDD), Color(0xFFA99F90), Color(0xFF3A3632),
        Color(0xFFC9A66B), Color(0xFFE0C595), Color(0xFF4A3F2E), Color(0xFF1A1611),
        Color(0xFF1C1A17), Color(0xFF2C2925), Color(0xFFA89F90),
        Color(0xFFE08D78), Color(0xFF4E2D26), Color(0xFF8FC4A0), Color(0xFF2D4134), Color(0xFFE3B06A), Color(0xFF4D3D27),
        Color(0xFFD9B77E) to Color(0xFFA8844C), Color(0xFF2A2622) to Color(0xFF141210),
        listOf(Color(0xFFC9A66B), Color(0xFF7FB894), Color(0xFF8EA6CC), Color(0xFFD98C9C), Color(0xFFA99F90)),
    ),
    EMERAUDE(
        "Émeraude nuit", true,
        Color(0xFF0C1C17), Color(0xFF1F3F37), Color(0xFFEEF2EA), Color(0xFFA3BAAF), Color(0xFF2C5047),
        Color(0xFFD4B072), Color(0xFFE6CC98), Color(0xFF4A4A30), Color(0xFF12241E),
        Color(0xFF12291F), Color(0xFF1C3A31), Color(0xFF9DB5AA),
        Color(0xFFE8998A), Color(0xFF4E3330), Color(0xFFA5D6B2), Color(0xFF2C4A3A), Color(0xFFE6B46E), Color(0xFF4A4430),
        Color(0xFFE2C185) to Color(0xFFB08C55), Color(0xFF1E3D33) to Color(0xFF0F2019),
        listOf(Color(0xFFD4B072), Color(0xFF9FD1AE), Color(0xFF8EA6CC), Color(0xFFD98C9C), Color(0xFFA3BAAF)),
    ),
    SAPHIR(
        "Saphir nuit", true,
        Color(0xFF0D1424), Color(0xFF22304F), Color(0xFFEDF0F7), Color(0xFFA3ADC6), Color(0xFF33436A),
        Color(0xFFCDAA6E), Color(0xFFE3C793), Color(0xFF47402F), Color(0xFF141B2E),
        Color(0xFF141D33), Color(0xFF1F2A47), Color(0xFF9AA5C0),
        Color(0xFFE8998A), Color(0xFF4A2F3A), Color(0xFF8FC9A5), Color(0xFF2A4440), Color(0xFFE3B06A), Color(0xFF4A3F30),
        Color(0xFFDDBC82) to Color(0xFFAA8650), Color(0xFF22304F) to Color(0xFF111A2D),
        listOf(Color(0xFFCDAA6E), Color(0xFF7FB894), Color(0xFF9DB5E0), Color(0xFFD98C9C), Color(0xFFA3ADC6)),
    ),
    CHAMPAGNE(
        "Champagne", false,
        Color(0xFFE6DBC6), Color(0xFFEFE4CF), Color(0xFF2A231B), Color(0xFF7D7163), Color(0xFFDCCFB6),
        Color(0xFF8C6A37), Color(0xFFC4A472), Color(0xFFE2D2B2), Color(0xFFFBF5EA),
        Color(0xFF2A231B), Color(0xFF3A3128), Color(0xFFB3A694),
        Color(0xFFA9533F), Color(0xFFEBD3C6), Color(0xFF4C7559), Color(0xFFD7DFCB), Color(0xFFA9742C), Color(0xFFEAD7B6),
        Color(0xFFBF9B62) to Color(0xFF8C6A37), Color(0xFF3A3128) to Color(0xFF1F1A15),
        listOf(Color(0xFFA8844C), Color(0xFF4C7559), Color(0xFF4A6585), Color(0xFF8E4A5A), Color(0xFF9A8E7E)),
    ),
    IVOIRE(
        "Ivoire (cartes blanches)", false,
        Color(0xFFF6F2EA), Color(0xFFFFFDF8), Color(0xFF1E1B17), Color(0xFF8B8478), Color(0xFFECE5D8),
        Color(0xFF9C7A42), Color(0xFFD8C39A), Color(0xFFF2EADB), Color(0xFFFFFFFF),
        Color(0xFF1F1C19), Color(0xFF2B2823), Color(0xFFA89F90),
        Color(0xFFA9533F), Color(0xFFF4E4DE), Color(0xFF4C7559), Color(0xFFE4EDE5), Color(0xFFA9742C), Color(0xFFF4EAD8),
        Color(0xFFBF9B62) to Color(0xFF8C6A37), Color(0xFF2E2A25) to Color(0xFF171513),
        listOf(Color(0xFFA8844C), Color(0xFF4C7559), Color(0xFF4A6585), Color(0xFF8E4A5A), Color(0xFFA89F90)),
    );

    companion object {
        /** Thème affiché ; lu par [Palette] pendant le dessin, donc tout l'écran suit un changement. */
        var current by mutableStateOf(RUBIS)

        private fun prefs(context: Context) = context.getSharedPreferences("apparence", Context.MODE_PRIVATE)

        fun load(context: Context) {
            current = prefs(context).getString("theme", null)?.let { name -> entries.find { it.name == name } } ?: RUBIS
        }

        fun choose(context: Context, theme: AppTheme) {
            current = theme
            prefs(context).edit().putString("theme", theme.name).apply()
        }
    }
}

/**
 * Couleurs de l'app, prises dans le thème choisi. Les noms historiques
 * (Accent, Navy…) sont gardés pour ne pas toucher aux écrans.
 */
object Palette {
    private val t get() = AppTheme.current
    val Background get() = t.background
    val Card get() = t.card
    val Ink get() = t.ink
    val Muted get() = t.muted
    val Line get() = t.line
    val Accent get() = t.accent
    val AccentSoft get() = t.accentSoft
    val AccentPale get() = t.accentPale
    val OnAccent get() = t.onAccent
    val Navy get() = t.navy
    val NavyRow get() = t.navyRow
    val NavyMuted get() = t.navyMuted
    val Red get() = t.red
    val RedPale get() = t.redPale
    val Green get() = t.green
    val GreenPale get() = t.greenPale
    val Orange get() = t.orange
    val OrangePale get() = t.orangePale

    /** Or (boutons, pilule active, carte « Reste à encaisser »). */
    val AccentGradient get() = Brush.linearGradient(listOf(t.gold.first, t.gold.second))
    /** Panneau sombre (fiche commande façon facture). */
    val PanelGradient get() = Brush.linearGradient(listOf(t.panel.first, t.panel.second))
}

/** Police d'affichage (titres, montants) : à empattements, façon joaillerie. */
val Display = FontFamily.Serif

/** Couleurs des séries de graphiques : or, émeraude, saphir, grenat, taupe. */
val ChartColors: List<Color> get() = AppTheme.current.chart

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

/** Gros montant des cartes. */
val AmountStyle: TextStyle
    get() = TextStyle(fontFamily = FontFamily.Serif, fontSize = 30.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp, color = Palette.Ink)

private fun ColorScheme.withTheme(theme: AppTheme) = copy(
    primary = theme.accent, onPrimary = theme.onAccent,
    primaryContainer = theme.accentPale, onPrimaryContainer = theme.accent,
    secondary = theme.accent, onSecondary = theme.onAccent,
    secondaryContainer = theme.accentPale, onSecondaryContainer = theme.ink,
    background = theme.background, onBackground = theme.ink,
    surface = theme.card, onSurface = theme.ink,
    surfaceVariant = theme.background, onSurfaceVariant = theme.muted,
    surfaceContainer = theme.card, surfaceContainerHigh = theme.card,
    surfaceContainerHighest = theme.card, surfaceContainerLow = theme.card,
    outline = theme.muted.copy(alpha = 0.6f), outlineVariant = theme.line,
    error = theme.red,
)

@Composable
fun GestionTheme(content: @Composable () -> Unit) {
    val theme = AppTheme.current
    val colors = (if (theme.dark) darkColorScheme() else lightColorScheme()).withTheme(theme)
    // Icônes de la barre d'état claires sur un thème sombre, foncées sinon.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !theme.dark
                isAppearanceLightNavigationBars = !theme.dark
            }
        }
    }
    MaterialTheme(colorScheme = colors, typography = Type, content = content)
}
