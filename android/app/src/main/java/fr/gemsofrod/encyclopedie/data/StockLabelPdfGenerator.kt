package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import fr.gemsofrod.encyclopedie.R
import java.io.File
import java.io.FileOutputStream

/**
 * Génère une étiquette produit (QR code ou code-barres) au format PDF, une
 * page carrée de 3 × 3 cm exactement — pour impression à taille réelle
 * (100 %, sans mise à l'échelle) sur une étiquette autocollante à coller
 * sur la pierre physique. N'utilise que les API graphiques natives
 * d'Android (android.graphics.pdf) et [BarcodeGenerator], aucune dépendance
 * de mise en page externe.
 */
object StockLabelPdfGenerator {
    // 3 cm = 3 / 2.54 pouce = 1,1811 pouce ; 1 pouce PDF = 72 points.
    private const val PAGE_SIZE_PT = 85 // ≈ 3 × 3 cm (85,04 pt)
    private const val MARGIN = 3f

    // Sur-échantillonnage du bitmap du code pour un rendu net à l'impression
    // (les points PDF ne présument pas la résolution finale du papier).
    private const val CODE_OVERSAMPLE = 4

    fun generate(context: Context, item: StockItem, codeType: LabelCodeType): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_SIZE_PT, PAGE_SIZE_PT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val contentWidth = PAGE_SIZE_PT - MARGIN * 2
        val payload = item.reference.ifBlank { item.id }

        val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 6.5f
            isFakeBoldText = true
        }
        val smallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 5f
        }

        var y = MARGIN

        when (codeType) {
            LabelCodeType.QR_CODE -> {
                val codeSize = 46f
                val codeLeft = MARGIN + (contentWidth - codeSize) / 2
                val bitmap = BarcodeGenerator.generate(
                    payload, codeType,
                    (codeSize * CODE_OVERSAMPLE).toInt(), (codeSize * CODE_OVERSAMPLE).toInt()
                )
                bitmap?.let {
                    canvas.drawBitmap(it, null, RectF(codeLeft, y, codeLeft + codeSize, y + codeSize), null)
                }
                y += codeSize + 5f
            }
            LabelCodeType.BARCODE -> {
                val codeHeight = 22f
                val bitmap = BarcodeGenerator.generate(
                    payload, codeType,
                    (contentWidth * CODE_OVERSAMPLE).toInt(), (codeHeight * CODE_OVERSAMPLE).toInt()
                )
                bitmap?.let {
                    canvas.drawBitmap(it, null, RectF(MARGIN, y, MARGIN + contentWidth, y + codeHeight), null)
                }
                y += codeHeight + 4f
                canvas.drawText(payload, MARGIN, y, smallPaint)
                y += 8f
            }
        }

        canvas.drawText(truncateToWidth(item.nom.ifBlank { context.getString(R.string.stock_label_unnamed) }, namePaint, contentWidth), MARGIN, y, namePaint)
        y += 7.5f

        if (codeType == LabelCodeType.QR_CODE) {
            canvas.drawText(
                context.getString(R.string.stock_label_reference_prefix, payload),
                MARGIN, y, smallPaint
            )
            y += 6.5f
        }

        val poids = item.poidsCarats?.let { "$it ct" }
        val taille = item.taille.ifBlank { null }
        val details = listOfNotNull(poids, taille).joinToString("  •  ")
        if (details.isNotEmpty()) {
            canvas.drawText(truncateToWidth(details, smallPaint, contentWidth), MARGIN, y, smallPaint)
        }

        document.finishPage(page)

        val outDir = File(context.cacheDir, "stock_labels").apply { mkdirs() }
        val fileName = "etiquette_${item.id}_${System.currentTimeMillis()}.pdf"
        val outFile = File(outDir, fileName)
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()

        return outFile
    }

    private fun truncateToWidth(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var end = text.length
        while (end > 0 && paint.measureText(text.substring(0, end) + "…") > maxWidth) end--
        return text.substring(0, end) + "…"
    }
}
