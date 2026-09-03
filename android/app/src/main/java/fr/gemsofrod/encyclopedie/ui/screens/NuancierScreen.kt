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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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
import fr.gemsofrod.encyclopedie.ui.components.DropdownField

/** dp par centimètre à la densité de référence Android (160dp = 1 pouce). */
private const val DP_PER_CM = 160f / 2.54f

private fun hsvToComposeColor(hue: Float, saturation: Float, value: Float): Color =
    Color(android.graphics.Color.HSVToColor(floatArrayOf(hue, saturation, value)))

private fun lerpFloat(start: Float, stop: Float, fraction: Float): Float =
    start + (stop - start) * fraction

/** Point de départ (quasi incolore) du curseur d'intensité par espèce. */
private const val INTENSITY_START_SATURATION = 0.05f
private const val INTENSITY_START_VALUE = 0.95f

/**
 * Nuancier de couleur : outil de comparaison visuelle entre la couleur
 * observée d'une pierre — posée sur le carré de fond neutre — et les
 * appellations commerciales du commerce gemmologique (« London Blue »,
 * « Paraíba », etc.), approchées via trois curseurs teinte/saturation/
 * luminosité (HSV), ou en choisissant une espèce dans le menu déroulant
 * (ex. « Aigue-marine ») pour se limiter à ses appellations et, si une
 * seule existe, à un unique curseur d'intensité (incolore → teinte
 * pleine). Indicatif uniquement : voir [NuancierPage.disclaimerBody] pour
 * les limites (rendu écran, éclairage ambiant).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuancierScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = NuancierInfo.page(languageCode)

    var hue by remember { mutableFloatStateOf(210f) }
    var saturation by remember { mutableFloatStateOf(0.5f) }
    var value by remember { mutableFloatStateOf(0.75f) }
    var intensity by remember { mutableFloatStateOf(0f) }

    // Sélection d'une espèce (ex. "Aigue-marine") : restreint la comparaison
    // aux appellations de cette espèce plutôt qu'à l'ensemble du nuancier.
    // null = mode libre d'origine (les 3 curseurs HSV, toutes espèces confondues).
    var selectedEspece by remember { mutableStateOf<String?>(null) }
    val especeOptions = remember(page) {
        page.colors.map { it.espece }.distinct().sorted().map { it to it }
    }
    val varieties = remember(page, selectedEspece) {
        selectedEspece?.let { espece -> page.colors.filter { it.espece == espece } } ?: emptyList()
    }
    // Une seule appellation pour l'espèce choisie (ex. l'aigue-marine n'a que
    // « Santa Maria ») : un unique curseur d'intensité suffit, de l'incolore
    // à cette teinte. Plusieurs appellations (ex. le saphir : Cornflower,
    // Royal, Padparadscha) : la teinte varie aussi selon la variété, un seul
    // axe incolore→X serait trompeur — on garde les 3 curseurs HSV, mais
    // recentrés sur cette espèce pour la comparaison.
    val singleVariety = varieties.singleOrNull()

    LaunchedEffect(selectedEspece) {
        if (singleVariety != null) {
            intensity = 0f
        } else if (varieties.isNotEmpty()) {
            hue = varieties.first().hueDeg
            saturation = 0.4f
            value = 0.85f
        }
    }

    val effectiveHue: Float
    val effectiveSaturation: Float
    val effectiveValue: Float
    if (singleVariety != null) {
        effectiveHue = singleVariety.hueDeg
        effectiveSaturation = lerpFloat(INTENSITY_START_SATURATION, singleVariety.saturation, intensity)
        effectiveValue = lerpFloat(INTENSITY_START_VALUE, singleVariety.valeur, intensity)
    } else {
        effectiveHue = hue
        effectiveSaturation = saturation
        effectiveValue = value
    }

    val currentColor = hsvToComposeColor(effectiveHue, effectiveSaturation, effectiveValue)
    val effectiveColors = if (selectedEspece != null) varieties else page.colors
    val matched = remember(effectiveHue, effectiveSaturation, effectiveValue, effectiveColors) {
        NuancierColorMatcher.match(effectiveHue, effectiveSaturation, effectiveValue, effectiveColors)
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
    val intensityGradient = remember(singleVariety) {
        Brush.horizontalGradient(
            (0..8).map { step ->
                val t = step / 8f
                val variety = singleVariety
                if (variety == null) {
                    Color.White
                } else {
                    hsvToComposeColor(
                        variety.hueDeg,
                        lerpFloat(INTENSITY_START_SATURATION, variety.saturation, t),
                        lerpFloat(INTENSITY_START_VALUE, variety.valeur, t)
                    )
                }
            }
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

            DropdownField(
                label = page.categoryPickerLabel,
                selectedLabel = selectedEspece,
                options = especeOptions,
                onSelect = { selectedEspece = it }
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
                MatchedNameField(page = page, matched = matched, saturation = effectiveSaturation)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                if (singleVariety != null) {
                    ColorAxisSlider(
                        label = page.intensityLabel,
                        value = intensity,
                        valueRange = 0f..1f,
                        background = intensityGradient,
                        onValueChange = { intensity = it }
                    )
                } else {
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
