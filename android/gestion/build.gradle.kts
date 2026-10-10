plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Application de gestion de la maison (commandes, stock, clients, graphiques).
// Module séparé, distribué en APK (jamais sur le Play Store) : il ne participe
// pas au pipeline de release android-release.yml, scopé à :app.
android {
    namespace = "fr.gemsofrod.gestion"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.gemsofrod.gestion"
        minSdk = 26
        targetSdk = 36
        // Numéro croissant à chaque publication (workflow gestion-apk.yml) :
        // sans lui, le téléphone croit la version déjà installée et propose
        // « Ouvrir » au lieu de l'installer.
        val build = System.getenv("GESTION_BUILD")?.toIntOrNull()
        versionCode = if (build != null) 100 + build else 3
        versionName = if (build != null) "1.2.$build" else "1.2.0"
    }

    // Clé de signature de débogage FIXE, versionnée exprès : sans elle, chaque
    // compilation sur GitHub Actions signe avec une clé nouvelle et le téléphone
    // refuse la mise à jour (« application non installée »). Ce n'est PAS la clé
    // de publication Play Store (celle-là ne se commite jamais) : cette app
    // n'est distribuée qu'en APK direct.
    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    // Version « release » publiée en APK : R8 retire le code et les icônes
    // inutilisés (~3 Mo au lieu de ~15). Signée avec la même clé que la
    // version de débogage, pour s'installer par-dessus sans désinstaller.
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
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

    // Captures d'écran (ScreenshotTest) : rendu Compose sur JVM via Robolectric.
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    // Activité de test vide requise par createComposeRule (captures d'écran).
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    testImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")
}
