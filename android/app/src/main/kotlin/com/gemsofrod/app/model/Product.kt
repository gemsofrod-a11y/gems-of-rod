package com.gemsofrod.app.model

data class Product(
    val id: String,
    val name: String,
    val variety: String,
    val category: String,
    val price: Double,
    val weightCarats: Double? = null,
    val cut: String? = null,
    val dimensions: String? = null,
    val color: String? = null,
    val clarity: String? = null,
    val origin: String? = null,
    val notes: String? = null,
    val isFeatured: Boolean = false
) {
    val formattedPrice: String get() {
        val nf = java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE)
        if (price == kotlin.math.floor(price)) {
            nf.maximumFractionDigits = 0
        } else {
            nf.minimumFractionDigits = 2
            nf.maximumFractionDigits = 2
        }
        return "${nf.format(price)} €"
    }

    val description: String get() {
        val parts = mutableListOf(variety)
        weightCarats?.let { parts.add("$it ct") }
        cut?.let { parts.add("taille $it") }
        color?.let { parts.add(it) }
        dimensions?.let { parts.add(it) }
        origin?.let { parts.add("origine : $it") }
        clarity?.let { parts.add("clarté $it") }
        val base = parts.joinToString(", ")
        return if (notes != null) "$base. $notes." else "$base."
    }
}

object ProductRepository {

    val products = listOf(
        Product(
            id = "PIE-001", name = "Saphir", variety = "Saphir bleu Sri Lanka",
            category = "Précieuse", price = 2200.0,
            weightCarats = 5.20, cut = "Ovale", dimensions = "10×8 mm",
            color = "Bleu royal", clarity = "VS", origin = "Sri Lanka",
            notes = "Beau bleu profond", isFeatured = true
        ),
        Product(
            id = "PIE-002", name = "Topaze", variety = "Topaze",
            category = "Fine courante", price = 4.20,
            weightCarats = 0.115, cut = "Carré"
        ),
        Product(
            id = "PIE-003", name = "Topaze", variety = "Topaze",
            category = "Fine courante", price = 8.40,
            weightCarats = 0.215, cut = "Carré"
        ),
        Product(
            id = "PIE-004", name = "Topaze", variety = "Topaze",
            category = "Fine courante", price = 10.80,
            weightCarats = 0.405, cut = "Ovale"
        ),
        Product(
            id = "PIE-005", name = "Grenat", variety = "Grenat couleur changeante",
            category = "Fine noble", price = 218.11,
            weightCarats = 0.655, cut = "Ovale",
            notes = "Couleur changeante"
        ),
        Product(
            id = "PIE-006", name = "Saphir", variety = "Saphir Bleu",
            category = "Précieuse", price = 309.42,
            weightCarats = 0.535, cut = "Poire"
        ),
        Product(
            id = "PIE-007", name = "Saphir", variety = "Saphir Bleu",
            category = "Précieuse", price = 211.09,
            weightCarats = 0.365, cut = "Ovale"
        ),
        Product(
            id = "PIE-008", name = "Saphir", variety = "Saphir Bleu",
            category = "Précieuse", price = 144.0,
            weightCarats = 0.250, cut = "Ovale"
        ),
        Product(
            id = "PIE-009", name = "Héliodore", variety = "Héliodore",
            category = "Fine noble", price = 151.32,
            weightCarats = 2.235, cut = "Rectangle"
        ),
        Product(
            id = "PIE-010", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 2400.0,
            weightCarats = 0.895, cut = "Ovale",
            color = "Bleu", isFeatured = true
        ),
        Product(
            id = "PIE-011", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 2964.0,
            weightCarats = 1.520, cut = "Poire",
            dimensions = "8,22×5,88×4,25 mm", color = "Bleu", isFeatured = true
        ),
        Product(
            id = "PIE-012", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 750.0,
            weightCarats = 0.730, cut = "Rectangle coins arrondis",
            dimensions = "6,93×4,5×2,75 mm", color = "Bleu"
        ),
        Product(
            id = "PIE-013", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 1170.0,
            weightCarats = 0.810, cut = "Brillant",
            dimensions = "5,48×3,70 mm", color = "Bleu"
        ),
        Product(
            id = "PIE-014", name = "Péridot", variety = "Péridot",
            category = "Fine courante", price = 66.0,
            weightCarats = 1.430, cut = "Ovale"
        ),
        Product(
            id = "PIE-015", name = "Zircon", variety = "Zircon",
            category = "Fine courante", price = 100.39,
            weightCarats = 0.750, cut = "Carré bizoté"
        ),
        Product(
            id = "PIE-016", name = "Zircon", variety = "Zircon",
            category = "Fine courante", price = 95.71,
            weightCarats = 0.715, cut = "Coussin"
        ),
        Product(
            id = "PIE-017", name = "Zircon", variety = "Zircon",
            category = "Fine courante", price = 117.80,
            weightCarats = 0.880, cut = "Baguette"
        ),
        Product(
            id = "PIE-018", name = "Iolite", variety = "Iolite",
            category = "Fine courante", price = 86.16,
            weightCarats = 0.470, cut = "Brillant",
            notes = "Lot de 3 pierres"
        ),
        Product(
            id = "PIE-019", name = "Iolite", variety = "Iolite",
            category = "Fine courante", price = 318.89,
            weightCarats = 1.740, cut = "Brillant",
            notes = "Lot de 13 pierres"
        ),
        Product(
            id = "PIE-020", name = "Iolite", variety = "Iolite",
            category = "Fine courante", price = 356.32,
            weightCarats = 1.955, cut = "Brillant",
            notes = "Lot de 23 pierres"
        ),
        Product(
            id = "PIE-021", name = "Rubis", variety = "Rubis",
            category = "Précieuse", price = 897.96,
            weightCarats = 0.265, cut = "Ovale", isFeatured = true
        ),
        Product(
            id = "PIE-022", name = "Diamant", variety = "Diamant",
            category = "Précieuse", price = 498.48,
            weightCarats = 0.155, cut = "Brillant",
            notes = "Lot de 5 pierres"
        ),
        Product(
            id = "PIE-023", name = "Diamant", variety = "Diamant",
            category = "Précieuse", price = 1384.28,
            weightCarats = 0.475, cut = "Brillant",
            notes = "Lot de 10 pierres", isFeatured = true
        ),
        Product(
            id = "PIE-024", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 312.24,
            weightCarats = 0.420, cut = "Ovale",
            dimensions = "6,24×4,08×2,86 mm"
        ),
        Product(
            id = "PIE-025", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 576.0,
            weightCarats = 2.500, cut = "Hexagonale",
            dimensions = "9,79×7,8×5,67 mm",
            notes = "Taille Illite"
        ),
        Product(
            id = "PIE-026", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 2131.20,
            weightCarats = 4.070, cut = "Rectangle coins arrondis",
            dimensions = "12,91×7,95×5,94 mm", isFeatured = true
        ),
        Product(
            id = "PIE-027", name = "Aigue-marine", variety = "Aigue-marine (brute)",
            category = "Fine noble", price = 850.0,
            weightCarats = 22.480, cut = "Rectangle",
            dimensions = "24×10×11 mm",
            notes = "Grande pièce brute"
        ),
        Product(
            id = "PIE-028", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 115.56,
            weightCarats = 2.140, cut = "Trillion",
            dimensions = "10,03×9,73×4,79 mm"
        ),
        Product(
            id = "PIE-029", name = "Citrine", variety = "Citrine",
            category = "Commune", price = 468.0,
            weightCarats = 18.390, cut = "Coussin",
            dimensions = "18,42×15,4×11,7 mm"
        ),
        Product(
            id = "PIE-030", name = "Topaze", variety = "Topaze Blue London",
            category = "Fine courante", price = 75.54,
            weightCarats = 3.0, cut = "Poire",
            dimensions = "12,18×7,19×4,70 mm",
            color = "Bleue (London Blue)", notes = "Blue London"
        ),
        Product(
            id = "PIE-031", name = "Spinelle", variety = "Spinelle Rose",
            category = "Fine noble", price = 614.40,
            weightCarats = 0.875, cut = "Ovale",
            dimensions = "6,68×5,01×3,63 mm", color = "Rose"
        ),
        Product(
            id = "PIE-032", name = "Spinelle", variety = "Spinelle Rose",
            category = "Fine noble", price = 535.20,
            weightCarats = 0.720, cut = "Coussin",
            dimensions = "6,07×4,30×3,26 mm", color = "Rose"
        ),
        Product(
            id = "PIE-033", name = "Spinelle", variety = "Spinelle Violet",
            category = "Fine noble", price = 7560.0,
            weightCarats = 1.355, cut = "Coussin",
            dimensions = "6,98×5,71×3,94 mm", color = "Violet",
            isFeatured = true
        ),
        Product(
            id = "PIE-034", name = "Spinelle", variety = "Spinelle Lavande",
            category = "Fine noble", price = 1140.0,
            weightCarats = 1.125, cut = "Ovale",
            dimensions = "7,21×5,06×3,87 mm", color = "Lavande"
        )
    )

    fun getById(id: String): Product? = products.find { it.id == id }

    fun getFeatured(): List<Product> = products.filter { it.isFeatured }

    fun getByCategory(category: String): List<Product> =
        if (category == "Tous") products
        else products.filter { it.category == category }
}
