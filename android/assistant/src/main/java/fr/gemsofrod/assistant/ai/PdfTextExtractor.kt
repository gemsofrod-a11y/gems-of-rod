package fr.gemsofrod.assistant.ai

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.ByteArrayInputStream

/** Extraction de texte depuis un PDF reçu en pièce jointe Gmail (devis
 * fournisseur, fiche technique...) — port Kotlin de backend/app/pdf_reader.py
 * (pypdf côté Python, PdfBox-Android ici). Le texte extrait est donné à
 * Saphir pour qu'elle en discute ; le PDF original reste consultable tel
 * quel par Sébastien via le rendu visuel de la pièce jointe (voir
 * ui/DocumentViewer côté UI).
 */
object PdfTextExtractor {
    private const val MAX_CHARS = 12_000
    @Volatile private var initialized = false

    private fun ensureInit(context: Context) {
        if (!initialized) {
            synchronized(this) {
                if (!initialized) {
                    PDFBoxResourceLoader.init(context.applicationContext)
                    initialized = true
                }
            }
        }
    }

    fun extractText(context: Context, pdfBytes: ByteArray): String {
        ensureInit(context)
        val document = try {
            PDDocument.load(ByteArrayInputStream(pdfBytes))
        } catch (e: Exception) {
            return "[Erreur de lecture du PDF : ${e.message}]"
        }

        val text = try {
            PDFTextStripper().getText(document).trim()
        } catch (e: Exception) {
            ""
        } finally {
            try {
                document.close()
            } catch (_: Exception) {
                // rien à faire, le document est de toute façon abandonné
            }
        }

        if (text.isEmpty()) {
            return "[Aucun texte extrait de ce PDF — probablement un document scanné " +
                "(image), sans texte sélectionnable. Il reste affiché à l'écran pour " +
                "que Sébastien puisse le lire directement.]"
        }
        return if (text.length > MAX_CHARS) text.take(MAX_CHARS) + "\n[...texte tronqué...]" else text
    }
}
