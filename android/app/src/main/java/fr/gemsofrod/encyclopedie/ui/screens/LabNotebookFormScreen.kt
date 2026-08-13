package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import fr.gemsofrod.encyclopedie.data.LabNotebookRepository
import fr.gemsofrod.encyclopedie.ui.components.DropdownField
import fr.gemsofrod.encyclopedie.ui.localizedLabel

/**
 * Formulaire d'ajout ou de modification d'une fiche du carnet de terrain.
 * Reprend les mêmes champs de mesure que l'outil d'analyse
 * ([AnalyseScreen]), plus un nom d'échantillon (obligatoire) et des notes
 * libres (contexte, provenance...). [sampleId] `null` crée une nouvelle
 * fiche ; sinon la fiche existante est chargée puis mise à jour à
 * l'enregistrement.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabNotebookFormScreen(
    sampleId: String?,
    onSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    val existing = remember(sampleId) { sampleId?.let { LabNotebookRepository.sampleById(it) } }

    var label by remember { mutableStateOf(existing?.label ?: "") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }
    var couleur by remember { mutableStateOf(existing?.criteria?.couleur) }
    var transparence by remember { mutableStateOf(existing?.criteria?.transparence) }
    var eclat by remember { mutableStateOf(existing?.criteria?.eclat) }
    var clivage by remember { mutableStateOf(existing?.criteria?.clivage) }
    var systemeCristallin by remember { mutableStateOf(existing?.criteria?.systemeCristallin) }
    var dureteInput by remember { mutableStateOf(existing?.criteria?.durete?.let { formatMeasurement(it) } ?: "") }
    var densiteInput by remember { mutableStateOf(existing?.criteria?.densite?.let { formatMeasurement(it) } ?: "") }
    var indiceRefractionInput by remember {
        mutableStateOf(existing?.criteria?.indiceRefraction?.let { formatMeasurement(it) } ?: "")
    }
    var pleochroisme by remember { mutableStateOf(existing?.criteria?.pleochroisme) }
    var fluorescence by remember { mutableStateOf(existing?.criteria?.fluorescence) }
    var typeInclusion by remember { mutableStateOf(existing?.criteria?.typeInclusion) }

    val systemesCristallins = remember { GemAnalyzer.systemesCristallins() }

    fun parseDecimal(input: String): Double? = input.trim().replace(',', '.').toDoubleOrNull()

    fun save() {
        if (label.isBlank()) return
        val criteria = AnalysisCriteria(
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
        if (sampleId != null) {
            LabNotebookRepository.updateSample(sampleId, label.trim(), notes.trim(), criteria)
        } else {
            LabNotebookRepository.addSample(label.trim(), notes.trim(), criteria)
        }
        onSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (sampleId != null) R.string.notebook_form_edit_title else R.string.notebook_form_new_title
                        )
                    )
                },
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
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text(stringResource(R.string.notebook_label_field)) },
                placeholder = { Text(stringResource(R.string.notebook_label_placeholder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(stringResource(R.string.notebook_notes_field)) },
                placeholder = { Text(stringResource(R.string.notebook_notes_placeholder)) },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.notebook_criteria_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DropdownField(
                        label = stringResource(R.string.analyse_couleur_label),
                        selectedLabel = couleur?.let { stringResource(it.labelRes) },
                        options = GemColorCategory.entries.map { it to stringResource(it.labelRes) },
                        onSelect = { couleur = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_transparence_label),
                        selectedLabel = transparence?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.TRANSPARENCE.map { it to localizedLabel(it) },
                        onSelect = { transparence = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_eclat_label),
                        selectedLabel = eclat?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.ECLAT.map { it to localizedLabel(it) },
                        onSelect = { eclat = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_clivage_label),
                        selectedLabel = clivage?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.CLIVAGE.map { it to localizedLabel(it) },
                        onSelect = { clivage = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_pleochroisme_label),
                        selectedLabel = pleochroisme?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.PLEOCHROISME.map { it to localizedLabel(it) },
                        onSelect = { pleochroisme = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_fluorescence_label),
                        selectedLabel = fluorescence?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.FLUORESCENCE.map { it to localizedLabel(it) },
                        onSelect = { fluorescence = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_type_inclusion_label),
                        selectedLabel = typeInclusion?.let { localizedLabel(it) },
                        options = AnalysisVocabulary.TYPE_INCLUSION.map { it to localizedLabel(it) },
                        onSelect = { typeInclusion = it }
                    )
                    DropdownField(
                        label = stringResource(R.string.analyse_systeme_cristallin_label),
                        selectedLabel = systemeCristallin?.let { localizedLabel(it) },
                        options = systemesCristallins.map { it to localizedLabel(it) },
                        onSelect = { systemeCristallin = it }
                    )
                    OutlinedTextField(
                        value = dureteInput,
                        onValueChange = { dureteInput = it },
                        label = { Text(stringResource(R.string.analyse_durete_label)) },
                        placeholder = { Text(stringResource(R.string.analyse_durete_placeholder)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = densiteInput,
                        onValueChange = { densiteInput = it },
                        label = { Text(stringResource(R.string.analyse_densite_label)) },
                        placeholder = { Text(stringResource(R.string.analyse_densite_placeholder)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
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
            }

            Button(
                onClick = { save() },
                enabled = label.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.notebook_save_button))
            }
        }
    }
}

/** Formate une mesure pour un champ de saisie : entier si possible ("7"), sinon décimal ("7.5"). */
private fun formatMeasurement(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
