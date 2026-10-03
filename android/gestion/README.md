# Gems of Rod Gestion (Android)

Application de gestion de la maison, distribuée en APK (pas sur le Play
Store) : commandes clients, stock, clients et tableau de bord avec
graphiques. Module `:gestion`, indépendant de l'encyclopédie (`:app`) et de
l'assistant vocal (`:assistant`).

## Écrans

- **Accueil** : CA du mois (et évolution vs mois précédent), marge du mois,
  commandes en cours, valeur du stock ; histogramme du CA sur 6 mois,
  répartition des ventes par catégorie (12 mois), valeur du stock par
  catégorie, commandes à traiter, alertes de stock bas.
- **Commandes** : numérotation automatique, client (liste ou saisie libre),
  date, statut (Devis → Confirmée → En préparation → Livrée, ou Annulée),
  articles du stock ou libres, acompte (bouton 30 %), reste à encaisser.
- **Stock** : pierres précieuses, pierres fines, métaux, bijoux ; unité
  (pièce, carat, gramme), prix d'achat / de vente, marge, seuil d'alerte.
- **Clients** : segment VIP / Régulier / Prospect, total des achats,
  historique des commandes, appel ou email en un geste.

## Règles de stock

Un devis ne touche pas le stock. Dès qu'une commande est confirmée (ou en
préparation, ou livrée), ses quantités sont sorties du stock ; si elle est
annulée ou supprimée, elles y reviennent. Modifier une commande recalcule
la différence. Le CA et la marge comptent les commandes confirmées, en
préparation et livrées, à leur date de commande.

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
