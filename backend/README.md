# Gems of Rod — Assistant vocal Gmail (backend)

Service personnel (usage Sébastien uniquement) qui lit, classe et agit sur la
boîte `gemsofrod@gmail.com`, piloté depuis l'app Android `android/assistant`.

## Ce qu'il fait

- **Triage automatique** (`app/triage.py`, lancé en boucle par
  `scripts/run_triage_loop.py`) : chaque nouvel email est classé par Claude en
  4 catégories et traité selon l'autonomie choisie :
  - `ignore` → marqué lu ou spam, rien d'autre.
  - `auto_triage` → étiqueté et archivé (newsletters, confirmations
    automatiques), aucune réponse.
  - `auto_reply` → réponse courte générée dans le ton Gems of Rod et
    **envoyée automatiquement** (accusés de réception, disponibilités,
    questions simples et récurrentes d'un contact non-VIP).
  - `needs_confirmation` → tout ce qui touche à un prix, une négociation, un
    client VIP ou reste ambigu. Une réponse est *proposée* mais **jamais
    envoyée** sans validation dans l'app (à la voix ou via les boutons de
    l'écran « En attente »).
- **Commandes vocales** (`app/voice_agent.py`) : l'app envoie le texte transcrit
  par la reconnaissance vocale du téléphone, l'agent Claude peut chercher,
  lire, étiqueter, archiver ou répondre à un email, ou vous présenter les
  emails en attente pour que vous décidiez à voix haute.

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
archivage — pas de suppression définitive possible).

## Lancer le service

```bash
cd backend
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

Exposez ce port à votre téléphone (réseau local, tunnel type Tailscale/ngrok,
ou déploiement sur un petit serveur/VPS avec HTTPS) puis renseignez l'URL et
le jeton (`ASSISTANT_API_TOKEN`) dans les Réglages de l'app Android.

## Lancer le triage automatique

Dans un second processus (service systemd, `tmux`/`screen`, ou conteneur) :

```bash
cd backend
python -m scripts.run_triage_loop
```

Chaque cycle (toutes les `TRIAGE_INTERVAL_SECONDS`, 5 min par défaut) traite
les nouveaux emails non lus de la boîte de réception.

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

- Pas de notification push : l'app doit être ouverte pour voir le nombre
  d'emails en attente (rafraîchi à l'ouverture et après chaque commande
  vocale). Une notification FCM pourra être ajoutée ensuite si besoin.
- Un seul utilisateur, une seule boîte Gmail : pas de gestion multi-compte.
- Le serveur doit tourner en continu quelque part (pas de mode « éteint par
  défaut ») pour que le triage automatique et les commandes vocales
  fonctionnent.
