package fr.gemsofrod.encyclopedie.data

/**
 * Source de données statique de l'encyclopédie des gemmes.
 * Contenu gemmologique général (non lié au catalogue commercial de Gems of Rod).
 */
object GemsRepository {

    val gems: List<Gem> = listOf(
        // ---------- ROUGE ----------
        Gem(
            id = "rubis",
            nom = "Rubis",
            nomLatin = "Corindon (variété rouge)",
            couleur = GemColorCategory.ROUGE,
            descriptionCourte = "Le corindon rouge, gemme royale par excellence.",
            descriptionLongue = "Le rubis est la variété rouge du corindon, sa couleur provenant de traces de chrome. Symbole de passion et de pouvoir depuis l'Antiquité, il est l'une des quatre pierres précieuses historiques aux côtés du diamant, de l'émeraude et du saphir. Les plus belles pierres présentent un rouge « sang de pigeon » d'une grande intensité.",
            formuleChimique = "Al₂O₃ (trace de Cr)",
            systemeCristallin = "Trigonal",
            durete = "9",
            origines = listOf("Birmanie (Mogok)", "Sri Lanka", "Mozambique", "Madagascar"),
            particularites = "Fluorescence rouge sous UV ; inclusions de rutile en « soie » pouvant produire un astérisme (étoile à 6 branches)."
        ),
        Gem(
            id = "grenat-almandin",
            nom = "Grenat almandin / pyrope",
            nomLatin = "Almandin – Pyrope",
            couleur = GemColorCategory.ROUGE,
            descriptionCourte = "Grenat rouge profond, jamais traité.",
            descriptionLongue = "Ces deux espèces du groupe des grenats forment une série continue et sont souvent commercialisées ensemble. Leur rouge intense, parfois brunâtre pour l'almandin, en fait des pierres accessibles très appréciées en joaillerie depuis l'époque romaine.",
            formuleChimique = "(Fe,Mg)₃Al₂(SiO₄)₃",
            systemeCristallin = "Cubique",
            durete = "7 - 7,5",
            origines = listOf("Inde", "Sri Lanka", "République tchèque", "Tanzanie"),
            particularites = "Aucun clivage, forte réfraction, jamais traité thermiquement ou chimiquement."
        ),
        Gem(
            id = "spinelle-rouge",
            nom = "Spinelle rouge",
            nomLatin = "Spinelle",
            couleur = GemColorCategory.ROUGE,
            descriptionCourte = "Longtemps confondu avec le rubis.",
            descriptionLongue = "Le spinelle rouge fut pendant des siècles pris pour du rubis : le célèbre « Rubis du Prince Noir » serti dans la couronne impériale britannique est en réalité un spinelle. Naturellement non traité dans la grande majorité des cas, il regagne aujourd'hui sa juste reconnaissance auprès des collectionneurs.",
            formuleChimique = "MgAl₂O₄ (trace de Cr)",
            systemeCristallin = "Cubique",
            durete = "8",
            origines = listOf("Birmanie (Mogok)", "Tadjikistan", "Sri Lanka"),
            particularites = "Cristaux souvent octaédriques ; quasiment toujours non traité."
        ),
        Gem(
            id = "tourmaline-rubellite",
            nom = "Tourmaline rubellite",
            nomLatin = "Elbaïte (variété rouge/rose)",
            couleur = GemColorCategory.ROUGE,
            descriptionCourte = "Rouge intense dû au manganèse.",
            descriptionLongue = "La rubellite est la variété rouge à rose framboise du groupe des tourmalines, un silicate boro-alumineux dont la palette de couleurs est la plus étendue du règne minéral. Son fort pléochroïsme fait varier son intensité selon l'angle d'observation.",
            formuleChimique = "Na(Li,Al)₃Al₆(BO₃)₃Si₆O₁₈(OH)₄",
            systemeCristallin = "Trigonal",
            durete = "7 - 7,5",
            origines = listOf("Brésil", "Nigeria", "Mozambique", "Afghanistan"),
            particularites = "Pléochroïsme marqué ; cristaux prismatiques striés caractéristiques."
        ),
        Gem(
            id = "grenat-rhodolite",
            nom = "Grenat rhodolite",
            nomLatin = "Pyrope-Almandin",
            couleur = GemColorCategory.ROUGE,
            descriptionCourte = "Rouge-violacé « framboise ».",
            descriptionLongue = "Mélange intermédiaire entre pyrope et almandin, la rhodolite offre une teinte rouge-violacé lumineuse, plus claire que l'almandin classique. Son nom vient du grec « rhodon » (rose) en référence à sa couleur.",
            formuleChimique = "(Mg,Fe)₃Al₂(SiO₄)₃",
            systemeCristallin = "Cubique",
            durete = "7 - 7,5",
            origines = listOf("Tanzanie", "Sri Lanka", "Inde"),
            particularites = "Excellente transparence, jamais traitée."
        ),

        // ---------- ORANGE ----------
        Gem(
            id = "grenat-spessartite",
            nom = "Grenat spessartite",
            nomLatin = "Spessartite",
            couleur = GemColorCategory.ORANGE,
            descriptionCourte = "Orange « mandarine » éclatant.",
            descriptionLongue = "Découverte en quantité commerciale en Namibie dans les années 1990, la spessartite offre un orange vif et saturé surnommé « mandarine garnet ». C'est aujourd'hui l'une des variétés de grenat les plus recherchées.",
            formuleChimique = "Mn₃Al₂(SiO₄)₃",
            systemeCristallin = "Cubique",
            durete = "7 - 7,5",
            origines = listOf("Namibie", "Nigeria", "Madagascar"),
            particularites = "Couleur naturelle, jamais traitée thermiquement."
        ),
        Gem(
            id = "topaze-imperiale",
            nom = "Topaze impériale",
            nomLatin = "Topaze (variété orangée)",
            couleur = GemColorCategory.ORANGE,
            descriptionCourte = "La variété la plus rare de la topaze.",
            descriptionLongue = "Réservée historiquement à la famille impériale russe, la topaze impériale présente une teinte orange à orange-rosé unique, provenant presque exclusivement des mines d'Ouro Preto au Brésil. Elle se distingue de la topaze bleue, bien plus commune et généralement traitée.",
            formuleChimique = "Al₂(SiO₄)(F,OH)₂",
            systemeCristallin = "Orthorhombique",
            durete = "8",
            origines = listOf("Ouro Preto, Brésil"),
            particularites = "Clivage parfait à surveiller lors de la taille et du sertissage."
        ),
        Gem(
            id = "hessonite",
            nom = "Hessonite",
            nomLatin = "Grenat grossulaire (variété orangée)",
            couleur = GemColorCategory.ORANGE,
            descriptionCourte = "Surnommée « pierre de cannelle ».",
            descriptionLongue = "Variété de grenat grossulaire riche en fer et manganèse, l'hessonite présente une couleur orangée chaude évoquant le sirop de cannelle, avec un effet de chatoiement interne (« treacle effect ») dû à ses inclusions caractéristiques.",
            formuleChimique = "Ca₃Al₂(SiO₄)₃ (Fe, Mn)",
            systemeCristallin = "Cubique",
            durete = "6,5 - 7",
            origines = listOf("Sri Lanka", "Inde", "Canada"),
            particularites = "Aspect huileux à mielleux typique dû aux inclusions internes."
        ),
        Gem(
            id = "opale-de-feu",
            nom = "Opale de feu",
            nomLatin = "Opale (variété orangée)",
            couleur = GemColorCategory.ORANGE,
            descriptionCourte = "Transparente à translucide, teinte flamboyante.",
            descriptionLongue = "Contrairement à l'opale précieuse, l'opale de feu tire sa valeur de sa couleur orangée à rouge intense plutôt que d'un jeu de couleurs, bien que certaines pierres présentent les deux phénomènes.",
            formuleChimique = "SiO₂ · nH₂O",
            systemeCristallin = "Amorphe",
            durete = "5,5 - 6,5",
            origines = listOf("Mexique"),
            particularites = "Pierre fragile et poreuse, sensible aux chocs thermiques."
        ),

        // ---------- JAUNE ----------
        Gem(
            id = "saphir-jaune",
            nom = "Saphir jaune",
            nomLatin = "Corindon (variété jaune)",
            couleur = GemColorCategory.JAUNE,
            descriptionCourte = "Le corindon dans toutes ses teintes hors rouge.",
            descriptionLongue = "Le terme « saphir » désigne toutes les variétés de corindon autres que le rouge (rubis). Le saphir jaune, coloré par des traces de fer, est très prisé en Asie du Sud pour ses vertus symboliques liées à la sagesse et à la prospérité.",
            formuleChimique = "Al₂O₃ (trace de Fe)",
            systemeCristallin = "Trigonal",
            durete = "9",
            origines = listOf("Sri Lanka", "Madagascar", "Tanzanie"),
            particularites = "Souvent chauffé pour intensifier et uniformiser la couleur."
        ),
        Gem(
            id = "citrine",
            nom = "Citrine",
            nomLatin = "Quartz (variété jaune)",
            couleur = GemColorCategory.JAUNE,
            descriptionCourte = "Quartz jaune miel à jaune doré.",
            descriptionLongue = "La citrine naturelle est rare ; la grande majorité des pierres du marché proviennent du chauffage d'améthyste ou de quartz fumé, qui transforme la couleur en jaune doré stable. Pierre de naissance du mois de novembre, associée à l'abondance.",
            formuleChimique = "SiO₂ (trace de Fe)",
            systemeCristallin = "Trigonal",
            durete = "7",
            origines = listOf("Brésil", "Bolivie", "Zambie"),
            particularites = "Très abordable et abondante ; peu ou pas d'inclusions visibles."
        ),
        Gem(
            id = "heliodore",
            nom = "Héliodore",
            nomLatin = "Béryl doré",
            couleur = GemColorCategory.JAUNE,
            descriptionCourte = "Cousin doré de l'émeraude et de l'aigue-marine.",
            descriptionLongue = "L'héliodore, dont le nom signifie « don du soleil » en grec, est la variété jaune à jaune-vert du béryl. Moins connue que ses cousines l'émeraude et l'aigue-marine, elle séduit par sa clarté lumineuse.",
            formuleChimique = "Be₃Al₂(SiO₃)₆ (trace de Fe)",
            systemeCristallin = "Hexagonal",
            durete = "7,5 - 8",
            origines = listOf("Namibie", "Brésil", "Madagascar"),
            particularites = "Généralement très pure, peu d'inclusions."
        ),
        Gem(
            id = "chrysoberyl",
            nom = "Chrysobéryl",
            nomLatin = "Chrysobéryl",
            couleur = GemColorCategory.JAUNE,
            descriptionCourte = "Grande dureté, parent de l'alexandrite.",
            descriptionLongue = "Avec une dureté de 8,5, le chrysobéryl jaune-vert est l'une des gemmes les plus résistantes après le corindon et le diamant. Sa variété à changement de couleur donne naissance à l'alexandrite, et sa variété chatoyante à l'œil-de-chat.",
            formuleChimique = "BeAl₂O₄",
            systemeCristallin = "Orthorhombique",
            durete = "8,5",
            origines = listOf("Sri Lanka", "Brésil", "Madagascar"),
            particularites = "Excellente résistance à l'usure, idéale pour un port quotidien."
        ),

        // ---------- VERT ----------
        Gem(
            id = "emeraude",
            nom = "Émeraude",
            nomLatin = "Béryl (variété verte)",
            couleur = GemColorCategory.VERT,
            descriptionCourte = "Le vert le plus recherché de la joaillerie.",
            descriptionLongue = "L'émeraude tire son vert profond du chrome et/ou du vanadium. Ses inclusions naturelles, surnommées « jardin », témoignent de son authenticité et sont généralement stabilisées par un traitement à l'huile de cèdre, une pratique acceptée et attendue sur le marché.",
            formuleChimique = "Be₃Al₂(SiO₃)₆ (Cr, V)",
            systemeCristallin = "Hexagonal",
            durete = "7,5 - 8",
            origines = listOf("Colombie (Muzo, Chivor)", "Zambie", "Brésil"),
            particularites = "Traitement à l'huile quasi systématique ; pierre fragile nécessitant des précautions."
        ),
        Gem(
            id = "peridot",
            nom = "Péridot",
            nomLatin = "Olivine",
            couleur = GemColorCategory.VERT,
            descriptionCourte = "Vert olive lumineux, jamais traité.",
            descriptionLongue = "Le péridot est l'une des rares gemmes composées d'une seule espèce minérale (l'olivine) et n'est jamais traité. Sa forte biréfringence rend parfois le dédoublement des arêtes visible à l'œil nu à travers la table de la pierre.",
            formuleChimique = "(Mg,Fe)₂SiO₄",
            systemeCristallin = "Orthorhombique",
            durete = "6,5 - 7",
            origines = listOf("Égypte (île de Zabargad)", "Pakistan", "Birmanie", "États-Unis (Arizona)"),
            particularites = "Biréfringence visible à l'œil nu ; certaines pierres proviennent de météorites (pallasites)."
        ),
        Gem(
            id = "tsavorite",
            nom = "Tsavorite",
            nomLatin = "Grenat grossulaire (variété verte)",
            couleur = GemColorCategory.VERT,
            descriptionCourte = "Alternative durable et non traitée à l'émeraude.",
            descriptionLongue = "Découverte en 1967 près du parc national de Tsavo au Kenya, la tsavorite doit sa couleur verte au vanadium et au chrome. Toujours non traitée, elle offre un éclat et une pureté souvent supérieurs à ceux de l'émeraude.",
            formuleChimique = "Ca₃Al₂(SiO₄)₃ (V, Cr)",
            systemeCristallin = "Cubique",
            durete = "7 - 7,5",
            origines = listOf("Kenya", "Tanzanie"),
            particularites = "Jamais traitée ; gisements limités rendant les grosses pierres très rares."
        ),
        Gem(
            id = "jade-jadeite",
            nom = "Jade (jadéite)",
            nomLatin = "Jadéite",
            couleur = GemColorCategory.VERT,
            descriptionCourte = "La pierre sacrée de la culture chinoise.",
            descriptionLongue = "La jadéite forme un agrégat microcristallin d'une ténacité exceptionnelle, ce qui la distingue du néphrite, l'autre minéral appelé « jade ». Vénérée en Chine comme « pierre du ciel », elle symbolise la pureté et la vertu.",
            formuleChimique = "NaAlSi₂O₆",
            systemeCristallin = "Monoclinique",
            durete = "6,5 - 7",
            origines = listOf("Birmanie", "Guatemala"),
            particularites = "Extrêmement tenace malgré une dureté modérée ; qualité « impériale » d'un vert translucide très rare."
        ),
        Gem(
            id = "tourmaline-verte",
            nom = "Tourmaline verte (Verdelite)",
            nomLatin = "Elbaïte (variété verte)",
            couleur = GemColorCategory.VERT,
            descriptionCourte = "Vert coloré par le fer ou le chrome.",
            descriptionLongue = "La verdelite couvre une large palette de verts, du vert menthe clair au vert forêt profond selon les traces de fer, de chrome ou de vanadium. Son fort pléochroïsme peut faire varier sensiblement sa teinte selon l'angle de taille.",
            formuleChimique = "Na(Li,Al)₃Al₆(BO₃)₃Si₆O₁₈(OH)₄",
            systemeCristallin = "Trigonal",
            durete = "7 - 7,5",
            origines = listOf("Brésil", "Mozambique", "Nigeria"),
            particularites = "Forte biréfringence ; les tailleurs orientent la pierre pour optimiser la couleur."
        ),

        // ---------- BLEU ----------
        Gem(
            id = "saphir-bleu",
            nom = "Saphir bleu",
            nomLatin = "Corindon (variété bleue)",
            couleur = GemColorCategory.BLEU,
            descriptionCourte = "Le bleu de référence en haute joaillerie.",
            descriptionLongue = "Le saphir bleu doit sa couleur au fer et au titane. Le « bleu velouté » des pierres non traitées du Cachemire reste la référence absolue des connaisseurs, suivi par les provenances de Birmanie et du Sri Lanka.",
            formuleChimique = "Al₂O₃ (Fe, Ti)",
            systemeCristallin = "Trigonal",
            durete = "9",
            origines = listOf("Cachemire", "Birmanie", "Sri Lanka", "Madagascar"),
            particularites = "Inclusions de rutile en soie pouvant produire un astérisme (saphir étoilé)."
        ),
        Gem(
            id = "aigue-marine",
            nom = "Aigue-marine",
            nomLatin = "Béryl (variété bleue)",
            couleur = GemColorCategory.BLEU,
            descriptionCourte = "« Eau de mer » cristalline.",
            descriptionLongue = "Son nom, issu du latin « aqua marina », évoque sa couleur bleu pâle à bleu-vert rappelant l'eau de mer. Le béryl bleu se distingue par sa grande pureté, avec très peu d'inclusions visibles à l'œil nu.",
            formuleChimique = "Be₃Al₂(SiO₃)₆ (trace de Fe)",
            systemeCristallin = "Hexagonal",
            durete = "7,5 - 8",
            origines = listOf("Brésil", "Madagascar", "Nigeria", "Zambie"),
            particularites = "Souvent chauffée pour atténuer la composante verte et intensifier le bleu."
        ),
        Gem(
            id = "tanzanite",
            nom = "Tanzanite",
            nomLatin = "Zoïsite (variété bleu-violet)",
            couleur = GemColorCategory.BLEU,
            descriptionCourte = "Une seule source au monde, au pied du Kilimandjaro.",
            descriptionLongue = "Découverte en 1967, la tanzanite n'existe que dans la zone de Mérelani en Tanzanie. Son fort trichroïsme fait apparaître du bleu, du violet et du bordeaux selon l'angle d'observation, avant que la chauffe standard ne stabilise la teinte bleu-violet recherchée.",
            formuleChimique = "Ca₂Al₃(SiO₄)₃(OH) (trace de V)",
            systemeCristallin = "Orthorhombique",
            durete = "6 - 7",
            origines = listOf("Mérelani, Tanzanie"),
            particularites = "Gisement unique et en voie d'épuisement, considérée comme une pierre d'investissement."
        ),
        Gem(
            id = "lapis-lazuli",
            nom = "Lapis-lazuli",
            nomLatin = "Roche (lazurite, pyrite, calcite)",
            couleur = GemColorCategory.BLEU,
            descriptionCourte = "Bleu profond constellé d'or, utilisé depuis l'Antiquité.",
            descriptionLongue = "Le lapis-lazuli est une roche composée principalement de lazurite, associée à de la pyrite (inclusions dorées) et de la calcite (veines blanches). Utilisé en Égypte antique et broyé pour le pigment « bleu outremer », il orne bijoux et objets d'art depuis des millénaires.",
            formuleChimique = "(Na,Ca)₈(AlSiO₄)₆(S,SO₄) + pyrite + calcite",
            systemeCristallin = "Amorphe (agrégat)",
            durete = "5 - 5,5",
            origines = listOf("Afghanistan (Badakhshan)", "Chili"),
            particularites = "Les inclusions dorées de pyrite, réparties uniformément, sont un gage de qualité."
        ),
        Gem(
            id = "spinelle-bleu",
            nom = "Spinelle bleu",
            nomLatin = "Spinelle (variété bleue, riche en cobalt)",
            couleur = GemColorCategory.BLEU,
            descriptionCourte = "Bleu intense, parfois coloré au cobalt.",
            descriptionLongue = "Plus rare que le spinelle rouge, le spinelle bleu doit sa teinte la plus prisée à des traces de cobalt, produisant une couleur d'un bleu profond et velouté sous éclairage incandescent.",
            formuleChimique = "MgAl₂O₄ (trace de Co, Fe)",
            systemeCristallin = "Cubique",
            durete = "8",
            origines = listOf("Sri Lanka", "Birmanie", "Tadjikistan"),
            particularites = "Généralement non traité, contrairement à de nombreux saphirs bleus."
        ),

        // ---------- VIOLET ----------
        Gem(
            id = "amethyste",
            nom = "Améthyste",
            nomLatin = "Quartz (variété violette)",
            couleur = GemColorCategory.VIOLET,
            descriptionCourte = "La pierre de tempérance.",
            descriptionLongue = "L'améthyste doit sa couleur violette à des traces de fer combinées à une irradiation naturelle. Abondante et accessible, elle a néanmoins été portée par la noblesse et le clergé pendant des siècles, symbole de sobriété et de spiritualité.",
            formuleChimique = "SiO₂ (trace de Fe)",
            systemeCristallin = "Trigonal",
            durete = "7",
            origines = listOf("Brésil", "Uruguay", "Zambie", "Madagascar"),
            particularites = "Peut se décolorer sous exposition prolongée à la lumière du soleil."
        ),
        Gem(
            id = "spinelle-violet",
            nom = "Spinelle violet",
            nomLatin = "Spinelle (variété violette)",
            couleur = GemColorCategory.VIOLET,
            descriptionCourte = "Résistance et éclat pour une alternative durable.",
            descriptionLongue = "Le spinelle violet combine une bonne dureté et l'absence de clivage, ce qui en fait une alternative très résistante à l'améthyste ou au saphir violet, avec un éclat vitreux particulièrement vif.",
            formuleChimique = "MgAl₂O₄ (trace de Fe, Zn)",
            systemeCristallin = "Cubique",
            durete = "8",
            origines = listOf("Sri Lanka", "Birmanie", "Tanzanie"),
            particularites = "Généralement non traité."
        ),
        Gem(
            id = "iolite",
            nom = "Iolite",
            nomLatin = "Cordiérite",
            couleur = GemColorCategory.VIOLET,
            descriptionCourte = "Le « saphir des Vikings ».",
            descriptionLongue = "L'iolite doit son nom au grec « ios » (violet). Son pléochroïsme spectaculaire fait passer sa couleur du bleu-violet au jaune pâle presque incolore selon l'angle d'observation, une particularité qu'auraient utilisée les navigateurs vikings pour s'orienter.",
            formuleChimique = "Mg₂Al₄Si₅O₁₈",
            systemeCristallin = "Orthorhombique",
            durete = "7 - 7,5",
            origines = listOf("Inde", "Sri Lanka", "Madagascar"),
            particularites = "Pléochroïsme trichroïque très marqué, déterminant pour l'orientation de taille."
        ),
        Gem(
            id = "grenat-rhodolite-violet",
            nom = "Grenat rhodolite violacé",
            nomLatin = "Pyrope-Almandin",
            couleur = GemColorCategory.VIOLET,
            descriptionCourte = "Teinte pourpre changeant selon l'éclairage.",
            descriptionLongue = "Certaines rhodolites présentent une dominante pourpre-violette particulièrement marquée sous éclairage naturel, offrant une alternative peu coûteuse aux pierres violettes plus rares.",
            formuleChimique = "(Mg,Fe)₃Al₂(SiO₄)₃",
            systemeCristallin = "Cubique",
            durete = "7 - 7,5",
            origines = listOf("Tanzanie", "Mozambique"),
            particularites = "Couleur stable, jamais traitée."
        ),

        // ---------- ROSE ----------
        Gem(
            id = "morganite",
            nom = "Morganite",
            nomLatin = "Béryl (variété rose)",
            couleur = GemColorCategory.ROSE,
            descriptionCourte = "Rose tendre à pêche, nommée en 1911.",
            descriptionLongue = "Baptisée en l'honneur du financier et collectionneur J.P. Morgan par le gemmologue George Kunz, la morganite offre une teinte rose à pêche délicate due au manganèse. Très pure, elle se taille souvent en grandes pierres spectaculaires.",
            formuleChimique = "Be₃Al₂(SiO₃)₆ (trace de Mn)",
            systemeCristallin = "Hexagonal",
            durete = "7,5 - 8",
            origines = listOf("Brésil", "Madagascar", "Afghanistan"),
            particularites = "Souvent chauffée pour atténuer une composante orangée et révéler un rose pur."
        ),
        Gem(
            id = "kunzite",
            nom = "Kunzite",
            nomLatin = "Spodumène (variété rose)",
            couleur = GemColorCategory.ROSE,
            descriptionCourte = "Pléochroïsme marqué, pierre délicate.",
            descriptionLongue = "Découverte en 1902 et nommée d'après le gemmologue George Frederick Kunz, la kunzite affiche un rose lilas séduisant. Elle est cependant sensible à la lumière prolongée et aux chocs thermiques, à réserver pour un port occasionnel.",
            formuleChimique = "LiAlSi₂O₆ (trace de Mn)",
            systemeCristallin = "Monoclinique",
            durete = "6,5 - 7",
            origines = listOf("Afghanistan", "Brésil", "Madagascar"),
            particularites = "Clivage net dans deux directions ; à protéger de la lumière directe prolongée."
        ),
        Gem(
            id = "saphir-rose",
            nom = "Saphir rose",
            nomLatin = "Corindon (variété rose)",
            couleur = GemColorCategory.ROSE,
            descriptionCourte = "À la frontière entre rubis et saphir.",
            descriptionLongue = "Coloré par le chrome comme le rubis mais en concentration moindre, le saphir rose se situe à la limite conventionnelle du rubis : au-delà d'une certaine saturation, la pierre est reclassée en rubis.",
            formuleChimique = "Al₂O₃ (trace de Cr)",
            systemeCristallin = "Trigonal",
            durete = "9",
            origines = listOf("Sri Lanka", "Madagascar", "Tanzanie"),
            particularites = "La limite rubis/saphir rose reste débattue selon les laboratoires gemmologiques."
        ),
        Gem(
            id = "tourmaline-rose",
            nom = "Tourmaline rose",
            nomLatin = "Elbaïte (variété rose)",
            couleur = GemColorCategory.ROSE,
            descriptionCourte = "Manganèse pour une teinte tendre.",
            descriptionLongue = "Moins saturée que la rubellite, la tourmaline rose offre des teintes douces allant du rose pâle au rose bonbon, souvent associées à des cristaux bicolores ou tricolores caractéristiques du groupe.",
            formuleChimique = "Na(Li,Al)₃Al₆(BO₃)₃Si₆O₁₈(OH)₄",
            systemeCristallin = "Trigonal",
            durete = "7 - 7,5",
            origines = listOf("Brésil", "Afghanistan", "Mozambique"),
            particularites = "Cristaux souvent zonés, à l'origine des tourmalines bicolores."
        ),
        Gem(
            id = "rhodochrosite",
            nom = "Rhodochrosite",
            nomLatin = "Rhodochrosite",
            couleur = GemColorCategory.ROSE,
            descriptionCourte = "Bandes concentriques roses et blanches.",
            descriptionLongue = "Reconnaissable à ses motifs concentriques rose et blanc évoquant des cernes d'arbre, la rhodochrosite est une pierre décorative et de collection appréciée, mais trop fragile pour un port quotidien en bague.",
            formuleChimique = "MnCO₃",
            systemeCristallin = "Trigonal",
            durete = "3,5 - 4",
            origines = listOf("Argentine", "Pérou", "États-Unis"),
            particularites = "Très fragile et sensible aux acides ; réservée aux pièces de collection ou pendentifs protégés."
        ),

        // ---------- INCOLORE & BLANC ----------
        Gem(
            id = "diamant",
            nom = "Diamant",
            nomLatin = "Carbone cristallisé",
            couleur = GemColorCategory.INCOLORE,
            descriptionCourte = "La gemme la plus dure connue.",
            descriptionLongue = "Formé sous très haute pression et température dans le manteau terrestre, le diamant est composé de carbone pur cristallisé. Sa dureté maximale, sa forte dispersion (le « feu ») et son éclat adamantin en font la pierre de référence de la haute joaillerie, évaluée selon les « 4C » : carat, couleur, pureté (clarity) et taille (cut).",
            formuleChimique = "C",
            systemeCristallin = "Cubique",
            durete = "10",
            origines = listOf("Afrique du Sud", "Botswana", "Russie", "Canada"),
            particularites = "Seule gemme à dureté 10 ; certification obligatoire (GIA, HRD...) pour les pierres importantes."
        ),
        Gem(
            id = "zircon-blanc",
            nom = "Zircon blanc",
            nomLatin = "Zircon (variété incolore)",
            couleur = GemColorCategory.INCOLORE,
            descriptionCourte = "Forte dispersion, longtemps utilisé comme substitut au diamant.",
            descriptionLongue = "À ne pas confondre avec la zircone cubique (matériau synthétique), le zircon est une gemme naturelle dont la forte dispersion lumineuse rivalise avec celle du diamant, ce qui en a fait un substitut historique apprécié.",
            formuleChimique = "ZrSiO₄",
            systemeCristallin = "Quadratique",
            durete = "6 - 7,5",
            origines = listOf("Cambodge", "Sri Lanka", "Australie"),
            particularites = "Souvent chauffé pour obtenir l'incolore à partir de zircon brun ; arêtes de facettes parfois dédoublées."
        ),
        Gem(
            id = "goshenite",
            nom = "Goshenite (béryl blanc)",
            nomLatin = "Béryl (variété incolore)",
            couleur = GemColorCategory.INCOLORE,
            descriptionCourte = "La variété incolore, rare, du béryl.",
            descriptionLongue = "Moins recherchée commercialement que ses cousines colorées (émeraude, aigue-marine, morganite), la goshenite est pourtant la forme la plus pure du béryl, totalement dépourvue d'éléments chromogènes.",
            formuleChimique = "Be₃Al₂(SiO₃)₆",
            systemeCristallin = "Hexagonal",
            durete = "7,5 - 8",
            origines = listOf("Brésil", "Madagascar"),
            particularites = "Servait historiquement de verres de lunettes avant l'usage du verre optique."
        ),
        Gem(
            id = "cristal-de-roche",
            nom = "Cristal de roche",
            nomLatin = "Quartz (variété incolore)",
            couleur = GemColorCategory.INCOLORE,
            descriptionCourte = "Le quartz pur, transparent et abondant.",
            descriptionLongue = "Totalement incolore à l'état pur, le cristal de roche est l'une des gemmes les plus abondantes sur Terre. Il fut longtemps utilisé pour tailler des sphères de divination et des optiques avant l'ère du verre industriel.",
            formuleChimique = "SiO₂",
            systemeCristallin = "Trigonal",
            durete = "7",
            origines = listOf("Brésil", "Madagascar", "Alpes suisses"),
            particularites = "Grande disponibilité en gros cristaux, permettant des tailles fantaisie spectaculaires."
        ),

        // ---------- NOIR ----------
        Gem(
            id = "onyx",
            nom = "Onyx",
            nomLatin = "Calcédoine (variété noire)",
            couleur = GemColorCategory.NOIR,
            descriptionCourte = "Bandes noires et blanches, souvent teintées.",
            descriptionLongue = "Variété microcristalline de quartz de la famille des calcédoines, l'onyx présente naturellement des bandes noires et blanches. La plupart des pierres commercialisées sont teintées pour obtenir un noir uniforme et intense.",
            formuleChimique = "SiO₂",
            systemeCristallin = "Trigonal (microcristallin)",
            durete = "6,5 - 7",
            origines = listOf("Brésil", "Inde", "Madagascar"),
            particularites = "Teinture artificielle quasi systématique pour homogénéiser la couleur."
        ),
        Gem(
            id = "spinelle-noir",
            nom = "Spinelle noir",
            nomLatin = "Spinelle (variété opaque, riche en fer)",
            couleur = GemColorCategory.NOIR,
            descriptionCourte = "Éclat métallique pour la joaillerie masculine.",
            descriptionLongue = "Opaque et d'un noir profond, le spinelle noir est particulièrement apprécié en joaillerie masculine contemporaine pour son éclat presque métallique et sa robustesse au port quotidien.",
            formuleChimique = "MgAl₂O₄ (riche en Fe)",
            systemeCristallin = "Cubique",
            durete = "8",
            origines = listOf("Sri Lanka", "Thaïlande"),
            particularites = "Résistance élevée aux rayures, adaptée aux bijoux portés au quotidien."
        ),
        Gem(
            id = "tourmaline-noire",
            nom = "Tourmaline noire (Schorl)",
            nomLatin = "Schorl",
            couleur = GemColorCategory.NOIR,
            descriptionCourte = "La variété de tourmaline la plus commune.",
            descriptionLongue = "Riche en fer, le schorl est de loin la variété de tourmaline la plus répandue sur Terre. Opaque et d'un noir de jais, il est traditionnellement associé à des vertus protectrices en lithothérapie.",
            formuleChimique = "NaFe₃Al₆(BO₃)₃Si₆O₁₈(OH)₄",
            systemeCristallin = "Trigonal",
            durete = "7 - 7,5",
            origines = listOf("Brésil", "Namibie", "Madagascar"),
            particularites = "Cristaux prismatiques souvent striés, parfois de grande taille."
        ),
        Gem(
            id = "obsidienne",
            nom = "Obsidienne",
            nomLatin = "Verre volcanique",
            couleur = GemColorCategory.NOIR,
            descriptionCourte = "Verre naturel à la cassure tranchante.",
            descriptionLongue = "Formée par le refroidissement rapide de lave riche en silice, l'obsidienne est un verre volcanique amorphe. Sa cassure conchoïdale extrêmement tranchante en a fait un outil de choix dès la Préhistoire, notamment pour les pointes de flèches et lames.",
            formuleChimique = "SiO₂ (verre amorphe)",
            systemeCristallin = "Amorphe",
            durete = "5 - 5,5",
            origines = listOf("Mexique", "Islande", "Arménie"),
            particularites = "Certaines variétés présentent un chatoiement doré ou irisé (obsidienne arc-en-ciel)."
        ),

        // ---------- MULTICOLORE / CHANGEMENT DE COULEUR ----------
        Gem(
            id = "alexandrite",
            nom = "Alexandrite",
            nomLatin = "Chrysobéryl (variété à changement de couleur)",
            couleur = GemColorCategory.MULTICOLORE,
            descriptionCourte = "Vert le jour, rouge sous lumière incandescente.",
            descriptionLongue = "Découverte dans l'Oural au XIXe siècle et nommée en l'honneur du futur tsar Alexandre II, l'alexandrite change spectaculairement de couleur : vert émeraude à la lumière du jour, rouge pourpre sous éclairage incandescent. Les pierres présentant un changement marqué sont extrêmement rares et recherchées.",
            formuleChimique = "BeAl₂O₄ (trace de Cr)",
            systemeCristallin = "Orthorhombique",
            durete = "8,5",
            origines = listOf("Oural (Russie, historique)", "Brésil", "Sri Lanka"),
            particularites = "Le pourcentage de changement de couleur est le principal critère de valorisation."
        ),
        Gem(
            id = "opale-precieuse",
            nom = "Opale précieuse",
            nomLatin = "Opale",
            couleur = GemColorCategory.MULTICOLORE,
            descriptionCourte = "Un jeu de couleurs unique en son genre.",
            descriptionLongue = "L'opale précieuse est composée de micro-sphères de silice ordonnées qui diffractent la lumière et produisent un jeu de couleurs mouvant (opalescence), unique à chaque pierre. Aucune autre gemme ne présente ce phénomène optique de la même façon.",
            formuleChimique = "SiO₂ · nH₂O",
            systemeCristallin = "Amorphe",
            durete = "5,5 - 6,5",
            origines = listOf("Australie", "Éthiopie", "Mexique"),
            particularites = "Contient de 3 à 21% d'eau ; sensible à la déshydratation et aux chocs."
        ),
        Gem(
            id = "tourmaline-pasteque",
            nom = "Tourmaline pastèque",
            nomLatin = "Elbaïte (cristal zoné bicolore)",
            couleur = GemColorCategory.MULTICOLORE,
            descriptionCourte = "Cœur rose cerné de vert, comme une tranche de fruit.",
            descriptionLongue = "Résultat d'une croissance cristalline en plusieurs étapes chimiques, la tourmaline pastèque présente un cœur rose entouré d'une couche verte, taillée en tranches fines pour révéler ce motif naturel saisissant.",
            formuleChimique = "Na(Li,Al)₃Al₆(BO₃)₃Si₆O₁₈(OH)₄",
            systemeCristallin = "Trigonal",
            durete = "7 - 7,5",
            origines = listOf("Brésil", "États-Unis (Maine)"),
            particularites = "Taillée le plus souvent en tranches transversales pour révéler l'effet « pastèque »."
        ),
        Gem(
            id = "labradorite",
            nom = "Labradorite",
            nomLatin = "Feldspath plagioclase",
            couleur = GemColorCategory.MULTICOLORE,
            descriptionCourte = "Reflets métalliques mobiles (labradorescence).",
            descriptionLongue = "La labradorite présente un phénomène optique appelé labradorescence : des éclats bleu-vert, parfois dorés ou violacés, apparaissent et disparaissent selon l'angle d'observation, dus à des lamelles internes qui diffractent la lumière.",
            formuleChimique = "(Ca,Na)(Al,Si)₄O₈",
            systemeCristallin = "Triclinique",
            durete = "6 - 6,5",
            origines = listOf("Canada (Labrador)", "Madagascar", "Finlande"),
            particularites = "L'orientation de la taille est déterminante pour révéler la labradorescence."
        )
    )

    fun byColor(color: GemColorCategory): List<Gem> =
        gems.filter { it.couleur == color }.sortedBy { it.nom }

    fun byId(id: String): Gem? = gems.firstOrNull { it.id == id }

    fun countByColor(color: GemColorCategory): Int = gems.count { it.couleur == color }
}
