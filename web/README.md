# Gems of Rod — Boutique en ligne

Application web de la boutique **Gems of Rod** : catalogue de pierres
précieuses, pierres fines, métaux précieux et bijoux, journal (articles), et
un espace professionnel pour gérer le stock, les articles et les demandes de
devis directement depuis un téléphone ou un ordinateur.

Construite avec [Next.js](https://nextjs.org) (App Router, TypeScript,
Tailwind CSS), [Prisma](https://www.prisma.io) et
[NextAuth](https://authjs.dev). Pensée pour être **modulable** (catégories,
champs produit, articles) et pour devenir, plus tard, une application
Android qui se met à jour automatiquement (voir plus bas).

## Fonctionnement général

- **Vitrine publique** (`/`, `/boutique`, `/articles`, `/contact`,
  `/a-propos`) : pas de paiement en ligne. Un visiteur intéressé par une
  pierre envoie une **demande de devis/contact**, dans l'esprit conseil et
  sur-mesure d'une maison de pierres précieuses.
- **Espace professionnel** (`/admin`), protégé par connexion :
  - **Produits** : ajouter/modifier/retirer une pierre ou un bijou, avec
    photos prises directement depuis l'appareil photo du téléphone ou
    importées depuis la galerie.
  - **Articles** : rédiger et publier les billets du journal.
  - **Demandes** : suivre les demandes de devis reçues (nouveau / en cours /
    traité).
  - **Utilisateurs** *(administrateurs uniquement)* : créer d'autres
    comptes (rôle Administrateur ou Éditeur), activer/désactiver un accès.
  - **Journal d'activité** *(administrateurs uniquement)* : historique de
    qui a fait quoi (ajout produit, modification, changement de statut...).

Deux rôles :

| Rôle | Peut gérer |
|---|---|
| **Administrateur** | Produits, articles, demandes, utilisateurs, journal d'activité |
| **Éditeur** | Produits, articles, demandes |

## Démarrage local

Nécessite une base Postgres. Le plus simple : démarrer uniquement le
service Postgres du `docker-compose.yml` à la racine du dépôt (pas besoin
de lancer toute l'app dans Docker pour développer) :

```bash
# Depuis la racine du dépôt, une fois :
cp .env.production.example .env   # POSTGRES_PASSWORD peut rester "change-me" en local
docker compose up -d postgres

# Depuis web/ :
npm install
cp .env.example .env.local        # DATABASE_URL y pointe déjà vers ce Postgres local
npx prisma migrate dev            # applique le schéma
npm run db:seed                   # crée le premier compte admin + importe le catalogue existant
npm run dev
```

Le seed crée un compte administrateur (email/mot de passe pris dans
`SEED_ADMIN_EMAIL` / `SEED_ADMIN_PASSWORD`, sinon des valeurs par défaut
affichées dans le terminal) et importe une fois le catalogue existant
(`agent/knowledge/products.json`, utilisé par l'agent Python) dans la base.
**Changez le mot de passe dès la première connexion** depuis
`/admin/utilisateurs`.

Après le seed, ce catalogue de départ vit dans la base de données : les
modifications se font ensuite depuis `/admin/produits`, pas en éditant le
fichier JSON.

## Variables d'environnement

Voir `.env.example`. En particulier :

- `DATABASE_URL` — connexion Postgres (locale via Docker en développement,
  ou managée/VPS en production — voir « Déploiement »).
- `AUTH_SECRET` — secret de session, à générer avec `openssl rand -base64 32`.

## Photos produits & articles

Les photos envoyées depuis l'admin (y compris directement depuis l'appareil
photo du téléphone) sont redimensionnées et compressées automatiquement,
puis stockées dans `data/uploads/` (hors de `public/`, car Next.js ne sert
que les fichiers présents dans `public/` au démarrage — les fichiers ajoutés
pendant que le serveur tourne y sont invisibles). Elles sont servies par une
route dédiée (`src/app/uploads/[...path]/route.ts`). Ce dossier doit donc
faire partie de la **sauvegarde régulière** du serveur (voir ci-dessous).

## Déploiement

La base de données (Postgres) est accessible en réseau, donc n'importe
quel hébergeur convient de ce côté. Il reste un état sur disque à
préserver : les photos uploadées (`data/uploads/`). Deux options :

1. **Recommandé pour démarrer : un VPS avec Docker** (voir ci-dessous),
   avec Postgres en conteneur à côté de l'app. Simple, pas de service tiers
   à payer. Sauvegardez régulièrement les volumes Docker `postgres-data` et
   `uploads-data`.
2. **Plateforme serverless (Vercel, Netlify...)** : le système de fichiers
   n'y est pas persistant entre les requêtes, donc `data/uploads/` ne peut
   pas y vivre. Il faut envoyer les photos vers un stockage objet (S3,
   Cloudinary...) à la place, en adaptant `src/lib/uploads.ts` — la base de
   données Postgres, elle, fonctionne déjà nativement sur ces plateformes
   (pointez `DATABASE_URL` vers une base managée : Neon, Supabase,
   Railway...).

### VPS avec Docker (recommandé)

Un `Dockerfile`, `docker-compose.yml` et `Caddyfile` sont fournis à la
racine du dépôt. Caddy sert de reverse proxy et obtient automatiquement un
certificat HTTPS (Let's Encrypt) pour votre domaine.

1. **Créer un serveur.** Par exemple chez [Hetzner Cloud](https://www.hetzner.com/cloud/)
   (~4-5 €/mois, image Ubuntu 24.04 — le plus petit plan CX22 suffit
   largement) ou une alternative française comme [OVH](https://www.ovhcloud.com/fr/vps/).
   Une fois créé, notez son adresse IP.
2. **Pointer le domaine.** Dans la zone DNS de `gemsofrod.com` (ou
   `gems-of-rod.fr`), créez un enregistrement `A` (et un pour `www`) vers
   l'adresse IP du serveur.
3. **Installer Docker** sur le serveur (en SSH) :
   ```bash
   curl -fsSL https://get.docker.com | sh
   ```
4. **Récupérer le code** sur le serveur :
   ```bash
   git clone <url-du-dépôt> gems-of-rod
   cd gems-of-rod
   ```
5. **Configurer l'environnement** :
   ```bash
   cp .env.production.example .env
   nano .env   # renseigner DOMAIN, DATABASE_URL, AUTH_SECRET (openssl rand -base64 32), etc.
   ```
6. **Démarrer** :
   ```bash
   docker compose up -d --build
   ```
   Caddy obtient le certificat HTTPS automatiquement dès que le DNS pointe
   correctement vers le serveur (ports 80/443 ouverts).
7. **Créer le premier compte admin + importer le catalogue** (une seule fois) :
   ```bash
   docker compose exec web npm run db:seed
   ```

Pour mettre à jour après un nouveau `git pull` :

```bash
git pull
docker compose up -d --build
```

Les migrations (`prisma migrate deploy`) s'appliquent automatiquement à
chaque démarrage du conteneur. Postgres et les photos vivent dans les
volumes Docker `postgres-data` et `uploads-data`, qui survivent aux
reconstructions de l'image — pensez à les sauvegarder régulièrement, par
exemple :

```bash
docker compose exec postgres pg_dump -U gemsofrod gemsofrod > backup-$(date +%F).sql
docker run --rm -v gems-of-rod_uploads-data:/data -v $(pwd):/backup alpine tar czf /backup/photos-$(date +%F).tar.gz /data
```

### Netlify

Un `netlify.toml` (à la racine du dépôt) est déjà configuré pour construire
`web/` avec le plugin officiel `@netlify/plugin-nextjs`. Il suffit de
connecter le dépôt sur Netlify (branche à déployer) et de définir dans les
réglages du site (Site configuration → Environment variables) :

- `AUTH_SECRET`
- `DATABASE_URL` (une base Postgres managée : Neon, Supabase...)
- `SEED_ADMIN_EMAIL`, `SEED_ADMIN_PASSWORD` (si vous voulez ré-exécuter le seed)

**Important :** avec `DATABASE_URL` sur une vraie base Postgres, le
catalogue, les articles et les demandes de devis fonctionnent normalement
sur Netlify. Seules les **photos uploadées depuis l'admin** ne persisteront
pas de façon fiable (fonctions serverless sans disque persistant) tant que
`src/lib/uploads.ts` n'envoie pas vers un stockage objet (S3, Cloudinary...)
— pour une simple vitrine sans gestion de photos depuis Netlify, ce n'est
pas bloquant.

Dans tous les cas :

```bash
npm run build
npx prisma migrate deploy   # applique les migrations en production
npm run start
```

Pensez à définir `AUTH_SECRET` (obligatoire) et, si l'app tourne derrière un
reverse proxy (Nginx, Caddy...) sur un nom de domaine, à ce que ce proxy
transmette bien l'en-tête `Host` — NextAuth est configuré en `trustHost`
pour un déploiement auto-hébergé.

Le nom de domaine `gemsofrod.com` (ou `gems-of-rod.fr`) doit pointer vers ce
serveur. `src/app/layout.tsx` utilise cette URL dans les métadonnées
(`metadataBase`) — à ajuster si le domaine final diffère.

## Devenir une application Android qui se met à jour automatiquement

L'application est déjà une **PWA** (Progressive Web App) : manifeste
(`src/app/manifest.ts`), icônes et service worker (`public/sw.js`) sont en
place. Dès que le site est déployé sur son domaine, un visiteur Android peut
déjà l'« Ajouter à l'écran d'accueil » depuis Chrome et l'utiliser comme une
app.

Pour aller plus loin et publier une vraie application sur le Play Store qui
**se met à jour toute seule sans jamais republier l'app elle-même** :

1. Utiliser une **TWA (Trusted Web Activity)** : une coquille Android très
   fine qui affiche le site en plein écran (sans barre d'adresse). Le
   contenu (nouvelles pierres, articles, prix) vient toujours du serveur en
   direct — l'app ne se « met à jour » pratiquement jamais, puisqu'il n'y a
   rien à mettre à jour côté app, tout vit sur le site.
2. Générer cette coquille avec l'outil officiel Google
   [Bubblewrap](https://github.com/GoogleChromeLabs/bubblewrap) :
   ```bash
   npm install -g @bubblewrap/cli
   bubblewrap init --manifest=https://gemsofrod.com/manifest.webmanifest
   bubblewrap build
   ```
   Cela produit un projet Android (dans un nouveau dossier, séparé de
   `android/` qui contient l'encyclopédie) prêt à être signé et envoyé sur
   le Play Store.
3. Publier une fois sur le Play Store. Ensuite, Android gère lui-même les
   mises à jour de la coquille (rares) via le Play Store standard ; le
   contenu, lui, est toujours à jour car chargé en direct depuis le site.

Cette étape est volontairement **séparée** du dépôt `android/` existant
(l'encyclopédie gemmologique, module `:app`) et de `android/assistant`
(assistant vocal Gmail) : ce sera un troisième module Android indépendant,
à créer quand la boutique sera prête à être publiée en tant qu'app.

## Structure du projet

```
web/
├── prisma/
│   ├── schema.prisma        # Modèle de données (Product, Article, User, Inquiry, AuditLog)
│   └── seed.ts               # Premier compte admin + import du catalogue existant
├── src/
│   ├── app/
│   │   ├── page.tsx, boutique/, articles/, contact/, a-propos/  # Vitrine publique
│   │   ├── admin/(auth)      # Connexion
│   │   ├── admin/(dashboard) # Produits, articles, demandes, utilisateurs, journal
│   │   ├── api/auth/...      # NextAuth
│   │   ├── uploads/...       # Sert les photos envoyées depuis l'admin
│   │   └── manifest.ts       # Manifeste PWA
│   ├── components/           # UI publique + composants admin
│   ├── lib/
│   │   ├── actions/          # Server Actions (produits, articles, devis, utilisateurs, connexion)
│   │   ├── prisma.ts, uploads.ts, audit.ts, validation.ts, session.ts
│   └── proxy.ts              # Protège /admin (redirige vers /admin/connexion si non connecté)
└── public/                   # Logo, icônes, manifeste, service worker
```

## Commandes utiles

```bash
npm run dev          # serveur de développement
npm run build        # build de production
npm run start        # sert le build de production
npm run lint         # ESLint
npm run db:seed      # (re)crée le compte admin + importe le catalogue existant
npm run db:migrate   # applique une nouvelle migration Prisma en développement
npm run db:studio    # interface graphique pour explorer la base
```
