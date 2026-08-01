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
    private val credits: Map<String, GemImageCredit> = mapOf()

    fun creditFor(gemId: String): GemImageCredit? = credits[gemId]
}
