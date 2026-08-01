package fr.gemsofrod.encyclopedie.data

// Fichier généré automatiquement par scripts/fetch_gem_images.py
// à partir de android/gem_image_credits.json (photos Wikimedia Commons).
// Ne pas éditer à la main : relancer le workflow "Fetch gem images".

enum class GemImageType { BRUTE, FACETTEE }

data class GemImageCredit(
    val type: GemImageType,
    val drawableName: String,
    val author: String,
    val license: String,
    val sourceUrl: String
)

object GemImages {
    private val credits: Map<String, List<GemImageCredit>> = mapOf(
        "aigue-marine" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_aigue_marine", "Gunnar Ries Amphibol", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Aquamarine_P1000141.JPG")),
        "alexandrite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_alexandrite", "Parent Géry", "CC0", "https://commons.wikimedia.org/wiki/File:Chrysobéryl_var._alexandrite_sous_UV_(Brésil)_1.jpg")),
        "amethyste" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_amethyste", "JJ Harrison (https://jjharrison.com.au/)", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Amethyst._Magaliesburg,_South_Africa.jpg")),
        "chrysoberyl" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_chrysoberyl", "Matteo Chinellato", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Chrysoberyl-282796.jpg")),
        "citrine" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_citrine", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Citrine_x_Smoky_quartz_gemstone_from_Czech_Republic_South_of_Bohemia.jpg")),
        "cristal-de-roche" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_cristal_de_roche", "JJ Harrison (https://www.jjharrison.com.au/)", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Quartz,_Tibet.jpg")),
        "diamant" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_diamant", "Pavel.Somov", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Diamond_(side_view).png")),
        "emeraude" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_emeraude", "Géry PARENT", "CC0", "https://commons.wikimedia.org/wiki/File:Béryl_var._émeraude_sur_gangue_(Muzo_Mine_Boyaca_-_Colombie)_2.jpg")),
        "goshenite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_goshenite", "Raimond Spekking", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Goshenite._Xuebaoding_Mt.,_Mianyang_City,_Sichuan_province,_China-9046.jpg")),
        "grenat-almandin" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_grenat_almandin", "Didier Descouens", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Almandin.jpg")),
        "grenat-rhodolite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_grenat_rhodolite", "Dave Dyet http://www.shutterstone.com  http://www.dyet.com", "Public domain", "https://commons.wikimedia.org/wiki/File:Rhodolite_in_Matrix-Garnet_Group_Magnesium_iron_aluminum_silicate_Macon_County_North_Carolina_2904.jpg")),
        "grenat-rhodolite-violet" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_grenat_rhodolite_violet", "Géry PARENT", "Public domain", "https://commons.wikimedia.org/wiki/File:Corundum,_garnet,_mica,_feldspar.jpg")),
        "grenat-spessartite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_grenat_spessartite", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Orange_spessartine_garnet_2.jpg")),
        "heliodore" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_heliodore", "Ra'ike (see also: de:Benutzer:Ra'ike)", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Goldberyll_2x_geschliffen_aus_Mosambik_und_Brasilien.jpg")),
        "hessonite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_hessonite", "Aangelo", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Hessonite_garnet_0019.jpg")),
        "iolite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_iolite", "Parent Géry", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Cordiérite_var._iolite_1.jpg")),
        "jade-jadeite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_jade_jadeite", "Joe Mabel", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Burmese_jadeite_faceted_stones_-_-_Burke_Museum.jpg")),
        "kunzite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_kunzite", "Didier Descouens", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Kunzite_Nouristan.jpg")),
        "labradorite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_labradorite", "Marco Hazard from Hong Kong, Hong Kong", "CC BY-SA 2.0", "https://commons.wikimedia.org/wiki/File:Gemstone_Collection_-_Labradorite_(17278919981).jpg")),
        "lapis-lazuli" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_lapis_lazuli", "Hannes Grobe", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Lapis-lazuli_hg.jpg")),
        "morganite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_morganite", "Eric Polk", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Beryl_var_morganite_NHMLA.png")),
        "obsidienne" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_obsidienne", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Obsidian_&_devitrified_obsidian_gravel_(Obsidian_Cliff,_Yellowstone,_Wyoming,_USA)_4.jpg")),
        "onyx" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_onyx", "Tamaghna Sengupta", "CC BY 3.0", "https://commons.wikimedia.org/wiki/File:Close_wing_position_of_Horaga_onyx_Moore,_1857_–_Common_Onyx_2.jpg")),
        "opale-de-feu" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_opale_de_feu", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Yellow_fire_opal_(Mexico)_2.jpg")),
        "opale-precieuse" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_opale_precieuse", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Precious_opal_after_glendonite_(White_Cliffs_Opal_Field,_New_South_Wales,_Australia).jpg")),
        "peridot" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_peridot", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Peridot_with_ludwigite_inclusion_-_faceted_gemstone_peridot_from_Pakistan.jpg")),
        "rhodochrosite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_rhodochrosite", "JJ Harrison (https://www.jjharrison.com.au/)", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Rhodochrosite_on_Matrix_-_Peru.jpg")),
        "rubis" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_rubis", "Marco Hazard from Hong Kong, Hong Kong", "CC BY-SA 2.0", "https://commons.wikimedia.org/wiki/File:Gemstone_Collection_-_Ruby_Crystal_(15635576883).jpg")),
        "saphir-bleu" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_saphir_bleu", "Montanabw", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Yogo2783_Close_crop.JPG")),
        "saphir-jaune" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_saphir_jaune", "Gemsphoto", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Yellow_sapphire_oval_gemstone.JPG")),
        "saphir-rose" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_saphir_rose", "Azuncha", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Sapphire01.jpg")),
        "spinelle-bleu" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_spinelle_bleu", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Spinel_(Mogok_Metamorphic_Belt,_Jurassic_to_Miocene;_Momeik_Township,_Shan_State,_Burma).jpg")),
        "spinelle-noir" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_spinelle_noir", "Laurent Massi", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Multi-phase_inclusions_in_a_gem_spinel.png")),
        "spinelle-rouge" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_spinelle_rouge", "Robert M. Lavinsky", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Spinel-49528.jpg")),
        "spinelle-violet" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_spinelle_violet", "Laurent Massi", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Cosmic-like_landscape_inclusions_in_a_gem_spinel.png")),
        "tanzanite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tanzanite", "Didier Descouens", "CC BY 3.0", "https://commons.wikimedia.org/wiki/File:Zoïsite_(Tanzanite).jpg")),
        "topaze-imperiale" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_topaze_imperiale", "Didier Descouens", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Topaze_Brésil.jpg")),
        "tourmaline-noire" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tourmaline_noire", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Tourmaline_-_schorl_x_dravite_Pikarec_Czech_Republic.jpg")),
        "tourmaline-pasteque" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tourmaline_pasteque", "Graeme Churchard", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Maine_Mineral_and_Gem_Museum,_Bethel_-_Elbaite_tourmaline_-_watermelon_tourmaline.jpg")),
        "tourmaline-rose" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tourmaline_rose", "Darla Sondrol", "CC0", "https://commons.wikimedia.org/wiki/File:Tourmaline_(GeoDIL_number_-_2751).jpg")),
        "tourmaline-rubellite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tourmaline_rubellite", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Rubellite_tourmaline_6.jpg")),
        "tourmaline-verte" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tourmaline_verte", "Arpingstone", "Public domain", "https://commons.wikimedia.org/wiki/File:Tumbled_gemstone_pebbles_arp.jpg")),
        "tsavorite" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_tsavorite", "Parent Géry", "Public domain", "https://commons.wikimedia.org/wiki/File:Grenat_tsavorite(Madagascar).jpg")),
        "zircon-blanc" to listOf(GemImageCredit(GemImageType.FACETTEE, "gem_zircon_blanc", "Robert M. Lavinsky", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Zircon-dtn1a.jpg")),
    )

    fun creditsFor(gemId: String): List<GemImageCredit> = credits[gemId].orEmpty()
}
