package fr.gemsofrod.encyclopedie.data

// Fichier généré automatiquement par scripts/fetch_lapidaire_diagrams.py
// à partir de android/lapidaire_diagram_credits.json (Wikimedia Commons / Openverse).
// Ne pas éditer à la main : relancer le workflow "Fetch lapidaire diagrams".

data class LapidaireDiagramCredit(
    val drawableName: String,
    val author: String,
    val license: String,
    val sourceUrl: String
)

object LapidaireDiagrams {
    private val credits: Map<String, LapidaireDiagramCredit> = mapOf(
        "machine_facettage_moderne" to LapidaireDiagramCredit("lapidaire_machine_facettage_moderne", "Kent County Council, Jo Ahmet, 2019-12-04 11:28:30", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File:Possible_roman_facetted_bead_(FindID_983752-1083997).jpg"),
        "moulin_taille_historique" to LapidaireDiagramCredit("lapidaire_moulin_taille_historique", "Auteur non renseigné", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Diamondworks;_interior_view,_a_wheel_used_by_diamond_cutters_Wellcome_V0023689.jpg"),
    )

    fun creditFor(diagramId: String): LapidaireDiagramCredit? = credits[diagramId]
}
