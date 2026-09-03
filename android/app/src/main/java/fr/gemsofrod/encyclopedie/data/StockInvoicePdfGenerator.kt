package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import fr.gemsofrod.encyclopedie.R
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Génère un reçu de vente PDF (une page A4) pour une pierre du stock déjà
 * marquée vendue — même patron que [CertificatePdfGenerator] et
 * [FavoritesPdfGenerator] : uniquement les API graphiques natives d'Android,
 * aucune dépendance externe. Ce n'est volontairement PAS présenté comme une
 * facture légale complète (l'app ne connaît ni SIRET ni régime de TVA de
 * l'utilisateur) — un disclaimer en ce sens est imprimé sur le document
 * lui-même (stock_invoice_disclaimer).
 */
object StockInvoicePdfGenerator {
    private const val PAGE_WIDTH = 595 // A4 à 72 dpi
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f

    fun generate(context: Context, item: StockItem): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val companyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 13f
            isFakeBoldText = true
        }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 22f
            isFakeBoldText = true
        }
        val gemNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 16f
            isFakeBoldText = true
        }
        val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 11f
        }
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 11f
        }
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 12f
            isFakeBoldText = true
        }
        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
        }
        val totalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 16f
            isFakeBoldText = true
        }

        var y = MARGIN

        canvas.drawText("Gems of Rod", MARGIN, y + 14f, companyPaint)
        y += 16f
        canvas.drawText(context.getString(R.string.stock_invoice_company_contact), MARGIN, y + 8f, subtitlePaint)
        y += 28f

        canvas.drawText(context.getString(R.string.stock_invoice_pdf_title), MARGIN, y + 18f, titlePaint)
        y += 24f

        val saleDate = item.venteDateMillis?.let { Date(it) } ?: Date()
        val locale = context.resources.configuration.locales[0]
        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale)
        val invoiceNumber = "GOR-" +
            SimpleDateFormat("yyyyMMdd", Locale.FRANCE).format(saleDate) +
            "-" + item.id.take(6).uppercase(Locale.FRANCE)
        canvas.drawText(context.getString(R.string.stock_invoice_number_format, invoiceNumber), MARGIN, y + 8f, subtitlePaint)
        y += 14f
        canvas.drawText(context.getString(R.string.stock_invoice_date_format, dateFormat.format(saleDate)), MARGIN, y + 8f, subtitlePaint)
        y += 24f
        canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
        y += 24f

        if (item.acheteurNom.isNotBlank()) {
            canvas.drawText(context.getString(R.string.stock_invoice_buyer_format, item.acheteurNom), MARGIN, y + 10f, valuePaint)
            y += 26f
        }

        canvas.drawText(item.nom, MARGIN, y + 14f, gemNamePaint)
        y += 30f

        fun row(label: String, value: String) {
            if (value.isBlank()) return
            canvas.drawText(label, MARGIN, y + 12f, labelPaint)
            canvas.drawText(value, MARGIN + 160f, y + 12f, valuePaint)
            y += 22f
        }

        row(context.getString(R.string.stock_field_reference_label), item.reference)
        row(context.getString(R.string.stock_field_weight_label), item.poidsCarats?.let { "$it ct" }.orEmpty())
        row(context.getString(R.string.stock_field_cut_label), item.taille)
        row(context.getString(R.string.stock_field_color_label), item.couleur)
        row(context.getString(R.string.stock_field_clarity_label), item.purete)
        row(context.getString(R.string.stock_field_treatment_label), item.traitement)
        row(context.getString(R.string.stock_field_certificate_number_label), item.certificatNumero)
        row(context.getString(R.string.stock_field_laboratory_label), item.laboratoire)

        y += 14f
        canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
        y += 28f

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        canvas.drawText(
            context.getString(R.string.stock_invoice_total_format, currencyFormat.format(item.prixVente ?: 0.0)),
            MARGIN, y + 14f, totalPaint
        )
        y += 36f

        if (item.factureNotes.isNotBlank()) {
            val contentWidth = PAGE_WIDTH - MARGIN * 2
            wrapText(item.factureNotes, valuePaint, contentWidth).forEach { line ->
                canvas.drawText(line, MARGIN, y + 10f, valuePaint)
                y += 16f
            }
            y += 8f
        }

        canvas.drawText(context.getString(R.string.stock_invoice_disclaimer), MARGIN, y, subtitlePaint)

        document.finishPage(page)

        val outDir = File(context.cacheDir, "stock_invoices").apply { mkdirs() }
        val fileName = "recu_vente_${item.id}_${System.currentTimeMillis()}.pdf"
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()

        return outFile
    }

    /** Découpe un texte libre en lignes qui tiennent dans [maxWidth], mot par mot. */
    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        for (paragraph in text.split("\n")) {
            var current = StringBuilder()
            for (word in paragraph.split(" ")) {
                val candidate = if (current.isEmpty()) word else "$current $word"
                if (paint.measureText(candidate) > maxWidth && current.isNotEmpty()) {
                    lines.add(current.toString())
                    current = StringBuilder(word)
                } else {
                    current = StringBuilder(candidate)
                }
            }
            lines.add(current.toString())
        }
        return lines
    }
}
