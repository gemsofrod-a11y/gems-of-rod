// Synchronisation cloud (Firestore), réservée aux comptes abonnés. Le
// stockage local (localStorage) reste la source de vérité synchrone pour
// toute l'UI de l'app — rien dans app.js n'attend une réponse réseau pour
// afficher quoi que ce soit. Firestore n'est qu'un miroir en arrière-plan :
// chaque écriture locale est aussi poussée vers le cloud (best-effort, ne
// bloque jamais l'action), et à la connexion on récupère les entrées du
// cloud pour les fusionner dans le stockage local (union par id — jamais de
// perte, jamais de doublon puisque les id sont déjà uniques).
//
// Le statut d'abonnement est un champ `isSubscribed` sur le document
// users/{uid} — aujourd'hui réglable uniquement depuis Réglages (voir
// app.js), en attendant une vraie intégration de facturation (Play Billing
// ou Stripe). Aucune vraie transaction n'a lieu ici.
const CloudSync = (() => {
  let db = null;
  let uid = null;
  let isSubscribed = false;
  const statusListeners = [];

  function notifyStatus() {
    statusListeners.forEach((cb) => cb(getStatus()));
  }

  function getStatus() {
    return { signedIn: !!uid, isSubscribed, active: isSyncActive() };
  }

  function onStatusChange(cb) {
    statusListeners.push(cb);
    cb(getStatus());
  }

  function init() {
    if (!Auth.isConfigured()) return;
    db = firebase.firestore();
    Auth.onAuthChange(async (user) => {
      uid = user ? user.uid : null;
      isSubscribed = false;
      if (uid) await loadSubscriptionStatus();
      notifyStatus();
    });
  }

  async function loadSubscriptionStatus() {
    try {
      const doc = await db.collection("users").doc(uid).get();
      isSubscribed = !!(doc.exists && doc.data().isSubscribed);
    } catch (e) {
      isSubscribed = false;
    }
  }

  // Bascule de test tant qu'il n'y a pas de vraie facturation — voir le
  // commentaire en tête de fichier.
  async function setTestSubscription(value) {
    if (!uid) return;
    await db.collection("users").doc(uid).set({ isSubscribed: !!value }, { merge: true });
    isSubscribed = !!value;
    notifyStatus();
  }

  function isSyncActive() {
    return !!(uid && isSubscribed);
  }

  function entriesRef() {
    return db.collection("users").doc(uid).collection("entries");
  }

  // Best-effort : une entrée qui ne part pas vers le cloud (hors-ligne,
  // erreur réseau...) reste de toute façon sauvegardée localement, donc on
  // avale silencieusement l'échec plutôt que de perturber l'utilisateur.
  function pushEntry(entry) {
    if (!isSyncActive() || !entry) return;
    entriesRef().doc(entry.id).set(entry).catch(() => {});
  }

  function deleteAllRemote() {
    if (!isSyncActive()) return;
    entriesRef()
      .get()
      .then((snap) => {
        const batch = db.batch();
        snap.forEach((doc) => batch.delete(doc.ref));
        return batch.commit();
      })
      .catch(() => {});
  }

  // Récupère les entrées cloud absentes en local et les y ajoute — appelé
  // à la connexion pour rapatrier ce qui a été journalisé sur un autre
  // appareil.
  async function pullAndMerge() {
    if (!isSyncActive()) return { pulled: 0 };
    const snap = await entriesRef().get();
    const remoteEntries = snap.docs.map((d) => d.data());
    const localIds = new Set(Storage.getEntries().map((e) => e.id));
    const toAdd = remoteEntries.filter((e) => !localIds.has(e.id));
    toAdd.forEach((e) => Storage.saveEntry(e));
    return { pulled: toAdd.length };
  }

  // Envoie toutes les entrées locales vers le cloud — utilisé pour l'import
  // initial, la première fois qu'un compte abonné est actif sur cet
  // appareil.
  async function pushAllLocal() {
    if (!isSyncActive()) return { pushed: 0 };
    const entries = Storage.getEntries();
    if (!entries.length) return { pushed: 0 };
    const batch = db.batch();
    entries.forEach((e) => batch.set(entriesRef().doc(e.id), e));
    await batch.commit();
    return { pushed: entries.length };
  }

  return {
    init,
    onStatusChange,
    getStatus,
    setTestSubscription,
    isSyncActive,
    pushEntry,
    deleteAllRemote,
    pullAndMerge,
    pushAllLocal,
  };
})();
