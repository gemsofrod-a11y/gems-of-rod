// Tour guidé au premier lancement : bulles explicatives mettant en
// surbrillance les zones clés de l'app. Purement local (pas de tracking),
// l'état "déjà vu" est mémorisé dans localStorage.
const Onboarding = (() => {
  const SEEN_KEY = "echo_onboarding_seen_v1";

  const els = {
    overlay: document.getElementById("onboarding-overlay"),
    highlight: document.getElementById("onboarding-highlight"),
    bubble: document.getElementById("onboarding-bubble"),
    title: document.getElementById("onboarding-title"),
    text: document.getElementById("onboarding-text"),
    dots: document.getElementById("onboarding-dots"),
    skip: document.getElementById("onboarding-skip"),
    next: document.getElementById("onboarding-next"),
  };

  const STEPS = [
    {
      title: "Bienvenue sur Écho",
      text: "Un journal vocal quotidien de 30 à 90 secondes. Tout est transcrit et analysé directement sur ton téléphone : gratuit, sans compte, sans clé API.",
    },
    {
      selector: "#btn-record",
      title: "Enregistre ta journée",
      text: "Appuie une fois pour commencer à parler, une seconde fois quand tu as fini. L'app continue d'écouter même si le téléphone marque une courte pause.",
    },
    {
      selector: '.tabbar [data-nav="history"]',
      title: "Historique",
      text: "Retrouve tous tes enregistrements passés et leur transcription ici.",
    },
    {
      selector: '.tabbar [data-nav="trends"]',
      title: "Tendances",
      text: "Après quelques jours, découvre l'évolution de ton énergie et de ton stress, et génère un résumé de la semaine en langage courant.",
    },
    {
      selector: '.tabbar [data-nav="settings"]',
      title: "Réglages",
      text: "Exporte tes données pour les sauvegarder, ou efface tout à tout moment — rien ne quitte jamais ton téléphone.",
    },
  ];

  let stepIndex = 0;

  function positionHighlight(step) {
    if (!step.selector) {
      els.highlight.style.opacity = "0";
      return;
    }
    const target = document.querySelector(step.selector);
    if (!target) {
      els.highlight.style.opacity = "0";
      return;
    }
    const rect = target.getBoundingClientRect();
    const pad = 8;
    els.highlight.style.opacity = "1";
    els.highlight.style.top = `${rect.top - pad}px`;
    els.highlight.style.left = `${rect.left - pad}px`;
    els.highlight.style.width = `${rect.width + pad * 2}px`;
    els.highlight.style.height = `${rect.height + pad * 2}px`;
  }

  function renderStep() {
    const step = STEPS[stepIndex];
    els.title.textContent = step.title;
    els.text.textContent = step.text;
    els.next.textContent = stepIndex === STEPS.length - 1 ? "C'est parti !" : "Suivant";
    els.dots.innerHTML = STEPS.map((_, i) =>
      `<span class="onboarding-dot${i === stepIndex ? " onboarding-dot-active" : ""}"></span>`
    ).join("");
    positionHighlight(step);
  }

  function start() {
    stepIndex = 0;
    els.overlay.hidden = false;
    renderStep();
  }

  function close() {
    els.overlay.hidden = true;
    localStorage.setItem(SEEN_KEY, "1");
  }

  function next() {
    if (stepIndex >= STEPS.length - 1) {
      close();
      return;
    }
    stepIndex++;
    renderStep();
  }

  els.next.addEventListener("click", next);
  els.skip.addEventListener("click", close);
  window.addEventListener("resize", () => {
    if (!els.overlay.hidden) positionHighlight(STEPS[stepIndex]);
  });

  function autostart() {
    if (!localStorage.getItem(SEEN_KEY)) {
      start();
    }
  }

  return { start, autostart };
})();
