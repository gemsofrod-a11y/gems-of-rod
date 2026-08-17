# Écho — Journal vocal quotidien

App web (PWA) minimaliste, personnelle et gratuite : chaque jour tu parles
~30 secondes, l'app transcrit ta voix dans le navigateur et détecte des
tendances émotionnelles (énergie, stress, fatigue, humeur) dans le temps.

- **100% local** : tout est stocké dans le `localStorage` du navigateur, rien
  n'est envoyé à un serveur.
- **Gratuit** : transcription via la Web Speech API du navigateur (aucune clé
  API, aucun abonnement), analyse par mots-clés faite en JavaScript.
- **PWA installable** : fonctionne hors-ligne une fois chargée, peut
  s'ajouter à l'écran d'accueil du téléphone.

## Lancer en local

La reconnaissance vocale et le service worker nécessitent d'être servis en
`http(s)://`, pas en `file://`. Depuis ce dossier :

```bash
cd echo-app
python3 -m http.server 8000
```

Puis ouvrir `http://localhost:8000` dans Chrome, Edge ou Safari récent (sur
mobile, utiliser l'IP locale de la machine ou déployer sur un hébergement
statique gratuit comme GitHub Pages).

## Limites connues (MVP)

- La Web Speech API n'est pas supportée par Firefox et varie selon les
  navigateurs mobiles — un message s'affiche si elle est indisponible.
- L'analyse est basée sur des mots-clés français simples, pas un vrai modèle
  de sentiment : elle donne une tendance indicative, pas un diagnostic.
- Les tendances hebdomadaires deviennent pertinentes après quelques jours
  d'utilisation régulière (minimum 3 entrées).
- Pas de sauvegarde cloud : exporter régulièrement ses données (onglet
  Réglages) pour ne pas les perdre en cas de changement d'appareil ou de
  nettoyage du navigateur.
