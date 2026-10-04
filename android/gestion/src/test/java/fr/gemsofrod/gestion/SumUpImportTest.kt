package fr.gemsofrod.gestion

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Category
import fr.gemsofrod.gestion.data.SumUpImport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Import des exports Excel SumUp, sur des fichiers fabriqués au même format
 * (jamais de vraies données de clients dans le dépôt).
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [33])
class SumUpImportTest {

    /** Classeur .xlsx minimal : une feuille, textes partagés. */
    private fun xlsx(rows: List<List<String>>): ByteArray {
        val strings = rows.flatten().distinct()
        fun esc(s: String) = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        fun colName(i: Int): String = if (i < 26) ('A' + i).toString() else colName(i / 26 - 1) + ('A' + i % 26)
        val shared = strings.joinToString("", "<sst xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">", "</sst>") {
            "<si><t xml:space=\"preserve\">${esc(it)}</t></si>"
        }
        val sheet = rows.mapIndexed { r, row ->
            row.mapIndexedNotNull { c, v ->
                if (v.isEmpty()) null else "<c r=\"${colName(c)}${r + 1}\" t=\"s\"><v>${strings.indexOf(v)}</v></c>"
            }.joinToString("", "<row r=\"${r + 1}\">", "</row>")
        }.joinToString("", "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"><sheetData>", "</sheetData></worksheet>")
        val out = ByteArrayOutputStream()
        ZipOutputStream(out).use { z ->
            z.putNextEntry(ZipEntry("xl/sharedStrings.xml")); z.write(shared.toByteArray()); z.closeEntry()
            z.putNextEntry(ZipEntry("xl/worksheets/sheet1.xml")); z.write(sheet.toByteArray()); z.closeEntry()
        }
        return out.toByteArray()
    }

    private val itemHeader = listOf(
        "Item name", "Variations", "Option set 1", "Option 1", "Price", "Cost price", "Unit",
        "Track inventory? (Yes/No)", "Quantity", "Low stock threshold", "SKU",
        "Description (Online Store and Invoices only)", "Category", "Item id (Do not change)",
    )

    @Test
    fun articlesAvecAccentsRepares() {
        // « AmÃ©trine » : accent encodé deux fois, comme dans les exports SumUp.
        val file = xlsx(
            listOf(
                itemHeader,
                listOf("AmÃ©trine radiant â_x0080__x0094_ 8 carats", "", "", "", "130.0", "", "each.each", "Yes", "2.0", "0.0", "AMT-1", "<p>Belle <strong>pierre</strong></p>", "Pierres Fines", "id-1"),
                listOf("Saphir Padparadscha 0,38 carat", "", "", "", "2300.0", "", "each.each", "Yes", "1.0", "0.0", "", "", "Pierres PrÃ©cieuses", "id-2"),
                listOf("Loupe 15x", "", "", "", "20.0", "", "each.each", "Yes", "4.0", "0.0", "", "", "MatÃ©riel", "id-3"),
            ),
        )
        val result = SumUpImport.import(file, AppData())
        assertEquals(SumUpImport.Kind.ARTICLES, result.kind)
        assertEquals(3, result.created)
        val p = result.data.products
        assertEquals("Amétrine radiant — 8 carats", p[0].name)
        assertEquals(130.0, p[0].price, 0.001)
        assertEquals(2.0, p[0].quantity, 0.001)
        assertEquals(Category.FINE, p[0].category)
        assertTrue(p[0].note.contains("Réf. AMT-1"))
        assertTrue(p[0].note.contains("Belle pierre"))
        assertEquals(Category.PRECIEUSE, p[1].category)
        assertEquals(Category.AUTRE, p[2].category)

        // Ré-import : mise à jour (nouvelle quantité), pas de doublon.
        val again = xlsx(
            listOf(itemHeader, listOf("Loupe 15x", "", "", "", "22.0", "", "each.each", "Yes", "3.0", "0.0", "", "", "MatÃ©riel", "id-3")),
        )
        val second = SumUpImport.import(again, result.data)
        assertEquals(0, second.created)
        assertEquals(1, second.updated)
        assertEquals(3, second.data.products.size)
        assertEquals(3.0, second.data.products.first { it.name == "Loupe 15x" }.quantity, 0.001)
    }

    @Test
    fun clients() {
        val file = xlsx(
            listOf(
                listOf("Creation date", "Full name", "Email", "Phone", "Is marketing consent accepted? (Yes/No)", "Address Line 1", "Address Line 2", "City", "Zip code", "Country", "VAT ID", "Note", "Products"),
                listOf("46000.5", "Client Exemple", "client@exemple.fr", "+33600000000", "No", "1 rue de l'Exemple", "0.0", "Paris", "75001.0", "FR", "", "", "online_store"),
            ),
        )
        val result = SumUpImport.import(file, AppData())
        assertEquals(SumUpImport.Kind.CLIENTS, result.kind)
        val c = result.data.clients.single()
        assertEquals("Client Exemple", c.name)
        assertEquals("client@exemple.fr", c.email)
        assertEquals("+33600000000", c.phone)
        assertTrue(c.note, c.note.contains("Adresse : 1 rue de l'Exemple, 75001 Paris, FR"))
    }
}
