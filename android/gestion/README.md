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
