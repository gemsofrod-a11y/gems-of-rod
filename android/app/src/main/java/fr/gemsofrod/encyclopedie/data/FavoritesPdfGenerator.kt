package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import fr.gemsofrod.encyclopedie.R
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.util.Date

/**
 * Génère un récapitulatif PDF (une ou plusieurs pages A4) de la collection de
 * favoris de l'utilisateur, une fiche par pierre. Même patron que
 * [CertificatePdfGenerator] : uniquement les API graphiques natives
 * d'Android (android.graphics.pdf), aucune dépendance externe.
 */
object FavoritesPdfGenerator {
    private const val PAGE_WIDTH = 595 // A4 à 72 dpi
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f
    private const val ROW_HEIGHT = 70f

    fun generate(context: Context, gems: List<Gem>): File {
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 20f
            isFakeBoldText = true
        }
        val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 11f
        }
        val gemNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 14f
            isFakeBoldText = true
        }
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 10f
        }
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 11f
        }
        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
        }

        val document = PdfDocument()
        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        fun drawHeader() {
            canvas.drawText(context.getString(R.string.favorites_pdf_title), MARGIN, y + 16f, titlePaint)
            y += 22f
            val locale = context.resources.configuration.locales[0]
            val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale)
            canvas.drawText(
                context.getString(R.string.favorites_pdf_generated_on, dateFormat.format(Date())),
                MARGIN,
                y + 8f,
                subtitlePaint
            )
            y += 14f
            canvas.drawText(
                context.getString(R.string.favorites_pdf_count_format, gems.size),
                MARGIN,
                y + 8f,
                subtitlePaint
            )
            y += 26f
            canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
            y += 20f
        }

        fun newPage() {
            document.finishPage(page)
            pageNumber += 1
            pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            page = document.startPage(pageInfo)
            canvas = page.canvas
            y = MARGIN
        }

        drawHeader()

        val labelX = MARGIN
        val valueX = MARGIN + 90f
        val columnGap = 140f

        for (gem in gems) {
            if (y + ROW_HEIGHT > PAGE_HEIGHT - MARGIN) {
                newPage()
            }
            canvas.drawText(gem.nom, labelX, y + 14f, gemNamePaint)
            y += 20f
            canvas.drawText(context.getString(R.string.fiche_famille), labelX, y + 10f, labelPaint)
            canvas.drawText(gem.famille, valueX, y + 10f, valuePaint)
            canvas.drawText(context.getString(R.string.fiche_durete), labelX + columnGap * 2, y + 10f, labelPaint)
            canvas.drawText(gem.durete, valueX + columnGap * 2, y + 10f, valuePaint)
            y += 16f
            canvas.drawText(context.getString(R.string.fiche_prix_indicatif), labelX, y + 10f, labelPaint)
            canvas.drawText(gem.prixCaratEur, valueX, y + 10f, valuePaint)
            y += 20f
            canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
            y += 14f
        }

        document.finishPage(page)

        val outDir = File(context.cacheDir, "favorites").apply { mkdirs() }
        val fileName = "favoris_gems_of_rod_${System.currentTimeMillis()}.pdf"
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()

        return outFile
    }
}
