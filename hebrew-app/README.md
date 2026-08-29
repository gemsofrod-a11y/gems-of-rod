# Apprendre l'hébreu

Application web statique (HTML/CSS/JS, sans dépendance ni build) pour apprendre l'hébreu :

- **Alphabet** — les 22 lettres avec prononciation et exemple de mot.
- **Notions de base** — vocabulaire par thème (salutations, nombres, couleurs, jours, famille, quotidien).
- **Phrases** — phrases plus avancées ; cliquez sur un mot pour afficher sa phonétique et sa traduction.

Chaque mot dispose d'un bouton 🔊 qui déclenche la prononciation via la synthèse vocale du navigateur (voix `he-IL`). La disponibilité d'une voix hébraïque dépend du navigateur/appareil — Chrome (desktop et Android) offre la meilleure compatibilité.

## Utilisation

Aucune installation requise : ouvrez `index.html` dans un navigateur, ou servez le dossier localement, par exemple :

```bash
cd hebrew-app
python3 -m http.server 8080
```

puis ouvrez `http://localhost:8080`.

## Structure

```
hebrew-app/
├── index.html
├── css/style.css
└── js/
    ├── data.js   # contenu (alphabet, vocabulaire, phrases)
    └── app.js    # rendu + interactions (onglets, clic sur mot, prononciation)
```

Pour ajouter du vocabulaire ou des phrases, il suffit d'éditer `js/data.js`.
