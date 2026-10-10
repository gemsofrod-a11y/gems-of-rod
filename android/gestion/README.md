# Gems of Rod Gestion (Android)

Application de gestion de la maison, distribuée en APK (pas sur le Play
Store) : commandes clients, stock, clients et tableau de bord avec
graphiques. Module `:gestion`, indépendant de l'encyclopédie (`:app`) et de
l'assistant vocal (`:assistant`).

## Design

Style « tableau de bord fintech » demandé par Sébastien le 03/10/2026 à
partir d'une maquette d'exemple : fond gris très clair, cartes blanches
arrondies à ombre douce, accent violet indigo, panneau marine pour la liste
« À suivre », onglets en pilule, barre de navigation flottante marine.
Captures à jour dans `screenshots/` (régénérées par le workflow
« Gestion screenshots » à chaque modification du module).

Depuis le 04/10/2026 (demande de Sébastien : « plus luxueux, couleurs moins
agressives ») : fond ivoire, or champagne, panneau onyx, titres et montants
en police à empattements, libellés en petites capitales espacées, courbes
fines dorées. Les cartes de l'Aperçu s'ouvrent sur une page de détail
(`ui/InsightScreen.kt` : CA et encaissé sur 12 mois, retards, délai de
paiement, reste à encaisser, ventes par catégorie, stock).

Thèmes (04/10/2026, Sébastien n'aimait pas le blanc) : « Rubis nuit » par
défaut (fond bordeaux très profond, cartes vieux rose foncé, or champagne),
et menu ⋮ → Thème pour choisir Onyx et or, Émeraude nuit, Saphir nuit,
Champagne (clair sans blanc) ou Ivoire (l'ancien style). Le choix est
appliqué aussitôt et mémorisé (préférences « apparence »). Toutes les
couleurs passent par `Palette`, qui lit `AppTheme.current` : ne jamais
figer une couleur dans une valeur calculée une seule fois (constante,
enum, `remember`), sinon elle ne suivrait pas un changement de thème.

## Écrans

- **Aperçu** : montant en retard, délai moyen de paiement, CA du mois
  (histogramme 6 mois), encaissé (courbe 6 mois), reste à encaisser,
  panneau « À suivre » (impayées / en retard / devis), ventes par catégorie,
  stock et alertes.
- **Commandes** : numérotation automatique (#CMD-0001), fiche façon
  facture avec bouton « Encaisser le solde », échéance de paiement (15 jours
  par défaut), client (liste ou saisie libre), date, statut (Devis → Confirmée → En préparation → Livrée, ou Annulée),
  articles du stock ou libres, acompte (bouton 30 %), reste à encaisser.
- **Stock** : pierres précieuses, pierres fines, métaux, bijoux ; unité
  (pièce, carat, gramme), prix d'achat / de vente, marge, seuil d'alerte.
- **Clients** : segment entièrement calculé sur 12 mois glissants (hors
  devis et annulations, règles de Sébastien du 04/10/2026, `segmentOf`) :
  VIP dès 500 € d'achats, sinon Régulier dès 3 commandes ou s'il a déjà été
  VIP (un ancien VIP retombe en Régulier), sinon Occasionnel. À la 3e
  commande sur 12 mois, un code personnel de livraison offerte
  (`GEMS-PRENOM-XXX`) est créé ; l'Aperçu signale les codes à envoyer et
  prépare un message de la maison (email/SMS/WhatsApp). Le code doit être
  créé à la main dans la boutique SumUp (pas d'API pour les réductions).
  À 5 commandes sur 12 mois, une remise fidélité de 5 % est proposée sur
  la commande suivante (`loyaltyDiscountAvailable`) : bouton « Appliquer »
  dans la saisie de commande, ligne « Remise fidélité 5 % » recalculée sur
  le sous-total ; une seule remise par période de 12 mois. Fiche client :
  « Panier du client » (articles achetés regroupés, panier moyen, dernier
  achat, `basketOf`), historique des commandes, appel ou email en un geste.
- **Paiements en ligne non aboutis** (demande de Sébastien, 04/10/2026) :
  SumUp n'expose pas les paniers abandonnés avant paiement ; l'app récupère
  en revanche les paiements en ligne échoués ou annulés (hors boutique :
  `POS`/`CASH` exclus), affichés sur l'Aperçu et dans l'écran SumUp, avec
  relance par nouveau lien de paiement du même montant ou « Écarter ». Ils
  disparaissent quand le même montant est payé dans les 3 jours
  (`openAttempts`). SumUp ne donne pas le nom du client de ces tentatives.

## Règles de stock

Un devis ne touche pas le stock. Dès qu'une commande est confirmée (ou en
préparation, ou livrée), ses quantités sont sorties du stock ; si elle est
annulée ou supprimée, elles y reviennent. Modifier une commande recalcule
la différence. Le CA et la marge comptent les commandes confirmées, en
préparation et livrées, à leur date de commande.

## SumUp

Menu ⋮ → **SumUp** : Sébastien colle sa clé API secrète (`sup_sk_…`, créée
sur me.sumup.com → Paramètres → Clés API) ; le code marchand est trouvé
automatiquement (`GET /v0.1/me`) ou saisi à la main.

- **Paiements** : à l'ouverture de l'app et sur « Synchroniser », les
  paiements réussis sont récupérés (`GET /v2.1/merchants/{code}/transactions/history`).
  Chacun peut être rattaché à une commande (le montant s'ajoute au reçu) ;
  sinon c'est une « vente directe », comptée dans le CA et l'encaissé.
- **Lien de paiement** : sur une fiche commande impayée, « Lien SumUp » crée
  un Hosted Checkout (`POST /v0.1/checkouts`) pour le reste dû et ouvre le
  partage Android (SMS, WhatsApp, email). À la synchronisation suivante, un
  lien payé encaisse la commande automatiquement. Nécessite que les
  paiements en ligne soient activés sur le compte SumUp ; SumUp limite la
  durée de validité de la page de paiement : recréer un lien si besoin.
- **Stock** : SumUp n'expose pas le catalogue ni le stock à une app tierce.
  En revanche le détail d'une transaction (`GET /v2.1/merchants/{code}/transactions?transaction_code=…`)
  donne les articles du catalogue SumUp vendus (`products` : nom, quantité).
  Pour les ventes postérieures à la connexion (`stockSince`, jamais
  rétroactif), l'app lit ces articles et les sort de son stock, reconnus par
  nom (sans accents ni casse) ou par une correspondance apprise
  (`sumupProductMap`, à choisir une fois dans l'écran SumUp). Une vente
  rattachée à une commande ne touche pas le stock (c'est la commande qui le
  gère) : rattacher/détacher rend ou ressort les quantités.
- **Prix repris des ventes** (demande de Sébastien, 07/10/2026) : SumUp
  n'expose pas son catalogue, donc un changement de tarif dans SumUp n'est
  pas visible tout de suite ; chaque vente récupérée garde le prix unitaire
  TTC de l'article (`SumUpItem.unitPrice`) et, quand les deux ventes les plus
  récentes d'un produit ont le même prix, différent de celui de l'app, le
  prix de l'app est mis à jour (`withSalePrices`) — une remise isolée ne
  change rien. Le message de synchronisation liste les prix changés. Pour un
  changement immédiat, réimporter l'export d'articles.
- **Import du catalogue et des clients** : SumUp n'ayant pas d'API pour
  lister catalogue et clients, le menu ⋮ → « Importer un export SumUp
  (Excel) » lit les exports .xlsx du tableau de bord (`items-export…xlsx` :
  nom, prix, quantité, seuil, réf., catégorie, description ;
  `customers_export…xlsx` : nom, email, téléphone, adresse). Type reconnu par
  l'entête ; ré-import = mise à jour (article par `Item id`, client par
  email/nom), jamais de doublon. L'export d'articles contenant tout le
  catalogue, un article venu de SumUp absent du nouvel export (supprimé dans
  SumUp) est retiré de l'app (produits créés dans l'app gardés ; demande de
  Sébastien, 04/10/2026). Les clients ne sont jamais retirés (historique des
  commandes). Les accents abîmés de l'export
  (« AmÃ©trine ») sont réparés (`SumUpImport.fixText`). Testé par
  `SumUpImportTest` sur des fichiers fabriqués — jamais de vrai export
  client dans le dépôt.
- Clé gardée dans les préférences privées de l'app, exclue des sauvegardes
  (`res/xml/backup_rules.xml`), jamais dans l'export JSON.

## Données

Tout reste sur le téléphone (`gestion.json` dans le stockage interne de
l'app, inclus dans la sauvegarde automatique Android) : aucune permission,
aucun accès réseau. Le menu ⋮ permet d'exporter / importer une sauvegarde
JSON (à faire régulièrement, par exemple vers Google Drive), de charger un
jeu d'exemple fictif ou de tout effacer.

## Obtenir l'APK

Le workflow GitHub Actions **Build Android APK** (`android-build.yml`)
compile le module à chaque push touchant `android/` et publie l'artefact
`gems-of-rod-gestion-debug-apk` (un zip contenant `gestion-debug.apk`) à
télécharger depuis l'onglet Actions, puis à installer sur le téléphone
(autoriser l'installation d'applications de sources inconnues).
