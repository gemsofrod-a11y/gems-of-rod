// Authentification Firebase (email + mot de passe), chargée via le SDK
// "compat" en CDN (voir index.html) — pas de dépendance npm, cohérent avec
// le reste de l'app qui n'a pas d'étape de build. Reste totalement inerte
// si firebase-config.js n'a pas été rempli avec un vrai projet : le compte
// et la synchronisation sont alors simplement indisponibles, jamais une
// erreur bloquante pour l'utilisateur qui n'utilise que le mode local.
const Auth = (() => {
  let app = null;
  let currentUser = null;
  const listeners = [];

  function isConfigured() {
    return !!app;
  }

  function init() {
    if (app) return;
    if (typeof firebase === "undefined") return;
    if (!FIREBASE_CONFIG || FIREBASE_CONFIG.apiKey === "REMPLACE-MOI") return;
    try {
      app = firebase.initializeApp(FIREBASE_CONFIG);
    } catch (e) {
      app = null;
      return;
    }
    firebase.auth().onAuthStateChanged((user) => {
      currentUser = user;
      listeners.forEach((cb) => cb(user));
    });
  }

  // `cb` est aussi appelé immédiatement avec l'état courant si Firebase est
  // déjà initialisé, pour ne jamais dépendre de l'ordre d'inscription.
  function onAuthChange(cb) {
    listeners.push(cb);
    if (app) cb(currentUser);
  }

  function getUser() {
    return currentUser;
  }

  // Traduit les codes d'erreur Firebase les plus courants en messages
  // compréhensibles ; garde le message brut pour les cas rares plutôt que
  // d'inventer une explication qui pourrait être fausse.
  function friendlyError(error) {
    const map = {
      "auth/email-already-in-use": "Un compte existe déjà avec cet email.",
      "auth/invalid-email": "Adresse email invalide.",
      "auth/weak-password": "Le mot de passe doit faire au moins 6 caractères.",
      "auth/wrong-password": "Mot de passe incorrect.",
      "auth/user-not-found": "Aucun compte avec cet email.",
      "auth/too-many-requests": "Trop de tentatives, réessaie dans quelques minutes.",
      "auth/network-request-failed": "Problème de connexion réseau.",
    };
    return (error && map[error.code]) || (error && error.message) || "Une erreur est survenue.";
  }

  async function signUp(email, password) {
    if (!app) throw new Error("Compte non disponible pour l'instant.");
    try {
      const cred = await firebase.auth().createUserWithEmailAndPassword(email, password);
      return cred.user;
    } catch (e) {
      throw new Error(friendlyError(e));
    }
  }

  async function signIn(email, password) {
    if (!app) throw new Error("Compte non disponible pour l'instant.");
    try {
      const cred = await firebase.auth().signInWithEmailAndPassword(email, password);
      return cred.user;
    } catch (e) {
      throw new Error(friendlyError(e));
    }
  }

  async function signOut() {
    if (!app) return;
    await firebase.auth().signOut();
  }

  return { init, isConfigured, onAuthChange, getUser, signUp, signIn, signOut };
})();
