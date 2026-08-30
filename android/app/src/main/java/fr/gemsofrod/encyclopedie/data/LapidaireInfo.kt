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
        machinesIntro = "Une machine à facettes (faceting machine) maintient la pierre à un angle et un index précis contre un disque abrasif rotatif (le plateau, ou lap). Sa précision mécanique — au dixième de degré près pour l'angle, au point près pour l'index — est ce qui distingue une taille professionnelle d'un simple meulage à main levée.",
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
                description = "Molette dentée fixée au bout de la quille, généralement graduée sur 96 crans (parfois 64 ou 32 sur d'anciens modèles), qui bloque la rotation de la pierre sur une position précise à chaque facette — condition indispensable à la symétrie d'une taille comme le brillant rond, qui répartit ses facettes selon une symétrie d'ordre 8."
            ),
            LapidaireComponent(
                nom = "Cheater (micromètre de rattrapage)",
                description = "Bague de réglage fin superposée à l'angle affiché sur le bras, qui permet de corriger de quelques dixièmes de degré sans toucher au réglage principal — utile pour compenser l'usure irrégulière d'un plateau ou ajuster un angle non prévu par la table d'index."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Tige métallique (laiton ou acier) sur laquelle la pierre brute est fixée par collage ou par cire chauffée, avant d'être insérée dans la quille. Un second dop, plus court, sert à retourner la pierre pour tailler la couronne après le pavillon (ou l'inverse selon la méthode)."
            ),
            LapidaireComponent(
                nom = "Plateau (lap)",
                description = "Disque métallique rotatif sur lequel est fixé ou chargé l'abrasif — diamant électrodéposé ou aggloméré pour le dégrossissage et le formage, étain ou cuivre chargé en pâte diamantée pour le polissage final. Une machine dispose généralement de plusieurs plateaux interchangeables, un par étape du grain."
            )
        ),
        disquesTitle = "Disques de mise en forme et de polissage",
        disquesIntro = "La taille progresse par étapes de grain de plus en plus fin, chacune effaçant les micro-rayures laissées par la précédente ; sauter une étape laisse des marques que le polissage final ne peut plus rattraper.",
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
                usage = "Plateau doux (étain, cuivre, plomb-étain ou parfois résine/laiton chargée en pâte diamantée) qui donne aux facettes leur brillance miroir, sans enlever de matière ni modifier la géométrie. Le choix du plateau de polissage varie selon la dureté et la sensibilité thermique de l'espèce taillée."
            )
        ),
        anglesTitle = "Angles de référence : le brillant rond",
        anglesIntro = "Les proportions ci-dessous sont celles publiées par le mathématicien belge Marcel Tolkowsky en 1919, qui a établi par le calcul l'angle optimal pour maximiser le retour de lumière (brillance) et la dispersion (feu) d'un diamant taillé en brillant rond — c'est la référence historique encore utilisée comme point de départ aujourd'hui, bien que les laboratoires modernes (GIA notamment) admettent une plage de tolérance autour de ces valeurs plutôt qu'un chiffre unique. Les autres tailles (princesse, ovale, poire, émeraude, coussin...) suivent chacune leur propre diagramme, très variable selon le fabricant du diagramme et l'objectif recherché (rendement de matière contre performance optique) : il n'existe pas de valeur universelle comparable à citer sans se référer à un diagramme précis.",
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
            LapidaireTip(texte = "Tester la symétrie finale en observant la pierre de face sous une source ponctuelle : les reflets des facettes de la couronne doivent former un motif régulier ; une asymétrie visible à l'œil nu à ce stade ne se rattrape plus au polissage.")
        ),
        disclaimerTitle = "Un métier qui s'apprend en atelier",
        disclaimerBody = "Cette fiche présente des repères généraux, pas un mode d'emploi complet : la taille de facettes s'apprend par la pratique encadrée, avec du matériel adapté et des consignes de sécurité (protection oculaire et respiratoire, refroidissement continu du plateau) propres à chaque atelier et à chaque machine. Les diagrammes affichés proviennent de sources réelles et libres de droits (voir crédits) ; en leur absence temporaire, seule la légende reste affichée."
    )

    private val en = LapidairePage(
        intro = "The lapidary shapes and polishes rough stones into cut gemstones. The trade covers several specialities — faceting for transparent stones, cabochon cutting for opaque or translucent stones, engraving and ornamental carving — but faceting, the most technical of them, is what gives a stone its play of light. This sheet presents the basic equipment and reference points of the trade; it is written for professionals and informed enthusiasts, not as a first, unsupervised lesson.",
        machinesTitle = "The faceting machine",
        machinesIntro = "A faceting machine holds the stone at a precise angle and index against a rotating abrasive disc (the lap). Its mechanical precision — to a tenth of a degree for the angle, to the exact point for the index — is what sets professional cutting apart from simple freehand grinding.",
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
                description = "Toothed wheel fixed at the end of the quill, usually graduated in 96 notches (sometimes 64 or 32 on older models), which locks the stone's rotation at a precise position for each facet — essential for the symmetry of a cut like the round brilliant, which arranges its facets in 8-fold symmetry."
            ),
            LapidaireComponent(
                nom = "Cheater",
                description = "Fine-adjustment ring layered on top of the angle shown on the arm, allowing corrections of a few tenths of a degree without touching the main setting — useful for compensating uneven lap wear or setting an angle not covered by the index table."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Metal rod (brass or steel) onto which the rough stone is fixed with glue or heated wax before being inserted into the quill. A second, shorter dop is used to flip the stone in order to cut the crown after the pavilion (or the reverse, depending on the method)."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Rotating metal disc onto which the abrasive is fixed or charged — electroplated or sintered diamond for coarse grinding and facet shaping, tin or copper charged with diamond paste for final polishing. A machine typically carries several interchangeable laps, one per grit stage."
            )
        ),
        disquesTitle = "Shaping and polishing discs",
        disquesIntro = "Cutting proceeds through progressively finer grit stages, each erasing the micro-scratches left by the previous one; skipping a stage leaves marks that final polishing can no longer remove.",
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
                usage = "Soft lap (tin, copper, lead-tin, or sometimes resin/brass charged with diamond paste) that gives the facets their mirror brilliance without removing material or altering the geometry. The choice of polishing lap depends on the hardness and heat sensitivity of the species being cut."
            )
        ),
        anglesTitle = "Reference angles: the round brilliant",
        anglesIntro = "The proportions below are those published by the Belgian mathematician Marcel Tolkowsky in 1919, who calculated the optimal angle to maximise light return (brilliance) and dispersion (fire) in a round-brilliant-cut diamond — the historical benchmark still used as a starting point today, though modern laboratories (GIA in particular) accept a tolerance range around these values rather than a single figure. Other cuts (princess, oval, pear, emerald, cushion...) each follow their own diagram, which varies widely by diagram designer and by goal (material yield versus optical performance): there is no comparable universal value to cite without referring to a specific diagram.",
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
            LapidaireTip(texte = "Test final symmetry by viewing the stone face-up under a point light source: the crown facets' reflections should form a regular pattern; an asymmetry visible to the naked eye at this stage can no longer be fixed by polishing.")
        ),
        disclaimerTitle = "A trade learned in the workshop",
        disclaimerBody = "This sheet presents general reference points, not a complete manual: faceting is learned through supervised practice, with suitable equipment and safety guidelines (eye and respiratory protection, continuous lap cooling) specific to each workshop and each machine. The diagrams shown come from real, freely licensed sources (see credits); while temporarily unavailable, only the caption is shown."
    )

    private val es = LapidairePage(
        intro = "El lapidario da forma y pule las piedras en bruto hasta convertirlas en piedras talladas. Este oficio abarca varias especialidades — la talla de facetas (faceting) para piedras transparentes, el cabujón para piedras opacas o translúcidas, el grabado y el trabajo ornamental —, pero la talla de facetas, la más técnica, es la que da a una piedra su juego de luz. Esta ficha presenta el equipo y las referencias básicas del oficio; está dirigida a profesionales y aficionados avanzados, no a un primer aprendizaje sin supervisión.",
        machinesTitle = "La máquina de tallar facetas",
        machinesIntro = "Una máquina de tallar facetas (faceting machine) mantiene la piedra en un ángulo y un índice precisos contra un disco abrasivo giratorio (el plato o lap). Su precisión mecánica —una décima de grado en el ángulo, un punto exacto en el índice— es lo que distingue una talla profesional de un simple desbaste a mano alzada.",
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
                description = "Rueda dentada fijada en el extremo de la caña, generalmente graduada en 96 muescas (a veces 64 o 32 en modelos antiguos), que bloquea la rotación de la piedra en una posición precisa para cada faceta — condición indispensable para la simetría de una talla como el brillante redondo, que reparte sus facetas con simetría de orden 8."
            ),
            LapidaireComponent(
                nom = "Cheater (micrómetro de ajuste fino)",
                description = "Anillo de ajuste fino superpuesto al ángulo indicado en el brazo, que permite corregir unas décimas de grado sin tocar el ajuste principal — útil para compensar el desgaste irregular de un plato o ajustar un ángulo no previsto por la tabla de índice."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Varilla metálica (latón o acero) sobre la que se fija la piedra en bruto mediante pegamento o cera caliente, antes de insertarla en la caña. Un segundo dop, más corto, sirve para invertir la piedra y tallar la corona después del pabellón (o al revés, según el método)."
            ),
            LapidaireComponent(
                nom = "Plato (lap)",
                description = "Disco metálico giratorio sobre el que se fija o se carga el abrasivo — diamante electrodepositado o aglomerado para el desbaste y el formado, estaño o cobre cargado con pasta diamantada para el pulido final. Una máquina suele disponer de varios platos intercambiables, uno por cada etapa de grano."
            )
        ),
        disquesTitle = "Discos de formado y pulido",
        disquesIntro = "La talla avanza por etapas de grano cada vez más fino, cada una elimina las microrrayas dejadas por la anterior; saltarse una etapa deja marcas que el pulido final ya no puede corregir.",
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
                usage = "Plato blando (estaño, cobre, plomo-estaño o a veces resina/latón cargados con pasta diamantada) que da a las facetas su brillo espejo, sin eliminar material ni alterar la geometría. La elección del plato de pulido depende de la dureza y la sensibilidad térmica de la especie tallada."
            )
        ),
        anglesTitle = "Ángulos de referencia: el brillante redondo",
        anglesIntro = "Las proporciones siguientes son las publicadas por el matemático belga Marcel Tolkowsky en 1919, quien calculó el ángulo óptimo para maximizar el retorno de luz (brillo) y la dispersión (fuego) de un diamante tallado en brillante redondo — es la referencia histórica que aún hoy se usa como punto de partida, aunque los laboratorios modernos (especialmente el GIA) admiten un margen de tolerancia alrededor de estos valores en lugar de una cifra única. Las demás tallas (princesa, ovalada, pera, esmeralda, cojín...) siguen cada una su propio diagrama, muy variable según el creador del diagrama y el objetivo buscado (rendimiento de material frente a rendimiento óptico): no existe un valor universal comparable que citar sin remitirse a un diagrama concreto.",
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
            LapidaireTip(texte = "Compruebe la simetría final observando la piedra de frente bajo una fuente de luz puntual: los reflejos de las facetas de la corona deben formar un patrón regular; una asimetría visible a simple vista en esta etapa ya no se puede corregir en el pulido.")
        ),
        disclaimerTitle = "Un oficio que se aprende en el taller",
        disclaimerBody = "Esta ficha presenta referencias generales, no un manual completo: la talla de facetas se aprende mediante práctica supervisada, con material adecuado y normas de seguridad (protección ocular y respiratoria, refrigeración continua del plato) propias de cada taller y de cada máquina. Los diagramas mostrados proceden de fuentes reales y libres de derechos (véanse los créditos); en su ausencia temporal, solo se muestra la leyenda."
    )

    private val it = LapidairePage(
        intro = "Il lapidario dà forma e lucida le pietre grezze trasformandole in pietre tagliate. Questo mestiere comprende diverse specializzazioni — la sfaccettatura (faceting) per le pietre trasparenti, il cabochon per le pietre opache o traslucide, l'incisione e il lavoro ornamentale — ma la sfaccettatura, la più tecnica, è quella che dà a una pietra il suo gioco di luce. Questa scheda presenta l'attrezzatura e i riferimenti di base del mestiere; è rivolta a professionisti e appassionati esperti, non a un primo apprendimento senza supervisione.",
        machinesTitle = "La macchina sfaccettatrice",
        machinesIntro = "Una macchina sfaccettatrice (faceting machine) mantiene la pietra a un angolo e un indice precisi contro un disco abrasivo rotante (il piatto, o lap). La sua precisione meccanica — al decimo di grado per l'angolo, al punto esatto per l'indice — è ciò che distingue un taglio professionale da una semplice smerigliatura a mano libera.",
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
                description = "Ruota dentata fissata all'estremità della cannula, generalmente graduata su 96 tacche (talvolta 64 o 32 nei modelli più vecchi), che blocca la rotazione della pietra in una posizione precisa per ogni sfaccettatura — condizione indispensabile per la simmetria di un taglio come il brillante rotondo, che distribuisce le sue sfaccettature con simmetria di ordine 8."
            ),
            LapidaireComponent(
                nom = "Cheater (micrometro di correzione)",
                description = "Ghiera di regolazione fine sovrapposta all'angolo indicato sul braccio, che permette di correggere di qualche decimo di grado senza toccare la regolazione principale — utile per compensare l'usura irregolare di un piatto o impostare un angolo non previsto dalla tabella d'indice."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Asta metallica (ottone o acciaio) sulla quale la pietra grezza viene fissata con colla o cera calda, prima di essere inserita nella cannula. Un secondo dop, più corto, serve a capovolgere la pietra per tagliare la corona dopo il padiglione (o viceversa, a seconda del metodo)."
            ),
            LapidaireComponent(
                nom = "Piatto (lap)",
                description = "Disco metallico rotante sul quale è fissato o caricato l'abrasivo — diamante elettrodepositato o sinterizzato per la sgrossatura e la formatura, stagno o rame caricati con pasta diamantata per la lucidatura finale. Una macchina dispone generalmente di più piatti intercambiabili, uno per ogni fase di grana."
            )
        ),
        disquesTitle = "Dischi di formatura e lucidatura",
        disquesIntro = "Il taglio procede per fasi di grana via via più fine, ciascuna delle quali cancella i micrograffi lasciati dalla precedente; saltare una fase lascia segni che la lucidatura finale non può più correggere.",
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
                usage = "Piatto morbido (stagno, rame, piombo-stagno o talvolta resina/ottone caricati con pasta diamantata) che dà alle sfaccettature la loro brillantezza a specchio, senza asportare materiale né alterare la geometria. La scelta del piatto di lucidatura varia secondo la durezza e la sensibilità termica della specie tagliata."
            )
        ),
        anglesTitle = "Angoli di riferimento: il brillante rotondo",
        anglesIntro = "Le proporzioni seguenti sono quelle pubblicate dal matematico belga Marcel Tolkowsky nel 1919, che calcolò l'angolo ottimale per massimizzare il ritorno di luce (brillantezza) e la dispersione (fuoco) di un diamante tagliato a brillante rotondo — è il riferimento storico ancora oggi usato come punto di partenza, sebbene i laboratori moderni (in particolare il GIA) ammettano un intervallo di tolleranza attorno a questi valori anziché una cifra unica. Gli altri tagli (princess, ovale, a pera, smeraldo, cuscino...) seguono ciascuno un proprio diagramma, molto variabile a seconda di chi lo ha ideato e dell'obiettivo perseguito (resa di materiale contro resa ottica): non esiste un valore universale comparabile da citare senza fare riferimento a un diagramma preciso.",
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
            LapidaireTip(texte = "Verificare la simmetria finale osservando la pietra frontalmente sotto una sorgente di luce puntiforme: i riflessi delle sfaccettature della corona devono formare un motivo regolare; un'asimmetria visibile a occhio nudo a questo stadio non è più correggibile in fase di lucidatura.")
        ),
        disclaimerTitle = "Un mestiere che si impara in laboratorio",
        disclaimerBody = "Questa scheda presenta riferimenti generali, non un manuale completo: la sfaccettatura si impara con la pratica seguita da un istruttore, con attrezzatura adeguata e norme di sicurezza (protezione degli occhi e delle vie respiratorie, raffreddamento continuo del piatto) proprie di ogni laboratorio e di ogni macchina. I diagrammi mostrati provengono da fonti reali e libere da diritti (vedi crediti); in loro assenza temporanea, viene mostrata solo la didascalia."
    )

    private val de = LapidairePage(
        intro = "Der Lapidar formt und poliert Rohsteine zu geschliffenen Edelsteinen. Das Handwerk umfasst mehrere Spezialgebiete — den Facettenschliff für transparente Steine, den Cabochonschliff für opake oder durchscheinende Steine, Gravur und ornamentale Arbeiten —, doch der Facettenschliff, der technisch anspruchsvollste, ist es, der einem Stein sein Lichtspiel verleiht. Dieses Merkblatt stellt die Grundausstattung und die wichtigsten Kennwerte des Handwerks vor; es richtet sich an Fachleute und erfahrene Amateure, nicht an einen unbetreuten Erstversuch.",
        machinesTitle = "Die Facettiermaschine",
        machinesIntro = "Eine Facettiermaschine (faceting machine) hält den Stein in einem präzisen Winkel und Index gegen eine rotierende Schleifscheibe (den Lap). Ihre mechanische Präzision — auf ein Zehntel Grad beim Winkel, auf den Punkt genau beim Index — unterscheidet einen professionellen Schliff von einfachem freihändigem Schleifen.",
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
                description = "Gezahntes Rad am Ende der Pinole, meist mit 96 Rasten (bei älteren Modellen manchmal 64 oder 32), das die Drehung des Steins für jede Facette an einer präzisen Position feststellt — unerlässlich für die Symmetrie eines Schliffs wie des runden Brillanten, dessen Facetten 8-zählig symmetrisch angeordnet sind."
            ),
            LapidaireComponent(
                nom = "Cheater (Feinjustierring)",
                description = "Feinjustierring, der dem am Arm angezeigten Winkel überlagert wird und Korrekturen um wenige Zehntelgrad erlaubt, ohne die Hauptgrundeinstellung anzurühren — nützlich, um ungleichmäßigen Verschleiß eines Laps auszugleichen oder einen von der Indextabelle nicht vorgesehenen Winkel einzustellen."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Metallstab (Messing oder Stahl), auf den der Rohstein mit Kleber oder erhitztem Wachs befestigt wird, bevor er in die Pinole eingesetzt wird. Ein zweiter, kürzerer Dop dient dazu, den Stein zu wenden, um nach dem Pavillon die Krone zu schleifen (oder umgekehrt, je nach Methode)."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Rotierende Metallscheibe, auf der das Schleifmittel befestigt oder aufgetragen ist — galvanisch gebundener oder gesinterter Diamant zum Grobschliff und zur Facettenformung, mit Diamantpaste beladenes Zinn oder Kupfer für die Endpolitur. Eine Maschine verfügt in der Regel über mehrere austauschbare Laps, einen pro Körnungsstufe."
            )
        ),
        disquesTitle = "Form- und Polierscheiben",
        disquesIntro = "Der Schliff schreitet in Stufen zunehmend feinerer Körnung voran, wobei jede die von der vorherigen hinterlassenen Mikrokratzer entfernt; das Überspringen einer Stufe hinterlässt Spuren, die die Endpolitur nicht mehr beseitigen kann.",
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
                usage = "Weicher Lap (Zinn, Kupfer, Blei-Zinn oder manchmal mit Diamantpaste beladenes Harz/Messing), der den Facetten ihren spiegelnden Glanz verleiht, ohne Material abzutragen oder die Geometrie zu verändern. Die Wahl des Polier-Laps richtet sich nach Härte und Hitzeempfindlichkeit der geschliffenen Art."
            )
        ),
        anglesTitle = "Referenzwinkel: der runde Brillant",
        anglesIntro = "Die folgenden Proportionen wurden 1919 vom belgischen Mathematiker Marcel Tolkowsky veröffentlicht, der den optimalen Winkel berechnete, um Lichtrückwurf (Brillanz) und Dispersion (Feuer) eines rund geschliffenen Brillanten zu maximieren — der historische Bezugspunkt, der noch heute als Ausgangswert dient, auch wenn moderne Labore (insbesondere das GIA) statt einer einzelnen Zahl einen Toleranzbereich um diese Werte akzeptieren. Andere Schliffformen (Princess, Oval, Tropfen, Smaragd, Kissen...) folgen jeweils einem eigenen Diagramm, das je nach Entwickler und Zielsetzung (Materialausbeute gegenüber optischer Leistung) stark variiert: Ohne Bezug auf ein konkretes Diagramm gibt es keinen vergleichbaren, allgemeingültigen Wert.",
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
            LapidaireTip(texte = "Die endgültige Symmetrie prüfen, indem der Stein von vorn unter einer punktförmigen Lichtquelle betrachtet wird: Die Reflexe der Kronenfacetten sollten ein regelmäßiges Muster bilden; eine in dieser Stufe mit bloßem Auge sichtbare Asymmetrie lässt sich durch Polieren nicht mehr beheben.")
        ),
        disclaimerTitle = "Ein Handwerk, das man in der Werkstatt lernt",
        disclaimerBody = "Dieses Merkblatt zeigt allgemeine Anhaltspunkte, keine vollständige Anleitung: Der Facettenschliff wird durch angeleitete Praxis erlernt, mit geeigneter Ausrüstung und Sicherheitsvorschriften (Augen- und Atemschutz, durchgehende Lap-Kühlung), die für jede Werkstatt und jede Maschine spezifisch sind. Die gezeigten Diagramme stammen aus echten, frei lizenzierten Quellen (siehe Credits); solange sie vorübergehend fehlen, wird nur die Bildunterschrift angezeigt."
    )

    private val pt = LapidairePage(
        intro = "O lapidário dá forma e polimento às pedras brutas, transformando-as em pedras lapidadas. Este ofício abrange várias especialidades — a lapidação de facetas (faceting) para pedras transparentes, o cabochão para pedras opacas ou translúcidas, a gravação e o trabalho ornamental —, mas a lapidação de facetas, a mais técnica, é a que confere a uma pedra o seu jogo de luz. Esta ficha apresenta o equipamento e as referências básicas do ofício; destina-se a profissionais e amadores experientes, não a uma primeira aprendizagem sem supervisão.",
        machinesTitle = "A máquina de facetar",
        machinesIntro = "Uma máquina de facetar (faceting machine) mantém a pedra num ângulo e índice precisos contra um disco abrasivo rotativo (o prato, ou lap). A sua precisão mecânica — a um décimo de grau no ângulo, ao ponto exato no índice — é o que distingue uma lapidação profissional de um simples desbaste à mão livre.",
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
                description = "Roda dentada fixada na extremidade da cânula, geralmente graduada em 96 entalhes (por vezes 64 ou 32 em modelos antigos), que trava a rotação da pedra numa posição precisa para cada faceta — condição indispensável para a simetria de uma lapidação como o brilhante redondo, que distribui as suas facetas com simetria de ordem 8."
            ),
            LapidaireComponent(
                nom = "Cheater (micrómetro de afinação)",
                description = "Anel de afinação fina sobreposto ao ângulo indicado no braço, que permite corrigir alguns décimos de grau sem tocar no ajuste principal — útil para compensar o desgaste irregular de um prato ou ajustar um ângulo não previsto pela tabela de índice."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Haste metálica (latão ou aço) na qual a pedra bruta é fixada com cola ou cera aquecida, antes de ser inserida na cânula. Um segundo dop, mais curto, serve para inverter a pedra e lapidar a coroa depois do pavilhão (ou o inverso, consoante o método)."
            ),
            LapidaireComponent(
                nom = "Prato (lap)",
                description = "Disco metálico rotativo sobre o qual é fixado ou carregado o abrasivo — diamante eletrodepositado ou sinterizado para o desbaste e o formatado, estanho ou cobre carregados com pasta diamantada para o polimento final. Uma máquina dispõe geralmente de vários pratos intercambiáveis, um por cada etapa de grão."
            )
        ),
        disquesTitle = "Discos de formatação e polimento",
        disquesIntro = "A lapidação progride por etapas de grão cada vez mais fino, cada uma apagando os microrriscos deixados pela anterior; saltar uma etapa deixa marcas que o polimento final já não consegue corrigir.",
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
                usage = "Prato macio (estanho, cobre, chumbo-estanho ou por vezes resina/latão carregados com pasta diamantada) que dá às facetas o seu brilho espelhado, sem remover material nem alterar a geometria. A escolha do prato de polimento varia consoante a dureza e a sensibilidade térmica da espécie lapidada."
            )
        ),
        anglesTitle = "Ângulos de referência: o brilhante redondo",
        anglesIntro = "As proporções abaixo são as publicadas pelo matemático belga Marcel Tolkowsky em 1919, que calculou o ângulo ótimo para maximizar o retorno de luz (brilho) e a dispersão (fogo) de um diamante lapidado em brilhante redondo — a referência histórica ainda hoje usada como ponto de partida, embora os laboratórios modernos (nomeadamente o GIA) admitam uma margem de tolerância em torno destes valores em vez de um único número. As restantes lapidações (princesa, oval, pera, esmeralda, almofada...) seguem cada uma o seu próprio diagrama, muito variável consoante o criador do diagrama e o objetivo procurado (rendimento de material versus desempenho ótico): não existe um valor universal comparável a citar sem recorrer a um diagrama preciso.",
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
            LapidaireTip(texte = "Teste a simetria final observando a pedra de frente sob uma fonte de luz pontual: os reflexos das facetas da coroa devem formar um padrão regular; uma assimetria visível a olho nu nesta fase já não se corrige no polimento.")
        ),
        disclaimerTitle = "Um ofício que se aprende na oficina",
        disclaimerBody = "Esta ficha apresenta referências gerais, não um manual completo: a lapidação de facetas aprende-se através de prática supervisionada, com equipamento adequado e normas de segurança (proteção ocular e respiratória, arrefecimento contínuo do prato) próprias de cada oficina e de cada máquina. Os diagramas apresentados provêm de fontes reais e livres de direitos (ver créditos); na sua ausência temporária, é apresentada apenas a legenda."
    )

    private val ru = LapidairePage(
        intro = "Огранщик придаёт форму и полирует необработанные камни, превращая их в гранёные драгоценные камни. Это ремесло охватывает несколько специализаций — огранку фасетами (faceting) для прозрачных камней, кабошон для непрозрачных или полупрозрачных камней, гравировку и декоративную обработку, — но именно фасетная огранка, самая техничная из них, придаёт камню игру света. В этой статье представлены базовое оборудование и ориентиры ремесла; она рассчитана на профессионалов и опытных любителей, а не на первое самостоятельное обучение без наставника.",
        machinesTitle = "Огранной станок",
        machinesIntro = "Огранной станок (faceting machine) удерживает камень под точным углом и индексом относительно вращающегося абразивного диска (планшайбы, или лапа). Именно механическая точность — до десятой доли градуса по углу и до точки по индексу — отличает профессиональную огранку от простой ручной шлифовки.",
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
                description = "Зубчатое колесо на конце квиля, обычно градуированное на 96 позиций (иногда 64 или 32 у старых моделей), фиксирующее вращение камня в точном положении для каждой грани — необходимое условие симметрии такой огранки, как круглый бриллиант, чьи грани распределены с симметрией 8-го порядка."
            ),
            LapidaireComponent(
                nom = "Читер (микрометр точной подстройки)",
                description = "Кольцо тонкой регулировки, накладываемое поверх угла, указанного на рычаге, позволяющее скорректировать несколько десятых долей градуса, не трогая основную настройку — полезно для компенсации неравномерного износа планшайбы или для установки угла, не предусмотренного индексной таблицей."
            ),
            LapidaireComponent(
                nom = "Доп (dop)",
                description = "Металлический стержень (латунь или сталь), на который необработанный камень крепится клеем или разогретым воском перед установкой в квиль. Второй, более короткий доп используется для переворота камня, чтобы огранить коронку после павильона (или наоборот, в зависимости от метода)."
            ),
            LapidaireComponent(
                nom = "Планшайба (lap)",
                description = "Вращающийся металлический диск, на который нанесён или закреплён абразив — гальванически осаждённый или спечённый алмаз для обдирки и формирования граней, олово или медь с алмазной пастой для финальной полировки. На станке обычно установлено несколько сменных планшайб, по одной на каждый этап зернистости."
            )
        ),
        disquesTitle = "Диски для формирования граней и полировки",
        disquesIntro = "Огранка проходит через этапы всё более мелкой зернистости, каждый из которых устраняет микроцарапины, оставленные предыдущим; пропуск этапа оставляет следы, которые финальная полировка уже не устранит.",
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
                usage = "Мягкая планшайба (олово, медь, свинцово-оловянный сплав или иногда смола/латунь с алмазной пастой), придающая граням зеркальный блеск без снятия материала и изменения геометрии. Выбор полировочной планшайбы зависит от твёрдости и термочувствительности гранимого минерала."
            )
        ),
        anglesTitle = "Эталонные углы: круглый бриллиант",
        anglesIntro = "Приведённые ниже пропорции опубликованы бельгийским математиком Марселем Толковски в 1919 году: он рассчитал оптимальный угол для максимизации возврата света (блеска) и дисперсии (игры) бриллианта круглой огранки — это исторический эталон, до сих пор служащий отправной точкой, хотя современные лаборатории (в частности, GIA) допускают диапазон допуска вокруг этих значений, а не единственное число. Другие огранки (принцесса, овал, груша, изумрудная, кушон...) следуют собственным схемам, сильно различающимся в зависимости от автора схемы и цели (выход материала против оптических характеристик): не существует сопоставимого универсального значения, которое можно было бы привести без обращения к конкретной схеме.",
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
            LapidaireTip(texte = "Проверяйте окончательную симметрию, рассматривая камень анфас под точечным источником света: отражения граней коронки должны образовывать регулярный узор; асимметрия, видимая невооружённым глазом на этом этапе, уже не исправляется полировкой.")
        ),
        disclaimerTitle = "Ремесло, которому учатся в мастерской",
        disclaimerBody = "Эта статья приводит общие ориентиры, а не полное руководство: фасетная огранка осваивается через практику под наставничеством, с подходящим оборудованием и правилами безопасности (защита глаз и органов дыхания, непрерывное охлаждение планшайбы), специфичными для каждой мастерской и каждого станка. Показанные схемы взяты из реальных источников со свободными лицензиями (см. указания авторства); при их временном отсутствии отображается только подпись."
    )

    private val nl = LapidairePage(
        intro = "De lapidarist geeft ruwe stenen vorm en polijst ze tot geslepen edelstenen. Dit vak omvat verschillende specialisaties — het facetteren voor transparante stenen, de cabochon voor ondoorzichtige of doorschijnende stenen, gravure en ornamenteel werk — maar het facetteren, het meest technische onderdeel, is wat een steen zijn lichtspel geeft. Dit overzicht presenteert de basisuitrusting en de belangrijkste kengetallen van het vak; het is bedoeld voor professionals en ervaren liefhebbers, niet als eerste, onbegeleide les.",
        machinesTitle = "De facetteermachine",
        machinesIntro = "Een facetteermachine (faceting machine) houdt de steen onder een precieze hoek en index tegen een roterende slijpschijf (de lap). De mechanische precisie — tot op een tiende graad voor de hoek, tot op het punt nauwkeurig voor de index — is wat professioneel slijpwerk onderscheidt van eenvoudig uit de vrije hand slijpen.",
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
                description = "Getand wiel aan het uiteinde van de pen, meestal verdeeld in 96 standen (soms 64 of 32 bij oudere modellen), dat de rotatie van de steen bij elke facet op een precieze positie vastzet — onmisbaar voor de symmetrie van een slijpvorm zoals de ronde briljant, waarvan de facetten volgens een 8-voudige symmetrie zijn verdeeld."
            ),
            LapidaireComponent(
                nom = "Cheater (fijnafstelring)",
                description = "Fijnafstelring die over de op de arm aangegeven hoek wordt gelegd en correcties van enkele tienden van een graad toelaat zonder de hoofdinstelling aan te raken — nuttig om oneffen slijtage van een lap te compenseren of een hoek in te stellen die niet in de indextabel voorkomt."
            ),
            LapidaireComponent(
                nom = "Dop",
                description = "Metalen staafje (messing of staal) waarop de ruwe steen met lijm of verwarmde was wordt bevestigd, voordat het in de pen wordt gestoken. Een tweede, kortere dop wordt gebruikt om de steen om te draaien en na het paviljoen de kroon te slijpen (of omgekeerd, afhankelijk van de methode)."
            ),
            LapidaireComponent(
                nom = "Lap",
                description = "Roterende metalen schijf waarop het slijpmiddel is bevestigd of aangebracht — galvanisch of gesinterd diamant voor het grofslijpen en facetteren, tin of koper met diamantpasta voor de eindpolitoer. Een machine heeft doorgaans meerdere verwisselbare lappen, één per korrelfase."
            )
        ),
        disquesTitle = "Vorm- en polijstschijven",
        disquesIntro = "Het slijpen verloopt in steeds fijnere korrelfasen, waarbij elke fase de microkrasjes van de vorige verwijdert; het overslaan van een fase laat sporen achter die de eindpolitoer niet meer kan wegwerken.",
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
                usage = "Zachte lap (tin, koper, lood-tin of soms hars/messing met diamantpasta) die de facetten hun spiegelglans geeft, zonder materiaal weg te nemen of de geometrie te veranderen. De keuze van de polijstlap hangt af van de hardheid en warmtegevoeligheid van de geslepen soort."
            )
        ),
        anglesTitle = "Referentiehoeken: de ronde briljant",
        anglesIntro = "Onderstaande verhoudingen zijn die welke de Belgische wiskundige Marcel Tolkowsky in 1919 publiceerde: hij berekende de optimale hoek om lichtterugkaatsing (schittering) en dispersie (vuur) van een rond geslepen briljant te maximaliseren — het historische referentiepunt dat nog steeds als uitgangspunt dient, hoewel moderne laboratoria (met name GIA) een tolerantiemarge rond deze waarden aanvaarden in plaats van één vast cijfer. Andere slijpvormen (prinses, ovaal, peer, smaragd, kussen...) volgen elk hun eigen diagram, dat sterk verschilt per ontwerper en doel (materiaalopbrengst tegenover optische prestatie): er bestaat geen vergelijkbare universele waarde die zonder verwijzing naar een precies diagram kan worden aangehaald.",
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
            LapidaireTip(texte = "Test de uiteindelijke symmetrie door de steen van voren onder een puntvormige lichtbron te bekijken: de reflecties van de kroonfacetten moeten een regelmatig patroon vormen; een asymmetrie die in deze fase met het blote oog zichtbaar is, kan niet meer door polijsten worden verholpen.")
        ),
        disclaimerTitle = "Een vak dat je in de werkplaats leert",
        disclaimerBody = "Dit overzicht geeft algemene richtlijnen, geen volledige handleiding: facetteren wordt geleerd door begeleide praktijk, met geschikte apparatuur en veiligheidsvoorschriften (oog- en ademhalingsbescherming, continue lapkoeling) die per werkplaats en per machine verschillen. De getoonde diagrammen zijn afkomstig uit echte, vrij te gebruiken bronnen (zie credits); zolang ze tijdelijk ontbreken, wordt alleen het onderschrift getoond."
    )

    private val zh = LapidairePage(
        intro = "宝石切磨师将原石加工、抛光成琢磨宝石。这一行业涵盖多个专业方向——针对透明宝石的刻面切磨（faceting）、针对不透明或半透明宝石的凸圆面（cabochon）切磨，以及雕刻和装饰性加工——但技术含量最高的刻面切磨，正是赋予宝石光彩的关键工艺。本篇介绍这一行业的基本设备与参考要点，面向专业人士和有经验的爱好者，而非无人指导的初学者。",
        machinesTitle = "刻面切磨机",
        machinesIntro = "刻面切磨机（faceting machine）以精确的角度和刻度索引将宝石抵住旋转的磨盘（lap）。其机械精度——角度精确到十分之一度，索引精确到点位——正是专业切磨区别于徒手打磨的关键。",
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
                description = "固定在套筒末端的齿轮，通常刻有96个刻度（旧型号有时为64或32），可将宝石的旋转锁定在每个刻面对应的精确位置——这是圆形明亮式切工等按8重对称分布刻面的切工所必需的对称性保证。"
            ),
            LapidaireComponent(
                nom = "微调器（cheater）",
                description = "叠加在臂上所示角度之上的精细调节环，可在不改动主设定的情况下修正几分之一度——用于补偿磨盘不均匀磨损，或设定索引表未预设的角度。"
            ),
            LapidaireComponent(
                nom = "夹持杆（dop）",
                description = "金属杆（黄铜或钢），原石在插入套筒前用胶水或加热的蜡固定在其上。第二根较短的夹持杆用于翻转宝石，以便在亭部之后切磨冠部（或按方法相反顺序）。"
            ),
            LapidaireComponent(
                nom = "磨盘（lap）",
                description = "旋转金属圆盘，其上固定或涂敷磨料——电镀或烧结金刚石用于粗磨与刻面成型，锡或铜配以金刚石抛光膏用于最终抛光。一台切磨机通常配备多个可更换磨盘，每种粒度对应一片。"
            )
        ),
        disquesTitle = "成型与抛光磨盘",
        disquesIntro = "切磨过程按粒度由粗到细逐级进行，每一级都会磨去上一级留下的微划痕；跳过某一级会留下最终抛光也无法消除的痕迹。",
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
                usage = "使用软质磨盘（锡、铜、铅锡合金，或有时配以金刚石抛光膏的树脂/黄铜），使刻面呈现镜面光泽，而不去除材料或改变几何形状。抛光磨盘的选择取决于所切磨宝石的硬度与热敏感性。"
            )
        ),
        anglesTitle = "参考角度：圆形明亮式切工",
        anglesIntro = "以下比例出自比利时数学家马塞尔·托尔科夫斯基（Marcel Tolkowsky）1919年发表的计算结果，他推算出能使圆形明亮式切工钻石的回光（明亮度）与色散（火彩）最大化的最佳角度——这是至今仍被用作起点的历史基准，尽管现代实验室（尤其是GIA）在这些数值周围采用一个容差范围，而非单一数字。其他切工（公主方形、椭圆形、梨形、祖母绿形、垫形等）各自遵循不同的图纸，因设计者和目标（材料出成率与光学表现之间的取舍）不同而差异很大：若不参照具体图纸，并不存在一个可比较的通用数值。",
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
            LapidaireTip(texte = "通过在点光源下正面观察宝石来检验最终对称性：冠部刻面的反光应形成规则图案；此阶段肉眼可见的不对称，抛光阶段已无法纠正。")
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
