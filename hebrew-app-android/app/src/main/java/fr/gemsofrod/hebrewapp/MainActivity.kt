package fr.gemsofrod.hebrewapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * Coquille native minimale : une WebView qui charge l'app "Apprendre l'hébreu"
 * (assets/www, identique à hebrew-app/) et un pont JS -> TextToSpeech natif.
 *
 * Le moteur speechSynthesis du Web n'est pas fiable dans une WebView Android
 * (contrairement à Chrome) : on utilise donc le moteur TTS du système via
 * android.speech.tts, exposé au JS sous window.AndroidTTS.speak(text).
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var webView: WebView
    private var ttsReady = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        webView = WebView(this)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(TtsBridge(), "AndroidTTS")
        webView.loadUrl("file:///android_asset/www/index.html")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("he", "IL"))
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    inner class TtsBridge {
        @JavascriptInterface
        fun speak(text: String) {
            if (!ttsReady) return
            runOnUiThread {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "hebrew-word")
            }
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
