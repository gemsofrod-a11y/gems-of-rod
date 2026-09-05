import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
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
    compileSdk = 36

    defaultConfig {
        applicationId = "fr.gemsofrod.encyclopedie"
        minSdk = 26
        targetSdk = 36
        versionCode = 156
        versionName = "9.53"
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
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (signingProp("KEYSTORE_FILE") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            // Symboles de débogage du code natif (bibliothèques CameraX/Camera2
            // du réflectomètre) : sans eux, Play Console ne peut pas produire de
            // trace de plantage lisible pour ce code natif, seulement des
            // adresses mémoire. Le fichier généré (native-debug-symbols.zip)
            // doit être importé manuellement dans Play Console avec chaque AAB.
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Nécessaire pour que les tests Robolectric (ScreenshotTest) résolvent
    // les ressources (drawables des photos de gemmes, strings) à l'exécution.
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    // Le choix de langue est fait dans l'app (LanguageScreen), indépendamment
    // de la langue système de l'appareil. Sans ceci, Play Feature Delivery ne
    // livre à chaque appareil que les ressources de sa langue système,
    // laissant les autres langues absentes de l'installation.
    bundle {
        language {
            enableSplit = false
        }
    }
}

kotlin {
    jvmToolchain(17)

    androidTarget()

    // Cible bureau (Windows/Linux/macOS) : voir le plan de portage
    // /root/.claude/plans/humble-scribbling-island.md. Phase 0 se limite à un
    // point d'entrée `main()` minimal ; le contenu de l'appli reste 100%
    // androidMain tant que les phases suivantes n'ont pas déplacé le code
    // portable vers commonMain.
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
            }
        }

        val commonTest by getting {
            // Tests de logique pure (sans dépendance Android/Compose), ex.
            // ReflectivityMeterTest — s'exécutent aussi bien via
            // androidUnitTest que desktopTest grâce à la hiérarchie de
            // source sets par défaut de Kotlin Multiplatform.
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            // Le code Kotlin reste physiquement sous src/main/java le temps de
            // la migration progressive vers commonMain (phases suivantes du
            // plan de portage) ; androidMain ajoute ce dossier existant sans
            // déplacer de code. Le manifeste, res/ et assets/ ont dû être
            // déplacés vers src/androidMain/ (contrairement au code Kotlin) :
            // avec androidTarget(), AGP les attend à cet emplacement par
            // convention et ignore un android.sourceSets["main"] réécrit
            // manuellement.
            kotlin.srcDir("src/main/java")
            dependencies {
                implementation("androidx.core:core-ktx:1.13.1")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
                implementation("androidx.activity:activity-compose:1.9.1")
                implementation("androidx.navigation:navigation-compose:2.7.7")
                // Écran de démarrage natif (fond + icône) avant que Compose ne prenne la main.
                implementation("androidx.core:core-splashscreen:1.0.1")
                implementation(compose.preview)
                implementation(compose.uiTooling)
                // Réflectomètre par caméra (Phase C) : capture avec verrouillage
                // manuel de l'exposition (camera-camera2, interop Camera2CameraControl)
                // et torche continue pour un éclairage constant pendant la mesure.
                implementation("androidx.camera:camera-core:1.4.1")
                implementation("androidx.camera:camera-camera2:1.4.1")
                implementation("androidx.camera:camera-lifecycle:1.4.1")
                implementation("androidx.camera:camera-view:1.4.1")
                // Génération d'étiquettes QR/code-barres pour le stock (pas de scan,
                // seul le module "core" est nécessaire — pur Java, sans dépendance).
                implementation("com.google.zxing:core:3.5.3")
            }
        }

        val androidUnitTest by getting {
            // Captures d'écran de l'app (ScreenshotTest) : rendu Compose sur JVM
            // via Robolectric, sans émulateur, pour alimenter la fiche Play Store.
            kotlin.srcDir("src/test/java")
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation("androidx.test.ext:junit:1.2.1")
                implementation("org.robolectric:robolectric:4.13")
                implementation("androidx.compose.ui:ui-test-junit4:1.7.5")
                implementation("androidx.compose.ui:ui-test-manifest:1.7.5")
            }
        }

        val desktopMain by getting {
            kotlin.srcDir("src/desktopMain/kotlin")
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
