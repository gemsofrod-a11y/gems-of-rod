package fr.gemsofrod.encyclopedie.data

/**
 * Point géographique approximatif d'une origine de gemme, destiné à poser un
 * repère sur une carte — pas une donnée scientifique de précision GPS.
 */
data class GeoPoint(val label: String, val lat: Double, val lng: Double)

/**
 * Coordonnées approximatives (latitude/longitude WGS84) des pays et régions
 * utilisés dans [Gem.origines] à travers tout le catalogue, indexées par le
 * même nom de pays normalisé que [GemOrigins.countriesOf] produit (voir
 * `countryAliases` dans Classification.kt — les deux listes doivent rester
 * cohérentes). Alimente la carte globale de l'écran Pays (un repère par
 * pays).
 *
 * Les coordonnées visent le centre approximatif du pays ou une ville/région
 * représentative, pas un point unique « officiel » : suffisant pour placer
 * un repère à l'échelle d'une carte du monde.
 */
object CountryCoordinates {
    private val points: Map<String, GeoPoint> = mapOf(
        "Afghanistan" to GeoPoint("Afghanistan", 34.5, 69.2),
        "Afrique du Sud" to GeoPoint("Afrique du Sud", -29.0, 24.0),
        "Allemagne" to GeoPoint("Allemagne", 51.0, 10.0),
        "Argentine" to GeoPoint("Argentine", -34.6, -64.0),
        "Arménie" to GeoPoint("Arménie", 40.2, 44.5),
        "Australie" to GeoPoint("Australie", -25.0, 133.0),
        "Australie occidentale" to GeoPoint("Australie-Occidentale", -26.0, 121.0),
        "Autriche" to GeoPoint("Autriche", 47.5, 14.5),
        "Birmanie" to GeoPoint("Birmanie (Myanmar)", 21.9, 96.0),
        "Bolivie" to GeoPoint("Bolivie", -17.0, -65.0),
        "Botswana" to GeoPoint("Botswana", -22.0, 24.0),
        "Brésil" to GeoPoint("Brésil", -14.0, -51.0),
        "Cachemire" to GeoPoint("Cachemire", 34.0, 76.0),
        "Cambodge" to GeoPoint("Cambodge", 12.5, 105.0),
        "Canada" to GeoPoint("Canada", 56.0, -106.0),
        "Caraïbes" to GeoPoint("Caraïbes", 21.0, -77.0),
        "Chili" to GeoPoint("Chili", -33.5, -70.7),
        "Chine" to GeoPoint("Chine", 35.0, 103.0),
        "Colombie" to GeoPoint("Colombie", 4.5, -74.0),
        "Corée du Sud" to GeoPoint("Corée du Sud", 36.5, 127.8),
        "Espagne" to GeoPoint("Espagne", 40.0, -3.7),
        "Finlande" to GeoPoint("Finlande", 64.0, 26.0),
        "France" to GeoPoint("France", 46.6, 2.2),
        "Groenland" to GeoPoint("Groenland", 72.0, -40.0),
        "Guatemala" to GeoPoint("Guatemala", 15.5, -90.2),
        "Hawaï" to GeoPoint("Hawaï", 20.8, -156.3),
        "Hongrie" to GeoPoint("Hongrie", 47.2, 19.5),
        "Inde" to GeoPoint("Inde", 21.0, 78.0),
        "Indonésie" to GeoPoint("Indonésie", -2.5, 118.0),
        "Iran" to GeoPoint("Iran", 32.0, 53.0),
        "Islande" to GeoPoint("Islande", 64.9, -19.0),
        "Italie" to GeoPoint("Italie", 42.8, 12.8),
        "Japon" to GeoPoint("Japon", 36.2, 138.2),
        "Kazakhstan" to GeoPoint("Kazakhstan", 48.0, 67.0),
        "Kenya" to GeoPoint("Kenya", 1.0, 38.0),
        "Libye" to GeoPoint("Libye", 27.0, 17.0),
        "Madagascar" to GeoPoint("Madagascar", -19.0, 47.0),
        "Malawi" to GeoPoint("Malawi", -13.3, 34.3),
        "Mali" to GeoPoint("Mali", 17.0, -4.0),
        "Maroc" to GeoPoint("Maroc", 31.8, -7.1),
        "Mexique" to GeoPoint("Mexique", 23.6, -102.5),
        "Mozambique" to GeoPoint("Mozambique", -18.6, 35.5),
        "Namibie" to GeoPoint("Namibie", -22.6, 17.1),
        "Nigeria" to GeoPoint("Nigeria", 9.1, 8.7),
        "Norvège" to GeoPoint("Norvège", 60.5, 8.5),
        "Nouvelle-Zélande" to GeoPoint("Nouvelle-Zélande", -41.0, 174.0),
        "Népal" to GeoPoint("Népal", 28.2, 84.0),
        "Pakistan" to GeoPoint("Pakistan", 30.4, 69.3),
        "Polynésie française" to GeoPoint("Polynésie française", -17.7, -149.4),
        "Portugal" to GeoPoint("Portugal", 39.4, -8.2),
        "Pérou" to GeoPoint("Pérou", -9.2, -75.0),
        "Royaume-Uni" to GeoPoint("Royaume-Uni", 54.0, -2.0),
        "Russie" to GeoPoint("Russie", 61.5, 90.0),
        "République dominicaine" to GeoPoint("République dominicaine", 19.0, -70.7),
        "République démocratique du Congo" to GeoPoint("RD Congo", -4.0, 21.8),
        "République tchèque" to GeoPoint("République tchèque", 49.8, 15.5),
        "Sardaigne" to GeoPoint("Sardaigne", 40.1, 9.1),
        "Slovaquie" to GeoPoint("Slovaquie", 48.7, 19.5),
        "Sri Lanka" to GeoPoint("Sri Lanka", 7.9, 80.7),
        "Suisse" to GeoPoint("Suisse", 46.8, 8.2),
        "Suède" to GeoPoint("Suède", 62.0, 15.0),
        "Tadjikistan" to GeoPoint("Tadjikistan", 38.5, 71.0),
        "Tanzanie" to GeoPoint("Tanzanie", -6.4, 34.9),
        "Thaïlande" to GeoPoint("Thaïlande", 15.9, 100.9),
        "Turquie" to GeoPoint("Turquie", 38.9, 35.2),
        "Ukraine" to GeoPoint("Ukraine", 48.4, 31.2),
        "Uruguay" to GeoPoint("Uruguay", -32.5, -55.8),
        "Zambie" to GeoPoint("Zambie", -13.1, 27.8),
        "Zimbabwe" to GeoPoint("Zimbabwe", -19.0, 29.2),
        "Égypte" to GeoPoint("Égypte", 26.8, 30.8),
        "États-Unis" to GeoPoint("États-Unis", 39.8, -98.6),
        "Éthiopie" to GeoPoint("Éthiopie", 9.1, 40.5)
    )

    fun of(country: String): GeoPoint? = points[country]
}

/**
 * Coordonnées précises de localités/mines nommées dans le détail entre
 * parenthèses d'une entrée [Gem.origines] (ex. « Birmanie (Mogok) » →
 * Mogok), pour une carte embarquée sur la fiche d'une gemme plus parlante
 * qu'un simple repère au centre du pays. Les coordonnées des localités les
 * moins connues (Ľubietová, Andrahomana, Chara/Tokko, Sar-e-Sang, massif
 * d'Ilbir, Ilímaussaq…) ont été vérifiées sur Mindat/Wikipédia ; les autres
 * (Mogok, Ratnapura, Minas Gerais, Mont-Blanc…) sont des localités de
 * référence standard en gemmologie.
 *
 * Correspondance par sous-chaîne (la clé la plus spécifique doit apparaître
 * avant une clé plus générale qui la contiendrait) : voir [coordinatesFor].
 */
object LocalityCoordinates {
    private val localities: List<Pair<String, GeoPoint>> = listOf(
        // Birmanie / Myanmar
        "Mogok" to GeoPoint("Mogok, Birmanie", 22.92, 96.51),
        "Hpakant" to GeoPoint("Hpakant, Kachin, Birmanie", 25.70, 96.25),
        "Tawmaw" to GeoPoint("Tawmaw, Kachin, Birmanie", 25.75, 96.27),
        // Sri Lanka
        "Ratnapura" to GeoPoint("Ratnapura, Sri Lanka", 6.68, 80.40),
        "Meetiyagoda" to GeoPoint("Meetiyagoda, Sri Lanka", 6.31, 80.13),
        // Madagascar
        "Ilakaka" to GeoPoint("Ilakaka, Madagascar", -22.80, 45.33),
        "Ambatondrazaka" to GeoPoint("Ambatondrazaka, Madagascar", -17.83, 48.42),
        "Ambatofinandrahana" to GeoPoint("Ambatofinandrahana, Madagascar", -20.58, 46.80),
        "Antsirabe" to GeoPoint("Antsirabe, Madagascar", -19.87, 47.03),
        "Toliara" to GeoPoint("Toliara, Madagascar", -23.35, 43.67),
        "Tuléar" to GeoPoint("Tuléar, Madagascar", -23.35, 43.67),
        "Fianarantsoa" to GeoPoint("Fianarantsoa, Madagascar", -21.45, 47.09),
        "Andrahomana" to GeoPoint("Andrahomana, Fort-Dauphin, Madagascar", -25.15, 46.60),
        "Fort-Dauphin" to GeoPoint("Fort-Dauphin, Madagascar", -25.03, 46.98),
        "Mahajanga" to GeoPoint("Mahajanga, Madagascar", -15.72, 46.32),
        "Sakoany" to GeoPoint("Sakoany, Madagascar", -15.35, 46.55),
        "Anjanabonoina" to GeoPoint("Anjanabonoina, Madagascar", -19.98, 46.55),
        "Anjahamiary" to GeoPoint("Anjahamiary, Madagascar", -21.60, 47.15),
        "Antetezambato" to GeoPoint("Antetezambato, Madagascar", -13.35, 48.90),
        "Itrongay" to GeoPoint("Itrongay, Madagascar", -22.60, 45.30),
        "Bekily" to GeoPoint("Bekily, Madagascar", -24.23, 45.32),
        "Morondava" to GeoPoint("Bassin de Morondava, Madagascar", -20.28, 44.28),
        "Antsongombato" to GeoPoint("Mont Bity, Madagascar", -20.03, 46.78),
        // Tanzanie / Kenya
        "Mérelani" to GeoPoint("Merelani, Tanzanie", -3.62, 37.40),
        "Merelani" to GeoPoint("Merelani, Tanzanie", -3.62, 37.40),
        "Tunduru" to GeoPoint("Tunduru, Tanzanie", -11.10, 37.35),
        "Umba" to GeoPoint("Vallée de l'Umba, Tanzanie", -4.65, 38.65),
        "Haneti" to GeoPoint("Haneti, Tanzanie", -5.85, 35.95),
        "Matombo" to GeoPoint("Matombo, Tanzanie", -7.02, 37.85),
        "Morogoro" to GeoPoint("Morogoro, Tanzanie", -6.82, 37.66),
        "Taita-Taveta" to GeoPoint("Taita-Taveta, Kenya", -3.40, 38.35),
        "Voi" to GeoPoint("Voi, Kenya", -3.40, 38.56),
        "Mgama Hills" to GeoPoint("Mgama Hills, Kenya", -4.05, 39.05),
        // Mozambique / Namibie / Nigéria / Zambie / Afrique australe
        "Alto Ligonha" to GeoPoint("Alto Ligonha, Mozambique", -15.57, 39.07),
        "Montepuez" to GeoPoint("Montepuez, Mozambique", -13.13, 38.99),
        "Cuamba" to GeoPoint("Cuamba, Mozambique", -14.80, 36.54),
        "Mavuco" to GeoPoint("Mavuco, Mozambique", -13.37, 39.73),
        "Erongo" to GeoPoint("Erongo, Namibie", -21.67, 15.70),
        "Tsumeb" to GeoPoint("Tsumeb, Namibie", -19.25, 17.72),
        "Kaokoveld" to GeoPoint("Kaokoveld, Namibie", -18.50, 13.50),
        "Karibib" to GeoPoint("Karibib, Namibie", -21.94, 15.86),
        "Swartbooisdrift" to GeoPoint("Swartbooisdrift, Namibie", -17.45, 13.90),
        "Kunene" to GeoPoint("Région du Kunene, Namibie", -18.80, 13.60),
        "Jos Plateau" to GeoPoint("Plateau de Jos, Nigeria", 9.90, 8.90),
        "plateau de Jos" to GeoPoint("Plateau de Jos, Nigeria", 9.90, 8.90),
        "Nasarawa" to GeoPoint("État du Nasarawa, Nigeria", 8.50, 8.20),
        "Mambilla" to GeoPoint("Mambilla, Nigeria", 6.95, 11.30),
        "Edeko" to GeoPoint("Edeko, Nigeria", 8.70, 5.75),
        "Oyo" to GeoPoint("Oyo, Nigeria", 7.85, 3.93),
        "Kafubu" to GeoPoint("Mine Kagem, Kafubu, Zambie", -13.10, 28.35),
        "Copperbelt" to GeoPoint("Copperbelt, Zambie", -12.80, 28.20),
        "Kalomo" to GeoPoint("Kalomo, Zambie", -17.03, 26.49),
        "Lundazi" to GeoPoint("Lundazi, Zambie", -12.30, 33.18),
        "Cullinan" to GeoPoint("Mine Cullinan, Afrique du Sud", -25.68, 28.52),
        "Kimberley" to GeoPoint("Kimberley, Afrique du Sud", -28.74, 24.76),
        "Prieska" to GeoPoint("Prieska, Cap-Nord, Afrique du Sud", -29.66, 22.75),
        "Bushveld" to GeoPoint("Bushveld, Afrique du Sud", -24.60, 28.80),
        "Wessels" to GeoPoint("Mine Wessels, Kalahari, Afrique du Sud", -27.33, 22.82),
        "Jwaneng" to GeoPoint("Mine de Jwaneng, Botswana", -24.60, 24.73),
        "Katanga" to GeoPoint("Katanga, RD Congo", -10.71, 25.47),
        "Kivu" to GeoPoint("Kivu, RD Congo", -2.20, 28.90),
        "Sandawana" to GeoPoint("Sandawana, Zimbabwe", -20.55, 30.03),
        "Sandaré" to GeoPoint("Sandaré, Mali", 14.80, -10.30),
        // Afrique du Nord
        "Bou Azzer" to GeoPoint("Bou Azzer, Maroc", 30.97, -6.50),
        "Aouli" to GeoPoint("Aouli, Midelt, Maroc", 32.80, -4.58),
        "Midelt" to GeoPoint("Midelt, Maroc", 32.68, -4.74),
        "Touissit" to GeoPoint("Touissit, Maroc", 34.52, -1.98),
        "Alnif" to GeoPoint("Alnif, Maroc", 31.12, -5.18),
        "Haute Atlas" to GeoPoint("Haut Atlas, Maroc", 31.10, -7.90),
        "Zabargad" to GeoPoint("Île de Zabargad, Égypte", 23.60, 36.20),
        "Wollo" to GeoPoint("Wollo, Éthiopie", 11.50, 39.50),
        // Afghanistan / Pakistan / Tadjikistan
        "Sar-e-Sang" to GeoPoint("Sar-e-Sang, Badakhshan, Afghanistan", 36.02, 70.67),
        "Badakhshan" to GeoPoint("Badakhshan, Afghanistan", 36.73, 70.81),
        "Paprok" to GeoPoint("Vallée de Paprok, Nouristan, Afghanistan", 35.30, 70.90),
        "Nouristan" to GeoPoint("Nouristan, Afghanistan", 35.33, 70.95),
        "Skardu" to GeoPoint("Skardu, Baltistan, Pakistan", 35.30, 75.63),
        "Baltistan" to GeoPoint("Baltistan, Pakistan", 35.30, 75.60),
        "Kohistan" to GeoPoint("Kohistan, Pakistan", 35.30, 73.00),
        "Baloutchistan" to GeoPoint("Baloutchistan, Pakistan", 28.50, 65.00),
        "Alchuri" to GeoPoint("Alchuri, Pakistan", 35.42, 75.08),
        "Katlang" to GeoPoint("Katlang, Mardan, Pakistan", 34.28, 71.90),
        "Kuh-i-Lal" to GeoPoint("Kuh-i-Lal, Pamir, Tadjikistan", 37.57, 71.70),
        "Pamir" to GeoPoint("Pamir, Tadjikistan", 38.50, 73.00),
        "Padar" to GeoPoint("Vallée de Padar, Cachemire", 33.30, 76.20),
        "Kali Gandaki" to GeoPoint("Vallée de Kali Gandaki, Népal", 28.60, 83.70),
        // Russie
        "Kola" to GeoPoint("Péninsule de Kola, Russie", 67.70, 33.00),
        "Lovozero" to GeoPoint("Massif de Lovozero, Russie", 67.90, 34.70),
        "Khibiny" to GeoPoint("Massif de Khibiny, Russie", 67.63, 33.60),
        "Chara" to GeoPoint("Rivière Chara, Sibérie, Russie", 58.37, 118.88),
        "Yakoutie" to GeoPoint("Yakoutie, Sibérie, Russie", 62.00, 129.70),
        "Baïkal" to GeoPoint("Lac Baïkal, Sibérie, Russie", 53.50, 108.00),
        "Mir" to GeoPoint("Mine Mir, Sibérie, Russie", 62.53, 113.98),
        "Adui" to GeoPoint("Montagnes d'Adui, Sibérie, Russie", 66.50, 65.50),
        "Malychevskoe" to GeoPoint("Mine de Malychevskoe, Oural, Russie", 56.87, 60.57),
        "Poldnevaya" to GeoPoint("Mine de Poldnevaya, Oural, Russie", 56.60, 60.30),
        "Saranovskoye" to GeoPoint("Mine de Saranovskoye, Oural, Russie", 58.60, 58.30),
        "Malo-Sedelnikovo" to GeoPoint("Mine de Malo-Sedelnikovo, Oural, Russie", 56.90, 60.70),
        "Oural" to GeoPoint("Oural, Russie", 58.00, 59.50),
        "Sibérie" to GeoPoint("Sibérie, Russie", 60.00, 100.00),
        // Chine
        "Yunnan" to GeoPoint("Yunnan, Chine", 25.00, 101.50),
        "Hunan" to GeoPoint("Hunan, Chine", 27.60, 111.70),
        "Yaogangxian" to GeoPoint("Yaogangxian, Hunan, Chine", 25.80, 113.10),
        "Jiangxi" to GeoPoint("Jiangxi, Chine", 27.60, 115.70),
        "Xinjiang" to GeoPoint("Xinjiang, Chine", 41.00, 85.00),
        "Yarkand" to GeoPoint("Fleuve Yarkand, Xinjiang, Chine", 38.42, 77.25),
        "Hénan" to GeoPoint("Henan, Chine", 33.90, 113.50),
        "Bayan Obo" to GeoPoint("Bayan Obo, Mongolie-Intérieure, Chine", 41.77, 109.98),
        "Zhushan" to GeoPoint("Zhushan, Hubei, Chine", 32.17, 110.23),
        "Hubei" to GeoPoint("Hubei, Chine", 31.00, 112.00),
        "Zhuji" to GeoPoint("Zhuji, Zhejiang, Chine", 29.72, 120.23),
        // Inde
        "Rajmahal" to GeoPoint("Rajmahal Hills, Jharkhand, Inde", 24.90, 87.80),
        "Rajpipla" to GeoPoint("Rajpipla, Gujarat, Inde", 21.78, 73.50),
        "Deccan" to GeoPoint("Plateau du Deccan, Inde", 18.50, 77.00),
        "Odisha" to GeoPoint("Odisha, Inde", 20.50, 85.00),
        "Orissa" to GeoPoint("Orissa, Inde", 20.50, 85.00),
        "Karnataka" to GeoPoint("Karnataka, Inde", 14.50, 75.70),
        "Pune" to GeoPoint("Pune, Maharashtra, Inde", 18.52, 73.86),
        "Rajasthan" to GeoPoint("Rajasthan, Inde", 27.00, 74.00),
        "Andhra Pradesh" to GeoPoint("Andhra Pradesh, Inde", 15.90, 79.70),
        "Golconde" to GeoPoint("Golconde, Inde", 17.38, 78.40),
        // Asie du Sud-Est
        "Ratanakiri" to GeoPoint("Ratanakiri, Cambodge", 13.73, 106.99),
        "Kanchanaburi" to GeoPoint("Kanchanaburi, Thaïlande", 14.02, 99.53),
        "Chanthaburi" to GeoPoint("Chanthaburi, Thaïlande", 12.61, 102.10),
        "Sulawesi" to GeoPoint("Sulawesi, Indonésie", -2.50, 121.00),
        "Java" to GeoPoint("Java, Indonésie", -7.60, 110.00),
        "Iwagi" to GeoPoint("Îlot d'Iwagi, Japon", 34.15, 133.20),
        "Kōchi" to GeoPoint("Kōchi, Japon", 33.56, 133.53),
        "Akoya" to GeoPoint("Akoya, Japon", 34.70, 135.50),
        // Brésil
        "Minas Gerais" to GeoPoint("Minas Gerais, Brésil", -18.50, -44.00),
        "Ouro Preto" to GeoPoint("Ouro Preto, Minas Gerais, Brésil", -20.38, -43.50),
        "Cruzeiro" to GeoPoint("Mine de Cruzeiro, Minas Gerais, Brésil", -18.77, -41.57),
        "Hematita" to GeoPoint("Mine de Hematita, Minas Gerais, Brésil", -18.90, -42.50),
        "Pedra Azul" to GeoPoint("Mine de Pedra Azul, Minas Gerais, Brésil", -16.02, -41.30),
        "Nova Era" to GeoPoint("Nova Era, Minas Gerais, Brésil", -19.78, -43.03),
        "Espírito Santo" to GeoPoint("Espírito Santo, Brésil", -19.70, -40.60),
        "Novo Horizonte" to GeoPoint("Mine de Novo Horizonte, Brésil", -19.23, -39.90),
        "Bahia" to GeoPoint("Bahia, Brésil", -12.50, -41.70),
        "Rio Grande do Sul" to GeoPoint("Rio Grande do Sul, Brésil", -30.00, -53.00),
        "São José da Batalha" to GeoPoint("São José da Batalha, Paraíba, Brésil", -6.85, -37.90),
        "Paraíba" to GeoPoint("Paraíba, Brésil", -7.10, -36.70),
        // Autres Amériques
        "Motagua" to GeoPoint("Vallée du Motagua, Guatemala", 15.10, -89.60),
        "Muzo" to GeoPoint("Muzo, Colombie", 5.53, -74.10),
        "Chivor" to GeoPoint("Chivor, Colombie", 4.88, -73.37),
        "Anahí" to GeoPoint("Mine Anahí, Bolivie", -17.80, -58.10),
        "Anloa" to GeoPoint("Anloa, Potosí, Bolivie", -19.60, -65.75),
        "Potosí" to GeoPoint("Potosí, Bolivie", -19.58, -65.75),
        "Capillitas" to GeoPoint("Mine Capillitas, Catamarca, Argentine", -27.33, -66.52),
        "Uchucchacua" to GeoPoint("Mine Uchucchacua, Pérou", -10.80, -76.90),
        "Huanzala" to GeoPoint("Mine Huanzala, Pérou", -9.90, -76.90),
        "Huaraz" to GeoPoint("Huaraz, Pérou", -9.53, -77.53),
        "Ayacucho" to GeoPoint("Ayacucho, Pérou", -13.16, -74.22),
        "Cuzco" to GeoPoint("Cuzco, Pérou", -13.53, -71.97),
        "Flor de los Andes" to GeoPoint("Mine Flor de los Andes, Ovalle, Chili", -30.60, -71.20),
        "Barahona" to GeoPoint("Barahona, République dominicaine", 18.21, -71.10),
        // États-Unis (classé par localité)
        "Pikes Peak" to GeoPoint("Pikes Peak, Colorado, États-Unis", 38.84, -105.04),
        "Sweet Home" to GeoPoint("Mine Sweet Home, Colorado, États-Unis", 39.47, -106.04),
        "Black Hills" to GeoPoint("Black Hills, Dakota du Sud, États-Unis", 44.00, -103.60),
        "Mountain Pass" to GeoPoint("Mountain Pass, Californie, États-Unis", 35.48, -115.53),
        "Bishop" to GeoPoint("Bishop, Californie, États-Unis", 37.36, -118.40),
        "San Benito" to GeoPoint("San Benito, Californie, États-Unis", 36.36, -120.65),
        "San Carlos" to GeoPoint("San Carlos, Arizona, États-Unis", 33.35, -110.45),
        "Bisbee" to GeoPoint("Bisbee, Arizona, États-Unis", 31.45, -109.93),
        "Sleeping Beauty" to GeoPoint("Mine Sleeping Beauty, Arizona, États-Unis", 33.40, -110.00),
        "Wah Wah" to GeoPoint("Monts Wah Wah, Utah, États-Unis", 38.45, -113.70),
        "Fairfield" to GeoPoint("Fairfield, Utah, États-Unis", 40.27, -112.02),
        "Hiddenite" to GeoPoint("Hiddenite, Caroline du Nord, États-Unis", 35.93, -81.10),
        "Unaka" to GeoPoint("Monts Unaka, Caroline du Nord, États-Unis", 36.10, -82.10),
        "Graves Mountain" to GeoPoint("Graves Mountain, Géorgie, États-Unis", 33.70, -82.70),
        "Fannin" to GeoPoint("Comté de Fannin, Géorgie, États-Unis", 34.86, -84.32),
        "Idaho" to GeoPoint("Idaho, États-Unis", 44.00, -114.70),
        "Cave-in-Rock" to GeoPoint("Mine Cave-in-Rock, Illinois, États-Unis", 37.47, -88.16),
        "Illinois" to GeoPoint("Illinois, États-Unis", 40.00, -89.20),
        "Tri-State" to GeoPoint("District du Tri-State, Missouri, États-Unis", 37.10, -94.60),
        "Kelly Mine" to GeoPoint("Kelly Mine, Nouveau-Mexique, États-Unis", 34.07, -107.55),
        "Elmwood" to GeoPoint("Elmwood, Tennessee, États-Unis", 36.37, -85.97),
        "Adirondacks" to GeoPoint("Adirondacks, New York, États-Unis", 44.00, -74.00),
        "comté de Orange" to GeoPoint("Comté d'Orange, New York, États-Unis", 41.40, -74.30),
        "Rock Creek" to GeoPoint("Rock Creek, Montana, États-Unis", 46.50, -113.90),
        "Montana" to GeoPoint("Montana, États-Unis", 46.90, -110.40),
        "Michigan" to GeoPoint("Michigan, États-Unis", 44.30, -85.60),
        "New Jersey" to GeoPoint("New Jersey, États-Unis", 40.10, -74.70),
        "Arkansas" to GeoPoint("Arkansas, États-Unis", 34.90, -92.40),
        "Biggs" to GeoPoint("Biggs, Oregon, États-Unis", 45.67, -120.83),
        "Plush" to GeoPoint("Plush, Oregon, États-Unis", 42.47, -119.90),
        "Lake County" to GeoPoint("Lake County, Oregon, États-Unis", 42.70, -120.40),
        "Oregon" to GeoPoint("Oregon, États-Unis", 43.80, -120.50),
        "Dunton" to GeoPoint("Mine Dunton, Maine, États-Unis", 44.60, -70.00),
        "Maine" to GeoPoint("Maine, États-Unis", 45.25, -69.40),
        "forêt pétrifiée" to GeoPoint("Forêt pétrifiée, Arizona, États-Unis", 35.07, -109.78),
        "Picher" to GeoPoint("Picher, Oklahoma, États-Unis", 36.98, -94.83),
        "Oklahoma" to GeoPoint("Oklahoma, États-Unis", 35.50, -97.50),
        "Put-in-Bay" to GeoPoint("Île de Put-in-Bay, Ohio, États-Unis", 41.65, -82.82),
        "Morrison" to GeoPoint("Formation Morrison, Utah, États-Unis", 38.90, -109.60),
        "Pachuca" to GeoPoint("Sierra de Pachuca, Mexique", 20.12, -98.73),
        "Querétaro" to GeoPoint("Querétaro, Mexique", 20.59, -100.39),
        "Santa Eulalia" to GeoPoint("Santa Eulalia, Chihuahua, Mexique", 28.70, -105.90),
        "San Marcos" to GeoPoint("San Marcos, Chihuahua, Mexique", 27.50, -105.50),
        "Chihuahua" to GeoPoint("Chihuahua, Mexique", 28.63, -106.07),
        "Zacatecas" to GeoPoint("Zacatecas, Mexique", 22.77, -102.57),
        "Baja California" to GeoPoint("Basse-Californie, Mexique", 29.00, -114.00),
        "Basse-Californie" to GeoPoint("Basse-Californie, Mexique", 29.00, -114.00),
        "Charcas" to GeoPoint("Charcas, San Luis Potosí, Mexique", 23.13, -101.12),
        "Sonora" to GeoPoint("Sonora, Mexique", 29.00, -110.90),
        // Europe
        "Mont-Blanc" to GeoPoint("Massif du Mont-Blanc, Chamonix", 45.92, 6.87),
        "Mont Blanc" to GeoPoint("Massif du Mont-Blanc, Chamonix", 45.92, 6.87),
        "Chamonix" to GeoPoint("Chamonix, France", 45.92, 6.87),
        "Val Cristallina" to GeoPoint("Val Cristallina, Alpes suisses", 46.52, 8.70),
        "massif du Gothard" to GeoPoint("Massif du Saint-Gothard, Suisse", 46.56, 8.57),
        "Bourg-d'Oisans" to GeoPoint("Bourg-d'Oisans, France", 45.06, 6.03),
        "Salau" to GeoPoint("Salau, Ariège, France", 42.73, 1.10),
        "Baud" to GeoPoint("Baud, Morbihan, France", 47.88, -3.02),
        "Chaponost" to GeoPoint("Beaunant-Chaponost, Rhône, France", 45.70, 4.76),
        "Beaunant" to GeoPoint("Beaunant-Chaponost, Rhône, France", 45.70, 4.76),
        "Montebras" to GeoPoint("Montebras, Creuse, France", 46.33, 2.17),
        "Chanteloube" to GeoPoint("Chanteloube, Haute-Vienne, France", 46.07, 1.30),
        "La Léchère" to GeoPoint("La Léchère, Tarentaise, France", 45.47, 6.43),
        "Cap Garonne" to GeoPoint("Cap Garonne, Le Pradet, France", 43.08, 6.03),
        "Chaillac" to GeoPoint("Chaillac, Indre, France", 46.50, 1.30),
        "Ménilmontant" to GeoPoint("Ménilmontant, Paris, France", 48.87, 2.40),
        "Oise" to GeoPoint("Vallée de l'Oise, France", 49.40, 2.50),
        "Espaly" to GeoPoint("Espaly-Saint-Marcel, Haute-Loire, France", 45.05, 3.87),
        "Vésuve" to GeoPoint("Vésuve, Italie", 40.82, 14.43),
        "Somma" to GeoPoint("Mont Somma, Italie", 40.84, 14.42),
        "Torre del Greco" to GeoPoint("Torre del Greco, Italie", 40.78, 14.37),
        "San Marcel" to GeoPoint("San Marcel, Val d'Aoste, Italie", 45.68, 7.60),
        "Val d'Aoste" to GeoPoint("Val d'Aoste, Italie", 45.73, 7.32),
        "Toscane" to GeoPoint("Toscane, Italie", 43.77, 11.26),
        "Sicile" to GeoPoint("Sicile, Italie", 37.60, 14.02),
        "Knappenwand" to GeoPoint("Vallée de Knappenwand, Autriche", 47.13, 12.50),
        "Tyrol" to GeoPoint("Tyrol, Autriche", 47.26, 11.40),
        "Kutná Hora" to GeoPoint("Kutná Hora, République tchèque", 49.95, 15.27),
        "Podsedice" to GeoPoint("Podsedice, Bohême, République tchèque", 50.48, 13.75),
        "Moldavie" to GeoPoint("Bassin de Moldavie, Bohême, République tchèque", 49.30, 14.30),
        "Bohême" to GeoPoint("Bohême, République tchèque", 50.00, 14.30),
        "Ľubietová" to GeoPoint("Ľubietová, Slovaquie", 48.745, 19.36),
        "Eifel" to GeoPoint("Eifel, Allemagne", 50.17, 6.78),
        "Messbach" to GeoPoint("Messbach, Saxe, Allemagne", 50.60, 12.50),
        "Werra" to GeoPoint("Bassin de la Werra, Allemagne", 51.00, 10.00),
        "Navajún" to GeoPoint("Navajún, La Rioja, Espagne", 42.08, -1.98),
        "Molina de Aragón" to GeoPoint("Molina de Aragón, Espagne", 40.84, -1.88),
        "Lugo" to GeoPoint("Lugo, Galice, Espagne", 43.00, -7.56),
        "Áliva" to GeoPoint("Mine de Áliva, Cantabrie, Espagne", 43.19, -4.83),
        "Asturies" to GeoPoint("Asturies, Espagne", 43.36, -5.85),
        "Telkibánya" to GeoPoint("Telkibánya, Hongrie", 48.40, 21.33),
        "Kaavi" to GeoPoint("Kaavi, Finlande", 63.10, 28.50),
        "Outokumpu" to GeoPoint("Outokumpu, Finlande", 62.72, 29.02),
        "Ylämaa" to GeoPoint("Ylämaa, Finlande", 60.98, 27.95),
        "Kiruna" to GeoPoint("Kiruna, Suède", 67.86, 20.22),
        "Långban" to GeoPoint("Långban, Suède", 59.85, 14.27),
        "Sauland" to GeoPoint("Sauland, Telemark, Norvège", 59.60, 8.90),
        "Telemark" to GeoPoint("Telemark, Norvège", 59.40, 8.30),
        "Langesundfjord" to GeoPoint("Langesundfjord, Norvège", 58.99, 9.75),
        "Tvedestrand" to GeoPoint("Tvedestrand, Norvège", 58.62, 8.93),
        "Hrafntinnusker" to GeoPoint("Hrafntinnusker, Islande", 64.08, -19.10),
        "Whitby" to GeoPoint("Whitby, Yorkshire, Royaume-Uni", 54.49, -0.61),
        "Weardale" to GeoPoint("Weardale, Angleterre", 54.73, -2.05),
        "Cumbria" to GeoPoint("Cumbria, Royaume-Uni", 54.57, -3.05),
        "Cornouailles" to GeoPoint("Cornouailles, Royaume-Uni", 50.40, -4.70),
        "Kent" to GeoPoint("Falaises de craie du Kent, Royaume-Uni", 51.17, 1.40),
        "Ilbir" to GeoPoint("Massif d'Ilbir, Muğla, Turquie", 37.30, 28.10),
        "Muğla" to GeoPoint("Muğla, Turquie", 37.21, 28.36),
        "Anatolie" to GeoPoint("Anatolie, Turquie", 39.00, 35.00),
        "Kertch" to GeoPoint("Péninsule de Kertch, Ukraine", 45.35, 36.47),
        // Groenland
        "Ilímaussaq" to GeoPoint("Complexe d'Ilímaussaq, Groenland", 60.92, -45.92),
        "Fiskenæsset" to GeoPoint("Fiskenæsset, Groenland", 63.08, -50.68),
        // Canada / Océanie / Nouvelle-Zélande
        "Jeffrey Mine" to GeoPoint("Jeffrey Mine, Québec, Canada", 45.47, -71.57),
        "mont Saint-Hilaire" to GeoPoint("Mont-Saint-Hilaire, Québec, Canada", 45.55, -73.15),
        "Gaspésie" to GeoPoint("Gaspésie, Québec, Canada", 48.80, -65.50),
        "Bancroft" to GeoPoint("Bancroft, Ontario, Canada", 45.05, -77.85),
        "Colombie-Britannique" to GeoPoint("Colombie-Britannique, Canada", 53.70, -127.60),
        "Ice River" to GeoPoint("Ice River, Colombie-Britannique, Canada", 51.40, -116.40),
        "Labrador" to GeoPoint("Labrador, Canada", 53.30, -61.00),
        "Paul Island" to GeoPoint("Paul Island, Labrador, Canada", 56.50, -61.40),
        "Diavik" to GeoPoint("Mine Diavik, Territoires du Nord-Ouest, Canada", 64.50, -110.27),
        "Nouvelle-Écosse" to GeoPoint("Nouvelle-Écosse, Canada", 45.00, -63.00),
        "Alberta" to GeoPoint("Alberta, Canada", 52.00, -115.00),
        "Broken Hill" to GeoPoint("Broken Hill, Nouvelle-Galles du Sud, Australie", -31.95, 141.47),
        "Lightning Ridge" to GeoPoint("Lightning Ridge, Nouvelle-Galles du Sud, Australie", -29.43, 147.98),
        "Nouvelle-Galles du Sud" to GeoPoint("Nouvelle-Galles du Sud, Australie", -32.00, 147.00),
        "Marlborough" to GeoPoint("Marlborough, Queensland, Australie", -22.82, 149.88),
        "Queensland" to GeoPoint("Queensland, Australie", -22.00, 144.00),
        "Broome" to GeoPoint("Broome, Australie", -17.96, 122.24),
        "Stichtite Hill" to GeoPoint("Mine Stichtite Hill, Tasmanie, Australie", -41.80, 145.57),
        "Tasmanie" to GeoPoint("Tasmanie, Australie", -42.00, 146.80),
        "Dundas" to GeoPoint("Dundas, Tasmanie, Australie", -41.65, 145.45),
        "Widgiemooltha" to GeoPoint("Widgiemooltha, Australie", -31.50, 121.57),
        "Wave Hill" to GeoPoint("Wave Hill, Australie-Occidentale", -17.50, 131.05),
        "Arahura" to GeoPoint("Rivière Arahura, Nouvelle-Zélande", -42.70, 170.95),
        "Anita Bay" to GeoPoint("Anita Bay, Fiordland, Nouvelle-Zélande", -44.67, 167.85),
        "Kaliningrad" to GeoPoint("Kaliningrad, Russie", 54.71, 20.51),
        // Caraïbes / Hawaï / Océans
        "Bahamas" to GeoPoint("Bahamas", 24.25, -76.00),
        "Tahiti" to GeoPoint("Tahiti, Polynésie française", -17.65, -149.42)
    )

    /**
     * Point correspondant à la localité la plus précise trouvée dans [origin]
     * (recherche par sous-chaîne, clés les plus spécifiques en tête de
     * liste), ou `null` si aucune localité nommée n'est reconnue — à l'appelant
     * de retomber alors sur [CountryCoordinates].
     */
    fun coordinatesFor(origin: String): GeoPoint? {
        // Comparaison insensible à la casse et aux tirets : les entrées
        // d'origines mêlent "Mont Saint-Hilaire" / "mont Saint-Hilaire" /
        // "Mont-Saint-Hilaire" selon la fiche — une correspondance strictement
        // littérale manquerait les variantes.
        val normalized = origin.lowercase().replace('-', ' ')
        return localities.firstOrNull { (key, _) -> normalized.contains(key.lowercase().replace('-', ' ')) }?.second
    }
}

/**
 * Résout le meilleur repère disponible pour une entrée [Gem.origines] :
 * la localité précise si elle est reconnue ([LocalityCoordinates]), sinon
 * le centre du pays ([CountryCoordinates]) via la même normalisation que
 * [GemOrigins.countriesOf] (donc cohérent avec l'écran Pays), sinon `null`
 * (entrée purement descriptive sans pays identifiable, ex. une note sur la
 * récolte d'une gemme organique — pas de repère affiché, pas d'erreur).
 */
fun geoPointForOrigin(origin: String): GeoPoint? =
    LocalityCoordinates.coordinatesFor(origin)
        ?: GemOrigins.countriesOf(origin).firstNotNullOfOrNull { CountryCoordinates.of(it) }
