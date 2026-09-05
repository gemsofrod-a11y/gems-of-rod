package fr.gemsofrod.encyclopedie.data

data class RockOriginEntry(
    val nom: String,
    val description: String,
    val especesTypiques: String
)

data class RochesMeresPage(
    val title: String,
    val intro: String,
    val especesLabel: String,
    val disclaimerTitle: String,
    val disclaimerBody: String,
    val roches: List<RockOriginEntry>
)

/**
 * Les trois grandes familles de roches où naissent les pierres précieuses,
 * plus la pegmatite (variété de roche magmatique à part, principale source
 * des pierres fines de collection) — un contenu qui manquait à l'app alors
 * que ce vocabulaire (magmatique, métamorphique...) apparaît déjà, épars,
 * dans certaines fiches gemmes ([GemsRepository]). Contenu original,
 * reformulé à partir de faits de pétrologie généraux ; français uniquement
 * pour l'instant (voir [GemLocalization] pour le principe de couverture
 * partielle des contenus).
 */
object RochesMeresInfo {
    private val fr = RochesMeresPage(
        title = "Roches mères",
        intro = "Une pierre précieuse ne naît jamais de nulle part : elle cristallise au sein d'une roche hôte, la « roche mère », dont l'histoire géologique — température, pression, composition chimique — détermine quelles espèces peuvent s'y former. On distingue trois grandes familles de roches, plus un cas particulier, la pegmatite, qui joue un rôle à part dans la genèse des pierres fines.",
        especesLabel = "Pierres caractéristiques",
        disclaimerTitle = "Une tendance, pas une règle",
        disclaimerBody = "Une même espèce peut parfois se former dans plusieurs contextes géologiques différents (le rubis, par exemple, existe aussi bien dans des marbres métamorphiques que dans certains basaltes) : cette classification indique l'origine la plus caractéristique de chaque pierre, pas une règle absolue.",
        roches = listOf(
            RockOriginEntry(
                nom = "Roche magmatique (ignée)",
                description = "Née du refroidissement d'un magma. En profondeur, le refroidissement lent laisse le temps aux cristaux de grossir (roche plutonique, comme le granite) ; en surface, le refroidissement rapide d'une lave donne des cristaux fins, voire un verre volcanique sans structure cristalline. Les pierres les plus profondes remontent parfois depuis le manteau terrestre par des cheminées volcaniques étroites.",
                especesTypiques = "Diamant (remonté du manteau par la kimberlite), péridot, zircon, moldavite"
            ),
            RockOriginEntry(
                nom = "Pegmatite",
                description = "Roche magmatique au grain exceptionnellement gros, issue de la toute dernière poche de magma à cristalliser — riche en eau et en éléments chimiques rares que les minéraux ordinaires n'ont pas absorbés. Cet environnement fluide laisse le temps à de gros cristaux, parfois de plusieurs mètres, de croître librement : la pegmatite est la principale source des pierres fines de collection.",
                especesTypiques = "Béryl (émeraude, aigue-marine, morganite, héliodore), tourmaline, topaze, kunzite, spodumène, lépidolite"
            ),
            RockOriginEntry(
                nom = "Roche métamorphique",
                description = "Roche préexistante (magmatique, sédimentaire ou déjà métamorphique) transformée en profondeur par la chaleur et la pression sans fusion complète, ce qui réorganise sa structure minérale. Le métamorphisme régional affecte de vastes zones lors de la formation des chaînes de montagnes ; le métamorphisme de contact, plus localisé, se produit à proximité d'une intrusion magmatique.",
                especesTypiques = "Rubis et saphir (marbres et gneiss métamorphiques), grenat, jade (jadéite), lapis-lazuli, disthène"
            ),
            RockOriginEntry(
                nom = "Roche sédimentaire",
                description = "Formée en surface, sans intervention de la chaleur profonde : par l'accumulation et la cimentation de particules (grès, calcaire) ou par la précipitation de minéraux dissous dans l'eau. L'altération chimique de roches préexistantes en climat aride peut aussi concentrer certains éléments et donner naissance à des gisements secondaires.",
                especesTypiques = "Opale (silice précipitée dans des failles), turquoise, malachite et azurite (zones d'altération de gisements de cuivre), ambre (résine fossilisée)"
            )
        )
    )

    private val byLanguage: Map<String, RochesMeresPage> = mapOf("fr" to fr)

    fun page(languageCode: String): RochesMeresPage = byLanguage[languageCode] ?: fr
}
