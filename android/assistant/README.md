# Gems of Rod — Assistant vocal (Android, autonome)

App Android **autonome** (usage personnel, pas de publication Play Store) qui
pilote Saphir à la voix — connexion Gmail directement dans l'app, appels
directs à l'API Anthropic, tri automatique en tâche de fond. **Plus de
serveur `backend/` à héberger** : tout tourne sur le téléphone, sauf l'IA
elle-même (Anthropic) qui reste un service payant à l'usage (voir plus bas).

Réécriture du 22/09/2026 à la demande de Sébastien (voir CLAUDE.md) : l'ancienne
version de cette app n'était qu'un client réseau pour `backend/`. Cette
version porte en Kotlin l'intégralité de la logique qui vivait côté serveur
(`backend/app/*.py`) — mêmes règles d'autonomie, même grille tarifaire, même
comportement.

## ⚠️ Avertissement important : code non compilé

Ce code a été écrit dans un environnement sans SDK Android installable
(téléchargement depuis `dl.google.com` bloqué par la politique réseau du
bac à sable) — **il n'a pas pu être compilé ni testé avant d'être livré.**
Chaque fichier a été relu attentivement à la main (cohérence des noms,
équilibre des accolades/parenthèses, signatures d'API), et les appels à
l'API Anthropic ont délibérément été faits en HTTP brut plutôt qu'avec le
SDK Java officiel — le format JSON de l'API Messages est bien connu, alors
que parier sur des noms de méthode précis d'un SDK qu'on ne peut pas
compiler aurait été un risque bien plus grand. Malgré ça, une vraie
première compilation dans Android Studio reste nécessaire pour attraper ce
qui a pu échapper à cette relecture. Si `./gradlew :assistant:assembleDebug`
échoue, l'erreur du compilateur pointera précisément l'endroit à corriger.

## Ce qui a changé par rapport à l'ancienne version

| Avant (client réseau) | Maintenant (autonome) |
|---|---|
| Connexion Gmail via un `token.json` généré sur ordinateur, côté serveur | Écran natif "Se connecter avec Google" dans l'app (`auth/GoogleAuthManager.kt`) |
| Appels Gmail/Anthropic via le serveur FastAPI (`backend/`) | Appels directs depuis le téléphone (`gmail/GmailClient.kt`, `ai/SaphirAgent.kt`) |
| Base SQLite du serveur | Base SQLite locale sur le téléphone (`data/LocalStore.kt`) |
| Tri automatique en tâche de fond serveur (toutes les 5 min, continu) | `WorkManager` périodique (toutes les 15 min minimum — plancher imposé par Android) |
| Réglages : URL du serveur + jeton d'accès | Réglages : connexion Google + clé API Anthropic |

## Coût : ce qui reste payant

Héberger `backend/` sur Render ne coûtait déjà rien (plan gratuit). Cette
version supprime même ce serveur. **Le seul coût réel, inchangé, est l'usage
de l'API Anthropic** (quelques centimes par conversation, quelques centimes
par email trié automatiquement — voir le détail donné à Sébastien dans la
conversation du 22/09/2026). Il n'existe pas de version gratuite de Claude à
l'usage : consultez console.anthropic.com → Facturation pour le coût réel.

## Configuration requise avant de compiler

### 1. Créer le client OAuth "Android" dans Google Cloud Console

Le projet Google Cloud est probablement déjà créé pour `backend/` (voir
`backend/README.md`) — réutilisez-le, ajoutez juste un nouveau client :

1. [console.cloud.google.com](https://console.cloud.google.com) → le projet
   utilisé pour `backend/` → **APIs et services → Identifiants**.
2. **Créer des identifiants → ID client OAuth** → type d'application
   **Android**.
3. Nom du package : `fr.gemsofrod.assistant`.
4. Empreinte du certificat SHA-1 : pour un build debug (recommandé, pas
   besoin de signer pour un usage personnel sideloadé) :
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore \
     -alias androiddebugkey -storepass android -keypass android
   ```
   Copiez la valeur `SHA1:`.
5. Créez. Google affiche l'**ID client** (ex.
   `123456789-abcdefg.apps.googleusercontent.com`). Pas de secret associé
   pour ce type de client (client public, normal).
6. Vérifiez que l'API Gmail est activée sur ce projet (elle l'est déjà si
   `backend/` fonctionne) et que le scope `gmail.modify` est autorisé sur
   l'écran de consentement OAuth.

### 2. Renseigner l'ID client dans le code

Deux endroits à modifier avec le **même** ID client (inversé) :

- `app/src/main/java/fr/gemsofrod/assistant/AppConfig.kt` :
  ```kotlin
  const val GOOGLE_OAUTH_CLIENT_ID = "123456789-abcdefg.apps.googleusercontent.com"
  val GOOGLE_OAUTH_REDIRECT_URI: Uri = Uri.parse("com.googleusercontent.apps.123456789-abcdefg:/oauth2redirect")
  ```
- `build.gradle.kts` (`manifestPlaceholders["appAuthRedirectScheme"]`) :
  ```kotlin
  manifestPlaceholders["appAuthRedirectScheme"] = "com.googleusercontent.apps.123456789-abcdefg"
  ```

Le schéma de redirection est l'ID client "inversé" (segments dans l'ordre
inverse, séparés par des points) — c'est la convention standard de Google
pour les clients OAuth Android, reconnue automatiquement.

### 3. Compiler

```bash
cd android
./gradlew :assistant:assembleDebug
# APK : android/assistant/build/outputs/apk/debug/assistant-debug.apk
```

Ou depuis Android Studio : ouvrir `android/`, configuration de lancement
`assistant`.

### 4. Premier lancement sur le téléphone

1. Installer l'APK (sideload — pas besoin du Play Store).
2. Ouvrir l'app → Réglages → **Se connecter avec Google** → choisir
   `gemsofrod@gmail.com` → accepter l'accès Gmail demandé.
3. Toujours dans Réglages, coller votre **clé API Anthropic**
   (console.anthropic.com → Clés API).
4. Retour à l'écran principal : appuyer sur *Parler*, ou taper une
   question. Le tri automatique démarre dès que les deux réglages ci-dessus
   sont faits (toutes les 15 minutes, en tâche de fond).

## Si la compilation échoue : points à vérifier en premier

Par ordre de risque décroissant (du plus probable au moins probable, selon
ce qui n'a pas pu être vérifié faute de compilateur) :

1. **Versions Gradle exactes** des nouvelles dépendances
   (`net.openid:appauth:0.11.1`, `com.tom-roush:pdfbox-android:2.0.27.0`,
   `androidx.security:security-crypto:1.1.0-alpha06`,
   `androidx.work:work-runtime-ktx:2.9.1`) dans `build.gradle.kts` : si une
   version précise n'existe plus sur Maven Central, Android Studio proposera
   la version disponible la plus proche — accepter la suggestion.
2. **AppAuth** (`auth/GoogleAuthManager.kt`) : l'API utilisée
   (`AuthState`, `AuthorizationService`, `AuthorizationRequest.Builder`,
   `NoClientAuthentication`) est stable depuis des années, mais n'a pas pu
   être vérifiée à la compilation.
3. **PdfBox-Android** (`ai/PdfTextExtractor.kt`) : `PDFBoxResourceLoader.init()`
   doit être appelé avant toute utilisation — déjà fait automatiquement à la
   première extraction, mais à surveiller si une erreur de ressources
   manquantes apparaît au premier essai.
4. Le reste (Gmail REST, appels Anthropic en HTTP brut, SQLite, WorkManager,
   Compose) utilise des API Android/OkHttp/org.json standards, bien connues.

## Limites assumées (différences avec la version serveur)

- **Tri automatique moins réactif** : toutes les 15 minutes minimum (plancher
  Android), contre 5 minutes en continu côté serveur.
- **App doit rester installée et le téléphone allumé** pour que le tri
  tourne — plus de serveur toujours disponible.
- **Pas de PDF.js / rendu visuel des pièces jointes PDF dans le fil de
  discussion** comme sur le client web : cette version affiche seulement le
  texte extrait par Saphir à l'écran. Si Sébastien veut aussi voir le PDF
  original tel quel dans l'app, c'est une amélioration à ajouter séparément
  (`android.graphics.pdf.PdfRenderer`, natif Android, bien adapté à cet
  usage).
- **Pas de streaming de la réponse** (contrairement au client web) : la
  réponse de Saphir arrive d'un bloc plutôt que phrase par phrase, pour
  fiabilité (voir `ai/AnthropicClient.kt`).
- **Contexte de marque figé** (`ai/Classifier.kt`, `BRAND_CONTEXT`) : copie
  statique de CLAUDE.md, pas de lecture dynamique d'un fichier de
  configuration comme `agent/knowledge/company.json` côté Python.
