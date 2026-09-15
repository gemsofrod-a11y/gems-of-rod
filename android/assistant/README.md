# Gems of Rod — Assistant vocal (Android)

App Android séparée (usage personnel, pas de publication Play Store prévue)
qui pilote à la voix l'assistant Gmail défini dans `backend/`.

- 🎤 Appui sur « Parler » → reconnaissance vocale du téléphone (gratuite,
  moteur Google) → la phrase transcrite est envoyée au backend.
- 🔊 La réponse de l'assistant est lue à voix haute avec le moteur de
  synthèse vocale du téléphone (gratuit, `TextToSpeech`).
- 📥 Onglet « En attente » : emails complexes que l'assistant ne traite
  jamais seul (négociation, VIP, réclamation...), avec la réponse proposée
  modifiable avant envoi.
- ⚙️ Réglages : adresse du serveur backend + jeton d'accès.

## Build

```bash
cd android
./gradlew :assistant:assembleDebug
# APK : android/assistant/build/outputs/apk/debug/assistant-debug.apk
```

Ou depuis Android Studio : ouvrir le dossier `android/`, sélectionner la
configuration de lancement `assistant`.

Avant de l'utiliser, il faut que `backend/` tourne quelque part
d'accessible depuis le téléphone (voir `backend/README.md`), et renseigner
son URL + le jeton `ASSISTANT_API_TOKEN` dans l'onglet Réglages de l'app.
