package fr.gemsofrod.encyclopedie.data

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Exporte l'ensemble du stock, avec ses photos, sous forme d'une archive
 * ZIP (« stock.csv » + un dossier « photos/ ») — pensée pour transférer le
 * stock d'un téléphone à un autre en un seul fichier, ou pour l'ouvrir dans
 * un tableur (le CSV, en UTF-8 avec BOM, s'ouvre proprement dans Excel).
 * Un seul export global plutôt qu'un export par fiche : le cas d'usage visé
 * est la transmission au comptable, l'ouverture dans un tableur, ou le
 * transfert vers un autre appareil — pas le partage d'une pierre
 * individuelle.
 */
object StockCsvExporter {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private fun csvField(value: String): String = "\"" + value.replace("\"", "\"\"") + "\""

    /** Contenu CSV complet (en-tête + lignes), colonne « Photo » incluse : le nom sous lequel la photo est rangée dans l'archive (dossier photos/), ou vide si la fiche n'en a pas. */
    fun buildCsv(items: List<StockItem>): String {
        val header = listOf(
            "Nom", "Référence", "Poids (ct)", "Taille", "Couleur", "Pureté", "Traitement",
            "N° certificat", "Laboratoire", "Fournisseur", "Date d'achat",
            "Coût d'achat (€)", "Prix de vente (€)", "Statut", "Notes", "Photo"
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
                item.notes,
                item.photoFileName ?: ""
            ).joinToString(";") { csvField(it) }
        }

        return (listOf(header) + rows).joinToString("\n")
    }

    /**
     * Archive complète du stock : « stock.csv » (colonne Photo référençant
     * les entrées « photos/<nom> ») plus une copie de chaque photo
     * réellement présente sur l'appareil.
     */
    fun exportZip(context: Context, items: List<StockItem>): File {
        val outDir = File(context.cacheDir, "stock_exports").apply { mkdirs() }
        val fileName = "stock_gems_of_rod_${System.currentTimeMillis()}.zip"
        val outFile = File(outDir, fileName)

        ZipOutputStream(FileOutputStream(outFile)).use { zip ->
            zip.putNextEntry(ZipEntry("stock.csv"))
            zip.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())) // BOM UTF-8, pour un import propre dans Excel
            zip.write(buildCsv(items).toByteArray(Charsets.UTF_8))
            zip.closeEntry()

            items.forEach { item ->
                val photoFileName = item.photoFileName ?: return@forEach
                val photoBytes = StockPhotoStorage.photoBytesOrNull(context, photoFileName) ?: return@forEach
                zip.putNextEntry(ZipEntry("photos/$photoFileName"))
                zip.write(photoBytes)
                zip.closeEntry()
            }
        }
        return outFile
    }
}
