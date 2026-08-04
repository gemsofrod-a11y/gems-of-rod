# Publier sur le Play Store — marche à suivre

Ce dossier contient tout ce qui a été préparé automatiquement. Voici les
étapes qu'il te reste à faire toi-même, dans l'ordre.

## 1. Récupérer et sécuriser ta clé de signature

Tu as reçu deux fichiers en pièce jointe dans le chat :
- `gems-of-rod-upload.jks` — ta clé de signature
- `credentials.txt` — les mots de passe associés

**Fais immédiatement :**
1. Copie ces deux fichiers dans un gestionnaire de mots de passe
   (1Password, Bitwarden...) ou un coffre-fort numérique.
2. Ne les mets JAMAIS dans un dépôt Git, un email non chiffré, ou un cloud
   public.
3. Si tu perds ce fichier, tu ne pourras plus jamais publier de mise à jour
   sous la même fiche Play Store — il n'existe aucun moyen de le
   régénérer à l'identique.

## 2. Activer la page de politique de confidentialité

Le fichier `docs/privacy-policy.html` est déjà dans le dépôt. Pour lui
donner une URL publique (obligatoire pour le Play Store) :
1. Sur GitHub : Settings → Pages
2. Source : "Deploy from a branch"
3. Branch : `main` (ou la branche par défaut), dossier `/docs`
4. Enregistre. L'URL sera :
   `https://gemsofrod-a11y.github.io/gems-of-rod/privacy-policy.html`
   (peut prendre quelques minutes à s'activer)

## 3. Créer le compte Google Play Console

1. Va sur https://play.google.com/console/signup
2. Connecte-toi avec le compte Google que tu veux utiliser pour Gems of Rod
   (perso ou dédié à l'entreprise — celui-ci sera propriétaire de l'app
   pour toujours, choisis-le avec soin)
3. Paye les 25$ de frais d'inscription unique
4. Complète la vérification d'identité (pièce d'identité + parfois
   justificatif d'entreprise) — compte plusieurs jours si tu déclares un
   compte "Organisation" (recommandé si tu veux que la fiche affiche
   "Gems of Rod" comme développeur plutôt que ton nom personnel)

## 4. Configurer les secrets GitHub pour builder l'AAB signé (optionnel mais recommandé)

Si tu veux que je puisse te régénérer un App Bundle signé automatiquement
à chaque nouvelle version, ajoute ces 4 secrets sur GitHub (Settings →
Secrets and variables → Actions → New repository secret) :

| Nom du secret | Valeur |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | `gems-of-rod-upload.jks` encodé en base64 (`base64 -w0 gems-of-rod-upload.jks` dans un terminal) |
| `ANDROID_KEYSTORE_PASSWORD` | valeur `STORE_PASSWORD` de `credentials.txt` |
| `ANDROID_KEY_ALIAS` | `gemsofrod-upload` |
| `ANDROID_KEY_PASSWORD` | valeur `KEY_PASSWORD` de `credentials.txt` |

Une fois configuré, le workflow "Build signed Android App Bundle" (menu
Actions → onglet gauche) peut être lancé manuellement et produit un
`app-release.aab` prêt à uploader.

Si tu préfères ne jamais mettre la clé sur GitHub, tu peux aussi builder
l'AAB en local avec Android Studio en remplissant
`android/keystore.properties` (copié depuis le `.template`) — ce fichier
est ignoré par Git.

## 5. Première publication sur Play Console

1. Play Console → "Créer une application"
2. Nom, langue par défaut (français), type (Application), gratuite
3. Accepte les déclarations (politique développeur, export US)
4. Section "Présence sur le Store" → colle le contenu de
   `docs/play-store-listing.md`
5. Section "Contenu de l'application" :
   - Politique de confidentialité → colle l'URL de l'étape 2
   - Questionnaire de classification du contenu
   - Fiche "Sécurité des données" → aucune donnée collectée
   - Public cible (âge)
6. Section "Production" → "Créer une release" → upload le fichier
   `app-release.aab`
7. **Active "Play App Signing"** quand Google te le propose lors du
   premier upload — c'est la case la plus importante : Google régénère
   alors une clé de signature finale gérée par leurs serveurs à partir de
   ta clé d'upload, ce qui te protège même en cas de perte future de ton
   fichier `.jks` (procédure de récupération possible auprès de Google).
8. Envoie en revue. Le premier examen prend généralement quelques heures
   à quelques jours.

## 6. Protéger le nom "Gems of Rod" au-delà du technique

La signature de l'app empêche quelqu'un de publier une *mise à jour* de ta
fiche existante, mais n'empêche pas légalement un tiers de publier une
application différente avec un nom proche. Pour une protection plus large :
- Dépôt de marque française : https://www.inpi.fr (environ 190€ pour une
  classe de produits/services, valable 10 ans renouvelables)
- Garder le nom de package `fr.gemsofrod.encyclopedie` (déjà fait) : deux
  apps ne peuvent jamais partager le même identifiant sur le Play Store

## Ce qui est déjà prêt dans le dépôt

- `android/app/build.gradle.kts` : configuration de signature release
- `android/keystore.properties.template` : modèle (sans secrets)
- `.github/workflows/android-release.yml` : build AAB signé à la demande
- `docs/privacy-policy.html` : politique de confidentialité
- `docs/play-store-listing.md` : textes de fiche Store
