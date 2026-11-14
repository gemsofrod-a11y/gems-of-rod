package fr.gemsofrod.encyclopedie.data

/**
 * Guide lithothérapie détaillé d'une fiche pierre (demande de Sébastien du
 * 14/11/2026 : « développer la partie lithothérapie avec plus de détails »).
 *
 * Chaque pierre reçoit, dans les 9 langues de l'app, une fiche pratique :
 * chakras et signes (repris de la rubrique lithothérapie approfondie de
 * [GemDetails]), éléments, planètes et jour favorable (déduits des signes,
 * selon les maîtrises astrologiques traditionnelles), façon de la porter
 * selon le chakra, purification et rechargement adaptés à la pierre, élixir
 * (méthode directe ou indirecte) et précautions de sécurité.
 *
 * Les données par pierre vivent dans [LithoGuideData] ; seules les phrases
 * types sont traduites ici, si bien qu'une nouvelle pierre n'a besoin que
 * d'une ligne de données pour avoir son guide dans toutes les langues.
 * Il s'agit de croyances et d'usages traditionnels, jamais d'allégations
 * médicales : l'écran rappelle l'avertissement habituel.
 */
enum class LithoWater { OK, BRIEF, NEVER }

/** OK : soleil doux possible ; FADE : décoloration ; HEAT : chaleur/dessèchement ; TRAD : déconseillé par la tradition. */
enum class LithoSun { OK, FADE, HEAT, TRAD }

enum class LithoCaution { TOXIC, FIBRES, SHARP, ORGANIC, HYDROPHANE, MAGNET, FRAGILE }

data class LithoEntry(
    /** Libellés français canoniques (traduits via [LabelLocalization]) ; "ALL" = tous. */
    val chakras: List<String>,
    val signs: List<String>,
    val water: LithoWater,
    val saltOk: Boolean,
    val sun: LithoSun,
    val elixirDirect: Boolean,
    val cautions: List<LithoCaution>
)

object LithoGuide {
    private const val ALL = "ALL"

    private val signElement = mapOf(
        "Bélier" to "fire", "Lion" to "fire", "Sagittaire" to "fire",
        "Taureau" to "earth", "Vierge" to "earth", "Capricorne" to "earth",
        "Gémeaux" to "air", "Balance" to "air", "Verseau" to "air",
        "Cancer" to "water", "Scorpion" to "water", "Poissons" to "water"
    )

    /** Maîtres modernes puis traditionnels ; le dernier sert au jour de la semaine. */
    private val signPlanets = mapOf(
        "Bélier" to listOf("mars"),
        "Taureau" to listOf("venus"),
        "Gémeaux" to listOf("mercury"),
        "Cancer" to listOf("moon"),
        "Lion" to listOf("sun"),
        "Vierge" to listOf("mercury"),
        "Balance" to listOf("venus"),
        "Scorpion" to listOf("pluto", "mars"),
        "Sagittaire" to listOf("jupiter"),
        "Capricorne" to listOf("saturn"),
        "Verseau" to listOf("uranus", "saturn"),
        "Poissons" to listOf("neptune", "jupiter")
    )

    private val planetDay = mapOf(
        "sun" to "sunday", "moon" to "monday", "mars" to "tuesday", "mercury" to "wednesday",
        "jupiter" to "thursday", "venus" to "friday", "saturn" to "saturday"
    )

    private val chakraKey = mapOf(
        "Racine" to "wear_root", "Sacré" to "wear_sacral", "Plexus solaire" to "wear_plexus",
        "Cœur" to "wear_heart", "Gorge" to "wear_throat", "Troisième œil" to "wear_third_eye",
        "Couronne" to "wear_crown"
    )

    fun entryFor(gemId: String): LithoEntry? = LithoGuideData.entries[gemId]

    /** Titre de la carte dans la langue demandée. */
    fun title(languageCode: String): String = text("title", languageCode)

    /** Lignes (intitulé, contenu) du guide, ou liste vide si la pierre n'a pas de données. */
    fun rows(gemId: String, languageCode: String): List<Pair<String, String>> {
        val e = entryFor(gemId) ?: return emptyList()
        val lang = if (languageCode in TEXTS) languageCode else "fr"
        fun t(key: String) = text(key, lang)
        fun label(fr: String) = LabelLocalization.localize(fr, lang)
        val rows = mutableListOf<Pair<String, String>>()

        val chakras = if (ALL in e.chakras) t("all_chakras") else e.chakras.joinToString(" · ") { label(it) }
        rows += t("row_chakras") to chakras

        if (ALL in e.signs) {
            rows += t("row_signs") to t("all_signs")
        } else if (e.signs.isNotEmpty()) {
            rows += t("row_signs") to e.signs.joinToString(", ") { label(it) }
            val elements = e.signs.mapNotNull { signElement[it] }.distinct()
            rows += t("row_elements") to elements.joinToString(", ") { t("el_$it") }
            val planets = e.signs.flatMap { signPlanets[it].orEmpty() }.distinct()
            rows += t("row_planets") to planets.joinToString(", ") { t("pl_$it") }
            val days = e.signs.mapNotNull { signPlanets[it]?.lastOrNull()?.let { p -> planetDay[p] } }.distinct()
            rows += t("row_days") to days.joinToString(", ") { t("day_$it") }
        }

        val wearKey = if (ALL in e.chakras) "wear_crown" else chakraKey[e.chakras.firstOrNull()] ?: "wear_crown"
        rows += t("row_wear") to t(wearKey)

        val water = when (e.water) {
            LithoWater.OK -> t("water_ok")
            LithoWater.BRIEF -> t("water_brief")
            LithoWater.NEVER -> t("water_never")
        }
        rows += t("row_cleanse") to water + " " + t(if (e.saltOk) "salt_ok" else "salt_no")

        val sun = when (e.sun) {
            LithoSun.OK -> t("sun_ok")
            LithoSun.FADE -> t("sun_fade")
            LithoSun.HEAT -> t("sun_heat")
            LithoSun.TRAD -> t("sun_trad")
        }
        rows += t("row_recharge") to sun

        rows += t("row_elixir") to t(if (e.elixirDirect) "elixir_direct" else "elixir_indirect")

        if (e.cautions.isNotEmpty()) {
            rows += t("row_cautions") to e.cautions.joinToString("\n") { "• " + t("c_" + it.name.lowercase()) }
        }
        return rows
    }

    private fun text(key: String, lang: String): String =
        TEXTS[lang]?.get(key) ?: TEXTS.getValue("fr").getValue(key)

    private val TEXTS: Map<String, Map<String, String>> = mapOf(
        "fr" to mapOf(
            "title" to "Guide lithothérapie détaillé",
            "row_chakras" to "Chakras",
            "row_signs" to "Signes",
            "row_elements" to "Éléments",
            "row_planets" to "Planètes",
            "row_days" to "Jour favorable",
            "row_wear" to "Comment la porter",
            "row_cleanse" to "Purification",
            "row_recharge" to "Rechargement",
            "row_elixir" to "Élixir",
            "row_cautions" to "Précautions",
            "all_chakras" to "Tous les chakras",
            "all_signs" to "Tous les signes",
            "el_fire" to "Feu", "el_earth" to "Terre", "el_air" to "Air", "el_water" to "Eau",
            "pl_sun" to "Soleil", "pl_moon" to "Lune", "pl_mercury" to "Mercure", "pl_venus" to "Vénus",
            "pl_mars" to "Mars", "pl_jupiter" to "Jupiter", "pl_saturn" to "Saturne",
            "pl_uranus" to "Uranus", "pl_neptune" to "Neptune", "pl_pluto" to "Pluton",
            "day_monday" to "lundi", "day_tuesday" to "mardi", "day_wednesday" to "mercredi",
            "day_thursday" to "jeudi", "day_friday" to "vendredi", "day_saturday" to "samedi", "day_sunday" to "dimanche",
            "wear_root" to "En bracelet, en chevillère ou dans une poche ; en méditation, posée au bas du dos ou entre les pieds.",
            "wear_sacral" to "En pendentif long ou dans une poche ; en méditation, posée sur le bas-ventre, sous le nombril.",
            "wear_plexus" to "En pendentif mi-long ; en méditation, posée sur le haut du ventre, sous le sternum.",
            "wear_heart" to "En pendentif à hauteur du cœur ; en méditation, posée au centre de la poitrine.",
            "wear_throat" to "En collier court ou en ras-du-cou ; en méditation, posée au creux de la gorge.",
            "wear_third_eye" to "En boucles d'oreilles ou en pendentif ; en méditation, allongé, posée au milieu du front.",
            "wear_crown" to "Sur la table de nuit ou dans un coin de méditation ; en méditation, posée juste au-dessus du sommet de la tête.",
            "water_ok" to "Quelques minutes sous l'eau claire, puis séchage à l'air libre ; la fumigation (sauge, encens) et le son (bol chantant) conviennent aussi.",
            "water_brief" to "Passage bref sous l'eau tiède seulement, sans tremper ni frotter, puis séchage immédiat avec un tissu doux ; la fumigation ou le son sont préférables.",
            "water_never" to "Jamais d'eau : elle peut se dissoudre, s'oxyder, se tacher ou se fendre. Purification par la fumigation, par le son ou posée quelques heures sur une druse de quartz.",
            "salt_ok" to "Le sel sec est toléré, jamais l'eau salée prolongée.",
            "salt_no" to "Jamais de sel ni d'eau salée, qui rayent, corrodent ou s'infiltrent.",
            "sun_ok" to "À la lumière douce du matin ou du soir, à la lune ou sur une druse de quartz.",
            "sun_fade" to "À la lune ou sur une druse de quartz ; jamais au soleil, qui peut pâlir ou altérer sa couleur.",
            "sun_heat" to "À la lune ou sur une druse de quartz ; éviter le soleil et la chaleur, qui la dessèchent et peuvent la fendiller.",
            "sun_trad" to "À la lune, sur la terre ou sur une druse de quartz ; la tradition déconseille le soleil prolongé pour cette pierre.",
            "elixir_direct" to "Méthode directe possible : une pierre propre, polie, non teintée et bien identifiée, quelques heures dans de l'eau de source.",
            "elixir_indirect" to "Méthode indirecte uniquement : la pierre dans un flacon en verre fermé, lui-même posé dans l'eau ; elle ne doit jamais toucher l'eau destinée à être bue.",
            "c_toxic" to "Contient des éléments à ne pas ingérer (métaux, soufre, fluor…) : ne pas la mettre en bouche, se laver les mains après avoir manipulé la pierre brute, la garder hors de portée des enfants et des animaux.",
            "c_fibres" to "Pierre fibreuse : ne jamais poncer, scier ni tailler soi-même la pierre brute, dont les poussières ne doivent pas être inhalées ; la pierre polie ne présente pas ce risque.",
            "c_sharp" to "Les éclats bruts sont très coupants : les manipuler avec précaution et préférer les pierres roulées ou polies.",
            "c_organic" to "Gemme organique : jamais de parfum, de laque, de crème ni de produit ménager à son contact ; la mettre en dernier, l'ôter en premier et la ranger à part.",
            "c_hydrophane" to "Opale hydrophane : elle boit l'eau et les huiles, change d'aspect et peut se fendiller ; ne jamais la tremper.",
            "c_magnet" to "La magnétite naturelle peut être aimantée : la tenir à l'écart des stimulateurs cardiaques et des supports magnétiques.",
            "c_fragile" to "Pierre fragile (faible dureté ou clivage) : éviter les chocs, les écarts de température et le port en bague au quotidien."
        ),
        "en" to mapOf(
            "title" to "Detailed crystal healing guide",
            "row_chakras" to "Chakras",
            "row_signs" to "Signs",
            "row_elements" to "Elements",
            "row_planets" to "Planets",
            "row_days" to "Favourable day",
            "row_wear" to "How to wear it",
            "row_cleanse" to "Cleansing",
            "row_recharge" to "Recharging",
            "row_elixir" to "Elixir",
            "row_cautions" to "Precautions",
            "all_chakras" to "All chakras",
            "all_signs" to "All signs",
            "el_fire" to "Fire", "el_earth" to "Earth", "el_air" to "Air", "el_water" to "Water",
            "pl_sun" to "Sun", "pl_moon" to "Moon", "pl_mercury" to "Mercury", "pl_venus" to "Venus",
            "pl_mars" to "Mars", "pl_jupiter" to "Jupiter", "pl_saturn" to "Saturn",
            "pl_uranus" to "Uranus", "pl_neptune" to "Neptune", "pl_pluto" to "Pluto",
            "day_monday" to "Monday", "day_tuesday" to "Tuesday", "day_wednesday" to "Wednesday",
            "day_thursday" to "Thursday", "day_friday" to "Friday", "day_saturday" to "Saturday", "day_sunday" to "Sunday",
            "wear_root" to "As a bracelet, an anklet or in a pocket; in meditation, placed at the base of the spine or between the feet.",
            "wear_sacral" to "As a long pendant or in a pocket; in meditation, placed on the lower abdomen, below the navel.",
            "wear_plexus" to "As a mid-length pendant; in meditation, placed on the upper abdomen, below the breastbone.",
            "wear_heart" to "As a pendant at heart level; in meditation, placed in the centre of the chest.",
            "wear_throat" to "As a short necklace or choker; in meditation, placed in the hollow of the throat.",
            "wear_third_eye" to "As earrings or a pendant; in meditation, lying down, placed in the middle of the forehead.",
            "wear_crown" to "On the bedside table or in a meditation corner; in meditation, placed just above the top of the head.",
            "water_ok" to "A few minutes under clear running water, then air-dry; smudging (sage, incense) and sound (singing bowl) also work.",
            "water_brief" to "Only a brief rinse under lukewarm water, without soaking or rubbing, then dry at once with a soft cloth; smudging or sound are preferable.",
            "water_never" to "Never use water: it can dissolve, oxidise, stain or crack. Cleanse by smudging, with sound, or by leaving it a few hours on a quartz cluster.",
            "salt_ok" to "Dry salt is tolerated, never prolonged salt water.",
            "salt_no" to "Never use salt or salt water, which scratch, corrode or seep in.",
            "sun_ok" to "In soft morning or evening light, in moonlight or on a quartz cluster.",
            "sun_fade" to "In moonlight or on a quartz cluster; never in sunlight, which can fade or alter its colour.",
            "sun_heat" to "In moonlight or on a quartz cluster; avoid sun and heat, which dry it out and can make it crack.",
            "sun_trad" to "In moonlight, on the earth or on a quartz cluster; tradition advises against prolonged sunlight for this stone.",
            "elixir_direct" to "Direct method possible: a clean, polished, undyed and properly identified stone, left a few hours in spring water.",
            "elixir_indirect" to "Indirect method only: the stone in a closed glass vial, itself placed in the water; it must never touch water intended for drinking.",
            "c_toxic" to "Contains elements that must not be ingested (metals, sulphur, fluorine…): do not put it in your mouth, wash your hands after handling the rough stone, keep it out of reach of children and pets.",
            "c_fibres" to "Fibrous stone: never sand, saw or cut the rough stone yourself, as its dust must not be inhaled; the polished stone poses no such risk.",
            "c_sharp" to "Raw chips are very sharp: handle them with care and prefer tumbled or polished stones.",
            "c_organic" to "Organic gem: no perfume, hairspray, cream or household products in contact with it; put it on last, take it off first and store it separately.",
            "c_hydrophane" to "Hydrophane opal: it absorbs water and oils, changes appearance and can crack; never soak it.",
            "c_magnet" to "Natural magnetite can be magnetised: keep it away from pacemakers and magnetic media.",
            "c_fragile" to "Fragile stone (low hardness or cleavage): avoid knocks, sudden temperature changes and everyday wear in a ring."
        ),
        "es" to mapOf(
            "title" to "Guía detallada de litoterapia",
            "row_chakras" to "Chakras",
            "row_signs" to "Signos",
            "row_elements" to "Elementos",
            "row_planets" to "Planetas",
            "row_days" to "Día favorable",
            "row_wear" to "Cómo llevarla",
            "row_cleanse" to "Purificación",
            "row_recharge" to "Recarga",
            "row_elixir" to "Elixir",
            "row_cautions" to "Precauciones",
            "all_chakras" to "Todos los chakras",
            "all_signs" to "Todos los signos",
            "el_fire" to "Fuego", "el_earth" to "Tierra", "el_air" to "Aire", "el_water" to "Agua",
            "pl_sun" to "Sol", "pl_moon" to "Luna", "pl_mercury" to "Mercurio", "pl_venus" to "Venus",
            "pl_mars" to "Marte", "pl_jupiter" to "Júpiter", "pl_saturn" to "Saturno",
            "pl_uranus" to "Urano", "pl_neptune" to "Neptuno", "pl_pluto" to "Plutón",
            "day_monday" to "lunes", "day_tuesday" to "martes", "day_wednesday" to "miércoles",
            "day_thursday" to "jueves", "day_friday" to "viernes", "day_saturday" to "sábado", "day_sunday" to "domingo",
            "wear_root" to "En pulsera, tobillera o en un bolsillo; en meditación, colocada en la base de la espalda o entre los pies.",
            "wear_sacral" to "En colgante largo o en un bolsillo; en meditación, colocada en el bajo vientre, bajo el ombligo.",
            "wear_plexus" to "En colgante de largo medio; en meditación, colocada en la parte alta del vientre, bajo el esternón.",
            "wear_heart" to "En colgante a la altura del corazón; en meditación, colocada en el centro del pecho.",
            "wear_throat" to "En collar corto o gargantilla; en meditación, colocada en el hueco de la garganta.",
            "wear_third_eye" to "En pendientes o colgante; en meditación, tumbado, colocada en el centro de la frente.",
            "wear_crown" to "En la mesilla de noche o en un rincón de meditación; en meditación, colocada justo encima de la coronilla.",
            "water_ok" to "Unos minutos bajo agua clara, luego secado al aire; el sahumerio (salvia, incienso) y el sonido (cuenco tibetano) también sirven.",
            "water_brief" to "Solo un paso breve bajo agua tibia, sin remojar ni frotar, y secado inmediato con un paño suave; mejor el sahumerio o el sonido.",
            "water_never" to "Nunca agua: puede disolverse, oxidarse, mancharse o agrietarse. Purificación con sahumerio, con sonido o dejándola unas horas sobre una drusa de cuarzo.",
            "salt_ok" to "Se tolera la sal seca, nunca el agua salada prolongada.",
            "salt_no" to "Nunca sal ni agua salada, que rayan, corroen o se infiltran.",
            "sun_ok" to "Con la luz suave de la mañana o de la tarde, con la luna o sobre una drusa de cuarzo.",
            "sun_fade" to "Con la luna o sobre una drusa de cuarzo; nunca al sol, que puede palidecer o alterar su color.",
            "sun_heat" to "Con la luna o sobre una drusa de cuarzo; evitar el sol y el calor, que la resecan y pueden agrietarla.",
            "sun_trad" to "Con la luna, sobre la tierra o sobre una drusa de cuarzo; la tradición desaconseja el sol prolongado para esta piedra.",
            "elixir_direct" to "Método directo posible: una piedra limpia, pulida, sin teñir y bien identificada, unas horas en agua de manantial.",
            "elixir_indirect" to "Solo método indirecto: la piedra en un frasco de vidrio cerrado, colocado a su vez en el agua; nunca debe tocar el agua destinada a beberse.",
            "c_toxic" to "Contiene elementos que no deben ingerirse (metales, azufre, flúor…): no llevarla a la boca, lavarse las manos tras manipular la piedra en bruto, mantenerla fuera del alcance de niños y animales.",
            "c_fibres" to "Piedra fibrosa: nunca lijar, serrar ni tallar uno mismo la piedra en bruto, cuyo polvo no debe inhalarse; la piedra pulida no presenta este riesgo.",
            "c_sharp" to "Las lascas en bruto son muy cortantes: manipularlas con cuidado y preferir piedras rodadas o pulidas.",
            "c_organic" to "Gema orgánica: nunca perfume, laca, crema ni productos de limpieza en contacto; ponérsela la última, quitársela la primera y guardarla aparte.",
            "c_hydrophane" to "Ópalo hidrófano: absorbe el agua y los aceites, cambia de aspecto y puede agrietarse; nunca sumergirlo.",
            "c_magnet" to "La magnetita natural puede estar imantada: mantenerla alejada de marcapasos y soportes magnéticos.",
            "c_fragile" to "Piedra frágil (baja dureza o exfoliación): evitar golpes, cambios bruscos de temperatura y llevarla a diario en sortija."
        ),
        "it" to mapOf(
            "title" to "Guida dettagliata di litoterapia",
            "row_chakras" to "Chakra",
            "row_signs" to "Segni",
            "row_elements" to "Elementi",
            "row_planets" to "Pianeti",
            "row_days" to "Giorno favorevole",
            "row_wear" to "Come portarla",
            "row_cleanse" to "Purificazione",
            "row_recharge" to "Ricarica",
            "row_elixir" to "Elisir",
            "row_cautions" to "Precauzioni",
            "all_chakras" to "Tutti i chakra",
            "all_signs" to "Tutti i segni",
            "el_fire" to "Fuoco", "el_earth" to "Terra", "el_air" to "Aria", "el_water" to "Acqua",
            "pl_sun" to "Sole", "pl_moon" to "Luna", "pl_mercury" to "Mercurio", "pl_venus" to "Venere",
            "pl_mars" to "Marte", "pl_jupiter" to "Giove", "pl_saturn" to "Saturno",
            "pl_uranus" to "Urano", "pl_neptune" to "Nettuno", "pl_pluto" to "Plutone",
            "day_monday" to "lunedì", "day_tuesday" to "martedì", "day_wednesday" to "mercoledì",
            "day_thursday" to "giovedì", "day_friday" to "venerdì", "day_saturday" to "sabato", "day_sunday" to "domenica",
            "wear_root" to "Come braccialetto, cavigliera o in tasca; in meditazione, posata alla base della schiena o tra i piedi.",
            "wear_sacral" to "Come pendente lungo o in tasca; in meditazione, posata sul basso ventre, sotto l'ombelico.",
            "wear_plexus" to "Come pendente di media lunghezza; in meditazione, posata sulla parte alta del ventre, sotto lo sterno.",
            "wear_heart" to "Come pendente all'altezza del cuore; in meditazione, posata al centro del petto.",
            "wear_throat" to "Come collana corta o girocollo; in meditazione, posata nell'incavo della gola.",
            "wear_third_eye" to "Come orecchini o pendente; in meditazione, sdraiati, posata al centro della fronte.",
            "wear_crown" to "Sul comodino o in un angolo di meditazione; in meditazione, posata appena sopra la sommità del capo.",
            "water_ok" to "Qualche minuto sotto acqua limpida, poi asciugatura all'aria; anche i suffumigi (salvia, incenso) e il suono (campana tibetana) vanno bene.",
            "water_brief" to "Solo un breve passaggio sotto acqua tiepida, senza immergerla né strofinarla, poi asciugatura immediata con un panno morbido; meglio suffumigi o suono.",
            "water_never" to "Mai acqua: può sciogliersi, ossidarsi, macchiarsi o incrinarsi. Purificazione con suffumigi, con il suono o lasciandola qualche ora su una drusa di quarzo.",
            "salt_ok" to "Il sale secco è tollerato, mai l'acqua salata prolungata.",
            "salt_no" to "Mai sale né acqua salata, che graffiano, corrodono o si infiltrano.",
            "sun_ok" to "Alla luce dolce del mattino o della sera, alla luna o su una drusa di quarzo.",
            "sun_fade" to "Alla luna o su una drusa di quarzo; mai al sole, che può sbiadire o alterare il suo colore.",
            "sun_heat" to "Alla luna o su una drusa di quarzo; evitare il sole e il calore, che la seccano e possono incrinarla.",
            "sun_trad" to "Alla luna, sulla terra o su una drusa di quarzo; la tradizione sconsiglia il sole prolungato per questa pietra.",
            "elixir_direct" to "Metodo diretto possibile: una pietra pulita, lucidata, non tinta e ben identificata, qualche ora in acqua di sorgente.",
            "elixir_indirect" to "Solo metodo indiretto: la pietra in una boccetta di vetro chiusa, a sua volta posta nell'acqua; non deve mai toccare l'acqua da bere.",
            "c_toxic" to "Contiene elementi da non ingerire (metalli, zolfo, fluoro…): non metterla in bocca, lavarsi le mani dopo aver maneggiato la pietra grezza, tenerla fuori dalla portata di bambini e animali.",
            "c_fibres" to "Pietra fibrosa: non levigare, segare né tagliare mai da sé la pietra grezza, le cui polveri non vanno inalate; la pietra lucidata non presenta questo rischio.",
            "c_sharp" to "Le schegge grezze sono molto taglienti: maneggiarle con cautela e preferire pietre burattate o lucidate.",
            "c_organic" to "Gemma organica: mai profumo, lacca, crema né detersivi a contatto; indossarla per ultima, toglierla per prima e riporla a parte.",
            "c_hydrophane" to "Opale idrofana: assorbe acqua e oli, cambia aspetto e può incrinarsi; non immergerla mai.",
            "c_magnet" to "La magnetite naturale può essere magnetizzata: tenerla lontana da pacemaker e supporti magnetici.",
            "c_fragile" to "Pietra fragile (bassa durezza o sfaldatura): evitare urti, sbalzi di temperatura e l'uso quotidiano in anello."
        ),
        "de" to mapOf(
            "title" to "Ausführlicher Leitfaden zur Steinheilkunde",
            "row_chakras" to "Chakren",
            "row_signs" to "Sternzeichen",
            "row_elements" to "Elemente",
            "row_planets" to "Planeten",
            "row_days" to "Günstiger Tag",
            "row_wear" to "So trägt man ihn",
            "row_cleanse" to "Reinigung",
            "row_recharge" to "Aufladen",
            "row_elixir" to "Elixier",
            "row_cautions" to "Vorsichtsmaßnahmen",
            "all_chakras" to "Alle Chakren",
            "all_signs" to "Alle Sternzeichen",
            "el_fire" to "Feuer", "el_earth" to "Erde", "el_air" to "Luft", "el_water" to "Wasser",
            "pl_sun" to "Sonne", "pl_moon" to "Mond", "pl_mercury" to "Merkur", "pl_venus" to "Venus",
            "pl_mars" to "Mars", "pl_jupiter" to "Jupiter", "pl_saturn" to "Saturn",
            "pl_uranus" to "Uranus", "pl_neptune" to "Neptun", "pl_pluto" to "Pluto",
            "day_monday" to "Montag", "day_tuesday" to "Dienstag", "day_wednesday" to "Mittwoch",
            "day_thursday" to "Donnerstag", "day_friday" to "Freitag", "day_saturday" to "Samstag", "day_sunday" to "Sonntag",
            "wear_root" to "Als Armband, Fußkettchen oder in der Tasche; bei der Meditation am unteren Rücken oder zwischen die Füße gelegt.",
            "wear_sacral" to "Als langer Anhänger oder in der Tasche; bei der Meditation auf den Unterbauch unterhalb des Nabels gelegt.",
            "wear_plexus" to "Als halblanger Anhänger; bei der Meditation auf den Oberbauch unterhalb des Brustbeins gelegt.",
            "wear_heart" to "Als Anhänger auf Herzhöhe; bei der Meditation in die Mitte der Brust gelegt.",
            "wear_throat" to "Als kurze Kette oder Halsreif; bei der Meditation in die Halsgrube gelegt.",
            "wear_third_eye" to "Als Ohrringe oder Anhänger; bei der Meditation im Liegen auf die Stirnmitte gelegt.",
            "wear_crown" to "Auf dem Nachttisch oder in einer Meditationsecke; bei der Meditation knapp über den Scheitel gelegt.",
            "water_ok" to "Einige Minuten unter klarem Wasser, dann an der Luft trocknen lassen; Räuchern (Salbei, Weihrauch) und Klang (Klangschale) eignen sich ebenfalls.",
            "water_brief" to "Nur kurz unter lauwarmes Wasser halten, ohne Einweichen oder Reiben, dann sofort mit einem weichen Tuch trocknen; Räuchern oder Klang sind vorzuziehen.",
            "water_never" to "Niemals Wasser: Er kann sich auflösen, oxidieren, fleckig werden oder springen. Reinigung durch Räuchern, mit Klang oder einige Stunden auf einer Quarzstufe.",
            "salt_ok" to "Trockenes Salz wird vertragen, nie längeres Salzwasser.",
            "salt_no" to "Niemals Salz oder Salzwasser, die kratzen, angreifen oder eindringen.",
            "sun_ok" to "Im sanften Morgen- oder Abendlicht, im Mondlicht oder auf einer Quarzstufe.",
            "sun_fade" to "Im Mondlicht oder auf einer Quarzstufe; niemals in der Sonne, die seine Farbe ausbleichen oder verändern kann.",
            "sun_heat" to "Im Mondlicht oder auf einer Quarzstufe; Sonne und Hitze meiden, die ihn austrocknen und rissig machen können.",
            "sun_trad" to "Im Mondlicht, auf der Erde oder auf einer Quarzstufe; die Überlieferung rät bei diesem Stein von längerer Sonne ab.",
            "elixir_direct" to "Direkte Methode möglich: ein sauberer, polierter, ungefärbter und sicher bestimmter Stein, einige Stunden in Quellwasser.",
            "elixir_indirect" to "Nur indirekte Methode: der Stein in einem verschlossenen Glasfläschchen, das ins Wasser gestellt wird; er darf das Trinkwasser nie berühren.",
            "c_toxic" to "Enthält Stoffe, die nicht verschluckt werden dürfen (Metalle, Schwefel, Fluor…): nicht in den Mund nehmen, nach dem Anfassen des Rohsteins die Hände waschen, außer Reichweite von Kindern und Haustieren aufbewahren.",
            "c_fibres" to "Faseriger Stein: den Rohstein nie selbst schleifen, sägen oder bearbeiten, sein Staub darf nicht eingeatmet werden; der polierte Stein birgt dieses Risiko nicht.",
            "c_sharp" to "Rohe Splitter sind sehr scharf: vorsichtig handhaben und lieber Trommelsteine oder polierte Steine wählen.",
            "c_organic" to "Organischer Edelstein: kein Parfüm, Haarspray, keine Creme und keine Haushaltsmittel daran; zuletzt anlegen, zuerst ablegen und getrennt aufbewahren.",
            "c_hydrophane" to "Hydrophaner Opal: Er saugt Wasser und Öle auf, verändert sein Aussehen und kann rissig werden; niemals einweichen.",
            "c_magnet" to "Natürlicher Magnetit kann magnetisch sein: von Herzschrittmachern und magnetischen Datenträgern fernhalten.",
            "c_fragile" to "Empfindlicher Stein (geringe Härte oder Spaltbarkeit): Stöße, Temperaturschwankungen und das tägliche Tragen im Ring vermeiden."
        ),
        "pt" to mapOf(
            "title" to "Guia detalhado de litoterapia",
            "row_chakras" to "Chakras",
            "row_signs" to "Signos",
            "row_elements" to "Elementos",
            "row_planets" to "Planetas",
            "row_days" to "Dia favorável",
            "row_wear" to "Como usá-la",
            "row_cleanse" to "Purificação",
            "row_recharge" to "Recarga",
            "row_elixir" to "Elixir",
            "row_cautions" to "Precauções",
            "all_chakras" to "Todos os chakras",
            "all_signs" to "Todos os signos",
            "el_fire" to "Fogo", "el_earth" to "Terra", "el_air" to "Ar", "el_water" to "Água",
            "pl_sun" to "Sol", "pl_moon" to "Lua", "pl_mercury" to "Mercúrio", "pl_venus" to "Vénus",
            "pl_mars" to "Marte", "pl_jupiter" to "Júpiter", "pl_saturn" to "Saturno",
            "pl_uranus" to "Urano", "pl_neptune" to "Neptuno", "pl_pluto" to "Plutão",
            "day_monday" to "segunda-feira", "day_tuesday" to "terça-feira", "day_wednesday" to "quarta-feira",
            "day_thursday" to "quinta-feira", "day_friday" to "sexta-feira", "day_saturday" to "sábado", "day_sunday" to "domingo",
            "wear_root" to "Em pulseira, tornozeleira ou no bolso; em meditação, pousada na base das costas ou entre os pés.",
            "wear_sacral" to "Em pendente comprido ou no bolso; em meditação, pousada no baixo-ventre, abaixo do umbigo.",
            "wear_plexus" to "Em pendente de comprimento médio; em meditação, pousada na parte alta do ventre, abaixo do esterno.",
            "wear_heart" to "Em pendente à altura do coração; em meditação, pousada no centro do peito.",
            "wear_throat" to "Em colar curto ou gargantilha; em meditação, pousada na cova da garganta.",
            "wear_third_eye" to "Em brincos ou pendente; em meditação, deitado, pousada no meio da testa.",
            "wear_crown" to "Na mesa de cabeceira ou num canto de meditação; em meditação, pousada logo acima do topo da cabeça.",
            "water_ok" to "Alguns minutos sob água limpa, depois secagem ao ar; a defumação (sálvia, incenso) e o som (taça tibetana) também servem.",
            "water_brief" to "Apenas uma passagem breve sob água morna, sem mergulhar nem esfregar, e secagem imediata com um pano macio; preferir a defumação ou o som.",
            "water_never" to "Nunca água: pode dissolver-se, oxidar, manchar ou rachar. Purificação por defumação, pelo som ou pousada algumas horas sobre uma drusa de quartzo.",
            "salt_ok" to "O sal seco é tolerado, nunca a água salgada prolongada.",
            "salt_no" to "Nunca sal nem água salgada, que riscam, corroem ou se infiltram.",
            "sun_ok" to "À luz suave da manhã ou do fim de tarde, à lua ou sobre uma drusa de quartzo.",
            "sun_fade" to "À lua ou sobre uma drusa de quartzo; nunca ao sol, que pode desbotar ou alterar a sua cor.",
            "sun_heat" to "À lua ou sobre uma drusa de quartzo; evitar o sol e o calor, que a ressecam e podem fazê-la rachar.",
            "sun_trad" to "À lua, sobre a terra ou sobre uma drusa de quartzo; a tradição desaconselha o sol prolongado para esta pedra.",
            "elixir_direct" to "Método direto possível: uma pedra limpa, polida, não tingida e bem identificada, algumas horas em água de nascente.",
            "elixir_indirect" to "Apenas método indireto: a pedra num frasco de vidro fechado, por sua vez colocado na água; nunca deve tocar a água destinada a ser bebida.",
            "c_toxic" to "Contém elementos que não devem ser ingeridos (metais, enxofre, flúor…): não a levar à boca, lavar as mãos depois de manusear a pedra em bruto, mantê-la fora do alcance de crianças e animais.",
            "c_fibres" to "Pedra fibrosa: nunca lixar, serrar nem lapidar a pedra em bruto por conta própria, cujas poeiras não devem ser inaladas; a pedra polida não apresenta esse risco.",
            "c_sharp" to "As lascas em bruto são muito cortantes: manuseá-las com cuidado e preferir pedras roladas ou polidas.",
            "c_organic" to "Gema orgânica: nunca perfume, laca, creme nem produtos de limpeza em contacto; colocá-la por último, tirá-la primeiro e guardá-la à parte.",
            "c_hydrophane" to "Opala hidrófana: absorve água e óleos, muda de aspeto e pode rachar; nunca a mergulhar.",
            "c_magnet" to "A magnetite natural pode estar magnetizada: mantê-la afastada de pacemakers e suportes magnéticos.",
            "c_fragile" to "Pedra frágil (baixa dureza ou clivagem): evitar choques, variações bruscas de temperatura e o uso diário em anel."
        ),
        "ru" to mapOf(
            "title" to "Подробное руководство по литотерапии",
            "row_chakras" to "Чакры",
            "row_signs" to "Знаки",
            "row_elements" to "Стихии",
            "row_planets" to "Планеты",
            "row_days" to "Благоприятный день",
            "row_wear" to "Как носить",
            "row_cleanse" to "Очищение",
            "row_recharge" to "Зарядка",
            "row_elixir" to "Эликсир",
            "row_cautions" to "Меры предосторожности",
            "all_chakras" to "Все чакры",
            "all_signs" to "Все знаки",
            "el_fire" to "Огонь", "el_earth" to "Земля", "el_air" to "Воздух", "el_water" to "Вода",
            "pl_sun" to "Солнце", "pl_moon" to "Луна", "pl_mercury" to "Меркурий", "pl_venus" to "Венера",
            "pl_mars" to "Марс", "pl_jupiter" to "Юпитер", "pl_saturn" to "Сатурн",
            "pl_uranus" to "Уран", "pl_neptune" to "Нептун", "pl_pluto" to "Плутон",
            "day_monday" to "понедельник", "day_tuesday" to "вторник", "day_wednesday" to "среда",
            "day_thursday" to "четверг", "day_friday" to "пятница", "day_saturday" to "суббота", "day_sunday" to "воскресенье",
            "wear_root" to "В браслете, на щиколотке или в кармане; при медитации — у основания позвоночника или между стопами.",
            "wear_sacral" to "В длинном кулоне или в кармане; при медитации — на низ живота, ниже пупка.",
            "wear_plexus" to "В кулоне средней длины; при медитации — на верх живота, под грудину.",
            "wear_heart" to "В кулоне на уровне сердца; при медитации — в центр груди.",
            "wear_throat" to "В коротком ожерелье или чокере; при медитации — в ямку на шее.",
            "wear_third_eye" to "В серьгах или кулоне; при медитации лёжа — на середину лба.",
            "wear_crown" to "На прикроватной тумбочке или в уголке для медитации; при медитации — чуть выше макушки.",
            "water_ok" to "Несколько минут под чистой водой, затем сушка на воздухе; подходят также окуривание (шалфей, благовония) и звук (поющая чаша).",
            "water_brief" to "Лишь быстро ополоснуть тёплой водой, не замачивая и не растирая, и сразу высушить мягкой тканью; лучше окуривание или звук.",
            "water_never" to "Никакой воды: камень может раствориться, окислиться, покрыться пятнами или треснуть. Очищение окуриванием, звуком или несколькими часами на щётке кварца.",
            "salt_ok" to "Сухая соль допустима, длительная солёная вода — никогда.",
            "salt_no" to "Никогда не использовать соль и солёную воду: они царапают, разъедают или проникают внутрь.",
            "sun_ok" to "В мягком утреннем или вечернем свете, при луне или на щётке кварца.",
            "sun_fade" to "При луне или на щётке кварца; никогда на солнце, которое может обесцветить или изменить его цвет.",
            "sun_heat" to "При луне или на щётке кварца; избегать солнца и жары, которые его иссушают и могут вызвать трещины.",
            "sun_trad" to "При луне, на земле или на щётке кварца; традиция не советует держать этот камень долго на солнце.",
            "elixir_direct" to "Возможен прямой метод: чистый, полированный, неокрашенный и точно определённый камень на несколько часов в родниковую воду.",
            "elixir_indirect" to "Только косвенный метод: камень в закрытом стеклянном флаконе, который ставят в воду; он никогда не должен касаться питьевой воды.",
            "c_toxic" to "Содержит вещества, которые нельзя глотать (металлы, сера, фтор…): не брать в рот, мыть руки после работы с необработанным камнем, хранить в недоступном для детей и животных месте.",
            "c_fibres" to "Волокнистый камень: никогда не шлифовать, не пилить и не обрабатывать необработанный камень самостоятельно — его пыль нельзя вдыхать; полированный камень такой опасности не представляет.",
            "c_sharp" to "Необработанные осколки очень острые: обращаться осторожно и выбирать галтованные или полированные камни.",
            "c_organic" to "Органический самоцвет: никаких духов, лака для волос, кремов и бытовой химии; надевать последним, снимать первым и хранить отдельно.",
            "c_hydrophane" to "Гидрофановый опал: впитывает воду и масла, меняет вид и может треснуть; никогда не замачивать.",
            "c_magnet" to "Природный магнетит может быть намагничен: держать подальше от кардиостимуляторов и магнитных носителей.",
            "c_fragile" to "Хрупкий камень (низкая твёрдость или спайность): избегать ударов, перепадов температуры и ежедневной носки в кольце."
        ),
        "nl" to mapOf(
            "title" to "Uitgebreide gids lithotherapie",
            "row_chakras" to "Chakra's",
            "row_signs" to "Sterrenbeelden",
            "row_elements" to "Elementen",
            "row_planets" to "Planeten",
            "row_days" to "Gunstige dag",
            "row_wear" to "Zo draag je hem",
            "row_cleanse" to "Reiniging",
            "row_recharge" to "Opladen",
            "row_elixir" to "Elixer",
            "row_cautions" to "Voorzorgen",
            "all_chakras" to "Alle chakra's",
            "all_signs" to "Alle sterrenbeelden",
            "el_fire" to "Vuur", "el_earth" to "Aarde", "el_air" to "Lucht", "el_water" to "Water",
            "pl_sun" to "Zon", "pl_moon" to "Maan", "pl_mercury" to "Mercurius", "pl_venus" to "Venus",
            "pl_mars" to "Mars", "pl_jupiter" to "Jupiter", "pl_saturn" to "Saturnus",
            "pl_uranus" to "Uranus", "pl_neptune" to "Neptunus", "pl_pluto" to "Pluto",
            "day_monday" to "maandag", "day_tuesday" to "dinsdag", "day_wednesday" to "woensdag",
            "day_thursday" to "donderdag", "day_friday" to "vrijdag", "day_saturday" to "zaterdag", "day_sunday" to "zondag",
            "wear_root" to "Als armband, enkelbandje of in een zak; tijdens meditatie onder aan de rug of tussen de voeten gelegd.",
            "wear_sacral" to "Als lange hanger of in een zak; tijdens meditatie op de onderbuik, onder de navel gelegd.",
            "wear_plexus" to "Als halflange hanger; tijdens meditatie op de bovenbuik, onder het borstbeen gelegd.",
            "wear_heart" to "Als hanger ter hoogte van het hart; tijdens meditatie midden op de borst gelegd.",
            "wear_throat" to "Als korte ketting of choker; tijdens meditatie in het kuiltje van de keel gelegd.",
            "wear_third_eye" to "Als oorbellen of hanger; tijdens meditatie, liggend, midden op het voorhoofd gelegd.",
            "wear_crown" to "Op het nachtkastje of in een meditatiehoek; tijdens meditatie net boven de kruin gelegd.",
            "water_ok" to "Enkele minuten onder helder water, daarna aan de lucht laten drogen; beroken (salie, wierook) en klank (klankschaal) werken ook.",
            "water_brief" to "Alleen kort onder lauw water, zonder te weken of te wrijven, en meteen drogen met een zachte doek; beroken of klank zijn beter.",
            "water_never" to "Nooit water: hij kan oplossen, oxideren, vlekken of barsten. Reinigen door beroken, met klank of door hem enkele uren op een kwartsgeode te leggen.",
            "salt_ok" to "Droog zout wordt verdragen, nooit langdurig zout water.",
            "salt_no" to "Nooit zout of zout water, dat krast, aantast of binnendringt.",
            "sun_ok" to "In zacht ochtend- of avondlicht, bij maanlicht of op een kwartsgeode.",
            "sun_fade" to "Bij maanlicht of op een kwartsgeode; nooit in de zon, die zijn kleur kan doen verbleken of veranderen.",
            "sun_heat" to "Bij maanlicht of op een kwartsgeode; zon en warmte vermijden, die hem uitdrogen en kunnen doen barsten.",
            "sun_trad" to "Bij maanlicht, op de aarde of op een kwartsgeode; de traditie raadt langdurige zon af voor deze steen.",
            "elixir_direct" to "Directe methode mogelijk: een schone, gepolijste, ongeverfde en goed geïdentificeerde steen, enkele uren in bronwater.",
            "elixir_indirect" to "Alleen de indirecte methode: de steen in een gesloten glazen flesje dat zelf in het water staat; hij mag het drinkwater nooit raken.",
            "c_toxic" to "Bevat stoffen die niet ingeslikt mogen worden (metalen, zwavel, fluor…): niet in de mond nemen, handen wassen na het hanteren van de ruwe steen, buiten bereik van kinderen en huisdieren bewaren.",
            "c_fibres" to "Vezelige steen: de ruwe steen nooit zelf schuren, zagen of slijpen, want het stof mag niet worden ingeademd; de gepolijste steen geeft dat risico niet.",
            "c_sharp" to "Ruwe splinters zijn erg scherp: voorzichtig hanteren en liever getrommelde of gepolijste stenen kiezen.",
            "c_organic" to "Organische edelsteen: nooit parfum, haarlak, crème of schoonmaakmiddel ermee in contact; als laatste aandoen, als eerste afdoen en apart bewaren.",
            "c_hydrophane" to "Hydrofaan opaal: hij neemt water en oliën op, verandert van uiterlijk en kan barsten; nooit laten weken.",
            "c_magnet" to "Natuurlijk magnetiet kan gemagnetiseerd zijn: uit de buurt houden van pacemakers en magnetische dragers.",
            "c_fragile" to "Kwetsbare steen (lage hardheid of splijting): stoten, temperatuurschommelingen en dagelijks dragen in een ring vermijden."
        ),
        "zh" to mapOf(
            "title" to "灵石疗愈详细指南",
            "row_chakras" to "脉轮",
            "row_signs" to "星座",
            "row_elements" to "元素",
            "row_planets" to "行星",
            "row_days" to "吉日",
            "row_wear" to "佩戴方式",
            "row_cleanse" to "净化",
            "row_recharge" to "充能",
            "row_elixir" to "宝石水",
            "row_cautions" to "注意事项",
            "all_chakras" to "所有脉轮",
            "all_signs" to "所有星座",
            "el_fire" to "火", "el_earth" to "土", "el_air" to "风", "el_water" to "水",
            "pl_sun" to "太阳", "pl_moon" to "月亮", "pl_mercury" to "水星", "pl_venus" to "金星",
            "pl_mars" to "火星", "pl_jupiter" to "木星", "pl_saturn" to "土星",
            "pl_uranus" to "天王星", "pl_neptune" to "海王星", "pl_pluto" to "冥王星",
            "day_monday" to "星期一", "day_tuesday" to "星期二", "day_wednesday" to "星期三",
            "day_thursday" to "星期四", "day_friday" to "星期五", "day_saturday" to "星期六", "day_sunday" to "星期日",
            "wear_root" to "做成手链、脚链或放在口袋里；冥想时放在腰背底部或双脚之间。",
            "wear_sacral" to "做成长吊坠或放在口袋里；冥想时放在小腹、肚脐下方。",
            "wear_plexus" to "做成中长吊坠；冥想时放在上腹部、胸骨下方。",
            "wear_heart" to "做成垂在心口高度的吊坠；冥想时放在胸口正中。",
            "wear_throat" to "做成短项链或颈链；冥想时放在喉咙凹处。",
            "wear_third_eye" to "做成耳环或吊坠；冥想时平躺，放在额头正中。",
            "wear_crown" to "放在床头柜或冥想角落；冥想时放在头顶正上方。",
            "water_ok" to "在清水下冲洗几分钟，然后自然晾干；烟熏（鼠尾草、熏香）和声音（颂钵）也适用。",
            "water_brief" to "只能在温水下快速冲一下，不可浸泡或搓洗，随后立即用软布擦干；最好使用烟熏或声音净化。",
            "water_never" to "切勿沾水：它可能溶解、氧化、染污或开裂。可用烟熏、声音净化，或在水晶簇上放置几个小时。",
            "salt_ok" to "可接受干盐，但切勿长时间浸泡盐水。",
            "salt_no" to "切勿使用盐或盐水，它们会刮伤、腐蚀或渗入宝石。",
            "sun_ok" to "在清晨或傍晚的柔和光线下、月光下或水晶簇上充能。",
            "sun_fade" to "在月光下或水晶簇上充能；切勿日晒，阳光会使其褪色或改变颜色。",
            "sun_heat" to "在月光下或水晶簇上充能；避免日晒和高温，以免其干燥开裂。",
            "sun_trad" to "在月光下、土地上或水晶簇上充能；传统上不建议长时间日晒这种宝石。",
            "elixir_direct" to "可用直接法：将干净、抛光、未染色且鉴定可靠的宝石放入泉水中浸泡几个小时。",
            "elixir_indirect" to "仅可用间接法：把宝石放入密封玻璃瓶，再将瓶子放入水中；宝石绝不能接触饮用水。",
            "c_toxic" to "含有不可摄入的成分（金属、硫、氟……）：不要放入口中，接触原石后要洗手，并放在儿童和宠物接触不到的地方。",
            "c_fibres" to "纤维状宝石：切勿自行打磨、锯切或加工原石，其粉尘不可吸入；抛光后的宝石没有这种风险。",
            "c_sharp" to "原石碎片非常锋利：请小心拿取，宜选滚磨石或抛光石。",
            "c_organic" to "有机宝石：避免接触香水、发胶、乳霜和清洁剂；最后戴上、最先摘下，并单独存放。",
            "c_hydrophane" to "水蛋白石：会吸收水和油脂，外观改变并可能开裂；切勿浸泡。",
            "c_magnet" to "天然磁铁矿可能带磁性：请远离心脏起搏器和磁性存储介质。",
            "c_fragile" to "易碎宝石（硬度低或有解理）：避免碰撞、温度骤变，也不宜日常作为戒指佩戴。"
        )
    )
}
