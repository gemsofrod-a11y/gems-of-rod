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
 * reformulé à partir de faits de pétrologie généraux ; traduit dans les
 * 9 langues de l'app.
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

    private val en = RochesMeresPage(
        title = "Host rocks",
        intro = "A gemstone never comes from nowhere: it crystallizes within a host rock, the \"parent rock,\" whose geological history — temperature, pressure, chemical composition — determines which species can form there. There are three broad rock families, plus a special case, pegmatite, which plays a distinct role in the genesis of fine gemstones.",
        especesLabel = "Characteristic stones",
        disclaimerTitle = "A tendency, not a rule",
        disclaimerBody = "The same species can sometimes form in several different geological settings (ruby, for instance, occurs in metamorphic marbles as well as in certain basalts): this classification indicates each stone's most characteristic origin, not an absolute rule.",
        roches = listOf(
            RockOriginEntry(
                nom = "Igneous rock",
                description = "Formed by the cooling of magma. At depth, slow cooling gives crystals time to grow large (plutonic rock, such as granite); at the surface, the rapid cooling of lava produces fine crystals, or even a crystal-free volcanic glass. The deepest stones sometimes rise from the Earth's mantle through narrow volcanic pipes.",
                especesTypiques = "Diamond (brought up from the mantle by kimberlite), peridot, zircon, moldavite"
            ),
            RockOriginEntry(
                nom = "Pegmatite",
                description = "An igneous rock with an exceptionally coarse grain, formed from the very last pocket of magma to crystallize — rich in water and in rare chemical elements that ordinary minerals failed to absorb. This fluid environment gives large crystals, sometimes several metres across, time to grow freely: pegmatite is the main source of collectible fine gemstones.",
                especesTypiques = "Beryl (emerald, aquamarine, morganite, heliodor), tourmaline, topaz, kunzite, spodumene, lepidolite"
            ),
            RockOriginEntry(
                nom = "Metamorphic rock",
                description = "A pre-existing rock (igneous, sedimentary, or already metamorphic) transformed at depth by heat and pressure without complete melting, which reorganizes its mineral structure. Regional metamorphism affects vast areas during mountain-building; contact metamorphism, more localized, occurs near a magmatic intrusion.",
                especesTypiques = "Ruby and sapphire (metamorphic marbles and gneisses), garnet, jade (jadeite), lapis lazuli, kyanite"
            ),
            RockOriginEntry(
                nom = "Sedimentary rock",
                description = "Formed at the surface, without deep heat: through the accumulation and cementation of particles (sandstone, limestone) or the precipitation of minerals dissolved in water. Chemical weathering of pre-existing rocks in arid climates can also concentrate certain elements, giving rise to secondary deposits.",
                especesTypiques = "Opal (silica precipitated in fissures), turquoise, malachite and azurite (weathering zones of copper deposits), amber (fossilized resin)"
            )
        )
    )

    private val es = RochesMeresPage(
        title = "Rocas madre",
        intro = "Una piedra preciosa nunca surge de la nada: cristaliza dentro de una roca huésped, la «roca madre», cuya historia geológica —temperatura, presión, composición química— determina qué especies pueden formarse en ella. Se distinguen tres grandes familias de rocas, más un caso particular, la pegmatita, que desempeña un papel aparte en la génesis de las piedras finas.",
        especesLabel = "Piedras características",
        disclaimerTitle = "Una tendencia, no una regla",
        disclaimerBody = "Una misma especie puede a veces formarse en varios contextos geológicos distintos (el rubí, por ejemplo, existe tanto en mármoles metamórficos como en ciertos basaltos): esta clasificación indica el origen más característico de cada piedra, no una regla absoluta.",
        roches = listOf(
            RockOriginEntry(
                nom = "Roca ígnea (magmática)",
                description = "Nacida del enfriamiento de un magma. En profundidad, el enfriamiento lento da tiempo a que los cristales crezcan (roca plutónica, como el granito); en superficie, el enfriamiento rápido de una lava produce cristales finos, o incluso un vidrio volcánico sin estructura cristalina. Las piedras más profundas a veces ascienden desde el manto terrestre por estrechas chimeneas volcánicas.",
                especesTypiques = "Diamante (traído desde el manto por la kimberlita), peridoto, circón, moldavita"
            ),
            RockOriginEntry(
                nom = "Pegmatita",
                description = "Roca ígnea de grano excepcionalmente grueso, procedente de la última bolsa de magma en cristalizar —rica en agua y en elementos químicos raros que los minerales ordinarios no absorbieron. Este entorno fluido da tiempo a que crezcan libremente grandes cristales, a veces de varios metros: la pegmatita es la principal fuente de piedras finas de colección.",
                especesTypiques = "Berilo (esmeralda, aguamarina, morganita, heliodoro), turmalina, topacio, kunzita, espodumena, lepidolita"
            ),
            RockOriginEntry(
                nom = "Roca metamórfica",
                description = "Roca preexistente (ígnea, sedimentaria o ya metamórfica) transformada en profundidad por el calor y la presión sin fusión completa, lo que reorganiza su estructura mineral. El metamorfismo regional afecta a vastas zonas durante la formación de cadenas montañosas; el metamorfismo de contacto, más localizado, se produce cerca de una intrusión magmática.",
                especesTypiques = "Rubí y zafiro (mármoles y gneises metamórficos), granate, jade (jadeíta), lapislázuli, distena"
            ),
            RockOriginEntry(
                nom = "Roca sedimentaria",
                description = "Formada en superficie, sin intervención del calor profundo: por acumulación y cementación de partículas (arenisca, caliza) o por precipitación de minerales disueltos en agua. La alteración química de rocas preexistentes en clima árido también puede concentrar ciertos elementos y dar lugar a yacimientos secundarios.",
                especesTypiques = "Ópalo (sílice precipitada en fisuras), turquesa, malaquita y azurita (zonas de alteración de yacimientos de cobre), ámbar (resina fosilizada)"
            )
        )
    )

    private val it = RochesMeresPage(
        title = "Rocce madri",
        intro = "Una pietra preziosa non nasce mai dal nulla: cristallizza all'interno di una roccia ospite, la «roccia madre», la cui storia geologica — temperatura, pressione, composizione chimica — determina quali specie possono formarvisi. Si distinguono tre grandi famiglie di rocce, più un caso particolare, la pegmatite, che svolge un ruolo a parte nella genesi delle pietre fini.",
        especesLabel = "Pietre caratteristiche",
        disclaimerTitle = "Una tendenza, non una regola",
        disclaimerBody = "Una stessa specie può talvolta formarsi in più contesti geologici diversi (il rubino, ad esempio, esiste sia in marmi metamorfici sia in alcuni basalti): questa classificazione indica l'origine più caratteristica di ciascuna pietra, non una regola assoluta.",
        roches = listOf(
            RockOriginEntry(
                nom = "Roccia magmatica (ignea)",
                description = "Nata dal raffreddamento di un magma. In profondità, il raffreddamento lento dà il tempo ai cristalli di ingrandirsi (roccia plutonica, come il granito); in superficie, il raffreddamento rapido di una lava produce cristalli fini, se non addirittura un vetro vulcanico privo di struttura cristallina. Le pietre più profonde risalgono talvolta dal mantello terrestre attraverso strette ciminiere vulcaniche.",
                especesTypiques = "Diamante (risalito dal mantello tramite la kimberlite), peridoto, zircone, moldavite"
            ),
            RockOriginEntry(
                nom = "Pegmatite",
                description = "Roccia magmatica dal grano eccezionalmente grosso, derivata dall'ultimissima sacca di magma a cristallizzare — ricca d'acqua e di elementi chimici rari che i minerali ordinari non hanno assorbito. Questo ambiente fluido dà il tempo a grandi cristalli, talvolta di più metri, di crescere liberamente: la pegmatite è la principale fonte di pietre fini da collezione.",
                especesTypiques = "Berillo (smeraldo, acquamarina, morganite, eliodoro), tormalina, topazio, kunzite, spodumene, lepidolite"
            ),
            RockOriginEntry(
                nom = "Roccia metamorfica",
                description = "Roccia preesistente (magmatica, sedimentaria o già metamorfica) trasformata in profondità dal calore e dalla pressione senza fusione completa, il che riorganizza la sua struttura minerale. Il metamorfismo regionale interessa vaste zone durante la formazione delle catene montuose; il metamorfismo di contatto, più localizzato, si produce in prossimità di un'intrusione magmatica.",
                especesTypiques = "Rubino e zaffiro (marmi e gneiss metamorfici), granato, giada (giadeite), lapislazzuli, cianite"
            ),
            RockOriginEntry(
                nom = "Roccia sedimentaria",
                description = "Formatasi in superficie, senza intervento del calore profondo: per accumulo e cementazione di particelle (arenaria, calcare) o per precipitazione di minerali disciolti in acqua. L'alterazione chimica di rocce preesistenti in clima arido può inoltre concentrare certi elementi e dare origine a giacimenti secondari.",
                especesTypiques = "Opale (silice precipitata in fessure), turchese, malachite e azzurrite (zone di alterazione di giacimenti di rame), ambra (resina fossilizzata)"
            )
        )
    )

    private val de = RochesMeresPage(
        title = "Muttergesteine",
        intro = "Ein Edelstein entsteht nie aus dem Nichts: Er kristallisiert innerhalb eines Wirtsgesteins, des „Muttergesteins\", dessen geologische Geschichte — Temperatur, Druck, chemische Zusammensetzung — bestimmt, welche Arten sich dort bilden können. Man unterscheidet drei große Gesteinsfamilien sowie einen Sonderfall, den Pegmatit, der bei der Entstehung von Farbedelsteinen eine besondere Rolle spielt.",
        especesLabel = "Charakteristische Steine",
        disclaimerTitle = "Eine Tendenz, keine Regel",
        disclaimerBody = "Ein und dieselbe Art kann sich manchmal in mehreren unterschiedlichen geologischen Umgebungen bilden (Rubin etwa kommt sowohl in metamorphen Marmoren als auch in bestimmten Basalten vor): Diese Einteilung zeigt die charakteristischste Herkunft jedes Steins, keine absolute Regel.",
        roches = listOf(
            RockOriginEntry(
                nom = "Magmatisches Gestein (Eruptivgestein)",
                description = "Entstanden durch die Abkühlung von Magma. In der Tiefe lässt die langsame Abkühlung den Kristallen Zeit zu wachsen (Plutonit, wie Granit); an der Oberfläche erzeugt die rasche Abkühlung einer Lava feine Kristalle oder sogar ein kristallstrukturloses Vulkanglas. Die tiefsten Steine steigen manchmal durch enge vulkanische Schlote aus dem Erdmantel auf.",
                especesTypiques = "Diamant (durch Kimberlit aus dem Erdmantel aufgestiegen), Peridot, Zirkon, Moldavit"
            ),
            RockOriginEntry(
                nom = "Pegmatit",
                description = "Magmatisches Gestein mit außergewöhnlich grobem Korn, entstanden aus der allerletzten auskristallisierenden Magmatasche — reich an Wasser und seltenen chemischen Elementen, die von den gewöhnlichen Mineralen nicht aufgenommen wurden. Diese flüssige Umgebung gibt großen, manchmal mehrere Meter großen Kristallen Zeit, frei zu wachsen: Pegmatit ist die wichtigste Quelle für Sammler-Farbedelsteine.",
                especesTypiques = "Beryll (Smaragd, Aquamarin, Morganit, Heliodor), Turmalin, Topas, Kunzit, Spodumen, Lepidolith"
            ),
            RockOriginEntry(
                nom = "Metamorphes Gestein",
                description = "Bereits bestehendes Gestein (magmatisch, sedimentär oder bereits metamorph), das in der Tiefe durch Hitze und Druck ohne vollständiges Aufschmelzen umgewandelt wird, wodurch sich seine mineralische Struktur neu ordnet. Die Regionalmetamorphose betrifft weite Gebiete bei der Bildung von Gebirgsketten; die lokalere Kontaktmetamorphose tritt in der Nähe einer magmatischen Intrusion auf.",
                especesTypiques = "Rubin und Saphir (metamorphe Marmore und Gneise), Granat, Jade (Jadeit), Lapislazuli, Disthen"
            ),
            RockOriginEntry(
                nom = "Sedimentgestein",
                description = "An der Oberfläche entstanden, ohne Einwirkung von Tiefenhitze: durch Ansammlung und Verfestigung von Partikeln (Sandstein, Kalkstein) oder durch Ausfällung von im Wasser gelösten Mineralen. Die chemische Verwitterung bereits bestehender Gesteine in ariden Klimazonen kann zudem bestimmte Elemente anreichern und sekundäre Lagerstätten entstehen lassen.",
                especesTypiques = "Opal (in Klüften ausgefällte Kieselsäure), Türkis, Malachit und Azurit (Verwitterungszonen von Kupferlagerstätten), Bernstein (versteinertes Harz)"
            )
        )
    )

    private val pt = RochesMeresPage(
        title = "Rochas-mãe",
        intro = "Uma pedra preciosa nunca nasce do nada: cristaliza no interior de uma rocha hospedeira, a «rocha-mãe», cuja história geológica — temperatura, pressão, composição química — determina que espécies podem formar-se ali. Distinguem-se três grandes famílias de rochas, mais um caso particular, o pegmatito, que desempenha um papel à parte na génese das pedras finas.",
        especesLabel = "Pedras características",
        disclaimerTitle = "Uma tendência, não uma regra",
        disclaimerBody = "Uma mesma espécie pode por vezes formar-se em vários contextos geológicos diferentes (o rubi, por exemplo, existe tanto em mármores metamórficos como em certos basaltos): esta classificação indica a origem mais característica de cada pedra, não uma regra absoluta.",
        roches = listOf(
            RockOriginEntry(
                nom = "Rocha ígnea (magmática)",
                description = "Nascida do arrefecimento de um magma. Em profundidade, o arrefecimento lento dá tempo aos cristais para crescerem (rocha plutónica, como o granito); à superfície, o arrefecimento rápido de uma lava dá origem a cristais finos, ou mesmo a um vidro vulcânico sem estrutura cristalina. As pedras mais profundas sobem por vezes do manto terrestre através de estreitas chaminés vulcânicas.",
                especesTypiques = "Diamante (trazido do manto pelo kimberlito), peridoto, zircão, moldavite"
            ),
            RockOriginEntry(
                nom = "Pegmatito",
                description = "Rocha ígnea de grão excecionalmente grosso, proveniente da última bolsa de magma a cristalizar — rica em água e em elementos químicos raros que os minerais comuns não absorveram. Este ambiente fluido dá tempo a grandes cristais, por vezes de vários metros, para crescerem livremente: o pegmatito é a principal fonte de pedras finas de coleção.",
                especesTypiques = "Berilo (esmeralda, água-marinha, morganite, heliodoro), turmalina, topázio, kunzite, espodumênio, lepidolite"
            ),
            RockOriginEntry(
                nom = "Rocha metamórfica",
                description = "Rocha pré-existente (ígnea, sedimentar ou já metamórfica) transformada em profundidade pelo calor e pela pressão sem fusão completa, o que reorganiza a sua estrutura mineral. O metamorfismo regional afeta vastas zonas durante a formação de cadeias montanhosas; o metamorfismo de contacto, mais localizado, ocorre perto de uma intrusão magmática.",
                especesTypiques = "Rubi e safira (mármores e gnaisses metamórficos), granada, jade (jadeíte), lápis-lazúli, cianite"
            ),
            RockOriginEntry(
                nom = "Rocha sedimentar",
                description = "Formada à superfície, sem intervenção do calor profundo: por acumulação e cimentação de partículas (arenito, calcário) ou por precipitação de minerais dissolvidos na água. A alteração química de rochas pré-existentes em clima árido também pode concentrar certos elementos e dar origem a depósitos secundários.",
                especesTypiques = "Opala (sílica precipitada em falhas), turquesa, malaquite e azurite (zonas de alteração de jazidas de cobre), âmbar (resina fossilizada)"
            )
        )
    )

    private val zh = RochesMeresPage(
        title = "母岩",
        intro = "宝石从不凭空产生：它在寄主岩石——即「母岩」——内部结晶，而母岩的地质历史（温度、压力、化学成分）决定了哪些矿物能够在其中形成。岩石可分为三大类，此外还有一个特例——伟晶岩，它在彩色宝石的形成中扮演着特殊角色。",
        especesLabel = "代表性宝石",
        disclaimerTitle = "趋势，而非定律",
        disclaimerBody = "同一种矿物有时可以在多种不同的地质环境中形成（例如红宝石既存在于变质大理岩中，也存在于某些玄武岩中）：此分类指出的是每种宝石最具代表性的成因，而非绝对规律。",
        roches = listOf(
            RockOriginEntry(
                nom = "岩浆岩（火成岩）",
                description = "由岩浆冷却而成。在地下深处，缓慢冷却使晶体有充分时间长大（深成岩，如花岗岩）；在地表，熔岩快速冷却则形成细小晶体，甚至形成没有晶体结构的火山玻璃。最深处形成的宝石有时会通过狭窄的火山通道从地幔中被带上地表。",
                especesTypiques = "钻石（经金伯利岩从地幔带出）、橄榄石、锆石、捷克陨石玻璃"
            ),
            RockOriginEntry(
                nom = "伟晶岩",
                description = "颗粒异常粗大的岩浆岩，由最后一批结晶的岩浆残余形成——富含水分及普通矿物未能吸收的稀有化学元素。这种富含流体的环境为大型晶体（有时可达数米）提供了自由生长的时间：伟晶岩是收藏级彩色宝石的主要来源。",
                especesTypiques = "绿柱石（祖母绿、海蓝宝石、摩根石、金绿柱石）、碧玺、黄玉、锂辉石、锂云母"
            ),
            RockOriginEntry(
                nom = "变质岩",
                description = "先存岩石（岩浆岩、沉积岩或已经变质的岩石）在地下深处经高温高压作用但未完全熔融而重新转变，从而使其矿物结构重新排列。区域变质作用在造山运动中影响广大区域；接触变质作用则更为局部，发生在岩浆侵入体附近。",
                especesTypiques = "红宝石与蓝宝石（变质大理岩及片麻岩）、石榴石、翡翠（硬玉）、青金石、蓝晶石"
            ),
            RockOriginEntry(
                nom = "沉积岩",
                description = "在地表形成，没有深部高温参与：通过颗粒的堆积与胶结（砂岩、石灰岩），或通过水中溶解矿物的沉淀而形成。干旱气候下先存岩石的化学风化也可能富集某些元素，从而形成次生矿床。",
                especesTypiques = "蛋白石（在裂隙中沉淀的二氧化硅）、绿松石、孔雀石与蓝铜矿（铜矿床风化带）、琥珀（化石树脂）"
            )
        )
    )

    private val ru = RochesMeresPage(
        title = "Материнские породы",
        intro = "Драгоценный камень никогда не возникает из ниоткуда: он кристаллизуется внутри вмещающей породы — «материнской породы», геологическая история которой (температура, давление, химический состав) определяет, какие виды камней могут в ней образоваться. Различают три большие семьи пород, а также особый случай — пегматит, играющий особую роль в образовании цветных камней.",
        especesLabel = "Характерные камни",
        disclaimerTitle = "Тенденция, а не правило",
        disclaimerBody = "Один и тот же вид камня иногда может образовываться в нескольких разных геологических условиях (рубин, например, встречается как в метаморфических мраморах, так и в некоторых базальтах): эта классификация указывает наиболее характерное происхождение каждого камня, а не абсолютное правило.",
        roches = listOf(
            RockOriginEntry(
                nom = "Магматическая порода",
                description = "Образуется при остывании магмы. На глубине медленное остывание даёт кристаллам время вырасти (плутоническая порода, например гранит); на поверхности быстрое остывание лавы даёт мелкие кристаллы или даже вулканическое стекло без кристаллической структуры. Камни, образовавшиеся на наибольшей глубине, иногда поднимаются из мантии Земли по узким вулканическим каналам.",
                especesTypiques = "Алмаз (поднятый из мантии кимберлитом), перидот, циркон, молдавит"
            ),
            RockOriginEntry(
                nom = "Пегматит",
                description = "Магматическая порода с исключительно крупным зерном, образовавшаяся из самого последнего кристаллизующегося очага магмы — богатого водой и редкими химическими элементами, не усвоенными обычными минералами. Эта жидкая среда даёт время крупным, порой многометровым кристаллам свободно расти: пегматит — главный источник коллекционных цветных камней.",
                especesTypiques = "Берилл (изумруд, аквамарин, морганит, гелиодор), турмалин, топаз, кунцит, сподумен, лепидолит"
            ),
            RockOriginEntry(
                nom = "Метаморфическая порода",
                description = "Ранее существовавшая порода (магматическая, осадочная или уже метаморфическая), преобразованная на глубине под действием тепла и давления без полного расплавления, что приводит к перестройке её минеральной структуры. Региональный метаморфизм охватывает обширные территории при формировании горных цепей; контактовый метаморфизм, более локальный, происходит вблизи магматической интрузии.",
                especesTypiques = "Рубин и сапфир (метаморфические мраморы и гнейсы), гранат, жад (жадеит), лазурит, кианит"
            ),
            RockOriginEntry(
                nom = "Осадочная порода",
                description = "Образуется на поверхности без участия глубинного тепла: путём накопления и цементации частиц (песчаник, известняк) или осаждения минералов, растворённых в воде. Химическое выветривание ранее существовавших пород в засушливом климате также может концентрировать определённые элементы, приводя к образованию вторичных месторождений.",
                especesTypiques = "Опал (кремнезём, осаждённый в трещинах), бирюза, малахит и азурит (зоны выветривания медных месторождений), янтарь (окаменевшая смола)"
            )
        )
    )

    private val nl = RochesMeresPage(
        title = "Moedergesteenten",
        intro = "Een edelsteen ontstaat nooit uit het niets: hij kristalliseert binnen een gastheergesteente, het \"moedergesteente\", waarvan de geologische geschiedenis — temperatuur, druk, chemische samenstelling — bepaalt welke soorten zich daarin kunnen vormen. Er zijn drie grote gesteentefamilies te onderscheiden, plus een bijzonder geval, het pegmatiet, dat een aparte rol speelt bij het ontstaan van edelstenen.",
        especesLabel = "Kenmerkende stenen",
        disclaimerTitle = "Een tendens, geen regel",
        disclaimerBody = "Eenzelfde soort kan soms in verschillende geologische omgevingen ontstaan (robijn bijvoorbeeld komt zowel voor in metamorfe marmers als in bepaalde basalten): deze indeling geeft de meest kenmerkende oorsprong van elke steen aan, geen absolute regel.",
        roches = listOf(
            RockOriginEntry(
                nom = "Stollingsgesteente (magmatisch gesteente)",
                description = "Ontstaan door de afkoeling van magma. In de diepte geeft langzame afkoeling de kristallen tijd om te groeien (plutonisch gesteente, zoals graniet); aan het oppervlak geeft de snelle afkoeling van lava fijne kristallen, of zelfs vulkanisch glas zonder kristalstructuur. De diepst gevormde stenen stijgen soms via smalle vulkanische schoorstenen op uit de aardmantel.",
                especesTypiques = "Diamant (door kimberliet uit de mantel omhooggebracht), olivijn, zirkoon, moldaviet"
            ),
            RockOriginEntry(
                nom = "Pegmatiet",
                description = "Stollingsgesteente met een uitzonderlijk grove korrel, afkomstig van de allerlaatste magmazak die kristalliseert — rijk aan water en aan zeldzame chemische elementen die de gewone mineralen niet hebben opgenomen. Deze vloeibare omgeving geeft grote kristallen, soms van meerdere meters, de tijd om vrij te groeien: pegmatiet is de belangrijkste bron van verzamelaars-edelstenen.",
                especesTypiques = "Beryl (smaragd, aquamarijn, morganiet, heliodoor), toermalijn, topaas, kunziet, spodumeen, lepidoliet"
            ),
            RockOriginEntry(
                nom = "Metamorf gesteente",
                description = "Reeds bestaand gesteente (stollingsgesteente, sedimentair of al metamorf) dat in de diepte door hitte en druk wordt omgevormd zonder volledige smelting, waardoor de minerale structuur wordt gereorganiseerd. Regionale metamorfose treft uitgestrekte gebieden tijdens het ontstaan van gebergten; contactmetamorfose, meer plaatselijk, vindt plaats nabij een magmatische intrusie.",
                especesTypiques = "Robijn en saffier (metamorfe marmers en gneizen), granaat, jade (jadeiet), lapis lazuli, disteen"
            ),
            RockOriginEntry(
                nom = "Sedimentair gesteente",
                description = "Gevormd aan het oppervlak, zonder tussenkomst van diepe hitte: door ophoping en cementatie van deeltjes (zandsteen, kalksteen) of door neerslag van in water opgeloste mineralen. Chemische verwering van reeds bestaand gesteente in een droog klimaat kan ook bepaalde elementen concentreren en aanleiding geven tot secundaire afzettingen.",
                especesTypiques = "Opaal (kiezelzuur neergeslagen in breuken), turkoois, malachiet en azuriet (verweringszones van kopererzlagen), barnsteen (gefossiliseerde hars)"
            )
        )
    )

    private val byLanguage: Map<String, RochesMeresPage> = mapOf(
        "fr" to fr,
        "en" to en,
        "es" to es,
        "it" to it,
        "de" to de,
        "pt" to pt,
        "zh" to zh,
        "ru" to ru,
        "nl" to nl
    )

    fun page(languageCode: String): RochesMeresPage = byLanguage[languageCode] ?: fr
}
