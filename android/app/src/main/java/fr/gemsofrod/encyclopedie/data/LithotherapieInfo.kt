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
            intro = "Associer plusieurs pierres entre elles est une pratique courante en lithothérapie traditionnelle, dans l'idée de cumuler ou d'équilibrer leurs vertus respectives.",
            listTitle = "Quelques associations traditionnelles",
            listItems = listOf(
                LithoInfoListItem("Améthyste + Quartz rose", "Une association réputée pour apaiser les émotions et favoriser la sérénité affective."),
                LithoInfoListItem("Œil de tigre + Citrine", "Traditionnellement associées à la confiance en soi et à l'abondance."),
                LithoInfoListItem("Labradorite + Pierre de lune", "Deux pierres souvent réunies pour accompagner l'intuition et les périodes de transition."),
                LithoInfoListItem("Grenat + Cornaline", "Une combinaison traditionnellement liée à l'énergie et à la vitalité."),
                LithoInfoListItem("Aigue-marine + Lapis-lazuli", "Associées à la communication et à l'expression de soi.")
            ),
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
            intro = "Pairing several stones together is a common practice in traditional crystal healing, with the idea of combining or balancing their respective virtues.",
            listTitle = "A few traditional pairings",
            listItems = listOf(
                LithoInfoListItem("Amethyst + Rose quartz", "A pairing reputed to soothe emotions and foster emotional serenity."),
                LithoInfoListItem("Tiger's eye + Citrine", "Traditionally associated with self-confidence and abundance."),
                LithoInfoListItem("Labradorite + Moonstone", "Two stones often brought together to support intuition and times of transition."),
                LithoInfoListItem("Garnet + Carnelian", "A combination traditionally linked to energy and vitality."),
                LithoInfoListItem("Aquamarine + Lapis lazuli", "Associated with communication and self-expression.")
            ),
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
            intro = "Asociar varias piedras entre sí es una práctica habitual en la litoterapia tradicional, con la idea de sumar o equilibrar sus respectivas virtudes.",
            listTitle = "Algunas asociaciones tradicionales",
            listItems = listOf(
                LithoInfoListItem("Amatista + Cuarzo rosa", "Una asociación reputada por calmar las emociones y favorecer la serenidad afectiva."),
                LithoInfoListItem("Ojo de tigre + Citrina", "Tradicionalmente asociadas a la confianza en uno mismo y a la abundancia."),
                LithoInfoListItem("Labradorita + Piedra de luna", "Dos piedras a menudo reunidas para acompañar la intuición y los periodos de transición."),
                LithoInfoListItem("Granate + Cornalina", "Una combinación tradicionalmente ligada a la energía y la vitalidad."),
                LithoInfoListItem("Aguamarina + Lapislázuli", "Asociadas a la comunicación y la expresión de uno mismo.")
            ),
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
            intro = "Abbinare più pietre tra loro è una pratica comune nella litoterapia tradizionale, con l'idea di sommare o bilanciare le rispettive virtù.",
            listTitle = "Alcuni abbinamenti tradizionali",
            listItems = listOf(
                LithoInfoListItem("Ametista + Quarzo rosa", "Un abbinamento noto per calmare le emozioni e favorire la serenità affettiva."),
                LithoInfoListItem("Occhio di tigre + Citrino", "Tradizionalmente associate alla fiducia in sé stessi e all'abbondanza."),
                LithoInfoListItem("Labradorite + Pietra di luna", "Due pietre spesso riunite per accompagnare l'intuizione e i periodi di transizione."),
                LithoInfoListItem("Granato + Corniola", "Una combinazione tradizionalmente legata all'energia e alla vitalità."),
                LithoInfoListItem("Acquamarina + Lapislazzuli", "Associate alla comunicazione e all'espressione di sé.")
            ),
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
            intro = "Mehrere Steine miteinander zu kombinieren ist in der traditionellen Kristallheilkunde eine verbreitete Praxis, mit der Idee, ihre jeweiligen Wirkungen zu vereinen oder auszugleichen.",
            listTitle = "Einige traditionelle Kombinationen",
            listItems = listOf(
                LithoInfoListItem("Amethyst + Rosenquarz", "Eine Kombination, der nachgesagt wird, Emotionen zu beruhigen und emotionale Gelassenheit zu fördern."),
                LithoInfoListItem("Tigerauge + Citrin", "Traditionell mit Selbstvertrauen und Fülle assoziiert."),
                LithoInfoListItem("Labradorit + Mondstein", "Zwei Steine, die oft zusammen verwendet werden, um Intuition und Übergangsphasen zu begleiten."),
                LithoInfoListItem("Granat + Karneol", "Eine Kombination, die traditionell mit Energie und Vitalität verbunden wird."),
                LithoInfoListItem("Aquamarin + Lapislazuli", "Mit Kommunikation und Selbstausdruck assoziiert.")
            ),
            note = "Es gibt keine strikte Regel: Manche bevorzugen es, sich auf einen einzigen Stein zu konzentrieren, andere kombinieren gerne mehrere. Die Überlieferung empfiehlt lediglich, auf die Kohärenz der gewünschten Wirkung zu achten, anstatt Steine mit widersprüchlichen Eigenschaften zu häufen.",
            disclaimerTitle = "Eine Frage des Glaubens",
            disclaimerBody = "Wie bei Reinigung und Aufladung entstammen auch diese Kombinationen traditionellen Glaubensvorstellungen, ohne Verpflichtung oder validierte wissenschaftliche Grundlage — frei zu erkunden, ganz nach eigenem Gefühl."
        )
    )

    private val byLanguage: Map<String, Map<String, LithoInfoPage>> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de
    )

    fun page(topic: String, languageCode: String): LithoInfoPage =
        byLanguage[languageCode]?.get(topic) ?: fr.getValue(topic)
}
