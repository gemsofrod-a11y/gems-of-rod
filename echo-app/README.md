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
  continue de fonctionner normalement avec les suggestions locales. Le
  prompt s'inspire de techniques d'écoute active et d'entretien
  motivationnel (reformulation, question ouverte, appui sur le contexte des
  derniers jours) — mais il ne prétend jamais être un·e psychologue ou un
  professionnel de santé, ne diagnostique rien, et invite à consulter un
  proche ou un professionnel si le ressenti semble intense. Cette limite
  est non négociable dans le prompt, volontairement placée au-dessus des
  consignes de style.
- **Filet de sécurité** : une carte avec des numéros d'urgence (15, 112,
  3114) s'affiche systématiquement — sans dépendre du réseau — si des mots
  de détresse aiguë sont détectés dans la transcription.
- **Verrouillage par code (optionnel)** : code à 4 chiffres, hashé
  (SHA-256, Web Crypto), jamais stocké en clair. "Code oublié ?" efface
  toutes les données locales plutôt que de laisser quiconque contourner le
  code sans coût.
- **Prompts du jour** : la question posée à l'enregistrement change chaque
  jour (7 variantes) plutôt que de toujours être la même.
- **Mots-clés récurrents, vue mensuelle, recherche dans l'historique,
  bilan imprimable/PDF** : dans les onglets Tendances/Historique. La réponse
  du compagnon est aussi conservée sur chaque entrée et réaffichée dans
  l'Historique, pour un vrai suivi dans le temps plutôt qu'une réponse
  visible seulement sur l'écran de résumé du jour même.
- **Check-in rapide** : deux curseurs (énergie, stress) sans passer par la
  voix, pour un point ponctuel entre deux journaux complets.
- **Rappel quotidien (optionnel)** : bannière dans l'app (+ notification
  best-effort) si rien n'a encore été journalisé après l'heure choisie —
  honnêtement limité : sans backend d'envoi push, aucun navigateur ne
  garantit un rappel quand l'app est complètement fermée.
- **Lecture audio de la réponse du compagnon** via la synthèse vocale du
  navigateur (fr-FR), sans dépendance ni appel réseau.
- **Petites touches ludiques** : badge de série ("🔥 X jours d'affilée",
  visible seulement si la série est encore vivante) et de paliers ("🏅 X
  journaux"), calendrier visuel des 35 derniers jours dans l'Historique,
  petite animation de clôture après un enregistrement (jamais en cas de
  signal de crise, ni si `prefers-reduced-motion` est activé), et 5
  couleurs d'accent au choix dans Réglages.

## Tester en développement

Les Deploy Previews Netlify liées à une Pull Request disparaissent une fois
celle-ci fermée ou fusionnée. Pour un lien stable pendant le développement
(utile pour tester à plusieurs sans dépendre d'une PR précise), un "branch
deploy" Netlify reste accessible tant que la branche existe :

```
https://claude-voice-mental-health-app-hx0nek--nimble-daffodil-e07745.netlify.app/echo-app/
```

Chaque personne qui ouvre ce lien a ses propres données en local
(`localStorage` du navigateur) : pas de compte, pas de partage de données
entre deux personnes qui testent en même temps.

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
