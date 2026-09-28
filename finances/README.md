# Budget Clair — analyse de relevé bancaire, 100 % locale

Page web autonome (HTML + JavaScript, sans dépendance ni serveur) qui lit un
export CSV ou un relevé de compte PDF personnel et indique ce qui est **essentiel**,
**utile** ou **superflu**, avec des pistes d'économie chiffrées.

Le fichier est lu dans le navigateur du téléphone : aucune requête réseau
n'envoie les opérations où que ce soit. Seuls deux éléments viennent d'Internet,
et aucun ne reçoit de donnée : les polices Google Fonts (avec repli sur les
polices du système hors-ligne) et le lecteur PDF pdf.js 3.11.174 (cdnjs),
téléchargé une fois puis gardé en cache par le service worker.

## Fonctionnalités

- Import CSV avec détection automatique du séparateur (`;` `,` tabulation),
  de l'encodage (UTF-8 ou Windows-1252), de la ligne d'en-tête et des colonnes
  (date, libellé, montant signé ou débit/crédit, catégorie de la banque,
  état Revolut). Testé sur les formats Boursorama, Fortuneo, Revolut et N26.
  Correction manuelle possible si une colonne n'est pas reconnue, et option
  « les dépenses sont en positif ».
- Relevés PDF (relevés de compte téléchargés depuis la banque, pas les
  scans) : lecture du texte avec pdf.js dans la page, regroupement en lignes,
  repérage des lignes « date … montant » et du sens grâce aux colonnes
  Débit / Crédit (ou Money out / Money in) de l'en-tête. Gère les dates
  courtes (`03/08`, année déduite de la période du relevé), les dates en
  toutes lettres (`3 août 2026`, `3 Aug 2026`), les libellés sur deux lignes,
  la colonne Solde/Balance, et ignore les lignes de solde et de total. Sans
  en-tête reconnu, le sens est deviné et l'appli le signale. Un PDF scanné
  (image sans texte) ou protégé par mot de passe donne un message explicite.
- Fichiers Excel (`.xlsx`, ex. « account-statement » Revolut) lus sans
  bibliothèque : l'archive est décompressée par le navigateur
  (`DecompressionStream`) et les XML lus avec `DOMParser` (`readXlsx`).
  Dates Excel (numéros de série, calendrier 1900/1904), textes partagés, texte
  mal encodé réparé (`fixText` : « DÃ©but » → « Début », `_x0089_`), choix
  automatique de la feuille qui contient les opérations. Colonnes Revolut :
  `Produit` (Valeur actuelle / Épargne = Pockets / Dépôt = compte d'épargne),
  `Frais` (opération de frais séparée), `État` (lignes « RENVOYÉ » ignorées).
  L'ancien format `.xls` n'est pas lu : message pour l'enregistrer en `.xlsx`.
- Période analysée (3, 6, 12 mois ou tout l'historique), proposée dès que
  l'historique dépasse 4 mois ; 12 derniers mois par défaut au-delà d'un an.
- « D'où vient cet argent ? » : pour les virements fréquents que l'appli ne
  peut pas deviner (soi-même depuis une autre banque, salaire, proche), une
  question posée une fois ; la réponse vaut pour ce nom et ce sens (entrée ou
  sortie), y compris dans les prochains relevés.
- Fichier de classement (`.json`, importé par le même bouton) :
  `{"type": "budget-clair-classement", "overrides": {"NOM COMMERCANT": "categorie"}}`.
  Il complète les choix de l'utilisateur sans jamais les remplacer ; un nom
  suivi de `|+` ou `|-` ne vaut que pour les entrées ou les sorties d'argent.
  Sert à classer d'un coup des commerces locaux sans les écrire dans le code.
- Catégorie « Activité pro » : dépenses exclues de l'analyse perso, montant
  affiché à part dans la Synthèse.
- Historique cumulé : chaque relevé importé (CSV ou PDF) s'ajoute aux
  précédents. Une opération déjà connue (même date, montant, libellé et
  compte) n'est pas recomptée : réimporter un relevé mis à jour n'ajoute que
  les nouvelles opérations. Deux opérations identiques le même jour restent
  deux (on garde le plus grand nombre d'exemplaires vus dans un même relevé).
- Relevés de compte d'épargne (ex. « Relevé d'épargne » Revolut, dont les
  opérations tiennent sur 2 à 3 lignes serrées) : dépôts et retraits comptés
  comme épargne, intérêts comme revenu, taux brut, prélèvement forfaitaire,
  soldes et impôt retenu repris du relevé. Les virements « vers l'épargne »
  du compte courant sur la même période ne sont pas comptés une deuxième fois.
  Colonne `Product` des CSV Revolut (`Savings`) reconnue de la même façon.
- Partage depuis une autre appli (Android, appli installée) : `share_target`
  du manifeste + service worker. « Partager » sur un relevé dans l'appli de la
  banque, puis Budget Clair : le fichier est importé à l'ouverture.
- Classement automatique par mots-clés (commerçants français courants) en
  ~30 catégories, chacune rattachée à un niveau Essentiel / Utile / Superflu,
  modifiable. Les virements d'épargne et entre comptes sont exclus des dépenses.
- Reclassement d'une opération : tous les paiements du même commerçant suivent,
  et la correction est mémorisée dans le navigateur.
- Budgets mensuels par catégorie (onglet Budgets) : plafond en € par mois,
  barre de progression et alertes à 80 %, en cas de dépassement, et quand le
  rythme du mois en cours mène à un dépassement (projection de fin de mois).
  Pastille du nombre d'alertes sur l'onglet et encart en tête de la Synthèse.
  « Proposer des budgets » part de la moyenne mensuelle (−20 % pour le
  superflu). Choix du mois à examiner. Les alertes sont affichées dans l'appli,
  pas en notification du téléphone (il faudrait un serveur).
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
2. Dans l'appli de la banque, exporter les opérations en CSV (le plus fiable)
   ou télécharger le relevé PDF, puis « Choisir un relevé » dans Budget Clair.
   Ouvrir l'appli une première fois avec une connexion pour que le lecteur PDF
   soit mis en cache.

Le dernier relevé importé et les corrections sont gardés dans le `localStorage`
du navigateur ; le bouton « Effacer mes données de cet appareil » les supprime.

## Modifier les règles

Tout est dans `index.html` :
- `CATS` : catégories, niveau par défaut et expressions régulières appliquées
  au libellé normalisé (majuscules, sans accents, ponctuation → espaces) ;
- `BANK_CAT_MAP` : correspondance avec les catégories fournies par la banque ;
- `pdfLinesToRows` : reconnaissance des lignes d'opérations dans un PDF ;
- `buildAdvice` : seuils et hypothèses de chaque conseil.
