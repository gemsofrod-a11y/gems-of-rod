package fr.gemsofrod.tradingor

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

/**
 * Coquille WebView : l'interface (prix, bougies) vit dans
 * assets/index.html + app.js, identique en esprit à la version web
 * (trading-app/frontend), mais alimentée directement par NativeBridge
 * (appels réseau natifs Kotlin) plutôt que par un serveur — pas de
 * souci de CORS, pas de serveur à faire tourner sur le téléphone.
 *
 * Hérite de la simple android.app.Activity (pas AppCompatActivity) :
 * pas de dépendance supplémentaire, pas d'exigence de thème
 * Theme.AppCompat.* à respecter — juste une WebView plein écran.
 */
class MainActivity : Activity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            addJavascriptInterface(NativeBridge(), "NativeBridge")
            loadUrl("file:///android_asset/index.html")
        }
        setContentView(webView)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
