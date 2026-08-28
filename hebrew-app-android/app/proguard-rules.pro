# Le pont JavaScript (TtsBridge) est appelé par réflexion depuis la WebView :
# ses méthodes @JavascriptInterface doivent survivre à l'obscurcissement.
-keepclassmembers class fr.gemsofrod.hebrewapp.MainActivity$TtsBridge {
    public *;
}
