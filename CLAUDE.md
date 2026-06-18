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
