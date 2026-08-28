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

    private val en = TreatmentsPage(
        intro = "The vast majority of gemstones sold worldwide have undergone some form of treatment, often stable and widely accepted by the trade (heat treatment of sapphire, for example). What matters ethically is not the treatment itself, but its systematic disclosure to the buyer. This reference sheet lists the most common treatments, the species they most often affect, and clues observable under a loupe or microscope — formal confirmation remains the responsibility of an accredited gemological laboratory (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Heating (heat treatment)",
                description = "Heats the stone to a high temperature (sometimes above 1600°C) to improve its colour or clarity by dissolving certain inclusions or altering the oxidation state of colouring elements. The oldest and most widely accepted treatment on the market when it does not involve the addition of foreign material.",
                especesConcernees = "Corundum (sapphire, ruby), zircon, tanzanite, aquamarine, tourmaline, citrine (by heating amethyst)",
                indicesDetection = "Melted or haloed crystal inclusions (\"tension halo\"), stress discs around crystals, discontinuous growth striations; the total absence of inclusions does not rule out heat treatment.",
                obligationDivulgation = "Mandatory in most jurisdictions and systematically noted on a laboratory certificate; widely accepted by the market with no major price discount for sapphire and ruby."
            ),
            TreatmentEntry(
                nom = "Oiling and resin impregnation",
                description = "Fills surface-reaching fractures with a colourless oil (traditionally cedar oil) or a synthetic resin, to reduce their visibility and improve apparent clarity. A long-standing practice, widely tolerated for emerald provided it is disclosed.",
                especesConcernees = "Almost exclusively emerald; occasionally other stones with surface-reaching fractures (peridot, quartz)",
                indicesDetection = "Greasy or \"oily\" lustre along fractures under a loupe, bubbles or flow structures visible within the fissures under high magnification, differing UV fluorescence between treated zones and the host stone.",
                obligationDivulgation = "Mandatory; the degree of impregnation (minor, moderate, significant) must be stated on a certificate, as it strongly affects value."
            ),
            TreatmentEntry(
                nom = "Irradiation",
                description = "Exposes the stone to radiation (electron beam, gamma rays, nuclear reactor) to alter the atomic structure of colouring elements and thereby change the colour, often followed by heating to stabilise or refine the resulting hue.",
                especesConcernees = "Blue topaz (almost always originally colourless material that has been irradiated), smoky quartz, fancy-colour diamonds (blue, green, black), tourmaline",
                indicesDetection = "A colour of unusual intensity or hue for the natural species; confirmation generally requires a laboratory, as the irradiation source itself is not optically visible.",
                obligationDivulgation = "Mandatory; some countries require a quarantine period before sale to ensure the absence of residual radioactivity, checked by the supplier."
            ),
            TreatmentEntry(
                nom = "Diffusion",
                description = "Under prolonged heating, drives colouring chemical elements (titanium, chromium, or more recently beryllium) from the stone's surface toward its interior, producing either a surface-only colouration (classic diffusion) or colour that penetrates deep throughout the whole stone (beryllium diffusion).",
                especesConcernees = "Sapphire, notably the orange/pink \"padparadscha-like\" hues obtained through beryllium diffusion",
                indicesDetection = "Classic diffusion: colour concentration at the periphery visible in immersion, paler colour once the stone is repolished. Beryllium diffusion: undetectable under a loupe, requires laboratory chemical analysis (LA-ICP-MS).",
                obligationDivulgation = "Mandatory; beryllium diffusion must always be stated separately, as its impact on value is significantly greater than that of classic diffusion."
            ),
            TreatmentEntry(
                nom = "Lead-glass fracture filling",
                description = "Fills the major fractures of a low-quality ruby with a low-melting-point, lead-rich glass, dramatically improving the apparent clarity of a stone that would otherwise be unsellable in its natural state.",
                especesConcernees = "Almost exclusively ruby, occasionally sapphire",
                indicesDetection = "Gas bubbles trapped in the filling glass, a bluish or orange \"flash\" effect depending on the viewing angle under a loupe, irregular surface texture around the filled areas.",
                obligationDivulgation = "Mandatory and essential: these stones, sometimes called \"composite ruby,\" must be clearly distinguished from a simply heated ruby, as their value is far lower and their durability more compromised (possible attack by common household cleaning products)."
            ),
            TreatmentEntry(
                nom = "Dyeing",
                description = "Applies a colourant (organic or mineral) that penetrates the stone's natural porosity or along its fractures, to intensify or change its colour.",
                especesConcernees = "Jade (jadeite in particular), agate, pearl, coral, porous turquoise, lower-quality lapis lazuli",
                indicesDetection = "Colour concentration in fissures or porous areas visible under a loupe, an acetone-soaked cotton swab that picks up colour on contact with the stone (a destructive test, reserved for professionals), an artificially uniform colour.",
                obligationDivulgation = "Mandatory; dyed jade (sometimes combined with resin impregnation, a category known as \"Type C\") must be clearly distinguished from untreated natural jade (\"Type A\")."
            ),
            TreatmentEntry(
                nom = "Bleaching",
                description = "Uses a chemical agent (generally chlorine- or peroxide-based) to lighten or even out a stone's natural colour, often as preparation for a further treatment (dyeing, impregnation).",
                especesConcernees = "Jade, cultured pearl",
                indicesDetection = "Difficult to detect visually on its own; usually associated with other treatments (dyeing, impregnation) whose indicators are more distinctive.",
                obligationDivulgation = "Mandatory, generally disclosed together with the associated treatment (dyeing or impregnation)."
            ),
            TreatmentEntry(
                nom = "HPHT treatment (high pressure, high temperature)",
                description = "Subjects the stone to extreme pressure and temperature conditions, artificially reproducing deep formation conditions, to improve colour (decolourising brown diamonds to colourless, or producing fancy colours) or transparency.",
                especesConcernees = "Almost exclusively diamond",
                indicesDetection = "Undetectable under a loupe; systematically requires specialised laboratory analysis (spectroscopy), as HPHT diamonds may show atypical UV fluorescence or phosphorescence.",
                obligationDivulgation = "Mandatory and generally laser-inscribed on the stone's girdle by the laboratories that certify it, owing to the significant value gap with an untreated natural diamond."
            ),
            TreatmentEntry(
                nom = "Surface coating",
                description = "Deposits a thin metallic or oxide layer on the stone's surface (notably by physical vapour deposition) to produce an iridescent colour effect or a hue that does not naturally occur in the species.",
                especesConcernees = "Quartz (\"aura quartz,\" \"titanium quartz\"), topaz (\"mystic topaz\"), occasionally other transparent stones",
                indicesDetection = "Scratches or wear on the coating visible under a loupe on facet edges after extended wear, colour that seems to \"float\" on the surface rather than permeate the stone, iridescence unusual for the species.",
                obligationDivulgation = "Mandatory; this treatment is not very durable (the coating wears off over time) and must be disclosed as such, particularly for everyday jewellery wear."
            ),
            TreatmentEntry(
                nom = "Stabilisation impregnation (wax, resin, polymer)",
                description = "Impregnates a naturally porous or friable stone with a substance (wax, resin, polymer) to reinforce its structure, even out its surface appearance, and make it easier to shape and polish.",
                especesConcernees = "Turquoise (the great majority of commercial turquoise is stabilised), lapis lazuli, porous opal",
                indicesDetection = "Water-drop test (a bead of water sits on the surface of stabilised turquoise, while it is absorbed by untreated natural turquoise), a slight colour change on contact with a hot point (a destructive test, reserved for professionals).",
                obligationDivulgation = "Mandatory; stabilised turquoise must be clearly distinguished from untreated natural turquoise, which is markedly rarer and more expensive."
            ),
            TreatmentEntry(
                nom = "Laser drilling",
                description = "Drills a fine laser channel down to a disturbing dark inclusion inside a diamond, then injects a bleaching agent into it or fills the channel, to improve the stone's apparent clarity.",
                especesConcernees = "Almost exclusively diamond",
                indicesDetection = "A fine, straight or slightly curved channel visible under high magnification, generally reaching the surface; the targeted inclusion sometimes appears bleached or partially dissolved.",
                obligationDivulgation = "Mandatory and systematically noted on a laboratory certificate, with a significant impact on value compared to a diamond of naturally equivalent clarity."
            )
        ),
        disclaimerTitle = "Laboratory confirmation remains essential",
        disclaimerBody = "The clues presented here can guide an observation under a loupe or microscope, but they never replace analysis by an accredited gemological laboratory (GIA, Gübelin, GFCO, SSEF, AGL...), the only body authorised to formally confirm a treatment. Systematic disclosure of treatments to the buyer is an ethical obligation and, in many jurisdictions, a legal one — regardless of how well the treatment is accepted by the market."
    )

    private val es = TreatmentsPage(
        intro = "La gran mayoría de las piedras preciosas vendidas en el mundo han recibido algún tratamiento, a menudo estable y ampliamente aceptado por el mercado (el tratamiento térmico del zafiro, por ejemplo). Lo que importa desde el punto de vista deontológico no es el tratamiento en sí, sino su divulgación sistemática al comprador. Esta ficha recoge los tratamientos más habituales, las especies a las que afectan con mayor frecuencia y algunos indicios observables con lupa o microscopio; una confirmación formal sigue siendo competencia exclusiva de un laboratorio gemológico acreditado (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Tratamiento térmico (calentamiento)",
                description = "Calienta la piedra a alta temperatura (a veces más de 1600 °C) para mejorar su color o su pureza disolviendo ciertas inclusiones o modificando el estado de oxidación de los elementos cromóforos. Es el tratamiento más antiguo y el más ampliamente aceptado del mercado cuando no implica la incorporación de materia.",
                especesConcernees = "Corindón (zafiro, rubí), circón, tanzanita, aguamarina, turmalina, citrino (por calentamiento de amatista)",
                indicesDetection = "Inclusiones cristalinas fundidas o rodeadas de un halo (halo de tensión), discos de tensión alrededor de cristales, estrías de crecimiento discontinuas; la ausencia total de inclusiones no descarta un calentamiento.",
                obligationDivulgation = "Obligatoria en la mayoría de las jurisdicciones y mencionada sistemáticamente en un certificado de laboratorio; ampliamente aceptada por el mercado, sin una reducción de valor significativa para el zafiro y el rubí."
            ),
            TreatmentEntry(
                nom = "Impregnación de aceite y resina",
                description = "Rellena las fracturas de superficie con un aceite incoloro (tradicionalmente aceite de cedro) o una resina sintética, para atenuar su visibilidad y mejorar la claridad aparente. Práctica antigua y ampliamente tolerada en la esmeralda, siempre que se divulgue.",
                especesConcernees = "Casi exclusivamente esmeralda; ocasionalmente otras piedras con fracturas de superficie (peridoto, cuarzo)",
                indicesDetection = "Brillo graso o «aceitoso» a lo largo de las fracturas con lupa, burbujas o flujos visibles en las fisuras con gran aumento, fluorescencia UV diferente entre las zonas tratadas y la matriz.",
                obligationDivulgation = "Obligatoria; el grado de impregnación (leve, moderado, importante) debe indicarse en un certificado, ya que influye fuertemente en el valor."
            ),
            TreatmentEntry(
                nom = "Irradiación",
                description = "Expone la piedra a una radiación (haz de electrones, rayos gamma, reactor nuclear) para modificar la estructura atómica de los elementos cromóforos y así cambiar el color, a menudo seguida de un calentamiento para estabilizar o afinar el tono obtenido.",
                especesConcernees = "Topacio azul (casi siempre a partir de material incoloro irradiado), cuarzo ahumado, diamantes de color fantasía (azul, verde, negro), turmalina",
                indicesDetection = "Color de una intensidad o tonalidad inusual para la especie natural; la confirmación suele requerir un laboratorio, ya que la fuente de irradiación no es visible ópticamente.",
                obligationDivulgation = "Obligatoria; algunos países exigen un período de cuarentena antes de la comercialización para garantizar la ausencia de radiactividad residual, controlada por el proveedor."
            ),
            TreatmentEntry(
                nom = "Difusión",
                description = "Hace penetrar, bajo calentamiento prolongado, elementos químicos cromóforos (titanio, cromo, o más recientemente berilio) desde la superficie de la piedra hacia el interior, produciendo una coloración superficial (difusión clásica) o penetrando profundamente en todo el cuerpo de la piedra (difusión de berilio).",
                especesConcernees = "Zafiro, especialmente los tonos naranja/rosa tipo «padparadscha» obtenidos por difusión de berilio",
                indicesDetection = "Difusión clásica: concentración de color en la periferia visible por inmersión, color más pálido tras repulir la piedra. Difusión de berilio: indetectable con lupa, requiere un análisis químico de laboratorio (LA-ICP-MS).",
                obligationDivulgation = "Obligatoria; la difusión de berilio debe mencionarse siempre por separado, ya que su impacto en el valor es sensiblemente mayor que el de la difusión clásica."
            ),
            TreatmentEntry(
                nom = "Relleno de fracturas con vidrio de plomo",
                description = "Rellena las fracturas importantes de un rubí de calidad mediocre con un vidrio rico en plomo y de bajo punto de fusión, mejorando notablemente la claridad aparente de una piedra que de otro modo sería invendible en su estado natural.",
                especesConcernees = "Casi exclusivamente rubí, a veces zafiro",
                indicesDetection = "Burbujas de gas atrapadas en el vidrio de relleno, efecto «flash» de color azulado o anaranjado según el ángulo de observación con lupa, textura de superficie irregular alrededor de las zonas rellenadas.",
                obligationDivulgation = "Obligatoria e ineludible: estas piedras, a veces llamadas «rubí compuesto», deben distinguirse claramente de un rubí simplemente calentado, ya que su valor es muy inferior y su fragilidad mayor (posible ataque por productos de limpieza domésticos habituales)."
            ),
            TreatmentEntry(
                nom = "Tinción",
                description = "Aplica un colorante (orgánico o mineral) que penetra en la porosidad natural de la piedra o a lo largo de sus fracturas, para intensificar o modificar su color.",
                especesConcernees = "Jade (especialmente la jadeíta), ágata, perla, coral, turquesa porosa, lapislázuli de calidad inferior",
                indicesDetection = "Concentración de color en las fisuras o zonas porosas visible con lupa, algodón impregnado de acetona que se tiñe al contacto con la piedra (prueba destructiva, reservada a profesionales), color de uniformidad artificial.",
                obligationDivulgation = "Obligatoria; el jade teñido (a veces combinado con impregnación de resina, categoría llamada «tipo C») debe distinguirse claramente del jade natural no tratado («tipo A»)."
            ),
            TreatmentEntry(
                nom = "Blanqueamiento",
                description = "Utiliza un agente químico (generalmente a base de cloro o peróxido) para aclarar o uniformizar el color natural de una piedra, a menudo como preparación para otro tratamiento (tinción, impregnación).",
                especesConcernees = "Jade, perla cultivada",
                indicesDetection = "Difícilmente detectable visualmente por sí solo; suele estar asociado a otros tratamientos (tinción, impregnación) cuyos indicios son más característicos.",
                obligationDivulgation = "Obligatoria, generalmente mencionada junto con el tratamiento asociado (tinción o impregnación)."
            ),
            TreatmentEntry(
                nom = "Tratamiento HPHT (alta presión y alta temperatura)",
                description = "Somete la piedra a condiciones extremas de presión y temperatura, reproduciendo artificialmente las condiciones de formación en profundidad, para mejorar el color (decoloración de diamantes marrones hasta incoloros, o producción de colores fantasía) o la transparencia.",
                especesConcernees = "Casi exclusivamente diamante",
                indicesDetection = "Indetectable con lupa; requiere sistemáticamente un análisis de laboratorio especializado (espectroscopia), ya que los diamantes HPHT pueden presentar fluorescencia o fosforescencia UV atípica.",
                obligationDivulgation = "Obligatoria y generalmente grabada con láser en el filete de la piedra por los laboratorios que la certifican, debido a la importante diferencia de valor con un diamante natural no tratado."
            ),
            TreatmentEntry(
                nom = "Recubrimiento superficial (coating)",
                description = "Deposita una fina capa metálica o de óxido en la superficie de la piedra (especialmente mediante deposición física en fase vapor) para producir un efecto de color iridiscente o un tono que no existe naturalmente en la especie.",
                especesConcernees = "Cuarzo («cuarzo aura», «cuarzo titanio»), topacio («topacio místico»), ocasionalmente otras piedras transparentes",
                indicesDetection = "Rayones o desgaste del recubrimiento visibles con lupa en las aristas de las facetas tras un uso prolongado, color que parece «flotar» en la superficie en lugar de impregnar la piedra, iridiscencia inusual para la especie.",
                obligationDivulgation = "Obligatoria; este tratamiento es poco duradero (el recubrimiento se desgasta con el tiempo) y debe señalarse como tal, en particular para un uso diario en joyería."
            ),
            TreatmentEntry(
                nom = "Impregnación estabilizante (cera, resina, polímero)",
                description = "Impregna una piedra naturalmente porosa o friable con una sustancia (cera, resina, polímero) para reforzar su estructura, uniformizar su aspecto superficial y facilitar su tallado y pulido.",
                especesConcernees = "Turquesa (la gran mayoría de la turquesa comercial está estabilizada), lapislázuli, ópalo poroso",
                indicesDetection = "Prueba de la gota de agua (una gota perlada permanece en la superficie de una turquesa estabilizada, mientras que es absorbida por una turquesa natural no tratada), ligero cambio de color al contacto con un objeto caliente (prueba destructiva, reservada a profesionales).",
                obligationDivulgation = "Obligatoria; la turquesa estabilizada debe distinguirse claramente de la turquesa natural no tratada, considerablemente más rara y costosa."
            ),
            TreatmentEntry(
                nom = "Perforación láser",
                description = "Perfora un fino canal con láser hasta una inclusión oscura molesta en el interior de un diamante, para luego inyectar un agente blanqueador o rellenar el canal, mejorando la pureza aparente de la piedra.",
                especesConcernees = "Casi exclusivamente diamante",
                indicesDetection = "Fino canal rectilíneo o ligeramente curvado visible con gran aumento, que generalmente aflora en la superficie; la inclusión afectada a veces aparece blanqueada o parcialmente disuelta.",
                obligationDivulgation = "Obligatoria y mencionada sistemáticamente en un certificado de laboratorio, con un impacto considerable en el valor respecto a un diamante de pureza naturalmente equivalente."
            )
        ),
        disclaimerTitle = "Sigue siendo necesaria una confirmación de laboratorio",
        disclaimerBody = "Los indicios presentados aquí permiten orientar una observación con lupa o microscopio, pero en ningún caso sustituyen un análisis de un laboratorio gemológico acreditado (GIA, Gübelin, GFCO, SSEF, AGL...), único habilitado para confirmar formalmente un tratamiento. La divulgación sistemática de los tratamientos al comprador es una obligación deontológica y, en numerosas jurisdicciones, también legal, independientemente de la aceptación del tratamiento por parte del mercado."
    )

    private val it = TreatmentsPage(
        intro = "La grande maggioranza delle pietre preziose vendute nel mondo ha subito un trattamento, spesso stabile e ampiamente accettato dal mercato (il trattamento termico dello zaffiro, ad esempio). Ciò che conta dal punto di vista deontologico non è il trattamento in sé, ma la sua sistematica divulgazione all'acquirente. Questa scheda elenca i trattamenti più comuni, le specie che riguardano più spesso e alcuni indizi osservabili con la lente o al microscopio: una conferma formale resta di competenza di un laboratorio gemmologico accreditato (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Trattamento termico (riscaldamento)",
                description = "Riscalda la pietra ad alta temperatura (a volte oltre 1600 °C) per migliorarne il colore o la purezza, dissolvendo alcune inclusioni o modificando lo stato di ossidazione degli elementi cromofori. È il trattamento più antico e più ampiamente accettato dal mercato quando non comporta l'aggiunta di materia.",
                especesConcernees = "Corindone (zaffiro, rubino), zircone, tanzanite, acquamarina, tormalina, citrino (per riscaldamento di ametista)",
                indicesDetection = "Inclusioni cristalline fuse o circondate da un alone (alone di tensione), dischi di tensione attorno ai cristalli, striature di crescita discontinue; l'assenza totale di inclusioni non esclude un riscaldamento.",
                obligationDivulgation = "Obbligatoria nella maggior parte delle giurisdizioni e sistematicamente indicata su un certificato di laboratorio; ampiamente accettata dal mercato senza una significativa riduzione di valore per zaffiro e rubino."
            ),
            TreatmentEntry(
                nom = "Oliatura e impregnazione resinosa",
                description = "Riempie le fratture affioranti in superficie con un olio incolore (tradizionalmente olio di cedro) o una resina sintetica, per attenuarne la visibilità e migliorare la purezza apparente. Pratica antica e ampiamente tollerata per lo smeraldo, a condizione che venga divulgata.",
                especesConcernees = "Quasi esclusivamente smeraldo; occasionalmente altre pietre con fratture di superficie (peridoto, quarzo)",
                indicesDetection = "Lucentezza grassa o «oleosa» lungo le fratture osservata con la lente, bolle o flussi visibili nelle fessure a forte ingrandimento, fluorescenza UV diversa tra le zone trattate e la matrice.",
                obligationDivulgation = "Obbligatoria; il grado di impregnazione (lieve, moderato, importante) deve essere indicato su un certificato, poiché influisce fortemente sul valore."
            ),
            TreatmentEntry(
                nom = "Irradiazione",
                description = "Espone la pietra a una radiazione (fascio di elettroni, raggi gamma, reattore nucleare) per modificare la struttura atomica degli elementi cromofori e quindi cambiarne il colore, spesso seguita da un riscaldamento per stabilizzare o affinare la tonalità ottenuta.",
                especesConcernees = "Topazio blu (quasi sempre ottenuto da materiale incolore irradiato), quarzo affumicato, diamanti di colore fantasia (blu, verde, nero), tormalina",
                indicesDetection = "Colore di intensità o tonalità insolita per la specie naturale; la conferma richiede generalmente un laboratorio, poiché la fonte di irradiazione non è visibile otticamente.",
                obligationDivulgation = "Obbligatoria; alcuni paesi impongono un periodo di quarantena prima della commercializzazione per garantire l'assenza di radioattività residua, controllata dal fornitore."
            ),
            TreatmentEntry(
                nom = "Diffusione",
                description = "Fa penetrare, tramite riscaldamento prolungato, elementi chimici cromofori (titanio, cromo, o più recentemente berillio) dalla superficie della pietra verso l'interno, producendo una colorazione superficiale (diffusione classica) oppure penetrando in profondità in tutto il corpo della pietra (diffusione al berillio).",
                especesConcernees = "Zaffiro, in particolare le tonalità arancio/rosa «padparadscha-like» ottenute per diffusione al berillio",
                indicesDetection = "Diffusione classica: concentrazione di colore alla periferia visibile per immersione, colore più pallido dopo una nuova lucidatura della pietra. Diffusione al berillio: non rilevabile con la lente, richiede un'analisi chimica di laboratorio (LA-ICP-MS).",
                obligationDivulgation = "Obbligatoria; la diffusione al berillio deve essere sempre indicata separatamente, poiché il suo impatto sul valore è sensibilmente maggiore rispetto alla diffusione classica."
            ),
            TreatmentEntry(
                nom = "Riempimento di fratture con vetro al piombo",
                description = "Riempie le fratture importanti di un rubino di qualità scadente con un vetro ricco di piombo a basso punto di fusione, migliorando notevolmente la purezza apparente di una pietra altrimenti invendibile allo stato naturale.",
                especesConcernees = "Quasi esclusivamente rubino, talvolta zaffiro",
                indicesDetection = "Bolle di gas intrappolate nel vetro di riempimento, effetto «flash» di colore bluastro o arancione a seconda dell'angolo di osservazione con la lente, texture superficiale irregolare attorno alle zone riempite.",
                obligationDivulgation = "Obbligatoria e imprescindibile: queste pietre, talvolta chiamate «rubino composito», devono essere chiaramente distinte da un rubino semplicemente riscaldato, poiché il loro valore è molto inferiore e la loro fragilità maggiore (possibile aggressione da parte di comuni prodotti per la pulizia)."
            ),
            TreatmentEntry(
                nom = "Tintura",
                description = "Applica un colorante (organico o minerale) che penetra nella porosità naturale della pietra o lungo le sue fratture, per intensificarne o modificarne il colore.",
                especesConcernees = "Giada (in particolare la giadeite), agata, perla, corallo, turchese poroso, lapislazzuli di qualità inferiore",
                indicesDetection = "Concentrazione di colore nelle fessure o nelle zone porose visibile con la lente, cotone imbevuto di acetone che si colora a contatto con la pietra (test distruttivo, riservato ai professionisti), colore di uniformità artificiale.",
                obligationDivulgation = "Obbligatoria; la giada tinta (talvolta combinata con un'impregnazione di resina, categoria detta «tipo C») deve essere chiaramente distinta dalla giada naturale non trattata («tipo A»)."
            ),
            TreatmentEntry(
                nom = "Sbiancamento",
                description = "Utilizza un agente chimico (generalmente a base di cloro o perossido) per schiarire o uniformare il colore naturale di una pietra, spesso in preparazione di un altro trattamento (tintura, impregnazione).",
                especesConcernees = "Giada, perla coltivata",
                indicesDetection = "Difficilmente rilevabile visivamente da solo; spesso associato ad altri trattamenti (tintura, impregnazione) i cui indizi sono più caratteristici.",
                obligationDivulgation = "Obbligatoria, generalmente indicata insieme al trattamento associato (tintura o impregnazione)."
            ),
            TreatmentEntry(
                nom = "Trattamento HPHT (alta pressione alta temperatura)",
                description = "Sottopone la pietra a condizioni estreme di pressione e temperatura, riproducendo artificialmente le condizioni di formazione in profondità, per migliorarne il colore (decolorazione di diamanti marroni fino a incolori, o produzione di colori fantasia) o la trasparenza.",
                especesConcernees = "Quasi esclusivamente diamante",
                indicesDetection = "Non rilevabile con la lente; richiede sistematicamente un'analisi di laboratorio specializzata (spettroscopia), poiché i diamanti HPHT possono presentare una fluorescenza o fosforescenza UV atipica.",
                obligationDivulgation = "Obbligatoria e generalmente incisa al laser sulla cintura della pietra dai laboratori che la certificano, a causa del notevole divario di valore rispetto a un diamante naturale non trattato."
            ),
            TreatmentEntry(
                nom = "Rivestimento superficiale (coating)",
                description = "Deposita un sottile strato metallico o di ossido sulla superficie della pietra (in particolare tramite deposizione fisica da vapore) per produrre un effetto di colore iridescente o una tonalità che non esiste naturalmente nella specie.",
                especesConcernees = "Quarzo («quarzo aura», «quarzo titanio»), topazio («topazio mistico»), occasionalmente altre pietre trasparenti",
                indicesDetection = "Graffi o usura del rivestimento visibili con la lente sugli spigoli delle sfaccettature dopo un uso prolungato, colore che sembra «galleggiare» sulla superficie invece di impregnare la pietra, iridescenza insolita per la specie.",
                obligationDivulgation = "Obbligatoria; questo trattamento è poco durevole (il rivestimento si consuma nel tempo) e deve essere segnalato come tale, in particolare per un uso quotidiano in gioielleria."
            ),
            TreatmentEntry(
                nom = "Impregnazione stabilizzante (cera, resina, polimero)",
                description = "Impregna una pietra naturalmente porosa o friabile con una sostanza (cera, resina, polimero) per rinforzarne la struttura, uniformarne l'aspetto superficiale e facilitarne la lavorazione e la lucidatura.",
                especesConcernees = "Turchese (la grande maggioranza del turchese commerciale è stabilizzato), lapislazzuli, opale poroso",
                indicesDetection = "Test della goccia d'acqua (una goccia perlata resta in superficie su un turchese stabilizzato, mentre viene assorbita da un turchese naturale non trattato), leggero cambiamento di colore a contatto con un oggetto caldo (test distruttivo, riservato ai professionisti).",
                obligationDivulgation = "Obbligatoria; il turchese stabilizzato deve essere chiaramente distinto dal turchese naturale non trattato, nettamente più raro e costoso."
            ),
            TreatmentEntry(
                nom = "Foratura laser",
                description = "Perfora un sottile canale con il laser fino a un'inclusione scura fastidiosa all'interno di un diamante, per poi iniettarvi un agente sbiancante o riempire il canale, migliorando la purezza apparente della pietra.",
                especesConcernees = "Quasi esclusivamente diamante",
                indicesDetection = "Sottile canale rettilineo o leggermente curvo visibile a forte ingrandimento, generalmente affiorante in superficie; l'inclusione interessata appare talvolta sbiancata o parzialmente dissolta.",
                obligationDivulgation = "Obbligatoria e sistematicamente indicata su un certificato di laboratorio, con un impatto significativo sul valore rispetto a un diamante di purezza naturalmente equivalente."
            )
        ),
        disclaimerTitle = "Resta necessaria una conferma di laboratorio",
        disclaimerBody = "Gli indizi qui presentati permettono di orientare un'osservazione con la lente o al microscopio, ma non sostituiscono in alcun caso un'analisi di un laboratorio gemmologico accreditato (GIA, Gübelin, GFCO, SSEF, AGL...), l'unico abilitato a confermare formalmente un trattamento. La divulgazione sistematica dei trattamenti all'acquirente è un obbligo deontologico e, in molte giurisdizioni, anche legale, indipendentemente dall'accettazione del trattamento da parte del mercato."
    )

    private val de = TreatmentsPage(
        intro = "Die überwiegende Mehrheit der weltweit verkauften Edelsteine wurde behandelt, häufig auf stabile und vom Markt weitgehend akzeptierte Weise (etwa die Erhitzung von Saphir). Aus deontologischer Sicht kommt es nicht auf die Behandlung selbst an, sondern auf ihre konsequente Offenlegung gegenüber dem Käufer. Dieses Merkblatt führt die gängigsten Behandlungen auf, die Arten, die davon am häufigsten betroffen sind, sowie Hinweise, die mit der Lupe oder dem Mikroskop beobachtet werden können – eine verbindliche Bestätigung bleibt einem akkreditierten gemmologischen Labor vorbehalten (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Erhitzung (Brennen)",
                description = "Erhitzt den Stein auf hohe Temperatur (mitunter über 1600 °C), um seine Farbe oder Reinheit zu verbessern, indem bestimmte Einschlüsse aufgelöst oder der Oxidationszustand der farbgebenden Elemente verändert wird. Die älteste und vom Markt am weitesten akzeptierte Behandlung, sofern dabei kein Fremdmaterial zugesetzt wird.",
                especesConcernees = "Korund (Saphir, Rubin), Zirkon, Tansanit, Aquamarin, Turmalin, Citrin (durch Erhitzen von Amethyst)",
                indicesDetection = "Geschmolzene oder von einem Hof umgebene Kristalleinschlüsse (Spannungshof), Spannungsscheiben um Kristalle, unterbrochene Wachstumsstreifen; das völlige Fehlen von Einschlüssen schließt eine Erhitzung nicht aus.",
                obligationDivulgation = "In den meisten Rechtsordnungen verpflichtend und stets auf einem Laborzertifikat vermerkt; vom Markt weitgehend akzeptiert, ohne nennenswerten Preisabschlag bei Saphir und Rubin."
            ),
            TreatmentEntry(
                nom = "Ölung und Harzimprägnierung",
                description = "Füllt an die Oberfläche reichende Risse mit einem farblosen Öl (traditionell Zedernöl) oder einem synthetischen Harz, um deren Sichtbarkeit zu verringern und die scheinbare Reinheit zu verbessern. Eine alte, bei Smaragd weithin tolerierte Praxis, sofern sie offengelegt wird.",
                especesConcernees = "Fast ausschließlich Smaragd; gelegentlich andere Steine mit oberflächlichen Rissen (Peridot, Quarz)",
                indicesDetection = "Fettiger oder „öliger\" Glanz entlang der Risse unter der Lupe, in den Rissen sichtbare Blasen oder Fließstrukturen bei starker Vergrößerung, unterschiedliche UV-Fluoreszenz zwischen behandelten Zonen und der Grundmasse.",
                obligationDivulgation = "Verpflichtend; der Grad der Imprägnierung (gering, mäßig, stark) muss auf einem Zertifikat angegeben werden, da er den Wert erheblich beeinflusst."
            ),
            TreatmentEntry(
                nom = "Bestrahlung",
                description = "Setzt den Stein einer Strahlung aus (Elektronenstrahl, Gammastrahlen, Kernreaktor), um die Atomstruktur der farbgebenden Elemente zu verändern und dadurch die Farbe zu ändern, häufig gefolgt von einer Erhitzung zur Stabilisierung oder Verfeinerung des erzielten Farbtons.",
                especesConcernees = "Blauer Topas (fast immer aus ursprünglich farblosem, bestrahltem Material), Rauchquarz, Fancy-Color-Diamanten (blau, grün, schwarz), Turmalin",
                indicesDetection = "Eine für die natürliche Art ungewöhnlich intensive oder ungewöhnliche Farbe; die Bestätigung erfordert in der Regel ein Labor, da die Bestrahlungsquelle optisch nicht sichtbar ist.",
                obligationDivulgation = "Verpflichtend; manche Länder schreiben vor dem Verkauf eine Quarantänezeit vor, um die Abwesenheit von Restradioaktivität sicherzustellen, kontrolliert durch den Lieferanten."
            ),
            TreatmentEntry(
                nom = "Diffusion",
                description = "Lässt unter anhaltender Erhitzung farbgebende chemische Elemente (Titan, Chrom oder neuerdings Beryllium) von der Oberfläche des Steins ins Innere eindringen, wodurch entweder eine oberflächliche Färbung (klassische Diffusion) oder eine tief in den gesamten Steinkörper eindringende Färbung (Berylliumdiffusion) entsteht.",
                especesConcernees = "Saphir, insbesondere die durch Berylliumdiffusion erzielten orange-rosa „padparadscha-ähnlichen\" Farbtöne",
                indicesDetection = "Klassische Diffusion: unter Immersion sichtbare Farbkonzentration am Rand, blassere Farbe nach erneutem Polieren des Steins. Berylliumdiffusion: mit der Lupe nicht nachweisbar, erfordert eine chemische Laboranalyse (LA-ICP-MS).",
                obligationDivulgation = "Verpflichtend; die Berylliumdiffusion muss stets gesondert angegeben werden, da ihr Einfluss auf den Wert deutlich größer ist als bei der klassischen Diffusion."
            ),
            TreatmentEntry(
                nom = "Bleiglasfüllung von Rissen",
                description = "Füllt große Risse eines minderwertigen Rubins mit einem bleireichen Glas mit niedrigem Schmelzpunkt, wodurch die scheinbare Reinheit eines andernfalls in diesem Zustand unverkäuflichen Steins erheblich verbessert wird.",
                especesConcernees = "Fast ausschließlich Rubin, gelegentlich Saphir",
                indicesDetection = "Im Füllglas eingeschlossene Gasblasen, ein je nach Betrachtungswinkel unter der Lupe erkennbarer bläulicher oder orangefarbener „Flash\"-Effekt, unregelmäßige Oberflächenstruktur rund um die gefüllten Bereiche.",
                obligationDivulgation = "Zwingend und unbedingt verpflichtend: Diese Steine, mitunter „Komposit-Rubin\" genannt, müssen klar von einem bloß erhitzten Rubin unterschieden werden, da ihr Wert deutlich niedriger und ihre Zerbrechlichkeit größer ist (mögliche Angriffe durch gängige Reinigungsmittel)."
            ),
            TreatmentEntry(
                nom = "Färbung",
                description = "Trägt einen Farbstoff (organisch oder mineralisch) auf, der in die natürliche Porosität des Steins oder entlang seiner Risse eindringt, um dessen Farbe zu intensivieren oder zu verändern.",
                especesConcernees = "Jade (insbesondere Jadeit), Achat, Perle, Koralle, poröser Türkis, minderwertiger Lapislazuli",
                indicesDetection = "Unter der Lupe sichtbare Farbkonzentration in Rissen oder porösen Bereichen, ein mit Aceton getränkter Wattebausch, der sich bei Kontakt mit dem Stein verfärbt (destruktiver Test, ausschließlich für Fachleute), künstlich wirkende, gleichmäßige Farbe.",
                obligationDivulgation = "Verpflichtend; gefärbte Jade (mitunter kombiniert mit einer Harzimprägnierung, als „Typ C\" bezeichnet) muss klar von unbehandelter, natürlicher Jade („Typ A\") unterschieden werden."
            ),
            TreatmentEntry(
                nom = "Bleichen",
                description = "Verwendet ein chemisches Mittel (in der Regel auf Chlor- oder Peroxidbasis), um die natürliche Farbe eines Steins aufzuhellen oder zu vereinheitlichen, häufig als Vorbereitung für eine weitere Behandlung (Färbung, Imprägnierung).",
                especesConcernees = "Jade, Zuchtperle",
                indicesDetection = "Allein visuell nur schwer nachweisbar; meist mit anderen Behandlungen (Färbung, Imprägnierung) verbunden, deren Hinweise charakteristischer sind.",
                obligationDivulgation = "Verpflichtend, in der Regel zusammen mit der begleitenden Behandlung (Färbung oder Imprägnierung) angegeben."
            ),
            TreatmentEntry(
                nom = "HPHT-Behandlung (Hochdruck-Hochtemperatur)",
                description = "Setzt den Stein extremen Druck- und Temperaturbedingungen aus, die künstlich die Bedingungen einer tiefen Entstehung nachbilden, um die Farbe (Entfärbung brauner Diamanten zu farblos, oder Erzeugung von Fancy-Farben) oder die Transparenz zu verbessern.",
                especesConcernees = "Fast ausschließlich Diamant",
                indicesDetection = "Mit der Lupe nicht nachweisbar; erfordert stets eine spezialisierte Laboranalyse (Spektroskopie), da HPHT-Diamanten eine untypische UV-Fluoreszenz oder -Phosphoreszenz zeigen können.",
                obligationDivulgation = "Verpflichtend und in der Regel von den zertifizierenden Labors auf dem Rundist des Steins per Laser eingraviert, aufgrund des erheblichen Wertunterschieds zu einem unbehandelten natürlichen Diamanten."
            ),
            TreatmentEntry(
                nom = "Oberflächenbeschichtung (Coating)",
                description = "Trägt eine dünne metallische oder oxidische Schicht auf die Oberfläche des Steins auf (insbesondere durch physikalische Gasphasenabscheidung), um einen schillernden Farbeffekt oder einen Farbton zu erzeugen, der in der Art natürlich nicht vorkommt.",
                especesConcernees = "Quarz („Aura-Quarz\", „Titan-Quarz\"), Topas („Mystic Topas\"), gelegentlich andere transparente Steine",
                indicesDetection = "Unter der Lupe an den Facettenkanten nach längerem Tragen sichtbare Kratzer oder Abnutzung der Beschichtung, eine Farbe, die auf der Oberfläche zu „schweben\" scheint, statt den Stein zu durchdringen, für die Art ungewöhnliche Irisierung.",
                obligationDivulgation = "Verpflichtend; diese Behandlung ist wenig dauerhaft (die Beschichtung nutzt sich mit der Zeit ab) und muss als solche gekennzeichnet werden, insbesondere bei alltäglichem Schmuckgebrauch."
            ),
            TreatmentEntry(
                nom = "Stabilisierende Imprägnierung (Wachs, Harz, Polymer)",
                description = "Imprägniert einen von Natur aus porösen oder brüchigen Stein mit einer Substanz (Wachs, Harz, Polymer), um dessen Struktur zu verstärken, das Oberflächenbild zu vereinheitlichen und die Formgebung und Politur zu erleichtern.",
                especesConcernees = "Türkis (die große Mehrheit des handelsüblichen Türkises ist stabilisiert), Lapislazuli, poröser Opal",
                indicesDetection = "Wassertropfentest (ein perlender Tropfen bleibt auf der Oberfläche eines stabilisierten Türkises stehen, während er von unbehandeltem, natürlichem Türkis aufgesogen wird), leichte Farbveränderung bei Kontakt mit einem heißen Gegenstand (destruktiver Test, ausschließlich für Fachleute).",
                obligationDivulgation = "Verpflichtend; stabilisierter Türkis muss klar von unbehandeltem, natürlichem Türkis unterschieden werden, der deutlich seltener und teurer ist."
            ),
            TreatmentEntry(
                nom = "Laserbohrung",
                description = "Bohrt mit dem Laser einen feinen Kanal bis zu einem störenden dunklen Einschluss im Inneren eines Diamanten und injiziert dort ein Bleichmittel oder füllt den Kanal, um die scheinbare Reinheit des Steins zu verbessern.",
                especesConcernees = "Fast ausschließlich Diamant",
                indicesDetection = "Bei starker Vergrößerung sichtbarer, feiner, gerader oder leicht gebogener Kanal, der in der Regel an der Oberfläche mündet; der betroffene Einschluss erscheint mitunter aufgehellt oder teilweise aufgelöst.",
                obligationDivulgation = "Verpflichtend und stets auf einem Laborzertifikat vermerkt, mit spürbarem Einfluss auf den Wert im Vergleich zu einem Diamanten von natürlich gleichwertiger Reinheit."
            )
        ),
        disclaimerTitle = "Eine Laborbestätigung bleibt erforderlich",
        disclaimerBody = "Die hier dargestellten Hinweise dienen der Orientierung bei einer Beobachtung mit der Lupe oder dem Mikroskop, ersetzen aber in keinem Fall eine Analyse durch ein akkreditiertes gemmologisches Labor (GIA, Gübelin, GFCO, SSEF, AGL...), das allein befugt ist, eine Behandlung formell zu bestätigen. Die konsequente Offenlegung von Behandlungen gegenüber dem Käufer ist eine deontologische und in vielen Rechtsordnungen auch gesetzliche Pflicht – unabhängig davon, wie sehr die Behandlung vom Markt akzeptiert wird."
    )

    private val pt = TreatmentsPage(
        intro = "A grande maioria das pedras preciosas vendidas no mundo sofreu algum tratamento, muitas vezes estável e amplamente aceite pelo mercado (o tratamento térmico da safira, por exemplo). O que importa do ponto de vista deontológico não é o tratamento em si, mas a sua divulgação sistemática ao comprador. Esta ficha reúne os tratamentos mais comuns, as espécies que mais frequentemente afetam e indícios observáveis à lupa ou ao microscópio — uma confirmação formal continua a ser da competência de um laboratório gemológico acreditado (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Tratamento térmico (aquecimento)",
                description = "Aquece a pedra a alta temperatura (por vezes acima de 1600 °C) para melhorar a sua cor ou pureza, dissolvendo certas inclusões ou alterando o estado de oxidação dos elementos corantes. É o tratamento mais antigo e mais amplamente aceite do mercado quando não implica a adição de matéria.",
                especesConcernees = "Coríndon (safira, rubi), zircão, tanzanite, água-marinha, turmalina, citrino (por aquecimento de ametista)",
                indicesDetection = "Inclusões cristalinas fundidas ou rodeadas por um halo (halo de tensão), discos de tensão em torno de cristais, estrias de crescimento descontínuas; a ausência total de inclusões não exclui um aquecimento.",
                obligationDivulgation = "Obrigatória na maioria das jurisdições e sistematicamente mencionada num certificado de laboratório; amplamente aceite pelo mercado sem uma desvalorização significativa para a safira e o rubi."
            ),
            TreatmentEntry(
                nom = "Impregnação com óleo e resina",
                description = "Preenche as fraturas de superfície com um óleo incolor (tradicionalmente óleo de cedro) ou uma resina sintética, para atenuar a sua visibilidade e melhorar a pureza aparente. Prática antiga e amplamente tolerada na esmeralda, desde que seja divulgada.",
                especesConcernees = "Quase exclusivamente esmeralda; ocasionalmente outras pedras com fraturas de superfície (peridoto, quartzo)",
                indicesDetection = "Brilho gorduroso ou «oleoso» ao longo das fraturas observado à lupa, bolhas ou fluxos visíveis nas fissuras com grande ampliação, fluorescência UV diferente entre as zonas tratadas e a matriz.",
                obligationDivulgation = "Obrigatória; o grau de impregnação (ligeiro, moderado, importante) deve ser indicado num certificado, pois influencia fortemente o valor."
            ),
            TreatmentEntry(
                nom = "Irradiação",
                description = "Expõe a pedra a uma radiação (feixe de eletrões, raios gama, reator nuclear) para alterar a estrutura atómica dos elementos corantes e assim mudar a cor, frequentemente seguida de um aquecimento para estabilizar ou aperfeiçoar o tom obtido.",
                especesConcernees = "Topázio azul (quase sempre a partir de material incolor irradiado), quartzo fumado, diamantes de cor fantasia (azul, verde, preto), turmalina",
                indicesDetection = "Cor de intensidade ou tonalidade invulgar para a espécie natural; a confirmação geralmente requer um laboratório, uma vez que a fonte de irradiação não é visível opticamente.",
                obligationDivulgation = "Obrigatória; alguns países impõem um período de quarentena antes da comercialização para garantir a ausência de radioatividade residual, controlada pelo fornecedor."
            ),
            TreatmentEntry(
                nom = "Difusão",
                description = "Faz penetrar, sob aquecimento prolongado, elementos químicos corantes (titânio, crómio, ou mais recentemente berílio) da superfície da pedra para o interior, produzindo uma coloração superficial (difusão clássica) ou penetrando profundamente em todo o corpo da pedra (difusão de berílio).",
                especesConcernees = "Safira, nomeadamente os tons laranja/rosa tipo «padparadscha» obtidos por difusão de berílio",
                indicesDetection = "Difusão clássica: concentração de cor na periferia visível por imersão, cor mais pálida depois de a pedra ser repolida. Difusão de berílio: indetetável à lupa, requer análise química de laboratório (LA-ICP-MS).",
                obligationDivulgation = "Obrigatória; a difusão de berílio deve ser sempre mencionada separadamente, uma vez que o seu impacto no valor é sensivelmente maior do que o da difusão clássica."
            ),
            TreatmentEntry(
                nom = "Preenchimento de fraturas com vidro de chumbo",
                description = "Preenche as fraturas importantes de um rubi de qualidade medíocre com um vidro rico em chumbo e de baixo ponto de fusão, melhorando fortemente a pureza aparente de uma pedra que, de outro modo, seria invendável no seu estado natural.",
                especesConcernees = "Quase exclusivamente rubi, por vezes safira",
                indicesDetection = "Bolhas de gás presas no vidro de preenchimento, efeito «flash» de cor azulada ou alaranjada consoante o ângulo de observação à lupa, textura de superfície irregular à volta das zonas preenchidas.",
                obligationDivulgation = "Obrigatória e imperativa: estas pedras, por vezes chamadas «rubi composto», devem ser claramente distinguidas de um rubi simplesmente aquecido, sendo o seu valor muito inferior e a sua fragilidade maior (possível ataque por produtos de limpeza domésticos comuns)."
            ),
            TreatmentEntry(
                nom = "Tingimento",
                description = "Aplica um corante (orgânico ou mineral) que penetra na porosidade natural da pedra ou ao longo das suas fraturas, para intensificar ou alterar a sua cor.",
                especesConcernees = "Jade (nomeadamente a jadeíte), ágata, pérola, coral, turquesa porosa, lápis-lazúli de qualidade inferior",
                indicesDetection = "Concentração de cor nas fissuras ou zonas porosas visível à lupa, algodão embebido em acetona que se tinge ao contacto com a pedra (teste destrutivo, reservado a profissionais), cor de uniformidade artificial.",
                obligationDivulgation = "Obrigatória; o jade tingido (por vezes combinado com uma impregnação de resina, categoria dita «tipo C») deve ser claramente distinguido do jade natural não tratado («tipo A»)."
            ),
            TreatmentEntry(
                nom = "Branqueamento",
                description = "Utiliza um agente químico (geralmente à base de cloro ou peróxido) para clarear ou uniformizar a cor natural de uma pedra, frequentemente em preparação para outro tratamento (tingimento, impregnação).",
                especesConcernees = "Jade, pérola cultivada",
                indicesDetection = "Dificilmente detetável visualmente por si só; frequentemente associado a outros tratamentos (tingimento, impregnação) cujos indícios são mais característicos.",
                obligationDivulgation = "Obrigatória, geralmente mencionada em conjunto com o tratamento associado (tingimento ou impregnação)."
            ),
            TreatmentEntry(
                nom = "Tratamento HPHT (alta pressão alta temperatura)",
                description = "Submete a pedra a condições extremas de pressão e temperatura, reproduzindo artificialmente as condições de formação em profundidade, para melhorar a cor (descoloração de diamantes castanhos até incolores, ou produção de cores fantasia) ou a transparência.",
                especesConcernees = "Quase exclusivamente diamante",
                indicesDetection = "Indetetável à lupa; requer sistematicamente uma análise de laboratório especializada (espetroscopia), podendo os diamantes HPHT apresentar fluorescência ou fosforescência UV atípica.",
                obligationDivulgation = "Obrigatória e geralmente gravada a laser no rondel da pedra pelos laboratórios que a certificam, devido à diferença de valor importante face a um diamante natural não tratado."
            ),
            TreatmentEntry(
                nom = "Revestimento de superfície (coating)",
                description = "Deposita uma fina camada metálica ou de óxido na superfície da pedra (nomeadamente por deposição física em fase vapor) para produzir um efeito de cor iridescente ou um tom que não existe naturalmente na espécie.",
                especesConcernees = "Quartzo («quartzo aura», «quartzo titânio»), topázio («topázio místico»), ocasionalmente outras pedras transparentes",
                indicesDetection = "Riscos ou desgaste do revestimento visíveis à lupa nas arestas das facetas após uso prolongado, cor que parece «flutuar» na superfície em vez de impregnar a pedra, irisação invulgar para a espécie.",
                obligationDivulgation = "Obrigatória; este tratamento é pouco durável (o revestimento desgasta-se com o tempo) e deve ser assinalado como tal, em particular para uso quotidiano em joalharia."
            ),
            TreatmentEntry(
                nom = "Impregnação estabilizante (cera, resina, polímero)",
                description = "Impregna uma pedra naturalmente porosa ou friável com uma substância (cera, resina, polímero) para reforçar a sua estrutura, uniformizar o aspeto da superfície e facilitar a sua modelação e polimento.",
                especesConcernees = "Turquesa (a grande maioria da turquesa comercial está estabilizada), lápis-lazúli, opala porosa",
                indicesDetection = "Teste da gota de água (uma gota perlada permanece à superfície numa turquesa estabilizada, enquanto é absorvida por uma turquesa natural não tratada), ligeira mudança de cor ao contacto com um objeto quente (teste destrutivo, reservado a profissionais).",
                obligationDivulgation = "Obrigatória; a turquesa estabilizada deve ser claramente distinguida da turquesa natural não tratada, nitidamente mais rara e dispendiosa."
            ),
            TreatmentEntry(
                nom = "Perfuração a laser",
                description = "Perfura um fino canal a laser até uma inclusão escura incómoda no interior de um diamante, injetando depois um agente branqueador ou preenchendo o canal, para melhorar a pureza aparente da pedra.",
                especesConcernees = "Quase exclusivamente diamante",
                indicesDetection = "Fino canal retilíneo ou ligeiramente curvo visível com grande ampliação, geralmente desembocando à superfície; a inclusão visada surge por vezes branqueada ou parcialmente dissolvida.",
                obligationDivulgation = "Obrigatória e sistematicamente mencionada num certificado de laboratório, com um impacto sensível no valor em comparação com um diamante de pureza naturalmente equivalente."
            )
        ),
        disclaimerTitle = "Continua a ser necessária uma confirmação de laboratório",
        disclaimerBody = "Os indícios aqui apresentados permitem orientar uma observação à lupa ou ao microscópio, mas não substituem, em caso algum, uma análise de um laboratório gemológico acreditado (GIA, Gübelin, GFCO, SSEF, AGL...), o único habilitado a confirmar formalmente um tratamento. A divulgação sistemática dos tratamentos ao comprador é uma obrigação deontológica e, em muitas jurisdições, também legal — independentemente da aceitação do tratamento pelo mercado."
    )

    private val zh = TreatmentsPage(
        intro = "全球销售的宝石绝大多数都经过某种优化处理，且这些处理通常是稳定的，也被市场广泛接受（例如蓝宝石的加热处理）。从职业道德的角度而言，重要的不是处理本身，而是必须系统地向买家披露。本资料汇总了最常见的处理方式、最常受影响的宝石品种，以及可用放大镜或显微镜观察到的鉴定线索——正式确认仍需由权威宝石实验室（GIA、Gübelin、GFCO、SSEF等）出具。",
        traitements = listOf(
            TreatmentEntry(
                nom = "加热处理",
                description = "将宝石加热至高温（有时超过1600°C），通过溶解某些内含物或改变致色元素的氧化状态来改善其颜色或净度。这是最古老、也是市场上接受度最高的处理方式，只要不涉及外来物质的添加。",
                especesConcernees = "刚玉类（蓝宝石、红宝石）、锆石、坦桑石、海蓝宝石、碧玺、黄水晶（由紫水晶加热而成）",
                indicesDetection = "熔融或带有晕圈的晶体内含物（应力晕），晶体周围的应力盘，不连续的生长纹；即使完全没有内含物也不能排除加热处理的可能。",
                obligationDivulgation = "在大多数司法管辖区为强制披露事项，并系统地标注于实验室证书中；市场对蓝宝石和红宝石的加热处理接受度很高，价格不会因此大幅折损。"
            ),
            TreatmentEntry(
                nom = "注油及树脂浸渍处理",
                description = "用无色油（传统上为雪松油）或合成树脂填充延伸至表面的裂隙，以减弱其可见度并提高表观净度。这是一种历史悠久、且在祖母绿中被广泛接受的做法，前提是必须予以披露。",
                especesConcernees = "几乎只见于祖母绿；偶尔用于其他有表面裂隙的宝石（橄榄石、石英）",
                indicesDetection = "放大镜下沿裂隙可见油腻或\"油状\"光泽，高倍放大下裂隙中可见气泡或流动痕迹，处理区域与母体之间紫外荧光反应不同。",
                obligationDivulgation = "强制披露；浸渍程度（轻微、中等、显著）必须在证书中注明，因其对价值影响很大。"
            ),
            TreatmentEntry(
                nom = "辐照处理",
                description = "使宝石暴露于辐射（电子束、伽马射线、核反应堆）中，以改变致色元素的原子结构从而改变颜色，通常随后进行加热以稳定或改善所获得的色调。",
                especesConcernees = "蓝色托帕石（几乎均由无色原石经辐照而成）、烟晶、彩钻（蓝色、绿色、黑色）、碧玺",
                indicesDetection = "颜色的强度或色调对该天然品种而言异常；确认通常需要实验室鉴定，因为辐照来源本身在光学上不可见。",
                obligationDivulgation = "强制披露；部分国家在上市销售前要求一段隔离检测期，以确保不存在残余放射性，由供应商负责监控。"
            ),
            TreatmentEntry(
                nom = "扩散处理",
                description = "在长时间加热下，使致色化学元素（钛、铬，或近年来更常见的铍）从宝石表面向内部渗透，产生表面着色（传统扩散处理）或深入渗透整个宝石体的着色（铍扩散处理）。",
                especesConcernees = "蓝宝石，尤其是通过铍扩散获得的橙/粉色\"类帕帕拉恰\"色调",
                indicesDetection = "传统扩散处理：浸油观察可见颜色集中于边缘，重新抛光后颜色变浅。铍扩散处理：放大镜下无法察觉，需实验室化学分析（激光剥蚀电感耦合等离子体质谱法，LA-ICP-MS）。",
                obligationDivulgation = "强制披露；铍扩散处理必须单独注明，因其对价值的影响明显大于传统扩散处理。"
            ),
            TreatmentEntry(
                nom = "铅玻璃充填处理",
                description = "用低熔点富铅玻璃填充劣质红宝石中的重大裂隙，大幅改善原本因裂隙过多而无法销售的宝石的表观净度。",
                especesConcernees = "几乎只见于红宝石，偶见于蓝宝石",
                indicesDetection = "充填玻璃中可见气泡，放大镜下随观察角度不同呈现蓝色或橙色\"闪光\"效应，填充区域周围表面纹理不规则。",
                obligationDivulgation = "必须强制披露：这类宝石有时被称为\"复合红宝石\"，必须与单纯经加热处理的红宝石明确区分，因其价值远低于后者，且更为脆弱（可能被常见清洁用品腐蚀）。"
            ),
            TreatmentEntry(
                nom = "染色处理",
                description = "施加一种染料（有机或矿物性）渗入宝石天然的孔隙或沿裂隙渗透，以加深或改变其颜色。",
                especesConcernees = "翡翠（尤其是硬玉）、玛瑙、珍珠、珊瑚、多孔绿松石、低品质青金石",
                indicesDetection = "放大镜下可见颜色集中于裂隙或多孔区域，蘸有丙酮的棉签接触宝石后染色（破坏性检测，仅限专业人士使用），颜色呈现不自然的均匀性。",
                obligationDivulgation = "强制披露；染色翡翠（有时结合树脂浸渍处理，即所谓\"C货\"）必须与未经处理的天然翡翠（\"A货\"）明确区分。"
            ),
            TreatmentEntry(
                nom = "漂白处理",
                description = "使用化学剂（通常以氯或过氧化物为基础）淡化或均匀化宝石的天然颜色，常作为另一种处理（染色、浸渍）的前期准备工序。",
                especesConcernees = "翡翠、养殖珍珠",
                indicesDetection = "单独通过肉眼很难察觉；通常与其他处理（染色、浸渍）并存，后者的鉴定特征更为明显。",
                obligationDivulgation = "强制披露，通常与相关处理（染色或浸渍）一并注明。"
            ),
            TreatmentEntry(
                nom = "HPHT处理（高温高压处理）",
                description = "使宝石承受极端的压力和温度条件，人为再现深部形成的环境，以改善颜色（将棕色钻石脱色为无色，或产生彩色钻石）或透明度。",
                especesConcernees = "几乎只见于钻石",
                indicesDetection = "放大镜下无法察觉；系统性地需要专业实验室分析（光谱学），因为经HPHT处理的钻石可能呈现异常的紫外荧光或磷光反应。",
                obligationDivulgation = "强制披露，出证实验室通常会在宝石腰围激光刻字标注，因其与未经处理的天然钻石之间价值差异显著。"
            ),
            TreatmentEntry(
                nom = "表面镀膜处理",
                description = "在宝石表面沉积一层薄薄的金属或氧化物涂层（尤其是通过物理气相沉积法），以产生该品种自然状态下不存在的虹彩色效果或色调。",
                especesConcernees = "石英（\"极光石英\"、\"钛晶\"）、托帕石（\"神秘托帕石\"），偶尔用于其他透明宝石",
                indicesDetection = "长期佩戴后放大镜下可见刻面棱边处涂层出现划痕或磨损，颜色看似\"漂浮\"于表面而非渗入宝石内部，该品种不寻常的虹彩现象。",
                obligationDivulgation = "强制披露；该处理耐久性较差（涂层会随时间磨损），必须如实标注，尤其是用于日常佩戴的首饰。"
            ),
            TreatmentEntry(
                nom = "稳定浸渍处理（蜡、树脂、聚合物）",
                description = "对天然多孔或质地松散的宝石浸渍某种物质（蜡、树脂、聚合物），以加固其结构、使表面外观均匀，并便于成型和抛光。",
                especesConcernees = "绿松石（市面上大多数商用绿松石均经稳定处理）、青金石、多孔蛋白石",
                indicesDetection = "水滴测试（水滴在经稳定处理的绿松石表面呈珠状停留，而在未经处理的天然绿松石上会被吸收），接触热源后颜色发生轻微变化（破坏性检测，仅限专业人士使用）。",
                obligationDivulgation = "强制披露；经稳定处理的绿松石必须与明显更稀有、更昂贵的未经处理天然绿松石明确区分。"
            ),
            TreatmentEntry(
                nom = "激光钻孔处理",
                description = "用激光在钻石内部钻出细小通道，直达影响外观的暗色内含物，随后注入漂白剂或填充该通道，以改善宝石的表观净度。",
                especesConcernees = "几乎只见于钻石",
                indicesDetection = "高倍放大下可见细直或略呈弯曲的通道，通常通至表面；目标内含物有时呈现漂白或部分溶解的外观。",
                obligationDivulgation = "强制披露，并系统性地标注于实验室证书中，与净度天然相当的钻石相比，其价值受到明显影响。"
            )
        ),
        disclaimerTitle = "仍需实验室确认",
        disclaimerBody = "本资料所列鉴定线索可为放大镜或显微镜观察提供参考方向，但在任何情况下都不能替代权威宝石实验室（GIA、Gübelin、GFCO、SSEF、AGL等）出具的分析报告，唯有此类实验室才有资格正式确认某项处理。无论市场对该处理的接受程度如何，向买家系统披露处理情况都是一项职业道德义务，并且在许多司法管辖区也是一项法律义务。"
    )

    private val ru = TreatmentsPage(
        intro = "Подавляющее большинство драгоценных камней, продаваемых в мире, прошли ту или иную обработку, часто стабильную и широко принятую рынком (например, термическая обработка сапфира). С точки зрения профессиональной этики важна не сама обработка, а её систематическое раскрытие покупателю. В этом справочнике перечислены наиболее распространённые виды обработки, виды камней, которые они чаще всего затрагивают, и признаки, наблюдаемые с помощью лупы или микроскопа — формальное подтверждение остаётся прерогативой аккредитованной геммологической лаборатории (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Термическая обработка (нагрев)",
                description = "Нагревание камня до высокой температуры (иногда свыше 1600 °C) для улучшения его цвета или чистоты за счёт растворения некоторых включений или изменения степени окисления красящих элементов. Самый древний и наиболее широко принятый рынком вид обработки, если он не связан с добавлением постороннего вещества.",
                especesConcernees = "Корунд (сапфир, рубин), циркон, танзанит, аквамарин, турмалин, цитрин (путём нагрева аметиста)",
                indicesDetection = "Оплавленные или окружённые ореолом кристаллические включения (напряжённый ореол), диски напряжения вокруг кристаллов, прерывистые полосы роста; полное отсутствие включений не исключает нагрев.",
                obligationDivulgation = "Обязательна к раскрытию в большинстве юрисдикций и систематически указывается в лабораторном сертификате; широко принята рынком без существенного снижения цены для сапфира и рубина."
            ),
            TreatmentEntry(
                nom = "Пропитка маслом и смолой",
                description = "Заполнение выходящих на поверхность трещин бесцветным маслом (традиционно кедровым) или синтетической смолой для снижения их видимости и повышения кажущейся чистоты. Давняя практика, широко допускаемая для изумруда при условии её раскрытия.",
                especesConcernees = "Почти исключительно изумруд; изредка другие камни с поверхностными трещинами (перидот, кварц)",
                indicesDetection = "Жирный или «маслянистый» блеск вдоль трещин под лупой, видимые при сильном увеличении пузырьки или потоки внутри трещин, различная УФ-флуоресценция между обработанными участками и основной массой камня.",
                obligationDivulgation = "Обязательна к раскрытию; степень пропитки (незначительная, умеренная, значительная) должна быть указана в сертификате, поскольку она сильно влияет на стоимость."
            ),
            TreatmentEntry(
                nom = "Облучение",
                description = "Воздействие на камень радиацией (электронный пучок, гамма-лучи, ядерный реактор) для изменения атомной структуры красящих элементов и, соответственно, изменения цвета, часто с последующим нагревом для стабилизации или уточнения полученного оттенка.",
                especesConcernees = "Голубой топаз (почти всегда получен облучением исходно бесцветного материала), дымчатый кварц, фантазийные бриллианты (синие, зелёные, чёрные), турмалин",
                indicesDetection = "Необычная для природного вида интенсивность или оттенок цвета; подтверждение, как правило, требует лаборатории, поскольку сам источник облучения оптически не виден.",
                obligationDivulgation = "Обязательна к раскрытию; в некоторых странах перед продажей требуется период карантина для подтверждения отсутствия остаточной радиоактивности, контролируемый поставщиком."
            ),
            TreatmentEntry(
                nom = "Диффузия",
                description = "При длительном нагреве приводит к проникновению красящих химических элементов (титана, хрома или, в последнее время, бериллия) с поверхности камня внутрь, создавая либо поверхностную окраску (классическая диффузия), либо окраску, глубоко проникающую по всему объёму камня (бериллиевая диффузия).",
                especesConcernees = "Сапфир, в частности оранжево-розовые оттенки типа «падпараджа», получаемые бериллиевой диффузией",
                indicesDetection = "Классическая диффузия: концентрация цвета по периферии, видимая в иммерсии, более бледный цвет после переполировки камня. Бериллиевая диффузия: не выявляется под лупой, требует химического анализа в лаборатории (LA-ICP-MS).",
                obligationDivulgation = "Обязательна к раскрытию; бериллиевая диффузия должна обязательно указываться отдельно, поскольку её влияние на стоимость значительно сильнее, чем у классической диффузии."
            ),
            TreatmentEntry(
                nom = "Заполнение трещин свинцовым стеклом",
                description = "Заполнение крупных трещин рубина низкого качества легкоплавким свинцовым стеклом, что существенно улучшает кажущуюся чистоту камня, который в противном случае был бы непригоден для продажи в исходном состоянии.",
                especesConcernees = "Почти исключительно рубин, иногда сапфир",
                indicesDetection = "Пузырьки газа, захваченные заполняющим стеклом, эффект синеватой или оранжевой «вспышки» в зависимости от угла наблюдения под лупой, неровная текстура поверхности вокруг заполненных участков.",
                obligationDivulgation = "Обязательное и безусловное раскрытие: такие камни, иногда называемые «композитный рубин», должны чётко отличаться от просто нагретого рубина, поскольку их стоимость значительно ниже, а хрупкость выше (возможно повреждение обычными бытовыми чистящими средствами)."
            ),
            TreatmentEntry(
                nom = "Окрашивание",
                description = "Нанесение красителя (органического или минерального), проникающего в естественную пористость камня или вдоль его трещин, для усиления или изменения цвета.",
                especesConcernees = "Жадеит (в частности), агат, жемчуг, коралл, пористая бирюза, лазурит более низкого качества",
                indicesDetection = "Концентрация цвета в трещинах или пористых участках, видимая под лупой, ватный тампон, смоченный ацетоном, окрашивающийся при контакте с камнем (разрушающий тест, только для специалистов), неестественно равномерный цвет.",
                obligationDivulgation = "Обязательна к раскрытию; окрашенный жадеит (иногда сочетаемый с пропиткой смолой, категория «тип С») должен чётко отличаться от необработанного природного жадеита («тип А»)."
            ),
            TreatmentEntry(
                nom = "Отбеливание",
                description = "Использование химического агента (обычно на основе хлора или пероксида) для осветления или выравнивания природного цвета камня, часто в качестве подготовки к другой обработке (окрашиванию, пропитке).",
                especesConcernees = "Жадеит, культивированный жемчуг",
                indicesDetection = "Трудно выявляется визуально само по себе; часто сопровождается другими видами обработки (окрашиванием, пропиткой), признаки которых более характерны.",
                obligationDivulgation = "Обязательна к раскрытию, обычно указывается вместе с сопутствующей обработкой (окрашиванием или пропиткой)."
            ),
            TreatmentEntry(
                nom = "HPHT-обработка (высокое давление и высокая температура)",
                description = "Воздействие на камень экстремальными условиями давления и температуры, искусственно воспроизводящими условия глубинного образования, для улучшения цвета (обесцвечивание коричневых бриллиантов до бесцветных или получение фантазийных цветов) или прозрачности.",
                especesConcernees = "Почти исключительно бриллиант",
                indicesDetection = "Не выявляется под лупой; систематически требует специализированного лабораторного анализа (спектроскопии), поскольку HPHT-бриллианты могут демонстрировать нетипичную УФ-флуоресценцию или фосфоресценцию.",
                obligationDivulgation = "Обязательна к раскрытию и, как правило, лазерно гравируется на рундисте камня сертифицирующими его лабораториями ввиду значительной разницы в стоимости с необработанным природным бриллиантом."
            ),
            TreatmentEntry(
                nom = "Поверхностное покрытие",
                description = "Нанесение тонкого металлического или оксидного слоя на поверхность камня (в частности методом физического осаждения из паровой фазы) для создания эффекта переливчатого цвета или оттенка, не встречающегося в природе у данного вида.",
                especesConcernees = "Кварц («аура-кварц», «титан-кварц»), топаз («мистик-топаз»), изредка другие прозрачные камни",
                indicesDetection = "Видимые под лупой царапины или износ покрытия на рёбрах граней после длительной носки, цвет, который словно «плавает» на поверхности, а не пропитывает камень, необычная для данного вида иризация.",
                obligationDivulgation = "Обязательна к раскрытию; эта обработка недолговечна (покрытие со временем стирается) и должна указываться как таковая, особенно при повседневном ношении украшений."
            ),
            TreatmentEntry(
                nom = "Стабилизирующая пропитка (воск, смола, полимер)",
                description = "Пропитка природно пористого или хрупкого камня веществом (воском, смолой, полимером) для укрепления его структуры, выравнивания вида поверхности и облегчения его обработки и полировки.",
                especesConcernees = "Бирюза (подавляющее большинство коммерческой бирюзы стабилизировано), лазурит, пористый опал",
                indicesDetection = "Тест каплей воды (капля остаётся на поверхности бисеринкой у стабилизированной бирюзы, тогда как впитывается необработанной природной бирюзой), лёгкое изменение цвета при контакте с горячим предметом (разрушающий тест, только для специалистов).",
                obligationDivulgation = "Обязательна к раскрытию; стабилизированная бирюза должна чётко отличаться от необработанной природной бирюзы, значительно более редкой и дорогой."
            ),
            TreatmentEntry(
                nom = "Лазерное сверление",
                description = "Просверливание лазером тонкого канала до мешающего тёмного включения внутри бриллианта с последующим введением отбеливающего агента или заполнением канала для улучшения кажущейся чистоты камня.",
                especesConcernees = "Почти исключительно бриллиант",
                indicesDetection = "Тонкий прямой или слегка изогнутый канал, видимый при сильном увеличении, как правило, выходящий на поверхность; целевое включение иногда выглядит отбеленным или частично растворённым.",
                obligationDivulgation = "Обязательна к раскрытию и систематически указывается в лабораторном сертификате, с заметным влиянием на стоимость по сравнению с бриллиантом природно эквивалентной чистоты."
            )
        ),
        disclaimerTitle = "Подтверждение лаборатории остаётся необходимым",
        disclaimerBody = "Представленные здесь признаки помогают ориентироваться при наблюдении с лупой или микроскопом, но ни в коем случае не заменяют анализ аккредитованной геммологической лаборатории (GIA, Gübelin, GFCO, SSEF, AGL...), единственной уполномоченной формально подтвердить обработку. Систематическое раскрытие обработок покупателю является этическим, а во многих юрисдикциях и юридическим обязательством — независимо от степени принятия обработки рынком."
    )

    private val nl = TreatmentsPage(
        intro = "De overgrote meerderheid van de wereldwijd verkochte edelstenen heeft een behandeling ondergaan, vaak stabiel en algemeen aanvaard door de markt (het verhitten van saffier, bijvoorbeeld). Wat vanuit deontologisch oogpunt telt, is niet de behandeling zelf, maar de systematische bekendmaking ervan aan de koper. Dit overzicht behandelt de meest voorkomende behandelingen, de soorten die er het vaakst mee te maken hebben en aanwijzingen die met een loep of microscoop waarneembaar zijn — een formele bevestiging blijft voorbehouden aan een erkend gemmologisch laboratorium (GIA, Gübelin, GFCO, SSEF...).",
        traitements = listOf(
            TreatmentEntry(
                nom = "Verhitting (thermische behandeling)",
                description = "Verhit de steen tot een hoge temperatuur (soms meer dan 1600 °C) om de kleur of zuiverheid te verbeteren door bepaalde insluitsels op te lossen of de oxidatietoestand van de kleurgevende elementen te wijzigen. De oudste en door de markt meest aanvaarde behandeling, zolang er geen vreemd materiaal wordt toegevoegd.",
                especesConcernees = "Korund (saffier, robijn), zirkoon, tanzaniet, aquamarijn, toermalijn, citrien (door verhitting van amethist)",
                indicesDetection = "Gesmolten of door een halo omgeven kristalinsluitsels (spanningshalo), spanningsschijven rond kristallen, onderbroken groeistrepen; de volledige afwezigheid van insluitsels sluit verhitting niet uit.",
                obligationDivulgation = "Verplicht in de meeste rechtsgebieden en systematisch vermeld op een laboratoriumcertificaat; door de markt breed aanvaard zonder noemenswaardige waardevermindering bij saffier en robijn."
            ),
            TreatmentEntry(
                nom = "Olie- en harsimpregnatie",
                description = "Vult tot aan het oppervlak reikende breuken op met een kleurloze olie (traditioneel cederolie) of een synthetische hars, om de zichtbaarheid ervan te verminderen en de schijnbare zuiverheid te verbeteren. Een oude, bij smaragd algemeen getolereerde praktijk, mits bekendgemaakt.",
                especesConcernees = "Vrijwel uitsluitend smaragd; af en toe andere stenen met breuken aan het oppervlak (peridoot, kwarts)",
                indicesDetection = "Vettige of \"olieachtige\" glans langs de breuken onder de loep, bij sterke vergroting zichtbare bellen of stromingspatronen in de scheuren, verschillende UV-fluorescentie tussen behandelde zones en de matrix.",
                obligationDivulgation = "Verplicht; de mate van impregnatie (gering, matig, aanzienlijk) moet op een certificaat worden vermeld, aangezien dit de waarde sterk beïnvloedt."
            ),
            TreatmentEntry(
                nom = "Bestraling",
                description = "Stelt de steen bloot aan straling (elektronenbundel, gammastraling, kernreactor) om de atoomstructuur van de kleurgevende elementen te wijzigen en zo de kleur te veranderen, vaak gevolgd door verhitting om de verkregen tint te stabiliseren of te verfijnen.",
                especesConcernees = "Blauwe topaas (vrijwel altijd afkomstig van oorspronkelijk kleurloos, bestraald materiaal), rokerige kwarts, fancy-kleur diamanten (blauw, groen, zwart), toermalijn",
                indicesDetection = "Een kleur met een voor de natuurlijke soort ongewone intensiteit of tint; bevestiging vereist doorgaans een laboratorium, aangezien de bestralingsbron zelf optisch niet zichtbaar is.",
                obligationDivulgation = "Verplicht; sommige landen leggen vóór de verkoop een quarantaineperiode op om de afwezigheid van restradioactiviteit te garanderen, gecontroleerd door de leverancier."
            ),
            TreatmentEntry(
                nom = "Diffusie",
                description = "Laat onder langdurige verhitting kleurgevende chemische elementen (titaan, chroom, of meer recentelijk beryllium) van het oppervlak van de steen naar binnen dringen, wat een oppervlakkige kleuring (klassieke diffusie) of een diep in de hele steen doordringende kleuring (berylliumdiffusie) oplevert.",
                especesConcernees = "Saffier, met name de oranje/roze \"padparadscha-achtige\" tinten verkregen door berylliumdiffusie",
                indicesDetection = "Klassieke diffusie: kleurconcentratie aan de rand zichtbaar bij immersie, bleker van kleur na het opnieuw polijsten van de steen. Berylliumdiffusie: niet detecteerbaar met de loep, vereist chemische laboratoriumanalyse (LA-ICP-MS).",
                obligationDivulgation = "Verplicht; berylliumdiffusie moet altijd afzonderlijk worden vermeld, aangezien de invloed ervan op de waarde aanzienlijk groter is dan bij klassieke diffusie."
            ),
            TreatmentEntry(
                nom = "Loodglasvulling van breuken",
                description = "Vult de grote breuken van een robijn van matige kwaliteit op met een loodrijk glas met een laag smeltpunt, wat de schijnbare zuiverheid van een anders in die staat onverkoopbare steen sterk verbetert.",
                especesConcernees = "Vrijwel uitsluitend robijn, soms saffier",
                indicesDetection = "In het vulglas ingesloten gasbelletjes, een blauwachtig of oranje \"flits\"-effect afhankelijk van de kijkhoek onder de loep, onregelmatige oppervlaktetextuur rond de gevulde zones.",
                obligationDivulgation = "Verplicht en onontbeerlijk: deze stenen, soms \"composietrobijn\" genoemd, moeten duidelijk worden onderscheiden van een louter verhitte robijn, aangezien hun waarde veel lager is en hun kwetsbaarheid groter (mogelijke aantasting door gangbare reinigingsmiddelen)."
            ),
            TreatmentEntry(
                nom = "Verkleuring (dyeing)",
                description = "Brengt een kleurstof aan (organisch of mineraal) die doordringt in de natuurlijke poreusheid van de steen of langs de breuken, om de kleur te versterken of te veranderen.",
                especesConcernees = "Jade (met name jadeïet), agaat, parel, koraal, poreuze turkoois, lagere kwaliteit lapis lazuli",
                indicesDetection = "Kleurconcentratie in scheuren of poreuze zones zichtbaar onder de loep, een met aceton doordrenkt wattenstaafje dat verkleurt bij contact met de steen (destructieve test, uitsluitend voor vakmensen), een kunstmatig uniforme kleur.",
                obligationDivulgation = "Verplicht; geverfde jade (soms gecombineerd met harsimpregnatie, de zogenaamde \"type C\"-categorie) moet duidelijk worden onderscheiden van onbehandelde natuurlijke jade (\"type A\")."
            ),
            TreatmentEntry(
                nom = "Bleken",
                description = "Gebruikt een chemisch middel (doorgaans op basis van chloor of peroxide) om de natuurlijke kleur van een steen lichter of gelijkmatiger te maken, vaak als voorbereiding op een andere behandeling (verkleuring, impregnatie).",
                especesConcernees = "Jade, gekweekte parel",
                indicesDetection = "Op zichzelf visueel moeilijk te detecteren; vaak gecombineerd met andere behandelingen (verkleuring, impregnatie) waarvan de aanwijzingen kenmerkender zijn.",
                obligationDivulgation = "Verplicht, doorgaans vermeld samen met de bijbehorende behandeling (verkleuring of impregnatie)."
            ),
            TreatmentEntry(
                nom = "HPHT-behandeling (hoge druk, hoge temperatuur)",
                description = "Onderwerpt de steen aan extreme druk- en temperatuurcondities die de omstandigheden van diepe vorming kunstmatig nabootsen, om de kleur (ontkleuring van bruine diamanten tot kleurloos, of het produceren van fancy kleuren) of de transparantie te verbeteren.",
                especesConcernees = "Vrijwel uitsluitend diamant",
                indicesDetection = "Niet detecteerbaar met de loep; vereist systematisch een gespecialiseerde laboratoriumanalyse (spectroscopie), aangezien HPHT-diamanten een atypische UV-fluorescentie of -fosforescentie kunnen vertonen.",
                obligationDivulgation = "Verplicht en doorgaans lasergegraveerd op de rondist van de steen door de certificerende laboratoria, vanwege het aanzienlijke waardeverschil met een onbehandelde natuurlijke diamant."
            ),
            TreatmentEntry(
                nom = "Oppervlaktecoating",
                description = "Brengt een dunne metalen of oxidelaag aan op het oppervlak van de steen (met name via fysische dampfasedepositie) om een iriserend kleureffect of een tint te produceren die van nature niet in de soort voorkomt.",
                especesConcernees = "Kwarts (\"aura-kwarts\", \"titaankwarts\"), topaas (\"mystic topaz\"), af en toe andere transparante stenen",
                indicesDetection = "Onder de loep zichtbare krassen of slijtage van de coating op de facetranden na langdurig dragen, kleur die op het oppervlak lijkt te \"zweven\" in plaats van de steen te doordringen, voor de soort ongebruikelijke irisatie.",
                obligationDivulgation = "Verplicht; deze behandeling is weinig duurzaam (de coating slijt na verloop van tijd) en moet als zodanig worden vermeld, met name bij dagelijks gedragen sieraden."
            ),
            TreatmentEntry(
                nom = "Stabiliserende impregnatie (was, hars, polymeer)",
                description = "Impregneert een van nature poreuze of brokkelige steen met een stof (was, hars, polymeer) om de structuur te versterken, het oppervlaktebeeld gelijkmatiger te maken en het vormen en polijsten te vergemakkelijken.",
                especesConcernees = "Turkoois (het overgrote deel van commerciële turkoois is gestabiliseerd), lapis lazuli, poreuze opaal",
                indicesDetection = "Waterdruppeltest (een parelende druppel blijft op het oppervlak van gestabiliseerde turkoois liggen, terwijl deze wordt opgenomen door onbehandelde natuurlijke turkoois), lichte kleurverandering bij contact met een heet voorwerp (destructieve test, uitsluitend voor vakmensen).",
                obligationDivulgation = "Verplicht; gestabiliseerde turkoois moet duidelijk worden onderscheiden van onbehandelde natuurlijke turkoois, die aanzienlijk zeldzamer en duurder is."
            ),
            TreatmentEntry(
                nom = "Laserboren",
                description = "Boort met een laser een fijn kanaal tot aan een storend donker insluitsel binnenin een diamant, en injecteert daar vervolgens een bleekmiddel of vult het kanaal, om de schijnbare zuiverheid van de steen te verbeteren.",
                especesConcernees = "Vrijwel uitsluitend diamant",
                indicesDetection = "Een fijn recht of licht gebogen kanaal, zichtbaar bij sterke vergroting, dat gewoonlijk aan het oppervlak uitmondt; het beoogde insluitsel lijkt soms gebleekt of gedeeltelijk opgelost.",
                obligationDivulgation = "Verplicht en systematisch vermeld op een laboratoriumcertificaat, met een merkbare invloed op de waarde in vergelijking met een diamant van van nature gelijkwaardige zuiverheid."
            )
        ),
        disclaimerTitle = "Een laboratoriumbevestiging blijft noodzakelijk",
        disclaimerBody = "De hier gepresenteerde aanwijzingen bieden houvast bij een waarneming met de loep of microscoop, maar vervangen in geen geval een analyse door een erkend gemmologisch laboratorium (GIA, Gübelin, GFCO, SSEF, AGL...), dat als enige bevoegd is om een behandeling formeel te bevestigen. Het systematisch bekendmaken van behandelingen aan de koper is een deontologische en, in veel rechtsgebieden, ook een wettelijke verplichting — ongeacht de mate waarin de behandeling door de markt wordt aanvaard."
    )

    private val byLanguage: Map<String, TreatmentsPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): TreatmentsPage = byLanguage[languageCode] ?: fr
}
