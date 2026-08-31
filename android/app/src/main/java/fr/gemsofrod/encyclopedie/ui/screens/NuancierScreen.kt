package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.NuancierColorMatcher
import fr.gemsofrod.encyclopedie.data.NuancierCouleur
import fr.gemsofrod.encyclopedie.data.NuancierInfo
import fr.gemsofrod.encyclopedie.data.NuancierPage

/** dp par centimètre à la densité de référence Android (160dp = 1 pouce). */
private const val DP_PER_CM = 160f / 2.54f

private fun hsvToComposeColor(hue: Float, saturation: Float, value: Float): Color =
    Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value)))

/**
 * Nuancier de couleur : outil de comparaison visuelle entre la couleur
 * observée d'une pierre — posée sur le carré de fond neutre — et les
 * appellations commerciales du commerce gemmologique (« London Blue »,
 * « Paraíba », etc.), approchées via trois curseurs teinte/saturation/
 * luminosité (HSV). Indicatif uniquement : voir [NuancierPage.disclaimerBody]
 * pour les limites (rendu écran, éclairage ambiant).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuancierScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = NuancierInfo.page(languageCode)

    var hue by remember { mutableFloatStateOf(210f) }
    var saturation by remember { mutableFloatStateOf(0.5f) }
    var value by remember { mutableFloatStateOf(0.75f) }

    val currentColor = hsvToComposeColor(hue, saturation, value)
    val matched = remember(hue, saturation, value) {
        NuancierColorMatcher.match(hue, saturation, value, page.colors)
    }

    // Léger décrochage du blanc pur pour rester "lumineux mais pas trop" —
    // réduit l'éblouissement lors de la comparaison visuelle prolongée.
    val whiteBackground = Color(0xFFF2F2EE)

    val hueGradient = remember {
        Brush.horizontalGradient(
            (0..12).map { step -> hsvToComposeColor(step * 30f, 1f, 1f) }
        )
    }
    val saturationGradient = remember(hue, value) {
        Brush.horizontalGradient(
            listOf(hsvToComposeColor(hue, 0f, value), hsvToComposeColor(hue, 1f, value))
        )
    }
    val valueGradient = remember(hue, saturation) {
        Brush.horizontalGradient(
            listOf(Color.Black, hsvToComposeColor(hue, saturation, 1f))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(page.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = page.intro,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size((4f * DP_PER_CM).dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(whiteBackground)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                )
                Text(
                    text = page.whiteSquareLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size((1.5f * DP_PER_CM).dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(currentColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                )
                Text(
                    text = page.colorSquareLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                MatchedNameField(page = page, matched = matched, saturation = saturation)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ColorAxisSlider(
                    label = page.hueLabel,
                    value = hue,
                    valueRange = 0f..360f,
                    background = hueGradient,
                    onValueChange = { hue = it }
                )
                ColorAxisSlider(
                    label = page.saturationLabel,
                    value = saturation,
                    valueRange = 0f..1f,
                    background = saturationGradient,
                    onValueChange = { saturation = it }
                )
                ColorAxisSlider(
                    label = page.valueLabel,
                    value = value,
                    valueRange = 0f..1f,
                    background = valueGradient,
                    onValueChange = { value = it }
                )
            }

            NuancierDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
        }
    }
}

@Composable
private fun MatchedNameField(page: NuancierPage, matched: NuancierCouleur?, saturation: Float) {
    val text = when {
        matched != null -> "${matched.nom} — ${matched.espece}"
        saturation < NuancierColorMatcher.ACHROMATIC_SATURATION_THRESHOLD -> page.achromaticLabel
        else -> page.noMatchLabel
    }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
private fun ColorAxisSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    background: Brush,
    onValueChange: (Float) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(background)
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NuancierDisclaimer(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}
