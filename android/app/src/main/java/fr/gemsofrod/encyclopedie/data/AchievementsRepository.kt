package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

private const val PREFS_NAME = "gems_of_rod_achievements"
private const val KEY_VIEWED_GEM_IDS = "viewed_gem_ids"
private const val KEY_QUIZZES_COMPLETED = "quizzes_completed"
private const val KEY_PERFECT_QUIZ_SCORE = "perfect_quiz_score"
private const val KEY_LEGENDARY_RIDDLE_SOLVED = "legendary_riddle_solved"

/**
 * Statistiques de progression utilisées pour déterminer quels succès
 * ([Achievements.BADGES]) sont débloqués. [favoritesCount] est lu
 * directement depuis [FavoritesRepository] plutôt que dupliqué ici.
 */
data class AchievementStats(
    val gemsViewedCount: Int,
    val colorsCoveredCount: Int,
    val favoritesCount: Int,
    val quizzesCompleted: Int,
    val hasPerfectQuizScore: Boolean,
    val hasSolvedLegendaryRiddle: Boolean
)

/**
 * Suit la progression de l'utilisateur (gemmes consultées, quiz terminés,
 * score parfait obtenu) pour débloquer les succès, persistée localement
 * (SharedPreferences) et exposée comme état observable par Compose — même
 * principe que [FavoritesRepository].
 */
object AchievementsRepository {
    private var prefs: SharedPreferences? = null
    private val viewedGemIds = mutableStateListOf<String>()
    private var quizzesCompleted by mutableIntStateOf(0)
    private var hasPerfectQuizScore by mutableStateOf(false)
    private var hasSolvedLegendaryRiddle by mutableStateOf(false)

    /** Succès venant d'être débloqués, en attente d'affichage (notification). */
    private val pendingUnlocks = mutableStateListOf<String>()

    fun init(context: Context) {
        if (prefs != null) return
        val sharedPrefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = sharedPrefs
        viewedGemIds.addAll(sharedPrefs.getStringSet(KEY_VIEWED_GEM_IDS, emptySet()).orEmpty())
        quizzesCompleted = sharedPrefs.getInt(KEY_QUIZZES_COMPLETED, 0)
        hasPerfectQuizScore = sharedPrefs.getBoolean(KEY_PERFECT_QUIZ_SCORE, false)
        hasSolvedLegendaryRiddle = sharedPrefs.getBoolean(KEY_LEGENDARY_RIDDLE_SOLVED, false)
    }

    fun recordGemViewed(gemId: String) {
        if (viewedGemIds.contains(gemId)) return
        val before = stats()
        viewedGemIds.add(gemId)
        prefs?.edit()?.putStringSet(KEY_VIEWED_GEM_IDS, viewedGemIds.toSet())?.apply()
        checkNewlyUnlocked(before, stats())
    }

    fun recordQuizCompleted(score: Int, total: Int) {
        val before = stats()
        quizzesCompleted++
        prefs?.edit()?.putInt(KEY_QUIZZES_COMPLETED, quizzesCompleted)?.apply()
        if (total > 0 && score == total) {
            hasPerfectQuizScore = true
            prefs?.edit()?.putBoolean(KEY_PERFECT_QUIZ_SCORE, true)?.apply()
        }
        checkNewlyUnlocked(before, stats())
    }

    fun recordLegendaryRiddleSolved() {
        if (hasSolvedLegendaryRiddle) return
        val before = stats()
        hasSolvedLegendaryRiddle = true
        prefs?.edit()?.putBoolean(KEY_LEGENDARY_RIDDLE_SOLVED, true)?.apply()
        checkNewlyUnlocked(before, stats())
    }

    fun stats(): AchievementStats {
        val colorsCovered = viewedGemIds.mapNotNull { GemsRepository.byId(it)?.couleur }.distinct().size
        return AchievementStats(
            gemsViewedCount = viewedGemIds.size,
            colorsCoveredCount = colorsCovered,
            favoritesCount = FavoritesRepository.favoriteIds().size,
            quizzesCompleted = quizzesCompleted,
            hasPerfectQuizScore = hasPerfectQuizScore,
            hasSolvedLegendaryRiddle = hasSolvedLegendaryRiddle
        )
    }

    /**
     * Compare la progression avant/après une action (gemme consultée, quiz
     * terminé, favori ajouté) et met en attente les succès qui viennent de
     * passer de verrouillé à débloqué, pour être notifiés à l'utilisateur.
     * Appelée aussi depuis [FavoritesRepository.toggle], seule autre action
     * de l'app capable de débloquer un succès.
     */
    fun checkNewlyUnlocked(before: AchievementStats, after: AchievementStats) {
        val unlockedBefore = Achievements.BADGES.filter { it.isUnlocked(before) }.map { it.id }.toSet()
        val unlockedAfter = Achievements.BADGES.filter { it.isUnlocked(after) }.map { it.id }.toSet()
        pendingUnlocks.addAll((unlockedAfter - unlockedBefore).sorted())
    }

    /** Retire et renvoie le prochain succès débloqué en attente de notification, ou `null`. */
    fun consumePendingUnlock(): String? = if (pendingUnlocks.isEmpty()) null else pendingUnlocks.removeAt(0)
}
