package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.net.Uri
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.zip.ZipInputStream

data class StockCsvImportResult(val imported: Int, val skipped: Int, val updated: Int = 0)

/**
 * Importe une archive générée par [StockCsvExporter.exportZip] (« ; »
 * comme séparateur, champs entre guillemets, en-tête sur la première
 * ligne, photos dans « photos/ ») — pensé pour transférer le stock d'un
 * téléphone à un autre, photos comprises. Accepte aussi un simple fichier
 * .csv sans photos (anciens exports, ou fichier édité à la main) : le
 * format est détecté à partir du contenu, pas de l'extension.
 *
 * Chaque ligne est rapprochée du stock existant avant d'être écrite (voir
 * [findDuplicate]) : une correspondance met à jour la fiche existante au
 * lieu d'en créer une nouvelle, pour qu'un import répété du même fichier
 * (catalogue mis à jour, nouvel essai après une première importation)
 * n'empile pas de doublons.
 */
object StockCsvImporter {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    private const val MIN_COLUMNS = 15
    private val ZIP_MAGIC = byteArrayOf(0x50, 0x4B) // "PK"

    fun import(context: Context, uri: Uri): StockCsvImportResult {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return StockCsvImportResult(0, 0)

        return if (bytes.size >= 2 && bytes[0] == ZIP_MAGIC[0] && bytes[1] == ZIP_MAGIC[1]) {
            importZip(context, bytes)
        } else {
            importRows(context, bytes.toString(Charsets.UTF_8), emptyMap())
        }
    }

    private fun importZip(context: Context, bytes: ByteArray): StockCsvImportResult {
        var csvText: String? = null
        val photos = mutableMapOf<String, ByteArray>()

        ZipInputStream(ByteArrayInputStream(bytes)).use { zip ->
            generateSequence { zip.nextEntry }.forEach { entry ->
                val content = zip.readBytes()
                when {
                    entry.name == "stock.csv" -> csvText = content.toString(Charsets.UTF_8)
                    entry.name.startsWith("photos/") -> photos[entry.name.removePrefix("photos/")] = content
                }
                zip.closeEntry()
            }
        }

        val text = csvText ?: return StockCsvImportResult(0, 0)
        return importRows(context, text, photos)
    }

    private fun importRows(context: Context, text: String, photos: Map<String, ByteArray>): StockCsvImportResult {
        // Retire le BOM UTF-8 ajouté par l'export, sinon la première colonne
        // du premier champ ("Nom") contiendrait le caractère invisible.
        val cleaned = text.removePrefix("﻿")
        val rows = parseCsv(cleaned)
        if (rows.size <= 1) return StockCsvImportResult(0, 0)

        var imported = 0
        var updated = 0
        var skipped = 0
        rows.drop(1).forEach { fields -> // ignore la ligne d'en-tête
            val item = fieldsToItem(context, fields, photos)
            if (item == null) {
                skipped++
                return@forEach
            }
            val existing = findDuplicate(item)
            if (existing != null) {
                // Conserve l'identité, la date de création et le suivi de
                // vente de la fiche existante ; seuls les champs présents
                // dans le CSV sont écrasés par les valeurs importées.
                StockRepository.updateItem(
                    item.copy(
                        id = existing.id,
                        createdAtMillis = existing.createdAtMillis,
                        photoFileName = item.photoFileName ?: existing.photoFileName,
                        acheteurNom = existing.acheteurNom,
                        venteDateMillis = existing.venteDateMillis,
                        factureNotes = existing.factureNotes
                    )
                )
                updated++
            } else {
                StockRepository.addItem(item)
                imported++
            }
        }
        return StockCsvImportResult(imported, skipped, updated)
    }

    /**
     * Cherche une fiche déjà en stock correspondant à [item] : d'abord par
     * référence (si renseignée des deux côtés), sinon par nom et poids
     * identiques — les deux seuls champs qu'on peut raisonnablement
     * attendre stables d'un import à l'autre pour une pierre sans
     * référence.
     */
    private fun findDuplicate(item: StockItem): StockItem? {
        val reference = item.reference.trim()
        if (reference.isNotEmpty()) {
            StockRepository.allItems().find { it.reference.trim().equals(reference, ignoreCase = true) }?.let { return it }
        }
        val nom = item.nom.trim()
        return StockRepository.allItems().find {
            it.nom.trim().equals(nom, ignoreCase = true) && it.poidsCarats == item.poidsCarats
        }
    }

    private fun fieldsToItem(context: Context, fields: List<String>, photos: Map<String, ByteArray>): StockItem? {
        if (fields.size < MIN_COLUMNS) return null
        val nom = fields[0]
        if (nom.isBlank()) return null

        // Ne réutilise jamais le nom de fichier de l'archive : un nom local
        // frais évite toute collision avec une photo déjà présente ici.
        val photoFileName = fields.getOrNull(15)
            ?.takeIf { it.isNotBlank() }
            ?.let { key -> photos[key] }
            ?.let { imageBytes -> StockPhotoStorage.saveImportedPhoto(context, imageBytes) }

        return StockItem(
            id = "",
            nom = nom,
            reference = fields[1],
            catalogGemId = null,
            poidsCarats = fields[2].toDoubleOrNull(),
            taille = fields[3],
            couleur = fields[4],
            purete = fields[5],
            traitement = fields[6],
            certificatNumero = fields[7],
            laboratoire = fields[8],
            fournisseur = fields[9],
            dateAchatMillis = runCatching { dateFormat.parse(fields[10])?.time }.getOrNull(),
            coutAchat = fields[11].toDoubleOrNull(),
            prixVente = fields[12].toDoubleOrNull(),
            statut = runCatching { StockStatus.valueOf(fields[13]) }.getOrDefault(StockStatus.EN_STOCK),
            notes = fields[14],
            photoFileName = photoFileName,
            createdAtMillis = 0L
        )
    }

    /**
     * Analyseur CSV minimal (« ; » + champs entre guillemets, « "" » pour
     * échapper un guillemet interne) qui suit le format écrit par
     * [StockCsvExporter]. Gère les retours à la ligne à l'intérieur d'un
     * champ entre guillemets (des notes multi-lignes, par exemple) en
     * traitant le texte caractère par caractère plutôt qu'en découpant par
     * ligne — un simple split sur '\n' couperait un tel champ en deux.
     */
    private fun parseCsv(text: String): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        var field = StringBuilder()
        var row = mutableListOf<String>()
        var inQuotes = false
        var i = 0
        while (i < text.length) {
            val c = text[i]
            when {
                inQuotes -> {
                    if (c == '"') {
                        if (i + 1 < text.length && text[i + 1] == '"') {
                            field.append('"')
                            i++
                        } else {
                            inQuotes = false
                        }
                    } else {
                        field.append(c)
                    }
                }
                c == '"' -> inQuotes = true
                c == ';' -> {
                    row.add(field.toString())
                    field = StringBuilder()
                }
                c == '\r' -> {} // ignoré, la fin de ligne est gérée par '\n'
                c == '\n' -> {
                    row.add(field.toString())
                    field = StringBuilder()
                    if (row.size > 1 || row[0].isNotEmpty()) rows.add(row)
                    row = mutableListOf()
                }
                else -> field.append(c)
            }
            i++
        }
        if (field.isNotEmpty() || row.isNotEmpty()) {
            row.add(field.toString())
            rows.add(row)
        }
        return rows
    }
}
