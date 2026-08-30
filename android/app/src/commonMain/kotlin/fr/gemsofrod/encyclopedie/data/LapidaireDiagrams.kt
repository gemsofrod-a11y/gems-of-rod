package fr.gemsofrod.encyclopedie.data

// Fichier généré automatiquement par scripts/fetch_lapidaire_diagrams.py
// à partir de android/lapidaire_diagram_credits.json (Wikimedia Commons / Openverse).
// Ne pas éditer à la main : relancer le workflow "Fetch lapidaire diagrams".
//
// Encore vide : le workflow n'a pas encore tourné pour cette section. Tant
// qu'une entrée n'existe pas ici, LapidaireDiagrams.creditFor(id) renvoie
// null et l'écran affiche la légende sans image plutôt qu'un espace vide.

data class LapidaireDiagramCredit(
    val drawableName: String,
    val author: String,
    val license: String,
    val sourceUrl: String
)

object LapidaireDiagrams {
    private val credits: Map<String, LapidaireDiagramCredit> = mapOf()

    fun creditFor(diagramId: String): LapidaireDiagramCredit? = credits[diagramId]
}
