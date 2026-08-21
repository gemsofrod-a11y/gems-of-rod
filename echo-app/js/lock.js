// Verrouillage local par code à 4 chiffres, optionnel. Le code n'est jamais
// stocké en clair : seul son hash SHA-256 (Web Crypto, déjà disponible sans
// dépendance) est conservé dans localStorage. Rien n'est envoyé nulle part —
// comme tout verrouillage purement local sans compte, un code oublié
// signifie qu'il faut effacer les données de l'app pour la déverrouiller.
const Lock = (() => {
  const PIN_HASH_KEY = "echo_lock_pin_hash";
  const UNLOCK_SESSION_KEY = "echo_unlocked";
  const PIN_LENGTH = 4;

  let overlay, dotsEl, errorEl, subtitleEl, keypadEl, cancelEl, forgotEl;
  let mode = null; // "unlock" | "set-1" | "set-2"
  let buffer = "";
  let firstPin = "";
  let onSuccessCb = null;
  let onCancelCb = null;

  function els() {
    if (!overlay) {
      overlay = document.getElementById("lock-overlay");
      dotsEl = document.getElementById("lock-dots");
      errorEl = document.getElementById("lock-error");
      subtitleEl = document.getElementById("lock-subtitle");
      keypadEl = document.getElementById("lock-keypad");
      cancelEl = document.getElementById("lock-cancel");
      forgotEl = document.getElementById("lock-forgot");
      keypadEl.addEventListener("click", (e) => {
        const btn = e.target.closest(".lock-key");
        if (!btn || btn.disabled) return;
        handleKey(btn.dataset.key);
      });
      cancelEl.addEventListener("click", () => {
        if (mode === "unlock") return; // pas d'annulation possible en déverrouillage
        const cb = onCancelCb;
        hide();
        if (cb) cb();
      });
      forgotEl.addEventListener("click", () => {
        if (mode !== "unlock") return;
        // Un code oublié ne peut pas être "retrouvé" : le seul recours
        // honnête est une réinitialisation complète des données locales de
        // l'app. Ce coût volontaire empêche aussi que ce lien serve de
        // contournement gratuit du verrou par quelqu'un qui prend le
        // téléphone (il perdrait les données, pas juste le code).
        const ok = confirm(
          "Réinitialiser le code effacera aussi toutes tes données Écho sur cet appareil (journaux, tendances). C'est impossible à annuler. Continuer ?"
        );
        if (!ok) return;
        localStorage.clear();
        sessionStorage.clear();
        if (window.indexedDB && indexedDB.deleteDatabase) {
          indexedDB.deleteDatabase("echo-audio");
        }
        location.reload();
      });
    }
    return overlay;
  }

  async function sha256Hex(text) {
    const data = new TextEncoder().encode(text);
    const digest = await crypto.subtle.digest("SHA-256", data);
    return Array.from(new Uint8Array(digest)).map((b) => b.toString(16).padStart(2, "0")).join("");
  }

  function isEnabled() {
    return !!localStorage.getItem(PIN_HASH_KEY);
  }

  function isUnlockedThisSession() {
    return sessionStorage.getItem(UNLOCK_SESSION_KEY) === "1";
  }

  function needsUnlock() {
    return isEnabled() && !isUnlockedThisSession();
  }

  function updateDots() {
    const dots = dotsEl.querySelectorAll(".lock-dot");
    dots.forEach((d, i) => d.classList.toggle("lock-dot-filled", i < buffer.length));
  }

  function render() {
    errorEl.hidden = true;
    cancelEl.hidden = mode === "unlock";
    forgotEl.hidden = mode !== "unlock";
    if (mode === "unlock") subtitleEl.textContent = "Entre ton code";
    if (mode === "set-1") subtitleEl.textContent = "Choisis un code à 4 chiffres";
    if (mode === "set-2") subtitleEl.textContent = "Confirme le code";
    updateDots();
  }

  async function handleKey(key) {
    if (key === "del") {
      buffer = buffer.slice(0, -1);
      updateDots();
      return;
    }
    if (buffer.length >= PIN_LENGTH) return;
    buffer += key;
    updateDots();
    if (buffer.length !== PIN_LENGTH) return;

    if (mode === "unlock") {
      const hash = await sha256Hex(buffer);
      if (hash === localStorage.getItem(PIN_HASH_KEY)) {
        sessionStorage.setItem(UNLOCK_SESSION_KEY, "1");
        const cb = onSuccessCb;
        hide();
        if (cb) cb();
      } else {
        errorEl.textContent = "Code incorrect";
        errorEl.hidden = false;
        buffer = "";
        updateDots();
      }
      return;
    }

    if (mode === "set-1") {
      firstPin = buffer;
      buffer = "";
      mode = "set-2";
      render();
      return;
    }

    if (mode === "set-2") {
      if (buffer !== firstPin) {
        errorEl.textContent = "Les codes ne correspondent pas, recommence";
        errorEl.hidden = false;
        buffer = "";
        firstPin = "";
        mode = "set-1";
        render();
        return;
      }
      const hash = await sha256Hex(buffer);
      localStorage.setItem(PIN_HASH_KEY, hash);
      sessionStorage.setItem(UNLOCK_SESSION_KEY, "1");
      const cb = onSuccessCb;
      hide();
      if (cb) cb();
    }
  }

  function show() {
    els().hidden = false;
  }
  function hide() {
    els().hidden = true;
    buffer = "";
    firstPin = "";
  }

  function showUnlock(onSuccess) {
    els();
    mode = "unlock";
    buffer = "";
    onSuccessCb = onSuccess;
    onCancelCb = null;
    render();
    show();
  }

  function showSetup(onDone, onCancel) {
    els();
    mode = "set-1";
    buffer = "";
    firstPin = "";
    onSuccessCb = onDone;
    onCancelCb = onCancel;
    render();
    show();
  }

  function disable() {
    localStorage.removeItem(PIN_HASH_KEY);
    sessionStorage.removeItem(UNLOCK_SESSION_KEY);
  }

  return { isEnabled, needsUnlock, showUnlock, showSetup, disable };
})();
