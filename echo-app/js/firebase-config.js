// Configuration publique du projet Firebase — ces valeurs sont conçues pour
// être exposées côté client (la sécurité vient des règles Firestore, pas du
// secret de ces champs). Remplace les valeurs ci-dessous par celles de ton
// projet : console Firebase > icône ⚙️ Paramètres du projet > Général >
// section "Vos applications" > app web > "Config".
//
// Tant que apiKey vaut "REMPLACE-MOI", Auth.init() ne fait rien : l'app
// continue de fonctionner normalement en local uniquement (compte et
// synchronisation simplement indisponibles, sans erreur pour l'utilisateur).
// `window.FIREBASE_CONFIG ||` : si déjà défini (utilisé par les tests pour
// injecter une config simulée avant le chargement de ce fichier), on ne
// l'écrase pas. En production, rien ne le prédéfinit, donc le
// comportement est identique à une simple constante.
window.FIREBASE_CONFIG = window.FIREBASE_CONFIG || {
  apiKey: "REMPLACE-MOI",
  authDomain: "REMPLACE-MOI.firebaseapp.com",
  projectId: "REMPLACE-MOI",
  storageBucket: "REMPLACE-MOI.appspot.com",
  messagingSenderId: "REMPLACE-MOI",
  appId: "REMPLACE-MOI",
};
