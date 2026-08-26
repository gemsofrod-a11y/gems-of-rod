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

    private val pt = MeteoriteClassificationPage(
        intro = "Os meteoritos classificam-se antes de mais pela sua composição: metal, rocha, ou uma mistura de ambos. Esta classificação determina diretamente o seu interesse para um lapidário — os meteoritos de ferro e as palasites podem ser cortados e polidos, enquanto a maioria dos meteoritos pedregosos permanece como peças de coleção científica.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Sideritos (meteoritos de ferro)",
                sousTypes = "Classificados por estrutura (hexaedritos, octaedritos, ataxitos) e por grupo químico (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Compostos quase inteiramente por uma liga de ferro e níquel, provêm do núcleo metálico de asteroides diferenciados, fragmentados por colisões. Um ataque com ácido nítrico numa fatia polida revela muitas vezes o padrão de Widmanstätten, uma malha geométrica característica do seu lento arrefecimento, por vezes ao longo de milhões de anos. São os meteoritos mais trabalhados em joalharia e em coutelaria de arte."
            ),
            MeteoriteFamilyExplainer(
                nom = "Siderólitos (meteoritos mistos)",
                sousTypes = "Palasites (olivina numa matriz de ferro) e mesosideritos (silicatos e metal numa mistura brechificada)",
                description = "Estes meteoritos misturam metal e silicatos em proporções sensivelmente iguais. As palasites, as mais procuradas, apresentam cristais de olivina — frequentemente de qualidade gema, de cor peridoto — engastados numa matriz de ferro-níquel: cortadas e polidas, contam-se entre as mais belas peças da lapidação meteorítica. Os mesosideritos, de textura mais caótica, são sobretudo peças de coleção."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerólitos (meteoritos pedregosos) — Condritos",
                sousTypes = "Condritos carbonáceos (CI, CM, CO, CV, CK, CR, CH, CB), condritos ordinários (H, L, LL), condritos de enstatite (EH, EL)",
                description = "Os meteoritos mais comuns, devem o seu nome aos côndrulos, minúsculas esférulas de silicatos formadas nos primeiríssimos instantes do sistema solar, ainda antes da formação dos planetas. Os condritos carbonáceos, ricos em compostos orgânicos, são os mais estudados cientificamente; os condritos ordinários, mais abundantes, são por vezes cortados em fatias económicas, embora mais frágeis do que o ferro ou a palasite."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aerólitos (meteoritos pedregosos) — Acondritos",
                sousTypes = "HED (Vesta), aubritos, ureilitos, angritos, acapulcoítos, lodranitos, winonaítos, e os raríssimos meteoritos marcianos e lunares",
                description = "Ao contrário dos condritos, os acondritos provêm de corpos-mãe suficientemente grandes para terem fundido e se terem diferenciado internamente, como a crosta do asteroide Vesta (grupo HED) — ou mesmo a de Marte e da Lua. Estes últimos, ejetados por impactos e depois capturados pela gravidade terrestre após uma longa viagem interplanetária, contam-se entre os materiais mais raros e mais caros do planeta."
            )
        ),
        disclaimerTitle = "Autenticidade e proveniência",
        disclaimerBody = "Tal como o mercado das pedras preciosas, o mercado dos meteoritos atrai um número crescente de imitações e de «meteor-wrongs» (rochas terrestres confundidas com meteoritos). Para uma compra destinada ao corte ou à coleção, exija sempre uma proveniência documentada e, idealmente, uma classificação publicada no Meteoritical Bulletin ou estabelecida por um membro da IMCA (International Meteorite Collectors Association). Os preços indicados nesta secção são meramente indicativos: variam enormemente consoante o tamanho do fragmento, a qualidade da preparação e o estado do mercado."
    )

    private val zh = MeteoriteClassificationPage(
        intro = "陨石首先按其成分分类:金属、岩石,或两者的混合体。这一分类直接决定了它们对雕琢工艺的意义——铁陨石和橄榄陨铁可以切割和抛光,而大多数石陨石仍属于科学收藏品。",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "陨铁(铁陨石)",
                sousTypes = "按结构分类(六面体陨铁、八面体陨铁、无纹陨铁)及按化学族分类(IAB、IIAB、IIIAB、IVA、IVB等)",
                description = "陨铁几乎完全由铁镍合金组成,来自经历分异作用的小行星金属核心,在碰撞中破碎而成。在抛光切片上以硝酸蚀刻,常可显现出魏德曼斯坦花纹——一种因其缓慢冷却(有时历时数百万年)而形成的特征性几何网状结构。这是珠宝制作和艺术刀具制造中加工最广泛的陨石类型。"
            ),
            MeteoriteFamilyExplainer(
                nom = "陨铁石(混合型陨石)",
                sousTypes = "橄榄陨铁(铁质基质中的橄榄石)与中铁陨石(角砾状混合的硅酸盐与金属)",
                description = "这类陨石中金属与硅酸盐的比例大致相当。其中最受追捧的橄榄陨铁,其橄榄石晶体——常达宝石级品质,呈橄榄石绿色——镶嵌于铁镍基质之中:经切割抛光后,是陨石雕琢工艺中最精美的作品之一。而中铁陨石质地更为杂乱,主要作为收藏标本存在。"
            ),
            MeteoriteFamilyExplainer(
                nom = "石陨石——球粒陨石",
                sousTypes = "碳质球粒陨石(CI、CM、CO、CV、CK、CR、CH、CB)、普通球粒陨石(H、L、LL)、顽火辉石球粒陨石(EH、EL)",
                description = "球粒陨石是最常见的陨石类型,其名称源自球粒——一种在太阳系形成之初、甚至早于行星形成之前便已产生的微小硅酸盐球体。碳质球粒陨石富含有机化合物,是科学研究最深入的类型;更为常见的普通球粒陨石有时被切割成价格亲民的薄片出售,不过比铁陨石或橄榄陨铁更为脆弱。"
            ),
            MeteoriteFamilyExplainer(
                nom = "石陨石——无球粒陨石",
                sousTypes = "HED族(源自灶神星)、顽火辉石无球粒陨石、稀有元素无球粒陨石、钙长辉长无球粒陨石、顶点无球粒陨石、洛德兰无球粒陨石、温诺纳无球粒陨石,以及极为罕见的火星陨石与月球陨石",
                description = "与球粒陨石不同,无球粒陨石来自足够大、内部曾发生熔融和分异的母体,例如灶神星小行星的地壳(HED族)——甚至火星和月球的地壳。后者因撞击被抛入太空,经过漫长的星际旅程后被地球引力捕获,是地球上最稀有、最昂贵的材料之一。"
            )
        ),
        disclaimerTitle = "真实性与来源",
        disclaimerBody = "如同宝石市场一样,陨石市场也吸引着越来越多的仿制品与“疑似陨石”(即被误认为陨石的地球岩石)。若购买用途是雕琢或收藏,务必要求提供有据可查的来源证明,并最好附有已发表于《陨石学通报》(Meteoritical Bulletin)或由国际陨石收藏家协会(IMCA)会员认定的分类鉴定。本节所列价格仅供参考:实际价格会因碎片大小、加工品质及市场状况而有极大差异。"
    )

    private val ru = MeteoriteClassificationPage(
        intro = "Метеориты классифицируют прежде всего по составу: металл, камень или смесь того и другого. Эта классификация напрямую определяет их интерес для лапидара — железные метеориты и палласиты можно резать и полировать, тогда как большинство каменных метеоритов остаются научными коллекционными образцами.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Сидериты (железные метеориты)",
                sousTypes = "Классифицируются по структуре (гексаэдриты, октаэдриты, атакситы) и по химической группе (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Состоящие почти целиком из железо-никелевого сплава, они происходят из металлического ядра дифференцированных астероидов, раздробленных при столкновениях. Травление азотной кислотой полированного среза часто выявляет видманштеттенов узор — геометрическую сетку, характерную для их медленного охлаждения, иногда продолжавшегося миллионы лет. Это наиболее широко обрабатываемые метеориты в ювелирном деле и художественном ножевом производстве."
            ),
            MeteoriteFamilyExplainer(
                nom = "Сидеролиты (смешанные метеориты)",
                sousTypes = "Палласиты (оливин в железной матрице) и мезосидериты (силикаты и металл в брекчиевидной смеси)",
                description = "Эти метеориты смешивают металл и силикаты примерно в равных долях. Палласиты, наиболее востребованные из них, содержат кристаллы оливина — часто ювелирного качества, цвета перидота, — заключённые в железо-никелевую матрицу: распиленные и отполированные, они входят в число красивейших образцов метеоритной обработки камня. Мезосидериты с их более хаотичной текстурой являются в основном коллекционными предметами."
            ),
            MeteoriteFamilyExplainer(
                nom = "Аэролиты (каменные метеориты) — хондриты",
                sousTypes = "Углистые хондриты (CI, CM, CO, CV, CK, CR, CH, CB), обыкновенные хондриты (H, L, LL), энстатитовые хондриты (EH, EL)",
                description = "Самые распространённые метеориты, обязанные своим названием хондрам — крошечным силикатным сферулам, образовавшимся в самые первые мгновения существования Солнечной системы, ещё до появления планет. Углистые хондриты, богатые органическими соединениями, наиболее изучены с научной точки зрения; обыкновенные хондриты, более многочисленные, иногда режут на недорогие срезы, хотя они более хрупки, чем железо или палласит."
            ),
            MeteoriteFamilyExplainer(
                nom = "Аэролиты (каменные метеориты) — ахондриты",
                sousTypes = "HED (Веста), обриты, уреилиты, ангриты, акапулькоиты, лодраниты, винонаиты и чрезвычайно редкие марсианские и лунные метеориты",
                description = "В отличие от хондритов, ахондриты происходят от родительских тел, достаточно крупных, чтобы расплавиться и дифференцироваться внутри, таких как кора астероида Веста (группа HED) — или даже кора Марса и Луны. Последние, выброшенные при ударах и впоследствии захваченные земной гравитацией после долгого межпланетного путешествия, входят в число самых редких и самых дорогих материалов на планете."
            )
        ),
        disclaimerTitle = "Подлинность и происхождение",
        disclaimerBody = "Как и рынок драгоценных камней, рынок метеоритов привлекает всё больше подделок и «meteor-wrongs» (земных пород, ошибочно принимаемых за метеориты). При покупке образца для обработки или коллекционирования всегда требуйте документально подтверждённое происхождение и, в идеале, классификацию, опубликованную в Meteoritical Bulletin или установленную членом IMCA (International Meteorite Collectors Association). Цены, указанные в этом разделе, носят исключительно ориентировочный характер: они значительно варьируются в зависимости от размера фрагмента, качества подготовки и состояния рынка."
    )

    private val nl = MeteoriteClassificationPage(
        intro = "Meteorieten worden allereerst ingedeeld naar samenstelling: metaal, gesteente, of een mengsel van beide. Deze indeling bepaalt rechtstreeks hun belang voor een lapidair — ijzermeteorieten en pallasieten kunnen worden gesneden en gepolijst, terwijl de meeste steenmeteorieten wetenschappelijke verzamelstukken blijven.",
        familles = listOf(
            MeteoriteFamilyExplainer(
                nom = "Sideriet (ijzermeteorieten)",
                sousTypes = "Geclassificeerd naar structuur (hexaëdrieten, octaëdrieten, ataxieten) en naar chemische groep (IAB, IIAB, IIIAB, IVA, IVB...)",
                description = "Bijna volledig samengesteld uit een ijzer-nikkellegering, zijn ze afkomstig uit de metalen kern van gedifferentieerde asteroïden die bij botsingen zijn gefragmenteerd. Een salpeterzuur-ets op een gepolijste plak onthult vaak het Widmanstätten-patroon, een geometrisch vlechtwerk dat kenmerkend is voor hun trage afkoeling, soms verspreid over miljoenen jaren. Dit zijn de meteorieten die het meest worden bewerkt in de sieradenmakerij en de kunstmessenmakerij."
            ),
            MeteoriteFamilyExplainer(
                nom = "Siderolieten (gemengde meteorieten)",
                sousTypes = "Pallasieten (olivijn in een ijzermatrix) en mesosideriten (silicaten en metaal in een breccieachtig mengsel)",
                description = "Deze meteorieten mengen metaal en silicaten in min of meer gelijke verhoudingen. Pallasieten, de meest gegeerde, tonen olivijnkristallen — vaak van edelsteenkwaliteit, peridootkleurig — gevat in een ijzer-nikkelmatrix: gesneden en gepolijst behoren ze tot de mooiste stukken uit de meteorietbewerking. Mesosideriten, met hun chaotischere textuur, zijn vooral verzamelstukken."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aëroliet (steenmeteorieten) — chondrieten",
                sousTypes = "Koolstofhoudende chondrieten (CI, CM, CO, CV, CK, CR, CH, CB), gewone chondrieten (H, L, LL), enstatietchondrieten (EH, EL)",
                description = "De meest voorkomende meteorieten danken hun naam aan chondrules, minuscule silicaatbolletjes die zijn gevormd in de allereerste momenten van het zonnestelsel, nog voordat de planeten bestonden. Koolstofhoudende chondrieten, rijk aan organische verbindingen, zijn wetenschappelijk het meest bestudeerd; gewone chondrieten, die talrijker zijn, worden soms gesneden tot betaalbare plakken, hoewel ze breekbaarder zijn dan ijzer of pallasiet."
            ),
            MeteoriteFamilyExplainer(
                nom = "Aëroliet (steenmeteorieten) — achondrieten",
                sousTypes = "HED (Vesta), aubrieten, ureilieten, angrieten, acapulcoïeten, lodranieten, winonaïeten, en de uiterst zeldzame Mars- en maanmeteorieten",
                description = "In tegenstelling tot chondrieten zijn achondrieten afkomstig van moederlichamen die groot genoeg waren om intern te smelten en te differentiëren, zoals de korst van de asteroïde Vesta (HED-groep) — of zelfs die van Mars en de Maan. Deze laatste, weggeslingerd door inslagen en vervolgens ingevangen door de aardse zwaartekracht na een lange interplanetaire reis, behoren tot de zeldzaamste en duurste materialen op de planeet."
            )
        ),
        disclaimerTitle = "Echtheid en herkomst",
        disclaimerBody = "Net als de markt voor edelstenen trekt ook de meteorietenmarkt een groeiend aantal namaakstukken en „meteor-wrongs” (aardse gesteenten die voor meteorieten worden aangezien) aan. Eis bij een aankoop bestemd voor snijwerk of verzameling steeds een gedocumenteerde herkomst en idealiter een classificatie gepubliceerd in de Meteoritical Bulletin of vastgesteld door een lid van de IMCA (International Meteorite Collectors Association). De in dit gedeelte vermelde prijzen zijn louter indicatief: ze variëren enorm naargelang de grootte van het fragment, de kwaliteit van de bewerking en de marktsituatie."
    )

    private val byLanguage: Map<String, MeteoriteClassificationPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): MeteoriteClassificationPage = byLanguage[languageCode] ?: fr
}
