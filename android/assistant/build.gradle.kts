plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "fr.gemsofrod.assistant"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.gemsofrod.assistant"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        // Schéma de redirection OAuth (AppAuth) : capturé par l'activité de la
        // librairie AppAuth elle-même (fusionnée automatiquement dans le
        // manifeste). À REMPLACER par le vôtre une fois le client OAuth Android
        // créé dans Google Cloud Console : c'est l'ID client Google inversé,
        // ex. un ID "123-abc.apps.googleusercontent.com" devient
        // "com.googleusercontent.apps.123-abc". Voir android/assistant/README.md.
        manifestPlaceholders["appAuthRedirectScheme"] = "com.googleusercontent.apps.REMPLACER_MOI"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.activity:activity-ktx:1.9.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Connexion Google directement dans l'app (OAuth 2.0 + PKCE, sans passer
    // par un serveur) — voir auth/GoogleAuthManager.kt.
    implementation("net.openid:appauth:0.11.1")
    implementation("androidx.browser:browser:1.8.0")
    // Stockage chiffré du refresh token Google (Android Keystore).
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Appels directs à l'API Anthropic (Saphir) depuis le téléphone — voir
    // ai/AnthropicClient.kt. Remplace l'ancien backend FastAPI. Fait en HTTP
    // brut (OkHttp + org.json, déjà des dépendances du module) plutôt qu'avec
    // le SDK Java officiel : impossible de compiler/vérifier ce module dans
    // l'environnement où ce code a été écrit, donc on évite tout pari sur des
    // noms de méthode de builder qu'on ne peut pas vérifier — le format JSON
    // brut de l'API Messages est stable et déjà bien connu (c'est ce que le
    // SDK Python du backend envoie/reçoit sous le capot).
    // implementation("com.anthropic:anthropic-java:2.34.0")

    // Extraction de texte des PDF reçus en pièce jointe — voir
    // ai/PdfTextExtractor.kt. Fork Android de Apache PDFBox.
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")

    // Tri automatique périodique en tâche de fond — voir triage/TriageWorker.kt.
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
