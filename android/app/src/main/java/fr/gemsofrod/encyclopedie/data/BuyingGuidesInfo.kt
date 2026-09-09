package fr.gemsofrod.encyclopedie.data

data class BuyingGuideArticle(
    val pierre: String,
    val accroche: String,
    val origineCouleur: String,
    val puretTraitements: String,
    val entretien: String
)

data class BuyingGuidesPage(
    val intro: String,
    val articles: List<BuyingGuideArticle>
)

/**
 * Guides d'achat par pierre de qualité gemme (origine et couleur recherchées,
 * pureté et traitements courants du marché, précautions d'entretien liées à
 * la dureté) : ce que Gems of Rod examine avant de sélectionner chaque gemme
 * pour sa clientèle, traduit dans les 9 langues de l'app indépendamment des
 * fiches gemmes. Le français sert de secours si une langue n'a pas de
 * traduction.
 */
object BuyingGuidesInfo {
    private val fr = BuyingGuidesPage(
        intro = "Chaque pierre précieuse ou fine se choisit selon ses propres critères : origine et couleur recherchées, pureté attendue et traitements courants du marché, précautions d'entretien liées à sa dureté. Voici, pierre par pierre, ce que nous examinons chez Gems of Rod avant de sélectionner une gemme pour notre clientèle.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Rubis",
                accroche = "Le rouge absolu : au plus haut degré de qualité, le rubis peut dépasser le diamant en valeur au carat.",
                origineCouleur = "Birmanie (Mogok) : rouge « sang de pigeon », fluorescence intense, référence historique. Mozambique : principale source moderne, belles couleurs à prix plus accessible. Thaïlande/Cambodge : teintes plus sombres, brunâtres. Rouge pur à légèrement violacé, saturation maximale sans virer au noir.",
                puretTraitements = "Chauffage courant et largement accepté par le marché. Le remplissage de fractures au verre au plomb doit impérativement être divulgué : valeur très inférieure et fragilité accrue. Les inclusions en « soie » de rutile discrètes sont tolérées, parfois recherchées.",
                entretien = "Dureté 9, très résistant au quotidien. Un rubis rempli au verre reste fragile : à protéger des produits ménagers et des ultrasons."
            ),
            BuyingGuideArticle(
                pierre = "Émeraude",
                accroche = "Verte comme nulle autre, l'émeraude fascine depuis l'Égypte antique et reste l'une des gemmes les plus recherchées des collectionneurs.",
                origineCouleur = "Colombie (Muzo, Chivor) : vert intense légèrement bleuté, référence historique. Zambie (Kagem) : vert profond, clarté souvent supérieure. Brésil : production abondante, vert plus clair et accessible. Vert saturé et homogène, avec une pointe de bleu.",
                puretTraitements = "Les inclusions (son « jardin ») font presque partie de son identité. Quasi-totalité des pierres traitées à l'huile ou à la résine incolore : divulgation obligatoire, degré d'imprégnation à préciser sur certificat (GIA, Gübelin, GRS).",
                entretien = "Dureté 7,5 à 8 mais ténacité limitée par les inclusions. Retirer avant toute activité physique, éviter les produits chimiques et proscrire le nettoyeur à ultrasons, qui peut faire ressortir l'huile de traitement."
            ),
            BuyingGuideArticle(
                pierre = "Diamant",
                accroche = "La référence absolue de la joaillerie, jugée sur ses quatre critères historiques : couleur, pureté, taille, carat.",
                origineCouleur = "L'origine géographique pèse peu sur la valeur (hormis la traçabilité éthique, encadrée par le Processus de Kimberley) : c'est la couleur qui compte, du blanc incolore (D) recherché aux teintes fantaisie rares (bleu, rose, vert).",
                puretTraitements = "Pureté classée à la loupe x10, de « sans inclusion » (FL) à incluse (I). Le traitement HPHT et le perçage laser doivent être divulgués et gravés sur le certificat : la grande majorité du marché reste non traitée.",
                entretien = "Dureté 10, insensible à l'usure quotidienne mais peut s'ébrécher sous un choc violent. Les ultrasons sont sans risque, sauf sur une pierre fracturée ou remplie."
            ),
            BuyingGuideArticle(
                pierre = "Alexandrite",
                accroche = "La pierre « caméléon » : verte à la lumière du jour, rouge ou violette sous éclairage incandescent.",
                origineCouleur = "Oural russe : gisement historique, aujourd'hui quasiment épuisé. Sri Lanka et Brésil : sources actuelles principales. Afrique de l'Est (Tanzanie) : production plus récente. La force du changement de couleur prime sur la teinte elle-même.",
                puretTraitements = "Rarement traitée. Pierre presque toujours incluse à l'œil nu : une clarté élevée est exceptionnelle et très valorisée. La taille est souvent adaptée pour maximiser l'effet de changement de couleur.",
                entretien = "Dureté 8,5, très résistante. Aucun soin particulier, hormis les précautions d'usage pour toute pierre de joaillerie."
            ),
            BuyingGuideArticle(
                pierre = "Topaze impériale",
                accroche = "À ne pas confondre avec la topaze bleue, systématiquement irradiée : la topaze impériale, naturelle, est la variété la plus recherchée.",
                origineCouleur = "Ouro Preto, au Brésil : source historique et quasi exclusive de qualité gemme. Teinte orangée à rose saumoné ; le rose pur, dit « topaze impériale rose », est le plus rare et le plus précieux.",
                puretTraitements = "Généralement non traitée, contrairement à la topaze bleue : un critère de valeur important à vérifier sur certificat. Clarté naturellement élevée.",
                entretien = "Dureté 8, mais un clivage parfait dans un sens la rend fragile aux chocs. Éviter les variations thermiques brutales et proscrire les ultrasons."
            ),
            BuyingGuideArticle(
                pierre = "Grenat démantoïde",
                accroche = "Le plus précieux des grenats : un vert éclatant porté par une dispersion (feu) qui peut surpasser celle du diamant.",
                origineCouleur = "Oural russe : source historique, reconnaissable à ses inclusions fibreuses en « queue de cheval ». Namibie : principale production moderne. Vert vif recherché, idéalement avec peu de jaune.",
                puretTraitements = "Espèce très rarement traitée : pureté naturelle presque systématique. Les inclusions « queue de cheval » russes sont même recherchées, comme preuve d'origine.",
                entretien = "Dureté 6,5 à 7 : plus tendre que la plupart des pierres précieuses, à protéger des chocs et des rayures. Éviter les ultrasons en présence d'inclusions importantes."
            ),
            BuyingGuideArticle(
                pierre = "Tanzanite",
                accroche = "Découverte en 1967 au pied du Kilimandjaro, une gemme issue d'un gisement unique au monde.",
                origineCouleur = "Exclusivement Merelani, en Tanzanie : source unique et non renouvelable, un argument de rareté fort. Bleu-violet intense, au pléochroïsme marqué (bleu, violet, bordeaux selon l'angle) ; la taille oriente la couleur dominante.",
                puretTraitements = "Chauffage quasi systématique pour développer la couleur bleu-violet, traitement stable et accepté par le marché. Clarté généralement bonne à l'œil nu.",
                entretien = "Dureté 6 à 7 seulement, avec un clivage net : à réserver à des bijoux peu exposés aux chocs. Proscrire les ultrasons et la vapeur."
            ),
            BuyingGuideArticle(
                pierre = "Améthyste",
                accroche = "La plus précieuse des variétés de quartz, pierre de naissance du mois de février.",
                origineCouleur = "Brésil et Uruguay : géodes, violet clair à moyen. Zambie : violet profond aux reflets rougeâtres, très recherché. Sibérie : source historique aujourd'hui rare. Violet intense et homogène, sans zones brunâtres.",
                puretTraitements = "Rarement traitée ; parfois chauffée pour éclaircir la teinte ou virer vers la citrine. Clarté généralement excellente, ce qui en fait une pierre fine accessible.",
                entretien = "Dureté 7, robuste et facile d'entretien. Éviter une exposition prolongée au soleil, qui peut pâlir la couleur avec le temps."
            ),
            BuyingGuideArticle(
                pierre = "Spinelle",
                accroche = "Longtemps confondu avec le rubis — le légendaire « Rubis du Prince Noir » en est un —, le spinelle est aujourd'hui apprécié pour lui-même.",
                origineCouleur = "Birmanie (Mogok) : rouges et roses vifs. Sri Lanka et Tanzanie : palette de couleurs incluant le spinelle « flamme » orangé. Rouge, rose, bleu cobalt (rare et très recherché) et gris « gunmetal ».",
                puretTraitements = "Espèce quasiment jamais traitée, hormis un chauffage occasionnel : un vrai avantage pour l'acheteur en quête d'authenticité. Clarté généralement bonne à l'œil nu.",
                entretien = "Dureté 8, résistant. S'entretient comme le saphir, sans précaution particulière."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline",
                accroche = "La pierre aux mille couleurs, du vert « paraïba » électrique au rose et vert bicolore du « pastèque ».",
                origineCouleur = "Brésil (Paraíba) : bleu-vert cuprifère néon, légendaire et rarissime. Mozambique et Nigeria : teintes « paraïba-like » plus accessibles. Afghanistan et Brésil : rubellite rouge-rose. Madagascar : palette multicolore. La variété paraïba domine la valorisation, suivie de la rubellite intense.",
                puretTraitements = "Chauffage courant pour intensifier certaines teintes, traitement accepté. Clarté variable selon la variété ; des inclusions filiformes sont fréquentes et tolérées si discrètes.",
                entretien = "Dureté 7 à 7,5, bonne robustesse générale. Pierre pyroélectrique, qui attire la poussière par électricité statique : un nettoyage doux et régulier est recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Saphir",
                accroche = "Le corindon bleu par excellence, mais aussi rose, jaune ou « padparadscha » — juste derrière le diamant en dureté.",
                origineCouleur = "Cachemire : bleu velouté légendaire, gisement quasiment épuisé, rarissime. Birmanie (Mogok) : bleu « royal », intense. Sri Lanka : bleu plus clair, grande transparence. Madagascar : principale production moderne. Bleu velouté profond, saturation forte sans excès de noir.",
                puretTraitements = "Chauffage quasi systématique et largement accepté, améliore couleur et clarté. La diffusion (titane classique, ou béryllium, plus problématique) doit impérativement être distinguée sur le certificat. Clarté élevée généralement recherchée.",
                entretien = "Dureté 9, juste après le diamant : très résistant au quotidien. Ultrasons sans risque, sauf sur une pierre diffusée ou remplie."
            )
        )
    )

    private val en = BuyingGuidesPage(
        intro = "Each precious or fine gemstone is chosen by its own criteria: sought-after origin and colour, expected clarity and the treatments common to the market, and care precautions tied to its hardness. Here, stone by stone, is what we look at at Gems of Rod before selecting a gem for our clients.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Ruby",
                accroche = "The ultimate red: at the very top of the quality scale, ruby can outvalue diamond per carat.",
                origineCouleur = "Myanmar (Mogok): \"pigeon's blood\" red with intense fluorescence, the historic benchmark. Mozambique: the leading modern source, fine colours at a more accessible price. Thailand/Cambodia: darker, brownish tones. Pure to slightly purplish red, at maximum saturation without turning black.",
                puretTraitements = "Heat treatment is common and widely accepted by the market. Lead-glass fracture filling must always be disclosed: it carries far lower value and greater fragility. Discreet rutile \"silk\" inclusions are tolerated, sometimes even sought after.",
                entretien = "Hardness 9, very resistant to daily wear. A lead-glass filled ruby remains fragile: keep it away from household chemicals and ultrasonic cleaners."
            ),
            BuyingGuideArticle(
                pierre = "Emerald",
                accroche = "Green like no other stone, emerald has fascinated since ancient Egypt and remains one of the most sought-after gems among collectors.",
                origineCouleur = "Colombia (Muzo, Chivor): intense, slightly bluish green, the historic benchmark. Zambia (Kagem): deep green, often with superior clarity. Brazil: abundant, more affordable production with lighter green. A saturated, even green with a hint of blue is most prized.",
                puretTraitements = "Inclusions (its \"garden\") are almost part of its identity. Nearly all stones are treated with colourless oil or resin: disclosure is mandatory, and the degree of impregnation must be stated on a certificate (GIA, Gübelin, GRS).",
                entretien = "Hardness 7.5 to 8, but toughness is limited by inclusions. Remove the ring before physical activity, avoid chemicals, and never use an ultrasonic cleaner, which can draw out the treatment oil."
            ),
            BuyingGuideArticle(
                pierre = "Diamond",
                accroche = "Jewellery's ultimate benchmark, judged on its four historic criteria: colour, clarity, cut, and carat.",
                origineCouleur = "Geographic origin carries little weight on value (aside from ethical traceability under the Kimberley Process): colour is what counts, from sought-after colourless white (D) to rare fancy hues (blue, pink, green).",
                puretTraitements = "Clarity is graded under 10x magnification, from flawless (FL) to included (I). HPHT treatment and laser drilling must be disclosed and laser-inscribed on the certificate: the vast majority of the market remains untreated.",
                entretien = "Hardness 10, impervious to everyday wear but can chip under a hard blow. Ultrasonic cleaning is safe, except on a fractured or filled stone."
            ),
            BuyingGuideArticle(
                pierre = "Alexandrite",
                accroche = "The \"chameleon\" stone: green in daylight, red or purple under incandescent light.",
                origineCouleur = "The Russian Urals: the historic deposit, now nearly exhausted. Sri Lanka and Brazil: today's main sources. East Africa (Tanzania): a more recent production. The strength of the colour change matters more than the hue itself.",
                puretTraitements = "Rarely treated. The stone is almost always eye-visibly included: high clarity is exceptional and highly prized. Cutting is often tailored to maximise the colour-change effect.",
                entretien = "Hardness 8.5, very resistant. No special care needed, beyond the usual precautions for any fine jewellery stone."
            ),
            BuyingGuideArticle(
                pierre = "Imperial topaz",
                accroche = "Not to be confused with blue topaz, which is always irradiated: natural imperial topaz is the most sought-after variety.",
                origineCouleur = "Ouro Preto, Brazil: the historic and almost exclusive source of gem quality. Orange to salmon-pink hues; pure pink, known as \"pink imperial topaz\", is the rarest and most valuable.",
                puretTraitements = "Generally untreated, unlike blue topaz: an important value criterion to check on the certificate. Naturally high clarity.",
                entretien = "Hardness 8, but a perfect single-direction cleavage makes it fragile under impact. Avoid sudden temperature changes and never use an ultrasonic cleaner."
            ),
            BuyingGuideArticle(
                pierre = "Demantoid garnet",
                accroche = "The most precious of garnets: a brilliant green carried by a dispersion (fire) that can outshine diamond's.",
                origineCouleur = "The Russian Urals: the historic source, recognisable by its fibrous \"horsetail\" inclusions. Namibia: the leading modern production. A vivid green with as little yellow as possible is most prized.",
                puretTraitements = "Very rarely treated: natural clarity is almost the rule. Russian \"horsetail\" inclusions are even sought after, as proof of origin.",
                entretien = "Hardness 6.5 to 7, softer than most precious stones and best protected from knocks and scratches. Avoid ultrasonic cleaning when inclusions are significant."
            ),
            BuyingGuideArticle(
                pierre = "Tanzanite",
                accroche = "Discovered in 1967 at the foot of Kilimanjaro, a gem from a single deposit found nowhere else on Earth.",
                origineCouleur = "Exclusively Merelani, Tanzania: a unique, non-renewable source, a strong argument for rarity. Intense blue-violet with marked pleochroism (blue, violet, burgundy depending on the angle); the cut determines which hue dominates.",
                puretTraitements = "Heat treatment to develop the blue-violet colour is near-universal, stable and accepted by the market. Clarity is generally good to the naked eye.",
                entretien = "Only 6 to 7 in hardness, with a distinct cleavage: best kept for jewellery not exposed to knocks. Avoid ultrasonic and steam cleaning."
            ),
            BuyingGuideArticle(
                pierre = "Amethyst",
                accroche = "The most precious of the quartz varieties, the birthstone for February.",
                origineCouleur = "Brazil and Uruguay: geodes with light to medium purple. Zambia: deep purple with reddish flashes, highly sought after. Siberia: a historic but now rare source. An intense, even purple free of brownish zones is most prized.",
                puretTraitements = "Rarely treated; sometimes heated to lighten the colour or turn it into citrine. Clarity is generally excellent, making it an accessible fine gemstone.",
                entretien = "Hardness 7, sturdy and easy to care for. Avoid prolonged sun exposure, which can fade the colour over time."
            ),
            BuyingGuideArticle(
                pierre = "Spinel",
                accroche = "Long mistaken for ruby — the legendary \"Black Prince's Ruby\" is in fact one — spinel is now valued in its own right.",
                origineCouleur = "Myanmar (Mogok): vivid reds and pinks. Sri Lanka and Tanzania: a wide colour range including orange \"flame\" spinel. Red, pink, cobalt blue (rare and highly prized), and \"gunmetal\" grey.",
                puretTraitements = "Almost never treated, aside from occasional heating: a real advantage for buyers seeking authenticity. Clarity is generally good to the naked eye.",
                entretien = "Hardness 8, durable. Care for it as you would sapphire, with no special precautions."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline",
                accroche = "The stone of a thousand colours, from electric \"Paraíba\" green-blue to bicolour pink-and-green \"watermelon\".",
                origineCouleur = "Brazil (Paraíba): legendary, extremely rare neon copper-bearing blue-green. Mozambique and Nigeria: more accessible \"Paraíba-like\" hues. Afghanistan and Brazil: red-pink rubellite. Madagascar: a multicoloured palette. The Paraíba variety commands the highest value, followed by intense rubellite.",
                puretTraitements = "Heat treatment to intensify certain hues is common and accepted. Clarity varies by variety; fine needle-like inclusions are frequent and tolerated when discreet.",
                entretien = "Hardness 7 to 7.5, generally sturdy. A pyroelectric stone that attracts dust through static electricity: gentle, regular cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Sapphire",
                accroche = "The quintessential blue corundum, but also found in pink, yellow, or \"padparadscha\" — second only to diamond in hardness.",
                origineCouleur = "Kashmir: legendary velvety blue, an almost exhausted, extremely rare deposit. Myanmar (Mogok): intense \"royal\" blue. Sri Lanka: lighter blue with great transparency. Madagascar: the leading modern production. A deep velvety blue with strong saturation, without excess black, is most prized.",
                puretTraitements = "Heat treatment is near-universal and widely accepted, improving colour and clarity. Diffusion (classic titanium, or the more problematic beryllium) must always be distinguished on the certificate. High clarity is generally sought after.",
                entretien = "Hardness 9, just below diamond: very resistant to daily wear. Ultrasonic cleaning is safe, except on a diffused or filled stone."
            )
        )
    )

    private val es = BuyingGuidesPage(
        intro = "Cada piedra preciosa o fina se elige según sus propios criterios: origen y color buscados, pureza esperada y tratamientos habituales del mercado, precauciones de cuidado ligadas a su dureza. Esto es, piedra por piedra, lo que examinamos en Gems of Rod antes de seleccionar una gema para nuestra clientela.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Rubí",
                accroche = "El rojo absoluto: en su más alta calidad, el rubí puede superar al diamante en valor por quilate.",
                origineCouleur = "Birmania (Mogok): rojo «sangre de paloma», fluorescencia intensa, referencia histórica. Mozambique: principal fuente moderna, bellos colores a precio más accesible. Tailandia/Camboya: tonos más oscuros, amarronados. Rojo puro a ligeramente violáceo, con la máxima saturación sin llegar al negro.",
                puretTraitements = "El calentamiento es habitual y ampliamente aceptado por el mercado. El relleno de fracturas con vidrio de plomo debe divulgarse obligatoriamente: valor muy inferior y mayor fragilidad. Las inclusiones discretas de «seda» de rutilo se toleran, incluso se buscan.",
                entretien = "Dureza 9, muy resistente al uso diario. Un rubí relleno de vidrio sigue siendo frágil: protegerlo de productos de limpieza y de los ultrasonidos."
            ),
            BuyingGuideArticle(
                pierre = "Esmeralda",
                accroche = "Verde como ninguna otra piedra, la esmeralda fascina desde el Antiguo Egipto y sigue siendo una de las gemas más buscadas por los coleccionistas.",
                origineCouleur = "Colombia (Muzo, Chivor): verde intenso ligeramente azulado, referencia histórica. Zambia (Kagem): verde profundo, a menudo con mayor claridad. Brasil: producción abundante y más accesible, verde más claro. Se busca un verde saturado y uniforme, con un toque de azul.",
                puretTraitements = "Las inclusiones (su «jardín») forman casi parte de su identidad. Casi la totalidad de las piedras se tratan con aceite o resina incolora: la divulgación es obligatoria, y el grado de impregnación debe indicarse en el certificado (GIA, Gübelin, GRS).",
                entretien = "Dureza de 7,5 a 8, pero tenacidad limitada por las inclusiones. Quitar el anillo antes de actividad física, evitar productos químicos y prohibir el limpiador de ultrasonidos, que puede hacer salir el aceite del tratamiento."
            ),
            BuyingGuideArticle(
                pierre = "Diamante",
                accroche = "La referencia absoluta de la joyería, juzgada según sus cuatro criterios históricos: color, pureza, talla y quilate.",
                origineCouleur = "El origen geográfico pesa poco en el valor (salvo la trazabilidad ética del Proceso de Kimberley): lo que cuenta es el color, desde el blanco incoloro (D) más buscado hasta los raros tonos fantasía (azul, rosa, verde).",
                puretTraitements = "La pureza se clasifica con lupa de 10 aumentos, de «sin inclusiones» (FL) a incluido (I). El tratamiento HPHT y la perforación láser deben divulgarse y grabarse en el certificado: la gran mayoría del mercado permanece sin tratar.",
                entretien = "Dureza 10, insensible al desgaste diario, pero puede astillarse con un golpe fuerte. Los ultrasonidos son seguros, salvo en una piedra fracturada o rellena."
            ),
            BuyingGuideArticle(
                pierre = "Alejandrita",
                accroche = "La piedra «camaleón»: verde a la luz del día, roja o violeta bajo luz incandescente.",
                origineCouleur = "Los Urales rusos: yacimiento histórico, hoy prácticamente agotado. Sri Lanka y Brasil: fuentes principales actuales. África Oriental (Tanzania): producción más reciente. La intensidad del cambio de color prevalece sobre el propio matiz.",
                puretTraitements = "Raramente tratada. Piedra casi siempre incluida a simple vista: una claridad elevada es excepcional y muy valorada. La talla suele adaptarse para maximizar el efecto de cambio de color.",
                entretien = "Dureza 8,5, muy resistente. Sin cuidados particulares, más allá de las precauciones habituales para cualquier piedra de joyería."
            ),
            BuyingGuideArticle(
                pierre = "Topacio imperial",
                accroche = "No confundir con el topacio azul, siempre irradiado: el topacio imperial natural es la variedad más buscada.",
                origineCouleur = "Ouro Preto, en Brasil: fuente histórica y casi exclusiva de calidad gema. Tono anaranjado a rosa salmón; el rosa puro, llamado «topacio imperial rosa», es el más raro y valioso.",
                puretTraitements = "Generalmente sin tratar, a diferencia del topacio azul: un criterio de valor importante que conviene verificar en el certificado. Claridad naturalmente elevada.",
                entretien = "Dureza 8, pero un clivaje perfecto en una dirección lo hace frágil ante los golpes. Evitar cambios bruscos de temperatura y prohibir los ultrasonidos."
            ),
            BuyingGuideArticle(
                pierre = "Granate demantoide",
                accroche = "El más precioso de los granates: un verde deslumbrante gracias a una dispersión (fuego) que puede superar a la del diamante.",
                origineCouleur = "Los Urales rusos: fuente histórica, reconocible por sus inclusiones fibrosas en «cola de caballo». Namibia: principal producción moderna. Se busca un verde vivo, idealmente con poco amarillo.",
                puretTraitements = "Especie muy raramente tratada: la pureza natural es casi la norma. Las inclusiones rusas en «cola de caballo» incluso se buscan, como prueba de origen.",
                entretien = "Dureza de 6,5 a 7: más blanda que la mayoría de las piedras preciosas, hay que protegerla de golpes y rayones. Evitar los ultrasonidos si las inclusiones son importantes."
            ),
            BuyingGuideArticle(
                pierre = "Tanzanita",
                accroche = "Descubierta en 1967 al pie del Kilimanjaro, una gema procedente de un único yacimiento en el mundo.",
                origineCouleur = "Exclusivamente Merelani, en Tanzania: fuente única y no renovable, un fuerte argumento de rareza. Azul-violeta intenso, con marcado pleocroísmo (azul, violeta, burdeos según el ángulo); la talla orienta el color dominante.",
                puretTraitements = "El calentamiento para desarrollar el color azul-violeta es casi sistemático, estable y aceptado por el mercado. Claridad generalmente buena a simple vista.",
                entretien = "Dureza de solo 6 a 7, con un clivaje marcado: reservar a joyas poco expuestas a golpes. Prohibir los ultrasonidos y el vapor."
            ),
            BuyingGuideArticle(
                pierre = "Amatista",
                accroche = "La más preciosa de las variedades de cuarzo, piedra de nacimiento del mes de febrero.",
                origineCouleur = "Brasil y Uruguay: geodas, violeta claro a medio. Zambia: violeta profundo con reflejos rojizos, muy buscado. Siberia: fuente histórica hoy escasa. Se busca un violeta intenso y uniforme, sin zonas amarronadas.",
                puretTraitements = "Raramente tratada; a veces calentada para aclarar el tono o virar hacia la citrina. Claridad generalmente excelente, lo que la convierte en una piedra fina accesible.",
                entretien = "Dureza 7, robusta y fácil de mantener. Evitar la exposición prolongada al sol, que puede desvanecer el color con el tiempo."
            ),
            BuyingGuideArticle(
                pierre = "Espinela",
                accroche = "Durante mucho tiempo confundida con el rubí —el legendario «Rubí del Príncipe Negro» lo es en realidad—, la espinela se aprecia hoy por sí misma.",
                origineCouleur = "Birmania (Mogok): rojos y rosas vivos. Sri Lanka y Tanzania: amplia gama de colores, incluida la espinela «llama» anaranjada. Rojo, rosa, azul cobalto (raro y muy buscado) y gris «gunmetal».",
                puretTraitements = "Especie casi nunca tratada, salvo un calentamiento ocasional: una verdadera ventaja para el comprador en busca de autenticidad. Claridad generalmente buena a simple vista.",
                entretien = "Dureza 8, resistente. Se cuida como el zafiro, sin precauciones particulares."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina",
                accroche = "La piedra de las mil colores, desde el verde «paraíba» eléctrico hasta el bicolor rosa y verde «sandía».",
                origineCouleur = "Brasil (Paraíba): azul-verde cuprífero neón, legendario y rarísimo. Mozambique y Nigeria: tonos «tipo paraíba» más accesibles. Afganistán y Brasil: rubelita roja-rosa. Madagascar: paleta multicolor. La variedad paraíba encabeza la valoración, seguida de la rubelita intensa.",
                puretTraitements = "El calentamiento para intensificar ciertos tonos es habitual y aceptado. La claridad varía según la variedad; las inclusiones filiformes son frecuentes y se toleran si son discretas.",
                entretien = "Dureza de 7 a 7,5, buena robustez general. Piedra piroeléctrica que atrae el polvo por electricidad estática: se recomienda una limpieza suave y regular."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro",
                accroche = "El corindón azul por excelencia, pero también rosa, amarillo o «padparadscha» — justo por detrás del diamante en dureza.",
                origineCouleur = "Cachemira: azul aterciopelado legendario, yacimiento casi agotado, rarísimo. Birmania (Mogok): azul «real», intenso. Sri Lanka: azul más claro, gran transparencia. Madagascar: principal producción moderna. Se busca un azul aterciopelado profundo, con fuerte saturación sin exceso de negro.",
                puretTraitements = "El calentamiento es casi sistemático y ampliamente aceptado, mejora el color y la claridad. La difusión (titanio clásico, o berilio, más problemática) debe distinguirse obligatoriamente en el certificado. Se busca generalmente una claridad elevada.",
                entretien = "Dureza 9, justo por detrás del diamante: muy resistente al uso diario. Los ultrasonidos son seguros, salvo en una piedra difundida o rellena."
            )
        )
    )

    private val it = BuyingGuidesPage(
        intro = "Ogni pietra preziosa o fine si sceglie secondo criteri propri: origine e colore ricercati, purezza attesa e trattamenti comuni sul mercato, precauzioni di cura legate alla sua durezza. Ecco, pietra per pietra, ciò che esaminiamo da Gems of Rod prima di selezionare una gemma per la nostra clientela.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Rubino",
                accroche = "Il rosso assoluto: al massimo livello di qualità, il rubino può superare il diamante in valore al carato.",
                origineCouleur = "Birmania (Mogok): rosso «sangue di piccione», fluorescenza intensa, riferimento storico. Mozambico: principale fonte moderna, bei colori a prezzo più accessibile. Thailandia/Cambogia: tonalità più scure, brunastre. Rosso puro a leggermente violaceo, con la massima saturazione senza virare al nero.",
                puretTraitements = "Il riscaldamento è comune e ampiamente accettato dal mercato. Il riempimento delle fratture con vetro al piombo deve essere sempre dichiarato: valore molto inferiore e maggiore fragilità. Le discrete inclusioni «seta» di rutilo sono tollerate, a volte persino ricercate.",
                entretien = "Durezza 9, molto resistente all'uso quotidiano. Un rubino riempito con vetro resta fragile: proteggerlo dai prodotti chimici domestici e dagli ultrasuoni."
            ),
            BuyingGuideArticle(
                pierre = "Smeraldo",
                accroche = "Verde come nessun'altra pietra, lo smeraldo affascina fin dall'Antico Egitto e resta una delle gemme più ricercate dai collezionisti.",
                origineCouleur = "Colombia (Muzo, Chivor): verde intenso leggermente bluastro, riferimento storico. Zambia (Kagem): verde profondo, spesso con chiarezza superiore. Brasile: produzione abbondante e più accessibile, verde più chiaro. Si ricerca un verde saturo e omogeneo, con una punta di blu.",
                puretTraitements = "Le inclusioni (il suo «giardino») fanno quasi parte della sua identità. Quasi la totalità delle pietre è trattata con olio o resina incolore: la dichiarazione è obbligatoria, e il grado di impregnazione va precisato sul certificato (GIA, Gübelin, GRS).",
                entretien = "Durezza 7,5-8, ma tenacità limitata dalle inclusioni. Togliere l'anello prima di attività fisica, evitare i prodotti chimici e vietare il pulitore a ultrasuoni, che può far riaffiorare l'olio del trattamento."
            ),
            BuyingGuideArticle(
                pierre = "Diamante",
                accroche = "Il riferimento assoluto della gioielleria, giudicato secondo i suoi quattro criteri storici: colore, purezza, taglio e carati.",
                origineCouleur = "L'origine geografica pesa poco sul valore (a parte la tracciabilità etica del Processo di Kimberley): ciò che conta è il colore, dal bianco incolore (D) più ricercato alle rare tonalità fantasia (blu, rosa, verde).",
                puretTraitements = "La purezza è classificata con lente 10x, da «senza inclusioni» (FL) a incluso (I). Il trattamento HPHT e la foratura laser devono essere dichiarati e incisi sul certificato: la grande maggioranza del mercato resta non trattata.",
                entretien = "Durezza 10, insensibile all'usura quotidiana ma può scheggiarsi con un urto violento. Gli ultrasuoni sono sicuri, tranne su una pietra fratturata o riempita."
            ),
            BuyingGuideArticle(
                pierre = "Alessandrite",
                accroche = "La pietra «camaleonte»: verde alla luce del giorno, rossa o viola sotto luce incandescente.",
                origineCouleur = "Gli Urali russi: giacimento storico, oggi quasi esaurito. Sri Lanka e Brasile: principali fonti attuali. Africa orientale (Tanzania): produzione più recente. L'intensità del cambiamento di colore prevale sulla tonalità stessa.",
                puretTraitements = "Raramente trattata. Pietra quasi sempre inclusa a occhio nudo: una chiarezza elevata è eccezionale e molto apprezzata. Il taglio è spesso adattato per massimizzare l'effetto del cambiamento di colore.",
                entretien = "Durezza 8,5, molto resistente. Nessuna cura particolare, oltre alle normali precauzioni per qualsiasi pietra di gioielleria."
            ),
            BuyingGuideArticle(
                pierre = "Topazio imperiale",
                accroche = "Da non confondere con il topazio blu, sempre irradiato: il topazio imperiale naturale è la varietà più ricercata.",
                origineCouleur = "Ouro Preto, in Brasile: fonte storica e quasi esclusiva di qualità gemma. Tonalità arancione a rosa salmone; il rosa puro, detto «topazio imperiale rosa», è il più raro e prezioso.",
                puretTraitements = "Generalmente non trattato, a differenza del topazio blu: un criterio di valore importante da verificare sul certificato. Chiarezza naturalmente elevata.",
                entretien = "Durezza 8, ma una sfaldatura perfetta in una direzione lo rende fragile agli urti. Evitare sbalzi termici improvvisi e vietare gli ultrasuoni."
            ),
            BuyingGuideArticle(
                pierre = "Granato demantoide",
                accroche = "Il più prezioso dei granati: un verde brillante sostenuto da una dispersione (fuoco) che può superare quella del diamante.",
                origineCouleur = "Gli Urali russi: fonte storica, riconoscibile dalle inclusioni fibrose a «coda di cavallo». Namibia: principale produzione moderna. Si ricerca un verde vivo, idealmente con poco giallo.",
                puretTraitements = "Specie trattata molto raramente: la purezza naturale è quasi la norma. Le inclusioni russe a «coda di cavallo» sono persino ricercate, come prova di origine.",
                entretien = "Durezza 6,5-7: più tenero della maggior parte delle pietre preziose, va protetto da urti e graffi. Evitare gli ultrasuoni in presenza di inclusioni importanti."
            ),
            BuyingGuideArticle(
                pierre = "Tanzanite",
                accroche = "Scoperta nel 1967 ai piedi del Kilimangiaro, una gemma proveniente da un unico giacimento al mondo.",
                origineCouleur = "Esclusivamente Merelani, in Tanzania: fonte unica e non rinnovabile, un forte argomento di rarità. Blu-violetto intenso, con pleocroismo marcato (blu, viola, bordeaux a seconda dell'angolo); il taglio orienta il colore dominante.",
                puretTraitements = "Il riscaldamento per sviluppare il colore blu-violetto è quasi sistematico, stabile e accettato dal mercato. Chiarezza generalmente buona a occhio nudo.",
                entretien = "Durezza di soli 6-7, con una sfaldatura netta: da riservare a gioielli poco esposti a urti. Vietare ultrasuoni e vapore."
            ),
            BuyingGuideArticle(
                pierre = "Ametista",
                accroche = "La più preziosa delle varietà di quarzo, pietra portafortuna del mese di febbraio.",
                origineCouleur = "Brasile e Uruguay: geodi, viola chiaro a medio. Zambia: viola profondo con riflessi rossastri, molto ricercato. Siberia: fonte storica oggi rara. Si ricerca un viola intenso e omogeneo, senza zone brunastre.",
                puretTraitements = "Raramente trattata; a volte riscaldata per schiarire la tonalità o virare verso il citrino. Chiarezza generalmente eccellente, il che ne fa una pietra fine accessibile.",
                entretien = "Durezza 7, robusta e facile da curare. Evitare un'esposizione prolungata al sole, che può sbiadire il colore nel tempo."
            ),
            BuyingGuideArticle(
                pierre = "Spinello",
                accroche = "A lungo confuso con il rubino — il leggendario «Rubino del Principe Nero» lo è in realtà —, lo spinello è oggi apprezzato per se stesso.",
                origineCouleur = "Birmania (Mogok): rossi e rosa vivaci. Sri Lanka e Tanzania: ampia gamma di colori, incluso lo spinello «fiamma» arancione. Rosso, rosa, blu cobalto (raro e molto ricercato) e grigio «canna di fucile».",
                puretTraitements = "Specie quasi mai trattata, a parte un riscaldamento occasionale: un vero vantaggio per l'acquirente in cerca di autenticità. Chiarezza generalmente buona a occhio nudo.",
                entretien = "Durezza 8, resistente. Si cura come lo zaffiro, senza precauzioni particolari."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina",
                accroche = "La pietra dai mille colori, dal verde «paraíba» elettrico al bicolore rosa e verde «anguria».",
                origineCouleur = "Brasile (Paraíba): blu-verde cuprifero neon, leggendario e rarissimo. Mozambico e Nigeria: tonalità «tipo paraíba» più accessibili. Afghanistan e Brasile: rubellite rosso-rosa. Madagascar: tavolozza multicolore. La varietà paraíba guida la valutazione, seguita dalla rubellite intensa.",
                puretTraitements = "Il riscaldamento per intensificare alcune tonalità è comune e accettato. La chiarezza varia secondo la varietà; le inclusioni filiformi sono frequenti e tollerate se discrete.",
                entretien = "Durezza 7-7,5, buona robustezza generale. Pietra piroelettrica che attira la polvere per elettricità statica: si consiglia una pulizia delicata e regolare."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro",
                accroche = "Il corindone blu per eccellenza, ma anche rosa, giallo o «padparadscha» — subito dopo il diamante in durezza.",
                origineCouleur = "Kashmir: blu vellutato leggendario, giacimento quasi esaurito, rarissimo. Birmania (Mogok): blu «royal», intenso. Sri Lanka: blu più chiaro, grande trasparenza. Madagascar: principale produzione moderna. Si ricerca un blu vellutato profondo, con forte saturazione senza eccesso di nero.",
                puretTraitements = "Il riscaldamento è quasi sistematico e ampiamente accettato, migliora colore e chiarezza. La diffusione (titanio classico, o berillio, più problematica) deve essere obbligatoriamente distinta sul certificato. Si ricerca generalmente una chiarezza elevata.",
                entretien = "Durezza 9, subito dopo il diamante: molto resistente all'uso quotidiano. Gli ultrasuoni sono sicuri, tranne su una pietra diffusa o riempita."
            )
        )
    )

    private val de = BuyingGuidesPage(
        intro = "Jeder Edel- oder Schmuckstein wird nach eigenen Kriterien ausgewählt: gesuchte Herkunft und Farbe, erwartete Reinheit und marktübliche Behandlungen, Pflegehinweise je nach Härte. Hier sehen Sie, Stein für Stein, worauf wir bei Gems of Rod achten, bevor wir eine Gemme für unsere Kundschaft auswählen.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Rubin",
                accroche = "Das ultimative Rot: In der höchsten Qualitätsstufe kann der Rubin den Diamanten im Preis pro Karat übertreffen.",
                origineCouleur = "Myanmar (Mogok): «Taubenblut»-Rot mit intensiver Fluoreszenz, die historische Referenz. Mosambik: heute die wichtigste Quelle, schöne Farben zu erschwinglicheren Preisen. Thailand/Kambodscha: dunklere, bräunliche Töne. Reines bis leicht violettes Rot mit maximaler Sättigung, ohne ins Schwarze zu kippen.",
                puretTraitements = "Erhitzen ist gängig und wird vom Markt weithin akzeptiert. Eine Bleiglas-Verfüllung von Rissen muss zwingend offengelegt werden: deutlich geringerer Wert und höhere Zerbrechlichkeit. Dezente «Seiden»-Einschlüsse aus Rutil werden toleriert, mitunter sogar geschätzt.",
                entretien = "Härte 9, sehr widerstandsfähig im Alltag. Ein mit Bleiglas verfüllter Rubin bleibt zerbrechlich: vor Haushaltschemikalien und Ultraschallreinigern schützen."
            ),
            BuyingGuideArticle(
                pierre = "Smaragd",
                accroche = "Grün wie kein anderer Stein — der Smaragd fasziniert seit dem alten Ägypten und zählt bis heute zu den begehrtesten Sammlersteinen.",
                origineCouleur = "Kolumbien (Muzo, Chivor): intensives, leicht bläuliches Grün, die historische Referenz. Sambia (Kagem): tiefes Grün, oft mit überdurchschnittlicher Reinheit. Brasilien: reichliche, erschwinglichere Produktion mit hellerem Grün. Gesucht ist ein gesättigtes, gleichmäßiges Grün mit einem Hauch Blau.",
                puretTraitements = "Einschlüsse (sein «Garten») gehören fast zu seiner Identität. Nahezu alle Steine werden mit farblosem Öl oder Harz behandelt: Offenlegung ist Pflicht, der Grad der Imprägnierung muss auf dem Zertifikat (GIA, Gübelin, GRS) angegeben werden.",
                entretien = "Härte 7,5 bis 8, aber durch Einschlüsse begrenzte Zähigkeit. Den Ring vor körperlicher Aktivität ablegen, Chemikalien meiden und keinesfalls den Ultraschallreiniger verwenden, der das Behandlungsöl wieder heraustreten lässt."
            ),
            BuyingGuideArticle(
                pierre = "Diamant",
                accroche = "Die ultimative Referenz der Schmuckkunst, beurteilt nach den vier klassischen Kriterien: Farbe, Reinheit, Schliff und Karat.",
                origineCouleur = "Die geografische Herkunft spielt für den Wert kaum eine Rolle (abgesehen von der ethischen Rückverfolgbarkeit nach dem Kimberley-Prozess): Entscheidend ist die Farbe, vom begehrten farblosen Weiß (D) bis zu seltenen Fancy-Farben (Blau, Rosa, Grün).",
                puretTraitements = "Die Reinheit wird bei 10-facher Lupenvergrößerung eingestuft, von «lupenrein» (FL) bis «included» (I). HPHT-Behandlung und Laserbohrung müssen offengelegt und auf dem Zertifikat lasergraviert werden: Der weitaus größte Teil des Marktes bleibt unbehandelt.",
                entretien = "Härte 10, unempfindlich gegen alltägliche Abnutzung, kann aber bei einem harten Schlag absplittern. Ultraschallreinigung ist unbedenklich, außer bei einem gebrochenen oder verfüllten Stein."
            ),
            BuyingGuideArticle(
                pierre = "Alexandrit",
                accroche = "Der «Chamäleon»-Stein: grün bei Tageslicht, rot oder violett unter Glühlicht.",
                origineCouleur = "Der russische Ural: die historische Lagerstätte, heute nahezu erschöpft. Sri Lanka und Brasilien: die heute wichtigsten Quellen. Ostafrika (Tansania): eine jüngere Förderung. Die Stärke des Farbwechsels zählt mehr als der Farbton selbst.",
                puretTraitements = "Selten behandelt. Der Stein ist fast immer mit bloßem Auge sichtbar eingeschlossen: hohe Reinheit ist außergewöhnlich und sehr geschätzt. Der Schliff wird oft so gewählt, dass der Farbwechseleffekt maximiert wird.",
                entretien = "Härte 8,5, sehr widerstandsfähig. Keine besondere Pflege nötig, über die üblichen Vorsichtsmaßnahmen für jeden Schmuckstein hinaus."
            ),
            BuyingGuideArticle(
                pierre = "Imperialtopas",
                accroche = "Nicht zu verwechseln mit dem stets bestrahlten blauen Topas: Der natürliche Imperialtopas ist die begehrteste Varietät.",
                origineCouleur = "Ouro Preto in Brasilien: die historische und nahezu ausschließliche Quelle für Edelsteinqualität. Orange bis lachsrosa Töne; das reine Rosa, «rosa Imperialtopas» genannt, ist am seltensten und wertvollsten.",
                puretTraitements = "Im Gegensatz zum blauen Topas in der Regel unbehandelt: ein wichtiges Wertkriterium, das auf dem Zertifikat zu prüfen ist. Von Natur aus hohe Reinheit.",
                entretien = "Härte 8, doch eine perfekte Spaltbarkeit in eine Richtung macht ihn stoßempfindlich. Plötzliche Temperaturwechsel vermeiden, Ultraschallreiniger meiden."
            ),
            BuyingGuideArticle(
                pierre = "Demantoid-Granat",
                accroche = "Der wertvollste aller Granate: ein leuchtendes Grün, getragen von einer Dispersion (Feuer), die die des Diamanten übertreffen kann.",
                origineCouleur = "Der russische Ural: die historische Quelle, erkennbar an faserigen «Pferdeschweif»-Einschlüssen. Namibia: die wichtigste moderne Förderung. Gesucht ist ein kräftiges Grün mit möglichst wenig Gelbanteil.",
                puretTraitements = "Diese Art wird nur sehr selten behandelt: natürliche Reinheit ist nahezu die Regel. Russische «Pferdeschweif»-Einschlüsse werden als Herkunftsnachweis sogar geschätzt.",
                entretien = "Härte 6,5 bis 7: weicher als die meisten Edelsteine, vor Stößen und Kratzern zu schützen. Bei ausgeprägten Einschlüssen Ultraschallreinigung vermeiden."
            ),
            BuyingGuideArticle(
                pierre = "Tansanit",
                accroche = "1967 am Fuße des Kilimandscharo entdeckt — ein Edelstein aus einer weltweit einzigartigen Lagerstätte.",
                origineCouleur = "Ausschließlich Merelani in Tansania: eine einzigartige, nicht erneuerbare Quelle, ein starkes Argument für Seltenheit. Intensives Blauviolett mit ausgeprägtem Pleochroismus (Blau, Violett, Bordeaux je nach Blickwinkel); der Schliff bestimmt die dominierende Farbe.",
                puretTraitements = "Das Erhitzen zur Entwicklung der blauvioletten Farbe ist nahezu die Regel, stabil und vom Markt akzeptiert. Reinheit meist gut mit bloßem Auge.",
                entretien = "Nur Härte 6 bis 7, mit deutlicher Spaltbarkeit: für Schmuckstücke reserviert, die wenig Stößen ausgesetzt sind. Ultraschall- und Dampfreinigung meiden."
            ),
            BuyingGuideArticle(
                pierre = "Amethyst",
                accroche = "Die wertvollste Quarzvarietät, der Geburtsstein des Monats Februar.",
                origineCouleur = "Brasilien und Uruguay: Geoden, helles bis mittleres Violett. Sambia: tiefes Violett mit rötlichen Reflexen, sehr begehrt. Sibirien: historische, heute seltene Quelle. Gesucht ist ein intensives, gleichmäßiges Violett ohne bräunliche Zonen.",
                puretTraitements = "Selten behandelt; gelegentlich erhitzt, um die Farbe aufzuhellen oder in Citrin zu verwandeln. Reinheit in der Regel ausgezeichnet, was ihn zu einem erschwinglichen Schmuckstein macht.",
                entretien = "Härte 7, robust und pflegeleicht. Längere Sonneneinstrahlung vermeiden, da die Farbe mit der Zeit verblassen kann."
            ),
            BuyingGuideArticle(
                pierre = "Spinell",
                accroche = "Lange mit dem Rubin verwechselt — der legendäre «Black Prince's Ruby» ist tatsächlich ein Spinell —, wird der Spinell heute um seiner selbst willen geschätzt.",
                origineCouleur = "Myanmar (Mogok): kräftige Rot- und Rosatöne. Sri Lanka und Tansania: breite Farbpalette, darunter der orangefarbene «Flammen»-Spinell. Rot, Rosa, Kobaltblau (selten und sehr begehrt) sowie «Gunmetal»-Grau.",
                puretTraitements = "Diese Art wird so gut wie nie behandelt, abgesehen von gelegentlichem Erhitzen: ein echter Vorteil für Käufer, die Authentizität suchen. Reinheit meist gut mit bloßem Auge.",
                entretien = "Härte 8, widerstandsfähig. Pflege wie beim Saphir, keine besonderen Vorsichtsmaßnahmen nötig."
            ),
            BuyingGuideArticle(
                pierre = "Turmalin",
                accroche = "Der Stein der tausend Farben, vom elektrischen «Paraíba»-Grünblau bis zum zweifarbigen rosa-grünen «Wassermelonen»-Turmalin.",
                origineCouleur = "Brasilien (Paraíba): legendäres, äußerst seltenes kupferhaltiges Neon-Grünblau. Mosambik und Nigeria: erschwinglichere «Paraíba-ähnliche» Töne. Afghanistan und Brasilien: rot-rosa Rubellit. Madagaskar: bunte Farbpalette. Die Paraíba-Varietät führt die Wertskala an, gefolgt vom intensiven Rubellit.",
                puretTraitements = "Erhitzen zur Intensivierung bestimmter Farbtöne ist üblich und akzeptiert. Die Reinheit variiert je nach Varietät; feine nadelförmige Einschlüsse sind häufig und werden toleriert, wenn sie dezent sind.",
                entretien = "Härte 7 bis 7,5, insgesamt robust. Ein pyroelektrischer Stein, der durch statische Elektrizität Staub anzieht: eine sanfte, regelmäßige Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Saphir",
                accroche = "Der blaue Korund schlechthin, aber auch in Rosa, Gelb oder als «Padparadscha» — gleich nach dem Diamanten die härteste Varietät.",
                origineCouleur = "Kaschmir: legendäres samtiges Blau, nahezu erschöpfte, äußerst seltene Lagerstätte. Myanmar (Mogok): intensives «Royal»-Blau. Sri Lanka: helleres Blau mit großer Transparenz. Madagaskar: die wichtigste moderne Förderung. Gesucht ist ein tiefes samtiges Blau mit starker Sättigung, ohne zu viel Schwarzanteil.",
                puretTraitements = "Erhitzen ist nahezu die Regel und wird weithin akzeptiert, verbessert Farbe und Reinheit. Diffusion (klassisch mit Titan, oder problematischer mit Beryllium) muss zwingend auf dem Zertifikat unterschieden werden. In der Regel wird hohe Reinheit gesucht.",
                entretien = "Härte 9, gleich nach dem Diamanten: sehr widerstandsfähig im Alltag. Ultraschallreinigung ist unbedenklich, außer bei einem diffundierten oder verfüllten Stein."
            )
        )
    )

    private val pt = BuyingGuidesPage(
        intro = "Cada pedra preciosa ou fina escolhe-se segundo critérios próprios: origem e cor procuradas, pureza esperada e tratamentos comuns do mercado, precauções de cuidado ligadas à sua dureza. Eis, pedra a pedra, o que examinamos na Gems of Rod antes de selecionar uma gema para a nossa clientela.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Rubi",
                accroche = "O vermelho absoluto: no seu grau máximo de qualidade, o rubi pode superar o diamante em valor por quilate.",
                origineCouleur = "Birmânia (Mogok): vermelho «sangue de pombo», fluorescência intensa, referência histórica. Moçambique: principal fonte moderna, belas cores a preço mais acessível. Tailândia/Camboja: tons mais escuros, acastanhados. Vermelho puro a ligeiramente violáceo, com saturação máxima sem tender ao negro.",
                puretTraitements = "O aquecimento é comum e amplamente aceite pelo mercado. O preenchimento de fraturas com vidro de chumbo deve ser sempre divulgado: valor muito inferior e maior fragilidade. Inclusões discretas de «seda» de rutilo são toleradas, por vezes até procuradas.",
                entretien = "Dureza 9, muito resistente ao uso diário. Um rubi preenchido com vidro continua frágil: proteger de produtos de limpeza e de ultrassons."
            ),
            BuyingGuideArticle(
                pierre = "Esmeralda",
                accroche = "Verde como nenhuma outra pedra, a esmeralda fascina desde o Antigo Egito e continua a ser uma das gemas mais procuradas pelos colecionadores.",
                origineCouleur = "Colômbia (Muzo, Chivor): verde intenso ligeiramente azulado, referência histórica. Zâmbia (Kagem): verde profundo, muitas vezes com pureza superior. Brasil: produção abundante e mais acessível, verde mais claro. Procura-se um verde saturado e uniforme, com um toque de azul.",
                puretTraitements = "As inclusões (o seu «jardim») fazem quase parte da sua identidade. Quase a totalidade das pedras é tratada com óleo ou resina incolor: a divulgação é obrigatória, e o grau de impregnação deve constar do certificado (GIA, Gübelin, GRS).",
                entretien = "Dureza de 7,5 a 8, mas tenacidade limitada pelas inclusões. Retirar o anel antes de atividade física, evitar produtos químicos e proibir o limpador de ultrassons, que pode fazer sair o óleo do tratamento."
            ),
            BuyingGuideArticle(
                pierre = "Diamante",
                accroche = "A referência absoluta da joalharia, avaliada segundo os seus quatro critérios históricos: cor, pureza, lapidação e quilate.",
                origineCouleur = "A origem geográfica pesa pouco no valor (à exceção da rastreabilidade ética do Processo de Kimberley): o que conta é a cor, do branco incolor (D) mais procurado às raras cores fantasia (azul, rosa, verde).",
                puretTraitements = "A pureza é classificada com lupa de 10 aumentos, de «sem inclusões» (FL) a incluído (I). O tratamento HPHT e a perfuração a laser devem ser divulgados e gravados no certificado: a grande maioria do mercado permanece não tratada.",
                entretien = "Dureza 10, insensível ao desgaste diário, mas pode lascar com um impacto forte. Os ultrassons são seguros, exceto numa pedra fraturada ou preenchida."
            ),
            BuyingGuideArticle(
                pierre = "Alexandrita",
                accroche = "A pedra «camaleão»: verde à luz do dia, vermelha ou violeta sob luz incandescente.",
                origineCouleur = "Os Urais russos: jazida histórica, hoje praticamente esgotada. Sri Lanka e Brasil: principais fontes atuais. África Oriental (Tanzânia): produção mais recente. A intensidade da mudança de cor prevalece sobre o próprio tom.",
                puretTraitements = "Raramente tratada. Pedra quase sempre incluída a olho nu: uma pureza elevada é excecional e muito valorizada. A lapidação é frequentemente adaptada para maximizar o efeito de mudança de cor.",
                entretien = "Dureza 8,5, muito resistente. Sem cuidados especiais, além das precauções habituais para qualquer pedra de joalharia."
            ),
            BuyingGuideArticle(
                pierre = "Topázio imperial",
                accroche = "A não confundir com o topázio azul, sempre irradiado: o topázio imperial natural é a variedade mais procurada.",
                origineCouleur = "Ouro Preto, no Brasil: fonte histórica e quase exclusiva de qualidade gema. Tom laranja a rosa salmão; o rosa puro, chamado «topázio imperial rosa», é o mais raro e valioso.",
                puretTraitements = "Geralmente não tratado, ao contrário do topázio azul: um critério de valor importante a verificar no certificado. Pureza naturalmente elevada.",
                entretien = "Dureza 8, mas uma clivagem perfeita numa direção torna-o frágil a impactos. Evitar variações térmicas bruscas e proibir os ultrassons."
            ),
            BuyingGuideArticle(
                pierre = "Granada demantoide",
                accroche = "A mais preciosa das granadas: um verde deslumbrante sustentado por uma dispersão (fogo) que pode superar a do diamante.",
                origineCouleur = "Os Urais russos: fonte histórica, reconhecível pelas suas inclusões fibrosas em «cauda de cavalo». Namíbia: principal produção moderna. Procura-se um verde vivo, idealmente com pouco amarelo.",
                puretTraitements = "Espécie muito raramente tratada: a pureza natural é quase a norma. As inclusões russas em «cauda de cavalo» são até procuradas, como prova de origem.",
                entretien = "Dureza de 6,5 a 7: mais macia do que a maioria das pedras preciosas, deve ser protegida de impactos e riscos. Evitar ultrassons na presença de inclusões importantes."
            ),
            BuyingGuideArticle(
                pierre = "Tanzanite",
                accroche = "Descoberta em 1967 aos pés do Kilimanjaro, uma gema proveniente de uma jazida única no mundo.",
                origineCouleur = "Exclusivamente Merelani, na Tanzânia: fonte única e não renovável, um forte argumento de raridade. Azul-violeta intenso, com pleocroísmo marcado (azul, violeta, bordô consoante o ângulo); a lapidação orienta a cor dominante.",
                puretTraitements = "O aquecimento para desenvolver a cor azul-violeta é quase sistemático, estável e aceite pelo mercado. Pureza geralmente boa a olho nu.",
                entretien = "Dureza de apenas 6 a 7, com clivagem marcada: reservar para joias pouco expostas a impactos. Proibir ultrassons e vapor."
            ),
            BuyingGuideArticle(
                pierre = "Ametista",
                accroche = "A mais preciosa das variedades de quartzo, pedra de nascimento do mês de fevereiro.",
                origineCouleur = "Brasil e Uruguai: geodas, violeta claro a médio. Zâmbia: violeta profundo com reflexos avermelhados, muito procurado. Sibéria: fonte histórica hoje rara. Procura-se um violeta intenso e uniforme, sem zonas acastanhadas.",
                puretTraitements = "Raramente tratada; por vezes aquecida para clarear o tom ou virar para citrino. Pureza geralmente excelente, o que a torna uma pedra fina acessível.",
                entretien = "Dureza 7, robusta e fácil de cuidar. Evitar a exposição prolongada ao sol, que pode desbotar a cor com o tempo."
            ),
            BuyingGuideArticle(
                pierre = "Espinela",
                accroche = "Durante muito tempo confundida com o rubi — o lendário «Rubi do Príncipe Negro» é na verdade uma espinela —, a espinela é hoje apreciada por si própria.",
                origineCouleur = "Birmânia (Mogok): vermelhos e rosas vivos. Sri Lanka e Tanzânia: ampla gama de cores, incluindo a espinela «chama» alaranjada. Vermelho, rosa, azul cobalto (raro e muito procurado) e cinzento «gunmetal».",
                puretTraitements = "Espécie quase nunca tratada, à exceção de um aquecimento ocasional: uma verdadeira vantagem para o comprador em busca de autenticidade. Pureza geralmente boa a olho nu.",
                entretien = "Dureza 8, resistente. Cuida-se como a safira, sem precauções especiais."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina",
                accroche = "A pedra de mil cores, do verde «paraíba» elétrico ao bicolor rosa e verde «melancia».",
                origineCouleur = "Brasil (Paraíba): azul-esverdeado cuprífero néon, lendário e raríssimo. Moçambique e Nigéria: tons «tipo paraíba» mais acessíveis. Afeganistão e Brasil: rubelite vermelho-rosa. Madagáscar: paleta multicolor. A variedade paraíba lidera a valorização, seguida da rubelite intensa.",
                puretTraitements = "O aquecimento para intensificar certos tons é comum e aceite. A pureza varia consoante a variedade; inclusões filiformes são frequentes e toleradas se discretas.",
                entretien = "Dureza de 7 a 7,5, boa robustez geral. Pedra piroelétrica que atrai poeira por eletricidade estática: recomenda-se uma limpeza suave e regular."
            ),
            BuyingGuideArticle(
                pierre = "Safira",
                accroche = "O corindo azul por excelência, mas também rosa, amarelo ou «padparadscha» — logo atrás do diamante em dureza.",
                origineCouleur = "Caxemira: azul aveludado lendário, jazida quase esgotada, raríssima. Birmânia (Mogok): azul «royal», intenso. Sri Lanka: azul mais claro, grande transparência. Madagáscar: principal produção moderna. Procura-se um azul aveludado profundo, com forte saturação sem excesso de negro.",
                puretTraitements = "O aquecimento é quase sistemático e amplamente aceite, melhora a cor e a pureza. A difusão (titânio clássico, ou berílio, mais problemática) deve ser obrigatoriamente distinguida no certificado. Procura-se geralmente uma pureza elevada.",
                entretien = "Dureza 9, logo atrás do diamante: muito resistente ao uso diário. Os ultrassons são seguros, exceto numa pedra difundida ou preenchida."
            )
        )
    )

    private val ru = BuyingGuidesPage(
        intro = "Каждый драгоценный или полудрагоценный камень выбирают по своим критериям: желаемое происхождение и цвет, ожидаемая чистота и распространённые на рынке обработки, меры по уходу с учётом твёрдости камня. Вот, камень за камнем, на что мы обращаем внимание в Gems of Rod перед тем, как отобрать самоцвет для наших клиентов.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Рубин",
                accroche = "Абсолютный красный: в своём высшем качестве рубин может превосходить бриллиант по стоимости карата.",
                origineCouleur = "Мьянма (Могок): красный цвет «голубиная кровь», интенсивная флуоресценция, исторический эталон. Мозамбик: главный современный источник, красивые цвета по более доступной цене. Таиланд/Камбоджа: более тёмные, коричневатые тона. Чистый красный с лёгким фиолетовым оттенком, максимальная насыщенность без перехода в чёрный.",
                puretTraitements = "Термообработка распространена и широко принята рынком. Заполнение трещин свинцовым стеклом должно обязательно указываться: значительно более низкая стоимость и повышенная хрупкость. Незаметные включения рутила («шёлк») допустимы, иногда даже ценятся.",
                entretien = "Твёрдость 9, очень устойчив к повседневному износу. Рубин, заполненный стеклом, остаётся хрупким: беречь от бытовой химии и ультразвуковой чистки."
            ),
            BuyingGuideArticle(
                pierre = "Изумруд",
                accroche = "Зелёный, как ни один другой камень: изумруд завораживал ещё в Древнем Египте и остаётся одним из самых востребованных камней у коллекционеров.",
                origineCouleur = "Колумбия (Музо, Чивор): интенсивный, слегка голубоватый зелёный цвет, исторический эталон. Замбия (Кагем): глубокий зелёный, часто с более высокой чистотой. Бразилия: обильная и более доступная добыча, более светлый зелёный. Ценится насыщенный однородный зелёный с лёгким голубым оттенком.",
                puretTraitements = "Включения (его «сад») почти неотъемлемая часть его природы. Почти все камни обрабатываются бесцветным маслом или смолой: раскрытие обязательно, степень пропитки должна указываться в сертификате (GIA, Gübelin, GRS).",
                entretien = "Твёрдость 7,5–8, но вязкость ограничена включениями. Снимать кольцо перед физической активностью, избегать химикатов и никогда не использовать ультразвуковую чистку, которая может вывести масло обработки наружу."
            ),
            BuyingGuideArticle(
                pierre = "Алмаз (бриллиант)",
                accroche = "Абсолютный эталон ювелирного искусства, оцениваемый по четырём классическим критериям: цвет, чистота, огранка и карат.",
                origineCouleur = "Географическое происхождение мало влияет на стоимость (за исключением этической прослеживаемости по Кимберлийскому процессу): значение имеет цвет — от желанного бесцветного белого (D) до редких фантазийных оттенков (синего, розового, зелёного).",
                puretTraitements = "Чистота оценивается под лупой 10x, от «без включений» (FL) до включённого (I). Обработка HPHT и лазерное сверление должны раскрываться и гравироваться на сертификате: подавляющее большинство камней на рынке остаются необработанными.",
                entretien = "Твёрдость 10, устойчив к повседневному износу, но может расколоться от сильного удара. Ультразвуковая чистка безопасна, кроме камней с трещинами или заполнениями."
            ),
            BuyingGuideArticle(
                pierre = "Александрит",
                accroche = "Камень-«хамелеон»: зелёный при дневном свете, красный или фиолетовый при лампах накаливания.",
                origineCouleur = "Уральские месторождения России: историческое месторождение, сегодня почти исчерпанное. Шри-Ланка и Бразилия: основные современные источники. Восточная Африка (Танзания): более новая добыча. Сила смены цвета важнее самого оттенка.",
                puretTraitements = "Редко обрабатывается. Камень почти всегда содержит видимые невооружённым глазом включения: высокая чистота исключительна и высоко ценится. Огранка часто подбирается так, чтобы максимизировать эффект смены цвета.",
                entretien = "Твёрдость 8,5, очень устойчив. Особого ухода не требует, помимо обычных мер предосторожности для любого ювелирного камня."
            ),
            BuyingGuideArticle(
                pierre = "Топаз империал",
                accroche = "Не следует путать с голубым топазом, который всегда облучён: природный топаз империал — самая востребованная разновидность.",
                origineCouleur = "Ору-Прету в Бразилии: исторический и почти единственный источник ювелирного качества. Оранжево-лососевые оттенки; чистый розовый, называемый «розовый топаз империал», самый редкий и ценный.",
                puretTraitements = "Обычно не обрабатывается, в отличие от голубого топаза: важный ценовой критерий, который нужно проверять по сертификату. Природная высокая чистота.",
                entretien = "Твёрдость 8, но идеальная спайность в одном направлении делает его хрупким к ударам. Избегать резких перепадов температуры, не использовать ультразвуковую чистку."
            ),
            BuyingGuideArticle(
                pierre = "Гранат демантоид",
                accroche = "Самый ценный из гранатов: яркий зелёный цвет, подчёркнутый дисперсией («игрой»), которая может превосходить бриллиантовую.",
                origineCouleur = "Уральские месторождения России: исторический источник, узнаваемый по волокнистым включениям типа «конский хвост». Намибия: основная современная добыча. Ценится яркий зелёный цвет с минимумом жёлтого оттенка.",
                puretTraitements = "Этот вид камня крайне редко обрабатывается: природная чистота — почти правило. Российские включения «конский хвост» даже ценятся как подтверждение происхождения.",
                entretien = "Твёрдость 6,5–7: мягче большинства драгоценных камней, требует защиты от ударов и царапин. Избегать ультразвуковой чистки при значительных включениях."
            ),
            BuyingGuideArticle(
                pierre = "Танзанит",
                accroche = "Открыт в 1967 году у подножия Килиманджаро — камень из единственного в мире месторождения.",
                origineCouleur = "Исключительно Мерелани в Танзании: единственный и невозобновляемый источник, весомый аргумент редкости. Насыщенный сине-фиолетовый цвет с выраженным плеохроизмом (синий, фиолетовый, бордовый в зависимости от угла); огранка определяет преобладающий оттенок.",
                puretTraitements = "Термообработка для развития сине-фиолетового цвета почти повсеместна, стабильна и принята рынком. Чистота обычно хороша невооружённым глазом.",
                entretien = "Твёрдость всего 6–7, с выраженной спайностью: подходит для украшений, редко подвергающихся ударам. Не использовать ультразвуковую и паровую чистку."
            ),
            BuyingGuideArticle(
                pierre = "Аметист",
                accroche = "Самая ценная разновидность кварца, камень-талисман февраля.",
                origineCouleur = "Бразилия и Уругвай: жеоды, светлый до среднего фиолетовый. Замбия: глубокий фиолетовый с красноватыми отблесками, очень востребован. Сибирь: исторический, сегодня редкий источник. Ценится насыщенный однородный фиолетовый без буроватых зон.",
                puretTraitements = "Редко обрабатывается; иногда нагревается для осветления оттенка или превращения в цитрин. Чистота обычно отличная, что делает его доступным полудрагоценным камнем.",
                entretien = "Твёрдость 7, прочен и прост в уходе. Избегать длительного пребывания на солнце, так как цвет может со временем выцветать."
            ),
            BuyingGuideArticle(
                pierre = "Шпинель",
                accroche = "Долгое время её путали с рубином — легендарный «рубин Чёрного принца» на самом деле шпинель, — сегодня шпинель ценится сама по себе.",
                origineCouleur = "Мьянма (Могок): яркие красные и розовые тона. Шри-Ланка и Танзания: широкая палитра цветов, включая оранжевую шпинель «пламя». Красный, розовый, кобальтово-синий (редкий и очень востребованный) и серый «gunmetal».",
                puretTraitements = "Этот вид камня почти никогда не обрабатывается, за исключением редкого нагрева: настоящее преимущество для покупателя, ищущего подлинность. Чистота обычно хороша невооружённым глазом.",
                entretien = "Твёрдость 8, устойчив. Уход как за сапфиром, без особых предосторожностей."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин",
                accroche = "Камень тысячи цветов — от электрически-зелёного «параиба» до двухцветного розово-зелёного «арбузного» турмалина.",
                origineCouleur = "Бразилия (Параиба): легендарный, чрезвычайно редкий медьсодержащий неоново-сине-зелёный цвет. Мозамбик и Нигерия: более доступные оттенки «в стиле параиба». Афганистан и Бразилия: красно-розовый рубеллит. Мадагаскар: многоцветная палитра. Разновидность параиба лидирует по стоимости, за ней следует насыщенный рубеллит.",
                puretTraitements = "Термообработка для усиления некоторых оттенков распространена и принята рынком. Чистота варьируется в зависимости от разновидности; тонкие игольчатые включения часты и допустимы, если незаметны.",
                entretien = "Твёрдость 7–7,5, в целом прочен. Пироэлектрический камень, притягивающий пыль статическим электричеством: рекомендуется бережная регулярная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир",
                accroche = "Синий корунд по преимуществу, но также розовый, жёлтый или «падпараджа» — по твёрдости уступает только алмазу.",
                origineCouleur = "Кашмир: легендарный бархатистый синий цвет, почти исчерпанное, чрезвычайно редкое месторождение. Мьянма (Могок): интенсивный «королевский» синий. Шри-Ланка: более светлый синий, высокая прозрачность. Мадагаскар: основная современная добыча. Ценится глубокий бархатистый синий с высокой насыщенностью без избытка чёрного.",
                puretTraitements = "Термообработка почти повсеместна и широко принята, улучшает цвет и чистоту. Диффузия (классическая титановая или более проблематичная бериллиевая) должна обязательно указываться отдельно в сертификате. Обычно ценится высокая чистота.",
                entretien = "Твёрдость 9, сразу после алмаза: очень устойчив к повседневному износу. Ультразвуковая чистка безопасна, кроме камней с диффузией или заполнением."
            )
        )
    )

    private val nl = BuyingGuidesPage(
        intro = "Elke edelsteen of halfedelsteen wordt op eigen criteria gekozen: gewenste herkomst en kleur, verwachte zuiverheid en gangbare marktbehandelingen, onderhoudsvoorzorgen die samenhangen met de hardheid. Hier ziet u, steen voor steen, waar wij bij Gems of Rod op letten voordat we een edelsteen voor onze klanten selecteren.",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "Robijn",
                accroche = "Het ultieme rood: in de hoogste kwaliteit kan robijn diamant in waarde per karaat overtreffen.",
                origineCouleur = "Myanmar (Mogok): «duivenbloed»-rood met intense fluorescentie, de historische referentie. Mozambique: de belangrijkste moderne bron, mooie kleuren tegen een toegankelijkere prijs. Thailand/Cambodja: donkerdere, bruinachtige tinten. Zuiver tot licht paarsig rood, met maximale verzadiging zonder naar zwart te neigen.",
                puretTraitements = "Verhitten is gebruikelijk en wordt door de markt algemeen aanvaard. Het opvullen van breuken met loodglas moet altijd worden vermeld: aanzienlijk lagere waarde en grotere breekbaarheid. Discrete rutiel-«zijde»-insluitsels worden getolereerd, soms zelfs gewaardeerd.",
                entretien = "Hardheid 9, zeer bestand tegen dagelijks gebruik. Een met loodglas opgevulde robijn blijft breekbaar: beschermen tegen huishoudchemicaliën en ultrasone reiniging."
            ),
            BuyingGuideArticle(
                pierre = "Smaragd",
                accroche = "Groen zoals geen andere steen: smaragd fascineert al sinds het Oude Egypte en blijft een van de meest gewilde stenen onder verzamelaars.",
                origineCouleur = "Colombia (Muzo, Chivor): intens, licht blauwig groen, de historische referentie. Zambia (Kagem): diep groen, vaak met een bovengemiddelde zuiverheid. Brazilië: ruime, toegankelijkere productie met lichter groen. Gezocht wordt een verzadigd, egaal groen met een vleugje blauw.",
                puretTraitements = "Insluitsels (zijn «tuin») horen bijna bij zijn identiteit. Vrijwel alle stenen worden behandeld met kleurloze olie of hars: vermelding is verplicht, de mate van impregnatie moet op het certificaat staan (GIA, Gübelin, GRS).",
                entretien = "Hardheid 7,5 tot 8, maar de taaiheid wordt beperkt door de insluitsels. Verwijder de ring vóór fysieke activiteit, vermijd chemicaliën en gebruik nooit een ultrasoonreiniger, die de behandelingsolie naar buiten kan laten treden."
            ),
            BuyingGuideArticle(
                pierre = "Diamant",
                accroche = "De ultieme referentie in de juwelierskunst, beoordeeld op de vier klassieke criteria: kleur, zuiverheid, slijpvorm en karaat.",
                origineCouleur = "De geografische herkomst weegt nauwelijks mee in de waarde (afgezien van ethische traceerbaarheid via het Kimberley-proces): de kleur telt, van het gewilde kleurloze wit (D) tot zeldzame fancy kleuren (blauw, roze, groen).",
                puretTraitements = "De zuiverheid wordt beoordeeld met een 10x loep, van «zonder insluitsels» (FL) tot ingesloten (I). HPHT-behandeling en laserboren moeten worden vermeld en op het certificaat gegraveerd: het overgrote deel van de markt blijft onbehandeld.",
                entretien = "Hardheid 10, ongevoelig voor dagelijkse slijtage maar kan afsplinteren bij een harde klap. Ultrasone reiniging is veilig, behalve bij een gebarsten of opgevulde steen."
            ),
            BuyingGuideArticle(
                pierre = "Alexandriet",
                accroche = "De «kameleon»-steen: groen bij daglicht, rood of paars onder gloeilicht.",
                origineCouleur = "De Russische Oeral: de historische vindplaats, tegenwoordig vrijwel uitgeput. Sri Lanka en Brazilië: de belangrijkste huidige bronnen. Oost-Afrika (Tanzania): een recentere productie. De sterkte van de kleurverandering weegt zwaarder dan de tint zelf.",
                puretTraitements = "Zelden behandeld. De steen bevat vrijwel altijd met het blote oog zichtbare insluitsels: hoge zuiverheid is uitzonderlijk en zeer gewild. De slijpvorm wordt vaak afgestemd om het kleurveranderingseffect te maximaliseren.",
                entretien = "Hardheid 8,5, zeer bestendig. Geen bijzondere verzorging nodig, buiten de gebruikelijke voorzorgen voor elke sieraadsteen."
            ),
            BuyingGuideArticle(
                pierre = "Keizertopaas",
                accroche = "Niet te verwarren met blauwe topaas, die altijd bestraald is: natuurlijke keizertopaas is de meest gewilde variëteit.",
                origineCouleur = "Ouro Preto in Brazilië: de historische en vrijwel exclusieve bron van edelsteenkwaliteit. Oranje tot zalmroze tinten; het zuivere roze, «roze keizertopaas» genoemd, is het zeldzaamst en waardevolst.",
                puretTraitements = "Meestal onbehandeld, in tegenstelling tot blauwe topaas: een belangrijk waardecriterium om op het certificaat te controleren. Van nature hoge zuiverheid.",
                entretien = "Hardheid 8, maar een perfecte splijting in één richting maakt hem breekbaar bij stoten. Vermijd plotselinge temperatuurschommelingen en gebruik geen ultrasoonreiniger."
            ),
            BuyingGuideArticle(
                pierre = "Demantoïdgranaat",
                accroche = "De meest kostbare van de granaten: een schitterend groen, gedragen door een dispersie («vuur») die die van diamant kan overtreffen.",
                origineCouleur = "De Russische Oeral: de historische bron, herkenbaar aan vezelige «paardenstaart»-insluitsels. Namibië: de belangrijkste moderne productie. Gezocht wordt een levendig groen, idealiter met weinig geel.",
                puretTraitements = "Deze soort wordt zeer zelden behandeld: natuurlijke zuiverheid is bijna de regel. Russische «paardenstaart»-insluitsels worden zelfs gewaardeerd als herkomstbewijs.",
                entretien = "Hardheid 6,5 tot 7: zachter dan de meeste edelstenen, te beschermen tegen stoten en krassen. Vermijd ultrasone reiniging bij aanzienlijke insluitsels."
            ),
            BuyingGuideArticle(
                pierre = "Tanzaniet",
                accroche = "Ontdekt in 1967 aan de voet van de Kilimanjaro, een edelsteen uit 's werelds enige vindplaats.",
                origineCouleur = "Uitsluitend Merelani, in Tanzania: een unieke, niet-hernieuwbare bron, een sterk zeldzaamheidsargument. Intens blauwpaars met uitgesproken pleochroïsme (blauw, paars, bordeauxrood al naar gelang de hoek); de slijpvorm bepaalt welke kleur domineert.",
                puretTraitements = "Verhitten om de blauwpaarse kleur te ontwikkelen is vrijwel de norm, stabiel en door de markt aanvaard. Zuiverheid over het algemeen goed met het blote oog.",
                entretien = "Slechts hardheid 6 tot 7, met een duidelijke splijting: voorbehouden aan sieraden die weinig aan stoten blootstaan. Geen ultrasone of stoomreiniging gebruiken."
            ),
            BuyingGuideArticle(
                pierre = "Amethist",
                accroche = "De meest kostbare kwartsvariëteit, de geboortesteen van de maand februari.",
                origineCouleur = "Brazilië en Uruguay: geodes, licht tot middelpaars. Zambia: diep paars met roodachtige weerschijn, zeer gewild. Siberië: historische, tegenwoordig zeldzame bron. Gezocht wordt een intens, egaal paars zonder bruinachtige zones.",
                puretTraitements = "Zelden behandeld; soms verhit om de tint lichter te maken of naar citrien te doen overgaan. Zuiverheid over het algemeen uitstekend, wat er een toegankelijke halfedelsteen van maakt.",
                entretien = "Hardheid 7, stevig en gemakkelijk te onderhouden. Vermijd langdurige blootstelling aan zon, die de kleur na verloop van tijd kan doen vervagen."
            ),
            BuyingGuideArticle(
                pierre = "Spinel",
                accroche = "Lange tijd verward met robijn — de legendarische «Black Prince's Ruby» is in werkelijkheid een spinel —, wordt spinel tegenwoordig om zichzelf gewaardeerd.",
                origineCouleur = "Myanmar (Mogok): levendige rode en roze tinten. Sri Lanka en Tanzania: breed kleurenpalet, waaronder de oranje «vlam»-spinel. Rood, roze, kobaltblauw (zeldzaam en zeer gewild) en «gunmetal»-grijs.",
                puretTraitements = "Deze soort wordt vrijwel nooit behandeld, afgezien van af en toe verhitten: een echt voordeel voor kopers die op zoek zijn naar authenticiteit. Zuiverheid over het algemeen goed met het blote oog.",
                entretien = "Hardheid 8, bestendig. Verzorging zoals bij saffier, zonder bijzondere voorzorgen."
            ),
            BuyingGuideArticle(
                pierre = "Toermalijn",
                accroche = "De steen met duizend kleuren, van het elektrische «paraíba»-groenblauw tot de tweekleurige roze-groene «watermeloen»-toermalijn.",
                origineCouleur = "Brazilië (Paraíba): legendarisch, uiterst zeldzaam koperhoudend neon-groenblauw. Mozambique en Nigeria: toegankelijkere «paraíba-achtige» tinten. Afghanistan en Brazilië: rood-roze rubelliet. Madagaskar: veelkleurig palet. De paraíba-variëteit voert de waardering aan, gevolgd door intense rubelliet.",
                puretTraitements = "Verhitten om bepaalde tinten te versterken is gebruikelijk en aanvaard. De zuiverheid varieert per variëteit; fijne, draadvormige insluitsels komen vaak voor en worden getolereerd indien discreet.",
                entretien = "Hardheid 7 tot 7,5, over het algemeen stevig. Een pyro-elektrische steen die stof aantrekt door statische elektriciteit: een zachte, regelmatige reiniging wordt aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Saffier",
                accroche = "Het blauwe corundum bij uitstek, maar ook roze, geel of «padparadscha» — na diamant de hardste edelsteen.",
                origineCouleur = "Kasjmir: legendarisch fluweelachtig blauw, een bijna uitgeputte, uiterst zeldzame vindplaats. Myanmar (Mogok): intens «royal» blauw. Sri Lanka: lichter blauw, grote transparantie. Madagaskar: de belangrijkste moderne productie. Gezocht wordt een diep fluweelachtig blauw met sterke verzadiging, zonder te veel zwart.",
                puretTraitements = "Verhitten is vrijwel de norm en algemeen aanvaard, verbetert kleur en zuiverheid. Diffusie (klassiek met titaan, of problematischer met beryllium) moet altijd apart op het certificaat worden vermeld. Meestal wordt hoge zuiverheid gezocht.",
                entretien = "Hardheid 9, net na diamant: zeer bestand tegen dagelijks gebruik. Ultrasone reiniging is veilig, behalve bij een gediffundeerde of opgevulde steen."
            )
        )
    )

    private val zh = BuyingGuidesPage(
        intro = "每一种贵重或半宝石宝石都有各自的挑选标准:理想的产地与颜色、预期的净度以及市场上常见的处理方式,以及与其硬度相关的保养注意事项。以下是 Gems of Rod 在为客户甄选每一种宝石之前,逐一审视的要点。",
        articles = listOf(
            BuyingGuideArticle(
                pierre = "红宝石",
                accroche = "极致的红色:在最高品质等级下,红宝石的每克拉价值甚至可能超过钻石。",
                origineCouleur = "缅甸(抹谷):「鸽血红」色泽浓郁,荧光强烈,是历史悠久的标杆产地。莫桑比克:如今最主要的现代产地,色泽美丽且价格更亲民。泰国/柬埔寨:色调更深,偏棕。理想颜色为纯正到略带紫色的红,饱和度极高但不发黑。",
                puretTraitements = "加热处理常见且被市场广泛接受。铅玻璃充填裂隙必须如实披露:价值明显偏低,且更易破损。细微的金红石「丝状」包裹体可以接受,有时甚至被视为特征。",
                entretien = "硬度9,日常佩戴非常耐用。经铅玻璃充填的红宝石仍然脆弱:应避免接触家用化学品及超声波清洗。"
            ),
            BuyingGuideArticle(
                pierre = "祖母绿",
                accroche = "绿得独一无二,祖母绿自古埃及以来便令人着迷,至今仍是收藏家最青睐的宝石之一。",
                origineCouleur = "哥伦比亚(穆佐、奇沃尔):浓郁略带蓝色调的绿色,是历史标杆产地。赞比亚(卡格姆):绿色更深,净度往往更高。巴西:产量丰富、价格更亲民,绿色较浅。理想颜色为饱和均匀、略带蓝色调的绿。",
                puretTraitements = "内含物(即其「花园」)几乎是它身份的一部分。几乎所有祖母绿都经无色油或树脂处理:必须披露,浸渍程度应在证书(GIA、Gübelin、GRS)中注明。",
                entretien = "硬度7.5至8,但因内含物而韧性有限。剧烈运动前应取下戒指,避免接触化学品,切勿使用超声波清洗机,以免处理用油渗出。"
            ),
            BuyingGuideArticle(
                pierre = "钻石",
                accroche = "珠宝界的终极标准,依据其历史悠久的四项标准评估:颜色、净度、切工与克拉重量。",
                origineCouleur = "产地对价值影响不大(道德可追溯性——金伯利进程除外):真正重要的是颜色,从最受追捧的无色白(D色)到罕见的彩色钻石(蓝、粉、绿)。",
                puretTraitements = "净度以10倍放大镜评级,从「无瑕」(FL)到「有瑕」(I)不等。HPHT高温高压处理及激光钻孔必须披露并镭射刻于证书上:市场上绝大多数钻石仍为未经处理。",
                entretien = "硬度10,日常佩戴不易磨损,但受到重击仍可能崩裂。超声波清洗通常安全,除非宝石有裂纹或经过充填处理。"
            ),
            BuyingGuideArticle(
                pierre = "亚历山大变石",
                accroche = "「变色龙」宝石:日光下呈绿色,白炽灯下则变为红色或紫色。",
                origineCouleur = "俄罗斯乌拉尔:历史产地,如今几近枯竭。斯里兰卡与巴西:当今主要产地。东非(坦桑尼亚):较新兴的产地。变色效果的强烈程度比色调本身更重要。",
                puretTraitements = "极少经过处理。肉眼几乎总能看到内含物:高净度极为罕见,备受珍视。切工常经调整,以最大化变色效应。",
                entretien = "硬度8.5,非常耐用。无需特殊保养,遵循一般宝石首饰的常规注意事项即可。"
            ),
            BuyingGuideArticle(
                pierre = "帝王托帕石",
                accroche = "切勿与经辐照处理的蓝色托帕石混淆:天然帝王托帕石才是最受追捧的品种。",
                origineCouleur = "巴西欧鲁普雷图:历史悠久且几乎是唯一的宝石级产地。色调从橙色到鲑鱼粉不等;纯粉色(称为「粉色帝王托帕石」)最为稀有珍贵。",
                puretTraitements = "与蓝色托帕石不同,通常未经处理:这是需在证书上核实的重要价值标准。天然净度高。",
                entretien = "硬度8,但单方向的完美解理使其易受撞击损坏。应避免剧烈温差变化,切勿使用超声波清洗。"
            ),
            BuyingGuideArticle(
                pierre = "翠榴石",
                accroche = "石榴石家族中最珍贵的品种:明亮的绿色搭配可能胜过钻石的色散(「火彩」)。",
                origineCouleur = "俄罗斯乌拉尔:历史产地,以纤维状「马尾」内含物为特征。纳米比亚:当今主要产地。理想颜色为鲜艳的绿色,黄色调越少越好。",
                puretTraitements = "极少经过处理:天然净度几乎是常态。俄罗斯产「马尾」内含物甚至被视为产地证明而受到青睐。",
                entretien = "硬度6.5至7,比大多数宝石更软,需防止碰撞与刮擦。若内含物明显,应避免超声波清洗。"
            ),
            BuyingGuideArticle(
                pierre = "坦桑石",
                accroche = "1967年发现于乞力马扎罗山脚下,是全球唯一产地的宝石。",
                origineCouleur = "仅产自坦桑尼亚梅雷拉尼:独一无二且不可再生的产地,稀有性极高。蓝紫色浓郁,多色性明显(随角度呈现蓝、紫、酒红色);切工决定主导色调。",
                puretTraitements = "几乎都经过加热处理以呈现蓝紫色,该处理稳定且被市场接受。净度通常肉眼可见良好。",
                entretien = "硬度仅6至7,解理明显:适合较少受到碰撞的首饰。切勿使用超声波或蒸汽清洗。"
            ),
            BuyingGuideArticle(
                pierre = "紫水晶",
                accroche = "石英家族中最珍贵的品种,二月的诞生石。",
                origineCouleur = "巴西与乌拉圭:晶洞产出,颜色浅紫至中紫。赞比亚:深紫色带红色反光,备受追捧。西伯利亚:历史产地,如今稀少。理想颜色为浓郁均匀的紫色,无棕色区域。",
                puretTraitements = "极少经过处理;有时经加热以淡化色调或转变为黄水晶。净度通常极佳,使其成为价格亲民的半宝石。",
                entretien = "硬度7,坚固且易于保养。应避免长时间日晒,以免颜色随时间褪色。"
            ),
            BuyingGuideArticle(
                pierre = "尖晶石",
                accroche = "长期以来常被误认为红宝石——传说中的「黑太子红宝石」实际上正是一颗尖晶石——如今尖晶石因其自身价值而备受欣赏。",
                origineCouleur = "缅甸(抹谷):鲜艳的红色与粉色。斯里兰卡与坦桑尼亚:色彩丰富,包括橙色的「火焰」尖晶石。红、粉、钴蓝(稀有且备受追捧)及「枪灰色」。",
                puretTraitements = "该品种几乎从不经过处理,偶尔加热除外:对追求天然的买家而言是真正的优势。净度通常肉眼可见良好。",
                entretien = "硬度8,耐用。保养方式与蓝宝石相同,无需特殊防护。"
            ),
            BuyingGuideArticle(
                pierre = "碧玺",
                accroche = "拥有千种颜色的宝石,从电光般的「帕拉伊巴」蓝绿色到双色的粉绿「西瓜」碧玺。",
                origineCouleur = "巴西(帕拉伊巴):传奇且极为稀有的含铜霓虹蓝绿色。莫桑比克与尼日利亚:价格更亲民的「类帕拉伊巴」色调。阿富汗与巴西:红粉色的红碧玺。马达加斯加:色彩缤纷。帕拉伊巴品种价值最高,其次是浓艳的红碧玺。",
                puretTraitements = "加热以增强某些色调的处理常见且被接受。净度因品种而异;细针状内含物常见,若不明显则可接受。",
                entretien = "硬度7至7.5,整体较为坚固。碧玺具有热电性,会因静电吸附灰尘:建议定期轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "蓝宝石",
                accroche = "典型的蓝色刚玉,但也有粉色、黄色或「帕帕拉恰」色——硬度仅次于钻石。",
                origineCouleur = "克什米尔:传奇的天鹅绒蓝,产地几近枯竭,极为稀有。缅甸(抹谷):浓郁的「皇家蓝」。斯里兰卡:蓝色较浅,透明度极高。马达加斯加:当今主要产地。理想颜色为浓郁的天鹅绒蓝,饱和度高且不过分发黑。",
                puretTraitements = "加热处理几乎是常态且被广泛接受,可改善颜色与净度。扩散处理(传统的钛扩散,或问题较多的铍扩散)必须在证书上明确区分。通常追求高净度。",
                entretien = "硬度9,仅次于钻石:日常佩戴非常耐用。超声波清洗通常安全,除非宝石经过扩散或充填处理。"
            )
        )
    )

    private val byLanguage: Map<String, BuyingGuidesPage> = mapOf(
        AppLanguage.EN.code to en,
        AppLanguage.ES.code to es,
        AppLanguage.IT.code to it,
        AppLanguage.DE.code to de,
        AppLanguage.PT.code to pt,
        AppLanguage.ZH.code to zh,
        AppLanguage.RU.code to ru,
        AppLanguage.NL.code to nl
    )

    fun page(languageCode: String): BuyingGuidesPage = byLanguage[languageCode] ?: fr
}
