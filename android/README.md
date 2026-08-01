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

## Prochaines évolutions possibles

- Ajouter des photos réelles par pierre (actuellement, chaque fiche affiche
  une pastille de couleur représentative).
- Champ de recherche par nom.
- Lien vers les fiches produits en stock (`agent/knowledge/products.json`)
  quand une pierre de l'encyclopédie correspond à une pierre disponible à
  la vente.
