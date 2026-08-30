package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R

/**
 * Hub du laboratoire gemmologique : point d'entrée unique vers les outils
 * d'identification (analyse rapide par formulaire, analyse guidée pas-à-pas)
 * et les ressources de référence (instruments, lexique) déjà existantes,
 * jusqu'ici accessibles uniquement depuis l'outil d'analyse rapide.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabMenuScreen(
    onQuickAnalysisClick: () -> Unit,
    onGuidedAnalysisClick: () -> Unit,
    onNotebookClick: () -> Unit,
    onInstrumentsClick: () -> Unit,
    onGlossaireClick: () -> Unit,
    onReflectivityMeterClick: () -> Unit,
    onTreatmentsClick: () -> Unit,
    onStockClick: () -> Unit,
    onLapidaireClick: () -> Unit,
    onLegendaryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.lab_menu_title)) },
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
                text = stringResource(R.string.lab_menu_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LabMenuCard(
                icon = Icons.Filled.Science,
                title = stringResource(R.string.lab_quick_analysis_title),
                subtitle = stringResource(R.string.lab_quick_analysis_subtitle),
                onClick = onQuickAnalysisClick
            )
            LabMenuCard(
                icon = Icons.Filled.Biotech,
                title = stringResource(R.string.lab_guided_analysis_title),
                subtitle = stringResource(R.string.lab_guided_analysis_subtitle),
                onClick = onGuidedAnalysisClick
            )
            LabMenuCard(
                icon = Icons.Filled.Edit,
                title = stringResource(R.string.lab_notebook_title),
                subtitle = stringResource(R.string.lab_notebook_subtitle),
                onClick = onNotebookClick
            )
            LabMenuCard(
                icon = Icons.Filled.Info,
                title = stringResource(R.string.instruments_title),
                subtitle = stringResource(R.string.analyse_instruments_card_subtitle),
                onClick = onInstrumentsClick
            )
            LabMenuCard(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                title = stringResource(R.string.glossaire_title),
                subtitle = stringResource(R.string.analyse_glossaire_card_subtitle),
                onClick = onGlossaireClick
            )
            LabMenuCard(
                icon = Icons.Filled.CameraAlt,
                title = stringResource(R.string.lab_reflectivity_title),
                subtitle = stringResource(R.string.lab_reflectivity_subtitle),
                onClick = onReflectivityMeterClick
            )
            LabMenuCard(
                icon = Icons.Filled.Verified,
                title = stringResource(R.string.lab_treatments_title),
                subtitle = stringResource(R.string.lab_treatments_subtitle),
                onClick = onTreatmentsClick
            )
            LabMenuCard(
                icon = Icons.Filled.Inventory2,
                title = stringResource(R.string.lab_stock_title),
                subtitle = stringResource(R.string.lab_stock_subtitle),
                onClick = onStockClick
            )
            LabMenuCard(
                icon = Icons.Filled.Diamond,
                title = stringResource(R.string.lab_lapidaire_title),
                subtitle = stringResource(R.string.lab_lapidaire_subtitle),
                onClick = onLapidaireClick
            )
            LabMenuCard(
                icon = Icons.Filled.Public,
                title = stringResource(R.string.legendary_menu_title),
                subtitle = stringResource(R.string.legendary_menu_subtitle),
                onClick = onLegendaryClick
            )
        }
    }
}

@Composable
private fun LabMenuCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
