package fr.gemsofrod.encyclopedie.data

data class FossileFamilyExplainer(
    val nom: String,
    val sousTypes: String,
    val description: String
)

data class FossileClassificationPage(
    val intro: String,
    val familles: List<FossileFamilyExplainer>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant la classification générale des
 * fossiles (mollusques, arthropodes, vertébrés, végétaux, résines, coraux et
 * échinodermes) et leurs principales sous-catégories, traduit dans les
 * langues de l'app indépendamment des fiches individuelles. Complète la
 * section "Fossiles" en expliquant le vocabulaire utilisé sur chaque fiche
 * (période géologique, minéralisation, taillabilité...).
 *
 * Traductions non françaises à venir (voir [MeteoriteClassificationInfo]
 * pour le patron à suivre) ; en attendant, [page] retombe sur le contenu
 * français pour toute langue non couverte.
 */
object FossileClassificationInfo {
    private val fr = FossileClassificationPage(
        intro = "On classe les fossiles d'abord par nature de l'organisme d'origine — coquillage, arthropode, vertébré, végétal, résine ou colonie corallienne — puis par mode de fossilisation, qui détermine directement leur intérêt pour un lapidaire. Une coquille remplacée par de la silice se taille et se polit comme une pierre fine ; une empreinte carbonée sur schiste reste une pièce de collection à préserver telle quelle.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammonites & mollusques",
                sousTypes = "Ammonites, ammolite, orthocères, goniatites, bélemnites",
                description = "Coquilles et rostres de céphalopodes marins aujourd'hui éteints, apparentés au nautile et au calmar actuels. Leur coquille calcaire se prête bien au sciage et au polissage ; l'ammolite, variété rarissime à nacre d'aragonite préservée, est même officiellement reconnue comme gemme organique à part entière."
            ),
            FossileFamilyExplainer(
                nom = "Trilobites & arthropodes",
                sousTypes = "Elrathia, Phacops, Calymene et des centaines d'autres genres, classés par ordre (Ptychopariida, Phacopida...)",
                description = "Arthropodes marins disparus à la fin du Permien, dont le corps segmenté en trois lobes (d'où leur nom) et parfois les yeux composés se sont fossilisés dans le calcaire ou le schiste. Rarement taillés, ils sont surtout préparés à la main pour être dégagés de leur roche encaissante et vendus comme pièces de collection."
            ),
            FossileFamilyExplainer(
                nom = "Vertébrés",
                sousTypes = "Dents de requin et de mosasaure, os de dinosaure agatisé, poissons fossiles, ivoire de mammouth",
                description = "Restes d'animaux à squelette interne, du requin géant mégalodon au mammouth laineux. Certains tissus osseux, remplacés molécule par molécule par de la silice colorée (\"gembone\"), deviennent de véritables gemmes ornementales ; d'autres, comme les dents ou l'ivoire, sont travaillés bruts ou sertis tels quels."
            ),
            FossileFamilyExplainer(
                nom = "Végétaux",
                sousTypes = "Bois pétrifié, fougères et autres empreintes végétales du Carbonifère",
                description = "Troncs, feuilles et fougères d'anciennes forêts, préservés soit par silicification complète (bois pétrifié, taillé comme une agate), soit sous forme de simple empreinte carbonée sur schiste, conservée intacte plutôt que travaillée."
            ),
            FossileFamilyExplainer(
                nom = "Ambre & insectes",
                sousTypes = "Ambre baltique, ambre à inclusion, copal (résine subfossile plus jeune)",
                description = "Résine végétale fossilisée, l'une des plus anciennes gemmes organiques travaillées par l'humanité. Sa transparence naturelle piège parfois des insectes ou de petits organismes, ce qui décuple sa valeur scientifique et esthétique. Le copal, résine plus jeune n'ayant pas terminé sa polymérisation, lui est parfois abusivement substitué sur le marché."
            ),
            FossileFamilyExplainer(
                nom = "Coraux & échinodermes",
                sousTypes = "Corail fossile agatisé, oursins fossiles, crinoïdes (entroques), stromatolites",
                description = "Organismes marins coloniaux ou fixés au fond de l'océan. Le corail fossile agatisé, entièrement silicifié, se taille en cabochons aux motifs floraux uniques ; oursins, crinoïdes et stromatolites restent généralement appréciés bruts ou en dalles polies pour leur structure naturelle."
            )
        ),
        disclaimerTitle = "Authenticité et provenance",
        disclaimerBody = "Comme celui des pierres précieuses et des météorites, le marché des fossiles attire son lot de reconstitutions, de moulages et de pièces recollées ou surpréparées pour paraître plus complètes qu'elles ne le sont réellement. Pour un achat de collection, privilégiez toujours une provenance documentée, et vérifiez si une réglementation locale encadre l'exportation (de nombreux pays, dont le Maroc et les États-Unis, restreignent la sortie de certains fossiles rares ou de vertébrés). Les prix indiqués dans cette section sont purement indicatifs : ils varient énormément selon la taille de la pièce, la qualité de préparation et l'état du marché."
    )

    private val byLanguage: Map<String, FossileClassificationPage> = emptyMap()

    fun page(languageCode: String): FossileClassificationPage = byLanguage[languageCode] ?: fr
}
