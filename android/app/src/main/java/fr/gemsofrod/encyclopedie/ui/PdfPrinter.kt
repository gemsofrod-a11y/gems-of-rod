package fr.gemsofrod.encyclopedie.ui

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Envoie un fichier PDF déjà généré vers la boîte de dialogue d'impression
 * du système (choix d'imprimante, aperçu, "Enregistrer en PDF" en secours) —
 * pas de dépendance externe, uniquement l'API [PrintManager] de la
 * plateforme. Le PDF est servi tel quel, sans re-mise en page : pour une
 * étiquette générée à taille réelle (ex. [fr.gemsofrod.encyclopedie.data.StockLabelPdfGenerator]),
 * l'utilisateur doit choisir « Taille réelle » / désactiver la mise à
 * l'échelle dans les options d'impression pour conserver les dimensions
 * physiques exactes.
 */
fun printPdfFile(context: Context, file: File, jobName: String) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
    printManager.print(jobName, FilePrintDocumentAdapter(file), null)
}

private class FilePrintDocumentAdapter(private val file: File) : PrintDocumentAdapter() {
    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        if (cancellationSignal?.isCanceled == true) {
            callback.onLayoutCancelled()
            return
        }
        val info = PrintDocumentInfo.Builder(file.name)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .build()
        callback.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {
        runCatching {
            FileInputStream(file).use { input ->
                FileOutputStream(destination.fileDescriptor).use { output ->
                    input.copyTo(output)
                }
            }
        }.onSuccess {
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        }.onFailure {
            callback.onWriteFailed(it.message)
        }
    }
}
