# Écho — Journal vocal quotidien

App web (PWA) minimaliste, personnelle et gratuite : chaque jour tu parles
~30 secondes, l'app transcrit ta voix dans le navigateur et détecte des
tendances émotionnelles (énergie, stress, fatigue, humeur) dans le temps.

- **100% local par défaut** : tout est stocké dans le `localStorage` du
  navigateur, rien n'est envoyé à un serveur.
- **Gratuit** : transcription via la Web Speech API du navigateur (aucune clé
  API, aucun abonnement), analyse par mots-clés faite en JavaScript.
- **PWA installable** : fonctionne hors-ligne une fois chargée, peut
  s'ajouter à l'écran d'accueil du téléphone.
- **Compagnon IA optionnel** : si une clé `ANTHROPIC_API_KEY` est configurée
  côté serveur (variable d'environnement Netlify, voir
  `netlify/functions/companion.js`), l'écran de résumé affiche en plus une
  réponse courte et personnalisée générée par Claude. Sans clé, l'app
  continue de fonctionner normalement avec les suggestions locales.
- **Filet de sécurité** : une carte avec des numéros d'urgence (15, 112,
  3114) s'affiche systématiquement — sans dépendre du réseau — si des mots
  de détresse aiguë sont détectés dans la transcription.

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
- L'analyse par défaut est basée sur des mots-clés français simples, pas un
  vrai modèle de sentiment : elle donne une tendance indicative, pas un
  diagnostic. Le compagnon IA (optionnel) apporte une lecture plus nuancée
  quand une clé API est configurée.
- Les tendances hebdomadaires deviennent pertinentes après quelques jours
  d'utilisation régulière (minimum 3 entrées).
- Pas de sauvegarde cloud : exporter régulièrement ses données (onglet
  Réglages) pour ne pas les perdre en cas de changement d'appareil ou de
  nettoyage du navigateur.
- **Piste audio (pauses, pics de volume, ton moyen en Hz) : coupée en dur
  pour l'instant.** Deux tentatives — capturer le micro via `getUserMedia`
  seulement après le premier résultat confirmé de reconnaissance vocale, et
  brancher en plus un compteur de niveau en direct (`AudioContext` /
  `AnalyserNode`) — ont toutes les deux fini par figer la transcription en
  plein enregistrement sur un appareil Android réel (aucune erreur, plus
  aucun résultat). Toute capture audio brute simultanée à la reconnaissance
  vocale semble donc incompatible avec ce moteur, pas seulement une
  question de timing. Le réglage dans Réglages est désactivé (grisé) et
  `isAudioTrackEnabled()` renvoie toujours `false` en dur dans `js/app.js`
  tant qu'une approche non simultanée n'a pas été trouvée et validée sur
  appareil réel — la transcription reste la fonction principale d'Écho et
  ne doit jamais être mise en danger pour cette fonctionnalité annexe.
- **Bulle d'aide sur les courbes tendances** : un bouton "?" à côté du
  graphique Énergie/Stress explique ce que veulent dire des scores hauts ou
  bas, pour ne pas laisser deviner l'échelle 0–100.
