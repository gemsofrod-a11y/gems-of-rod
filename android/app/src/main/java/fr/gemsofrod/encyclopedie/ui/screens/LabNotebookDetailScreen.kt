package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AnalysisCriteria
import fr.gemsofrod.encyclopedie.data.AnalysisMatch
import fr.gemsofrod.encyclopedie.data.GemAnalyzer
import fr.gemsofrod.encyclopedie.data.LabNotebookRepository
import fr.gemsofrod.encyclopedie.ui.components.AnalysisResultRow
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import java.text.DateFormat
import java.util.Date

/**
 * Fiche détail d'un échantillon du carnet de terrain : les mesures relevées,
 * les notes libres, et une comparaison au catalogue à la demande (le même
 * moteur que l'analyse rapide/guidée, [GemAnalyzer]), sans jamais comparer
 * automatiquement pour laisser l'utilisateur consulter ses notes brutes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabNotebookDetailScreen(
    sampleId: String,
    onGemClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    onBackClick: () -> Unit
) {
    val sample = remember(sampleId) { LabNotebookRepository.sampleById(sampleId) }

    if (sample == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf<List<AnalysisMatch>?>(null) }
    val dateFormat = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sample.label) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(sample.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.notebook_edit_button))
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.notebook_delete_button))
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
            Text(
                text = dateFormat.format(Date(sample.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (sample.notes.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.notebook_notes_field),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = sample.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.notebook_criteria_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (sample.criteria.isEmpty) {
                Text(
                    text = stringResource(R.string.notebook_no_criteria),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        CriteriaRows(sample.criteria)
                    }
                }
            }

            Button(onClick = { results = GemAnalyzer.analyze(sample.criteria) }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.notebook_compare_button))
            }

            val currentResults = results
            if (currentResults != null) {
                if (currentResults.isEmpty()) {
                    Text(
                        text = stringResource(R.string.analyse_no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = stringResource(R.string.analyse_results_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        currentResults.forEach { match ->
                            AnalysisResultRow(match = match, onClick = { onGemClick(match.gem.id) })
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.notebook_delete_confirm_title)) },
            text = { Text(stringResource(R.string.notebook_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    LabNotebookRepository.deleteSample(sample.id)
                    showDeleteConfirm = false
                    onDeleted()
                }) {
                    Text(stringResource(R.string.notebook_delete_confirm_button))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.notebook_cancel_button))
                }
            }
        )
    }
}

/** Formate une mesure pour l'affichage : entier si possible ("7"), sinon décimal ("7.5"). */
private fun formatMeasurement(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

@Composable
private fun CriteriaRows(criteria: AnalysisCriteria) {
    criteria.couleur?.let { CriteriaRow(stringResource(R.string.analyse_couleur_label), stringResource(it.labelRes)) }
    criteria.transparence?.let { CriteriaRow(stringResource(R.string.analyse_transparence_label), localizedLabel(it)) }
    criteria.eclat?.let { CriteriaRow(stringResource(R.string.analyse_eclat_label), localizedLabel(it)) }
    criteria.clivage?.let { CriteriaRow(stringResource(R.string.analyse_clivage_label), localizedLabel(it)) }
    criteria.typeInclusion?.let { CriteriaRow(stringResource(R.string.analyse_type_inclusion_label), localizedLabel(it)) }
    criteria.durete?.let { CriteriaRow(stringResource(R.string.analyse_durete_label), formatMeasurement(it)) }
    criteria.densite?.let { CriteriaRow(stringResource(R.string.analyse_densite_label), formatMeasurement(it)) }
    criteria.pleochroisme?.let { CriteriaRow(stringResource(R.string.analyse_pleochroisme_label), localizedLabel(it)) }
    criteria.indiceRefraction?.let {
        CriteriaRow(stringResource(R.string.analyse_indice_refraction_label), formatMeasurement(it))
    }
    criteria.systemeCristallin?.let {
        CriteriaRow(stringResource(R.string.analyse_systeme_cristallin_label), localizedLabel(it))
    }
    criteria.fluorescence?.let { CriteriaRow(stringResource(R.string.analyse_fluorescence_label), localizedLabel(it)) }
}

@Composable
private fun CriteriaRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
