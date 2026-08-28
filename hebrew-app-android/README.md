# Apprendre l'hébreu — app Android

Coquille Android native minimale : une `WebView` plein écran qui charge l'app web
[`hebrew-app/`](../hebrew-app) (copiée dans `app/src/main/assets/www/`), avec un pont
JavaScript vers le moteur de synthèse vocale du système (`android.speech.tts`),
plus fiable dans une WebView que le `speechSynthesis` du Web.

## Build

```bash
cd hebrew-app-android
./gradlew assembleDebug
```

L'APK de debug est généré dans `app/build/outputs/apk/debug/app-debug.apk`.

Le workflow GitHub Actions `.github/workflows/hebrew-app-android-build.yml` construit
automatiquement cet APK à chaque push touchant ce dossier et le publie comme artefact
de build (`hebrew-app-debug-apk`).

## Installation sur téléphone

1. Télécharger `app-debug.apk` (artefact du workflow, ou build local).
2. Sur le téléphone Android, autoriser l'installation depuis une source inconnue pour
   l'application utilisée pour ouvrir le fichier (navigateur, fichiers…).
3. Ouvrir le fichier `.apk` et confirmer l'installation.

## Synchronisation avec l'app web

`app/src/main/assets/www/` est une copie statique de `hebrew-app/`. Si le contenu de
`hebrew-app/` évolue (nouveau vocabulaire, corrections…), il faut recopier les fichiers :

```bash
cp -r hebrew-app/index.html hebrew-app/css hebrew-app/js hebrew-app-android/app/src/main/assets/www/
```
