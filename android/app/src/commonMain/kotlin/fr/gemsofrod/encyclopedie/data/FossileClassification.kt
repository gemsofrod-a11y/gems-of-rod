package fr.gemsofrod.encyclopedie.data

data class FossileFamilyExplainer(
    val nom: String,
    val sousTypes: String,
    val description: String
)

data class FossileClassificationPage(
    val intro: String,
    val familles: List<FossileFamilyExplainer>,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique présentant la classification générale des
 * fossiles (mollusques, arthropodes, vertébrés, végétaux, résines, coraux et
 * échinodermes) et leurs principales sous-catégories, traduit dans les
 * langues de l'app indépendamment des fiches individuelles. Complète la
 * section "Fossiles" en expliquant le vocabulaire utilisé sur chaque fiche
 * (période géologique, minéralisation, taillabilité...).
 *
 */
object FossileClassificationInfo {
    private val fr = FossileClassificationPage(
        intro = "On classe les fossiles d'abord par nature de l'organisme d'origine — coquillage, arthropode, vertébré, végétal, résine ou colonie corallienne — puis par mode de fossilisation, qui détermine directement leur intérêt pour un lapidaire. Une coquille remplacée par de la silice se taille et se polit comme une pierre fine ; une empreinte carbonée sur schiste reste une pièce de collection à préserver telle quelle.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammonites & mollusques",
                sousTypes = "Ammonites, ammolite, orthocères, goniatites, bélemnites",
                description = "Coquilles et rostres de céphalopodes marins aujourd'hui éteints, apparentés au nautile et au calmar actuels. Leur coquille calcaire se prête bien au sciage et au polissage ; l'ammolite, variété rarissime à nacre d'aragonite préservée, est même officiellement reconnue comme gemme organique à part entière."
            ),
            FossileFamilyExplainer(
                nom = "Trilobites & arthropodes",
                sousTypes = "Elrathia, Phacops, Calymene et des centaines d'autres genres, classés par ordre (Ptychopariida, Phacopida...)",
                description = "Arthropodes marins disparus à la fin du Permien, dont le corps segmenté en trois lobes (d'où leur nom) et parfois les yeux composés se sont fossilisés dans le calcaire ou le schiste. Rarement taillés, ils sont surtout préparés à la main pour être dégagés de leur roche encaissante et vendus comme pièces de collection."
            ),
            FossileFamilyExplainer(
                nom = "Vertébrés",
                sousTypes = "Dents de requin et de mosasaure, os de dinosaure agatisé, poissons fossiles, ivoire de mammouth",
                description = "Restes d'animaux à squelette interne, du requin géant mégalodon au mammouth laineux. Certains tissus osseux, remplacés molécule par molécule par de la silice colorée (\"gembone\"), deviennent de véritables gemmes ornementales ; d'autres, comme les dents ou l'ivoire, sont travaillés bruts ou sertis tels quels."
            ),
            FossileFamilyExplainer(
                nom = "Végétaux",
                sousTypes = "Bois pétrifié, fougères et autres empreintes végétales du Carbonifère",
                description = "Troncs, feuilles et fougères d'anciennes forêts, préservés soit par silicification complète (bois pétrifié, taillé comme une agate), soit sous forme de simple empreinte carbonée sur schiste, conservée intacte plutôt que travaillée."
            ),
            FossileFamilyExplainer(
                nom = "Ambre & insectes",
                sousTypes = "Ambre baltique, ambre à inclusion, copal (résine subfossile plus jeune)",
                description = "Résine végétale fossilisée, l'une des plus anciennes gemmes organiques travaillées par l'humanité. Sa transparence naturelle piège parfois des insectes ou de petits organismes, ce qui décuple sa valeur scientifique et esthétique. Le copal, résine plus jeune n'ayant pas terminé sa polymérisation, lui est parfois abusivement substitué sur le marché."
            ),
            FossileFamilyExplainer(
                nom = "Coraux & échinodermes",
                sousTypes = "Corail fossile agatisé, oursins fossiles, crinoïdes (entroques), stromatolites",
                description = "Organismes marins coloniaux ou fixés au fond de l'océan. Le corail fossile agatisé, entièrement silicifié, se taille en cabochons aux motifs floraux uniques ; oursins, crinoïdes et stromatolites restent généralement appréciés bruts ou en dalles polies pour leur structure naturelle."
            )
        ),
        disclaimerTitle = "Authenticité et provenance",
        disclaimerBody = "Comme celui des pierres précieuses et des météorites, le marché des fossiles attire son lot de reconstitutions, de moulages et de pièces recollées ou surpréparées pour paraître plus complètes qu'elles ne le sont réellement. Pour un achat de collection, privilégiez toujours une provenance documentée, et vérifiez si une réglementation locale encadre l'exportation (de nombreux pays, dont le Maroc et les États-Unis, restreignent la sortie de certains fossiles rares ou de vertébrés). Les prix indiqués dans cette section sont purement indicatifs : ils varient énormément selon la taille de la pièce, la qualité de préparation et l'état du marché."
    )

    private val en = FossileClassificationPage(
        intro = "Fossils are first classified by the nature of the original organism — shell, arthropod, vertebrate, plant, resin or coral colony — and then by mode of fossilisation, which directly determines their interest to a lapidary. A shell replaced by silica can be cut and polished like a semi-precious stone; a carbon film imprint on shale remains a collector's piece to be preserved as found.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammonites & mollusks",
                sousTypes = "Ammonites, ammolite, orthoceras, goniatites, belemnites",
                description = "Shells and guards of now-extinct marine cephalopods related to today's nautilus and squid. Their calcareous shell lends itself well to sawing and polishing; ammolite, an extremely rare variety with preserved aragonite nacre, is even officially recognised as an organic gemstone in its own right."
            ),
            FossileFamilyExplainer(
                nom = "Trilobites & arthropods",
                sousTypes = "Elrathia, Phacops, Calymene and hundreds of other genera, classified by order (Ptychopariida, Phacopida...)",
                description = "Marine arthropods that vanished at the end of the Permian, whose body segmented into three lobes (hence their name) and sometimes compound eyes fossilised in limestone or shale. Rarely cut, they are mostly hand-prepared to free them from their host rock and sold as collector's pieces."
            ),
            FossileFamilyExplainer(
                nom = "Vertebrates",
                sousTypes = "Shark and mosasaur teeth, agatised dinosaur bone, fossil fish, mammoth ivory",
                description = "Remains of animals with an internal skeleton, from the giant megalodon shark to the woolly mammoth. Some bone tissue, replaced molecule by molecule with coloured silica (\"gembone\"), becomes a true ornamental gemstone; others, such as teeth or ivory, are worked raw or set as-is."
            ),
            FossileFamilyExplainer(
                nom = "Plants",
                sousTypes = "Petrified wood, ferns and other plant imprints from the Carboniferous",
                description = "Trunks, leaves and ferns of ancient forests, preserved either by complete silicification (petrified wood, cut like agate) or as a simple carbon film imprint on shale, kept intact rather than worked."
            ),
            FossileFamilyExplainer(
                nom = "Amber & insects",
                sousTypes = "Baltic amber, amber with inclusions, copal (younger sub-fossil resin)",
                description = "Fossilised plant resin, one of the oldest organic gemstones worked by humankind. Its natural transparency sometimes traps insects or small organisms, greatly increasing its scientific and aesthetic value. Copal, a younger resin that has not finished polymerising, is sometimes wrongly substituted for it on the market."
            ),
            FossileFamilyExplainer(
                nom = "Corals & echinoderms",
                sousTypes = "Agatised fossil coral, fossil sea urchins, crinoids (entroques), stromatolites",
                description = "Colonial marine organisms or those fixed to the ocean floor. Agatised fossil coral, fully silicified, is cut into cabochons with unique floral patterns; sea urchins, crinoids and stromatolites are generally prized raw or as polished slabs for their natural structure."
            )
        ),
        disclaimerTitle = "Authenticity and provenance",
        disclaimerBody = "Like the gemstone and meteorite markets, the fossil market attracts its share of reconstructions, casts, and pieces glued together or over-prepared to look more complete than they really are. For a collector's purchase, always favour documented provenance, and check whether local regulations govern export (many countries, including Morocco and the United States, restrict the export of certain rare fossils or vertebrates). Prices given in this section are purely indicative: they vary enormously with the size of the piece, preparation quality and market conditions."
    )

    private val es = FossileClassificationPage(
        intro = "Los fósiles se clasifican ante todo por la naturaleza del organismo de origen —concha, artrópodo, vertebrado, vegetal, resina o colonia coralina— y después por el modo de fosilización, que determina directamente su interés para un lapidario. Una concha reemplazada por sílice se puede cortar y pulir como una piedra fina; una impronta carbonosa sobre esquisto sigue siendo una pieza de colección que debe conservarse tal cual.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Amonites y moluscos",
                sousTypes = "Amonites, ammolita, ortoceras, goniatites, belemnites",
                description = "Conchas y rostros de cefalópodos marinos hoy extintos, emparentados con el nautilo y el calamar actuales. Su concha calcárea se presta bien al corte y al pulido; la ammolita, variedad rarísima con nácar de aragonito preservado, está incluso reconocida oficialmente como gema orgánica por derecho propio."
            ),
            FossileFamilyExplainer(
                nom = "Trilobites y artrópodos",
                sousTypes = "Elrathia, Phacops, Calymene y cientos de otros géneros, clasificados por orden (Ptychopariida, Phacopida...)",
                description = "Artrópodos marinos extinguidos a finales del Pérmico, cuyo cuerpo segmentado en tres lóbulos (de ahí su nombre) y a veces sus ojos compuestos se fosilizaron en caliza o esquisto. Rara vez se cortan; se preparan sobre todo a mano para liberarlos de su roca matriz y venderlos como piezas de colección."
            ),
            FossileFamilyExplainer(
                nom = "Vertebrados",
                sousTypes = "Dientes de tiburón y de mosasaurio, hueso de dinosaurio agatizado, peces fósiles, marfil de mamut",
                description = "Restos de animales con esqueleto interno, desde el gigantesco tiburón megalodón hasta el mamut lanudo. Algunos tejidos óseos, reemplazados molécula a molécula por sílice coloreada (\"gembone\"), se convierten en auténticas gemas ornamentales; otros, como los dientes o el marfil, se trabajan en bruto o se engastan tal cual."
            ),
            FossileFamilyExplainer(
                nom = "Vegetales",
                sousTypes = "Madera petrificada, helechos y otras improntas vegetales del Carbonífero",
                description = "Troncos, hojas y helechos de antiguos bosques, preservados ya sea por silicificación completa (madera petrificada, cortada como un ágata), ya sea como una simple impronta carbonosa sobre esquisto, conservada intacta en lugar de trabajada."
            ),
            FossileFamilyExplainer(
                nom = "Ámbar e insectos",
                sousTypes = "Ámbar báltico, ámbar con inclusiones, copal (resina subfósil más joven)",
                description = "Resina vegetal fosilizada, una de las gemas orgánicas más antiguas trabajadas por la humanidad. Su transparencia natural a veces atrapa insectos u organismos pequeños, lo que multiplica su valor científico y estético. El copal, resina más joven que no ha terminado de polimerizarse, se le sustituye a veces indebidamente en el mercado."
            ),
            FossileFamilyExplainer(
                nom = "Corales y equinodermos",
                sousTypes = "Coral fósil agatizado, erizos de mar fósiles, crinoideos (entroques), estromatolitos",
                description = "Organismos marinos coloniales o fijados al fondo oceánico. El coral fósil agatizado, totalmente silicificado, se corta en cabujones con motivos florales únicos; erizos, crinoideos y estromatolitos suelen apreciarse en bruto o en losas pulidas por su estructura natural."
            )
        ),
        disclaimerTitle = "Autenticidad y procedencia",
        disclaimerBody = "Al igual que el de las piedras preciosas y los meteoritos, el mercado de fósiles atrae su cuota de reconstrucciones, moldes y piezas pegadas o sobrepreparadas para parecer más completas de lo que realmente son. Para una compra de colección, priorice siempre una procedencia documentada y verifique si alguna normativa local regula la exportación (numerosos países, entre ellos Marruecos y Estados Unidos, restringen la salida de ciertos fósiles raros o vertebrados). Los precios indicados en esta sección son puramente orientativos: varían enormemente según el tamaño de la pieza, la calidad de la preparación y el estado del mercado."
    )

    private val it = FossileClassificationPage(
        intro = "I fossili si classificano innanzitutto in base alla natura dell'organismo di origine — conchiglia, artropode, vertebrato, vegetale, resina o colonia corallina — e poi in base al modo di fossilizzazione, che determina direttamente il loro interesse per un lapidario. Una conchiglia sostituita dalla silice si taglia e si lucida come una pietra fine; un'impronta carboniosa su scisto resta un pezzo da collezione da conservare così com'è.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammoniti e molluschi",
                sousTypes = "Ammoniti, ammolite, ortoceras, goniatiti, belemniti",
                description = "Conchiglie e rostri di cefalopodi marini oggi estinti, imparentati con l'attuale nautilus e il calamaro. Il loro guscio calcareo si presta bene al taglio e alla lucidatura; l'ammolite, varietà rarissima con madreperla di aragonite preservata, è persino ufficialmente riconosciuta come gemma organica a pieno titolo."
            ),
            FossileFamilyExplainer(
                nom = "Trilobiti e artropodi",
                sousTypes = "Elrathia, Phacops, Calymene e centinaia di altri generi, classificati per ordine (Ptychopariida, Phacopida...)",
                description = "Artropodi marini estinti alla fine del Permiano, il cui corpo segmentato in tre lobi (da cui il nome) e talvolta gli occhi composti si sono fossilizzati in calcare o scisto. Raramente tagliati, vengono soprattutto preparati a mano per liberarli dalla roccia incassante e venduti come pezzi da collezione."
            ),
            FossileFamilyExplainer(
                nom = "Vertebrati",
                sousTypes = "Denti di squalo e di mosasauro, osso di dinosauro agatizzato, pesci fossili, avorio di mammut",
                description = "Resti di animali a scheletro interno, dal gigantesco squalo megalodonte al mammut lanoso. Alcuni tessuti ossei, sostituiti molecola per molecola da silice colorata (\"gembone\"), diventano vere e proprie gemme ornamentali; altri, come denti o avorio, vengono lavorati grezzi o incastonati così come sono."
            ),
            FossileFamilyExplainer(
                nom = "Vegetali",
                sousTypes = "Legno pietrificato, felci e altre impronte vegetali del Carbonifero",
                description = "Tronchi, foglie e felci di antiche foreste, preservati o tramite silicizzazione completa (legno pietrificato, tagliato come un'agata), o come semplice impronta carboniosa su scisto, conservata intatta anziché lavorata."
            ),
            FossileFamilyExplainer(
                nom = "Ambra e insetti",
                sousTypes = "Ambra baltica, ambra con inclusioni, copale (resina subfossile più giovane)",
                description = "Resina vegetale fossilizzata, una delle più antiche gemme organiche lavorate dall'umanità. La sua trasparenza naturale intrappola talvolta insetti o piccoli organismi, il che ne moltiplica il valore scientifico ed estetico. Il copale, resina più giovane che non ha terminato la polimerizzazione, le viene talvolta impropriamente sostituito sul mercato."
            ),
            FossileFamilyExplainer(
                nom = "Coralli ed echinodermi",
                sousTypes = "Corallo fossile agatizzato, ricci di mare fossili, crinoidi (entroques), stromatoliti",
                description = "Organismi marini coloniali o fissati sul fondale oceanico. Il corallo fossile agatizzato, interamente silicizzato, viene tagliato in cabochon dai motivi floreali unici; ricci, crinoidi e stromatoliti sono generalmente apprezzati grezzi o in lastre lucidate per la loro struttura naturale."
            )
        ),
        disclaimerTitle = "Autenticità e provenienza",
        disclaimerBody = "Come quello delle pietre preziose e dei meteoriti, il mercato dei fossili attira la sua quota di ricostruzioni, calchi e pezzi incollati o sovra-preparati per sembrare più completi di quanto non siano realmente. Per un acquisto da collezione, privilegiate sempre una provenienza documentata, e verificate se una normativa locale disciplina l'esportazione (numerosi paesi, tra cui Marocco e Stati Uniti, limitano l'uscita di alcuni fossili rari o vertebrati). I prezzi indicati in questa sezione sono puramente indicativi: variano enormemente in base alla dimensione del pezzo, alla qualità della preparazione e allo stato del mercato."
    )

    private val de = FossileClassificationPage(
        intro = "Fossilien werden zunächst nach der Art des ursprünglichen Organismus klassifiziert — Muschel, Gliederfüßer, Wirbeltier, Pflanze, Harz oder Korallenkolonie — und danach nach der Art der Fossilisation, die unmittelbar über ihr Interesse für einen Lapidar entscheidet. Eine durch Kieselsäure ersetzte Schale lässt sich wie ein Schmuckstein schneiden und polieren; ein Kohlenstoffabdruck auf Schiefer bleibt ein Sammlerstück, das unverändert erhalten werden sollte.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammoniten & Weichtiere",
                sousTypes = "Ammoniten, Ammolith, Orthoceras, Goniatiten, Belemniten",
                description = "Schalen und Rostren heute ausgestorbener mariner Kopffüßer, verwandt mit dem heutigen Nautilus und Tintenfisch. Ihre Kalkschale eignet sich gut zum Sägen und Polieren; Ammolith, eine äußerst seltene Varietät mit erhaltenem Aragonit-Perlmutt, ist sogar offiziell als eigenständiger organischer Schmuckstein anerkannt."
            ),
            FossileFamilyExplainer(
                nom = "Trilobiten & Gliederfüßer",
                sousTypes = "Elrathia, Phacops, Calymene und Hunderte weiterer Gattungen, klassifiziert nach Ordnung (Ptychopariida, Phacopida...)",
                description = "Marine Gliederfüßer, die am Ende des Perms ausstarben, deren in drei Lappen gegliederter Körper (daher der Name) und manchmal die Facettenaugen sich in Kalkstein oder Schiefer fossilisierten. Selten geschnitten, werden sie vor allem von Hand aus ihrem Muttergestein freigelegt und als Sammlerstücke verkauft."
            ),
            FossileFamilyExplainer(
                nom = "Wirbeltiere",
                sousTypes = "Hai- und Mosasaurierzähne, agatisierter Dinosaurierknochen, fossile Fische, Mammutelfenbein",
                description = "Überreste von Tieren mit innerem Skelett, vom riesigen Megalodon-Hai bis zum Wollhaarmammut. Manches Knochengewebe, molekülweise durch farbige Kieselsäure ersetzt (\"Gembone\"), wird zu einem echten Schmuckstein; anderes, wie Zähne oder Elfenbein, wird roh bearbeitet oder unverändert gefasst."
            ),
            FossileFamilyExplainer(
                nom = "Pflanzen",
                sousTypes = "Versteinertes Holz, Farne und andere Pflanzenabdrücke aus dem Karbon",
                description = "Stämme, Blätter und Farne alter Wälder, entweder durch vollständige Verkieselung erhalten (versteinertes Holz, geschnitten wie ein Achat) oder als einfacher Kohlenstoffabdruck auf Schiefer, unverändert statt bearbeitet."
            ),
            FossileFamilyExplainer(
                nom = "Bernstein & Insekten",
                sousTypes = "Baltischer Bernstein, Bernstein mit Einschlüssen, Kopal (jüngeres subfossiles Harz)",
                description = "Fossilisiertes Pflanzenharz, eine der ältesten von Menschen bearbeiteten organischen Schmucksteine. Seine natürliche Transparenz schließt manchmal Insekten oder kleine Organismen ein, was seinen wissenschaftlichen und ästhetischen Wert erheblich steigert. Kopal, ein jüngeres Harz, dessen Polymerisation noch nicht abgeschlossen ist, wird ihm auf dem Markt manchmal fälschlich untergeschoben."
            ),
            FossileFamilyExplainer(
                nom = "Korallen & Stachelhäuter",
                sousTypes = "Agatisierte fossile Koralle, fossile Seeigel, Krinoiden (Entroques), Stromatolithen",
                description = "Koloniale oder am Meeresboden fixierte Meeresorganismen. Agatisierte fossile Koralle, vollständig verkieselt, wird zu Cabochons mit einzigartigen Blütenmustern geschnitten; Seeigel, Krinoiden und Stromatolithen werden meist roh oder als polierte Platten wegen ihrer natürlichen Struktur geschätzt."
            )
        ),
        disclaimerTitle = "Echtheit und Herkunft",
        disclaimerBody = "Wie der Edelstein- und der Meteoritenmarkt zieht auch der Fossilienmarkt eine gewisse Zahl an Nachbildungen, Abgüssen und zusammengeklebten oder übermäßig präparierten Stücken an, die vollständiger wirken sollen, als sie tatsächlich sind. Bevorzugen Sie bei einem Sammlerkauf stets eine dokumentierte Herkunft und prüfen Sie, ob eine lokale Regelung die Ausfuhr betrifft (zahlreiche Länder, darunter Marokko und die USA, beschränken die Ausfuhr bestimmter seltener Fossilien oder Wirbeltiere). Die in diesem Abschnitt angegebenen Preise sind rein indikativ: Sie schwanken stark je nach Stückgröße, Aufbereitungsqualität und Marktlage."
    )

    private val pt = FossileClassificationPage(
        intro = "Os fósseis classificam-se antes de mais pela natureza do organismo de origem — concha, artrópode, vertebrado, vegetal, resina ou colónia coralina — e depois pelo modo de fossilização, que determina diretamente o seu interesse para um lapidário. Uma concha substituída por sílica pode ser cortada e polida como uma pedra fina; uma impressão carbonosa em xisto continua a ser uma peça de coleção a preservar tal como está.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Amonites e moluscos",
                sousTypes = "Amonites, amolita, ortoceras, goniatites, belemnites",
                description = "Conchas e rostros de cefalópodes marinhos hoje extintos, aparentados com o nautilo e a lula atuais. A sua concha calcária presta-se bem ao corte e ao polimento; a amolita, variedade raríssima com nácar de aragonite preservado, é mesmo oficialmente reconhecida como gema orgânica por direito próprio."
            ),
            FossileFamilyExplainer(
                nom = "Trilobites e artrópodes",
                sousTypes = "Elrathia, Phacops, Calymene e centenas de outros géneros, classificados por ordem (Ptychopariida, Phacopida...)",
                description = "Artrópodes marinhos extintos no final do Pérmico, cujo corpo segmentado em três lobos (daí o nome) e por vezes olhos compostos se fossilizaram em calcário ou xisto. Raramente cortados, são sobretudo preparados à mão para serem libertados da rocha encaixante e vendidos como peças de coleção."
            ),
            FossileFamilyExplainer(
                nom = "Vertebrados",
                sousTypes = "Dentes de tubarão e de mosassauro, osso de dinossauro agatizado, peixes fósseis, marfim de mamute",
                description = "Restos de animais com esqueleto interno, do gigantesco tubarão-megalodonte ao mamute-lanoso. Alguns tecidos ósseos, substituídos molécula a molécula por sílica colorida (\"gembone\"), tornam-se verdadeiras gemas ornamentais; outros, como dentes ou marfim, são trabalhados em bruto ou engastados tal como são."
            ),
            FossileFamilyExplainer(
                nom = "Vegetais",
                sousTypes = "Madeira petrificada, fetos e outras impressões vegetais do Carbonífero",
                description = "Troncos, folhas e fetos de florestas antigas, preservados ou por silicificação completa (madeira petrificada, cortada como uma ágata), ou como simples impressão carbonosa em xisto, mantida intacta em vez de trabalhada."
            ),
            FossileFamilyExplainer(
                nom = "Âmbar e insetos",
                sousTypes = "Âmbar báltico, âmbar com inclusões, copal (resina subfóssil mais jovem)",
                description = "Resina vegetal fossilizada, uma das gemas orgânicas mais antigas trabalhadas pela humanidade. A sua transparência natural por vezes aprisiona insetos ou pequenos organismos, o que multiplica o seu valor científico e estético. O copal, resina mais jovem que não terminou a polimerização, é por vezes indevidamente substituído por ela no mercado."
            ),
            FossileFamilyExplainer(
                nom = "Corais e equinodermes",
                sousTypes = "Coral fóssil agatizado, ouriços-do-mar fósseis, crinoides (entroques), estromatólitos",
                description = "Organismos marinhos coloniais ou fixados no fundo oceânico. O coral fóssil agatizado, totalmente silicificado, é cortado em cabochões com padrões florais únicos; ouriços, crinoides e estromatólitos são geralmente apreciados em bruto ou em lajes polidas pela sua estrutura natural."
            )
        ),
        disclaimerTitle = "Autenticidade e proveniência",
        disclaimerBody = "Tal como o das pedras preciosas e dos meteoritos, o mercado dos fósseis atrai a sua quota de reconstituições, moldes e peças coladas ou sobrepreparadas para parecerem mais completas do que realmente são. Para uma compra de coleção, privilegie sempre uma proveniência documentada e verifique se alguma regulamentação local rege a exportação (numerosos países, incluindo Marrocos e os Estados Unidos, restringem a saída de certos fósseis raros ou vertebrados). Os preços indicados nesta secção são puramente indicativos: variam imenso consoante o tamanho da peça, a qualidade da preparação e o estado do mercado."
    )

    private val zh = FossileClassificationPage(
        intro = "化石首先按原始生物的性质分类——贝壳、节肢动物、脊椎动物、植物、树脂或珊瑚群体——然后按石化方式分类，这直接决定了它们对宝石雕刻师的价值。被二氧化硅取代的贝壳可以像半宝石一样切割和抛光；页岩上的碳质印痕仍然是一件应保持原状的收藏品。",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "菊石与软体动物",
                sousTypes = "菊石、菊石玉、直角石、齿菊石、箭石",
                description = "如今已灭绝的海洋头足类动物的壳体和吻部，与现今的鹦鹉螺和乌贼有亲缘关系。它们的钙质壳非常适合切割和抛光；菊石玉是一种极为罕见的品种，保留了文石质珍珠层，甚至被正式认定为独立的有机宝石。"
            ),
            FossileFamilyExplainer(
                nom = "三叶虫与节肢动物",
                sousTypes = "德氏虫、镜眼虫、褶颊虫等数百个属，按目分类（褶颊虫目、镜眼虫目等）",
                description = "二叠纪末灭绝的海洋节肢动物，其分为三叶的躯体（因此得名）有时还有复眼，化石化于石灰岩或页岩中。很少被切割，主要靠手工从围岩中剥离出来，作为收藏品出售。"
            ),
            FossileFamilyExplainer(
                nom = "脊椎动物",
                sousTypes = "鲨鱼和沧龙牙齿、玛瑙化恐龙骨、鱼类化石、猛犸象牙",
                description = "具有内骨骼的动物的遗骸，从巨齿鲨到长毛猛犸象。某些骨组织被彩色二氧化硅逐分子替换（“骨玉”），成为真正的装饰性宝石；其他如牙齿或象牙，则以原始状态加工或直接镶嵌。"
            ),
            FossileFamilyExplainer(
                nom = "植物",
                sousTypes = "硅化木、蕨类植物及石炭纪其他植物印痕",
                description = "古代森林的树干、树叶和蕨类，或通过完全硅化保存（硅化木，像玛瑙一样切割），或以页岩上简单的碳质印痕形式保存，保持完整而非加工。"
            ),
            FossileFamilyExplainer(
                nom = "琥珀与昆虫",
                sousTypes = "波罗的海琥珀、含内含物琥珀、柯巴脂（较年轻的亚化石树脂）",
                description = "石化的植物树脂，是人类加工的最古老的有机宝石之一。其天然透明度有时会包裹昆虫或小型生物，大大提高其科学与美学价值。柯巴脂是尚未完成聚合的较年轻树脂，有时会在市场上被不当地冒充琥珀。"
            ),
            FossileFamilyExplainer(
                nom = "珊瑚与棘皮动物",
                sousTypes = "玛瑙化化石珊瑚、化石海胆、海百合（海百合茎）、叠层石",
                description = "群居或固着于海底的海洋生物。完全硅化的玛瑙化化石珊瑚被切割成具有独特花状图案的凸圆面宝石；海胆、海百合和叠层石通常因其天然结构而以原始状态或抛光板状形式受到珍视。"
            )
        ),
        disclaimerTitle = "真实性与来源",
        disclaimerBody = "与宝石和陨石市场一样，化石市场也吸引了不少复原品、模制品以及经过粘合或过度修复以显得比实际更完整的标本。若为收藏而购买，请始终优先选择有据可查的来源，并核实当地法规是否对出口有所限制（包括摩洛哥和美国在内的许多国家都限制某些稀有化石或脊椎动物化石的出口）。本节所列价格仅供参考：价格因标本大小、修复质量和市场状况而有很大差异。"
    )

    private val ru = FossileClassificationPage(
        intro = "Ископаемые классифицируют прежде всего по природе исходного организма — раковина, членистоногое, позвоночное, растение, смола или коралловая колония — а затем по способу фоссилизации, который напрямую определяет их ценность для камнереза. Раковина, замещённая кремнезёмом, режется и полируется как поделочный камень; угольный отпечаток на сланце остаётся коллекционным экземпляром, который следует сохранять в первозданном виде.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Аммониты и моллюски",
                sousTypes = "Аммониты, аммолит, ортоцерасы, гониатиты, белемниты",
                description = "Раковины и ростры ныне вымерших морских головоногих, родственных современным наутилусу и кальмару. Их известковая раковина хорошо поддаётся распиловке и полировке; аммолит — чрезвычайно редкая разновидность с сохранившимся арагонитовым перламутром — даже официально признан самостоятельным органическим драгоценным камнем."
            ),
            FossileFamilyExplainer(
                nom = "Трилобиты и членистоногие",
                sousTypes = "Elrathia, Phacops, Calymene и сотни других родов, классифицируемых по отрядам (Ptychopariida, Phacopida...)",
                description = "Морские членистоногие, вымершие в конце пермского периода, чьё тело, разделённое на три лопасти (отсюда и название), а иногда и фасеточные глаза окаменели в известняке или сланце. Их редко режут — в основном вручную освобождают от вмещающей породы и продают как коллекционные экземпляры."
            ),
            FossileFamilyExplainer(
                nom = "Позвоночные",
                sousTypes = "Зубы акул и мозазавров, агатизированная кость динозавра, ископаемые рыбы, бивень мамонта",
                description = "Останки животных с внутренним скелетом — от гигантской акулы мегалодона до шерстистого мамонта. Некоторые костные ткани, молекула за молекулой замещённые окрашенным кремнезёмом (\"гембон\"), становятся настоящими декоративными камнями; другие, как зубы или бивни, обрабатывают в необработанном виде или оправляют как есть."
            ),
            FossileFamilyExplainer(
                nom = "Растения",
                sousTypes = "Окаменелая древесина, папоротники и другие растительные отпечатки каменноугольного периода",
                description = "Стволы, листья и папоротники древних лесов, сохранившиеся либо благодаря полной силификации (окаменелая древесина, обрабатываемая как агат), либо в виде простого угольного отпечатка на сланце, сохраняемого в первозданном виде, а не обрабатываемого."
            ),
            FossileFamilyExplainer(
                nom = "Янтарь и насекомые",
                sousTypes = "Балтийский янтарь, янтарь с инклюзами, копал (более молодая субфоссильная смола)",
                description = "Окаменевшая растительная смола — один из старейших органических драгоценных камней, обрабатываемых человеком. Её природная прозрачность иногда захватывает насекомых или мелких организмов, что многократно повышает её научную и эстетическую ценность. Копал, более молодая смола, не завершившая полимеризацию, иногда неправомерно выдаётся за янтарь на рынке."
            ),
            FossileFamilyExplainer(
                nom = "Кораллы и иглокожие",
                sousTypes = "Агатизированный ископаемый коралл, ископаемые морские ежи, криноидеи (энтроки), строматолиты",
                description = "Колониальные морские организмы или организмы, прикреплённые ко дну океана. Полностью силифицированный агатизированный ископаемый коралл режут в кабошоны с уникальным цветочным узором; морские ежи, криноидеи и строматолиты обычно ценятся в необработанном виде или в виде полированных плит за их природную структуру."
            )
        ),
        disclaimerTitle = "Подлинность и происхождение",
        disclaimerBody = "Как и рынок драгоценных камней и метеоритов, рынок ископаемых привлекает свою долю реконструкций, слепков и склеенных или чрезмерно подготовленных экземпляров, призванных выглядеть более полными, чем они есть на самом деле. При покупке для коллекции всегда отдавайте предпочтение документированному происхождению и проверяйте, регулируется ли экспорт местным законодательством (многие страны, включая Марокко и США, ограничивают вывоз некоторых редких ископаемых или позвоночных). Цены, указанные в этом разделе, носят исключительно ориентировочный характер: они сильно варьируются в зависимости от размера экземпляра, качества подготовки и состояния рынка."
    )

    private val nl = FossileClassificationPage(
        intro = "Fossielen worden eerst geclassificeerd naar de aard van het oorspronkelijke organisme — schelp, geleedpotige, gewervelde, plant, hars of koraalkolonie — en vervolgens naar de wijze van fossilisatie, die rechtstreeks bepaalt hoe interessant ze zijn voor een lapidarist. Een schelp die door silica is vervangen, kan worden gesneden en gepolijst als een halfedelsteen; een koolstofafdruk op leisteen blijft een verzamelstuk dat ongewijzigd bewaard moet worden.",
        familles = listOf(
            FossileFamilyExplainer(
                nom = "Ammonieten & weekdieren",
                sousTypes = "Ammonieten, ammoliet, orthoceras, goniatieten, belemnieten",
                description = "Schelpen en rostra van inmiddels uitgestorven zeeweekdieren, verwant aan de huidige nautilus en inktvis. Hun kalkschelp leent zich goed voor zagen en polijsten; ammoliet, een zeer zeldzame variëteit met bewaard aragoniet-parelmoer, wordt zelfs officieel erkend als een op zichzelf staande organische edelsteen."
            ),
            FossileFamilyExplainer(
                nom = "Trilobieten & geleedpotigen",
                sousTypes = "Elrathia, Phacops, Calymene en honderden andere geslachten, ingedeeld naar orde (Ptychopariida, Phacopida...)",
                description = "Mariene geleedpotigen die aan het einde van het Perm uitstierven, waarvan het in drie lobben verdeelde lichaam (vandaar de naam) en soms de facetogen fossiliseerden in kalksteen of leisteen. Ze worden zelden gesneden en vooral met de hand vrijgemaakt van het omringende gesteente om als verzamelstuk te worden verkocht."
            ),
            FossileFamilyExplainer(
                nom = "Gewervelden",
                sousTypes = "Haaien- en mosasaurustanden, geagatiseerd dinosaurusbot, fossiele vissen, mammoetivoor",
                description = "Overblijfselen van dieren met een inwendig skelet, van de gigantische megalodonhaai tot de wolharige mammoet. Sommige beenweefsels, molecuul voor molecuul vervangen door gekleurde silica (\"gembone\"), worden echte sierstenen; andere, zoals tanden of ivoor, worden ruw bewerkt of ongewijzigd gezet."
            ),
            FossileFamilyExplainer(
                nom = "Planten",
                sousTypes = "Versteend hout, varens en andere plantenafdrukken uit het Carboon",
                description = "Stammen, bladeren en varens van oude bossen, bewaard gebleven hetzij door volledige silicificatie (versteend hout, gesneden als agaat), hetzij als eenvoudige koolstofafdruk op leisteen, intact bewaard in plaats van bewerkt."
            ),
            FossileFamilyExplainer(
                nom = "Barnsteen & insecten",
                sousTypes = "Baltisch barnsteen, barnsteen met inclusies, copal (jongere subfossiele hars)",
                description = "Gefossiliseerde plantenhars, een van de oudste organische edelstenen die door de mens worden bewerkt. De natuurlijke transparantie vangt soms insecten of kleine organismen, wat de wetenschappelijke en esthetische waarde sterk verhoogt. Copal, een jongere hars die zijn polymerisatie nog niet heeft voltooid, wordt op de markt soms ten onrechte hiervoor aangezien."
            ),
            FossileFamilyExplainer(
                nom = "Koralen & stekelhuidigen",
                sousTypes = "Geagatiseerd fossiel koraal, fossiele zee-egels, crinoïden (entroques), stromatolieten",
                description = "Koloniale zeeorganismen of organismen die vastzitten aan de oceaanbodem. Geagatiseerd fossiel koraal, volledig gesilicificeerd, wordt gesneden tot cabochons met unieke bloemmotieven; zee-egels, crinoïden en stromatolieten worden meestal gewaardeerd in ruwe vorm of als gepolijste platen vanwege hun natuurlijke structuur."
            )
        ),
        disclaimerTitle = "Authenticiteit en herkomst",
        disclaimerBody = "Net als de markt voor edelstenen en meteorieten trekt ook de fossielenmarkt zijn deel reconstructies, afgietsels en gelijmde of overmatig geprepareerde stukken aan die er completer uit moeten zien dan ze in werkelijkheid zijn. Geef bij een verzamelaankoop altijd de voorkeur aan een gedocumenteerde herkomst en controleer of lokale regelgeving de uitvoer regelt (veel landen, waaronder Marokko en de Verenigde Staten, beperken de uitvoer van bepaalde zeldzame fossielen of gewervelden). De in dit gedeelte vermelde prijzen zijn puur indicatief: ze variëren enorm naargelang de grootte van het stuk, de kwaliteit van de preparatie en de marktsituatie."
    )

    private val byLanguage: Map<String, FossileClassificationPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): FossileClassificationPage = byLanguage[languageCode] ?: fr
}
