# Budget Clair — analyse de relevé bancaire, 100 % locale

Page web autonome (HTML + JavaScript, sans dépendance ni serveur) qui lit un
export CSV de compte bancaire personnel et indique ce qui est **essentiel**,
**utile** ou **superflu**, avec des pistes d'économie chiffrées.

Le fichier CSV est lu dans le navigateur du téléphone : aucune requête réseau
n'envoie les opérations où que ce soit. Seules les polices Google Fonts sont
chargées depuis Internet (avec repli sur les polices du système hors-ligne).

## Fonctionnalités

- Import CSV avec détection automatique du séparateur (`;` `,` tabulation),
  de l'encodage (UTF-8 ou Windows-1252), de la ligne d'en-tête et des colonnes
  (date, libellé, montant signé ou débit/crédit, catégorie de la banque,
  état Revolut). Testé sur les formats Boursorama, Fortuneo, Revolut et N26.
  Correction manuelle possible si une colonne n'est pas reconnue, et option
  « les dépenses sont en positif ».
- Classement automatique par mots-clés (commerçants français courants) en
  ~30 catégories, chacune rattachée à un niveau Essentiel / Utile / Superflu,
  modifiable. Les virements d'épargne et entre comptes sont exclus des dépenses.
- Reclassement d'une opération : tous les paiements du même commerçant suivent,
  et la correction est mémorisée dans le navigateur.
- Synthèse mensuelle, règle 50/30/20, graphique mois par mois, détection des
  abonnements et prélèvements récurrents.
- Conseils : livraison de repas, restaurants, streaming en doublon, frais
  bancaires, jeux d'argent, tabac, shopping, petits achats, forfaits, assurances,
  retraits d'espèces… avec une estimation d'économie annuelle (sans double
  comptage).

## Utilisation sur le téléphone

1. Héberger le dossier `finances/` sur n'importe quel hébergement HTTPS
   statique (GitHub Pages, Netlify…), ouvrir l'adresse dans Chrome sur Android
   ou Safari sur iPhone, puis « Ajouter à l'écran d'accueil ».
   Le service worker (`sw.js`) met l'appli en cache : elle s'ouvre ensuite
   sans connexion.
2. Dans l'appli de la banque, exporter les opérations en CSV, puis
   « Choisir un fichier CSV » dans Budget Clair.

Le dernier relevé importé et les corrections sont gardés dans le `localStorage`
du navigateur ; le bouton « Effacer mes données de cet appareil » les supprime.

## Modifier les règles

Tout est dans `index.html` :
- `CATS` : catégories, niveau par défaut et expressions régulières appliquées
  au libellé normalisé (majuscules, sans accents, ponctuation → espaces) ;
- `BANK_CAT_MAP` : correspondance avec les catégories fournies par la banque ;
- `buildAdvice` : seuils et hypothèses de chaque conseil.
