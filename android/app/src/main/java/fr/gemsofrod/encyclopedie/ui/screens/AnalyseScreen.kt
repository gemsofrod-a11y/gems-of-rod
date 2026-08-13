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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AnalysisCriteria
import fr.gemsofrod.encyclopedie.data.AnalysisMatch
import fr.gemsofrod.encyclopedie.data.AnalysisVocabulary
import fr.gemsofrod.encyclopedie.data.GemAnalyzer
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.ui.components.AnalysisResultRow
import fr.gemsofrod.encyclopedie.ui.components.DropdownField
import fr.gemsofrod.encyclopedie.ui.localizedLabel

/**
 * Outil d'identification de pierre : l'utilisateur renseigne les
 * caractéristiques qu'il connaît ou observe (couleur, transparence, éclat,
 * clivage, pléochroïsme, fluorescence UV, système cristallin, dureté,
 * densité, indice de réfraction), tous facultatifs, et l'app renvoie les
 * gemmes du catalogue les plus proches, classées par nombre de critères
 * correspondants. Une carte donne accès à une page présentant les
 * instruments gemmologiques utilisés pour mesurer ces caractéristiques.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyseScreen(
    onGemClick: (String) -> Unit,
    onInstrumentsClick: () -> Unit,
    onGlossaireClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var couleur by remember { mutableStateOf<GemColorCategory?>(null) }
    var transparence by remember { mutableStateOf<String?>(null) }
    var eclat by remember { mutableStateOf<String?>(null) }
    var clivage by remember { mutableStateOf<String?>(null) }
    var systemeCristallin by remember { mutableStateOf<String?>(null) }
    var dureteInput by remember { mutableStateOf("") }
    var densiteInput by remember { mutableStateOf("") }
    var indiceRefractionInput by remember { mutableStateOf("") }
    var pleochroisme by remember { mutableStateOf<String?>(null) }
    var fluorescence by remember { mutableStateOf<String?>(null) }
    var typeInclusion by remember { mutableStateOf<String?>(null) }
    var results by remember { mutableStateOf<List<AnalysisMatch>?>(null) }

    val systemesCristallins = remember { GemAnalyzer.systemesCristallins() }

    fun parseDecimal(input: String): Double? = input.trim().replace(',', '.').toDoubleOrNull()

    fun runAnalysis() {
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
        results = runCatching { GemAnalyzer.analyze(criteria) }.getOrDefault(emptyList())
    }

    fun reset() {
        couleur = null
        transparence = null
        eclat = null
        clivage = null
        systemeCristallin = null
        dureteInput = ""
        densiteInput = ""
        indiceRefractionInput = ""
        pleochroisme = null
        fluorescence = null
        typeInclusion = null
        results = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gemmologie_analyse_title)) },
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
            Text(
                text = stringResource(R.string.analyse_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                onClick = onInstrumentsClick,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.analyse_instruments_card_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = stringResource(R.string.analyse_instruments_card_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Card(
                onClick = onGlossaireClick,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.analyse_glossaire_card_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = stringResource(R.string.analyse_glossaire_card_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

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

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { runAnalysis() }, modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.analyse_button))
                        }
                        OutlinedButton(onClick = { reset() }) {
                            Text(stringResource(R.string.analyse_reset_button))
                        }
                    }
                }
            }

            val currentResults = results
            when {
                currentResults == null -> {
                    Text(
                        text = stringResource(R.string.analyse_empty_state),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                currentResults.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.analyse_no_results),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    Text(
                        text = stringResource(R.string.analyse_results_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        currentResults.forEach { match ->
                            AnalysisResultRow(match = match, onClick = { onGemClick(match.gem.id) })
                        }
                    }
                }
            }
        }
    }
}
