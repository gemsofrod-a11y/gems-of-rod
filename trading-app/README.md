# Trading Or (XAU) — Gems of Rod

Application de suivi et de simulation de trading sur l'or au comptant
(XAU/USD), pensée comme projet séparé dans ce dépôt, indépendant de
l'application Android « encyclopédie / stock » de Gems of Rod.

## Ce que fait l'application

- **Cours XAU/USD en quasi temps réel**, via une source publique
  gratuite, avec repli automatique sur un cours simulé (marche
  aléatoire) si la source réelle est injoignable — l'app reste
  utilisable hors-ligne pour tester le bot ou l'interface.
- **Broker interne** : deux comptes par défaut, « Compte Réel » et
  « Compte Démo », chacun avec un solde, une position en onces, un
  historique de transactions et une courbe de valorisation. L'app est
  sa propre contrepartie — aucun argent réel ne transite par elle. Le
  compte « Réel » n'a de « réel » que ce que vous décidez d'y faire
  correspondre (voir avertissement plus bas).
- **Bot de trading automatique** avec deux stratégies d'analyse
  technique classiques (croisement de moyennes mobiles, RSI retour à
  la moyenne), paramétrables, activables/désactivables par compte.
- **Backtester** : rejoue une stratégie sur une série de cours
  simulée et calcule rendement, drawdown, nombre de trades, taux de
  réussite.
- **Connecteurs broker externes optionnels** (désactivés par défaut) :
  - **OANDA** — broker gratuit avec compte *practice* (démo) et API
    v20 publique, stable et documentée de longue date. C'est le
    connecteur recommandé pour une exécution réaliste sans risque.
  - **eToro** — connecteur **bêta**, écrit sans accès à la
    documentation officielle (bloquée depuis l'environnement de
    développement où il a été rédigé). Les chemins d'API et noms de
    champs sont une meilleure estimation à vérifier avant usage, même
    en démo (voir les commentaires en tête de
    `backend/etoro_connector.py`).
- **Interface web responsive**, utilisable sur téléphone (mobile
  d'abord, installable en PWA « ajouter à l'écran d'accueil »).

## ⚠️ Avertissement important

Cette application **ne se connecte à aucun courtier réel par défaut**
et ne peut, dans sa configuration de base, exécuter aucun ordre avec
de l'argent réel. Le « Compte Réel » est un registre interne : il
applique le vrai cours de l'or à des montants virtuels que vous seul
interprétez comme représentant votre argent réel.

Si vous activez le connecteur **OANDA**, les ordres sont exécutés sur
le compte OANDA (practice ou live) que vous avez configuré — sur un
compte *practice*, c'est un compte de démonstration gratuit sans
argent réel ; ne passez `OANDA_ENV=live` que si vous savez exactement
ce que vous faites, avec un compte OANDA réel et vos propres fonds.

Le connecteur **eToro** est expérimental (voir plus haut) : ne
l'activez pas sur un compte réel avant d'avoir vérifié son
comportement face à la documentation officielle
(https://api-portal.etoro.com/).

Aucune stratégie fournie ici ne garantit un profit. Ceci n'est pas un
conseil en investissement.

## Démarrage

Aucune dépendance à installer : uniquement la bibliothèque standard
Python 3 (>= 3.9).

```bash
cd trading-app
python3 -m backend.server
```

Puis ouvrir http://localhost:8420 dans un navigateur.

### Utilisation depuis un téléphone

Le serveur écoute sur `0.0.0.0:8420` : depuis un téléphone connecté au
même réseau Wi‑Fi que la machine qui exécute le serveur, ouvrez
`http://<adresse-ip-de-la-machine>:8420` dans le navigateur du
téléphone. Le site propose ensuite « Ajouter à l'écran d'accueil »
(PWA) pour un accès en un tap, comme une app installée.

Pour un accès depuis n'importe où (pas seulement le même Wi‑Fi), il
faut héberger le serveur sur une machine accessible depuis internet
(VPS, box avec redirection de port, etc.) — non fourni ici.

### Configuration optionnelle (brokers externes)

```bash
cp .env.example .env
# puis éditer .env avec vos identifiants OANDA et/ou eToro
```

Voir les commentaires dans `.env.example` pour la procédure
d'obtention de chaque identifiant.

## Structure

```
trading-app/
├── backend/
│   ├── server.py          # serveur HTTP (stdlib), routes API + fichiers statiques
│   ├── price_feed.py      # cours XAU/USD réel + repli simulé
│   ├── store.py           # persistance SQLite (comptes, trades, historique)
│   ├── broker.py          # broker interne : exécution d'ordres, solde/position
│   ├── execution.py       # routage broker interne / OANDA / eToro
│   ├── oanda_connector.py # connecteur OANDA (API v20, compte practice gratuit)
│   ├── etoro_connector.py # connecteur eToro (BÊTA, à vérifier)
│   ├── strategies.py      # stratégies du bot (SMA crossover, RSI)
│   ├── bot_engine.py       # exécution du bot en tâche de fond
│   ├── backtest.py         # backtester sur données simulées
│   └── config.py           # configuration (variables d'environnement / .env)
├── frontend/
│   ├── index.html, app.js, styles.css, manifest.json
└── data/                    # base SQLite locale (ignorée par git)
```

## Choix techniques notables

- **Aucune dépendance externe** côté serveur (stdlib uniquement) :
  aucune installation `pip` nécessaire, portable partout où Python 3
  tourne.
- **Le broker interne n'est jamais court-circuité** : même quand un
  broker externe exécute l'ordre, le registre interne (SQLite) reste
  la source de vérité affichée, avec le prix d'exécution et
  l'identifiant d'ordre réels du broker externe conservés sur la
  transaction.
