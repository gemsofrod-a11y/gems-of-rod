# Gems of Rod — Base de connaissance & Guide de l'agent IA

## L'entreprise

**Gems of Rod** est une maison française indépendante fondée en 2020, spécialisée dans la sélection, la vente et la valorisation de pierres précieuses, pierres fines, métaux précieux et bijoux d'exception. Basée en France, elle s'adresse à une clientèle de collectionneurs, investisseurs et amateurs de joaillerie fine.

**Contact principal :** gemsofrod@gmail.com  
**Site web :** gems-of-rod.fr  
**Instagram :** @gemsofrod  
**Slogan :** *"Pierres précieuses & bijoux d'exception"*  

---

## Catégories de produits

### Pierres précieuses
Les quatre grandes pierres précieuses : **diamant, rubis, émeraude, saphir**. Critères de sélection : origine, traitement, certification GIA/GUBELIN/GFCO, couleur (hue/tone/saturation), pureté (clarity), taille (cut).

### Pierres fines
Alexandrite, tanzanite, spinelle, tourmaline, topaze impériale, grenat démantoïde, kunzite, morganite, etc. Sélectionnées pour leur rareté et leur éclat.

### Métaux précieux
Or 18 carats (jaune, blanc, rose), platine, palladium. Achat/vente de lingots, pièces et recyclage joaillier.

### Bijoux
Bijoux sertis sur commande, créations partenaires joailliers. Bagues, colliers, pendentifs, boucles d'oreilles.

---

## Voix de marque & Ton

- **Registre :** Luxueux, authentique, expert, chaleureux mais formel
- **Langue :** Français en priorité, anglais pour les fournisseurs internationaux
- **Valeurs :** Rareté, authenticité, transparence, passion, expertise
- **À éviter :** Langage commercial agressif, fausses promesses, hyperboles vides

**Formules d'ouverture email :** "Madame, Monsieur," / "Cher [prénom]," (clients VIP)  
**Signature :** "Avec mes sincères salutations, / L'équipe Gems of Rod"

---

## Fournisseurs & Partenaires

### Types de fournisseurs
- **Lapidaires** : Taillent et polissent les pierres brutes
- **Négociants** : Achètent/vendent des pierres taillées en gros
- **Joailliers/Bijoutiers** : Créent des montages sur mesure
- **Mineurs/Exportateurs** : Fournissent des pierres brutes à la source

### Approche fournisseurs
- Toujours se présenter avec le nom de la société
- Préciser les volumes indicatifs pour obtenir des prix sérieux
- Demander certificats d'origine et fiches gemmologiques
- Négocier avec respect des délais et des cultures (Asie du Sud-Est, Afrique de l'Est, Amérique du Sud)

---

## Clients

### Segments
- **VIP** : Gros acheteurs (>5k€/an), contact direct, newsletter prioritaire
- **Réguliers** : 1-3 achats/an, newsletter mensuelle
- **Prospects** : Inscrits à la newsletter, pas encore acheteurs

### Communication clients
- Newsletter mensuelle avec nouvelles pierres et articles
- Email personnalisé pour les VIP sur les nouvelles acquisitions rares
- Propositions de bijoux sur mesure aux VIP

---

## Architecture de l'agent IA

### Fichiers principaux
```
agent/
├── main.py              # Point d'entrée CLI : python agent/main.py [commande]
├── agent.py             # Boucle agent avec Anthropic SDK (claude-opus-4-8)
├── config.py            # Configuration et chargement des données
├── tools/
│   ├── products.py      # Gestion des fiches produits
│   ├── instagram.py     # Création de posts Instagram
│   ├── suppliers.py     # Communications fournisseurs
│   ├── articles.py      # Articles site web
│   ├── newsletter.py    # Newsletters clients
│   └── email_tools.py   # Emails et récapitulatif journalier
└── knowledge/
    ├── company.json     # Infos société
    ├── products.json    # Catalogue produits
    ├── suppliers.json   # Base fournisseurs
    └── clients.json     # Base clients

data/
├── products/            # Fiches produits individuelles (JSON)
├── drafts/
│   ├── emails/          # Emails en attente d'envoi
│   ├── instagram/       # Posts Instagram prêts
│   ├── articles/        # Articles à valider
│   └── newsletters/     # Newsletters prêtes
└── logs/                # Journal des actions de l'agent
```

### Commandes disponibles
```bash
python agent/main.py run              # Lancement complet journalier
python agent/main.py products         # Gestion des fiches produits uniquement
python agent/main.py instagram        # Création de posts Instagram
python agent/main.py suppliers        # Communications fournisseurs
python agent/main.py articles         # Gestion articles web
python agent/main.py newsletter       # Création newsletter
python agent/main.py summary          # Envoi récapitulatif journalier
python agent/main.py ask "question"   # Poser une question précise à l'agent
```

---

## Workflow d'orchestration Claude Code

Quand l'agent Python génère du contenu (drafts), Claude Code doit ensuite :

### Envoi des emails (Gmail MCP)
1. Lire les fichiers dans `data/drafts/emails/`
2. Utiliser `mcp__Gmail__create_draft` pour créer des brouillons Gmail
3. Pour les récapitulatifs journaliers, envoyer directement à gemsofrod@gmail.com

### Posts Instagram (Canva MCP)
1. Lire les briefs dans `data/drafts/instagram/`
2. Utiliser `mcp__Canva__search-brand-templates` pour trouver des templates adaptés
3. Utiliser `mcp__Canva__generate-design` ou `mcp__Canva__create-design-from-brand-template`
4. Exporter le visuel avec `mcp__Canva__export-design`

### Articles web (Google Drive MCP)
1. Lire les articles dans `data/drafts/articles/`
2. Utiliser `mcp__Google_Drive__create_file` pour sauvegarder dans Drive
3. Partager le lien pour validation

### Newsletters
1. Lire dans `data/drafts/newsletters/`
2. Créer un brouillon Gmail avec `mcp__Gmail__create_draft`

---

## Règles de l'agent

1. **Toujours logger** chaque action dans `data/logs/`
2. **Jamais envoyer** d'email sans sauvegarder le brouillon d'abord
3. **Proposer** les articles, ne pas les publier directement
4. **Poser des questions** à l'utilisateur pour les décisions importantes
5. **Récapitulatif journalier** : toujours en fin de session
6. **Ton luxueux** dans toutes les communications

---

## Routines automatiques (planifiées)

Les routines s'exécutent automatiquement selon le planning ci-dessous. Chaque routine :
1. Lance l'agent Python correspondant
2. Traite les drafts générés via les MCP (Gmail, Canva, Google Drive)
3. Crée des **brouillons** — jamais d'envoi automatique sans validation

### Planning hebdomadaire

| Routine | Jours | Heure | Commande agent |
|---|---|---|---|
| Instagram | Lun / Mer / Ven | 9h03 | `python agent/main.py instagram` |
| Fournisseurs | Mardi | 9h07 | `python agent/main.py suppliers` |
| Articles web | Mercredi | 10h05 | `python agent/main.py articles` |
| Fiches produits | Jeudi | 9h05 | `python agent/main.py products` |
| Newsletter | 1er du mois | 8h05 | `python agent/main.py newsletter` |
| Récapitulatif | Lun–Ven | 18h03 | `python agent/main.py summary` |

### Ce que chaque routine produit

**Instagram (Lun/Mer/Ven 9h03)**
- Génère 2+ posts via `instagram` agent
- Crée les visuels sur Canva (`mcp__Canva__*`)
- Envoie un brouillon Gmail de récapitulatif avec liens Canva

**Fournisseurs (Mar 9h07)**
- Génère les emails fournisseurs (demandes de prix, relances, devis)
- Crée des brouillons Gmail (`mcp__Gmail__create_draft`) — prêts à l'envoi
- NE PAS envoyer sans validation

**Articles web (Mer 10h05)**
- Propose 1 article de blog gemmologique
- Sauvegarde dans Google Drive (`mcp__Google_Drive__create_file`)
- Envoie un brouillon Gmail avec le lien Drive pour validation

**Fiches produits (Jeu 9h05)**
- Enrichit et met à jour les fiches produits existantes
- Crée les nouvelles fiches si nécessaire
- Envoie un brouillon Gmail de synthèse

**Newsletter (1er du mois 8h05)**
- Génère la newsletter mensuelle HTML
- Crée un brouillon Gmail avec le contenu HTML
- Attendre la validation avant envoi à la liste clients

**Récapitulatif journalier (Lun–Ven 18h03)**
- Synthèse de toutes les actions de la journée
- Questions en attente de réponse
- Prochaines étapes suggérées
- Brouillon Gmail envoyé à gemsofrod@gmail.com

### Fichiers de configuration des routines
Les routines durables sont stockées dans `.claude/scheduled_tasks.json` et survivent aux redémarrages de session.

---

## Application Android — Encyclopédie « Gems of Rod »

Contenu éditorial (fiches Info.kt : `RochesMeresInfo`, `LapidaireInfo`, `GemGlossary`, etc.) dans `android/app/src/main/java/fr/gemsofrod/encyclopedie/data/` et `android/app/src/commonMain/kotlin/fr/gemsofrod/encyclopedie/data/`.

**Règle de traduction — automatique, sans qu'on ait à le redemander :** toute nouvelle section, fiche ou terme de glossaire ajouté à l'encyclopédie doit être traduit dans les **9 langues de l'app** (fr, en, es, it, de, pt, ru, nl, zh) avant d'être livré, dans la même PR que l'ajout — jamais laissé français uniquement par défaut. Exception : si une section est explicitement livrée français-only à la demande de l'utilisateur (ex. pour valider le contenu avant d'investir dans la traduction), le dire clairement et proposer la traduction dans un message séparé ensuite.

Pour traduire un nouveau contenu :
1. Repérer le bloc `private val fr = ...Page(...)` et les 8 autres blocs de langue existants dans le même fichier/objet.
2. Ajouter le nouveau contenu au même index/position dans chacun des 8 autres blocs (l'ordre suit celui du français, pas un ré-alphabétisation par langue — voir `GemGlossary.kt`).
3. Réutiliser la terminologie déjà employée ailleurs dans le même fichier pour cette langue (ex. comment « meule », « cabochon », « facettage » sont déjà traduits) plutôt que d'introduire une variante.
4. Vérifier l'équilibre des parenthèses/accolades et le nombre d'entrées par langue après édition (ex. `grep -c` sur le nom de la classe de données) avant de committer.
