# Add project specific ProGuard rules here.

# Pont JavaScript de la carte légendaire (LegendaryMapScreen.kt) : la page
# HTML appelle window.Android.onCountrySelected(...) par réflexion depuis le
# WebView, invisible à l'analyse d'atteignabilité de R8 — sans cette règle,
# la méthode serait renommée/supprimée et l'énigme casserait silencieusement
# (aucune erreur de compilation, juste un pont JS qui ne répond plus).
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
