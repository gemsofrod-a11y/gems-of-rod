# Budget Clair — analyse de relevé bancaire, 100 % locale

Page web autonome (HTML + JavaScript, sans dépendance ni serveur) qui lit un
export CSV ou un relevé de compte PDF personnel et indique ce qui est **essentiel**,
**utile** ou **superflu**, avec des pistes d'économie chiffrées.

Le fichier est lu dans le navigateur du téléphone : aucune requête réseau
n'envoie les opérations où que ce soit. Seuls deux éléments viennent d'Internet,
et aucun ne reçoit de donnée : les polices Google Fonts (avec repli sur les
polices du système hors-ligne) et le lecteur PDF pdf.js 3.11.174 (cdnjs),
téléchargé une fois puis gardé en cache par le service worker.

## Écrans (refonte du 29/09/2026)

Cinq onglets dans un dock en bas de l'écran, pensés pour le téléphone :

- **Accueil** : le mois choisi (pastilles des 12 derniers mois). Bloc « Reste
  à vivre » = tout ce qui est entré sur le compte courant ce mois-ci
  (revenus nets, autres entrées, épargne récupérée) − tout ce qui en est
  sorti (dépenses, argent mis de côté ou viré vers vos propres comptes,
  activité pro réglée depuis le compte perso),
  pour tomber sur ce que montre le solde du compte, avec le détail de
  chaque revenu déclaré (reçu, « attendu » si son jour habituel n'est pas
  encore passé, « pas reçu pour ce mois », ou ramené à un versement quand il
  tombe deux fois). Le mois de budget va de paie à paie : il commence
  le jour du salaire et finit la veille du suivant, et prend le nom du mois
  qui commence dans les 7 jours (salaire du 31/08 → septembre). Le salaire
  et tout ce qu'il paie le jour même (loyer, virements au foyer) comptent
  donc dans le même mois. Sans revenu déclaré : mois du calendrier. Le compte ne pouvant
  pas être à découvert, le reste à vivre ne descend jamais sous 0 € : un
  dépassement est affiché à part, pris sur l'argent qui restait sur le
  compte au début du mois.
  Puis, seulement quand il y a quelque chose à dire :
  déclaration des revenus, alertes budgets, où part l'argent (niveaux et 5
  premières catégories), conseils principaux, questions sur les virements
  fréquents, prochains prélèvements, graphique mois par mois.
- **Mouvements** : − Sorties (par catégorie ou par nom), + Entrées (par
  payeur, avec ce que l'appli en fait), Opérations (recherche, filtres,
  reclassement). Période analysée.
- **Budgets** : plafonds du mois, catégories sans plafond (repliées),
  abonnements et prélèvements récurrents.
- **Conseils** : pistes d'économie, conseils Revolut, plan d'épargne et de
  placements.
- **Mon budget** : revenus, charges fixes, crédits, puis relevés et réglages
  (import, exemple, colonnes, période, effacement). Chaque ligne a un volet
  « Modifier » avec un bouton « Valider » (enregistre tous les champs, même
  celui en cours de saisie, et referme le volet). Une charge trouvée dans le
  relevé affiche le montant d'un mois où elle est payée (médiane), pas la
  moyenne de la période. Une ligne terminée, ou payée à nouveau après son
  « dernier mois », propose « Toujours en cours ».

Tous les montants s'affichent et se saisissent au centime près (ex. 120,74 €),
sans arrondi, pour se retrouver tels quels dans le relevé. Une charge ou un
crédit introuvable dans le relevé propose le bénéficiaire qui revient le plus
souvent avec un montant proche de la mensualité (ex. « Repayment »).

Relevé PDF Revolut du compte courant : le libellé garde le nom du commerçant ou
du bénéficiaire (comme l'export Excel) ; les lignes de détail (« À : … »,
« Référence : … ») sont gardées à part, sans numéro de carte ni IBAN. Importer
le PDF et l'export Excel d'une même période ne crée pas de doublon : une
opération de l'autre format est reconnue au même montant, à 4 jours près (un
paiement par carte peut apparaître 3 jours plus tard dans le PDF), avec au moins
un mot en commun ; le candidat qui partage le nom du commerçant, puis le plus
proche en date, l'emporte. Sections du PDF : compte courant, « Transactions de
dépôt » (compte d'épargne), paiements « Renvoyés » (ignorés, jamais débités) et
compte d'un proche rattaché, ex. compte d'un enfant (ignoré : ce n'est pas le
compte de l'utilisateur, l'argent qu'il y envoie compte déjà comme virement).
Réimporter un PDF Revolut remplace, sur sa période, les opérations du compte
courant lues dans un PDF précédent (nettoie les lectures d'anciennes versions).
L'abonnement au forfait (Metal…) a son propre
conseil et n'est plus compté dans les « frais bancaires évitables ».

Sans relevé, l'accueil explique la marche à suivre ; l'exemple fictif n'est
chargé qu'à la demande.

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
  `Frais` (opération de frais séparée, y compris sur une ligne à 0 € comme
  le forfait Metal), `État` (lignes « RENVOYÉ » ignorées).
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
- Conseils Revolut (section « Ce que Revolut peut faire pour vous »), quand le
  relevé vient de Revolut : forfait payé ou gratuit comparé aux retraits
  réellement faits, frais de retrait, arrondis vers un coffre (montant calculé
  sur les paiements par carte), virement programmé, blocage des jeux
  d'argent. Tarifs dans `REVOLUT_PLANS` (relevés au 9 juillet 2026) : à mettre
  à jour si Revolut les change.
- Onglet « Mon budget » : revenus, charges fixes et crédits saisis par
  l'utilisateur. Chaque ligne a un « nom dans le relevé » (reconnu
  automatiquement, puis cumulé) et/ou un « montant réel par mois » : l'appli
  ajoute la différence entre ce montant et la moyenne qu'elle trouve dans le
  relevé (salaire versé en partie ailleurs, loyer payé depuis une autre
  banque). Un nom classé par le fichier de classement reste reconnu par le
  profil. Revenus et charges ont un « dernier mois » : une aide qui s'arrête
  (ex. la CAF) reste dans l'historique mais ne compte plus dans « un mois type
  aujourd'hui » (Synthèse), les conseils ni le plan de placements. Une ligne
  qui n'apparaît plus depuis plus de 40 jours est signalée, avec un bouton
  « C'est terminé ». Le mois type compte chaque revenu encore actif pour son
  dernier montant net reçu (ramené à sa médiane si le mois l'a reçu deux fois).
  Crédits : mensualité et dernière échéance, avec reste à payer et argent
  libéré ensuite. Option « seules ces sources comptent comme revenus ».
  Plan « Où placer votre argent » : épargne de précaution (3 à 6 mois de
  dépenses essentielles), livrets (LEP, Livret A, LDDS), puis long terme (PEA,
  assurance-vie), mise en garde trading/crypto. Taux dans `RATES` (au
  1er août 2026). Le profil peut aussi arriver par le fichier de classement
  (`"profile": {...}`), pour ne jamais écrire de nom réel dans le code.
- Accueil : quand des revenus sont déclarés dans « Mon budget », le reste à
  vivre ne compte que ces revenus (ex. salaire + CAF), en net tels que reçus
  sur le compte ce mois-là ; il avance à chaque nouveau relevé.
  Un revenu terminé n'y figure plus ; un mois où un revenu tombe deux fois
  est ramené à sa médiane. Sans revenu déclaré, l'accueil propose les
  entrées régulières du relevé avec un bouton « C'est un revenu » (le bouton
  « Un revenu » des questions sur les virements les déclare aussi) ; en
  attendant, il compte toutes les entrées d'argent du mois.
- Onglet « Mouvements » : deux listes séparées (+ Entrées, − Sorties),
  regroupées par payeur ou bénéficiaire, avec total, moyenne par mois et ce
  que l'appli en fait (revenu compté, autre entrée non comptée,
  remboursement, retrait d'épargne, entre vos comptes ; niveau et catégorie
  pour les sorties). Touchez une ligne pour voir ses opérations et changer
  leur catégorie. La vue Opérations a aussi un filtre + / −.
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
  Pastille du nombre d'alertes sur l'onglet et carte d'alertes à l'accueil.
  « Proposer des budgets » part de la moyenne mensuelle (−20 % pour le
  superflu). Choix du mois à examiner. Les alertes sont affichées dans l'appli,
  pas en notification du téléphone (il faudrait un serveur).
- Graphique mois par mois, détection des abonnements et prélèvements
  récurrents, avec les prochains prélèvements attendus à l'accueil.
- Conseils : livraison de repas, restaurants, streaming en doublon, frais
  bancaires, jeux d'argent, tabac, shopping, petits achats, forfaits, assurances,
  retraits d'espèces… avec une estimation d'économie annuelle (sans double
  comptage).

## Utilisation sur le téléphone

1. Adresse officielle : **https://gemsofrod-a11y.github.io/gems-of-rod/finances/**.
   GitHub Pages sert le dossier `docs/` de `main` : chaque fusion qui modifie
   `docs/finances/` est en ligne une à deux minutes plus tard, gratuitement.
   Tout autre hébergement HTTPS statique convient aussi. Ouvrir l'adresse dans
   Chrome sur Android ou Safari sur iPhone, puis « Ajouter à l'écran
   d'accueil ». Les données sont gardées par adresse : en changeant
   d'adresse, réimporter ses relevés et son fichier de classement.
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
