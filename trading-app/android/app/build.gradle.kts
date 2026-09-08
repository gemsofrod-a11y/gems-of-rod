plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "fr.gemsofrod.tradingor"
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.gemsofrod.tradingor"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"
    }

    // Clé de débogage fixe (commitée, non secrète — c'est la norme pour
    // une clé de debug) : sans ça, chaque run CI en régénère une
    // différente et la mise à jour de l'APK échoue silencieusement tant
    // que l'ancienne version n'a pas été complètement désinstallée.
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

