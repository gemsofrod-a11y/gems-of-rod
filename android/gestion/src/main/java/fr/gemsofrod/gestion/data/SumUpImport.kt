package fr.gemsofrod.gestion.data

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.CodingErrorAction
import java.util.zip.ZipInputStream

/**
 * Import des exports Excel du tableau de bord SumUp (me.sumup.com) :
 * « items-export » (catalogue : nom, prix, quantité, catégorie…) et
 * « customers_export » (nom, email, téléphone, adresse). Lecteur .xlsx
 * minimal (zip + XML, fournis par Android), sans bibliothèque.
 */
object SumUpImport {

    enum class Kind { ARTICLES, CLIENTS }

    data class Result(val data: AppData, val kind: Kind, val created: Int, val updated: Int)

    class ImportException(message: String) : Exception(message)

    /** Lit le fichier et fusionne son contenu dans [data]. */
    fun import(bytes: ByteArray, data: AppData): Result {
        val rows = readSheet(bytes)
        if (rows.isEmpty()) throw ImportException("Le fichier est vide.")
        val header = rows.first().map { it.trim().lowercase() }
        val body = rows.drop(1).filter { r -> r.any { it.isNotBlank() } }
        return when {
            "item name" in header -> importItems(header, body, data)
            "full name" in header -> importCustomers(header, body, data)
            else -> throw ImportException("Ce fichier n'est pas un export SumUp d'articles ou de clients.")
        }
    }

    // --- Articles ---

    private fun importItems(header: List<String>, rows: List<List<String>>, data: AppData): Result {
        fun col(name: String) = header.indexOf(name)
        val iName = col("item name"); val iPrice = col("price"); val iCost = col("cost price")
        val iQty = col("quantity"); val iLow = col("low stock threshold"); val iSku = col("sku")
        val iCat = col("category"); val iDesc = col("description (online store and invoices only)")
        val iId = col("item id (do not change)"); val iUnit = col("unit"); val iOpt = col("option 1")

        var products = data.products
        var map = data.sumupProductMap
        var created = 0; var updated = 0
        for (r in rows) {
            fun cell(i: Int) = if (i in r.indices) fixText(r[i]).trim() else ""
            val base = cell(iName)
            if (base.isBlank()) continue
            // Variante (ex. taille) : « Nom — option ».
            val name = cell(iOpt).let { if (it.isNotBlank()) "$base — $it" else base }
            val sumupId = cell(iId).ifBlank { null }?.let { id -> if (cell(iOpt).isNotBlank()) "$id/${cell(iOpt)}" else id }
            val existing = products.find { sumupId != null && it.sumupId == sumupId }
                ?: products.find { normalizeName(it.name) == normalizeName(name) }
            val note = listOfNotNull(
                cell(iSku).takeIf { it.isNotBlank() }?.let { "Réf. $it" },
                stripHtml(cell(iDesc)).takeIf { it.isNotBlank() }?.let { if (it.length > 400) it.take(400).trimEnd() + "…" else it },
            ).joinToString("\n")
            val product = Product(
                id = existing?.id ?: newId(),
                name = name,
                category = category(cell(iCat), existing?.category),
                unit = unit(cell(iUnit), existing?.unit),
                quantity = number(cell(iQty)) ?: existing?.quantity ?: 0.0,
                cost = number(cell(iCost))?.takeIf { it > 0 } ?: existing?.cost ?: 0.0,
                price = number(cell(iPrice)) ?: existing?.price ?: 0.0,
                threshold = number(cell(iLow)) ?: existing?.threshold ?: 0.0,
                note = note.ifBlank { existing?.note.orEmpty() },
                sumupId = sumupId,
            )
            products = if (existing != null) products.map { if (it.id == existing.id) product else it } else products + product
            // Le nom SumUp sert aussi à reconnaître l'article dans les ventes.
            map = map + (normalizeName(base) to product.id) + (normalizeName(name) to product.id)
            if (existing != null) updated++ else created++
        }
        return Result(data.copy(products = products, sumupProductMap = map), Kind.ARTICLES, created, updated)
    }

    private fun category(text: String, fallback: Category?): Category {
        val t = normalizeName(text)
        return when {
            t.contains("precieuse") -> Category.PRECIEUSE
            t.contains("bijou") || t.contains("collaboration") -> Category.BIJOU
            t.contains("metal") || t.contains("or ") || t == "or" -> Category.METAL
            t.contains("pierre") -> Category.FINE
            t.isBlank() -> fallback ?: Category.FINE
            else -> Category.AUTRE
        }
    }

    private fun unit(text: String, fallback: StockUnit?): StockUnit {
        val t = text.lowercase()
        return when {
            t.contains("carat") || t.startsWith("ct") -> StockUnit.CARAT
            t.startsWith("g") || t.contains("gram") -> StockUnit.GRAMME
            t.isBlank() -> fallback ?: StockUnit.PIECE
            else -> StockUnit.PIECE
        }
    }

    // --- Clients ---

    private fun importCustomers(header: List<String>, rows: List<List<String>>, data: AppData): Result {
        fun col(name: String) = header.indexOf(name)
        val iName = col("full name"); val iEmail = col("email"); val iPhone = col("phone")
        val iA1 = col("address line 1"); val iA2 = col("address line 2"); val iCity = col("city")
        val iZip = col("zip code"); val iCountry = col("country"); val iVat = col("vat id"); val iNote = col("note")

        var clients = data.clients
        var created = 0; var updated = 0
        for (r in rows) {
            fun cell(i: Int) = if (i in r.indices) fixText(r[i]).trim() else ""
            val name = cell(iName)
            if (name.isBlank()) continue
            val email = cell(iEmail)
            val existing = clients.find { email.isNotBlank() && it.email.equals(email, ignoreCase = true) }
                ?: clients.find { normalizeName(it.name) == normalizeName(name) }
            val address = listOf(
                cell(iA1), cell(iA2).takeUnless { it == "0.0" || it == "0" }.orEmpty(),
                listOf(integerText(cell(iZip)), cell(iCity)).filter { it.isNotBlank() }.joinToString(" "),
                cell(iCountry),
            ).filter { it.isNotBlank() }.joinToString(", ")
            val note = listOfNotNull(
                address.takeIf { it.isNotBlank() }?.let { "Adresse : $it" },
                cell(iVat).takeIf { it.isNotBlank() }?.let { "TVA : $it" },
                cell(iNote).takeIf { it.isNotBlank() },
            ).joinToString("\n")
            val client = Client(
                id = existing?.id ?: newId(),
                name = name,
                email = email.ifBlank { existing?.email.orEmpty() },
                phone = cell(iPhone).ifBlank { existing?.phone.orEmpty() },
                segment = existing?.segment ?: Segment.OCCASIONNEL,
                note = if (existing != null && existing.note.isNotBlank() && note.isNotBlank() && !existing.note.contains(note)) "${existing.note}\n$note"
                else note.ifBlank { existing?.note.orEmpty() },
            )
            clients = if (existing != null) clients.map { if (it.id == existing.id) client else it } else clients + client
            if (existing != null) updated++ else created++
        }
        return Result(data.copy(clients = clients), Kind.CLIENTS, created, updated)
    }

    // --- Outils ---

    private fun number(text: String): Double? = text.replace(',', '.').toDoubleOrNull()

    /** « 75001.0 » → « 75001 » (codes postaux lus comme nombres). */
    private fun integerText(text: String): String =
        text.toDoubleOrNull()?.takeIf { it % 1.0 == 0.0 }?.toLong()?.toString() ?: text

    /** Retire le HTML des descriptions (balises, entités courantes). */
    fun stripHtml(html: String): String = html
        .replace(Regex("<br\\s*/?>|</p>", RegexOption.IGNORE_CASE), "\n")
        .replace(Regex("<[^>]+>"), "")
        .replace("&nbsp;", " ").replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&#39;", "'")
        .lines().map { it.trim() }.filter { it.isNotEmpty() }.joinToString("\n")

    /**
     * Répare le texte des exports SumUp : séquences Excel « _x0080_ » et
     * accents encodés deux fois (« AmÃ©trine » → « Amétrine »).
     */
    fun fixText(raw: String): String {
        val unescaped = Regex("_x([0-9A-Fa-f]{4})_").replace(raw) { it.groupValues[1].toInt(16).toChar().toString() }
        if (unescaped.none { it.code in 0x80..0xFF } || unescaped.any { it.code > 0xFF }) return unescaped
        val bytes = ByteArray(unescaped.length) { unescaped[it].code.toByte() }
        return try {
            Charsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes)).toString()
        } catch (e: CharacterCodingException) {
            unescaped // déjà correct (vrai latin-1)
        }
    }

    /** Première feuille du classeur, en lignes de cellules (colonnes vides comprises). */
    fun readSheet(bytes: ByteArray): List<List<String>> {
        var shared: ByteArray? = null
        var sheet: ByteArray? = null
        try {
            ZipInputStream(ByteArrayInputStream(bytes)).use { zip ->
                while (true) {
                    val e = zip.nextEntry ?: break
                    when (e.name) {
                        "xl/sharedStrings.xml" -> shared = zip.readBytes()
                        "xl/worksheets/sheet1.xml" -> sheet = zip.readBytes()
                    }
                }
            }
        } catch (e: Exception) {
            throw ImportException("Fichier illisible : choisissez le fichier Excel (.xlsx) exporté de SumUp.")
        }
        val sheetBytes = sheet ?: throw ImportException("Fichier illisible : choisissez le fichier Excel (.xlsx) exporté de SumUp.")
        val strings = shared?.let { parseSharedStrings(it) } ?: emptyList()
        return parseSheet(sheetBytes, strings)
    }

    private fun parser(bytes: ByteArray): XmlPullParser =
        Xml.newPullParser().apply { setInput(ByteArrayInputStream(bytes), "UTF-8") }

    private fun parseSharedStrings(bytes: ByteArray): List<String> {
        val p = parser(bytes)
        val out = mutableListOf<String>()
        val current = StringBuilder()
        var inT = false
        while (p.next() != XmlPullParser.END_DOCUMENT) {
            when (p.eventType) {
                XmlPullParser.START_TAG -> when (p.name) {
                    "si" -> current.setLength(0)
                    "t" -> inT = true
                }
                XmlPullParser.TEXT -> if (inT) current.append(p.text)
                XmlPullParser.END_TAG -> when (p.name) {
                    "t" -> inT = false
                    "si" -> out += current.toString()
                }
            }
        }
        return out
    }

    private fun parseSheet(bytes: ByteArray, strings: List<String>): List<List<String>> {
        val p = parser(bytes)
        val rows = mutableListOf<List<String>>()
        var row = sortedMapOf<Int, String>()
        var col = 0
        var type: String? = null
        val value = StringBuilder()
        var inValue = false
        while (p.next() != XmlPullParser.END_DOCUMENT) {
            when (p.eventType) {
                XmlPullParser.START_TAG -> when (p.name) {
                    "row" -> row = sortedMapOf()
                    "c" -> {
                        col = columnIndex(p.getAttributeValue(null, "r").orEmpty(), row.size)
                        type = p.getAttributeValue(null, "t")
                        value.setLength(0)
                    }
                    "v", "t" -> inValue = true
                }
                XmlPullParser.TEXT -> if (inValue) value.append(p.text)
                XmlPullParser.END_TAG -> when (p.name) {
                    "v", "t" -> inValue = false
                    "c" -> row[col] = if (type == "s") value.toString().toIntOrNull()?.let { strings.getOrNull(it) }.orEmpty() else value.toString()
                    "row" -> {
                        val width = (row.keys.maxOrNull() ?: -1) + 1
                        rows += List(width) { row[it].orEmpty() }
                    }
                }
            }
        }
        return rows
    }

    /** « AC12 » → 28 (colonnes à partir de 0) ; sans référence, colonne suivante. */
    private fun columnIndex(ref: String, fallback: Int): Int {
        val letters = ref.takeWhile { it.isLetter() }.uppercase()
        if (letters.isEmpty()) return fallback
        return letters.fold(0) { acc, c -> acc * 26 + (c - 'A' + 1) } - 1
    }
}
