package fr.gemsofrod.encyclopedie.data

data class CoquillageFamilyExplainer(
    val nom: String,
    val sousTypes: String,
    val description: String
)

data class CoquillageClassificationPage(
    val intro: String,
    val familles: List<CoquillageFamilyExplainer>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant la classification générale des
 * coquillages (gastéropodes, bivalves, céphalopode à coquille) et leurs
 * principaux représentants d'intérêt lapidaire, traduit dans les langues de
 * l'app indépendamment des fiches individuelles — même principe que
 * [FossileClassificationInfo] et [MeteoriteClassificationInfo]. Seul le
 * français est disponible pour l'instant ; [page] retombe sur le français
 * pour toute langue non encore traduite.
 */
object CoquillageClassificationInfo {
    private val fr = CoquillageClassificationPage(
        intro = "Les coquillages présentés ici sont classés par grand groupe de mollusques : gastéropodes (coquille unique, souvent enroulée en spirale), bivalves (coquille en deux valves symétriques) et céphalopodes à coquille externe, dont le nautile est aujourd'hui l'unique représentant vivant. L'intérêt lapidaire d'un coquillage dépend surtout de la structure de sa coquille : une coquille nacrée (aragonite en fines lamelles) se scie et se polit en dégageant une iridescence recherchée, tandis qu'une coquille calcitique opaque reste avant tout appréciée pour sa forme entière.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gastéropodes",
                sousTypes = "Ormeau, cauris, conque reine, trocas, burgau, murex, strombes",
                description = "Mollusques à coquille unique, le plus souvent enroulée en spirale, qui rampent sur un large pied musculeux. C'est le groupe le plus diversifié : il réunit aussi bien des espèces à nacre épaisse très recherchée en bijouterie (ormeau, trocas, burgau) que des espèces à coquille porcelanée non nacrée appréciées entières (cauris) ou des espèces à intérêt avant tout historique, comme le murex, source de la pourpre antique."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalves",
                sousTypes = "Huîtres perlières, coquille Saint-Jacques, spondyle, bénitier géant, moule perlière d'eau douce",
                description = "Mollusques à coquille en deux valves symétriques reliées par une charnière, filtrant leur nourriture dans l'eau. Ce groupe comprend les principales espèces nacrières exploitées pour la perliculture (huîtres perlières de Tahiti et des mers du Sud), aux côtés d'espèces non nacrées à intérêt purement décoratif ou historique (coquille Saint-Jacques, spondyle andin)."
            ),
            CoquillageFamilyExplainer(
                nom = "Céphalopode à coquille",
                sousTypes = "Nautile",
                description = "Groupe aujourd'hui réduit à un seul genre vivant, le nautile, dernier héritier d'une lignée de céphalopodes à coquille externe autrefois dominée par les ammonites, aujourd'hui entièrement fossiles. Sa coquille en spirale logarithmique cloisonnée en loges successives en fait une pièce à part, à mi-chemin entre le coquillage ornemental et la curiosité scientifique."
            )
        ),
        disclaimerTitle = "Espèces protégées et provenance",
        disclaimerBody = "Plusieurs coquillages présentés dans cette section proviennent d'espèces aujourd'hui protégées ou menacées (nautile, bénitier géant, moule perlière d'eau douce, conque reine), dont le commerce international est encadré par la Convention de Washington (CITES) ou par des réglementations locales. Pour tout achat, privilégiez une provenance documentée et légale, en particulier pour les espèces les plus vulnérables. Les prix indiqués dans cette section sont purement indicatifs : ils varient énormément selon la taille de la pièce, son état de conservation et l'état du marché."
    )

    private val byLanguage: Map<String, CoquillageClassificationPage> = emptyMap()

    fun page(languageCode: String): CoquillageClassificationPage = byLanguage[languageCode] ?: fr
}
