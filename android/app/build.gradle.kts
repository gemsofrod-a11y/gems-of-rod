import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Charge les identifiants de signature depuis keystore.properties (jamais commité,
// voir android/keystore.properties.template) ou depuis des variables d'environnement
// (utilisé par le workflow CI android-release.yml via les secrets GitHub).
val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore.properties")
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(keystorePropertiesFile.inputStream())
}
fun signingProp(key: String): String? =
    keystoreProperties.getProperty(key) ?: System.getenv(key)

android {
    namespace = "fr.gemsofrod.encyclopedie"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.gemsofrod.encyclopedie"
        minSdk = 26
        targetSdk = 35
        versionCode = 53
        versionName = "4.2"
    }

    signingConfigs {
        create("release") {
            val storeFilePath = signingProp("KEYSTORE_FILE")
            if (storeFilePath != null) {
                storeFile = rootProject.file(storeFilePath)
                storePassword = signingProp("STORE_PASSWORD")
                keyAlias = signingProp("KEY_ALIAS")
                keyPassword = signingProp("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (signingProp("KEYSTORE_FILE") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
