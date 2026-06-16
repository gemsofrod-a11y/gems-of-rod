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
        // ── Pierres Précieuses ─────────────────────────────────────────────
        Product(
            id = "PIE-001", name = "Saphir", variety = "Saphir bleu Sri Lanka",
            category = "Précieuse", price = 2200.0,
            weightCarats = 5.20, cut = "Ovale", dimensions = "10×8 mm",
            color = "Bleu royal", clarity = "VS", origin = "Sri Lanka",
            notes = "Beau bleu profond", isFeatured = true
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
            id = "PIE-039", name = "Saphir", variety = "Saphir Padparadscha",
            category = "Précieuse", price = 5600.0,
            weightCarats = 0.94, cut = "Coussin allongée",
            dimensions = "7,5×4,5×3,5 mm", color = "Rose-orange",
            origin = "Mozambique", isFeatured = true
        ),
        Product(
            id = "PIE-040", name = "Saphir", variety = "Saphir Padparadscha",
            category = "Précieuse", price = 2300.0,
            weightCarats = 0.38, cut = "Ovale",
            dimensions = "6×3,5×2 mm", color = "Rose-orange",
            origin = "Mozambique"
        ),
        Product(
            id = "PIE-041", name = "Saphir", variety = "Saphir Padparadscha",
            category = "Précieuse", price = 10800.0,
            weightCarats = 1.85, cut = "Rectangle pans coupés",
            dimensions = "9×6×4,5 mm", color = "Rose-orange intense",
            origin = "Mozambique", isFeatured = true
        ),

        // ── Pierres Fines Nobles ───────────────────────────────────────────
        Product(
            id = "PIE-005", name = "Grenat", variety = "Grenat couleur changeante",
            category = "Fine noble", price = 181.76,
            weightCarats = 0.655, cut = "Ovale",
            dimensions = "5,35×4,46×2,94 mm",
            notes = "Couleur changeante"
        ),
        Product(
            id = "PIE-009", name = "Héliodore", variety = "Héliodore",
            category = "Fine noble", price = 151.32,
            weightCarats = 2.235, cut = "Rectangle"
        ),
        Product(
            id = "PIE-010", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 758.33,
            weightCarats = 0.895, cut = "Ovale",
            dimensions = "6,33×5,39×3,75 mm", color = "Bleu", isFeatured = true
        ),
        Product(
            id = "PIE-011", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 2964.0,
            weightCarats = 1.520, cut = "Poire",
            dimensions = "8,22×5,88×4,25 mm", color = "Bleu", isFeatured = true
        ),
        Product(
            id = "PIE-012", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 625.0,
            weightCarats = 0.730, cut = "Rectangle coins arrondis",
            dimensions = "6,93×4,5×2,75 mm", color = "Bleu",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-013", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 270.83,
            weightCarats = 0.810, cut = "Brillant",
            dimensions = "5,48×3,70 mm", color = "Bleu intense",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-024", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 260.20,
            weightCarats = 0.420, cut = "Ovale",
            dimensions = "6,18×4,06×2,85 mm",
            origin = "Pakistan"
        ),
        Product(
            id = "PIE-025", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 480.0,
            weightCarats = 2.500, cut = "Hexagonale (Hilite)",
            dimensions = "9,79×7,8×5,67 mm",
            origin = "Pakistan",
            notes = "Taille Hilite Cut"
        ),
        Product(
            id = "PIE-026", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 148.0,
            weightCarats = 4.070, cut = "Rectangle coins arrondis",
            dimensions = "12,91×7,95×5,94 mm", isFeatured = true
        ),
        Product(
            id = "PIE-027", name = "Aigue-marine", variety = "Aigue-marine (pendentif)",
            category = "Fine noble", price = 1100.0,
            weightCarats = 22.480, cut = "Rectangle",
            dimensions = "24×10×11 mm",
            notes = "Pierre brute sertie en pendentif wirewrapping cuivre artisanal. Pièce unique."
        ),
        Product(
            id = "PIE-028", name = "Aigue-marine", variety = "Aigue-marine",
            category = "Fine noble", price = 115.56,
            weightCarats = 2.140, cut = "Trillion",
            dimensions = "10,03×9,73×4,79 mm"
        ),
        Product(
            id = "PIE-031", name = "Spinelle", variety = "Spinelle Rose",
            category = "Fine noble", price = 512.0,
            weightCarats = 0.875, cut = "Ovale",
            dimensions = "6,68×5,01×3,63 mm", color = "Rose",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-032", name = "Spinelle", variety = "Spinelle Rose",
            category = "Fine noble", price = 446.0,
            weightCarats = 0.720, cut = "Rectangle coins ovales",
            dimensions = "6,07×4,30×3,26 mm", color = "Rose",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-033", name = "Spinelle", variety = "Spinelle Violet",
            category = "Fine noble", price = 2916.67,
            weightCarats = 1.355, cut = "Coussin",
            dimensions = "6,98×5,71×3,94 mm", color = "Violet profond",
            origin = "Sri Lanka", isFeatured = true
        ),
        Product(
            id = "PIE-034", name = "Spinelle", variety = "Spinelle Lavande",
            category = "Fine noble", price = 950.0,
            weightCarats = 1.125, cut = "Ovale",
            dimensions = "7,21×5,06×3,87 mm", color = "Lavande",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-035", name = "Grenat", variety = "Grenat Rubellite",
            category = "Fine noble", price = 300.0,
            weightCarats = 0.92, cut = "Rond",
            dimensions = "6×6×4,5 mm", color = "Rose-rouge intense",
            origin = "RDC"
        ),
        Product(
            id = "PIE-036", name = "Spinelle", variety = "Spinelle Bleu",
            category = "Fine noble", price = 1041.67,
            weightCarats = 1.385, cut = "Ovale",
            dimensions = "8,21×5,82×3,77 mm", color = "Bleu intense",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-037", name = "Spinelle", variety = "Spinelle Rouge",
            category = "Fine noble", price = 1100.0,
            weightCarats = 1.08, cut = "Coussin rectangulaire",
            color = "Rouge intense", origin = "Thaïlande"
        ),

        // ── Pierres Fines Courantes ────────────────────────────────────────
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
            id = "PIE-004", name = "Topaze", variety = "Topaze Champagne",
            category = "Fine courante", price = 16.25,
            weightCarats = 0.405, cut = "Ovale",
            dimensions = "6,06×4,20×2,60 mm", color = "Champagne"
        ),
        Product(
            id = "PIE-015", name = "Zircon", variety = "Zircon Jaune",
            category = "Fine courante", price = 83.66,
            weightCarats = 0.750, cut = "Carré bizoté",
            color = "Jaune", origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-016", name = "Zircon", variety = "Zircon Jaune",
            category = "Fine courante", price = 79.76,
            weightCarats = 0.715, cut = "Octogonale",
            dimensions = "4,73×4,36×2,95 mm", color = "Jaune",
            origin = "Sri Lanka"
        ),
        Product(
            id = "PIE-017", name = "Zircon", variety = "Zircon Jaune",
            category = "Fine courante", price = 98.17,
            weightCarats = 0.880, cut = "Baguette",
            dimensions = "6,68×3,53×2,94 mm", color = "Jaune",
            origin = "Sri Lanka"
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
            id = "PIE-030", name = "Topaze", variety = "Topaze Blue London",
            category = "Fine courante", price = 75.54,
            weightCarats = 3.0, cut = "Poire",
            dimensions = "12,18×7,19×4,70 mm",
            color = "Bleue (London Blue)", notes = "Blue London"
        ),
        Product(
            id = "PIE-038", name = "Topaze", variety = "Topaze Incolore",
            category = "Fine courante", price = 60.0,
            weightCarats = 4.375, cut = "Octogonale",
            dimensions = "10 mm Ø × 6 mm", color = "Incolore"
        ),
        Product(
            id = "PIE-014", name = "Péridot", variety = "Péridot",
            category = "Fine courante", price = 66.0,
            weightCarats = 1.430, cut = "Ovale"
        ),

        // ── Communes ──────────────────────────────────────────────────────
        Product(
            id = "PIE-029", name = "Citrine", variety = "Pendentif Citrine",
            category = "Commune", price = 716.0,
            weightCarats = 18.390, cut = "Coussin",
            dimensions = "18,42×15,4×11,7 mm",
            notes = "Pierre sertie en pendentif argent 925 artisanal. Vendu avec chaîne."
        ),
        Product(
            id = "PIE-042", name = "Apatite", variety = "Lot Apatite Bleue",
            category = "Commune", price = 45.0,
            weightCarats = 28.596, cut = "Cabochon",
            color = "Bleu azur",
            notes = "Lot de cabochons naturels"
        ),
        Product(
            id = "PIE-043", name = "Grenat", variety = "Lot Grenat Hessonite",
            category = "Commune", price = 30.0,
            weightCarats = 26.165,
            color = "Orange miel",
            notes = "Lot, qualité transparente"
        ),

        // ── Bijoux ────────────────────────────────────────────────────────
        Product(
            id = "BIJ-001", name = "Bague", variety = "Bague Argent 925 Labradorite",
            category = "Bijou", price = 65.0,
            notes = "Argent 925 poinçonné, labradorite naturelle avec labradorescence, accents oxyde de zirconium"
        ),
        Product(
            id = "BIJ-002", name = "Bague", variety = "Bague Solitaire Saphir Rose",
            category = "Bijou", price = 155.0,
            notes = "Argent 925, saphir rose synthétique solitaire, dureté 9/10 Mohs"
        ),
        Product(
            id = "BIJ-003", name = "Pendentif", variety = "Pendentif Hackmanite",
            category = "Bijou", price = 104.95,
            notes = "Argent 925, hackmanite naturelle — ténébrescence (change de couleur sous UV). Vendu sans chaîne."
        )
    )

    fun getById(id: String): Product? = products.find { it.id == id }

    fun getFeatured(): List<Product> = products.filter { it.isFeatured }

    fun getByCategory(category: String): List<Product> =
        if (category == "Tous") products
        else products.filter { it.category == category }
}
