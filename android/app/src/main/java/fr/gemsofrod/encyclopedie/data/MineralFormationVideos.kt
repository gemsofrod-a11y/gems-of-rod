package fr.gemsofrod.encyclopedie.data

/** Vidéo pédagogique tierce (YouTube) illustrant la formation d'une pierre. */
data class MineralFormationVideo(
    val youtubeId: String,
    val title: String
)

/** Les trois sujets couverts par l'écran « Formation des minéraux ». */
enum class MineralFormationStone { DIAMANT, EMERAUDE, SAPHIR_RUBIS }

/**
 * Une vidéo par pierre, choisie dans la langue d'interface quand une source
 * fiable existe dans cette langue ; l'anglais sert de secours pour les
 * langues sans vidéo native trouvée (actuellement néerlandais et chinois).
 * Contenu tiers hébergé sur YouTube — Gems of Rod n'en est pas l'auteur.
 */
object MineralFormationVideos {
    private val diamant = mapOf(
        "fr" to MineralFormationVideo("uRqB2636rNQ", "L'origine des pierres précieuses : le diamant"),
        "en" to MineralFormationVideo("AF2eYuAKsao", "Where Do Diamonds Come From?: Crash Course Geology #8"),
        "es" to MineralFormationVideo("sNLyrvj1KEw", "Cómo se forman los diamantes en la Tierra"),
        "it" to MineralFormationVideo("uOjAaKSN5QI", "Fabrizio Nestola: «Ecco dove si formano i diamanti»"),
        "de" to MineralFormationVideo("0LWuMvK_p6Q", "Wie Diamanten entstehen und zum teuersten Stein der Welt werden"),
        "pt" to MineralFormationVideo("YTfcMYTRshE", "Diamantes: por que são tão raros, tão duros e tão valiosos?"),
        "ru" to MineralFormationVideo("lLYLEuSZyz0", "Как добывают алмазы")
    )

    private val emeraude = mapOf(
        "fr" to MineralFormationVideo("icfFY8NYko4", "Les Émeraudes"),
        "en" to MineralFormationVideo("8LzKNVZvR2g", "How Emeralds Form | 3 Unique Ways!"),
        "es" to MineralFormationVideo("77SokbDzpgQ", "Todo sobre las esmeraldas: secretos geológicos al descubierto"),
        "it" to MineralFormationVideo("Rq-YMngloYk", "Lo Smeraldo della Colombia"),
        "de" to MineralFormationVideo("LMPlNMlKqN4", "Der Smaragd von Kolumbien"),
        "pt" to MineralFormationVideo("nDSWrzlOoEk", "Você sabe como uma Esmeralda se Forma?!"),
        "ru" to MineralFormationVideo("SflWAeLvzrs", "Изумруды Колумбии")
    )

    private val saphirRubis = mapOf(
        "fr" to MineralFormationVideo("Ez8jXfFTpB4", "Le corindon : saphir et rubis"),
        "en" to MineralFormationVideo("rc315Gm80R0", "Crystal & Mineral Education: CORUNDUM (Ruby / Sapphire)"),
        "es" to MineralFormationVideo("8bUpHqw0gCA", "Rubí, zafiro, corindón: curiosidades minerales"),
        "it" to MineralFormationVideo("IDBsZzMRQ4M", "Pillole di minerali: il corindone"),
        "de" to MineralFormationVideo("1hYXaom428g", "Edelsteinlexikon: Korund (Rubin und Saphir)"),
        "pt" to MineralFormationVideo("eiaEAwdWINM", "Como nascem os coríndon, rubi e safiras"),
        "ru" to MineralFormationVideo("jf33nLQF2as", "Рубин и сапфир. Драгоценности, рождённые природой")
    )

    private fun mapFor(stone: MineralFormationStone): Map<String, MineralFormationVideo> = when (stone) {
        MineralFormationStone.DIAMANT -> diamant
        MineralFormationStone.EMERAUDE -> emeraude
        MineralFormationStone.SAPHIR_RUBIS -> saphirRubis
    }

    /** Vidéo pour cette pierre dans [languageCode], ou la version anglaise si absente. */
    fun video(stone: MineralFormationStone, languageCode: String): MineralFormationVideo {
        val map = mapFor(stone)
        return map[languageCode] ?: map.getValue("en")
    }

    /** Faux si [video] a dû se replier sur l'anglais faute de version native dans [languageCode]. */
    fun hasNativeVideo(stone: MineralFormationStone, languageCode: String): Boolean =
        mapFor(stone).containsKey(languageCode)
}
