package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.LearningPathRepository

private data class LearningPathStep(
    val id: String,
    val titleRes: Int,
    val subtitleRes: Int,
    val onClick: () -> Unit
)

/**
 * Progression guidée à travers les bases de la gemmologie déjà présentes
 * dans l'app (vocabulaire, familles, cristallographie, achat, lithothérapie,
 * quiz) : chaque étape ouvre un écran existant et se marque comme vue dès
 * qu'elle est ouverte — pas de nouveau contenu, juste un ordre suggéré et
 * une progression visible ([LearningPathRepository]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathScreen(
    onBackClick: () -> Unit,
    onVocabulaireClick: () -> Unit,
    onFamillesClick: () -> Unit,
    onCristauxClick: () -> Unit,
    onAchatClick: () -> Unit,
    onLithotherapieClick: () -> Unit,
    onQuizClick: () -> Unit
) {
    val steps = listOf(
        LearningPathStep("vocabulaire", R.string.learning_path_step_vocabulaire_title, R.string.learning_path_step_vocabulaire_subtitle, onVocabulaireClick),
        LearningPathStep("familles", R.string.learning_path_step_familles_title, R.string.learning_path_step_familles_subtitle, onFamillesClick),
        LearningPathStep("cristaux", R.string.learning_path_step_cristaux_title, R.string.learning_path_step_cristaux_subtitle, onCristauxClick),
        LearningPathStep("achat", R.string.learning_path_step_achat_title, R.string.learning_path_step_achat_subtitle, onAchatClick),
        LearningPathStep("lithotherapie", R.string.learning_path_step_lithotherapie_title, R.string.learning_path_step_lithotherapie_subtitle, onLithotherapieClick),
        LearningPathStep("quiz", R.string.learning_path_step_quiz_title, R.string.learning_path_step_quiz_subtitle, onQuizClick)
    )
    val completed = steps.count { LearningPathRepository.isCompleted(it.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.learning_path_title)) },
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.learning_path_progress_format, completed, steps.size),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LinearProgressIndicator(
                        progress = { completed.toFloat() / steps.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
            }
            items(steps) { step ->
                LearningPathStepRow(
                    index = steps.indexOf(step) + 1,
                    titleRes = step.titleRes,
                    subtitleRes = step.subtitleRes,
                    done = LearningPathRepository.isCompleted(step.id),
                    onClick = {
                        LearningPathRepository.markCompleted(step.id)
                        step.onClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun LearningPathStepRow(index: Int, titleRes: Int, subtitleRes: Int, done: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
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
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (done) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
