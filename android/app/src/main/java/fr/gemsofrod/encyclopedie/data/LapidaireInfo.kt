package fr.gemsofrod.encyclopedie.data

data class LapidaireComponent(
    val nom: String,
    val description: String
)

data class LapidaireDisc(
    val nom: String,
    val grain: String,
    val usage: String
)

data class LapidaireAngles(
    val coupe: String,
    val couronne: String,
    val pavillon: String,
    val table: String,
    val facettes: String,
    val source: String
)

data class LapidaireDiagram(
    val id: String,
    val legende: String
)

data class LapidaireTip(
    val texte: String
)

data class LapidaireOptiqueEntry(
    val pierre: String,
    val angleCritique: String,
    val angleExtinction: String
)

data class LapidaireDefaut(
    val probleme: String,
    val cause: String,
    val remede: String
)

data class LapidaireIndexEntry(
    val index: String,
    val rotationParCran: String,
    val cotesTaillables: String
)

data class LapidaireEspeceFiche(
    val pierre: String,
    val orientation: String,
    val fragilite: String,
    val polissage: String
)

data class LapidaireConservationTip(
    val titre: String,
    val conseil: String
)

enum class LapidaireMachineCategorie { FACETTAGE, CABOCHON, POLYVALENT }

data class LapidaireMachineFiche(
    val photoId: String,
    val nom: String,
    val categorie: LapidaireMachineCategorie,
    val description: String,
    val caracteristiques: List<String>,
    val technique: String
)

data class LapidairePoidsCalculator(
    val title: String,
    val intro: String,
    val shapeLabels: Map<LapidaireCutShape, String>,
    val dimension1Label: String,
    val dimension2Label: String,
    val heightLabel: String,
    val sgLabel: String,
    val computeLabel: String,
    val resultLabel: String,
    val disclaimer: String,
    val errorMessage: String
)

data class LapidairePage(
    val intro: String,
    val machinesTitle: String,
    val machinesIntro: String,
    val machines: List<LapidaireComponent>,
    val disquesTitle: String,
    val disquesIntro: String,
    val disques: List<LapidaireDisc>,
    val indexTitle: String,
    val indexIntro: String,
    val indexTable: List<LapidaireIndexEntry>,
    val poidsCalculator: LapidairePoidsCalculator,
    val anglesTitle: String,
    val anglesIntro: String,
    val angles: List<LapidaireAngles>,
    val optiqueTitle: String,
    val optiqueIntro: String,
    val optiqueTable: List<LapidaireOptiqueEntry>,
    val defautsTitle: String,
    val defautsIntro: String,
    val defauts: List<LapidaireDefaut>,
    val especesTitle: String,
    val especesIntro: String,
    val especes: List<LapidaireEspeceFiche>,
    val diagrammesTitle: String,
    val diagrammes: List<LapidaireDiagram>,
    val tipsTitle: String,
    val tips: List<LapidaireTip>,
    val conservationTitle: String,
    val conservationIntro: String,
    val conservation: List<LapidaireConservationTip>,
    val disclaimerTitle: String,
    val disclaimerBody: String,
    // Section "Les machines du métier" : panorama des types de machines
    // réellement utilisés en atelier (facettage et cabochonnage), distinct
    // de [machines] ci-dessus qui décrit les composants d'une seule machine
    // à facettes. Traduite dans les 9 langues de l'app ; les valeurs par
    // défaut restent vides pour que l'écran masque la section si jamais
    // une page venait à en manquer.
    val machinesTypesTitle: String = "",
    val machinesTypesIntro: String = "",
    val categorieFacettageLabel: String = "",
    val categorieCabochonLabel: String = "",
    val categoriePolyvalentLabel: String = "",
    val caracteristiquesLabel: String = "",
    val techniqueLabel: String = "",
    val machinesTypes: List<LapidaireMachineFiche> = emptyList()
)

/**
 * Contenu éditorial dédié au métier de lapidaire, centré sur la taille des
 * facettes (faceting) : composants d'une machine à facettes, progression des
 * disques de mise en forme et de polissage, angles de référence pour le
 * brillant rond (seule coupe pour laquelle une table de proportions précise
 * et largement vérifiable — Tolkowsky, 1919 — est citée ; les autres tailles
 * varient trop selon le dessin pour donner des chiffres inventés), et
 * conseils pratiques. Les diagrammes référencés ici ([diagrammes]) pointent
 * vers des images réelles récupérées automatiquement sur Wikimedia Commons /
 * Openverse par scripts/fetch_lapidaire_diagrams.py (mêmes règles de licence
 * que les photos de gemmes) — jamais une illustration dessinée par l'agent.
 * Même patron que [TreatmentsInfo] et [GemInstrumentsInfo].
 */
object LapidaireInfo {
    private val fr = LapidairePage(
        intro = "Le lapidaire façonne et polit les pierres brutes en pierres taillées. Ce métier recouvre plusieurs spécialités — la taille de facettes (faceting) pour les pierres transparentes, le cabochon pour les pierres opaques ou translucides, la gravure et le travail ornemental — mais la taille de facettes, la plus technique, est celle qui donne à une pierre son jeu de lumière. Cette fiche présente le matériel et les repères de base du métier ; elle s'adresse aux professionnels et aux amateurs éclairés, pas à un premier apprentissage sans encadrement.",
        machinesTitle = "La machine à facettes",
        machinesIntro = "Une machine à facettes (faceting machine) maintient la pierre à un angle et un index précis contre un disque abrasif rotatif (le plateau, ou lap). Sa précision mécanique — au dixième de degré près pour l'angle, au point près pour l'index — est ce qui distingue une taille professionnelle d'un simple meulage à main levée. Le matériel va du simple bâton avec évention (artisanal, rapide mais peu précis) aux machines à diviseur mécanique (amovibles ou fixes, précises mais de production plus lente) jusqu'aux modèles pilotés par ordinateur pour la taille en série.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mât (mast)",
                description = "Colonne verticale fixée au bâti de la machine, sur laquelle coulisse et pivote la tête porte-quille. Sa hauteur et son inclinaison réglable déterminent, avec le bras, l'angle de facettage appliqué au plateau."
            ),
            LapidaireComponent(
                nom = "Quille (quill)",
                description = "Tube dans lequel coulisse la tige portant la pierre (le dop). La quille tourne sur elle-même pour amener successivement chaque facette prévue face au disque, et coulisse pour avancer la pierre à mesure que la matière est enlevée."
            ),
            LapidaireComponent(
                nom = "Tête d'index (index gear)",
                description = "Molette dentée fixée au bout de la quille, disponible chez la plupart des fabricants en plusieurs graduations — 64, 72, 80, 96 ou 120 crans — pour couvrir tous les types de facettages ; un disque « index rapide », à moins de crans, se superpose souvent à l'index normal pour accélérer le repérage lors d'une taille en série. Le blocage sur une position précise à chaque facette est indispensable à la symétrie d'une taille comme le brillant rond, qui répartit ses facettes selon une symétrie d'ordre 8. Le choix de l'index dépend du nombre de côtés à tailler : il doit en être un multiple exact — un pentagone, par exemple, est infaisable avec un index 96 (96 ÷ 5 n'est pas entier) mais se taille sans problème avec un index 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater (« tricheur »)",
                description = "Bague de réglage fin superposée à l'angle affiché sur le bras. Son usage principal n'est pas de modifier l'angle de coupe lui-même, mais de rétablir le parallélisme entre la facette en cours et le plateau lorsque celui-ci s'est légèrement voilé ou usé de façon inégale — un réglage à refaire chaque fois qu'une facette ne se polit plus uniformément sur toute sa surface."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Tige (bois, aluminium ou laiton — ces deux derniers retiennent mieux la chaleur de préchauffage) sur laquelle la pierre est fixée avant d'être insérée dans la quille. La fixation se fait à la cire chauffée (le plus courant, fond vers 80 °C), à la colle cyanoacrylate (« superglue », prise rapide mais résidus difficiles à retirer) ou à l'époxy (bonne tenue, mais réaction exothermique à surveiller sur pierre sensible à la chaleur). Les dops sophistiqués portent un ergot, un téton ou une rainure qui les oriente toujours de la même façon dans le diviseur — condition indispensable pour transférer la pierre d'un dop à l'autre (tailler la couronne après le pavillon) sans désaxement ni pivotement parasite."
            ),
            LapidaireComponent(
                nom = "Plateau (lap)",
                description = "Disque métallique rotatif, généralement de 6\" (≈ 152 mm) ou 8\" (≈ 203 mm) de diamètre, sur lequel est fixé ou chargé l'abrasif. On distingue les plateaux pour tailler (diamant électrodéposé en surface, ou fonte imprégnée de particules de diamant par roulage) des plateaux pour polir, de matière plus tendre. Une machine dispose généralement de plusieurs plateaux interchangeables, un par étape du grain."
            ),
            LapidaireComponent(
                nom = "Goniomètre",
                description = "Instrument de contrôle qui mesure l'angle réel d'une pierre déjà taillée, sur deux facettes principales diamétralement opposées, pour vérifier la coupe obtenue. L'angle mesuré (C' pour la couronne, P' pour le pavillon) se convertit par la formule angle = (180° − angle mesuré) / 2, puisque le rapporteur relève l'angle complémentaire formé avec le plan du rondiste."
            )
        ),
        disquesTitle = "Disques de mise en forme et de polissage",
        disquesIntro = "La taille progresse par étapes de grain de plus en plus fin, chacune effaçant les micro-rayures laissées par la précédente ; sauter une étape laisse des marques que le polissage final ne peut plus rattraper. La vitesse de rotation du plateau accompagne cette progression : plutôt lente à l'ébauche (100 à 300 tours/minute), plus rapide au formage des facettes (300 à 600 tr/min), et la plus rapide au polissage (700 à 1000 tr/min ou plus) — toujours en fonction de la sensibilité de la pierre, ralentie sans hésiter pour une pierre tendre ou clivable.",
        disques = listOf(
            LapidaireDisc(
                nom = "Dégrossissage",
                grain = "260 à 325",
                usage = "Élimine rapidement la matière pour donner à la pierre sa forme générale (préforme) à partir du brut ou d'une tranche sciée. Plateau diamanté agressif, forte pression, refroidissement à l'eau obligatoire."
            ),
            LapidaireDisc(
                nom = "Formage des facettes",
                grain = "600 à 1200",
                usage = "Trace et affine chaque facette à l'angle et à l'index exacts prévus par le diagramme de taille. C'est l'étape la plus longue et la plus déterminante pour la symétrie et la précision des points de rencontre (meet points) entre facettes voisines."
            ),
            LapidaireDisc(
                nom = "Pré-polissage",
                grain = "3000 à 8000",
                usage = "Affine l'état de surface laissé par le formage jusqu'à un aspect presque mat mais sans rayure profonde, en conservant exactement les mêmes angles — toute correction d'angle à ce stade doit être minime, sous peine de devoir redescendre à un grain plus grossier."
            ),
            LapidaireDisc(
                nom = "Polissage final",
                grain = "Pâte ou poudre submicronique (souvent oxyde de cérium ou diamant très fin)",
                usage = "Le plateau de polissage se choisit par dureté (échelle de Mohs), du plus dur au plus tendre : céramique (9), cuivre (3), zinc (2,5), phénolique (2,2), plexiglas ou étain-plomb (2), étain pur (1,7), plomb (1,5), PVC (1) et cire additionnée de charges diverses (0,3, variable selon la formule). Ce n'est jamais le plateau lui-même qui polit, seulement la poudre appliquée dessus qui joue le rôle d'agent abrasif — le plateau n'est qu'un support dont la dureté doit correspondre à celle de la pierre. Polissage à sec (poudre fixée par un liant gras) ou humide (poudre délayée dans l'eau, appliquée en continu) selon la méthode retenue."
            )
        ),
        indexTitle = "Choix de l'index",
        indexIntro = "Un index se choisit avant tout pour le nombre de côtés à tailler : il doit en être un multiple exact (un pentagone est infaisable sur un index 96, possible sur un index 80). La rotation apportée par un cran est inversement proportionnelle au nombre de crans de l'index (360° divisé par le nombre de crans) — un index 120 avance de 3° par cran, un index 32 de 11,25°. Reconvertir un schéma de taille d'un index vers un autre n'est possible que si les deux index sont multiples ou sous-multiples l'un de l'autre : il suffit alors de diviser ou multiplier chaque numéro d'index par leur rapport (diviser par 3, par exemple, pour passer d'un index 96 à un index 32). Tous les index courants étant des multiples de 4, un carré se taille sur n'importe lequel d'entre eux ; en revanche, il est impossible d'indexer à une position intermédiaire entre deux crans, ce qui exclut d'office la plupart des polygones à nombre impair de côtés. L'index 96 reste le plus répandu chez les fabricants, offrant un bon compromis entre finesse de graduation et rapidité de repérage ; l'index 120 est le plus complet, au prix d'un repérage plus long.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / cran", cotesTaillables = "Carré (4), octogone (8), hexadécagone (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / cran", cotesTaillables = "Triangle (3), carré (4), pentagone (5), hexagone (6), décagone (10), dodécagone (12), pentadécagone (15), icosagone (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / cran", cotesTaillables = "Carré (4), octogone (8), hexadécagone (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / cran", cotesTaillables = "Triangle (3), carré (4), hexagone (6), octogone (8), ennéagone (9), dodécagone (12), octodécagone (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / cran", cotesTaillables = "Carré (4), pentagone (5), octogone (8), décagone (10), hexadécagone (16), icosagone (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / cran", cotesTaillables = "Le plus courant — 9 polygones réguliers taillables au total, dont carré, hexagone, octogone, dodécagone"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / cran", cotesTaillables = "Le plus complet — 14 polygones réguliers taillables au total, le maximum parmi les index courants"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / cran", cotesTaillables = "Carré (4), octogone (8), hexadécagone (16), 32 côtés, 64 côtés")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Poids estimé",
            intro = "Pour une pierre déjà taillée et montée, impossible à peser directement : le poids se déduit du volume, à partir de mesures au pied à coulisse (précision au 1/100 mm) et du poids spécifique de l'espèce. Formule indicative, précision de l'ordre de 10 à 15 % — pas un pesage réel.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Rond",
                LapidaireCutShape.OVALE to "Ovale",
                LapidaireCutShape.COUSSIN_CARRE to "Coussin carré",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Coussin rectangulaire",
                LapidaireCutShape.CARRE_A_GRADIN to "Carré à gradins",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rectangle à gradins",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Coussin carré (gradins)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Coussin rectangulaire (gradins)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Poire",
                LapidaireCutShape.TRIANGLE_BOMBE to "Triangle bombé",
                LapidaireCutShape.TRIANGLE to "Triangle",
                LapidaireCutShape.TRAPEZE to "Trapèze",
                LapidaireCutShape.COEUR to "Cœur"
            ),
            dimension1Label = "Longueur ou diamètre (mm)",
            dimension2Label = "Largeur (mm)",
            heightLabel = "Hauteur totale, table à culet (mm)",
            sgLabel = "Poids spécifique de l'espèce",
            computeLabel = "Calculer",
            resultLabel = "Poids estimé : %s carats",
            disclaimer = "Estimation par le volume, précision indicative 10 à 15 % — ne remplace pas une pesée réelle.",
            errorMessage = "Renseignez des valeurs valides (nombres positifs) pour toutes les dimensions et le poids spécifique."
        ),
        anglesTitle = "Angles de référence : le brillant rond",
        anglesIntro = "Les proportions ci-dessous sont celles publiées par le mathématicien belge Marcel Tolkowsky en 1919, qui a établi par le calcul l'angle optimal pour maximiser le retour de lumière (brillance) et la dispersion (feu) d'un diamant taillé en brillant rond — c'est la référence historique encore utilisée comme point de départ aujourd'hui, bien que les laboratoires modernes (GIA notamment) admettent une plage de tolérance autour de ces valeurs plutôt qu'un chiffre unique. Pour les pierres de couleur, dont l'indice de réfraction diffère de celui du diamant, le rapport classique entre couronne et rondiste se situe plutôt entre 25/75 % et 30/70 %. Règle impérative, quelle que soit la pierre : ne jamais tailler une facette de pavillon à un angle inférieur à l'angle critique de l'espèce (les rayons s'échappent alors par le pavillon, effet de « fenêtre »), ni supérieur à l'angle d'extinction (zones sombres). Pour vérifier une pierre déjà taillée, un goniomètre mesure l'angle sur deux facettes principales opposées : l'angle réel de couronne ou de pavillon se déduit de la lecture par la formule (180° − angle mesuré) / 2. Les autres tailles (princesse, ovale, poire, émeraude, coussin...) suivent chacune leur propre diagramme, très variable selon le fabricant du diagramme et l'objectif recherché (rendement de matière contre performance optique) : il n'existe pas de valeur universelle comparable à citer sans se référer à un diagramme précis.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Brillant rond (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53 % du diamètre du rondiste",
                facettes = "57 ou 58 (33 sur la couronne, 24 ou 25 sur le pavillon, symétrie d'ordre 8)",
                source = "Marcel Tolkowsky, « Diamond Design », 1919"
            )
        ),
        optiqueTitle = "L'angle critique et l'angle d'extinction",
        optiqueIntro = "On distingue schématiquement trois cas de figure pour le trajet de la lumière entrant par la table : un pavillon trop peu profond (« shallow cut ») la laisse s'échapper directement par en dessous ; un pavillon trop profond (« deep cut ») la piège dans des réflexions internes qui ressortent par une facette latérale sans revenir vers l'œil, donnant une pierre terne malgré un poli parfait ; un pavillon bien proportionné (« well cut ») la renvoie au contraire vers l'observateur à travers la couronne, pour un maximum de brillance. Le pavillon d'une pierre facettée agit comme un miroir : en dessous de l'angle critique de l'espèce, la lumière entrant par la table fuit à travers le pavillon (effet de fenêtre déjà évoqué plus haut) ; taillé exactement à l'angle critique, un autre défaut apparaît, l'« œil de poisson » — la table paraît terne alors que le contour de la couronne reste brillant, car les rayons qui la traversent longent la paroi du cône de réflexion sans s'y réfléchir. Il existe aussi une limite haute, l'angle d'extinction, au-delà de laquelle le pavillon perd à nouveau la lumière par la facette opposée : angle d'extinction = 60° − (angle critique / 3). Un pavillon bien taillé respecte donc angle critique < angle du pavillon < angle d'extinction ; plus l'indice de réfraction de la gemme est élevé, plus cette fourchette de travail s'élargit — un diamant pardonne beaucoup plus d'écart qu'une fluorite. La couronne obéit à une règle complémentaire : son angle maximal est inversement proportionnel à celui du pavillon (un pavillon plus court autorise une couronne plus haute) et directement proportionnel à l'indice de réfraction ; travailler quelques dixièmes de degré en dessous de ces maximums reste le choix le plus sûr. Ces schémas de coupe s'appuient tous sur la Technique du Point de Jonction (méthode américaine, Long & Steele), qui utilise les intersections entre trois facettes ou plus comme repères pour garantir automatiquement de bonnes proportions et une bonne conservation de poids par rapport au brut ; pour estimer le poids d'une pierre déjà taillée sans la déserti, la formule usuelle est poids (en carats) = largeur³ × coefficient de volume de la coupe × poids spécifique de l'espèce / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamant", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Sphène (Titanite)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Zircon (haut)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Grenat démantoïde", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alexandrite (chrysobéryl)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Rubis et saphir (corindon)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Spinelle", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Péridot", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Tourmaline", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topaze", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Béryl", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Émeraude", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Aigue-marine", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Améthyste et quartz", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calcite", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluorite", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Défauts courants et corrections",
        defautsIntro = "La plupart des défauts de facettage viennent d'un report d'angle inexact, d'une indexation erronée ou d'un transfert de dop mal aligné plutôt que d'un problème de plateau. La règle de base pour corriger un mauvais raccordement ou un polissage inégal : toujours agir sur le côté opposé au défaut.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Facette décentrée ou mal polie sur un côté",
                cause = "Léger défaut d'alignement de la pierre sur le correcteur.",
                remede = "Tourner le correcteur pour incliner la pierre du côté opposé au défaut."
            ),
            LapidaireDefaut(
                probleme = "Facette mal raccordée ou mal polie en haut, angle trop grand",
                cause = "L'angle affiché sur le rapporteur est supérieur à ce qu'il devrait être.",
                remede = "Réduire légèrement l'angle sur le rapporteur pour incliner la pierre vers le bas."
            ),
            LapidaireDefaut(
                probleme = "Facette mal raccordée ou mal polie en bas, angle trop petit",
                cause = "L'angle affiché sur le rapporteur est inférieur à ce qu'il devrait être.",
                remede = "Augmenter légèrement l'angle sur le rapporteur pour incliner la pierre vers le haut."
            ),
            LapidaireDefaut(
                probleme = "Défaut combiné en diagonale (par exemple en haut à gauche)",
                cause = "La pierre a pu légèrement pivoter sur le dop pendant le transfert.",
                remede = "Combiner une correction au correcteur et sur l'angle, en réduisant au besoin la vitesse de rotation du plateau le temps de reprendre la main."
            ),
            LapidaireDefaut(
                probleme = "La pierre émet un bruit strident sur le plateau de polissage",
                cause = "La facette en cours n'est pas parallèle au plateau.",
                remede = "Arrêter et rectifier la position de la pierre avant de continuer — un signe fiable, à ne jamais ignorer."
            ),
            LapidaireDefaut(
                probleme = "Stries parallèles traversant plusieurs facettes",
                cause = "Plateau contaminé par des grains d'une granulométrie différente.",
                remede = "Décontaminer le plateau (brossage à l'eau chaude savonneuse, puis raclage ou rectification si nécessaire) avant de reprendre."
            ),
            LapidaireDefaut(
                probleme = "Traces de « brûlure » en surface d'une facette",
                cause = "Polissage trop stationnaire, avec une pression excessive, provoquant un échauffement localisé.",
                remede = "Balayer régulièrement la facette sur le plateau, réduire la pression, ajouter au besoin une goutte d'eau par seconde."
            ),
            LapidaireDefaut(
                probleme = "Petite cavité qui apparaît en cours de facettage",
                cause = "Une inclusion proche de la surface a été mise à jour.",
                remede = "Mieux « nettoyer » les inclusions dès l'ébrutage reste la meilleure prévention ; une fois le trou apparu, retailler localement ou envisager une recoupe totale si nécessaire."
            )
        ),
        especesTitle = "Fiches pratiques par espèce",
        especesIntro = "Au-delà de l'angle critique et de l'angle d'extinction déjà donnés plus haut, chaque espèce a ses propres habitudes de taille : orientation de la table selon le pléochroïsme ou le clivage, sensibilité particulière, plateau et poudre de polissage qui donnent les meilleurs résultats. Repères issus de la pratique, pas une règle universelle — chaque pierre garde ses particularités individuelles (inclusions, zonations, état de surface du brut).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamant",
                orientation = "Isotrope : aucune contrainte d'orientation liée à un axe optique.",
                fragilite = "Clivage parfait selon 4 plans octaédriques, bien identifié et exploité par les tailleurs plutôt que subi ; la pointe du pavillon reste le point le plus vulnérable aux chocs.",
                polissage = "Plateau en fonte (scaife) chargé de poudre de diamant — seul le diamant polit le diamant."
            ),
            LapidaireEspeceFiche(
                pierre = "Corindon (Rubis, Saphir)",
                orientation = "Pas de clivage à prendre en compte pour l'orientation.",
                fragilite = "Dureté 9, parmi les plus dures des pierres facettées : peu de précautions particulières.",
                polissage = "Excellents résultats sur plateau cuivre et poudre de diamant."
            ),
            LapidaireEspeceFiche(
                pierre = "Béryl (Émeraude, Aigue-marine, Morganite, Héliodore)",
                orientation = "Clivage imparfait, sans réelle contrainte d'orientation.",
                fragilite = "L'émeraude en particulier réclame une attention aux givres (inclusions), qui peuvent la faire se briser à la taille comme au polissage.",
                polissage = "Acide acétique dans l'eau de refroidissement améliore le polissage ; poudre oxyde d'aluminium, de cérium ou d'étain, ou Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Quartz (Améthyste, Citrine, Cristal de roche, Quartz fumé)",
                orientation = "Pas de clivage ; tenir compte des zonations de couleur au moment de l'orientation.",
                fragilite = "Aucune fragilité de clivage particulière.",
                polissage = "Le résultat réserve parfois des surprises inattendues, sans cause identifiée."
            ),
            LapidaireEspeceFiche(
                pierre = "Topaze",
                orientation = "Décaler la table d'environ 10° par rapport au plan de clivage basal, pour ne pas s'exposer directement dans son axe.",
                fragilite = "Clivage net selon une seule direction (plan basal) — facile à facetter dès lors qu'on l'évite.",
                polissage = "Pas de recommandation de plateau distincte des règles générales."
            ),
            LapidaireEspeceFiche(
                pierre = "Tourmaline",
                orientation = "Tailler de préférence la table parallèle à l'axe optique ; une pierre polychrome se sectionne nettement à la jonction de deux couleurs — mieux vaut la coller que la scier à cet endroit.",
                fragilite = "Sensible à la chaleur et aux chocs en cours de taille.",
                polissage = "Pas de recommandation de plateau distincte des règles générales."
            ),
            LapidaireEspeceFiche(
                pierre = "Grenat (Almandin, Démantoïde, Grossulaire, Pyrope, Spessartite, Uvarovite)",
                orientation = "Isotrope : orientation de table sans contrainte, aucune direction privilégiée.",
                fragilite = "Aucun problème de fragilité lié au clivage.",
                polissage = "Acide acétique (vinaigre) dans l'eau de refroidissement améliore systématiquement le résultat ; poudre oxyde de cérium, d'étain ou d'aluminium."
            ),
            LapidaireEspeceFiche(
                pierre = "Spinelle",
                orientation = "Isotrope, aucune contrainte d'orientation.",
                fragilite = "Aucun problème de fragilité lié au clivage.",
                polissage = "Se polit remarquablement bien sur disque cuivre et pâte diamantée."
            ),
            LapidaireEspeceFiche(
                pierre = "Péridot (Olivine)",
                orientation = "Pléochroïsme très faible, orientation peu contraignante.",
                fragilite = "Aucune fragilité de clivage notable.",
                polissage = "Le résultat réserve parfois des surprises inattendues ; la poudre diamantée convient bien."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorite",
                orientation = "Se sépare facilement selon 4 directions : bien choisir l'orientation pour limiter le risque.",
                fragilite = "Un des angles critiques les plus fermés parmi les pierres facettées, très sensible aux chocs et à l'égrisure.",
                polissage = "Plateau de préférence en cire ; l'acide améliore le résultat."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcite",
                orientation = "Bien orienter pour ne pas exposer directement les plans de clivage.",
                fragilite = "Dureté très faible (3) et clivage parfait selon 3 directions, forte sensibilité à la chaleur — pierre difficile à tailler et à polir ; tourner le plateau lentement (environ 100 tr/min).",
                polissage = "Plateau bois ou cire, poudre oxyde d'étain ou de chrome, avec quelques gouttes d'acide oxalique."
            ),
            LapidaireEspeceFiche(
                pierre = "Chrysobéryl (Alexandrite)",
                orientation = "Pas de contrainte d'orientation liée au clivage.",
                fragilite = "Dureté élevée (8,5), pierre dure sans souci particulier.",
                polissage = "Se polit rapidement à l'acide et à la poudre Linde A ; excellents résultats sur plateau cuivre et poudre de diamant."
            )
        ),
        diagrammesTitle = "Diagrammes",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagramme de proportions du brillant rond : nomenclature de la couronne, du rondiste et du pavillon."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagramme du trajet de la lumière selon la profondeur du pavillon : trop peu profond, bien proportionné, ou trop profond."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Gravure ancienne représentant un moulin à tailler le diamant — la mécanique du plateau rotatif, inchangée dans son principe depuis des siècles."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Machine à facettes moderne : mât, quille et plateau, tels que décrits ci-dessus."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Étapes de la transformation d'une pierre brute en pierre facettée."
            )
        ),
        tipsTitle = "Conseils pratiques",
        tips = listOf(
            LapidaireTip(texte = "Toujours vérifier l'étalonnage de l'angle affiché sur le bras avec une pierre test avant de commencer une pièce de valeur : un décalage de 0,5° sur le pavillon suffit à assombrir visiblement une pierre autrement bien taillée."),
            LapidaireTip(texte = "Centrer soigneusement la pierre sur le dop : un léger désaxement se traduit par un rondiste d'épaisseur irrégulière et des facettes qui ne se rejoignent pas au même point sur tout le pourtour."),
            LapidaireTip(texte = "Respecter une épaisseur de rondiste modérée (ni « fil de rasoir » fragile aux chocs, ni excessivement épaisse, qui emprisonne de la matière sans bénéfice optique) — un repère visuel régulier, ni trop fin ni trop épais, est un des signes d'un travail soigné."),
            LapidaireTip(texte = "Contrôler les points de rencontre (meet points) entre facettes à la loupe sous un éclairage rasant à chaque étape : un point de rencontre manqué se corrige facilement au stade du formage, presque plus une fois le pré-polissage entamé."),
            LapidaireTip(texte = "Adapter la lubrification à la matière du plateau : l'eau convient à la plupart des plateaux diamantés électrodéposés, tandis que certains plateaux de polissage (étain notamment) donnent de meilleurs résultats avec une lubrification à l'huile ou un mélange spécifique — se référer aux recommandations du fabricant du plateau."),
            LapidaireTip(texte = "Vérifier la sensibilité thermique et le clivage de l'espèce avant de tailler : certaines pierres (topaze, kunzite) ont un clivage net qui peut se propager sous la chaleur ou la pression d'un dégrossissage trop agressif."),
            LapidaireTip(texte = "Tester la symétrie finale en observant la pierre de face sous une source ponctuelle : les reflets des facettes de la couronne doivent former un motif régulier ; une asymétrie visible à l'œil nu à ce stade ne se rattrape plus au polissage."),
            LapidaireTip(texte = "Ne jamais poser la pierre sur un plateau à l'arrêt puis le démarrer : lancez toujours le plateau en rotation avant d'y présenter la pierre, avec un mouvement pendulaire léger pour éviter qu'elle ne reste figée au même endroit, ce qui userait le disque de façon inégale et userait la pierre elle-même de façon irrégulière."),
            LapidaireTip(texte = "Une table qui semble « éteinte » alors que le contour de la pierre reste brillant signale un effet de fenêtre : le pavillon est trop peu profond et laisse la lumière s'échapper par en dessous plutôt que de la renvoyer par la table — corrigez en approfondissant l'angle du pavillon, sans jamais dépasser l'angle d'extinction."),
            LapidaireTip(texte = "Sur les pierres allongées (ovale, navette, poire, coussin...), une croix sombre visible au centre (effet dit « X+ ») trahit des facettes de longueur et de largeur mal raccordées sur le pavillon ; un diagramme de coupe bien étudié pour la forme choisie l'évite mieux qu'un ajustement au cas par cas.")
        ),
        conservationTitle = "Conservation et manipulation des pierres taillées",
        conservationIntro = "Une pierre bien taillée reste vulnérable une fois montée ou stockée : quelques précautions simples évitent l'essentiel des rayures et des dégâts accidentels.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Nettoyage",
                conseil = "Ne jamais essuyer une pierre à sec : la poussière agit comme un abrasif et raye la surface polie. Laver à l'eau tiède savonneuse avec une brosse douce, ou à l'alcool pour un dégraissage rapide, puis sécher avec un chiffon non pelucheux."
            ),
            LapidaireConservationTip(
                titre = "Rangement individuel",
                conseil = "Ranger chaque pierre séparément, dans un pli de papier ou une pochette individuelle : au contact les unes des autres, même bref, les pierres se rayent mutuellement."
            ),
            LapidaireConservationTip(
                titre = "Ne jamais mélanger les duretés",
                conseil = "Ne jamais réunir dans un même compartiment des pierres de dureté différente : la plus dure raye systématiquement la plus tendre, même par un simple frottement pendant le transport."
            ),
            LapidaireConservationTip(
                titre = "Chocs thermiques et produits chimiques",
                conseil = "Éviter les écarts brusques de température et les produits chimiques agressifs (javel, acides), qui peuvent fissurer certaines pierres ou altérer un traitement (émeraude huilée, pierre imprégnée). Le nettoyeur à ultrasons est à proscrire pour les pierres fracturées, huilées ou fragiles."
            ),
            LapidaireConservationTip(
                titre = "Exposition à la lumière",
                conseil = "Certaines pierres sont photosensibles : l'améthyste ou la kunzite exposées longtemps en pleine lumière peuvent pâlir. Conserver les pierres sensibles à l'abri d'une exposition prolongée."
            ),
            LapidaireConservationTip(
                titre = "Transport et manipulation",
                conseil = "Pour le transport, utiliser une pochette rembourrée et éviter tout contact direct entre plusieurs pierres ou bijoux réunis ensemble ; manipuler par la monture ou la table plutôt que par le pavillon, plus vulnérable aux chocs."
            )
        ),
        disclaimerTitle = "Un métier qui s'apprend en atelier",
        disclaimerBody = "Cette fiche présente des repères généraux, pas un mode d'emploi complet : la taille de facettes s'apprend par la pratique encadrée, avec du matériel adapté et des consignes de sécurité (protection oculaire et respiratoire, refroidissement continu du plateau) propres à chaque atelier et à chaque machine. Les diagrammes affichés proviennent de sources réelles et libres de droits (voir crédits) ; en leur absence temporaire, seule la légende reste affichée.",
        machinesTypesTitle = "Les machines du métier",
        machinesTypesIntro = "Au-delà des composants d'une machine à facettes détaillés plus haut, voici un panorama des différents types de machines réellement utilisés en atelier, pour le facettage comme pour le cabochonnage, avec leurs caractéristiques et la technique associée. Les marques citées le sont à titre d'exemple représentatif de leur catégorie, sans lien commercial avec Gems of Rod.",
        categorieFacettageLabel = "Facettage",
        categorieCabochonLabel = "Cabochonnage",
        categoriePolyvalentLabel = "Polyvalent",
        caracteristiquesLabel = "Caractéristiques",
        techniqueLabel = "Technique",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Bras manuel à jauge (jam-peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Le plus simple et le plus ancien des outils de facettage : la pierre est collée au bout d'une tige tenue à la main contre la meule, l'angle étant réglé à l'œil ou à l'aide d'une jauge simple. Toujours enseigné dans les écoles traditionnelles de taille (Sri Lanka, Thaïlande).",
                caracteristiques = listOf(
                    "Aucune pièce mécanique d'indexation",
                    "Coût quasi nul",
                    "Dépend entièrement du savoir-faire du tailleur",
                    "Cadence de travail rapide entre des mains expérimentées"
                ),
                technique = "La main du tailleur ajuste en continu l'angle et la pression contre le disque ; la précision des facettes tient à la répétition du geste, pas à la machine."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Facetteuse à tête d'index mécanique amovible",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Standard des ateliers occidentaux depuis les années 1970 (Facetron, Ultra Tec, Poly-Metric...) : une tête d'index interchangeable règle l'angle et la rotation de la pierre au dixième de degré, le bras glissant verticalement pour ajuster la profondeur de coupe.",
                caracteristiques = listOf(
                    "Précision d'angle au dixième de degré",
                    "Têtes d'index interchangeables (facettes, écailles, cavetto)",
                    "Butée de profondeur micrométrique",
                    "Investissement significatif (machine + meules)"
                ),
                technique = "Le tailleur règle l'angle et le cran d'index avant chaque facette, puis abaisse la pierre contre la meule jusqu'à la butée réglée : la reproductibilité remplace le geste libre du bras manuel."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Facetteuse à tête d'index fixe intégrée",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Version plus abordable où la tête d'index est solidaire du bras plutôt qu'interchangeable, avec un nombre de crans gravés fixé à la fabrication (souvent 64, 96 ou 120 selon le modèle).",
                caracteristiques = listOf(
                    "Prix d'entrée nettement inférieur aux têtes amovibles",
                    "Nombre de crans d'index limité et non modifiable",
                    "Bonne robustesse pour un usage régulier",
                    "Adaptée à l'apprentissage et aux tailles courantes"
                ),
                technique = "Même principe que la tête amovible, mais le choix des motifs de facettage se limite aux divisions d'index gravées sur la machine — suffisant pour la plupart des tailles classiques (rond, ovale, coussin)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Facetteuse assistée par ordinateur (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Machine pilotée numériquement qui reproduit un plan de taille (diagramme d'angles et d'index) de façon identique sur une série de pierres, utilisée en production industrielle ou pour des tailles fantaisie très complexes.",
                caracteristiques = listOf(
                    "Reproductibilité parfaite d'une pierre à l'autre",
                    "Programmation à partir d'un plan de taille numérique",
                    "Investissement élevé, réservé à la production en volume",
                    "Réduit la part de savoir-faire manuel dans le résultat final"
                ),
                technique = "Le plan de taille est chargé dans le logiciel de la machine, qui enchaîne automatiquement les angles et les index programmés ; le rôle du lapidaire se déplace vers le réglage et le contrôle qualité."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Cabocheuse à meules multiples en ligne",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Équipement de référence pour le cabochon (type Genie, CabKing) : 6 à 8 meules diamantées de grain dégressif puis des disques feutrés de polissage, alignées sur un même bâti avec arrosage continu.",
                caracteristiques = listOf(
                    "6 à 8 postes de grain dégressif sur un même arbre",
                    "Arrosage en continu de chaque meule",
                    "Passage rapide d'un poste à l'autre sans changer de disque",
                    "Prix élevé mais très bonne longévité"
                ),
                technique = "Le cabochon est dégrossi puis affiné en passant d'une meule à l'autre par grain décroissant, jusqu'aux feutres de polissage finaux — chaque poste efface les micro-rayures laissées par le précédent."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Cabocheuse Vevor (multi-meules, entrée de gamme)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Déclinaison économique de la cabocheuse à meules multiples, produite en Chine sous la marque Vevor : très répandue chez les débutants et petits ateliers grâce à un prix nettement inférieur aux marques spécialisées (Genie, CabKing).",
                caracteristiques = listOf(
                    "6 à 8 meules diamantées + feutres sur un même arbre, comme les modèles professionnels",
                    "Moteur et roulements d'entrée de gamme, tolérances mécaniques plus larges",
                    "Bac de récupération d'eau intégré",
                    "Prix nettement inférieur aux marques spécialisées, au prix d'une durée de vie plus courte"
                ),
                technique = "Même principe de progression meule par meule qu'une cabocheuse professionnelle, mais avec davantage de vibrations : une pression de travail plus légère et un entretien plus fréquent des roulements compensent la mécanique moins précise."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Meuleuse-polisseuse combinée à arbre horizontal",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Version plus artisanale : un ou deux arbres horizontaux sur lesquels on monte soi-même les disques (meules, feutres) selon le besoin, plutôt qu'une ligne de postes fixes.",
                caracteristiques = listOf(
                    "Disques interchangeables au choix du lapidaire",
                    "Également utilisée pour dégrossir un brut avant sciage",
                    "Moins chère qu'une cabocheuse multi-meules dédiée",
                    "Demande davantage de manipulations entre les étapes"
                ),
                technique = "Le lapidaire change lui-même le disque monté sur l'arbre à chaque étape de grain, contrairement à la cabocheuse multi-meules où les postes sont fixes et juxtaposés."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Scie-tranche (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Petite scie circulaire à lame diamantée, utilisée en amont du cabochon pour débiter le brut en tranches (slabs) de l'épaisseur voulue avant la mise en forme sur les meules.",
                caracteristiques = listOf(
                    "Lame diamantée refroidie par bain d'huile ou d'eau",
                    "Diamètre courant de 10 à 25 cm selon le modèle",
                    "Guide de coupe réglable pour des tranches régulières",
                    "Étape préalable indispensable, pas de taille de facettes"
                ),
                technique = "Le brut est avancé manuellement contre la lame en rotation lente ; l'épaisseur de la tranche obtenue détermine directement l'épaisseur maximale du futur cabochon."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Perceuse de gemmes",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Perceuse à colonne équipée de mèches diamantées creuses et d'un système d'arrosage, utilisée pour percer les perles et les cabochons destinés à être montés en pendentif ou enfilés.",
                caracteristiques = listOf(
                    "Mèches diamantées creuses de différents diamètres",
                    "Refroidissement à l'eau obligatoire pour éviter la fissuration",
                    "Vitesse de rotation réglable selon la dureté de la pierre",
                    "Perçage en deux temps (des deux faces) sur les pierres fragiles"
                ),
                technique = "Le perçage se fait à vitesse lente et sous arrosage constant, souvent en attaquant la pierre depuis les deux faces pour éviter l'éclat de sortie caractéristique d'un perçage traversant en un seul passage."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Touret combiné facettage/cabochon d'entrée de gamme",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Petite machine bon marché associant meules et feutres sur un même arbre pour s'initier aussi bien au cabochon qu'à un facettage sommaire, sans tête d'index précise.",
                caracteristiques = listOf(
                    "Prix d'accès très bas, format compact",
                    "Combine plusieurs usages sur une seule machine",
                    "Absence de tête d'index précise pour un vrai facettage",
                    "Adaptée à la découverte, limitée pour un résultat professionnel"
                ),
                technique = "Le débutant expérimente les deux disciplines sur le même bâti, au prix d'une précision d'angle et d'index bien inférieure à une machine spécialisée."
            )
        )
    )

    private val en = LapidairePage(
        intro = "The lapidary shapes and polishes rough stones into cut gemstones. The trade covers several specialities — faceting for transparent stones, cabochon cutting for opaque or translucent stones, engraving and ornamental carving — but faceting, the most technical of them, is what gives a stone its play of light. This sheet presents the basic equipment and reference points of the trade; it is written for professionals and informed enthusiasts, not as a first, unsupervised lesson.",
        machinesTitle = "The faceting machine",
        machinesIntro = "A faceting machine holds the stone at a precise angle and index against a rotating abrasive disc (the lap). Its mechanical precision — to a tenth of a degree for the angle, to the exact point for the index — is what sets professional cutting apart from simple freehand grinding. Equipment ranges from a simple stick-and-cheater rig (hand-held, fast but imprecise) to machines with a mechanical index head (detachable or fixed, precise but slower) up to computer-controlled models for series cutting.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mast",
                description = "Vertical column fixed to the machine's frame, along which the quill head slides and pivots. Its height and adjustable tilt, together with the arm, determine the faceting angle applied against the lap."
            ),
            LapidaireComponent(
                nom = "Quill",
                description = "Tube through which the rod holding the stone (the dop) slides. The quill rotates to bring each planned facet in turn against the disc, and slides forward to advance the stone as material is removed."
            ),
            LapidaireComponent(
                nom = "Index gear",
                description = "Toothed wheel fixed at the end of the quill, typically available from manufacturers in several counts — 64, 72, 80, 96 or 120 notches — to cover every style of cut; a lower-count \"quick index\" disc is often layered on top of the standard one to speed up positioning when cutting a series of stones. Locking the stone's rotation at a precise position for each facet is essential for the symmetry of a cut like the round brilliant, which arranges its facets in 8-fold symmetry. The index chosen must be an exact multiple of the number of sides to be cut — a pentagon, for instance, cannot be cut on a 96 index (96 ÷ 5 is not a whole number) but cuts cleanly on an 80 index (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Fine-adjustment ring layered on top of the angle shown on the arm. Its main purpose is not to change the cutting angle itself, but to restore parallelism between the facet being cut and the lap once the lap has warped slightly or worn unevenly — a setting to revisit whenever a facet stops polishing uniformly across its whole surface."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "A rod (wood, aluminium, or brass — the latter two hold preheating warmth better) onto which the stone is fixed before being inserted into the quill. Fixing is done with heated wax (the most common method, melting around 80°C), cyanoacrylate glue (\"superglue\", fast-setting but leaves residue that is hard to remove), or epoxy (strong hold, but its exothermic cure needs watching on heat-sensitive stones). Sophisticated dops carry a pin, stud, or groove that always orients them the same way in the index head — essential for transferring the stone from one dop to another (cutting the crown after the pavilion) without misalignment or unwanted rotation."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Rotating metal disc, typically 6\" (≈152 mm) or 8\" (≈203 mm) in diameter, onto which the abrasive is fixed or charged. Cutting laps (surface-electroplated diamond, or cast metal impregnated with diamond particles by rolling) are distinct from polishing laps, made of softer material. A machine typically carries several interchangeable laps, one per grit stage."
            ),
            LapidaireComponent(
                nom = "Goniometer",
                description = "A checking instrument that measures the actual angle of an already-cut stone, on two opposite main facets, to verify the cut obtained. The measured angle (C' for the crown, P' for the pavilion) converts via the formula angle = (180° − measured angle) / 2, since the protractor reads the supplementary angle formed with the girdle plane."
            )
        ),
        disquesTitle = "Shaping and polishing discs",
        disquesIntro = "Cutting proceeds through progressively finer grit stages, each erasing the micro-scratches left by the previous one; skipping a stage leaves marks that final polishing can no longer remove. Lap rotation speed follows the same progression: fairly slow for rough grinding (100 to 300 rpm), faster for facet shaping (300 to 600 rpm), and fastest for polishing (700 to 1000 rpm or more) — always tempered by how sensitive the stone is, slowed without hesitation for a soft or cleavable species.",
        disques = listOf(
            LapidaireDisc(
                nom = "Coarse grinding",
                grain = "260 to 325",
                usage = "Quickly removes material to give the stone its general shape (preform) from the rough or a sawn slab. Aggressive diamond lap, firm pressure, constant water cooling required."
            ),
            LapidaireDisc(
                nom = "Facet shaping",
                grain = "600 to 1200",
                usage = "Cuts and refines each facet at the exact angle and index called for by the cutting diagram. The longest and most decisive stage for symmetry and for the precision of the meet points between neighbouring facets."
            ),
            LapidaireDisc(
                nom = "Pre-polish",
                grain = "3000 to 8000",
                usage = "Refines the surface left by shaping to an almost matte finish with no deep scratches, keeping exactly the same angles — any angle correction at this stage must be minimal, or the work has to drop back to a coarser grit."
            ),
            LapidaireDisc(
                nom = "Final polish",
                grain = "Sub-micron paste or powder (often cerium oxide or very fine diamond)",
                usage = "The polishing lap is chosen by hardness (Mohs scale), from hardest to softest: ceramic (9), copper (3), zinc (2.5), phenolic resin (2.2), acrylic or lead-tin (2), pure tin (1.7), lead (1.5), PVC (1), and wax loaded with various fillers (0.3, variable by formula). It is never the lap itself that polishes, only the powder applied to it, which acts as the abrasive agent — the lap is only a support whose hardness should match the stone's. Dry polishing (powder held by a greasy binder) or wet polishing (powder diluted in water, applied continuously), depending on the method used."
            )
        ),
        indexTitle = "Choosing an index",
        indexIntro = "An index is chosen above all for the number of sides to be cut: it must be an exact multiple of that number (a pentagon is impossible on a 96 index, but works on an 80 index). The rotation per notch is inversely proportional to the number of notches on the index (360° divided by the notch count) — a 120 index advances 3° per notch, a 32 index 11.25°. Converting a cutting diagram from one index to another is only possible if the two indexes are multiples or sub-multiples of one another: simply divide or multiply each index number by their ratio (dividing by 3, for example, to go from a 96 index to a 32 index). Since all common indexes are multiples of 4, a square can be cut on any of them; conversely, it is impossible to index to a position between two notches, which rules out most odd-sided polygons outright. The 96 index remains the most widespread among manufacturers, offering a good balance between fine graduation and quick indexing; the 120 index is the most complete, at the cost of slower indexing.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11.25° / notch", cotesTaillables = "Square (4), octagon (8), hexadecagon (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / notch", cotesTaillables = "Triangle (3), square (4), pentagon (5), hexagon (6), decagon (10), dodecagon (12), pentadecagon (15), icosagon (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5.625° / notch", cotesTaillables = "Square (4), octagon (8), hexadecagon (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / notch", cotesTaillables = "Triangle (3), square (4), hexagon (6), octagon (8), nonagon (9), dodecagon (12), octadecagon (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4.5° / notch", cotesTaillables = "Square (4), pentagon (5), octagon (8), decagon (10), hexadecagon (16), icosagon (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3.75° / notch", cotesTaillables = "The most common — 9 regular polygons cuttable in total, including square, hexagon, octagon, dodecagon"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / notch", cotesTaillables = "The most complete — 14 regular polygons cuttable in total, the maximum among common indexes"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2.8125° / notch", cotesTaillables = "Square (4), octagon (8), hexadecagon (16), 32 sides, 64 sides")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Estimated weight",
            intro = "For a stone already cut and set, impossible to weigh directly: weight is deduced from volume, from caliper measurements (accurate to 1/100 mm) and the species' specific gravity. An indicative formula, accurate to within about 10-15% — not a real weighing.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Round",
                LapidaireCutShape.OVALE to "Oval",
                LapidaireCutShape.COUSSIN_CARRE to "Square cushion",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Rectangular cushion",
                LapidaireCutShape.CARRE_A_GRADIN to "Square step-cut",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rectangular step-cut",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Square cushion (step-cut)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Rectangular cushion (step-cut)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Pear",
                LapidaireCutShape.TRIANGLE_BOMBE to "Bombé triangle",
                LapidaireCutShape.TRIANGLE to "Triangle",
                LapidaireCutShape.TRAPEZE to "Trapezoid",
                LapidaireCutShape.COEUR to "Heart"
            ),
            dimension1Label = "Length or diameter (mm)",
            dimension2Label = "Width (mm)",
            heightLabel = "Total depth, table to culet (mm)",
            sgLabel = "Species' specific gravity",
            computeLabel = "Calculate",
            resultLabel = "Estimated weight: %s carats",
            disclaimer = "Volume-based estimate, indicative accuracy 10-15% — not a real weighing.",
            errorMessage = "Enter valid values (positive numbers) for all dimensions and the specific gravity."
        ),
        anglesTitle = "Reference angles: the round brilliant",
        anglesIntro = "The proportions below are those published by the Belgian mathematician Marcel Tolkowsky in 1919, who calculated the optimal angle to maximise light return (brilliance) and dispersion (fire) in a round-brilliant-cut diamond — the historical benchmark still used as a starting point today, though modern laboratories (GIA in particular) accept a tolerance range around these values rather than a single figure. For coloured stones, whose refractive index differs from diamond's, the classic crown-to-girdle ratio instead sits between 25/75% and 30/70%. A firm rule regardless of species: never cut a pavilion facet at an angle below the species' critical angle (light then escapes through the pavilion, a \"window\" effect), nor above the extinction angle (dark zones). To check an already-cut stone, a goniometer measures the angle on two opposite main facets: the true crown or pavilion angle is derived from the reading via the formula (180° − measured angle) / 2. Other cuts (princess, oval, pear, emerald, cushion...) each follow their own diagram, which varies widely by diagram designer and by goal (material yield versus optical performance): there is no comparable universal value to cite without referring to a specific diagram.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Round brilliant (Tolkowsky, 1919)",
                couronne = "34.5°",
                pavillon = "40.75°",
                table = "53% of girdle diameter",
                facettes = "57 or 58 (33 on the crown, 24 or 25 on the pavilion, 8-fold symmetry)",
                source = "Marcel Tolkowsky, \"Diamond Design\", 1919"
            )
        ),
        optiqueTitle = "Critical angle and extinction angle",
        optiqueIntro = "There are broadly three cases for the path of light entering through the table: a pavilion cut too shallow lets it escape straight out the bottom; a pavilion cut too deep traps it in internal reflections that exit through a side facet without returning to the eye, leaving the stone dull despite a perfect polish; a well-proportioned pavilion, by contrast, sends it back toward the viewer through the crown for maximum brilliance. A cut stone's pavilion acts as a mirror: below the species' critical angle, light entering through the table escapes through the pavilion (the \"window\" effect already covered above); cut exactly at the critical angle, a different defect appears, the \"fish eye\" — the table looks dull while the crown's outline stays bright, because the rays crossing it graze the wall of the reflection cone without actually reflecting off it. There is also an upper limit, the extinction angle, beyond which the pavilion again loses light through the opposite facet: extinction angle = 60° − (critical angle / 3). A well-cut pavilion therefore keeps critical angle < pavilion angle < extinction angle; the higher the gem's refractive index, the wider this working range becomes — a diamond forgives far more deviation than a fluorite does. The crown follows a complementary rule: its maximum angle is inversely proportional to the pavilion's (a shorter pavilion allows a taller crown) and directly proportional to the refractive index; working a few tenths of a degree below these maximums is the safest choice. All of these cut diagrams rest on the Meet Point Technique (an American method, Long & Steele), which uses the intersections of three or more facets as reference points to automatically secure good proportions and good weight retention from the rough; to estimate the weight of an already-cut stone without unsetting it, the usual formula is weight (in carats) = width³ × the cut's volume coefficient × the species' specific gravity / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamond", angleCritique = "24.4°", angleExtinction = "51.85°"),
            LapidaireOptiqueEntry(pierre = "Sphene (titanite)", angleCritique = "31.76°", angleExtinction = "49.41°"),
            LapidaireOptiqueEntry(pierre = "Zircon (high)", angleCritique = "31.3°", angleExtinction = "49.57°"),
            LapidaireOptiqueEntry(pierre = "Demantoid garnet", angleCritique = "32.62°", angleExtinction = "49.13°"),
            LapidaireOptiqueEntry(pierre = "Alexandrite (chrysoberyl)", angleCritique = "34.94°", angleExtinction = "48.35°"),
            LapidaireOptiqueEntry(pierre = "Ruby and sapphire (corundum)", angleCritique = "34.58°", angleExtinction = "48.47°"),
            LapidaireOptiqueEntry(pierre = "Spinel", angleCritique = "35.74°", angleExtinction = "48.09°"),
            LapidaireOptiqueEntry(pierre = "Peridot", angleCritique = "37.2°", angleExtinction = "47.6°"),
            LapidaireOptiqueEntry(pierre = "Tourmaline", angleCritique = "38.01°", angleExtinction = "47.33°"),
            LapidaireOptiqueEntry(pierre = "Topaz", angleCritique = "38.15°", angleExtinction = "47.28°"),
            LapidaireOptiqueEntry(pierre = "Beryl", angleCritique = "39.35°", angleExtinction = "46.88°"),
            LapidaireOptiqueEntry(pierre = "Emerald", angleCritique = "39.72°", angleExtinction = "46.76°"),
            LapidaireOptiqueEntry(pierre = "Aquamarine", angleCritique = "39.75°", angleExtinction = "46.75°"),
            LapidaireOptiqueEntry(pierre = "Amethyst and quartz", angleCritique = "40.37°", angleExtinction = "46.54°"),
            LapidaireOptiqueEntry(pierre = "Calcite", angleCritique = "42.29°", angleExtinction = "45.9°"),
            LapidaireOptiqueEntry(pierre = "Fluorite", angleCritique = "44.21°", angleExtinction = "45.26°")
        ),
        defautsTitle = "Common defects and corrections",
        defautsIntro = "Most faceting defects come from an inaccurate angle setting, a wrong index reading, or a badly aligned dop transfer rather than a problem with the lap itself. The basic rule for correcting a bad meet or an uneven polish: always act on the side opposite the defect.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Facet off-centre or poorly polished on one side",
                cause = "Slight misalignment of the stone on the cheater.",
                remede = "Turn the cheater to tilt the stone toward the side opposite the defect."
            ),
            LapidaireDefaut(
                probleme = "Facet meets or polishes badly at the top, angle too large",
                cause = "The angle shown on the protractor is higher than it should be.",
                remede = "Slightly reduce the angle on the protractor to tilt the stone downward."
            ),
            LapidaireDefaut(
                probleme = "Facet meets or polishes badly at the bottom, angle too small",
                cause = "The angle shown on the protractor is lower than it should be.",
                remede = "Slightly increase the angle on the protractor to tilt the stone upward."
            ),
            LapidaireDefaut(
                probleme = "Combined diagonal defect (for example, top-left)",
                cause = "The stone may have rotated slightly on the dop during transfer.",
                remede = "Combine a correction on the cheater with one on the angle, slowing the lap's rotation speed if needed while you regain control."
            ),
            LapidaireDefaut(
                probleme = "The stone squeals on the polishing lap",
                cause = "The facet being worked is not parallel to the lap.",
                remede = "Stop and correct the stone's position before continuing — a reliable warning sign, never to be ignored."
            ),
            LapidaireDefaut(
                probleme = "Parallel scratches running across several facets",
                cause = "The lap is contaminated with grit of a different grain size.",
                remede = "Decontaminate the lap (scrub with hot soapy water, then scrape or true up the surface if needed) before resuming."
            ),
            LapidaireDefaut(
                probleme = "\"Burn\" marks on a facet's surface",
                cause = "Polishing held too long in one spot with too much pressure, causing local overheating.",
                remede = "Sweep the facet regularly across the lap, reduce pressure, and add a drop of water per second if needed."
            ),
            LapidaireDefaut(
                probleme = "A small cavity appears partway through faceting",
                cause = "An inclusion close to the surface has been exposed.",
                remede = "Cleaning up inclusions more thoroughly at the rough-grinding stage is the best prevention; once the hole appears, recut that area locally or consider a full recut if needed."
            )
        ),
        especesTitle = "Practical species sheets",
        especesIntro = "Beyond the critical angle and extinction angle given above, each species has its own cutting habits: table orientation based on pleochroism or cleavage, particular sensitivities, and the lap and polishing powder that give the best results. Practice-based pointers, not a universal rule — each stone keeps its own individual quirks (inclusions, zoning, rough surface condition).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamond",
                orientation = "Isotropic: no orientation constraint tied to an optical axis.",
                fragilite = "Perfect cleavage along 4 octahedral planes, well understood and exploited by cutters rather than merely endured; the pavilion tip remains the most impact-vulnerable point.",
                polissage = "Cast-iron lap (scaife) charged with diamond powder — only diamond polishes diamond."
            ),
            LapidaireEspeceFiche(
                pierre = "Corundum (Ruby, Sapphire)",
                orientation = "No cleavage to account for in orientation.",
                fragilite = "Hardness 9, among the hardest faceted stones: few special precautions needed.",
                polissage = "Excellent results on a copper lap with diamond powder."
            ),
            LapidaireEspeceFiche(
                pierre = "Beryl (Emerald, Aquamarine, Morganite, Heliodor)",
                orientation = "Imperfect cleavage, no real orientation constraint.",
                fragilite = "Emerald in particular needs attention to jardin (inclusions), which can cause it to fracture during both cutting and polishing.",
                polissage = "Acetic acid in the coolant water improves polishing; aluminum, cerium, or tin oxide powder, or Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Quartz (Amethyst, Citrine, Rock crystal, Smoky quartz)",
                orientation = "No cleavage; account for color zoning when orienting.",
                fragilite = "No particular cleavage fragility.",
                polissage = "Results sometimes hold unexpected surprises, with no identified cause."
            ),
            LapidaireEspeceFiche(
                pierre = "Topaz",
                orientation = "Offset the table by about 10° from the basal cleavage plane, to avoid exposing it directly along its axis.",
                fragilite = "Clean cleavage in a single direction (basal plane) — easy to facet once avoided.",
                polissage = "No lap recommendation beyond the general rules."
            ),
            LapidaireEspeceFiche(
                pierre = "Tourmaline",
                orientation = "Preferably cut the table parallel to the optic axis; a parti-colored stone splits cleanly at the junction between two colors — better to glue it there than to saw it.",
                fragilite = "Sensitive to heat and impact during cutting.",
                polissage = "No lap recommendation beyond the general rules."
            ),
            LapidaireEspeceFiche(
                pierre = "Garnet (Almandine, Demantoid, Grossular, Pyrope, Spessartine, Uvarovite)",
                orientation = "Isotropic: table orientation is unconstrained, no preferred direction.",
                fragilite = "No fragility issue tied to cleavage.",
                polissage = "Acetic acid (vinegar) in the coolant water consistently improves the result; cerium, tin, or aluminum oxide powder."
            ),
            LapidaireEspeceFiche(
                pierre = "Spinel",
                orientation = "Isotropic, no orientation constraint.",
                fragilite = "No fragility issue tied to cleavage.",
                polissage = "Polishes remarkably well on a copper lap with diamond paste."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridot (Olivine)",
                orientation = "Very weak pleochroism, orientation is not very constraining.",
                fragilite = "No notable cleavage fragility.",
                polissage = "Results sometimes hold unexpected surprises; diamond powder works well."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorite",
                orientation = "Splits easily along 4 directions: choose the orientation carefully to limit the risk.",
                fragilite = "One of the most closed critical angles among faceted stones, very sensitive to impact and chipping.",
                polissage = "Preferably a wax lap; acid improves the result."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcite",
                orientation = "Orient carefully so as not to expose the cleavage planes directly.",
                fragilite = "Very low hardness (3) and perfect cleavage in 3 directions, highly heat-sensitive — a stone difficult to cut and polish; run the lap slowly (about 100 rpm).",
                polissage = "Wood or wax lap, tin or chromium oxide powder, with a few drops of oxalic acid."
            ),
            LapidaireEspeceFiche(
                pierre = "Chrysoberyl (Alexandrite)",
                orientation = "No orientation constraint tied to cleavage.",
                fragilite = "High hardness (8.5), a tough stone with no particular concern.",
                polissage = "Polishes quickly with acid and Linde A powder; excellent results on a copper lap with diamond powder."
            )
        ),
        diagrammesTitle = "Diagrams",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Round brilliant proportions diagram: crown, girdle, and pavilion nomenclature."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagram of the light path depending on pavilion depth: too shallow, well-proportioned, or too deep."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Antique engraving of a diamond cutting mill — the mechanics of the rotating lap, essentially unchanged in principle for centuries."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "A modern faceting machine: mast, quill, and lap, as described above."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Stages in transforming a rough stone into a faceted gem."
            )
        ),
        tipsTitle = "Practical tips",
        tips = listOf(
            LapidaireTip(texte = "Always check the calibration of the angle shown on the arm with a test stone before starting a valuable piece: a 0.5° offset on the pavilion is enough to visibly darken an otherwise well-cut stone."),
            LapidaireTip(texte = "Centre the stone carefully on the dop: even a slight misalignment shows up as an uneven girdle thickness and facets that fail to meet at the same point all the way around."),
            LapidaireTip(texte = "Keep girdle thickness moderate — neither a fragile \"knife edge\" prone to chipping, nor excessively thick, which traps material with no optical benefit — a regular, visible girdle line, neither too thin nor too thick, is one of the marks of careful work."),
            LapidaireTip(texte = "Check meet points between facets with a loupe under raking light at every stage: a missed meet point is easy to correct during shaping, almost impossible once pre-polishing has begun."),
            LapidaireTip(texte = "Match lubrication to the lap material: water suits most electroplated diamond laps, while some polishing laps (tin in particular) give better results with oil-based lubrication or a specific mix — follow the lap manufacturer's recommendations."),
            LapidaireTip(texte = "Check the species' heat sensitivity and cleavage before cutting: some stones (topaz, kunzite) have a distinct cleavage that can propagate under the heat or pressure of overly aggressive grinding."),
            LapidaireTip(texte = "Test final symmetry by viewing the stone face-up under a point light source: the crown facets' reflections should form a regular pattern; an asymmetry visible to the naked eye at this stage can no longer be fixed by polishing."),
            LapidaireTip(texte = "Never rest the stone on a lap at a standstill and then start the motor: always get the lap spinning first before presenting the stone, with a light pendulum motion to keep it from sitting still in one spot, which wears the disc unevenly and the stone irregularly."),
            LapidaireTip(texte = "A table that looks \"dead\" while the stone's outline stays bright signals a window effect: the pavilion is too shallow and lets light escape underneath instead of returning it through the table — fix it by deepening the pavilion angle, never past the extinction angle."),
            LapidaireTip(texte = "On elongated shapes (oval, marquise, pear, cushion...), a dark cross visible at the centre (the \"X+\" effect) points to pavilion facets whose length and width sides don't meet correctly; a cutting diagram designed for that specific shape prevents it far better than a case-by-case adjustment.")
        ),
        conservationTitle = "Storing and handling cut stones",
        conservationIntro = "A well-cut stone stays vulnerable once mounted or stored: a few simple precautions prevent most accidental scratches and damage.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Cleaning",
                conseil = "Never wipe a stone dry: dust acts as an abrasive and scratches the polished surface. Wash with lukewarm soapy water and a soft brush, or with alcohol for a quick degreasing, then dry with a lint-free cloth."
            ),
            LapidaireConservationTip(
                titre = "Individual storage",
                conseil = "Store each stone separately, in a folded paper wrapper or an individual pouch: even brief contact between stones scratches them."
            ),
            LapidaireConservationTip(
                titre = "Never mix hardnesses",
                conseil = "Never keep stones of different hardness together in the same compartment: the harder one will systematically scratch the softer one, even from simple friction during transport."
            ),
            LapidaireConservationTip(
                titre = "Thermal shock and chemicals",
                conseil = "Avoid sudden temperature changes and harsh chemicals (bleach, acids), which can crack certain stones or damage a treatment (oiled emerald, impregnated stone). Ultrasonic cleaners should be avoided for fractured, oiled, or fragile stones."
            ),
            LapidaireConservationTip(
                titre = "Light exposure",
                conseil = "Some stones are photosensitive: amethyst or kunzite exposed to strong light for a long time can fade. Keep sensitive stones away from prolonged exposure."
            ),
            LapidaireConservationTip(
                titre = "Transport and handling",
                conseil = "For transport, use a padded pouch and avoid direct contact between several stones or pieces of jewelry kept together; handle by the setting or the table rather than the pavilion, which is more vulnerable to impact."
            )
        ),
        disclaimerTitle = "A trade learned in the workshop",
        disclaimerBody = "This sheet presents general reference points, not a complete manual: faceting is learned through supervised practice, with suitable equipment and safety guidelines (eye and respiratory protection, continuous lap cooling) specific to each workshop and each machine. The diagrams shown come from real, freely licensed sources (see credits); while temporarily unavailable, only the caption is shown.",
        machinesTypesTitle = "Machines of the trade",
        machinesTypesIntro = "Beyond the components of a faceting machine detailed above, here is an overview of the different types of machines actually used in the workshop, for both faceting and cabbing, with their characteristics and associated technique. Brand names are cited only as representative examples of their category, with no commercial link to Gems of Rod.",
        categorieFacettageLabel = "Faceting",
        categorieCabochonLabel = "Cabbing",
        categoriePolyvalentLabel = "Versatile",
        caracteristiquesLabel = "Characteristics",
        techniqueLabel = "Technique",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Hand-held jam-peg",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "The simplest and oldest faceting tool: the stone is glued to the end of a rod held by hand against the lap, with the angle set by eye or with a simple gauge. Still taught in traditional cutting schools (Sri Lanka, Thailand).",
                caracteristiques = listOf(
                    "No mechanical indexing part",
                    "Virtually no cost",
                    "Entirely dependent on the cutter's skill",
                    "Fast working pace in experienced hands"
                ),
                technique = "The cutter's hand continuously adjusts the angle and pressure against the lap; facet precision comes from repetition of the motion, not from the machine."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Faceting machine with removable mechanical index head",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "The standard in Western workshops since the 1970s (Facetron, Ultra Tec, Poly-Metric...): an interchangeable index head sets the stone's angle and rotation to a tenth of a degree, with the arm sliding vertically to adjust cutting depth.",
                caracteristiques = listOf(
                    "Angle precision to a tenth of a degree",
                    "Interchangeable index heads (facets, scallops, cavetto)",
                    "Micrometric depth stop",
                    "Significant investment (machine + laps)"
                ),
                technique = "The cutter sets the angle and index notch before each facet, then lowers the stone against the lap to the set stop: reproducibility replaces the free hand movement of the manual arm."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Faceting machine with fixed integrated index head",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "A more affordable version where the index head is fixed to the arm rather than interchangeable, with a number of engraved notches set at manufacture (often 64, 96, or 120 depending on the model).",
                caracteristiques = listOf(
                    "Entry price well below removable heads",
                    "Limited, non-adjustable number of index notches",
                    "Good durability for regular use",
                    "Suited to learning and common cuts"
                ),
                technique = "Same principle as the removable head, but the choice of faceting patterns is limited to the index divisions engraved on the machine — enough for most classic cuts (round, oval, cushion)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Computer-controlled (CNC) faceting machine",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "A digitally controlled machine that reproduces a cutting plan (angle and index diagram) identically across a series of stones, used in industrial production or for very complex fancy cuts.",
                caracteristiques = listOf(
                    "Perfect reproducibility from one stone to the next",
                    "Programmed from a digital cutting plan",
                    "High investment, reserved for volume production",
                    "Reduces the share of manual skill in the final result"
                ),
                technique = "The cutting plan is loaded into the machine's software, which automatically runs through the programmed angles and index positions; the lapidary's role shifts to setup and quality control."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "In-line multi-wheel cabbing machine",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "The reference equipment for cabbing (Genie, CabKing type): 6 to 8 diamond wheels of decreasing grit followed by felt polishing wheels, aligned on a single frame with continuous water flow.",
                caracteristiques = listOf(
                    "6 to 8 decreasing-grit stations on the same shaft",
                    "Continuous water flow on each wheel",
                    "Quick move from one station to the next without changing discs",
                    "High price but very good longevity"
                ),
                technique = "The cabochon is roughed out then refined by moving from one wheel to the next in decreasing grit, down to the final polishing felts — each station erases the micro-scratches left by the previous one."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Vevor cabbing machine (multi-wheel, entry-level)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "A budget version of the multi-wheel cabbing machine, made in China under the Vevor brand: very common among beginners and small workshops thanks to a price well below specialized brands (Genie, CabKing).",
                caracteristiques = listOf(
                    "6 to 8 diamond wheels + felts on the same shaft, as on professional models",
                    "Entry-level motor and bearings, wider mechanical tolerances",
                    "Built-in water recovery tray",
                    "Well below specialized brands in price, at the cost of a shorter lifespan"
                ),
                technique = "Same wheel-by-wheel progression as a professional cabbing machine, but with more vibration: lighter working pressure and more frequent bearing maintenance compensate for the less precise mechanics."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Combined grinder-polisher with horizontal shaft",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "A more artisanal version: one or two horizontal shafts onto which the lapidary mounts discs (wheels, felts) as needed, rather than a line of fixed stations.",
                caracteristiques = listOf(
                    "Interchangeable discs chosen by the lapidary",
                    "Also used to rough out rough material before sawing",
                    "Cheaper than a dedicated multi-wheel cabbing machine",
                    "Requires more handling between steps"
                ),
                technique = "The lapidary changes the disc mounted on the shaft at each grit stage, unlike the multi-wheel cabbing machine where the stations are fixed and side by side."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Trim saw",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "A small circular saw with a diamond blade, used upstream of cabbing to cut rough material into slabs of the desired thickness before shaping on the wheels.",
                caracteristiques = listOf(
                    "Diamond blade cooled by an oil or water bath",
                    "Common diameter of 10 to 25 cm depending on the model",
                    "Adjustable cutting guide for even slabs",
                    "An essential preliminary step, not a faceting operation"
                ),
                technique = "The rough material is fed manually against the slowly rotating blade; the resulting slab thickness directly determines the maximum thickness of the future cabochon."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Gem drilling machine",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "A drill press fitted with hollow diamond bits and a water-flow system, used to drill beads and cabochons intended to be mounted as pendants or strung.",
                caracteristiques = listOf(
                    "Hollow diamond bits of various diameters",
                    "Water cooling required to prevent cracking",
                    "Adjustable rotation speed depending on the stone's hardness",
                    "Two-sided drilling on fragile stones"
                ),
                technique = "Drilling is done at low speed under constant water flow, often approaching the stone from both sides to avoid the exit chipping typical of a single-pass through-drill."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Entry-level combined faceting/cabbing bench grinder",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "A cheap, small machine combining wheels and felts on a single shaft to try both cabbing and basic faceting, without a precise index head.",
                caracteristiques = listOf(
                    "Very low entry price, compact footprint",
                    "Combines several uses in a single machine",
                    "No precise index head for true faceting",
                    "Suited to discovery, limited for a professional result"
                ),
                technique = "The beginner experiments with both disciplines on the same frame, at the cost of angle and index precision far below a specialized machine."
            )
        )
    )

    private val es = LapidairePage(
        intro = "El lapidario da forma y pule las piedras en bruto hasta convertirlas en piedras talladas. Este oficio abarca varias especialidades — la talla de facetas (faceting) para piedras transparentes, el cabujón para piedras opacas o translúcidas, el grabado y el trabajo ornamental —, pero la talla de facetas, la más técnica, es la que da a una piedra su juego de luz. Esta ficha presenta el equipo y las referencias básicas del oficio; está dirigida a profesionales y aficionados avanzados, no a un primer aprendizaje sin supervisión.",
        machinesTitle = "La máquina de tallar facetas",
        machinesIntro = "Una máquina de tallar facetas (faceting machine) mantiene la piedra en un ángulo y un índice precisos contra un disco abrasivo giratorio (el plato o lap). Su precisión mecánica —una décima de grado en el ángulo, un punto exacto en el índice— es lo que distingue una talla profesional de un simple desbaste a mano alzada. El equipo va desde el simple bastón con «cheater» (artesanal, rápido pero poco preciso) hasta las máquinas con cabezal divisor mecánico (desmontable o fijo, precisas pero de producción más lenta) y los modelos controlados por ordenador para la talla en serie.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mástil (mast)",
                description = "Columna vertical fijada al bastidor de la máquina, sobre la que se desliza y gira el cabezal portacaña. Su altura y su inclinación regulable determinan, junto con el brazo, el ángulo de talla aplicado sobre el plato."
            ),
            LapidaireComponent(
                nom = "Caña (quill)",
                description = "Tubo por el que se desliza la varilla que sostiene la piedra (el dop). La caña gira sobre sí misma para presentar sucesivamente cada faceta prevista frente al disco, y se desliza para avanzar la piedra a medida que se elimina material."
            ),
            LapidaireComponent(
                nom = "Rueda de índice (index gear)",
                description = "Rueda dentada fijada en el extremo de la caña, disponible habitualmente en varias graduaciones —64, 72, 80, 96 o 120 muescas— para cubrir todos los estilos de talla; un disco de «índice rápido», con menos muescas, suele superponerse al índice normal para agilizar el posicionado al tallar una serie de piedras. Bloquear la rotación de la piedra en una posición precisa para cada faceta es condición indispensable para la simetría de una talla como el brillante redondo, que reparte sus facetas con simetría de orden 8. El índice elegido debe ser múltiplo exacto del número de lados a tallar: un pentágono, por ejemplo, es imposible con un índice 96 (96 ÷ 5 no es un número entero) pero se talla sin problema con un índice 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Anillo de ajuste fino superpuesto al ángulo indicado en el brazo. Su función principal no es modificar el ángulo de talla en sí, sino restablecer el paralelismo entre la faceta en curso y el plato cuando este se ha alabeado ligeramente o desgastado de forma desigual — un ajuste a repetir cada vez que una faceta deja de pulirse de manera uniforme en toda su superficie."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Varilla (madera, aluminio o latón —estos dos últimos retienen mejor el calor del precalentamiento—) sobre la que se fija la piedra antes de insertarla en la caña. La fijación se hace con cera caliente (lo más habitual, funde hacia 80 °C), pegamento cianocrilato («superglue», fija rápido pero deja residuos difíciles de retirar) o epoxi (buen agarre, pero su reacción exotérmica hay que vigilarla en piedras sensibles al calor). Los dops más sofisticados llevan una espiga, un resalte o una ranura que los orienta siempre de la misma forma en el divisor — condición indispensable para transferir la piedra de un dop a otro (tallar la corona después del pabellón) sin desalineación ni giro parásito."
            ),
            LapidaireComponent(
                nom = "Plato (lap)",
                description = "Disco metálico giratorio, generalmente de 6\" (≈152 mm) u 8\" (≈203 mm) de diámetro, sobre el que se fija o se carga el abrasivo. Se distinguen los platos para tallar (diamante electrodepositado en superficie, o fundición impregnada de partículas de diamante por laminado) de los platos para pulir, de material más blando. Una máquina suele disponer de varios platos intercambiables, uno por cada etapa de grano."
            ),
            LapidaireComponent(
                nom = "Goniómetro",
                description = "Instrumento de control que mide el ángulo real de una piedra ya tallada, sobre dos facetas principales diametralmente opuestas, para verificar la talla obtenida. El ángulo medido (C' para la corona, P' para el pabellón) se convierte mediante la fórmula ángulo = (180° − ángulo medido) / 2, ya que el transportador registra el ángulo suplementario formado con el plano del rondel."
            )
        ),
        disquesTitle = "Discos de formado y pulido",
        disquesIntro = "La talla avanza por etapas de grano cada vez más fino, cada una elimina las microrrayas dejadas por la anterior; saltarse una etapa deja marcas que el pulido final ya no puede corregir. La velocidad de rotación del plato acompaña esta progresión: bastante lenta en el desbaste (100 a 300 rpm), más rápida en el formado de facetas (300 a 600 rpm), y la más rápida en el pulido (700 a 1000 rpm o más) —siempre en función de la sensibilidad de la piedra, reducida sin dudar para una piedra blanda o exfoliable.",
        disques = listOf(
            LapidaireDisc(
                nom = "Desbaste",
                grain = "260 a 325",
                usage = "Elimina rápidamente material para dar a la piedra su forma general (preforma) a partir del bruto o de una lámina serrada. Plato diamantado agresivo, presión firme, refrigeración por agua obligatoria."
            ),
            LapidaireDisc(
                nom = "Formado de facetas",
                grain = "600 a 1200",
                usage = "Traza y afina cada faceta con el ángulo y el índice exactos previstos por el diagrama de talla. Es la etapa más larga y la más determinante para la simetría y la precisión de los puntos de encuentro (meet points) entre facetas vecinas."
            ),
            LapidaireDisc(
                nom = "Prepulido",
                grain = "3000 a 8000",
                usage = "Afina el acabado dejado por el formado hasta un aspecto casi mate y sin rayas profundas, conservando exactamente los mismos ángulos — cualquier corrección de ángulo en esta etapa debe ser mínima, o habrá que volver a un grano más grueso."
            ),
            LapidaireDisc(
                nom = "Pulido final",
                grain = "Pasta o polvo submicrónico (a menudo óxido de cerio o diamante muy fino)",
                usage = "El plato de pulido se elige por dureza (escala de Mohs), de más duro a más blando: cerámica (9), cobre (3), zinc (2,5), fenólico (2,2), plexiglás o plomo-estaño (2), estaño puro (1,7), plomo (1,5), PVC (1) y cera con diversas cargas (0,3, variable según la fórmula). Nunca es el plato en sí el que pule, solo el polvo aplicado sobre él actúa como agente abrasivo — el plato es solo un soporte cuya dureza debe corresponder a la de la piedra. Pulido en seco (polvo fijado con un aglutinante graso) o húmedo (polvo diluido en agua, aplicado de forma continua), según el método elegido."
            )
        ),
        indexTitle = "Elección del índice",
        indexIntro = "Un índice se elige ante todo según el número de lados a tallar: debe ser un múltiplo exacto de ese número (un pentágono es imposible con un índice 96, pero se puede tallar con un índice 80). La rotación por muesca es inversamente proporcional al número de muescas del índice (360° dividido entre el número de muescas) — un índice 120 avanza 3° por muesca, un índice 32 avanza 11,25°. Reconvertir un diagrama de talla de un índice a otro solo es posible si ambos índices son múltiplos o submúltiplos entre sí: basta con dividir o multiplicar cada número de índice por su razón (dividir entre 3, por ejemplo, para pasar de un índice 96 a un índice 32). Como todos los índices habituales son múltiplos de 4, un cuadrado se puede tallar con cualquiera de ellos; en cambio, es imposible indexar a una posición intermedia entre dos muescas, lo que descarta de entrada la mayoría de los polígonos de lados impares. El índice 96 sigue siendo el más extendido entre los fabricantes, ya que ofrece un buen equilibrio entre finura de graduación y rapidez de indexado; el índice 120 es el más completo, a costa de un indexado más lento.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / muesca", cotesTaillables = "Cuadrado (4), octógono (8), hexadecágono (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / muesca", cotesTaillables = "Triángulo (3), cuadrado (4), pentágono (5), hexágono (6), decágono (10), dodecágono (12), pentadecágono (15), icoságono (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / muesca", cotesTaillables = "Cuadrado (4), octógono (8), hexadecágono (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / muesca", cotesTaillables = "Triángulo (3), cuadrado (4), hexágono (6), octógono (8), eneágono (9), dodecágono (12), octodecágono (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / muesca", cotesTaillables = "Cuadrado (4), pentágono (5), octógono (8), decágono (10), hexadecágono (16), icoságono (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / muesca", cotesTaillables = "El más habitual — 9 polígonos regulares tallables en total, entre ellos cuadrado, hexágono, octógono, dodecágono"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / muesca", cotesTaillables = "El más completo — 14 polígonos regulares tallables en total, el máximo entre los índices habituales"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / muesca", cotesTaillables = "Cuadrado (4), octógono (8), hexadecágono (16), 32 lados, 64 lados")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Peso estimado",
            intro = "Para una piedra ya tallada y montada, imposible de pesar directamente: el peso se deduce del volumen, a partir de medidas con calibre (precisión de 1/100 mm) y el peso específico de la especie. Fórmula indicativa, precisión del orden del 10-15 % — no es un pesaje real.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Redondo",
                LapidaireCutShape.OVALE to "Ovalado",
                LapidaireCutShape.COUSSIN_CARRE to "Cojín cuadrado",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Cojín rectangular",
                LapidaireCutShape.CARRE_A_GRADIN to "Cuadrado escalonado",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rectángulo escalonado",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Cojín cuadrado (escalonado)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Cojín rectangular (escalonado)",
                LapidaireCutShape.MARQUISE to "Marquesa",
                LapidaireCutShape.POIRE to "Pera",
                LapidaireCutShape.TRIANGLE_BOMBE to "Triángulo abombado",
                LapidaireCutShape.TRIANGLE to "Triángulo",
                LapidaireCutShape.TRAPEZE to "Trapecio",
                LapidaireCutShape.COEUR to "Corazón"
            ),
            dimension1Label = "Longitud o diámetro (mm)",
            dimension2Label = "Anchura (mm)",
            heightLabel = "Profundidad total, tabla a culata (mm)",
            sgLabel = "Peso específico de la especie",
            computeLabel = "Calcular",
            resultLabel = "Peso estimado: %s quilates",
            disclaimer = "Estimación por volumen, precisión indicativa del 10-15 % — no sustituye a un pesaje real.",
            errorMessage = "Introduce valores válidos (números positivos) para todas las dimensiones y el peso específico."
        ),
        anglesTitle = "Ángulos de referencia: el brillante redondo",
        anglesIntro = "Las proporciones siguientes son las publicadas por el matemático belga Marcel Tolkowsky en 1919, quien calculó el ángulo óptimo para maximizar el retorno de luz (brillo) y la dispersión (fuego) de un diamante tallado en brillante redondo — es la referencia histórica que aún hoy se usa como punto de partida, aunque los laboratorios modernos (especialmente el GIA) admiten un margen de tolerancia alrededor de estos valores en lugar de una cifra única. Para las piedras de color, cuyo índice de refracción difiere del diamante, la proporción clásica entre corona y rondel se sitúa más bien entre 25/75 % y 30/70 %. Regla imperativa, sea cual sea la piedra: nunca tallar una faceta de pabellón con un ángulo inferior al ángulo crítico de la especie (la luz se escapa entonces por el pabellón, efecto «ventana»), ni superior al ángulo de extinción (zonas oscuras). Para verificar una piedra ya tallada, un goniómetro mide el ángulo sobre dos facetas principales opuestas: el ángulo real de corona o pabellón se deduce de la lectura mediante la fórmula (180° − ángulo medido) / 2. Las demás tallas (princesa, ovalada, pera, esmeralda, cojín...) siguen cada una su propio diagrama, muy variable según el creador del diagrama y el objetivo buscado (rendimiento de material frente a rendimiento óptico): no existe un valor universal comparable que citar sin remitirse a un diagrama concreto.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Brillante redondo (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53 % del diámetro del rondel",
                facettes = "57 u 58 (33 en la corona, 24 o 25 en el pabellón, simetría de orden 8)",
                source = "Marcel Tolkowsky, «Diamond Design», 1919"
            )
        ),
        optiqueTitle = "El ángulo crítico y el ángulo de extinción",
        optiqueIntro = "Se distinguen a grandes rasgos tres casos para el trayecto de la luz que entra por la tabla: un pabellón demasiado poco profundo la deja escapar directamente por abajo; un pabellón demasiado profundo la atrapa en reflexiones internas que salen por una faceta lateral sin volver al ojo, dejando la piedra apagada pese a un pulido perfecto; un pabellón bien proporcionado, en cambio, la devuelve hacia el observador a través de la corona para un brillo máximo. El pabellón de una piedra tallada actúa como un espejo: por debajo del ángulo crítico de la especie, la luz que entra por la tabla se escapa a través del pabellón (el efecto «ventana» ya visto más arriba); tallado exactamente en el ángulo crítico aparece otro defecto, el «ojo de pez» — la tabla se ve apagada mientras el contorno de la corona sigue brillante, porque los rayos que la atraviesan rozan la pared del cono de reflexión sin llegar a reflejarse en ella. También existe un límite superior, el ángulo de extinción, más allá del cual el pabellón vuelve a perder luz por la faceta opuesta: ángulo de extinción = 60° − (ángulo crítico / 3). Un pabellón bien tallado respeta por tanto ángulo crítico < ángulo del pabellón < ángulo de extinción; cuanto mayor es el índice de refracción de la gema, más se amplía este margen de trabajo — un diamante perdona mucho más desvío que una fluorita. La corona sigue una regla complementaria: su ángulo máximo es inversamente proporcional al del pabellón (un pabellón más corto permite una corona más alta) y directamente proporcional al índice de refracción; trabajar unas décimas de grado por debajo de estos máximos sigue siendo la opción más segura. Todos estos esquemas de talla se apoyan en la Técnica del Punto de Encuentro (método estadounidense, Long y Steele), que utiliza las intersecciones entre tres o más facetas como referencias para garantizar automáticamente buenas proporciones y una buena conservación del peso respecto al bruto; para estimar el peso de una piedra ya tallada sin desengastarla, la fórmula habitual es peso (en quilates) = anchura³ × coeficiente de volumen de la talla × peso específico de la especie / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamante", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Esfena (titanita)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Circón (alto)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Granate demantoide", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alejandrita (crisoberilo)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Rubí y zafiro (corindón)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Espinela", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Peridoto", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Turmalina", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topacio", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Berilo", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Esmeralda", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Aguamarina", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Amatista y cuarzo", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calcita", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluorita", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Defectos habituales y correcciones",
        defautsIntro = "La mayoría de los defectos de talla provienen de un ángulo mal ajustado, una indexación errónea o una transferencia de dop mal alineada, más que de un problema del plato. La regla básica para corregir un mal encuentro o un pulido desigual: actuar siempre en el lado opuesto al defecto.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Faceta descentrada o mal pulida en un lado",
                cause = "Ligero desajuste de la piedra sobre el cheater.",
                remede = "Girar el cheater para inclinar la piedra hacia el lado opuesto al defecto."
            ),
            LapidaireDefaut(
                probleme = "Faceta mal encontrada o mal pulida arriba, ángulo demasiado grande",
                cause = "El ángulo indicado en el transportador es superior al que debería ser.",
                remede = "Reducir ligeramente el ángulo en el transportador para inclinar la piedra hacia abajo."
            ),
            LapidaireDefaut(
                probleme = "Faceta mal encontrada o mal pulida abajo, ángulo demasiado pequeño",
                cause = "El ángulo indicado en el transportador es inferior al que debería ser.",
                remede = "Aumentar ligeramente el ángulo en el transportador para inclinar la piedra hacia arriba."
            ),
            LapidaireDefaut(
                probleme = "Defecto combinado en diagonal (por ejemplo, arriba a la izquierda)",
                cause = "La piedra pudo haber girado ligeramente sobre el dop durante la transferencia.",
                remede = "Combinar una corrección en el cheater con otra en el ángulo, reduciendo si hace falta la velocidad de rotación del plato mientras se recupera el control."
            ),
            LapidaireDefaut(
                probleme = "La piedra emite un chirrido en el plato de pulido",
                cause = "La faceta en curso no es paralela al plato.",
                remede = "Detenerse y corregir la posición de la piedra antes de continuar — una señal fiable que nunca debe ignorarse."
            ),
            LapidaireDefaut(
                probleme = "Rayas paralelas que atraviesan varias facetas",
                cause = "El plato está contaminado con granos de una granulometría distinta.",
                remede = "Descontaminar el plato (cepillado con agua caliente y jabón, y raspado o rectificado si es necesario) antes de continuar."
            ),
            LapidaireDefaut(
                probleme = "Marcas de «quemadura» en la superficie de una faceta",
                cause = "Pulido demasiado estacionario, con presión excesiva, que provoca un sobrecalentamiento local.",
                remede = "Barrer la faceta con regularidad sobre el plato, reducir la presión y añadir, si hace falta, una gota de agua por segundo."
            ),
            LapidaireDefaut(
                probleme = "Aparece una pequeña cavidad durante la talla",
                cause = "Se ha puesto al descubierto una inclusión próxima a la superficie.",
                remede = "Limpiar mejor las inclusiones desde el desbaste sigue siendo la mejor prevención; una vez aparecido el agujero, retallar localmente o considerar una nueva talla completa si es necesario."
            )
        ),
        especesTitle = "Fichas prácticas por especie",
        especesIntro = "Más allá del ángulo crítico y el ángulo de extinción ya indicados, cada especie tiene sus propias costumbres de talla: orientación de la tabla según el pleocroísmo o el clivaje, sensibilidades particulares, y el disco y la pasta de pulido que dan mejores resultados. Son indicaciones basadas en la práctica, no una regla universal — cada piedra conserva sus propias particularidades (inclusiones, zonificación, estado de la superficie en bruto).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamante",
                orientation = "Isótropo: sin restricción de orientación ligada a un eje óptico.",
                fragilite = "Clivaje perfecto según 4 planos octaédricos, bien conocido y aprovechado por los talladores más que sufrido; la punta del pabellón sigue siendo el punto más vulnerable a los golpes.",
                polissage = "Disco de fundición (scaife) cargado con polvo de diamante — solo el diamante pule el diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Corindón (Rubí, Zafiro)",
                orientation = "Sin clivaje que tener en cuenta para la orientación.",
                fragilite = "Dureza 9, entre las más duras de las piedras talladas: pocas precauciones especiales.",
                polissage = "Excelentes resultados en disco de cobre con polvo de diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Berilo (Esmeralda, Aguamarina, Morganita, Heliodoro)",
                orientation = "Clivaje imperfecto, sin restricción real de orientación.",
                fragilite = "La esmeralda en particular requiere atención a los jardines (inclusiones), que pueden hacer que se fracture tanto al tallar como al pulir.",
                polissage = "El ácido acético en el agua de refrigeración mejora el pulido; polvo de óxido de aluminio, cerio o estaño, o Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Cuarzo (Amatista, Citrino, Cristal de roca, Cuarzo ahumado)",
                orientation = "Sin clivaje; tener en cuenta las zonificaciones de color al orientar.",
                fragilite = "Sin fragilidad particular de clivaje.",
                polissage = "El resultado a veces reserva sorpresas inesperadas, sin causa identificada."
            ),
            LapidaireEspeceFiche(
                pierre = "Topacio",
                orientation = "Desplazar la tabla unos 10° respecto al plano de clivaje basal, para no exponerlo directamente en su eje.",
                fragilite = "Clivaje neto en una sola dirección (plano basal) — fácil de facetar una vez evitado.",
                polissage = "Sin recomendación de disco más allá de las reglas generales."
            ),
            LapidaireEspeceFiche(
                pierre = "Turmalina",
                orientation = "Tallar preferiblemente la tabla paralela al eje óptico; una piedra policroma se secciona claramente en la unión de dos colores — mejor pegarla que serrarla en ese punto.",
                fragilite = "Sensible al calor y a los golpes durante la talla.",
                polissage = "Sin recomendación de disco más allá de las reglas generales."
            ),
            LapidaireEspeceFiche(
                pierre = "Granate (Almandino, Demantoide, Grosularia, Piropo, Espesartina, Uvarovita)",
                orientation = "Isótropo: orientación de la tabla sin restricción, sin dirección preferente.",
                fragilite = "Sin problema de fragilidad ligado al clivaje.",
                polissage = "El ácido acético (vinagre) en el agua de refrigeración mejora sistemáticamente el resultado; polvo de óxido de cerio, estaño o aluminio."
            ),
            LapidaireEspeceFiche(
                pierre = "Espinela",
                orientation = "Isótropa, sin restricción de orientación.",
                fragilite = "Sin problema de fragilidad ligado al clivaje.",
                polissage = "Se pule notablemente bien en disco de cobre con pasta de diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridoto (Olivino)",
                orientation = "Pleocroísmo muy débil, orientación poco restrictiva.",
                fragilite = "Sin fragilidad de clivaje notable.",
                polissage = "El resultado a veces reserva sorpresas inesperadas; el polvo de diamante funciona bien."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorita",
                orientation = "Se separa fácilmente según 4 direcciones: elegir bien la orientación para limitar el riesgo.",
                fragilite = "Uno de los ángulos críticos más cerrados entre las piedras talladas, muy sensible a golpes y desconchados.",
                polissage = "Disco preferiblemente de cera; el ácido mejora el resultado."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcita",
                orientation = "Orientar con cuidado para no exponer directamente los planos de clivaje.",
                fragilite = "Dureza muy baja (3) y clivaje perfecto en 3 direcciones, muy sensible al calor — piedra difícil de tallar y pulir; girar el disco despacio (unas 100 rpm).",
                polissage = "Disco de madera o cera, polvo de óxido de estaño o cromo, con unas gotas de ácido oxálico."
            ),
            LapidaireEspeceFiche(
                pierre = "Crisoberilo (Alejandrita)",
                orientation = "Sin restricción de orientación ligada al clivaje.",
                fragilite = "Dureza elevada (8,5), piedra dura sin problema particular.",
                polissage = "Se pule rápido con ácido y polvo Linde A; excelentes resultados en disco de cobre con polvo de diamante."
            )
        ),
        diagrammesTitle = "Diagramas",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagrama de proporciones del brillante redondo: nomenclatura de la corona, el rondel y el pabellón."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagrama del trayecto de la luz según la profundidad del pabellón: demasiado poco profundo, bien proporcionado, o demasiado profundo."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Grabado antiguo que representa un molino para tallar diamantes — la mecánica del plato giratorio, prácticamente inalterada en su principio desde hace siglos."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Máquina de tallar facetas moderna: mástil, caña y plato, tal como se describen más arriba."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Etapas de la transformación de una piedra en bruto en una piedra facetada."
            )
        ),
        tipsTitle = "Consejos prácticos",
        tips = listOf(
            LapidaireTip(texte = "Verifique siempre la calibración del ángulo indicado en el brazo con una piedra de prueba antes de empezar una pieza valiosa: un desfase de 0,5° en el pabellón basta para oscurecer visiblemente una piedra por lo demás bien tallada."),
            LapidaireTip(texte = "Centre cuidadosamente la piedra en el dop: un ligero desalineamiento se traduce en un rondel de grosor irregular y facetas que no se encuentran en el mismo punto en todo el contorno."),
            LapidaireTip(texte = "Mantenga un grosor de rondel moderado (ni un «filo de cuchillo» frágil ante los golpes, ni excesivamente grueso, lo que atrapa material sin ningún beneficio óptico) — una línea visible y regular, ni demasiado fina ni demasiado gruesa, es una de las señales de un trabajo cuidado."),
            LapidaireTip(texte = "Controle los puntos de encuentro (meet points) entre facetas con lupa y luz rasante en cada etapa: un punto de encuentro fallido se corrige fácilmente durante el formado, casi imposible una vez iniciado el prepulido."),
            LapidaireTip(texte = "Adapte la lubricación al material del plato: el agua es adecuada para la mayoría de los platos diamantados electrodepositados, mientras que algunos platos de pulido (el estaño en particular) dan mejores resultados con lubricación a base de aceite o una mezcla específica — siga las recomendaciones del fabricante del plato."),
            LapidaireTip(texte = "Verifique la sensibilidad térmica y el clivaje de la especie antes de tallar: algunas piedras (topacio, kunzita) tienen un clivaje marcado que puede propagarse por el calor o la presión de un desbaste demasiado agresivo."),
            LapidaireTip(texte = "Compruebe la simetría final observando la piedra de frente bajo una fuente de luz puntual: los reflejos de las facetas de la corona deben formar un patrón regular; una asimetría visible a simple vista en esta etapa ya no se puede corregir en el pulido."),
            LapidaireTip(texte = "Nunca apoye la piedra sobre un plato parado y luego lo ponga en marcha: arranque siempre el plato en rotación antes de presentarle la piedra, con un ligero movimiento pendular para evitar que quede fija en el mismo punto, lo que desgastaría el disco de forma desigual y la piedra de manera irregular."),
            LapidaireTip(texte = "Una tabla que parece «apagada» mientras el contorno de la piedra sigue brillante señala un efecto ventana: el pabellón es demasiado poco profundo y deja escapar la luz por debajo en lugar de devolverla por la tabla — corríjalo profundizando el ángulo del pabellón, sin superar nunca el ángulo de extinción."),
            LapidaireTip(texte = "En piedras alargadas (óvalo, marquesa, pera, cojín...), una cruz oscura visible en el centro (el efecto llamado «X+») delata facetas de longitud y anchura mal empalmadas en el pabellón; un diagrama de talla bien estudiado para la forma elegida lo evita mucho mejor que un ajuste caso por caso.")
        ),
        conservationTitle = "Conservación y manipulación de las piedras talladas",
        conservationIntro = "Una piedra bien tallada sigue siendo vulnerable una vez montada o guardada: unas pocas precauciones sencillas evitan la mayoría de los arañazos y daños accidentales.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Limpieza",
                conseil = "Nunca frotar una piedra en seco: el polvo actúa como abrasivo y raya la superficie pulida. Lavar con agua tibia jabonosa y un cepillo suave, o con alcohol para un desengrasado rápido, y secar con un paño que no suelte pelusa."
            ),
            LapidaireConservationTip(
                titre = "Guardado individual",
                conseil = "Guardar cada piedra por separado, en un pliegue de papel o una bolsita individual: el contacto entre piedras, aunque sea breve, las raya mutuamente."
            ),
            LapidaireConservationTip(
                titre = "Nunca mezclar durezas",
                conseil = "Nunca reunir en un mismo compartimento piedras de dureza diferente: la más dura raya sistemáticamente a la más blanda, incluso por un simple roce durante el transporte."
            ),
            LapidaireConservationTip(
                titre = "Choques térmicos y productos químicos",
                conseil = "Evitar los cambios bruscos de temperatura y los productos químicos agresivos (lejía, ácidos), que pueden agrietar ciertas piedras o alterar un tratamiento (esmeralda aceitada, piedra impregnada). El limpiador ultrasónico debe evitarse en piedras fracturadas, aceitadas o frágiles."
            ),
            LapidaireConservationTip(
                titre = "Exposición a la luz",
                conseil = "Algunas piedras son fotosensibles: la amatista o la kunzita expuestas mucho tiempo a luz intensa pueden decolorarse. Conservar las piedras sensibles al abrigo de una exposición prolongada."
            ),
            LapidaireConservationTip(
                titre = "Transporte y manipulación",
                conseil = "Para el transporte, usar una bolsita acolchada y evitar el contacto directo entre varias piedras o joyas reunidas juntas; manipular por el engaste o la mesa y no por el pabellón, más vulnerable a los golpes."
            )
        ),
        disclaimerTitle = "Un oficio que se aprende en el taller",
        disclaimerBody = "Esta ficha presenta referencias generales, no un manual completo: la talla de facetas se aprende mediante práctica supervisada, con material adecuado y normas de seguridad (protección ocular y respiratoria, refrigeración continua del plato) propias de cada taller y de cada máquina. Los diagramas mostrados proceden de fuentes reales y libres de derechos (véanse los créditos); en su ausencia temporal, solo se muestra la leyenda.",
        machinesTypesTitle = "Las máquinas del oficio",
        machinesTypesIntro = "Más allá de los componentes de una máquina de tallar facetas detallados más arriba, aquí tienes un panorama de los diferentes tipos de máquinas realmente utilizadas en el taller, tanto para el facetado como para el cabujón, con sus características y la técnica asociada. Las marcas citadas lo son a título de ejemplo representativo de su categoría, sin vínculo comercial con Gems of Rod.",
        categorieFacettageLabel = "Facetado",
        categorieCabochonLabel = "Cabujón",
        categoriePolyvalentLabel = "Polivalente",
        caracteristiquesLabel = "Características",
        techniqueLabel = "Técnica",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Brazo manual con calibre (jam-peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "La herramienta de talla más simple y antigua: la piedra se pega al extremo de una varilla sostenida a mano contra el disco, ajustando el ángulo a ojo o con un calibre simple. Todavía se enseña en las escuelas tradicionales de talla (Sri Lanka, Tailandia).",
                caracteristiques = listOf(
                    "Ninguna pieza mecánica de indexación",
                    "Coste prácticamente nulo",
                    "Depende por completo de la destreza del tallador",
                    "Ritmo de trabajo rápido en manos expertas"
                ),
                technique = "La mano del tallador ajusta continuamente el ángulo y la presión contra el disco; la precisión de las facetas depende de la repetición del gesto, no de la máquina."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Talladora con cabezal de índice mecánico extraíble",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Estándar de los talleres occidentales desde los años 1970 (Facetron, Ultra Tec, Poly-Metric...): un cabezal de índice intercambiable ajusta el ángulo y la rotación de la piedra con precisión de una décima de grado, mientras el brazo se desliza verticalmente para ajustar la profundidad de corte.",
                caracteristiques = listOf(
                    "Precisión angular de una décima de grado",
                    "Cabezales de índice intercambiables (facetas, escamas, cavetto)",
                    "Tope de profundidad micrométrico",
                    "Inversión significativa (máquina + discos)"
                ),
                technique = "El tallador ajusta el ángulo y la muesca de índice antes de cada faceta, y luego baja la piedra contra el disco hasta el tope fijado: la reproducibilidad sustituye al gesto libre del brazo manual."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Talladora con cabezal de índice fijo integrado",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Versión más asequible en la que el cabezal de índice está solidario al brazo en lugar de ser intercambiable, con un número de muescas grabadas fijado de fábrica (a menudo 64, 96 o 120 según el modelo).",
                caracteristiques = listOf(
                    "Precio de entrada muy inferior al de los cabezales extraíbles",
                    "Número de muescas de índice limitado y no modificable",
                    "Buena robustez para un uso regular",
                    "Adecuada para el aprendizaje y las tallas habituales"
                ),
                technique = "Mismo principio que el cabezal extraíble, pero la elección de los patrones de facetado se limita a las divisiones de índice grabadas en la máquina, suficiente para la mayoría de las tallas clásicas (redonda, ovalada, cojín)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Talladora asistida por ordenador (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Máquina controlada digitalmente que reproduce un plan de talla (diagrama de ángulos e índices) de forma idéntica en una serie de piedras, utilizada en producción industrial o para tallas de fantasía muy complejas.",
                caracteristiques = listOf(
                    "Reproducibilidad perfecta de una piedra a otra",
                    "Programación a partir de un plan de talla digital",
                    "Inversión elevada, reservada a la producción en volumen",
                    "Reduce la parte de destreza manual en el resultado final"
                ),
                technique = "El plan de talla se carga en el software de la máquina, que encadena automáticamente los ángulos e índices programados; el papel del lapidario se desplaza hacia el ajuste y el control de calidad."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Cabocheadora de muelas múltiples en línea",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Equipo de referencia para el cabujón (tipo Genie, CabKing): de 6 a 8 muelas diamantadas de grano decreciente seguidas de discos de fieltro para pulir, alineadas sobre un mismo bastidor con riego continuo.",
                caracteristiques = listOf(
                    "De 6 a 8 puestos de grano decreciente sobre un mismo eje",
                    "Riego continuo de cada muela",
                    "Paso rápido de un puesto a otro sin cambiar de disco",
                    "Precio elevado pero muy buena longevidad"
                ),
                technique = "El cabujón se desbasta y luego se afina pasando de una muela a otra por grano decreciente, hasta los fieltros de pulido finales: cada puesto borra las microrrayas dejadas por el anterior."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Cabocheadora Vevor (multi-muelas, de entrada)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versión económica de la cabocheadora de muelas múltiples, fabricada en China bajo la marca Vevor: muy extendida entre principiantes y pequeños talleres gracias a un precio claramente inferior al de las marcas especializadas (Genie, CabKing).",
                caracteristiques = listOf(
                    "De 6 a 8 muelas diamantadas + fieltros sobre un mismo eje, como en los modelos profesionales",
                    "Motor y rodamientos de gama de entrada, tolerancias mecánicas más amplias",
                    "Bandeja de recuperación de agua integrada",
                    "Precio claramente inferior al de las marcas especializadas, a costa de una vida útil más corta"
                ),
                technique = "Mismo principio de progresión muela a muela que una cabocheadora profesional, pero con más vibraciones: una presión de trabajo más ligera y un mantenimiento más frecuente de los rodamientos compensan la mecánica menos precisa."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Esmeriladora-pulidora combinada de eje horizontal",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versión más artesanal: uno o dos ejes horizontales en los que se montan los discos (muelas, fieltros) según la necesidad, en lugar de una línea de puestos fijos.",
                caracteristiques = listOf(
                    "Discos intercambiables a elección del lapidario",
                    "También se usa para desbastar un bruto antes del corte",
                    "Más barata que una cabocheadora de muelas múltiples dedicada",
                    "Requiere más manipulaciones entre etapas"
                ),
                technique = "El lapidario cambia él mismo el disco montado en el eje en cada etapa de grano, a diferencia de la cabocheadora de muelas múltiples, donde los puestos son fijos y están yuxtapuestos."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Sierra de corte (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Pequeña sierra circular de hoja diamantada, utilizada antes del cabujón para cortar el bruto en láminas (slabs) del grosor deseado antes de darle forma en las muelas.",
                caracteristiques = listOf(
                    "Hoja diamantada refrigerada por baño de aceite o agua",
                    "Diámetro habitual de 10 a 25 cm según el modelo",
                    "Guía de corte regulable para láminas uniformes",
                    "Etapa previa indispensable, no es talla de facetas"
                ),
                technique = "El bruto se avanza manualmente contra la hoja en rotación lenta; el grosor de la lámina obtenida determina directamente el grosor máximo del futuro cabujón."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Taladradora de gemas",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Taladro de columna equipado con brocas diamantadas huecas y un sistema de riego, utilizado para perforar perlas y cabujones destinados a montarse en colgantes o enhebrarse.",
                caracteristiques = listOf(
                    "Brocas diamantadas huecas de diferentes diámetros",
                    "Refrigeración por agua obligatoria para evitar la fisuración",
                    "Velocidad de rotación regulable según la dureza de la piedra",
                    "Perforación en dos tiempos (por ambas caras) en piedras frágiles"
                ),
                technique = "La perforación se realiza a velocidad lenta y con riego constante, a menudo atacando la piedra desde ambas caras para evitar el astillado de salida característico de una perforación pasante en un solo paso."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Esmeriladora combinada de facetado/cabujón de entrada",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Pequeña máquina económica que combina muelas y fieltros sobre un mismo eje para iniciarse tanto en el cabujón como en un facetado elemental, sin cabezal de índice preciso.",
                caracteristiques = listOf(
                    "Precio de acceso muy bajo, formato compacto",
                    "Combina varios usos en una sola máquina",
                    "Ausencia de cabezal de índice preciso para un verdadero facetado",
                    "Adecuada para el descubrimiento, limitada para un resultado profesional"
                ),
                technique = "El principiante experimenta ambas disciplinas sobre el mismo bastidor, a costa de una precisión angular y de índice muy inferior a la de una máquina especializada."
            )
        )
    )

    private val it = LapidairePage(
        intro = "Il lapidario dà forma e lucida le pietre grezze trasformandole in pietre tagliate. Questo mestiere comprende diverse specializzazioni — la sfaccettatura (faceting) per le pietre trasparenti, il cabochon per le pietre opache o traslucide, l'incisione e il lavoro ornamentale — ma la sfaccettatura, la più tecnica, è quella che dà a una pietra il suo gioco di luce. Questa scheda presenta l'attrezzatura e i riferimenti di base del mestiere; è rivolta a professionisti e appassionati esperti, non a un primo apprendimento senza supervisione.",
        machinesTitle = "La macchina sfaccettatrice",
        machinesIntro = "Una macchina sfaccettatrice (faceting machine) mantiene la pietra a un angolo e un indice precisi contro un disco abrasivo rotante (il piatto, o lap). La sua precisione meccanica — al decimo di grado per l'angolo, al punto esatto per l'indice — è ciò che distingue un taglio professionale da una semplice smerigliatura a mano libera. L'attrezzatura spazia dal semplice bastone con cheater (artigianale, veloce ma poco preciso) alle macchine con testa divisoria meccanica (amovibile o fissa, precise ma di produzione più lenta), fino ai modelli controllati da computer per il taglio in serie.",
        machines = listOf(
            LapidaireComponent(
                nom = "Albero (mast)",
                description = "Colonna verticale fissata al telaio della macchina, lungo la quale scorre e ruota la testa portacannula. La sua altezza e la sua inclinazione regolabile determinano, insieme al braccio, l'angolo di sfaccettatura applicato sul piatto."
            ),
            LapidaireComponent(
                nom = "Cannula (quill)",
                description = "Tubo in cui scorre l'asta che sostiene la pietra (il dop). La cannula ruota su se stessa per portare successivamente ogni sfaccettatura prevista davanti al disco, e scorre per far avanzare la pietra man mano che il materiale viene asportato."
            ),
            LapidaireComponent(
                nom = "Ruota d'indice (index gear)",
                description = "Ruota dentata fissata all'estremità della cannula, generalmente disponibile in diverse graduazioni — 64, 72, 80, 96 o 120 tacche — per coprire ogni stile di taglio; un disco a «indice rapido», con meno tacche, si sovrappone spesso all'indice normale per velocizzare il posizionamento quando si taglia una serie di pietre. Bloccare la rotazione della pietra in una posizione precisa per ogni sfaccettatura è condizione indispensabile per la simmetria di un taglio come il brillante rotondo, che distribuisce le sue sfaccettature con simmetria di ordine 8. L'indice scelto deve essere un multiplo esatto del numero di lati da tagliare: un pentagono, ad esempio, è impossibile con un indice 96 (96 ÷ 5 non è un numero intero) ma si taglia senza problemi con un indice 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Ghiera di regolazione fine sovrapposta all'angolo indicato sul braccio. Il suo scopo principale non è modificare l'angolo di taglio in sé, ma ripristinare il parallelismo tra la sfaccettatura in lavorazione e il piatto quando quest'ultimo si è leggermente deformato o usurato in modo irregolare — una regolazione da rifare ogni volta che una sfaccettatura smette di lucidarsi in modo uniforme su tutta la sua superficie."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Asta (legno, alluminio o ottone — questi ultimi due trattengono meglio il calore del preriscaldamento) sulla quale la pietra viene fissata prima di essere inserita nella cannula. Il fissaggio avviene con cera calda (il metodo più comune, fonde intorno agli 80 °C), colla cianoacrilica («superglue», presa rapida ma residui difficili da rimuovere) o epossidica (buona tenuta, ma la sua reazione esotermica va tenuta d'occhio su pietre sensibili al calore). I dop più sofisticati hanno un perno, un risalto o una scanalatura che li orienta sempre allo stesso modo nel divisore — condizione indispensabile per trasferire la pietra da un dop all'altro (tagliare la corona dopo il padiglione) senza disallineamento né rotazione indesiderata."
            ),
            LapidaireComponent(
                nom = "Piatto (lap)",
                description = "Disco metallico rotante, generalmente da 6\" (≈152 mm) o 8\" (≈203 mm) di diametro, sul quale è fissato o caricato l'abrasivo. Si distinguono i piatti per tagliare (diamante elettrodepositato in superficie, o ghisa impregnata di particelle di diamante mediante rullatura) dai piatti per lucidare, di materiale più morbido. Una macchina dispone generalmente di più piatti intercambiabili, uno per ogni fase di grana."
            ),
            LapidaireComponent(
                nom = "Goniometro",
                description = "Strumento di controllo che misura l'angolo reale di una pietra già tagliata, su due sfaccettature principali diametralmente opposte, per verificare il taglio ottenuto. L'angolo misurato (C' per la corona, P' per il padiglione) si converte con la formula angolo = (180° − angolo misurato) / 2, poiché il goniometro rileva l'angolo supplementare formato con il piano della cintura."
            )
        ),
        disquesTitle = "Dischi di formatura e lucidatura",
        disquesIntro = "Il taglio procede per fasi di grana via via più fine, ciascuna delle quali cancella i micrograffi lasciati dalla precedente; saltare una fase lascia segni che la lucidatura finale non può più correggere. La velocità di rotazione del piatto accompagna questa progressione: piuttosto lenta nella sgrossatura (100-300 giri/min), più rapida nella formatura delle sfaccettature (300-600 giri/min), e più rapida ancora nella lucidatura (700-1000 giri/min o più) — sempre in base alla sensibilità della pietra, rallentata senza esitazione per una pietra tenera o sfaldabile.",
        disques = listOf(
            LapidaireDisc(
                nom = "Sgrossatura",
                grain = "260-325",
                usage = "Asporta rapidamente materiale per dare alla pietra la sua forma generale (preforma) a partire dal grezzo o da una lastra segata. Piatto diamantato aggressivo, forte pressione, raffreddamento ad acqua obbligatorio."
            ),
            LapidaireDisc(
                nom = "Formatura delle sfaccettature",
                grain = "600-1200",
                usage = "Traccia e rifinisce ogni sfaccettatura all'angolo e all'indice esatti previsti dal diagramma di taglio. È la fase più lunga e più determinante per la simmetria e la precisione dei punti d'incontro (meet points) tra sfaccettature vicine."
            ),
            LapidaireDisc(
                nom = "Prelucidatura",
                grain = "3000-8000",
                usage = "Rifinisce la superficie lasciata dalla formatura fino a un aspetto quasi opaco ma senza graffi profondi, mantenendo esattamente gli stessi angoli — qualsiasi correzione dell'angolo in questa fase deve essere minima, pena il dover tornare a una grana più grossa."
            ),
            LapidaireDisc(
                nom = "Lucidatura finale",
                grain = "Pasta o polvere submicronica (spesso ossido di cerio o diamante finissimo)",
                usage = "Il piatto di lucidatura si sceglie per durezza (scala di Mohs), dal più duro al più morbido: ceramica (9), rame (3), zinco (2,5), fenolico (2,2), plexiglas o piombo-stagno (2), stagno puro (1,7), piombo (1,5), PVC (1) e cera con varie cariche (0,3, variabile a seconda della formula). Non è mai il piatto in sé a lucidare, ma solo la polvere applicata su di esso che funge da agente abrasivo — il piatto è solo un supporto la cui durezza deve corrispondere a quella della pietra. Lucidatura a secco (polvere fissata da un legante grasso) o a umido (polvere diluita in acqua, applicata di continuo), a seconda del metodo scelto."
            )
        ),
        indexTitle = "Scelta dell'indice",
        indexIntro = "Un indice si sceglie soprattutto in base al numero di lati da tagliare: deve esserne un multiplo esatto (un pentagono è impossibile con un indice 96, ma fattibile con un indice 80). La rotazione per tacca è inversamente proporzionale al numero di tacche dell'indice (360° diviso il numero di tacche) — un indice 120 avanza di 3° per tacca, un indice 32 di 11,25°. Riconvertire uno schema di taglio da un indice a un altro è possibile solo se i due indici sono multipli o sottomultipli tra loro: basta dividere o moltiplicare ciascun numero di indice per il loro rapporto (dividere per 3, ad esempio, per passare da un indice 96 a un indice 32). Poiché tutti gli indici comuni sono multipli di 4, un quadrato si taglia con qualsiasi di essi; al contrario, è impossibile indicizzare a una posizione intermedia tra due tacche, il che esclude a priori la maggior parte dei poligoni a lati dispari. L'indice 96 resta il più diffuso tra i produttori, offrendo un buon compromesso tra finezza di graduazione e rapidità di posizionamento; l'indice 120 è il più completo, a costo di un posizionamento più lento.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / tacca", cotesTaillables = "Quadrato (4), ottagono (8), esadecagono (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / tacca", cotesTaillables = "Triangolo (3), quadrato (4), pentagono (5), esagono (6), decagono (10), dodecagono (12), pentadecagono (15), icosagono (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / tacca", cotesTaillables = "Quadrato (4), ottagono (8), esadecagono (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / tacca", cotesTaillables = "Triangolo (3), quadrato (4), esagono (6), ottagono (8), ennagono (9), dodecagono (12), ottodecagono (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / tacca", cotesTaillables = "Quadrato (4), pentagono (5), ottagono (8), decagono (10), esadecagono (16), icosagono (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / tacca", cotesTaillables = "Il più diffuso — 9 poligoni regolari tagliabili in totale, tra cui quadrato, esagono, ottagono, dodecagono"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / tacca", cotesTaillables = "Il più completo — 14 poligoni regolari tagliabili in totale, il massimo tra gli indici comuni"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / tacca", cotesTaillables = "Quadrato (4), ottagono (8), esadecagono (16), 32 lati, 64 lati")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Peso stimato",
            intro = "Per una pietra già tagliata e montata, impossibile da pesare direttamente: il peso si deduce dal volume, a partire da misure al calibro (precisione di 1/100 mm) e dal peso specifico della specie. Formula indicativa, precisione dell'ordine del 10-15% — non è una pesata reale.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Rotondo",
                LapidaireCutShape.OVALE to "Ovale",
                LapidaireCutShape.COUSSIN_CARRE to "Cuscino quadrato",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Cuscino rettangolare",
                LapidaireCutShape.CARRE_A_GRADIN to "Quadrato a gradini",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rettangolo a gradini",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Cuscino quadrato (a gradini)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Cuscino rettangolare (a gradini)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Goccia",
                LapidaireCutShape.TRIANGLE_BOMBE to "Triangolo bombato",
                LapidaireCutShape.TRIANGLE to "Triangolo",
                LapidaireCutShape.TRAPEZE to "Trapezio",
                LapidaireCutShape.COEUR to "Cuore"
            ),
            dimension1Label = "Lunghezza o diametro (mm)",
            dimension2Label = "Larghezza (mm)",
            heightLabel = "Profondità totale, tavola-culet (mm)",
            sgLabel = "Peso specifico della specie",
            computeLabel = "Calcola",
            resultLabel = "Peso stimato: %s carati",
            disclaimer = "Stima per volume, precisione indicativa 10-15% — non sostituisce una pesata reale.",
            errorMessage = "Inserisci valori validi (numeri positivi) per tutte le dimensioni e il peso specifico."
        ),
        anglesTitle = "Angoli di riferimento: il brillante rotondo",
        anglesIntro = "Le proporzioni seguenti sono quelle pubblicate dal matematico belga Marcel Tolkowsky nel 1919, che calcolò l'angolo ottimale per massimizzare il ritorno di luce (brillantezza) e la dispersione (fuoco) di un diamante tagliato a brillante rotondo — è il riferimento storico ancora oggi usato come punto di partenza, sebbene i laboratori moderni (in particolare il GIA) ammettano un intervallo di tolleranza attorno a questi valori anziché una cifra unica. Per le pietre colorate, il cui indice di rifrazione differisce da quello del diamante, il rapporto classico tra corona e cintura si colloca piuttosto tra il 25/75% e il 30/70%. Regola imperativa, qualunque sia la pietra: non tagliare mai una sfaccettatura di padiglione con un angolo inferiore all'angolo critico della specie (la luce sfugge allora dal padiglione, effetto «finestra»), né superiore all'angolo di estinzione (zone scure). Per verificare una pietra già tagliata, un goniometro misura l'angolo su due sfaccettature principali opposte: l'angolo reale di corona o padiglione si deduce dalla lettura con la formula (180° − angolo misurato) / 2. Gli altri tagli (princess, ovale, a pera, smeraldo, cuscino...) seguono ciascuno un proprio diagramma, molto variabile a seconda di chi lo ha ideato e dell'obiettivo perseguito (resa di materiale contro resa ottica): non esiste un valore universale comparabile da citare senza fare riferimento a un diagramma preciso.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Brillante rotondo (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53% del diametro della cintura",
                facettes = "57 o 58 (33 sulla corona, 24 o 25 sul padiglione, simmetria di ordine 8)",
                source = "Marcel Tolkowsky, «Diamond Design», 1919"
            )
        ),
        optiqueTitle = "L'angolo critico e l'angolo di estinzione",
        optiqueIntro = "Si distinguono schematicamente tre casi per il percorso della luce che entra dalla tavola: un padiglione troppo poco profondo la lascia sfuggire direttamente dal basso; un padiglione troppo profondo la intrappola in riflessioni interne che escono da una sfaccettatura laterale senza tornare all'occhio, lasciando la pietra spenta nonostante una lucidatura perfetta; un padiglione ben proporzionato, al contrario, la rimanda verso l'osservatore attraverso la corona per la massima brillantezza. Il padiglione di una pietra sfaccettata agisce come uno specchio: sotto l'angolo critico della specie, la luce che entra dalla tavola sfugge attraverso il padiglione (l'effetto «finestra» già visto sopra); tagliato esattamente all'angolo critico, compare un altro difetto, l'«occhio di pesce» — la tavola appare spenta mentre il contorno della corona resta brillante, perché i raggi che la attraversano sfiorano la parete del cono di riflessione senza riflettersi davvero. Esiste anche un limite superiore, l'angolo di estinzione, oltre il quale il padiglione perde di nuovo luce dalla sfaccettatura opposta: angolo di estinzione = 60° − (angolo critico / 3). Un padiglione ben tagliato rispetta quindi angolo critico < angolo del padiglione < angolo di estinzione; più è alto l'indice di rifrazione della gemma, più si allarga questo margine di lavoro — un diamante perdona molto più scarto di una fluorite. La corona segue una regola complementare: il suo angolo massimo è inversamente proporzionale a quello del padiglione (un padiglione più corto consente una corona più alta) e direttamente proporzionale all'indice di rifrazione; lavorare qualche decimo di grado sotto questi massimi resta la scelta più sicura. Tutti questi schemi di taglio si basano sulla Tecnica del Punto d'Incontro (metodo americano, Long e Steele), che usa le intersezioni tra tre o più sfaccettature come riferimenti per garantire automaticamente buone proporzioni e una buona conservazione del peso rispetto al grezzo; per stimare il peso di una pietra già tagliata senza smontarla dal castone, la formula abituale è peso (in carati) = larghezza³ × coefficiente di volume del taglio × peso specifico della specie / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamante", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Sfene (titanite)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Zircone (alto)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Granato demantoide", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alessandrite (crisoberillo)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Rubino e zaffiro (corindone)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Spinello", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Peridoto", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Tormalina", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topazio", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Berillo", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Smeraldo", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Acquamarina", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Ametista e quarzo", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calcite", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluorite", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Difetti comuni e correzioni",
        defautsIntro = "La maggior parte dei difetti di sfaccettatura deriva da un angolo mal impostato, un'indicizzazione errata o un trasferimento di dop mal allineato, più che da un problema del piatto. La regola di base per correggere un cattivo incontro o una lucidatura irregolare: agire sempre sul lato opposto al difetto.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Sfaccettatura decentrata o mal lucidata su un lato",
                cause = "Leggero disallineamento della pietra sul cheater.",
                remede = "Ruotare il cheater per inclinare la pietra verso il lato opposto al difetto."
            ),
            LapidaireDefaut(
                probleme = "Sfaccettatura mal raccordata o mal lucidata in alto, angolo troppo grande",
                cause = "L'angolo indicato sul goniometro è superiore a quanto dovrebbe essere.",
                remede = "Ridurre leggermente l'angolo sul goniometro per inclinare la pietra verso il basso."
            ),
            LapidaireDefaut(
                probleme = "Sfaccettatura mal raccordata o mal lucidata in basso, angolo troppo piccolo",
                cause = "L'angolo indicato sul goniometro è inferiore a quanto dovrebbe essere.",
                remede = "Aumentare leggermente l'angolo sul goniometro per inclinare la pietra verso l'alto."
            ),
            LapidaireDefaut(
                probleme = "Difetto combinato in diagonale (ad esempio in alto a sinistra)",
                cause = "La pietra potrebbe aver ruotato leggermente sul dop durante il trasferimento.",
                remede = "Combinare una correzione sul cheater con una sull'angolo, riducendo se necessario la velocità di rotazione del piatto finché non si riprende il controllo."
            ),
            LapidaireDefaut(
                probleme = "La pietra emette uno stridio sul piatto di lucidatura",
                cause = "La sfaccettatura in lavorazione non è parallela al piatto.",
                remede = "Fermarsi e correggere la posizione della pietra prima di continuare — un segnale affidabile, da non ignorare mai."
            ),
            LapidaireDefaut(
                probleme = "Graffi paralleli che attraversano più sfaccettature",
                cause = "Il piatto è contaminato da grani di granulometria diversa.",
                remede = "Decontaminare il piatto (spazzolatura con acqua calda e sapone, poi raschiatura o rettifica se necessario) prima di riprendere."
            ),
            LapidaireDefaut(
                probleme = "Tracce di «bruciatura» sulla superficie di una sfaccettatura",
                cause = "Lucidatura troppo ferma in un punto, con pressione eccessiva, che provoca un surriscaldamento localizzato.",
                remede = "Spazzolare regolarmente la sfaccettatura sul piatto, ridurre la pressione, aggiungere se necessario una goccia d'acqua al secondo."
            ),
            LapidaireDefaut(
                probleme = "Compare una piccola cavità durante la sfaccettatura",
                cause = "È stata messa a nudo un'inclusione vicina alla superficie.",
                remede = "Ripulire meglio le inclusioni già in fase di sgrossatura resta la migliore prevenzione; una volta comparso il foro, ritagliare localmente o considerare un ritaglio completo se necessario."
            )
        ),
        especesTitle = "Schede pratiche per specie",
        especesIntro = "Oltre all'angolo critico e all'angolo di estinzione già indicati, ogni specie ha le proprie abitudini di taglio: orientamento della tavola in base al pleocroismo o alla sfaldatura, sensibilità particolari, e il disco e la polvere di lucidatura che danno i migliori risultati. Indicazioni tratte dalla pratica, non una regola universale — ogni pietra conserva le proprie particolarità (inclusioni, zonature, stato della superficie grezza).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamante",
                orientation = "Isotropo: nessun vincolo di orientamento legato a un asse ottico.",
                fragilite = "Sfaldatura perfetta secondo 4 piani ottaedrici, ben conosciuta e sfruttata dai tagliatori più che subita; la punta del padiglione resta il punto più vulnerabile agli urti.",
                polissage = "Disco in ghisa (scaife) caricato con polvere di diamante — solo il diamante lucida il diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Corindone (Rubino, Zaffiro)",
                orientation = "Nessuna sfaldatura da considerare per l'orientamento.",
                fragilite = "Durezza 9, tra le più dure fra le pietre sfaccettate: poche precauzioni particolari.",
                polissage = "Ottimi risultati su disco di rame con polvere di diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Berillo (Smeraldo, Acquamarina, Morganite, Eliodoro)",
                orientation = "Sfaldatura imperfetta, senza vero vincolo di orientamento.",
                fragilite = "Lo smeraldo in particolare richiede attenzione ai giardin (inclusioni), che possono causarne la rottura sia in taglio che in lucidatura.",
                polissage = "L'acido acetico nell'acqua di raffreddamento migliora la lucidatura; polvere di ossido di alluminio, cerio o stagno, o Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Quarzo (Ametista, Citrino, Cristallo di rocca, Quarzo fumé)",
                orientation = "Nessuna sfaldatura; tenere conto delle zonature di colore nell'orientamento.",
                fragilite = "Nessuna fragilità particolare di sfaldatura.",
                polissage = "Il risultato riserva talvolta sorprese inattese, senza causa identificata."
            ),
            LapidaireEspeceFiche(
                pierre = "Topazio",
                orientation = "Spostare la tavola di circa 10° rispetto al piano di sfaldatura basale, per non esporlo direttamente nel suo asse.",
                fragilite = "Sfaldatura netta in una sola direzione (piano basale) — facile da sfaccettare una volta evitata.",
                polissage = "Nessuna raccomandazione di disco oltre le regole generali."
            ),
            LapidaireEspeceFiche(
                pierre = "Tormalina",
                orientation = "Tagliare preferibilmente la tavola parallela all'asse ottico; una pietra policroma si sezione nettamente alla giunzione tra due colori — meglio incollarla che segarla in quel punto.",
                fragilite = "Sensibile al calore e agli urti durante il taglio.",
                polissage = "Nessuna raccomandazione di disco oltre le regole generali."
            ),
            LapidaireEspeceFiche(
                pierre = "Granato (Almandino, Demantoide, Grossularia, Piropo, Spessartina, Uvarovite)",
                orientation = "Isotropo: orientamento della tavola senza vincoli, nessuna direzione preferenziale.",
                fragilite = "Nessun problema di fragilità legato alla sfaldatura.",
                polissage = "L'acido acetico (aceto) nell'acqua di raffreddamento migliora sistematicamente il risultato; polvere di ossido di cerio, stagno o alluminio."
            ),
            LapidaireEspeceFiche(
                pierre = "Spinello",
                orientation = "Isotropo, nessun vincolo di orientamento.",
                fragilite = "Nessun problema di fragilità legato alla sfaldatura.",
                polissage = "Si lucida notevolmente bene su disco di rame con pasta diamantata."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridoto (Olivina)",
                orientation = "Pleocroismo molto debole, orientamento poco vincolante.",
                fragilite = "Nessuna fragilità di sfaldatura degna di nota.",
                polissage = "Il risultato riserva talvolta sorprese inattese; la polvere diamantata funziona bene."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorite",
                orientation = "Si separa facilmente secondo 4 direzioni: scegliere bene l'orientamento per limitare il rischio.",
                fragilite = "Uno degli angoli critici più chiusi tra le pietre sfaccettate, molto sensibile a urti e scheggiature.",
                polissage = "Disco preferibilmente in cera; l'acido migliora il risultato."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcite",
                orientation = "Orientare con cura per non esporre direttamente i piani di sfaldatura.",
                fragilite = "Durezza molto bassa (3) e sfaldatura perfetta in 3 direzioni, fortemente sensibile al calore — pietra difficile da tagliare e lucidare; far girare il disco lentamente (circa 100 giri/min).",
                polissage = "Disco in legno o cera, polvere di ossido di stagno o cromo, con qualche goccia di acido ossalico."
            ),
            LapidaireEspeceFiche(
                pierre = "Crisoberillo (Alessandrite)",
                orientation = "Nessun vincolo di orientamento legato alla sfaldatura.",
                fragilite = "Durezza elevata (8,5), pietra dura senza problemi particolari.",
                polissage = "Si lucida rapidamente con acido e polvere Linde A; ottimi risultati su disco di rame con polvere di diamante."
            )
        ),
        diagrammesTitle = "Diagrammi",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagramma delle proporzioni del brillante rotondo: nomenclatura di corona, cintura e padiglione."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagramma del percorso della luce in base alla profondità del padiglione: troppo poco profondo, ben proporzionato, o troppo profondo."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Incisione antica che raffigura un mulino per tagliare i diamanti — la meccanica del piatto rotante, di principio pressoché invariata da secoli."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Una macchina sfaccettatrice moderna: albero, cannula e piatto, come descritti sopra."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Fasi della trasformazione di una pietra grezza in una pietra sfaccettata."
            )
        ),
        tipsTitle = "Consigli pratici",
        tips = listOf(
            LapidaireTip(texte = "Verificare sempre la taratura dell'angolo indicato sul braccio con una pietra di prova prima di iniziare un pezzo di valore: uno scarto di 0,5° sul padiglione basta a scurire visibilmente una pietra altrimenti ben tagliata."),
            LapidaireTip(texte = "Centrare con cura la pietra sul dop: un lieve disallineamento si traduce in una cintura di spessore irregolare e in sfaccettature che non si incontrano nello stesso punto lungo tutto il perimetro."),
            LapidaireTip(texte = "Mantenere uno spessore di cintura moderato (né un «filo di rasoio» fragile agli urti, né eccessivamente spesso, il che intrappola materiale senza alcun beneficio ottico) — una linea visibile e regolare, né troppo sottile né troppo spessa, è uno dei segni di un lavoro curato."),
            LapidaireTip(texte = "Controllare i punti d'incontro (meet points) tra le sfaccettature con la lente sotto luce radente a ogni fase: un punto d'incontro mancato si corregge facilmente durante la formatura, quasi impossibile una volta iniziata la prelucidatura."),
            LapidaireTip(texte = "Adattare la lubrificazione al materiale del piatto: l'acqua è adatta alla maggior parte dei piatti diamantati elettrodepositati, mentre alcuni piatti di lucidatura (in particolare lo stagno) danno risultati migliori con una lubrificazione a base d'olio o una miscela specifica — attenersi alle raccomandazioni del produttore del piatto."),
            LapidaireTip(texte = "Verificare la sensibilità termica e la sfaldatura della specie prima di tagliare: alcune pietre (topazio, kunzite) hanno una sfaldatura netta che può propagarsi sotto il calore o la pressione di una sgrossatura troppo aggressiva."),
            LapidaireTip(texte = "Verificare la simmetria finale osservando la pietra frontalmente sotto una sorgente di luce puntiforme: i riflessi delle sfaccettature della corona devono formare un motivo regolare; un'asimmetria visibile a occhio nudo a questo stadio non è più correggibile in fase di lucidatura."),
            LapidaireTip(texte = "Non appoggiare mai la pietra su un piatto fermo per poi avviarlo: mettere sempre il piatto in rotazione prima di presentargli la pietra, con un leggero movimento pendolare per evitare che resti ferma nello stesso punto, il che usurerebbe il disco in modo irregolare e la pietra in modo disomogeneo."),
            LapidaireTip(texte = "Una tavola che appare «spenta» mentre il contorno della pietra resta brillante segnala un effetto finestra: il padiglione è troppo poco profondo e lascia sfuggire la luce da sotto invece di rimandarla attraverso la tavola — correggere approfondendo l'angolo del padiglione, senza mai superare l'angolo di estinzione."),
            LapidaireTip(texte = "Sulle pietre allungate (ovale, marquise, a pera, cuscino...), una croce scura visibile al centro (il cosiddetto effetto «X+») rivela sfaccettature di lunghezza e larghezza mal raccordate sul padiglione; un diagramma di taglio ben studiato per la forma scelta lo evita molto meglio di un aggiustamento caso per caso.")
        ),
        conservationTitle = "Conservazione e manipolazione delle pietre tagliate",
        conservationIntro = "Una pietra ben tagliata resta vulnerabile una volta montata o conservata: poche precauzioni semplici evitano la maggior parte dei graffi e dei danni accidentali.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Pulizia",
                conseil = "Non asciugare mai una pietra a secco: la polvere agisce come un abrasivo e graffia la superficie lucidata. Lavare con acqua tiepida e sapone e una spazzola morbida, oppure con alcol per uno sgrassaggio rapido, poi asciugare con un panno che non lasci pelucchi."
            ),
            LapidaireConservationTip(
                titre = "Conservazione individuale",
                conseil = "Conservare ogni pietra separatamente, in una piega di carta o in una bustina individuale: il contatto tra pietre, anche breve, le graffia a vicenda."
            ),
            LapidaireConservationTip(
                titre = "Non mescolare mai le durezze",
                conseil = "Non riunire mai nello stesso scomparto pietre di durezza diversa: la più dura graffia sistematicamente la più tenera, anche per un semplice sfregamento durante il trasporto."
            ),
            LapidaireConservationTip(
                titre = "Sbalzi termici e prodotti chimici",
                conseil = "Evitare bruschi sbalzi di temperatura e prodotti chimici aggressivi (candeggina, acidi), che possono fessurare alcune pietre o alterare un trattamento (smeraldo oliato, pietra impregnata). Il pulitore a ultrasuoni è da evitare per pietre fratturate, oliate o fragili."
            ),
            LapidaireConservationTip(
                titre = "Esposizione alla luce",
                conseil = "Alcune pietre sono fotosensibili: l'ametista o la kunzite esposte a lungo a luce intensa possono sbiadire. Conservare le pietre sensibili al riparo da un'esposizione prolungata."
            ),
            LapidaireConservationTip(
                titre = "Trasporto e manipolazione",
                conseil = "Per il trasporto, usare una bustina imbottita ed evitare il contatto diretto tra più pietre o gioielli riuniti insieme; maneggiare dalla montatura o dal tavolo piuttosto che dal padiglione, più vulnerabile agli urti."
            )
        ),
        disclaimerTitle = "Un mestiere che si impara in laboratorio",
        disclaimerBody = "Questa scheda presenta riferimenti generali, non un manuale completo: la sfaccettatura si impara con la pratica seguita da un istruttore, con attrezzatura adeguata e norme di sicurezza (protezione degli occhi e delle vie respiratorie, raffreddamento continuo del piatto) proprie di ogni laboratorio e di ogni macchina. I diagrammi mostrati provengono da fonti reali e libere da diritti (vedi crediti); in loro assenza temporanea, viene mostrata solo la didascalia.",
        machinesTypesTitle = "Le macchine del mestiere",
        machinesTypesIntro = "Oltre ai componenti di una macchina sfaccettatrice descritti sopra, ecco una panoramica dei diversi tipi di macchine realmente utilizzate in laboratorio, sia per la sfaccettatura sia per il cabochon, con le loro caratteristiche e la tecnica associata. I marchi citati lo sono a titolo di esempio rappresentativo della loro categoria, senza alcun legame commerciale con Gems of Rod.",
        categorieFacettageLabel = "Sfaccettatura",
        categorieCabochonLabel = "Cabochon",
        categoriePolyvalentLabel = "Polivalente",
        caracteristiquesLabel = "Caratteristiche",
        techniqueLabel = "Tecnica",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Braccio manuale a calibro (jam-peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Lo strumento di sfaccettatura più semplice e antico: la pietra è incollata all'estremità di un'asta tenuta a mano contro la mola, con l'angolo regolato a occhio o con un calibro semplice. Ancora insegnato nelle scuole tradizionali di taglio (Sri Lanka, Thailandia).",
                caracteristiques = listOf(
                    "Nessun componente meccanico di indicizzazione",
                    "Costo quasi nullo",
                    "Dipende interamente dall'abilità del tagliatore",
                    "Ritmo di lavoro rapido in mani esperte"
                ),
                technique = "La mano del tagliatore regola in continuo l'angolo e la pressione contro il disco; la precisione delle sfaccettature dipende dalla ripetizione del gesto, non dalla macchina."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Sfaccettatrice con testa d'indice meccanica amovibile",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Standard degli laboratori occidentali dagli anni '70 (Facetron, Ultra Tec, Poly-Metric...): una testa d'indice intercambiabile regola l'angolo e la rotazione della pietra al decimo di grado, con il braccio che scorre verticalmente per regolare la profondità di taglio.",
                caracteristiques = listOf(
                    "Precisione angolare al decimo di grado",
                    "Teste d'indice intercambiabili (sfaccettature, scaglie, cavetto)",
                    "Fermo di profondità micrometrico",
                    "Investimento significativo (macchina + mole)"
                ),
                technique = "Il tagliatore imposta l'angolo e la tacca d'indice prima di ogni sfaccettatura, poi abbassa la pietra contro la mola fino al fermo impostato: la riproducibilità sostituisce il gesto libero del braccio manuale."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Sfaccettatrice con testa d'indice fissa integrata",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Versione più economica in cui la testa d'indice è solidale al braccio anziché intercambiabile, con un numero di tacche incise fissato in fabbrica (spesso 64, 96 o 120 a seconda del modello).",
                caracteristiques = listOf(
                    "Prezzo d'ingresso nettamente inferiore alle teste amovibili",
                    "Numero di tacche d'indice limitato e non modificabile",
                    "Buona robustezza per un uso regolare",
                    "Adatta all'apprendimento e ai tagli comuni"
                ),
                technique = "Stesso principio della testa amovibile, ma la scelta dei motivi di sfaccettatura si limita alle divisioni d'indice incise sulla macchina — sufficiente per la maggior parte dei tagli classici (tondo, ovale, cuscino)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Sfaccettatrice a controllo numerico (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Macchina a controllo digitale che riproduce un piano di taglio (diagramma di angoli e indici) in modo identico su una serie di pietre, utilizzata nella produzione industriale o per tagli fantasia molto complessi.",
                caracteristiques = listOf(
                    "Riproducibilità perfetta da una pietra all'altra",
                    "Programmazione a partire da un piano di taglio digitale",
                    "Investimento elevato, riservato alla produzione in volume",
                    "Riduce la componente di abilità manuale nel risultato finale"
                ),
                technique = "Il piano di taglio viene caricato nel software della macchina, che esegue automaticamente gli angoli e gli indici programmati; il ruolo del lapidario si sposta verso la regolazione e il controllo qualità."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Cabochonatrice a mole multiple in linea",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Attrezzatura di riferimento per il cabochon (tipo Genie, CabKing): da 6 a 8 mole diamantate a grana decrescente seguite da dischi in feltro per la lucidatura, allineate su un unico telaio con irrigazione continua.",
                caracteristiques = listOf(
                    "Da 6 a 8 postazioni a grana decrescente sullo stesso albero",
                    "Irrigazione continua di ogni mola",
                    "Passaggio rapido da una postazione all'altra senza cambiare disco",
                    "Prezzo elevato ma ottima longevità"
                ),
                technique = "Il cabochon viene sgrossato e poi rifinito passando da una mola all'altra a grana decrescente, fino ai feltri di lucidatura finali — ogni postazione elimina le micrograffiature lasciate dalla precedente."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Cabochonatrice Vevor (multi-mole, entry level)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versione economica della cabochonatrice a mole multiple, prodotta in Cina con il marchio Vevor: molto diffusa tra principianti e piccoli laboratori grazie a un prezzo nettamente inferiore ai marchi specializzati (Genie, CabKing).",
                caracteristiques = listOf(
                    "Da 6 a 8 mole diamantate + feltri sullo stesso albero, come nei modelli professionali",
                    "Motore e cuscinetti entry level, tolleranze meccaniche più ampie",
                    "Vaschetta di recupero dell'acqua integrata",
                    "Prezzo nettamente inferiore ai marchi specializzati, a costo di una durata di vita più breve"
                ),
                technique = "Stesso principio di progressione mola per mola di una cabochonatrice professionale, ma con più vibrazioni: una pressione di lavoro più leggera e una manutenzione più frequente dei cuscinetti compensano la meccanica meno precisa."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Smerigliatrice-lucidatrice combinata ad albero orizzontale",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versione più artigianale: uno o due alberi orizzontali su cui si montano da soli i dischi (mole, feltri) a seconda della necessità, invece di una linea di postazioni fisse.",
                caracteristiques = listOf(
                    "Dischi intercambiabili a scelta del lapidario",
                    "Usata anche per sgrossare un grezzo prima del taglio",
                    "Meno costosa di una cabochonatrice a mole multiple dedicata",
                    "Richiede più manipolazioni tra le fasi"
                ),
                technique = "Il lapidario cambia da solo il disco montato sull'albero a ogni fase di grana, a differenza della cabochonatrice a mole multiple dove le postazioni sono fisse e affiancate."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Sega da taglio (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Piccola sega circolare con lama diamantata, utilizzata a monte del cabochon per tagliare il grezzo in fette (slab) dello spessore desiderato prima della modellatura sulle mole.",
                caracteristiques = listOf(
                    "Lama diamantata raffreddata a bagno d'olio o d'acqua",
                    "Diametro comune da 10 a 25 cm a seconda del modello",
                    "Guida di taglio regolabile per fette regolari",
                    "Fase preliminare indispensabile, non è taglio a sfaccettature"
                ),
                technique = "Il grezzo viene fatto avanzare manualmente contro la lama in rotazione lenta; lo spessore della fetta ottenuta determina direttamente lo spessore massimo del futuro cabochon."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Trapano per gemme",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Trapano a colonna dotato di punte diamantate cave e di un sistema di irrigazione, utilizzato per forare perle e cabochon destinati a essere montati come pendenti o infilati.",
                caracteristiques = listOf(
                    "Punte diamantate cave di diversi diametri",
                    "Raffreddamento ad acqua obbligatorio per evitare la fessurazione",
                    "Velocità di rotazione regolabile secondo la durezza della pietra",
                    "Foratura in due fasi (dai due lati) sulle pietre fragili"
                ),
                technique = "La foratura avviene a bassa velocità e sotto irrigazione costante, spesso attaccando la pietra da entrambi i lati per evitare la scheggiatura di uscita tipica di una foratura passante in un solo passaggio."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Smerigliatrice combinata sfaccettatura/cabochon entry level",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Piccola macchina economica che associa mole e feltri sullo stesso albero per iniziarsi sia al cabochon sia a una sfaccettatura sommaria, senza testa d'indice precisa.",
                caracteristiques = listOf(
                    "Prezzo d'accesso molto basso, formato compatto",
                    "Combina più usi in un'unica macchina",
                    "Assenza di testa d'indice precisa per una vera sfaccettatura",
                    "Adatta alla scoperta, limitata per un risultato professionale"
                ),
                technique = "Il principiante sperimenta entrambe le discipline sullo stesso telaio, a costo di una precisione angolare e d'indice nettamente inferiore a quella di una macchina specializzata."
            )
        )
    )

    private val de = LapidairePage(
        intro = "Der Lapidar formt und poliert Rohsteine zu geschliffenen Edelsteinen. Das Handwerk umfasst mehrere Spezialgebiete — den Facettenschliff für transparente Steine, den Cabochonschliff für opake oder durchscheinende Steine, Gravur und ornamentale Arbeiten —, doch der Facettenschliff, der technisch anspruchsvollste, ist es, der einem Stein sein Lichtspiel verleiht. Dieses Merkblatt stellt die Grundausstattung und die wichtigsten Kennwerte des Handwerks vor; es richtet sich an Fachleute und erfahrene Amateure, nicht an einen unbetreuten Erstversuch.",
        machinesTitle = "Die Facettiermaschine",
        machinesIntro = "Eine Facettiermaschine (faceting machine) hält den Stein in einem präzisen Winkel und Index gegen eine rotierende Schleifscheibe (den Lap). Ihre mechanische Präzision — auf ein Zehntel Grad beim Winkel, auf den Punkt genau beim Index — unterscheidet einen professionellen Schliff von einfachem freihändigem Schleifen. Die Ausstattung reicht vom einfachen Stab mit Cheater (handwerklich, schnell, aber wenig präzise) über Maschinen mit mechanischem Indexkopf (abnehmbar oder fest, präzise, aber langsamer in der Fertigung) bis zu computergesteuerten Modellen für die Serienfertigung.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mast",
                description = "Senkrechte Säule am Maschinengestell, an der sich der Kopf mit der Pinole verschiebt und dreht. Ihre Höhe und einstellbare Neigung bestimmen zusammen mit dem Arm den auf den Lap angewendeten Facettierwinkel."
            ),
            LapidaireComponent(
                nom = "Pinole (quill)",
                description = "Rohr, in dem sich der Stab mit dem Stein (der Dop) verschiebt. Die Pinole dreht sich, um nacheinander jede vorgesehene Facette der Scheibe zuzuwenden, und schiebt sich vor, um den Stein nachzuführen, während Material abgetragen wird."
            ),
            LapidaireComponent(
                nom = "Indexrad (index gear)",
                description = "Gezahntes Rad am Ende der Pinole, üblicherweise bei den meisten Herstellern in mehreren Abstufungen erhältlich — 64, 72, 80, 96 oder 120 Rasten —, um jede Schliffart abzudecken; eine „Schnellindex“-Scheibe mit weniger Rasten wird oft über den normalen Index gelegt, um die Positionierung bei einer Serienfertigung zu beschleunigen. Das Feststellen der Steindrehung an einer präzisen Position für jede Facette ist unerlässlich für die Symmetrie eines Schliffs wie des runden Brillanten, dessen Facetten 8-zählig symmetrisch angeordnet sind. Der gewählte Index muss ein exaktes Vielfaches der zu schleifenden Seitenzahl sein: Ein Fünfeck etwa ist mit einem 96er-Index unmöglich (96 ÷ 5 ergibt keine ganze Zahl), lässt sich aber mit einem 80er-Index problemlos schleifen (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Feinjustierring, der dem am Arm angezeigten Winkel überlagert wird. Sein Hauptzweck ist nicht, den Schliffwinkel selbst zu ändern, sondern die Parallelität zwischen der gerade geschliffenen Facette und dem Lap wiederherzustellen, wenn dieser sich leicht verzogen oder ungleichmäßig abgenutzt hat — eine Einstellung, die immer dann neu vorzunehmen ist, wenn eine Facette nicht mehr gleichmäßig über ihre gesamte Fläche poliert."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Ein Stab (Holz, Aluminium oder Messing — die beiden letzteren halten die Vorwärmwärme besser), auf den der Stein befestigt wird, bevor er in die Pinole eingesetzt wird. Die Befestigung erfolgt mit erhitztem Wachs (am gebräuchlichsten, schmilzt bei rund 80 °C), Cyanacrylat-Kleber („Sekundenkleber“, schnell abbindend, aber schwer zu entfernende Rückstände) oder Epoxidharz (guter Halt, doch dessen exotherme Reaktion ist bei hitzeempfindlichen Steinen zu beachten). Ausgefeiltere Dops tragen einen Stift, einen Nocken oder eine Nut, die sie stets gleich im Indexkopf ausrichten — unerlässlich, um den Stein ohne Dezentrierung oder unerwünschte Drehung von einem Dop auf einen anderen zu übertragen (Krone nach dem Pavillon schleifen)."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Rotierende Metallscheibe, meist 6\" (≈152 mm) oder 8\" (≈203 mm) im Durchmesser, auf der das Schleifmittel befestigt oder aufgetragen ist. Man unterscheidet Schleif-Laps (oberflächlich galvanisch aufgebrachter Diamant, oder Gusseisen, in das Diamantpartikel eingewalzt wurden) von Polier-Laps aus weicherem Material. Eine Maschine verfügt in der Regel über mehrere austauschbare Laps, einen pro Körnungsstufe."
            ),
            LapidaireComponent(
                nom = "Goniometer",
                description = "Kontrollinstrument, das den tatsächlichen Winkel eines bereits geschliffenen Steins an zwei diametral gegenüberliegenden Hauptfacetten misst, um den erzielten Schliff zu überprüfen. Der gemessene Winkel (C' für die Krone, P' für den Pavillon) wird mit der Formel Winkel = (180° − gemessener Winkel) / 2 umgerechnet, da das Winkelmessgerät den Ergänzungswinkel zur Rondistenebene abliest."
            )
        ),
        disquesTitle = "Form- und Polierscheiben",
        disquesIntro = "Der Schliff schreitet in Stufen zunehmend feinerer Körnung voran, wobei jede die von der vorherigen hinterlassenen Mikrokratzer entfernt; das Überspringen einer Stufe hinterlässt Spuren, die die Endpolitur nicht mehr beseitigen kann. Die Drehzahl des Laps folgt derselben Abstufung: eher langsam beim Grobschliff (100 bis 300 U/min), schneller bei der Facettenformung (300 bis 600 U/min) und am schnellsten beim Polieren (700 bis 1000 U/min oder mehr) — stets abhängig von der Empfindlichkeit des Steins, bei weichen oder spaltbaren Arten ohne Zögern verlangsamt.",
        disques = listOf(
            LapidaireDisc(
                nom = "Grobschliff",
                grain = "260 bis 325",
                usage = "Trägt schnell Material ab, um dem Stein aus dem Rohmaterial oder einer gesägten Scheibe seine grobe Form (Preform) zu geben. Aggressiver Diamant-Lap, hoher Anpressdruck, durchgehende Wasserkühlung erforderlich."
            ),
            LapidaireDisc(
                nom = "Facettenformung",
                grain = "600 bis 1200",
                usage = "Schleift und verfeinert jede Facette im exakten Winkel und Index, den das Schliffdiagramm vorgibt. Die längste und für Symmetrie und Präzision der Treffpunkte (meet points) benachbarter Facetten entscheidendste Stufe."
            ),
            LapidaireDisc(
                nom = "Vorpolitur",
                grain = "3000 bis 8000",
                usage = "Verfeinert die von der Formung hinterlassene Oberfläche bis zu einem fast matten, aber kratzerfreien Aussehen, bei exakt gleichbleibenden Winkeln — jede Winkelkorrektur in dieser Stufe muss minimal bleiben, sonst muss auf eine gröbere Körnung zurückgegangen werden."
            ),
            LapidaireDisc(
                nom = "Endpolitur",
                grain = "Submikron-Paste oder -Pulver (oft Ceroxid oder sehr feiner Diamant)",
                usage = "Der Polier-Lap wird nach Härte (Mohs-Skala) gewählt, vom härtesten zum weichsten: Keramik (9), Kupfer (3), Zink (2,5), Phenolharz (2,2), Plexiglas oder Blei-Zinn (2), reines Zinn (1,7), Blei (1,5), PVC (1) sowie Wachs mit verschiedenen Füllstoffen (0,3, je nach Rezeptur variabel). Nie poliert der Lap selbst, sondern nur das darauf aufgetragene Pulver, das als Schleifmittel wirkt — der Lap ist nur ein Träger, dessen Härte zur Härte des Steins passen muss. Trockenpolitur (Pulver durch ein fettiges Bindemittel fixiert) oder Nasspolitur (Pulver in Wasser verdünnt, kontinuierlich aufgetragen), je nach gewählter Methode."
            )
        ),
        indexTitle = "Wahl des Indexrads",
        indexIntro = "Ein Indexrad wird vor allem nach der Anzahl der zu schleifenden Seiten gewählt: Es muss ein exaktes Vielfaches dieser Zahl sein (ein Fünfeck ist mit einem 96er-Index unmöglich, aber mit einem 80er-Index machbar). Die Drehung pro Raste ist umgekehrt proportional zur Anzahl der Rasten des Index (360° geteilt durch die Rastenzahl) — ein 120er-Index rückt 3° pro Raste vor, ein 32er-Index 11,25°. Ein Schliffschema von einem Index auf einen anderen umzurechnen ist nur möglich, wenn beide Indizes Vielfache oder Teiler voneinander sind: Man teilt oder multipliziert dann einfach jede Indexnummer mit ihrem Verhältnis (zum Beispiel durch 3, um von einem 96er- auf einen 32er-Index zu wechseln). Da alle gängigen Indizes Vielfache von 4 sind, lässt sich ein Quadrat auf jedem von ihnen schleifen; dagegen ist es unmöglich, auf eine Position zwischen zwei Rasten zu indexieren, was die meisten Polygone mit ungerader Seitenzahl von vornherein ausschließt. Der 96er-Index bleibt bei Herstellern am weitesten verbreitet und bietet einen guten Kompromiss zwischen feiner Abstufung und schnellem Indexieren; der 120er-Index ist der vollständigste, allerdings auf Kosten eines langsameren Indexierens.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / Raste", cotesTaillables = "Quadrat (4), Achteck (8), Sechzehneck (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / Raste", cotesTaillables = "Dreieck (3), Quadrat (4), Fünfeck (5), Sechseck (6), Zehneck (10), Zwölfeck (12), Fünfzehneck (15), Zwanzigeck (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / Raste", cotesTaillables = "Quadrat (4), Achteck (8), Sechzehneck (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / Raste", cotesTaillables = "Dreieck (3), Quadrat (4), Sechseck (6), Achteck (8), Neuneck (9), Zwölfeck (12), Achtzehneck (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / Raste", cotesTaillables = "Quadrat (4), Fünfeck (5), Achteck (8), Zehneck (10), Sechzehneck (16), Zwanzigeck (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / Raste", cotesTaillables = "Der gängigste — insgesamt 9 schleifbare regelmäßige Vielecke, darunter Quadrat, Sechseck, Achteck, Zwölfeck"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / Raste", cotesTaillables = "Der vollständigste — insgesamt 14 schleifbare regelmäßige Vielecke, das Maximum unter den gängigen Indizes"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / Raste", cotesTaillables = "Quadrat (4), Achteck (8), Sechzehneck (16), 32 Seiten, 64 Seiten")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Geschätztes Gewicht",
            intro = "Bei einem bereits geschliffenen und gefassten Stein lässt sich das Gewicht nicht direkt messen: Es wird aus dem Volumen abgeleitet, anhand von Messschieber-Messungen (Genauigkeit 1/100 mm) und dem spezifischen Gewicht der Art. Eine Richtformel mit einer Genauigkeit von etwa 10-15 % — kein echtes Wiegen.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Rund",
                LapidaireCutShape.OVALE to "Oval",
                LapidaireCutShape.COUSSIN_CARRE to "Quadratisches Kissen",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Rechteckiges Kissen",
                LapidaireCutShape.CARRE_A_GRADIN to "Quadratischer Stufenschliff",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rechteckiger Stufenschliff",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Quadratisches Kissen (Stufenschliff)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Rechteckiges Kissen (Stufenschliff)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Tropfen",
                LapidaireCutShape.TRIANGLE_BOMBE to "Gewölbtes Dreieck",
                LapidaireCutShape.TRIANGLE to "Dreieck",
                LapidaireCutShape.TRAPEZE to "Trapez",
                LapidaireCutShape.COEUR to "Herz"
            ),
            dimension1Label = "Länge oder Durchmesser (mm)",
            dimension2Label = "Breite (mm)",
            heightLabel = "Gesamttiefe, Tafel bis Kulasse (mm)",
            sgLabel = "Spezifisches Gewicht der Art",
            computeLabel = "Berechnen",
            resultLabel = "Geschätztes Gewicht: %s Karat",
            disclaimer = "Schätzung über das Volumen, Richtgenauigkeit 10-15 % — ersetzt kein echtes Wiegen.",
            errorMessage = "Bitte gültige Werte (positive Zahlen) für alle Abmessungen und das spezifische Gewicht eingeben."
        ),
        anglesTitle = "Referenzwinkel: der runde Brillant",
        anglesIntro = "Die folgenden Proportionen wurden 1919 vom belgischen Mathematiker Marcel Tolkowsky veröffentlicht, der den optimalen Winkel berechnete, um Lichtrückwurf (Brillanz) und Dispersion (Feuer) eines rund geschliffenen Brillanten zu maximieren — der historische Bezugspunkt, der noch heute als Ausgangswert dient, auch wenn moderne Labore (insbesondere das GIA) statt einer einzelnen Zahl einen Toleranzbereich um diese Werte akzeptieren. Bei Farbsteinen, deren Brechungsindex sich vom Diamanten unterscheidet, liegt das klassische Verhältnis von Krone zu Rondiste eher zwischen 25/75 % und 30/70 %. Zwingende Regel, unabhängig von der Steinart: Niemals eine Pavillonfacette unterhalb des kritischen Winkels der Art schleifen (das Licht entweicht dann durch den Pavillon, „Fenster“-Effekt), noch oberhalb des Auslöschungswinkels (dunkle Zonen). Zur Kontrolle eines bereits geschliffenen Steins misst ein Goniometer den Winkel an zwei gegenüberliegenden Hauptfacetten: Der tatsächliche Kronen- oder Pavillonwinkel ergibt sich aus der Ablesung über die Formel (180° − gemessener Winkel) / 2. Andere Schliffformen (Princess, Oval, Tropfen, Smaragd, Kissen...) folgen jeweils einem eigenen Diagramm, das je nach Entwickler und Zielsetzung (Materialausbeute gegenüber optischer Leistung) stark variiert: Ohne Bezug auf ein konkretes Diagramm gibt es keinen vergleichbaren, allgemeingültigen Wert.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Runder Brillant (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53 % des Rondistendurchmessers",
                facettes = "57 oder 58 (33 an der Krone, 24 oder 25 am Pavillon, 8-zählige Symmetrie)",
                source = "Marcel Tolkowsky, „Diamond Design“, 1919"
            )
        ),
        optiqueTitle = "Der kritische Winkel und der Auslöschungswinkel",
        optiqueIntro = "Grob lassen sich drei Fälle für den Weg des durch die Tafel eintretenden Lichts unterscheiden: Ein zu flacher Pavillon lässt es direkt unten entweichen; ein zu tiefer Pavillon fängt es in inneren Reflexionen ein, die über eine seitliche Facette austreten, ohne zum Auge zurückzukehren, wodurch der Stein trotz perfekter Politur matt wirkt; ein gut proportionierter Pavillon lenkt es dagegen durch die Krone zum Betrachter zurück, für maximale Brillanz. Der Pavillon eines geschliffenen Steins wirkt wie ein Spiegel: Unterhalb des kritischen Winkels der Art entweicht durch die Tafel eintretendes Licht durch den Pavillon (der oben bereits behandelte „Fenster“-Effekt); genau am kritischen Winkel geschliffen, tritt ein anderer Fehler auf, das „Fischauge“ — die Tafel wirkt matt, während die Kronenkontur weiterhin glänzt, weil die sie durchquerenden Strahlen die Wand des Reflexionskegels streifen, ohne tatsächlich reflektiert zu werden. Es gibt außerdem eine obere Grenze, den Auslöschungswinkel, jenseits derer der Pavillon wieder Licht über die gegenüberliegende Facette verliert: Auslöschungswinkel = 60° − (kritischer Winkel / 3). Ein gut geschliffener Pavillon hält sich daher an kritischer Winkel < Pavillonwinkel < Auslöschungswinkel; je höher der Brechungsindex der Gemme, desto breiter wird dieser Arbeitsbereich — ein Diamant verzeiht deutlich mehr Abweichung als ein Fluorit. Die Krone folgt einer ergänzenden Regel: Ihr Maximalwinkel ist umgekehrt proportional zum Pavillonwinkel (ein kürzerer Pavillon erlaubt eine höhere Krone) und direkt proportional zum Brechungsindex; einige Zehntelgrad unterhalb dieser Maxima zu arbeiten, bleibt die sicherste Wahl. All diese Schliffdiagramme beruhen auf der Meetpoint-Technik (amerikanische Methode, Long & Steele), die die Schnittpunkte von drei oder mehr Facetten als Bezugspunkte nutzt, um automatisch gute Proportionen und eine gute Gewichtserhaltung gegenüber dem Rohstein zu sichern; um das Gewicht eines bereits geschliffenen, noch gefassten Steins zu schätzen, gilt üblicherweise die Formel Gewicht (in Karat) = Breite³ × Volumenkoeffizient des Schliffs × spezifisches Gewicht der Art / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamant", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Titanit (Sphen)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Zirkon (hoch)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Demantoid-Granat", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alexandrit (Chrysoberyll)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Rubin und Saphir (Korund)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Spinell", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Peridot", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Turmalin", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topas", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Beryll", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Smaragd", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Aquamarin", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Amethyst und Quarz", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calcit", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluorit", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Häufige Fehler und Korrekturen",
        defautsIntro = "Die meisten Schliffehler entstehen eher durch einen falsch eingestellten Winkel, eine fehlerhafte Indexierung oder einen schlecht ausgerichteten Dop-Transfer als durch ein Problem des Laps. Die Grundregel zur Korrektur eines schlechten Treffpunkts oder einer ungleichmäßigen Politur: immer auf der dem Fehler gegenüberliegenden Seite ansetzen.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Facette an einer Seite dezentriert oder schlecht poliert",
                cause = "Leichte Fehlausrichtung des Steins am Cheater.",
                remede = "Den Cheater drehen, um den Stein zur dem Fehler gegenüberliegenden Seite zu neigen."
            ),
            LapidaireDefaut(
                probleme = "Facette trifft oben schlecht oder poliert dort schlecht, Winkel zu groß",
                cause = "Der am Winkelmesser angezeigte Winkel ist höher als er sein sollte.",
                remede = "Den Winkel am Winkelmesser leicht verringern, um den Stein nach unten zu neigen."
            ),
            LapidaireDefaut(
                probleme = "Facette trifft unten schlecht oder poliert dort schlecht, Winkel zu klein",
                cause = "Der am Winkelmesser angezeigte Winkel ist niedriger als er sein sollte.",
                remede = "Den Winkel am Winkelmesser leicht erhöhen, um den Stein nach oben zu neigen."
            ),
            LapidaireDefaut(
                probleme = "Kombinierter diagonaler Fehler (zum Beispiel oben links)",
                cause = "Der Stein könnte sich beim Transfer leicht auf dem Dop verdreht haben.",
                remede = "Eine Korrektur am Cheater mit einer am Winkel kombinieren, dabei bei Bedarf die Drehzahl des Laps verringern, bis die Kontrolle wieder gewonnen ist."
            ),
            LapidaireDefaut(
                probleme = "Der Stein gibt auf dem Polier-Lap ein schrilles Geräusch von sich",
                cause = "Die gerade bearbeitete Facette liegt nicht parallel zum Lap.",
                remede = "Anhalten und die Position des Steins korrigieren, bevor es weitergeht — ein zuverlässiges Warnzeichen, das niemals ignoriert werden sollte."
            ),
            LapidaireDefaut(
                probleme = "Parallele Kratzer, die mehrere Facetten durchziehen",
                cause = "Der Lap ist mit Körnern anderer Korngröße verunreinigt.",
                remede = "Den Lap dekontaminieren (Bürsten mit heißem Seifenwasser, bei Bedarf abschaben oder abrichten), bevor weitergearbeitet wird."
            ),
            LapidaireDefaut(
                probleme = "„Brand“-Spuren auf der Oberfläche einer Facette",
                cause = "Zu stationäre Politur mit zu hohem Druck, die eine örtliche Überhitzung verursacht.",
                remede = "Die Facette regelmäßig über den Lap führen, den Druck verringern, bei Bedarf einen Wassertropfen pro Sekunde hinzufügen."
            ),
            LapidaireDefaut(
                probleme = "Ein kleiner Hohlraum entsteht während des Schleifens",
                cause = "Ein oberflächennaher Einschluss wurde freigelegt.",
                remede = "Einschlüsse schon beim Grobschliff besser „aufzuräumen“ bleibt die beste Vorbeugung; ist das Loch erst entstanden, örtlich nachschleifen oder bei Bedarf einen kompletten Neuschliff in Betracht ziehen."
            )
        ),
        especesTitle = "Praktische Artensteckbriefe",
        especesIntro = "Über den bereits genannten kritischen Winkel und Auslöschungswinkel hinaus hat jede Art ihre eigenen Schleifgewohnheiten: Tafelausrichtung je nach Pleochroismus oder Spaltbarkeit, besondere Empfindlichkeiten sowie die Schleifscheibe und das Polierpulver, die die besten Ergebnisse liefern. Praxiserprobte Anhaltspunkte, keine universelle Regel — jeder Stein behält seine eigenen Besonderheiten (Einschlüsse, Zonierung, Zustand der Rohsteinoberfläche).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamant",
                orientation = "Isotrop: keine Ausrichtungsbeschränkung durch eine optische Achse.",
                fragilite = "Vollkommene Spaltbarkeit entlang 4 Oktaederflächen, von Schleifern gut verstanden und genutzt statt nur erduldet; die Pavillonspitze bleibt der stoßempfindlichste Punkt.",
                polissage = "Gusseisenscheibe (Scaife), mit Diamantpulver beladen — nur Diamant poliert Diamant."
            ),
            LapidaireEspeceFiche(
                pierre = "Korund (Rubin, Saphir)",
                orientation = "Keine Spaltbarkeit bei der Ausrichtung zu berücksichtigen.",
                fragilite = "Härte 9, unter den härtesten Facettensteinen: kaum besondere Vorsichtsmaßnahmen nötig.",
                polissage = "Ausgezeichnete Ergebnisse auf Kupferscheibe mit Diamantpulver."
            ),
            LapidaireEspeceFiche(
                pierre = "Beryll (Smaragd, Aquamarin, Morganit, Heliodor)",
                orientation = "Unvollkommene Spaltbarkeit, keine echte Ausrichtungsbeschränkung.",
                fragilite = "Besonders der Smaragd erfordert Aufmerksamkeit für den Jardin (Einschlüsse), der ihn sowohl beim Schleifen als auch beim Polieren zerbrechen lassen kann.",
                polissage = "Essigsäure im Kühlwasser verbessert das Polieren; Aluminium-, Cer- oder Zinnoxidpulver, oder Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Quarz (Amethyst, Citrin, Bergkristall, Rauchquarz)",
                orientation = "Keine Spaltbarkeit; Farbzonierung bei der Ausrichtung berücksichtigen.",
                fragilite = "Keine besondere Spaltbarkeits-Fragilität.",
                polissage = "Das Ergebnis birgt manchmal unerwartete Überraschungen ohne erkennbare Ursache."
            ),
            LapidaireEspeceFiche(
                pierre = "Topas",
                orientation = "Die Tafel um etwa 10° gegenüber der basalen Spaltebene versetzen, um sie nicht direkt in ihrer Achse freizulegen.",
                fragilite = "Klare Spaltbarkeit in nur einer Richtung (Basalebene) — leicht zu facettieren, sobald man sie vermeidet.",
                polissage = "Keine besondere Scheibenempfehlung über die allgemeinen Regeln hinaus."
            ),
            LapidaireEspeceFiche(
                pierre = "Turmalin",
                orientation = "Die Tafel vorzugsweise parallel zur optischen Achse schneiden; ein mehrfarbiger Stein trennt sich sauber an der Grenze zweier Farben — dort besser kleben als sägen.",
                fragilite = "Empfindlich gegenüber Hitze und Stößen beim Schleifen.",
                polissage = "Keine besondere Scheibenempfehlung über die allgemeinen Regeln hinaus."
            ),
            LapidaireEspeceFiche(
                pierre = "Granat (Almandin, Demantoid, Grossular, Pyrop, Spessartin, Uwarowit)",
                orientation = "Isotrop: Tafelausrichtung ohne Einschränkung, keine bevorzugte Richtung.",
                fragilite = "Kein Fragilitätsproblem durch Spaltbarkeit.",
                polissage = "Essigsäure (Essig) im Kühlwasser verbessert das Ergebnis durchgängig; Cer-, Zinn- oder Aluminiumoxidpulver."
            ),
            LapidaireEspeceFiche(
                pierre = "Spinell",
                orientation = "Isotrop, keine Ausrichtungsbeschränkung.",
                fragilite = "Kein Fragilitätsproblem durch Spaltbarkeit.",
                polissage = "Poliert sich bemerkenswert gut auf Kupferscheibe mit Diamantpaste."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridot (Olivin)",
                orientation = "Sehr schwacher Pleochroismus, wenig einschränkende Ausrichtung.",
                fragilite = "Keine nennenswerte Spaltbarkeits-Fragilität.",
                polissage = "Das Ergebnis birgt manchmal unerwartete Überraschungen; Diamantpulver eignet sich gut."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorit",
                orientation = "Trennt sich leicht entlang 4 Richtungen: die Ausrichtung sorgfältig wählen, um das Risiko zu begrenzen.",
                fragilite = "Einer der geschlossensten kritischen Winkel unter den Facettensteinen, sehr stoß- und absplitterungsempfindlich.",
                polissage = "Vorzugsweise Wachsscheibe; Säure verbessert das Ergebnis."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcit",
                orientation = "Sorgfältig ausrichten, um die Spaltebenen nicht direkt freizulegen.",
                fragilite = "Sehr geringe Härte (3) und vollkommene Spaltbarkeit in 3 Richtungen, stark hitzeempfindlich — ein Stein, der schwer zu schleifen und zu polieren ist; die Scheibe langsam drehen lassen (etwa 100 U/min).",
                polissage = "Holz- oder Wachsscheibe, Zinn- oder Chromoxidpulver, mit einigen Tropfen Oxalsäure."
            ),
            LapidaireEspeceFiche(
                pierre = "Chrysoberyll (Alexandrit)",
                orientation = "Keine Ausrichtungsbeschränkung durch Spaltbarkeit.",
                fragilite = "Hohe Härte (8,5), ein unproblematisch harter Stein.",
                polissage = "Poliert sich schnell mit Säure und Linde-A-Pulver; ausgezeichnete Ergebnisse auf Kupferscheibe mit Diamantpulver."
            )
        ),
        diagrammesTitle = "Diagramme",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Proportionsdiagramm des runden Brillanten: Nomenklatur von Krone, Rondiste und Pavillon."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagramm des Lichtwegs je nach Pavillontiefe: zu flach, gut proportioniert oder zu tief."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Alter Stich einer Diamantschleifmühle — die Mechanik der rotierenden Scheibe, im Prinzip seit Jahrhunderten unverändert."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Eine moderne Facettiermaschine: Mast, Pinole und Lap, wie oben beschrieben."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Stufen der Umwandlung eines Rohsteins in einen facettierten Edelstein."
            )
        ),
        tipsTitle = "Praktische Tipps",
        tips = listOf(
            LapidaireTip(texte = "Die am Arm angezeigte Winkelkalibrierung vor Beginn eines wertvollen Stücks stets mit einem Teststein prüfen: eine Abweichung von 0,5° am Pavillon reicht aus, um einen ansonsten gut geschliffenen Stein sichtbar zu verdunkeln."),
            LapidaireTip(texte = "Den Stein sorgfältig auf dem Dop zentrieren: Eine leichte Dezentrierung führt zu einer ungleichmäßigen Rondistendicke und zu Facetten, die sich nicht rundum am gleichen Punkt treffen."),
            LapidaireTip(texte = "Eine moderate Rondistendicke einhalten (weder eine stoßempfindliche „Rasierklinge“ noch übermäßig dick, was Material ohne optischen Nutzen einschließt) — eine regelmäßige, sichtbare Rondistenlinie, weder zu dünn noch zu dick, ist eines der Kennzeichen sorgfältiger Arbeit."),
            LapidaireTip(texte = "Die Treffpunkte (meet points) zwischen Facetten in jeder Stufe mit der Lupe im Streiflicht kontrollieren: Ein verfehlter Treffpunkt lässt sich während der Formung leicht korrigieren, kaum noch, sobald die Vorpolitur begonnen hat."),
            LapidaireTip(texte = "Die Schmierung an das Lap-Material anpassen: Wasser eignet sich für die meisten galvanisch gebundenen Diamant-Laps, während manche Polier-Laps (insbesondere Zinn) mit ölbasierter Schmierung oder einer speziellen Mischung bessere Ergebnisse liefern — die Empfehlungen des Lap-Herstellers befolgen."),
            LapidaireTip(texte = "Vor dem Schleifen Hitzeempfindlichkeit und Spaltbarkeit der Art prüfen: Manche Steine (Topas, Kunzit) besitzen eine ausgeprägte Spaltbarkeit, die sich unter der Hitze oder dem Druck eines zu aggressiven Grobschliffs fortsetzen kann."),
            LapidaireTip(texte = "Die endgültige Symmetrie prüfen, indem der Stein von vorn unter einer punktförmigen Lichtquelle betrachtet wird: Die Reflexe der Kronenfacetten sollten ein regelmäßiges Muster bilden; eine in dieser Stufe mit bloßem Auge sichtbare Asymmetrie lässt sich durch Polieren nicht mehr beheben."),
            LapidaireTip(texte = "Den Stein niemals auf einen stillstehenden Lap legen und dann erst starten: Den Lap stets zuerst in Drehung versetzen, bevor der Stein herangeführt wird, mit einer leichten Pendelbewegung, damit er nicht an derselben Stelle verharrt — sonst nutzt sich die Scheibe ungleichmäßig und der Stein unregelmäßig ab."),
            LapidaireTip(texte = "Eine Tafel, die „tot“ wirkt, während der Umriss des Steins glänzend bleibt, deutet auf einen Fenster-Effekt hin: Der Pavillon ist zu flach und lässt das Licht nach unten entweichen, statt es durch die Tafel zurückzuwerfen — korrigieren Sie dies, indem Sie den Pavillonwinkel vertiefen, jedoch nie über den Auslöschungswinkel hinaus."),
            LapidaireTip(texte = "Bei länglichen Formen (Oval, Navette, Tropfen, Kissen...) verrät ein dunkles Kreuz in der Mitte (der sogenannte „X+“-Effekt) am Pavillon schlecht aneinander angepasste Facetten in Länge und Breite; ein für die gewählte Form gut durchdachtes Schliffdiagramm vermeidet dies deutlich besser als eine Anpassung von Fall zu Fall.")
        ),
        conservationTitle = "Aufbewahrung und Handhabung geschliffener Steine",
        conservationIntro = "Ein gut geschliffener Stein bleibt auch gefasst oder gelagert empfindlich: ein paar einfache Vorsichtsmaßnahmen verhindern die meisten Kratzer und versehentlichen Schäden.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Reinigung",
                conseil = "Einen Stein nie trocken abwischen: Staub wirkt wie ein Schleifmittel und zerkratzt die polierte Oberfläche. Mit lauwarmem Seifenwasser und einer weichen Bürste waschen, oder mit Alkohol für eine schnelle Entfettung, dann mit einem fusselfreien Tuch trocknen."
            ),
            LapidaireConservationTip(
                titre = "Einzelne Aufbewahrung",
                conseil = "Jeden Stein einzeln aufbewahren, in einer Papierfalte oder einem Einzelbeutel: Schon kurzer Kontakt zwischen Steinen zerkratzt sie gegenseitig."
            ),
            LapidaireConservationTip(
                titre = "Härtegrade nie mischen",
                conseil = "Nie Steine unterschiedlicher Härte im selben Fach aufbewahren: Der härtere zerkratzt den weicheren systematisch, selbst durch einfache Reibung beim Transport."
            ),
            LapidaireConservationTip(
                titre = "Temperaturschocks und Chemikalien",
                conseil = "Plötzliche Temperaturwechsel und aggressive Chemikalien (Bleichmittel, Säuren) vermeiden, die manche Steine sprengen oder eine Behandlung beeinträchtigen können (geölter Smaragd, imprägnierter Stein). Ultraschallreiniger sollten bei rissigen, geölten oder empfindlichen Steinen vermieden werden."
            ),
            LapidaireConservationTip(
                titre = "Lichteinwirkung",
                conseil = "Manche Steine sind lichtempfindlich: Amethyst oder Kunzit können bei langer, intensiver Lichteinwirkung verblassen. Empfindliche Steine vor längerer Lichteinwirkung schützen."
            ),
            LapidaireConservationTip(
                titre = "Transport und Handhabung",
                conseil = "Für den Transport einen gepolsterten Beutel verwenden und direkten Kontakt zwischen mehreren zusammen aufbewahrten Steinen oder Schmuckstücken vermeiden; am Fassungsrand oder an der Tafel anfassen statt am Pavillon, der stoßempfindlicher ist."
            )
        ),
        disclaimerTitle = "Ein Handwerk, das man in der Werkstatt lernt",
        disclaimerBody = "Dieses Merkblatt zeigt allgemeine Anhaltspunkte, keine vollständige Anleitung: Der Facettenschliff wird durch angeleitete Praxis erlernt, mit geeigneter Ausrüstung und Sicherheitsvorschriften (Augen- und Atemschutz, durchgehende Lap-Kühlung), die für jede Werkstatt und jede Maschine spezifisch sind. Die gezeigten Diagramme stammen aus echten, frei lizenzierten Quellen (siehe Credits); solange sie vorübergehend fehlen, wird nur die Bildunterschrift angezeigt.",
        machinesTypesTitle = "Die Maschinen des Handwerks",
        machinesTypesIntro = "Über die oben beschriebenen Komponenten einer Facettiermaschine hinaus folgt hier ein Überblick über die verschiedenen Maschinentypen, die tatsächlich in der Werkstatt verwendet werden, sowohl für den Facettenschliff als auch für den Cabochon-Schliff, mit ihren Merkmalen und der zugehörigen Technik. Die genannten Marken dienen nur als repräsentatives Beispiel ihrer Kategorie, ohne kommerzielle Verbindung zu Gems of Rod.",
        categorieFacettageLabel = "Facettenschliff",
        categorieCabochonLabel = "Cabochon-Schliff",
        categoriePolyvalentLabel = "Vielseitig",
        caracteristiquesLabel = "Merkmale",
        techniqueLabel = "Technik",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Handstab mit Lehre (Jam-Peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Das einfachste und älteste Facettierwerkzeug: Der Stein wird an das Ende eines von Hand gegen die Schleifscheibe gehaltenen Stabs geklebt, wobei der Winkel nach Augenmaß oder mit einer einfachen Lehre eingestellt wird. Wird noch immer in traditionellen Schleifschulen (Sri Lanka, Thailand) gelehrt.",
                caracteristiques = listOf(
                    "Kein mechanisches Indexierungsteil",
                    "Praktisch keine Kosten",
                    "Hängt vollständig vom Können des Schleifers ab",
                    "Hohes Arbeitstempo in erfahrenen Händen"
                ),
                technique = "Die Hand des Schleifers passt Winkel und Anpressdruck gegen die Scheibe fortlaufend an; die Präzision der Facetten beruht auf der Wiederholung der Bewegung, nicht auf der Maschine."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Facettiermaschine mit abnehmbarem mechanischem Indexkopf",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Standard westlicher Werkstätten seit den 1970er-Jahren (Facetron, Ultra Tec, Poly-Metric...): Ein austauschbarer Indexkopf stellt Winkel und Drehung des Steins auf ein Zehntelgrad genau ein, während der Arm vertikal gleitet, um die Schnitttiefe einzustellen.",
                caracteristiques = listOf(
                    "Winkelgenauigkeit auf ein Zehntelgrad",
                    "Austauschbare Indexköpfe (Facetten, Schuppen, Cavetto)",
                    "Mikrometrischer Tiefenanschlag",
                    "Erhebliche Investition (Maschine + Schleifscheiben)"
                ),
                technique = "Der Schleifer stellt vor jeder Facette Winkel und Indexraste ein und senkt den Stein dann bis zum eingestellten Anschlag gegen die Schleifscheibe: Reproduzierbarkeit ersetzt die freie Handbewegung des manuellen Arms."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Facettiermaschine mit fest integriertem Indexkopf",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Erschwinglichere Version, bei der der Indexkopf fest mit dem Arm verbunden statt austauschbar ist, mit einer werkseitig festgelegten Anzahl gravierter Rasten (oft 64, 96 oder 120 je nach Modell).",
                caracteristiques = listOf(
                    "Einstiegspreis deutlich unter dem abnehmbarer Köpfe",
                    "Begrenzte, nicht veränderbare Anzahl an Indexrasten",
                    "Gute Robustheit für den regelmäßigen Einsatz",
                    "Geeignet für das Erlernen und gängige Schliffe"
                ),
                technique = "Gleiches Prinzip wie beim abnehmbaren Kopf, doch die Wahl der Facettenmuster beschränkt sich auf die auf der Maschine gravierten Indexteilungen — ausreichend für die meisten klassischen Schliffe (Rund-, Oval-, Kissenschliff)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Computergesteuerte Facettiermaschine (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Digital gesteuerte Maschine, die einen Schliffplan (Winkel- und Indexdiagramm) identisch auf eine Reihe von Steinen überträgt, eingesetzt in der industriellen Produktion oder für sehr komplexe Fantasieschliffe.",
                caracteristiques = listOf(
                    "Perfekte Reproduzierbarkeit von Stein zu Stein",
                    "Programmierung anhand eines digitalen Schliffplans",
                    "Hohe Investition, der Serienproduktion vorbehalten",
                    "Verringert den Anteil handwerklichen Könnens am Endergebnis"
                ),
                technique = "Der Schliffplan wird in die Maschinensoftware geladen, die die programmierten Winkel und Indexpositionen automatisch abarbeitet; die Rolle des Lapidärs verschiebt sich hin zu Einrichtung und Qualitätskontrolle."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Mehrscheiben-Cabochonschleifmaschine (Linienbauweise)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Referenzgerät für den Cabochon-Schliff (Typ Genie, CabKing): 6 bis 8 Diamantscheiben mit abnehmender Körnung, gefolgt von Filzscheiben zum Polieren, auf einem gemeinsamen Gestell mit durchgehender Wasserzufuhr angeordnet.",
                caracteristiques = listOf(
                    "6 bis 8 Stationen mit abnehmender Körnung auf derselben Welle",
                    "Durchgehende Wasserzufuhr an jeder Scheibe",
                    "Schneller Wechsel von einer Station zur nächsten ohne Scheibenwechsel",
                    "Hoher Preis, aber sehr gute Langlebigkeit"
                ),
                technique = "Der Cabochon wird grob vorgeformt und dann durch den Wechsel von einer Scheibe zur nächsten mit abnehmender Körnung bis zu den abschließenden Polierfilzen verfeinert — jede Station entfernt die Mikrokratzer der vorherigen."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Vevor-Cabochonschleifmaschine (Mehrscheiben, Einstiegsklasse)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Preisgünstige Ausführung der Mehrscheiben-Cabochonschleifmaschine, in China unter der Marke Vevor hergestellt: bei Einsteigern und kleinen Werkstätten weit verbreitet dank eines Preises deutlich unter dem spezialisierter Marken (Genie, CabKing).",
                caracteristiques = listOf(
                    "6 bis 8 Diamantscheiben + Filze auf derselben Welle wie bei Profimodellen",
                    "Motor und Lager der Einstiegsklasse, größere mechanische Toleranzen",
                    "Integrierte Wasserauffangwanne",
                    "Deutlich günstiger als spezialisierte Marken, dafür kürzere Lebensdauer"
                ),
                technique = "Gleiches Prinzip der Scheibe-für-Scheibe-Progression wie bei einer professionellen Cabochonschleifmaschine, jedoch mit mehr Vibrationen: geringerer Arbeitsdruck und häufigere Lagerwartung gleichen die weniger präzise Mechanik aus."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Kombinierte Schleif-Poliermaschine mit horizontaler Welle",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Handwerklichere Version: eine oder zwei horizontale Wellen, auf die je nach Bedarf selbst Scheiben (Schleifscheiben, Filze) montiert werden, statt einer Reihe fester Stationen.",
                caracteristiques = listOf(
                    "Vom Lapidär frei wählbare, austauschbare Scheiben",
                    "Auch zum Vorformen eines Rohsteins vor dem Sägen genutzt",
                    "Günstiger als eine spezielle Mehrscheiben-Cabochonschleifmaschine",
                    "Erfordert mehr Handgriffe zwischen den Schritten"
                ),
                technique = "Der Lapidär wechselt bei jedem Körnungsschritt selbst die auf der Welle montierte Scheibe, im Gegensatz zur Mehrscheibenmaschine, bei der die Stationen fest und nebeneinander angeordnet sind."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Trennsäge (Trim Saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Kleine Kreissäge mit Diamantblatt, die dem Cabochon-Schliff vorgelagert eingesetzt wird, um den Rohstein vor der Formgebung an den Schleifscheiben in Scheiben (Slabs) der gewünschten Dicke zu zerteilen.",
                caracteristiques = listOf(
                    "Diamantblatt, gekühlt durch Öl- oder Wasserbad",
                    "Gängiger Durchmesser von 10 bis 25 cm je nach Modell",
                    "Verstellbare Schnittführung für gleichmäßige Scheiben",
                    "Unverzichtbarer vorbereitender Schritt, kein Facettenschliff"
                ),
                technique = "Der Rohstein wird von Hand gegen das langsam rotierende Blatt geführt; die Dicke der entstehenden Scheibe bestimmt direkt die maximale Dicke des künftigen Cabochons."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Edelsteinbohrmaschine",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Ständerbohrmaschine mit hohlen Diamantbohrern und Wasserzufuhrsystem, eingesetzt zum Bohren von Perlen und Cabochons, die als Anhänger gefasst oder aufgefädelt werden sollen.",
                caracteristiques = listOf(
                    "Hohle Diamantbohrer verschiedener Durchmesser",
                    "Wasserkühlung zwingend erforderlich, um Rissbildung zu vermeiden",
                    "Je nach Steinhärte einstellbare Drehzahl",
                    "Zweiseitiges Bohren bei zerbrechlichen Steinen"
                ),
                technique = "Gebohrt wird mit niedriger Drehzahl unter ständiger Wasserzufuhr, häufig von beiden Seiten des Steins aus, um den für einen einseitigen Durchgangsbohrvorgang typischen Ausrisssplitter zu vermeiden."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Kombinierter Facettier-/Cabochon-Schleifbock (Einstiegsklasse)",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Kleine, preisgünstige Maschine, die Schleifscheiben und Filze auf derselben Welle vereint, um sich sowohl im Cabochon-Schliff als auch in einfachem Facettenschliff zu versuchen, ohne präzisen Indexkopf.",
                caracteristiques = listOf(
                    "Sehr niedriger Einstiegspreis, kompaktes Format",
                    "Vereint mehrere Anwendungen in einer Maschine",
                    "Kein präziser Indexkopf für echten Facettenschliff",
                    "Geeignet zum Ausprobieren, für ein professionelles Ergebnis begrenzt"
                ),
                technique = "Der Anfänger probiert beide Disziplinen auf demselben Gestell aus, allerdings mit einer Winkel- und Indexgenauigkeit, die deutlich unter der einer Spezialmaschine liegt."
            )
        )
    )

    private val pt = LapidairePage(
        intro = "O lapidário dá forma e polimento às pedras brutas, transformando-as em pedras lapidadas. Este ofício abrange várias especialidades — a lapidação de facetas (faceting) para pedras transparentes, o cabochão para pedras opacas ou translúcidas, a gravação e o trabalho ornamental —, mas a lapidação de facetas, a mais técnica, é a que confere a uma pedra o seu jogo de luz. Esta ficha apresenta o equipamento e as referências básicas do ofício; destina-se a profissionais e amadores experientes, não a uma primeira aprendizagem sem supervisão.",
        machinesTitle = "A máquina de facetar",
        machinesIntro = "Uma máquina de facetar (faceting machine) mantém a pedra num ângulo e índice precisos contra um disco abrasivo rotativo (o prato, ou lap). A sua precisão mecânica — a um décimo de grau no ângulo, ao ponto exato no índice — é o que distingue uma lapidação profissional de um simples desbaste à mão livre. O equipamento vai desde a simples vara com cheater (artesanal, rápida mas pouco precisa) até às máquinas com cabeça divisora mecânica (amovível ou fixa, precisas mas de produção mais lenta), passando pelos modelos controlados por computador para a lapidação em série.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mastro (mast)",
                description = "Coluna vertical fixada à estrutura da máquina, ao longo da qual desliza e roda a cabeça porta-cânula. A sua altura e inclinação regulável determinam, com o braço, o ângulo de facetamento aplicado sobre o prato."
            ),
            LapidaireComponent(
                nom = "Cânula (quill)",
                description = "Tubo no qual desliza a haste que sustenta a pedra (o dop). A cânula gira sobre si mesma para apresentar sucessivamente cada faceta prevista diante do disco, e desliza para avançar a pedra à medida que o material é removido."
            ),
            LapidaireComponent(
                nom = "Roda de índice (index gear)",
                description = "Roda dentada fixada na extremidade da cânula, geralmente disponível em várias graduações — 64, 72, 80, 96 ou 120 entalhes — para cobrir todos os estilos de lapidação; um disco de «índice rápido», com menos entalhes, sobrepõe-se muitas vezes ao índice normal para acelerar o posicionamento ao lapidar uma série de pedras. Travar a rotação da pedra numa posição precisa para cada faceta é condição indispensável para a simetria de uma lapidação como o brilhante redondo, que distribui as suas facetas com simetria de ordem 8. O índice escolhido deve ser um múltiplo exato do número de lados a lapidar: um pentágono, por exemplo, é impossível com um índice 96 (96 ÷ 5 não é um número inteiro) mas lapida-se sem problemas com um índice 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Anel de afinação fina sobreposto ao ângulo indicado no braço. A sua função principal não é alterar o ângulo de corte em si, mas restabelecer o paralelismo entre a faceta em curso e o prato quando este empenou ligeiramente ou desgastou de forma desigual — um ajuste a repetir sempre que uma faceta deixa de polir uniformemente em toda a sua superfície."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Haste (madeira, alumínio ou latão — estes dois últimos retêm melhor o calor do pré-aquecimento) na qual a pedra é fixada antes de ser inserida na cânula. A fixação faz-se com cera aquecida (a mais comum, funde por volta dos 80 °C), cola cianoacrilato («super cola», presa rápida mas com resíduos difíceis de remover) ou epóxi (boa fixação, mas a sua reação exotérmica deve ser vigiada em pedras sensíveis ao calor). Os dops mais sofisticados têm um pino, um ressalto ou uma ranhura que os orienta sempre da mesma forma no divisor — condição indispensável para transferir a pedra de um dop para outro (lapidar a coroa depois do pavilhão) sem desalinhamento nem rotação indesejada."
            ),
            LapidaireComponent(
                nom = "Prato (lap)",
                description = "Disco metálico rotativo, geralmente de 6\" (≈152 mm) ou 8\" (≈203 mm) de diâmetro, sobre o qual é fixado ou carregado o abrasivo. Distinguem-se os pratos para desbastar/lapidar (diamante eletrodepositado à superfície, ou ferro fundido impregnado de partículas de diamante por laminagem) dos pratos para polir, de material mais macio. Uma máquina dispõe geralmente de vários pratos intercambiáveis, um por cada etapa de grão."
            ),
            LapidaireComponent(
                nom = "Goniómetro",
                description = "Instrumento de controlo que mede o ângulo real de uma pedra já lapidada, sobre duas facetas principais diametralmente opostas, para verificar o corte obtido. O ângulo medido (C' para a coroa, P' para o pavilhão) converte-se pela fórmula ângulo = (180° − ângulo medido) / 2, uma vez que o goniómetro regista o ângulo suplementar formado com o plano da cintura."
            )
        ),
        disquesTitle = "Discos de formatação e polimento",
        disquesIntro = "A lapidação progride por etapas de grão cada vez mais fino, cada uma apagando os microrriscos deixados pela anterior; saltar uma etapa deixa marcas que o polimento final já não consegue corrigir. A velocidade de rotação do prato acompanha esta progressão: bastante lenta no desbaste (100 a 300 rpm), mais rápida na formatação das facetas (300 a 600 rpm), e a mais rápida no polimento (700 a 1000 rpm ou mais) — sempre em função da sensibilidade da pedra, reduzida sem hesitar para uma pedra macia ou clivável.",
        disques = listOf(
            LapidaireDisc(
                nom = "Desbaste",
                grain = "260 a 325",
                usage = "Remove rapidamente material para dar à pedra a sua forma geral (pré-forma) a partir do bruto ou de uma lâmina serrada. Prato diamantado agressivo, pressão firme, arrefecimento a água obrigatório."
            ),
            LapidaireDisc(
                nom = "Formatação das facetas",
                grain = "600 a 1200",
                usage = "Traça e afina cada faceta no ângulo e índice exatos previstos pelo diagrama de lapidação. É a etapa mais longa e a mais determinante para a simetria e a precisão dos pontos de encontro (meet points) entre facetas vizinhas."
            ),
            LapidaireDisc(
                nom = "Pré-polimento",
                grain = "3000 a 8000",
                usage = "Afina o acabamento deixado pela formatação até um aspeto quase mate mas sem riscos profundos, mantendo exatamente os mesmos ângulos — qualquer correção de ângulo nesta etapa deve ser mínima, sob pena de ser necessário recuar a um grão mais grosso."
            ),
            LapidaireDisc(
                nom = "Polimento final",
                grain = "Pasta ou pó submicrónico (frequentemente óxido de cério ou diamante muito fino)",
                usage = "O prato de polimento escolhe-se pela dureza (escala de Mohs), do mais duro ao mais macio: cerâmica (9), cobre (3), zinco (2,5), fenólico (2,2), plexiglas ou estanho-chumbo (2), estanho puro (1,7), chumbo (1,5), PVC (1) e cera com diversas cargas (0,3, variável consoante a fórmula). Nunca é o prato em si que polir, apenas o pó aplicado sobre ele que atua como agente abrasivo — o prato é apenas um suporte cuja dureza deve corresponder à da pedra. Polimento a seco (pó fixado por um ligante gordo) ou húmido (pó diluído em água, aplicado continuamente) consoante o método adotado."
            )
        ),
        indexTitle = "Escolha do índice",
        indexIntro = "Um índice escolhe-se sobretudo em função do número de lados a talhar: tem de ser um múltiplo exato desse número (um pentágono é impossível num índice 96, mas viável num índice 80). A rotação por entalhe é inversamente proporcional ao número de entalhes do índice (360° dividido pelo número de entalhes) — um índice 120 avança 3° por entalhe, um índice 32 avança 11,25°. Reconverter um esquema de talhe de um índice para outro só é possível se os dois índices forem múltiplos ou submúltiplos entre si: basta dividir ou multiplicar cada número de índice pela sua razão (dividir por 3, por exemplo, para passar de um índice 96 para um índice 32). Como todos os índices comuns são múltiplos de 4, um quadrado talha-se em qualquer um deles; em contrapartida, é impossível indexar numa posição intermédia entre dois entalhes, o que exclui à partida a maioria dos polígonos de lados ímpares. O índice 96 continua a ser o mais difundido entre os fabricantes, oferecendo um bom equilíbrio entre finura de graduação e rapidez de indexação; o índice 120 é o mais completo, ao custo de uma indexação mais lenta.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / entalhe", cotesTaillables = "Quadrado (4), octógono (8), hexadecágono (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / entalhe", cotesTaillables = "Triângulo (3), quadrado (4), pentágono (5), hexágono (6), decágono (10), dodecágono (12), pentadecágono (15), icoságono (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / entalhe", cotesTaillables = "Quadrado (4), octógono (8), hexadecágono (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / entalhe", cotesTaillables = "Triângulo (3), quadrado (4), hexágono (6), octógono (8), eneágono (9), dodecágono (12), octodecágono (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / entalhe", cotesTaillables = "Quadrado (4), pentágono (5), octógono (8), decágono (10), hexadecágono (16), icoságono (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / entalhe", cotesTaillables = "O mais comum — 9 polígonos regulares talháveis no total, incluindo quadrado, hexágono, octógono, dodecágono"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / entalhe", cotesTaillables = "O mais completo — 14 polígonos regulares talháveis no total, o máximo entre os índices comuns"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / entalhe", cotesTaillables = "Quadrado (4), octógono (8), hexadecágono (16), 32 lados, 64 lados")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Peso estimado",
            intro = "Para uma pedra já lapidada e montada, impossível pesar diretamente: o peso deduz-se do volume, a partir de medições com paquímetro (precisão de 1/100 mm) e do peso específico da espécie. Fórmula indicativa, precisão da ordem de 10-15% — não é uma pesagem real.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Redondo",
                LapidaireCutShape.OVALE to "Oval",
                LapidaireCutShape.COUSSIN_CARRE to "Almofada quadrada",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Almofada retangular",
                LapidaireCutShape.CARRE_A_GRADIN to "Quadrado em degraus",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Retângulo em degraus",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Almofada quadrada (em degraus)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Almofada retangular (em degraus)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Pera",
                LapidaireCutShape.TRIANGLE_BOMBE to "Triângulo bombeado",
                LapidaireCutShape.TRIANGLE to "Triângulo",
                LapidaireCutShape.TRAPEZE to "Trapézio",
                LapidaireCutShape.COEUR to "Coração"
            ),
            dimension1Label = "Comprimento ou diâmetro (mm)",
            dimension2Label = "Largura (mm)",
            heightLabel = "Profundidade total, mesa a culassa (mm)",
            sgLabel = "Peso específico da espécie",
            computeLabel = "Calcular",
            resultLabel = "Peso estimado: %s quilates",
            disclaimer = "Estimativa pelo volume, precisão indicativa de 10-15% — não substitui uma pesagem real.",
            errorMessage = "Introduza valores válidos (números positivos) para todas as dimensões e o peso específico."
        ),
        anglesTitle = "Ângulos de referência: o brilhante redondo",
        anglesIntro = "As proporções abaixo são as publicadas pelo matemático belga Marcel Tolkowsky em 1919, que calculou o ângulo ótimo para maximizar o retorno de luz (brilho) e a dispersão (fogo) de um diamante lapidado em brilhante redondo — a referência histórica ainda hoje usada como ponto de partida, embora os laboratórios modernos (nomeadamente o GIA) admitam uma margem de tolerância em torno destes valores em vez de um único número. Para as pedras coloridas, cujo índice de refração difere do diamante, a relação clássica entre coroa e cintura situa-se antes entre 25/75 % e 30/70 %. Regra imperativa, seja qual for a pedra: nunca lapidar uma faceta de pavilhão a um ângulo inferior ao ângulo crítico da espécie (a luz escapa então pelo pavilhão, efeito de «janela»), nem superior ao ângulo de extinção (zonas escuras). Para verificar uma pedra já lapidada, um goniómetro mede o ângulo em duas facetas principais opostas: o ângulo real de coroa ou de pavilhão deduz-se da leitura pela fórmula (180° − ângulo medido) / 2. As restantes lapidações (princesa, oval, pera, esmeralda, almofada...) seguem cada uma o seu próprio diagrama, muito variável consoante o criador do diagrama e o objetivo procurado (rendimento de material versus desempenho ótico): não existe um valor universal comparável a citar sem recorrer a um diagrama preciso.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Brilhante redondo (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53 % do diâmetro da cintura",
                facettes = "57 ou 58 (33 na coroa, 24 ou 25 no pavilhão, simetria de ordem 8)",
                source = "Marcel Tolkowsky, «Diamond Design», 1919"
            )
        ),
        optiqueTitle = "O ângulo crítico e o ângulo de extinção",
        optiqueIntro = "Distinguem-se de forma esquemática três casos para o trajeto da luz que entra pela mesa: um pavilhão pouco profundo demais deixa-a escapar diretamente por baixo; um pavilhão profundo demais aprisiona-a em reflexões internas que saem por uma faceta lateral sem voltar ao olho, deixando a pedra baça apesar de um polimento perfeito; um pavilhão bem proporcionado, pelo contrário, devolve-a ao observador através da coroa, para um brilho máximo. O pavilhão de uma pedra lapidada atua como um espelho: abaixo do ângulo crítico da espécie, a luz que entra pela mesa escapa através do pavilhão (o efeito «janela» já visto acima); lapidado exatamente no ângulo crítico, surge outro defeito, o «olho de peixe» — a mesa parece baça enquanto o contorno da coroa permanece brilhante, porque os raios que a atravessam roçam a parede do cone de reflexão sem chegarem a refletir-se nela. Existe também um limite superior, o ângulo de extinção, além do qual o pavilhão volta a perder luz pela faceta oposta: ângulo de extinção = 60° − (ângulo crítico / 3). Um pavilhão bem lapidado respeita assim ângulo crítico < ângulo do pavilhão < ângulo de extinção; quanto mais elevado for o índice de refração da gema, mais larga se torna esta margem de trabalho — um diamante perdoa muito mais desvio do que uma fluorite. A coroa segue uma regra complementar: o seu ângulo máximo é inversamente proporcional ao do pavilhão (um pavilhão mais curto permite uma coroa mais alta) e diretamente proporcional ao índice de refração; trabalhar algumas décimas de grau abaixo destes máximos continua a ser a escolha mais segura. Todos estes esquemas de corte assentam na Técnica do Ponto de Encontro (método americano, Long e Steele), que usa as interseções entre três ou mais facetas como referências para garantir automaticamente boas proporções e uma boa conservação de peso em relação ao bruto; para estimar o peso de uma pedra já lapidada sem a desengastar, a fórmula habitual é peso (em quilates) = largura³ × coeficiente de volume do corte × peso específico da espécie / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamante", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Esfena (titanite)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Zircão (alto)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Granada demantoide", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alexandrite (crisoberilo)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Rubi e safira (corindo)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Espinela", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Peridoto", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Turmalina", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topázio", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Berilo", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Esmeralda", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Água-marinha", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Ametista e quartzo", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calcite", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluorite", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Defeitos comuns e correções",
        defautsIntro = "A maioria dos defeitos de lapidação resulta de um ângulo mal regulado, de uma indexação errada ou de uma transferência de dop mal alinhada, mais do que de um problema do prato. A regra básica para corrigir um mau encontro ou um polimento desigual: agir sempre do lado oposto ao defeito.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Faceta descentrada ou mal polida de um lado",
                cause = "Ligeiro desalinhamento da pedra sobre o cheater.",
                remede = "Rodar o cheater para inclinar a pedra para o lado oposto ao defeito."
            ),
            LapidaireDefaut(
                probleme = "Faceta mal encontrada ou mal polida em cima, ângulo demasiado grande",
                cause = "O ângulo indicado no goniómetro é superior ao que deveria ser.",
                remede = "Reduzir ligeiramente o ângulo no goniómetro para inclinar a pedra para baixo."
            ),
            LapidaireDefaut(
                probleme = "Faceta mal encontrada ou mal polida em baixo, ângulo demasiado pequeno",
                cause = "O ângulo indicado no goniómetro é inferior ao que deveria ser.",
                remede = "Aumentar ligeiramente o ângulo no goniómetro para inclinar a pedra para cima."
            ),
            LapidaireDefaut(
                probleme = "Defeito combinado na diagonal (por exemplo em cima à esquerda)",
                cause = "A pedra pode ter rodado ligeiramente sobre o dop durante a transferência.",
                remede = "Combinar uma correção no cheater com outra no ângulo, reduzindo se necessário a velocidade de rotação do prato até recuperar o controlo."
            ),
            LapidaireDefaut(
                probleme = "A pedra emite um ruído estridente no prato de polimento",
                cause = "A faceta em curso não está paralela ao prato.",
                remede = "Parar e corrigir a posição da pedra antes de continuar — um sinal fiável, a nunca ignorar."
            ),
            LapidaireDefaut(
                probleme = "Riscos paralelos que atravessam várias facetas",
                cause = "O prato está contaminado com grãos de granulometria diferente.",
                remede = "Descontaminar o prato (escovagem com água quente e sabão, depois raspagem ou retificação se necessário) antes de continuar."
            ),
            LapidaireDefaut(
                probleme = "Marcas de «queimadura» na superfície de uma faceta",
                cause = "Polimento demasiado estacionário, com pressão excessiva, provocando um sobreaquecimento localizado.",
                remede = "Varrer regularmente a faceta sobre o prato, reduzir a pressão, acrescentar se necessário uma gota de água por segundo."
            ),
            LapidaireDefaut(
                probleme = "Aparece uma pequena cavidade durante a lapidação",
                cause = "Foi posta a descoberto uma inclusão próxima da superfície.",
                remede = "Limpar melhor as inclusões logo no desbaste continua a ser a melhor prevenção; depois de aparecer o buraco, relapidar localmente ou considerar um recorte total se necessário."
            )
        ),
        especesTitle = "Fichas práticas por espécie",
        especesIntro = "Para além do ângulo crítico e do ângulo de extinção já indicados, cada espécie tem os seus próprios hábitos de lapidação: orientação da mesa consoante o pleocroísmo ou o clivagem, sensibilidades particulares, e o prato e o pó de polimento que dão melhores resultados. Indicações vindas da prática, não uma regra universal — cada pedra mantém as suas particularidades (inclusões, zonagem, estado da superfície em bruto).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamante",
                orientation = "Isotrópico: sem restrição de orientação ligada a um eixo óptico.",
                fragilite = "Clivagem perfeita segundo 4 planos octaédricos, bem conhecida e aproveitada pelos lapidadores mais do que sofrida; a ponta do pavilhão continua a ser o ponto mais vulnerável aos choques.",
                polissage = "Prato de ferro fundido (scaife) carregado com pó de diamante — só o diamante polir o diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Corindo (Rubi, Safira)",
                orientation = "Sem clivagem a considerar na orientação.",
                fragilite = "Dureza 9, entre as mais duras das pedras lapidadas: poucos cuidados especiais.",
                polissage = "Excelentes resultados em prato de cobre com pó de diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Berilo (Esmeralda, Água-marinha, Morganite, Heliodoro)",
                orientation = "Clivagem imperfeita, sem restrição real de orientação.",
                fragilite = "A esmeralda em particular exige atenção aos jardins (inclusões), que podem provocar a sua quebra tanto na lapidação como no polimento.",
                polissage = "O ácido acético na água de refrigeração melhora o polimento; pó de óxido de alumínio, cério ou estanho, ou Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Quartzo (Ametista, Citrino, Cristal de rocha, Quartzo fumado)",
                orientation = "Sem clivagem; ter em conta as zonagens de cor na orientação.",
                fragilite = "Sem fragilidade particular de clivagem.",
                polissage = "O resultado por vezes reserva surpresas inesperadas, sem causa identificada."
            ),
            LapidaireEspeceFiche(
                pierre = "Topázio",
                orientation = "Deslocar a mesa cerca de 10° em relação ao plano de clivagem basal, para não a expor diretamente no seu eixo.",
                fragilite = "Clivagem nítida numa só direção (plano basal) — fácil de lapidar depois de evitada.",
                polissage = "Sem recomendação de prato além das regras gerais."
            ),
            LapidaireEspeceFiche(
                pierre = "Turmalina",
                orientation = "Lapidar de preferência a mesa paralela ao eixo óptico; uma pedra policromática seciona-se claramente na junção de duas cores — melhor colar do que serrar nesse ponto.",
                fragilite = "Sensível ao calor e aos choques durante a lapidação.",
                polissage = "Sem recomendação de prato além das regras gerais."
            ),
            LapidaireEspeceFiche(
                pierre = "Granada (Almandina, Demantoide, Grossulária, Piropo, Espessartite, Uvarovite)",
                orientation = "Isotrópica: orientação da mesa sem restrição, sem direção preferencial.",
                fragilite = "Sem problema de fragilidade ligado à clivagem.",
                polissage = "O ácido acético (vinagre) na água de refrigeração melhora sistematicamente o resultado; pó de óxido de cério, estanho ou alumínio."
            ),
            LapidaireEspeceFiche(
                pierre = "Espinela",
                orientation = "Isotrópica, sem restrição de orientação.",
                fragilite = "Sem problema de fragilidade ligado à clivagem.",
                polissage = "Polir-se-á notavelmente bem em prato de cobre com pasta de diamante."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridoto (Olivina)",
                orientation = "Pleocroísmo muito fraco, orientação pouco restritiva.",
                fragilite = "Sem fragilidade de clivagem notável.",
                polissage = "O resultado por vezes reserva surpresas inesperadas; o pó de diamante funciona bem."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluorite",
                orientation = "Separa-se facilmente segundo 4 direções: escolher bem a orientação para limitar o risco.",
                fragilite = "Um dos ângulos críticos mais fechados entre as pedras lapidadas, muito sensível a choques e lascamento.",
                polissage = "Prato de preferência em cera; o ácido melhora o resultado."
            ),
            LapidaireEspeceFiche(
                pierre = "Calcite",
                orientation = "Orientar com cuidado para não expor diretamente os planos de clivagem.",
                fragilite = "Dureza muito baixa (3) e clivagem perfeita em 3 direções, muito sensível ao calor — pedra difícil de lapidar e polir; rodar o prato devagar (cerca de 100 rpm).",
                polissage = "Prato de madeira ou cera, pó de óxido de estanho ou crómio, com algumas gotas de ácido oxálico."
            ),
            LapidaireEspeceFiche(
                pierre = "Crisoberilo (Alexandrite)",
                orientation = "Sem restrição de orientação ligada à clivagem.",
                fragilite = "Dureza elevada (8,5), pedra dura sem problema particular.",
                polissage = "Polir-se-á rapidamente com ácido e pó Linde A; excelentes resultados em prato de cobre com pó de diamante."
            )
        ),
        diagrammesTitle = "Diagramas",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagrama de proporções do brilhante redondo: nomenclatura da coroa, da cintura e do pavilhão."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagrama do trajeto da luz conforme a profundidade do pavilhão: pouco profundo, bem proporcionado ou profundo demais."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Gravura antiga representando um moinho de lapidar diamantes — a mecânica do prato rotativo, praticamente inalterada no seu princípio há séculos."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Uma máquina de facetar moderna: mastro, cânula e prato, tal como descritos acima."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Etapas da transformação de uma pedra bruta numa pedra facetada."
            )
        ),
        tipsTitle = "Conselhos práticos",
        tips = listOf(
            LapidaireTip(texte = "Verifique sempre a calibração do ângulo indicado no braço com uma pedra de teste antes de começar uma peça de valor: um desvio de 0,5° no pavilhão basta para escurecer visivelmente uma pedra de resto bem lapidada."),
            LapidaireTip(texte = "Centre cuidadosamente a pedra no dop: um ligeiro desalinhamento traduz-se numa cintura de espessura irregular e em facetas que não se encontram no mesmo ponto ao longo de todo o contorno."),
            LapidaireTip(texte = "Mantenha uma espessura de cintura moderada (nem um «fio de lâmina» frágil a impactos, nem excessivamente espessa, o que retém material sem qualquer benefício ótico) — uma linha visível e regular, nem demasiado fina nem demasiado espessa, é um dos sinais de um trabalho cuidado."),
            LapidaireTip(texte = "Controle os pontos de encontro (meet points) entre facetas com lupa e luz rasante em cada etapa: um ponto de encontro falhado corrige-se facilmente durante a formatação, quase impossível depois de iniciado o pré-polimento."),
            LapidaireTip(texte = "Adapte a lubrificação ao material do prato: a água é adequada à maioria dos pratos diamantados eletrodepositados, enquanto alguns pratos de polimento (o estanho em particular) dão melhores resultados com lubrificação à base de óleo ou uma mistura específica — siga as recomendações do fabricante do prato."),
            LapidaireTip(texte = "Verifique a sensibilidade térmica e a clivagem da espécie antes de lapidar: algumas pedras (topázio, kunzite) têm uma clivagem nítida que pode propagar-se sob o calor ou a pressão de um desbaste demasiado agressivo."),
            LapidaireTip(texte = "Teste a simetria final observando a pedra de frente sob uma fonte de luz pontual: os reflexos das facetas da coroa devem formar um padrão regular; uma assimetria visível a olho nu nesta fase já não se corrige no polimento."),
            LapidaireTip(texte = "Nunca pouse a pedra sobre um prato parado para depois o ligar: coloque sempre o prato em rotação antes de apresentar a pedra, com um ligeiro movimento pendular para evitar que fique fixa sempre no mesmo ponto, o que desgastaria o disco de forma desigual e a própria pedra de forma irregular."),
            LapidaireTip(texte = "Uma mesa que parece «apagada» enquanto o contorno da pedra permanece brilhante indica um efeito de janela: o pavilhão é demasiado raso e deixa a luz escapar por baixo em vez de a devolver pela mesa — corrija aprofundando o ângulo do pavilhão, sem nunca ultrapassar o ângulo de extinção."),
            LapidaireTip(texte = "Nas pedras alongadas (oval, marquise, pera, almofada...), uma cruz escura visível ao centro (efeito dito «X+») revela facetas de comprimento e largura mal ligadas no pavilhão; um diagrama de corte bem estudado para a forma escolhida evita-o melhor do que um ajuste caso a caso.")
        ),
        conservationTitle = "Conservação e manuseamento das pedras lapidadas",
        conservationIntro = "Uma pedra bem lapidada continua vulnerável depois de montada ou guardada: algumas precauções simples evitam a maior parte dos riscos e danos acidentais.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Limpeza",
                conseil = "Nunca limpar uma pedra a seco: o pó age como abrasivo e risca a superfície polida. Lavar com água morna e sabão e uma escova macia, ou com álcool para um desengorduramento rápido, e secar com um pano que não largue fiapos."
            ),
            LapidaireConservationTip(
                titre = "Guardar individualmente",
                conseil = "Guardar cada pedra separadamente, numa dobra de papel ou num saquinho individual: o contacto entre pedras, mesmo breve, risca-as mutuamente."
            ),
            LapidaireConservationTip(
                titre = "Nunca misturar durezas",
                conseil = "Nunca reunir no mesmo compartimento pedras de dureza diferente: a mais dura risca sistematicamente a mais mole, mesmo por um simples atrito durante o transporte."
            ),
            LapidaireConservationTip(
                titre = "Choques térmicos e produtos químicos",
                conseil = "Evitar variações bruscas de temperatura e produtos químicos agressivos (lixívia, ácidos), que podem rachar certas pedras ou alterar um tratamento (esmeralda oleada, pedra impregnada). O limpador ultrassónico deve ser evitado em pedras fraturadas, oleadas ou frágeis."
            ),
            LapidaireConservationTip(
                titre = "Exposição à luz",
                conseil = "Algumas pedras são fotossensíveis: a ametista ou a kunzite expostas muito tempo a luz forte podem desbotar. Guardar as pedras sensíveis ao abrigo de exposição prolongada."
            ),
            LapidaireConservationTip(
                titre = "Transporte e manuseamento",
                conseil = "Para o transporte, usar um saquinho acolchoado e evitar o contacto direto entre várias pedras ou joias juntas; pegar pelo engaste ou pela mesa e não pelo pavilhão, mais vulnerável a choques."
            )
        ),
        disclaimerTitle = "Um ofício que se aprende na oficina",
        disclaimerBody = "Esta ficha apresenta referências gerais, não um manual completo: a lapidação de facetas aprende-se através de prática supervisionada, com equipamento adequado e normas de segurança (proteção ocular e respiratória, arrefecimento contínuo do prato) próprias de cada oficina e de cada máquina. Os diagramas apresentados provêm de fontes reais e livres de direitos (ver créditos); na sua ausência temporária, é apresentada apenas a legenda.",
        machinesTypesTitle = "As máquinas do ofício",
        machinesTypesIntro = "Para além dos componentes de uma máquina de lapidar facetas detalhados acima, eis um panorama dos diferentes tipos de máquinas realmente utilizadas em oficina, tanto para o facetado como para o cabochão, com as suas características e a técnica associada. As marcas citadas são-no a título de exemplo representativo da sua categoria, sem qualquer ligação comercial com a Gems of Rod.",
        categorieFacettageLabel = "Facetado",
        categorieCabochonLabel = "Cabochão",
        categoriePolyvalentLabel = "Polivalente",
        caracteristiquesLabel = "Características",
        techniqueLabel = "Técnica",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Braço manual com calibre (jam-peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "A ferramenta de lapidação mais simples e antiga: a pedra é colada na extremidade de uma haste segura à mão contra o prato, com o ângulo ajustado a olho ou com um calibre simples. Ainda ensinada nas escolas tradicionais de lapidação (Sri Lanca, Tailândia).",
                caracteristiques = listOf(
                    "Nenhuma peça mecânica de indexação",
                    "Custo quase nulo",
                    "Depende inteiramente da destreza do lapidário",
                    "Ritmo de trabalho rápido em mãos experientes"
                ),
                technique = "A mão do lapidário ajusta continuamente o ângulo e a pressão contra o disco; a precisão das facetas assenta na repetição do gesto, não na máquina."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Faceteadora com cabeça de índice mecânica amovível",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Padrão das oficinas ocidentais desde os anos 1970 (Facetron, Ultra Tec, Poly-Metric...): uma cabeça de índice intercambiável ajusta o ângulo e a rotação da pedra à décima de grau, com o braço a deslizar verticalmente para ajustar a profundidade de corte.",
                caracteristiques = listOf(
                    "Precisão angular à décima de grau",
                    "Cabeças de índice intercambiáveis (facetas, escamas, cavetto)",
                    "Batente de profundidade micrométrico",
                    "Investimento significativo (máquina + pratos)"
                ),
                technique = "O lapidário ajusta o ângulo e o entalhe de índice antes de cada faceta, depois desce a pedra contra o prato até ao batente definido: a reprodutibilidade substitui o gesto livre do braço manual."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Faceteadora com cabeça de índice fixa integrada",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Versão mais acessível em que a cabeça de índice é solidária com o braço em vez de intercambiável, com um número de entalhes gravados fixado de fábrica (frequentemente 64, 96 ou 120 consoante o modelo).",
                caracteristiques = listOf(
                    "Preço de entrada bastante inferior ao das cabeças amovíveis",
                    "Número de entalhes de índice limitado e não modificável",
                    "Boa robustez para uso regular",
                    "Adequada à aprendizagem e às lapidações correntes"
                ),
                technique = "Mesmo princípio da cabeça amovível, mas a escolha dos padrões de facetado limita-se às divisões de índice gravadas na máquina — suficiente para a maioria das lapidações clássicas (redonda, oval, almofada)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Faceteadora assistida por computador (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Máquina controlada digitalmente que reproduz um plano de lapidação (diagrama de ângulos e índices) de forma idêntica numa série de pedras, utilizada em produção industrial ou para lapidações de fantasia muito complexas.",
                caracteristiques = listOf(
                    "Reprodutibilidade perfeita de pedra para pedra",
                    "Programação a partir de um plano de lapidação digital",
                    "Investimento elevado, reservado à produção em volume",
                    "Reduz a componente de destreza manual no resultado final"
                ),
                technique = "O plano de lapidação é carregado no software da máquina, que encadeia automaticamente os ângulos e índices programados; o papel do lapidário desloca-se para o ajuste e o controlo de qualidade."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Cabocheadora de discos múltiplos em linha",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Equipamento de referência para o cabochão (tipo Genie, CabKing): 6 a 8 discos diamantados de grão decrescente seguidos de discos de feltro para polir, alinhados sobre a mesma estrutura com rega contínua.",
                caracteristiques = listOf(
                    "6 a 8 postos de grão decrescente no mesmo eixo",
                    "Rega contínua de cada disco",
                    "Passagem rápida de um posto para outro sem trocar de disco",
                    "Preço elevado mas muito boa longevidade"
                ),
                technique = "O cabochão é desbastado e depois afinado passando de um disco para outro por grão decrescente, até aos feltros de polimento finais — cada posto apaga os microrriscos deixados pelo anterior."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Cabocheadora Vevor (discos múltiplos, entrada de gama)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versão económica da cabocheadora de discos múltiplos, fabricada na China sob a marca Vevor: muito difundida entre principiantes e pequenas oficinas graças a um preço claramente inferior ao das marcas especializadas (Genie, CabKing).",
                caracteristiques = listOf(
                    "6 a 8 discos diamantados + feltros no mesmo eixo, como nos modelos profissionais",
                    "Motor e rolamentos de entrada de gama, tolerâncias mecânicas mais largas",
                    "Tabuleiro de recuperação de água integrado",
                    "Preço claramente inferior ao das marcas especializadas, à custa de uma vida útil mais curta"
                ),
                technique = "Mesmo princípio de progressão disco a disco de uma cabocheadora profissional, mas com mais vibrações: uma pressão de trabalho mais leve e uma manutenção mais frequente dos rolamentos compensam a mecânica menos precisa."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Esmeriladora-polidora combinada de eixo horizontal",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Versão mais artesanal: um ou dois eixos horizontais nos quais se montam os próprios discos (discos abrasivos, feltros) consoante a necessidade, em vez de uma linha de postos fixos.",
                caracteristiques = listOf(
                    "Discos intercambiáveis à escolha do lapidário",
                    "Também utilizada para desbastar um bruto antes do corte",
                    "Mais barata do que uma cabocheadora de discos múltiplos dedicada",
                    "Exige mais manuseamentos entre etapas"
                ),
                technique = "O lapidário troca ele próprio o disco montado no eixo em cada etapa de grão, ao contrário da cabocheadora de discos múltiplos, onde os postos são fixos e justapostos."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Serra de corte (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Pequena serra circular com lâmina diamantada, utilizada a montante do cabochão para cortar o bruto em fatias (slabs) da espessura pretendida antes da moldagem nos discos.",
                caracteristiques = listOf(
                    "Lâmina diamantada arrefecida por banho de óleo ou água",
                    "Diâmetro corrente de 10 a 25 cm consoante o modelo",
                    "Guia de corte regulável para fatias regulares",
                    "Etapa prévia indispensável, não é lapidação de facetas"
                ),
                technique = "O bruto é avançado manualmente contra a lâmina em rotação lenta; a espessura da fatia obtida determina diretamente a espessura máxima do futuro cabochão."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Perfuradora de gemas",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Berbequim de coluna equipado com brocas diamantadas ocas e um sistema de rega, utilizado para perfurar pérolas e cabochões destinados a serem montados em pendentes ou enfiados.",
                caracteristiques = listOf(
                    "Brocas diamantadas ocas de diferentes diâmetros",
                    "Arrefecimento a água obrigatório para evitar a fissuração",
                    "Velocidade de rotação regulável consoante a dureza da pedra",
                    "Perfuração em dois tempos (pelas duas faces) em pedras frágeis"
                ),
                technique = "A perfuração é feita a baixa velocidade e sob rega constante, muitas vezes atacando a pedra pelas duas faces para evitar a lasca de saída característica de uma perfuração passante num único passo."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Esmeriladora combinada de facetado/cabochão de entrada de gama",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Pequena máquina económica que associa discos e feltros no mesmo eixo para se iniciar tanto no cabochão como num facetado sumário, sem cabeça de índice precisa.",
                caracteristiques = listOf(
                    "Preço de acesso muito baixo, formato compacto",
                    "Combina vários usos numa só máquina",
                    "Ausência de cabeça de índice precisa para um verdadeiro facetado",
                    "Adequada à descoberta, limitada para um resultado profissional"
                ),
                technique = "O principiante experimenta as duas disciplinas sobre a mesma estrutura, à custa de uma precisão angular e de índice bastante inferior à de uma máquina especializada."
            )
        )
    )

    private val ru = LapidairePage(
        intro = "Огранщик придаёт форму и полирует необработанные камни, превращая их в гранёные драгоценные камни. Это ремесло охватывает несколько специализаций — огранку фасетами (faceting) для прозрачных камней, кабошон для непрозрачных или полупрозрачных камней, гравировку и декоративную обработку, — но именно фасетная огранка, самая техничная из них, придаёт камню игру света. В этой статье представлены базовое оборудование и ориентиры ремесла; она рассчитана на профессионалов и опытных любителей, а не на первое самостоятельное обучение без наставника.",
        machinesTitle = "Огранной станок",
        machinesIntro = "Огранной станок (faceting machine) удерживает камень под точным углом и индексом относительно вращающегося абразивного диска (планшайбы, или лапа). Именно механическая точность — до десятой доли градуса по углу и до точки по индексу — отличает профессиональную огранку от простой ручной шлифовки. Оборудование варьируется от простого штифта с читером (кустарного, быстрого, но неточного) до станков с механической делительной головкой (съёмной или фиксированной, точных, но более медленных в работе), а также моделей с компьютерным управлением для серийной огранки.",
        machines = listOf(
            LapidaireComponent(
                nom = "Мачта (mast)",
                description = "Вертикальная стойка, закреплённая на станине станка, вдоль которой скользит и поворачивается головка с квилем. Её высота и регулируемый наклон вместе с рычагом определяют угол огранки, прикладываемый к планшайбе."
            ),
            LapidaireComponent(
                nom = "Квиль (quill)",
                description = "Трубка, в которой скользит стержень с камнем (доп). Квиль вращается, поочерёдно подводя каждую запланированную грань к диску, и выдвигается, продвигая камень по мере снятия материала."
            ),
            LapidaireComponent(
                nom = "Индексное колесо (index gear)",
                description = "Зубчатое колесо на конце квиля, обычно доступное в нескольких градуировках — 64, 72, 80, 96 или 120 позиций — для покрытия всех стилей огранки; диск «быстрого индекса» с меньшим числом позиций часто накладывается поверх обычного индекса, чтобы ускорить позиционирование при огранке серии камней. Фиксация вращения камня в точном положении для каждой грани — необходимое условие симметрии такой огранки, как круглый бриллиант, чьи грани распределены с симметрией 8-го порядка. Выбранный индекс должен быть точным кратным числу граней: например, пятиугольник невозможно огранить с индексом 96 (96 ÷ 5 — не целое число), но он без труда гранится с индексом 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Читер (микрометр точной подстройки)",
                description = "Кольцо тонкой регулировки, накладываемое поверх угла, указанного на рычаге. Его основная функция — не изменение самого угла огранки, а восстановление параллельности между текущей гранью и планшайбой, когда та слегка покоробилась или неравномерно износилась — регулировка, которую следует повторять всякий раз, когда грань перестаёт полироваться равномерно по всей своей поверхности."
            ),
            LapidaireComponent(
                nom = "Доп (dop)",
                description = "Стержень (дерево, алюминий или латунь — последние два лучше удерживают тепло предварительного нагрева), на который камень крепится перед установкой в квиль. Крепление выполняется разогретым воском (наиболее распространённый способ, плавится около 80 °C), цианоакрилатным клеем («суперклей», быстрое схватывание, но с трудноудаляемыми остатками) или эпоксидной смолой (хорошее сцепление, но её экзотермическая реакция требует контроля на термочувствительных камнях). Более совершенные допы имеют штифт, выступ или паз, всегда ориентирующие их одинаково в делительной головке — необходимое условие для переноса камня с одного допа на другой (огранка коронки после павильона) без смещения или нежелательного поворота."
            ),
            LapidaireComponent(
                nom = "Планшайба (lap)",
                description = "Вращающийся металлический диск, обычно диаметром 6\" (≈152 мм) или 8\" (≈203 мм), на который нанесён или закреплён абразив. Различают планшайбы для обдирки/формирования граней (гальванически осаждённый алмаз или чугун, пропитанный алмазными частицами прокаткой) и планшайбы для полировки, более мягкие. На станке обычно установлено несколько сменных планшайб, по одной на каждый этап зернистости."
            ),
            LapidaireComponent(
                nom = "Гониометр",
                description = "Контрольный инструмент, измеряющий фактический угол уже огранённого камня по двум противоположным основным граням, чтобы проверить полученную огранку. Измеренный угол (C' для коронки, P' для павильона) пересчитывается по формуле угол = (180° − измеренный угол) / 2, поскольку гониометр показывает дополнительный угол, образованный с плоскостью рундиста."
            )
        ),
        disquesTitle = "Диски для формирования граней и полировки",
        disquesIntro = "Огранка проходит через этапы всё более мелкой зернистости, каждый из которых устраняет микроцарапины, оставленные предыдущим; пропуск этапа оставляет следы, которые финальная полировка уже не устранит. Скорость вращения планшайбы соответствует этому прогрессу: довольно медленная при обдирке (100–300 об/мин), быстрее при формировании граней (300–600 об/мин) и самая высокая при полировке (700–1000 об/мин и более) — всегда с учётом чувствительности камня, снижая скорость без колебаний для мягкого или спайного камня.",
        disques = listOf(
            LapidaireDisc(
                nom = "Обдирка",
                grain = "260–325",
                usage = "Быстро снимает материал, придавая камню общую форму (преформу) из необработанного сырья или распиленной пластины. Агрессивная алмазная планшайба, сильное давление, обязательное водяное охлаждение."
            ),
            LapidaireDisc(
                nom = "Формирование граней",
                grain = "600–1200",
                usage = "Наносит и уточняет каждую грань под точным углом и индексом, предусмотренными схемой огранки. Самый длительный и решающий этап для симметрии и точности точек стыковки (meet points) соседних граней."
            ),
            LapidaireDisc(
                nom = "Предварительная полировка",
                grain = "3000–8000",
                usage = "Доводит поверхность, оставленную формированием, до почти матового вида без глубоких царапин, сохраняя точно те же углы — любая корректировка угла на этом этапе должна быть минимальной, иначе придётся вернуться к более грубой зернистости."
            ),
            LapidaireDisc(
                nom = "Финальная полировка",
                grain = "Субмикронная паста или порошок (часто оксид церия или очень мелкий алмаз)",
                usage = "Полировочная планшайба выбирается по твёрдости (шкала Мооса), от самой твёрдой до самой мягкой: керамика (9), медь (3), цинк (2,5), фенопласт (2,2), плексиглас или свинцово-оловянный сплав (2), чистое олово (1,7), свинец (1,5), ПВХ (1) и воск с различными наполнителями (0,3, варьируется в зависимости от состава). Полирует никогда не сама планшайба, а только нанесённый на неё порошок, играющий роль абразива, — планшайба лишь основа, твёрдость которой должна соответствовать твёрдости камня. Сухая полировка (порошок, закреплённый жировым связующим) или мокрая (порошок, разведённый в воде, наносимый непрерывно) — в зависимости от выбранного метода."
            )
        ),
        indexTitle = "Выбор делительной головки",
        indexIntro = "Делительную головку выбирают прежде всего исходя из числа граней, которые нужно нарезать: оно должно быть точным делителем числа насечек (пятиугольник невозможен на головке с 96 насечками, но возможен на головке с 80). Поворот на одну насечку обратно пропорционален их общему числу (360°, делённые на число насечек) — головка на 120 насечек продвигается на 3° за насечку, головка на 32 насечки — на 11,25°. Пересчитать схему огранки с одной головки на другую можно только если обе головки кратны друг другу: достаточно разделить или умножить номер каждой грани на их отношение (например, разделить на 3 при переходе с головки 96 на головку 32). Поскольку все распространённые головки кратны 4, квадрат можно нарезать на любой из них; напротив, невозможно установить положение между двумя насечками, что сразу исключает большинство многоугольников с нечётным числом сторон. Головка с 96 насечками остаётся самой распространённой у производителей, предлагая хороший баланс между точностью деления и скоростью позиционирования; головка на 120 насечек самая полная, но требует более медленного позиционирования.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / насечка", cotesTaillables = "Квадрат (4), восьмиугольник (8), шестнадцатиугольник (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / насечка", cotesTaillables = "Треугольник (3), квадрат (4), пятиугольник (5), шестиугольник (6), десятиугольник (10), двенадцатиугольник (12), пятнадцатиугольник (15), двадцатиугольник (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / насечка", cotesTaillables = "Квадрат (4), восьмиугольник (8), шестнадцатиугольник (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / насечка", cotesTaillables = "Треугольник (3), квадрат (4), шестиугольник (6), восьмиугольник (8), девятиугольник (9), двенадцатиугольник (12), восемнадцатиугольник (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / насечка", cotesTaillables = "Квадрат (4), пятиугольник (5), восьмиугольник (8), десятиугольник (10), шестнадцатиугольник (16), двадцатиугольник (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / насечка", cotesTaillables = "Самая распространённая — всего 9 правильных многоугольников, включая квадрат, шестиугольник, восьмиугольник, двенадцатиугольник"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / насечка", cotesTaillables = "Самая полная — всего 14 правильных многоугольников, максимум среди распространённых головок"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / насечка", cotesTaillables = "Квадрат (4), восьмиугольник (8), шестнадцатиугольник (16), 32 стороны, 64 стороны")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Оценочный вес",
            intro = "Для уже огранённого и вставленного в оправу камня прямое взвешивание невозможно: вес выводится из объёма — по измерениям штангенциркулем (точность 1/100 мм) и удельному весу вида. Приблизительная формула, точность порядка 10-15% — не заменяет реальное взвешивание.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Круглая",
                LapidaireCutShape.OVALE to "Овальная",
                LapidaireCutShape.COUSSIN_CARRE to "Квадратная кушон",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Прямоугольная кушон",
                LapidaireCutShape.CARRE_A_GRADIN to "Квадратная ступенчатая",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Прямоугольная ступенчатая",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Квадратная кушон (ступенчатая)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Прямоугольная кушон (ступенчатая)",
                LapidaireCutShape.MARQUISE to "Маркиза",
                LapidaireCutShape.POIRE to "Груша",
                LapidaireCutShape.TRIANGLE_BOMBE to "Выпуклый треугольник",
                LapidaireCutShape.TRIANGLE to "Треугольник",
                LapidaireCutShape.TRAPEZE to "Трапеция",
                LapidaireCutShape.COEUR to "Сердце"
            ),
            dimension1Label = "Длина или диаметр (мм)",
            dimension2Label = "Ширина (мм)",
            heightLabel = "Общая глубина, от площадки до шипа (мм)",
            sgLabel = "Удельный вес вида",
            computeLabel = "Рассчитать",
            resultLabel = "Оценочный вес: %s карат",
            disclaimer = "Оценка по объёму, приблизительная точность 10-15% — не заменяет реальное взвешивание.",
            errorMessage = "Введите корректные значения (положительные числа) для всех размеров и удельного веса."
        ),
        anglesTitle = "Эталонные углы: круглый бриллиант",
        anglesIntro = "Приведённые ниже пропорции опубликованы бельгийским математиком Марселем Толковски в 1919 году: он рассчитал оптимальный угол для максимизации возврата света (блеска) и дисперсии (игры) бриллианта круглой огранки — это исторический эталон, до сих пор служащий отправной точкой, хотя современные лаборатории (в частности, GIA) допускают диапазон допуска вокруг этих значений, а не единственное число. Для цветных камней, показатель преломления которых отличается от алмазного, классическое соотношение между коронкой и рундистом обычно составляет от 25/75 % до 30/70 %. Обязательное правило, независимо от камня: никогда не гранить грань павильона под углом меньше критического угла данного минерала (тогда свет уходит через павильон — эффект «окна»), и не больше угла погасания (тёмные зоны). Для проверки уже огранённого камня гониометр измеряет угол по двум противоположным основным граням: реальный угол коронки или павильона выводится из показания по формуле (180° − измеренный угол) / 2. Другие огранки (принцесса, овал, груша, изумрудная, кушон...) следуют собственным схемам, сильно различающимся в зависимости от автора схемы и цели (выход материала против оптических характеристик): не существует сопоставимого универсального значения, которое можно было бы привести без обращения к конкретной схеме.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Круглый бриллиант (Толковски, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53 % от диаметра рундиста",
                facettes = "57 или 58 (33 на коронке, 24 или 25 на павильоне, симметрия 8-го порядка)",
                source = "Марсель Толковски, «Diamond Design», 1919"
            )
        ),
        optiqueTitle = "Критический угол и угол погасания",
        optiqueIntro = "В общих чертах можно выделить три случая для пути света, входящего через площадку: слишком мелкий павильон пропускает его прямо вниз; слишком глубокий павильон улавливает его во внутренних отражениях, которые выходят через боковую грань, не возвращаясь к глазу, из-за чего камень выглядит тусклым несмотря на безупречную полировку; хорошо пропорциональный павильон, напротив, направляет свет обратно к наблюдателю через коронку, обеспечивая максимальный блеск. Павильон огранённого камня действует как зеркало: ниже критического угла минерала свет, входящий через площадку, уходит через павильон (эффект «окна», уже рассмотренный выше); при огранке точно на критическом угле появляется другой дефект, «рыбий глаз» — площадка выглядит тусклой, а контур коронки остаётся ярким, потому что проходящие через него лучи скользят по стенке конуса отражения, не отражаясь от неё по-настоящему. Существует и верхний предел — угол погасания, за которым павильон снова теряет свет через противоположную грань: угол погасания = 60° − (критический угол / 3). Хорошо огранённый павильон, таким образом, соблюдает соотношение критический угол < угол павильона < угол погасания; чем выше показатель преломления камня, тем шире этот рабочий диапазон — алмаз прощает намного больше отклонений, чем флюорит. Коронка подчиняется дополнительному правилу: её максимальный угол обратно пропорционален углу павильона (более короткий павильон допускает более высокую коронку) и прямо пропорционален показателю преломления; работать на несколько десятых градуса ниже этих максимумов остаётся самым надёжным выбором. Все эти схемы огранки опираются на Технику Точки Стыковки (американский метод, Лонг и Стил), использующую пересечения трёх и более граней как ориентиры для автоматического обеспечения хороших пропорций и хорошего сохранения веса относительно сырья; чтобы оценить вес уже огранённого камня, не снимая его с оправы, обычно применяют формулу вес (в каратах) = ширина³ × коэффициент объёма огранки × удельный вес минерала / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Алмаз", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Сфен (титанит)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Циркон (высокий)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Демантоид (гранат)", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Александрит (хризоберилл)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Рубин и сапфир (корунд)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Шпинель", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Перидот", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Турмалин", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Топаз", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Берилл", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Изумруд", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Аквамарин", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Аметист и кварц", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Кальцит", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Флюорит", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Частые дефекты и их устранение",
        defautsIntro = "Большинство дефектов огранки возникает скорее из-за неточно выставленного угла, ошибочной индексации или плохо выровненного переноса допа, чем из-за проблемы с планшайбой. Базовое правило исправления плохой стыковки или неровной полировки: всегда действовать с противоположной дефекту стороны.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Грань смещена от центра или плохо отполирована с одной стороны",
                cause = "Небольшая несоосность камня на читере.",
                remede = "Повернуть читер, чтобы наклонить камень в сторону, противоположную дефекту."
            ),
            LapidaireDefaut(
                probleme = "Грань плохо стыкуется или плохо полируется сверху, угол слишком большой",
                cause = "Угол, показанный на угломере, выше, чем должен быть.",
                remede = "Немного уменьшить угол на угломере, чтобы наклонить камень вниз."
            ),
            LapidaireDefaut(
                probleme = "Грань плохо стыкуется или плохо полируется снизу, угол слишком маленький",
                cause = "Угол, показанный на угломере, ниже, чем должен быть.",
                remede = "Немного увеличить угол на угломере, чтобы наклонить камень вверх."
            ),
            LapidaireDefaut(
                probleme = "Комбинированный диагональный дефект (например, вверху слева)",
                cause = "Камень мог слегка повернуться на допе во время переноса.",
                remede = "Совместить коррекцию на читере с коррекцией угла, при необходимости снизив скорость вращения планшайбы, пока не восстановится контроль."
            ),
            LapidaireDefaut(
                probleme = "Камень издаёт резкий скрип на полировочной планшайбе",
                cause = "Обрабатываемая грань не параллельна планшайбе.",
                remede = "Остановиться и исправить положение камня, прежде чем продолжать — надёжный признак, который никогда нельзя игнорировать."
            ),
            LapidaireDefaut(
                probleme = "Параллельные царапины, проходящие через несколько граней",
                cause = "Планшайба загрязнена зёрнами другой зернистости.",
                remede = "Обеззаразить планшайбу (чистка горячей мыльной водой, при необходимости соскабливание или рихтовка поверхности) перед продолжением работы."
            ),
            LapidaireDefaut(
                probleme = "Следы «ожога» на поверхности грани",
                cause = "Слишком долгая полировка на одном месте с чрезмерным давлением, вызывающая локальный перегрев.",
                remede = "Регулярно перемещать грань по планшайбе, снизить давление, при необходимости добавлять каплю воды в секунду."
            ),
            LapidaireDefaut(
                probleme = "В ходе огранки появляется небольшая полость",
                cause = "Вскрылось включение, находившееся близко к поверхности.",
                remede = "Более тщательная «очистка» включений ещё на этапе обдирки остаётся лучшей профилактикой; если отверстие уже появилось — переогранить этот участок локально или, при необходимости, рассмотреть полную переогранку."
            )
        ),
        especesTitle = "Практические справки по видам",
        especesIntro = "Помимо уже указанных критического угла и угла погасания, у каждого вида есть свои особенности огранки: ориентация площадки по плеохроизму или спайности, особая чувствительность, а также планшайба и полировальный порошок, дающие лучший результат. Это ориентиры из практики, а не универсальное правило — у каждого камня остаются свои индивидуальные особенности (включения, зональность, состояние поверхности сырья).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Алмаз",
                orientation = "Изотропен: ограничений по ориентации, связанных с оптической осью, нет.",
                fragilite = "Совершенная спайность по 4 октаэдрическим плоскостям, хорошо изученная и используемая огранщиками, а не просто терпимая; вершина павильона остаётся наиболее уязвимой к ударам точкой.",
                polissage = "Чугунная планшайба (скайф), заряженная алмазным порошком, — только алмаз полирует алмаз."
            ),
            LapidaireEspeceFiche(
                pierre = "Корунд (Рубин, Сапфир)",
                orientation = "Спайность при ориентации не учитывается.",
                fragilite = "Твёрдость 9, один из самых твёрдых среди огранённых камней: особых предосторожностей не требуется.",
                polissage = "Отличные результаты на медной планшайбе с алмазным порошком."
            ),
            LapidaireEspeceFiche(
                pierre = "Берилл (Изумруд, Аквамарин, Морганит, Гелиодор)",
                orientation = "Несовершенная спайность, реальных ограничений по ориентации нет.",
                fragilite = "Изумруд особенно требует внимания к «саду» (включениям), из-за которых он может расколоться как при огранке, так и при полировке.",
                polissage = "Уксусная кислота в охлаждающей воде улучшает полировку; порошок оксида алюминия, церия или олова, либо Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Кварц (Аметист, Цитрин, Горный хрусталь, Дымчатый кварц)",
                orientation = "Спайности нет; при ориентации учитывать цветовую зональность.",
                fragilite = "Особой хрупкости по спайности нет.",
                polissage = "Результат иногда преподносит неожиданные сюрпризы без выясненной причины."
            ),
            LapidaireEspeceFiche(
                pierre = "Топаз",
                orientation = "Сместить площадку примерно на 10° относительно базальной плоскости спайности, чтобы не выставлять её прямо по оси.",
                fragilite = "Чёткая спайность в одном направлении (базальная плоскость) — легко огранять, если её избегать.",
                polissage = "Особых рекомендаций по планшайбе, кроме общих правил, нет."
            ),
            LapidaireEspeceFiche(
                pierre = "Турмалин",
                orientation = "Площадку желательно резать параллельно оптической оси; многоцветный камень чётко раскалывается на стыке двух цветов — в этом месте лучше склеить, чем распиливать.",
                fragilite = "Чувствителен к нагреву и ударам при огранке.",
                polissage = "Особых рекомендаций по планшайбе, кроме общих правил, нет."
            ),
            LapidaireEspeceFiche(
                pierre = "Гранат (Альмандин, Демантоид, Гроссуляр, Пироп, Спессартин, Уваровит)",
                orientation = "Изотропен: ориентация площадки без ограничений, предпочтительного направления нет.",
                fragilite = "Проблем с хрупкостью из-за спайности нет.",
                polissage = "Уксусная кислота (уксус) в охлаждающей воде стабильно улучшает результат; порошок оксида церия, олова или алюминия."
            ),
            LapidaireEspeceFiche(
                pierre = "Шпинель",
                orientation = "Изотропна, ограничений по ориентации нет.",
                fragilite = "Проблем с хрупкостью из-за спайности нет.",
                polissage = "Прекрасно полируется на медной планшайбе алмазной пастой."
            ),
            LapidaireEspeceFiche(
                pierre = "Перидот (Оливин)",
                orientation = "Очень слабый плеохроизм, ориентация не сильно ограничивает.",
                fragilite = "Заметной хрупкости по спайности нет.",
                polissage = "Результат иногда преподносит неожиданные сюрпризы; алмазный порошок подходит хорошо."
            ),
            LapidaireEspeceFiche(
                pierre = "Флюорит",
                orientation = "Легко раскалывается по 4 направлениям: тщательно выбирать ориентацию, чтобы снизить риск.",
                fragilite = "Один из самых узких критических углов среди огранённых камней, очень чувствителен к ударам и сколам.",
                polissage = "Планшайба предпочтительно восковая; кислота улучшает результат."
            ),
            LapidaireEspeceFiche(
                pierre = "Кальцит",
                orientation = "Тщательно ориентировать, чтобы не выставлять плоскости спайности напрямую.",
                fragilite = "Очень низкая твёрдость (3) и совершенная спайность по 3 направлениям, сильная чувствительность к нагреву — камень, трудный в огранке и полировке; вращать планшайбу медленно (около 100 об/мин).",
                polissage = "Деревянная или восковая планшайба, порошок оксида олова или хрома, с несколькими каплями щавелевой кислоты."
            ),
            LapidaireEspeceFiche(
                pierre = "Хризоберилл (Александрит)",
                orientation = "Ограничений по ориентации из-за спайности нет.",
                fragilite = "Высокая твёрдость (8,5), камень твёрдый, без особых проблем.",
                polissage = "Быстро полируется кислотой и порошком Linde A; отличные результаты на медной планшайбе с алмазным порошком."
            )
        ),
        diagrammesTitle = "Схемы",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Схема пропорций круглого бриллианта: номенклатура коронки, рундиста и павильона."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Схема хода света в зависимости от глубины павильона: слишком мелкий, хорошо пропорционированный или слишком глубокий."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Старинная гравюра, изображающая мельницу для огранки алмазов — механика вращающейся планшайбы, принципиально не изменившаяся за века."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Современный огранной станок: мачта, квиль и планшайба, как описано выше."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Этапы превращения необработанного камня в гранёный."
            )
        ),
        tipsTitle = "Практические советы",
        tips = listOf(
            LapidaireTip(texte = "Всегда проверяйте калибровку угла, указанного на рычаге, с помощью тестового камня перед началом работы над ценным изделием: отклонение в 0,5° на павильоне достаточно, чтобы заметно затемнить в остальном хорошо огранённый камень."),
            LapidaireTip(texte = "Тщательно центрируйте камень на допе: небольшое смещение оси приводит к неравномерной толщине рундиста и граням, не сходящимся в одной точке по всему периметру."),
            LapidaireTip(texte = "Соблюдайте умеренную толщину рундиста (не хрупкое «лезвие», уязвимое к сколам, и не чрезмерно толстое, что удерживает материал без оптической пользы) — ровная видимая линия рундиста, не слишком тонкая и не слишком толстая, — один из признаков аккуратной работы."),
            LapidaireTip(texte = "Проверяйте точки стыковки (meet points) между гранями через лупу при скользящем освещении на каждом этапе: пропущенная точка стыковки легко исправляется на этапе формирования и почти не поддаётся исправлению после начала предварительной полировки."),
            LapidaireTip(texte = "Подбирайте смазку под материал планшайбы: вода подходит для большинства гальванических алмазных планшайб, тогда как некоторые полировочные планшайбы (особенно оловянные) дают лучшие результаты со смазкой на масляной основе или специальной смесью — следуйте рекомендациям производителя планшайбы."),
            LapidaireTip(texte = "Перед огранкой проверяйте термочувствительность и спайность минерала: у некоторых камней (топаз, кунцит) выраженная спайность может распространиться под воздействием тепла или давления слишком агрессивной обдирки."),
            LapidaireTip(texte = "Проверяйте окончательную симметрию, рассматривая камень анфас под точечным источником света: отражения граней коронки должны образовывать регулярный узор; асимметрия, видимая невооружённым глазом на этом этапе, уже не исправляется полировкой."),
            LapidaireTip(texte = "Никогда не кладите камень на неподвижную планшайбу перед запуском: всегда сначала приводите планшайбу во вращение и лишь затем подводите камень, с лёгким маятниковым движением, чтобы он не оставался всё время в одной точке — это неравномерно изнашивает диск и сам камень."),
            LapidaireTip(texte = "Площадка, которая кажется «погасшей», в то время как контур камня остаётся блестящим, указывает на эффект окна: павильон слишком мелкий и пропускает свет наружу снизу вместо того, чтобы возвращать его через площадку — исправляйте, углубляя угол павильона, никогда не превышая угол погасания."),
            LapidaireTip(texte = "На вытянутых камнях (овал, маркиз, груша, кушон...) тёмный крест, видимый в центре (так называемый эффект «X+»), выдаёт грани длины и ширины, плохо состыкованные на павильоне; хорошо продуманная схема огранки для выбранной формы предотвращает это лучше, чем подгонка по месту.")
        ),
        conservationTitle = "Хранение и обращение с огранёнными камнями",
        conservationIntro = "Хорошо огранённый камень остаётся уязвимым и после того, как его закрепили в оправу или убрали на хранение: несколько простых мер предосторожности предотвращают большинство царапин и случайных повреждений.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Чистка",
                conseil = "Никогда не протирайте камень насухо: пыль действует как абразив и царапает полированную поверхность. Промывайте тёплой мыльной водой мягкой щёткой или спиртом для быстрого обезжиривания, затем протирайте безворсовой тканью."
            ),
            LapidaireConservationTip(
                titre = "Раздельное хранение",
                conseil = "Храните каждый камень отдельно, в бумажном конвертике или индивидуальном мешочке: даже кратковременный контакт между камнями царапает их взаимно."
            ),
            LapidaireConservationTip(
                titre = "Никогда не смешивайте твёрдости",
                conseil = "Никогда не держите в одном отделении камни разной твёрдости: более твёрдый систематически царапает более мягкий, даже от простого трения при перевозке."
            ),
            LapidaireConservationTip(
                titre = "Тепловой шок и химикаты",
                conseil = "Избегайте резких перепадов температуры и агрессивных химикатов (отбеливателя, кислот), которые могут растрескать некоторые камни или повредить обработку (промасленный изумруд, пропитанный камень). Ультразвуковую чистку следует избегать для треснувших, промасленных или хрупких камней."
            ),
            LapidaireConservationTip(
                titre = "Воздействие света",
                conseil = "Некоторые камни светочувствительны: аметист или кунцит при длительном воздействии яркого света могут выцвести. Храните чувствительные камни вдали от длительного освещения."
            ),
            LapidaireConservationTip(
                titre = "Перевозка и обращение",
                conseil = "Для перевозки используйте мягкий мешочек и избегайте прямого контакта между несколькими камнями или украшениями, собранными вместе; берите за оправу или площадку, а не за павильон, который более уязвим к ударам."
            )
        ),
        disclaimerTitle = "Ремесло, которому учатся в мастерской",
        disclaimerBody = "Эта статья приводит общие ориентиры, а не полное руководство: фасетная огранка осваивается через практику под наставничеством, с подходящим оборудованием и правилами безопасности (защита глаз и органов дыхания, непрерывное охлаждение планшайбы), специфичными для каждой мастерской и каждого станка. Показанные схемы взяты из реальных источников со свободными лицензиями (см. указания авторства); при их временном отсутствии отображается только подпись.",
        machinesTypesTitle = "Станки ремесла",
        machinesTypesIntro = "Помимо описанных выше компонентов огранного станка, вот обзор различных типов станков, реально используемых в мастерской — как для фасетной огранки, так и для кабошонирования, — с их характеристиками и связанной техникой. Упомянутые бренды приводятся лишь как показательный пример своей категории, без коммерческой связи с Gems of Rod.",
        categorieFacettageLabel = "Фасетная огранка",
        categorieCabochonLabel = "Кабошонирование",
        categoriePolyvalentLabel = "Универсальный",
        caracteristiquesLabel = "Характеристики",
        techniqueLabel = "Техника",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Ручной держатель с шаблоном (джем-пег)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Самый простой и старый инструмент огранки: камень приклеивается к концу стержня, который держат рукой у планшайбы, а угол выставляют на глаз или с помощью простого шаблона. До сих пор преподаётся в традиционных школах огранки (Шри-Ланка, Таиланд).",
                caracteristiques = listOf(
                    "Отсутствие механического индексирующего узла",
                    "Практически нулевая стоимость",
                    "Полностью зависит от мастерства огранщика",
                    "Высокий темп работы в опытных руках"
                ),
                technique = "Рука огранщика непрерывно регулирует угол и нажим на планшайбу; точность граней определяется повторяемостью движения, а не станком."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Огранный станок со сменной механической индексной головкой",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Стандарт западных мастерских с 1970-х годов (Facetron, Ultra Tec, Poly-Metric...): сменная индексная головка задаёт угол и поворот камня с точностью до десятой доли градуса, а рычаг скользит вертикально для регулировки глубины реза.",
                caracteristiques = listOf(
                    "Точность угла до десятой доли градуса",
                    "Сменные индексные головки (грани, чешуйки, каветто)",
                    "Микрометрический упор глубины",
                    "Значительные вложения (станок + планшайбы)"
                ),
                technique = "Огранщик задаёт угол и индексную позицию перед каждой гранью, затем опускает камень к планшайбе до заданного упора: воспроизводимость заменяет свободное движение руки при ручной огранке."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Огранный станок с фиксированной встроенной индексной головкой",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Более доступная версия, в которой индексная головка неразъёмно соединена с рычагом, а не сменная, с числом гравированных позиций, заданным при изготовлении (часто 64, 96 или 120 в зависимости от модели).",
                caracteristiques = listOf(
                    "Цена входа заметно ниже, чем у сменных головок",
                    "Ограниченное и неизменяемое число индексных позиций",
                    "Хорошая надёжность при регулярном использовании",
                    "Подходит для обучения и распространённых огранок"
                ),
                technique = "Тот же принцип, что и у сменной головки, но выбор рисунков огранки ограничен индексными делениями, выгравированными на станке — этого достаточно для большинства классических огранок (круглая, овальная, «подушка»)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Огранный станок с компьютерным управлением (ЧПУ)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Станок с цифровым управлением, который идентично воспроизводит план огранки (диаграмму углов и индексов) на серии камней; применяется в промышленном производстве или для очень сложных фантазийных огранок.",
                caracteristiques = listOf(
                    "Идеальная воспроизводимость от камня к камню",
                    "Программирование на основе цифрового плана огранки",
                    "Высокие вложения, применяется только при серийном производстве",
                    "Снижает долю ручного мастерства в конечном результате"
                ),
                technique = "План огранки загружается в программное обеспечение станка, которое автоматически отрабатывает заданные углы и индексные позиции; роль огранщика смещается к настройке и контролю качества."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Многодисковый кабошонировочный станок линейного типа",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Эталонное оборудование для кабошонирования (тип Genie, CabKing): от 6 до 8 алмазных дисков с убывающей зернистостью, за которыми следуют фетровые полировальные диски, выстроенные на одной станине с непрерывной подачей воды.",
                caracteristiques = listOf(
                    "От 6 до 8 постов с убывающей зернистостью на одном валу",
                    "Непрерывная подача воды к каждому диску",
                    "Быстрый переход от одного поста к другому без смены диска",
                    "Высокая цена, но очень хорошая долговечность"
                ),
                technique = "Кабошон грубо формируется, а затем доводится путём перехода от диска к диску с убывающей зернистостью вплоть до финальных полировальных фетров — каждый пост удаляет микроцарапины, оставленные предыдущим."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Кабошонировочный станок Vevor (многодисковый, начального уровня)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Бюджетная версия многодискового кабошонировочного станка, производимая в Китае под маркой Vevor: очень распространена среди начинающих и небольших мастерских благодаря цене, заметно ниже специализированных брендов (Genie, CabKing).",
                caracteristiques = listOf(
                    "От 6 до 8 алмазных дисков + фетры на одном валу, как у профессиональных моделей",
                    "Двигатель и подшипники начального уровня, более широкие механические допуски",
                    "Встроенный поддон для сбора воды",
                    "Заметно ниже по цене специализированных брендов, но за счёт меньшего срока службы"
                ),
                technique = "Тот же принцип последовательного перехода от диска к диску, что и у профессионального станка, но с большей вибрацией: более лёгкое рабочее давление и более частое обслуживание подшипников компенсируют менее точную механику."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Комбинированный шлифовально-полировальный станок с горизонтальным валом",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Более кустарная версия: один или два горизонтальных вала, на которые самостоятельно устанавливаются диски (планшайбы, фетры) по мере необходимости, вместо линии фиксированных постов.",
                caracteristiques = listOf(
                    "Сменные диски по выбору мастера",
                    "Также используется для грубой обработки заготовки перед распиловкой",
                    "Дешевле специализированного многодискового станка",
                    "Требует больше манипуляций между этапами"
                ),
                technique = "Мастер сам меняет установленный на валу диск на каждом этапе зернистости, в отличие от многодискового станка, где посты фиксированы и расположены рядом друг с другом."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Отрезной станок (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Небольшая циркулярная пила с алмазным диском, применяемая перед кабошонированием для распиловки заготовки на пластины (слэбы) нужной толщины перед формовкой на дисках.",
                caracteristiques = listOf(
                    "Алмазный диск, охлаждаемый масляной или водяной ванной",
                    "Распространённый диаметр от 10 до 25 см в зависимости от модели",
                    "Регулируемая направляющая для ровных пластин",
                    "Обязательный подготовительный этап, не связанный с огранкой граней"
                ),
                technique = "Заготовка вручную подаётся к медленно вращающемуся диску; толщина полученной пластины напрямую определяет максимальную толщину будущего кабошона."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Сверлильный станок для камней",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Настольно-сверлильный станок с полыми алмазными свёрлами и системой подачи воды, применяемый для сверления бусин и кабошонов, предназначенных для крепления в подвески или нанизывания.",
                caracteristiques = listOf(
                    "Полые алмазные свёрла разных диаметров",
                    "Обязательное водяное охлаждение для предотвращения растрескивания",
                    "Регулируемая скорость вращения в зависимости от твёрдости камня",
                    "Двустороннее сверление хрупких камней"
                ),
                technique = "Сверление выполняется на низкой скорости при постоянной подаче воды, часто с обеих сторон камня, чтобы избежать характерного скола на выходе при сквозном сверлении за один проход."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Комбинированный точильно-шлифовальный станок для огранки/кабошонирования начального уровня",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Небольшой недорогой станок, объединяющий диски и фетры на одном валу для знакомства как с кабошонированием, так и с упрощённой фасетной огранкой, без точной индексной головки.",
                caracteristiques = listOf(
                    "Очень низкая цена входа, компактный формат",
                    "Совмещает несколько применений в одном станке",
                    "Отсутствие точной индексной головки для настоящей огранки",
                    "Подходит для знакомства с ремеслом, ограничен для профессионального результата"
                ),
                technique = "Новичок пробует обе дисциплины на одной станине, жертвуя точностью угла и индекса, которая значительно уступает специализированному станку."
            )
        )
    )

    private val nl = LapidairePage(
        intro = "De lapidarist geeft ruwe stenen vorm en polijst ze tot geslepen edelstenen. Dit vak omvat verschillende specialisaties — het facetteren voor transparante stenen, de cabochon voor ondoorzichtige of doorschijnende stenen, gravure en ornamenteel werk — maar het facetteren, het meest technische onderdeel, is wat een steen zijn lichtspel geeft. Dit overzicht presenteert de basisuitrusting en de belangrijkste kengetallen van het vak; het is bedoeld voor professionals en ervaren liefhebbers, niet als eerste, onbegeleide les.",
        machinesTitle = "De facetteermachine",
        machinesIntro = "Een facetteermachine (faceting machine) houdt de steen onder een precieze hoek en index tegen een roterende slijpschijf (de lap). De mechanische precisie — tot op een tiende graad voor de hoek, tot op het punt nauwkeurig voor de index — is wat professioneel slijpwerk onderscheidt van eenvoudig uit de vrije hand slijpen. De apparatuur loopt uiteen van de eenvoudige staaf met cheater (ambachtelijk, snel maar weinig precies) tot machines met een mechanische deelkop (verwisselbaar of vast, nauwkeurig maar trager in gebruik), en computergestuurde modellen voor serieproductie.",
        machines = listOf(
            LapidaireComponent(
                nom = "Mast",
                description = "Verticale kolom bevestigd aan het frame van de machine, waarlangs de kop met de pen (quill) schuift en draait. De hoogte en de instelbare kanteling bepalen samen met de arm de facetteerhoek die op de lap wordt toegepast."
            ),
            LapidaireComponent(
                nom = "Pen (quill)",
                description = "Buis waarin de staaf met de steen (de dop) schuift. De pen draait om beurtelings elke geplande facet tegenover de schijf te brengen, en schuift naar voren om de steen aan te voeren naarmate materiaal wordt weggenomen."
            ),
            LapidaireComponent(
                nom = "Indexwiel (index gear)",
                description = "Getand wiel aan het uiteinde van de pen, doorgaans verkrijgbaar in meerdere verdelingen — 64, 72, 80, 96 of 120 standen — om alle slijpstijlen te dekken; een „snelindex”-schijf met minder standen wordt vaak over de gewone index gelegd om het positioneren te versnellen bij het slijpen van een reeks stenen. Het vastzetten van de rotatie van de steen op een precieze positie bij elke facet is onmisbaar voor de symmetrie van een slijpvorm zoals de ronde briljant, waarvan de facetten volgens een 8-voudige symmetrie zijn verdeeld. De gekozen index moet een exact veelvoud zijn van het aantal te slijpen zijden: een vijfhoek is bijvoorbeeld onmogelijk met een index 96 (96 ÷ 5 is geen geheel getal), maar lukt probleemloos met een index 80 (80 ÷ 5 = 16)."
            ),
            LapidaireComponent(
                nom = "Cheater (fijnafstelring)",
                description = "Fijnafstelring die over de op de arm aangegeven hoek wordt gelegd. De belangrijkste functie ervan is niet het wijzigen van de slijphoek zelf, maar het herstellen van de parallelliteit tussen de bewerkte facet en de lap wanneer die licht is kromgetrokken of ongelijk versleten — een afstelling die herhaald moet worden telkens wanneer een facet niet meer gelijkmatig over het hele oppervlak polijst."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Staafje (hout, aluminium of messing — die laatste twee houden de warmte van het voorverwarmen beter vast) waarop de steen wordt bevestigd voordat het in de pen wordt gestoken. De bevestiging gebeurt met verwarmde was (meest gebruikelijk, smelt rond 80 °C), cyanoacrylaatlijm („secondelijm”, snel uithardend maar met moeilijk te verwijderen resten) of epoxy (goede hechting, maar de exotherme reactie ervan moet in de gaten worden gehouden bij warmtegevoelige stenen). De meer geavanceerde dops hebben een pen, een nokje of een groef die ze altijd op dezelfde manier in de deelkop oriënteert — onmisbaar om de steen zonder verschuiving of ongewenste rotatie van de ene dop naar de andere over te brengen (de kroon slijpen na het paviljoen)."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Roterende metalen schijf, meestal 6\" (≈152 mm) of 8\" (≈203 mm) in diameter, waarop het slijpmiddel is bevestigd of aangebracht. Er wordt onderscheid gemaakt tussen lappen voor grofslijpen/facetteren (galvanisch diamant of gietijzer geïmpregneerd met diamantdeeltjes door walsen) en zachtere polijstlappen. Een machine heeft doorgaans meerdere verwisselbare lappen, één per korrelfase."
            ),
            LapidaireComponent(
                nom = "Goniometer",
                description = "Controle-instrument dat de werkelijke hoek van een reeds geslepen steen meet op twee tegenoverliggende hoofdfacetten, om de bereikte slijpvorm te verifiëren. De gemeten hoek (C' voor de kroon, P' voor het paviljoen) wordt omgezet met de formule hoek = (180° − gemeten hoek) / 2, aangezien de goniometer de aanvullende hoek meet die met het rondistevlak wordt gevormd."
            )
        ),
        disquesTitle = "Vorm- en polijstschijven",
        disquesIntro = "Het slijpen verloopt in steeds fijnere korrelfasen, waarbij elke fase de microkrasjes van de vorige verwijdert; het overslaan van een fase laat sporen achter die de eindpolitoer niet meer kan wegwerken. De rotatiesnelheid van de lap volgt deze opbouw: vrij traag bij het grofslijpen (100 tot 300 tpm), sneller bij het facetteren (300 tot 600 tpm), en het snelst bij het polijsten (700 tot 1000 tpm of meer) — steeds afhankelijk van de gevoeligheid van de steen, zonder aarzelen vertraagd voor een zachte of splijtbare steen.",
        disques = listOf(
            LapidaireDisc(
                nom = "Grofslijpen",
                grain = "260 tot 325",
                usage = "Verwijdert snel materiaal om de steen zijn algemene vorm (voorvorm) te geven vanuit het ruwe materiaal of een gezaagde plak. Agressieve diamantlap, stevige druk, continue waterkoeling verplicht."
            ),
            LapidaireDisc(
                nom = "Facetvorming",
                grain = "600 tot 1200",
                usage = "Tekent en verfijnt elke facet op de exacte hoek en index uit het slijpdiagram. De langste en meest bepalende fase voor de symmetrie en de precisie van de ontmoetingspunten (meet points) tussen naburige facetten."
            ),
            LapidaireDisc(
                nom = "Voorpolitoer",
                grain = "3000 tot 8000",
                usage = "Verfijnt het oppervlak van de vormfase tot een bijna matte, krasvrije afwerking, met behoud van exact dezelfde hoeken — elke hoekcorrectie in deze fase moet minimaal blijven, anders moet er teruggegaan worden naar een grovere korrel."
            ),
            LapidaireDisc(
                nom = "Eindpolitoer",
                grain = "Submicron pasta of poeder (vaak ceriumoxide of zeer fijn diamant)",
                usage = "De polijstlap wordt gekozen op hardheid (schaal van Mohs), van hardst naar zachtst: keramiek (9), koper (3), zink (2,5), fenolhars (2,2), plexiglas of lood-tin (2), zuiver tin (1,7), lood (1,5), pvc (1) en was met diverse vulstoffen (0,3, variabel per samenstelling). Nooit is het de lap zelf die polijst, alleen het erop aangebrachte poeder werkt als slijpmiddel — de lap is slechts een drager waarvan de hardheid moet overeenkomen met die van de steen. Droog polijsten (poeder vastgehouden door een vettig bindmiddel) of nat (poeder aangelengd met water, continu aangebracht), afhankelijk van de gekozen methode."
            )
        ),
        indexTitle = "Keuze van het indexwiel",
        indexIntro = "Een indexwiel wordt vooral gekozen op basis van het aantal te slijpen zijden: dat aantal moet er een exact veelvoud van zijn (een vijfhoek is onmogelijk met een index 96, maar wel haalbaar met een index 80). De rotatie per stand is omgekeerd evenredig met het aantal standen van de index (360° gedeeld door het aantal standen) — een index 120 gaat 3° per stand vooruit, een index 32 11,25°. Een slijpschema van de ene index naar de andere omrekenen kan alleen als beide indexen veelvouden of delers van elkaar zijn: deel of vermenigvuldig dan simpelweg elk indexnummer met hun verhouding (bijvoorbeeld delen door 3 om van een index 96 naar een index 32 te gaan). Omdat alle gangbare indexen veelvouden van 4 zijn, kan een vierkant op elk ervan geslepen worden; het is echter onmogelijk om te indexeren op een positie tussen twee standen in, wat de meeste veelhoeken met een oneven aantal zijden meteen uitsluit. De index 96 blijft de meest voorkomende bij fabrikanten, met een goede balans tussen fijne gradatie en snelle instelling; de index 120 is de meest complete, ten koste van een tragere instelling.",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11,25° / stand", cotesTaillables = "Vierkant (4), achthoek (8), zestienhoek (16)"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6° / stand", cotesTaillables = "Driehoek (3), vierkant (4), vijfhoek (5), zeshoek (6), tienhoek (10), twaalfhoek (12), vijftienhoek (15), twintighoek (20)"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5,625° / stand", cotesTaillables = "Vierkant (4), achthoek (8), zestienhoek (16)"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5° / stand", cotesTaillables = "Driehoek (3), vierkant (4), zeshoek (6), achthoek (8), negenhoek (9), twaalfhoek (12), achttienhoek (18)"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4,5° / stand", cotesTaillables = "Vierkant (4), vijfhoek (5), achthoek (8), tienhoek (10), zestienhoek (16), twintighoek (20)"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3,75° / stand", cotesTaillables = "De meest gangbare — in totaal 9 regelmatige veelhoeken slijpbaar, waaronder vierkant, zeshoek, achthoek, twaalfhoek"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3° / stand", cotesTaillables = "De meest complete — in totaal 14 regelmatige veelhoeken slijpbaar, het maximum onder de gangbare indexen"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2,8125° / stand", cotesTaillables = "Vierkant (4), achthoek (8), zestienhoek (16), 32 zijden, 64 zijden")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "Geschat gewicht",
            intro = "Voor een steen die al geslepen en gezet is, is direct wegen onmogelijk: het gewicht wordt afgeleid uit het volume, op basis van schuifmaatmetingen (nauwkeurigheid 1/100 mm) en het soortelijk gewicht van de soort. Een indicatieve formule, met een nauwkeurigheid van ongeveer 10-15% — geen echte weging.",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "Rond",
                LapidaireCutShape.OVALE to "Ovaal",
                LapidaireCutShape.COUSSIN_CARRE to "Vierkant kussen",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "Rechthoekig kussen",
                LapidaireCutShape.CARRE_A_GRADIN to "Vierkante trapslijpvorm",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "Rechthoekige trapslijpvorm",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "Vierkant kussen (trapslijp)",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "Rechthoekig kussen (trapslijp)",
                LapidaireCutShape.MARQUISE to "Marquise",
                LapidaireCutShape.POIRE to "Peer",
                LapidaireCutShape.TRIANGLE_BOMBE to "Bomberende driehoek",
                LapidaireCutShape.TRIANGLE to "Driehoek",
                LapidaireCutShape.TRAPEZE to "Trapezium",
                LapidaireCutShape.COEUR to "Hart"
            ),
            dimension1Label = "Lengte of diameter (mm)",
            dimension2Label = "Breedte (mm)",
            heightLabel = "Totale diepte, tafel tot culet (mm)",
            sgLabel = "Soortelijk gewicht van de soort",
            computeLabel = "Berekenen",
            resultLabel = "Geschat gewicht: %s karaat",
            disclaimer = "Schatting op basis van volume, indicatieve nauwkeurigheid 10-15% — vervangt geen echte weging.",
            errorMessage = "Voer geldige waarden (positieve getallen) in voor alle afmetingen en het soortelijk gewicht."
        ),
        anglesTitle = "Referentiehoeken: de ronde briljant",
        anglesIntro = "Onderstaande verhoudingen zijn die welke de Belgische wiskundige Marcel Tolkowsky in 1919 publiceerde: hij berekende de optimale hoek om lichtterugkaatsing (schittering) en dispersie (vuur) van een rond geslepen briljant te maximaliseren — het historische referentiepunt dat nog steeds als uitgangspunt dient, hoewel moderne laboratoria (met name GIA) een tolerantiemarge rond deze waarden aanvaarden in plaats van één vast cijfer. Voor gekleurde stenen, met een andere brekingsindex dan diamant, ligt de klassieke verhouding tussen kroon en rondiste eerder tussen 25/75% en 30/70%. Dwingende regel, ongeacht de steen: slijp een paviljoenfacet nooit onder de kritieke hoek van de soort (het licht ontsnapt dan via het paviljoen, het „venstereffect”), en nooit boven de uitdovingshoek (donkere zones). Om een reeds geslepen steen te controleren, meet een goniometer de hoek op twee tegenoverliggende hoofdfacetten: de werkelijke kroon- of paviljoenhoek wordt uit de aflezing afgeleid met de formule (180° − gemeten hoek) / 2. Andere slijpvormen (prinses, ovaal, peer, smaragd, kussen...) volgen elk hun eigen diagram, dat sterk verschilt per ontwerper en doel (materiaalopbrengst tegenover optische prestatie): er bestaat geen vergelijkbare universele waarde die zonder verwijzing naar een precies diagram kan worden aangehaald.",
        angles = listOf(
            LapidaireAngles(
                coupe = "Ronde briljant (Tolkowsky, 1919)",
                couronne = "34,5°",
                pavillon = "40,75°",
                table = "53% van de rondistediameter",
                facettes = "57 of 58 (33 op de kroon, 24 of 25 op het paviljoen, 8-voudige symmetrie)",
                source = "Marcel Tolkowsky, „Diamond Design”, 1919"
            )
        ),
        optiqueTitle = "De kritieke hoek en de uitdovingshoek",
        optiqueIntro = "Er zijn schematisch drie gevallen te onderscheiden voor de weg van het licht dat via de tafel binnenkomt: een te ondiep paviljoen laat het rechtstreeks onderaan ontsnappen; een te diep paviljoen vangt het in interne reflecties die via een zijfacet naar buiten treden zonder naar het oog terug te keren, waardoor de steen dof oogt ondanks een perfecte politoer; een goed geproportioneerd paviljoen stuurt het daarentegen via de kroon terug naar de kijker, voor maximale schittering. Het paviljoen van een geslepen steen werkt als een spiegel: onder de kritieke hoek van de soort ontsnapt licht dat via de tafel binnenkomt door het paviljoen (het „venstereffect” hierboven al behandeld); precies op de kritieke hoek geslepen ontstaat een ander gebrek, het „vissenoog” — de tafel oogt dof terwijl de contour van de kroon helder blijft, omdat de stralen die erdoorheen gaan de wand van de reflectiekegel raken zonder er echt op te reflecteren. Er bestaat ook een bovengrens, de uitdovingshoek, waarboven het paviljoen opnieuw licht verliest via de tegenoverliggende facet: uitdovingshoek = 60° − (kritieke hoek / 3). Een goed geslepen paviljoen houdt zich dus aan kritieke hoek < paviljoenhoek < uitdovingshoek; hoe hoger de brekingsindex van de edelsteen, hoe breder deze werkmarge wordt — een diamant vergeeft veel meer afwijking dan een fluoriet. De kroon volgt een aanvullende regel: de maximale hoek ervan is omgekeerd evenredig met die van het paviljoen (een korter paviljoen staat een hogere kroon toe) en recht evenredig met de brekingsindex; enkele tienden van een graad onder deze maxima werken blijft de veiligste keuze. Al deze slijpschema's steunen op de Meetpoint-techniek (Amerikaanse methode, Long & Steele), die de snijpunten van drie of meer facetten als referentiepunten gebruikt om automatisch goede verhoudingen en een goed gewichtsbehoud ten opzichte van het ruwe materiaal te waarborgen; om het gewicht van een reeds geslepen, nog gevatte steen te schatten, geldt gewoonlijk de formule gewicht (in karaat) = breedte³ × volumecoëfficiënt van de slijpvorm × soortelijk gewicht van de soort / 200.",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "Diamant", angleCritique = "24,4°", angleExtinction = "51,85°"),
            LapidaireOptiqueEntry(pierre = "Sfeen (titaniet)", angleCritique = "31,76°", angleExtinction = "49,41°"),
            LapidaireOptiqueEntry(pierre = "Zirkoon (hoog)", angleCritique = "31,3°", angleExtinction = "49,57°"),
            LapidaireOptiqueEntry(pierre = "Demantoïdgranaat", angleCritique = "32,62°", angleExtinction = "49,13°"),
            LapidaireOptiqueEntry(pierre = "Alexandriet (chrysoberil)", angleCritique = "34,94°", angleExtinction = "48,35°"),
            LapidaireOptiqueEntry(pierre = "Robijn en saffier (korund)", angleCritique = "34,58°", angleExtinction = "48,47°"),
            LapidaireOptiqueEntry(pierre = "Spinel", angleCritique = "35,74°", angleExtinction = "48,09°"),
            LapidaireOptiqueEntry(pierre = "Peridoot", angleCritique = "37,2°", angleExtinction = "47,6°"),
            LapidaireOptiqueEntry(pierre = "Toermalijn", angleCritique = "38,01°", angleExtinction = "47,33°"),
            LapidaireOptiqueEntry(pierre = "Topaas", angleCritique = "38,15°", angleExtinction = "47,28°"),
            LapidaireOptiqueEntry(pierre = "Beril", angleCritique = "39,35°", angleExtinction = "46,88°"),
            LapidaireOptiqueEntry(pierre = "Smaragd", angleCritique = "39,72°", angleExtinction = "46,76°"),
            LapidaireOptiqueEntry(pierre = "Aquamarijn", angleCritique = "39,75°", angleExtinction = "46,75°"),
            LapidaireOptiqueEntry(pierre = "Amethist en kwarts", angleCritique = "40,37°", angleExtinction = "46,54°"),
            LapidaireOptiqueEntry(pierre = "Calciet", angleCritique = "42,29°", angleExtinction = "45,9°"),
            LapidaireOptiqueEntry(pierre = "Fluoriet", angleCritique = "44,21°", angleExtinction = "45,26°")
        ),
        defautsTitle = "Veelvoorkomende gebreken en correcties",
        defautsIntro = "De meeste slijpgebreken komen eerder voort uit een onjuist ingestelde hoek, een verkeerde indexering of een slecht uitgelijnde dopoverdracht dan uit een probleem met de lap. De basisregel om een slechte aansluiting of een ongelijke politoer te corrigeren: altijd ingrijpen aan de kant tegenover het gebrek.",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "Facet uit het midden of slecht gepolijst aan één kant",
                cause = "Lichte verkeerde uitlijning van de steen op de cheater.",
                remede = "Draai de cheater om de steen naar de kant tegenover het gebrek te kantelen."
            ),
            LapidaireDefaut(
                probleme = "Facet sluit slecht aan of polijst slecht bovenaan, hoek te groot",
                cause = "De hoek op de hoekmeter is hoger dan hij zou moeten zijn.",
                remede = "Verlaag de hoek op de hoekmeter lichtjes om de steen naar beneden te kantelen."
            ),
            LapidaireDefaut(
                probleme = "Facet sluit slecht aan of polijst slecht onderaan, hoek te klein",
                cause = "De hoek op de hoekmeter is lager dan hij zou moeten zijn.",
                remede = "Verhoog de hoek op de hoekmeter lichtjes om de steen naar boven te kantelen."
            ),
            LapidaireDefaut(
                probleme = "Gecombineerd diagonaal gebrek (bijvoorbeeld linksboven)",
                cause = "De steen kan tijdens de overdracht licht gedraaid zijn op de dop.",
                remede = "Combineer een correctie op de cheater met een correctie op de hoek, en verlaag zo nodig de rotatiesnelheid van de lap tot de controle is hersteld."
            ),
            LapidaireDefaut(
                probleme = "De steen piept op de polijstlap",
                cause = "De bewerkte facet ligt niet parallel aan de lap.",
                remede = "Stop en corrigeer de positie van de steen voordat u verdergaat — een betrouwbaar signaal dat nooit genegeerd mag worden."
            ),
            LapidaireDefaut(
                probleme = "Evenwijdige krasjes over meerdere facetten",
                cause = "De lap is verontreinigd met korrels van een andere korrelgrootte.",
                remede = "Ontsmet de lap (borstelen met heet zeepwater, indien nodig afschrapen of africhten) voordat u verdergaat."
            ),
            LapidaireDefaut(
                probleme = "„Brand”-sporen op het oppervlak van een facet",
                cause = "Te stationair polijsten met te veel druk, waardoor plaatselijke oververhitting ontstaat.",
                remede = "Beweeg de facet regelmatig over de lap, verminder de druk, voeg zo nodig een druppel water per seconde toe."
            ),
            LapidaireDefaut(
                probleme = "Er verschijnt een kleine holte tijdens het slijpen",
                cause = "Een insluitsel dicht bij het oppervlak is blootgelegd.",
                remede = "Insluitsels beter „opruimen” al bij het grofslijpen blijft de beste preventie; eenmaal het gaatje verschenen is, plaatselijk naslijpen of zo nodig een volledige herslijping overwegen."
            )
        ),
        especesTitle = "Praktische soortenfiches",
        especesIntro = "Naast de kritieke hoek en de uitdovingshoek die hierboven al gegeven zijn, heeft elke soort zijn eigen slijpgewoontes: tafeloriëntatie op basis van pleochroïsme of splijting, bijzondere gevoeligheden, en de schijf en polijstpoeder die de beste resultaten geven. Op de praktijk gebaseerde aanwijzingen, geen universele regel — elke steen behoudt zijn eigen bijzonderheden (insluitsels, zonering, staat van het ruwe oppervlak).",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "Diamant",
                orientation = "Isotroop: geen oriëntatiebeperking gekoppeld aan een optische as.",
                fragilite = "Perfecte splijting volgens 4 octaëdrische vlakken, door slijpers goed begrepen en benut in plaats van louter ondergaan; de paviljoenpunt blijft het meest stootgevoelige punt.",
                polissage = "Gietijzeren schijf (scaife) beladen met diamantpoeder — alleen diamant polijst diamant."
            ),
            LapidaireEspeceFiche(
                pierre = "Korund (Robijn, Saffier)",
                orientation = "Geen splijting om rekening mee te houden bij de oriëntatie.",
                fragilite = "Hardheid 9, een van de hardste geslepen stenen: weinig bijzondere voorzorgen nodig.",
                polissage = "Uitstekende resultaten op een koperen schijf met diamantpoeder."
            ),
            LapidaireEspeceFiche(
                pierre = "Beryl (Smaragd, Aquamarijn, Morganiet, Heliodoor)",
                orientation = "Onvolkomen splijting, geen echte oriëntatiebeperking.",
                fragilite = "Vooral smaragd vraagt aandacht voor de jardin (insluitsels), die de steen zowel bij het slijpen als bij het polijsten kunnen doen breken.",
                polissage = "Azijnzuur in het koelwater verbetert het polijsten; aluminium-, cerium- of tinoxidepoeder, of Linde A."
            ),
            LapidaireEspeceFiche(
                pierre = "Kwarts (Amethist, Citrien, Bergkristal, Rookkwarts)",
                orientation = "Geen splijting; houd bij de oriëntatie rekening met kleurzonering.",
                fragilite = "Geen bijzondere splijtingsgevoeligheid.",
                polissage = "Het resultaat houdt soms onverwachte verrassingen in, zonder aanwijsbare oorzaak."
            ),
            LapidaireEspeceFiche(
                pierre = "Topaas",
                orientation = "Verschuif de tafel ongeveer 10° ten opzichte van het basale splijtingsvlak, om dit niet direct in de as bloot te leggen.",
                fragilite = "Duidelijke splijting in één richting (basaal vlak) — eenvoudig te slijpen zodra men dit vermijdt.",
                polissage = "Geen specifieke schijfaanbeveling naast de algemene regels."
            ),
            LapidaireEspeceFiche(
                pierre = "Toermalijn",
                orientation = "Slijp de tafel bij voorkeur parallel aan de optische as; een veelkleurige steen breekt netjes af op de overgang tussen twee kleuren — daar beter lijmen dan zagen.",
                fragilite = "Gevoelig voor hitte en stoten tijdens het slijpen.",
                polissage = "Geen specifieke schijfaanbeveling naast de algemene regels."
            ),
            LapidaireEspeceFiche(
                pierre = "Granaat (Almandien, Demantoïde, Grossulaar, Pyroop, Spessartien, Uvarovite)",
                orientation = "Isotroop: tafeloriëntatie zonder beperking, geen voorkeursrichting.",
                fragilite = "Geen splijtingsgerelateerd broosheidsprobleem.",
                polissage = "Azijnzuur (azijn) in het koelwater verbetert het resultaat stelselmatig; cerium-, tin- of aluminiumoxidepoeder."
            ),
            LapidaireEspeceFiche(
                pierre = "Spinel",
                orientation = "Isotroop, geen oriëntatiebeperking.",
                fragilite = "Geen splijtingsgerelateerd broosheidsprobleem.",
                polissage = "Polijst opmerkelijk goed op een koperen schijf met diamantpasta."
            ),
            LapidaireEspeceFiche(
                pierre = "Peridoot (Olivijn)",
                orientation = "Zeer zwak pleochroïsme, weinig beperkende oriëntatie.",
                fragilite = "Geen noemenswaardige splijtingsgevoeligheid.",
                polissage = "Het resultaat houdt soms onverwachte verrassingen in; diamantpoeder werkt goed."
            ),
            LapidaireEspeceFiche(
                pierre = "Fluoriet",
                orientation = "Splijt gemakkelijk in 4 richtingen: kies de oriëntatie zorgvuldig om het risico te beperken.",
                fragilite = "Een van de meest gesloten kritieke hoeken onder de geslepen stenen, zeer gevoelig voor stoten en afsplinteren.",
                polissage = "Bij voorkeur een wassen schijf; zuur verbetert het resultaat."
            ),
            LapidaireEspeceFiche(
                pierre = "Calciet",
                orientation = "Zorgvuldig oriënteren om de splijtingsvlakken niet direct bloot te leggen.",
                fragilite = "Zeer lage hardheid (3) en perfecte splijting in 3 richtingen, sterk hittegevoelig — een steen die moeilijk te slijpen en te polijsten is; laat de schijf langzaam draaien (ongeveer 100 tpm).",
                polissage = "Houten of wassen schijf, tin- of chroomoxidepoeder, met enkele druppels oxaalzuur."
            ),
            LapidaireEspeceFiche(
                pierre = "Chrysoberyl (Alexandriet)",
                orientation = "Geen oriëntatiebeperking gekoppeld aan splijting.",
                fragilite = "Hoge hardheid (8,5), een harde steen zonder bijzonder probleem.",
                polissage = "Polijst snel met zuur en Linde A-poeder; uitstekende resultaten op een koperen schijf met diamantpoeder."
            )
        ),
        diagrammesTitle = "Diagrammen",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Verhoudingsdiagram van de ronde briljant: naamgeving van kroon, rondiste en paviljoen."
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "Diagram van het lichtpad afhankelijk van de paviljoendiepte: te ondiep, goed geproportioneerd, of te diep."
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "Oude gravure van een diamantslijpmolen — de mechaniek van de roterende schijf, in principe al eeuwenlang ongewijzigd."
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "Een moderne facetteermachine: mast, pen en lap, zoals hierboven beschreven."
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "Stadia van de omvorming van een ruwe steen tot een geslepen edelsteen."
            )
        ),
        tipsTitle = "Praktische tips",
        tips = listOf(
            LapidaireTip(texte = "Controleer altijd de kalibratie van de op de arm aangegeven hoek met een teststeen voordat u aan een waardevol stuk begint: een afwijking van 0,5° op het paviljoen volstaat om een verder goed geslepen steen zichtbaar te verduisteren."),
            LapidaireTip(texte = "Centreer de steen zorgvuldig op de dop: een lichte ontregeling vertaalt zich in een ongelijkmatige rondistedikte en facetten die niet op hetzelfde punt rondom samenkomen."),
            LapidaireTip(texte = "Houd een gematigde rondistedikte aan (geen breekbaar „scheermesje” dat gevoelig is voor schokken, maar ook niet overmatig dik, wat materiaal opsluit zonder optisch voordeel) — een regelmatige, zichtbare rondistelijn, niet te dun en niet te dik, is een van de kenmerken van zorgvuldig werk."),
            LapidaireTip(texte = "Controleer de ontmoetingspunten (meet points) tussen facetten in elke fase met een loep bij strijklicht: een gemist ontmoetingspunt is tijdens het vormen makkelijk te corrigeren, nauwelijks meer zodra de voorpolitoer is begonnen."),
            LapidaireTip(texte = "Pas de smering aan het lapmateriaal aan: water is geschikt voor de meeste galvanische diamantlappen, terwijl sommige polijstlappen (met name tin) betere resultaten geven met oliegebaseerde smering of een specifiek mengsel — volg de aanbevelingen van de lapfabrikant."),
            LapidaireTip(texte = "Controleer de warmtegevoeligheid en de splijting van de soort vóór het slijpen: sommige stenen (topaas, kunziet) hebben een duidelijke splijting die zich onder de hitte of druk van een te agressief grofslijpen kan voortzetten."),
            LapidaireTip(texte = "Test de uiteindelijke symmetrie door de steen van voren onder een puntvormige lichtbron te bekijken: de reflecties van de kroonfacetten moeten een regelmatig patroon vormen; een asymmetrie die in deze fase met het blote oog zichtbaar is, kan niet meer door polijsten worden verholpen."),
            LapidaireTip(texte = "Leg de steen nooit op een stilstaande lap om die daarna te starten: laat de lap altijd eerst draaien voordat u de steen aanbiedt, met een lichte pendelbeweging zodat hij niet steeds op dezelfde plek blijft — dat slijt de schijf ongelijk en de steen zelf onregelmatig."),
            LapidaireTip(texte = "Een tafel die „dof” lijkt terwijl de omtrek van de steen wel glanst, wijst op een venstereffect: het paviljoen is te ondiep en laat het licht er onderaan uit ontsnappen in plaats van het via de tafel terug te kaatsen — corrigeer door de paviljoenhoek te verdiepen, zonder ooit de uitdovingshoek te overschrijden."),
            LapidaireTip(texte = "Bij langwerpige vormen (ovaal, markies, peer, kussen...) verraadt een donker kruis zichtbaar in het midden (het zogenoemde „X+-effect”) lengte- en breedtefacetten die slecht op elkaar aansluiten op het paviljoen; een goed doordacht slijpdiagram voor de gekozen vorm voorkomt dit beter dan een aanpassing per geval.")
        ),
        conservationTitle = "Bewaren en hanteren van geslepen stenen",
        conservationIntro = "Een goed geslepen steen blijft kwetsbaar eenmaal gezet of opgeborgen: een paar eenvoudige voorzorgsmaatregelen voorkomen de meeste krassen en onbedoelde schade.",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "Reiniging",
                conseil = "Veeg een steen nooit droog af: stof werkt als schuurmiddel en krast het gepolijste oppervlak. Was met lauw zeepwater en een zachte borstel, of met alcohol voor een snelle ontvetting, en droog met een pluisvrije doek."
            ),
            LapidaireConservationTip(
                titre = "Afzonderlijk bewaren",
                conseil = "Bewaar elke steen apart, in een papieren vouw of een individueel zakje: zelfs kort contact tussen stenen krast ze onderling."
            ),
            LapidaireConservationTip(
                titre = "Nooit hardheden mengen",
                conseil = "Bewaar nooit stenen van verschillende hardheid samen in hetzelfde vakje: de hardere krast systematisch de zachtere, zelfs door eenvoudige wrijving tijdens het transport."
            ),
            LapidaireConservationTip(
                titre = "Temperatuurschokken en chemicaliën",
                conseil = "Vermijd plotselinge temperatuurschommelingen en agressieve chemicaliën (bleekwater, zuren), die bepaalde stenen kunnen doen barsten of een behandeling kunnen aantasten (geoliede smaragd, geïmpregneerde steen). Een ultrasoonreiniger is af te raden bij gebarsten, geoliede of breekbare stenen."
            ),
            LapidaireConservationTip(
                titre = "Blootstelling aan licht",
                conseil = "Sommige stenen zijn lichtgevoelig: amethist of kunziet die lang aan fel licht wordt blootgesteld, kan verbleken. Bewaar gevoelige stenen uit de buurt van langdurige blootstelling."
            ),
            LapidaireConservationTip(
                titre = "Vervoer en hantering",
                conseil = "Gebruik voor vervoer een gevoerd zakje en vermijd direct contact tussen meerdere stenen of sieraden die samen worden bewaard; vastpakken bij de zetting of de tafel in plaats van bij het paviljoen, dat gevoeliger is voor stoten."
            )
        ),
        disclaimerTitle = "Een vak dat je in de werkplaats leert",
        disclaimerBody = "Dit overzicht geeft algemene richtlijnen, geen volledige handleiding: facetteren wordt geleerd door begeleide praktijk, met geschikte apparatuur en veiligheidsvoorschriften (oog- en ademhalingsbescherming, continue lapkoeling) die per werkplaats en per machine verschillen. De getoonde diagrammen zijn afkomstig uit echte, vrij te gebruiken bronnen (zie credits); zolang ze tijdelijk ontbreken, wordt alleen het onderschrift getoond.",
        machinesTypesTitle = "De machines van het vak",
        machinesTypesIntro = "Naast de hierboven beschreven onderdelen van een facetteermachine volgt hier een overzicht van de verschillende soorten machines die daadwerkelijk in het atelier worden gebruikt, zowel voor het facetteren als voor het cabochonslijpen, met hun kenmerken en de bijbehorende techniek. De genoemde merken dienen enkel als representatief voorbeeld van hun categorie, zonder commerciële band met Gems of Rod.",
        categorieFacettageLabel = "Facetteren",
        categorieCabochonLabel = "Cabochonslijpen",
        categoriePolyvalentLabel = "Veelzijdig",
        caracteristiquesLabel = "Kenmerken",
        techniqueLabel = "Techniek",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "Handstok met meter (jam-peg)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Het eenvoudigste en oudste facetteergereedschap: de steen wordt gelijmd aan het uiteinde van een staafje dat met de hand tegen de schijf wordt gehouden, waarbij de hoek op het oog of met een eenvoudige meter wordt ingesteld. Wordt nog steeds onderwezen in traditionele slijpscholen (Sri Lanka, Thailand).",
                caracteristiques = listOf(
                    "Geen mechanisch indexeeronderdeel",
                    "Nagenoeg geen kosten",
                    "Volledig afhankelijk van de vaardigheid van de slijper",
                    "Hoog werktempo in ervaren handen"
                ),
                technique = "De hand van de slijper past voortdurend de hoek en de druk tegen de schijf aan; de precisie van de facetten hangt af van de herhaling van de beweging, niet van de machine."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "Facetteermachine met verwisselbare mechanische indexkop",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Standaard in westerse ateliers sinds de jaren 1970 (Facetron, Ultra Tec, Poly-Metric...): een verwisselbare indexkop stelt de hoek en de rotatie van de steen tot op een tiende graad nauwkeurig in, terwijl de arm verticaal schuift om de zaagdiepte aan te passen.",
                caracteristiques = listOf(
                    "Hoekprecisie tot op een tiende graad",
                    "Verwisselbare indexkoppen (facetten, schubben, cavetto)",
                    "Micrometrische diepteaanslag",
                    "Aanzienlijke investering (machine + schijven)"
                ),
                technique = "De slijper stelt de hoek en de indexstand in vóór elk facet en laat de steen dan tegen de schijf zakken tot aan de ingestelde aanslag: reproduceerbaarheid vervangt de vrije handbeweging van de handstok."
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "Facetteermachine met vast geïntegreerde indexkop",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Betaalbaarder versie waarbij de indexkop vast aan de arm zit in plaats van verwisselbaar te zijn, met een bij de fabricage vastgelegd aantal gegraveerde standen (vaak 64, 96 of 120 naargelang het model).",
                caracteristiques = listOf(
                    "Instapprijs ruim onder die van verwisselbare koppen",
                    "Beperkt en niet aanpasbaar aantal indexstanden",
                    "Goede robuustheid bij regelmatig gebruik",
                    "Geschikt voor het leerproces en veelvoorkomende slijpvormen"
                ),
                technique = "Hetzelfde principe als de verwisselbare kop, maar de keuze aan facetteerpatronen beperkt zich tot de op de machine gegraveerde indexverdelingen — voldoende voor de meeste klassieke slijpvormen (rond, ovaal, cushion)."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "Computergestuurde facetteermachine (CNC)",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "Digitaal aangestuurde machine die een slijpplan (hoek- en indexdiagram) identiek reproduceert op een reeks stenen, gebruikt in industriële productie of voor zeer complexe fantasieslijpvormen.",
                caracteristiques = listOf(
                    "Perfecte reproduceerbaarheid van steen tot steen",
                    "Programmering vanuit een digitaal slijpplan",
                    "Hoge investering, voorbehouden aan productie op grote schaal",
                    "Vermindert het aandeel van handwerk in het eindresultaat"
                ),
                technique = "Het slijpplan wordt geladen in de software van de machine, die automatisch de geprogrammeerde hoeken en indexposities doorloopt; de rol van de lapidarist verschuift naar instelling en kwaliteitscontrole."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "Meerschijfs cabochonslijpmachine in lijnopstelling",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Referentieapparaat voor het cabochonslijpen (type Genie, CabKing): 6 tot 8 diamantschijven met afnemende korrelgrofte, gevolgd door viltschijven om te polijsten, uitgelijnd op één frame met continue waterspoeling.",
                caracteristiques = listOf(
                    "6 tot 8 stations met afnemende korrelgrofte op dezelfde as",
                    "Continue waterspoeling op elke schijf",
                    "Snelle overgang van het ene station naar het andere zonder schijfwissel",
                    "Hoge prijs, maar zeer goede levensduur"
                ),
                technique = "De cabochon wordt eerst ruw gevormd en daarna verfijnd door van schijf naar schijf te gaan met afnemende korrelgrofte, tot aan de laatste polijstvilten toe — elk station verwijdert de microkrasjes die het vorige achterliet."
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Vevor-cabochonslijpmachine (meerschijfs, instapmodel)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Voordelige uitvoering van de meerschijfs cabochonslijpmachine, gemaakt in China onder het merk Vevor: erg verspreid onder beginners en kleine ateliers dankzij een prijs die duidelijk lager ligt dan gespecialiseerde merken (Genie, CabKing).",
                caracteristiques = listOf(
                    "6 tot 8 diamantschijven + vilten op dezelfde as, zoals bij professionele modellen",
                    "Motor en lagers van instapniveau, ruimere mechanische toleranties",
                    "Ingebouwde wateropvangbak",
                    "Duidelijk goedkoper dan gespecialiseerde merken, ten koste van een kortere levensduur"
                ),
                technique = "Zelfde principe van schijf-voor-schijf-progressie als bij een professionele cabochonslijpmachine, maar met meer trillingen: een lichtere werkdruk en een frequenter onderhoud van de lagers compenseren de minder nauwkeurige mechaniek."
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "Gecombineerde slijp-polijstmachine met horizontale as",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Meer ambachtelijke versie: één of twee horizontale assen waarop naar behoefte zelf schijven (slijpschijven, vilten) worden gemonteerd, in plaats van een reeks vaste stations.",
                caracteristiques = listOf(
                    "Verwisselbare schijven naar keuze van de lapidarist",
                    "Ook gebruikt om een ruwe steen voor te vormen vóór het zagen",
                    "Goedkoper dan een speciale meerschijfs cabochonslijpmachine",
                    "Vereist meer handelingen tussen de stappen"
                ),
                technique = "De lapidarist verwisselt zelf de op de as gemonteerde schijf bij elke korrelstap, in tegenstelling tot de meerschijfsmachine waar de stations vast en naast elkaar staan."
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "Zaagmachine (trim saw)",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Kleine cirkelzaag met diamantblad, gebruikt voorafgaand aan het cabochonslijpen om de ruwe steen te versnijden in plakken (slabs) van de gewenste dikte vóór het vormgeven op de schijven.",
                caracteristiques = listOf(
                    "Diamantblad gekoeld door een olie- of waterbad",
                    "Gangbare diameter van 10 tot 25 cm naargelang het model",
                    "Verstelbare zaaggeleider voor gelijkmatige plakken",
                    "Onmisbare voorafgaande stap, geen facetteerwerk"
                ),
                technique = "De ruwe steen wordt handmatig tegen het langzaam draaiende blad aangevoerd; de dikte van de verkregen plak bepaalt rechtstreeks de maximale dikte van de toekomstige cabochon."
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "Edelsteenboormachine",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "Kolomboormachine uitgerust met holle diamantboortjes en een spoelsysteem, gebruikt om parels en cabochons te doorboren die bedoeld zijn om als hanger gezet of geregen te worden.",
                caracteristiques = listOf(
                    "Holle diamantboortjes met verschillende diameters",
                    "Waterkoeling verplicht om scheuren te voorkomen",
                    "Instelbare rotatiesnelheid naargelang de hardheid van de steen",
                    "Tweezijdig boren bij kwetsbare stenen"
                ),
                technique = "Het boren gebeurt op lage snelheid en onder constante waterspoeling, vaak door de steen vanaf beide zijden aan te vallen om de typische uitgangsafsplintering van een enkelvoudige doorboring te vermijden."
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "Gecombineerde slijpsteen voor facetteren/cabochonslijpen, instapmodel",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "Kleine, goedkope machine die schijven en vilten op dezelfde as combineert om zowel het cabochonslijpen als een eenvoudig facetteerwerk uit te proberen, zonder precieze indexkop.",
                caracteristiques = listOf(
                    "Zeer lage instapprijs, compact formaat",
                    "Combineert meerdere toepassingen in één machine",
                    "Geen precieze indexkop voor echt facetteerwerk",
                    "Geschikt om te ontdekken, beperkt voor een professioneel resultaat"
                ),
                technique = "De beginner probeert beide disciplines uit op hetzelfde frame, ten koste van een hoek- en indexprecisie die ver onder die van een gespecialiseerde machine ligt."
            )
        )
    )

    private val zh = LapidairePage(
        intro = "宝石切磨师将原石加工、抛光成琢磨宝石。这一行业涵盖多个专业方向——针对透明宝石的刻面切磨（faceting）、针对不透明或半透明宝石的凸圆面（cabochon）切磨，以及雕刻和装饰性加工——但技术含量最高的刻面切磨，正是赋予宝石光彩的关键工艺。本篇介绍这一行业的基本设备与参考要点，面向专业人士和有经验的爱好者，而非无人指导的初学者。",
        machinesTitle = "刻面切磨机",
        machinesIntro = "刻面切磨机（faceting machine）以精确的角度和刻度索引将宝石抵住旋转的磨盘（lap）。其机械精度——角度精确到十分之一度，索引精确到点位——正是专业切磨区别于徒手打磨的关键。设备种类从简单的带微调器手工杆（手工制作，速度快但精度低），到配备机械分度头（可拆卸式或固定式，精度高但加工较慢）的切磨机，再到用于批量切磨的电脑控制机型，各不相同。",
        machines = listOf(
            LapidaireComponent(
                nom = "立柱（mast）",
                description = "固定在机身上的竖直立柱，套筒头沿其滑动和转动。它的高度与可调倾角，与臂共同决定作用在磨盘上的切磨角度。"
            ),
            LapidaireComponent(
                nom = "套筒（quill）",
                description = "夹持固定宝石的杆（dop）在其中滑动的管件。套筒自转，依次将预定的每个刻面对准磨盘，并随材料被磨去而向前推进宝石。"
            ),
            LapidaireComponent(
                nom = "索引齿轮（index gear）",
                description = "固定在套筒末端的齿轮，通常有64、72、80、96或120等多种刻度可选，以适应各种切工风格；刻度更少的「快速索引」盘常叠加在常规索引上，以便在批量切磨时加快定位。将宝石的旋转锁定在每个刻面对应的精确位置，是圆形明亮式切工等按8重对称分布刻面的切工所必需的对称性保证。所选索引必须是所需切磨边数的精确倍数：例如五边形无法用96索引切磨（96÷5不是整数），但用80索引则可顺利完成（80÷5=16）。"
            ),
            LapidaireComponent(
                nom = "微调器（cheater）",
                description = "叠加在臂上所示角度之上的精细调节环。它的主要作用并非改变切磨角度本身，而是在磨盘出现轻微翘曲或不均匀磨损时，恢复正在切磨的刻面与磨盘之间的平行关系——每当某个刻面无法在整个表面均匀抛光时，都需要重新进行这一调整。"
            ),
            LapidaireComponent(
                nom = "夹持杆（dop）",
                description = "杆件（木、铝或黄铜——后两者更能保持预热的热量），宝石在插入套筒前固定在其上。固定方式包括加热的蜡（最常见，约80°C熔化）、氰基丙烯酸酯胶（「瞬干胶」，粘接快但残留物难以清除）或环氧树脂（粘接牢固，但其放热反应需在对热敏感的宝石上加以注意）。较精密的夹持杆带有定位销、凸台或凹槽，可确保每次装入分度头时方向一致——这是将宝石从一根夹持杆转移到另一根（例如亭部切磨完成后再切磨冠部）而不发生错位或意外转动的必要条件。"
            ),
            LapidaireComponent(
                nom = "磨盘（lap）",
                description = "旋转金属圆盘，直径通常为6英寸（约152毫米）或8英寸（约203毫米），其上固定或涂敷磨料。粗磨/刻面成型磨盘（表面电镀金刚石，或以轧制方式将金刚石颗粒嵌入铸铁）与质地更软的抛光磨盘有所区别。一台切磨机通常配备多个可更换磨盘，每种粒度对应一片。"
            ),
            LapidaireComponent(
                nom = "测角仪（goniometer）",
                description = "一种检验仪器，在两个相对的主刻面上测量已切磨宝石的实际角度，以核实所得切工。测得角度（冠部为C'，亭部为P'）需通过公式：角度 = (180° − 测得角度) / 2 换算，因为测角仪读取的是与腰面所成的补角。"
            )
        ),
        disquesTitle = "成型与抛光磨盘",
        disquesIntro = "切磨过程按粒度由粗到细逐级进行，每一级都会磨去上一级留下的微划痕；跳过某一级会留下最终抛光也无法消除的痕迹。磨盘转速随各阶段而变化：粗磨阶段较慢（每分钟100至300转），刻面成型阶段较快（每分钟300至600转），抛光阶段最快（每分钟700至1000转甚至更高）——始终需根据宝石的敏感程度调整，遇到质地较软或易解理的宝石应毫不犹豫地降低转速。",
        disques = listOf(
            LapidaireDisc(
                nom = "粗磨",
                grain = "260 至 325",
                usage = "从原石或锯切薄片快速去除材料，形成宝石的大致形状（预成型）。使用侵蚀性强的金刚石磨盘，压力较大，须持续用水冷却。"
            ),
            LapidaireDisc(
                nom = "刻面成型",
                grain = "600 至 1200",
                usage = "按切磨图纸给出的精确角度与索引描绘并细化每个刻面。这是耗时最长、对相邻刻面交会点（meet points）的对称性与精度最具决定性的阶段。"
            ),
            LapidaireDisc(
                nom = "预抛光",
                grain = "3000 至 8000",
                usage = "将成型阶段留下的表面细化至接近哑光但无深划痕的状态，同时保持完全相同的角度——此阶段的任何角度修正都必须极小，否则须退回更粗的粒度重来。"
            ),
            LapidaireDisc(
                nom = "最终抛光",
                grain = "亚微米级抛光膏或粉末（常为氧化铈或极细金刚石）",
                usage = "抛光磨盘按硬度（莫氏硬度）由硬到软选择：陶瓷（9）、铜（3）、锌（2.5）、酚醛树脂（2.2）、有机玻璃或铅锡合金（2）、纯锡（1.7）、铅（1.5）、聚氯乙烯（1），以及加入各种填料的蜡（0.3，因配方而异）。真正起抛光作用的从来不是磨盘本身，而是涂敷其上的粉末，磨料才是抛光的关键——磨盘只是载体，其硬度须与宝石相匹配。可选择干抛（粉末以油性粘合剂固定）或湿抛（粉末以水调和、连续涂敷），视所用方法而定。"
            )
        ),
        indexTitle = "分度头的选择",
        indexIntro = "选择分度头首先取决于要切割的面数：分度数必须是该面数的整数倍（五边形无法在96分度头上完成，但可以在80分度头上完成）。每格的旋转角度与分度头的格数成反比（360°除以格数）——120分度头每格前进3°，32分度头每格前进11.25°。要把切割图从一种分度头换算到另一种，只有当两个分度头互为倍数或约数时才可行：只需将每个分度号除以或乘以两者的比值即可（例如从96分度换算到32分度时除以3）。由于常见的分度头都是4的倍数，正方形可以在任何一种上切割；反之，无法在两格之间定位，这就直接排除了大多数奇数边的多边形。96分度头在厂商中最为常见，在刻度精细度和定位速度之间取得了良好平衡；120分度头功能最全，但定位速度较慢。",
        indexTable = listOf(
            LapidaireIndexEntry(index = "32", rotationParCran = "11.25°/格", cotesTaillables = "正方形（4）、八边形（8）、十六边形（16）"),
            LapidaireIndexEntry(index = "60", rotationParCran = "6°/格", cotesTaillables = "三角形（3）、正方形（4）、五边形（5）、六边形（6）、十边形（10）、十二边形（12）、十五边形（15）、二十边形（20）"),
            LapidaireIndexEntry(index = "64", rotationParCran = "5.625°/格", cotesTaillables = "正方形（4）、八边形（8）、十六边形（16）"),
            LapidaireIndexEntry(index = "72", rotationParCran = "5°/格", cotesTaillables = "三角形（3）、正方形（4）、六边形（6）、八边形（8）、九边形（9）、十二边形（12）、十八边形（18）"),
            LapidaireIndexEntry(index = "80", rotationParCran = "4.5°/格", cotesTaillables = "正方形（4）、五边形（5）、八边形（8）、十边形（10）、十六边形（16）、二十边形（20）"),
            LapidaireIndexEntry(index = "96", rotationParCran = "3.75°/格", cotesTaillables = "最常见——共可切割9种正多边形，包括正方形、六边形、八边形、十二边形"),
            LapidaireIndexEntry(index = "120", rotationParCran = "3°/格", cotesTaillables = "功能最全——共可切割14种正多边形，是常见分度头中的最大值"),
            LapidaireIndexEntry(index = "128", rotationParCran = "2.8125°/格", cotesTaillables = "正方形（4）、八边形（8）、十六边形（16）、32边、64边")
        ),
        poidsCalculator = LapidairePoidsCalculator(
            title = "估算重量",
            intro = "对于已切割镶嵌的宝石，无法直接称重：重量根据体积推算，需要用卡尺测量（精度1/100毫米）并知道该品种的比重。这是一个参考公式，精度约为10-15%——并非真实称重。",
            shapeLabels = mapOf(
                LapidaireCutShape.ROND to "圆形",
                LapidaireCutShape.OVALE to "椭圆形",
                LapidaireCutShape.COUSSIN_CARRE to "方形垫形",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE to "长方形垫形",
                LapidaireCutShape.CARRE_A_GRADIN to "阶梯形方形",
                LapidaireCutShape.RECTANGLE_A_GRADINS to "阶梯形长方形",
                LapidaireCutShape.COUSSIN_CARRE_GRADIN to "方形垫形（阶梯形）",
                LapidaireCutShape.COUSSIN_RECTANGULAIRE_GRADIN to "长方形垫形（阶梯形）",
                LapidaireCutShape.MARQUISE to "马眼形",
                LapidaireCutShape.POIRE to "梨形",
                LapidaireCutShape.TRIANGLE_BOMBE to "凸面三角形",
                LapidaireCutShape.TRIANGLE to "三角形",
                LapidaireCutShape.TRAPEZE to "梯形",
                LapidaireCutShape.COEUR to "心形"
            ),
            dimension1Label = "长度或直径（毫米）",
            dimension2Label = "宽度（毫米）",
            heightLabel = "总深度，台面至底尖（毫米）",
            sgLabel = "该品种的比重",
            computeLabel = "计算",
            resultLabel = "估算重量：%s 克拉",
            disclaimer = "根据体积估算，参考精度10-15%——不能替代真实称重。",
            errorMessage = "请为所有尺寸和比重填写有效值（正数）。"
        ),
        anglesTitle = "参考角度：圆形明亮式切工",
        anglesIntro = "以下比例出自比利时数学家马塞尔·托尔科夫斯基（Marcel Tolkowsky）1919年发表的计算结果，他推算出能使圆形明亮式切工钻石的回光（明亮度）与色散（火彩）最大化的最佳角度——这是至今仍被用作起点的历史基准，尽管现代实验室（尤其是GIA）在这些数值周围采用一个容差范围，而非单一数字。对于折射率不同于钻石的彩色宝石，冠部与腰部的经典比例则更接近25/75%至30/70%之间。无论何种宝石都须遵守的硬性规则：亭部刻面角度绝不能低于该矿物的临界角（否则光线会从亭部漏出，形成「漏光窗」效应），也不能高于消光角（形成暗区）。要核验已切磨完成的宝石，可用测角仪在两个相对的主刻面上测量角度：实际的冠角或亭角可通过公式 (180° − 测得角度) / 2 由读数推算得出。其他切工（公主方形、椭圆形、梨形、祖母绿形、垫形等）各自遵循不同的图纸，因设计者和目标（材料出成率与光学表现之间的取舍）不同而差异很大：若不参照具体图纸，并不存在一个可比较的通用数值。",
        angles = listOf(
            LapidaireAngles(
                coupe = "圆形明亮式切工（托尔科夫斯基，1919年）",
                couronne = "34.5°",
                pavillon = "40.75°",
                table = "腰围直径的53%",
                facettes = "57或58个刻面（冠部33个，亭部24或25个，8重对称）",
                source = "马塞尔·托尔科夫斯基，《Diamond Design》，1919年"
            )
        ),
        optiqueTitle = "临界角与消光角",
        optiqueIntro = "从台面射入的光线大致可分三种情形：亭部过浅会让光线直接从底部漏出；亭部过深会让光线陷入内部反射，最终从侧面刻面漏出而无法返回观察者眼中，即使抛光完美，宝石依然显得暗淡；而比例得当的亭部则会将光线经冠部送回观察者，呈现最大的亮度。琢磨宝石的亭部如同一面镜子：低于该矿物的临界角时，从台面射入的光线会从亭部漏出（即上文提到的「漏光窗」效应）；若恰好按临界角切磨，则会出现另一种缺陷——「鱼眼效应」：台面显得暗淡，而冠部轮廓依然明亮，因为穿过台面的光线只是掠过反射锥的锥壁，并未真正发生反射。此外还存在一个上限——消光角，超过这个角度，亭部会再次从对面的刻面漏光：消光角 = 60° − (临界角 / 3)。因此，切磨良好的亭部应满足临界角 < 亭部角度 < 消光角；宝石的折射率越高，这一可用角度范围就越宽——钻石允许的误差范围远大于萤石。冠部则遵循一条互补规则：其最大角度与亭部角度成反比（亭部越浅，冠部可以越高），与折射率成正比；将角度控制在这些最大值以下几分之一度，始终是最稳妥的做法。所有这些切磨图纸都建立在「交会点技术」（美国方法，由Long与Steele提出）之上，该技术利用三个或更多刻面的交会点作为基准，自动保证良好的比例并最大限度保留原石重量；若想在不拆下镶嵌的情况下估算已切磨宝石的重量，常用公式为：重量（克拉）= 宽度³ × 切工体积系数 × 该矿物的比重 / 200。",
        optiqueTable = listOf(
            LapidaireOptiqueEntry(pierre = "钻石", angleCritique = "24.4°", angleExtinction = "51.85°"),
            LapidaireOptiqueEntry(pierre = "榍石（楔石）", angleCritique = "31.76°", angleExtinction = "49.41°"),
            LapidaireOptiqueEntry(pierre = "锆石（高型）", angleCritique = "31.3°", angleExtinction = "49.57°"),
            LapidaireOptiqueEntry(pierre = "翠榴石（石榴石）", angleCritique = "32.62°", angleExtinction = "49.13°"),
            LapidaireOptiqueEntry(pierre = "亚历山大变石（金绿宝石）", angleCritique = "34.94°", angleExtinction = "48.35°"),
            LapidaireOptiqueEntry(pierre = "红宝石与蓝宝石（刚玉）", angleCritique = "34.58°", angleExtinction = "48.47°"),
            LapidaireOptiqueEntry(pierre = "尖晶石", angleCritique = "35.74°", angleExtinction = "48.09°"),
            LapidaireOptiqueEntry(pierre = "橄榄石", angleCritique = "37.2°", angleExtinction = "47.6°"),
            LapidaireOptiqueEntry(pierre = "碧玺", angleCritique = "38.01°", angleExtinction = "47.33°"),
            LapidaireOptiqueEntry(pierre = "托帕石", angleCritique = "38.15°", angleExtinction = "47.28°"),
            LapidaireOptiqueEntry(pierre = "绿柱石", angleCritique = "39.35°", angleExtinction = "46.88°"),
            LapidaireOptiqueEntry(pierre = "祖母绿", angleCritique = "39.72°", angleExtinction = "46.76°"),
            LapidaireOptiqueEntry(pierre = "海蓝宝石", angleCritique = "39.75°", angleExtinction = "46.75°"),
            LapidaireOptiqueEntry(pierre = "紫水晶与石英", angleCritique = "40.37°", angleExtinction = "46.54°"),
            LapidaireOptiqueEntry(pierre = "方解石", angleCritique = "42.29°", angleExtinction = "45.9°"),
            LapidaireOptiqueEntry(pierre = "萤石", angleCritique = "44.21°", angleExtinction = "45.26°")
        ),
        defautsTitle = "常见问题与解决方法",
        defautsIntro = "大多数切磨缺陷源于角度设置不准、索引读数错误或夹持杆转移时未对齐，而非磨盘本身的问题。修正交会不良或抛光不均的基本原则：始终在缺陷的对侧进行调整。",
        defauts = listOf(
            LapidaireDefaut(
                probleme = "刻面一侧偏心或抛光不良",
                cause = "宝石在微调器上略有偏移。",
                remede = "转动微调器，使宝石向缺陷的对侧倾斜。"
            ),
            LapidaireDefaut(
                probleme = "刻面上方交会或抛光不良，角度过大",
                cause = "量角器上显示的角度高于应有的数值。",
                remede = "在量角器上略微减小角度，使宝石向下倾斜。"
            ),
            LapidaireDefaut(
                probleme = "刻面下方交会或抛光不良，角度过小",
                cause = "量角器上显示的角度低于应有的数值。",
                remede = "在量角器上略微增大角度，使宝石向上倾斜。"
            ),
            LapidaireDefaut(
                probleme = "对角线方向的组合缺陷（例如左上方）",
                cause = "宝石在转移夹持杆时可能发生了轻微转动。",
                remede = "同时在微调器和角度上进行修正，必要时降低磨盘转速，直到重新掌握控制。"
            ),
            LapidaireDefaut(
                probleme = "宝石在抛光磨盘上发出尖锐刺耳的声音",
                cause = "正在加工的刻面与磨盘不平行。",
                remede = "立即停止并修正宝石的位置后再继续——这是一个可靠的警示信号，绝不能忽视。"
            ),
            LapidaireDefaut(
                probleme = "多个刻面上出现平行划痕",
                cause = "磨盘被不同粒度的颗粒污染。",
                remede = "先对磨盘进行去污处理（用热肥皂水刷洗，必要时刮除或重新修整表面），再继续作业。"
            ),
            LapidaireDefaut(
                probleme = "刻面表面出现「烧痕」",
                cause = "抛光时长时间停留同一位置且压力过大，导致局部过热。",
                remede = "让刻面在磨盘上有规律地来回移动，减小压力，必要时每秒滴加一滴水。"
            ),
            LapidaireDefaut(
                probleme = "切磨过程中出现小凹坑",
                cause = "靠近表面的内含物被暴露了出来。",
                remede = "在粗磨阶段更彻底地「清理」内含物是最好的预防措施；一旦出现凹坑，可对该区域局部重新切磨，必要时考虑整颗重新切磨。"
            )
        ),
        especesTitle = "各品种实用要点",
        especesIntro = "除了上文已给出的临界角和消光角之外，每个品种都有其自身的切磨习惯：根据多色性或解理确定台面朝向、特殊的敏感性，以及能获得最佳效果的抛光盘和抛光粉。这些是来自实践经验的参考，并非放之四海皆准的规则——每颗宝石都保留着自己的个体特点（内含物、色带、原石表面状况）。",
        especes = listOf(
            LapidaireEspeceFiche(
                pierre = "钻石",
                orientation = "均质体：不存在与光轴相关的定向限制。",
                fragilite = "沿4个八面体方向具有完全解理，切磨师对此了然于胸并善加利用而非被动承受；亭部尖端始终是最易受冲击的部位。",
                polissage = "铸铁抛光盘（scaife）加载金刚石粉——只有金刚石能抛光金刚石。"
            ),
            LapidaireEspeceFiche(
                pierre = "刚玉（红宝石、蓝宝石）",
                orientation = "定向时无需考虑解理。",
                fragilite = "硬度9，是刻面宝石中最硬的品种之一：无需特别防范。",
                polissage = "在铜盘上使用金刚石粉可获得极佳效果。"
            ),
            LapidaireEspeceFiche(
                pierre = "绿柱石（祖母绿、海蓝宝石、摩根石、金绿柱石）",
                orientation = "解理不完全，无真正的定向限制。",
                fragilite = "祖母绿尤其需要留意其内部的\"花园\"（内含物），无论切磨还是抛光都可能因此而破裂。",
                polissage = "冷却水中加入醋酸有助于抛光；使用氧化铝、氧化铈或氧化锡粉，或Linde A抛光粉。"
            ),
            LapidaireEspeceFiche(
                pierre = "石英（紫水晶、黄水晶、水晶、烟晶）",
                orientation = "无解理；定向时需考虑色带分布。",
                fragilite = "无特别的解理脆性。",
                polissage = "抛光效果有时会出现无法查明原因的意外情况。"
            ),
            LapidaireEspeceFiche(
                pierre = "黄玉",
                orientation = "台面相对底面解理方向偏移约10°，避免直接沿其轴向暴露。",
                fragilite = "沿单一方向（底面）具有清晰解理——只要避开即容易切磨。",
                polissage = "除一般规则外无特别的抛光盘建议。"
            ),
            LapidaireEspeceFiche(
                pierre = "碧玺",
                orientation = "台面最好平行于光轴切割；多色宝石在两种颜色交界处会整齐断裂——此处最好粘合而非锯切。",
                fragilite = "切磨过程中对热和冲击敏感。",
                polissage = "除一般规则外无特别的抛光盘建议。"
            ),
            LapidaireEspeceFiche(
                pierre = "石榴石（铁铝榴石、翠榴石、钙铝榴石、镁铝榴石、锰铝榴石、钙铬榴石）",
                orientation = "均质体：台面定向不受限制，无优先方向。",
                fragilite = "无与解理相关的脆性问题。",
                polissage = "冷却水中加入醋酸可持续改善效果；使用氧化铈、氧化锡或氧化铝粉。"
            ),
            LapidaireEspeceFiche(
                pierre = "尖晶石",
                orientation = "均质体，无定向限制。",
                fragilite = "无与解理相关的脆性问题。",
                polissage = "在铜盘上用金刚石抛光膏可获得极佳抛光效果。"
            ),
            LapidaireEspeceFiche(
                pierre = "橄榄石",
                orientation = "多色性很弱，定向限制不大。",
                fragilite = "无明显的解理脆性。",
                polissage = "抛光效果有时会出现意外情况；金刚石粉效果良好。"
            ),
            LapidaireEspeceFiche(
                pierre = "萤石",
                orientation = "沿4个方向容易裂开：需谨慎选择定向以降低风险。",
                fragilite = "是刻面宝石中临界角最窄的品种之一，对冲击和崩裂非常敏感。",
                polissage = "抛光盘最好使用蜡盘；加酸有助于提升效果。"
            ),
            LapidaireEspeceFiche(
                pierre = "方解石",
                orientation = "需谨慎定向，避免直接暴露解理面。",
                fragilite = "硬度极低（3）且沿3个方向具有完全解理，对热非常敏感——是难以切磨和抛光的宝石；抛光盘应低速转动（约100转/分钟）。",
                polissage = "木质或蜡质抛光盘，使用氧化锡或氧化铬粉，并加几滴草酸。"
            ),
            LapidaireEspeceFiche(
                pierre = "金绿宝石（变石）",
                orientation = "无与解理相关的定向限制。",
                fragilite = "硬度高（8.5），坚硬且无特别问题。",
                polissage = "用酸和Linde A粉抛光速度快；在铜盘上使用金刚石粉可获得极佳效果。"
            )
        ),
        diagrammesTitle = "图解",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "圆形明亮式切工比例图：冠部、腰部与亭部命名示意。"
            ),
            LapidaireDiagram(
                id = "trajet_lumiere_pavillon",
                legende = "根据亭部深度展示光路的示意图：过浅、比例良好或过深。"
            ),
            LapidaireDiagram(
                id = "moulin_taille_historique",
                legende = "描绘古代钻石切磨磨坊的古版画——旋转磨盘的机械原理，数百年来基本未变。"
            ),
            LapidaireDiagram(
                id = "machine_facettage_moderne",
                legende = "现代刻面切磨机：立柱、套筒与磨盘，如上文所述。"
            ),
            LapidaireDiagram(
                id = "etapes_taille_brut_facette",
                legende = "原石转变为刻面宝石的各个阶段。"
            )
        ),
        tipsTitle = "实用建议",
        tips = listOf(
            LapidaireTip(texte = "在开始加工贵重宝石前，务必用测试石核对臂上所示角度的校准：亭部角度偏差0.5°即足以使一颗原本切磨良好的宝石明显发暗。"),
            LapidaireTip(texte = "仔细将宝石对中固定在夹持杆上：轻微偏心会导致腰围厚度不均，各刻面在整个周边无法在同一点交会。"),
            LapidaireTip(texte = "保持适中的腰围厚度（既不要脆弱易崩的「刀锋」薄腰，也不要过厚而白白包裹材料且无光学益处）——均匀可见、不薄不厚的腰线是精细做工的标志之一。"),
            LapidaireTip(texte = "在每个阶段用放大镜在侧光下检查刻面之间的交会点（meet points）：交会点偏差在成型阶段容易修正，一旦进入预抛光阶段则几乎无法再修正。"),
            LapidaireTip(texte = "根据磨盘材质调整润滑方式：水适用于大多数电镀金刚石磨盘，而某些抛光磨盘（尤其是锡质磨盘）使用油性润滑或特定配方效果更佳——请遵循磨盘制造商的建议。"),
            LapidaireTip(texte = "切磨前检查该矿物的热敏感性与解理：某些宝石（黄玉、锂辉石）具有明显解理，在粗磨过度侵蚀产生的高温或压力下可能扩展。"),
            LapidaireTip(texte = "通过在点光源下正面观察宝石来检验最终对称性：冠部刻面的反光应形成规则图案；此阶段肉眼可见的不对称，抛光阶段已无法纠正。"),
            LapidaireTip(texte = "切勿将宝石放在静止的磨盘上再启动磨盘：应始终先让磨盘转动起来，再轻轻以钟摆式动作将宝石送入接触，避免宝石始终停留在同一点，否则会使磨盘磨损不均，宝石本身也会磨损不规则。"),
            LapidaireTip(texte = "若台面看起来「发暗」而宝石轮廓仍然明亮，说明出现了漏光窗效应：亭部过浅，光线从下方漏出而未能经台面反射回来——应加深亭部角度加以纠正，但绝不能超过消光角。"),
            LapidaireTip(texte = "在细长形宝石（椭圆形、榄尖形、梨形、垫形等）上，中央出现明显暗十字（所谓「X+效应」）表明亭部长宽方向的刻面未能良好衔接；针对所选形状精心设计的切磨图纸，比逐案调整更能有效避免这一问题。")
        ),
        conservationTitle = "切磨宝石的保存与操作",
        conservationIntro = "一颗切磨精良的宝石在镶嵌或存放之后仍然容易受损：几项简单的预防措施就能避免大部分意外划痕和损伤。",
        conservation = listOf(
            LapidaireConservationTip(
                titre = "清洁",
                conseil = "切勿干擦宝石：灰尘会起到研磨剂的作用，划伤抛光表面。用温肥皂水配软刷清洗，或用酒精快速去油，然后用无绒布擦干。"
            ),
            LapidaireConservationTip(
                titre = "分开存放",
                conseil = "每颗宝石应分开存放，用折叠的纸包或独立的小袋子：宝石之间哪怕短暂接触也会相互刮伤。"
            ),
            LapidaireConservationTip(
                titre = "切勿混放不同硬度",
                conseil = "切勿将不同硬度的宝石放在同一格子里：较硬的宝石会系统性地划伤较软的宝石，哪怕只是运输过程中的简单摩擦。"
            ),
            LapidaireConservationTip(
                titre = "热冲击与化学品",
                conseil = "避免温度骤变和刺激性化学品（漂白剂、酸类），它们可能使某些宝石开裂或破坏处理效果（注油祖母绿、浸渍宝石）。对于有裂纹、注油或脆弱的宝石，应避免使用超声波清洗机。"
            ),
            LapidaireConservationTip(
                titre = "光照",
                conseil = "有些宝石对光敏感：紫水晶或紫锂辉石长时间暴露在强光下可能褪色。应将敏感宝石存放在避免长时间光照的地方。"
            ),
            LapidaireConservationTip(
                titre = "运输与操作",
                conseil = "运输时应使用带衬垫的小袋，避免多颗宝石或首饰放在一起直接接触；拿取时应握住镶座或台面，而非更易受冲击的亭部。"
            )
        ),
        disclaimerTitle = "在工坊中学习的手艺",
        disclaimerBody = "本篇提供的是通用参考要点，而非完整操作手册：刻面切磨需要通过有指导的实践学习，配合适当设备，以及每个工坊、每台机器各自特有的安全规范（护目护呼吸装备、磨盘持续冷却）。所展示的图解均来自真实、可自由使用授权的来源（见版权说明）；在暂时缺失图片时，仅显示图注文字。",
        machinesTypesTitle = "行业机械",
        machinesTypesIntro = "除了上文详述的切磨机部件外，以下是工坊中实际使用的各类机械概览，涵盖刻面切磨与凸圆面切磨，附带各自的特点与相关技术。所提及的品牌仅作为该类别的代表性示例，与Gems of Rod没有任何商业关系。",
        categorieFacettageLabel = "刻面切磨",
        categorieCabochonLabel = "凸圆面切磨",
        categoriePolyvalentLabel = "多用途",
        caracteristiquesLabel = "特点",
        techniqueLabel = "技术",
        machinesTypes = listOf(
            LapidaireMachineFiche(
                photoId = "machine_bras_manuel",
                nom = "手持量规杆（jam-peg）",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "最简单也最古老的切磨工具：宝石粘在一根手持杆的末端，抵住磨盘，角度靠目测或简单量规调整。斯里兰卡、泰国等地的传统切磨学校至今仍在传授这种方法。",
                caracteristiques = listOf(
                    "无任何机械分度部件",
                    "成本几乎为零",
                    "完全依赖切磨师的手艺",
                    "熟练工匠操作速度快"
                ),
                technique = "切磨师的手持续调整角度与抵住磨盘的压力；刻面的精度取决于动作的重复性，而非机器本身。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_amovible",
                nom = "可拆卸机械分度头切磨机",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "自20世纪70年代以来西方工坊的标准设备（Facetron、Ultra Tec、Poly-Metric等）：可更换的分度头将宝石的角度和旋转精确调整到十分之一度，机臂垂直滑动以调节切割深度。",
                caracteristiques = listOf(
                    "角度精度达十分之一度",
                    "分度头可更换（刻面、鳞状面、凹面）",
                    "微米级深度限位",
                    "投入较大（机器+磨盘）"
                ),
                technique = "切磨师在每个刻面之前设定角度和分度档位，再将宝石抵住磨盘下降至设定限位：可重复性取代了手持杆的自由操作。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_index_fixe",
                nom = "固定式一体分度头切磨机",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "更经济的版本，分度头与机臂固定在一起而非可拆卸，出厂时即固定好刻度档位数量（视型号常为64、96或120档）。",
                caracteristiques = listOf(
                    "入门价格明显低于可拆卸分度头机型",
                    "分度档位数量有限且不可更改",
                    "日常使用坚固耐用",
                    "适合学习和常见切磨款式"
                ),
                technique = "原理与可拆卸分度头相同，但刻面图案的选择局限于机器上刻好的分度档位——对大多数经典切磨款式（圆形、椭圆形、垫形）已经足够。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_cnc",
                nom = "电脑数控（CNC）切磨机",
                categorie = LapidaireMachineCategorie.FACETTAGE,
                description = "数字控制的机器，能在一系列宝石上完全一致地重现切磨方案（角度与分度图），用于工业化生产或非常复杂的花式切磨。",
                caracteristiques = listOf(
                    "宝石间的完美可重复性",
                    "根据数字切磨方案编程",
                    "投入较高，专用于批量生产",
                    "降低了最终成果中手工技艺的比重"
                ),
                technique = "切磨方案被载入机器软件，软件自动依次执行编程好的角度与分度位置；切磨师的角色转向设置与质量控制。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_multi_meules",
                nom = "直列式多磨盘凸圆面切磨机",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "凸圆面切磨的标杆设备（Genie、CabKing等型号）：6至8个粒度递减的金刚石磨盘，随后接毛毡抛光盘，排列在同一机架上并持续供水。",
                caracteristiques = listOf(
                    "同一轴上有6至8个粒度递减工位",
                    "每个磨盘持续供水",
                    "工位之间快速切换，无需更换磨盘",
                    "价格较高，但耐用性极佳"
                ),
                technique = "凸圆面先粗磨，再依次经过粒度递减的磨盘细磨，直至最后的抛光毛毡——每个工位都会消除前一工位留下的细微划痕。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_cabocheuse_vevor",
                nom = "Vevor凸圆面切磨机（多磨盘，入门级）",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "多磨盘凸圆面切磨机的经济版本，中国制造，品牌为Vevor：价格明显低于Genie、CabKing等专业品牌，因此在初学者和小型工坊中十分普及。",
                caracteristiques = listOf(
                    "同一轴上配6至8个金刚石磨盘+毛毡，与专业机型相仿",
                    "入门级电机与轴承，机械公差更大",
                    "内置集水盘",
                    "价格明显低于专业品牌，但使用寿命较短"
                ),
                technique = "与专业凸圆面切磨机相同的逐盘递进原理，但振动更明显：较轻的操作压力和更频繁的轴承保养可弥补机械精度的不足。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_meuleuse_polisseuse",
                nom = "卧轴组合式磨抛机",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "更为手工化的版本：一根或两根水平轴，可根据需要自行安装磨盘或毛毡，而非固定工位排列。",
                caracteristiques = listOf(
                    "磨盘可由切磨师自由更换",
                    "也用于锯切前对原石进行粗磨",
                    "比专用多磨盘凸圆面切磨机便宜",
                    "各工序之间需要更多手动操作"
                ),
                technique = "切磨师在每个粒度阶段都要亲自更换装在轴上的磨盘，这与工位固定并排排列的多磨盘切磨机不同。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_scie_tranche",
                nom = "切片锯（trim saw）",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "装有金刚石锯片的小型圆锯，用于凸圆面切磨之前，将原石切成所需厚度的薄片（slab），再上磨盘塑形。",
                caracteristiques = listOf(
                    "金刚石锯片以油浴或水浴冷却",
                    "常见直径为10至25厘米，视型号而定",
                    "可调切割导板，保证切片厚度均匀",
                    "必不可少的前置工序，并非刻面切磨"
                ),
                technique = "原石手动推进，抵住缓慢旋转的锯片；所得切片的厚度直接决定了未来凸圆面的最大厚度。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_perceuse_gemmes",
                nom = "宝石钻孔机",
                categorie = LapidaireMachineCategorie.CABOCHON,
                description = "配备空心金刚石钻头和供水系统的立式钻床，用于给准备制成吊坠或穿线的珍珠及凸圆面钻孔。",
                caracteristiques = listOf(
                    "不同直径的空心金刚石钻头",
                    "必须用水冷却以避免开裂",
                    "转速可根据宝石硬度调节",
                    "脆性宝石采用两面分次钻孔"
                ),
                technique = "钻孔以低速并在持续供水下进行，常从宝石两面分别钻入，以避免单面一次性钻穿时典型的出口崩裂。"
            ),
            LapidaireMachineFiche(
                photoId = "machine_touret_combine",
                nom = "入门级刻面/凸圆面组合砂轮机",
                categorie = LapidaireMachineCategorie.POLYVALENT,
                description = "一台价格低廉的小型机器，在同一根轴上组合磨盘与毛毡，可同时尝试凸圆面切磨和简单的刻面切磨，但没有精确的分度头。",
                caracteristiques = listOf(
                    "入门价格很低，机身紧凑",
                    "一台机器兼顾多种用途",
                    "缺乏精确分度头，无法完成真正的刻面切磨",
                    "适合入门体验，专业效果有限"
                ),
                technique = "初学者在同一机架上体验两种工艺，但角度和分度精度远不及专用机型。"
            )
        )
    )

    private val byLanguage: Map<String, LapidairePage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl,
        AppLanguage.ZH.code to zh
    )

    fun page(languageCode: String): LapidairePage = byLanguage[languageCode] ?: fr
}
