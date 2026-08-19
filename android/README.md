# Gems of Rod — Encyclopédie des gemmes (Android)

Application Android native (Kotlin + Jetpack Compose) : une encyclopédie
gemmologique organisée par couleur. Chaque catégorie de couleur ouvre une
liste de pierres, et chaque pierre a sa fiche détaillée cliquable
(composition chimique, système cristallin, dureté, origines, particularités).

## Parcours de l'application

1. **Écran d'accueil** — grille des catégories de couleur (Rouge, Orange,
   Jaune, Vert, Bleu, Violet, Rose, Incolore & blanc, Noir, Multicolore).
2. **Liste par couleur** — pierres appartenant à la catégorie sélectionnée.
3. **Fiche pierre** — description complète, formule chimique, système
   cristallin, dureté (échelle de Mohs), origines et particularités.

## Structure du projet

```
android/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/fr/gemsofrod/encyclopedie/
│       │   ├── MainActivity.kt
│       │   ├── data/            # Modèle Gem, catégories de couleur, données statiques
│       │   ├── ui/navigation/   # Navigation Compose (3 écrans)
│       │   ├── ui/screens/      # CategoriesScreen, GemsListScreen, GemDetailScreen
│       │   └── ui/theme/        # Thème (or/anthracite, clair et sombre)
│       └── res/
├── build.gradle.kts
└── settings.gradle.kts
```

Le contenu gemmologique (44 pierres réparties sur 10 catégories) est une base
de connaissances générale, indépendante du catalogue commercial de
Gems of Rod (`agent/knowledge/products.json`).

## Compiler et lancer le projet

Cet environnement d'exécution ne dispose pas du SDK Android (aucune
compilation n'a donc pu être testée ici). Pour lancer l'application :

1. Ouvrir le dossier `android/` avec **Android Studio** (Koala ou plus
   récent).
2. Laisser Android Studio synchroniser Gradle (le wrapper est déjà fourni,
   Gradle 8.7 / AGP 8.5.2 / Kotlin 1.9.24).
3. Lancer sur un émulateur ou un appareil physique (Android 8.0 / API 26
   minimum).

En ligne de commande (une fois le SDK Android installé et `ANDROID_HOME`
configuré) :

```bash
cd android
./gradlew assembleDebug
```

## Photos des gemmes

Chaque fiche peut afficher une photo (repli sur la pastille de couleur si
absente). Les photos sont récupérées automatiquement depuis **Wikimedia
Commons**, en ne retenant que des licences librement réutilisables (domaine
public, CC0, CC BY, CC BY-SA — jamais NC/ND), avec crédit photographique
affiché sous chaque image.

Pour (re)générer les photos : lancer manuellement le workflow GitHub Actions
**"Fetch gem images"** (`.github/workflows/fetch-gem-images.yml`, déclenché
via `workflow_dispatch`). Il exécute `scripts/fetch_gem_images.py`, qui :
- télécharge une photo par gemme dans `app/src/androidMain/res/drawable-nodpi/`,
- régénère `app/src/main/java/.../data/GemImages.kt` (mapping id → photo + crédit),
- publie `android/IMAGE_FETCH_REPORT.md` (liste des sources/licences pour revue),
et commit directement le résultat sur la branche.

## Prochaines évolutions possibles

- Champ de recherche par nom.
- Lien vers les fiches produits en stock (`agent/knowledge/products.json`)
  quand une pierre de l'encyclopédie correspond à une pierre disponible à
  la vente.
