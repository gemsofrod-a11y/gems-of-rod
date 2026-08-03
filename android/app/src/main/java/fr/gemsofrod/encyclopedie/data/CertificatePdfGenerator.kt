package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Informations propres à un exemplaire physique de pierre, renseignées par
 * l'utilisateur pour la génération d'un certificat — distinctes des données
 * génériques de l'espèce (fourchette de prix, dureté, etc.) déjà présentes
 * dans [Gem].
 */
data class CertificateInfo(
    val dimensions: String,
    val poids: String,
    val origine: String,
    val traitement: String
)

/**
 * Génère un certificat d'authenticité au format PDF (une page A4) pour une
 * gemme donnée, avec photo optionnelle de l'exemplaire. N'utilise que les
 * API graphiques natives d'Android (android.graphics.pdf), aucune
 * dépendance externe.
 */
object CertificatePdfGenerator {
    private const val PAGE_WIDTH = 595 // A4 à 72 dpi
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f

    fun generate(context: Context, gem: Gem, photo: Bitmap?, info: CertificateInfo): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 22f
            isFakeBoldText = true
        }
        val gemNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 18f
            isFakeBoldText = true
        }
        val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 11f
            isFakeBoldText = false
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

        var y = MARGIN

        canvas.drawText("GEMS OF ROD", MARGIN, y + 18f, titlePaint)
        y += 24f
        canvas.drawText("Certificat d'authenticité — gemsofrod@gmail.com", MARGIN, y + 8f, subtitlePaint)
        y += 22f
        canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
        y += 26f

        if (photo != null) {
            val maxWidth = PAGE_WIDTH - MARGIN * 2
            val maxHeight = 240f
            val scale = minOf(maxWidth / photo.width, maxHeight / photo.height)
            val destWidth = photo.width * scale
            val destHeight = photo.height * scale
            val destLeft = MARGIN + (maxWidth - destWidth) / 2
            val rect = RectF(destLeft, y, destLeft + destWidth, y + destHeight)
            canvas.drawBitmap(photo, null, rect, null)
            y += destHeight + 22f
        }

        canvas.drawText(gem.nom, MARGIN, y + 16f, gemNamePaint)
        y += 20f
        canvas.drawText(gem.nomLatin, MARGIN, y + 10f, subtitlePaint)
        y += 28f

        fun row(label: String, value: String) {
            canvas.drawText(label, MARGIN, y + 12f, labelPaint)
            canvas.drawText(value.ifBlank { "—" }, MARGIN + 150f, y + 12f, valuePaint)
            y += 24f
            canvas.drawLine(MARGIN, y, (PAGE_WIDTH - MARGIN), y, linePaint)
            y += 14f
        }

        row("Famille", gem.famille)
        row("Dimensions", info.dimensions)
        row("Poids", info.poids)
        row("Origine", info.origine)
        row("Traitement", info.traitement)
        row("Dureté (Mohs)", gem.durete)
        row("Prix indicatif", gem.prixCaratEur)

        y += 20f
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.FRANCE)
        canvas.drawText("Émis le ${dateFormat.format(Date())}", MARGIN, y, subtitlePaint)
        y += 16f
        canvas.drawText(
            "Ce document décrit un exemplaire renseigné par l'utilisateur ; il ne constitue pas une expertise.",
            MARGIN,
            y,
            subtitlePaint
        )

        document.finishPage(page)

        val outDir = File(context.cacheDir, "certificates").apply { mkdirs() }
        val fileName = "certificat_${gem.id}_${System.currentTimeMillis()}.pdf"
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()

        return outFile
    }
}
