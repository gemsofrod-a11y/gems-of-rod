package fr.gemsofrod.encyclopedie.data

data class MeteoriteFamilyExplainer(
    val nom: String,
    val sousTypes: String,
    val description: String
)

data class MeteoriteClassificationPage(
    val intro: String,
    val familles: List<MeteoriteFamilyExplainer>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant la classification générale des
 * météorites (sidérites, sidérolithes, aérolithes) et leurs principales
 * sous-catégories, traduit dans les 5 langues de l'app indépendamment des
 * fiches individuelles. Complète la section "Météorites" en expliquant le
 * vocabulaire utilisé sur chaque fiche (classification, composition...).
 */
object MeteoriteClassificationInfo {
    private val fr = MeteoriteClassificationPage(
        intro = "On distingue les météorites d'abord par leur composition : métal, roche, ou un mélange des deux. Cette classification détermine directement leur intérêt pour un lapidaire — les météorites de fer et les pallasites se taillent et se polissent, tandis que la plupart des météorites pierreuses restent des pièces de collection scientifique.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Sidérites (météorites de fer)",
                sousTypes = "Classées par structure (hexaédrites, octaédrites, ataxites) et par groupe chimique (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Composées presque entièrement d'un alliage de fer et de nickel, elles proviennent du noyau métallique d'astéroïdes différenciés, fragmentés lors de collisions. Une attaque à l'acide nitrique sur une tranche polie révèle souvent les figures de Widmanstätten, un maillage géométrique caractéristique de leur lent refroidissement, parfois sur des millions d'années. Ce sont les météorites les plus travaillées en bijouterie et en coutellerie d'art."
            ),
            MeteoriteFamilyExplainer(
                nom = "Sidérolithes (météorites mixtes)",
                sousTypes = "Pallasites (olivine dans une matrice de fer) et mésosidérites (silicates et métal en mélange bréchique)",
                description = "Ces météorites mêlent à parts sensiblement égales métal et silicates. Les pallasites, les plus recherchées, montrent des cristaux d'olivine — souvent de qualité gemme, couleur péridot — enchâssés dans une matrice de fer-nickel : tranchées et polies, elles comptent parmi les plus belles pièces de la lapidairerie météoritique. Les mésosidérites, plus chaotiques dans leur texture, sont surtout des pièces de collection."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aérolithes (météorites pierreuses) — Chondrites",
                sousTypes = "Chondrites carbonées (CI, CM, CO, CV, CK, CR, CH, CB), chondrites ordinaires (H, L, LL), chondrites à enstatite (EH, EL)",
                description = "Les plus courantes des météorites, elles doivent leur nom aux chondres, de minuscules sphérules de silicates formées dès les tout premiers instants du système solaire, avant même la formation des planètes. Les chondrites carbonées, riches en composés organiques, sont les plus étudiées scientifiquement ; les chondrites ordinaires, plus abondantes, se taillent parfois en tranches économiques, bien que plus fragiles que le fer ou la pallasite."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aérolithes (météorites pierreuses) — Achondrites",
                sousTypes = "HED (Vesta), aubrites, urélites, angrites, acapulcoïtes, lodranites, winonaïtes, et les rarissimes météorites martiennes et lunaires",
                description = "Contrairement aux chondrites, les achondrites proviennent de corps parents suffisamment grands pour avoir fondu et différencié leur intérieur, comme la croûte de l'astéroïde Vesta (groupe HED) — ou même celle de Mars et de la Lune. Ces dernières, éjectées par des impacts puis capturées par la gravité terrestre après un long voyage interplanétaire, comptent parmi les matériaux les plus rares et les plus chers de la planète."
            )
        ),
        disclaimerTitle = "Authenticité et provenance",
        disclaimerBody = "Le marché des météorites attire, comme celui des pierres précieuses, un nombre croissant d'imitations et de \"meteor-wrongs\" (roches terrestres prises pour des météorites). Pour un achat destiné à la taille ou à la collection, exigez toujours une provenance documentée et, idéalement, une classification publiée dans le Meteoritical Bulletin ou établie par un membre de l'IMCA (International Meteorite Collectors Association). Les prix indiqués dans cette section sont purement indicatifs : ils varient énormément selon la taille du fragment, la qualité de préparation et l'état du marché."
    )

    private val en = MeteoriteClassificationPage(
        intro = "Meteorites are first classified by composition: metal, rock, or a mix of both. This classification directly determines their interest to a lapidary — iron meteorites and pallasites can be cut and polished, while most stony meteorites remain scientific collector's pieces.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Irons",
                sousTypes = "Classified by structure (hexahedrites, octahedrites, ataxites) and by chemical group (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Made almost entirely of an iron-nickel alloy, they come from the metallic core of differentiated asteroids, fragmented by collisions. A nitric acid etch on a polished slice often reveals the Widmanstätten pattern, a geometric mesh characteristic of their slow cooling, sometimes over millions of years. These are the most widely worked meteorites in jewellery and art knife-making."
            ),
            MeteoriteFamilyExplainer(
                nom = "Stony-irons",
                sousTypes = "Pallasites (olivine in an iron matrix) and mesosiderites (silicates and metal in a brecciated mix)",
                description = "These meteorites mix metal and silicates in roughly equal parts. Pallasites, the most sought-after, show olivine crystals — often of gem quality, peridot-coloured — set in an iron-nickel matrix: sliced and polished, they rank among the finest pieces in meteorite lapidary work. Mesosiderites, more chaotic in texture, are mostly collector's items."
            ),
            MeteoriteFamilyExplainer(
                nom = "Stones — Chondrites",
                sousTypes = "Carbonaceous chondrites (CI, CM, CO, CV, CK, CR, CH, CB), ordinary chondrites (H, L, LL), enstatite chondrites (EH, EL)",
                description = "The most common meteorites, named after chondrules, tiny silicate spherules formed in the very first moments of the solar system, before the planets even existed. Carbonaceous chondrites, rich in organic compounds, are the most scientifically studied; ordinary chondrites, more abundant, are sometimes cut into affordable slices, though more fragile than iron or pallasite."
            ),
            MeteoriteFamilyExplainer(
                nom = "Stones — Achondrites",
                sousTypes = "HED (Vesta), aubrites, ureilites, angrites, acapulcoites, lodranites, winonaites, and the extremely rare Martian and Lunar meteorites",
                description = "Unlike chondrites, achondrites come from parent bodies large enough to have melted and differentiated internally, such as the crust of asteroid Vesta (HED group) — or even that of Mars and the Moon. The latter, ejected by impacts and later captured by Earth's gravity after a long interplanetary journey, rank among the rarest and most expensive materials on the planet."
            )
        ),
        disclaimerTitle = "Authenticity and provenance",
        disclaimerBody = "Like the gemstone market, the meteorite market attracts a growing number of fakes and \"meteor-wrongs\" (Earth rocks mistaken for meteorites). For a purchase intended for cutting or collecting, always require documented provenance and, ideally, a classification published in the Meteoritical Bulletin or established by an IMCA (International Meteorite Collectors Association) member. Prices given in this section are purely indicative: they vary enormously with fragment size, preparation quality and market conditions."
    )

    private val es = MeteoriteClassificationPage(
        intro = "Los meteoritos se clasifican ante todo por su composición: metal, roca, o una mezcla de ambos. Esta clasificación determina directamente su interés para un lapidario: los meteoritos de hierro y las palasitas se pueden cortar y pulir, mientras que la mayoría de los meteoritos pétreos siguen siendo piezas de colección científica.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Sideritos (meteoritos de hierro)",
                sousTypes = "Clasificados por estructura (hexaedritas, octaedritas, ataxitas) y por grupo químico (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Compuestos casi enteramente por una aleación de hierro y níquel, proceden del núcleo metálico de asteroides diferenciados, fragmentados por colisiones. Un grabado con ácido nítrico sobre una lámina pulida suele revelar las figuras de Widmanstätten, un entramado geométrico característico de su lento enfriamiento, a veces a lo largo de millones de años. Son los meteoritos más trabajados en joyería y cuchillería de arte."
            ),
            MeteoriteFamilyExplainer(
                nom = "Siderolitos (meteoritos mixtos)",
                sousTypes = "Palasitas (olivino en una matriz de hierro) y mesosideritos (silicatos y metal en una mezcla brechada)",
                description = "Estos meteoritos mezclan metal y silicatos en proporciones bastante similares. Las palasitas, las más codiciadas, muestran cristales de olivino —a menudo de calidad gema, color peridoto— engastados en una matriz de hierro-níquel: cortadas y pulidas, se cuentan entre las piezas más bellas de la lapidaria meteorítica. Los mesosideritos, de textura más caótica, son sobre todo piezas de colección."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerolitos (meteoritos pétreos) — Condritas",
                sousTypes = "Condritas carbonáceas (CI, CM, CO, CV, CK, CR, CH, CB), condritas ordinarias (H, L, LL), condritas de enstatita (EH, EL)",
                description = "Los meteoritos más comunes, deben su nombre a los cóndrulos, diminutas esférulas de silicatos formadas en los primerísimos instantes del sistema solar, incluso antes de que existieran los planetas. Las condritas carbonáceas, ricas en compuestos orgánicos, son las más estudiadas científicamente; las condritas ordinarias, más abundantes, a veces se cortan en láminas asequibles, aunque más frágiles que el hierro o la palasita."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerolitos (meteoritos pétreos) — Acondritas",
                sousTypes = "HED (Vesta), aubritas, ureilitas, angritas, acapulcoítas, lodranitas, winonaítas, y los rarísimos meteoritos marcianos y lunares",
                description = "A diferencia de las condritas, las acondritas proceden de cuerpos parentales lo bastante grandes como para haberse fundido y diferenciado internamente, como la corteza del asteroide Vesta (grupo HED) — o incluso la de Marte y la Luna. Estos últimos, expulsados por impactos y capturados después por la gravedad terrestre tras un largo viaje interplanetario, se cuentan entre los materiales más raros y caros del planeta."
            )
        ),
        disclaimerTitle = "Autenticidad y procedencia",
        disclaimerBody = "Al igual que el mercado de piedras preciosas, el mercado de meteoritos atrae un número creciente de imitaciones y \"meteor-wrongs\" (rocas terrestres confundidas con meteoritos). Para una compra destinada al corte o a la colección, exija siempre una procedencia documentada y, idealmente, una clasificación publicada en el Meteoritical Bulletin o establecida por un miembro de la IMCA (International Meteorite Collectors Association). Los precios indicados en esta sección son puramente orientativos: varían enormemente según el tamaño del fragmento, la calidad de preparación y el estado del mercado."
    )

    private val it = MeteoriteClassificationPage(
        intro = "I meteoriti si classificano innanzitutto per composizione: metallo, roccia, o un misto dei due. Questa classificazione determina direttamente il loro interesse per un lapidario: i meteoriti di ferro e le pallasiti si tagliano e si lucidano, mentre la maggior parte dei meteoriti pietrosi resta pezzi da collezione scientifica.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Siderite (meteoriti di ferro)",
                sousTypes = "Classificate per struttura (esaedriti, ottaedriti, atassiti) e per gruppo chimico (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Composte quasi interamente da una lega di ferro e nichel, provengono dal nucleo metallico di asteroidi differenziati, frammentati da collisioni. Un'incisione con acido nitrico su una fetta lucidata rivela spesso le figure di Widmanstätten, un reticolo geometrico caratteristico del loro lento raffreddamento, talvolta durato milioni di anni. Sono i meteoriti più lavorati in gioielleria e coltelleria d'arte."
            ),
            MeteoriteFamilyExplainer(
                nom = "Sideroliti (meteoriti misti)",
                sousTypes = "Pallasiti (olivina in una matrice di ferro) e mesosideriti (silicati e metallo in una miscela brecciata)",
                description = "Questi meteoriti mescolano metallo e silicati in proporzioni pressoché uguali. Le pallasiti, le più ricercate, mostrano cristalli di olivina — spesso di qualità gemma, color peridoto — incastonati in una matrice di ferro-nichel: tagliate e lucidate, sono tra i pezzi più belli della lapidaria meteoritica. Le mesosideriti, dalla tessitura più caotica, sono soprattutto pezzi da collezione."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aeroliti (meteoriti pietrosi) — Condriti",
                sousTypes = "Condriti carbonacee (CI, CM, CO, CV, CK, CR, CH, CB), condriti ordinarie (H, L, LL), condriti a enstatite (EH, EL)",
                description = "I meteoriti più comuni, devono il nome ai condri, minuscole sferule di silicati formatesi nei primissimi istanti del sistema solare, ancor prima della formazione dei pianeti. Le condriti carbonacee, ricche di composti organici, sono le più studiate scientificamente; le condriti ordinarie, più abbondanti, vengono talvolta tagliate in fette economiche, sebbene più fragili del ferro o della pallasite."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aeroliti (meteoriti pietrosi) — Acondriti",
                sousTypes = "HED (Vesta), aubriti, ureiliti, angriti, acapulcoiti, lodraniti, winonaiti, e i rarissimi meteoriti marziani e lunari",
                description = "A differenza delle condriti, le acondriti provengono da corpi progenitori abbastanza grandi da essersi fusi e differenziati internamente, come la crosta dell'asteroide Vesta (gruppo HED) — o persino quella di Marte e della Luna. Questi ultimi, espulsi da impatti e poi catturati dalla gravità terrestre dopo un lungo viaggio interplanetario, sono tra i materiali più rari e costosi del pianeta."
            )
        ),
        disclaimerTitle = "Autenticità e provenienza",
        disclaimerBody = "Come il mercato delle pietre preziose, anche quello dei meteoriti attira un numero crescente di imitazioni e \"meteor-wrongs\" (rocce terrestri scambiate per meteoriti). Per un acquisto destinato al taglio o alla collezione, esigete sempre una provenienza documentata e, idealmente, una classificazione pubblicata nel Meteoritical Bulletin o stabilita da un membro dell'IMCA (International Meteorite Collectors Association). I prezzi indicati in questa sezione sono puramente indicativi: variano enormemente in base alla dimensione del frammento, alla qualità della preparazione e allo stato del mercato."
    )

    private val de = MeteoriteClassificationPage(
        intro = "Meteorite werden zunächst nach ihrer Zusammensetzung klassifiziert: Metall, Gestein oder eine Mischung aus beidem. Diese Klassifikation bestimmt unmittelbar ihr Interesse für einen Lapidar — Eisenmeteorite und Pallasite lassen sich schneiden und polieren, während die meisten Steinmeteorite wissenschaftliche Sammlerstücke bleiben.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Siderite (Eisenmeteorite)",
                sousTypes = "Klassifiziert nach Struktur (Hexaedrite, Oktaedrite, Ataxite) und nach chemischer Gruppe (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Sie bestehen fast ausschließlich aus einer Eisen-Nickel-Legierung und stammen aus dem metallischen Kern differenzierter Asteroiden, die bei Kollisionen zerbrochen sind. Eine Salpetersäure-Ätzung auf einer polierten Scheibe offenbart oft die Widmanstätten-Figuren, ein geometrisches Muster, das für ihre langsame, mitunter über Millionen Jahre dauernde Abkühlung charakteristisch ist. Es sind die am häufigsten in Schmuck und Kunstmesserbau verarbeiteten Meteorite."
            ),
            MeteoriteFamilyExplainer(
                nom = "Siderolithe (Stein-Eisen-Meteorite)",
                sousTypes = "Pallasite (Olivin in einer Eisenmatrix) und Mesosiderite (Silikate und Metall in einer brekziösen Mischung)",
                description = "Diese Meteorite mischen Metall und Silikate zu annähernd gleichen Teilen. Pallasite, die begehrtesten, zeigen Olivinkristalle — oft in Schmuckqualität, peridotfarben — eingebettet in eine Eisen-Nickel-Matrix: geschnitten und poliert zählen sie zu den schönsten Stücken der meteoritischen Lapidarkunst. Mesosiderite mit ihrer chaotischeren Textur sind vor allem Sammlerstücke."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerolithe (Steinmeteorite) — Chondrite",
                sousTypes = "Kohlige Chondrite (CI, CM, CO, CV, CK, CR, CH, CB), gewöhnliche Chondrite (H, L, LL), Enstatit-Chondrite (EH, EL)",
                description = "Die häufigsten Meteorite verdanken ihren Namen den Chondren, winzigen Silikatkügelchen, die in den allerersten Momenten des Sonnensystems entstanden sind, noch bevor es die Planeten gab. Kohlige Chondrite, reich an organischen Verbindungen, sind wissenschaftlich am intensivsten untersucht; gewöhnliche Chondrite, häufiger vorkommend, werden manchmal zu günstigen Scheiben geschnitten, sind aber zerbrechlicher als Eisen oder Pallasit."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerolithe (Steinmeteorite) — Achondrite",
                sousTypes = "HED (Vesta), Aubrite, Ureilite, Angrite, Acapulcoite, Lodranite, Winonaite sowie die extrem seltenen Mars- und Mondmeteorite",
                description = "Anders als Chondrite stammen Achondrite von Mutterkörpern, die groß genug waren, um im Inneren zu schmelzen und sich zu differenzieren, wie die Kruste des Asteroiden Vesta (HED-Gruppe) — oder sogar die von Mars und Mond. Letztere, durch Einschläge ausgeworfen und nach einer langen interplanetaren Reise von der Erdanziehung eingefangen, zählen zu den seltensten und teuersten Materialien des Planeten."
            )
        ),
        disclaimerTitle = "Echtheit und Herkunft",
        disclaimerBody = "Wie der Edelsteinmarkt zieht auch der Meteoritenmarkt eine wachsende Zahl von Fälschungen und \"Meteor-wrongs\" (irdische Gesteine, die für Meteorite gehalten werden) an. Verlangen Sie bei einem für Schnitt oder Sammlung bestimmten Kauf stets eine dokumentierte Herkunft und idealerweise eine im Meteoritical Bulletin veröffentlichte oder von einem IMCA-Mitglied (International Meteorite Collectors Association) erstellte Klassifikation. Die in diesem Abschnitt angegebenen Preise sind rein indikativ: Sie schwanken enorm je nach Fragmentgröße, Aufbereitungsqualität und Marktlage."
    )

    private val byLanguage: Map<String, MeteoriteClassificationPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de
    )

    fun page(languageCode: String): MeteoriteClassificationPage = byLanguage[languageCode] ?: fr
}
