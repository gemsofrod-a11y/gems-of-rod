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
  de détresse aiguë sont détectés dans la transcription. Quand le compagnon
  IA est actif, il sert de second filet : s'il perçoit une détresse que la
  liste locale n'a pas captée (formulation indirecte), il le signale et la
  même carte s'affiche.
- **Émotions précises** : après chaque journal vocal (et dans le check-in
  rapide), choix facultatif d'une ou deux émotions parmi 12 (joie, fierté,
  gratitude, sérénité, soulagement, motivation ; anxiété, tristesse,
  colère, frustration, solitude, découragement — voir `js/emotions.js`).
  Toujours choisies par l'utilisateur, jamais déduites. Affichées dans
  l'Historique (et trouvables par la recherche), fréquences sur 30 jours
  dans Tendances et dans le bilan imprimable, transmises au bilan de la
  semaine du compagnon. Le choix n'est pas proposé en cas de signal de
  crise, pour laisser toute la place à la carte d'aide.
- **Gratitude** : après un journal vocal, champ facultatif « Une chose qui
  s'est bien passée aujourd'hui ? » (200 caractères max, gardé sur l'entrée
  du jour, vider le champ la retire). Carnet de gratitude repliable en haut
  de l'Historique, avec « Un souvenir au hasard » pour les jours plus
  difficiles. Trouvable par la recherche, repris dans le bilan imprimable
  (30 derniers jours) et dans le bilan de la semaine du compagnon. Pas
  proposé en cas de signal de crise.
- **Bilan de la semaine par le compagnon** : dans Tendances, le bouton
  « Générer mon résumé de la semaine » affiche le résumé local puis, si le
  compagnon est configuré, un bilan qui relie les journaux des 7 derniers
  jours (sujets qui reviennent, évolution, une piste pour la semaine
  suivante). Mis en cache tant que les journaux de la semaine ne changent
  pas, pour ne pas refaire d'appel payant à chaque clic. Le même signal de
  détresse s'y applique : la carte d'urgence s'affiche dans le bilan.
- **Import de sauvegarde** : fusionne avec les journaux déjà présents
  (par id) au lieu de les remplacer — importer une ancienne sauvegarde ne
  fait jamais perdre les entrées plus récentes.
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
  voix, pour un point ponctuel entre deux journaux complets, avec en option
  les heures de sommeil de la nuit passée (de « 4 h ou moins » à « 10 h ou
  plus », aucune valeur enregistrée si rien n'est choisi) et une ou deux
  émotions. Dans Tendances, une carte « Ton sommeil » donne la moyenne sur
  30 jours et, dès 3 nuits de 7 h ou plus et 3 plus courtes, compare
  l'énergie et le stress notés selon la durée de la nuit (seulement si
  l'écart est net, au moins 10 points).
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
- **Compte & synchronisation cloud (optionnel, réservé aux abonné·e·s)** :
  authentification Firebase (email + mot de passe, SDK "compat" chargé en
  CDN — voir `js/firebase-config.js`, `js/auth.js`, `js/cloudsync.js`).
  Le `localStorage` reste la source de vérité synchrone pour toute l'UI ;
  Firestore n'est qu'un miroir en arrière-plan (chaque écriture locale est
  aussi poussée vers le cloud, best-effort, et à la connexion les entrées
  cloud absentes en local sont rapatriées — union par id, jamais de perte
  ni de doublon). Reste totalement inerte tant que `FIREBASE_CONFIG.apiKey`
  vaut `"REMPLACE-MOI"` : sans projet Firebase configuré, le compte est
  simplement indisponible et l'app continue de fonctionner normalement en
  local. **Le statut d'abonnement est un interrupteur de test** dans
  Réglages (`isSubscribed` sur `users/{uid}`, modifiable directement par le
  client) — aucune vraie facturation n'est branchée ; à remplacer par une
  vérification serveur (Play Billing / Stripe) avant toute mise en
  production réelle. Règles de sécurité Firestore recommandées :
  ```
  rules_version = '2';
  service cloud.firestore {
    match /databases/{database}/documents {
      match /users/{userId} {
        allow read, write: if request.auth != null && request.auth.uid == userId;
        match /entries/{entryId} {
          allow read, write: if request.auth != null && request.auth.uid == userId;
        }
      }
    }
  }
  ```

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
- **Vitesse de transcription** : elle dépend entièrement du moteur de
  reconnaissance du téléphone (service cloud de Chrome/Google Play
  Services) — l'app ne peut pas la rendre plus rapide directement, ce
  n'est pas un paramètre exposé par cette API. Une connexion instable ou
  Chrome/Play Services pas à jour peuvent nettement ralentir le ressenti.
  Ce qui est sous notre contrôle et a été optimisé : à chaque redémarrage
  automatique du moteur (il s'arrête tout seul après une pause, même en
  mode continu), `js/recorder.js` recrée une instance fraîche de
  `SpeechRecognition` plutôt que de rappeler `.start()` sur la même — ça
  réduit la fenêtre pendant laquelle la parole n'est pas captée à ce
  moment-là.
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
