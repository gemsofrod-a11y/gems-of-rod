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

data class LapidairePage(
    val intro: String,
    val machinesTitle: String,
    val machinesIntro: String,
    val machines: List<LapidaireComponent>,
    val disquesTitle: String,
    val disquesIntro: String,
    val disques: List<LapidaireDisc>,
    val anglesTitle: String,
    val anglesIntro: String,
    val angles: List<LapidaireAngles>,
    val optiqueTitle: String,
    val optiqueIntro: String,
    val optiqueTable: List<LapidaireOptiqueEntry>,
    val defautsTitle: String,
    val defautsIntro: String,
    val defauts: List<LapidaireDefaut>,
    val diagrammesTitle: String,
    val diagrammes: List<LapidaireDiagram>,
    val tipsTitle: String,
    val tips: List<LapidaireTip>,
    val disclaimerTitle: String,
    val disclaimerBody: String
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
        diagrammesTitle = "Diagrammes",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagramme de proportions du brillant rond : nomenclature de la couronne, du rondiste et du pavillon."
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
        disclaimerTitle = "Un métier qui s'apprend en atelier",
        disclaimerBody = "Cette fiche présente des repères généraux, pas un mode d'emploi complet : la taille de facettes s'apprend par la pratique encadrée, avec du matériel adapté et des consignes de sécurité (protection oculaire et respiratoire, refroidissement continu du plateau) propres à chaque atelier et à chaque machine. Les diagrammes affichés proviennent de sources réelles et libres de droits (voir crédits) ; en leur absence temporaire, seule la légende reste affichée."
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
        diagrammesTitle = "Diagrams",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Round brilliant proportions diagram: crown, girdle, and pavilion nomenclature."
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
        disclaimerTitle = "A trade learned in the workshop",
        disclaimerBody = "This sheet presents general reference points, not a complete manual: faceting is learned through supervised practice, with suitable equipment and safety guidelines (eye and respiratory protection, continuous lap cooling) specific to each workshop and each machine. The diagrams shown come from real, freely licensed sources (see credits); while temporarily unavailable, only the caption is shown."
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
        diagrammesTitle = "Diagramas",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagrama de proporciones del brillante redondo: nomenclatura de la corona, el rondel y el pabellón."
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
        disclaimerTitle = "Un oficio que se aprende en el taller",
        disclaimerBody = "Esta ficha presenta referencias generales, no un manual completo: la talla de facetas se aprende mediante práctica supervisada, con material adecuado y normas de seguridad (protección ocular y respiratoria, refrigeración continua del plato) propias de cada taller y de cada máquina. Los diagramas mostrados proceden de fuentes reales y libres de derechos (véanse los créditos); en su ausencia temporal, solo se muestra la leyenda."
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
        diagrammesTitle = "Diagrammi",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagramma delle proporzioni del brillante rotondo: nomenclatura di corona, cintura e padiglione."
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
        disclaimerTitle = "Un mestiere che si impara in laboratorio",
        disclaimerBody = "Questa scheda presenta riferimenti generali, non un manuale completo: la sfaccettatura si impara con la pratica seguita da un istruttore, con attrezzatura adeguata e norme di sicurezza (protezione degli occhi e delle vie respiratorie, raffreddamento continuo del piatto) proprie di ogni laboratorio e di ogni macchina. I diagrammi mostrati provengono da fonti reali e libere da diritti (vedi crediti); in loro assenza temporanea, viene mostrata solo la didascalia."
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
        diagrammesTitle = "Diagramme",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Proportionsdiagramm des runden Brillanten: Nomenklatur von Krone, Rondiste und Pavillon."
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
        disclaimerTitle = "Ein Handwerk, das man in der Werkstatt lernt",
        disclaimerBody = "Dieses Merkblatt zeigt allgemeine Anhaltspunkte, keine vollständige Anleitung: Der Facettenschliff wird durch angeleitete Praxis erlernt, mit geeigneter Ausrüstung und Sicherheitsvorschriften (Augen- und Atemschutz, durchgehende Lap-Kühlung), die für jede Werkstatt und jede Maschine spezifisch sind. Die gezeigten Diagramme stammen aus echten, frei lizenzierten Quellen (siehe Credits); solange sie vorübergehend fehlen, wird nur die Bildunterschrift angezeigt."
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
        diagrammesTitle = "Diagramas",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Diagrama de proporções do brilhante redondo: nomenclatura da coroa, da cintura e do pavilhão."
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
        disclaimerTitle = "Um ofício que se aprende na oficina",
        disclaimerBody = "Esta ficha apresenta referências gerais, não um manual completo: a lapidação de facetas aprende-se através de prática supervisionada, com equipamento adequado e normas de segurança (proteção ocular e respiratória, arrefecimento contínuo do prato) próprias de cada oficina e de cada máquina. Os diagramas apresentados provêm de fontes reais e livres de direitos (ver créditos); na sua ausência temporária, é apresentada apenas a legenda."
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
        diagrammesTitle = "Схемы",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Схема пропорций круглого бриллианта: номенклатура коронки, рундиста и павильона."
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
        disclaimerTitle = "Ремесло, которому учатся в мастерской",
        disclaimerBody = "Эта статья приводит общие ориентиры, а не полное руководство: фасетная огранка осваивается через практику под наставничеством, с подходящим оборудованием и правилами безопасности (защита глаз и органов дыхания, непрерывное охлаждение планшайбы), специфичными для каждой мастерской и каждого станка. Показанные схемы взяты из реальных источников со свободными лицензиями (см. указания авторства); при их временном отсутствии отображается только подпись."
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
        diagrammesTitle = "Diagrammen",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "Verhoudingsdiagram van de ronde briljant: naamgeving van kroon, rondiste en paviljoen."
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
        disclaimerTitle = "Een vak dat je in de werkplaats leert",
        disclaimerBody = "Dit overzicht geeft algemene richtlijnen, geen volledige handleiding: facetteren wordt geleerd door begeleide praktijk, met geschikte apparatuur en veiligheidsvoorschriften (oog- en ademhalingsbescherming, continue lapkoeling) die per werkplaats en per machine verschillen. De getoonde diagrammen zijn afkomstig uit echte, vrij te gebruiken bronnen (zie credits); zolang ze tijdelijk ontbreken, wordt alleen het onderschrift getoond."
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
        diagrammesTitle = "图解",
        diagrammes = listOf(
            LapidaireDiagram(
                id = "brillant_rond_proportions",
                legende = "圆形明亮式切工比例图：冠部、腰部与亭部命名示意。"
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
        disclaimerTitle = "在工坊中学习的手艺",
        disclaimerBody = "本篇提供的是通用参考要点，而非完整操作手册：刻面切磨需要通过有指导的实践学习，配合适当设备，以及每个工坊、每台机器各自特有的安全规范（护目护呼吸装备、磨盘持续冷却）。所展示的图解均来自真实、可自由使用授权的来源（见版权说明）；在暂时缺失图片时，仅显示图注文字。"
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
