package fr.gemsofrod.encyclopedie.data

data class LithoInfoSection(val title: String, val body: String)
data class LithoInfoListItem(val title: String, val description: String)

data class LithoInfoPage(
    val intro: String,
    val sections: List<LithoInfoSection> = emptyList(),
    val listTitle: String? = null,
    val listItems: List<LithoInfoListItem> = emptyList(),
    val note: String? = null,
    val disclaimerTitle: String,
    val disclaimerBody: String
)

/**
 * Contenu éditorial statique de la section Lithothérapie (nettoyage/rechargement,
 * association de pierres), traduit dans les 5 langues de l'app indépendamment
 * des fiches gemmes. Le français sert de secours si une langue n'a pas
 * (encore) de traduction pour un sujet donné.
 */
object LithotherapieInfo {
    const val NETTOYAGE_RECHARGEMENT = "nettoyage_rechargement"
    const val ASSOCIATIONS = "associations"

    private val fr: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "En lithothérapie traditionnelle, deux gestes sont souvent confondus : nettoyer une pierre et la recharger. Il ne s'agit pas d'entretien physique, mais de pratiques symboliques héritées de croyances anciennes.",
            sections = listOf(
                LithoInfoSection(
                    title = "Nettoyer, ce n'est pas recharger",
                    body = "Le nettoyage vise à débarrasser une pierre des énergies qu'elle aurait accumulées au contact de son environnement ou d'autres personnes. Le rechargement, lui, consiste à restaurer ou renforcer sa vibration propre, une fois la pierre nettoyée. On nettoie donc avant de recharger, jamais l'inverse."
                )
            ),
            listTitle = "Les différentes méthodes de rechargement",
            listItems = listOf(
                LithoInfoListItem("Lumière de la pleine lune", "La méthode la plus douce : poser la pierre une nuit à la lueur de la lune convient à l'ensemble des pierres."),
                LithoInfoListItem("Lumière du soleil", "À réserver aux pierres foncées et résistantes : une exposition prolongée peut décolorer les pierres claires comme l'améthyste, la citrine ou le quartz rose."),
                LithoInfoListItem("Amas de quartz ou druse d'améthyste", "Poser la pierre sur un amas de quartz ou une druse d'améthyste est une méthode courante, réputée douce et sans risque."),
                LithoInfoListItem("Terre", "Enfouir la pierre quelques heures dans la terre, puis la laver avant de la récupérer — à éviter pour les pierres qui se rayent ou se ternissent facilement."),
                LithoInfoListItem("Eau", "Un rinçage à l'eau claire, parfois salée, est une pratique répandue — mais à proscrire pour les pierres tendres ou poreuses (sélénite, malachite, turquoise, pyrite, lapis-lazuli...), qui peuvent s'abîmer à son contact."),
                LithoInfoListItem("Fumigation", "La fumée de sauge blanche, de palo santo ou d'encens est traditionnellement utilisée pour purifier une pierre sans contact direct."),
                LithoInfoListItem("Son", "Le son d'un bol chantant, d'une cloche ou d'un diapason est parfois utilisé pour faire vibrer la pierre et la débarrasser des énergies accumulées."),
                LithoInfoListItem("Pierres auto-nettoyantes", "La sélénite, la kyanite ou le quartz sont considérées comme n'ayant jamais besoin d'être nettoyées ni rechargées, et peuvent même servir à recharger d'autres pierres.")
            ),
            disclaimerTitle = "Une question de croyance",
            disclaimerBody = "Ces pratiques relèvent de traditions et de croyances populaires, sans fondement scientifique démontré. Il n'existe aucune obligation de nettoyer ou de recharger ses pierres : libre à chacun de suivre ou non ces usages, selon sa sensibilité."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Associer plusieurs pierres entre elles est une pratique courante en lithothérapie traditionnelle, dans l'idée de cumuler ou d'équilibrer leurs vertus respectives. Voici les associations les plus connues, chacune reliée aux fiches des gemmes concernées.",
            note = "Il n'existe pas de règle stricte : certains préfèrent se concentrer sur une seule pierre à la fois, d'autres aiment en associer plusieurs. La tradition recommande simplement de privilégier la cohérence de l'intention recherchée, plutôt que de multiplier les pierres aux vertus contradictoires.",
            disclaimerTitle = "Une question de croyance",
            disclaimerBody = "Comme pour le nettoyage et le rechargement, ces associations relèvent de croyances traditionnelles, sans obligation ni fondement scientifique validé — à explorer librement, selon votre ressenti."
        )
    )

    private val en: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "In traditional crystal healing, two gestures are often confused: cleansing a stone and recharging it. These are not about physical upkeep, but symbolic practices inherited from age-old beliefs.",
            sections = listOf(
                LithoInfoSection(
                    title = "Cleansing is not recharging",
                    body = "Cleansing aims to rid a stone of the energies it is said to accumulate through contact with its environment or other people. Recharging, on the other hand, consists of restoring or strengthening its own vibration once the stone has been cleansed. A stone is therefore cleansed before it is recharged, never the other way round."
                )
            ),
            listTitle = "The different recharging methods",
            listItems = listOf(
                LithoInfoListItem("Full moonlight", "The gentlest method: leaving the stone out overnight under the moonlight is suitable for all stones."),
                LithoInfoListItem("Sunlight", "Best reserved for dark, resilient stones: prolonged exposure can fade lighter stones such as amethyst, citrine or rose quartz."),
                LithoInfoListItem("A quartz cluster or amethyst geode", "Placing the stone on a quartz cluster or an amethyst geode is a common method, considered gentle and risk-free."),
                LithoInfoListItem("Earth", "Burying the stone in soil for a few hours, then washing it before retrieving it — best avoided for stones that scratch or tarnish easily."),
                LithoInfoListItem("Water", "Rinsing with clear or sometimes salted water is a widespread practice — but should be avoided for soft or porous stones (selenite, malachite, turquoise, pyrite, lapis lazuli...), which can be damaged by contact with water."),
                LithoInfoListItem("Smudging", "The smoke of white sage, palo santo or incense is traditionally used to purify a stone without direct contact."),
                LithoInfoListItem("Sound", "The sound of a singing bowl, a bell or a tuning fork is sometimes used to make the stone vibrate and release accumulated energies."),
                LithoInfoListItem("Self-cleansing stones", "Selenite, kyanite and quartz are considered never to need cleansing or recharging themselves, and can even be used to recharge other stones.")
            ),
            disclaimerTitle = "A matter of belief",
            disclaimerBody = "These practices stem from folk traditions and beliefs, with no demonstrated scientific basis. There is no obligation to cleanse or recharge your stones: it is entirely up to you whether or not to follow these customs, according to your own sensibility."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Pairing several stones together is a common practice in traditional crystal healing, with the idea of combining or balancing their respective virtues. Here are the best-known pairings, each linked to the relevant gem sheets.",
            note = "There is no strict rule: some prefer to focus on a single stone at a time, while others enjoy combining several. Tradition simply recommends favouring coherence of intent, rather than multiplying stones with contradictory virtues.",
            disclaimerTitle = "A matter of belief",
            disclaimerBody = "As with cleansing and recharging, these pairings stem from traditional beliefs, with no obligation or validated scientific basis — to be explored freely, according to your own feeling."
        )
    )

    private val es: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "En la litoterapia tradicional, se confunden a menudo dos gestos: limpiar una piedra y recargarla. No se trata de mantenimiento físico, sino de prácticas simbólicas heredadas de antiguas creencias.",
            sections = listOf(
                LithoInfoSection(
                    title = "Limpiar no es recargar",
                    body = "La limpieza busca liberar a una piedra de las energías que habría acumulado en contacto con su entorno o con otras personas. La recarga, por su parte, consiste en restaurar o reforzar su propia vibración, una vez limpiada la piedra. Por tanto, se limpia antes de recargar, nunca al revés."
                )
            ),
            listTitle = "Los diferentes métodos de recarga",
            listItems = listOf(
                LithoInfoListItem("Luz de luna llena", "El método más suave: dejar la piedra una noche bajo la luz de la luna es apto para todas las piedras."),
                LithoInfoListItem("Luz solar", "Reservada a piedras oscuras y resistentes: una exposición prolongada puede decolorar piedras claras como la amatista, la citrina o el cuarzo rosa."),
                LithoInfoListItem("Cúmulo de cuarzo o drusa de amatista", "Colocar la piedra sobre un cúmulo de cuarzo o una drusa de amatista es un método habitual, considerado suave y sin riesgo."),
                LithoInfoListItem("Tierra", "Enterrar la piedra unas horas en la tierra y lavarla antes de recuperarla — a evitar con piedras que se rayan o se opacan con facilidad."),
                LithoInfoListItem("Agua", "Un enjuague con agua clara, a veces salada, es una práctica extendida — pero debe evitarse con piedras blandas o porosas (selenita, malaquita, turquesa, pirita, lapislázuli...), que pueden dañarse al contacto con el agua."),
                LithoInfoListItem("Sahumado", "El humo de salvia blanca, palo santo o incienso se utiliza tradicionalmente para purificar una piedra sin contacto directo."),
                LithoInfoListItem("Sonido", "El sonido de un cuenco tibetano, una campana o un diapasón se usa a veces para hacer vibrar la piedra y liberarla de las energías acumuladas."),
                LithoInfoListItem("Piedras autolimpiantes", "La selenita, la cianita o el cuarzo se consideran piedras que nunca necesitan limpieza ni recarga, e incluso pueden usarse para recargar otras piedras.")
            ),
            disclaimerTitle = "Una cuestión de creencia",
            disclaimerBody = "Estas prácticas provienen de tradiciones y creencias populares, sin fundamento científico demostrado. No existe ninguna obligación de limpiar o recargar tus piedras: cada persona es libre de seguir o no estos usos, según su propia sensibilidad."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Asociar varias piedras entre sí es una práctica habitual en la litoterapia tradicional, con la idea de sumar o equilibrar sus respectivas virtudes. Estas son las asociaciones más conocidas, cada una enlazada con las fichas de las gemas correspondientes.",
            note = "No existe una regla estricta: algunos prefieren centrarse en una sola piedra a la vez, mientras que otros disfrutan combinando varias. La tradición simplemente recomienda privilegiar la coherencia de la intención buscada, en lugar de multiplicar piedras con virtudes contradictorias.",
            disclaimerTitle = "Una cuestión de creencia",
            disclaimerBody = "Al igual que la limpieza y la recarga, estas asociaciones provienen de creencias tradicionales, sin obligación ni fundamento científico validado — para explorar libremente, según tu propio sentir."
        )
    )

    private val it: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "Nella litoterapia tradizionale si confondono spesso due gesti: pulire una pietra e ricaricarla. Non si tratta di manutenzione fisica, ma di pratiche simboliche ereditate da antiche credenze.",
            sections = listOf(
                LithoInfoSection(
                    title = "Pulire non significa ricaricare",
                    body = "La pulizia mira a liberare una pietra dalle energie che avrebbe accumulato a contatto con il proprio ambiente o con altre persone. La ricarica, invece, consiste nel ripristinare o rafforzare la sua vibrazione propria, una volta pulita la pietra. Si pulisce quindi prima di ricaricare, mai il contrario."
                )
            ),
            listTitle = "I diversi metodi di ricarica",
            listItems = listOf(
                LithoInfoListItem("Luce della luna piena", "Il metodo più delicato: lasciare la pietra una notte alla luce della luna è adatto a tutte le pietre."),
                LithoInfoListItem("Luce solare", "Da riservare alle pietre scure e resistenti: un'esposizione prolungata può scolorire pietre chiare come l'ametista, il citrino o il quarzo rosa."),
                LithoInfoListItem("Drusa di quarzo o di ametista", "Appoggiare la pietra su una drusa di quarzo o di ametista è un metodo comune, considerato delicato e privo di rischi."),
                LithoInfoListItem("Terra", "Seppellire la pietra per qualche ora nella terra, poi lavarla prima di recuperarla — da evitare per le pietre che si graffiano o si opacizzano facilmente."),
                LithoInfoListItem("Acqua", "Un risciacquo con acqua limpida, talvolta salata, è una pratica diffusa — ma da evitare per le pietre tenere o porose (selenite, malachite, turchese, pirite, lapislazzuli...), che possono danneggiarsi a contatto con l'acqua."),
                LithoInfoListItem("Fumigazione", "Il fumo di salvia bianca, palo santo o incenso è tradizionalmente utilizzato per purificare una pietra senza contatto diretto."),
                LithoInfoListItem("Suono", "Il suono di una campana tibetana, di una campanella o di un diapason viene talvolta usato per far vibrare la pietra e liberarla dalle energie accumulate."),
                LithoInfoListItem("Pietre autopulenti", "La selenite, la cianite o il quarzo sono considerate pietre che non necessitano mai di pulizia né di ricarica, e possono persino essere usate per ricaricare altre pietre.")
            ),
            disclaimerTitle = "Una questione di credenza",
            disclaimerBody = "Queste pratiche derivano da tradizioni e credenze popolari, senza un fondamento scientifico dimostrato. Non esiste alcun obbligo di pulire o ricaricare le proprie pietre: ognuno è libero di seguire o meno queste usanze, secondo la propria sensibilità."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Abbinare più pietre tra loro è una pratica comune nella litoterapia tradizionale, con l'idea di sommare o bilanciare le rispettive virtù. Ecco gli abbinamenti più conosciuti, ciascuno collegato alle relative schede delle gemme.",
            note = "Non esiste una regola rigida: alcuni preferiscono concentrarsi su una sola pietra alla volta, altri amano combinarne diverse. La tradizione raccomanda semplicemente di privilegiare la coerenza dell'intenzione ricercata, piuttosto che moltiplicare pietre dalle virtù contraddittorie.",
            disclaimerTitle = "Una questione di credenza",
            disclaimerBody = "Come per la pulizia e la ricarica, questi abbinamenti derivano da credenze tradizionali, senza obbligo né fondamento scientifico validato — da esplorare liberamente, secondo la propria sensibilità."
        )
    )

    private val de: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "In der traditionellen Kristallheilkunde werden oft zwei Vorgänge verwechselt: das Reinigen eines Steins und seine Aufladung. Es handelt sich dabei nicht um physische Pflege, sondern um symbolische Praktiken, die aus alten Überlieferungen stammen.",
            sections = listOf(
                LithoInfoSection(
                    title = "Reinigen ist nicht Aufladen",
                    body = "Die Reinigung soll einen Stein von den Energien befreien, die er durch den Kontakt mit seiner Umgebung oder anderen Menschen angesammelt haben soll. Die Aufladung wiederum besteht darin, seine eigene Schwingung wiederherzustellen oder zu stärken, sobald der Stein gereinigt wurde. Man reinigt also, bevor man auflädt, niemals umgekehrt."
                )
            ),
            listTitle = "Die verschiedenen Aufladungsmethoden",
            listItems = listOf(
                LithoInfoListItem("Licht des Vollmonds", "Die sanfteste Methode: den Stein eine Nacht lang im Mondlicht liegen zu lassen, eignet sich für alle Steine."),
                LithoInfoListItem("Sonnenlicht", "Nur für dunkle, widerstandsfähige Steine geeignet: längere Sonneneinstrahlung kann helle Steine wie Amethyst, Citrin oder Rosenquarz ausbleichen."),
                LithoInfoListItem("Bergkristall- oder Amethystdruse", "Den Stein auf eine Bergkristall- oder Amethystdruse zu legen, ist eine verbreitete, als sanft und risikofrei geltende Methode."),
                LithoInfoListItem("Erde", "Den Stein einige Stunden in der Erde vergraben und vor der Entnahme waschen — bei Steinen, die leicht verkratzen oder anlaufen, besser vermeiden."),
                LithoInfoListItem("Wasser", "Ein Abspülen mit klarem, manchmal salzigem Wasser ist eine verbreitete Praxis — bei weichen oder porösen Steinen (Selenit, Malachit, Türkis, Pyrit, Lapislazuli...) jedoch zu vermeiden, da sie durch den Kontakt mit Wasser beschädigt werden können."),
                LithoInfoListItem("Räuchern", "Der Rauch von weißem Salbei, Palo Santo oder Räucherwerk wird traditionell verwendet, um einen Stein ohne direkten Kontakt zu reinigen."),
                LithoInfoListItem("Klang", "Der Klang einer Klangschale, einer Glocke oder einer Stimmgabel wird manchmal genutzt, um den Stein zum Schwingen zu bringen und ihn von angesammelten Energien zu befreien."),
                LithoInfoListItem("Selbstreinigende Steine", "Selenit, Disthen (Kyanit) und Bergkristall gelten als Steine, die selbst nie gereinigt oder aufgeladen werden müssen, und können sogar verwendet werden, um andere Steine aufzuladen.")
            ),
            disclaimerTitle = "Eine Frage des Glaubens",
            disclaimerBody = "Diese Praktiken entstammen volkstümlichen Überlieferungen und Glaubensvorstellungen, ohne nachgewiesene wissenschaftliche Grundlage. Es besteht keinerlei Verpflichtung, seine Steine zu reinigen oder aufzuladen: Es steht jedem frei, diesen Gepflogenheiten zu folgen oder nicht — ganz nach eigenem Empfinden."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Mehrere Steine miteinander zu kombinieren ist in der traditionellen Kristallheilkunde eine verbreitete Praxis, mit der Idee, ihre jeweiligen Wirkungen zu vereinen oder auszugleichen. Hier sind die bekanntesten Kombinationen, jeweils verlinkt mit den entsprechenden Steinprofilen.",
            note = "Es gibt keine strikte Regel: Manche bevorzugen es, sich auf einen einzigen Stein zu konzentrieren, andere kombinieren gerne mehrere. Die Überlieferung empfiehlt lediglich, auf die Kohärenz der gewünschten Wirkung zu achten, anstatt Steine mit widersprüchlichen Eigenschaften zu häufen.",
            disclaimerTitle = "Eine Frage des Glaubens",
            disclaimerBody = "Wie bei Reinigung und Aufladung entstammen auch diese Kombinationen traditionellen Glaubensvorstellungen, ohne Verpflichtung oder validierte wissenschaftliche Grundlage — frei zu erkunden, ganz nach eigenem Gefühl."
        )
    )

    private val pt: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "Na litoterapia tradicional, dois gestos são frequentemente confundidos: limpar uma pedra e recarregá-la. Não se trata de manutenção física, mas de práticas simbólicas herdadas de crenças antigas.",
            sections = listOf(
                LithoInfoSection(
                    title = "Limpar não é recarregar",
                    body = "A limpeza visa libertar uma pedra das energias que teria acumulado em contacto com o seu ambiente ou com outras pessoas. A recarga, por sua vez, consiste em restaurar ou reforçar a sua vibração própria, uma vez limpa a pedra. Limpa-se, portanto, antes de recarregar, nunca o contrário."
                )
            ),
            listTitle = "Os diferentes métodos de recarga",
            listItems = listOf(
                LithoInfoListItem("Luz da lua cheia", "O método mais suave: deixar a pedra uma noite à luz da lua é adequado a todas as pedras."),
                LithoInfoListItem("Luz solar", "A reservar para pedras escuras e resistentes: uma exposição prolongada pode desbotar pedras claras como a ametista, a citrina ou o quartzo rosa."),
                LithoInfoListItem("Aglomerado de quartzo ou drusa de ametista", "Colocar a pedra sobre um aglomerado de quartzo ou uma drusa de ametista é um método comum, reputado como suave e sem risco."),
                LithoInfoListItem("Terra", "Enterrar a pedra algumas horas na terra e depois lavá-la antes de a recuperar — a evitar em pedras que se riscam ou embaciam facilmente."),
                LithoInfoListItem("Água", "Um enxaguamento com água limpa, por vezes salgada, é uma prática comum — mas a evitar em pedras macias ou porosas (selenite, malaquite, turquesa, pirite, lápis-lazúli...), que podem danificar-se em contacto com a água."),
                LithoInfoListItem("Fumigação", "O fumo de salva branca, palo santo ou incenso é tradicionalmente usado para purificar uma pedra sem contacto direto."),
                LithoInfoListItem("Som", "O som de uma taça tibetana, de um sino ou de um diapasão é por vezes usado para fazer vibrar a pedra e libertá-la das energias acumuladas."),
                LithoInfoListItem("Pedras autolimpantes", "A selenite, a cianite ou o quartzo são consideradas pedras que nunca precisam de ser limpas nem recarregadas, podendo mesmo servir para recarregar outras pedras.")
            ),
            disclaimerTitle = "Uma questão de crença",
            disclaimerBody = "Estas práticas decorrem de tradições e crenças populares, sem fundamento científico demonstrado. Não existe qualquer obrigação de limpar ou recarregar as suas pedras: cada um é livre de seguir ou não estes usos, de acordo com a sua própria sensibilidade."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Associar várias pedras entre si é uma prática comum na litoterapia tradicional, com a ideia de acumular ou equilibrar as suas respetivas virtudes. Eis as associações mais conhecidas, cada uma ligada às fichas das gemas em causa.",
            note = "Não existe uma regra estrita: alguns preferem concentrar-se numa única pedra de cada vez, outros gostam de associar várias. A tradição recomenda simplesmente privilegiar a coerência da intenção procurada, em vez de multiplicar pedras com virtudes contraditórias.",
            disclaimerTitle = "Uma questão de crença",
            disclaimerBody = "Tal como para a limpeza e a recarga, estas associações decorrem de crenças tradicionais, sem obrigação nem fundamento científico validado — a explorar livremente, segundo o seu próprio sentir."
        )
    )

    private val zh: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "在传统水晶疗法中，两种做法常被混淆：清洁一颗宝石与为其充能。这并非物理上的保养，而是承袭自古老信仰的象征性习俗。",
            sections = listOf(
                LithoInfoSection(
                    title = "清洁不等于充能",
                    body = "清洁旨在去除宝石因接触其所处环境或他人而据信积累的能量。充能则是在宝石清洁之后，恢复或增强其自身的振动。因此，应先清洁再充能，绝不可颠倒顺序。"
                )
            ),
            listTitle = "各种充能方法",
            listItems = listOf(
                LithoInfoListItem("满月月光", "最温和的方法：将宝石在月光下放置一整夜，适用于所有宝石。"),
                LithoInfoListItem("阳光", "仅建议用于深色且耐晒的宝石：长时间曝晒可能使紫水晶、黄水晶或粉晶等浅色宝石褪色。"),
                LithoInfoListItem("水晶簇或紫晶洞", "将宝石放置在水晶簇或紫晶洞上，是一种常见方法，被认为温和且无风险。"),
                LithoInfoListItem("土壤", "将宝石埋入土中数小时，取出后清洗——容易被刮伤或失去光泽的宝石应避免使用此法。"),
                LithoInfoListItem("水", "用清水（有时是盐水）冲洗是一种普遍做法——但对于质地较软或多孔的宝石（透石膏、孔雀石、绿松石、黄铁矿、青金石等）应避免使用，因为接触水可能会造成损伤。"),
                LithoInfoListItem("烟熏净化", "传统上使用白鼠尾草、圣木或熏香的烟雾，在不直接接触的情况下净化宝石。"),
                LithoInfoListItem("声音", "颂钵、铃铛或音叉的声音有时被用来使宝石产生振动，从而释放积累的能量。"),
                LithoInfoListItem("自净宝石", "透石膏、蓝晶石和石英被认为从不需要清洁或充能，甚至可以用来为其他宝石充能。")
            ),
            disclaimerTitle = "一种信仰问题",
            disclaimerBody = "这些做法源于民间传统与信仰，并无经证实的科学依据。清洁或充能宝石并非任何形式的义务：是否遵循这些习俗，完全取决于个人的感受与选择。"
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "将多颗宝石相互搭配是传统水晶疗法中的常见做法，旨在叠加或平衡它们各自的功效。以下是最广为人知的搭配方式，每一种都关联到相关宝石的说明页面。",
            note = "并没有严格的规则：有些人偏好一次专注于一颗宝石，也有人喜欢同时搭配多颗。传统上仅建议优先考虑所追求意图的一致性，而非堆砌功效相互矛盾的宝石。",
            disclaimerTitle = "一种信仰问题",
            disclaimerBody = "与清洁和充能一样，这些搭配源于传统信仰，并无义务性质，也无经证实的科学依据——可根据个人感受自由探索。"
        )
    )

    private val ru: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "В традиционной кристаллотерапии часто путают два действия: очищение камня и его подзарядку. Речь идёт не о физическом уходе, а о символических практиках, унаследованных от древних поверий.",
            sections = listOf(
                LithoInfoSection(
                    title = "Очищение — не то же самое, что подзарядка",
                    body = "Очищение направлено на то, чтобы избавить камень от энергий, которые он якобы накопил в контакте со своей средой или другими людьми. Подзарядка же заключается в восстановлении или усилении его собственной вибрации после очищения камня. Таким образом, камень сначала очищают, а затем подзаряжают, но никогда не наоборот."
                )
            ),
            listTitle = "Различные методы подзарядки",
            listItems = listOf(
                LithoInfoListItem("Свет полной луны", "Самый мягкий метод: оставить камень на ночь под светом луны подходит для всех камней."),
                LithoInfoListItem("Солнечный свет", "Рекомендуется только для тёмных, стойких камней: длительное пребывание на солнце может вызвать выцветание светлых камней, таких как аметист, цитрин или розовый кварц."),
                LithoInfoListItem("Друза кварца или аметиста", "Положить камень на друзу кварца или аметиста — распространённый метод, считающийся мягким и безопасным."),
                LithoInfoListItem("Земля", "Закопать камень на несколько часов в землю, а затем промыть его перед тем, как достать — не рекомендуется для камней, которые легко царапаются или тускнеют."),
                LithoInfoListItem("Вода", "Ополаскивание чистой, иногда солёной водой — распространённая практика, но её следует избегать для мягких или пористых камней (селенит, малахит, бирюза, пирит, лазурит...), которые могут повредиться при контакте с водой."),
                LithoInfoListItem("Окуривание дымом", "Дым белого шалфея, пало-санто или благовоний традиционно используется для очищения камня без прямого контакта."),
                LithoInfoListItem("Звук", "Звук поющей чаши, колокольчика или камертона иногда используют, чтобы заставить камень вибрировать и освободить его от накопленных энергий."),
                LithoInfoListItem("Самоочищающиеся камни", "Селенит, кианит и кварц считаются камнями, которые сами никогда не нуждаются в очищении или подзарядке, и их даже можно использовать для подзарядки других камней.")
            ),
            disclaimerTitle = "Вопрос веры",
            disclaimerBody = "Эти практики относятся к народным традициям и поверьям, не имеющим доказанного научного обоснования. Не существует никакой обязанности очищать или подзаряжать свои камни: каждый вправе следовать этим обычаям или нет, в зависимости от собственных ощущений."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Сочетание нескольких камней между собой — распространённая практика в традиционной кристаллотерапии, основанная на идее суммирования или уравновешивания их соответствующих свойств. Вот наиболее известные сочетания, каждое из которых связано с описанием соответствующих камней.",
            note = "Строгих правил не существует: одни предпочитают сосредоточиться на одном камне за раз, другие любят сочетать несколько. Традиция лишь рекомендует отдавать предпочтение согласованности искомого намерения, а не умножать число камней с противоречивыми свойствами.",
            disclaimerTitle = "Вопрос веры",
            disclaimerBody = "Как и в случае очищения и подзарядки, эти сочетания основаны на традиционных поверьях, не несут никаких обязательств и не имеют подтверждённого научного обоснования — их можно свободно исследовать, доверяя собственным ощущениям."
        )
    )

    private val nl: Map<String, LithoInfoPage> = mapOf(
        NETTOYAGE_RECHARGEMENT to LithoInfoPage(
            intro = "In de traditionele kristalgenezing worden twee handelingen vaak door elkaar gehaald: het reinigen van een steen en het opladen ervan. Het gaat hierbij niet om fysiek onderhoud, maar om symbolische praktijken die voortkomen uit oude overtuigingen.",
            sections = listOf(
                LithoInfoSection(
                    title = "Reinigen is niet hetzelfde als opladen",
                    body = "Reinigen is bedoeld om een steen te bevrijden van de energieën die hij zou hebben opgebouwd door contact met zijn omgeving of andere personen. Opladen bestaat er daarentegen in om zijn eigen trilling te herstellen of te versterken, zodra de steen is gereinigd. Men reinigt dus vóór het opladen, nooit andersom."
                )
            ),
            listTitle = "De verschillende oplaadmethoden",
            listItems = listOf(
                LithoInfoListItem("Licht van volle maan", "De zachtste methode: de steen een nacht lang in het maanlicht laten liggen is geschikt voor alle stenen."),
                LithoInfoListItem("Zonlicht", "Voorbehouden aan donkere, robuuste stenen: langdurige blootstelling kan lichte stenen zoals amethist, citrien of rozenkwarts doen verbleken."),
                LithoInfoListItem("Kwartscluster of amethistdruze", "De steen op een kwartscluster of amethistdruze leggen is een gangbare methode, die als zacht en risicoloos geldt."),
                LithoInfoListItem("Aarde", "De steen enkele uren in de aarde begraven en vervolgens wassen voordat u hem terughaalt — te vermijden bij stenen die gemakkelijk krassen of dof worden."),
                LithoInfoListItem("Water", "Spoelen met helder, soms gezouten water is een wijdverbreide praktijk — maar te vermijden bij zachte of poreuze stenen (seleniet, malachiet, turkoois, pyriet, lapis lazuli...), die door contact met water beschadigd kunnen raken."),
                LithoInfoListItem("Reukwerk (smudging)", "De rook van witte salie, palo santo of wierook wordt van oudsher gebruikt om een steen te zuiveren zonder direct contact."),
                LithoInfoListItem("Klank", "Het geluid van een klankschaal, een bel of een stemvork wordt soms gebruikt om de steen te laten trillen en hem te bevrijden van opgehoopte energieën."),
                LithoInfoListItem("Zelfreinigende stenen", "Seleniet, kyaniet en kwarts worden beschouwd als stenen die zelf nooit gereinigd of opgeladen hoeven te worden, en die zelfs kunnen worden gebruikt om andere stenen op te laden.")
            ),
            disclaimerTitle = "Een kwestie van geloof",
            disclaimerBody = "Deze praktijken komen voort uit volkstradities en overtuigingen, zonder aangetoonde wetenschappelijke grondslag. Er bestaat geen enkele verplichting om uw stenen te reinigen of op te laden: het staat iedereen vrij deze gebruiken al dan niet te volgen, naargelang zijn eigen gevoel."
        ),
        ASSOCIATIONS to LithoInfoPage(
            intro = "Meerdere stenen met elkaar combineren is een gangbare praktijk in de traditionele kristalgenezing, met het idee hun respectieve eigenschappen te combineren of in evenwicht te brengen. Hier zijn de bekendste combinaties, elk gekoppeld aan de fiches van de betreffende edelstenen.",
            note = "Er bestaat geen strikte regel: sommigen kiezen ervoor zich op één enkele steen tegelijk te concentreren, anderen combineren er graag meerdere. De traditie beveelt eenvoudigweg aan de samenhang van de gezochte intentie te laten voorgaan, in plaats van stenen met tegenstrijdige eigenschappen te vermenigvuldigen.",
            disclaimerTitle = "Een kwestie van geloof",
            disclaimerBody = "Zoals bij reiniging en opladen, komen ook deze combinaties voort uit traditionele overtuigingen, zonder verplichting of gevalideerde wetenschappelijke grondslag — vrij te verkennen, naargelang uw eigen gevoel."
        )
    )

    private val byLanguage: Map<String, Map<String, LithoInfoPage>> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(topic: String, languageCode: String): LithoInfoPage =
        byLanguage[languageCode]?.get(topic) ?: fr.getValue(topic)
}
