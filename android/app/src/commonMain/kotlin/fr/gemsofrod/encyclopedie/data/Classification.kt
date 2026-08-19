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
        GemsRepository.gems
            .groupBy { baseName(it.famille) }
            .toSortedMap()
            .map { (name, gems) -> name to gems.sortedBy { it.nom } }

    fun gemsFor(familyName: String): List<Gem> =
        GemsRepository.gems.filter { baseName(it.famille) == familyName }.sortedBy { it.nom }
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
        GemsRepository.gems
            .flatMap { gem -> gem.origines.flatMap { countriesOf(it) }.distinct().map { country -> country to gem } }
            .groupBy({ it.first }, { it.second })
            .toSortedMap()
            .map { (country, gems) -> country to gems.sortedBy { it.nom } }

    fun gemsFor(country: String): List<Gem> =
        GemsRepository.gems
            .filter { gem -> gem.origines.any { origin -> countriesOf(origin).contains(country) } }
            .sortedBy { it.nom }
}

/**
 * Regroupements complémentaires des gemmes, utilisés par les sous-catégories
 * de la section Lithothérapie. Tout est dérivé des champs existants de [Gem]
 * (couleur, texte lithothérapie) ou de tables de correspondance statiques
 * courtes (signe astrologique, mois de naissance) — aucune donnée
 * supplémentaire n'est stockée sur [Gem].
 */
object Chakras {
    private fun chakraFor(couleur: GemColorCategory): String = when (couleur) {
        GemColorCategory.ROUGE, GemColorCategory.BRUN, GemColorCategory.NOIR -> "Racine"
        GemColorCategory.ORANGE -> "Sacré"
        GemColorCategory.JAUNE -> "Plexus solaire"
        GemColorCategory.VERT, GemColorCategory.ROSE -> "Cœur"
        GemColorCategory.BLEU -> "Gorge"
        GemColorCategory.VIOLET -> "Troisième œil"
        GemColorCategory.INCOLORE, GemColorCategory.MULTICOLORE -> "Couronne"
    }

    /** Ordre traditionnel du bas vers le haut du corps. */
    private val ORDER = listOf("Racine", "Sacré", "Plexus solaire", "Cœur", "Gorge", "Troisième œil", "Couronne")

    fun groups(): List<Pair<String, List<Gem>>> {
        val byChakra = GemsRepository.gems.groupBy { chakraFor(it.couleur) }
        return ORDER.map { it to (byChakra[it].orEmpty().sortedBy { g -> g.nom }) }
    }

    fun gemsFor(chakra: String): List<Gem> =
        GemsRepository.gems.filter { chakraFor(it.couleur) == chakra }.sortedBy { it.nom }
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
        "Ancrage" to listOf("ancrage", "stabilité")
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

object ZodiacSigns {
    private val SIGNS: List<Pair<String, List<String>>> = listOf(
        "Bélier" to listOf("diamant", "rubis", "grenat-pyrope", "cornaline", "hematite"),
        "Taureau" to listOf("emeraude", "quartz-rose", "topaze-imperiale", "lapis-lazuli"),
        "Gémeaux" to listOf("agate", "citrine", "perle", "tourmaline-verte"),
        "Cancer" to listOf("pierre-de-lune", "perle", "emeraude", "rubis"),
        "Lion" to listOf("oeil-de-tigre", "peridot", "citrine", "ambre"),
        "Vierge" to listOf("saphir-bleu", "jade-nephrite", "amethyste", "cornaline"),
        "Balance" to listOf("opale-noire", "lapis-lazuli", "tourmaline-rose", "saphir-rose"),
        "Scorpion" to listOf("topaze-imperiale", "obsidienne", "malachite", "grenat-almandin"),
        "Sagittaire" to listOf("turquoise", "topaze-bleue", "amethyste", "lapis-lazuli"),
        "Capricorne" to listOf("grenat-almandin", "onyx", "hematite", "rubis"),
        "Verseau" to listOf("amethyste", "aigue-marine", "labradorite", "saphir-bleu"),
        "Poissons" to listOf("aigue-marine", "amethyste", "perle", "labradorite")
    )

    val labels: List<String> = SIGNS.map { it.first }

    fun groups(): List<Pair<String, List<Gem>>> =
        SIGNS.map { (sign, ids) -> sign to ids.mapNotNull { GemsRepository.byId(it) } }

    fun gemsFor(sign: String): List<Gem> =
        SIGNS.firstOrNull { it.first == sign }?.second.orEmpty().mapNotNull { GemsRepository.byId(it) }
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
