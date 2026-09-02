package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Locale

data class StockCsvImportResult(val imported: Int, val skipped: Int)

/**
 * Importe un fichier CSV généré par [StockCsvExporter] (même format : « ; »
 * comme séparateur, champs entre guillemets, en-tête sur la première
 * ligne) — pensé pour transférer le stock d'un téléphone à un autre via le
 * fichier exporté. Chaque ligne valide devient une nouvelle fiche : aucune
 * correspondance avec le stock existant, un import répété du même fichier
 * crée donc des doublons. Les photos ne font pas partie du CSV et ne sont
 * donc jamais réimportées.
 */
object StockCsvImporter {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    private const val EXPECTED_COLUMNS = 15

    fun import(context: Context, uri: Uri): StockCsvImportResult {
        val text = context.contentResolver.openInputStream(uri)?.use { stream ->
            stream.readBytes().toString(Charsets.UTF_8)
        } ?: return StockCsvImportResult(0, 0)

        // Retire le BOM UTF-8 ajouté par l'export, sinon la première colonne
        // du premier champ ("Nom") contiendrait le caractère invisible.
        val cleaned = text.removePrefix("﻿")
        val rows = parseCsv(cleaned)
        if (rows.size <= 1) return StockCsvImportResult(0, 0)

        var imported = 0
        var skipped = 0
        rows.drop(1).forEach { fields -> // ignore la ligne d'en-tête
            val item = fieldsToItem(fields)
            if (item != null) {
                StockRepository.addItem(item)
                imported++
            } else {
                skipped++
            }
        }
        return StockCsvImportResult(imported, skipped)
    }

    private fun fieldsToItem(fields: List<String>): StockItem? {
        if (fields.size < EXPECTED_COLUMNS) return null
        val nom = fields[0]
        if (nom.isBlank()) return null
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
            photoFileName = null,
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
