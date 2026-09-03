package fr.gemsofrod.encyclopedie.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import fr.gemsofrod.encyclopedie.R

// « Cabinet du Soir » : Fraunces italique pour les titres (le sérieux du
// spécimen numéroté), Manrope pour le texte courant, IBM Plex Mono pour les
// références de catalogue (voir StockDetailScreen / StockListScreen).
val GorDisplayFont = FontFamily(
    Font(R.font.fraunces_italic_medium, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.fraunces_italic_semibold, FontWeight.SemiBold, FontStyle.Italic)
)

val GorBodyFont = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold)
)

val GorMonoFont = FontFamily(
    Font(R.font.plexmono_regular, FontWeight.Normal),
    Font(R.font.plexmono_medium, FontWeight.Medium)
)

val GorTypography = Typography(
    headlineMedium = TextStyle(fontFamily = GorDisplayFont, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, fontSize = 27.sp),
    titleLarge = TextStyle(fontFamily = GorDisplayFont, fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = GorBodyFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    bodyLarge = TextStyle(fontFamily = GorBodyFont, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = GorBodyFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = GorBodyFont, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
)
