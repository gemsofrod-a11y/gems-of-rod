package fr.gemsofrod.tradingor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.WebView

/**
 * Coquille WebView : l'interface (prix, bougies, portefeuille, bot)
 * vit dans assets/index.html + app.js, alimentée directement par
 * NativeBridge (appels réseau natifs Kotlin) plutôt qu'un serveur —
 * pas de souci de CORS, pas de serveur à faire tourner sur le
 * téléphone. Le bot lui-même tourne dans TradingBotService, pas ici,
 * pour continuer même app fermée.
 *
 * Hérite de la simple android.app.Activity (pas AppCompatActivity) :
 * pas de dépendance supplémentaire, pas d'exigence de thème
 * Theme.AppCompat.* à respecter — juste une WebView plein écran.
 */
class MainActivity : Activity() {

    private lateinit var webView: WebView
    private val notificationPermissionRequestCode = 4201

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            addJavascriptInterface(NativeBridge(this@MainActivity), "NativeBridge")
            loadUrl("file:///android_asset/index.html")
        }
        setContentView(webView)

        requestNotificationPermissionIfNeeded()
    }

    /** Nécessaire depuis Android 13 pour que la notification persistante
     * du bot (service de premier plan) s'affiche. Sans elle, le bot
     * continue quand même de tourner : c'est juste la visibilité qui
     * manquerait. */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), notificationPermissionRequestCode)
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
