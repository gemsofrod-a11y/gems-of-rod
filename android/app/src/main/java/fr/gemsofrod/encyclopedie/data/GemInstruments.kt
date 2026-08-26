package fr.gemsofrod.encyclopedie.data

data class GemInstrument(
    val nom: String,
    val mesure: String,
    val description: String,
    val astuce: String? = null
)

data class GemInstrumentsPage(
    val intro: String,
    val instruments: List<GemInstrument>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant les instruments d'analyse gemmologique
 * de base (réfractomètre, balance hydrostatique, dichroscope...), traduit dans
 * les 5 langues de l'app indépendamment des fiches gemmes. Complète l'outil
 * "Analyse de pierre" en expliquant comment ses champs (transparence, éclat,
 * densité, pléochroïsme, fluorescence...) sont mesurés en pratique.
 */
object GemInstrumentsInfo {
    private val fr = GemInstrumentsPage(
        intro = "Au-delà de l'œil et de l'expérience, les gemmologues professionnels s'appuient sur une poignée d'instruments normalisés pour caractériser une pierre de façon objective. Voici les outils de base d'un laboratoire de gemmologie, et ce que chacun permet de déterminer.",
        instruments = listOf(
            GemInstrument(
                nom = "Réfractomètre",
                mesure = "Mesure l'indice de réfraction",
                description = "Un contact optique liquide pose la pierre sur un prisme de verre à haut indice ; l'angle de réflexion totale interne, lu sur une échelle graduée, donne l'indice de réfraction — et sa variation selon l'orientation pour les pierres biréfringentes.",
                astuce = "L'instrument de base de tout gemmologue : rapide et non destructif, mais limité aux indices inférieurs à environ 1,81 — au-delà, comme pour la plupart des grenats ou le diamant, il ne fonctionne plus."
            ),
            GemInstrument(
                nom = "Balance hydrostatique",
                mesure = "Mesure la densité",
                description = "La pierre est pesée dans l'air puis immergée dans l'eau ; la différence entre les deux pesées, appliquée au principe d'Archimède, donne la densité relative — une donnée très discriminante entre espèces d'apparence proche.",
                astuce = "Nécessite une pierre détachée, non montée, et une pesée précise au centième de carat."
            ),
            GemInstrument(
                nom = "Dichroscope",
                mesure = "Révèle le pléochroïsme",
                description = "Un petit tube optique muni d'un cristal de calcite dédouble la lumière traversant la pierre en deux images juxtaposées ; si leurs couleurs diffèrent, la pierre est pléochroïque — un indice précieux pour distinguer, par exemple, un saphir (pléochroïque) d'un spinelle bleu (qui ne l'est jamais)."
            ),
            GemInstrument(
                nom = "Polariscope",
                mesure = "Détermine le caractère optique",
                description = "La pierre est observée entre deux filtres polarisants croisés sous une lumière tournante : elle s'éteint totalement à chaque rotation si elle est isotrope (grenat, spinelle, verre), ou alterne clair et sombre si elle est anisotrope, comme la majorité des pierres facettées.",
                astuce = "Permet aussi de repérer les tensions internes et le dédoublement d'image caractéristique d'une forte biréfringence."
            ),
            GemInstrument(
                nom = "Spectroscope à main",
                mesure = "Analyse le spectre d'absorption",
                description = "En décomposant la lumière traversant la pierre, l'instrument révèle des bandes d'absorption caractéristiques de certains éléments chromophores (chrome, fer, cobalt...) — une signature parfois propre à une espèce ou à un traitement, comme les raies du chrome dans le rubis et l'émeraude."
            ),
            GemInstrument(
                nom = "Loupe 10x et microscope binoculaire",
                mesure = "Observe les inclusions",
                description = "Le grossissement révèle les inclusions internes (cristaux, fractures, bulles, structures de croissance) qui trahissent l'origine naturelle ou synthétique d'une pierre, ainsi que d'éventuels traitements — fissures remplies de verre ou d'huile, par exemple.",
                astuce = "L'outil le plus universel du gemmologue, souvent la première étape de tout examen."
            ),
            GemInstrument(
                nom = "Lampe UV (ondes courtes et longues)",
                mesure = "Observe la fluorescence",
                description = "Certaines pierres émettent une lumière visible sous rayonnement ultraviolet invisible ; l'intensité et la couleur de cette fluorescence, observées en chambre noire, aident à identifier une espèce ou à repérer certains traitements et remplissages."
            ),
            GemInstrument(
                nom = "Filtre de Chelsea",
                mesure = "Détecte le chrome",
                description = "Ce filtre coloré ne laisse passer que deux bandes étroites du spectre : une pierre verte contenant du chrome, comme l'émeraude naturelle, apparaît rougeâtre au travers, tandis que la plupart des imitations restent vertes.",
                astuce = "Un test rapide et peu coûteux, mais toujours à confirmer par d'autres méthodes."
            ),
            GemInstrument(
                nom = "Échelle de dureté de Mohs (kit de pointes)",
                mesure = "Teste la résistance à la rayure",
                description = "Une série de pointes calibrées de dureté connue, de 2 à 9, est appliquée avec précaution sur une facette discrète ; la plus dure qui raye la pierre sans être rayée par elle situe sa dureté sur l'échelle de Mohs.",
                astuce = "Un test destructif, à réserver en dernier recours et jamais sur une pierre de valeur déjà taillée."
            ),
            GemInstrument(
                nom = "Testeur de conductivité thermique et électrique",
                mesure = "Distingue le diamant de ses simulants",
                description = "Une sonde mesure la vitesse à laquelle la pierre évacue la chaleur ; le diamant, exceptionnellement bon conducteur thermique, se détache nettement des simulants comme le zircone cubique ou le verre. Les modèles récents ajoutent un test de conductivité électrique pour démasquer la moissanite, seule pierre à imiter aussi sa conductivité thermique."
            )
        ),
        disclaimerTitle = "Un usage professionnel",
        disclaimerBody = "Ces instruments demandent un apprentissage technique et une pierre correctement préparée pour donner des résultats fiables. L'outil d'analyse de l'application est un guide d'orientation à partir des caractéristiques que vous avez pu observer ou mesurer ; pour une identification ou une certification officielle, seul un laboratoire gemmologique agréé (GIA, Gübelin, GFCO...) fait foi."
    )

    private val en = GemInstrumentsPage(
        intro = "Beyond the trained eye, professional gemologists rely on a handful of standardised instruments to characterise a stone objectively. Here are the basic tools of a gemology lab, and what each one determines.",
        instruments = listOf(
            GemInstrument(
                nom = "Refractometer",
                mesure = "Measures the refractive index",
                description = "An optical contact liquid seats the stone on a high-index glass prism; the angle of total internal reflection, read off a graduated scale, gives the refractive index — and its variation with orientation for birefringent stones.",
                astuce = "Every gemologist's basic tool: fast and non-destructive, but limited to indices below roughly 1.81 — beyond that, as with most garnets or diamond, it no longer works."
            ),
            GemInstrument(
                nom = "Hydrostatic balance",
                mesure = "Measures specific gravity",
                description = "The stone is weighed in air, then immersed in water; the difference between the two readings, applied to Archimedes' principle, gives its specific gravity — a highly discriminating figure between species that look alike.",
                astuce = "Requires a loose, unmounted stone and a weighing precise to a hundredth of a carat."
            ),
            GemInstrument(
                nom = "Dichroscope",
                mesure = "Reveals pleochroism",
                description = "A small optical tube fitted with a calcite crystal splits the light passing through the stone into two side-by-side images; if their colours differ, the stone is pleochroic — a valuable clue to tell, say, a sapphire (pleochroic) from a blue spinel (never pleochroic)."
            ),
            GemInstrument(
                nom = "Polariscope",
                mesure = "Determines the optic character",
                description = "The stone is viewed between two crossed polarising filters under rotating light: it goes fully dark on every turn if isotropic (garnet, spinel, glass), or alternates light and dark if anisotropic, as most faceted stones are.",
                astuce = "Also reveals internal strain and the doubling of facet edges typical of strong birefringence."
            ),
            GemInstrument(
                nom = "Hand-held spectroscope",
                mesure = "Analyses the absorption spectrum",
                description = "By splitting the light passing through the stone, this instrument reveals absorption bands characteristic of certain chromophore elements (chromium, iron, cobalt...) — a signature sometimes specific to a species or a treatment, such as the chromium lines in ruby and emerald."
            ),
            GemInstrument(
                nom = "10x loupe and binocular microscope",
                mesure = "Examines inclusions",
                description = "Magnification reveals internal inclusions (crystals, fractures, bubbles, growth structures) that give away a stone's natural or synthetic origin, as well as any treatments — glass- or oil-filled fractures, for example.",
                astuce = "The gemologist's most universal tool, often the first step of any examination."
            ),
            GemInstrument(
                nom = "UV lamp (shortwave and longwave)",
                mesure = "Observes fluorescence",
                description = "Some stones emit visible light under invisible ultraviolet radiation; the intensity and colour of this fluorescence, observed in a dark chamber, help identify a species or spot certain treatments and fillings."
            ),
            GemInstrument(
                nom = "Chelsea filter",
                mesure = "Detects chromium",
                description = "This coloured filter only transmits two narrow bands of the spectrum: a green stone containing chromium, such as natural emerald, appears reddish through it, while most imitations stay green.",
                astuce = "A quick, inexpensive test, but always to be confirmed with other methods."
            ),
            GemInstrument(
                nom = "Mohs hardness kit (test points)",
                mesure = "Tests scratch resistance",
                description = "A set of calibrated points of known hardness, from 2 to 9, is carefully applied to an inconspicuous facet; the hardest one that scratches the stone without being scratched by it places its hardness on the Mohs scale.",
                astuce = "A destructive test, to be used only as a last resort and never on an already-cut valuable stone."
            ),
            GemInstrument(
                nom = "Thermal and electrical conductivity tester",
                mesure = "Tells diamond apart from its simulants",
                description = "A probe measures how fast the stone conducts heat away; diamond, an exceptionally good thermal conductor, stands out clearly from simulants like cubic zirconia or glass. Newer models add an electrical conductivity test to unmask moissanite, the only stone that also mimics diamond's thermal conductivity."
            )
        ),
        disclaimerTitle = "A professional practice",
        disclaimerBody = "These instruments require technical training and a properly prepared stone to give reliable results. The app's analysis tool is a guide based on the characteristics you were able to observe or measure; for an official identification or certification, only an accredited gemological laboratory (GIA, Gübelin, GFCO...) is authoritative."
    )

    private val es = GemInstrumentsPage(
        intro = "Más allá del ojo experto, los gemólogos profesionales se apoyan en un puñado de instrumentos normalizados para caracterizar una piedra de forma objetiva. Estas son las herramientas básicas de un laboratorio de gemología, y lo que cada una permite determinar.",
        instruments = listOf(
            GemInstrument(
                nom = "Refractómetro",
                mesure = "Mide el índice de refracción",
                description = "Un líquido de contacto óptico posa la piedra sobre un prisma de vidrio de alto índice; el ángulo de reflexión total interna, leído en una escala graduada, da el índice de refracción — y su variación según la orientación en piedras birrefringentes.",
                astuce = "La herramienta básica de todo gemólogo: rápida y no destructiva, pero limitada a índices inferiores a unos 1,81 — más allá, como en la mayoría de los granates o el diamante, deja de funcionar."
            ),
            GemInstrument(
                nom = "Balanza hidrostática",
                mesure = "Mide la densidad",
                description = "La piedra se pesa en el aire y luego se sumerge en agua; la diferencia entre ambas pesadas, aplicando el principio de Arquímedes, da la densidad relativa — un dato muy discriminante entre especies de apariencia similar.",
                astuce = "Requiere una piedra suelta, sin montar, y una pesada precisa hasta la centésima de quilate."
            ),
            GemInstrument(
                nom = "Dicroscopio",
                mesure = "Revela el pleocroísmo",
                description = "Un pequeño tubo óptico con un cristal de calcita desdobla la luz que atraviesa la piedra en dos imágenes yuxtapuestas; si sus colores difieren, la piedra es pleocroica — un indicio valioso para distinguir, por ejemplo, un zafiro (pleocroico) de una espinela azul (nunca pleocroica)."
            ),
            GemInstrument(
                nom = "Polariscopio",
                mesure = "Determina el carácter óptico",
                description = "La piedra se observa entre dos filtros polarizadores cruzados bajo luz giratoria: se apaga por completo en cada rotación si es isótropa (granate, espinela, vidrio), o alterna claro y oscuro si es anisótropa, como la mayoría de las piedras facetadas.",
                astuce = "También permite detectar tensiones internas y el desdoblamiento de aristas propio de una fuerte birrefringencia."
            ),
            GemInstrument(
                nom = "Espectroscopio de mano",
                mesure = "Analiza el espectro de absorción",
                description = "Al descomponer la luz que atraviesa la piedra, el instrumento revela bandas de absorción características de ciertos elementos cromóforos (cromo, hierro, cobalto...) — una firma a veces propia de una especie o de un tratamiento, como las líneas del cromo en el rubí y la esmeralda."
            ),
            GemInstrument(
                nom = "Lupa 10x y microscopio binocular",
                mesure = "Observa las inclusiones",
                description = "El aumento revela inclusiones internas (cristales, fracturas, burbujas, estructuras de crecimiento) que delatan el origen natural o sintético de una piedra, así como posibles tratamientos — fracturas rellenas de vidrio o aceite, por ejemplo.",
                astuce = "La herramienta más universal del gemólogo, a menudo el primer paso de cualquier examen."
            ),
            GemInstrument(
                nom = "Lámpara UV (onda corta y larga)",
                mesure = "Observa la fluorescencia",
                description = "Algunas piedras emiten luz visible bajo radiación ultravioleta invisible; la intensidad y el color de esta fluorescencia, observados en cámara oscura, ayudan a identificar una especie o a detectar ciertos tratamientos y rellenos."
            ),
            GemInstrument(
                nom = "Filtro de Chelsea",
                mesure = "Detecta el cromo",
                description = "Este filtro coloreado solo deja pasar dos bandas estrechas del espectro: una piedra verde que contiene cromo, como la esmeralda natural, aparece rojiza a través de él, mientras que la mayoría de las imitaciones permanecen verdes.",
                astuce = "Una prueba rápida y económica, pero que siempre debe confirmarse con otros métodos."
            ),
            GemInstrument(
                nom = "Kit de dureza de Mohs (puntas de prueba)",
                mesure = "Prueba la resistencia al rayado",
                description = "Una serie de puntas calibradas de dureza conocida, de 2 a 9, se aplica con cuidado sobre una faceta discreta; la más dura que raya la piedra sin ser rayada por ella sitúa su dureza en la escala de Mohs.",
                astuce = "Una prueba destructiva, reservada como último recurso y nunca sobre una piedra de valor ya tallada."
            ),
            GemInstrument(
                nom = "Probador de conductividad térmica y eléctrica",
                mesure = "Distingue el diamante de sus simulantes",
                description = "Una sonda mide la velocidad a la que la piedra disipa el calor; el diamante, un conductor térmico excepcionalmente bueno, se distingue claramente de simulantes como la circonia cúbica o el vidrio. Los modelos más recientes añaden una prueba de conductividad eléctrica para desenmascarar la moisanita, la única piedra que también imita su conductividad térmica."
            )
        ),
        disclaimerTitle = "Un uso profesional",
        disclaimerBody = "Estos instrumentos requieren formación técnica y una piedra correctamente preparada para dar resultados fiables. La herramienta de análisis de la aplicación es una guía orientativa a partir de las características que haya podido observar o medir; para una identificación o certificación oficial, solo un laboratorio gemológico acreditado (GIA, Gübelin, GFCO...) tiene validez."
    )

    private val it = GemInstrumentsPage(
        intro = "Oltre all'occhio esperto, i gemmologi professionisti si affidano a una manciata di strumenti standardizzati per caratterizzare una pietra in modo oggettivo. Ecco gli strumenti di base di un laboratorio di gemmologia, e cosa permette di determinare ciascuno di essi.",
        instruments = listOf(
            GemInstrument(
                nom = "Rifrattometro",
                mesure = "Misura l'indice di rifrazione",
                description = "Un liquido di contatto ottico appoggia la pietra su un prisma di vetro ad alto indice; l'angolo di riflessione totale interna, letto su una scala graduata, fornisce l'indice di rifrazione — e la sua variazione secondo l'orientamento nelle pietre birifrangenti.",
                astuce = "Lo strumento base di ogni gemmologo: rapido e non distruttivo, ma limitato a indici inferiori a circa 1,81 — oltre, come per la maggior parte dei granati o il diamante, non funziona più."
            ),
            GemInstrument(
                nom = "Bilancia idrostatica",
                mesure = "Misura la densità",
                description = "La pietra viene pesata in aria e poi immersa in acqua; la differenza tra le due pesate, applicando il principio di Archimede, fornisce la densità relativa — un dato molto discriminante tra specie di aspetto simile.",
                astuce = "Richiede una pietra sciolta, non montata, e una pesata precisa al centesimo di carato."
            ),
            GemInstrument(
                nom = "Dicroscopio",
                mesure = "Rivela il pleocroismo",
                description = "Un piccolo tubo ottico dotato di un cristallo di calcite sdoppia la luce che attraversa la pietra in due immagini affiancate; se i loro colori differiscono, la pietra è pleocroica — un indizio prezioso per distinguere, ad esempio, uno zaffiro (pleocroico) da uno spinello blu (mai pleocroico)."
            ),
            GemInstrument(
                nom = "Polariscopio",
                mesure = "Determina il carattere ottico",
                description = "La pietra viene osservata tra due filtri polarizzatori incrociati sotto luce rotante: si spegne completamente a ogni rotazione se è isotropa (granato, spinello, vetro), oppure alterna chiaro e scuro se è anisotropa, come la maggior parte delle pietre sfaccettate.",
                astuce = "Permette anche di individuare tensioni interne e lo sdoppiamento degli spigoli tipico di una forte birifrangenza."
            ),
            GemInstrument(
                nom = "Spettroscopio portatile",
                mesure = "Analizza lo spettro di assorbimento",
                description = "Scomponendo la luce che attraversa la pietra, lo strumento rivela bande di assorbimento caratteristiche di certi elementi cromofori (cromo, ferro, cobalto...) — una firma talvolta propria di una specie o di un trattamento, come le righe del cromo nel rubino e nello smeraldo."
            ),
            GemInstrument(
                nom = "Lente 10x e microscopio binoculare",
                mesure = "Osserva le inclusioni",
                description = "L'ingrandimento rivela le inclusioni interne (cristalli, fratture, bolle, strutture di crescita) che tradiscono l'origine naturale o sintetica di una pietra, oltre a eventuali trattamenti — fratture riempite di vetro o olio, ad esempio.",
                astuce = "Lo strumento più universale del gemmologo, spesso il primo passo di ogni esame."
            ),
            GemInstrument(
                nom = "Lampada UV (onde corte e lunghe)",
                mesure = "Osserva la fluorescenza",
                description = "Alcune pietre emettono luce visibile sotto radiazione ultravioletta invisibile; l'intensità e il colore di questa fluorescenza, osservati in camera oscura, aiutano a identificare una specie o a individuare certi trattamenti e riempimenti."
            ),
            GemInstrument(
                nom = "Filtro di Chelsea",
                mesure = "Rileva il cromo",
                description = "Questo filtro colorato lascia passare solo due strette bande dello spettro: una pietra verde contenente cromo, come lo smeraldo naturale, appare rossastra attraverso di esso, mentre la maggior parte delle imitazioni resta verde.",
                astuce = "Un test rapido ed economico, ma da confermare sempre con altri metodi."
            ),
            GemInstrument(
                nom = "Kit di durezza di Mohs (punte di prova)",
                mesure = "Testa la resistenza alla graffiatura",
                description = "Una serie di punte calibrate di durezza nota, da 2 a 9, viene applicata con cautela su una faccetta discreta; la più dura che graffia la pietra senza esserne graffiata colloca la sua durezza sulla scala di Mohs.",
                astuce = "Un test distruttivo, da riservare come ultima risorsa e mai su una pietra di valore già tagliata."
            ),
            GemInstrument(
                nom = "Tester di conducibilità termica ed elettrica",
                mesure = "Distingue il diamante dai suoi simulanti",
                description = "Una sonda misura la velocità con cui la pietra disperde il calore; il diamante, conduttore termico eccezionalmente buono, si distingue nettamente da simulanti come lo zircone cubico o il vetro. I modelli più recenti aggiungono un test di conducibilità elettrica per smascherare la moissanite, l'unica pietra a imitare anche la sua conducibilità termica."
            )
        ),
        disclaimerTitle = "Un uso professionale",
        disclaimerBody = "Questi strumenti richiedono una formazione tecnica e una pietra correttamente preparata per dare risultati affidabili. Lo strumento di analisi dell'app è una guida orientativa basata sulle caratteristiche che siete riusciti a osservare o misurare; per un'identificazione o una certificazione ufficiale, solo un laboratorio gemmologico accreditato (GIA, Gübelin, GFCO...) fa fede."
    )

    private val de = GemInstrumentsPage(
        intro = "Über das geschulte Auge hinaus stützen sich professionelle Gemmologen auf eine Handvoll standardisierter Instrumente, um einen Stein objektiv zu charakterisieren. Hier sind die Grundwerkzeuge eines gemmologischen Labors und das, was jedes davon bestimmen lässt.",
        instruments = listOf(
            GemInstrument(
                nom = "Refraktometer",
                mesure = "Misst den Brechungsindex",
                description = "Eine optische Kontaktflüssigkeit legt den Stein auf ein Glasprisma mit hohem Brechungsindex; der Winkel der Totalreflexion, abgelesen auf einer Skala, ergibt den Brechungsindex — und dessen Schwankung je nach Ausrichtung bei doppelbrechenden Steinen.",
                astuce = "Das Grundwerkzeug jedes Gemmologen: schnell und zerstörungsfrei, aber begrenzt auf Indizes unter etwa 1,81 — darüber, wie bei den meisten Granaten oder Diamant, funktioniert es nicht mehr."
            ),
            GemInstrument(
                nom = "Hydrostatische Waage",
                mesure = "Misst die Dichte",
                description = "Der Stein wird an der Luft und anschließend in Wasser getaucht gewogen; die Differenz beider Messungen ergibt nach dem archimedischen Prinzip die relative Dichte — ein sehr aussagekräftiger Wert zur Unterscheidung ähnlich aussehender Arten.",
                astuce = "Erfordert einen losen, ungefassten Stein und eine auf ein Hundertstel Karat genaue Wägung."
            ),
            GemInstrument(
                nom = "Dichroskop",
                mesure = "Zeigt den Pleochroismus",
                description = "Ein kleines optisches Rohr mit einem Calcitkristall spaltet das durch den Stein fallende Licht in zwei nebeneinanderliegende Bilder; unterscheiden sich deren Farben, ist der Stein pleochroitisch — ein wertvoller Hinweis, um etwa einen Saphir (pleochroitisch) von einem blauen Spinell (nie pleochroitisch) zu unterscheiden."
            ),
            GemInstrument(
                nom = "Polariskop",
                mesure = "Bestimmt den optischen Charakter",
                description = "Der Stein wird zwischen zwei gekreuzten Polarisationsfiltern unter rotierendem Licht betrachtet: Er verdunkelt sich bei jeder Drehung vollständig, wenn er isotrop ist (Granat, Spinell, Glas), oder wechselt zwischen hell und dunkel, wenn er anisotrop ist — wie die meisten facettierten Steine.",
                astuce = "Zeigt zudem innere Spannungen und die für starke Doppelbrechung typische Kantendopplung."
            ),
            GemInstrument(
                nom = "Handspektroskop",
                mesure = "Analysiert das Absorptionsspektrum",
                description = "Durch Zerlegung des durch den Stein fallenden Lichts zeigt das Instrument Absorptionsbanden, die für bestimmte farbgebende Elemente (Chrom, Eisen, Kobalt...) charakteristisch sind — eine Signatur, die manchmal einer bestimmten Art oder Behandlung eigen ist, wie die Chromlinien in Rubin und Smaragd."
            ),
            GemInstrument(
                nom = "10x-Lupe und Stereomikroskop",
                mesure = "Untersucht Einschlüsse",
                description = "Die Vergrößerung offenbart innere Einschlüsse (Kristalle, Risse, Blasen, Wachstumsstrukturen), die die natürliche oder synthetische Herkunft eines Steins sowie mögliche Behandlungen verraten — etwa mit Glas oder Öl gefüllte Risse.",
                astuce = "Das universellste Werkzeug des Gemmologen, oft der erste Schritt jeder Untersuchung."
            ),
            GemInstrument(
                nom = "UV-Lampe (kurz- und langwellig)",
                mesure = "Beobachtet die Fluoreszenz",
                description = "Manche Steine senden unter unsichtbarer Ultraviolettstrahlung sichtbares Licht aus; Intensität und Farbe dieser Fluoreszenz, in einer Dunkelkammer beobachtet, helfen, eine Art zu bestimmen oder bestimmte Behandlungen und Füllungen zu erkennen."
            ),
            GemInstrument(
                nom = "Chelsea-Filter",
                mesure = "Weist Chrom nach",
                description = "Dieser Farbfilter lässt nur zwei schmale Spektralbänder durch: Ein grüner, chromhaltiger Stein wie ein natürlicher Smaragd erscheint dadurch rötlich, während die meisten Imitationen grün bleiben.",
                astuce = "Ein schneller, kostengünstiger Test, der aber stets durch andere Methoden bestätigt werden sollte."
            ),
            GemInstrument(
                nom = "Mohs-Härteset (Prüfspitzen)",
                mesure = "Prüft die Kratzfestigkeit",
                description = "Eine Reihe kalibrierter Prüfspitzen bekannter Härte, von 2 bis 9, wird vorsichtig auf eine unauffällige Facette aufgesetzt; die härteste Spitze, die den Stein ritzt, ohne selbst geritzt zu werden, verortet seine Härte auf der Mohs-Skala.",
                astuce = "Ein zerstörender Test, der letzten Fällen vorbehalten bleibt und niemals an einem bereits geschliffenen, wertvollen Stein angewendet werden sollte."
            ),
            GemInstrument(
                nom = "Wärme- und Elektroleitfähigkeitstester",
                mesure = "Unterscheidet Diamant von seinen Simulanten",
                description = "Eine Sonde misst, wie schnell der Stein Wärme ableitet; Diamant, ein außergewöhnlich guter Wärmeleiter, hebt sich deutlich von Simulanten wie kubischem Zirkonia oder Glas ab. Neuere Geräte ergänzen einen Elektroleitfähigkeitstest, um Moissanit zu entlarven — den einzigen Stein, der auch die Wärmeleitfähigkeit von Diamant nachahmt."
            )
        ),
        disclaimerTitle = "Ein professioneller Einsatz",
        disclaimerBody = "Diese Instrumente erfordern eine technische Schulung und einen korrekt vorbereiteten Stein, um zuverlässige Ergebnisse zu liefern. Das Analysewerkzeug der App ist eine Orientierungshilfe auf Basis der von Ihnen beobachteten oder gemessenen Merkmale; für eine offizielle Identifikation oder Zertifizierung ist ausschließlich ein akkreditiertes gemmologisches Labor (GIA, Gübelin, GFCO...) maßgeblich."
    )

    private val pt = GemInstrumentsPage(
        intro = "Para além do olhar treinado, os gemólogos profissionais recorrem a um punhado de instrumentos normalizados para caracterizar uma pedra de forma objetiva. Eis as ferramentas de base de um laboratório de gemologia, e o que cada uma permite determinar.",
        instruments = listOf(
            GemInstrument(
                nom = "Refratómetro",
                mesure = "Mede o índice de refração",
                description = "Um líquido de contacto óptico apoia a pedra sobre um prisma de vidro de alto índice; o ângulo de reflexão total interna, lido numa escala graduada, dá o índice de refração — e a sua variação consoante a orientação nas pedras birrefringentes.",
                astuce = "A ferramenta básica de todo gemólogo: rápida e não destrutiva, mas limitada a índices inferiores a cerca de 1,81 — além disso, como na maioria dos granadas ou no diamante, deixa de funcionar."
            ),
            GemInstrument(
                nom = "Balança hidrostática",
                mesure = "Mede a densidade",
                description = "A pedra é pesada ao ar e depois imersa em água; a diferença entre as duas pesagens, aplicando o princípio de Arquimedes, dá a densidade relativa — um dado muito discriminante entre espécies de aparência semelhante.",
                astuce = "Requer uma pedra solta, não montada, e uma pesagem precisa ao centésimo de quilate."
            ),
            GemInstrument(
                nom = "Dicroscópio",
                mesure = "Revela o pleocroísmo",
                description = "Um pequeno tubo óptico equipado com um cristal de calcite desdobra a luz que atravessa a pedra em duas imagens justapostas; se as suas cores diferirem, a pedra é pleocroica — um indício valioso para distinguir, por exemplo, uma safira (pleocroica) de uma espinela azul (nunca pleocroica)."
            ),
            GemInstrument(
                nom = "Polariscópio",
                mesure = "Determina o carácter óptico",
                description = "A pedra é observada entre dois filtros polarizadores cruzados sob luz rotativa: apaga-se totalmente a cada rotação se for isótropa (granada, espinela, vidro), ou alterna entre claro e escuro se for anisótropa, como a maioria das pedras facetadas.",
                astuce = "Permite também detetar tensões internas e o desdobramento de arestas característico de uma forte birrefringência."
            ),
            GemInstrument(
                nom = "Espectroscópio manual",
                mesure = "Analisa o espectro de absorção",
                description = "Ao decompor a luz que atravessa a pedra, o instrumento revela bandas de absorção características de certos elementos cromóforos (crómio, ferro, cobalto...) — uma assinatura por vezes própria de uma espécie ou de um tratamento, como as linhas do crómio no rubi e na esmeralda."
            ),
            GemInstrument(
                nom = "Lupa 10x e microscópio binocular",
                mesure = "Observa as inclusões",
                description = "A ampliação revela as inclusões internas (cristais, fraturas, bolhas, estruturas de crescimento) que denunciam a origem natural ou sintética de uma pedra, bem como eventuais tratamentos — fraturas preenchidas com vidro ou óleo, por exemplo.",
                astuce = "A ferramenta mais universal do gemólogo, frequentemente o primeiro passo de qualquer exame."
            ),
            GemInstrument(
                nom = "Lâmpada UV (ondas curtas e longas)",
                mesure = "Observa a fluorescência",
                description = "Algumas pedras emitem luz visível sob radiação ultravioleta invisível; a intensidade e a cor dessa fluorescência, observadas em câmara escura, ajudam a identificar uma espécie ou a detetar certos tratamentos e preenchimentos."
            ),
            GemInstrument(
                nom = "Filtro de Chelsea",
                mesure = "Deteta o crómio",
                description = "Este filtro colorido só deixa passar duas bandas estreitas do espetro: uma pedra verde que contém crómio, como a esmeralda natural, aparece avermelhada através dele, enquanto a maioria das imitações permanece verde.",
                astuce = "Um teste rápido e pouco dispendioso, mas sempre a confirmar por outros métodos."
            ),
            GemInstrument(
                nom = "Kit de dureza de Mohs (pontas de teste)",
                mesure = "Testa a resistência ao risco",
                description = "Uma série de pontas calibradas de dureza conhecida, de 2 a 9, é aplicada com cuidado numa faceta discreta; a mais dura que risca a pedra sem ser riscada por ela situa a sua dureza na escala de Mohs.",
                astuce = "Um teste destrutivo, a reservar como último recurso e nunca sobre uma pedra de valor já lapidada."
            ),
            GemInstrument(
                nom = "Testador de condutividade térmica e elétrica",
                mesure = "Distingue o diamante dos seus simulantes",
                description = "Uma sonda mede a velocidade a que a pedra dissipa o calor; o diamante, condutor térmico excecionalmente bom, destaca-se claramente de simulantes como a zircónia cúbica ou o vidro. Os modelos mais recentes acrescentam um teste de condutividade elétrica para desmascarar a moissanite, a única pedra que também imita a sua condutividade térmica."
            )
        ),
        disclaimerTitle = "Um uso profissional",
        disclaimerBody = "Estes instrumentos exigem formação técnica e uma pedra corretamente preparada para dar resultados fiáveis. A ferramenta de análise da aplicação é um guia orientativo a partir das características que conseguiu observar ou medir; para uma identificação ou certificação oficial, apenas um laboratório gemológico acreditado (GIA, Gübelin, GFCO...) faz fé."
    )

    private val zh = GemInstrumentsPage(
        intro = "除了训练有素的眼力，专业宝石学家还依靠一系列标准化仪器来客观地鉴定宝石特征。以下是宝石学实验室的基本工具，以及每种仪器能够测定的内容。",
        instruments = listOf(
            GemInstrument(
                nom = "折射仪",
                mesure = "测量折射率",
                description = "光学接触液使宝石贴合在高折射率玻璃棱镜上；在刻度盘上读取的全内反射角度即可得出折射率——对于双折射宝石，还能读出其随方向变化的数值。",
                astuce = "每位宝石学家的基本工具：快速且无损，但仅适用于折射率约低于1.81的宝石——超过此范围，如大多数石榴石或钻石，仪器便不再适用。"
            ),
            GemInstrument(
                nom = "静水天平",
                mesure = "测量密度",
                description = "先在空气中称量宝石，再将其浸入水中称量；两次读数之差依据阿基米德原理换算出比重——这是区分外观相近品种的重要判据。",
                astuce = "需要一颗未镶嵌的裸石，以及精确到百分之一克拉的称量。"
            ),
            GemInstrument(
                nom = "二色镜",
                mesure = "揭示多色性",
                description = "一根装有方解石晶体的小型光学镜筒，将穿过宝石的光分裂为两幅并列的图像；若两者颜色不同，则该宝石具有多色性——这是区分例如蓝宝石（具多色性）与蓝色尖晶石（从不具多色性）的宝贵线索。"
            ),
            GemInstrument(
                nom = "偏光镜",
                mesure = "判定光学性质",
                description = "宝石在旋转光线下置于两片交叉的偏振滤光片之间观察：若为均质体（如石榴石、尖晶石、玻璃），每次旋转均会完全变暗；若为非均质体——如大多数刻面宝石——则会明暗交替。",
                astuce = "还可用于发现内部应力，以及强双折射所特有的刻面棱线重影现象。"
            ),
            GemInstrument(
                nom = "手持分光镜",
                mesure = "分析吸收光谱",
                description = "通过分解穿过宝石的光线，该仪器能揭示某些致色元素（铬、铁、钴等）所特有的吸收谱带——这种特征有时是某一品种或处理方式所独有的，例如红宝石和祖母绿中的铬吸收线。"
            ),
            GemInstrument(
                nom = "10倍放大镜与双目显微镜",
                mesure = "观察内含物",
                description = "放大观察可揭示内部内含物（晶体、裂隙、气泡、生长结构），从而判断宝石的天然或合成来源，以及可能的处理痕迹——例如充填玻璃或油类的裂隙。",
                astuce = "宝石学家最通用的工具，通常是任何鉴定的第一步。"
            ),
            GemInstrument(
                nom = "紫外灯（短波与长波）",
                mesure = "观察荧光",
                description = "某些宝石在不可见的紫外线照射下会发出可见光；在暗室中观察这种荧光的强度和颜色，有助于鉴定品种或发现某些处理和填充痕迹。"
            ),
            GemInstrument(
                nom = "切尔西滤色镜",
                mesure = "检测铬元素",
                description = "这种彩色滤镜只允许光谱中两条狭窄谱带通过：含铬的绿色宝石，如天然祖母绿，透过它会呈现偏红色，而大多数仿制品则仍呈绿色。",
                astuce = "一种快速且成本低廉的测试，但仍需通过其他方法加以确认。"
            ),
            GemInstrument(
                nom = "莫氏硬度测试套件（测试针）",
                mesure = "测试抗刮擦能力",
                description = "一套硬度已知、从2到9校准的测试针，小心地施加于不显眼的刻面上；能刻划宝石而自身不被刻划的最硬测试针，即可确定其在莫氏硬度表上的位置。",
                astuce = "一种破坏性测试，应作为最后手段使用，绝不可用于已切割的贵重宝石。"
            ),
            GemInstrument(
                nom = "热导率与电导率测试仪",
                mesure = "区分钻石与其仿制品",
                description = "探头测量宝石导热的速度；钻石作为异常优良的热导体，与立方氧化锆或玻璃等仿制品明显区分开来。较新型号还增加了电导率测试，以识别莫桑石——唯一一种同时模仿钻石热导率的宝石。"
            )
        ),
        disclaimerTitle = "专业用途",
        disclaimerBody = "这些仪器需要专业技术培训和正确制备的宝石样本才能给出可靠结果。应用程序中的分析工具是基于您所能观察或测量的特征提供的参考指引；如需官方鉴定或证书，唯有经认可的宝石学实验室（GIA、Gübelin、GFCO等）出具的结果具有权威性。"
    )

    private val ru = GemInstrumentsPage(
        intro = "Помимо натренированного глаза, профессиональные геммологи опираются на ряд стандартизированных приборов для объективной характеристики камня. Вот базовые инструменты геммологической лаборатории и то, что каждый из них позволяет определить.",
        instruments = listOf(
            GemInstrument(
                nom = "Рефрактометр",
                mesure = "Измеряет показатель преломления",
                description = "Оптическая контактная жидкость помещает камень на стеклянную призму с высоким показателем преломления; угол полного внутреннего отражения, считанный по градуированной шкале, даёт показатель преломления — и его изменение в зависимости от ориентации для двупреломляющих камней.",
                astuce = "Базовый инструмент каждого геммолога: быстрый и неразрушающий, но ограниченный показателями ниже примерно 1,81 — выше этого значения, как у большинства гранатов или алмаза, он перестаёт работать."
            ),
            GemInstrument(
                nom = "Гидростатические весы",
                mesure = "Измеряют плотность",
                description = "Камень взвешивают на воздухе, затем погружают в воду; разница между двумя показаниями, применённая к принципу Архимеда, даёт относительную плотность — весьма показательный параметр для различения внешне схожих видов.",
                astuce = "Требует несомненного, неоправленного камня и взвешивания с точностью до сотой карата."
            ),
            GemInstrument(
                nom = "Дихроскоп",
                mesure = "Выявляет плеохроизм",
                description = "Небольшая оптическая трубка с кристаллом кальцита разделяет проходящий через камень свет на два расположенных рядом изображения; если их цвета различаются, камень плеохроичен — ценный признак для отличия, например, сапфира (плеохроичного) от синей шпинели (никогда не плеохроичной)."
            ),
            GemInstrument(
                nom = "Полярископ",
                mesure = "Определяет оптический характер",
                description = "Камень рассматривают между двумя скрещенными поляризационными фильтрами при вращающемся освещении: он полностью темнеет при каждом повороте, если он изотропен (гранат, шпинель, стекло), либо чередует светлые и тёмные участки, если анизотропен, как большинство огранённых камней.",
                astuce = "Также позволяет выявить внутренние напряжения и раздвоение граней, характерное для сильного двупреломления."
            ),
            GemInstrument(
                nom = "Ручной спектроскоп",
                mesure = "Анализирует спектр поглощения",
                description = "Разлагая свет, проходящий через камень, этот прибор выявляет полосы поглощения, характерные для определённых хромофорных элементов (хрома, железа, кобальта...) — иногда эта сигнатура характерна именно для конкретного вида или облагораживания, как линии хрома в рубине и изумруде."
            ),
            GemInstrument(
                nom = "Лупа 10x и бинокулярный микроскоп",
                mesure = "Изучает включения",
                description = "Увеличение выявляет внутренние включения (кристаллы, трещины, пузырьки, структуры роста), которые указывают на природное или синтетическое происхождение камня, а также на возможное облагораживание — например, трещины, заполненные стеклом или маслом.",
                astuce = "Самый универсальный инструмент геммолога, часто первый шаг любого исследования."
            ),
            GemInstrument(
                nom = "УФ-лампа (коротковолновая и длинноволновая)",
                mesure = "Наблюдает флуоресценцию",
                description = "Некоторые камни излучают видимый свет под воздействием невидимого ультрафиолетового излучения; интенсивность и цвет этой флуоресценции, наблюдаемые в тёмной камере, помогают определить вид камня или выявить некоторые виды облагораживания и заполнения."
            ),
            GemInstrument(
                nom = "Фильтр Челси",
                mesure = "Выявляет хром",
                description = "Этот цветной фильтр пропускает лишь две узкие полосы спектра: зелёный камень, содержащий хром, например природный изумруд, кажется через него красноватым, тогда как большинство имитаций остаются зелёными.",
                astuce = "Быстрый и недорогой тест, который, однако, всегда следует подтверждать другими методами."
            ),
            GemInstrument(
                nom = "Набор для определения твёрдости по Моосу (пробные иглы)",
                mesure = "Проверяет устойчивость к царапинам",
                description = "Набор калиброванных игл известной твёрдости, от 2 до 9, осторожно прикладывают к неприметной грани; самая твёрдая игла, которая царапает камень, сама не будучи им поцарапана, определяет его твёрдость по шкале Мооса.",
                astuce = "Разрушающий тест, применяемый лишь в крайнем случае и никогда — на уже огранённом ценном камне."
            ),
            GemInstrument(
                nom = "Прибор для измерения тепло- и электропроводности",
                mesure = "Отличает алмаз от его имитаций",
                description = "Датчик измеряет скорость, с которой камень отводит тепло; алмаз, исключительно хороший проводник тепла, чётко выделяется на фоне имитаций, таких как кубический цирконий или стекло. Более новые модели дополнительно проверяют электропроводность, чтобы разоблачить муассанит — единственный камень, имитирующий также теплопроводность алмаза."
            )
        ),
        disclaimerTitle = "Профессиональное применение",
        disclaimerBody = "Эти приборы требуют технической подготовки и правильно подготовленного камня для получения достоверных результатов. Инструмент анализа приложения служит ориентировочным руководством на основе характеристик, которые вы смогли наблюдать или измерить; для официальной идентификации или сертификации авторитетным является только аккредитованная геммологическая лаборатория (GIA, Gübelin, GFCO...)."
    )

    private val nl = GemInstrumentsPage(
        intro = "Naast het geoefende oog vertrouwen professionele gemmologen op een handvol gestandaardiseerde instrumenten om een steen objectief te karakteriseren. Hier zijn de basisinstrumenten van een gemmologisch laboratorium, en wat elk ervan kan bepalen.",
        instruments = listOf(
            GemInstrument(
                nom = "Refractometer",
                mesure = "Meet de brekingsindex",
                description = "Een optische contactvloeistof legt de steen op een glazen prisma met hoge brekingsindex; de hoek van totale interne reflectie, afgelezen op een schaalverdeling, geeft de brekingsindex — en de variatie ervan naargelang de oriëntatie bij dubbelbrekende stenen.",
                astuce = "Het basisinstrument van elke gemmoloog: snel en niet-destructief, maar beperkt tot indices onder ongeveer 1,81 — daarboven, zoals bij de meeste granaten of diamant, werkt het niet meer."
            ),
            GemInstrument(
                nom = "Hydrostatische weegschaal",
                mesure = "Meet de dichtheid",
                description = "De steen wordt in de lucht gewogen en vervolgens in water ondergedompeld; het verschil tussen de twee wegingen, toegepast op het principe van Archimedes, geeft het soortelijk gewicht — een sterk onderscheidend gegeven tussen soorten met een vergelijkbaar uiterlijk.",
                astuce = "Vereist een losse, ongezette steen en een weging nauwkeurig tot op een honderdste karaat."
            ),
            GemInstrument(
                nom = "Dichroscoop",
                mesure = "Onthult pleochroïsme",
                description = "Een kleine optische buis met een calcietkristal splitst het licht dat door de steen valt in twee naast elkaar liggende beelden; als hun kleuren verschillen, is de steen pleochroïtisch — een waardevolle aanwijzing om bijvoorbeeld een saffier (pleochroïtisch) van een blauwe spinel (nooit pleochroïtisch) te onderscheiden."
            ),
            GemInstrument(
                nom = "Polariscoop",
                mesure = "Bepaalt het optische karakter",
                description = "De steen wordt bekeken tussen twee gekruiste polarisatiefilters onder draaiend licht: hij wordt bij elke draaiing volledig donker als hij isotroop is (granaat, spinel, glas), of wisselt tussen licht en donker als hij anisotroop is, zoals de meeste geslepen stenen.",
                astuce = "Maakt het ook mogelijk interne spanningen en de verdubbeling van facetranden, kenmerkend voor sterke dubbele breking, op te sporen."
            ),
            GemInstrument(
                nom = "Handspectroscoop",
                mesure = "Analyseert het absorptiespectrum",
                description = "Door het licht dat door de steen valt te ontleden, onthult dit instrument absorptiebanden die kenmerkend zijn voor bepaalde kleurgevende elementen (chroom, ijzer, kobalt...) — een signatuur die soms specifiek is voor een soort of een behandeling, zoals de chroomlijnen in robijn en smaragd."
            ),
            GemInstrument(
                nom = "10x loep en binoculaire microscoop",
                mesure = "Onderzoekt insluitsels",
                description = "De vergroting onthult interne insluitsels (kristallen, breuken, bellen, groeistructuren) die de natuurlijke of synthetische oorsprong van een steen verraden, evenals eventuele behandelingen — bijvoorbeeld met glas of olie gevulde breuken.",
                astuce = "Het meest universele instrument van de gemmoloog, vaak de eerste stap van elk onderzoek."
            ),
            GemInstrument(
                nom = "UV-lamp (kortgolvig en langgolvig)",
                mesure = "Observeert de fluorescentie",
                description = "Sommige stenen zenden zichtbaar licht uit onder onzichtbare ultraviolette straling; de intensiteit en kleur van deze fluorescentie, waargenomen in een donkere kamer, helpen een soort te identificeren of bepaalde behandelingen en vullingen op te sporen."
            ),
            GemInstrument(
                nom = "Chelsea-filter",
                mesure = "Detecteert chroom",
                description = "Dit gekleurde filter laat slechts twee smalle banden van het spectrum door: een groene steen met chroom, zoals een natuurlijke smaragd, lijkt erdoor roodachtig, terwijl de meeste imitaties groen blijven.",
                astuce = "Een snelle, goedkope test, maar altijd te bevestigen met andere methoden."
            ),
            GemInstrument(
                nom = "Mohs-hardheidsset (testpunten)",
                mesure = "Test de krasvastheid",
                description = "Een reeks gekalibreerde punten van bekende hardheid, van 2 tot 9, wordt voorzichtig op een onopvallend facet aangebracht; de hardste punt die de steen kan krassen zonder er zelf door gekrast te worden, plaatst de hardheid ervan op de Mohs-schaal.",
                astuce = "Een destructieve test, voorbehouden aan het uiterste geval en nooit toe te passen op een reeds geslepen waardevolle steen."
            ),
            GemInstrument(
                nom = "Warmte- en elektrische-geleidbaarheidstester",
                mesure = "Onderscheidt diamant van zijn simulanten",
                description = "Een sonde meet hoe snel de steen warmte afvoert; diamant, een uitzonderlijk goede warmtegeleider, onderscheidt zich duidelijk van simulanten zoals kubisch zirkonia of glas. Nieuwere modellen voegen een test voor elektrische geleidbaarheid toe om moissaniet te ontmaskeren, de enige steen die ook de warmtegeleiding van diamant nabootst."
            )
        ),
        disclaimerTitle = "Een professioneel gebruik",
        disclaimerBody = "Deze instrumenten vereisen technische scholing en een correct voorbereide steen om betrouwbare resultaten te geven. Het analyse-instrument van de app is een oriënterende gids op basis van de kenmerken die u hebt kunnen observeren of meten; voor een officiële identificatie of certificering is alleen een erkend gemmologisch laboratorium (GIA, Gübelin, GFCO...) gezaghebbend."
    )

    private val byLanguage: Map<String, GemInstrumentsPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): GemInstrumentsPage = byLanguage[languageCode] ?: fr
}
