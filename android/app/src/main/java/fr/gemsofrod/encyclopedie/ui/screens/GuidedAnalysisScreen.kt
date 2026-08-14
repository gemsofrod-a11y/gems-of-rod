package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AnalysisCriteria
import fr.gemsofrod.encyclopedie.data.AnalysisVocabulary
import fr.gemsofrod.encyclopedie.data.GemAnalyzer
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GuidedAnalysisState
import fr.gemsofrod.encyclopedie.ui.components.AnalysisResultRow
import fr.gemsofrod.encyclopedie.ui.components.DropdownField
import fr.gemsofrod.encyclopedie.ui.localizedLabel

private const val TOTAL_STEPS = 11

/**
 * Version guidée de l'outil d'analyse : au lieu d'un formulaire complet
 * affiché d'un bloc, chaque caractéristique est présentée une par une,
 * dans l'ordre où un gemmologue les observerait en pratique (examen visuel,
 * puis tests instrumentaux), avec une explication de comment l'observer ou
 * la mesurer. Utilise le même moteur de correspondance ([GemAnalyzer]) et
 * les mêmes critères ([AnalysisCriteria]) que l'analyse rapide
 * ([AnalyseScreen]) : seule la présentation diffère.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidedAnalysisScreen(onGemClick: (String) -> Unit, onBackClick: () -> Unit) {
    var stepIndex by GuidedAnalysisState.stepIndex
    var couleur by GuidedAnalysisState.couleur
    var transparence by GuidedAnalysisState.transparence
    var eclat by GuidedAnalysisState.eclat
    var clivage by GuidedAnalysisState.clivage
    var typeInclusion by GuidedAnalysisState.typeInclusion
    var dureteInput by GuidedAnalysisState.dureteInput
    var densiteInput by GuidedAnalysisState.densiteInput
    var pleochroisme by GuidedAnalysisState.pleochroisme
    var indiceRefractionInput by GuidedAnalysisState.indiceRefractionInput
    var systemeCristallin by GuidedAnalysisState.systemeCristallin
    var fluorescence by GuidedAnalysisState.fluorescence
    var results by GuidedAnalysisState.results

    val systemesCristallins = remember { GemAnalyzer.systemesCristallins() }

    fun parseDecimal(input: String): Double? = input.trim().replace(',', '.').toDoubleOrNull()

    fun currentCriteria() = AnalysisCriteria(
        couleur = couleur,
        transparence = transparence,
        eclat = eclat,
        clivage = clivage,
        systemeCristallin = systemeCristallin,
        durete = parseDecimal(dureteInput),
        densite = parseDecimal(densiteInput),
        indiceRefraction = parseDecimal(indiceRefractionInput),
        pleochroisme = pleochroisme,
        fluorescence = fluorescence,
        typeInclusion = typeInclusion
    )

    fun restart() {
        stepIndex = 0
        couleur = null
        transparence = null
        eclat = null
        clivage = null
        typeInclusion = null
        dureteInput = ""
        densiteInput = ""
        pleochroisme = null
        indiceRefractionInput = ""
        systemeCristallin = null
        fluorescence = null
        results = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.lab_guided_analysis_title)) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val currentResults = results
            if (currentResults != null) {
                Text(
                    text = stringResource(R.string.analyse_results_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (currentResults.isEmpty()) {
                    Text(
                        text = stringResource(R.string.analyse_no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        currentResults.forEach { match ->
                            AnalysisResultRow(match = match, onClick = { onGemClick(match.gem.id) })
                        }
                    }
                }
                Button(onClick = { restart() }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.guided_restart_button))
                }
            } else {
                Text(
                    text = stringResource(R.string.guided_step_progress, stepIndex + 1, TOTAL_STEPS),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LinearProgressIndicator(
                    progress = { (stepIndex + 1) / TOTAL_STEPS.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )

                when (stepIndex) {
                    0 -> GuidedStep(
                        title = stringResource(R.string.analyse_couleur_label),
                        instruction = stringResource(R.string.guided_step_couleur_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_couleur_label),
                            selectedLabel = couleur?.let { stringResource(it.labelRes) },
                            options = GemColorCategory.entries.map { it to stringResource(it.labelRes) },
                            onSelect = { couleur = it }
                        )
                    }
                    1 -> GuidedStep(
                        title = stringResource(R.string.analyse_transparence_label),
                        instruction = stringResource(R.string.guided_step_transparence_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_transparence_label),
                            selectedLabel = transparence?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.TRANSPARENCE.map { it to localizedLabel(it) },
                            onSelect = { transparence = it }
                        )
                    }
                    2 -> GuidedStep(
                        title = stringResource(R.string.analyse_eclat_label),
                        instruction = stringResource(R.string.guided_step_eclat_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_eclat_label),
                            selectedLabel = eclat?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.ECLAT.map { it to localizedLabel(it) },
                            onSelect = { eclat = it }
                        )
                    }
                    3 -> GuidedStep(
                        title = stringResource(R.string.analyse_clivage_label),
                        instruction = stringResource(R.string.guided_step_clivage_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_clivage_label),
                            selectedLabel = clivage?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.CLIVAGE.map { it to localizedLabel(it) },
                            onSelect = { clivage = it }
                        )
                    }
                    4 -> GuidedStep(
                        title = stringResource(R.string.analyse_type_inclusion_label),
                        instruction = stringResource(R.string.guided_step_type_inclusion_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_type_inclusion_label),
                            selectedLabel = typeInclusion?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.TYPE_INCLUSION.map { it to localizedLabel(it) },
                            onSelect = { typeInclusion = it }
                        )
                    }
                    5 -> GuidedStep(
                        title = stringResource(R.string.analyse_durete_label),
                        instruction = stringResource(R.string.guided_step_durete_instruction)
                    ) {
                        OutlinedTextField(
                            value = dureteInput,
                            onValueChange = { dureteInput = it },
                            label = { Text(stringResource(R.string.analyse_durete_label)) },
                            placeholder = { Text(stringResource(R.string.analyse_durete_placeholder)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    6 -> GuidedStep(
                        title = stringResource(R.string.analyse_densite_label),
                        instruction = stringResource(R.string.guided_step_densite_instruction)
                    ) {
                        OutlinedTextField(
                            value = densiteInput,
                            onValueChange = { densiteInput = it },
                            label = { Text(stringResource(R.string.analyse_densite_label)) },
                            placeholder = { Text(stringResource(R.string.analyse_densite_placeholder)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    7 -> GuidedStep(
                        title = stringResource(R.string.analyse_pleochroisme_label),
                        instruction = stringResource(R.string.guided_step_pleochroisme_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_pleochroisme_label),
                            selectedLabel = pleochroisme?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.PLEOCHROISME.map { it to localizedLabel(it) },
                            onSelect = { pleochroisme = it }
                        )
                    }
                    8 -> GuidedStep(
                        title = stringResource(R.string.analyse_indice_refraction_label),
                        instruction = stringResource(R.string.guided_step_indice_refraction_instruction)
                    ) {
                        OutlinedTextField(
                            value = indiceRefractionInput,
                            onValueChange = { indiceRefractionInput = it },
                            label = { Text(stringResource(R.string.analyse_indice_refraction_label)) },
                            placeholder = { Text(stringResource(R.string.analyse_indice_refraction_placeholder)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    9 -> GuidedStep(
                        title = stringResource(R.string.analyse_systeme_cristallin_label),
                        instruction = stringResource(R.string.guided_step_systeme_cristallin_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_systeme_cristallin_label),
                            selectedLabel = systemeCristallin?.let { localizedLabel(it) },
                            options = systemesCristallins.map { it to localizedLabel(it) },
                            onSelect = { systemeCristallin = it }
                        )
                    }
                    else -> GuidedStep(
                        title = stringResource(R.string.analyse_fluorescence_label),
                        instruction = stringResource(R.string.guided_step_fluorescence_instruction)
                    ) {
                        DropdownField(
                            label = stringResource(R.string.analyse_fluorescence_label),
                            selectedLabel = fluorescence?.let { localizedLabel(it) },
                            options = AnalysisVocabulary.FLUORESCENCE.map { it to localizedLabel(it) },
                            onSelect = { fluorescence = it }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    if (stepIndex > 0) {
                        OutlinedButton(onClick = { stepIndex-- }) {
                            Text(stringResource(R.string.guided_previous_button))
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            if (stepIndex >= TOTAL_STEPS - 1) {
                                results = runCatching { GemAnalyzer.analyze(currentCriteria()) }.getOrDefault(emptyList())
                            } else {
                                stepIndex++
                            }
                        }
                    ) {
                        Text(
                            if (stepIndex >= TOTAL_STEPS - 1) {
                                stringResource(R.string.guided_finish_button)
                            } else {
                                stringResource(R.string.guided_next_button)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GuidedStep(title: String, instruction: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
    content()
}
