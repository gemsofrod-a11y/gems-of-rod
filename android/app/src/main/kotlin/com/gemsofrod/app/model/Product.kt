package com.gemsofrod.app.model

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val origin: String,
    val weightCarats: Double,
    val treatment: String,
    val certification: String,
    val description: String,
    val imageResId: Int,
    val isFeatured: Boolean = false
) {
    val formattedPrice: String get() = "${String.format("%,d", price)} €"
}

object ProductRepository {

    val products = listOf(
        Product(
            id = "SAP-001",
            name = "Saphir naturel du Sri Lanka",
            category = "Pierre précieuse",
            price = 1200,
            origin = "Sri Lanka",
            weightCarats = 3.45,
            treatment = "Chauffage modéré",
            certification = "GIA",
            description = "Un saphir d'une qualité exceptionnelle provenant des mines de Sri Lanka. Sa couleur bleu royal intense et sa clarté remarquable en font une pierre rare et convoitée. Accompagné de son certificat d'authenticité GIA.",
            imageResId = -1,
            isFeatured = true
        ),
        Product(
            id = "EME-001",
            name = "Émeraude de Colombie",
            category = "Pierre précieuse",
            price = 1800,
            origin = "Colombie",
            weightCarats = 2.80,
            treatment = "Huile de cèdre légère",
            certification = "AGL",
            description = "Cette émeraude colombienne présente un vert profond et vivant, caractéristique des plus belles pierres de la région de Muzo. Sa transparence et son feu exceptionnel en font une gemme de collection.",
            imageResId = -1,
            isFeatured = true
        ),
        Product(
            id = "RUB-001",
            name = "Rubis de Birmanie",
            category = "Pierre précieuse",
            price = 2200,
            origin = "Myanmar (Birmanie)",
            weightCarats = 2.12,
            treatment = "Aucun",
            certification = "Gübelin",
            description = "Un rubis birman de qualité pigeon blood, la couleur la plus prisée et la plus rare. Sans aucun traitement, cette pierre est accompagnée d'un rapport Gübelin attestant son origine et sa pureté naturelle.",
            imageResId = -1,
            isFeatured = true
        ),
        Product(
            id = "AME-001",
            name = "Améthyste royale de Zambie",
            category = "Pierre fine",
            price = 450,
            origin = "Zambie",
            weightCarats = 8.60,
            treatment = "Aucun",
            certification = "IGI",
            description = "Une améthyste d'un violet profond et royal, extraite des gisements de Zambie. Sa taille coussin met en valeur ses reflets rouge-violet caractéristiques des meilleures améthystes africaines.",
            imageResId = -1,
            isFeatured = false
        ),
        Product(
            id = "TOP-001",
            name = "Topaze impériale du Brésil",
            category = "Pierre fine",
            price = 680,
            origin = "Brésil",
            weightCarats = 6.25,
            treatment = "Aucun",
            certification = "IGI",
            description = "La topaze impériale est la variété la plus précieuse de la topaze, avec ses reflets orangés à pêche caractéristiques. Cette pierre brésilienne offre une brillance et une pureté remarquables.",
            imageResId = -1,
            isFeatured = false
        ),
        Product(
            id = "DIA-001",
            name = "Diamant taillé brillant",
            category = "Pierre précieuse",
            price = 4800,
            origin = "Botswana",
            weightCarats = 1.02,
            treatment = "Aucun",
            certification = "GIA",
            description = "Un diamant de couleur F, pureté VS1, taillé brillant rond selon les proportions idéales. Accompagné de son certificat GIA, ce diamant offre un feu et une brillance exceptionnels.",
            imageResId = -1,
            isFeatured = false
        )
    )

    fun getById(id: String): Product? = products.find { it.id == id }

    fun getFeatured(): List<Product> = products.filter { it.isFeatured }

    fun getByCategory(category: String): List<Product> =
        if (category == "Tous") products
        else products.filter { it.category == category }
}
