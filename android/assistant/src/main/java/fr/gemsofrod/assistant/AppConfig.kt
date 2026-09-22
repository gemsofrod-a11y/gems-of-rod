package fr.gemsofrod.assistant

import android.net.Uri

/** Identifiants OAuth Google de l'app — à remplacer une fois le client OAuth
 * de type "Android" créé dans Google Cloud Console (voir README.md). Un ID
 * client Android est un identifiant PUBLIC (pas de secret associé, PKCE
 * uniquement) : le mettre en dur dans le code source ne pose pas de
 * problème de sécurité, contrairement à un secret client "Web"/"Desktop".
 */
object AppConfig {
    const val GOOGLE_OAUTH_CLIENT_ID = "REMPLACER_MOI.apps.googleusercontent.com"

    // Doit correspondre exactement au manifestPlaceholders["appAuthRedirectScheme"]
    // de build.gradle.kts (l'ID client inversé) — voir README.md.
    val GOOGLE_OAUTH_REDIRECT_URI: Uri = Uri.parse("com.googleusercontent.apps.REMPLACER_MOI:/oauth2redirect")

    const val ANTHROPIC_MODEL = "claude-opus-4-8"
}
