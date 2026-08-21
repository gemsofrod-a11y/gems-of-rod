package fr.gemsofrod.encyclopedie.data

/**
 * Catalogue des associations de pierres traditionnellement reconnues en
 * lithothérapie, chacune reliée aux fiches réelles des gemmes concernées
 * ([GemsRepository]). Contenu éditorial en français ; voir
 * [AssociationLocalization] pour les traductions.
 */
object AssociationsRepository {
    private val associations: List<Association> = listOf(
        Association(
            id = "amethyste-quartz-rose",
            gemIds = listOf("amethyste", "quartz-rose"),
            titre = "Améthyste & Quartz rose",
            intention = "Sérénité affective",
            descriptionCourte = "Une association réputée pour apaiser les émotions et favoriser la sérénité affective.",
            descriptionLongue = "L'améthyste, pierre de calme et de clarté d'esprit, est traditionnellement associée au quartz rose, pierre de l'amour et de la tendresse envers soi-même. Ensemble, elles sont réputées apaiser les tensions émotionnelles et adoucir le regard porté sur ses propres sentiments.",
            conseilUtilisation = "Porter les deux pierres en pendentif ou les garder ensemble dans une poche, notamment lors de périodes de sensibilité émotionnelle accrue."
        ),
        Association(
            id = "oeil-de-tigre-citrine",
            gemIds = listOf("oeil-de-tigre", "citrine"),
            titre = "Œil de tigre & Citrine",
            intention = "Confiance et abondance",
            descriptionCourte = "Traditionnellement associées à la confiance en soi et à l'abondance.",
            descriptionLongue = "L'œil de tigre, pierre d'ancrage et de détermination, est souvent combiné à la citrine, pierre solaire liée à la réussite et à l'abondance. Cette paire est traditionnellement recherchée avant une prise de décision importante ou un nouveau projet professionnel.",
            conseilUtilisation = "Garder les deux pierres sur son bureau ou dans son espace de travail, ou les porter ensemble lors d'un entretien ou d'une négociation."
        ),
        Association(
            id = "labradorite-pierre-de-lune",
            gemIds = listOf("labradorite", "pierre-de-lune"),
            titre = "Labradorite & Pierre de lune",
            intention = "Intuition et transitions",
            descriptionCourte = "Deux pierres souvent réunies pour accompagner l'intuition et les périodes de transition.",
            descriptionLongue = "La labradorite, à l'éclat changeant, est associée à la protection de l'aura et à l'éveil de l'intuition ; la pierre de lune, elle, est liée au cycle féminin et à la douceur des nouveaux départs. Ensemble, elles sont traditionnellement portées lors des périodes de changement de vie.",
            conseilUtilisation = "Les méditer ensemble au coucher, ou les porter lors d'un déménagement, d'un changement professionnel ou de toute étape de vie incertaine."
        ),
        Association(
            id = "grenat-cornaline",
            gemIds = listOf("grenat-almandin", "cornaline"),
            titre = "Grenat & Cornaline",
            intention = "Énergie et vitalité",
            descriptionCourte = "Une combinaison traditionnellement liée à l'énergie et à la vitalité.",
            descriptionLongue = "Le grenat, pierre de force vitale et d'endurance, et la cornaline, pierre solaire associée au courage et à la motivation, forment un duo traditionnellement recherché en période de fatigue ou de baisse de motivation.",
            conseilUtilisation = "Les porter en bracelet au poignet lors des journées qui demandent de l'énergie et de la persévérance."
        ),
        Association(
            id = "aigue-marine-lapis-lazuli",
            gemIds = listOf("aigue-marine", "lapis-lazuli"),
            titre = "Aigue-marine & Lapis-lazuli",
            intention = "Communication",
            descriptionCourte = "Associées à la communication et à l'expression de soi.",
            descriptionLongue = "L'aigue-marine, pierre de fluidité et d'apaisement de la gorge, est traditionnellement associée au lapis-lazuli, pierre de vérité et de sagesse. Cette paire est réputée soutenir une expression claire et honnête, à l'oral comme à l'écrit.",
            conseilUtilisation = "Porter un collier ou un pendentif associant les deux pierres avant une prise de parole en public ou une conversation délicate."
        ),
        Association(
            id = "tourmaline-noire-hematite",
            gemIds = listOf("tourmaline-noire", "hematite"),
            titre = "Tourmaline noire & Hématite",
            intention = "Protection et ancrage",
            descriptionCourte = "Un duo classique de protection énergétique et d'ancrage.",
            descriptionLongue = "La tourmaline noire est l'une des pierres les plus citées en lithothérapie traditionnelle pour la protection contre les énergies négatives et les ondes environnantes ; l'hématite, dense et magnétique, est associée à l'ancrage et à la stabilité. Réunies, elles forment un binôme de protection au quotidien.",
            conseilUtilisation = "Les porter en bracelet ou les placer près de la porte d'entrée du domicile ou du lieu de travail."
        ),
        Association(
            id = "obsidienne-jade-nephrite",
            gemIds = listOf("obsidienne", "jade-nephrite"),
            titre = "Obsidienne & Jade néphrite",
            intention = "Protection douce et harmonie",
            descriptionCourte = "Une protection franche adoucie par l'harmonie traditionnellement attribuée au jade.",
            descriptionLongue = "L'obsidienne, verre volcanique réputé absorber les tensions, est associée au jade néphrite, pierre de sagesse et d'harmonie utilisée depuis des millénaires en Asie de l'Est. Ensemble, elles sont censées protéger tout en préservant la sérénité intérieure.",
            conseilUtilisation = "Porter les deux pierres en pendentif, ou garder l'obsidienne près de soi et le jade dans un lieu de vie partagé pour l'harmonie du foyer."
        ),
        Association(
            id = "turquoise-howlite",
            gemIds = listOf("turquoise", "howlite"),
            titre = "Turquoise & Howlite",
            intention = "Apaisement et calme mental",
            descriptionCourte = "Une association pour calmer le mental et apaiser l'agitation intérieure.",
            descriptionLongue = "La turquoise, pierre protectrice ancienne, est associée à la howlite, réputée en lithothérapie traditionnelle pour son effet calmant sur un mental agité ou insomniaque. Cette paire est traditionnellement recherchée en période de stress ou de surmenage.",
            conseilUtilisation = "Garder les deux pierres sur la table de chevet, ou les porter en journée lors de périodes particulièrement stressantes."
        ),
        Association(
            id = "cyanite-amethyste",
            gemIds = listOf("cyanite", "amethyste"),
            titre = "Cyanite & Améthyste",
            intention = "Clarté mentale et méditation",
            descriptionCourte = "Une paire prisée pour la méditation et la clarté mentale.",
            descriptionLongue = "La cyanite (ou disthène), qui ne retient traditionnellement aucune énergie et n'a donc pas besoin d'être nettoyée, est associée à l'améthyste pour approfondir les pratiques méditatives et clarifier les pensées.",
            conseilUtilisation = "Les tenir dans chaque main pendant une séance de méditation ou de relaxation."
        ),
        Association(
            id = "rhodonite-rhodochrosite",
            gemIds = listOf("rhodonite", "rhodochrosite"),
            titre = "Rhodonite & Rhodochrosite",
            intention = "Guérison émotionnelle et pardon de soi",
            descriptionCourte = "Deux pierres roses traditionnellement liées à la guérison émotionnelle et au pardon de soi.",
            descriptionLongue = "La rhodonite, pierre du pardon et de la réconciliation, est associée à la rhodochrosite, réputée aider à guérir les blessures affectives anciennes. Cette paire est traditionnellement recherchée après une rupture ou une épreuve émotionnelle.",
            conseilUtilisation = "Les porter ensemble en collier au niveau du cœur durant les périodes de reconstruction émotionnelle."
        ),
        Association(
            id = "malachite-pyrite",
            gemIds = listOf("malachite", "pyrite"),
            titre = "Malachite & Pyrite",
            intention = "Protection énergétique et abondance matérielle",
            descriptionCourte = "Une combinaison traditionnellement liée à la protection énergétique et à la prospérité.",
            descriptionLongue = "La malachite, pierre transformatrice qui absorberait les énergies négatives de son entourage, est associée à la pyrite, surnommée « or des fous », traditionnellement liée à l'abondance matérielle et à la confiance en l'avenir.",
            conseilUtilisation = "Placer les deux pierres dans l'espace professionnel ou près d'un tiroir-caisse, selon la tradition des commerçants."
        ),
        Association(
            id = "aventurine-jade-jadeite",
            gemIds = listOf("aventurine", "jade-jadeite"),
            titre = "Aventurine & Jade jadéite",
            intention = "Chance et nouvelles opportunités",
            descriptionCourte = "Une paire traditionnellement associée à la chance et aux nouvelles opportunités.",
            descriptionLongue = "L'aventurine verte, surnommée pierre de chance dans de nombreuses traditions, est associée au jade jadéite, gemme précieuse d'Asie de l'Est traditionnellement liée à la prospérité et à la réussite.",
            conseilUtilisation = "Les porter ensemble lors d'un événement important : entretien, examen, ou lancement d'un nouveau projet."
        ),
        Association(
            id = "agate-cristal-de-roche",
            gemIds = listOf("agate", "cristal-de-roche"),
            titre = "Agate & Cristal de roche",
            intention = "Équilibre et amplification",
            descriptionCourte = "L'agate pour l'équilibre, le cristal de roche pour amplifier son effet.",
            descriptionLongue = "L'agate, pierre d'équilibre et de stabilité aux multiples variétés, est associée au cristal de roche, réputé en lithothérapie traditionnelle pour amplifier les propriétés des pierres placées à son contact.",
            conseilUtilisation = "Placer le cristal de roche à proximité de l'agate portée ou posée, pour renforcer, selon la tradition, son action stabilisante."
        ),
        Association(
            id = "ambre-corail",
            gemIds = listOf("ambre", "corail"),
            titre = "Ambre & Corail",
            intention = "Vitalité et joie de vivre",
            descriptionCourte = "Deux gemmes organiques traditionnellement associées à la vitalité et à la joie de vivre.",
            descriptionLongue = "L'ambre, résine fossilisée à l'énergie solaire, et le corail, gemme marine associée à la vitalité et à la joie enfantine dans de nombreuses cultures méditerranéennes, forment une paire traditionnellement offerte pour accompagner la croissance et l'entrain.",
            conseilUtilisation = "Porter les deux gemmes en collier ou en bracelet, une combinaison traditionnellement appréciée dès l'enfance dans certaines cultures."
        ),
        Association(
            id = "perle-pierre-de-lune",
            gemIds = listOf("perle", "pierre-de-lune"),
            titre = "Perle & Pierre de lune",
            intention = "Féminité et intuition",
            descriptionCourte = "Deux gemmes lunaires associées à la féminité et à l'intuition.",
            descriptionLongue = "La perle, née dans l'eau et associée à la pureté depuis l'Antiquité, est traditionnellement combinée à la pierre de lune, gemme du cycle féminin et de l'intuition, pour honorer les énergies dites lunaires.",
            conseilUtilisation = "Les porter ensemble lors des phases de pleine lune, selon la tradition, ou simplement au quotidien en pendentif."
        ),
        Association(
            id = "sodalite-lapis-lazuli",
            gemIds = listOf("sodalite", "lapis-lazuli"),
            titre = "Sodalite & Lapis-lazuli",
            intention = "Vérité et expression de soi",
            descriptionCourte = "Une paire bleue traditionnellement liée à la vérité et à l'expression de soi.",
            descriptionLongue = "La sodalite, pierre de logique et de communication rationnelle, est associée au lapis-lazuli, pierre de sagesse et de vérité intérieure. Ensemble, elles sont traditionnellement recherchées pour aligner la pensée et la parole.",
            conseilUtilisation = "Les porter en collier au niveau de la gorge lors d'échanges ou de négociations importantes."
        ),
        Association(
            id = "unakite-rhodonite",
            gemIds = listOf("unakite", "rhodonite"),
            titre = "Unakite & Rhodonite",
            intention = "Équilibre émotionnel après une rupture",
            descriptionCourte = "Une association traditionnellement recherchée pour retrouver l'équilibre après une rupture.",
            descriptionLongue = "L'unakite, mélange de feldspath rose et d'épidote verte, est réputée aider à équilibrer les émotions ; associée à la rhodonite et son énergie de pardon, elle forme une paire traditionnellement portée dans les périodes de deuil affectif ou de séparation.",
            conseilUtilisation = "Les garder proches de soi (poche, bracelet) pendant les semaines qui suivent une rupture ou une perte affective."
        ),
        Association(
            id = "jaspe-grenat",
            gemIds = listOf("jaspe", "grenat-almandin"),
            titre = "Jaspe & Grenat",
            intention = "Courage et détermination",
            descriptionCourte = "Une combinaison de pierres terrestres associée au courage et à la détermination.",
            descriptionLongue = "Le jaspe, pierre nourricière et protectrice depuis l'Antiquité, est associé au grenat, pierre de force vitale, pour former un duo traditionnellement recherché avant une épreuve exigeant du courage ou de l'endurance.",
            conseilUtilisation = "Les porter en bracelet lors d'un défi sportif, d'un examen ou de toute épreuve demandant de la persévérance."
        ),
        Association(
            id = "chrysocolle-turquoise",
            gemIds = listOf("chrysocolle", "turquoise"),
            titre = "Chrysocolle & Turquoise",
            intention = "Communication apaisée",
            descriptionCourte = "Deux pierres bleu-vert traditionnellement liées à une communication apaisée.",
            descriptionLongue = "La chrysocolle, associée à la douceur dans l'expression, est combinée à la turquoise, pierre protectrice ancienne réputée favoriser une parole sincère sans agressivité. Cette paire est traditionnellement recherchée pour désamorcer les tensions relationnelles.",
            conseilUtilisation = "Les porter ensemble avant une conversation difficile où l'on souhaite garder son calme."
        ),
        Association(
            id = "kunzite-quartz-rose",
            gemIds = listOf("kunzite", "quartz-rose"),
            titre = "Kunzite & Quartz rose",
            intention = "Amour inconditionnel",
            descriptionCourte = "Une association traditionnellement liée à l'amour inconditionnel.",
            descriptionLongue = "La kunzite, pierre rose lithinifère réputée apaiser l'anxiété, est associée au quartz rose, gemme de l'amour universel. Ensemble, elles sont traditionnellement recherchées pour ouvrir le cœur, envers soi-même comme envers les autres.",
            conseilUtilisation = "Les porter en pendentif proche du cœur, en particulier lors de périodes d'anxiété relationnelle."
        ),
        Association(
            id = "morganite-quartz-rose",
            gemIds = listOf("morganite", "quartz-rose"),
            titre = "Morganite & Quartz rose",
            intention = "Ouverture du cœur et tendresse",
            descriptionCourte = "Deux gemmes roses associées à la tendresse et à l'ouverture du cœur.",
            descriptionLongue = "La morganite, variété rose du béryl, est traditionnellement associée à la compassion et à la guérison émotionnelle profonde ; combinée au quartz rose, elle forme une paire réputée soutenir la tendresse envers soi et envers l'entourage proche.",
            conseilUtilisation = "Les porter ensemble lors des périodes qui demandent douceur et indulgence envers soi-même."
        ),
        Association(
            id = "lepidolite-amethyste",
            gemIds = listOf("lepidolite", "amethyste"),
            titre = "Lépidolite & Améthyste",
            intention = "Lâcher-prise et gestion du stress",
            descriptionCourte = "Une paire traditionnellement recherchée pour le lâcher-prise et la gestion du stress.",
            descriptionLongue = "La lépidolite, mica lithinifère réputé apaisant, est associée à l'améthyste pour former un duo traditionnellement recherché en période de stress intense ou de difficulté à lâcher prise.",
            conseilUtilisation = "Les garder sur la table de nuit, ou les porter en bracelet lors de journées particulièrement chargées."
        ),
        Association(
            id = "sugilite-charoite",
            gemIds = listOf("sugilite", "charoite"),
            titre = "Sugilite & Charoïte",
            intention = "Protection spirituelle intense",
            descriptionCourte = "Deux pierres violettes rares, réputées pour une protection spirituelle intense.",
            descriptionLongue = "La sugilite, pierre violette rare découverte au XXe siècle, est associée à la charoïte, gemme sibérienne au motif tourbillonnant, pour former une paire traditionnellement recherchée par les pratiquants les plus avancés de la lithothérapie, en quête de protection spirituelle profonde.",
            conseilUtilisation = "Les réserver à des séances de méditation approfondie plutôt qu'à un port quotidien, selon l'usage traditionnel."
        ),
        Association(
            id = "larimar-aigue-marine",
            gemIds = listOf("larimar", "aigue-marine"),
            titre = "Larimar & Aigue-marine",
            intention = "Sérénité et énergie de l'eau",
            descriptionCourte = "Deux pierres bleu-turquoise associées à la sérénité et à l'énergie de l'eau.",
            descriptionLongue = "Le larimar, pectolite bleu-ciel originaire de République dominicaine, est associé à l'aigue-marine, gemme traditionnellement liée à la mer et à l'apaisement, pour un duo réputé porteur de calme et de fluidité intérieure.",
            conseilUtilisation = "Les porter lors de voyages en bord de mer ou de moments de repos, selon leur symbolique aquatique."
        ),
        Association(
            id = "prehnite-epidote",
            gemIds = listOf("prehnite", "epidote"),
            titre = "Préhnite & Épidote",
            intention = "Nouveaux départs et renouveau",
            descriptionCourte = "Une association verte traditionnellement liée aux nouveaux départs.",
            descriptionLongue = "La préhnite, surnommée « pierre de la prophétie », est associée à l'épidote, réputée amplifier ce qui est déjà en germe. Ensemble, elles sont traditionnellement recherchées au début d'un nouveau projet ou d'un nouveau chapitre de vie.",
            conseilUtilisation = "Les garder à portée de main lors du lancement d'un projet personnel ou professionnel."
        ),
        Association(
            id = "topaze-bleue-aigue-marine",
            gemIds = listOf("topaze-bleue", "aigue-marine"),
            titre = "Topaze bleue & Aigue-marine",
            intention = "Communication claire et vérité",
            descriptionCourte = "Deux pierres bleues associées à la communication claire et à la vérité.",
            descriptionLongue = "La topaze bleue, réputée favoriser la sincérité, est associée à l'aigue-marine pour former une paire traditionnellement recherchée quand il s'agit de dire une vérité difficile avec clarté et sans agressivité.",
            conseilUtilisation = "Les porter en boucles d'oreilles ou en collier avant un échange où l'honnêteté est essentielle."
        ),
        Association(
            id = "peridot-citrine",
            gemIds = listOf("peridot", "citrine"),
            titre = "Péridot & Citrine",
            intention = "Abondance et joie de vivre",
            descriptionCourte = "Deux pierres vert et jaune associées à l'abondance et à la joie de vivre.",
            descriptionLongue = "Le péridot, gemme volcanique traditionnellement liée à la prospérité et à la joie, est associé à la citrine, pierre solaire de la réussite, pour former un duo traditionnellement porté pour attirer l'abondance sous toutes ses formes.",
            conseilUtilisation = "Les porter ensemble en début d'année ou au lancement d'une nouvelle entreprise, selon la tradition."
        ),
        Association(
            id = "tourmaline-verte-tourmaline-rose",
            gemIds = listOf("tourmaline-verte", "tourmaline-rose"),
            titre = "Tourmaline verte & Tourmaline rose",
            intention = "Équilibre du cœur",
            descriptionCourte = "Deux variétés de tourmaline associées à l'équilibre entre raison et sentiment.",
            descriptionLongue = "La tourmaline verte, liée à la croissance et à l'équilibre, est associée à la tourmaline rose, gemme de l'amour et de la compassion, pour former une paire traditionnellement recherchée afin d'équilibrer le cœur entre ouverture affective et discernement.",
            conseilUtilisation = "Les porter ensemble en pendentif lors de décisions sentimentales importantes."
        ),
        Association(
            id = "azurite-dioptase",
            gemIds = listOf("azurite", "dioptase"),
            titre = "Azurite & Dioptase",
            intention = "Transformation intérieure profonde",
            descriptionCourte = "Une association intense, réputée pour la transformation intérieure profonde.",
            descriptionLongue = "L'azurite, pierre bleu profond associée à l'introspection, est combinée à la dioptase, gemme verte rare et intense, pour former une paire traditionnellement réservée à des périodes de remise en question ou de transformation personnelle marquée.",
            conseilUtilisation = "Les méditer séparément dans un premier temps, puis ensemble, selon l'usage traditionnel recommandé pour les associations intenses."
        ),
        Association(
            id = "angelite-cyanite",
            gemIds = listOf("angelite", "cyanite"),
            titre = "Angélite & Cyanite",
            intention = "Connexion spirituelle et communication",
            descriptionCourte = "Deux pierres bleu pâle traditionnellement associées à la connexion spirituelle.",
            descriptionLongue = "L'angélite, anhydrite bleu pâle dont le nom évoque la légèreté, est associée à la cyanite pour former une paire traditionnellement recherchée dans les pratiques méditatives axées sur la communication intérieure et la connexion spirituelle.",
            conseilUtilisation = "Les tenir ensemble en main lors d'une méditation silencieuse ou d'une pratique contemplative."
        )
    )

    fun all(): List<Association> = associations

    fun byId(id: String): Association? = associations.firstOrNull { it.id == id }
}
