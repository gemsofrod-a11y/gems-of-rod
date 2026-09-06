package fr.gemsofrod.encyclopedie.data

data class GemGlossaryTerm(
    val terme: String,
    val definition: String
)

data class GemGlossaryPage(
    val intro: String,
    val termes: List<GemGlossaryTerm>
)

/**
 * Lexique gemmologique statique expliquant en langage clair le vocabulaire
 * technique employé dans les fiches d'inclusions typiques (soie, halo de
 * tension, trichites...) et dans les champs de l'outil "Analyse de pierre"
 * (biréfringence, pléochroïsme, clivage...), traduit dans les 5 langues de
 * l'app indépendamment des fiches gemmes. Les termes sont classés par ordre
 * alphabétique de leur libellé français.
 */
object GemGlossary {
    private val fr = GemGlossaryPage(
        intro = "Le vocabulaire de la gemmologie peut sembler technique au premier abord. Ce lexique explique en langage clair les termes les plus courants — ceux utilisés dans les fiches d'inclusions typiques et dans l'outil d'analyse — pour mieux comprendre ce que révèle l'observation d'une pierre.",
        termes = listOf(
            GemGlossaryTerm("Adularescence", "Reflet bleuté et flottant qui semble se déplacer sous la surface d'une pierre, dû à la diffraction de la lumière entre de fines lamelles internes ; caractéristique de la pierre de lune."),
            GemGlossaryTerm("Agrégat botryoïdal", "Assemblage de petites sphères soudées entre elles, en forme de grappe de raisin, typique de certains minéraux formés en surface comme la smithsonite ou l'hémimorphite."),
            GemGlossaryTerm("Astérisme", "Étoile lumineuse à quatre, six ou douze branches qui se déplace à la surface d'une pierre taillée en cabochon, provoquée par la réflexion de la lumière sur des inclusions fibreuses orientées selon les axes du cristal."),
            GemGlossaryTerm("Aventurescence", "Scintillement produit par la réflexion de la lumière sur de fines paillettes minérales orientées à l'intérieur de la pierre, comme dans l'aventurine ou la pierre de soleil."),
            GemGlossaryTerm("Biréfringence", "Propriété de certains cristaux à dédoubler un rayon lumineux qui les traverse en deux rayons distincts ; une forte biréfringence provoque un dédoublement visible des arêtes internes à la loupe."),
            GemGlossaryTerm("Chatoyance (effet œil-de-chat)", "Bande lumineuse mobile qui traverse la surface d'une pierre taillée en cabochon, causée par la réflexion de la lumière sur des inclusions fibreuses ou des canaux parallèles."),
            GemGlossaryTerm("Clivage", "Tendance d'un cristal à se fendre selon des plans plats et réguliers, liés à sa structure atomique ; un clivage parfait rend une pierre plus délicate à tailler et à porter."),
            GemGlossaryTerm("Cristal négatif", "Cavité interne vide ou remplie de fluide, dont la forme reproduit exactement la géométrie du système cristallin de la pierre hôte."),
            GemGlossaryTerm("Densité", "Rapport entre la masse d'une pierre et celle d'un même volume d'eau ; mesurée à la balance hydrostatique, c'est un critère très discriminant entre espèces d'apparence proche."),
            GemGlossaryTerm("Dispersion", "Capacité d'une pierre à décomposer la lumière blanche en ses couleurs spectrales, perçue comme des éclats colorés (le « feu ») ; particulièrement forte chez le diamant et la sphène."),
            GemGlossaryTerm("Éclat", "Aspect de la lumière réfléchie par la surface d'une pierre (vitreux, adamantin, soyeux, gras, résineux, nacré...), qui dépend de son indice de réfraction et de son poli."),
            GemGlossaryTerm("Empreinte digitale (inclusion de guérison)", "Réseau de fines inclusions fluides en volutes, ressemblant à une empreinte digitale, formé par la cicatrisation partielle d'une fracture interne pendant la croissance du cristal."),
            GemGlossaryTerm("Exsolution (lamelles d'exsolution)", "Fines lamelles internes formées lorsque deux minéraux mélangés à haute température se séparent en refroidissant ; à l'origine d'effets optiques comme l'adularescence ou la labradorescence."),
            GemGlossaryTerm("Fluorescence", "Émission de lumière visible par une pierre exposée aux rayons ultraviolets, dont la couleur et l'intensité aident à identifier une espèce ou un traitement."),
            GemGlossaryTerm("Halo de tension", "Fine fissure circulaire qui se forme autour d'une inclusion cristalline sous l'effet des contraintes internes exercées pendant la croissance de la pierre hôte."),
            GemGlossaryTerm("Indice de réfraction", "Mesure de la déviation de la lumière en pénétrant dans une pierre, caractéristique de chaque espèce et mesurée au réfractomètre."),
            GemGlossaryTerm("Inclusion", "Tout élément étranger (cristal, fluide, gaz, fracture) emprisonné à l'intérieur d'une pierre pendant sa formation ; son étude est l'un des principaux outils d'identification et d'authentification en gemmologie."),
            GemGlossaryTerm("Jardin", "Terme employé pour désigner l'ensemble des inclusions visibles d'une émeraude, dont la richesse et la nature aident à déterminer son origine géographique."),
            GemGlossaryTerm("Labradorescence", "Jeu de couleurs métalliques (bleu, vert, orangé) qui apparaît en faisant pivoter la pierre, dû à la diffraction de la lumière sur de fines lamelles internes ; caractéristique de la labradorite."),
            GemGlossaryTerm("Macle", "Association de deux ou plusieurs cristaux de la même espèce, orientés selon une règle géométrique précise, formant un cristal composite unique."),
            GemGlossaryTerm("Magmatique (roche)", "Roche née du refroidissement d'un magma, en profondeur (roche plutonique, à gros grains) ou en surface (roche volcanique, à grains fins) ; berceau du diamant, du péridot et du zircon."),
            GemGlossaryTerm("Métamicte", "Se dit d'un cristal dont la structure atomique a été partiellement désorganisée par la désintégration radioactive d'éléments traces qu'il contient, comme certains zircons."),
            GemGlossaryTerm("Métamorphique (roche)", "Roche préexistante transformée en profondeur par la chaleur et la pression sans fusion complète ; berceau du rubis, du saphir, du grenat et du jade."),
            GemGlossaryTerm("Pegmatite", "Roche magmatique à grain exceptionnellement gros, issue de la dernière poche de magma riche en eau à cristalliser ; principale source des pierres fines de collection (béryl, tourmaline, topaze...)."),
            GemGlossaryTerm("Pléochroïsme", "Propriété de certaines pierres à montrer des couleurs différentes selon l'angle d'observation, due à l'absorption inégale de la lumière selon les axes du cristal."),
            GemGlossaryTerm("Sédimentaire (roche)", "Roche formée en surface par accumulation de particules ou précipitation de minéraux dissous dans l'eau, sans chaleur profonde ; berceau de l'opale et de la turquoise."),
            GemGlossaryTerm("Soie", "Fines inclusions aciculaires (souvent de rutile), disposées en réseaux denses, qui donnent un aspect soyeux à la pierre et sont à l'origine de l'astérisme lorsqu'elles sont orientées selon plusieurs axes."),
            GemGlossaryTerm("Structure fibro-radiée", "Assemblage de fibres minérales disposées en éventail à partir d'un point central, donnant un aspect satiné ou soyeux à la pierre."),
            GemGlossaryTerm("Système cristallin", "L'une des sept familles géométriques (cubique, quadratique, hexagonal, trigonal, orthorhombique, monoclinique, triclinique) selon lesquelles s'organise la structure atomique d'un cristal."),
            GemGlossaryTerm("Ténébrescence", "Changement de couleur réversible d'une pierre exposée puis soustraite à la lumière (ou aux UV), observé notamment chez la hackmanite."),
            GemGlossaryTerm("Transparence", "Degré auquel la lumière traverse une pierre, du transparent (image nette à travers la pierre) au translucide (lumière diffusée) jusqu'à l'opaque (aucune lumière transmise)."),
            GemGlossaryTerm("Trichite", "Inclusion liquide fragmentée en fins filaments ramifiés, évoquant des fils de coton, fréquente dans les tourmalines."),
            GemGlossaryTerm("Tube creux (canal de croissance)", "Fine cavité allongée, parallèle à l'axe de croissance du cristal, parfois remplie de liquide ; caractéristique de nombreux béryls."),
            GemGlossaryTerm("Zonage de couleur", "Répartition inégale de la couleur à l'intérieur d'une pierre, organisée en bandes ou en secteurs qui suivent la géométrie de croissance du cristal.")
        )
    )

    private val en = GemGlossaryPage(
        intro = "Gemological vocabulary can seem technical at first. This glossary explains, in plain language, the most common terms — the ones used in the typical-inclusions notes and in the analysis tool — to help you better understand what examining a stone reveals.",
        termes = listOf(
            GemGlossaryTerm("Adularescence", "A floating, bluish sheen that seems to move beneath a stone's surface, caused by light diffracting between fine internal layers; characteristic of moonstone."),
            GemGlossaryTerm("Botryoidal aggregate", "A cluster of small fused spheres resembling a bunch of grapes, typical of certain minerals formed near the surface, such as smithsonite or hemimorphite."),
            GemGlossaryTerm("Asterism", "A luminous four-, six-, or twelve-rayed star that moves across the surface of a cabochon-cut stone, caused by light reflecting off fibrous inclusions aligned with the crystal's axes."),
            GemGlossaryTerm("Aventurescence", "A sparkle produced by light reflecting off fine mineral platelets oriented inside the stone, as in aventurine or sunstone."),
            GemGlossaryTerm("Birefringence", "The property of certain crystals to split a light ray passing through them into two distinct rays; strong birefringence causes a visible doubling of internal facet edges under magnification."),
            GemGlossaryTerm("Chatoyancy (cat's-eye effect)", "A moving band of light crossing the surface of a cabochon-cut stone, caused by light reflecting off parallel fibrous inclusions or channels."),
            GemGlossaryTerm("Cleavage", "A crystal's tendency to split along flat, regular planes related to its atomic structure; perfect cleavage makes a stone more delicate to cut and wear."),
            GemGlossaryTerm("Negative crystal", "An internal cavity, empty or fluid-filled, whose shape exactly mirrors the crystal system of the host stone."),
            GemGlossaryTerm("Specific gravity (density)", "The ratio between a stone's mass and that of an equal volume of water; measured with a hydrostatic balance, it is a highly discriminating criterion between species that look alike."),
            GemGlossaryTerm("Dispersion", "A stone's ability to split white light into its spectral colours, perceived as flashes of colour (\"fire\"); particularly strong in diamond and sphene."),
            GemGlossaryTerm("Luster", "The appearance of light reflected off a stone's surface (vitreous, adamantine, silky, greasy, resinous, pearly...), which depends on its refractive index and polish."),
            GemGlossaryTerm("Fingerprint inclusion (healing feature)", "A network of fine, swirling fluid inclusions resembling a fingerprint, formed by the partial healing of an internal fracture during the crystal's growth."),
            GemGlossaryTerm("Exsolution (exsolution lamellae)", "Fine internal layers formed when two minerals mixed at high temperature separate on cooling; the origin of optical effects such as adularescence or labradorescence."),
            GemGlossaryTerm("Fluorescence", "The emission of visible light by a stone exposed to ultraviolet rays, whose colour and intensity help identify a species or a treatment."),
            GemGlossaryTerm("Tension halo (stress halo)", "A fine circular crack that forms around an included crystal under the internal stress exerted during the host stone's growth."),
            GemGlossaryTerm("Refractive index", "A measure of how much light bends on entering a stone, characteristic of each species and measured with a refractometer."),
            GemGlossaryTerm("Inclusion", "Any foreign material (crystal, fluid, gas, fracture) trapped inside a stone during its formation; studying inclusions is one of the main tools for identification and authentication in gemology."),
            GemGlossaryTerm("Jardin (\"garden\")", "Term used for the overall inclusions visible in an emerald, whose richness and nature help determine its geographic origin."),
            GemGlossaryTerm("Labradorescence", "A play of metallic colours (blue, green, orange) that appears when the stone is turned, caused by light diffracting off fine internal layers; characteristic of labradorite."),
            GemGlossaryTerm("Twinning", "The intergrowth of two or more crystals of the same species, joined according to a precise geometric rule, forming a single composite crystal."),
            GemGlossaryTerm("Igneous rock", "Rock formed by the cooling of magma, either at depth (plutonic rock, coarse-grained) or at the surface (volcanic rock, fine-grained); the birthplace of diamond, peridot, and zircon."),
            GemGlossaryTerm("Metamict", "Describes a crystal whose atomic structure has been partially disrupted by the radioactive decay of trace elements it contains, as in some zircons."),
            GemGlossaryTerm("Metamorphic rock", "A pre-existing rock transformed at depth by heat and pressure without complete melting; the birthplace of ruby, sapphire, garnet, and jade."),
            GemGlossaryTerm("Pegmatite", "An exceptionally coarse-grained igneous rock formed from the last, water-rich pocket of magma to crystallize; the main source of collectible gemstones (beryl, tourmaline, topaz...)."),
            GemGlossaryTerm("Pleochroism", "The property of certain stones to show different colours depending on the viewing angle, due to uneven light absorption along the crystal's axes."),
            GemGlossaryTerm("Sedimentary rock", "Rock formed at the surface by the accumulation of particles or the precipitation of minerals dissolved in water, without deep heat; the birthplace of opal and turquoise."),
            GemGlossaryTerm("Silk", "Fine needle-like inclusions (often rutile), arranged in dense networks, which give a stone a silky appearance and cause asterism when aligned along several axes."),
            GemGlossaryTerm("Fibro-radiating structure", "An assembly of mineral fibres arranged in a fan from a central point, giving the stone a satiny or silky appearance."),
            GemGlossaryTerm("Crystal system", "One of the seven geometric families (cubic, tetragonal, hexagonal, trigonal, orthorhombic, monoclinic, triclinic) by which a crystal's atomic structure is organised."),
            GemGlossaryTerm("Tenebrescence", "A reversible colour change in a stone exposed to, then removed from, light (or UV), notably seen in hackmanite."),
            GemGlossaryTerm("Transparency", "The degree to which light passes through a stone, from transparent (a clear image seen through the stone) to translucent (scattered light) to opaque (no light transmitted)."),
            GemGlossaryTerm("Trichite", "A liquid inclusion broken up into fine branching threads, resembling cotton fibres, common in tourmalines."),
            GemGlossaryTerm("Hollow tube (growth channel)", "A fine elongated cavity parallel to the crystal's growth axis, sometimes liquid-filled; characteristic of many beryls."),
            GemGlossaryTerm("Colour zoning", "An uneven distribution of colour inside a stone, arranged in bands or sectors that follow the crystal's growth geometry.")
        )
    )

    private val es = GemGlossaryPage(
        intro = "El vocabulario gemológico puede parecer técnico a primera vista. Este glosario explica en un lenguaje claro los términos más habituales —los usados en las fichas de inclusiones típicas y en la herramienta de análisis— para comprender mejor lo que revela la observación de una piedra.",
        termes = listOf(
            GemGlossaryTerm("Adularescencia", "Reflejo azulado y flotante que parece desplazarse bajo la superficie de una piedra, causado por la difracción de la luz entre finas capas internas; característico de la piedra de luna."),
            GemGlossaryTerm("Agregado botrioidal", "Conjunto de pequeñas esferas soldadas entre sí, con forma de racimo de uvas, típico de ciertos minerales formados cerca de la superficie, como la smithsonita o la hemimorfita."),
            GemGlossaryTerm("Asterismo", "Estrella luminosa de cuatro, seis o doce brazos que se desplaza sobre la superficie de una piedra tallada en cabujón, provocada por el reflejo de la luz en inclusiones fibrosas orientadas según los ejes del cristal."),
            GemGlossaryTerm("Aventurescencia", "Destello producido por el reflejo de la luz en finas laminillas minerales orientadas dentro de la piedra, como en la aventurina o la piedra de sol."),
            GemGlossaryTerm("Birrefringencia", "Propiedad de ciertos cristales de desdoblar un rayo de luz que los atraviesa en dos rayos distintos; una fuerte birrefringencia provoca un doblaje visible de las aristas internas con lupa."),
            GemGlossaryTerm("Efecto ojo de gato (chatoyance)", "Banda luminosa móvil que recorre la superficie de una piedra tallada en cabujón, causada por el reflejo de la luz en inclusiones fibrosas o canales paralelos."),
            GemGlossaryTerm("Exfoliación (clivaje)", "Tendencia de un cristal a partirse según planos lisos y regulares, ligados a su estructura atómica; una exfoliación perfecta hace que una piedra sea más delicada de tallar y de llevar."),
            GemGlossaryTerm("Cristal negativo", "Cavidad interna vacía o rellena de fluido, cuya forma reproduce exactamente la geometría del sistema cristalino de la piedra huésped."),
            GemGlossaryTerm("Densidad (gravedad específica)", "Relación entre la masa de una piedra y la de un volumen igual de agua; medida con la balanza hidrostática, es un criterio muy discriminante entre especies de apariencia similar."),
            GemGlossaryTerm("Dispersión", "Capacidad de una piedra de descomponer la luz blanca en sus colores espectrales, percibida como destellos de color (el \"fuego\"); especialmente fuerte en el diamante y la esfena."),
            GemGlossaryTerm("Brillo", "Aspecto de la luz reflejada por la superficie de una piedra (vítreo, adamantino, sedoso, graso, resinoso, nacarado...), que depende de su índice de refracción y de su pulido."),
            GemGlossaryTerm("Inclusión en huella dactilar (de cicatrización)", "Red de finas inclusiones fluidas en volutas, semejante a una huella dactilar, formada por la cicatrización parcial de una fractura interna durante el crecimiento del cristal."),
            GemGlossaryTerm("Exsolución (laminillas de exsolución)", "Finas laminillas internas formadas cuando dos minerales mezclados a alta temperatura se separan al enfriarse; origen de efectos ópticos como la adularescencia o la labradorescencia."),
            GemGlossaryTerm("Fluorescencia", "Emisión de luz visible por una piedra expuesta a rayos ultravioletas, cuyo color e intensidad ayudan a identificar una especie o un tratamiento."),
            GemGlossaryTerm("Halo de tensión", "Fina fisura circular que se forma alrededor de un cristal incluido bajo el efecto de las tensiones internas ejercidas durante el crecimiento de la piedra huésped."),
            GemGlossaryTerm("Índice de refracción", "Medida de la desviación de la luz al penetrar en una piedra, característica de cada especie y medida con el refractómetro."),
            GemGlossaryTerm("Inclusión", "Cualquier elemento ajeno (cristal, fluido, gas, fractura) atrapado dentro de una piedra durante su formación; su estudio es una de las principales herramientas de identificación y autenticación en gemología."),
            GemGlossaryTerm("Jardín", "Término empleado para designar el conjunto de inclusiones visibles de una esmeralda, cuya riqueza y naturaleza ayudan a determinar su origen geográfico."),
            GemGlossaryTerm("Labradorescencia", "Juego de colores metálicos (azul, verde, anaranjado) que aparece al girar la piedra, debido a la difracción de la luz en finas laminillas internas; característico de la labradorita."),
            GemGlossaryTerm("Macla", "Asociación de dos o más cristales de la misma especie, orientados según una regla geométrica precisa, formando un cristal compuesto único."),
            GemGlossaryTerm("Roca ígnea", "Roca nacida del enfriamiento de un magma, en profundidad (roca plutónica, de grano grueso) o en superficie (roca volcánica, de grano fino); cuna del diamante, el peridoto y el circón."),
            GemGlossaryTerm("Metamíctico", "Se dice de un cristal cuya estructura atómica ha sido parcialmente desorganizada por la desintegración radiactiva de elementos traza que contiene, como ciertos circones."),
            GemGlossaryTerm("Roca metamórfica", "Roca preexistente transformada en profundidad por el calor y la presión sin fusión completa; cuna del rubí, el zafiro, el granate y el jade."),
            GemGlossaryTerm("Pegmatita", "Roca ígnea de grano excepcionalmente grueso, procedente de la última bolsa de magma rica en agua en cristalizar; principal fuente de piedras finas de colección (berilo, turmalina, topacio...)."),
            GemGlossaryTerm("Pleocroísmo", "Propiedad de ciertas piedras de mostrar colores diferentes según el ángulo de observación, debida a la absorción desigual de la luz según los ejes del cristal."),
            GemGlossaryTerm("Roca sedimentaria", "Roca formada en superficie por acumulación de partículas o precipitación de minerales disueltos en agua, sin calor profundo; cuna del ópalo y la turquesa."),
            GemGlossaryTerm("Seda", "Finas inclusiones aciculares (a menudo de rutilo), dispuestas en redes densas, que dan un aspecto sedoso a la piedra y originan el asterismo cuando están orientadas según varios ejes."),
            GemGlossaryTerm("Estructura fibrorradiada", "Conjunto de fibras minerales dispuestas en abanico a partir de un punto central, dando un aspecto satinado o sedoso a la piedra."),
            GemGlossaryTerm("Sistema cristalino", "Una de las siete familias geométricas (cúbico, tetragonal, hexagonal, trigonal, ortorrómbico, monoclínico, triclínico) según las cuales se organiza la estructura atómica de un cristal."),
            GemGlossaryTerm("Tenebrescencia", "Cambio de color reversible de una piedra expuesta y luego sustraída de la luz (o de los UV), observado en particular en la hackmanita."),
            GemGlossaryTerm("Transparencia", "Grado en que la luz atraviesa una piedra, desde transparente (imagen nítida a través de la piedra) hasta translúcida (luz difusa) y opaca (ninguna luz transmitida)."),
            GemGlossaryTerm("Tricita", "Inclusión líquida fragmentada en finos filamentos ramificados, que evocan hilos de algodón, frecuente en las turmalinas."),
            GemGlossaryTerm("Tubo hueco (canal de crecimiento)", "Fina cavidad alargada, paralela al eje de crecimiento del cristal, a veces rellena de líquido; característica de numerosos berilos."),
            GemGlossaryTerm("Zonación de color", "Distribución desigual del color dentro de una piedra, organizada en bandas o sectores que siguen la geometría de crecimiento del cristal.")
        )
    )

    private val it = GemGlossaryPage(
        intro = "Il vocabolario gemmologico può sembrare tecnico a prima vista. Questo glossario spiega in modo chiaro i termini più comuni — quelli usati nelle schede delle inclusioni tipiche e nello strumento di analisi — per comprendere meglio ciò che rivela l'osservazione di una pietra.",
        termes = listOf(
            GemGlossaryTerm("Adularescenza", "Riflesso bluastro e fluttuante che sembra spostarsi sotto la superficie di una pietra, causato dalla diffrazione della luce tra sottili strati interni; caratteristico della pietra di luna."),
            GemGlossaryTerm("Aggregato botrioidale", "Insieme di piccole sfere saldate tra loro, a forma di grappolo d'uva, tipico di alcuni minerali formatisi vicino alla superficie, come la smithsonite o l'emimorfite."),
            GemGlossaryTerm("Asterismo", "Stella luminosa a quattro, sei o dodici raggi che si sposta sulla superficie di una pietra tagliata a cabochon, provocata dal riflesso della luce su inclusioni fibrose orientate secondo gli assi del cristallo."),
            GemGlossaryTerm("Aventurescenza", "Scintillio prodotto dal riflesso della luce su sottili lamelle minerali orientate all'interno della pietra, come nell'avventurina o nella pietra di sole."),
            GemGlossaryTerm("Birifrangenza", "Proprietà di certi cristalli di sdoppiare un raggio di luce che li attraversa in due raggi distinti; una forte birifrangenza provoca uno sdoppiamento visibile degli spigoli interni con la lente."),
            GemGlossaryTerm("Effetto occhio di gatto (chatoyance)", "Banda luminosa mobile che attraversa la superficie di una pietra tagliata a cabochon, causata dal riflesso della luce su inclusioni fibrose o canali paralleli."),
            GemGlossaryTerm("Sfaldatura (clivaggio)", "Tendenza di un cristallo a spaccarsi secondo piani lisci e regolari, legati alla sua struttura atomica; una sfaldatura perfetta rende una pietra più delicata da tagliare e da indossare."),
            GemGlossaryTerm("Cristallo negativo", "Cavità interna vuota o riempita di fluido, la cui forma riproduce esattamente la geometria del sistema cristallino della pietra ospite."),
            GemGlossaryTerm("Densità (peso specifico)", "Rapporto tra la massa di una pietra e quella di un pari volume d'acqua; misurata con la bilancia idrostatica, è un criterio molto discriminante tra specie di aspetto simile."),
            GemGlossaryTerm("Dispersione", "Capacità di una pietra di scomporre la luce bianca nei suoi colori spettrali, percepita come bagliori colorati (il \"fuoco\"); particolarmente forte nel diamante e nella titanite."),
            GemGlossaryTerm("Lucentezza", "Aspetto della luce riflessa dalla superficie di una pietra (vitrea, adamantina, setosa, grassa, resinosa, perlacea...), che dipende dal suo indice di rifrazione e dalla sua lucidatura."),
            GemGlossaryTerm("Inclusione a impronta digitale (di guarigione)", "Rete di sottili inclusioni fluide a volute, che ricorda un'impronta digitale, formata dalla guarigione parziale di una frattura interna durante la crescita del cristallo."),
            GemGlossaryTerm("Essoluzione (lamelle di essoluzione)", "Sottili lamelle interne formatesi quando due minerali mescolati ad alta temperatura si separano raffreddandosi; all'origine di effetti ottici come l'adularescenza o la labradorescenza."),
            GemGlossaryTerm("Fluorescenza", "Emissione di luce visibile da parte di una pietra esposta ai raggi ultravioletti, il cui colore e la cui intensità aiutano a identificare una specie o un trattamento."),
            GemGlossaryTerm("Alone di tensione", "Sottile frattura circolare che si forma attorno a un cristallo incluso per effetto delle tensioni interne esercitate durante la crescita della pietra ospite."),
            GemGlossaryTerm("Indice di rifrazione", "Misura della deviazione della luce che penetra in una pietra, caratteristica di ogni specie e misurata con il rifrattometro."),
            GemGlossaryTerm("Inclusione", "Qualsiasi elemento estraneo (cristallo, fluido, gas, frattura) intrappolato all'interno di una pietra durante la sua formazione; il suo studio è uno dei principali strumenti di identificazione e autenticazione in gemmologia."),
            GemGlossaryTerm("Giardino", "Termine usato per indicare l'insieme delle inclusioni visibili di uno smeraldo, la cui ricchezza e natura aiutano a determinarne l'origine geografica."),
            GemGlossaryTerm("Labradorescenza", "Gioco di colori metallici (blu, verde, arancione) che appare ruotando la pietra, dovuto alla diffrazione della luce su sottili lamelle interne; caratteristico della labradorite."),
            GemGlossaryTerm("Geminato", "Associazione di due o più cristalli della stessa specie, orientati secondo una regola geometrica precisa, che formano un cristallo composito unico."),
            GemGlossaryTerm("Roccia ignea", "Roccia nata dal raffreddamento di un magma, in profondità (roccia plutonica, a grana grossa) o in superficie (roccia vulcanica, a grana fine); culla del diamante, del peridoto e dello zircone."),
            GemGlossaryTerm("Metamittico", "Si dice di un cristallo la cui struttura atomica è stata parzialmente disorganizzata dal decadimento radioattivo di elementi in tracce che contiene, come alcuni zirconi."),
            GemGlossaryTerm("Roccia metamorfica", "Roccia preesistente trasformata in profondità dal calore e dalla pressione senza fusione completa; culla del rubino, dello zaffiro, del granato e della giada."),
            GemGlossaryTerm("Pegmatite", "Roccia magmatica a grana eccezionalmente grossa, derivata dall'ultima sacca di magma ricca d'acqua a cristallizzare; principale fonte di pietre fini da collezione (berillo, tormalina, topazio...)."),
            GemGlossaryTerm("Pleocroismo", "Proprietà di certe pietre di mostrare colori diversi a seconda dell'angolo di osservazione, dovuta all'assorbimento disuguale della luce lungo gli assi del cristallo."),
            GemGlossaryTerm("Roccia sedimentaria", "Roccia formatasi in superficie per accumulo di particelle o precipitazione di minerali disciolti in acqua, senza calore profondo; culla dell'opale e della turchese."),
            GemGlossaryTerm("Seta", "Sottili inclusioni aciculari (spesso di rutilo), disposte in reti dense, che danno un aspetto setoso alla pietra e sono all'origine dell'asterismo quando sono orientate secondo più assi."),
            GemGlossaryTerm("Struttura fibroso-raggiata", "Insieme di fibre minerali disposte a ventaglio a partire da un punto centrale, che conferisce un aspetto satinato o setoso alla pietra."),
            GemGlossaryTerm("Sistema cristallino", "Una delle sette famiglie geometriche (cubico, tetragonale, esagonale, trigonale, ortorombico, monoclino, triclino) secondo cui si organizza la struttura atomica di un cristallo."),
            GemGlossaryTerm("Tenebrescenza", "Cambiamento di colore reversibile di una pietra esposta e poi sottratta alla luce (o ai UV), osservato in particolare nella hackmanite."),
            GemGlossaryTerm("Trasparenza", "Grado in cui la luce attraversa una pietra, da trasparente (immagine nitida attraverso la pietra) a translucida (luce diffusa) fino a opaca (nessuna luce trasmessa)."),
            GemGlossaryTerm("Tricchite", "Inclusione liquida frammentata in sottili filamenti ramificati, che ricordano fili di cotone, frequente nelle tormaline."),
            GemGlossaryTerm("Tubo cavo (canale di crescita)", "Sottile cavità allungata, parallela all'asse di crescita del cristallo, talvolta riempita di liquido; caratteristica di numerosi berilli."),
            GemGlossaryTerm("Zonatura del colore", "Distribuzione disuguale del colore all'interno di una pietra, organizzata in bande o settori che seguono la geometria di crescita del cristallo.")
        )
    )

    private val de = GemGlossaryPage(
        intro = "Das gemmologische Vokabular kann auf den ersten Blick technisch wirken. Dieses Glossar erklärt in verständlicher Sprache die gängigsten Begriffe — jene, die in den Hinweisen zu typischen Einschlüssen und im Analysewerkzeug verwendet werden —, um besser zu verstehen, was die Untersuchung eines Steins offenbart.",
        termes = listOf(
            GemGlossaryTerm("Adulareszenz", "Ein schwebender, bläulicher Schimmer, der sich unter der Oberfläche eines Steins zu bewegen scheint, verursacht durch Lichtbeugung zwischen feinen inneren Schichten; charakteristisch für Mondstein."),
            GemGlossaryTerm("Traubenförmiges Aggregat", "Ansammlung kleiner, miteinander verwachsener Kugeln in Form einer Weintraube, typisch für bestimmte oberflächennah gebildete Minerale wie Smithsonit oder Hemimorphit."),
            GemGlossaryTerm("Asterismus", "Ein leuchtender vier-, sechs- oder zwölfstrahliger Stern, der sich über die Oberfläche eines im Cabochon geschliffenen Steins bewegt, verursacht durch Lichtreflexion an faserigen, entlang der Kristallachsen ausgerichteten Einschlüssen."),
            GemGlossaryTerm("Aventureszenz", "Ein Funkeln, das durch Lichtreflexion an feinen, im Stein orientierten Mineralplättchen entsteht, wie bei Aventurin oder Sonnenstein."),
            GemGlossaryTerm("Doppelbrechung", "Die Eigenschaft bestimmter Kristalle, einen durchtretenden Lichtstrahl in zwei getrennte Strahlen aufzuspalten; starke Doppelbrechung verursacht eine unter der Lupe sichtbare Kantenverdopplung im Inneren."),
            GemGlossaryTerm("Katzenaugeneffekt (Chatoyance)", "Ein wandernder Lichtstreifen, der über die Oberfläche eines im Cabochon geschliffenen Steins läuft, verursacht durch Lichtreflexion an parallelen faserigen Einschlüssen oder Kanälen."),
            GemGlossaryTerm("Spaltbarkeit", "Die Neigung eines Kristalls, sich entlang glatter, regelmäßiger Ebenen zu spalten, die mit seiner Atomstruktur zusammenhängen; vollkommene Spaltbarkeit macht einen Stein empfindlicher beim Schleifen und Tragen."),
            GemGlossaryTerm("Negativkristall", "Ein innerer, leerer oder mit Flüssigkeit gefüllter Hohlraum, dessen Form genau die Kristallsystem-Geometrie des Wirtssteins widerspiegelt."),
            GemGlossaryTerm("Dichte (spezifisches Gewicht)", "Das Verhältnis zwischen der Masse eines Steins und der eines gleich großen Wasservolumens; mit der hydrostatischen Waage gemessen, ist es ein sehr aussagekräftiges Kriterium zur Unterscheidung ähnlich aussehender Arten."),
            GemGlossaryTerm("Dispersion", "Die Fähigkeit eines Steins, weißes Licht in seine Spektralfarben zu zerlegen, wahrgenommen als farbige Blitze (das „Feuer\"); besonders stark bei Diamant und Titanit."),
            GemGlossaryTerm("Glanz", "Das Erscheinungsbild des von der Oberfläche eines Steins reflektierten Lichts (glasig, diamantartig, seidig, fettig, harzig, perlmuttartig...), das von seinem Brechungsindex und seiner Politur abhängt."),
            GemGlossaryTerm("Fingerabdruck-Einschluss (Heilungsriss)", "Ein Netz feiner, wirbelnder Flüssigkeitseinschlüsse, die einem Fingerabdruck ähneln, entstanden durch die teilweise Ausheilung eines inneren Risses während des Kristallwachstums."),
            GemGlossaryTerm("Entmischung (Entmischungslamellen)", "Feine innere Lamellen, die entstehen, wenn zwei bei hoher Temperatur vermischte Minerale sich beim Abkühlen trennen; Ursprung optischer Effekte wie Adulareszenz oder Labradoreszenz."),
            GemGlossaryTerm("Fluoreszenz", "Die Aussendung sichtbaren Lichts durch einen Stein, der ultravioletter Strahlung ausgesetzt ist; Farbe und Intensität helfen, eine Art oder eine Behandlung zu identifizieren."),
            GemGlossaryTerm("Spannungshof", "Ein feiner, kreisförmiger Riss, der sich um einen eingeschlossenen Kristall bildet, verursacht durch innere Spannungen während des Wachstums des Wirtssteins."),
            GemGlossaryTerm("Brechungsindex", "Ein Maß dafür, wie stark sich Licht beim Eintritt in einen Stein bricht, charakteristisch für jede Art und mit dem Refraktometer gemessen."),
            GemGlossaryTerm("Einschluss", "Jedes Fremdmaterial (Kristall, Flüssigkeit, Gas, Riss), das während der Entstehung eines Steins in ihm eingeschlossen wurde; die Untersuchung von Einschlüssen ist eines der wichtigsten Werkzeuge zur Identifikation und Echtheitsprüfung in der Gemmologie."),
            GemGlossaryTerm("Jardin", "Bezeichnung für die Gesamtheit der sichtbaren Einschlüsse eines Smaragds, deren Reichtum und Art helfen, seine geografische Herkunft zu bestimmen."),
            GemGlossaryTerm("Labradoreszenz", "Ein Spiel metallischer Farben (Blau, Grün, Orange), das beim Drehen des Steins erscheint, verursacht durch Lichtbeugung an feinen inneren Lamellen; charakteristisch für Labradorit."),
            GemGlossaryTerm("Zwilling", "Der Verwachs zweier oder mehrerer Kristalle derselben Art, nach einer präzisen geometrischen Regel verbunden, die einen einzigen zusammengesetzten Kristall bilden."),
            GemGlossaryTerm("Magmatisches Gestein", "Gestein, das durch Abkühlung von Magma entsteht, in der Tiefe (Plutonit, grobkörnig) oder an der Oberfläche (Vulkanit, feinkörnig); Ursprungsort von Diamant, Peridot und Zirkon."),
            GemGlossaryTerm("Metamikt", "Beschreibt einen Kristall, dessen Atomstruktur durch den radioaktiven Zerfall enthaltener Spurenelemente teilweise gestört wurde, wie bei manchen Zirkonen."),
            GemGlossaryTerm("Metamorphes Gestein", "Bereits bestehendes Gestein, das in der Tiefe durch Hitze und Druck ohne vollständiges Aufschmelzen umgewandelt wurde; Ursprungsort von Rubin, Saphir, Granat und Jade."),
            GemGlossaryTerm("Pegmatit", "Magmatisches Gestein mit außergewöhnlich grobem Korn, entstanden aus der letzten, wasserreichen Magmatasche, die auskristallisiert; Hauptquelle für Sammler-Farbedelsteine (Beryll, Turmalin, Topas...)."),
            GemGlossaryTerm("Pleochroismus", "Die Eigenschaft bestimmter Steine, je nach Betrachtungswinkel unterschiedliche Farben zu zeigen, verursacht durch ungleichmäßige Lichtabsorption entlang der Kristallachsen."),
            GemGlossaryTerm("Sedimentgestein", "Gestein, das an der Oberfläche durch Ansammlung von Partikeln oder Ausfällung im Wasser gelöster Minerale entsteht, ohne Tiefenhitze; Ursprungsort von Opal und Türkis."),
            GemGlossaryTerm("Seide", "Feine nadelförmige Einschlüsse (oft Rutil), in dichten Netzen angeordnet, die einem Stein ein seidiges Aussehen verleihen und bei Ausrichtung entlang mehrerer Achsen Asterismus verursachen."),
            GemGlossaryTerm("Faserig-radialstrahlige Struktur", "Eine Anordnung von Mineralfasern, die fächerförmig von einem zentralen Punkt ausgehen und dem Stein ein seidig-glänzendes Aussehen verleihen."),
            GemGlossaryTerm("Kristallsystem", "Eine der sieben geometrischen Familien (kubisch, tetragonal, hexagonal, trigonal, orthorhombisch, monoklin, triklin), nach denen die Atomstruktur eines Kristalls organisiert ist."),
            GemGlossaryTerm("Tenebreszenz", "Ein reversibler Farbwechsel eines Steins, der Licht (oder UV) ausgesetzt und diesem dann wieder entzogen wird, insbesondere bei Hackmanit zu beobachten."),
            GemGlossaryTerm("Transparenz", "Der Grad, in dem Licht einen Stein durchdringt, von transparent (klares Bild durch den Stein) über durchscheinend (gestreutes Licht) bis undurchsichtig (kein durchgelassenes Licht)."),
            GemGlossaryTerm("Trichit", "Ein Flüssigkeitseinschluss, aufgespalten in feine verzweigte Fäden, die an Wattefäden erinnern, häufig bei Turmalinen."),
            GemGlossaryTerm("Hohlkanal (Wachstumskanal)", "Ein feiner, länglicher Hohlraum parallel zur Wachstumsachse des Kristalls, manchmal mit Flüssigkeit gefüllt; charakteristisch für viele Beryllvarietäten."),
            GemGlossaryTerm("Farbzonierung", "Eine ungleichmäßige Farbverteilung innerhalb eines Steins, angeordnet in Bändern oder Sektoren, die der Wachstumsgeometrie des Kristalls folgen.")
        )
    )

    private val pt = GemGlossaryPage(
        intro = "O vocabulário da gemologia pode parecer técnico à primeira vista. Este glossário explica em linguagem clara os termos mais comuns — os usados nas fichas de inclusões típicas e na ferramenta de análise — para compreender melhor o que a observação de uma pedra revela.",
        termes = listOf(
            GemGlossaryTerm("Adularescência", "Reflexo azulado e flutuante que parece deslocar-se sob a superfície de uma pedra, causado pela difração da luz entre finas camadas internas; característico da pedra da lua."),
            GemGlossaryTerm("Agregado botrioidal", "Conjunto de pequenas esferas soldadas entre si, em forma de cacho de uvas, típico de certos minerais formados perto da superfície, como a smithsonita ou a hemimorfite."),
            GemGlossaryTerm("Asterismo", "Estrela luminosa de quatro, seis ou doze pontas que se desloca à superfície de uma pedra lapidada em cabochão, provocada pela reflexão da luz em inclusões fibrosas orientadas segundo os eixos do cristal."),
            GemGlossaryTerm("Aventurescência", "Cintilação produzida pela reflexão da luz em finas lamelas minerais orientadas no interior da pedra, como na aventurina ou na pedra do sol."),
            GemGlossaryTerm("Birrefringência", "Propriedade de certos cristais de desdobrar um raio de luz que os atravessa em dois raios distintos; uma forte birrefringência provoca um desdobramento visível das arestas internas com lupa."),
            GemGlossaryTerm("Efeito olho-de-gato (chatoyance)", "Faixa luminosa móvel que percorre a superfície de uma pedra lapidada em cabochão, causada pela reflexão da luz em inclusões fibrosas ou canais paralelos."),
            GemGlossaryTerm("Clivagem", "Tendência de um cristal a fender-se segundo planos lisos e regulares, ligados à sua estrutura atómica; uma clivagem perfeita torna uma pedra mais delicada de lapidar e de usar."),
            GemGlossaryTerm("Cristal negativo", "Cavidade interna vazia ou preenchida por fluido, cuja forma reproduz exatamente a geometria do sistema cristalino da pedra hospedeira."),
            GemGlossaryTerm("Densidade (peso específico)", "Relação entre a massa de uma pedra e a de um volume igual de água; medida com a balança hidrostática, é um critério muito discriminante entre espécies de aparência semelhante."),
            GemGlossaryTerm("Dispersão", "Capacidade de uma pedra de decompor a luz branca nas suas cores espectrais, percebida como lampejos coloridos (o \"fogo\"); particularmente forte no diamante e na esfena."),
            GemGlossaryTerm("Brilho", "Aspeto da luz refletida pela superfície de uma pedra (vítreo, adamantino, sedoso, gorduroso, resinoso, nacarado...), que depende do seu índice de refração e do seu polimento."),
            GemGlossaryTerm("Inclusão em impressão digital (de cicatrização)", "Rede de finas inclusões fluidas em voluta, semelhante a uma impressão digital, formada pela cicatrização parcial de uma fratura interna durante o crescimento do cristal."),
            GemGlossaryTerm("Exsolução (lamelas de exsolução)", "Finas lamelas internas formadas quando dois minerais misturados a alta temperatura se separam ao arrefecer; na origem de efeitos ópticos como a adularescência ou a labradorescência."),
            GemGlossaryTerm("Fluorescência", "Emissão de luz visível por uma pedra exposta a raios ultravioleta, cuja cor e intensidade ajudam a identificar uma espécie ou um tratamento."),
            GemGlossaryTerm("Halo de tensão", "Fina fissura circular que se forma em torno de um cristal incluído sob o efeito das tensões internas exercidas durante o crescimento da pedra hospedeira."),
            GemGlossaryTerm("Índice de refração", "Medida do desvio da luz ao penetrar numa pedra, característica de cada espécie e medida com o refratómetro."),
            GemGlossaryTerm("Inclusão", "Qualquer elemento estranho (cristal, fluido, gás, fratura) aprisionado no interior de uma pedra durante a sua formação; o seu estudo é uma das principais ferramentas de identificação e autenticação em gemologia."),
            GemGlossaryTerm("Jardim", "Termo usado para designar o conjunto das inclusões visíveis de uma esmeralda, cuja riqueza e natureza ajudam a determinar a sua origem geográfica."),
            GemGlossaryTerm("Labradorescência", "Jogo de cores metálicas (azul, verde, laranja) que aparece ao rodar a pedra, devido à difração da luz em finas lamelas internas; característico da labradorite."),
            GemGlossaryTerm("Macla", "Associação de dois ou mais cristais da mesma espécie, orientados segundo uma regra geométrica precisa, formando um cristal composto único."),
            GemGlossaryTerm("Rocha ígnea", "Rocha nascida do arrefecimento de um magma, em profundidade (rocha plutónica, de grão grosso) ou à superfície (rocha vulcânica, de grão fino); berço do diamante, do peridoto e do zircão."),
            GemGlossaryTerm("Metamítico", "Diz-se de um cristal cuja estrutura atómica foi parcialmente desorganizada pela desintegração radioativa de elementos vestigiais que contém, como certos zircões."),
            GemGlossaryTerm("Rocha metamórfica", "Rocha pré-existente transformada em profundidade pelo calor e pela pressão sem fusão completa; berço do rubi, da safira, da granada e do jade."),
            GemGlossaryTerm("Pegmatito", "Rocha ígnea de grão excecionalmente grosso, proveniente da última bolsa de magma rica em água a cristalizar; principal fonte de pedras finas de coleção (berilo, turmalina, topázio...)."),
            GemGlossaryTerm("Pleocroísmo", "Propriedade de certas pedras de mostrar cores diferentes consoante o ângulo de observação, devido à absorção desigual da luz segundo os eixos do cristal."),
            GemGlossaryTerm("Rocha sedimentar", "Rocha formada à superfície por acumulação de partículas ou precipitação de minerais dissolvidos na água, sem calor profundo; berço da opala e da turquesa."),
            GemGlossaryTerm("Seda", "Finas inclusões aciculares (frequentemente de rutilo), dispostas em redes densas, que conferem um aspeto sedoso à pedra e estão na origem do asterismo quando orientadas segundo vários eixos."),
            GemGlossaryTerm("Estrutura fibrorradiada", "Conjunto de fibras minerais dispostas em leque a partir de um ponto central, conferindo um aspeto acetinado ou sedoso à pedra."),
            GemGlossaryTerm("Sistema cristalino", "Uma das sete famílias geométricas (cúbico, tetragonal, hexagonal, trigonal, ortorrômbico, monoclínico, triclínico) segundo as quais se organiza a estrutura atómica de um cristal."),
            GemGlossaryTerm("Tenebrescência", "Alteração de cor reversível de uma pedra exposta e depois retirada da luz (ou dos UV), observada nomeadamente na hackmanite."),
            GemGlossaryTerm("Transparência", "Grau em que a luz atravessa uma pedra, do transparente (imagem nítida através da pedra) ao translúcido (luz difusa) até ao opaco (nenhuma luz transmitida)."),
            GemGlossaryTerm("Tricita", "Inclusão líquida fragmentada em finos filamentos ramificados, que evocam fios de algodão, frequente nas turmalinas."),
            GemGlossaryTerm("Tubo oco (canal de crescimento)", "Fina cavidade alongada, paralela ao eixo de crescimento do cristal, por vezes preenchida com líquido; característica de numerosos berilos."),
            GemGlossaryTerm("Zonamento de cor", "Distribuição desigual da cor no interior de uma pedra, organizada em faixas ou setores que seguem a geometria de crescimento do cristal.")
        )
    )

    private val zh = GemGlossaryPage(
        intro = "宝石学词汇初看可能显得专业艰深。本词汇表用通俗的语言解释最常见的术语——即典型内含物说明和分析工具中所使用的词汇——帮助您更好地理解观察一颗宝石所揭示的信息。",
        termes = listOf(
            GemGlossaryTerm("月光效应", "宝石表面下方一种漂浮的蓝色光晕，看似在移动，由光线在细薄内部层间发生衍射所致；月光石的特征效应。"),
            GemGlossaryTerm("葡萄状集合体", "由许多小球体熔合聚集而成、形似一串葡萄的矿物集合体，常见于菱锌矿或异极矿等近地表形成的矿物。"),
            GemGlossaryTerm("星光效应", "在弧面型宝石表面移动的四射、六射或十二射光芒星形，由光线在沿晶轴排列的纤维状内含物上反射而产生。"),
            GemGlossaryTerm("砂金效应", "光线在宝石内部定向排列的细小矿物薄片上反射所产生的闪光效果，如砂金石或日光石中所见。"),
            GemGlossaryTerm("双折射", "某些晶体将穿过其内部的一束光分裂为两束不同光线的特性；强双折射会使放大观察时内部刻面棱线出现明显的重影。"),
            GemGlossaryTerm("猫眼效应", "在弧面型宝石表面移动的一条光带，由光线在平行的纤维状内含物或管状通道上反射所致。"),
            GemGlossaryTerm("解理", "晶体沿与其原子结构相关的平整规则平面裂开的倾向；完全解理使宝石在切割和佩戴时更为脆弱。"),
            GemGlossaryTerm("负晶", "内部的空洞或充满流体的空腔，其形状精确复刻寄主宝石晶系的几何结构。"),
            GemGlossaryTerm("密度（比重）", "宝石质量与同体积水质量之比；用静水天平测定，是区分外观相近品种的重要判据。"),
            GemGlossaryTerm("色散", "宝石将白光分解为光谱色的能力，表现为彩色的闪光（即\"火彩\"）；在钻石和榍石中尤为显著。"),
            GemGlossaryTerm("光泽", "宝石表面反射光线所呈现的外观（玻璃光泽、金刚光泽、丝绢光泽、油脂光泽、树脂光泽、珍珠光泽等），取决于其折射率和抛光质量。"),
            GemGlossaryTerm("指纹状内含物（愈合裂隙）", "由细小卷曲流体内含物构成的网络，形似指纹，是晶体生长过程中内部裂隙部分愈合所形成。"),
            GemGlossaryTerm("出溶（出溶片晶）", "两种矿物在高温下混合后冷却分离时形成的细薄内部层理；是月光效应或拉长石效应等光学现象的成因。"),
            GemGlossaryTerm("荧光", "宝石在紫外线照射下发出可见光的现象，其颜色和强度有助于鉴定品种或识别处理方式。"),
            GemGlossaryTerm("应力晕", "在寄主宝石生长过程中因内部应力作用而在包裹晶体周围形成的细小环形裂纹。"),
            GemGlossaryTerm("折射率", "光线进入宝石时发生偏折的量度，是每个品种的特征值，用折射仪测定。"),
            GemGlossaryTerm("内含物", "宝石形成过程中被包裹在内部的任何外来物质（晶体、流体、气体、裂隙）；对内含物的研究是宝石学中鉴定与鉴真的主要手段之一。"),
            GemGlossaryTerm("花园状内含物", "用于形容祖母绿中可见内含物整体外观的术语，其丰富程度和性质有助于判断其产地。"),
            GemGlossaryTerm("拉长石效应", "转动宝石时出现的金属色彩变幻（蓝、绿、橙），由光线在细薄内部层理上衍射所致；拉长石的特征效应。"),
            GemGlossaryTerm("双晶", "两个或多个同种晶体按精确的几何规律结合，形成一个单一复合晶体。"),
            GemGlossaryTerm("岩浆岩", "由岩浆冷却而成的岩石，深部冷却形成颗粒粗大的侵入岩，地表冷却形成颗粒细小的喷出岩；是钻石、橄榄石和锆石的摇篮。"),
            GemGlossaryTerm("变生质（蜕晶化）", "形容一种晶体，其原子结构因所含微量放射性元素的衰变而部分紊乱，某些锆石即属此类。"),
            GemGlossaryTerm("变质岩", "先存岩石在深部经高温高压作用但未完全熔融而转变形成的岩石；是红宝石、蓝宝石、石榴石和翡翠的摇篮。"),
            GemGlossaryTerm("伟晶岩", "颗粒异常粗大的岩浆岩，由最后一批富含水分的岩浆结晶而成；是绿柱石、碧玺、黄玉等收藏级彩色宝石的主要来源。"),
            GemGlossaryTerm("多色性", "某些宝石因光线沿晶轴的不均匀吸收，而在不同观察角度呈现不同颜色的特性。"),
            GemGlossaryTerm("沉积岩", "在地表由颗粒堆积或水中溶解矿物沉淀而成的岩石，形成过程中没有深部高温；是蛋白石和绿松石的摇篮。"),
            GemGlossaryTerm("绢丝状内含物", "细密排列成网状的针状内含物（常为金红石），赋予宝石丝绢般的外观，当沿多个方向排列时会产生星光效应。"),
            GemGlossaryTerm("纤维放射状结构", "矿物纤维从中心点呈扇形排列的集合体，赋予宝石缎光或丝绢般的外观。"),
            GemGlossaryTerm("晶系", "晶体原子结构所遵循的七种几何分类之一（等轴晶系、四方晶系、六方晶系、三方晶系、斜方晶系、单斜晶系、三斜晶系）。"),
            GemGlossaryTerm("光敏变色效应", "宝石经光照（或紫外线照射）后再移除光源时发生的可逆颜色变化，尤见于紫方钠石。"),
            GemGlossaryTerm("透明度", "光线穿透宝石的程度，从透明（可清晰透视宝石内部）到半透明（光线散射）直至不透明（无光线透过）。"),
            GemGlossaryTerm("发状内含物", "分裂成细密分支纤维状的液态内含物，形似棉絮细丝，常见于碧玺中。"),
            GemGlossaryTerm("中空管道（生长通道）", "与晶体生长轴平行的细长空腔，有时充满液体；多见于绿柱石类宝石。"),
            GemGlossaryTerm("色带", "宝石内部颜色分布不均的现象，呈条带状或区块状排列，沿晶体生长的几何方向延伸。")
        )
    )

    private val ru = GemGlossaryPage(
        intro = "Геммологическая терминология может на первый взгляд показаться сложной. Этот словарь простым языком объясняет наиболее употребительные термины — те, что используются в описаниях типичных включений и в инструменте анализа, — чтобы лучше понимать, что раскрывает изучение камня.",
        termes = listOf(
            GemGlossaryTerm("Адуляресценция", "Плавающий голубоватый отблеск, будто перемещающийся под поверхностью камня, вызванный дифракцией света между тонкими внутренними слоями; характерен для лунного камня."),
            GemGlossaryTerm("Гроздевидный агрегат", "Скопление мелких сросшихся сферолитов, напоминающее виноградную гроздь, характерное для некоторых минералов, образующихся близ поверхности, таких как смитсонит или гемиморфит."),
            GemGlossaryTerm("Астеризм", "Светящаяся звезда с четырьмя, шестью или двенадцатью лучами, перемещающаяся по поверхности камня, ограненного кабошоном, вызванная отражением света от волокнистых включений, ориентированных вдоль осей кристалла."),
            GemGlossaryTerm("Авантюресценция", "Мерцание, возникающее при отражении света от тонких минеральных пластинок, ориентированных внутри камня, как в авантюрине или солнечном камне."),
            GemGlossaryTerm("Двупреломление", "Свойство некоторых кристаллов разделять проходящий через них луч света на два отдельных луча; сильное двупреломление вызывает заметное под лупой раздвоение внутренних граней."),
            GemGlossaryTerm("Эффект кошачьего глаза (шатойанс)", "Подвижная светлая полоса, пробегающая по поверхности камня, ограненного кабошоном, вызванная отражением света от параллельных волокнистых включений или каналов."),
            GemGlossaryTerm("Спайность", "Способность кристалла раскалываться по ровным правильным плоскостям, связанным с его атомной структурой; совершенная спайность делает камень более уязвимым при огранке и ношении."),
            GemGlossaryTerm("Отрицательный кристалл", "Внутренняя полость, пустая или заполненная флюидом, форма которой в точности повторяет геометрию кристаллической системы вмещающего камня."),
            GemGlossaryTerm("Плотность (удельный вес)", "Отношение массы камня к массе равного объёма воды; измеряется гидростатическими весами и служит важным критерием для различения внешне схожих видов."),
            GemGlossaryTerm("Дисперсия", "Способность камня разлагать белый свет на спектральные цвета, воспринимаемая как цветные вспышки («игра» камня); особенно сильна у алмаза и сфена."),
            GemGlossaryTerm("Блеск", "Характер света, отражённого поверхностью камня (стеклянный, алмазный, шелковистый, жирный, смолистый, перламутровый...), зависящий от показателя преломления и качества полировки."),
            GemGlossaryTerm("Включение типа «отпечаток пальца» (заживший разрыв)", "Сеть тонких завитых флюидных включений, напоминающая отпечаток пальца, образованная частичным заживлением внутренней трещины в процессе роста кристалла."),
            GemGlossaryTerm("Распад твёрдого раствора (пластинки распада)", "Тонкие внутренние пластинки, образующиеся при разделении двух минералов, смешанных при высокой температуре, в процессе охлаждения; лежат в основе таких оптических эффектов, как адуляресценция или лабрадоресценция."),
            GemGlossaryTerm("Флуоресценция", "Излучение видимого света камнем под воздействием ультрафиолетовых лучей; цвет и интенсивность свечения помогают определить вид камня или выявить облагораживание."),
            GemGlossaryTerm("Ореол напряжения", "Тонкая кольцевая трещина, образующаяся вокруг включённого кристалла под действием внутренних напряжений в процессе роста вмещающего камня."),
            GemGlossaryTerm("Показатель преломления", "Мера отклонения света при входе в камень, характерная для каждого вида и измеряемая рефрактометром."),
            GemGlossaryTerm("Включение", "Любой посторонний элемент (кристалл, флюид, газ, трещина), заключённый внутри камня в процессе его формирования; изучение включений — один из главных инструментов идентификации и подтверждения подлинности в геммологии."),
            GemGlossaryTerm("«Сад» (жарден)", "Термин, обозначающий совокупность видимых включений в изумруде; их богатство и характер помогают определить географическое происхождение камня."),
            GemGlossaryTerm("Лабрадоресценция", "Игра металлических цветов (синего, зелёного, оранжевого), появляющаяся при повороте камня, вызванная дифракцией света на тонких внутренних пластинках; характерна для лабрадорита."),
            GemGlossaryTerm("Двойник", "Сросток из двух или более кристаллов одного вида, ориентированных по строгому геометрическому закону и образующих единый составной кристалл."),
            GemGlossaryTerm("Магматическая порода", "Порода, образовавшаяся при остывании магмы — на глубине (плутоническая порода, крупнозернистая) или на поверхности (вулканическая порода, мелкозернистая); колыбель алмаза, перидота и циркона."),
            GemGlossaryTerm("Метамиктный", "Определение для кристалла, атомная структура которого частично нарушена радиоактивным распадом содержащихся в нём микропримесей, как у некоторых цирконов."),
            GemGlossaryTerm("Метаморфическая порода", "Ранее существовавшая порода, преобразованная на глубине под действием тепла и давления без полного расплавления; колыбель рубина, сапфира, граната и жада."),
            GemGlossaryTerm("Пегматит", "Магматическая порода с исключительно крупным зерном, образовавшаяся из последнего, богатого водой очага магмы при кристаллизации; главный источник коллекционных цветных камней (берилл, турмалин, топаз...)."),
            GemGlossaryTerm("Плеохроизм", "Свойство некоторых камней демонстрировать разные цвета в зависимости от угла наблюдения, обусловленное неравномерным поглощением света вдоль осей кристалла."),
            GemGlossaryTerm("Осадочная порода", "Порода, образовавшаяся на поверхности в результате накопления частиц или осаждения минералов, растворённых в воде, без глубинного тепла; колыбель опала и бирюзы."),
            GemGlossaryTerm("Шёлк", "Тонкие игольчатые включения (часто рутиловые), расположенные плотными сетками, придающие камню шелковистый вид и лежащие в основе астеризма при ориентации по нескольким осям."),
            GemGlossaryTerm("Волокнисто-лучистая структура", "Скопление минеральных волокон, расходящихся веерообразно из одной центральной точки, придающее камню атласный или шелковистый вид."),
            GemGlossaryTerm("Кристаллическая система", "Одна из семи геометрических категорий (кубическая, тетрагональная, гексагональная, тригональная, ромбическая, моноклинная, триклинная), по которым организована атомная структура кристалла."),
            GemGlossaryTerm("Тенебресценция", "Обратимое изменение цвета камня при воздействии света (или УФ-излучения) и после его удаления, наблюдаемое, в частности, у гакманита."),
            GemGlossaryTerm("Прозрачность", "Степень, в которой свет проходит через камень, от прозрачного (чёткое изображение сквозь камень) до полупрозрачного (рассеянный свет) и непрозрачного (свет не проходит)."),
            GemGlossaryTerm("Трихит", "Жидкое включение, распавшееся на тонкие ветвящиеся нити, напоминающие хлопковые волокна, часто встречающееся в турмалинах."),
            GemGlossaryTerm("Полый канал (канал роста)", "Тонкая удлинённая полость, параллельная оси роста кристалла, иногда заполненная жидкостью; характерна для многих бериллов."),
            GemGlossaryTerm("Зональная окраска", "Неравномерное распределение цвета внутри камня, организованное в виде полос или секторов, следующих геометрии роста кристалла.")
        )
    )

    private val nl = GemGlossaryPage(
        intro = "Gemmologische terminologie kan op het eerste gezicht technisch overkomen. Dit lexicon legt in duidelijke taal de meest gangbare termen uit — die welke worden gebruikt in de fiches over typische insluitsels en in het analyse-instrument — om beter te begrijpen wat de observatie van een steen onthult.",
        termes = listOf(
            GemGlossaryTerm("Adularescentie", "Een zwevende, blauwachtige glans die onder het oppervlak van een steen lijkt te bewegen, veroorzaakt door lichtdiffractie tussen fijne interne lagen; kenmerkend voor maansteen."),
            GemGlossaryTerm("Druivenvormig aggregaat", "Verzameling kleine, aan elkaar gesmolten bolletjes in de vorm van een druiventros, typisch voor bepaalde mineralen die nabij het oppervlak ontstaan, zoals smithsoniet of hemimorfiet."),
            GemGlossaryTerm("Asterisme", "Een lichtgevende ster met vier, zes of twaalf stralen die over het oppervlak van een cabochon geslepen steen beweegt, veroorzaakt door lichtreflectie op vezelige insluitsels die zijn uitgelijnd volgens de kristalassen."),
            GemGlossaryTerm("Aventurescentie", "Een fonkeling die ontstaat door lichtreflectie op fijne mineraalplaatjes die zich binnenin de steen bevinden, zoals bij aventurijn of zonnesteen."),
            GemGlossaryTerm("Dubbele breking", "De eigenschap van bepaalde kristallen om een doorgaande lichtstraal te splitsen in twee afzonderlijke stralen; sterke dubbele breking veroorzaakt een zichtbare verdubbeling van interne facetranden onder de loep."),
            GemGlossaryTerm("Kattenoogeffect (chatoyance)", "Een bewegende lichtband die over het oppervlak van een cabochon geslepen steen loopt, veroorzaakt door lichtreflectie op parallelle vezelige insluitsels of kanalen."),
            GemGlossaryTerm("Splijting", "De neiging van een kristal om te splijten langs vlakke, regelmatige vlakken die samenhangen met zijn atoomstructuur; volmaakte splijting maakt een steen kwetsbaarder bij het slijpen en dragen."),
            GemGlossaryTerm("Negatief kristal", "Een interne holte, leeg of met vloeistof gevuld, waarvan de vorm exact de kristalstelsel-geometrie van de gastheersteen weergeeft."),
            GemGlossaryTerm("Dichtheid (soortelijk gewicht)", "De verhouding tussen de massa van een steen en die van eenzelfde volume water; gemeten met de hydrostatische weegschaal, is dit een sterk onderscheidend criterium tussen soorten met een vergelijkbaar uiterlijk."),
            GemGlossaryTerm("Dispersie", "Het vermogen van een steen om wit licht te ontleden in zijn spectrale kleuren, waargenomen als kleurige flitsen (het \"vuur\"); bijzonder sterk bij diamant en sfeen."),
            GemGlossaryTerm("Glans", "Het uiterlijk van het licht dat wordt weerkaatst door het oppervlak van een steen (glasachtig, diamantachtig, zijdeachtig, vettig, harsachtig, parelmoerachtig...), afhankelijk van de brekingsindex en de polijsting."),
            GemGlossaryTerm("Vingerafdruk-insluitsel (genezingsinsluitsel)", "Een netwerk van fijne, kronkelende vloeistofinsluitsels dat op een vingerafdruk lijkt, gevormd door de gedeeltelijke genezing van een interne breuk tijdens de groei van het kristal."),
            GemGlossaryTerm("Ontmenging (ontmengingslamellen)", "Fijne interne laagjes die ontstaan wanneer twee bij hoge temperatuur gemengde mineralen zich bij afkoeling scheiden; de oorzaak van optische effecten zoals adularescentie of labradorescentie."),
            GemGlossaryTerm("Fluorescentie", "De uitzending van zichtbaar licht door een steen die aan ultraviolette straling wordt blootgesteld; kleur en intensiteit helpen bij het identificeren van een soort of een behandeling."),
            GemGlossaryTerm("Spanningshalo", "Een fijne cirkelvormige scheur die zich vormt rond een ingesloten kristal onder invloed van de interne spanningen tijdens de groei van de gastheersteen."),
            GemGlossaryTerm("Brekingsindex", "Een maat voor de afbuiging van licht bij het binnendringen van een steen, kenmerkend voor elke soort en gemeten met de refractometer."),
            GemGlossaryTerm("Insluitsel", "Elk vreemd element (kristal, vloeistof, gas, breuk) dat tijdens de vorming van een steen daarbinnen is opgesloten; de studie ervan is een van de belangrijkste hulpmiddelen voor identificatie en authenticatie in de gemmologie."),
            GemGlossaryTerm("Jardin (\"tuin\")", "Term die wordt gebruikt voor het geheel van zichtbare insluitsels in een smaragd, waarvan de rijkdom en aard helpen de geografische herkomst te bepalen."),
            GemGlossaryTerm("Labradorescentie", "Een spel van metaalachtige kleuren (blauw, groen, oranje) dat verschijnt wanneer de steen wordt gedraaid, veroorzaakt door lichtdiffractie op fijne interne laagjes; kenmerkend voor labradoriet."),
            GemGlossaryTerm("Tweeling", "De vergroeiing van twee of meer kristallen van dezelfde soort, verbonden volgens een precieze geometrische regel, die samen één enkel samengesteld kristal vormen."),
            GemGlossaryTerm("Stollingsgesteente", "Gesteente ontstaan door de afkoeling van magma, in de diepte (plutonisch gesteente, grofkorrelig) of aan het oppervlak (vulkanisch gesteente, fijnkorrelig); de bakermat van diamant, olivijn en zirkoon."),
            GemGlossaryTerm("Metamict", "Beschrijft een kristal waarvan de atoomstructuur gedeeltelijk is verstoord door het radioactieve verval van sporenelementen die het bevat, zoals bij sommige zirkonen."),
            GemGlossaryTerm("Metamorf gesteente", "Reeds bestaand gesteente dat in de diepte door hitte en druk is omgevormd zonder volledige smelting; de bakermat van robijn, saffier, granaat en jade."),
            GemGlossaryTerm("Pegmatiet", "Stollingsgesteente met uitzonderlijk grove korrel, ontstaan uit de laatste, waterrijke magmazak die kristalliseerde; de belangrijkste bron van verzamelaars-edelstenen (beryl, toermalijn, topaas...)."),
            GemGlossaryTerm("Pleochroïsme", "De eigenschap van bepaalde stenen om, afhankelijk van de kijkhoek, verschillende kleuren te tonen, veroorzaakt door ongelijke lichtabsorptie langs de kristalassen."),
            GemGlossaryTerm("Sedimentair gesteente", "Gesteente gevormd aan het oppervlak door ophoping van deeltjes of neerslag van in water opgeloste mineralen, zonder diepe hitte; de bakermat van opaal en turkoois."),
            GemGlossaryTerm("Zijde", "Fijne naaldvormige insluitsels (vaak rutiel), gerangschikt in dichte netwerken, die een steen een zijdeachtig uiterlijk geven en asterisme veroorzaken wanneer ze langs meerdere assen zijn uitgelijnd."),
            GemGlossaryTerm("Vezelig-stralende structuur", "Een geheel van minerale vezels die waaiervormig vanuit een centraal punt zijn gerangschikt, wat de steen een satijnachtig of zijdeachtig uiterlijk geeft."),
            GemGlossaryTerm("Kristalstelsel", "Een van de zeven geometrische families (kubisch, tetragonaal, hexagonaal, trigonaal, orthorombisch, monoklien, triklien) waarnaar de atoomstructuur van een kristal is georganiseerd."),
            GemGlossaryTerm("Tenebrescentie", "Een omkeerbare kleurverandering van een steen die aan licht (of UV) wordt blootgesteld en er vervolgens weer aan wordt onttrokken, met name waargenomen bij hackmaniet."),
            GemGlossaryTerm("Transparantie", "De mate waarin licht een steen doordringt, van doorzichtig (een scherp beeld door de steen heen) via doorschijnend (verstrooid licht) tot ondoorzichtig (geen doorgelaten licht)."),
            GemGlossaryTerm("Trichiet", "Een vloeibaar insluitsel dat uiteenvalt in fijne vertakte draadjes, die aan katoenvezels doen denken, veel voorkomend in toermalijnen."),
            GemGlossaryTerm("Holle buis (groeikanaal)", "Een fijne, langwerpige holte evenwijdig aan de groeias van het kristal, soms met vloeistof gevuld; kenmerkend voor vele berylsoorten."),
            GemGlossaryTerm("Kleurzonering", "Een ongelijke verdeling van kleur binnenin een steen, gerangschikt in banden of sectoren die de groeigeometrie van het kristal volgen.")
        )
    )

    private val byLanguage: Map<String, GemGlossaryPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): GemGlossaryPage = byLanguage[languageCode] ?: fr
}
