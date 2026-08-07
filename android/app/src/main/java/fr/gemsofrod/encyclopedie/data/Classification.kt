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
