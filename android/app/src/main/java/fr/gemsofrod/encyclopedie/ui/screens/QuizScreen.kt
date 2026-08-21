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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import fr.gemsofrod.encyclopedie.data.FossileFamille
import fr.gemsofrod.encyclopedie.data.FossilesRepository
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemGlossary
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.QuizEngine
import fr.gemsofrod.encyclopedie.data.QuizQuestion
import fr.gemsofrod.encyclopedie.data.QuizQuestionType
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.localizedLabel

/**
 * Quiz de révision gemmologique à choix multiples : couleur, famille
 * minérale et rareté piochées dans le catalogue, vocabulaire piochée dans
 * le lexique. Un nouveau tirage aléatoire de [QuizEngine.QUESTION_COUNT]
 * questions est généré à chaque partie ; le score n'est pas persisté, le
 * quiz sert à réviser dans l'instant plutôt qu'à suivre une progression.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBackClick: () -> Unit) {
    var quiz by remember { mutableStateOf<List<QuizQuestion>?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedChoice by remember { mutableStateOf<Int?>(null) }

    fun startQuiz() {
        quiz = QuizEngine.generateQuiz()
        currentIndex = 0
        score = 0
        selectedChoice = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.quiz_title)) },
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
        val currentQuiz = quiz
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                currentQuiz == null -> QuizIntro(onStartClick = { startQuiz() })
                currentIndex >= currentQuiz.size -> QuizResult(
                    score = score,
                    total = currentQuiz.size,
                    onRestartClick = { startQuiz() }
                )
                else -> QuizQuestionContent(
                    question = currentQuiz[currentIndex],
                    index = currentIndex,
                    total = currentQuiz.size,
                    selectedChoice = selectedChoice,
                    onChoiceSelected = { choiceIndex ->
                        if (selectedChoice == null) {
                            selectedChoice = choiceIndex
                            if (choiceIndex == currentQuiz[currentIndex].correctIndex) score++
                        }
                    },
                    onNextClick = {
                        val nextIndex = currentIndex + 1
                        if (nextIndex >= currentQuiz.size) {
                            AchievementsRepository.recordQuizCompleted(score, currentQuiz.size)
                        }
                        currentIndex = nextIndex
                        selectedChoice = null
                    }
                )
            }
        }
    }
}

@Composable
private fun QuizIntro(onStartClick: () -> Unit) {
    Text(
        text = stringResource(R.string.quiz_intro, QuizEngine.QUESTION_COUNT),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.quiz_start_button))
    }
}

@Composable
private fun QuizQuestionContent(
    question: QuizQuestion,
    index: Int,
    total: Int,
    selectedChoice: Int?,
    onChoiceSelected: (Int) -> Unit,
    onNextClick: () -> Unit
) {
    Text(
        text = stringResource(R.string.quiz_progress, index + 1, total),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
        text = questionPrompt(question),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
    if (question.type == QuizQuestionType.GLOSSAIRE) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = glossaryDefinition(question),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val choiceCount = if (question.type == QuizQuestionType.GLOSSAIRE) {
            question.glossaryChoiceIndices.size
        } else {
            question.choiceKeys.size
        }
        for (choiceIndex in 0 until choiceCount) {
            val state = when {
                selectedChoice == null -> QuizChoiceState.NEUTRAL
                choiceIndex == question.correctIndex -> QuizChoiceState.CORRECT
                choiceIndex == selectedChoice -> QuizChoiceState.INCORRECT
                else -> QuizChoiceState.NEUTRAL
            }
            QuizChoiceRow(
                label = choiceLabel(question, choiceIndex),
                state = state,
                onClick = { onChoiceSelected(choiceIndex) }
            )
        }
    }
    if (selectedChoice != null) {
        Button(onClick = onNextClick, modifier = Modifier.fillMaxWidth()) {
            Text(
                if (index + 1 < total) stringResource(R.string.quiz_next_button)
                else stringResource(R.string.quiz_finish_button)
            )
        }
    }
}

private enum class QuizChoiceState { NEUTRAL, CORRECT, INCORRECT }

@Composable
private fun QuizChoiceRow(label: String, state: QuizChoiceState, onClick: () -> Unit) {
    val containerColor = when (state) {
        QuizChoiceState.NEUTRAL -> MaterialTheme.colorScheme.surface
        QuizChoiceState.CORRECT -> MaterialTheme.colorScheme.primaryContainer
        QuizChoiceState.INCORRECT -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (state) {
        QuizChoiceState.NEUTRAL -> MaterialTheme.colorScheme.onSurface
        QuizChoiceState.CORRECT -> MaterialTheme.colorScheme.onPrimaryContainer
        QuizChoiceState.INCORRECT -> MaterialTheme.colorScheme.onErrorContainer
    }
    Card(
        onClick = onClick,
        enabled = state == QuizChoiceState.NEUTRAL,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun QuizResult(score: Int, total: Int, onRestartClick: () -> Unit) {
    Text(
        text = stringResource(R.string.quiz_score_title, score, total),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = quizResultMessage(score, total),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Button(onClick = onRestartClick, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.quiz_restart_button))
    }
}

@Composable
private fun quizResultMessage(score: Int, total: Int): String {
    val ratio = score.toFloat() / total
    return when {
        ratio >= 0.9f -> stringResource(R.string.quiz_result_excellent)
        ratio >= 0.6f -> stringResource(R.string.quiz_result_good)
        else -> stringResource(R.string.quiz_result_encourage)
    }
}

@Composable
private fun questionPrompt(question: QuizQuestion): String = when (question.type) {
    QuizQuestionType.COULEUR -> {
        val gem = GemsRepository.byId(question.gemId.orEmpty())?.localized()
        stringResource(R.string.quiz_question_couleur, gem?.nom.orEmpty())
    }
    QuizQuestionType.FAMILLE -> {
        val gem = GemsRepository.byId(question.gemId.orEmpty())?.localized()
        stringResource(R.string.quiz_question_famille, gem?.nom.orEmpty())
    }
    QuizQuestionType.RARETE -> {
        val gem = GemsRepository.byId(question.gemId.orEmpty())?.localized()
        stringResource(R.string.quiz_question_rarete, gem?.nom.orEmpty())
    }
    QuizQuestionType.GLOSSAIRE -> stringResource(R.string.quiz_question_glossaire)
    QuizQuestionType.FOSSILE_FAMILLE -> {
        val fossile = FossilesRepository.byId(question.fossileId.orEmpty())?.localized()
        stringResource(R.string.quiz_question_fossile_famille, fossile?.nom.orEmpty())
    }
}

@Composable
private fun glossaryDefinition(question: QuizQuestion): String {
    val languageCode = LocalConfiguration.current.locales[0].language
    val termIndex = question.glossaryChoiceIndices[question.correctIndex]
    return GemGlossary.page(languageCode).termes[termIndex].definition
}

@Composable
private fun choiceLabel(question: QuizQuestion, index: Int): String = when (question.type) {
    QuizQuestionType.COULEUR -> stringResource(GemColorCategory.valueOf(question.choiceKeys[index]).labelRes)
    QuizQuestionType.RARETE -> stringResource(GemRarete.valueOf(question.choiceKeys[index]).labelRes)
    QuizQuestionType.FAMILLE -> localizedLabel(question.choiceKeys[index])
    QuizQuestionType.GLOSSAIRE -> {
        val languageCode = LocalConfiguration.current.locales[0].language
        GemGlossary.page(languageCode).termes[question.glossaryChoiceIndices[index]].terme
    }
    QuizQuestionType.FOSSILE_FAMILLE -> stringResource(FossileFamille.valueOf(question.choiceKeys[index]).labelRes)
}
