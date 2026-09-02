package fr.gemsofrod.encyclopedie.data

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/** Type de code apposable sur une étiquette produit du stock. */
enum class LabelCodeType { QR_CODE, BARCODE }

/**
 * Génère un QR code ou un code-barres (Code 128) à partir d'une chaîne, en
 * pur Java via ZXing — aucune caméra ni permission requise, uniquement la
 * génération (pas le scan). Le bitmap produit est en noir et blanc, à la
 * résolution pixel demandée ; l'appelant l'insère ensuite dans l'étiquette
 * PDF à l'échelle physique voulue (voir [StockLabelPdfGenerator]).
 */
object BarcodeGenerator {
    fun generate(content: String, type: LabelCodeType, widthPx: Int, heightPx: Int): Bitmap? {
        if (content.isBlank()) return null
        val format = when (type) {
            LabelCodeType.QR_CODE -> BarcodeFormat.QR_CODE
            LabelCodeType.BARCODE -> BarcodeFormat.CODE_128
        }
        val hints = when (type) {
            LabelCodeType.QR_CODE -> mapOf(
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.MARGIN to 0
            )
            LabelCodeType.BARCODE -> mapOf(EncodeHintType.MARGIN to 2)
        }
        val matrix = runCatching {
            MultiFormatWriter().encode(content, format, widthPx, heightPx, hints)
        }.getOrNull() ?: return null
        return matrix.toBitmap()
    }

    private fun BitMatrix.toBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}
