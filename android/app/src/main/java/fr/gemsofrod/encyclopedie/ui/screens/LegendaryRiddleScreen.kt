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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.LegendaryRiddleState

private val CLUE_RESOURCES = listOf(
    R.string.legendary_clue_1,
    R.string.legendary_clue_2,
    R.string.legendary_clue_3,
    R.string.legendary_clue_4
)

/**
 * Énigme du succès légendaire caché : révèle un indice à la fois sur une
 * gemme précise du catalogue sans jamais la nommer, jusqu'à ce que
 * l'utilisateur soit prêt à désigner son pays d'origine sur la carte
 * ([LegendaryMapScreen]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendaryRiddleScreen(
    onOpenMapClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var cluesRevealed by LegendaryRiddleState.cluesRevealed

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.legendary_riddle_title)) },
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
                text = stringResource(R.string.legendary_riddle_intro),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            for (i in 0 until cluesRevealed) {
                ClueCard(index = i + 1, text = stringResource(CLUE_RESOURCES[i]))
            }

            if (cluesRevealed < CLUE_RESOURCES.size) {
                Button(onClick = { cluesRevealed++ }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.legendary_next_clue_button))
                }
            } else {
                Button(onClick = onOpenMapClick, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.legendary_open_map_button))
                }
            }
        }
    }
}

@Composable
private fun ClueCard(index: Int, text: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.legendary_clue_label, index),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
