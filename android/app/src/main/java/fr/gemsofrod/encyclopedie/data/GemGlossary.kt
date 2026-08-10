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
            GemGlossaryTerm("Métamicte", "Se dit d'un cristal dont la structure atomique a été partiellement désorganisée par la désintégration radioactive d'éléments traces qu'il contient, comme certains zircons."),
            GemGlossaryTerm("Pléochroïsme", "Propriété de certaines pierres à montrer des couleurs différentes selon l'angle d'observation, due à l'absorption inégale de la lumière selon les axes du cristal."),
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
            GemGlossaryTerm("Metamict", "Describes a crystal whose atomic structure has been partially disrupted by the radioactive decay of trace elements it contains, as in some zircons."),
            GemGlossaryTerm("Pleochroism", "The property of certain stones to show different colours depending on the viewing angle, due to uneven light absorption along the crystal's axes."),
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
            GemGlossaryTerm("Metamíctico", "Se dice de un cristal cuya estructura atómica ha sido parcialmente desorganizada por la desintegración radiactiva de elementos traza que contiene, como ciertos circones."),
            GemGlossaryTerm("Pleocroísmo", "Propiedad de ciertas piedras de mostrar colores diferentes según el ángulo de observación, debida a la absorción desigual de la luz según los ejes del cristal."),
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
            GemGlossaryTerm("Metamittico", "Si dice di un cristallo la cui struttura atomica è stata parzialmente disorganizzata dal decadimento radioattivo di elementi in tracce che contiene, come alcuni zirconi."),
            GemGlossaryTerm("Pleocroismo", "Proprietà di certe pietre di mostrare colori diversi a seconda dell'angolo di osservazione, dovuta all'assorbimento disuguale della luce lungo gli assi del cristallo."),
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
            GemGlossaryTerm("Metamikt", "Beschreibt einen Kristall, dessen Atomstruktur durch den radioaktiven Zerfall enthaltener Spurenelemente teilweise gestört wurde, wie bei manchen Zirkonen."),
            GemGlossaryTerm("Pleochroismus", "Die Eigenschaft bestimmter Steine, je nach Betrachtungswinkel unterschiedliche Farben zu zeigen, verursacht durch ungleichmäßige Lichtabsorption entlang der Kristallachsen."),
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

    private val byLanguage: Map<String, GemGlossaryPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de
    )

    fun page(languageCode: String): GemGlossaryPage = byLanguage[languageCode] ?: fr
}
