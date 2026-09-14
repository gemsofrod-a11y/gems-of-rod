package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import fr.gemsofrod.encyclopedie.data.RingCutShape
import fr.gemsofrod.encyclopedie.data.RingGemSpecies
import fr.gemsofrod.encyclopedie.data.RingMetal
import fr.gemsofrod.encyclopedie.data.RingPresets
import fr.gemsofrod.encyclopedie.data.RingPricingCalculator
import fr.gemsofrod.encyclopedie.data.RingSertissage
import fr.gemsofrod.encyclopedie.data.RingSideStoneSelection
import fr.gemsofrod.encyclopedie.data.RingStoneSelection
import fr.gemsofrod.encyclopedie.ui.components.ColorSwatchRow
import fr.gemsofrod.encyclopedie.ui.components.RequiredDropdownField
import fr.gemsofrod.encyclopedie.ui.components.RingIllustrationCanvas
import fr.gemsofrod.encyclopedie.ui.labelRes
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

/** Normalise la virgule décimale (clavier français) avant analyse — même idiome que StockFormScreen. */
private fun parseDecimalOrNull(input: String): Double? = input.trim().replace(',', '.').toDoubleOrNull()

/**
 * Compositeur de bague : un calculateur en direct (aucune sauvegarde,
 * comme le prototype web testé avec l'équipe) permettant d'esquisser une
 * composition — pierre centrale, pierres annexes, métal — et d'en tirer
 * un aperçu schématique et une fourchette de prix indicative à montrer
 * à un client, avant de passer commande auprès de l'atelier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingConfiguratorScreen(onBackClick: () -> Unit) {
    var metal by remember { mutableStateOf(RingMetal.OR_JAUNE) }
    var gramsMetal by remember { mutableFloatStateOf(3.0f) }

    var centralSpecies by remember { mutableStateOf(RingGemSpecies.DIAMANT) }
    var centralColor by remember { mutableStateOf(RingGemSpecies.DIAMANT.defaultColor) }
    var centralCut by remember { mutableStateOf(RingCutShape.OVALE) }
    var centralSertissage by remember { mutableStateOf(RingSertissage.GRIFFES) }
    var centralWidth by remember { mutableStateOf("8.0") }
    var centralHeight by remember { mutableStateOf("6.0") }
    var centralOrientation by remember { mutableFloatStateOf(0f) }

    var coteSpecies by remember { mutableStateOf(RingGemSpecies.SAPHIR) }
    var coteColor by remember { mutableStateOf(RingGemSpecies.SAPHIR.defaultColor) }
    var coteCut by remember { mutableStateOf(RingCutShape.RONDE) }
    var coteSertissage by remember { mutableStateOf(RingSertissage.PAVE) }
    var coteWidth by remember { mutableStateOf("1.8") }
    var coteHeight by remember { mutableStateOf("1.8") }
    var coteNombre by remember { mutableFloatStateOf(0f) }
    var coteEspacement by remember { mutableFloatStateOf(0.15f) }
    var coteOrientationOffset by remember { mutableFloatStateOf(0f) }

    fun applyPreset(preset: RingConfiguration) {
        metal = preset.metal
        gramsMetal = preset.gramsMetal.toFloat()
        centralSpecies = preset.central.species
        centralColor = preset.central.color
        centralCut = preset.central.cut
        centralSertissage = preset.central.sertissage
        centralWidth = preset.central.widthMm.toString()
        centralHeight = preset.central.heightMm.toString()
        centralOrientation = preset.central.orientationDeg
        coteSpecies = preset.cote.stone.species
        coteColor = preset.cote.stone.color
        coteCut = preset.cote.stone.cut
        coteSertissage = preset.cote.stone.sertissage
        coteWidth = preset.cote.stone.widthMm.toString()
        coteHeight = preset.cote.stone.heightMm.toString()
        coteNombre = preset.cote.nombre.toFloat()
        coteEspacement = preset.cote.espacement
        coteOrientationOffset = preset.cote.orientationOffsetDeg
    }

    val centralWidthMm = parseDecimalOrNull(centralWidth)?.takeIf { it > 0.0 } ?: 1.0
    val centralHeightMm = if (centralCut.isSingleDimension) centralWidthMm
        else parseDecimalOrNull(centralHeight)?.takeIf { it > 0.0 } ?: 1.0
    val coteWidthMm = parseDecimalOrNull(coteWidth)?.takeIf { it > 0.0 } ?: 1.0
    val coteHeightMm = if (coteCut.isSingleDimension) coteWidthMm
        else parseDecimalOrNull(coteHeight)?.takeIf { it > 0.0 } ?: 1.0
    val nombre = coteNombre.roundToInt()

    val config = remember(
        metal, gramsMetal, centralSpecies, centralColor, centralCut, centralSertissage,
        centralWidthMm, centralHeightMm, centralOrientation,
        coteSpecies, coteColor, coteCut, coteSertissage, coteWidthMm, coteHeightMm,
        nombre, coteEspacement, coteOrientationOffset
    ) {
        RingConfiguration(
            metal = metal,
            gramsMetal = gramsMetal.toDouble(),
            central = RingStoneSelection(
                centralSpecies, centralColor, centralSertissage, centralCut,
                centralWidthMm, centralHeightMm, centralOrientation
            ),
            cote = RingSideStoneSelection(
                stone = RingStoneSelection(
                    coteSpecies, coteColor, coteSertissage, coteCut,
                    coteWidthMm, coteHeightMm, 0f
                ),
                nombre = nombre,
                espacement = coteEspacement,
                orientationOffsetDeg = coteOrientationOffset
            )
        )
    }
    val pricing = remember(config) { RingPricingCalculator.compute(config) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.lab_ring_configurator_title)) },
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.ring_configurator_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { applyPreset(RingPresets.SOLITAIRE) }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.ring_preset_solitaire))
                }
                OutlinedButton(onClick = { applyPreset(RingPresets.TRILOGIE) }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.ring_preset_trilogie))
                }
                OutlinedButton(onClick = { applyPreset(RingPresets.PAVE_COMPLET) }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.ring_preset_pave))
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                RingIllustrationCanvas(config = config, modifier = Modifier.padding(12.dp))
            }

            RingSectionCard(stringResource(R.string.ring_section_metal_title)) {
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_metal_label),
                    selectedLabel = stringResource(metal.labelRes),
                    options = RingMetal.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { metal = it }
                )
                RingSliderField(
                    label = stringResource(R.string.ring_field_weight_label),
                    valueText = "%.1f g".format(gramsMetal),
                    value = gramsMetal,
                    onValueChange = { gramsMetal = it },
                    valueRange = 1f..15f
                )
            }

            RingSectionCard(stringResource(R.string.ring_section_central_title)) {
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_species_label),
                    selectedLabel = stringResource(centralSpecies.labelRes),
                    options = RingGemSpecies.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { centralSpecies = it; centralColor = it.defaultColor }
                )
                ColorSwatchRow(baseColor = centralSpecies.defaultColor, selected = centralColor, onSelect = { centralColor = it })
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_cut_label),
                    selectedLabel = stringResource(centralCut.labelRes),
                    options = RingCutShape.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { centralCut = it }
                )
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_sertissage_label),
                    selectedLabel = stringResource(centralSertissage.labelRes),
                    options = RingSertissage.entries.filter { it.availableForCentral }.map { it to stringResource(it.labelRes) },
                    onSelect = { centralSertissage = it }
                )
                if (centralCut.isSingleDimension) {
                    RingNumberField(
                        label = stringResource(R.string.ring_field_diameter_label),
                        value = centralWidth,
                        onValueChange = { centralWidth = it }
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        RingNumberField(
                            label = stringResource(R.string.ring_field_length_label),
                            value = centralWidth,
                            onValueChange = { centralWidth = it },
                            modifier = Modifier.weight(1f)
                        )
                        RingNumberField(
                            label = stringResource(R.string.ring_field_width_label),
                            value = centralHeight,
                            onValueChange = { centralHeight = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                RingSliderField(
                    label = stringResource(R.string.ring_field_orientation_label),
                    valueText = formatDegrees(centralOrientation),
                    value = centralOrientation,
                    onValueChange = { centralOrientation = it },
                    valueRange = -180f..180f
                )
            }

            RingSectionCard(stringResource(R.string.ring_section_cote_title)) {
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_species_label),
                    selectedLabel = stringResource(coteSpecies.labelRes),
                    options = RingGemSpecies.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { coteSpecies = it; coteColor = it.defaultColor }
                )
                ColorSwatchRow(baseColor = coteSpecies.defaultColor, selected = coteColor, onSelect = { coteColor = it })
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_cut_label),
                    selectedLabel = stringResource(coteCut.labelRes),
                    options = RingCutShape.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { coteCut = it }
                )
                RequiredDropdownField(
                    label = stringResource(R.string.ring_field_sertissage_label),
                    selectedLabel = stringResource(coteSertissage.labelRes),
                    options = RingSertissage.entries.map { it to stringResource(it.labelRes) },
                    onSelect = { coteSertissage = it }
                )
                if (coteCut.isSingleDimension) {
                    RingNumberField(
                        label = stringResource(R.string.ring_field_diameter_label),
                        value = coteWidth,
                        onValueChange = { coteWidth = it }
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        RingNumberField(
                            label = stringResource(R.string.ring_field_length_label),
                            value = coteWidth,
                            onValueChange = { coteWidth = it },
                            modifier = Modifier.weight(1f)
                        )
                        RingNumberField(
                            label = stringResource(R.string.ring_field_width_label),
                            value = coteHeight,
                            onValueChange = { coteHeight = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                RingSliderField(
                    label = stringResource(R.string.ring_field_nombre_label),
                    valueText = nombre.toString(),
                    value = coteNombre,
                    onValueChange = { coteNombre = it },
                    valueRange = 0f..8f,
                    steps = 7
                )
                RingSliderField(
                    label = stringResource(R.string.ring_field_espacement_label),
                    valueText = "%.1f×".format(0.8f + coteEspacement),
                    value = coteEspacement,
                    onValueChange = { coteEspacement = it },
                    valueRange = 0f..1f
                )
                RingSliderField(
                    label = stringResource(R.string.ring_field_orientation_offset_label),
                    valueText = formatDegrees(coteOrientationOffset),
                    value = coteOrientationOffset,
                    onValueChange = { coteOrientationOffset = it },
                    valueRange = -180f..180f
                )
            }

            RingSectionCard(stringResource(R.string.ring_section_price_title)) {
                Text(
                    text = ringSummary(config),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                val currency = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }
                Text(
                    text = "${currency.format(pricing.rangeMinRounded)} – ${currency.format(pricing.rangeMaxRounded)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.ring_price_disclaimer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ringSummary(config: RingConfiguration): String {
    val metalLabel = stringResource(config.metal.labelRes)
    val cSpecies = stringResource(config.central.species.labelRes)
    val cCut = stringResource(config.central.cut.labelRes)
    val cSert = stringResource(config.central.sertissage.labelRes)
    return if (config.cote.nombre > 0) {
        stringResource(
            R.string.ring_summary_with_cote,
            cSpecies, cCut, cSert, metalLabel,
            config.cote.nombre * 2,
            stringResource(config.cote.stone.species.labelRes),
            stringResource(config.cote.stone.cut.labelRes),
            stringResource(config.cote.stone.sertissage.labelRes)
        )
    } else {
        stringResource(R.string.ring_summary_solo, cSpecies, cCut, cSert, metalLabel)
    }
}

private fun formatDegrees(value: Float): String {
    val rounded = value.roundToInt()
    return if (rounded > 0) "+$rounded°" else "$rounded°"
}

@Composable
private fun RingSectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun RingNumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun RingSliderField(
    label: String,
    valueText: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = valueText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange, steps = steps)
    }
}
