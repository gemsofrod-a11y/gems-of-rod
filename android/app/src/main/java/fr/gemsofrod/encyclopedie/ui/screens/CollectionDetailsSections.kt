package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.ui.localizedCollectionDetails

/**
 * Rubriques approfondies (formation et origine, structure, provenance,
 * traditions) d'une fiche fossile, coquillage ou météorite, affichées sous la
 * fiche technique. N'affiche rien si la fiche n'a pas encore été enrichie.
 */
@Composable
fun CollectionDetailsSections(id: String) {
    val details = localizedCollectionDetails(id) ?: return
    CollectionDetailSection(stringResource(R.string.collection_details_origin_title), details.formation)
    CollectionDetailSection(stringResource(R.string.collection_details_structure_title), details.cristallographie)
    CollectionDetailSection(stringResource(R.string.collection_details_provenance_title), details.gisements)
    CollectionDetailSection(stringResource(R.string.collection_details_traditions_title), details.lithotherapie)
}

@Composable
private fun CollectionDetailSection(title: String, text: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
