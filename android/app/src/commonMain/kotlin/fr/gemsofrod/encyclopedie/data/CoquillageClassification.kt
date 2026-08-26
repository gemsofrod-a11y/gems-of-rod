package fr.gemsofrod.encyclopedie.data

data class CoquillageFamilyExplainer(
    val nom: String,
    val sousTypes: String,
    val description: String
)

data class CoquillageClassificationPage(
    val intro: String,
    val familles: List<CoquillageFamilyExplainer>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant la classification générale des
 * coquillages (gastéropodes, bivalves, céphalopode à coquille) et leurs
 * principaux représentants d'intérêt lapidaire, traduit dans les langues de
 * l'app indépendamment des fiches individuelles — même principe que
 * [FossileClassificationInfo] et [MeteoriteClassificationInfo].
 */
object CoquillageClassificationInfo {
    private val fr = CoquillageClassificationPage(
        intro = "Les coquillages présentés ici sont classés par grand groupe de mollusques : gastéropodes (coquille unique, souvent enroulée en spirale), bivalves (coquille en deux valves symétriques) et céphalopodes à coquille externe, dont le nautile est aujourd'hui l'unique représentant vivant. L'intérêt lapidaire d'un coquillage dépend surtout de la structure de sa coquille : une coquille nacrée (aragonite en fines lamelles) se scie et se polit en dégageant une iridescence recherchée, tandis qu'une coquille calcitique opaque reste avant tout appréciée pour sa forme entière.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gastéropodes",
                sousTypes = "Ormeau, cauris, conque reine, trocas, burgau, murex, strombes",
                description = "Mollusques à coquille unique, le plus souvent enroulée en spirale, qui rampent sur un large pied musculeux. C'est le groupe le plus diversifié : il réunit aussi bien des espèces à nacre épaisse très recherchée en bijouterie (ormeau, trocas, burgau) que des espèces à coquille porcelanée non nacrée appréciées entières (cauris) ou des espèces à intérêt avant tout historique, comme le murex, source de la pourpre antique."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalves",
                sousTypes = "Huîtres perlières, coquille Saint-Jacques, spondyle, bénitier géant, moule perlière d'eau douce",
                description = "Mollusques à coquille en deux valves symétriques reliées par une charnière, filtrant leur nourriture dans l'eau. Ce groupe comprend les principales espèces nacrières exploitées pour la perliculture (huîtres perlières de Tahiti et des mers du Sud), aux côtés d'espèces non nacrées à intérêt purement décoratif ou historique (coquille Saint-Jacques, spondyle andin)."
            ),
            CoquillageFamilyExplainer(
                nom = "Céphalopode à coquille",
                sousTypes = "Nautile",
                description = "Groupe aujourd'hui réduit à un seul genre vivant, le nautile, dernier héritier d'une lignée de céphalopodes à coquille externe autrefois dominée par les ammonites, aujourd'hui entièrement fossiles. Sa coquille en spirale logarithmique cloisonnée en loges successives en fait une pièce à part, à mi-chemin entre le coquillage ornemental et la curiosité scientifique."
            )
        ),
        disclaimerTitle = "Espèces protégées et provenance",
        disclaimerBody = "Plusieurs coquillages présentés dans cette section proviennent d'espèces aujourd'hui protégées ou menacées (nautile, bénitier géant, moule perlière d'eau douce, conque reine), dont le commerce international est encadré par la Convention de Washington (CITES) ou par des réglementations locales. Pour tout achat, privilégiez une provenance documentée et légale, en particulier pour les espèces les plus vulnérables. Les prix indiqués dans cette section sont purement indicatifs : ils varient énormément selon la taille de la pièce, son état de conservation et l'état du marché."
    )

    private val en = CoquillageClassificationPage(
        intro = "The shells presented here are classified by broad mollusc group: gastropods (a single shell, usually coiled in a spiral), bivalves (a shell in two symmetrical valves) and shelled cephalopods, of which the nautilus is today the sole living representative. A shell's lapidary interest mainly depends on its structure: a nacreous shell (aragonite in fine layers) can be sawn and polished to reveal a prized iridescence, while an opaque calcitic shell is mainly valued for its whole form.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gastropods",
                sousTypes = "Abalone, cowries, queen conch, trochus, turban shell, murex, spider conch",
                description = "Molluscs with a single shell, usually coiled in a spiral, that crawl on a broad muscular foot. This is the most diverse group: it includes species with thick nacre highly sought after in jewellery (abalone, trochus, turban shell), non-nacreous porcelain-shelled species prized whole (cowries), and species of mainly historical interest, such as murex, the source of ancient purple dye."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalves",
                sousTypes = "Pearl oysters, scallop, thorny oyster, giant clam, freshwater pearl mussel",
                description = "Molluscs with a shell in two symmetrical valves joined by a hinge, filtering their food from the water. This group includes the main nacreous species farmed for pearl culture (Tahitian and South Sea pearl oysters), alongside non-nacreous species valued purely for decoration or history (scallop, Andean thorny oyster)."
            ),
            CoquillageFamilyExplainer(
                nom = "Shelled cephalopod",
                sousTypes = "Nautilus",
                description = "A group now reduced to a single living genus, the nautilus, the last heir of a lineage of shelled cephalopods once dominated by ammonites, today entirely fossil. Its logarithmic spiral shell, partitioned into successive chambers, makes it a thing apart, halfway between ornamental shell and scientific curiosity."
            )
        ),
        disclaimerTitle = "Protected species and provenance",
        disclaimerBody = "Several shells presented in this section come from species that are now protected or endangered (nautilus, giant clam, freshwater pearl mussel, queen conch), whose international trade is regulated by the Washington Convention (CITES) or by local regulations. For any purchase, favour documented, legal provenance, especially for the most vulnerable species. The prices given in this section are purely indicative: they vary enormously depending on the size of the piece, its condition and market conditions."
    )

    private val es = CoquillageClassificationPage(
        intro = "Los caracoles y conchas presentados aquí se clasifican por gran grupo de moluscos: gasterópodos (concha única, a menudo enrollada en espiral), bivalvos (concha en dos valvas simétricas) y cefalópodos con concha externa, de los cuales el nautilo es hoy el único representante vivo. El interés lapidario de una concha depende sobre todo de la estructura de su caparazón: una concha nacarada (aragonito en finas láminas) se sierra y se pule revelando una iridiscencia muy buscada, mientras que una concha calcítica opaca se aprecia sobre todo por su forma entera.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gasterópodos",
                sousTypes = "Oreja de mar, cauríes, caracola reina, troco, turbo, múrex, strombus araña",
                description = "Moluscos de concha única, generalmente enrollada en espiral, que se desplazan sobre un amplio pie muscular. Es el grupo más diverso: reúne tanto especies de nácar grueso muy apreciado en joyería (oreja de mar, troco, turbo) como especies de concha porcelánica no nacarada apreciadas enteras (cauríes) o especies de interés sobre todo histórico, como el múrex, fuente de la púrpura antigua."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalvos",
                sousTypes = "Ostras perlíferas, vieira, spondylus, tridacna gigante, mejillón perlífero de agua dulce",
                description = "Moluscos de concha en dos valvas simétricas unidas por una charnela, que filtran su alimento en el agua. Este grupo incluye las principales especies nacarinas explotadas para la perlicultura (ostras perlíferas de Tahití y de los mares del Sur), junto a especies no nacaradas de interés puramente decorativo o histórico (vieira, spondylus andino)."
            ),
            CoquillageFamilyExplainer(
                nom = "Cefalópodo con concha",
                sousTypes = "Nautilo",
                description = "Grupo hoy reducido a un único género vivo, el nautilo, último heredero de un linaje de cefalópodos de concha externa antiguamente dominado por los amonites, hoy completamente fósiles. Su concha en espiral logarítmica, tabicada en cámaras sucesivas, la convierte en una pieza aparte, a medio camino entre la concha ornamental y la curiosidad científica."
            )
        ),
        disclaimerTitle = "Especies protegidas y procedencia",
        disclaimerBody = "Varias conchas presentadas en esta sección proceden de especies hoy protegidas o amenazadas (nautilo, tridacna gigante, mejillón perlífero de agua dulce, caracola reina), cuyo comercio internacional está regulado por la Convención de Washington (CITES) o por normativas locales. Para cualquier compra, priorice una procedencia documentada y legal, en particular para las especies más vulnerables. Los precios indicados en esta sección son puramente indicativos: varían enormemente según el tamaño de la pieza, su estado de conservación y la situación del mercado."
    )

    private val it = CoquillageClassificationPage(
        intro = "Le conchiglie presentate qui sono classificate per grande gruppo di molluschi: gasteropodi (conchiglia unica, spesso avvolta a spirale), bivalvi (conchiglia in due valve simmetriche) e cefalopodi a conchiglia esterna, di cui il nautilus è oggi l'unico rappresentante vivente. L'interesse lapidario di una conchiglia dipende soprattutto dalla struttura del suo guscio: una conchiglia madreperlacea (aragonite in sottili lamelle) si sega e si lucida rivelando un'iridescenza ricercata, mentre una conchiglia calcitica opaca viene apprezzata soprattutto per la sua forma intera.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gasteropodi",
                sousTypes = "Aliotide, ciprea, strombo regina, trochus, turbo, murice, strombo ragno",
                description = "Molluschi a conchiglia unica, generalmente avvolta a spirale, che strisciano su un ampio piede muscoloso. È il gruppo più diversificato: comprende sia specie dalla madreperla spessa molto ricercata in gioielleria (aliotide, trochus, turbo), sia specie dalla conchiglia porcellanata non madreperlacea apprezzate intere (ciprea), sia specie di interesse soprattutto storico, come il murice, fonte della porpora antica."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalvi",
                sousTypes = "Ostriche perlifere, capasanta, spondilo, tridacna gigante, cozza perlifera d'acqua dolce",
                description = "Molluschi a conchiglia in due valve simmetriche unite da una cerniera, che filtrano il nutrimento nell'acqua. Questo gruppo comprende le principali specie madreperlacee sfruttate per la perlicoltura (ostriche perlifere di Tahiti e dei mari del Sud), accanto a specie non madreperlacee di interesse puramente decorativo o storico (capasanta, spondilo andino)."
            ),
            CoquillageFamilyExplainer(
                nom = "Cefalopode a conchiglia",
                sousTypes = "Nautilus",
                description = "Gruppo oggi ridotto a un solo genere vivente, il nautilus, ultimo erede di una linea di cefalopodi a conchiglia esterna un tempo dominata dalle ammoniti, oggi interamente fossili. La sua conchiglia a spirale logaritmica, suddivisa in camere successive, ne fa un pezzo a parte, a metà strada tra la conchiglia ornamentale e la curiosità scientifica."
            )
        ),
        disclaimerTitle = "Specie protette e provenienza",
        disclaimerBody = "Diverse conchiglie presentate in questa sezione provengono da specie oggi protette o minacciate (nautilus, tridacna gigante, cozza perlifera d'acqua dolce, strombo regina), il cui commercio internazionale è regolato dalla Convenzione di Washington (CITES) o da normative locali. Per qualsiasi acquisto, privilegiate una provenienza documentata e legale, in particolare per le specie più vulnerabili. I prezzi indicati in questa sezione sono puramente indicativi: variano enormemente in base alle dimensioni del pezzo, al suo stato di conservazione e alla situazione del mercato."
    )

    private val de = CoquillageClassificationPage(
        intro = "Die hier vorgestellten Muscheln und Schnecken werden nach großen Weichtiergruppen eingeteilt: Schnecken (einteilige, meist spiralig gewundene Schale), Muscheln (Schale aus zwei symmetrischen Klappen) und Kopffüßer mit Außenschale, von denen der Nautilus heute der einzige lebende Vertreter ist. Das lapidare Interesse an einer Schale hängt vor allem von ihrer Struktur ab: Eine perlmuttartige Schale (Aragonit in feinen Lamellen) lässt sich sägen und polieren, wodurch eine begehrte Irisierung sichtbar wird, während eine opake kalzitische Schale vor allem wegen ihrer vollständigen Form geschätzt wird.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Schnecken",
                sousTypes = "Seeohr, Kaurischnecken, Fechterschnecke, Kreiselschnecke, Turbanschnecke, Stachelschnecke, Spinnenschnecke",
                description = "Weichtiere mit einteiliger, meist spiralig gewundener Schale, die auf einem breiten Muskelfuß kriechen. Es ist die vielfältigste Gruppe: Sie vereint sowohl Arten mit dickem, in der Schmuckherstellung sehr gefragtem Perlmutt (Seeohr, Kreiselschnecke, Turbanschnecke) als auch nicht perlmuttartige, porzellanartige Arten, die im Ganzen geschätzt werden (Kaurischnecken), sowie Arten von vor allem historischem Interesse wie die Stachelschnecke, Quelle des antiken Purpurs."
            ),
            CoquillageFamilyExplainer(
                nom = "Muscheln",
                sousTypes = "Perlaustern, Jakobsmuschel, Stachelauster, Riesenmuschel, Flussperlmuschel",
                description = "Weichtiere mit einer aus zwei symmetrischen, durch ein Schloss verbundenen Klappen bestehenden Schale, die ihre Nahrung aus dem Wasser filtern. Diese Gruppe umfasst die wichtigsten für die Perlenzucht genutzten Perlmuttarten (Perlaustern aus Tahiti und den Südseeinseln) neben nicht perlmuttartigen Arten von rein dekorativem oder historischem Interesse (Jakobsmuschel, andine Stachelauster)."
            ),
            CoquillageFamilyExplainer(
                nom = "Kopffüßer mit Schale",
                sousTypes = "Nautilus",
                description = "Eine heute auf eine einzige lebende Gattung reduzierte Gruppe, der Nautilus, letzter Erbe einer Linie von Kopffüßern mit Außenschale, die einst von den heute vollständig fossilen Ammoniten dominiert wurde. Seine logarithmische Spiralschale, unterteilt in aufeinanderfolgende Kammern, macht ihn zu einem Sonderfall zwischen Zierschale und wissenschaftlicher Kuriosität."
            )
        ),
        disclaimerTitle = "Geschützte Arten und Herkunft",
        disclaimerBody = "Mehrere in diesem Abschnitt vorgestellte Schalen stammen von heute geschützten oder bedrohten Arten (Nautilus, Riesenmuschel, Flussperlmuschel, Fechterschnecke), deren internationaler Handel durch das Washingtoner Artenschutzübereinkommen (CITES) oder lokale Vorschriften geregelt ist. Achten Sie bei jedem Kauf auf eine dokumentierte und legale Herkunft, insbesondere bei den am stärksten gefährdeten Arten. Die in diesem Abschnitt genannten Preise sind rein indikativ: Sie variieren stark je nach Größe des Stücks, seinem Erhaltungszustand und der Marktlage."
    )

    private val pt = CoquillageClassificationPage(
        intro = "As conchas apresentadas aqui são classificadas por grande grupo de moluscos: gastrópodes (concha única, geralmente enrolada em espiral), bivalves (concha em duas valvas simétricas) e cefalópodes de concha externa, dos quais o nautilo é hoje o único representante vivo. O interesse lapidar de uma concha depende sobretudo da estrutura da sua carapaça: uma concha nacarada (aragonite em finas lamelas) serra-se e polir-se, revelando uma iridescência muito procurada, enquanto uma concha calcítica opaca é sobretudo apreciada pela sua forma inteira.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Gastrópodes",
                sousTypes = "Orelha-do-mar, búzios-moeda, búzio-rainha, trocus, turbo, múrex, aranha-do-mar",
                description = "Moluscos de concha única, geralmente enrolada em espiral, que rastejam sobre um amplo pé muscular. É o grupo mais diversificado: reúne espécies de nácar espesso muito procurado em joalharia (orelha-do-mar, trocus, turbo), espécies de concha porcelanada não nacarada apreciadas inteiras (búzios-moeda) e espécies de interesse sobretudo histórico, como o múrex, fonte da púrpura antiga."
            ),
            CoquillageFamilyExplainer(
                nom = "Bivalves",
                sousTypes = "Ostras perlíferas, vieira, spondylus, tridacna gigante, mexilhão perlífero de água doce",
                description = "Moluscos de concha em duas valvas simétricas unidas por uma charneira, que filtram o alimento na água. Este grupo inclui as principais espécies nacarinas exploradas para a perlicultura (ostras perlíferas do Taiti e dos mares do Sul), a par de espécies não nacaradas de interesse puramente decorativo ou histórico (vieira, spondylus andino)."
            ),
            CoquillageFamilyExplainer(
                nom = "Cefalópode de concha",
                sousTypes = "Nautilo",
                description = "Grupo hoje reduzido a um único género vivo, o nautilo, último herdeiro de uma linhagem de cefalópodes de concha externa outrora dominada pelos amonites, hoje inteiramente fósseis. A sua concha em espiral logarítmica, dividida em câmaras sucessivas, torna-a uma peça à parte, a meio caminho entre a concha ornamental e a curiosidade científica."
            )
        ),
        disclaimerTitle = "Espécies protegidas e proveniência",
        disclaimerBody = "Várias conchas apresentadas nesta secção provêm de espécies hoje protegidas ou ameaçadas (nautilo, tridacna gigante, mexilhão perlífero de água doce, búzio-rainha), cujo comércio internacional é regulado pela Convenção de Washington (CITES) ou por regulamentações locais. Para qualquer compra, privilegie uma proveniência documentada e legal, em particular para as espécies mais vulneráveis. Os preços indicados nesta secção são puramente indicativos: variam enormemente consoante o tamanho da peça, o seu estado de conservação e a situação do mercado."
    )

    private val zh = CoquillageClassificationPage(
        intro = "本节介绍的贝壳按软体动物大类划分:腹足纲(单壳,通常呈螺旋状盘绕)、双壳纲(壳由两片对称的壳瓣组成)以及有壳头足纲,鹦鹉螺是该类现今唯一存活的代表。贝壳的雕刻价值主要取决于其结构:珍珠层贝壳(细薄层状文石)可锯切抛光,呈现出备受追捧的珠光色彩;而不透明的方解石质贝壳则主要因其完整形态而受到珍视。",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "腹足纲",
                sousTypes = "鲍鱼、宝螺、女王凤凰螺、马蹄螺、蝾螺、骨螺、蜘蛛螺",
                description = "单壳软体动物,通常呈螺旋状盘绕,依靠宽大的肌肉足爬行。这是最多样化的类群:既有厚珍珠层、备受珠宝业青睐的物种(鲍鱼、马蹄螺、蝾螺),也有非珍珠质、瓷质外壳、整壳受人喜爱的物种(宝螺),还有主要具有历史意义的物种,如骨螺,是古代紫色染料的来源。"
            ),
            CoquillageFamilyExplainer(
                nom = "双壳纲",
                sousTypes = "珍珠牡蛎、扇贝、多刺牡蛎、大砗磲、淡水珍珠蚌",
                description = "双壳软体动物,壳由两片经铰合部相连的对称壳瓣组成,通过滤食水中的食物为生。该类群包括用于珍珠养殖的主要珍珠质物种(大溪地及南太平洋珍珠牡蛎),以及纯粹具有装饰或历史价值的非珍珠质物种(扇贝、安第斯多刺牡蛎)。"
            ),
            CoquillageFamilyExplainer(
                nom = "有壳头足纲",
                sousTypes = "鹦鹉螺",
                description = "该类群如今仅剩一个存活属——鹦鹉螺,是曾经由如今已完全化石化的菊石所主导的有壳头足类谱系的最后传人。其呈对数螺旋形、分隔为连续腔室的外壳,使其成为介于观赏贝壳与科学奇珍之间的独特存在。"
            )
        ),
        disclaimerTitle = "受保护物种与来源",
        disclaimerBody = "本节介绍的部分贝壳来自现今受保护或濒危的物种(鹦鹉螺、大砗磲、淡水珍珠蚌、女王凤凰螺),其国际贸易受《华盛顿公约》(CITES)或当地法规管制。购买时请务必优先选择来源可追溯、合法的产品,尤其是最脆弱的物种。本节所列价格仅供参考:实际价格会因标本大小、保存状态及市场行情而有很大差异。"
    )

    private val ru = CoquillageClassificationPage(
        intro = "Представленные здесь раковины классифицируются по крупным группам моллюсков: брюхоногие (единая раковина, чаще всего закрученная спиралью), двустворчатые (раковина из двух симметричных створок) и раковинные головоногие, из которых наутилус — единственный ныне живущий представитель. Ювелирно-камнерезная ценность раковины зависит прежде всего от структуры её панциря: перламутровая раковина (арагонит тонкими слоями) распиливается и полируется, обнажая ценимый переливчатый блеск, тогда как непрозрачная кальцитовая раковина ценится прежде всего за свою цельную форму.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Брюхоногие",
                sousTypes = "Морское ушко, каури, королевский стромбус, трохус, турбо, мурекс, паучий стромбус",
                description = "Моллюски с единой раковиной, чаще всего закрученной спиралью, передвигающиеся на широкой мускулистой ноге. Это самая разнообразная группа: она объединяет виды с толстым перламутром, высоко ценимым в ювелирном деле (морское ушко, трохус, турбо), неперламутровые фарфоровидные виды, ценимые целиком (каури), а также виды, представляющие прежде всего исторический интерес, например мурекс — источник античного пурпура."
            ),
            CoquillageFamilyExplainer(
                nom = "Двустворчатые",
                sousTypes = "Жемчужницы, гребешок, шпондилус, гигантская тридакна, пресноводная жемчужница",
                description = "Моллюски с раковиной из двух симметричных створок, соединённых замком, фильтрующие пищу из воды. К этой группе относятся основные перламутровые виды, используемые в жемчужном промысле (таитянские и южноморские жемчужницы), а также неперламутровые виды чисто декоративного или исторического интереса (гребешок, андский шпондилус)."
            ),
            CoquillageFamilyExplainer(
                nom = "Раковинный головоногий",
                sousTypes = "Наутилус",
                description = "Группа, ныне сведённая к единственному живому роду — наутилусу, последнему представителю линии раковинных головоногих, некогда доминировавшей благодаря аммонитам, сегодня полностью ископаемым. Его логарифмически завитая раковина, разделённая на последовательные камеры, делает его особым объектом — на полпути между декоративной раковиной и научным курьёзом."
            )
        ),
        disclaimerTitle = "Охраняемые виды и происхождение",
        disclaimerBody = "Некоторые раковины, представленные в этом разделе, происходят от видов, ныне охраняемых или находящихся под угрозой исчезновения (наутилус, гигантская тридакна, пресноводная жемчужница, королевский стромбус), международная торговля которыми регулируется Вашингтонской конвенцией (СИТЕС) или местным законодательством. При любой покупке отдавайте предпочтение документированному и законному происхождению, особенно для наиболее уязвимых видов. Указанные в этом разделе цены носят исключительно ориентировочный характер: они сильно варьируются в зависимости от размера экземпляра, состояния сохранности и конъюнктуры рынка."
    )

    private val nl = CoquillageClassificationPage(
        intro = "De hier gepresenteerde schelpen worden ingedeeld naar grote weekdiergroep: buikpotigen (één schelp, meestal spiraalvormig gewonden), tweekleppigen (schelp met twee symmetrische kleppen) en koppotigen met uitwendige schelp, waarvan de nautilus tegenwoordig de enige levende vertegenwoordiger is. Het lapidaire belang van een schelp hangt vooral af van de structuur ervan: een parelmoeren schelp (aragoniet in dunne laagjes) kan worden gezaagd en gepolijst, waarbij een gewilde iriserende glans zichtbaar wordt, terwijl een ondoorzichtige calcietschelp vooral wordt gewaardeerd om haar volledige vorm.",
        familles = listOf(
            CoquillageFamilyExplainer(
                nom = "Buikpotigen",
                sousTypes = "Zee-oor, kauri's, koningskroonslak, trochus, turboslak, stekelhoorn, spinnenhoorn",
                description = "Weekdieren met één schelp, meestal spiraalvormig gewonden, die op een brede gespierde voet kruipen. Dit is de meest diverse groep: ze omvat zowel soorten met dik parelmoer dat zeer gewild is in de juwelierskunst (zee-oor, trochus, turboslak) als niet-parelmoeren porseleinachtige soorten die heel worden gewaardeerd (kauri's), of soorten van vooral historisch belang, zoals de stekelhoorn, bron van de antieke purperkleurstof."
            ),
            CoquillageFamilyExplainer(
                nom = "Tweekleppigen",
                sousTypes = "Parelmoesters, jakobsschelp, doornoester, reuzentridacna, zoetwaterparelmossel",
                description = "Weekdieren met een schelp van twee symmetrische, door een scharnier verbonden kleppen, die hun voedsel uit het water filteren. Deze groep omvat de belangrijkste parelmoeren soorten die voor parelteelt worden gebruikt (parelmoesters uit Tahiti en de Zuidzee), naast niet-parelmoeren soorten van puur decoratief of historisch belang (jakobsschelp, Andes-doornoester)."
            ),
            CoquillageFamilyExplainer(
                nom = "Koppotige met schelp",
                sousTypes = "Nautilus",
                description = "Een groep die tegenwoordig is teruggebracht tot één enkel levend geslacht, de nautilus, de laatste erfgenaam van een lijn van koppotigen met uitwendige schelp die ooit werd gedomineerd door ammonieten, tegenwoordig volledig fossiel. Haar logaritmisch gewonden schelp, onderverdeeld in opeenvolgende kamers, maakt haar tot een aparte verschijning, halverwege tussen sierschelp en wetenschappelijke curiositeit."
            )
        ),
        disclaimerTitle = "Beschermde soorten en herkomst",
        disclaimerBody = "Verschillende in dit onderdeel gepresenteerde schelpen zijn afkomstig van soorten die tegenwoordig beschermd of bedreigd zijn (nautilus, reuzentridacna, zoetwaterparelmossel, koningskroonslak), waarvan de internationale handel wordt gereguleerd door het CITES-verdrag (Washington) of lokale regelgeving. Kies bij elke aankoop voor een gedocumenteerde en legale herkomst, met name voor de meest kwetsbare soorten. De in dit onderdeel vermelde prijzen zijn louter indicatief: ze variëren enorm naargelang de grootte van het stuk, de bewaartoestand en de marktsituatie."
    )

    private val byLanguage: Map<String, CoquillageClassificationPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): CoquillageClassificationPage = byLanguage[languageCode] ?: fr
}
