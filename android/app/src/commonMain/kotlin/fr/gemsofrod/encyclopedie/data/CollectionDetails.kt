package fr.gemsofrod.encyclopedie.data

/**
 * Rubriques approfondies des fiches fossiles, coquillages et météorites
 * (demande de Sébastien : « toutes les fiches »), sur le même modèle que
 * [GemDetails], dont la classe est réutilisée champ pour champ :
 * - `formation` : formation et origine (fossilisation, croissance de la
 *   coquille, origine dans le système solaire) ;
 * - `cristallographie` : structure et composition ;
 * - `gisements` : provenance détaillée ;
 * - `lithotherapie` : traditions et lithothérapie (croyances, jamais
 *   allégations médicales) et conseils d'entretien.
 *
 * Les identifiants des trois collections sont préfixés (`fossile-`,
 * `coquillage-`, `meteorite-`) : un seul dépôt suffit. Chaque lot porte les
 * 9 langues de l'app (règle CLAUDE.md).
 */
object CollectionDetailsRepository {
    private val byId: Map<String, Map<String, GemDetails>> = buildMap {
        putAll(FossileDetailsLot01.data)
        putAll(FossileDetailsLot02.data)
        putAll(FossileDetailsLot03.data)
        putAll(FossileDetailsLot04.data)
        putAll(FossileDetailsLot05.data)
        putAll(FossileDetailsLot06.data)
        putAll(CoquillageDetailsLot01.data)
        putAll(CoquillageDetailsLot02.data)
        putAll(CoquillageDetailsLot03.data)
        putAll(CoquillageDetailsLot04.data)
        putAll(CoquillageDetailsLot05.data)
        putAll(MeteoriteDetailsLot01.data)
        putAll(MeteoriteDetailsLot02.data)
        putAll(MeteoriteDetailsLot03.data)
    }

    fun detailsFor(id: String, languageCode: String): GemDetails? {
        val forId = byId[id] ?: return null
        return forId[languageCode] ?: forId["fr"]
    }

    fun hasDetails(id: String): Boolean = byId.containsKey(id)
}
