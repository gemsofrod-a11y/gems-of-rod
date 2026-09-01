package fr.gemsofrod.encyclopedie.data

/**
 * Forme géométrique de référence utilisée pour l'esquisse de chaque système
 * (voir [fr.gemsofrod.encyclopedie.ui.screens.CrystalSystemsScreen]). Non
 * traduite : c'est un paramètre de dessin, pas un texte affiché.
 */
enum class CrystalSystemShape {
    CUBIQUE,
    QUADRATIQUE,
    HEXAGONAL,
    MONOCLINIQUE,
    ORTHORHOMBIQUE,
    TRICLINIQUE
}

data class CrystalSystemEntry(
    val name: String,
    val description: String,
    val examples: String,
    val shape: CrystalSystemShape
)

data class CrystalSystemsPage(
    val title: String,
    val intro: String,
    val disclaimerTitle: String,
    val disclaimerBody: String,
    val examplesLabel: String,
    val systems: List<CrystalSystemEntry>
)

/**
 * Les six systèmes cristallins, selon la classification traditionnelle qui
 * regroupe hexagonal et rhomboédrique (trigonal) en un seul système. Contenu
 * original, reformulé à partir de faits de cristallographie généraux (axes,
 * angles, éléments de symétrie, espèces minérales représentatives) — aucun
 * texte n'est repris d'un ouvrage existant.
 */
object CrystalSystemsInfo {
    private val fr = CrystalSystemsPage(
        title = "Systèmes cristallins",
        intro = "La structure atomique des cristaux s'organise selon des axes de symétrie caractéristiques. Selon la classification traditionnelle retenue ici, on distingue six systèmes cristallins : cubique, quadratique, hexagonal et rhomboédrique (regroupés en un seul système), monoclinique, orthorhombique et triclinique. Une forme simplifiée est illustrée pour chacun ; en réalité, un même système peut engendrer des habitus très variés.",
        disclaimerTitle = "Des formes idéalisées",
        disclaimerBody = "Ces esquisses représentent la géométrie théorique de chaque système, pas l'aspect réel des cristaux : macles, faces multiples, croissance irrégulière et altération modifient fortement l'habitus observé sur le terrain.",
        examplesLabel = "Exemples",
        systems = listOf(
            CrystalSystemEntry(
                name = "Cubique",
                description = "Trois axes de longueur égale, à angle droit les uns par rapport aux autres, et quatre axes de symétrie d'ordre trois. Les formes typiques comprennent le cube, l'octaèdre et le dodécaèdre rhombique.",
                examples = "Diamant, grenat, spinelle, pyrite, fluorine, or natif, galène",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Quadratique",
                description = "Trois axes à angle droit : deux de longueur égale et un troisième plus long ou plus court, avec un seul axe de symétrie d'ordre quatre. Les cristaux évoquent des prismes à base carrée.",
                examples = "Zircon, rutile, cassitérite, chalcopyrite, wulfénite",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonal et rhomboédrique",
                description = "Trois axes horizontaux de longueur égale, disposés à 120° les uns des autres, et un quatrième axe vertical perpendiculaire à ce plan. Le système hexagonal présente une symétrie d'ordre six, le système rhomboédrique (ou trigonal) une symétrie d'ordre trois.",
                examples = "Béryl (émeraude, aigue-marine), apatite, quartz, calcite, tourmaline, corindon (rubis, saphir)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoclinique",
                description = "Trois axes de longueur inégale : l'un est perpendiculaire aux deux autres, qui eux ne le sont pas entre eux. Un seul axe de symétrie d'ordre deux. C'est le système le plus fréquent parmi les minéraux.",
                examples = "Gypse, orthose, muscovite, jadéite, azurite, malachite",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Orthorhombique",
                description = "Trois axes de longueur inégale, tous perpendiculaires entre eux, avec trois axes de symétrie d'ordre deux.",
                examples = "Topaze, péridot, aragonite, barytine, marcassite",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triclinique",
                description = "Trois axes de longueur inégale, dont aucun n'est perpendiculaire aux deux autres — la symétrie la plus faible de tous les systèmes cristallins.",
                examples = "Plagioclases (albite, anorthite), disthène, amazonite (microcline), turquoise",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val en = CrystalSystemsPage(
        title = "Crystal systems",
        intro = "The atomic structure of crystals is organized around characteristic axes of symmetry. Under the traditional classification used here, crystals fall into six systems: cubic, tetragonal, hexagonal and rhombohedral (grouped here as a single system), monoclinic, orthorhombic, and triclinic. A simplified sketch illustrates each one; in practice, a single system can still produce very different habits.",
        disclaimerTitle = "Idealized shapes",
        disclaimerBody = "These sketches show the theoretical geometry of each system, not how real crystals actually look: twinning, extra faces, irregular growth, and weathering all strongly affect the habit seen in the field.",
        examplesLabel = "Examples",
        systems = listOf(
            CrystalSystemEntry(
                name = "Cubic",
                description = "Three axes of equal length, at right angles to one another, with four three-fold symmetry axes. Typical forms include the cube, the octahedron, and the rhombic dodecahedron.",
                examples = "Diamond, garnet, spinel, pyrite, fluorite, native gold, galena",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonal",
                description = "Three axes at right angles: two of equal length and a third that is longer or shorter, with a single four-fold symmetry axis. Crystals resemble square-based prisms.",
                examples = "Zircon, rutile, cassiterite, chalcopyrite, wulfenite",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonal and rhombohedral",
                description = "Three horizontal axes of equal length set 120° apart, plus a fourth vertical axis perpendicular to that plane. The hexagonal system has six-fold symmetry; the rhombohedral (or trigonal) system has three-fold symmetry.",
                examples = "Beryl (emerald, aquamarine), apatite, quartz, calcite, tourmaline, corundum (ruby, sapphire)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoclinic",
                description = "Three unequal axes: one is perpendicular to the other two, which are not perpendicular to each other. A single two-fold symmetry axis. This is the most common system among minerals.",
                examples = "Gypsum, orthoclase, muscovite, jadeite, azurite, malachite",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Orthorhombic",
                description = "Three unequal axes, all mutually perpendicular, with three two-fold symmetry axes.",
                examples = "Topaz, peridot, aragonite, barite, marcasite",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triclinic",
                description = "Three unequal axes, none of them perpendicular to the other two — the lowest symmetry of any crystal system.",
                examples = "Plagioclase feldspars (albite, anorthite), kyanite, amazonite (microcline), turquoise",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val es = CrystalSystemsPage(
        title = "Sistemas cristalinos",
        intro = "La estructura atómica de los cristales se organiza según ejes de simetría característicos. Según la clasificación tradicional aquí utilizada, los cristales se agrupan en seis sistemas: cúbico, tetragonal, hexagonal y romboédrico (agrupados aquí como un solo sistema), monoclínico, ortorrómbico y triclínico. Se ilustra una forma simplificada de cada uno; en la práctica, un mismo sistema puede dar lugar a hábitos muy variados.",
        disclaimerTitle = "Formas idealizadas",
        disclaimerBody = "Estos esquemas muestran la geometría teórica de cada sistema, no el aspecto real de los cristales: el maclado, las caras adicionales, el crecimiento irregular y la alteración modifican mucho el hábito observado en el campo.",
        examplesLabel = "Ejemplos",
        systems = listOf(
            CrystalSystemEntry(
                name = "Cúbico",
                description = "Tres ejes de igual longitud, perpendiculares entre sí, con cuatro ejes de simetría de orden tres. Las formas típicas incluyen el cubo, el octaedro y el dodecaedro rómbico.",
                examples = "Diamante, granate, espinela, pirita, fluorita, oro nativo, galena",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonal",
                description = "Tres ejes en ángulo recto: dos de igual longitud y un tercero más largo o más corto, con un único eje de simetría de orden cuatro. Los cristales recuerdan prismas de base cuadrada.",
                examples = "Circón, rutilo, casiterita, calcopirita, wulfenita",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonal y romboédrico",
                description = "Tres ejes horizontales de igual longitud dispuestos a 120° entre sí, más un cuarto eje vertical perpendicular a ese plano. El sistema hexagonal presenta simetría de orden seis; el romboédrico (o trigonal), de orden tres.",
                examples = "Berilo (esmeralda, aguamarina), apatito, cuarzo, calcita, turmalina, corindón (rubí, zafiro)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoclínico",
                description = "Tres ejes de longitud desigual: uno es perpendicular a los otros dos, que no lo son entre sí. Un único eje de simetría de orden dos. Es el sistema más frecuente entre los minerales.",
                examples = "Yeso, ortoclasa, moscovita, jadeíta, azurita, malaquita",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Ortorrómbico",
                description = "Tres ejes de longitud desigual, todos perpendiculares entre sí, con tres ejes de simetría de orden dos.",
                examples = "Topacio, peridoto, aragonito, baritina, marcasita",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triclínico",
                description = "Tres ejes de longitud desigual, ninguno perpendicular a los otros dos: la simetría más baja de todos los sistemas cristalinos.",
                examples = "Feldespatos plagioclasas (albita, anortita), cianita, amazonita (microclina), turquesa",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val it = CrystalSystemsPage(
        title = "Sistemi cristallini",
        intro = "La struttura atomica dei cristalli si organizza secondo assi di simmetria caratteristici. Nella classificazione tradizionale qui adottata, i cristalli si suddividono in sei sistemi: cubico, tetragonale, esagonale e romboedrico (qui raggruppati in un unico sistema), monoclino, ortorombico e triclino. Per ciascuno è illustrata una forma semplificata; nella pratica, uno stesso sistema può dare origine ad abiti molto diversi.",
        disclaimerTitle = "Forme idealizzate",
        disclaimerBody = "Questi schizzi mostrano la geometria teorica di ciascun sistema, non l'aspetto reale dei cristalli: geminazione, facce aggiuntive, crescita irregolare e alterazione modificano fortemente l'abito osservato sul campo.",
        examplesLabel = "Esempi",
        systems = listOf(
            CrystalSystemEntry(
                name = "Cubico",
                description = "Tre assi di uguale lunghezza, perpendicolari tra loro, con quattro assi di simmetria di ordine tre. Le forme tipiche comprendono il cubo, l'ottaedro e il dodecaedro romboidale.",
                examples = "Diamante, granato, spinello, pirite, fluorite, oro nativo, galena",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonale",
                description = "Tre assi ad angolo retto: due di uguale lunghezza e un terzo più lungo o più corto, con un unico asse di simmetria di ordine quattro. I cristalli ricordano prismi a base quadrata.",
                examples = "Zircone, rutilo, cassiterite, calcopirite, wulfenite",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Esagonale e romboedrico",
                description = "Tre assi orizzontali di uguale lunghezza disposti a 120° l'uno dall'altro, più un quarto asse verticale perpendicolare a quel piano. Il sistema esagonale ha simmetria di ordine sei; il romboedrico (o trigonale), di ordine tre.",
                examples = "Berillo (smeraldo, acquamarina), apatite, quarzo, calcite, tormalina, corindone (rubino, zaffiro)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoclino",
                description = "Tre assi di lunghezza diseguale: uno è perpendicolare agli altri due, che non lo sono tra loro. Un unico asse di simmetria di ordine due. È il sistema più diffuso tra i minerali.",
                examples = "Gesso, ortoclasio, muscovite, giadeite, azzurrite, malachite",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Ortorombico",
                description = "Tre assi di lunghezza diseguale, tutti perpendicolari tra loro, con tre assi di simmetria di ordine due.",
                examples = "Topazio, peridoto, aragonite, barite, marcasite",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triclino",
                description = "Tre assi di lunghezza diseguale, nessuno perpendicolare agli altri due: la simmetria più bassa fra tutti i sistemi cristallini.",
                examples = "Plagioclasi (albite, anortite), cianite, amazzonite (microclino), turchese",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val de = CrystalSystemsPage(
        title = "Kristallsysteme",
        intro = "Der atomare Aufbau von Kristallen richtet sich nach charakteristischen Symmetrieachsen. Nach der hier verwendeten traditionellen Einteilung unterscheidet man sechs Kristallsysteme: kubisch, tetragonal, hexagonal und rhomboedrisch (hier als ein System zusammengefasst), monoklin, orthorhombisch und triklin. Für jedes System ist eine vereinfachte Form dargestellt; in der Praxis kann ein und dasselbe System sehr unterschiedliche Kristalltrachten hervorbringen.",
        disclaimerTitle = "Idealisierte Formen",
        disclaimerBody = "Diese Skizzen zeigen die theoretische Geometrie jedes Systems, nicht das tatsächliche Aussehen realer Kristalle: Verzwilligung, zusätzliche Flächen, unregelmäßiges Wachstum und Verwitterung verändern die im Gelände beobachtete Tracht stark.",
        examplesLabel = "Beispiele",
        systems = listOf(
            CrystalSystemEntry(
                name = "Kubisch",
                description = "Drei gleich lange Achsen, rechtwinklig zueinander, mit vier dreizähligen Symmetrieachsen. Typische Formen sind der Würfel, das Oktaeder und das Rhombendodekaeder.",
                examples = "Diamant, Granat, Spinell, Pyrit, Fluorit, gediegen Gold, Bleiglanz",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonal",
                description = "Drei rechtwinklige Achsen: zwei gleich lang, eine dritte länger oder kürzer, mit einer einzigen vierzähligen Symmetrieachse. Die Kristalle erinnern an quadratische Prismen.",
                examples = "Zirkon, Rutil, Kassiterit, Chalkopyrit, Wulfenit",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonal und rhomboedrisch",
                description = "Drei gleich lange horizontale Achsen im Winkel von 120° zueinander sowie eine vierte, zu dieser Ebene senkrechte Achse. Das hexagonale System besitzt sechszählige Symmetrie, das rhomboedrische (oder trigonale) System dreizählige Symmetrie.",
                examples = "Beryll (Smaragd, Aquamarin), Apatit, Quarz, Calcit, Turmalin, Korund (Rubin, Saphir)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoklin",
                description = "Drei ungleich lange Achsen: eine steht senkrecht auf den beiden anderen, die untereinander nicht senkrecht stehen. Eine einzige zweizählige Symmetrieachse. Dies ist das unter Mineralen häufigste System.",
                examples = "Gips, Orthoklas, Muskovit, Jadeit, Azurit, Malachit",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Orthorhombisch",
                description = "Drei ungleich lange Achsen, alle rechtwinklig zueinander, mit drei zweizähligen Symmetrieachsen.",
                examples = "Topas, Peridot, Aragonit, Baryt, Markasit",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triklin",
                description = "Drei ungleich lange Achsen, von denen keine senkrecht zu den beiden anderen steht — die niedrigste Symmetrie aller Kristallsysteme.",
                examples = "Plagioklase (Albit, Anorthit), Disthen, Amazonit (Mikroklin), Türkis",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val pt = CrystalSystemsPage(
        title = "Sistemas cristalinos",
        intro = "A estrutura atômica dos cristais se organiza segundo eixos de simetria característicos. Segundo a classificação tradicional aqui utilizada, os cristais dividem-se em seis sistemas: cúbico, tetragonal, hexagonal e romboédrico (aqui agrupados em um único sistema), monoclínico, ortorrômbico e triclínico. Uma forma simplificada é ilustrada para cada um; na prática, um mesmo sistema pode dar origem a hábitos muito variados.",
        disclaimerTitle = "Formas idealizadas",
        disclaimerBody = "Estes esboços mostram a geometria teórica de cada sistema, não o aspecto real dos cristais: maclas, faces adicionais, crescimento irregular e alteração modificam fortemente o hábito observado em campo.",
        examplesLabel = "Exemplos",
        systems = listOf(
            CrystalSystemEntry(
                name = "Cúbico",
                description = "Três eixos de igual comprimento, perpendiculares entre si, com quatro eixos de simetria de ordem três. As formas típicas incluem o cubo, o octaedro e o dodecaedro rômbico.",
                examples = "Diamante, granada, espinélio, pirita, fluorita, ouro nativo, galena",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonal",
                description = "Três eixos em ângulo reto: dois de igual comprimento e um terceiro mais longo ou mais curto, com um único eixo de simetria de ordem quatro. Os cristais lembram prismas de base quadrada.",
                examples = "Zircão, rutilo, cassiterita, calcopirita, wulfenita",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonal e romboédrico",
                description = "Três eixos horizontais de igual comprimento dispostos a 120° entre si, mais um quarto eixo vertical perpendicular a esse plano. O sistema hexagonal tem simetria de ordem seis; o romboédrico (ou trigonal), de ordem três.",
                examples = "Berilo (esmeralda, água-marinha), apatita, quartzo, calcita, turmalina, coríndon (rubi, safira)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoclínico",
                description = "Três eixos de comprimento desigual: um é perpendicular aos outros dois, que não são perpendiculares entre si. Um único eixo de simetria de ordem dois. É o sistema mais comum entre os minerais.",
                examples = "Gipsita, ortoclásio, muscovita, jadeíta, azurita, malaquita",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Ortorrômbico",
                description = "Três eixos de comprimento desigual, todos perpendiculares entre si, com três eixos de simetria de ordem dois.",
                examples = "Topázio, peridoto, aragonita, barita, marcassita",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triclínico",
                description = "Três eixos de comprimento desigual, nenhum perpendicular aos outros dois — a menor simetria entre todos os sistemas cristalinos.",
                examples = "Plagioclásios (albita, anortita), cianita, amazonita (microclina), turquesa",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val ru = CrystalSystemsPage(
        title = "Кристаллические сингонии",
        intro = "Атомная структура кристаллов организована вокруг характерных осей симметрии. Согласно традиционной классификации, использованной здесь, кристаллы делятся на шесть систем: кубическая, тетрагональная, гексагональная и ромбоэдрическая (здесь объединены в одну систему), моноклинная, ромбическая и триклинная. Для каждой приведена упрощённая схема; на практике одна и та же система может порождать самые разные габитусы.",
        disclaimerTitle = "Идеализированные формы",
        disclaimerBody = "Эти схемы показывают теоретическую геометрию каждой системы, а не реальный вид кристаллов: двойникование, дополнительные грани, неравномерный рост и выветривание сильно меняют облик, наблюдаемый в природе.",
        examplesLabel = "Примеры",
        systems = listOf(
            CrystalSystemEntry(
                name = "Кубическая",
                description = "Три оси равной длины, взаимно перпендикулярные, с четырьмя осями симметрии третьего порядка. Типичные формы — куб, октаэдр и ромбододекаэдр.",
                examples = "Алмаз, гранат, шпинель, пирит, флюорит, самородное золото, галенит",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Тетрагональная",
                description = "Три оси под прямым углом: две равной длины и третья длиннее или короче, с одной осью симметрии четвёртого порядка. Кристаллы напоминают призмы с квадратным основанием.",
                examples = "Циркон, рутил, касситерит, халькопирит, вульфенит",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Гексагональная и ромбоэдрическая",
                description = "Три равные горизонтальные оси, расположенные под углом 120° друг к другу, и четвёртая вертикальная ось, перпендикулярная этой плоскости. Гексагональная система обладает симметрией шестого порядка, ромбоэдрическая (тригональная) — третьего.",
                examples = "Берилл (изумруд, аквамарин), апатит, кварц, кальцит, турмалин, корунд (рубин, сапфир)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Моноклинная",
                description = "Три оси неравной длины: одна перпендикулярна двум другим, которые не перпендикулярны друг другу. Единственная ось симметрии второго порядка. Это самая распространённая система среди минералов.",
                examples = "Гипс, ортоклаз, мусковит, жадеит, азурит, малахит",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Ромбическая",
                description = "Три оси неравной длины, все взаимно перпендикулярны, с тремя осями симметрии второго порядка.",
                examples = "Топаз, перидот, арагонит, барит, марказит",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Триклинная",
                description = "Три оси неравной длины, ни одна из которых не перпендикулярна двум другим — самая низкая симметрия среди всех кристаллических систем.",
                examples = "Плагиоклазы (альбит, анортит), кианит, амазонит (микроклин), бирюза",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val nl = CrystalSystemsPage(
        title = "Kristalsystemen",
        intro = "De atomaire structuur van kristallen is opgebouwd rond karakteristieke symmetrieassen. Volgens de hier gebruikte traditionele indeling worden kristallen ingedeeld in zes systemen: kubisch, tetragonaal, hexagonaal en romboëdrisch (hier samengevoegd tot één systeem), monoklien, orthorombisch en triklien. Voor elk systeem is een vereenvoudigde vorm afgebeeld; in de praktijk kan hetzelfde systeem zeer uiteenlopende habitussen opleveren.",
        disclaimerTitle = "Geïdealiseerde vormen",
        disclaimerBody = "Deze schetsen tonen de theoretische geometrie van elk systeem, niet het werkelijke uiterlijk van kristallen: tweelingvorming, extra vlakken, onregelmatige groei en verwering veranderen de in het veld waargenomen habitus sterk.",
        examplesLabel = "Voorbeelden",
        systems = listOf(
            CrystalSystemEntry(
                name = "Kubisch",
                description = "Drie even lange assen, loodrecht op elkaar, met vier drietallige symmetrieassen. Typische vormen zijn de kubus, het octaëder en het rombendodecaëder.",
                examples = "Diamant, granaat, spinel, pyriet, fluoriet, gedegen goud, galeniet",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "Tetragonaal",
                description = "Drie assen loodrecht op elkaar: twee even lang en een derde langer of korter, met één viertallige symmetrieas. De kristallen doen denken aan prisma's met een vierkant grondvlak.",
                examples = "Zirkoon, rutiel, cassiteriet, chalcopyriet, wulfeniet",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "Hexagonaal en romboëdrisch",
                description = "Drie even lange horizontale assen die 120° met elkaar maken, plus een vierde verticale as loodrecht op dat vlak. Het hexagonale systeem heeft zestallige symmetrie, het romboëdrische (of trigonale) systeem drietallige symmetrie.",
                examples = "Beryl (smaragd, aquamarijn), apatiet, kwarts, calciet, toermalijn, korund (robijn, saffier)",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "Monoklien",
                description = "Drie ongelijk lange assen: één staat loodrecht op de andere twee, die onderling niet loodrecht staan. Eén enkele tweetallige symmetrieas. Dit is het meest voorkomende systeem onder mineralen.",
                examples = "Gips, orthoklaas, muscoviet, jadeiet, azuriet, malachiet",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "Orthorombisch",
                description = "Drie ongelijk lange assen, alle loodrecht op elkaar, met drie tweetallige symmetrieassen.",
                examples = "Topaas, peridoot, aragoniet, bariet, marcasiet",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "Triklien",
                description = "Drie ongelijk lange assen, waarvan geen enkele loodrecht op de andere twee staat — de laagste symmetrie van alle kristalsystemen.",
                examples = "Plagioklazen (albiet, anorthiet), kyaniet, amazoniet (microklien), turkoois",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val zh = CrystalSystemsPage(
        title = "晶系",
        intro = "晶体的原子结构围绕特有的对称轴排列。按照本页采用的传统分类法，晶体分为六个晶系：等轴晶系、四方晶系、六方晶系与三方晶系（在此归为一个晶系）、单斜晶系、斜方晶系和三斜晶系。下面为每个晶系配有一幅简化示意图；实际上，同一晶系仍可呈现出差异很大的晶习。",
        disclaimerTitle = "理想化的形态",
        disclaimerBody = "以下示意图展示的是各晶系的理论几何形态，并非真实晶体的外观：双晶、多面发育、不规则生长和风化都会显著改变野外观察到的晶习。",
        examplesLabel = "示例",
        systems = listOf(
            CrystalSystemEntry(
                name = "等轴晶系",
                description = "三条长度相等、彼此垂直的晶轴，并具有四条三重对称轴。典型晶形包括立方体、八面体和菱形十二面体。",
                examples = "钻石、石榴石、尖晶石、黄铁矿、萤石、自然金、方铅矿",
                shape = CrystalSystemShape.CUBIQUE
            ),
            CrystalSystemEntry(
                name = "四方晶系",
                description = "三条相互垂直的晶轴：其中两条长度相等，第三条更长或更短，并具有一条四重对称轴。晶体形似方柱。",
                examples = "锆石、金红石、锡石、黄铜矿、钼铅矿",
                shape = CrystalSystemShape.QUADRATIQUE
            ),
            CrystalSystemEntry(
                name = "六方晶系与三方晶系",
                description = "三条水平晶轴长度相等，彼此成120°角，另有一条垂直晶轴与该平面垂直。六方晶系具有六重对称，三方晶系具有三重对称。",
                examples = "绿柱石（祖母绿、海蓝宝石）、磷灰石、石英、方解石、碧玺、刚玉（红宝石、蓝宝石）",
                shape = CrystalSystemShape.HEXAGONAL
            ),
            CrystalSystemEntry(
                name = "单斜晶系",
                description = "三条长度不等的晶轴：其中一条与另外两条垂直，而那两条彼此并不垂直。仅有一条二重对称轴。这是矿物中最常见的晶系。",
                examples = "石膏、正长石、白云母、硬玉、蓝铜矿、孔雀石",
                shape = CrystalSystemShape.MONOCLINIQUE
            ),
            CrystalSystemEntry(
                name = "斜方晶系",
                description = "三条长度不等、彼此相互垂直的晶轴，具有三条二重对称轴。",
                examples = "黄玉、橄榄石、文石、重晶石、白铁矿",
                shape = CrystalSystemShape.ORTHORHOMBIQUE
            ),
            CrystalSystemEntry(
                name = "三斜晶系",
                description = "三条长度不等的晶轴，彼此均不垂直——是所有晶系中对称性最低的一种。",
                examples = "斜长石（钠长石、钙长石）、蓝晶石、天河石（微斜长石）、绿松石",
                shape = CrystalSystemShape.TRICLINIQUE
            )
        )
    )

    private val byLanguage: Map<String, CrystalSystemsPage> = mapOf(
        "fr" to fr, "en" to en, "es" to es, "it" to it, "de" to de,
        "pt" to pt, "ru" to ru, "nl" to nl, "zh" to zh
    )

    fun page(languageCode: String): CrystalSystemsPage = byLanguage[languageCode] ?: fr
}
