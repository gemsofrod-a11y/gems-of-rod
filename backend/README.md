# Gems of Rod — Assistant vocal Gmail (backend)

Service personnel (usage Sébastien uniquement) qui lit, classe et agit sur la
boîte `gemsofrod@gmail.com`, piloté depuis le téléphone — via le client web
intégré (`backend/static/`, aucune installation requise) ou l'app Android
native `android/assistant`.

## Ce qu'il fait

- **Triage automatique** (`app/triage.py`, lancé en boucle par
  `scripts/run_triage_loop.py`) : chaque nouvel email est classé par Claude en
  5 catégories et traité selon l'autonomie choisie :
  - `ignore` → marqué lu ou spam, rien d'autre.
  - `auto_delete` → **newsletters et publicités** : désabonnement tenté via
    l'en-tête Gmail `List-Unsubscribe` (best-effort — certains expéditeurs
    exigent une confirmation manuelle) puis **suppression** (corbeille,
    récupérable 30 jours comme dans Gmail).
  - `auto_triage` → notifications légitimes qui ne sont pas des
    newsletters/pubs (confirmations de commande, accusés administratifs) :
    étiquetées et archivées, jamais supprimées, aucune réponse.
  - `auto_reply` → réponse courte générée dans le ton Gems of Rod et
    **envoyée automatiquement** (accusés de réception, disponibilités,
    questions simples et récurrentes d'un contact non-VIP).
  - `needs_confirmation` → tout ce qui touche à un prix (y compris une
    demande de devis), une prise de rendez-vous, une négociation, un client
    VIP ou reste ambigu. Une réponse est *proposée* mais **jamais envoyée**
    sans validation dans l'app (à la voix ou via les boutons de l'écran
    « En attente »).
- **Commandes vocales** (`app/voice_agent.py`) : l'app envoie le texte transcrit
  par la reconnaissance vocale du téléphone, l'agent Claude peut chercher,
  lire, étiqueter, archiver, supprimer/désabonner ou répondre à un email, ou
  vous présenter les emails en attente pour que vous décidiez à voix haute.

Toute action (automatique ou vocale) est journalisée dans `var/assistant.db`
(SQLite) et dans `data/logs/voice_assistant.log` côté dépôt.

## Installation

```bash
cd backend
python3 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env   # puis renseignez ANTHROPIC_API_KEY et ASSISTANT_API_TOKEN
```

## Autoriser l'accès à Gmail (une seule fois)

1. Dans [Google Cloud Console](https://console.cloud.google.com/), créez (ou
   réutilisez) un projet, activez l'**API Gmail**, puis créez un identifiant
   OAuth de type **Application de bureau**.
2. Téléchargez le JSON et enregistrez-le sous `backend/credentials.json`
   (ignoré par git — ne jamais le commiter).
3. Lancez le flux d'autorisation, connectez-vous avec `gemsofrod@gmail.com` :

   ```bash
   cd backend
   python -m scripts.gmail_oauth_setup
   ```

   Cela crée `backend/token.json` (également ignoré par git), rafraîchi
   automatiquement ensuite.

Scope demandé : `gmail.modify` uniquement (lecture, envoi, étiquettes,
archivage, corbeille — jamais de suppression définitive/irréversible : un
message mis à la corbeille par l'assistant reste récupérable 30 jours,
comme dans l'interface Gmail).

## Lancer le service (en local, sur un PC)

```bash
cd backend
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

Depuis cette version, l'API lance elle-même le tri automatique en tâche de
fond (voir `TRIAGE_AUTOSTART` — activé par défaut) : un seul process suffit,
plus besoin de lancer `scripts/run_triage_loop.py` séparément. Le client web
(voir plus bas) est servi directement par ce même process sur `/`.

Exposez ce port à votre téléphone (réseau local, tunnel type Tailscale/ngrok)
si vous restez en local — mais voir la section **Déploiement cloud** ci-dessous
pour une solution qui ne dépend pas d'un PC allumé en permanence.

### Lancer le triage comme process séparé (optionnel)

Si vous préférez isoler le triage de l'API (ex. déploiement à part), mettez
`TRIAGE_AUTOSTART=false` dans `.env` puis, dans un second process :

```bash
cd backend
python -m scripts.run_triage_loop
```

## Déploiement cloud gratuit (recommandé)

Pour que le tri automatique tourne vraiment en continu sans dépendre d'un PC
allumé, déployez ce dossier `backend/` sur un hébergeur gratuit comme
[Render](https://render.com). Le fichier `backend/render.yaml` décrit déjà le
service (Blueprint Render) — il ne reste que des étapes manuelles côté
tableau de bord (identifiants, secrets) qu'aucune automatisation ne peut faire
à votre place :

1. **Authentifier Gmail en local une seule fois** (si ce n'est pas déjà fait) :
   suivez « Autoriser l'accès à Gmail » ci-dessus sur votre PC pour obtenir
   `backend/credentials.json` et `backend/token.json`. Ce sont ces deux
   fichiers qu'on transfère ensuite au serveur cloud — inutile de refaire le
   flux OAuth sur le serveur lui-même.
2. **Passer l'écran de consentement OAuth en « Production »** dans
   [Google Cloud Console](https://console.cloud.google.com/) → APIs et
   services → Écran de consentement OAuth → onglet Audience → bouton
   « Publier l'application ». Sans cette étape, Google régénère le jeton
   d'actualisation tous les 7 jours (mode « Test ») et le tri automatique
   s'arrêterait de fonctionner au bout d'une semaine sans réautorisation
   manuelle. En Production non vérifiée, un écran « application non
   validée par Google » apparaît la première fois — c'est normal pour une
   application personnelle, cliquez sur « Continuer ».
3. Créez un compte sur [render.com](https://render.com) (gratuit), connectez
   votre compte GitHub et autorisez l'accès au dépôt `gems-of-rod`.
4. Render → **New** → **Blueprint** → sélectionnez ce dépôt : il détecte
   `backend/render.yaml` et propose de créer le service `gems-of-rod-assistant`.
   Validez.
5. Render va demander les variables marquées `sync: false` dans le fichier —
   remplissez-les dans son tableau de bord (onglet *Environment*) :
   - `ANTHROPIC_API_KEY` : votre clé [console.anthropic.com](https://console.anthropic.com/)
     (sans date d'expiration, pour un service qui tourne en continu).
   - `ASSISTANT_API_TOKEN` : la même valeur longue et aléatoire que vous
     utiliserez ensuite pour vous connecter depuis le téléphone.
   - `GOOGLE_CREDENTIALS_JSON` : collez tout le contenu du fichier
     `backend/credentials.json` généré à l'étape 1.
   - `GOOGLE_TOKEN_JSON` : collez tout le contenu du fichier
     `backend/token.json` généré à l'étape 1.
6. Une fois déployé, Render donne une URL du type
   `https://gems-of-rod-assistant.onrender.com`. Ouvrez-la dans Chrome sur
   votre téléphone : c'est le client web de l'assistant (voir ci-dessous).
7. **Garder le service éveillé** : le plan gratuit de Render met le service
   en veille après 15 minutes sans requête (le tri automatique s'arrêterait
   pendant la veille). Pour un vrai fonctionnement 24h/24, ajoutez un ping
   gratuit toutes les 10 minutes vers `https://<votre-service>.onrender.com/api/health`
   via [UptimeRobot](https://uptimerobot.com) (gratuit, 2 minutes de config,
   type de moniteur « HTTP(s) »).

## Client web (téléphone, sans rien à installer)

`backend/static/index.html` est une petite page web (mic + synthèse vocale du
navigateur, aucune dépendance, aucun build) servie directement par l'API sur
son URL racine — pas de CORS, pas de configuration d'adresse serveur à
saisir puisqu'elle appelle l'API sur laquelle elle est hébergée. Sur le
téléphone :

1. Ouvrez l'URL du service (Render ou locale) dans Chrome.
2. Collez le `ASSISTANT_API_TOKEN` une seule fois — il reste enregistré sur
   l'appareil (`localStorage`).
3. Trois onglets : **Vocal** (micro ou texte, réponse lue à voix haute),
   **En attente** (emails à valider, réponse modifiable avant envoi),
   **Résumé** (compteurs du jour + bouton pour forcer un cycle de tri).
4. Menu ⋮ de Chrome → « Ajouter à l'écran d'accueil » pour une icône comme
   une vraie application.

Ceci remplace, pour un usage immédiat, l'application Android native
`android/assistant` (dont la compilation reste bloquée localement — voir son
propre README) : les deux peuvent coexister, ce sont deux clients différents
pour la même API.

## Endpoints REST (utilisés par l'app Android)

Toutes les routes `/api/*` sauf `/api/health` demandent l'en-tête
`Authorization: Bearer <ASSISTANT_API_TOKEN>`.

| Méthode | Route | Rôle |
|---|---|---|
| POST | `/api/voice` | Envoie un texte (commande vocale transcrite), reçoit la réponse à lire à voix haute. |
| GET | `/api/pending` | Liste les emails complexes en attente de décision. |
| POST | `/api/pending/{id}/approve` | Envoie la réponse suggérée (ou modifiée). |
| POST | `/api/pending/{id}/reject` | N'envoie rien, laisse l'email tel quel. |
| POST | `/api/triage/run` | Déclenche un cycle de triage à la demande. |
| GET | `/api/digest/today` | Résumé des actions automatiques du jour + nombre en attente. |

## Limites connues (v1)

- Pas de notification push : l'app (ou le client web) doit être ouverte pour
  voir le nombre d'emails en attente (rafraîchi à l'ouverture et après chaque
  commande vocale). Une notification FCM pourra être ajoutée ensuite si besoin.
- Un seul utilisateur, une seule boîte Gmail : pas de gestion multi-compte.
- Le serveur doit tourner en continu quelque part pour que le triage
  automatique et les commandes vocales fonctionnent — voir « Déploiement
  cloud gratuit » ci-dessus pour ne pas dépendre d'un PC allumé en permanence.
- Sur un hébergeur gratuit sans disque persistant (ex. plan gratuit Render),
  `var/assistant.db` (historique des emails déjà traités, en attente) est
  réinitialisée à chaque redéploiement. Sans conséquence grave : un email
  déjà traité pourrait être reclassé une fois après un redéploiement, mais
  rien n'est perdu côté Gmail (tout reste récupérable dans la boîte/corbeille).
