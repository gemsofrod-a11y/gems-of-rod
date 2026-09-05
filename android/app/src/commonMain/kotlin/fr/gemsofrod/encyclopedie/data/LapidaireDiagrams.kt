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
        "brillant_rond_proportions" to LapidaireDiagramCredit("lapidaire_brillant_rond_proportions", "Gems of Rod", "Illustration originale — tous droits réservés", "https://gems-of-rod.fr"),
        "machine_facettage_moderne" to LapidaireDiagramCredit("lapidaire_machine_facettage_moderne", "Doug Coldwell", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File:Faceting_machine.JPG"),
        "machine_perceuse_gemmes" to LapidaireDiagramCredit("lapidaire_machine_perceuse_gemmes", "Henry G. Gilbert Nursery and Seed Trade Catalog Collection.; Peter Henderson & Co.", "Public domain", "https://commons.wikimedia.org/wiki/File:Everything_for_the_garden_(16234349223).jpg"),
        "moulin_taille_historique" to LapidaireDiagramCredit("lapidaire_moulin_taille_historique", "Auteur non renseigné", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File:Diamondworks;_interior_view,_a_wheel_used_by_diamond_cutters_Wellcome_V0023689.jpg"),
        "trajet_lumiere_pavillon" to LapidaireDiagramCredit("lapidaire_trajet_lumiere_pavillon", "Gems of Rod", "Illustration originale — tous droits réservés", "https://gems-of-rod.fr"),
    )

    fun creditFor(diagramId: String): LapidaireDiagramCredit? = credits[diagramId]
}
