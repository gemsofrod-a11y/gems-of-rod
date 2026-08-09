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

    private val byLanguage: Map<String, GemInstrumentsPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de
    )

    fun page(languageCode: String): GemInstrumentsPage = byLanguage[languageCode] ?: fr
}
