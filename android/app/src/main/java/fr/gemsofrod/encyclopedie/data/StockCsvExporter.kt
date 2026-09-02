package fr.gemsofrod.encyclopedie.data

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Exporte l'ensemble du stock en CSV (encodage UTF-8 avec BOM pour un import
 * propre dans Excel), partageable ensuite via FileProvider comme le
 * certificat PDF. Un seul export global plutôt qu'un export par fiche : le
 * cas d'usage visé est la transmission au comptable ou l'ouverture dans un
 * tableur, pas le partage d'une pierre individuelle.
 */
object StockCsvExporter {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private fun csvField(value: String): String = "\"" + value.replace("\"", "\"\"") + "\""

    fun export(context: Context, items: List<StockItem>): File {
        val header = listOf(
            "Nom", "Référence", "Poids (ct)", "Taille", "Couleur", "Pureté", "Traitement",
            "N° certificat", "Laboratoire", "Fournisseur", "Date d'achat",
            "Coût d'achat (€)", "Prix de vente (€)", "Statut", "Notes"
        ).joinToString(";") { csvField(it) }

        val rows = items.sortedByDescending { it.createdAtMillis }.map { item ->
            listOf(
                item.nom,
                item.reference,
                item.poidsCarats?.toString() ?: "",
                item.taille,
                item.couleur,
                item.purete,
                item.traitement,
                item.certificatNumero,
                item.laboratoire,
                item.fournisseur,
                item.dateAchatMillis?.let { dateFormat.format(it) } ?: "",
                item.coutAchat?.toString() ?: "",
                item.prixVente?.toString() ?: "",
                item.statut.name,
                item.notes
            ).joinToString(";") { csvField(it) }
        }

        val content = (listOf(header) + rows).joinToString("\n")

        val outDir = File(context.cacheDir, "stock_exports").apply { mkdirs() }
        val fileName = "stock_gems_of_rod_${System.currentTimeMillis()}.csv"
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { out ->
            out.write(0xEF); out.write(0xBB); out.write(0xBF) // BOM UTF-8, pour un import propre dans Excel
            out.write(content.toByteArray(Charsets.UTF_8))
        }
        return outFile
    }
}
