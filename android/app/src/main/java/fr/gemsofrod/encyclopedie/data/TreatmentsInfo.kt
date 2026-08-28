package fr.gemsofrod.encyclopedie.data

data class TreatmentEntry(
    val nom: String,
    val description: String,
    val especesConcernees: String,
    val indicesDetection: String,
    val obligationDivulgation: String
)

data class TreatmentsPage(
    val intro: String,
    val traitements: List<TreatmentEntry>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Référence des traitements courants rencontrés en gemmologie (chauffage,
 * huilage, irradiation...), avec les espèces les plus concernées, des
 * indices de détection accessibles sans laboratoire et un rappel de
 * l'obligation de divulgation — outil destiné aux professionnels
 * (gemmologues, joailliers, négociants), traduit dans les 9 langues de
 * l'app indépendamment des fiches gemmes. Contenu informatif : seul un
 * laboratoire gemmologique accrédité peut confirmer un traitement.
 */
object TreatmentsInfo {
    private val fr = TreatmentsPage(
        intro = "La grande majorité des pierres précieuses vendues dans le monde ont subi un traitement, souvent stable et largement accepté par le marché (le chauffage du saphir, par exemple). Ce qui importe déontologiquement n'est pas le traitement en lui-même, mais sa divulgation systématique à l'acheteur. Cette fiche recense les traitements les plus courants, les espèces qu'ils concernent le plus souvent et des indices observables à la loupe ou au microscope — une confirmation formelle reste du ressort d'un laboratoire gemmologique accrédité (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Chauffage",
                description = "Chauffe la pierre à haute température (parfois plus de 1600°C) pour améliorer sa couleur ou sa pureté en dissolvant certaines inclusions ou en modifiant l'état d'oxydation des éléments colorants. Traitement le plus ancien et le plus largement accepté du marché lorsqu'il n'implique pas d'ajout de matière.",
                especesConcernees = "Corindon (saphir, rubis), zircon, tanzanite, aigue-marine, tourmaline, citrine (par chauffage d'améthyste)",
                indicesDetection = "Inclusions cristallines fondues ou auréolées (« halo » de tension), disques de tension autour de cristaux, stries de croissance discontinues ; l'absence totale d'inclusions n'exclut pas un chauffage.",
                obligationDivulgation = "Obligatoire dans la plupart des juridictions et systématiquement mentionnée sur un certificat de laboratoire ; largement accepté par le marché sans décote majeure pour le saphir et le rubis."
            ),
            TreatmentEntry(
                nom = "Huilage et imprégnation résineuse",
                description = "Comble les fractures de surface débouchantes avec une huile incolore (traditionnellement de l'huile de cèdre) ou une résine synthétique, pour atténuer leur visibilité et améliorer la clarté apparente. Pratique ancienne et largement tolérée pour l'émeraude, à condition d'être divulguée.",
                especesConcernees = "Émeraude presque exclusivement ; occasionnellement d'autres pierres à fractures de surface (péridot, quartz)",
                indicesDetection = "Éclat gras ou « huileux » le long des fractures sous loupe, bulles ou flux visibles dans les fissures sous fort grossissement, fluorescence UV différente entre les zones traitées et la matrice.",
                obligationDivulgation = "Obligatoire ; le degré d'imprégnation (mineur, modéré, important) doit être précisé sur un certificat, car il influence fortement la valeur."
            ),
            TreatmentEntry(
                nom = "Irradiation",
                description = "Expose la pierre à un rayonnement (faisceau d'électrons, rayons gamma, réacteur nucléaire) pour modifier la structure atomique des éléments colorants et ainsi changer la couleur, souvent suivi d'un chauffage pour stabiliser ou affiner la teinte obtenue.",
                especesConcernees = "Topaze bleue (quasi systématiquement d'origine incolore irradiée), quartz fumé, diamants de couleur fantaisie (bleu, vert, noir), tourmaline",
                indicesDetection = "Couleur d'une intensité ou d'une teinte inhabituelle pour l'espèce naturelle ; la confirmation nécessite en général un laboratoire, la source d'irradiation n'étant pas visible optiquement.",
                obligationDivulgation = "Obligatoire ; certains pays imposent une période de quarantaine avant commercialisation pour s'assurer de l'absence de radioactivité résiduelle, contrôlée par le fournisseur."
            ),
            TreatmentEntry(
                nom = "Diffusion",
                description = "Fait pénétrer, sous chauffage prolongé, des éléments chimiques colorants (titane, chrome, ou plus récemment béryllium) depuis la surface de la pierre vers l'intérieur, produisant une coloration superficielle (diffusion classique) ou pénétrant profondément dans tout le corps de la pierre (diffusion au béryllium).",
                especesConcernees = "Saphir, notamment les teintes orange/rose « padparadscha-like » obtenues par diffusion au béryllium",
                indicesDetection = "Diffusion classique : concentration de couleur en périphérie visible en immersion, couleur plus pâle une fois la pierre repolie. Diffusion au béryllium : indétectable à la loupe, nécessite une analyse chimique de laboratoire (LA-ICP-MS).",
                obligationDivulgation = "Obligatoire ; la diffusion au béryllium doit impérativement être mentionnée séparément, son impact sur la valeur étant sensiblement plus important que la diffusion classique."
            ),
            TreatmentEntry(
                nom = "Remplissage de fractures au verre au plomb",
                description = "Comble les fractures importantes d'un rubis de qualité médiocre avec un verre riche en plomb à bas point de fusion, améliorant fortement la clarté apparente d'une pierre autrement invendable en l'état.",
                especesConcernees = "Rubis presque exclusivement, parfois saphir",
                indicesDetection = "Bulles de gaz piégées dans le verre de comblement, effet « flash » de couleur bleuâtre ou orangée selon l'angle d'observation sous loupe, texture de surface irrégulière autour des zones remplies.",
                obligationDivulgation = "Obligatoire et impérative : ces pierres, parfois appelées « composite ruby », doivent être clairement distinguées d'un rubis simplement chauffé, leur valeur étant très inférieure et leur fragilité accrue (attaque possible par des produits d'entretien courants)."
            ),
            TreatmentEntry(
                nom = "Teinture",
                description = "Applique un colorant (organique ou minéral) pénétrant dans la porosité naturelle de la pierre ou le long de ses fractures, pour intensifier ou modifier sa couleur.",
                especesConcernees = "Jade (jadéite notamment), agate, perle, corail, turquoise poreuse, lapis-lazuli de qualité inférieure",
                indicesDetection = "Concentration de couleur dans les fissures ou les zones poreuses visible à la loupe, coton imbibé d'acétone qui se colore au contact de la pierre (test destructif, à réserver aux professionnels), couleur d'une uniformité artificielle.",
                obligationDivulgation = "Obligatoire ; le jade teinté (parfois combiné à une imprégnation de résine, catégorie dite « type C ») doit être clairement distingué du jade naturel non traité (« type A »)."
            ),
            TreatmentEntry(
                nom = "Blanchiment",
                description = "Utilise un agent chimique (généralement à base de chlore ou de peroxyde) pour éclaircir ou uniformiser la couleur naturelle d'une pierre, souvent en préparation d'un autre traitement (teinture, imprégnation).",
                especesConcernees = "Jade, perle de culture",
                indicesDetection = "Difficilement détectable visuellement seul ; souvent associé à d'autres traitements (teinture, imprégnation) dont les indices sont plus caractéristiques.",
                obligationDivulgation = "Obligatoire, généralement mentionnée conjointement avec le traitement associé (teinture ou imprégnation)."
            ),
            TreatmentEntry(
                nom = "Traitement HPHT (haute pression haute température)",
                description = "Soumet la pierre à des conditions extrêmes de pression et de température, reproduisant artificiellement les conditions de formation profonde, pour améliorer la couleur (décoloration de diamants bruns en incolore, ou production de couleurs fantaisie) ou la transparence.",
                especesConcernees = "Diamant presque exclusivement",
                indicesDetection = "Indétectable à la loupe ; nécessite systématiquement une analyse de laboratoire spécialisée (spectroscopie), les diamants HPHT pouvant présenter une fluorescence ou une phosphorescence UV atypique.",
                obligationDivulgation = "Obligatoire et généralement gravée au laser sur le rondiste de la pierre par les laboratoires qui la certifient, du fait de l'écart de valeur important avec un diamant naturel non traité."
            ),
            TreatmentEntry(
                nom = "Enrobage de surface (coating)",
                description = "Dépose une fine couche métallique ou d'oxyde à la surface de la pierre (dépôt physique en phase vapeur notamment) pour produire un effet de couleur irisée ou une teinte qui n'existe pas naturellement dans l'espèce.",
                especesConcernees = "Quartz (« quartz aura », « quartz titane »), topaze (« topaze mystique »), occasionnellement d'autres pierres transparentes",
                indicesDetection = "Rayures ou usure du revêtement visibles à la loupe sur les arêtes de facettes après un port prolongé, couleur qui semble « flotter » à la surface plutôt qu'imprégner la pierre, irisation inhabituelle pour l'espèce.",
                obligationDivulgation = "Obligatoire ; ce traitement est peu durable (le revêtement s'use avec le temps) et doit être signalé comme tel, en particulier pour un usage en bijouterie quotidienne."
            ),
            TreatmentEntry(
                nom = "Imprégnation stabilisante (cire, résine, polymère)",
                description = "Imprègne une pierre naturellement poreuse ou friable d'une substance (cire, résine, polymère) pour en renforcer la structure, en uniformiser l'aspect de surface et faciliter sa mise en forme et son polissage.",
                especesConcernees = "Turquoise (la grande majorité de la turquoise commerciale est stabilisée), lapis-lazuli, opale poreuse",
                indicesDetection = "Test de la goutte d'eau (une goutte perlée reste en surface sur une turquoise stabilisée, tandis qu'elle est absorbée par une turquoise naturelle non traitée), léger changement de couleur au contact d'un objet chaud (test destructif, réservé aux professionnels).",
                obligationDivulgation = "Obligatoire ; la turquoise stabilisée doit être clairement distinguée de la turquoise naturelle non traitée, nettement plus rare et onéreuse."
            ),
            TreatmentEntry(
                nom = "Perçage au laser",
                description = "Perce un fin canal au laser jusqu'à une inclusion sombre gênante à l'intérieur d'un diamant, puis y injecte un agent blanchissant ou comble le canal, pour améliorer la pureté apparente de la pierre.",
                especesConcernees = "Diamant presque exclusivement",
                indicesDetection = "Fin canal rectiligne ou légèrement courbé visible à fort grossissement, débouchant généralement en surface ; l'inclusion visée apparaît parfois blanchie ou partiellement dissoute.",
                obligationDivulgation = "Obligatoire et systématiquement mentionnée sur un certificat de laboratoire, avec un impact sensible sur la valeur par rapport à un diamant de pureté naturellement équivalente."
            )
        ),
        disclaimerTitle = "Une confirmation de laboratoire reste nécessaire",
        disclaimerBody = "Les indices présentés ici permettent d'orienter une observation à la loupe ou au microscope, mais ne remplacent en aucun cas une analyse de laboratoire gemmologique accrédité (GIA, Gübelin, GFCO, SSEF, AGL...), seule habilitée à confirmer formellement un traitement. La divulgation systématique des traitements à l'acheteur est une obligation déontologique et, dans de nombreuses juridictions, légale — indépendamment de l'acceptation du traitement par le marché."
    )

    private val byLanguage: Map<String, TreatmentsPage> = mapOf(
        AppLanguage.EN.code to fr,
        AppLanguage.ES.code to fr,
        AppLanguage.IT.code to fr,
        AppLanguage.DE.code to fr,
        AppLanguage.PT.code to fr,
        AppLanguage.ZH.code to fr,
        AppLanguage.RU.code to fr,
        AppLanguage.NL.code to fr
    )

    fun page(languageCode: String): TreatmentsPage = byLanguage[languageCode] ?: fr
}
