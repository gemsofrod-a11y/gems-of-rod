package fr.gemsofrod.encyclopedie.data

/**
 * Regroupement des gemmes du catalogue par famille minéralogique, utilisé par
 * l'écran "Familles minérales". Dérivé du champ `famille` existant de [Gem] —
 * aucune donnée supplémentaire n'est stockée.
 */
object GemFamilies {
    /** Nom de famille "de base", sans la précision entre parenthèses
     * (ex. "Grenat (pyrope)" -> "Grenat"), pour regrouper les variétés
     * d'une même famille minérale. */
    fun baseName(famille: String): String = famille.substringBefore(" (").trim()

    fun groups(): List<Pair<String, List<Gem>>> =
        GemsRepository.minerals()
            .groupBy { baseName(it.famille) }
            .toSortedMap()
            .map { (name, gems) -> name to gems.sortedBy { it.nom } }

    fun gemsFor(familyName: String): List<Gem> =
        GemsRepository.minerals().filter { baseName(it.famille) == familyName }.sortedBy { it.nom }
}

/**
 * Regroupement des gemmes du catalogue par pays d'origine, utilisé par
 * l'écran "Pays" de la Gemmologie. Dérivé du champ `origines` existant de
 * [Gem] (chaque entrée est écrite "Pays (région)") — aucune donnée
 * supplémentaire n'est stockée. Une gemme ayant plusieurs origines apparaît
 * sous chacun des pays correspondants.
 *
 * Quelques entrées d'origine désignent la même réalité géographique sous des
 * libellés différents (ex. "Myanmar" / "Birmanie", "Congo" / "République
 * démocratique du Congo") ou omettent le pays alors qu'il est sans ambiguïté
 * (ex. "Ouro Preto" au Brésil) : [countryAliases] les normalise vers le
 * libellé déjà utilisé ailleurs dans le catalogue, pour éviter des boutons
 * "pays" en double. Les régions/territoires réellement distincts (Cachemire,
 * Caraïbes...) restent inchangés.
 */
object GemOrigins {
    private val countryAliases: Map<String, String> = mapOf(
        "Myanmar" to "Birmanie",
        "Congo" to "République démocratique du Congo",
        "Angleterre" to "Royaume-Uni",
        "Écosse" to "Royaume-Uni",
        "Oural" to "Russie",
        "Mogok Stone Tract, Birmanie" to "Birmanie",
        "Mérelani, Tanzanie" to "Tanzanie",
        "São José da Batalha, Paraíba, Brésil" to "Brésil",
        "San Benito, Californie, États-Unis" to "États-Unis",
        "Rivière Chara, Sibérie, Russie" to "Russie",
        "Alberta, Canada" to "Canada",
        "Ouro Preto" to "Brésil",
        "Val Cristallina, Alpes suisses" to "Suisse"
    )

    /** Pays d'une entrée d'origine (ex. "Sri Lanka (Ratnapura)" -> "Sri Lanka"),
     * plusieurs pays si l'entrée en combine deux (ex. "Égypte / Libye"). */
    private fun countriesOf(origin: String): List<String> =
        origin.substringBefore(" (").trim()
            .split(" / ")
            .map { countryAliases[it.trim()] ?: it.trim() }

    fun groups(): List<Pair<String, List<Gem>>> =
        GemsRepository.minerals()
            .flatMap { gem -> gem.origines.flatMap { countriesOf(it) }.distinct().map { country -> country to gem } }
            .groupBy({ it.first }, { it.second })
            .toSortedMap()
            .map { (country, gems) -> country to gems.sortedBy { it.nom } }

    fun gemsFor(country: String): List<Gem> =
        GemsRepository.minerals()
            .filter { gem -> gem.origines.any { origin -> countriesOf(origin).contains(country) } }
            .sortedBy { it.nom }
}

// Regroupements complémentaires des gemmes, utilisés par les sous-catégories
// de la section Lithothérapie : chakras et signes d'après les fiches
// ([LithoGuideData]), bienfaits d'après le texte lithothérapie, mois de
// naissance d'après une courte table statique.

/**
 * Classement des pierres par chakra. Source unique : le ou les chakras
 * indiqués dans la rubrique lithothérapie de chaque fiche (repris dans
 * [LithoGuideData]) — et non plus une déduction d'après la couleur, qui
 * contredisait 45 fiches (ex. topaze impériale rangée au chakra sacré alors
 * que sa fiche la dit au plexus solaire). Alignement demandé par Sébastien
 * le 16/11/2026. Une pierre liée à plusieurs chakras apparaît sous chacun.
 */
object Chakras {
    private const val ALL_CHAKRAS = "ALL"

    /** Ordre traditionnel du bas vers le haut du corps. */
    private val ORDER = listOf("Racine", "Sacré", "Plexus solaire", "Cœur", "Gorge", "Troisième œil", "Couronne")

    val labels: List<String> get() = ORDER

    private fun chakrasOf(gem: Gem): List<String> = LithoGuideData.entries[gem.id]?.chakras.orEmpty()

    fun gemsFor(chakra: String): List<Gem> =
        GemsRepository.gems
            .filter { gem -> chakrasOf(gem).let { chakra in it || ALL_CHAKRAS in it } }
            .sortedWith(compareBy<Gem>({ ALL_CHAKRAS in chakrasOf(it) }, { it.nom }))

    fun groups(): List<Pair<String, List<Gem>>> = ORDER.map { it to gemsFor(it) }
}

object Bienfaits {
    // Chaque catégorie est déduite par mots-clés du texte lithotherapie déjà
    // rédigé pour chaque gemme (une gemme peut apparaître dans plusieurs
    // catégories si son texte évoque plusieurs vertus).
    private val CATEGORIES: List<Pair<String, List<String>>> = listOf(
        "Stress & Apaisement" to listOf("stress", "apais", "calme", "sérénité", "tension"),
        "Confiance & Réussite" to listOf("confiance", "réussite", "détermination", "motivation", "discipline", "discernement"),
        "Amour & Relations" to listOf("amour", "cœur", "tendress", "compassion", "affectiv"),
        "Protection" to listOf("protection", "protectrice", "protecteur"),
        "Sommeil" to listOf("sommeil"),
        "Énergie & Vitalité" to listOf("énergie", "vitalité", "dynamis", "joie"),
        "Créativité & Communication" to listOf("créativité", "communication", "expression"),
        "Spiritualité & Intuition" to listOf("spiritualité", "intuition", "méditation", "éveil", "conscience", "sagesse"),
        "Équilibre & Harmonie" to listOf("équilibre", "harmonie"),
        "Ancrage" to listOf("ancrage", "stabilité"),
        "Protection contre le mauvais œil" to listOf("mauvais œil", "mauvais oeil"),
        "Énergies négatives" to listOf("énergies négatives", "energies negatives", "ondes négatives"),
        "Bouclier psychique" to listOf("bouclier psychique", "protection psychique"),
        "Protection des voyages" to listOf("voyageurs", "protège durant les voyages", "protège les voyages"),
        "Protection du foyer" to listOf("protège le foyer", "protège la maison", "protège le domicile")
    )

    val labels: List<String> = CATEGORIES.map { it.first }

    private fun matches(gem: Gem, keywords: List<String>): Boolean {
        val text = gem.lithotherapie.lowercase()
        return keywords.any { text.contains(it) }
    }

    fun groups(): List<Pair<String, List<Gem>>> =
        CATEGORIES.map { (label, keywords) ->
            label to GemsRepository.gems.filter { matches(it, keywords) }.sortedBy { it.nom }
        }

    fun gemsFor(bienfait: String): List<Gem> {
        val keywords = CATEGORIES.firstOrNull { it.first == bienfait }?.second.orEmpty()
        return GemsRepository.gems.filter { matches(it, keywords) }.sortedBy { it.nom }
    }
}

/**
 * Classement des pierres par signe du zodiaque. Source unique : le signe
 * indiqué dans la rubrique lithothérapie de chaque fiche (repris dans
 * [LithoGuideData]), pour que la liste d'un signe ne contredise jamais la
 * fiche d'une pierre (correction demandée par Sébastien le 16/11/2026 —
 * l'ancienne liste écrite à la main classait par ex. le rubis en Bélier
 * alors que sa fiche le dit Lion). Une pierre associée à plusieurs signes
 * apparaît sous chacun ; les pierres dites « de tous les signes » viennent
 * après les pierres propres au signe.
 */
object ZodiacSigns {
    private const val ALL_SIGNS = "ALL"

    val labels: List<String> = listOf(
        "Bélier", "Taureau", "Gémeaux", "Cancer", "Lion", "Vierge",
        "Balance", "Scorpion", "Sagittaire", "Capricorne", "Verseau", "Poissons"
    )

    private fun signsOf(gem: Gem): List<String> = LithoGuideData.entries[gem.id]?.signs.orEmpty()

    fun gemsFor(sign: String): List<Gem> =
        GemsRepository.gems
            .filter { gem -> signsOf(gem).let { sign in it || ALL_SIGNS in it } }
            .sortedWith(compareBy<Gem>({ ALL_SIGNS in signsOf(it) }, { it.nom }))

    fun groups(): List<Pair<String, List<Gem>>> = labels.map { it to gemsFor(it) }
}

object BirthMonths {
    private val MONTHS: List<Pair<String, List<String>>> = listOf(
        "Janvier" to listOf("grenat-almandin"),
        "Février" to listOf("amethyste"),
        "Mars" to listOf("aigue-marine"),
        "Avril" to listOf("diamant"),
        "Mai" to listOf("emeraude"),
        "Juin" to listOf("perle", "pierre-de-lune"),
        "Juillet" to listOf("rubis"),
        "Août" to listOf("peridot"),
        "Septembre" to listOf("saphir-bleu"),
        "Octobre" to listOf("opale-noire", "tourmaline-rose"),
        "Novembre" to listOf("topaze-imperiale", "citrine"),
        "Décembre" to listOf("turquoise", "zircon-bleu")
    )

    val labels: List<String> = MONTHS.map { it.first }

    fun groups(): List<Pair<String, List<Gem>>> =
        MONTHS.map { (mois, ids) -> mois to ids.mapNotNull { GemsRepository.byId(it) } }

    fun gemsFor(mois: String): List<Gem> =
        MONTHS.firstOrNull { it.first == mois }?.second.orEmpty().mapNotNull { GemsRepository.byId(it) }
}
