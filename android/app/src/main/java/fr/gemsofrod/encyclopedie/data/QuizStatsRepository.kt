package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf

private const val PREFS_NAME = "gems_of_rod_quiz_stats"

/** Nombre de réponses données et de bonnes réponses pour un type de question. */
data class QuizTypeStat(val attempts: Int, val correct: Int) {
    val errorCount: Int get() = attempts - correct
}

/**
 * Historique des réponses du quiz par type de question ([QuizQuestionType]),
 * persisté localement (SharedPreferences, deux entiers par type) et exposé
 * comme état observable par Compose. Sert uniquement à pondérer un tirage
 * de révision ciblée ([targetedTypePool]) vers les types où l'utilisateur
 * se trompe le plus — pas d'écran de statistiques dédié, ni de nom de
 * catégorie affiché : juste un bouton « Réviser mes points faibles » sur
 * l'écran d'accueil du quiz.
 */
object QuizStatsRepository {
    private var prefs: SharedPreferences? = null
    private val stats = mutableStateMapOf<QuizQuestionType, QuizTypeStat>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        QuizQuestionType.entries.forEach { type ->
            val attempts = sharedPrefs.getInt(attemptsKey(type), 0)
            val correct = sharedPrefs.getInt(correctKey(type), 0)
            if (attempts > 0) stats[type] = QuizTypeStat(attempts, correct)
        }
    }

    /** Au moins une réponse a déjà été enregistrée — active le bouton de révision ciblée. */
    fun hasHistory(): Boolean = stats.values.any { it.attempts > 0 }

    fun recordAnswer(type: QuizQuestionType, correct: Boolean) {
        val current = stats[type] ?: QuizTypeStat(0, 0)
        val updated = QuizTypeStat(current.attempts + 1, current.correct + if (correct) 1 else 0)
        stats[type] = updated
        prefs?.edit()
            ?.putInt(attemptsKey(type), updated.attempts)
            ?.putInt(correctKey(type), updated.correct)
            ?.apply()
    }

    /**
     * Bassin de types pondéré par le nombre d'erreurs passées : chaque type
     * garde un poids de base de 1 (pour rester tirable même sans historique),
     * puis +3 par erreur commise — un type jamais travaillé ou déjà maîtrisé
     * revient rarement, un type où l'utilisateur se trompe souvent revient
     * proportionnellement plus.
     */
    fun targetedTypePool(): List<QuizQuestionType> =
        QuizQuestionType.entries.flatMap { type ->
            val errors = stats[type]?.errorCount ?: 0
            List(1 + errors * 3) { type }
        }

    private fun attemptsKey(type: QuizQuestionType) = "attempts_${type.name}"
    private fun correctKey(type: QuizQuestionType) = "correct_${type.name}"
}
