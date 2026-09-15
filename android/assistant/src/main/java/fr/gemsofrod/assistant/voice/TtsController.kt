package fr.gemsofrod.assistant.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/** Synthèse vocale gratuite du téléphone (moteur TTS système, ex. Google
 * Text-to-Speech déjà installé sur la plupart des Android). */
class TtsController(context: Context) {
    private var ready = false
    private val tts = TextToSpeech(context.applicationContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
            ready = true
        }
    }

    init {
        tts.language = Locale.FRANCE
    }

    fun speak(text: String) {
        if (text.isBlank()) return
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "assistant_reply")
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
