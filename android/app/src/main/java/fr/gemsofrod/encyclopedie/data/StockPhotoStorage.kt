package fr.gemsofrod.encyclopedie.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

/**
 * Persiste les photos des fiches de stock dans le stockage interne de l'app
 * (contrairement à la photo du certificat, éphémère le temps de générer un
 * PDF, une photo de stock doit survivre au redémarrage de l'app). Le nom de
 * fichier retourné par [savePhoto] est la seule donnée conservée dans
 * [StockItem] ; le contenu de l'image vit ici.
 */
object StockPhotoStorage {
    private const val DIR_NAME = "stock_photos"

    private fun photoFile(context: Context, fileName: String): File =
        File(context.filesDir, DIR_NAME).apply { mkdirs() }.let { dir -> File(dir, fileName) }

    /** Enregistre le bitmap (déjà sous-échantillonné par l'appelant) et retourne son nom de fichier. */
    fun savePhoto(context: Context, bitmap: Bitmap): String {
        val fileName = "stock_${System.currentTimeMillis()}.jpg"
        FileOutputStream(photoFile(context, fileName)).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }
        return fileName
    }

    fun deletePhoto(context: Context, fileName: String?) {
        if (fileName == null) return
        runCatching { photoFile(context, fileName).delete() }
    }

    /** Décode la photo sous-échantillonnée à environ [targetPx] (× 2 de marge), ou null si absente. */
    fun loadSampledBitmap(context: Context, fileName: String?, targetPx: Int): Bitmap? {
        if (fileName == null) return null
        val file = photoFile(context, fileName)
        if (!file.exists()) return null

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sampleSize = 1
        while (bounds.outWidth / sampleSize > targetPx * 2 || bounds.outHeight / sampleSize > targetPx * 2) {
            sampleSize *= 2
        }
        return BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options().apply { inSampleSize = sampleSize })
    }
}
