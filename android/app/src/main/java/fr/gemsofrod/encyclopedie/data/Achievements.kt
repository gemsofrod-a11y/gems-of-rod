package fr.gemsofrod.encyclopedie.data

/** Regroupe les succès par thème pour l'affichage. */
enum class BadgeCategory { EXPLORATION, FAVORIS, QUIZ }

/**
 * Un succès à débloquer dans l'app. [threshold] est la valeur cible pour un
 * compteur de progression donné par [progress] ; le succès est débloqué dès
 * que [progress] atteint [threshold]. Les libellés affichés sont résolus
 * dans l'UI via les ressources de chaînes `achievement_<id>_title` /
 * `achievement_<id>_desc`, pas stockés ici.
 */
data class Badge(
    val id: String,
    val category: BadgeCategory,
    val emoji: String,
    val threshold: Int,
    val progress: (AchievementStats) -> Int
) {
    fun isUnlocked(stats: AchievementStats): Boolean = progress(stats) >= threshold
}

/**
 * Liste des succès proposés par l'app, dérivés du catalogue (gemmes
 * consultées, couleurs découvertes), des favoris et du quiz gemmologique.
 * Les seuils liés au catalogue ([GemsRepository.gems] et
 * [GemColorCategory.entries]) sont calculés dynamiquement pour rester
 * corrects si le catalogue grandit.
 */
object Achievements {
    val BADGES: List<Badge> = listOf(
        Badge("first_gem", BadgeCategory.EXPLORATION, "🔎", threshold = 1) { it.gemsViewedCount },
        Badge("curious", BadgeCategory.EXPLORATION, "🧐", threshold = 10) { it.gemsViewedCount },
        Badge("collector", BadgeCategory.EXPLORATION, "📚", threshold = 50) { it.gemsViewedCount },
        Badge("encyclopedist", BadgeCategory.EXPLORATION, "🎓", threshold = 100) { it.gemsViewedCount },
        Badge("master", BadgeCategory.EXPLORATION, "👑", threshold = GemsRepository.gems.size) { it.gemsViewedCount },
        Badge("rainbow", BadgeCategory.EXPLORATION, "🌈", threshold = GemColorCategory.entries.size) { it.colorsCoveredCount },
        Badge("first_favorite", BadgeCategory.FAVORIS, "💛", threshold = 1) { it.favoritesCount },
        Badge("fan", BadgeCategory.FAVORIS, "💖", threshold = 10) { it.favoritesCount },
        Badge("big_collector", BadgeCategory.FAVORIS, "💎", threshold = 25) { it.favoritesCount },
        Badge("first_quiz", BadgeCategory.QUIZ, "🧠", threshold = 1) { it.quizzesCompleted },
        Badge("quiz_regular", BadgeCategory.QUIZ, "🏅", threshold = 10) { it.quizzesCompleted },
        Badge("perfect_score", BadgeCategory.QUIZ, "🏆", threshold = 1) { if (it.hasPerfectQuizScore) 1 else 0 }
    )
}
