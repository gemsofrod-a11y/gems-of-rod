package fr.gemsofrod.encyclopedie.data

/** Type de question posée dans le quiz de révision gemmologique. */
enum class QuizQuestionType { COULEUR, FAMILLE, RARETE, GLOSSAIRE, FOSSILE_FAMILLE }

/**
 * Une question du quiz. Les champs pertinents dépendent de [type] :
 * - [COULEUR], [RARETE] : [gemId] renseigné, [choiceKeys] contient les noms
 *   d'enum ([GemColorCategory.name] ou [GemRarete.name]) dont un correspond
 *   à [correctIndex].
 * - [FAMILLE] : [gemId] renseigné, [choiceKeys] contient des noms de famille
 *   en français (clé canonique de [LabelLocalization]).
 * - [GLOSSAIRE] : [glossaryChoiceIndices] contient des index dans
 *   `GemGlossary.page(langue).termes`, dont un correspond à [correctIndex] ;
 *   la définition à afficher est celle du terme à cet index.
 * - [FOSSILE_FAMILLE] : [fossileId] renseigné, [choiceKeys] contient les
 *   noms d'enum [FossileFamille.name] dont un correspond à [correctIndex].
 */
data class QuizQuestion(
    val type: QuizQuestionType,
    val correctIndex: Int,
    val gemId: String? = null,
    val fossileId: String? = null,
    val choiceKeys: List<String> = emptyList(),
    val glossaryChoiceIndices: List<Int> = emptyList()
)

/**
 * Génère des quiz de révision à choix multiples à partir du catalogue de
 * gemmes (couleur, famille minérale, rareté) et du lexique gemmologique
 * (association définition <-> terme), pour un usage pédagogique léger sans
 * dépendance externe. Les mauvaises réponses sont piochées aléatoirement
 * parmi les autres valeurs possibles, si bien qu'un nouveau quiz ne
 * ressemble presque jamais au précédent.
 */
object QuizEngine {
    const val QUESTION_COUNT = 10

    fun generateQuiz(): List<QuizQuestion> {
        val gems = GemsRepository.gems
        val allFamilies = gems.map { GemFamilies.baseName(it.famille) }.distinct()
        val glossaryTermCount = GemGlossary.page("fr").termes.size
        val types = QuizQuestionType.entries

        return (0 until QUESTION_COUNT).map { generateQuestion(types.random(), gems, allFamilies, glossaryTermCount) }
    }

    private fun generateQuestion(
        type: QuizQuestionType,
        gems: List<Gem>,
        allFamilies: List<String>,
        glossaryTermCount: Int
    ): QuizQuestion = when (type) {
        QuizQuestionType.FOSSILE_FAMILLE -> {
            val fossile = FossilesRepository.all().random()
            val choices = (FossileFamille.entries.filter { it != fossile.famille }.shuffled().take(3) + fossile.famille).shuffled()
            QuizQuestion(
                type = QuizQuestionType.FOSSILE_FAMILLE,
                fossileId = fossile.id,
                correctIndex = choices.indexOf(fossile.famille),
                choiceKeys = choices.map { it.name }
            )
        }
        QuizQuestionType.COULEUR -> {
            val gem = gems.random()
            val choices = (GemColorCategory.entries.filter { it != gem.couleur }.shuffled().take(3) + gem.couleur).shuffled()
            QuizQuestion(
                type = QuizQuestionType.COULEUR,
                gemId = gem.id,
                correctIndex = choices.indexOf(gem.couleur),
                choiceKeys = choices.map { it.name }
            )
        }
        QuizQuestionType.FAMILLE -> {
            val gem = gems.random()
            val correctFamille = GemFamilies.baseName(gem.famille)
            val choices = (allFamilies.filter { it != correctFamille }.shuffled().take(3) + correctFamille).shuffled()
            QuizQuestion(
                type = QuizQuestionType.FAMILLE,
                gemId = gem.id,
                correctIndex = choices.indexOf(correctFamille),
                choiceKeys = choices
            )
        }
        QuizQuestionType.RARETE -> {
            val gem = gems.random()
            val choices = (GemRarete.entries.filter { it != gem.rarete }.shuffled().take(3) + gem.rarete).shuffled()
            QuizQuestion(
                type = QuizQuestionType.RARETE,
                gemId = gem.id,
                correctIndex = choices.indexOf(gem.rarete),
                choiceKeys = choices.map { it.name }
            )
        }
        QuizQuestionType.GLOSSAIRE -> {
            val correctTermIndex = (0 until glossaryTermCount).random()
            val choices = ((0 until glossaryTermCount).filter { it != correctTermIndex }.shuffled().take(3) + correctTermIndex).shuffled()
            QuizQuestion(
                type = QuizQuestionType.GLOSSAIRE,
                correctIndex = choices.indexOf(correctTermIndex),
                glossaryChoiceIndices = choices
            )
        }
    }
}
