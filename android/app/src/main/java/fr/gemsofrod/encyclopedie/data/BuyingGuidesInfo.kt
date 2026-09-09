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
                pierre = "Saphir jaune",
                accroche = "Le corindon dans toutes les teintes hors rouge trouve dans le jaune l'une de ses variétés les plus lumineuses et abordables.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzanie (Tunduru). Jaune vif et lumineux, homogène, sans reflet verdâtre.",
                puretTraitements = "Chauffage courant pour intensifier et uniformiser la couleur, traitement accepté et stable. Clarté généralement bonne à l'œil nu.",
                entretien = "Dureté 9, très résistant au quotidien. Ultrasons sans risque pour une pierre non diffusée."
            ),
            BuyingGuideArticle(
                pierre = "Saphir vert",
                accroche = "Un vert discret et souvent dichroïque, la couleur la plus abordable du corindon.",
                origineCouleur = "Australie (Nouvelle-Galles du Sud), Thaïlande (Kanchanaburi), Nigeria (Mambilla). Vert franc, sans trop de gris ; le dichroïsme vert/jaune-vert selon l'angle est caractéristique de l'espèce.",
                puretTraitements = "Chauffage courant et accepté pour homogénéiser la teinte. Clarté généralement bonne.",
                entretien = "Dureté 9, très résistant. Ultrasons sans risque pour une pierre non diffusée."
            ),
            BuyingGuideArticle(
                pierre = "Saphir bleu",
                accroche = "Le bleu de référence de la haute joaillerie, juste derrière le diamant en dureté.",
                origineCouleur = "Cachemire : bleu velouté légendaire, gisement quasiment épuisé, rarissime. Birmanie (Mogok) : bleu « royal » intense. Sri Lanka : bleu plus clair, grande transparence. Madagascar : principale production moderne. Bleu velouté profond, saturation forte sans excès de noir.",
                puretTraitements = "Chauffage quasi systématique et largement accepté. La diffusion (titane classique, ou béryllium, plus problématique) doit être distinguée sur le certificat. Des inclusions de rutile en soie peuvent produire un astérisme (saphir étoilé).",
                entretien = "Dureté 9, très résistant au quotidien. Ultrasons sans risque, sauf sur une pierre diffusée ou remplie."
            ),
            BuyingGuideArticle(
                pierre = "Saphir d'Auvergne",
                accroche = "Un corindon français à la teinte bleu-violet profonde, extrait des terrains volcaniques d'Auvergne.",
                origineCouleur = "France (Espaly-Saint-Marcel, Haute-Loire), seule source. Bleu-violet très soutenu, typique des saphirs d'origine basaltique ; cristaux généralement petits, rarement plus d'un carat taillé.",
                puretTraitements = "Peu ou pas chauffé, la rareté et l'origine locale primant sur l'intensification de la couleur. Petite production, à négocier pièce par pièce.",
                entretien = "Dureté 9, très résistant. Ultrasons sans risque."
            ),
            BuyingGuideArticle(
                pierre = "Saphir violet",
                accroche = "Un violet profond, à la frontière entre le saphir bleu et le rubis.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzanie (Tunduru). Violet soutenu et homogène ; peut présenter un léger changement de couleur entre lumière du jour et lumière incandescente.",
                puretTraitements = "Chauffage courant et accepté pour stabiliser la teinte. Clarté généralement bonne à l'œil nu.",
                entretien = "Dureté 9, très résistant. Ultrasons sans risque pour une pierre non diffusée."
            ),
            BuyingGuideArticle(
                pierre = "Saphir rose",
                accroche = "À la frontière entre rubis et saphir, dont la limite officielle reste débattue selon les laboratoires.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzanie (Tunduru). Rose vif et soutenu ; au-delà d'un certain seuil de saturation, la pierre est reclassée rubis par certains laboratoires.",
                puretTraitements = "Chauffage courant et accepté. Vérifier la classification (saphir rose ou rubis) sur le certificat, la valeur pouvant varier fortement.",
                entretien = "Dureté 9, très résistant. Ultrasons sans risque pour une pierre non diffusée."
            ),
            BuyingGuideArticle(
                pierre = "Saphir blanc",
                accroche = "Le corindon sans trace colorante, alternative discrète et abordable au diamant.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Australie (Nouvelle-Galles du Sud). Incolore et limpide ; dispersion inférieure à celle du diamant, mais dureté quasi égale.",
                puretTraitements = "Rarement traité, sa transparence naturelle étant déjà son principal atout. Clarté élevée recherchée.",
                entretien = "Dureté 9, excellente résistance à l'usure quotidienne. Ultrasons sans risque."
            ),
            BuyingGuideArticle(
                pierre = "Saphir étoilé",
                accroche = "Une étoile lumineuse à six branches, révélée par une taille en cabochon, souvent dans un saphir bleu ou noir.",
                origineCouleur = "Sri Lanka (Ratnapura), Birmanie (Mogok), Thaïlande (Kanchanaburi, variété noire). L'astérisme le plus recherché présente six branches parfaitement droites, centrées sur le dôme du cabochon.",
                puretTraitements = "Rarement chauffé, un traitement thermique risquant d'altérer les inclusions de rutile responsables de l'astérisme. La netteté et le centrage de l'étoile priment sur la transparence.",
                entretien = "Dureté 9, très résistant. Nettoyage doux recommandé pour préserver le poli du dôme."
            ),
            BuyingGuideArticle(
                pierre = "Saphir teal",
                accroche = "Un corindon bicolore où bleu et jaune se combinent en une teinte bleu-vert singulière, très prisée des connaisseurs.",
                origineCouleur = "États-Unis (Montana, Rock Creek), Australie (Nouvelle-Galles du Sud), Madagascar (Ilakaka). La bicoloration bleu-jaune, visible en zones distinctes à l'état brut, se fond en un bleu-vert homogène une fois la pierre taillée et orientée.",
                puretTraitements = "Souvent non chauffé, la nuance naturelle étant elle-même recherchée par les collectionneurs. Le talent du tailleur, qui oriente la pierre pour équilibrer les deux couleurs, est déterminant.",
                entretien = "Dureté 9, très résistant. Ultrasons sans risque pour une pierre non diffusée."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline rubellite",
                accroche = "Un rouge intense dû au manganèse, la variété la plus proche du rubis parmi les tourmalines.",
                origineCouleur = "Brésil (mine de Cruzeiro, Minas Gerais), Nigeria (Oyo), Mozambique (Alto Ligonha), Afghanistan (vallée de Paprok). Rouge à rouge-rose intense et homogène ; pléochroïsme marqué, cristaux prismatiques striés caractéristiques.",
                puretTraitements = "Chauffage occasionnel pour atténuer les tons bruns, traitement accepté. Clarté variable ; inclusions filiformes fréquentes et tolérées si discrètes.",
                entretien = "Dureté 7 à 7,5, bonne robustesse générale. Pierre pyroélectrique attirant la poussière par électricité statique : nettoyage doux recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline jaune",
                accroche = "Un jaune canari lumineux, naturel sans traitement — la plus rare des couleurs de tourmaline.",
                origineCouleur = "Malawi (Zomba), Brésil (Minas Gerais), Nigeria (Oyo). Jaune vif et lumineux, sans intervention nécessaire contrairement à de nombreuses teintes commerciales de la tourmaline.",
                puretTraitements = "Généralement non traitée, sa couleur naturelle étant déjà recherchée. Clarté souvent bonne.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux et régulier recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline chrome",
                accroche = "Un vert profond et saturé dû au chrome et au vanadium, distinct du vert « classique » de la tourmaline.",
                origineCouleur = "Kenya (Taita-Taveta), Tanzanie (Umba). Vert intense proche de l'émeraude ; dichroïsme marqué, l'orientation de la table étant déterminante pour révéler la teinte la plus saturée.",
                puretTraitements = "Rarement traitée, la couleur due au chrome et au vanadium étant naturellement intense. Clarté généralement bonne.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline verte (Verdelite)",
                accroche = "Un vert coloré par le fer ou le chrome, la couleur la plus classique et répandue de la tourmaline.",
                origineCouleur = "Brésil (Minas Gerais), Mozambique (Alto Ligonha), Nigeria (Oyo). Vert franc à vert sombre ; forte biréfringence, les tailleurs orientant la pierre pour optimiser la couleur.",
                puretTraitements = "Chauffage occasionnel accepté pour éclaircir les teintes trop sombres. Clarté variable selon l'origine.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux et régulier recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline bleue (indigolite)",
                accroche = "Un bleu profond, distinct du bleu-vert cuivré de la Paraíba, sans intervention du cuivre.",
                origineCouleur = "Brésil (Minas Gerais), Namibie (Erongo), Afghanistan (Nouristan). Bleu sombre à bleu-gris, généralement plus foncé que la Paraíba.",
                puretTraitements = "Chauffage occasionnel pour éclaircir un ton trop sombre, traitement accepté. Clarté variable.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline rose",
                accroche = "Une teinte tendre due au manganèse, à l'origine des tourmalines bicolores les plus recherchées.",
                origineCouleur = "Brésil (mine de Cruzeiro, Minas Gerais), Afghanistan (Paprok), Mozambique (Alto Ligonha). Rose clair à rose soutenu ; cristaux souvent zonés, à l'origine des variétés bicolores comme la pastèque.",
                puretTraitements = "Rarement traitée. Clarté généralement bonne à l'œil nu.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux recommandé."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline noire (Schorl)",
                accroche = "La variété de tourmaline la plus commune, appréciée en bijouterie sobre comme en lithothérapie.",
                origineCouleur = "Brésil (Minas Gerais), Namibie (Erongo), Madagascar (Antsirabe). Noir opaque à sub-transparent ; cristaux prismatiques souvent striés, parfois de grande taille.",
                puretTraitements = "Jamais traitée, sa couleur et son abondance ne justifiant aucune intervention. Prix très accessible.",
                entretien = "Dureté 7 à 7,5, robuste. Entretien simple, sans précaution particulière."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline pastèque",
                accroche = "Un cœur rose cerné de vert, comme une tranche de fruit — l'une des tourmalines bicolores les plus spectaculaires.",
                origineCouleur = "Brésil (Minas Gerais), États-Unis (mine Dunton, Maine). Zonation nette rose au centre, verte en périphérie ; taillée le plus souvent en tranches transversales pour révéler l'effet « pastèque ».",
                puretTraitements = "Rarement traitée, la zonation naturelle étant l'attrait principal de la pierre. Clarté variable selon la zone.",
                entretien = "Dureté 7 à 7,5. Nettoyage doux recommandé, en particulier pour les tranches fines."
            ),
            BuyingGuideArticle(
                pierre = "Tourmaline Paraïba",
                accroche = "Un bleu-vert « néon » électrique, unique dans le règne minéral — la tourmaline la plus recherchée et la plus onéreuse.",
                origineCouleur = "São José da Batalha (Paraíba, Brésil), source historique quasi épuisée. Mozambique (Mavuco) et Nigeria (Edeko) : sources modernes plus accessibles. Couleur « néon » due au cuivre, jamais observée dans les autres tourmalines.",
                puretTraitements = "Un certificat précisant l'origine (Brésil vs Afrique) est essentiel, l'écart de valeur étant considérable. Clarté et intensité de la couleur cuivrée priment sur tout autre critère.",
                entretien = "Dureté 7 à 7,5. Pierre pyroélectrique : nettoyage doux recommandé, à protéger comme toute pierre de très haute valeur."
            ),
            BuyingGuideArticle(
                pierre = "Grenat pyrope",
                accroche = "Le grenat rouge sang classique, sans dilution rhodolite — la variété la plus pure de la famille pyrope.",
                origineCouleur = "République tchèque (Bohême, région de Podsedice), Afrique du Sud (mine de Kao, kimberlites), Tanzanie (Umba). Rouge sang profond et homogène, sans nuance violacée notable.",
                puretTraitements = "Jamais traité, comme l'ensemble de la famille des grenats. Contrairement à la rhodolite, le pyrope pur ne contient pas de fer en proportion significative.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier au-delà des précautions usuelles pour toute pierre de joaillerie."
            ),
            BuyingGuideArticle(
                pierre = "Grenat almandin / pyrope",
                accroche = "Un grenat rouge profond, jamais traité — le plus courant et le plus abordable de la famille.",
                origineCouleur = "Inde (Rajmahal Hills, Jharkhand), Sri Lanka (Ratnapura), République tchèque (Podsedice, Bohême), Tanzanie (vallée de l'Umba). Rouge sombre à rouge-brun, parfois légèrement violacé.",
                puretTraitements = "Jamais traité thermiquement ou chimiquement. Aucun clivage, forte réfraction et bon éclat.",
                entretien = "Dureté 7 à 7,5, bonne robustesse générale. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat rhodolite",
                accroche = "Un rouge-violacé « framboise » à l'excellente transparence, parmi les grenats les plus élégants.",
                origineCouleur = "Tanzanie (vallée de l'Umba), Sri Lanka (Ratnapura), Inde (Orissa). Rouge-violacé lumineux et homogène.",
                puretTraitements = "Jamais traitée. Excellente transparence naturelle, rarement incluse à l'œil nu.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat étoilé",
                accroche = "Un almandin à quatre ou six branches lumineuses, une rareté trouvée quasi uniquement dans l'Idaho.",
                origineCouleur = "États-Unis (Idaho, seul gisement mondial d'astérisme à quatre branches), Inde (Odisha). Rouge sombre ; l'astérisme à quatre branches est unique au monde, celui à six branches, plus rare encore, provient des mêmes gisements.",
                puretTraitements = "Jamais traité. La netteté et le centrage de l'étoile priment sur la transparence du fond.",
                entretien = "Dureté 7 à 7,5. Nettoyage doux recommandé pour préserver le poli du dôme."
            ),
            BuyingGuideArticle(
                pierre = "Grenat malaya",
                accroche = "Un rose-orangé chaleureux né du mélange de deux grenats, sans aucun traitement.",
                origineCouleur = "Tanzanie (Umba), Kenya (Taita-Taveta), Madagascar (Anjanabonoina). Rose-orangé à orangé chaud, teinte unique parmi les grenats.",
                puretTraitements = "Aucun traitement n'est nécessaire ni pratiqué : couleur 100 % naturelle, l'un des grands arguments de vente de cette variété.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat spessartite",
                accroche = "Un orange « mandarine » éclatant, parmi les couleurs les plus lumineuses du règne minéral.",
                origineCouleur = "Namibie (région du Kunene), Nigeria (État du Nasarawa), Madagascar (Fianarantsoa). Orange vif et saturé, parfois tirant vers le rouge-orangé.",
                puretTraitements = "Couleur naturelle, jamais traitée thermiquement. Clarté généralement bonne à excellente.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat grossulaire",
                accroche = "Le grenat vert à jaune-vert, cousin peu coloré de la tsavorite et de l'hessonite — la famille la plus diversifiée en couleurs.",
                origineCouleur = "Mali (Sandaré), Kenya (Voi), Canada (Québec, Jeffrey Mine). Du incolore au vert profond, en passant par le jaune-vert et le brun-orangé de l'hessonite.",
                puretTraitements = "Jamais traité. Clarté variable selon la teinte et le gisement.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat rhodolite violacé",
                accroche = "Une teinte pourpre changeant selon l'éclairage, variante violette de la rhodolite classique.",
                origineCouleur = "Tanzanie (vallée de l'Umba), Mozambique (Cuamba). Pourpre à violet soutenu, la nuance dominante variant selon la source lumineuse.",
                puretTraitements = "Couleur stable, jamais traitée. Clarté généralement bonne.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Grenat à changement de couleur",
                accroche = "Vert bleuté de jour, rouge-violet le soir — un rival méconnu de l'alexandrite, à un prix bien plus accessible.",
                origineCouleur = "Tanzanie (Umba, Tunduru), Madagascar (Bekily). Le vanadium, plutôt que le chrome, est responsable du changement de couleur dans ce grenat, contrairement à l'alexandrite.",
                puretTraitements = "Jamais traité. La force du changement de couleur prime sur la teinte elle-même, comme pour l'alexandrite.",
                entretien = "Dureté 7 à 7,5. Aucun soin particulier."
            ),
            BuyingGuideArticle(
                pierre = "Topaze bleue",
                accroche = "Le bleu profond obtenu par irradiation puis chauffe d'une topaze incolore — la couleur la plus commercialisée de l'espèce.",
                origineCouleur = "Brésil (Minas Gerais) et Nigeria fournissent la matière première incolore, irradiée puis chauffée pour développer un bleu allant du ciel au « London Blue » profond.",
                puretTraitements = "Couleur quasi systématiquement obtenue par irradiation puis traitement thermique, stable et permanente — divulgation obligatoire, largement acceptée par le marché du fait de son prix accessible.",
                entretien = "Dureté 8, mais un clivage parfait dans un sens la rend fragile aux chocs. Éviter les variations thermiques brutales et proscrire les ultrasons."
            ),
            BuyingGuideArticle(
                pierre = "Topaze rose",
                accroche = "Le rose naturel le plus rare de la topaze, historiquement extrait au Pakistan.",
                origineCouleur = "Pakistan (Katlang, Mardan), Russie (Oural, historique). Rose délicat à soutenu ; le rose naturel non traité est rare, à distinguer de la topaze rose obtenue par chauffage de pierres brunes.",
                puretTraitements = "Vérifier sur le certificat si la teinte est naturelle ou obtenue par traitement thermique de matière brune, l'écart de valeur étant important. Clarté naturellement élevée.",
                entretien = "Dureté 8, clivage parfait dans un sens : éviter les chocs et les variations thermiques brutales, proscrire les ultrasons."
            ),
            BuyingGuideArticle(
                pierre = "Topaze blanche",
                accroche = "La topaze dans sa forme la plus pure et la plus courante, incolore et limpide — une alternative économique au diamant.",
                origineCouleur = "Brésil (Minas Gerais, Ouro Preto), Nigeria (Jos Plateau), Pakistan (Katlang). Incolore et parfaitement limpide ; c'est aussi la matière première la plus abondante pour la production de topaze bleue traitée.",
                puretTraitements = "Généralement non traitée elle-même. Clarté très élevée recherchée, sa transparence étant son principal atout.",
                entretien = "Dureté 8, clivage parfait dans un sens : éviter les chocs et proscrire les ultrasons."
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
                pierre = "Yellow sapphire",
                accroche = "Corundum comes in every hue but red, and yellow gives it one of its brightest and most affordable varieties.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). A vivid, luminous yellow, even in tone and free of any greenish cast, is most prized.",
                puretTraitements = "Heat treatment is common to intensify and even out the colour, a stable and accepted treatment. Clarity is generally good to the naked eye.",
                entretien = "Hardness 9, very resistant to daily wear. Ultrasonic cleaning is safe on a stone that has not been diffused."
            ),
            BuyingGuideArticle(
                pierre = "Green sapphire",
                accroche = "A quiet, often dichroic green — the most affordable colour in the corundum family.",
                origineCouleur = "Australia (New South Wales), Thailand (Kanchanaburi), Nigeria (Mambilla). A true green without too much grey; dichroism between green and yellow-green depending on the angle is characteristic of the species.",
                puretTraitements = "Heat treatment to even out the hue is common and accepted. Clarity is generally good.",
                entretien = "Hardness 9, very resistant. Ultrasonic cleaning is safe on a stone that has not been diffused."
            ),
            BuyingGuideArticle(
                pierre = "Blue sapphire",
                accroche = "The benchmark blue of fine jewellery, second only to diamond in hardness.",
                origineCouleur = "Kashmir: legendary velvety blue, an almost exhausted, extremely rare deposit. Myanmar (Mogok): intense \"royal\" blue. Sri Lanka: lighter blue with great transparency. Madagascar: the leading modern production. A deep velvety blue with strong saturation, without excess black, is most prized.",
                puretTraitements = "Heat treatment is near-universal and widely accepted. Diffusion (classic titanium, or the more problematic beryllium) must be distinguished on the certificate. Rutile silk inclusions can produce asterism (star sapphire).",
                entretien = "Hardness 9, very resistant to daily wear. Ultrasonic cleaning is safe, except on a diffused or filled stone."
            ),
            BuyingGuideArticle(
                pierre = "Auvergne sapphire",
                accroche = "A French corundum in a deep blue-violet hue, mined from the volcanic terrain of Auvergne.",
                origineCouleur = "France (Espaly-Saint-Marcel, Haute-Loire), the only source. A very deep blue-violet typical of basalt-derived sapphires; crystals are generally small, rarely yielding more than a carat once cut.",
                puretTraitements = "Little or no heat treatment, as rarity and local origin matter more than intensifying the colour. Production is small, negotiated stone by stone.",
                entretien = "Hardness 9, very resistant. Ultrasonic cleaning is safe."
            ),
            BuyingGuideArticle(
                pierre = "Violet sapphire",
                accroche = "A deep violet, standing at the frontier between blue sapphire and ruby.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). A deep, even violet; may show a slight colour change between daylight and incandescent light.",
                puretTraitements = "Heat treatment to stabilise the hue is common and accepted. Clarity is generally good to the naked eye.",
                entretien = "Hardness 9, very resistant. Ultrasonic cleaning is safe on a stone that has not been diffused."
            ),
            BuyingGuideArticle(
                pierre = "Pink sapphire",
                accroche = "At the frontier between ruby and sapphire, where the official boundary is still debated between laboratories.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). A vivid, deep pink; beyond a certain saturation threshold, some laboratories reclassify the stone as ruby.",
                puretTraitements = "Heat treatment is common and accepted. Check the classification (pink sapphire or ruby) on the certificate, as value can vary considerably.",
                entretien = "Hardness 9, very resistant. Ultrasonic cleaning is safe on a stone that has not been diffused."
            ),
            BuyingGuideArticle(
                pierre = "White sapphire",
                accroche = "Corundum with no trace of colour, a discreet and affordable alternative to diamond.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Australia (New South Wales). Colourless and clear; dispersion is lower than diamond's, but hardness is nearly equal.",
                puretTraitements = "Rarely treated, its natural transparency already being its main asset. High clarity is sought after.",
                entretien = "Hardness 9, excellent resistance to daily wear. Ultrasonic cleaning is safe."
            ),
            BuyingGuideArticle(
                pierre = "Star sapphire",
                accroche = "A luminous six-rayed star, revealed by a cabochon cut, often in a blue or black sapphire.",
                origineCouleur = "Sri Lanka (Ratnapura), Myanmar (Mogok), Thailand (Kanchanaburi, for the black variety). The most sought-after asterism shows six perfectly straight rays, centred on the dome of the cabochon.",
                puretTraitements = "Rarely heat-treated, since heating risks altering the rutile inclusions responsible for the asterism. The sharpness and centring of the star matter more than transparency.",
                entretien = "Hardness 9, very resistant. Gentle cleaning is recommended to preserve the polish of the dome."
            ),
            BuyingGuideArticle(
                pierre = "Teal sapphire",
                accroche = "A bicolour corundum where blue and yellow combine into a singular blue-green hue, highly prized by connoisseurs.",
                origineCouleur = "United States (Montana, Rock Creek), Australia (New South Wales), Madagascar (Ilakaka). The blue-yellow bicolouration, visible as distinct zones in the rough, blends into an even blue-green once the stone is cut and oriented.",
                puretTraitements = "Often untreated, as the natural nuance is itself sought after by collectors. The cutter's skill in orienting the stone to balance the two colours is decisive.",
                entretien = "Hardness 9, very resistant. Ultrasonic cleaning is safe on a stone that has not been diffused."
            ),
            BuyingGuideArticle(
                pierre = "Rubellite tourmaline",
                accroche = "An intense red caused by manganese, the tourmaline variety closest to ruby.",
                origineCouleur = "Brazil (Cruzeiro mine, Minas Gerais), Nigeria (Oyo), Mozambique (Alto Ligonha), Afghanistan (Paprok valley). An intense, even red to red-pink; marked pleochroism, with characteristic striated prismatic crystals.",
                puretTraitements = "Occasional heat treatment to tone down brownish casts, an accepted treatment. Clarity varies; fine needle-like inclusions are frequent and tolerated when discreet.",
                entretien = "Hardness 7 to 7.5, generally sturdy. A pyroelectric stone that attracts dust through static electricity: gentle cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Yellow tourmaline",
                accroche = "A luminous canary yellow, natural and untreated — the rarest of tourmaline's colours.",
                origineCouleur = "Malawi (Zomba), Brazil (Minas Gerais), Nigeria (Oyo). A vivid, luminous yellow that needs no intervention, unlike many of tourmaline's commercial hues.",
                puretTraitements = "Generally untreated, its natural colour already being sought after. Clarity is often good.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle, regular cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Chrome tourmaline",
                accroche = "A deep, saturated green caused by chromium and vanadium, distinct from tourmaline's \"classic\" green.",
                origineCouleur = "Kenya (Taita-Taveta), Tanzania (Umba). An intense green close to emerald's; marked dichroism, with table orientation decisive in revealing the most saturated hue.",
                puretTraitements = "Rarely treated, as the colour caused by chromium and vanadium is naturally intense. Clarity is generally good.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Green tourmaline (Verdelite)",
                accroche = "A green coloured by iron or chromium, the most classic and widespread of tourmaline's colours.",
                origineCouleur = "Brazil (Minas Gerais), Mozambique (Alto Ligonha), Nigeria (Oyo). A true to dark green; strong birefringence, with cutters orienting the stone to optimise the colour.",
                puretTraitements = "Occasional heat treatment is accepted to lighten overly dark tones. Clarity varies by origin.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle, regular cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Blue tourmaline (indicolite)",
                accroche = "A deep blue, distinct from Paraíba's copper-driven blue-green, with no copper involved.",
                origineCouleur = "Brazil (Minas Gerais), Namibia (Erongo), Afghanistan (Nuristan). Dark blue to blue-grey, generally darker than Paraíba.",
                puretTraitements = "Occasional heat treatment to lighten an overly dark tone, an accepted treatment. Clarity varies.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Pink tourmaline",
                accroche = "A soft hue caused by manganese, at the origin of tourmaline's most sought-after bicolour stones.",
                origineCouleur = "Brazil (Cruzeiro mine, Minas Gerais), Afghanistan (Paprok), Mozambique (Alto Ligonha). Light to deep pink; crystals are often zoned, giving rise to bicolour varieties such as watermelon tourmaline.",
                puretTraitements = "Rarely treated. Clarity is generally good to the naked eye.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle cleaning is recommended."
            ),
            BuyingGuideArticle(
                pierre = "Black tourmaline (Schorl)",
                accroche = "Tourmaline's most common variety, valued in understated jewellery as much as in crystal healing.",
                origineCouleur = "Brazil (Minas Gerais), Namibia (Erongo), Madagascar (Antsirabe). Opaque to sub-transparent black; prismatic crystals, often striated, sometimes of large size.",
                puretTraitements = "Never treated, as its colour and abundance justify no intervention. Very affordable.",
                entretien = "Hardness 7 to 7.5, sturdy. Simple care, with no special precautions."
            ),
            BuyingGuideArticle(
                pierre = "Watermelon tourmaline",
                accroche = "A pink heart ringed with green, like a slice of fruit — one of the most spectacular bicolour tourmalines.",
                origineCouleur = "Brazil (Minas Gerais), United States (Dunton mine, Maine). Sharp zoning, pink at the centre and green at the periphery; most often cut into cross-sections to reveal the \"watermelon\" effect.",
                puretTraitements = "Rarely treated, as the natural zoning is the stone's main appeal. Clarity varies by zone.",
                entretien = "Hardness 7 to 7.5. Gentle cleaning is recommended, particularly for thin slices."
            ),
            BuyingGuideArticle(
                pierre = "Paraíba tourmaline",
                accroche = "An electric \"neon\" blue-green, unique in the mineral kingdom — the most sought-after and most expensive of tourmalines.",
                origineCouleur = "São José da Batalha (Paraíba, Brazil), the historic, nearly exhausted source. Mozambique (Mavuco) and Nigeria (Edeko): more accessible modern sources. The \"neon\" colour is caused by copper, never observed in other tourmalines.",
                puretTraitements = "A certificate specifying origin (Brazil vs Africa) is essential, as the value gap is considerable. Clarity and the intensity of the copper-driven colour matter more than any other criterion.",
                entretien = "Hardness 7 to 7.5. A pyroelectric stone: gentle cleaning is recommended, and it should be protected like any stone of very high value."
            ),
            BuyingGuideArticle(
                pierre = "Pyrope garnet",
                accroche = "The classic blood-red garnet, undiluted by rhodolite — the purest variety in the pyrope family.",
                origineCouleur = "Czech Republic (Bohemia, Podsedice region), South Africa (Kao mine, kimberlites), Tanzania (Umba). A deep, even blood-red, with no notable violet cast.",
                puretTraitements = "Never treated, like the entire garnet family. Unlike rhodolite, pure pyrope contains no significant proportion of iron.",
                entretien = "Hardness 7 to 7.5. No special care beyond the usual precautions for any jewellery stone."
            ),
            BuyingGuideArticle(
                pierre = "Almandine / pyrope garnet",
                accroche = "A deep red garnet, never treated — the most common and most affordable in the family.",
                origineCouleur = "India (Rajmahal Hills, Jharkhand), Sri Lanka (Ratnapura), Czech Republic (Podsedice), Tanzania (Umba valley). Dark red to red-brown, sometimes slightly violet.",
                puretTraitements = "Never treated, thermally or chemically. No cleavage, strong refraction, and good luster.",
                entretien = "Hardness 7 to 7.5, generally sturdy. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Rhodolite garnet",
                accroche = "A \"raspberry\" red-violet with excellent transparency, among the most elegant of garnets.",
                origineCouleur = "Tanzania (Umba valley), Sri Lanka (Ratnapura), India (Orissa). A luminous, even red-violet.",
                puretTraitements = "Never treated. Excellent natural transparency, rarely eye-visibly included.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Star garnet",
                accroche = "An almandine with four or six luminous rays, a rarity found almost exclusively in Idaho.",
                origineCouleur = "United States (Idaho, the world's only deposit of four-rayed asterism), India (Odisha). Dark red; the four-rayed asterism is unique in the world, and the even rarer six-rayed asterism comes from the same deposits.",
                puretTraitements = "Never treated. The sharpness and centring of the star matter more than the transparency of the body colour.",
                entretien = "Hardness 7 to 7.5. Gentle cleaning is recommended to preserve the polish of the dome."
            ),
            BuyingGuideArticle(
                pierre = "Malaya garnet",
                accroche = "A warm pink-orange born of a blend of two garnet species, entirely untreated.",
                origineCouleur = "Tanzania (Umba), Kenya (Taita-Taveta), Madagascar (Anjanabonoina). Pink-orange to warm orange, a hue unique among garnets.",
                puretTraitements = "No treatment is needed or practised: a 100% natural colour, one of this variety's great selling points.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Spessartite garnet",
                accroche = "A brilliant \"mandarin\" orange, among the most luminous colours in the mineral kingdom.",
                origineCouleur = "Namibia (Kunene region), Nigeria (Nasarawa State), Madagascar (Fianarantsoa). Vivid, saturated orange, sometimes leaning toward red-orange.",
                puretTraitements = "A natural colour, never heat-treated. Clarity is generally good to excellent.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Grossular garnet",
                accroche = "The green to yellow-green garnet, a pale cousin of tsavorite and hessonite — the most colour-diverse family of garnets.",
                origineCouleur = "Mali (Sandaré), Kenya (Voi), Canada (Quebec, Jeffrey Mine). From colourless to deep green, passing through yellow-green and the brown-orange of hessonite.",
                puretTraitements = "Never treated. Clarity varies by hue and deposit.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Purple rhodolite garnet",
                accroche = "A purple hue that shifts with lighting, a violet variant of classic rhodolite.",
                origineCouleur = "Tanzania (Umba valley), Mozambique (Cuamba). Purple to deep violet, with the dominant nuance shifting depending on the light source.",
                puretTraitements = "A stable colour, never treated. Clarity is generally good.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Color-change garnet",
                accroche = "Bluish green by day, red-violet by night — an underrated rival to alexandrite, at a far more accessible price.",
                origineCouleur = "Tanzania (Umba, Tunduru), Madagascar (Bekily). Vanadium, rather than chromium, is responsible for the colour change in this garnet, unlike in alexandrite.",
                puretTraitements = "Never treated. As with alexandrite, the strength of the colour change matters more than the hue itself.",
                entretien = "Hardness 7 to 7.5. No special care needed."
            ),
            BuyingGuideArticle(
                pierre = "Blue topaz",
                accroche = "The deep blue obtained by irradiating and then heating a colourless topaz — the species' most commercially sold colour.",
                origineCouleur = "Brazil (Minas Gerais) and Nigeria supply the colourless rough, which is irradiated and then heated to develop a blue ranging from sky blue to deep \"London Blue\".",
                puretTraitements = "The colour is almost always achieved through irradiation followed by heat treatment, stable and permanent — disclosure is mandatory, and the treatment is widely accepted by the market given the stone's accessible price.",
                entretien = "Hardness 8, but a perfect single-direction cleavage makes it fragile under impact. Avoid sudden temperature changes and never use an ultrasonic cleaner."
            ),
            BuyingGuideArticle(
                pierre = "Pink topaz",
                accroche = "Topaz's rarest natural pink, historically mined in Pakistan.",
                origineCouleur = "Pakistan (Katlang, Mardan), Russia (the Urals, historic). Delicate to deep pink; untreated natural pink is rare, and should be distinguished from pink topaz obtained by heating brown stones.",
                puretTraitements = "Check the certificate to see whether the hue is natural or obtained by heat-treating brown material, as the value gap is significant. Naturally high clarity.",
                entretien = "Hardness 8, with a perfect single-direction cleavage: avoid impacts and sudden temperature changes, and never use an ultrasonic cleaner."
            ),
            BuyingGuideArticle(
                pierre = "White topaz",
                accroche = "Topaz in its purest and most common form, colourless and clear — an economical alternative to diamond.",
                origineCouleur = "Brazil (Minas Gerais, Ouro Preto), Nigeria (Jos Plateau), Pakistan (Katlang). Colourless and perfectly clear; it is also the most abundant raw material for producing treated blue topaz.",
                puretTraitements = "Generally untreated in itself. Very high clarity is sought after, its transparency being its main asset.",
                entretien = "Hardness 8, with a perfect single-direction cleavage: avoid impacts and never use an ultrasonic cleaner."
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
                pierre = "Zafiro amarillo",
                accroche = "El corindón, en todas las tonalidades salvo el rojo, encuentra en el amarillo una de sus variedades más luminosas y accesibles.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Amarillo vivo y luminoso, homogéneo, sin reflejo verdoso.",
                puretTraitements = "El calentamiento es habitual para intensificar y uniformar el color, un tratamiento aceptado y estable. Claridad generalmente buena a simple vista.",
                entretien = "Dureza 9, muy resistente al uso diario. Los ultrasonidos son seguros en una piedra no difundida."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro verde",
                accroche = "Un verde discreto y a menudo dicroico, el color más accesible del corindón.",
                origineCouleur = "Australia (Nueva Gales del Sur), Tailandia (Kanchanaburi), Nigeria (Mambilla). Verde franco, sin exceso de gris; el dicroísmo verde/amarillo-verde según el ángulo es característico de la especie.",
                puretTraitements = "El calentamiento es habitual y aceptado para homogeneizar el tono. Claridad generalmente buena.",
                entretien = "Dureza 9, muy resistente. Los ultrasonidos son seguros en una piedra no difundida."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro azul",
                accroche = "El azul de referencia de la alta joyería, justo por detrás del diamante en dureza.",
                origineCouleur = "Cachemira: azul aterciopelado legendario, yacimiento casi agotado, rarísimo. Birmania (Mogok): azul «royal» intenso. Sri Lanka: azul más claro, gran transparencia. Madagascar: principal producción moderna. Azul aterciopelado profundo, con fuerte saturación sin exceso de negro.",
                puretTraitements = "El calentamiento es casi sistemático y ampliamente aceptado. La difusión (titanio clásico, o berilio, más problemática) debe distinguirse en el certificado. Las inclusiones de rutilo en seda pueden producir un asterismo (zafiro estrella).",
                entretien = "Dureza 9, muy resistente al uso diario. Los ultrasonidos son seguros, salvo en una piedra difundida o rellena."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro de Auvernia",
                accroche = "Un corindón francés de tono azul-violeta profundo, extraído de los terrenos volcánicos de Auvernia.",
                origineCouleur = "Francia (Espaly-Saint-Marcel, Alto Loira), fuente única. Azul-violeta muy intenso, típico de los zafiros de origen basáltico; cristales generalmente pequeños, raramente superan el quilate una vez tallados.",
                puretTraitements = "Poco o nada calentado, ya que la rareza y el origen local priman sobre la intensificación del color. Producción escasa, a negociar pieza por pieza.",
                entretien = "Dureza 9, muy resistente. Los ultrasonidos son seguros."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro violeta",
                accroche = "Un violeta profundo, en la frontera entre el zafiro azul y el rubí.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Violeta intenso y homogéneo; puede presentar un ligero cambio de color entre la luz diurna y la luz incandescente.",
                puretTraitements = "El calentamiento es habitual y aceptado para estabilizar el tono. Claridad generalmente buena a simple vista.",
                entretien = "Dureza 9, muy resistente. Los ultrasonidos son seguros en una piedra no difundida."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro rosa",
                accroche = "En la frontera entre rubí y zafiro, cuyo límite oficial sigue siendo objeto de debate entre laboratorios.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Rosa vivo e intenso; más allá de cierto umbral de saturación, algunos laboratorios reclasifican la piedra como rubí.",
                puretTraitements = "El calentamiento es habitual y aceptado. Conviene verificar la clasificación (zafiro rosa o rubí) en el certificado, ya que el valor puede variar considerablemente.",
                entretien = "Dureza 9, muy resistente. Los ultrasonidos son seguros en una piedra no difundida."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro blanco",
                accroche = "El corindón sin rastro colorante, una alternativa discreta y accesible al diamante.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Australia (Nueva Gales del Sur). Incoloro y límpido; dispersión inferior a la del diamante, pero dureza casi igual.",
                puretTraitements = "Raramente tratado, ya que su transparencia natural es de por sí su principal atractivo. Se busca una claridad elevada.",
                entretien = "Dureza 9, excelente resistencia al desgaste diario. Los ultrasonidos son seguros."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro estrella",
                accroche = "Una estrella luminosa de seis puntas, revelada por una talla en cabujón, a menudo en un zafiro azul o negro.",
                origineCouleur = "Sri Lanka (Ratnapura), Birmania (Mogok), Tailandia (Kanchanaburi, variedad negra). El asterismo más buscado presenta seis puntas perfectamente rectas, centradas sobre la cúpula del cabujón.",
                puretTraitements = "Raramente calentado, ya que un tratamiento térmico podría alterar las inclusiones de rutilo responsables del asterismo. La nitidez y el centrado de la estrella priman sobre la transparencia.",
                entretien = "Dureza 9, muy resistente. Se recomienda una limpieza suave para preservar el pulido de la cúpula."
            ),
            BuyingGuideArticle(
                pierre = "Zafiro teal",
                accroche = "Un corindón bicolor en el que el azul y el amarillo se combinan en un tono azul-verde singular, muy apreciado por los conocedores.",
                origineCouleur = "Estados Unidos (Montana, Rock Creek), Australia (Nueva Gales del Sur), Madagascar (Ilakaka). La bicoloración azul-amarillo, visible en zonas distintas en bruto, se funde en un azul-verde homogéneo una vez tallada y orientada la piedra.",
                puretTraitements = "A menudo sin calentar, ya que el matiz natural es en sí mismo lo que buscan los coleccionistas. El talento del tallista, que orienta la piedra para equilibrar los dos colores, resulta determinante.",
                entretien = "Dureza 9, muy resistente. Los ultrasonidos son seguros en una piedra no difundida."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina rubelita",
                accroche = "Un rojo intenso debido al manganeso, la variedad más próxima al rubí entre las turmalinas.",
                origineCouleur = "Brasil (mina de Cruzeiro, Minas Gerais), Nigeria (Oyo), Mozambique (Alto Ligonha), Afganistán (valle de Paprok). Rojo a rojo-rosa intenso y homogéneo; pleocroísmo marcado, cristales prismáticos estriados característicos.",
                puretTraitements = "El calentamiento ocasional para atenuar los tonos pardos es un tratamiento aceptado. Claridad variable; las inclusiones filiformes son frecuentes y se toleran si son discretas.",
                entretien = "Dureza de 7 a 7,5, buena robustez general. Piedra piroeléctrica que atrae el polvo por electricidad estática: se recomienda una limpieza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina amarilla",
                accroche = "Un amarillo canario luminoso, natural y sin tratamiento — el más raro de los colores de la turmalina.",
                origineCouleur = "Malaui (Zomba), Brasil (Minas Gerais), Nigeria (Oyo). Amarillo vivo y luminoso, sin necesidad de intervención, a diferencia de numerosos tonos comerciales de la turmalina.",
                puretTraitements = "Generalmente sin tratar, ya que su color natural es de por sí buscado. Claridad a menudo buena.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave y regular."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina de cromo",
                accroche = "Un verde profundo y saturado debido al cromo y al vanadio, distinto del verde «clásico» de la turmalina.",
                origineCouleur = "Kenia (Taita-Taveta), Tanzania (Umba). Verde intenso próximo al de la esmeralda; dicroísmo marcado, siendo determinante la orientación de la tabla para revelar el tono más saturado.",
                puretTraitements = "Raramente tratada, ya que el color debido al cromo y al vanadio es naturalmente intenso. Claridad generalmente buena.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina verde (Verdelita)",
                accroche = "Un verde coloreado por el hierro o el cromo, el color más clásico y extendido de la turmalina.",
                origineCouleur = "Brasil (Minas Gerais), Mozambique (Alto Ligonha), Nigeria (Oyo). Verde franco a verde oscuro; fuerte birrefringencia, por lo que los tallistas orientan la piedra para optimizar el color.",
                puretTraitements = "El calentamiento ocasional para aclarar los tonos demasiado oscuros es un tratamiento aceptado. Claridad variable según el origen.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave y regular."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina azul (indicolita)",
                accroche = "Un azul profundo, distinto del azul-verde cuprífero de la Paraíba, sin intervención del cobre.",
                origineCouleur = "Brasil (Minas Gerais), Namibia (Erongo), Afganistán (Nuristán). Azul oscuro a azul-grisáceo, generalmente más oscuro que la Paraíba.",
                puretTraitements = "El calentamiento ocasional para aclarar un tono demasiado oscuro es un tratamiento aceptado. Claridad variable.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina rosa",
                accroche = "Un tono suave debido al manganeso, en el origen de las turmalinas bicolores más buscadas.",
                origineCouleur = "Brasil (mina de Cruzeiro, Minas Gerais), Afganistán (Paprok), Mozambique (Alto Ligonha). Rosa claro a rosa intenso; cristales a menudo zonados, en el origen de variedades bicolores como la sandía.",
                puretTraitements = "Raramente tratada. Claridad generalmente buena a simple vista.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina negra (Chorlo)",
                accroche = "La variedad de turmalina más común, apreciada tanto en joyería sobria como en litoterapia.",
                origineCouleur = "Brasil (Minas Gerais), Namibia (Erongo), Madagascar (Antsirabe). Negro opaco a subtransparente; cristales prismáticos a menudo estriados, a veces de gran tamaño.",
                puretTraitements = "Nunca tratada, ya que su color y su abundancia no justifican ninguna intervención. Precio muy accesible.",
                entretien = "Dureza de 7 a 7,5, robusta. Cuidado sencillo, sin precauciones particulares."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina sandía",
                accroche = "Un corazón rosa rodeado de verde, como una rodaja de fruta — una de las turmalinas bicolores más espectaculares.",
                origineCouleur = "Brasil (Minas Gerais), Estados Unidos (mina Dunton, Maine). Zonación nítida, rosa en el centro y verde en la periferia; se talla la mayoría de las veces en lonchas transversales para revelar el efecto «sandía».",
                puretTraitements = "Raramente tratada, ya que la zonación natural es el principal atractivo de la piedra. Claridad variable según la zona.",
                entretien = "Dureza de 7 a 7,5. Se recomienda una limpieza suave, en particular para las lonchas finas."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina Paraíba",
                accroche = "Un azul-verde «neón» eléctrico, único en el reino mineral — la turmalina más buscada y más costosa.",
                origineCouleur = "São José da Batalha (Paraíba, Brasil), fuente histórica casi agotada. Mozambique (Mavuco) y Nigeria (Edeko): fuentes modernas más accesibles. Color «neón» debido al cobre, nunca observado en las demás turmalinas.",
                puretTraitements = "Un certificado que precise el origen (Brasil o África) es esencial, dado que la diferencia de valor es considerable. La claridad y la intensidad del color cuprífero priman sobre cualquier otro criterio.",
                entretien = "Dureza de 7 a 7,5. Piedra piroeléctrica: se recomienda una limpieza suave, y protegerla como cualquier piedra de muy alto valor."
            ),
            BuyingGuideArticle(
                pierre = "Granate piropo",
                accroche = "El granate rojo sangre clásico, sin dilución rodolita — la variedad más pura de la familia piropo.",
                origineCouleur = "República Checa (Bohemia, región de Podsedice), Sudáfrica (mina de Kao, kimberlitas), Tanzania (Umba). Rojo sangre profundo y homogéneo, sin matiz violáceo notable.",
                puretTraitements = "Nunca tratado, como el conjunto de la familia de los granates. A diferencia de la rodolita, el piropo puro no contiene hierro en proporción significativa.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares, más allá de las precauciones habituales para cualquier piedra de joyería."
            ),
            BuyingGuideArticle(
                pierre = "Granate almandino / piropo",
                accroche = "Un granate rojo profundo, nunca tratado — el más corriente y accesible de la familia.",
                origineCouleur = "India (colinas de Rajmahal, Jharkhand), Sri Lanka (Ratnapura), República Checa (Podsedice), Tanzania (valle del Umba). Rojo oscuro a rojo-marrón, a veces ligeramente violáceo.",
                puretTraitements = "Nunca tratado térmica ni químicamente. Sin clivaje, fuerte refracción y buen brillo.",
                entretien = "Dureza de 7 a 7,5, buena robustez general. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate rodolita",
                accroche = "Un rojo-violáceo «frambuesa» de excelente transparencia, entre los granates más elegantes.",
                origineCouleur = "Tanzania (valle del Umba), Sri Lanka (Ratnapura), India (Orissa). Rojo-violáceo luminoso y homogéneo.",
                puretTraitements = "Nunca tratada. Excelente transparencia natural, raramente incluida a simple vista.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate estrellado",
                accroche = "Un almandino de cuatro o seis puntas luminosas, una rareza que se encuentra casi exclusivamente en Idaho.",
                origineCouleur = "Estados Unidos (Idaho, único yacimiento mundial de asterismo de cuatro puntas), India (Odisha). Rojo oscuro; el asterismo de cuatro puntas es único en el mundo, y el de seis puntas, aún más raro, procede de los mismos yacimientos.",
                puretTraitements = "Nunca tratado. La nitidez y el centrado de la estrella priman sobre la transparencia del fondo.",
                entretien = "Dureza de 7 a 7,5. Se recomienda una limpieza suave para preservar el pulido de la cúpula."
            ),
            BuyingGuideArticle(
                pierre = "Granate malaya",
                accroche = "Un rosa-anaranjado cálido, nacido de la mezcla de dos granates, sin tratamiento alguno.",
                origineCouleur = "Tanzania (Umba), Kenia (Taita-Taveta), Madagascar (Anjanabonoina). Rosa-anaranjado a anaranjado cálido, tono único entre los granates.",
                puretTraitements = "No es necesario ni se practica ningún tratamiento: color 100 % natural, uno de los grandes argumentos de venta de esta variedad.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate espesartita",
                accroche = "Un naranja «mandarina» resplandeciente, entre los colores más luminosos del reino mineral.",
                origineCouleur = "Namibia (región de Kunene), Nigeria (estado de Nasarawa), Madagascar (Fianarantsoa). Naranja vivo y saturado, a veces tirando a rojo-anaranjado.",
                puretTraitements = "Color natural, nunca tratado térmicamente. Claridad generalmente buena a excelente.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate grosularia",
                accroche = "El granate verde a amarillo-verde, primo poco coloreado de la tsavorita y la hesonita — la familia con mayor diversidad de colores.",
                origineCouleur = "Malí (Sandaré), Kenia (Voi), Canadá (Quebec, mina Jeffrey). Desde el incoloro hasta el verde profundo, pasando por el amarillo-verde y el marrón-anaranjado de la hesonita.",
                puretTraitements = "Nunca tratado. Claridad variable según el tono y el yacimiento.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate rodolita violáceo",
                accroche = "Un tono púrpura que cambia según la iluminación, variante violácea de la rodolita clásica.",
                origineCouleur = "Tanzania (valle del Umba), Mozambique (Cuamba). Púrpura a violeta intenso, con el matiz dominante variando según la fuente luminosa.",
                puretTraitements = "Color estable, nunca tratado. Claridad generalmente buena.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Granate de cambio de color",
                accroche = "Verde azulado de día, rojo-violeta por la noche — un rival poco conocido de la alejandrita, a un precio mucho más accesible.",
                origineCouleur = "Tanzania (Umba, Tunduru), Madagascar (Bekily). El vanadio, más que el cromo, es el responsable del cambio de color en este granate, a diferencia de la alejandrita.",
                puretTraitements = "Nunca tratado. La intensidad del cambio de color prevalece sobre el propio matiz, igual que en la alejandrita.",
                entretien = "Dureza de 7 a 7,5. Sin cuidados particulares."
            ),
            BuyingGuideArticle(
                pierre = "Topacio azul",
                accroche = "El azul profundo obtenido por irradiación y posterior calentamiento de un topacio incoloro — el color más comercializado de la especie.",
                origineCouleur = "Brasil (Minas Gerais) y Nigeria suministran la materia prima incolora, que se irradia y luego se calienta para desarrollar un azul que va del celeste al «London Blue» profundo.",
                puretTraitements = "Color obtenido casi sistemáticamente por irradiación y posterior tratamiento térmico, estable y permanente — divulgación obligatoria, ampliamente aceptada por el mercado dado su precio accesible.",
                entretien = "Dureza 8, pero un clivaje perfecto en una dirección lo hace frágil ante los golpes. Evitar cambios bruscos de temperatura y prohibir los ultrasonidos."
            ),
            BuyingGuideArticle(
                pierre = "Topacio rosa",
                accroche = "El rosa natural más raro del topacio, extraído históricamente en Pakistán.",
                origineCouleur = "Pakistán (Katlang, Mardan), Rusia (Urales, histórico). Rosa delicado a intenso; el rosa natural sin tratar es raro, y debe distinguirse del topacio rosa obtenido por calentamiento de piedras marrones.",
                puretTraitements = "Conviene verificar en el certificado si el tono es natural u obtenido por tratamiento térmico de material marrón, ya que la diferencia de valor es importante. Claridad naturalmente elevada.",
                entretien = "Dureza 8, clivaje perfecto en una dirección: evitar los golpes y los cambios bruscos de temperatura, prohibir los ultrasonidos."
            ),
            BuyingGuideArticle(
                pierre = "Topacio blanco",
                accroche = "El topacio en su forma más pura y corriente, incoloro y límpido — una alternativa económica al diamante.",
                origineCouleur = "Brasil (Minas Gerais, Ouro Preto), Nigeria (meseta de Jos), Pakistán (Katlang). Incoloro y perfectamente límpido; es también la materia prima más abundante para la producción de topacio azul tratado.",
                puretTraitements = "Generalmente sin tratar en sí mismo. Se busca una claridad muy elevada, ya que su transparencia es su principal atractivo.",
                entretien = "Dureza 8, clivaje perfecto en una dirección: evitar los golpes y prohibir los ultrasonidos."
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
                pierre = "Zaffiro giallo",
                accroche = "Il corindone, in tutte le tonalità tranne il rosso, trova nel giallo una delle sue varietà più luminose e accessibili.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Giallo vivo e luminoso, omogeneo, senza riflessi verdastri.",
                puretTraitements = "Il riscaldamento è comune per intensificare e uniformare il colore, trattamento accettato e stabile. Chiarezza generalmente buona a occhio nudo.",
                entretien = "Durezza 9, molto resistente all'uso quotidiano. Gli ultrasuoni sono sicuri per una pietra non diffusa."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro verde",
                accroche = "Un verde discreto e spesso dicroico, la tonalità più accessibile del corindone.",
                origineCouleur = "Australia (Nuovo Galles del Sud), Thailandia (Kanchanaburi), Nigeria (Mambilla). Verde deciso, senza troppo grigio; il dicroismo verde/giallo-verde a seconda dell'angolo è caratteristico della specie.",
                puretTraitements = "Il riscaldamento è comune e accettato per omogeneizzare la tonalità. Chiarezza generalmente buona.",
                entretien = "Durezza 9, molto resistente. Gli ultrasuoni sono sicuri per una pietra non diffusa."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro blu",
                accroche = "Il blu di riferimento dell'alta gioielleria, subito dopo il diamante in durezza.",
                origineCouleur = "Kashmir: blu vellutato leggendario, giacimento quasi esaurito, rarissimo. Birmania (Mogok): blu «royal» intenso. Sri Lanka: blu più chiaro, grande trasparenza. Madagascar: principale produzione moderna. Blu vellutato profondo, forte saturazione senza eccesso di nero.",
                puretTraitements = "Il riscaldamento è quasi sistematico e ampiamente accettato. La diffusione (titanio classico, o berillio, più problematica) deve essere distinta sul certificato. Inclusioni di rutilo «seta» possono produrre un asterismo (zaffiro stellato).",
                entretien = "Durezza 9, molto resistente all'uso quotidiano. Gli ultrasuoni sono sicuri, tranne su una pietra diffusa o riempita."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro d'Alvernia",
                accroche = "Un corindone francese dalla tonalità blu-violetto profonda, estratto dai terreni vulcanici dell'Alvernia.",
                origineCouleur = "Francia (Espaly-Saint-Marcel, Haute-Loire), unica fonte. Blu-violetto molto intenso, tipico degli zaffiri di origine basaltica; cristalli generalmente piccoli, raramente oltre un carato tagliato.",
                puretTraitements = "Poco o per nulla riscaldato, poiché la rarità e l'origine locale prevalgono sull'intensificazione del colore. Produzione limitata, da negoziare pietra per pietra.",
                entretien = "Durezza 9, molto resistente. Gli ultrasuoni sono sicuri."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro viola",
                accroche = "Un viola profondo, al confine tra lo zaffiro blu e il rubino.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Viola intenso e omogeneo; può presentare un lieve cambiamento di colore tra luce diurna e luce incandescente.",
                puretTraitements = "Il riscaldamento è comune e accettato per stabilizzare la tonalità. Chiarezza generalmente buona a occhio nudo.",
                entretien = "Durezza 9, molto resistente. Gli ultrasuoni sono sicuri per una pietra non diffusa."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro rosa",
                accroche = "Al confine tra rubino e zaffiro, un limite ufficiale ancora dibattuto a seconda dei laboratori.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Tanzania (Tunduru). Rosa vivo e intenso; oltre una certa soglia di saturazione, alcuni laboratori riclassificano la pietra come rubino.",
                puretTraitements = "Il riscaldamento è comune e accettato. Verificare la classificazione (zaffiro rosa o rubino) sul certificato, poiché il valore può variare notevolmente.",
                entretien = "Durezza 9, molto resistente. Gli ultrasuoni sono sicuri per una pietra non diffusa."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro bianco",
                accroche = "Il corindone privo di traccia colorante, un'alternativa discreta e accessibile al diamante.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagascar (Ilakaka), Australia (Nuovo Galles del Sud). Incolore e limpido; dispersione inferiore a quella del diamante, ma durezza quasi pari.",
                puretTraitements = "Raramente trattato, poiché la sua trasparenza naturale è già il suo principale punto di forza. Si ricerca una chiarezza elevata.",
                entretien = "Durezza 9, eccellente resistenza all'uso quotidiano. Gli ultrasuoni sono sicuri."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro stellato",
                accroche = "Una stella luminosa a sei raggi, rivelata da un taglio a cabochon, spesso in uno zaffiro blu o nero.",
                origineCouleur = "Sri Lanka (Ratnapura), Birmania (Mogok), Thailandia (Kanchanaburi, varietà nera). L'asterismo più ricercato presenta sei raggi perfettamente diritti, centrati sulla cupola del cabochon.",
                puretTraitements = "Raramente riscaldato, poiché un trattamento termico rischierebbe di alterare le inclusioni di rutilo responsabili dell'asterismo. La nitidezza e la centratura della stella prevalgono sulla trasparenza.",
                entretien = "Durezza 9, molto resistente. Si consiglia una pulizia delicata per preservare la lucidatura della cupola."
            ),
            BuyingGuideArticle(
                pierre = "Zaffiro teal",
                accroche = "Un corindone bicolore in cui blu e giallo si combinano in una tonalità blu-verde singolare, molto apprezzata dagli intenditori.",
                origineCouleur = "Stati Uniti (Montana, Rock Creek), Australia (Nuovo Galles del Sud), Madagascar (Ilakaka). La bicolorazione blu-giallo, visibile in zone distinte allo stato grezzo, si fonde in un blu-verde omogeneo una volta che la pietra è tagliata e orientata.",
                puretTraitements = "Spesso non riscaldato, poiché la sfumatura naturale è essa stessa ricercata dai collezionisti. Il talento del tagliatore, che orienta la pietra per bilanciare i due colori, è determinante.",
                entretien = "Durezza 9, molto resistente. Gli ultrasuoni sono sicuri per una pietra non diffusa."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina rubellite",
                accroche = "Un rosso intenso dovuto al manganese, la varietà di tormalina più vicina al rubino.",
                origineCouleur = "Brasile (miniera di Cruzeiro, Minas Gerais), Nigeria (Oyo), Mozambico (Alto Ligonha), Afghanistan (valle di Paprok). Rosso a rosso-rosa intenso e omogeneo; pleocroismo marcato, cristalli prismatici striati caratteristici.",
                puretTraitements = "Riscaldamento occasionale per attenuare i toni bruni, trattamento accettato. Chiarezza variabile; inclusioni filiformi frequenti e tollerate se discrete.",
                entretien = "Durezza 7-7,5, buona robustezza generale. Pietra piroelettrica che attira la polvere per elettricità statica: si consiglia una pulizia delicata."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina gialla",
                accroche = "Un giallo canarino luminoso, naturale e senza trattamento — la più rara tra le tonalità di tormalina.",
                origineCouleur = "Malawi (Zomba), Brasile (Minas Gerais), Nigeria (Oyo). Giallo vivo e luminoso, senza necessità di intervento a differenza di molte tonalità commerciali della tormalina.",
                puretTraitements = "Generalmente non trattata, poiché il suo colore naturale è già ricercato di per sé. Chiarezza spesso buona.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata e regolare."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina cromifera",
                accroche = "Un verde profondo e saturo dovuto al cromo e al vanadio, distinto dal verde «classico» della tormalina.",
                origineCouleur = "Kenya (Taita-Taveta), Tanzania (Umba). Verde intenso prossimo allo smeraldo; dicroismo marcato, l'orientamento della tavola è determinante per rivelare la tonalità più satura.",
                puretTraitements = "Raramente trattata, poiché il colore dovuto al cromo e al vanadio è naturalmente intenso. Chiarezza generalmente buona.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina verde (Verdelite)",
                accroche = "Un verde colorato dal ferro o dal cromo, la tonalità più classica e diffusa della tormalina.",
                origineCouleur = "Brasile (Minas Gerais), Mozambico (Alto Ligonha), Nigeria (Oyo). Verde deciso a verde scuro; forte birifrangenza, i tagliatori orientano la pietra per ottimizzare il colore.",
                puretTraitements = "Riscaldamento occasionale accettato per schiarire le tonalità troppo scure. Chiarezza variabile a seconda dell'origine.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata e regolare."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina blu (indicolite)",
                accroche = "Un blu profondo, distinto dal blu-verde cuprifero della Paraíba, senza l'intervento del rame.",
                origineCouleur = "Brasile (Minas Gerais), Namibia (Erongo), Afghanistan (Nuristan). Blu scuro a blu-grigio, generalmente più scuro della Paraíba.",
                puretTraitements = "Riscaldamento occasionale per schiarire un tono troppo scuro, trattamento accettato. Chiarezza variabile.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina rosa",
                accroche = "Una tonalità delicata dovuta al manganese, all'origine delle tormaline bicolori più ricercate.",
                origineCouleur = "Brasile (miniera di Cruzeiro, Minas Gerais), Afghanistan (Paprok), Mozambico (Alto Ligonha). Rosa chiaro a rosa intenso; cristalli spesso zonati, all'origine delle varietà bicolori come l'anguria.",
                puretTraitements = "Raramente trattata. Chiarezza generalmente buona a occhio nudo.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina nera (Schorl)",
                accroche = "La varietà di tormalina più comune, apprezzata tanto nella gioielleria sobria quanto nella litoterapia.",
                origineCouleur = "Brasile (Minas Gerais), Namibia (Erongo), Madagascar (Antsirabe). Nero opaco a sub-trasparente; cristalli prismatici spesso striati, talvolta di grandi dimensioni.",
                puretTraitements = "Mai trattata, poiché il suo colore e la sua abbondanza non giustificano alcun intervento. Prezzo molto accessibile.",
                entretien = "Durezza 7-7,5, robusta. Cura semplice, senza precauzioni particolari."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina anguria",
                accroche = "Un cuore rosa cinto di verde, come una fetta di frutto — una delle tormaline bicolori più spettacolari.",
                origineCouleur = "Brasile (Minas Gerais), Stati Uniti (miniera di Dunton, Maine). Zonatura netta, rosa al centro e verde in periferia; tagliata il più delle volte in fette trasversali per rivelare l'effetto «anguria».",
                puretTraitements = "Raramente trattata, poiché la zonatura naturale è il principale attrattivo della pietra. Chiarezza variabile a seconda della zona.",
                entretien = "Durezza 7-7,5. Si consiglia una pulizia delicata, in particolare per le fette sottili."
            ),
            BuyingGuideArticle(
                pierre = "Tormalina Paraíba",
                accroche = "Un blu-verde «neon» elettrico, unico nel regno minerale — la tormalina più ricercata e più costosa.",
                origineCouleur = "São José da Batalha (Paraíba, Brasile), fonte storica quasi esaurita. Mozambico (Mavuco) e Nigeria (Edeko): fonti moderne più accessibili. Colore «neon» dovuto al rame, mai osservato nelle altre tormaline.",
                puretTraitements = "Un certificato che precisi l'origine (Brasile o Africa) è essenziale, poiché lo scarto di valore è considerevole. Chiarezza e intensità del colore cuprifero prevalgono su qualsiasi altro criterio.",
                entretien = "Durezza 7-7,5. Pietra piroelettrica: si consiglia una pulizia delicata, da proteggere come qualsiasi pietra di altissimo valore."
            ),
            BuyingGuideArticle(
                pierre = "Granato piropo",
                accroche = "Il classico granato rosso sangue, senza la diluizione della rodolite — la varietà più pura della famiglia piropo.",
                origineCouleur = "Repubblica Ceca (Boemia, regione di Podsedice), Sudafrica (miniera di Kao, kimberliti), Tanzania (Umba). Rosso sangue profondo e omogeneo, senza sfumatura violacea significativa.",
                puretTraitements = "Mai trattato, come l'intera famiglia dei granati. A differenza della rodolite, il piropo puro non contiene ferro in proporzione significativa.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare, oltre alle normali precauzioni per qualsiasi pietra di gioielleria."
            ),
            BuyingGuideArticle(
                pierre = "Granato almandino / piropo",
                accroche = "Un granato rosso profondo, mai trattato — il più comune e il più accessibile della famiglia.",
                origineCouleur = "India (Rajmahal Hills, Jharkhand), Sri Lanka (Ratnapura), Repubblica Ceca (Podsedice), Tanzania (valle dell'Umba). Rosso scuro a rosso-bruno, talvolta leggermente violaceo.",
                puretTraitements = "Mai trattato termicamente o chimicamente. Nessuna sfaldatura, forte rifrazione e buona lucentezza.",
                entretien = "Durezza 7-7,5, buona robustezza generale. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato rodolite",
                accroche = "Un rosso-violaceo «lampone» dall'eccellente trasparenza, tra i granati più eleganti.",
                origineCouleur = "Tanzania (valle dell'Umba), Sri Lanka (Ratnapura), India (Orissa). Rosso-violaceo luminoso e omogeneo.",
                puretTraitements = "Mai trattata. Eccellente trasparenza naturale, raramente inclusa a occhio nudo.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato stellato",
                accroche = "Un almandino a quattro o sei raggi luminosi, una rarità che si trova quasi esclusivamente in Idaho.",
                origineCouleur = "Stati Uniti (Idaho, unico giacimento al mondo di asterismo a quattro raggi), India (Odisha). Rosso scuro; l'asterismo a quattro raggi è unico al mondo, quello a sei raggi, ancora più raro, proviene dagli stessi giacimenti.",
                puretTraitements = "Mai trattato. La nitidezza e la centratura della stella prevalgono sulla trasparenza del fondo.",
                entretien = "Durezza 7-7,5. Si consiglia una pulizia delicata per preservare la lucidatura della cupola."
            ),
            BuyingGuideArticle(
                pierre = "Granato malaya",
                accroche = "Un rosa-arancio caldo nato dalla combinazione di due granati, senza alcun trattamento.",
                origineCouleur = "Tanzania (Umba), Kenya (Taita-Taveta), Madagascar (Anjanabonoina). Rosa-arancio ad arancio caldo, tonalità unica tra i granati.",
                puretTraitements = "Nessun trattamento è necessario né praticato: colore al 100% naturale, uno dei grandi punti di forza di questa varietà.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato spessartite",
                accroche = "Un arancio «mandarino» brillante, tra i colori più luminosi del regno minerale.",
                origineCouleur = "Namibia (regione del Kunene), Nigeria (Stato del Nasarawa), Madagascar (Fianarantsoa). Arancio vivo e saturo, talvolta tendente al rosso-arancio.",
                puretTraitements = "Colore naturale, mai trattato termicamente. Chiarezza generalmente buona o eccellente.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato grossularia",
                accroche = "Il granato dal verde al giallo-verde, cugino poco colorato della tsavorite e dell'essonite — la famiglia più diversificata nei colori.",
                origineCouleur = "Mali (Sandaré), Kenya (Voi), Canada (Québec, Jeffrey Mine). Dall'incolore al verde profondo, passando per il giallo-verde e il bruno-arancio dell'essonite.",
                puretTraitements = "Mai trattato. Chiarezza variabile a seconda della tonalità e del giacimento.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato rodolite violaceo",
                accroche = "Una tonalità porpora che muta a seconda dell'illuminazione, variante violacea della classica rodolite.",
                origineCouleur = "Tanzania (valle dell'Umba), Mozambico (Cuamba). Porpora a violetto intenso, con la sfumatura dominante che varia a seconda della fonte luminosa.",
                puretTraitements = "Colore stabile, mai trattato. Chiarezza generalmente buona.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Granato a cambiamento di colore",
                accroche = "Verde bluastro di giorno, rosso-violetto la sera — un rivale poco conosciuto dell'alessandrite, a un prezzo molto più accessibile.",
                origineCouleur = "Tanzania (Umba, Tunduru), Madagascar (Bekily). È il vanadio, piuttosto che il cromo, a essere responsabile del cambiamento di colore in questo granato, a differenza dell'alessandrite.",
                puretTraitements = "Mai trattato. L'intensità del cambiamento di colore prevale sulla tonalità stessa, come per l'alessandrite.",
                entretien = "Durezza 7-7,5. Nessuna cura particolare."
            ),
            BuyingGuideArticle(
                pierre = "Topazio blu",
                accroche = "Il blu profondo ottenuto per irradiazione e successivo riscaldamento di un topazio incolore — la tonalità più commercializzata della specie.",
                origineCouleur = "Brasile (Minas Gerais) e Nigeria forniscono la materia prima incolore, irradiata e poi riscaldata per sviluppare un blu che va dal celeste al profondo «London Blue».",
                puretTraitements = "Colore ottenuto quasi sistematicamente per irradiazione e successivo trattamento termico, stabile e permanente — divulgazione obbligatoria, ampiamente accettata dal mercato per via del suo prezzo accessibile.",
                entretien = "Durezza 8, ma una sfaldatura perfetta in una direzione lo rende fragile agli urti. Evitare sbalzi termici improvvisi e vietare gli ultrasuoni."
            ),
            BuyingGuideArticle(
                pierre = "Topazio rosa",
                accroche = "Il rosa naturale più raro del topazio, storicamente estratto in Pakistan.",
                origineCouleur = "Pakistan (Katlang, Mardan), Russia (Urali, storico). Rosa delicato a intenso; il rosa naturale non trattato è raro, da distinguere dal topazio rosa ottenuto per riscaldamento di pietre brune.",
                puretTraitements = "Verificare sul certificato se la tonalità è naturale o ottenuta per trattamento termico di materia bruna, poiché lo scarto di valore è importante. Chiarezza naturalmente elevata.",
                entretien = "Durezza 8, sfaldatura perfetta in una direzione: evitare urti e sbalzi termici improvvisi, vietare gli ultrasuoni."
            ),
            BuyingGuideArticle(
                pierre = "Topazio bianco",
                accroche = "Il topazio nella sua forma più pura e più comune, incolore e limpido — un'alternativa economica al diamante.",
                origineCouleur = "Brasile (Minas Gerais, Ouro Preto), Nigeria (Jos Plateau), Pakistan (Katlang). Incolore e perfettamente limpido; è anche la materia prima più abbondante per la produzione di topazio blu trattato.",
                puretTraitements = "Generalmente non trattato di per sé. Si ricerca una chiarezza molto elevata, poiché la sua trasparenza è il suo principale punto di forza.",
                entretien = "Durezza 8, sfaldatura perfetta in una direzione: evitare urti e vietare gli ultrasuoni."
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
                pierre = "Gelber Saphir",
                accroche = "Der Korund in all seinen Farben außer Rot findet im Gelb eine seiner leuchtendsten und erschwinglichsten Varietäten.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tansania (Tunduru). Kräftiges, leuchtendes Gelb, gleichmäßig, ohne grünlichen Schimmer.",
                puretTraitements = "Erhitzen ist gängig, um die Farbe zu intensivieren und zu vereinheitlichen — eine akzeptierte und stabile Behandlung. Reinheit meist gut mit bloßem Auge.",
                entretien = "Härte 9, sehr widerstandsfähig im Alltag. Ultraschallreinigung ist bei einem nicht diffundierten Stein unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Grüner Saphir",
                accroche = "Ein dezentes, oft dichroitisches Grün — die erschwinglichste Farbe des Korunds.",
                origineCouleur = "Australien (New South Wales), Thailand (Kanchanaburi), Nigeria (Mambilla). Kräftiges Grün ohne zu viel Grauanteil; der je nach Blickwinkel wechselnde Dichroismus Grün/Gelbgrün ist charakteristisch für die Art.",
                puretTraitements = "Erhitzen ist gängig und akzeptiert, um den Farbton zu vereinheitlichen. Reinheit meist gut.",
                entretien = "Härte 9, sehr widerstandsfähig. Ultraschallreinigung ist bei einem nicht diffundierten Stein unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Blauer Saphir",
                accroche = "Das Referenzblau der Haute Joaillerie, in der Härte gleich nach dem Diamanten.",
                origineCouleur = "Kaschmir: legendäres samtiges Blau, eine nahezu erschöpfte, äußerst seltene Lagerstätte. Myanmar (Mogok): intensives «Royal»-Blau. Sri Lanka: helleres Blau, große Transparenz. Madagaskar: wichtigste moderne Förderung. Tiefes samtiges Blau mit starker Sättigung, ohne zu viel Schwarzanteil.",
                puretTraitements = "Erhitzen ist nahezu die Regel und wird weithin akzeptiert. Diffusion (klassisch mit Titan, oder problematischer mit Beryllium) muss auf dem Zertifikat unterschieden werden. Seiden-Einschlüsse aus Rutil können einen Asterismus (Sternsaphir) hervorrufen.",
                entretien = "Härte 9, sehr widerstandsfähig im Alltag. Ultraschallreinigung ist unbedenklich, außer bei einem diffundierten oder verfüllten Stein."
            ),
            BuyingGuideArticle(
                pierre = "Auvergne-Saphir",
                accroche = "Ein französischer Korund mit tiefer blauvioletter Farbe, gefördert aus den vulkanischen Böden der Auvergne.",
                origineCouleur = "Frankreich (Espaly-Saint-Marcel, Haute-Loire), einzige Quelle. Sehr kräftiges Blauviolett, typisch für Saphire basaltischen Ursprungs; die Kristalle sind meist klein, geschliffen selten über einem Karat.",
                puretTraitements = "Kaum oder gar nicht erhitzt, denn Seltenheit und regionale Herkunft zählen hier mehr als die Farbintensivierung. Geringe Fördermenge, Stück für Stück zu verhandeln.",
                entretien = "Härte 9, sehr widerstandsfähig. Ultraschallreinigung unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Violetter Saphir",
                accroche = "Ein tiefes Violett, an der Grenze zwischen blauem Saphir und Rubin.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tansania (Tunduru). Kräftiges, gleichmäßiges Violett; kann zwischen Tageslicht und Glühlicht einen leichten Farbwechsel zeigen.",
                puretTraitements = "Erhitzen ist gängig und akzeptiert, um den Farbton zu stabilisieren. Reinheit meist gut mit bloßem Auge.",
                entretien = "Härte 9, sehr widerstandsfähig. Ultraschallreinigung ist bei einem nicht diffundierten Stein unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Rosa Saphir",
                accroche = "An der Grenze zwischen Rubin und Saphir, deren offizielle Trennlinie je nach Labor umstritten bleibt.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tansania (Tunduru). Kräftiges, sattes Rosa; ab einem bestimmten Sättigungsgrad stufen manche Labore den Stein als Rubin um.",
                puretTraitements = "Erhitzen ist gängig und akzeptiert. Die Einstufung (rosa Saphir oder Rubin) sollte auf dem Zertifikat geprüft werden, da der Wert stark schwanken kann.",
                entretien = "Härte 9, sehr widerstandsfähig. Ultraschallreinigung ist bei einem nicht diffundierten Stein unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Weißer Saphir",
                accroche = "Der Korund ohne jede farbgebende Spur — eine dezente und erschwingliche Alternative zum Diamanten.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Australien (New South Wales). Farblos und klar; die Dispersion ist geringer als beim Diamanten, die Härte jedoch fast gleich.",
                puretTraitements = "Selten behandelt, da seine natürliche Transparenz bereits sein wichtigster Vorzug ist. Hohe Reinheit ist gefragt.",
                entretien = "Härte 9, ausgezeichnete Widerstandsfähigkeit gegen alltägliche Abnutzung. Ultraschallreinigung unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Sternsaphir",
                accroche = "Ein leuchtender sechsstrahliger Stern, der durch einen Cabochonschliff sichtbar wird, meist in einem blauen oder schwarzen Saphir.",
                origineCouleur = "Sri Lanka (Ratnapura), Myanmar (Mogok), Thailand (Kanchanaburi, schwarze Varietät). Der begehrteste Asterismus zeigt sechs vollkommen gerade Strahlen, zentriert auf der Wölbung des Cabochons.",
                puretTraitements = "Selten erhitzt, da eine Wärmebehandlung die für den Asterismus verantwortlichen Rutil-Einschlüsse beeinträchtigen könnte. Schärfe und Zentrierung des Sterns zählen mehr als die Transparenz.",
                entretien = "Härte 9, sehr widerstandsfähig. Eine sanfte Reinigung wird empfohlen, um die Politur der Wölbung zu erhalten."
            ),
            BuyingGuideArticle(
                pierre = "Teal-Saphir",
                accroche = "Ein zweifarbiger Korund, bei dem Blau und Gelb sich zu einem einzigartigen Blaugrün verbinden — bei Kennern sehr geschätzt.",
                origineCouleur = "USA (Montana, Rock Creek), Australien (New South Wales), Madagaskar (Ilakaka). Die im Rohzustand in getrennten Zonen sichtbare Zweifarbigkeit Blau-Gelb verschmilzt nach dem Schliff und der richtigen Ausrichtung zu einem gleichmäßigen Blaugrün.",
                puretTraitements = "Oft nicht erhitzt, da gerade die natürliche Nuance bei Sammlern gefragt ist. Entscheidend ist das Können des Schleifers, der den Stein ausrichtet, um beide Farben auszubalancieren.",
                entretien = "Härte 9, sehr widerstandsfähig. Ultraschallreinigung ist bei einem nicht diffundierten Stein unbedenklich."
            ),
            BuyingGuideArticle(
                pierre = "Rubellit-Turmalin",
                accroche = "Ein durch Mangan verursachtes intensives Rot — die dem Rubin am nächsten kommende Varietät unter den Turmalinen.",
                origineCouleur = "Brasilien (Mine Cruzeiro, Minas Gerais), Nigeria (Oyo), Mosambik (Alto Ligonha), Afghanistan (Paprok-Tal). Intensives, gleichmäßiges Rot bis Rotrosa; ausgeprägter Pleochroismus, charakteristisch gestreifte prismatische Kristalle.",
                puretTraitements = "Gelegentliches Erhitzen, um bräunliche Töne abzuschwächen — eine akzeptierte Behandlung. Reinheit variabel; feine nadelförmige Einschlüsse sind häufig und werden toleriert, wenn sie dezent sind.",
                entretien = "Härte 7 bis 7,5, insgesamt robust. Ein pyroelektrischer Stein, der durch statische Elektrizität Staub anzieht: eine sanfte Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Gelber Turmalin",
                accroche = "Ein leuchtendes Kanariengelb, natürlich und unbehandelt — die seltenste aller Turmalinfarben.",
                origineCouleur = "Malawi (Zomba), Brasilien (Minas Gerais), Nigeria (Oyo). Kräftiges, leuchtendes Gelb, das im Gegensatz zu vielen handelsüblichen Turmalinfarben keinerlei Eingriff benötigt.",
                puretTraitements = "In der Regel unbehandelt, da seine natürliche Farbe bereits gefragt ist. Reinheit oft gut.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte, regelmäßige Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Chromturmalin",
                accroche = "Ein tiefes, gesättigtes Grün durch Chrom und Vanadium — verschieden vom «klassischen» Grün des Turmalins.",
                origineCouleur = "Kenia (Taita-Taveta), Tansania (Umba). Intensives, dem Smaragd nahekommendes Grün; ausgeprägter Dichroismus, wobei die Ausrichtung der Tafel entscheidend ist, um den gesättigtsten Farbton hervorzubringen.",
                puretTraitements = "Selten behandelt, da die durch Chrom und Vanadium verursachte Farbe von Natur aus intensiv ist. Reinheit meist gut.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Grüner Turmalin (Verdelith)",
                accroche = "Ein durch Eisen oder Chrom gefärbtes Grün — die klassischste und verbreitetste Farbe des Turmalins.",
                origineCouleur = "Brasilien (Minas Gerais), Mosambik (Alto Ligonha), Nigeria (Oyo). Kräftiges bis dunkles Grün; starke Doppelbrechung, weshalb Schleifer den Stein ausrichten, um die Farbe zu optimieren.",
                puretTraitements = "Gelegentliches, akzeptiertes Erhitzen, um zu dunkle Töne aufzuhellen. Reinheit je nach Herkunft unterschiedlich.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte, regelmäßige Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Blauer Turmalin (Indigolith)",
                accroche = "Ein tiefes Blau, das sich ohne Kupferbeteiligung vom kupferhaltigen Blaugrün der Paraíba unterscheidet.",
                origineCouleur = "Brasilien (Minas Gerais), Namibia (Erongo), Afghanistan (Nuristan). Dunkles Blau bis Blaugrau, meist dunkler als bei der Paraíba.",
                puretTraitements = "Gelegentliches Erhitzen, um einen zu dunklen Ton aufzuhellen — eine akzeptierte Behandlung. Reinheit unterschiedlich.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Rosa Turmalin",
                accroche = "Ein zarter, durch Mangan verursachter Farbton — Ursprung der begehrtesten zweifarbigen Turmaline.",
                origineCouleur = "Brasilien (Mine Cruzeiro, Minas Gerais), Afghanistan (Paprok), Mosambik (Alto Ligonha). Helles bis kräftiges Rosa; die Kristalle sind oft zoniert und Ursprung zweifarbiger Varietäten wie der Wassermelonenturmalin.",
                puretTraitements = "Selten behandelt. Reinheit meist gut mit bloßem Auge.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte Reinigung wird empfohlen."
            ),
            BuyingGuideArticle(
                pierre = "Schwarzer Turmalin (Schörl)",
                accroche = "Die häufigste Turmalinvarietät, geschätzt sowohl für schlichten Schmuck als auch in der Lithotherapie.",
                origineCouleur = "Brasilien (Minas Gerais), Namibia (Erongo), Madagaskar (Antsirabe). Opakes bis leicht durchscheinendes Schwarz; oft gestreifte prismatische Kristalle, mitunter von beachtlicher Größe.",
                puretTraitements = "Niemals behandelt, da Farbe und Häufigkeit keinerlei Eingriff rechtfertigen. Sehr erschwinglicher Preis.",
                entretien = "Härte 7 bis 7,5, robust. Einfache Pflege, ohne besondere Vorsichtsmaßnahmen."
            ),
            BuyingGuideArticle(
                pierre = "Wassermelonenturmalin",
                accroche = "Ein rosafarbener Kern, umgeben von Grün, wie eine Fruchtscheibe — einer der spektakulärsten zweifarbigen Turmaline.",
                origineCouleur = "Brasilien (Minas Gerais), USA (Mine Dunton, Maine). Klare Zonierung, rosa im Zentrum, grün am Rand; meist in Querscheiben geschliffen, um den «Wassermelonen»-Effekt sichtbar zu machen.",
                puretTraitements = "Selten behandelt, da die natürliche Zonierung den Hauptreiz des Steins ausmacht. Reinheit je nach Zone unterschiedlich.",
                entretien = "Härte 7 bis 7,5. Eine sanfte Reinigung wird empfohlen, besonders bei dünnen Scheiben."
            ),
            BuyingGuideArticle(
                pierre = "Paraíba-Turmalin",
                accroche = "Ein elektrisches «Neon»-Blaugrün, einzigartig im Mineralreich — der begehrteste und teuerste aller Turmaline.",
                origineCouleur = "São José da Batalha (Paraíba, Brasilien), die historische, nahezu erschöpfte Quelle. Mosambik (Mavuco) und Nigeria (Edeko): erschwinglichere moderne Quellen. Die «Neon»-Farbe wird durch Kupfer verursacht und ist bei keiner anderen Turmalinart zu beobachten.",
                puretTraitements = "Ein Zertifikat, das die Herkunft (Brasilien oder Afrika) angibt, ist unerlässlich, da der Wertunterschied beträchtlich ist. Reinheit und Intensität der kupferbedingten Farbe zählen mehr als jedes andere Kriterium.",
                entretien = "Härte 7 bis 7,5. Ein pyroelektrischer Stein: eine sanfte Reinigung wird empfohlen, zu schützen wie jeder Stein von sehr hohem Wert."
            ),
            BuyingGuideArticle(
                pierre = "Pyrop-Granat",
                accroche = "Der klassische blutrote Granat, ohne rhodolithische Beimischung — die reinste Varietät der Pyrop-Familie.",
                origineCouleur = "Tschechien (Böhmen, Region Podsedice), Südafrika (Kao-Mine, Kimberlite), Tansania (Umba). Tiefes, gleichmäßiges Blutrot, ohne nennenswerten violetten Unterton.",
                puretTraitements = "Wie die gesamte Granatfamilie niemals behandelt. Im Gegensatz zum Rhodolith enthält der reine Pyrop keinen nennenswerten Eisenanteil.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig, über die üblichen Vorsichtsmaßnahmen für jeden Schmuckstein hinaus."
            ),
            BuyingGuideArticle(
                pierre = "Almandin-/Pyrop-Granat",
                accroche = "Ein tiefroter, niemals behandelter Granat — der häufigste und erschwinglichste der Familie.",
                origineCouleur = "Indien (Rajmahal Hills, Jharkhand), Sri Lanka (Ratnapura), Tschechien (Podsedice), Tansania (Umba-Tal). Dunkles Rot bis Rotbraun, gelegentlich leicht violett.",
                puretTraitements = "Weder thermisch noch chemisch behandelt. Keine Spaltbarkeit, starke Lichtbrechung und guter Glanz.",
                entretien = "Härte 7 bis 7,5, insgesamt robust. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Rhodolith-Granat",
                accroche = "Ein himbeerfarbenes Rotviolett mit ausgezeichneter Transparenz — einer der elegantesten Granate.",
                origineCouleur = "Tansania (Umba-Tal), Sri Lanka (Ratnapura), Indien (Orissa). Leuchtendes, gleichmäßiges Rotviolett.",
                puretTraitements = "Niemals behandelt. Ausgezeichnete natürliche Transparenz, selten mit bloßem Auge eingeschlossen.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Sterngranat",
                accroche = "Ein Almandin mit vier oder sechs leuchtenden Strahlen — eine Rarität, die fast ausschließlich in Idaho gefunden wird.",
                origineCouleur = "USA (Idaho, weltweit einzige Lagerstätte für vierstrahligen Asterismus), Indien (Odisha). Dunkles Rot; der vierstrahlige Asterismus ist weltweit einzigartig, der noch seltenere sechsstrahlige stammt aus denselben Lagerstätten.",
                puretTraitements = "Niemals behandelt. Schärfe und Zentrierung des Sterns zählen mehr als die Transparenz des Untergrunds.",
                entretien = "Härte 7 bis 7,5. Eine sanfte Reinigung wird empfohlen, um die Politur der Wölbung zu erhalten."
            ),
            BuyingGuideArticle(
                pierre = "Malaya-Granat",
                accroche = "Ein warmes Rosaorange, entstanden aus der Mischung zweier Granatarten, völlig unbehandelt.",
                origineCouleur = "Tansania (Umba), Kenia (Taita-Taveta), Madagaskar (Anjanabonoina). Rosaorange bis warmes Orange — ein unter den Granaten einzigartiger Farbton.",
                puretTraitements = "Keine Behandlung ist nötig oder üblich: zu 100 % natürliche Farbe — eines der großen Verkaufsargumente dieser Varietät.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Spessartin-Granat",
                accroche = "Ein leuchtendes «Mandarin»-Orange — eine der strahlendsten Farben im Mineralreich.",
                origineCouleur = "Namibia (Region Kunene), Nigeria (Bundesstaat Nasarawa), Madagaskar (Fianarantsoa). Kräftiges, gesättigtes Orange, das mitunter ins Rotorange spielt.",
                puretTraitements = "Natürliche Farbe, niemals thermisch behandelt. Reinheit meist gut bis ausgezeichnet.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Grossular-Granat",
                accroche = "Der grüne bis gelbgrüne Granat, ein wenig gefärbter Verwandter von Tsavorit und Hessonit — die farblich vielfältigste Familie.",
                origineCouleur = "Mali (Sandaré), Kenia (Voi), Kanada (Québec, Jeffrey Mine). Von farblos bis tiefgrün, über Gelbgrün bis zum Orangebraun des Hessonits.",
                puretTraitements = "Niemals behandelt. Reinheit je nach Farbton und Lagerstätte unterschiedlich.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Violetter Rhodolith-Granat",
                accroche = "Ein purpurner Farbton, der je nach Beleuchtung wechselt — eine violette Variante des klassischen Rhodoliths.",
                origineCouleur = "Tansania (Umba-Tal), Mosambik (Cuamba). Purpur bis kräftiges Violett, wobei der dominierende Farbton je nach Lichtquelle variiert.",
                puretTraitements = "Stabile Farbe, niemals behandelt. Reinheit meist gut.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Farbwechsel-Granat",
                accroche = "Bei Tag bläulich grün, am Abend rotviolett — ein wenig bekannter Rivale des Alexandrits, zu einem weit erschwinglicheren Preis.",
                origineCouleur = "Tansania (Umba, Tunduru), Madagaskar (Bekily). Anders als beim Alexandrit ist bei diesem Granat nicht Chrom, sondern Vanadium für den Farbwechsel verantwortlich.",
                puretTraitements = "Niemals behandelt. Wie beim Alexandrit zählt die Stärke des Farbwechsels mehr als der Farbton selbst.",
                entretien = "Härte 7 bis 7,5. Keine besondere Pflege nötig."
            ),
            BuyingGuideArticle(
                pierre = "Blauer Topas",
                accroche = "Das tiefe Blau, das durch Bestrahlung und anschließendes Erhitzen eines farblosen Topases entsteht — die meistvermarktete Farbe dieser Art.",
                origineCouleur = "Brasilien (Minas Gerais) und Nigeria liefern das farblose Rohmaterial, das bestrahlt und anschließend erhitzt wird, um ein Blau von Himmelblau bis zum tiefen «London Blue» zu entwickeln.",
                puretTraitements = "Die Farbe entsteht nahezu immer durch Bestrahlung und anschließende Wärmebehandlung, stabil und dauerhaft — die Offenlegung ist Pflicht, wird vom Markt aber dank des erschwinglichen Preises weithin akzeptiert.",
                entretien = "Härte 8, doch eine perfekte Spaltbarkeit in eine Richtung macht ihn stoßempfindlich. Plötzliche Temperaturwechsel vermeiden, Ultraschallreiniger meiden."
            ),
            BuyingGuideArticle(
                pierre = "Rosa Topas",
                accroche = "Das seltenste natürliche Rosa des Topases, historisch in Pakistan gefördert.",
                origineCouleur = "Pakistan (Katlang, Mardan), Russland (Ural, historisch). Zartes bis kräftiges Rosa; das natürliche, unbehandelte Rosa ist selten und vom durch Erhitzen brauner Steine erzeugten rosa Topas zu unterscheiden.",
                puretTraitements = "Auf dem Zertifikat prüfen, ob der Farbton natürlich ist oder durch Wärmebehandlung von braunem Material erzielt wurde, da der Wertunterschied erheblich ist. Von Natur aus hohe Reinheit.",
                entretien = "Härte 8, perfekte Spaltbarkeit in eine Richtung: Stöße und plötzliche Temperaturwechsel vermeiden, Ultraschallreiniger meiden."
            ),
            BuyingGuideArticle(
                pierre = "Weißer Topas",
                accroche = "Der Topas in seiner reinsten und häufigsten Form, farblos und klar — eine preiswerte Alternative zum Diamanten.",
                origineCouleur = "Brasilien (Minas Gerais, Ouro Preto), Nigeria (Jos-Plateau), Pakistan (Katlang). Farblos und vollkommen klar; er ist zugleich das häufigste Rohmaterial für die Produktion von behandeltem blauem Topas.",
                puretTraitements = "In der Regel selbst unbehandelt. Sehr hohe Reinheit ist gefragt, da seine Transparenz sein wichtigster Vorzug ist.",
                entretien = "Härte 8, perfekte Spaltbarkeit in eine Richtung: Stöße vermeiden und Ultraschallreiniger meiden."
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
                pierre = "Safira amarela",
                accroche = "O corindo em todas as tonalidades exceto o vermelho encontra no amarelo uma das suas variedades mais luminosas e acessíveis.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagáscar (Ilakaka), Tanzânia (Tunduru). Amarelo vivo e luminoso, homogéneo, sem reflexo esverdeado.",
                puretTraitements = "Aquecimento comum para intensificar e uniformizar a cor, tratamento aceite e estável. Pureza geralmente boa a olho nu.",
                entretien = "Dureza 9, muito resistente ao uso diário. Ultrassons sem risco numa pedra não difundida."
            ),
            BuyingGuideArticle(
                pierre = "Safira verde",
                accroche = "Um verde discreto e frequentemente dicroico, a cor mais acessível do corindo.",
                origineCouleur = "Austrália (Nova Gales do Sul), Tailândia (Kanchanaburi), Nigéria (Mambilla). Verde franco, sem excesso de cinzento; o dicroísmo verde/amarelo-esverdeado consoante o ângulo é característico da espécie.",
                puretTraitements = "Aquecimento comum e aceite para homogeneizar o tom. Pureza geralmente boa.",
                entretien = "Dureza 9, muito resistente. Ultrassons sem risco numa pedra não difundida."
            ),
            BuyingGuideArticle(
                pierre = "Safira azul",
                accroche = "O azul de referência da alta joalharia, logo atrás do diamante em dureza.",
                origineCouleur = "Caxemira: azul aveludado lendário, jazida quase esgotada, raríssima. Birmânia (Mogok): azul «royal» intenso. Sri Lanka: azul mais claro, grande transparência. Madagáscar: principal produção moderna. Azul aveludado profundo, forte saturação sem excesso de negro.",
                puretTraitements = "Aquecimento quase sistemático e amplamente aceite. A difusão (titânio clássico, ou berílio, mais problemática) deve ser distinguida no certificado. Inclusões de rutilo em seda podem produzir um asterismo (safira estrelada).",
                entretien = "Dureza 9, muito resistente ao uso diário. Ultrassons sem risco, exceto numa pedra difundida ou preenchida."
            ),
            BuyingGuideArticle(
                pierre = "Safira de Auvérnia",
                accroche = "Um corindo francês de tom azul-violeta profundo, extraído dos terrenos vulcânicos de Auvérnia.",
                origineCouleur = "França (Espaly-Saint-Marcel, Haute-Loire), única fonte. Azul-violeta muito intenso, típico das safiras de origem basáltica; cristais geralmente pequenos, raramente com mais de um quilate lapidado.",
                puretTraitements = "Pouco ou nada aquecida, a raridade e a origem local prevalecendo sobre a intensificação da cor. Pequena produção, a negociar peça por peça.",
                entretien = "Dureza 9, muito resistente. Ultrassons sem risco."
            ),
            BuyingGuideArticle(
                pierre = "Safira violeta",
                accroche = "Um violeta profundo, na fronteira entre a safira azul e o rubi.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagáscar (Ilakaka), Tanzânia (Tunduru). Violeta intenso e homogéneo; pode apresentar uma ligeira mudança de cor entre a luz do dia e a luz incandescente.",
                puretTraitements = "Aquecimento comum e aceite para estabilizar o tom. Pureza geralmente boa a olho nu.",
                entretien = "Dureza 9, muito resistente. Ultrassons sem risco numa pedra não difundida."
            ),
            BuyingGuideArticle(
                pierre = "Safira rosa",
                accroche = "Na fronteira entre rubi e safira, cujo limite oficial continua a ser debatido consoante os laboratórios.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagáscar (Ilakaka), Tanzânia (Tunduru). Rosa vivo e intenso; para além de um determinado limiar de saturação, a pedra é reclassificada como rubi por alguns laboratórios.",
                puretTraitements = "Aquecimento comum e aceite. Verificar a classificação (safira rosa ou rubi) no certificado, uma vez que o valor pode variar fortemente.",
                entretien = "Dureza 9, muito resistente. Ultrassons sem risco numa pedra não difundida."
            ),
            BuyingGuideArticle(
                pierre = "Safira branca",
                accroche = "O corindo sem qualquer vestígio corante, alternativa discreta e acessível ao diamante.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagáscar (Ilakaka), Austrália (Nova Gales do Sul). Incolor e límpida; dispersão inferior à do diamante, mas dureza quase igual.",
                puretTraitements = "Raramente tratada, sendo a sua transparência natural já o seu principal trunfo. Procura-se uma pureza elevada.",
                entretien = "Dureza 9, excelente resistência ao desgaste diário. Ultrassons sem risco."
            ),
            BuyingGuideArticle(
                pierre = "Safira estrelada",
                accroche = "Uma estrela luminosa de seis pontas, revelada por uma lapidação em cabochão, frequentemente numa safira azul ou negra.",
                origineCouleur = "Sri Lanka (Ratnapura), Birmânia (Mogok), Tailândia (Kanchanaburi, variedade negra). O asterismo mais procurado apresenta seis pontas perfeitamente retas, centradas na cúpula do cabochão.",
                puretTraitements = "Raramente aquecida, um tratamento térmico correndo o risco de alterar as inclusões de rutilo responsáveis pelo asterismo. A nitidez e a centragem da estrela prevalecem sobre a transparência.",
                entretien = "Dureza 9, muito resistente. Recomenda-se uma limpeza suave para preservar o polimento da cúpula."
            ),
            BuyingGuideArticle(
                pierre = "Safira teal",
                accroche = "Um corindo bicolor em que azul e amarelo se combinam num tom azul-esverdeado singular, muito apreciado pelos conhecedores.",
                origineCouleur = "Estados Unidos (Montana, Rock Creek), Austrália (Nova Gales do Sul), Madagáscar (Ilakaka). A bicoloração azul-amarelo, visível em zonas distintas em bruto, funde-se num azul-esverdeado homogéneo depois de a pedra ser lapidada e orientada.",
                puretTraitements = "Frequentemente não aquecida, sendo o próprio matiz natural procurado pelos colecionadores. O talento do lapidador, que orienta a pedra para equilibrar as duas cores, é determinante.",
                entretien = "Dureza 9, muito resistente. Ultrassons sem risco numa pedra não difundida."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina rubelite",
                accroche = "Um vermelho intenso devido ao manganês, a variedade de turmalina mais próxima do rubi.",
                origineCouleur = "Brasil (mina do Cruzeiro, Minas Gerais), Nigéria (Oyo), Moçambique (Alto Ligonha), Afeganistão (vale de Paprok). Vermelho a vermelho-rosado intenso e homogéneo; pleocroísmo marcado, cristais prismáticos estriados característicos.",
                puretTraitements = "Aquecimento ocasional para atenuar os tons acastanhados, tratamento aceite. Pureza variável; inclusões filiformes frequentes e toleradas se discretas.",
                entretien = "Dureza de 7 a 7,5, boa robustez geral. Pedra piroelétrica que atrai a poeira por eletricidade estática: recomenda-se uma limpeza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina amarela",
                accroche = "Um amarelo canário luminoso, natural e sem tratamento — a mais rara das cores da turmalina.",
                origineCouleur = "Maláui (Zomba), Brasil (Minas Gerais), Nigéria (Oyo). Amarelo vivo e luminoso, sem necessidade de intervenção ao contrário de muitos tons comerciais da turmalina.",
                puretTraitements = "Geralmente não tratada, sendo a sua cor natural já procurada. Pureza frequentemente boa.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave e regular."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina crómica",
                accroche = "Um verde profundo e saturado devido ao crómio e ao vanádio, distinto do verde «clássico» da turmalina.",
                origineCouleur = "Quénia (Taita-Taveta), Tanzânia (Umba). Verde intenso próximo da esmeralda; dicroísmo marcado, a orientação da mesa sendo determinante para revelar o tom mais saturado.",
                puretTraitements = "Raramente tratada, sendo a cor devida ao crómio e ao vanádio naturalmente intensa. Pureza geralmente boa.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina verde (Verdelite)",
                accroche = "Um verde colorido pelo ferro ou pelo crómio, a cor mais clássica e difundida da turmalina.",
                origineCouleur = "Brasil (Minas Gerais), Moçambique (Alto Ligonha), Nigéria (Oyo). Verde franco a verde escuro; forte birrefringência, os lapidadores orientando a pedra para otimizar a cor.",
                puretTraitements = "Aquecimento ocasional aceite para clarear os tons demasiado escuros. Pureza variável consoante a origem.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave e regular."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina azul (indicolite)",
                accroche = "Um azul profundo, distinto do azul-esverdeado cuprífero da Paraíba, sem qualquer intervenção do cobre.",
                origineCouleur = "Brasil (Minas Gerais), Namíbia (Erongo), Afeganistão (Nurestão). Azul escuro a azul-acinzentado, geralmente mais escuro do que a Paraíba.",
                puretTraitements = "Aquecimento ocasional para clarear um tom demasiado escuro, tratamento aceite. Pureza variável.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina rosa",
                accroche = "Um tom suave devido ao manganês, na origem das turmalinas bicolores mais procuradas.",
                origineCouleur = "Brasil (mina do Cruzeiro, Minas Gerais), Afeganistão (Paprok), Moçambique (Alto Ligonha). Rosa claro a rosa intenso; cristais frequentemente zonados, na origem de variedades bicolores como a melancia.",
                puretTraitements = "Raramente tratada. Pureza geralmente boa a olho nu.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina negra (Xorlo)",
                accroche = "A variedade de turmalina mais comum, apreciada tanto na joalharia sóbria como na litoterapia.",
                origineCouleur = "Brasil (Minas Gerais), Namíbia (Erongo), Madagáscar (Antsirabe). Negro opaco a subtransparente; cristais prismáticos frequentemente estriados, por vezes de grande dimensão.",
                puretTraitements = "Nunca tratada, a sua cor e abundância não justificando qualquer intervenção. Preço muito acessível.",
                entretien = "Dureza de 7 a 7,5, robusta. Cuidado simples, sem precauções especiais."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina melancia",
                accroche = "Um coração rosa rodeado de verde, como uma fatia de fruta — uma das turmalinas bicolores mais espetaculares.",
                origineCouleur = "Brasil (Minas Gerais), Estados Unidos (mina Dunton, Maine). Zonagem nítida, rosa no centro e verde na periferia; lapidada sobretudo em fatias transversais para revelar o efeito «melancia».",
                puretTraitements = "Raramente tratada, sendo a zonagem natural o principal atrativo da pedra. Pureza variável consoante a zona.",
                entretien = "Dureza de 7 a 7,5. Recomenda-se uma limpeza suave, em particular para as fatias finas."
            ),
            BuyingGuideArticle(
                pierre = "Turmalina Paraíba",
                accroche = "Um azul-esverdeado «néon» elétrico, único no reino mineral — a turmalina mais procurada e mais dispendiosa.",
                origineCouleur = "São José da Batalha (Paraíba, Brasil), fonte histórica quase esgotada. Moçambique (Mavuco) e Nigéria (Edeko): fontes modernas mais acessíveis. Cor «néon» devida ao cobre, nunca observada nas restantes turmalinas.",
                puretTraitements = "Um certificado que precise a origem (Brasil vs. África) é essencial, sendo a diferença de valor considerável. A pureza e a intensidade da cor cuprífera prevalecem sobre qualquer outro critério.",
                entretien = "Dureza de 7 a 7,5. Pedra piroelétrica: recomenda-se uma limpeza suave, a proteger como qualquer pedra de valor muito elevado."
            ),
            BuyingGuideArticle(
                pierre = "Granada piropo",
                accroche = "A granada vermelho-sangue clássica, sem diluição rodolita — a variedade mais pura da família piropo.",
                origineCouleur = "República Checa (Boémia, região de Podsedice), África do Sul (mina de Kao, kimberlitos), Tanzânia (Umba). Vermelho-sangue profundo e homogéneo, sem tonalidade violácea notável.",
                puretTraitements = "Nunca tratada, tal como toda a família das granadas. Ao contrário da rodolita, o piropo puro não contém ferro em proporção significativa.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial para além das precauções habituais para qualquer pedra de joalharia."
            ),
            BuyingGuideArticle(
                pierre = "Granada almandina / piropo",
                accroche = "Uma granada vermelho profundo, nunca tratada — a mais comum e mais acessível da família.",
                origineCouleur = "Índia (Colinas de Rajmahal, Jharkhand), Sri Lanka (Ratnapura), República Checa (Podsedice), Tanzânia (vale do Umba). Vermelho escuro a vermelho-acastanhado, por vezes ligeiramente violáceo.",
                puretTraitements = "Nunca tratada térmica ou quimicamente. Sem clivagem, forte refração e bom brilho.",
                entretien = "Dureza de 7 a 7,5, boa robustez geral. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada rodolita",
                accroche = "Um vermelho-violáceo «framboesa» de excelente transparência, entre as granadas mais elegantes.",
                origineCouleur = "Tanzânia (vale do Umba), Sri Lanka (Ratnapura), Índia (Orissa). Vermelho-violáceo luminoso e homogéneo.",
                puretTraitements = "Nunca tratada. Excelente transparência natural, raramente incluída a olho nu.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada estrelada",
                accroche = "Uma almandina de quatro ou seis pontas luminosas, uma raridade encontrada quase exclusivamente no Idaho.",
                origineCouleur = "Estados Unidos (Idaho, única jazida mundial de asterismo a quatro pontas), Índia (Odisha). Vermelho escuro; o asterismo de quatro pontas é único no mundo, o de seis pontas, ainda mais raro, provém das mesmas jazidas.",
                puretTraitements = "Nunca tratada. A nitidez e a centragem da estrela prevalecem sobre a transparência do fundo.",
                entretien = "Dureza de 7 a 7,5. Recomenda-se uma limpeza suave para preservar o polimento da cúpula."
            ),
            BuyingGuideArticle(
                pierre = "Granada malaia",
                accroche = "Um rosa-alaranjado caloroso nascido da mistura de duas granadas, sem qualquer tratamento.",
                origineCouleur = "Tanzânia (Umba), Quénia (Taita-Taveta), Madagáscar (Anjanabonoina). Rosa-alaranjado a alaranjado quente, tom único entre as granadas.",
                puretTraitements = "Nenhum tratamento é necessário nem praticado: cor 100% natural, um dos grandes argumentos de venda desta variedade.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada espessartite",
                accroche = "Um laranja «mandarina» radiante, entre as cores mais luminosas do reino mineral.",
                origineCouleur = "Namíbia (região do Cunene), Nigéria (Estado de Nasarawa), Madagáscar (Fianarantsoa). Laranja vivo e saturado, por vezes tendendo para o vermelho-alaranjado.",
                puretTraitements = "Cor natural, nunca tratada termicamente. Pureza geralmente boa a excelente.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada grossulária",
                accroche = "A granada verde a amarelo-esverdeado, prima pouco colorida da tsavorite e da hessonite — a família mais diversificada em cores.",
                origineCouleur = "Mali (Sandaré), Quénia (Voi), Canadá (Quebeque, Mina Jeffrey). De incolor a verde profundo, passando pelo amarelo-esverdeado e o castanho-alaranjado da hessonite.",
                puretTraitements = "Nunca tratada. Pureza variável consoante o tom e a jazida.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada rodolita violácea",
                accroche = "Um tom púrpura que muda consoante a iluminação, variante violeta da rodolita clássica.",
                origineCouleur = "Tanzânia (vale do Umba), Moçambique (Cuamba). Púrpura a violeta intenso, o matiz dominante variando consoante a fonte de luz.",
                puretTraitements = "Cor estável, nunca tratada. Pureza geralmente boa.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Granada com mudança de cor",
                accroche = "Verde-azulado de dia, vermelho-violeta à noite — um rival pouco conhecido da alexandrita, a um preço bem mais acessível.",
                origineCouleur = "Tanzânia (Umba, Tunduru), Madagáscar (Bekily). O vanádio, e não o crómio, é responsável pela mudança de cor nesta granada, ao contrário da alexandrita.",
                puretTraitements = "Nunca tratada. A intensidade da mudança de cor prevalece sobre o próprio tom, tal como na alexandrita.",
                entretien = "Dureza de 7 a 7,5. Nenhum cuidado especial."
            ),
            BuyingGuideArticle(
                pierre = "Topázio azul",
                accroche = "O azul profundo obtido por irradiação seguida de aquecimento de um topázio incolor — a cor mais comercializada da espécie.",
                origineCouleur = "Brasil (Minas Gerais) e Nigéria fornecem a matéria-prima incolor, irradiada e depois aquecida para desenvolver um azul que vai do céu ao «London Blue» profundo.",
                puretTraitements = "Cor quase sistematicamente obtida por irradiação seguida de tratamento térmico, estável e permanente — divulgação obrigatória, amplamente aceite pelo mercado devido ao seu preço acessível.",
                entretien = "Dureza 8, mas uma clivagem perfeita numa direção torna-o frágil a impactos. Evitar variações térmicas bruscas e proibir os ultrassons."
            ),
            BuyingGuideArticle(
                pierre = "Topázio rosa",
                accroche = "O rosa natural mais raro do topázio, historicamente extraído no Paquistão.",
                origineCouleur = "Paquistão (Katlang, Mardan), Rússia (Urais, histórico). Rosa delicado a intenso; o rosa natural não tratado é raro, a distinguir do topázio rosa obtido por aquecimento de pedras castanhas.",
                puretTraitements = "Verificar no certificado se o tom é natural ou obtido por tratamento térmico de matéria castanha, sendo a diferença de valor importante. Pureza naturalmente elevada.",
                entretien = "Dureza 8, clivagem perfeita numa direção: evitar impactos e variações térmicas bruscas, proibir os ultrassons."
            ),
            BuyingGuideArticle(
                pierre = "Topázio branco",
                accroche = "O topázio na sua forma mais pura e mais comum, incolor e límpido — uma alternativa económica ao diamante.",
                origineCouleur = "Brasil (Minas Gerais, Ouro Preto), Nigéria (Planalto de Jos), Paquistão (Katlang). Incolor e perfeitamente límpido; é também a matéria-prima mais abundante para a produção de topázio azul tratado.",
                puretTraitements = "Geralmente não tratado ele próprio. Procura-se uma pureza muito elevada, sendo a sua transparência o seu principal trunfo.",
                entretien = "Dureza 8, clivagem perfeita numa direção: evitar impactos e proibir os ultrassons."
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
                pierre = "Сапфир жёлтый",
                accroche = "Корунд во всех оттенках, кроме красного, обретает в жёлтом одну из самых светлых и доступных своих разновидностей.",
                origineCouleur = "Шри-Ланка (Ратнапура), Мадагаскар (Илакака), Танзания (Тундуру). Ярко-жёлтый, светлый и однородный цвет, без зеленоватого отлива.",
                puretTraitements = "Термообработка распространена для усиления и выравнивания цвета — принятая и стабильная обработка. Чистота обычно хороша невооружённым глазом.",
                entretien = "Твёрдость 9, очень устойчив к повседневному износу. Ультразвуковая чистка безопасна для камня без диффузии."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир зелёный",
                accroche = "Сдержанный, часто дихроичный зелёный цвет — самая доступная окраска корунда.",
                origineCouleur = "Австралия (Новый Южный Уэльс), Таиланд (Канчанабури), Нигерия (Мамбилла). Чистый зелёный цвет без избытка серого; дихроизм зелёный/жёлто-зелёный в зависимости от угла характерен для этого вида.",
                puretTraitements = "Термообработка распространена и принята для выравнивания оттенка. Чистота обычно хороша.",
                entretien = "Твёрдость 9, очень устойчив. Ультразвуковая чистка безопасна для камня без диффузии."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир синий",
                accroche = "Эталонный синий цвет высокой ювелирной моды — по твёрдости уступает только алмазу.",
                origineCouleur = "Кашмир: легендарный бархатистый синий цвет, почти исчерпанное, чрезвычайно редкое месторождение. Мьянма (Могок): интенсивный «королевский» синий. Шри-Ланка: более светлый синий, высокая прозрачность. Мадагаскар: основная современная добыча. Ценится глубокий бархатистый синий с высокой насыщенностью без избытка чёрного.",
                puretTraitements = "Термообработка почти повсеместна и широко принята. Диффузия (классическая титановая или более проблематичная бериллиевая) должна быть указана отдельно в сертификате. Шёлковистые включения рутила могут вызывать астеризм (звёздчатый сапфир).",
                entretien = "Твёрдость 9, очень устойчив к повседневному износу. Ультразвуковая чистка безопасна, кроме камней с диффузией или заполнением."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир из Оверни",
                accroche = "Французский корунд глубокого сине-фиолетового оттенка, добываемый в вулканических породах Оверни.",
                origineCouleur = "Франция (Эспали-Сен-Марсель, департамент Верхняя Луара) — единственный источник. Насыщенный сине-фиолетовый цвет, типичный для сапфиров базальтового происхождения; кристаллы обычно небольшие, огранённые камни редко превышают один карат.",
                puretTraitements = "Мало или совсем не подвергается термообработке — редкость и местное происхождение важнее усиления цвета. Добыча невелика, камни оговариваются поштучно.",
                entretien = "Твёрдость 9, очень устойчив. Ультразвуковая чистка безопасна."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир фиолетовый",
                accroche = "Глубокий фиолетовый цвет — на границе между синим сапфиром и рубином.",
                origineCouleur = "Шри-Ланка (Ратнапура), Мадагаскар (Илакака), Танзания (Тундуру). Насыщенный однородный фиолетовый цвет; может демонстрировать лёгкую смену оттенка между дневным светом и лампами накаливания.",
                puretTraitements = "Термообработка распространена и принята для стабилизации оттенка. Чистота обычно хороша невооружённым глазом.",
                entretien = "Твёрдость 9, очень устойчив. Ультразвуковая чистка безопасна для камня без диффузии."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир розовый",
                accroche = "На границе между рубином и сапфиром — официальная граница между ними остаётся предметом споров между лабораториями.",
                origineCouleur = "Шри-Ланка (Ратнапура), Мадагаскар (Илакака), Танзания (Тундуру). Яркий насыщенный розовый цвет; при превышении определённого порога насыщенности некоторые лаборатории переклассифицируют камень в рубин.",
                puretTraitements = "Термообработка распространена и принята. Классификацию (розовый сапфир или рубин) следует проверять по сертификату, поскольку стоимость может сильно различаться.",
                entretien = "Твёрдость 9, очень устойчив. Ультразвуковая чистка безопасна для камня без диффузии."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир белый",
                accroche = "Корунд без следов окрашивающих элементов — сдержанная и доступная альтернатива алмазу.",
                origineCouleur = "Шри-Ланка (Ратнапура), Мадагаскар (Илакака), Австралия (Новый Южный Уэльс). Бесцветный и прозрачный; дисперсия ниже, чем у алмаза, но твёрдость почти такая же.",
                puretTraitements = "Редко обрабатывается, поскольку его природная прозрачность уже является главным достоинством. Ценится высокая чистота.",
                entretien = "Твёрдость 9, отличная устойчивость к повседневному износу. Ультразвуковая чистка безопасна."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир звёздчатый",
                accroche = "Светящаяся шестилучевая звезда, проявляющаяся при огранке кабошоном, чаще всего в синем или чёрном сапфире.",
                origineCouleur = "Шри-Ланка (Ратнапура), Мьянма (Могок), Таиланд (Канчанабури, чёрная разновидность). Наиболее ценится астеризм с шестью идеально прямыми лучами, центрированными на куполе кабошона.",
                puretTraitements = "Редко подвергается термообработке, поскольку она может повредить включения рутила, ответственные за астеризм. Чёткость и центрирование звезды важнее прозрачности.",
                entretien = "Твёрдость 9, очень устойчив. Рекомендуется бережная чистка для сохранения полировки купола."
            ),
            BuyingGuideArticle(
                pierre = "Сапфир тил",
                accroche = "Двухцветный корунд, в котором синий и жёлтый сочетаются в своеобразный сине-зелёный оттенок, высоко ценимый знатоками.",
                origineCouleur = "США (Монтана, Рок-Крик), Австралия (Новый Южный Уэльс), Мадагаскар (Илакака). Сине-жёлтая двухцветность, заметная отдельными зонами в необработанном камне, сливается в однородный сине-зелёный оттенок после огранки и правильной ориентации камня.",
                puretTraitements = "Часто не подвергается термообработке, поскольку сам природный оттенок ценится коллекционерами. Решающую роль играет мастерство огранщика, ориентирующего камень для баланса двух цветов.",
                entretien = "Твёрдость 9, очень устойчив. Ультразвуковая чистка безопасна для камня без диффузии."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин рубеллит",
                accroche = "Насыщенный красный цвет, обязанный марганцу, — разновидность турмалина, ближе всего стоящая к рубину.",
                origineCouleur = "Бразилия (рудник Крузейру, штат Минас-Жерайс), Нигерия (Ойо), Мозамбик (Алту-Лигонья), Афганистан (долина Папрок). Интенсивный и однородный красный до красно-розового цвет; выраженный плеохроизм, характерные штриховатые призматические кристаллы.",
                puretTraitements = "Иногда подвергается термообработке для смягчения коричневатых тонов — принятая обработка. Чистота варьируется; игольчатые включения часты и допустимы, если незаметны.",
                entretien = "Твёрдость 7–7,5, в целом хорошая прочность. Пироэлектрический камень, притягивающий пыль статическим электричеством: рекомендуется бережная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин жёлтый",
                accroche = "Светлый канареечно-жёлтый цвет, природный, без обработки, — самая редкая окраска турмалина.",
                origineCouleur = "Малави (Зомба), Бразилия (штат Минас-Жерайс), Нигерия (Ойо). Яркий светлый жёлтый цвет, не требующий вмешательства, в отличие от многих коммерческих оттенков турмалина.",
                puretTraitements = "Обычно не обрабатывается, поскольку природный цвет уже востребован. Чистота часто хороша.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная регулярная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин хромистый",
                accroche = "Глубокий насыщенный зелёный цвет, обязанный хрому и ванадию, — отличается от «классического» зелёного турмалина.",
                origineCouleur = "Кения (Тайта-Тавета), Танзания (Умба). Насыщенный зелёный цвет, близкий к изумрудному; выраженный дихроизм, ориентация площадки имеет решающее значение для раскрытия наиболее насыщенного оттенка.",
                puretTraitements = "Редко обрабатывается, поскольку цвет, обязанный хрому и ванадию, от природы интенсивен. Чистота обычно хороша.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин зелёный (верделит)",
                accroche = "Зелёный цвет, обусловленный железом или хромом, — самая классическая и распространённая окраска турмалина.",
                origineCouleur = "Бразилия (штат Минас-Жерайс), Мозамбик (Алту-Лигонья), Нигерия (Ойо). От чистого до тёмно-зелёного цвета; сильное двупреломление — огранщики ориентируют камень для оптимизации цвета.",
                puretTraitements = "Иногда допускается термообработка для осветления слишком тёмных оттенков. Чистота варьируется в зависимости от происхождения.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная регулярная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин синий (индиголит)",
                accroche = "Глубокий синий цвет, отличный от медьсодержащего сине-зелёного параиба, — без участия меди.",
                origineCouleur = "Бразилия (штат Минас-Жерайс), Намибия (Эронго), Афганистан (Нуристан). От тёмно-синего до сине-серого цвета, обычно темнее, чем у параиба.",
                puretTraitements = "Иногда подвергается термообработке для осветления слишком тёмного тона — принятая обработка. Чистота варьируется.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин розовый",
                accroche = "Нежный оттенок, обязанный марганцу, лежащий в основе самых востребованных двухцветных турмалинов.",
                origineCouleur = "Бразилия (рудник Крузейру, штат Минас-Жерайс), Афганистан (Папрок), Мозамбик (Алту-Лигонья). От светло- до насыщенно-розового цвета; кристаллы часто зональны, что лежит в основе двухцветных разновидностей, таких как «арбузный» турмалин.",
                puretTraitements = "Редко обрабатывается. Чистота обычно хороша невооружённым глазом.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная чистка."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин чёрный (шерл)",
                accroche = "Самая распространённая разновидность турмалина, ценимая как в сдержанной ювелирке, так и в литотерапии.",
                origineCouleur = "Бразилия (штат Минас-Жерайс), Намибия (Эронго), Мадагаскар (Антсирабе). От непрозрачного до полупрозрачного чёрного цвета; призматические кристаллы часто со штриховкой, иногда крупные.",
                puretTraitements = "Никогда не обрабатывается — его цвет и распространённость не требуют никакого вмешательства. Очень доступная цена.",
                entretien = "Твёрдость 7–7,5, прочен. Уход простой, без особых предосторожностей."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин арбузный",
                accroche = "Розовая сердцевина, окружённая зелёным, — словно ломтик фрукта, одна из самых эффектных двухцветных разновидностей турмалина.",
                origineCouleur = "Бразилия (штат Минас-Жерайс), США (рудник Дантон, штат Мэн). Чёткая зональность: розовый цвет в центре, зелёный по краям; чаще всего гранится поперечными срезами, чтобы раскрыть «арбузный» эффект.",
                puretTraitements = "Редко обрабатывается — природная зональность и есть главное достоинство камня. Чистота варьируется в зависимости от зоны.",
                entretien = "Твёрдость 7–7,5. Рекомендуется бережная чистка, особенно для тонких срезов."
            ),
            BuyingGuideArticle(
                pierre = "Турмалин парайба",
                accroche = "Электрический «неоновый» сине-зелёный цвет, уникальный в минеральном царстве, — самый востребованный и дорогой из турмалинов.",
                origineCouleur = "Сан-Жозе-да-Баталья (штат Параиба, Бразилия) — исторический, почти исчерпанный источник. Мозамбик (Мавуко) и Нигерия (Эдеко): более доступные современные источники. «Неоновый» цвет, обязанный меди, никогда не встречается у других турмалинов.",
                puretTraitements = "Сертификат с указанием происхождения (Бразилия или Африка) обязателен, поскольку разница в стоимости весьма значительна. Чистота и интенсивность медного цвета важнее любого другого критерия.",
                entretien = "Твёрдость 7–7,5. Пироэлектрический камень: рекомендуется бережная чистка, беречь как любой камень очень высокой стоимости."
            ),
            BuyingGuideArticle(
                pierre = "Гранат пироп",
                accroche = "Классический гранат кроваво-красного цвета, без родолитового разбавления, — самая чистая разновидность семейства пиропов.",
                origineCouleur = "Чехия (Богемия, район Подседице), ЮАР (кимберлитовый рудник Као), Танзания (Умба). Глубокий однородный кроваво-красный цвет, без заметного фиолетового оттенка.",
                puretTraitements = "Никогда не обрабатывается, как и всё семейство гранатов. В отличие от родолита, чистый пироп не содержит значительной доли железа.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует, помимо обычных мер предосторожности для любого ювелирного камня."
            ),
            BuyingGuideArticle(
                pierre = "Гранат альмандин / пироп",
                accroche = "Гранат глубокого красного цвета, никогда не обрабатывается, — самый распространённый и доступный представитель семейства.",
                origineCouleur = "Индия (холмы Раджмахал, штат Джаркханд), Шри-Ланка (Ратнапура), Чехия (Подседице), Танзания (долина Умба). От тёмно-красного до красно-коричневого цвета, иногда с лёгким фиолетовым оттенком.",
                puretTraitements = "Никогда не подвергается термической или химической обработке. Спайность отсутствует, сильное преломление и хороший блеск.",
                entretien = "Твёрдость 7–7,5, в целом хорошая прочность. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат родолит",
                accroche = "«Малиновый» красно-фиолетовый цвет отличной прозрачности — один из самых элегантных гранатов.",
                origineCouleur = "Танзания (долина Умба), Шри-Ланка (Ратнапура), Индия (Орисса). Яркий однородный красно-фиолетовый цвет.",
                puretTraitements = "Никогда не обрабатывается. Отличная природная прозрачность, редко содержит видимые невооружённым глазом включения.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат звёздчатый",
                accroche = "Альмандин с четырьмя или шестью светящимися лучами — редкость, встречающаяся почти исключительно в Айдахо.",
                origineCouleur = "США (Айдахо — единственное в мире месторождение с четырёхлучевым астеризмом), Индия (Одиша). Тёмно-красный цвет; четырёхлучевой астеризм уникален в мире, ещё более редкий шестилучевой происходит из тех же месторождений.",
                puretTraitements = "Никогда не обрабатывается. Чёткость и центрирование звезды важнее прозрачности фона.",
                entretien = "Твёрдость 7–7,5. Рекомендуется бережная чистка для сохранения полировки купола."
            ),
            BuyingGuideArticle(
                pierre = "Гранат малайя",
                accroche = "Тёплый розово-оранжевый цвет, рождённый смешением двух гранатов, — без какой-либо обработки.",
                origineCouleur = "Танзания (Умба), Кения (Тайта-Тавета), Мадагаскар (Анджанабонуина). От розово-оранжевого до тёплого оранжевого цвета — уникальный оттенок среди гранатов.",
                puretTraitements = "Никакая обработка не требуется и не применяется: цвет на 100% природный — один из главных аргументов в пользу этой разновидности.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат спессартин",
                accroche = "Яркий «мандариновый» оранжевый цвет — один из самых светлых в минеральном царстве.",
                origineCouleur = "Намибия (регион Кунене), Нигерия (штат Насарава), Мадагаскар (Фианаранцуа). Яркий насыщенный оранжевый цвет, иногда с уклоном в красно-оранжевый.",
                puretTraitements = "Природный цвет, никогда не подвергается термообработке. Чистота обычно от хорошей до отличной.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат гроссуляр",
                accroche = "Гранат от зелёного до жёлто-зелёного цвета, слабо окрашенный родственник цаворита и гессонита, — самое разнообразное по окраске семейство.",
                origineCouleur = "Мали (Сандаре), Кения (Вои), Канада (провинция Квебек, рудник Джеффри). От бесцветного до глубокого зелёного цвета, через жёлто-зелёный и коричнево-оранжевый гессонита.",
                puretTraitements = "Никогда не обрабатывается. Чистота варьируется в зависимости от оттенка и месторождения.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат родолит фиолетовый",
                accroche = "Пурпурный оттенок, меняющийся в зависимости от освещения, — фиолетовый вариант классического родолита.",
                origineCouleur = "Танзания (долина Умба), Мозамбик (Куамба). От пурпурного до насыщенного фиолетового цвета — преобладающий оттенок меняется в зависимости от источника света.",
                puretTraitements = "Цвет стабилен, никогда не обрабатывается. Чистота обычно хороша.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Гранат с эффектом смены цвета",
                accroche = "Синевато-зелёный днём, красно-фиолетовый вечером — малоизвестный соперник александрита по значительно более доступной цене.",
                origineCouleur = "Танзания (Умба, Тундуру), Мадагаскар (Бекили). В отличие от александрита, за смену цвета в этом гранате отвечает не хром, а ванадий.",
                puretTraitements = "Никогда не обрабатывается. Как и в случае с александритом, сила смены цвета важнее самого оттенка.",
                entretien = "Твёрдость 7–7,5. Особого ухода не требует."
            ),
            BuyingGuideArticle(
                pierre = "Топаз голубой",
                accroche = "Глубокий голубой цвет, полученный облучением, а затем нагревом бесцветного топаза, — самая коммерчески распространённая окраска этого вида.",
                origineCouleur = "Бразилия (штат Минас-Жерайс) и Нигерия поставляют бесцветное сырьё, которое облучают, а затем нагревают для получения голубого цвета — от небесно-голубого до глубокого «London Blue».",
                puretTraitements = "Цвет почти всегда получен облучением с последующей термообработкой, стабилен и постоянен — раскрытие обязательно, обработка широко принята рынком благодаря доступной цене камня.",
                entretien = "Твёрдость 8, но идеальная спайность в одном направлении делает его хрупким к ударам. Избегать резких перепадов температуры, не использовать ультразвуковую чистку."
            ),
            BuyingGuideArticle(
                pierre = "Топаз розовый",
                accroche = "Самый редкий природный розовый цвет топаза, исторически добывавшийся в Пакистане.",
                origineCouleur = "Пакистан (Катланг, Мардан), Россия (Урал, историческое месторождение). От нежного до насыщенного розового цвета; природный необработанный розовый редок, его следует отличать от розового топаза, полученного нагревом коричневых камней.",
                puretTraitements = "Следует проверять по сертификату, природный ли это оттенок или полученный термообработкой коричневого материала, поскольку разница в стоимости значительна. Природная высокая чистота.",
                entretien = "Твёрдость 8, идеальная спайность в одном направлении: избегать ударов и резких перепадов температуры, не использовать ультразвуковую чистку."
            ),
            BuyingGuideArticle(
                pierre = "Топаз белый",
                accroche = "Топаз в самой чистой и распространённой форме — бесцветный и прозрачный, экономичная альтернатива алмазу.",
                origineCouleur = "Бразилия (штат Минас-Жерайс, Ору-Прету), Нигерия (плато Джос), Пакистан (Катланг). Бесцветный и совершенно прозрачный; это также самое распространённое сырьё для производства обработанного голубого топаза.",
                puretTraitements = "Сам по себе обычно не обрабатывается. Ценится очень высокая чистота, поскольку прозрачность — его главное достоинство.",
                entretien = "Твёрдость 8, идеальная спайность в одном направлении: избегать ударов, не использовать ультразвуковую чистку."
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
                pierre = "Gele saffier",
                accroche = "Korund in elke tint behalve rood vindt in geel een van zijn meest stralende en toegankelijke variëteiten.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tanzania (Tunduru). Levendig en stralend geel, egaal, zonder groenachtige zweem.",
                puretTraitements = "Verhitten is gebruikelijk om de kleur te intensiveren en gelijkmatiger te maken, een aanvaarde en stabiele behandeling. Zuiverheid over het algemeen goed met het blote oog.",
                entretien = "Hardheid 9, zeer bestand tegen dagelijks gebruik. Ultrasone reiniging is veilig voor een niet-gediffundeerde steen."
            ),
            BuyingGuideArticle(
                pierre = "Groene saffier",
                accroche = "Een ingetogen en vaak dichroïsch groen, de meest toegankelijke kleur van het korund.",
                origineCouleur = "Australië (Nieuw-Zuid-Wales), Thailand (Kanchanaburi), Nigeria (Mambilla). Zuiver groen, zonder te veel grijs; het dichroïsme groen/geelgroen naargelang de hoek is kenmerkend voor de soort.",
                puretTraitements = "Verhitten is gebruikelijk en aanvaard om de tint gelijkmatiger te maken. Zuiverheid over het algemeen goed.",
                entretien = "Hardheid 9, zeer bestendig. Ultrasone reiniging is veilig voor een niet-gediffundeerde steen."
            ),
            BuyingGuideArticle(
                pierre = "Blauwe saffier",
                accroche = "Het referentieblauw van de haute joaillerie, na diamant de hardste edelsteen.",
                origineCouleur = "Kasjmir: legendarisch fluweelachtig blauw, een vrijwel uitgeputte, uiterst zeldzame vindplaats. Myanmar (Mogok): intens «royal» blauw. Sri Lanka: lichter blauw, grote transparantie. Madagaskar: de belangrijkste moderne productie. Diep fluweelachtig blauw, sterke verzadiging zonder overmatig zwart.",
                puretTraitements = "Verhitten is vrijwel systematisch en algemeen aanvaard. Diffusie (klassiek met titaan, of problematischer met beryllium) moet apart op het certificaat worden vermeld. Zijdeachtige rutiel-insluitsels kunnen een asterisme (sterrensaffier) veroorzaken.",
                entretien = "Hardheid 9, zeer bestand tegen dagelijks gebruik. Ultrasone reiniging is veilig, behalve bij een gediffundeerde of opgevulde steen."
            ),
            BuyingGuideArticle(
                pierre = "Saffier van Auvergne",
                accroche = "Een Frans korund met een diepe blauwpaarse tint, gewonnen uit de vulkanische bodem van Auvergne.",
                origineCouleur = "Frankrijk (Espaly-Saint-Marcel, Haute-Loire), de enige bron. Zeer intens blauwpaars, kenmerkend voor saffieren van basaltische oorsprong; over het algemeen kleine kristallen, zelden meer dan één karaat geslepen.",
                puretTraitements = "Weinig of niet verhit, aangezien de zeldzaamheid en de lokale herkomst zwaarder wegen dan het intensiveren van de kleur. Kleine productie, per stuk te onderhandelen.",
                entretien = "Hardheid 9, zeer bestendig. Ultrasone reiniging is veilig."
            ),
            BuyingGuideArticle(
                pierre = "Paarse saffier",
                accroche = "Een diep paars, op de grens tussen blauwe saffier en robijn.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tanzania (Tunduru). Intens en egaal paars; kan een lichte kleurverandering vertonen tussen daglicht en gloeilicht.",
                puretTraitements = "Verhitten is gebruikelijk en aanvaard om de tint te stabiliseren. Zuiverheid over het algemeen goed met het blote oog.",
                entretien = "Hardheid 9, zeer bestendig. Ultrasone reiniging is veilig voor een niet-gediffundeerde steen."
            ),
            BuyingGuideArticle(
                pierre = "Roze saffier",
                accroche = "Op de grens tussen robijn en saffier, waarvan de officiële grens per laboratorium nog steeds ter discussie staat.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Tanzania (Tunduru). Levendig en intens roze; boven een bepaalde verzadigingsdrempel wordt de steen door sommige laboratoria als robijn geherclassificeerd.",
                puretTraitements = "Verhitten is gebruikelijk en aanvaard. Controleer de classificatie (roze saffier of robijn) op het certificaat, aangezien de waarde sterk kan verschillen.",
                entretien = "Hardheid 9, zeer bestendig. Ultrasone reiniging is veilig voor een niet-gediffundeerde steen."
            ),
            BuyingGuideArticle(
                pierre = "Witte saffier",
                accroche = "Het korund zonder kleurgevend spoor, een ingetogen en toegankelijk alternatief voor diamant.",
                origineCouleur = "Sri Lanka (Ratnapura), Madagaskar (Ilakaka), Australië (Nieuw-Zuid-Wales). Kleurloos en helder; dispersie lager dan die van diamant, maar vrijwel gelijke hardheid.",
                puretTraitements = "Zelden behandeld, aangezien de natuurlijke transparantie al de belangrijkste troef is. Hoge zuiverheid is gewild.",
                entretien = "Hardheid 9, uitstekend bestand tegen dagelijkse slijtage. Ultrasone reiniging is veilig."
            ),
            BuyingGuideArticle(
                pierre = "Sterrensaffier",
                accroche = "Een stralende ster met zes stralen, zichtbaar gemaakt door een cabochonslijpvorm, meestal in een blauwe of zwarte saffier.",
                origineCouleur = "Sri Lanka (Ratnapura), Myanmar (Mogok), Thailand (Kanchanaburi, zwarte variëteit). Het meest gewilde asterisme toont zes volkomen rechte stralen, gecentreerd op de koepel van de cabochon.",
                puretTraitements = "Zelden verhit, aangezien een thermische behandeling de rutiel-insluitsels die het asterisme veroorzaken kan aantasten. De scherpte en centrering van de ster wegen zwaarder dan de transparantie.",
                entretien = "Hardheid 9, zeer bestendig. Zachte reiniging aanbevolen om het polijstwerk van de koepel te behouden."
            ),
            BuyingGuideArticle(
                pierre = "Teal-saffier",
                accroche = "Een tweekleurig korund waarin blauw en geel samensmelten tot een unieke blauwgroene tint, zeer gewild bij kenners.",
                origineCouleur = "Verenigde Staten (Montana, Rock Creek), Australië (Nieuw-Zuid-Wales), Madagaskar (Ilakaka). De blauw-gele tweekleurigheid, in de ruwe steen zichtbaar in afzonderlijke zones, versmelt tot een egaal blauwgroen zodra de steen geslepen en georiënteerd is.",
                puretTraitements = "Vaak onverhit, aangezien de natuurlijke nuance juist door verzamelaars wordt gezocht. Het vakmanschap van de slijper, die de steen oriënteert om beide kleuren in balans te brengen, is doorslaggevend.",
                entretien = "Hardheid 9, zeer bestendig. Ultrasone reiniging is veilig voor een niet-gediffundeerde steen."
            ),
            BuyingGuideArticle(
                pierre = "Rubelliet toermalijn",
                accroche = "Een intens rood dankzij mangaan, de variëteit die het dichtst bij robijn komt onder de toermalijnen.",
                origineCouleur = "Brazilië (Cruzeiro-mijn, Minas Gerais), Nigeria (Oyo), Mozambique (Alto Ligonha), Afghanistan (Paprok-vallei). Intens en egaal rood tot roodroze; uitgesproken pleochroïsme, kenmerkende gestreepte prismatische kristallen.",
                puretTraitements = "Af en toe verhit om bruine ondertonen te verzachten, een aanvaarde behandeling. Wisselende zuiverheid; fijne, draadvormige insluitsels komen vaak voor en worden getolereerd indien discreet.",
                entretien = "Hardheid 7 tot 7,5, over het algemeen stevig. Een pyro-elektrische steen die stof aantrekt door statische elektriciteit: zachte reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Gele toermalijn",
                accroche = "Een stralend kanariegeel, natuurlijk en onbehandeld — de zeldzaamste kleur binnen de toermalijnen.",
                origineCouleur = "Malawi (Zomba), Brazilië (Minas Gerais), Nigeria (Oyo). Levendig en stralend geel, zonder dat ingrijpen nodig is, in tegenstelling tot veel commerciële tinten van toermalijn.",
                puretTraitements = "Meestal onbehandeld, aangezien de natuurlijke kleur al gewild is. Zuiverheid vaak goed.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte en regelmatige reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Chroomtoermalijn",
                accroche = "Een diep en verzadigd groen dankzij chroom en vanadium, te onderscheiden van het «klassieke» groen van toermalijn.",
                origineCouleur = "Kenia (Taita-Taveta), Tanzania (Umba). Intens groen dat dicht bij smaragd komt; uitgesproken dichroïsme, waarbij de oriëntatie van de tafel bepalend is om de meest verzadigde tint te tonen.",
                puretTraitements = "Zelden behandeld, aangezien de kleur door chroom en vanadium van nature al intens is. Zuiverheid over het algemeen goed.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Groene toermalijn (verdeliet)",
                accroche = "Een groen dat zijn kleur dankt aan ijzer of chroom, de meest klassieke en verbreide kleur van toermalijn.",
                origineCouleur = "Brazilië (Minas Gerais), Mozambique (Alto Ligonha), Nigeria (Oyo). Zuiver tot donker groen; sterke dubbele breking, waarbij slijpers de steen oriënteren om de kleur te optimaliseren.",
                puretTraitements = "Af en toe verhit, een aanvaarde behandeling om te donkere tinten lichter te maken. Zuiverheid wisselt naargelang de herkomst.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte en regelmatige reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Blauwe toermalijn (indigoliet)",
                accroche = "Een diep blauw, te onderscheiden van het kopergedreven blauwgroen van Paraíba, zonder tussenkomst van koper.",
                origineCouleur = "Brazilië (Minas Gerais), Namibië (Erongo), Afghanistan (Nuristan). Donkerblauw tot blauwgrijs, over het algemeen donkerder dan Paraíba.",
                puretTraitements = "Af en toe verhit om een te donkere tint lichter te maken, een aanvaarde behandeling. Wisselende zuiverheid.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Roze toermalijn",
                accroche = "Een tedere tint dankzij mangaan, aan de oorsprong van de meest gewilde tweekleurige toermalijnen.",
                origineCouleur = "Brazilië (Cruzeiro-mijn, Minas Gerais), Afghanistan (Paprok), Mozambique (Alto Ligonha). Lichtroze tot intens roze; vaak gezoneerde kristallen, aan de oorsprong van tweekleurige variëteiten zoals de watermeloentoermalijn.",
                puretTraitements = "Zelden behandeld. Zuiverheid over het algemeen goed met het blote oog.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte reiniging aanbevolen."
            ),
            BuyingGuideArticle(
                pierre = "Zwarte toermalijn (schörl)",
                accroche = "De meest voorkomende toermalijnvariëteit, gewaardeerd in sobere sieraden zoals in lithotherapie.",
                origineCouleur = "Brazilië (Minas Gerais), Namibië (Erongo), Madagaskar (Antsirabe). Ondoorzichtig tot subtransparant zwart; vaak gestreepte prismatische kristallen, soms van aanzienlijke omvang.",
                puretTraitements = "Nooit behandeld, aangezien de kleur en de overvloed geen enkel ingrijpen rechtvaardigen. Zeer toegankelijke prijs.",
                entretien = "Hardheid 7 tot 7,5, stevig. Eenvoudig onderhoud, zonder bijzondere voorzorgen."
            ),
            BuyingGuideArticle(
                pierre = "Watermeloentoermalijn",
                accroche = "Een roze hart omgeven door groen, als een plakje fruit — een van de meest spectaculaire tweekleurige toermalijnen.",
                origineCouleur = "Brazilië (Minas Gerais), Verenigde Staten (Dunton-mijn, Maine). Duidelijke zonering, roze in het midden en groen aan de rand; meestal geslepen in dwarse plakjes om het «watermeloen»-effect te tonen.",
                puretTraitements = "Zelden behandeld, aangezien de natuurlijke zonering de belangrijkste aantrekkingskracht van de steen is. Zuiverheid wisselt per zone.",
                entretien = "Hardheid 7 tot 7,5. Zachte reiniging aanbevolen, met name voor de dunne plakjes."
            ),
            BuyingGuideArticle(
                pierre = "Paraíba-toermalijn",
                accroche = "Een elektrisch «neon» blauwgroen, uniek in het mineralenrijk — de meest gewilde en kostbaarste toermalijn.",
                origineCouleur = "São José da Batalha (Paraíba, Brazilië), een vrijwel uitgeputte historische bron. Mozambique (Mavuco) en Nigeria (Edeko): toegankelijkere moderne bronnen. «Neon»-kleur dankzij koper, nooit waargenomen bij andere toermalijnen.",
                puretTraitements = "Een certificaat dat de herkomst (Brazilië versus Afrika) vermeldt is essentieel, aangezien het waardeverschil aanzienlijk is. Zuiverheid en intensiteit van de koperkleur wegen zwaarder dan elk ander criterium.",
                entretien = "Hardheid 7 tot 7,5. Een pyro-elektrische steen: zachte reiniging aanbevolen, te beschermen zoals elke steen van zeer hoge waarde."
            ),
            BuyingGuideArticle(
                pierre = "Pyroopgranaat",
                accroche = "Het klassieke bloedrode granaat, zonder de verdunning van rhodoliet — de zuiverste variëteit binnen de pyroopfamilie.",
                origineCouleur = "Tsjechië (Bohemen, streek van Podsedice), Zuid-Afrika (Kao-mijn, kimberlieten), Tanzania (Umba). Diep en egaal bloedrood, zonder noemenswaardige paarse ondertoon.",
                puretTraitements = "Nooit behandeld, zoals de gehele granaatfamilie. In tegenstelling tot rhodoliet bevat zuiver pyroop geen significant aandeel ijzer.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig, buiten de gebruikelijke voorzorgen voor elke sieraadsteen."
            ),
            BuyingGuideArticle(
                pierre = "Almandijn-/pyroopgranaat",
                accroche = "Een dieprood granaat, nooit behandeld — het meest voorkomende en toegankelijke lid van de familie.",
                origineCouleur = "India (Rajmahal Hills, Jharkhand), Sri Lanka (Ratnapura), Tsjechië (Podsedice), Tanzania (Umba-vallei). Donkerrood tot roodbruin, soms licht paarsig.",
                puretTraitements = "Nooit thermisch of chemisch behandeld. Geen splijting, sterke breking en goede glans.",
                entretien = "Hardheid 7 tot 7,5, over het algemeen stevig. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Rhodolietgranaat",
                accroche = "Een «framboos»-rood met paarse ondertoon en een uitstekende transparantie, een van de meest elegante granaten.",
                origineCouleur = "Tanzania (Umba-vallei), Sri Lanka (Ratnapura), India (Orissa). Levendig en egaal roodpaars.",
                puretTraitements = "Nooit behandeld. Uitstekende natuurlijke transparantie, zelden met het blote oog insluitsels zichtbaar.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Sterrengranaat",
                accroche = "Een almandijn met vier of zes stralende punten, een zeldzaamheid die vrijwel uitsluitend in Idaho wordt gevonden.",
                origineCouleur = "Verenigde Staten (Idaho, de enige vindplaats ter wereld met vierstralig asterisme), India (Odisha). Donkerrood; het vierstralige asterisme is wereldwijd uniek, het nog zeldzamere zesstralige asterisme komt uit dezelfde vindplaatsen.",
                puretTraitements = "Nooit behandeld. De scherpte en centrering van de ster wegen zwaarder dan de transparantie van de ondergrond.",
                entretien = "Hardheid 7 tot 7,5. Zachte reiniging aanbevolen om het polijstwerk van de koepel te behouden."
            ),
            BuyingGuideArticle(
                pierre = "Malayagranaat",
                accroche = "Een warm roze-oranje, ontstaan uit de menging van twee granaten, zonder enige behandeling.",
                origineCouleur = "Tanzania (Umba), Kenia (Taita-Taveta), Madagaskar (Anjanabonoina). Roze-oranje tot warm oranje, een unieke tint onder de granaten.",
                puretTraitements = "Geen enkele behandeling is nodig of gebruikelijk: 100% natuurlijke kleur, een van de grote verkoopargumenten van deze variëteit.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Spessartietgranaat",
                accroche = "Een stralend «mandarijn»-oranje, een van de meest levendige kleuren uit het mineralenrijk.",
                origineCouleur = "Namibië (Kunene-regio), Nigeria (staat Nasarawa), Madagaskar (Fianarantsoa). Levendig en verzadigd oranje, soms neigend naar roodoranje.",
                puretTraitements = "Natuurlijke kleur, nooit thermisch behandeld. Zuiverheid over het algemeen goed tot uitstekend.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Grossulaargranaat",
                accroche = "Het groene tot geelgroene granaat, een minder gekleurde neef van tsavoriet en hessoniet — de kleurrijkste familie binnen de granaten.",
                origineCouleur = "Mali (Sandaré), Kenia (Voi), Canada (Québec, Jeffrey Mine). Van kleurloos tot diep groen, via geelgroen en het bruinoranje van hessoniet.",
                puretTraitements = "Nooit behandeld. Zuiverheid varieert naargelang de tint en de vindplaats.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Paarse rhodolietgranaat",
                accroche = "Een purperen tint die verandert naargelang de verlichting, een paarse variant van de klassieke rhodoliet.",
                origineCouleur = "Tanzania (Umba-vallei), Mozambique (Cuamba). Purper tot intens paars, waarbij de dominante nuance verschilt naargelang de lichtbron.",
                puretTraitements = "Stabiele kleur, nooit behandeld. Zuiverheid over het algemeen goed.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Kleurveranderende granaat",
                accroche = "Blauwgroen bij daglicht, roodpaars 's avonds — een miskende rivaal van alexandriet, tegen een veel toegankelijkere prijs.",
                origineCouleur = "Tanzania (Umba, Tunduru), Madagaskar (Bekily). In tegenstelling tot alexandriet is hier vanadium, en niet chroom, verantwoordelijk voor de kleurverandering van dit granaat.",
                puretTraitements = "Nooit behandeld. De sterkte van de kleurverandering weegt zwaarder dan de tint zelf, net als bij alexandriet.",
                entretien = "Hardheid 7 tot 7,5. Geen bijzondere verzorging nodig."
            ),
            BuyingGuideArticle(
                pierre = "Blauwe topaas",
                accroche = "Het diepe blauw verkregen door bestraling gevolgd door verhitting van een kleurloze topaas — de meest verhandelde kleur binnen deze soort.",
                origineCouleur = "Brazilië (Minas Gerais) en Nigeria leveren de kleurloze grondstof, die bestraald en vervolgens verhit wordt om een blauw te ontwikkelen dat varieert van hemelsblauw tot diep «London Blue».",
                puretTraitements = "De kleur wordt vrijwel systematisch verkregen door bestraling gevolgd door een thermische behandeling, stabiel en permanent — vermelding is verplicht, en de behandeling wordt door de markt breed aanvaard vanwege de toegankelijke prijs.",
                entretien = "Hardheid 8, maar een perfecte splijting in één richting maakt hem breekbaar bij stoten. Vermijd plotselinge temperatuurschommelingen en gebruik geen ultrasoonreiniger."
            ),
            BuyingGuideArticle(
                pierre = "Roze topaas",
                accroche = "Het zeldzaamste natuurlijke roze van de topaas, van oudsher gewonnen in Pakistan.",
                origineCouleur = "Pakistan (Katlang, Mardan), Rusland (Oeral, historisch). Delicaat tot intens roze; het onbehandelde natuurlijke roze is zeldzaam en te onderscheiden van roze topaas verkregen door verhitting van bruine stenen.",
                puretTraitements = "Controleer op het certificaat of de tint natuurlijk is of verkregen door thermische behandeling van bruin materiaal, aangezien het waardeverschil aanzienlijk is. Van nature hoge zuiverheid.",
                entretien = "Hardheid 8, perfecte splijting in één richting: vermijd stoten en plotselinge temperatuurschommelingen, gebruik geen ultrasoonreiniger."
            ),
            BuyingGuideArticle(
                pierre = "Witte topaas",
                accroche = "Topaas in zijn zuiverste en meest voorkomende vorm, kleurloos en helder — een betaalbaar alternatief voor diamant.",
                origineCouleur = "Brazilië (Minas Gerais, Ouro Preto), Nigeria (Jos Plateau), Pakistan (Katlang). Kleurloos en volmaakt helder; tevens de meest voorkomende grondstof voor de productie van behandelde blauwe topaas.",
                puretTraitements = "Doorgaans zelf niet behandeld. Zeer hoge zuiverheid is gewild, aangezien de transparantie de belangrijkste troef is.",
                entretien = "Hardheid 8, perfecte splijting in één richting: vermijd stoten en gebruik geen ultrasoonreiniger."
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
                pierre = "黄色蓝宝石",
                accroche = "除红色以外的所有色调之中,黄色蓝宝石是刚玉家族中最明亮、也最平易近人的品种之一。",
                origineCouleur = "斯里兰卡(拉特纳普勒)、马达加斯加(伊拉卡卡)、坦桑尼亚(通杜鲁)。理想颜色为鲜艳明亮、色调均匀的黄色,不带绿色反光。",
                puretTraitements = "加热处理常见,用以强化并均匀色彩,该处理稳定且被市场接受。净度通常肉眼可见良好。",
                entretien = "硬度9,日常佩戴非常耐用。对未经扩散处理的宝石而言,超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "绿色蓝宝石",
                accroche = "低调内敛、常具二色性的绿色,是刚玉家族中价格最亲民的色彩。",
                origineCouleur = "澳大利亚(新南威尔士)、泰国(坎查那武里)、尼日利亚(曼比拉高原)。理想颜色为纯正的绿色,不带过多灰色调;依观察角度呈现绿色与黄绿色之间明显的二色性,是这一品种的特征。",
                puretTraitements = "加热处理常见且被接受,用以均匀色调。净度通常良好。",
                entretien = "硬度9,非常耐用。对未经扩散处理的宝石而言,超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "蓝宝石",
                accroche = "高级珠宝中标杆性的蓝色,硬度仅次于钻石。",
                origineCouleur = "克什米尔:传奇的天鹅绒蓝,产地几近枯竭,极为稀有。缅甸(抹谷):浓郁的「皇家蓝」。斯里兰卡:蓝色较浅,透明度极高。马达加斯加:当今主要产地。理想颜色为浓郁的天鹅绒蓝,饱和度高且不过分发黑。",
                puretTraitements = "加热处理几乎是常态,且被广泛接受。扩散处理(传统的钛扩散,或问题较多的铍扩散)必须在证书上明确区分。金红石丝状包裹体有时会产生星光效应(星光蓝宝石)。",
                entretien = "硬度9,日常佩戴非常耐用。超声波清洗通常安全,除非宝石经过扩散或充填处理。"
            ),
            BuyingGuideArticle(
                pierre = "奥弗涅蓝宝石",
                accroche = "产自法国奥弗涅火山地带的深蓝紫色刚玉。",
                origineCouleur = "法国(上卢瓦尔省埃斯帕利圣马塞尔),唯一产地。理想颜色为极为浓烈的蓝紫色,是玄武岩成因蓝宝石的典型特征;晶体通常细小,切割后很少超过一克拉。",
                puretTraitements = "极少或从不经加热处理,其稀有性与本土产地价值胜过色彩的强化。产量稀少,须逐颗议价。",
                entretien = "硬度9,非常耐用。超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "紫色蓝宝石",
                accroche = "深邃的紫色,正处于蓝宝石与红宝石之间的边界地带。",
                origineCouleur = "斯里兰卡(拉特纳普勒)、马达加斯加(伊拉卡卡)、坦桑尼亚(通杜鲁)。理想颜色为浓郁均匀的紫色;部分宝石在日光与白炽灯光下会呈现轻微的变色效应。",
                puretTraitements = "加热处理常见且被接受,用以稳定色调。净度通常肉眼可见良好。",
                entretien = "硬度9,非常耐用。对未经扩散处理的宝石而言,超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "粉色蓝宝石",
                accroche = "介于红宝石与蓝宝石之间的边界地带,其官方分界线在各宝石学实验室之间仍存争议。",
                origineCouleur = "斯里兰卡(拉特纳普勒)、马达加斯加(伊拉卡卡)、坦桑尼亚(通杜鲁)。理想颜色为鲜艳浓郁的粉色;一旦超过某种饱和度,部分实验室会将宝石重新归类为红宝石。",
                puretTraitements = "加热处理常见且被接受。应在证书上核实其分类(粉色蓝宝石或红宝石),因其价值可能因此产生巨大差异。",
                entretien = "硬度9,非常耐用。对未经扩散处理的宝石而言,超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "白色蓝宝石",
                accroche = "不含致色元素的刚玉,是钻石低调而平易近人的替代品。",
                origineCouleur = "斯里兰卡(拉特纳普勒)、马达加斯加(伊拉卡卡)、澳大利亚(新南威尔士)。理想状态为无色而清澈透亮;色散低于钻石,但硬度几乎相当。",
                puretTraitements = "极少经过处理,其天然的透明度本身便是主要卖点。追求高净度。",
                entretien = "硬度9,对日常磨损有极佳的抵抗力。超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "星光蓝宝石",
                accroche = "一颗明亮的六射星光,经凸圆面切割而呈现,常见于蓝色或黑色蓝宝石之中。",
                origineCouleur = "斯里兰卡(拉特纳普勒)、缅甸(抹谷)、泰国(坎查那武里,黑色品种)。最受追捧的星光效应呈现出六道完全笔直、以凸圆面顶点为中心的星芒。",
                puretTraitements = "极少经加热处理,因热处理可能破坏造成星光效应的金红石包裹体。星芒的清晰度与居中程度比透明度更为重要。",
                entretien = "硬度9,非常耐用。建议轻柔清洁,以保护凸圆面顶部的抛光效果。"
            ),
            BuyingGuideArticle(
                pierre = "青色蓝宝石",
                accroche = "一种双色刚玉,蓝色与黄色交融成独特的蓝绿色调,深受行家珍视。",
                origineCouleur = "美国(蒙大拿州洛克克里克)、澳大利亚(新南威尔士)、马达加斯加(伊拉卡卡)。原石中清晰可见的蓝黄双色区域,经切割及方向调整后会融合成均匀的蓝绿色调。",
                puretTraitements = "常未经加热处理,其天然色调本身便是收藏家追求的对象。切割师的技艺(调整方向以平衡两种颜色)对最终呈现至关重要。",
                entretien = "硬度9,非常耐用。对未经扩散处理的宝石而言,超声波清洗安全无虞。"
            ),
            BuyingGuideArticle(
                pierre = "红碧玺",
                accroche = "因锰元素而呈现的浓烈红色,是碧玺家族中最接近红宝石的品种。",
                origineCouleur = "巴西(米纳斯吉拉斯州克鲁泽罗矿)、尼日利亚(奥约)、莫桑比克(阿尔托利贡哈)、阿富汗(帕普罗克河谷)。理想颜色为浓郁均匀的红色至红粉色;多色性明显,具该品种特有的条纹状柱状晶体。",
                puretTraitements = "偶尔经加热处理以减弱棕色调,该处理被接受。净度因宝石而异;细针状内含物常见,若不明显则可接受。",
                entretien = "硬度7至7.5,整体较为坚固。碧玺具有热电性,会因静电吸附灰尘:建议轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "黄碧玺",
                accroche = "明亮的金丝雀黄,天然无需处理——碧玺家族中最稀有的色彩。",
                origineCouleur = "马拉维(松巴)、巴西(米纳斯吉拉斯州)、尼日利亚(奥约)。理想颜色为鲜艳明亮的黄色,不同于许多商业化处理的碧玺色调,无需任何人工干预。",
                puretTraitements = "通常未经处理,其天然色彩本身便备受追捧。净度通常良好。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议定期轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "铬碧玺",
                accroche = "因铬和钒而呈现的深邃饱和绿色,有别于碧玺「经典」的绿色。",
                origineCouleur = "肯尼亚(泰塔-塔韦塔)、坦桑尼亚(翁巴)。理想颜色为接近祖母绿的浓烈绿色;二色性明显,台面方向的选取对呈现最饱和的色调至关重要。",
                puretTraitements = "极少经过处理,由铬与钒致色的绿色本身已足够浓烈。净度通常良好。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "绿碧玺",
                accroche = "由铁或铬致色的绿色,是碧玺家族中最经典、也最常见的色彩。",
                origineCouleur = "巴西(米纳斯吉拉斯州)、莫桑比克(阿尔托利贡哈)、尼日利亚(奥约)。理想颜色为纯正绿色至深绿色;双折射较强,切割师会调整原石方向以优化色彩表现。",
                puretTraitements = "偶尔经加热处理以使过深的色调变浅,该处理被接受。净度因产地而异。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议定期轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "蓝碧玺（靛蓝碧玺）",
                accroche = "深邃的蓝色,不含铜元素,有别于帕拉伊巴碧玺铜绿蓝色调。",
                origineCouleur = "巴西(米纳斯吉拉斯州)、纳米比亚(埃龙戈)、阿富汗(努里斯坦)。理想颜色为深蓝至蓝灰色,通常比帕拉伊巴碧玺更为深邃。",
                puretTraitements = "偶尔经加热处理以使过深的色调变浅,该处理被接受。净度因宝石而异。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "粉碧玺",
                accroche = "锰元素造就的柔美色调,是最受追捧的双色碧玺的成因之一。",
                origineCouleur = "巴西(米纳斯吉拉斯州克鲁泽罗矿)、阿富汗(帕普罗克)、莫桑比克(阿尔托利贡哈)。理想颜色为浅粉至浓郁的粉色;晶体常呈色带分区,正是西瓜碧玺等双色品种的成因。",
                puretTraitements = "极少经过处理。净度通常肉眼可见良好。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议轻柔清洁。"
            ),
            BuyingGuideArticle(
                pierre = "黑碧玺（黑电气石）",
                accroche = "碧玺家族中最常见的品种,在简约珠宝与水晶疗法中皆备受青睐。",
                origineCouleur = "巴西(米纳斯吉拉斯州)、纳米比亚(埃龙戈)、马达加斯加(安齐拉贝)。理想颜色为不透明至半透明的乌黑色;柱状晶体常带有条纹,有时体积硕大。",
                puretTraitements = "从不经过处理,其色彩与丰富的储量无需任何人工干预。价格十分亲民。",
                entretien = "硬度7至7.5,质地坚固。保养简单,无需特殊防护。"
            ),
            BuyingGuideArticle(
                pierre = "西瓜碧玺",
                accroche = "粉色心芯环绕绿色边缘,宛如一片水果切面——最为惊艳的双色碧玺之一。",
                origineCouleur = "巴西(米纳斯吉拉斯州)、美国(缅因州邓顿矿)。呈现粉色核心环绕绿色外层的清晰分区;多切割成横截薄片,以呈现「西瓜」效果。",
                puretTraitements = "极少经过处理,其天然的色带分区正是这种宝石的主要魅力所在。净度依区域而异。",
                entretien = "硬度7至7.5。建议轻柔清洁,薄片切割的宝石尤需注意。"
            ),
            BuyingGuideArticle(
                pierre = "帕拉伊巴碧玺",
                accroche = "电光般的「霓虹」蓝绿色,在矿物界独一无二——碧玺家族中最受追捧、也最昂贵的品种。",
                origineCouleur = "巴西帕拉伊巴州圣若泽达巴塔利亚:历史性产地,如今几近枯竭。莫桑比克(马武科)与尼日利亚(埃德科):价格更为亲民的现代产地。这种由铜元素造就的「霓虹」色彩,从未在其他碧玺品种中出现。",
                puretTraitements = "一份注明产地(巴西还是非洲)的证书至关重要,因二者价值差距悬殊。净度与含铜蓝绿色的浓烈程度,比其他任何标准都更为关键。",
                entretien = "硬度7至7.5。碧玺具有热电性:建议轻柔清洁,并如同所有高价值宝石一样加以妥善保护。"
            ),
            BuyingGuideArticle(
                pierre = "镁铝榴石",
                accroche = "经典的血红色石榴石,未经玫瑰石榴石稀释——镁铝榴石家族中最为纯正的品种。",
                origineCouleur = "捷克(波希米亚波德塞迪采地区)、南非(金伯利岩考矿)、坦桑尼亚(翁巴)。理想颜色为浓郁均匀的血红色,不带明显的紫色调。",
                puretTraitements = "如整个石榴石家族一样,从不经过处理。与玫瑰石榴石不同,纯净的镁铝榴石不含明显比例的铁。",
                entretien = "硬度7至7.5。无需特殊保养,遵循一般宝石首饰的常规注意事项即可。"
            ),
            BuyingGuideArticle(
                pierre = "铁铝榴石／镁铝榴石",
                accroche = "深红色的石榴石,从不经过处理——石榴石家族中最常见、也最平易近人的品种。",
                origineCouleur = "印度(贾坎德邦拉杰默哈尔丘陵)、斯里兰卡(拉特纳普勒)、捷克(波德塞迪采)、坦桑尼亚(翁巴河谷)。理想颜色为深红色至红棕色,偶带轻微紫色调。",
                puretTraitements = "从未经过热处理或化学处理。无解理,折射率高,光泽良好。",
                entretien = "硬度7至7.5,整体较为坚固。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "玫瑰石榴石",
                accroche = "「覆盆子」般的红紫色,透明度极佳,是石榴石家族中最为优雅的品种之一。",
                origineCouleur = "坦桑尼亚(翁巴河谷)、斯里兰卡(拉特纳普勒)、印度(奥里萨邦)。理想颜色为明亮均匀的红紫色。",
                puretTraitements = "从不经过处理。天然透明度极佳,肉眼可见内含物的情形十分罕见。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "星光石榴石",
                accroche = "呈现四射或六射光芒的铁铝榴石,几乎仅产于美国爱达荷州的珍稀品种。",
                origineCouleur = "美国(爱达荷州,全世界唯一的四射星光石榴石产地)、印度(奥里萨邦)。理想颜色为深红色;四射星光效应举世无双,更为罕见的六射星光同样产自这些矿床。",
                puretTraitements = "从不经过处理。星芒的清晰度与居中程度比底色的透明度更为重要。",
                entretien = "硬度7至7.5。建议轻柔清洁,以保护凸圆面顶部的抛光效果。"
            ),
            BuyingGuideArticle(
                pierre = "马拉亚石榴石",
                accroche = "由两种石榴石混合而成的温暖粉橙色,不经任何处理。",
                origineCouleur = "坦桑尼亚(翁巴)、肯尼亚(泰塔-塔韦塔)、马达加斯加(安贾纳博诺伊纳)。理想颜色为粉橙色至温暖的橙色,是石榴石家族中独一无二的色调。",
                puretTraitements = "无需也不进行任何处理:色彩100%天然,是这一品种最大的卖点之一。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "锰铝榴石",
                accroche = "鲜艳夺目的「橘子」橙色,是矿物界中最为明亮的色彩之一。",
                origineCouleur = "纳米比亚(库内内地区)、尼日利亚(纳萨拉瓦州)、马达加斯加(菲亚纳兰楚阿)。理想颜色为鲜艳饱和的橙色,有时略带红橙色调。",
                puretTraitements = "天然色彩,从未经过热处理。净度通常良好至极佳。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "钙铝榴石",
                accroche = "绿色至黄绿色的石榴石,是沙弗莱石与桂榴石色彩较浅的近亲——色彩最为多样的石榴石家族。",
                origineCouleur = "马里(桑达雷)、肯尼亚(沃伊)、加拿大(魁北克省杰弗里矿)。色彩范围从无色到浓烈的深绿色,乃至黄绿色与桂榴石特有的棕橙色。",
                puretTraitements = "从不经过处理。净度因色调及产地而异。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "紫色玫瑰石榴石",
                accroche = "随光照变化的紫红色调,是经典玫瑰石榴石的紫色变种。",
                origineCouleur = "坦桑尼亚(翁巴河谷)、莫桑比克(库安巴)。理想颜色为紫红色至浓郁的紫色,主导色调依光源不同而变化。",
                puretTraitements = "色彩稳定,从不经过处理。净度通常良好。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "变色石榴石",
                accroche = "日光下呈蓝绿色,夜晚灯光下则变为紫红色——变石鲜为人知的对手,价格却亲民得多。",
                origineCouleur = "坦桑尼亚(翁巴、通杜鲁)、马达加斯加(贝基利)。与变石不同,造就这种石榴石变色效应的是钒元素,而非铬元素。",
                puretTraitements = "从不经过处理。如同变石一样,变色效应的强烈程度比色调本身更为重要。",
                entretien = "硬度7至7.5。无需特殊保养。"
            ),
            BuyingGuideArticle(
                pierre = "蓝色托帕石",
                accroche = "经辐照后再加热无色托帕石而获得的深邃蓝色——这一品种中商业化程度最高的色彩。",
                origineCouleur = "巴西(米纳斯吉拉斯州)与尼日利亚提供无色原料,经辐照后再加热处理,可呈现从天空蓝到深邃的「伦敦蓝」之间的各种蓝色调。",
                puretTraitements = "颜色几乎总是通过辐照后再加热处理获得,稳定且持久——必须披露,因价格亲民而被市场广泛接受。",
                entretien = "硬度8,但单方向的完美解理使其易受撞击损坏。应避免剧烈温差变化,切勿使用超声波清洗。"
            ),
            BuyingGuideArticle(
                pierre = "粉色托帕石",
                accroche = "托帕石中最为稀有的天然粉色,历史上开采自巴基斯坦。",
                origineCouleur = "巴基斯坦(马尔丹卡特朗)、俄罗斯(乌拉尔,历史产地)。理想颜色为细腻至浓郁的粉色;未经处理的天然粉色十分罕见,须与通过加热棕色宝石获得的粉色托帕石加以区分。",
                puretTraitements = "应在证书上核实其色调是天然还是经棕色原料加热处理而成,因二者价值差距明显。天然净度较高。",
                entretien = "硬度8,单方向具完美解理:应避免碰撞及剧烈温差变化,切勿使用超声波清洗。"
            ),
            BuyingGuideArticle(
                pierre = "白色托帕石",
                accroche = "托帕石最为纯净、最常见的形态,无色而清澈透亮——钻石经济实惠的替代品。",
                origineCouleur = "巴西(米纳斯吉拉斯州欧鲁普雷图)、尼日利亚(乔斯高原)、巴基斯坦(卡特朗)。理想状态为无色且完全清澈透亮;它也是生产经处理蓝色托帕石最主要的原材料。",
                puretTraitements = "本身通常未经处理。追求极高的净度,其透明度正是主要卖点。",
                entretien = "硬度8,单方向具完美解理:应避免碰撞,切勿使用超声波清洗。"
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
