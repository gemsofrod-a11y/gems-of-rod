package fr.gemsofrod.encyclopedie.data

// Fichier généré automatiquement par scripts/fetch_gem_images.py
// à partir de photos librement réutilisables de Wikimedia Commons.
// Ne pas éditer à la main : relancer le workflow "Fetch gem images".

data class GemImageCredit(
    val drawableName: String,
    val author: String,
    val license: String,
    val sourceUrl: String
)

object GemImages {
    private val credits: Map<String, GemImageCredit> = mapOf(
        "rubis" to GemImageCredit("gem_rubis", "Marco Hazard from Hong Kong, Hong Kong", "CC BY-SA 2.0", "https://commons.wikimedia.org/wiki/File:Gemstone_Collection_-_Ruby_Crystal_(15635576883).jpg"),
        "grenat-almandin" to GemImageCredit("gem_grenat_almandin", "Didier Descouens", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Almandin.jpg"),
        "spinelle-rouge" to GemImageCredit("gem_spinelle_rouge", "Robert M. Lavinsky", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Spinel-49528.jpg"),
        "tourmaline-rubellite" to GemImageCredit("gem_tourmaline_rubellite", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Rubellite_tourmaline_6.jpg"),
        "grenat-rhodolite" to GemImageCredit("gem_grenat_rhodolite", "Dave Dyet http://www.shutterstone.com  http://www.dyet.com", "Public domain", "https://commons.wikimedia.org/wiki/File:Rhodolite_in_Matrix-Garnet_Group_Magnesium_iron_aluminum_silicate_Macon_County_North_Carolina_2904.jpg"),
        "grenat-spessartite" to GemImageCredit("gem_grenat_spessartite", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Orange_spessartine_garnet_2.jpg"),
        "topaze-imperiale" to GemImageCredit("gem_topaze_imperiale", "Didier Descouens", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Topaze_Brésil.jpg"),
        "opale-de-feu" to GemImageCredit("gem_opale_de_feu", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Yellow_fire_opal_(Mexico)_2.jpg"),
        "saphir-jaune" to GemImageCredit("gem_saphir_jaune", "Gemsphoto", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Yellow_sapphire_oval_gemstone.JPG"),
        "citrine" to GemImageCredit("gem_citrine", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Citrine_x_Smoky_quartz_gemstone_from_Czech_Republic_South_of_Bohemia.jpg"),
        "heliodore" to GemImageCredit("gem_heliodore", "Ra'ike (see also: de:Benutzer:Ra'ike)", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Goldberyll_2x_geschliffen_aus_Mosambik_und_Brasilien.jpg"),
        "chrysoberyl" to GemImageCredit("gem_chrysoberyl", "Matteo Chinellato", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Chrysoberyl-282796.jpg"),
        "emeraude" to GemImageCredit("gem_emeraude", "Géry PARENT", "CC0", "https://commons.wikimedia.org/wiki/File:Béryl_var._émeraude_sur_gangue_(Muzo_Mine_Boyaca_-_Colombie)_2.jpg"),
        "peridot" to GemImageCredit("gem_peridot", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Peridot_with_ludwigite_inclusion_-_faceted_gemstone_peridot_from_Pakistan.jpg"),
        "tsavorite" to GemImageCredit("gem_tsavorite", "Parent Géry", "Public domain", "https://commons.wikimedia.org/wiki/File:Grenat_tsavorite(Madagascar).jpg"),
        "jade-jadeite" to GemImageCredit("gem_jade_jadeite", "Joe Mabel", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Burmese_jadeite_faceted_stones_-_-_Burke_Museum.jpg"),
        "tourmaline-verte" to GemImageCredit("gem_tourmaline_verte", "Arpingstone", "Public domain", "https://commons.wikimedia.org/wiki/File:Tumbled_gemstone_pebbles_arp.jpg"),
        "saphir-bleu" to GemImageCredit("gem_saphir_bleu", "Montanabw", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Yogo2783_Close_crop.JPG"),
        "aigue-marine" to GemImageCredit("gem_aigue_marine", "Gunnar Ries Amphibol", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Aquamarine_P1000141.JPG"),
        "tanzanite" to GemImageCredit("gem_tanzanite", "Didier Descouens", "CC BY 3.0", "https://commons.wikimedia.org/wiki/File:Zoïsite_(Tanzanite).jpg"),
        "lapis-lazuli" to GemImageCredit("gem_lapis_lazuli", "Hannes Grobe", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Lapis-lazuli_hg.jpg"),
        "spinelle-bleu" to GemImageCredit("gem_spinelle_bleu", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Spinel_(Mogok_Metamorphic_Belt,_Jurassic_to_Miocene;_Momeik_Township,_Shan_State,_Burma).jpg"),
        "amethyste" to GemImageCredit("gem_amethyste", "JJ Harrison (https://jjharrison.com.au/)", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File:Amethyst._Magaliesburg,_South_Africa.jpg"),
        "cristal-de-roche" to GemImageCredit("gem_cristal_de_roche", "JJ Harrison (https://www.jjharrison.com.au/)", "CC BY-SA 2.5", "https://commons.wikimedia.org/wiki/File:Quartz,_Tibet.jpg"),
        "onyx" to GemImageCredit("gem_onyx", "Tamaghna Sengupta", "CC BY 3.0", "https://commons.wikimedia.org/wiki/File:Close_wing_position_of_Horaga_onyx_Moore,_1857_–_Common_Onyx_2.jpg"),
        "spinelle-noir" to GemImageCredit("gem_spinelle_noir", "Laurent Massi", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Multi-phase_inclusions_in_a_gem_spinel.png"),
        "tourmaline-noire" to GemImageCredit("gem_tourmaline_noire", "AdamStejskal", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Tourmaline_-_schorl_x_dravite_Pikarec_Czech_Republic.jpg"),
        "obsidienne" to GemImageCredit("gem_obsidienne", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Obsidian_&_devitrified_obsidian_gravel_(Obsidian_Cliff,_Yellowstone,_Wyoming,_USA)_4.jpg"),
        "alexandrite" to GemImageCredit("gem_alexandrite", "Parent Géry", "CC0", "https://commons.wikimedia.org/wiki/File:Chrysobéryl_var._alexandrite_sous_UV_(Brésil)_1.jpg"),
        "opale-precieuse" to GemImageCredit("gem_opale_precieuse", "James St. John", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Precious_opal_after_glendonite_(White_Cliffs_Opal_Field,_New_South_Wales,_Australia).jpg"),
        "labradorite" to GemImageCredit("gem_labradorite", "Marco Hazard from Hong Kong, Hong Kong", "CC BY-SA 2.0", "https://commons.wikimedia.org/wiki/File:Gemstone_Collection_-_Labradorite_(17278919981).jpg"),
    )

    fun creditFor(gemId: String): GemImageCredit? = credits[gemId]
}
