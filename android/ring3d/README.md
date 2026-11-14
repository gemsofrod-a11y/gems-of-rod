# Moteur 3D du compositeur de bague

Rendu 3D réaliste de la bague composée dans l'app (écran « Compositeur de
bague » et fiche « Simuler la bague »), demandé par Sébastien le 14/11/2026.

- Sources : `src/engine.js` (modèle 3D : jonc, tête, pierres, sertissages,
  studio de lumière) et `src/main.js` (visionneuse interactive et fiche
  technique 4 vues cotées).
- Moteur : three.js (licence MIT), embarqué — aucun accès réseau.
- Fichier livré dans l'app : `app/src/androidMain/assets/ring3d/ring3d.js`
  (page `viewer.html` à côté), chargé par `Ring3DView`
  (`ui/components/Ring3D.kt`) dans une WebView.

Après toute modification des sources, reconstruire le fichier livré :

```bash
cd android/ring3d
npm install
npm run build
```

et committer `app/src/androidMain/assets/ring3d/ring3d.js` avec les sources.
Les contours des tailles de pierre doivent rester identiques à ceux de
`RingStoneGeometry.kt`.
