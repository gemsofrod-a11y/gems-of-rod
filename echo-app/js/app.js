(() => {
  const MAX_RECORDING_MS = 3 * 60 * 1000;

  // Une question différente chaque jour plutôt que toujours la même, pour
  // éviter la lassitude et creuser des angles différents. Choisie par date
  // (pas au hasard à chaque ouverture) pour rester stable toute la journée.
  const RECORD_PROMPTS = [
    "Parle librement pendant environ 30 secondes. Comment s'est passée ta journée ?",
    "Qu'est-ce qui t'a pris le plus d'énergie aujourd'hui ?",
    "Y a-t-il quelque chose dont tu es reconnaissant·e aujourd'hui, même petit ?",
    "Qu'est-ce qui t'a stressé ou pesé aujourd'hui ?",
    "Quelle a été ta petite victoire du jour ?",
    "Comment tu te sens, là, maintenant, en quelques mots ?",
    "Qu'est-ce que tu aimerais faire différemment demain ?",
  ];

  function todaysPrompt() {
    const dayIndex = Math.floor(Date.now() / 86400000);
    return RECORD_PROMPTS[dayIndex % RECORD_PROMPTS.length];
  }

  // Couleur d'accent personnalisable, purement cosmétique (--primary /
  // --primary-2 uniquement) : n'affecte jamais --energy/--stress, qui
  // restent les couleurs fixes des scores dans les graphiques.
  const ACCENT_KEY = "echo_accent_color";
  const ACCENT_PALETTE = {
    violet: { primary: "#6f8cff", primary2: "#8f6fff" },
    bleu: { primary: "#3d9cf2", primary2: "#5fb8ff" },
    vert: { primary: "#34b378", primary2: "#5ecf95" },
    ambre: { primary: "#e8a33d", primary2: "#f0bc63" },
    rose: { primary: "#e0609f", primary2: "#ec85b8" },
  };

  function applyAccent(name) {
    const accent = ACCENT_PALETTE[name];
    if (!accent) return;
    document.documentElement.style.setProperty("--primary", accent.primary);
    document.documentElement.style.setProperty("--primary-2", accent.primary2);
    els.accentSwatches.querySelectorAll(".accent-swatch").forEach((btn) => {
      btn.classList.toggle("accent-swatch-active", btn.dataset.accent === name);
    });
  }

  // Rappel quotidien. Honnête sur sa limite : sans backend d'envoi push, un
  // navigateur ne peut pas notifier de façon fiable une fois l'app
  // complètement fermée. Ce qu'on peut garantir, c'est une bannière dans
  // l'app + une notification best-effort au moment où l'app est rouverte
  // après l'heure choisie, tant qu'aucune entrée n'a encore été faite ce
  // jour-là.
  const REMINDER_ENABLED_KEY = "echo_reminder_enabled";
  const REMINDER_TIME_KEY = "echo_reminder_time";

  function isReminderEnabled() {
    return localStorage.getItem(REMINDER_ENABLED_KEY) === "1";
  }
  function getReminderTime() {
    return localStorage.getItem(REMINDER_TIME_KEY) || "19:00";
  }
  function todayKey() {
    return new Date().toDateString();
  }
  function hasJournaledToday() {
    return Storage.getEntries().some((e) => new Date(e.date).toDateString() === todayKey());
  }
  function reminderDismissKey() {
    return `echo_reminder_dismissed_${todayKey()}`;
  }
  function shouldShowReminder() {
    if (!isReminderEnabled() || hasJournaledToday()) return false;
    if (localStorage.getItem(reminderDismissKey()) === "1") return false;
    const [h, m] = getReminderTime().split(":").map(Number);
    const now = new Date();
    return now.getHours() * 60 + now.getMinutes() >= h * 60 + m;
  }
  // Petits badges ludiques, purement décoratifs : une série en cours (si
  // >= 2 jours) et le plus grand palier de journaux totaux atteint. Aucun
  // impact sur l'analyse — juste un repère satisfaisant à l'ouverture.
  const STREAK_MILESTONES = [7, 14, 30, 60, 100, 200, 365];

  function renderStreakBadges() {
    const entries = Storage.getEntries();
    const streak = Analysis.currentStreak(entries);
    const milestone = [...STREAK_MILESTONES].reverse().find((m) => entries.length >= m);

    const badges = [];
    if (streak >= 2) badges.push(`🔥 ${streak} jours d'affilée`);
    if (milestone) badges.push(`🏅 ${milestone} journaux`);

    if (!badges.length) {
      els.streakBadges.hidden = true;
      return;
    }
    els.streakBadges.innerHTML = badges.map((b) => `<span class="streak-badge">${escapeHtml(b)}</span>`).join("");
    els.streakBadges.hidden = false;
  }

  function checkReminder() {
    els.reminderBanner.hidden = !shouldShowReminder();
    if (els.reminderBanner.hidden) return;
    const notifiedKey = `echo_reminder_notified_${todayKey()}`;
    if ("Notification" in window && Notification.permission === "granted" && !sessionStorage.getItem(notifiedKey)) {
      try {
        new Notification("Écho", { body: "Tu n'as pas encore journalisé aujourd'hui." });
        sessionStorage.setItem(notifiedKey, "1");
      } catch (e) {
        // Best-effort uniquement : la bannière dans l'app reste le rappel fiable.
      }
    }
  }

  // Désactivée en dur (pas seulement par défaut) : demander l'accès au
  // micro via getUserMedia PENDANT que la reconnaissance vocale tourne
  // encore casse la transcription sur au moins un appareil Android réel
  // (plus aucun résultat, sans erreur) — confirmé à deux reprises, une
  // fois avec le compteur en direct (AudioContext) et une fois sans (juste
  // AudioCapture/MediaRecorder). Ce n'est donc pas un problème de timing
  // ni du compteur en direct spécifiquement : toute capture audio brute
  // simultanée à la reconnaissance semble incompatible avec ce moteur. Tant
  // qu'une approche fiable n'est pas trouvée (ex. capture non simultanée),
  // la fonction reste coupée pour ne jamais risquer la reconnaissance
  // vocale, qui est le cœur de l'app.
  const AUDIO_TRACK_SETTING_KEY = "echo_audio_track_enabled";
  localStorage.removeItem(AUDIO_TRACK_SETTING_KEY);

  function isAudioTrackEnabled() {
    return false;
  }

  const els = {
    views: document.querySelectorAll(".view"),
    tabs: document.querySelectorAll(".tab-btn"),
    navButtons: document.querySelectorAll("[data-nav]"),
    recordHint: document.getElementById("record-hint"),
    streakBadges: document.getElementById("streak-badges"),
    btnRecord: document.getElementById("btn-record"),
    timer: document.getElementById("timer"),
    recStatus: document.getElementById("rec-status"),
    liveTranscript: document.getElementById("live-transcript"),
    liveMeter: document.getElementById("live-meter"),
    liveMeterCanvas: document.getElementById("live-meter-canvas"),
    unsupported: document.getElementById("unsupported"),
    summaryContent: document.getElementById("summary-content"),
    summaryConfetti: document.getElementById("summary-confetti"),
    crisisCard: document.getElementById("crisis-card"),
    companionCard: document.getElementById("companion-card"),
    companionText: document.getElementById("companion-text"),
    btnCompanionSpeak: document.getElementById("btn-companion-speak"),
    waveformCard: document.getElementById("waveform-card"),
    chartWaveform: document.getElementById("chart-waveform"),
    waveformStats: document.getElementById("waveform-stats"),
    historyHeatmap: document.getElementById("history-heatmap"),
    historySearch: document.getElementById("history-search"),
    historyList: document.getElementById("history-list"),
    insights: document.getElementById("insights"),
    keywordsCard: document.getElementById("keywords-card"),
    keywordsList: document.getElementById("keywords-list"),
    chartTimeline: document.getElementById("chart-timeline"),
    chartWeekday: document.getElementById("chart-weekday"),
    monthlySection: document.getElementById("monthly-section"),
    chartMonthly: document.getElementById("chart-monthly"),
    bestMonthNote: document.getElementById("best-month-note"),
    btnPrintReport: document.getElementById("btn-print-report"),
    printReport: document.getElementById("print-report"),
    btnWeeklySummary: document.getElementById("btn-weekly-summary"),
    weeklySummary: document.getElementById("weekly-summary"),
    btnScaleInfo: document.getElementById("btn-scale-info"),
    scaleInfo: document.getElementById("scale-info"),
    btnExport: document.getElementById("btn-export"),
    inputImport: document.getElementById("input-import"),
    btnClear: document.getElementById("btn-clear"),
    btnHelp: document.getElementById("btn-help"),
    audioTrackToggle: document.getElementById("audio-track-toggle"),
    accentSwatches: document.getElementById("accent-swatches"),
    btnLockToggle: document.getElementById("btn-lock-toggle"),
    lockSettingDesc: document.getElementById("lock-setting-desc"),
    btnCheckin: document.getElementById("btn-checkin"),
    checkinOverlay: document.getElementById("checkin-overlay"),
    checkinEnergy: document.getElementById("checkin-energy"),
    checkinEnergyValue: document.getElementById("checkin-energy-value"),
    checkinStress: document.getElementById("checkin-stress"),
    checkinStressValue: document.getElementById("checkin-stress-value"),
    checkinCancel: document.getElementById("checkin-cancel"),
    checkinSave: document.getElementById("checkin-save"),
    reminderBanner: document.getElementById("reminder-banner"),
    reminderBannerDismiss: document.getElementById("reminder-banner-dismiss"),
    reminderToggle: document.getElementById("reminder-toggle"),
    reminderTimeRow: document.getElementById("reminder-time-row"),
    reminderTime: document.getElementById("reminder-time"),
  };

  let isRecording = false;
  let startTime = 0;
  let timerInterval = null;
  let lastEntry = null;
  let sessionFinalized = false;
  let audioTrackStarted = false;
  let meterAudioCtx = null;
  let meterAnalyser = null;
  let meterRafId = null;

  function navigate(view) {
    els.views.forEach((v) => v.classList.toggle("view-active", v.id === `view-${view}`));
    els.tabs.forEach((t) => t.classList.toggle("tab-active", t.dataset.nav === view));
    if (view === "history") renderHistory();
    if (view === "trends") renderTrends();
  }

  els.navButtons.forEach((btn) => {
    btn.addEventListener("click", () => navigate(btn.dataset.nav));
  });

  els.historySearch.addEventListener("input", () => renderHistory());

  function formatTimer(ms) {
    const totalSec = Math.floor(ms / 1000);
    const m = String(Math.floor(totalSec / 60)).padStart(2, "0");
    const s = String(totalSec % 60).padStart(2, "0");
    return `${m}:${s}`;
  }

  function startRecording() {
    if (!Recorder.isSupported()) {
      els.unsupported.hidden = false;
      return;
    }
    isRecording = true;
    sessionFinalized = false;
    startTime = Date.now();
    els.btnRecord.classList.add("recording");
    els.btnRecord.setAttribute("aria-label", "Arrêter l'enregistrement");
    els.recStatus.textContent = "Je t'écoute...";
    els.liveTranscript.hidden = false;
    els.liveTranscript.textContent = "";
    audioTrackStarted = false;

    timerInterval = setInterval(() => {
      const elapsedMs = Date.now() - startTime;
      els.timer.textContent = formatTimer(elapsedMs);
      // Filet de sécurité : le moteur de reconnaissance redémarre tout
      // seul en continu tant qu'on ne clique pas sur "Arrêter" (voir
      // recorder.js). On coupe automatiquement après quelques minutes
      // pour éviter une écoute qui tournerait indéfiniment en arrière-plan.
      if (elapsedMs >= MAX_RECORDING_MS) {
        els.recStatus.textContent = "Durée maximale atteinte, enregistrement arrêté.";
        stopRecording();
      }
    }, 250);

    Recorder.start({
      onInterim: (text) => {
        els.liveTranscript.textContent = text || "...";
        // On ne démarre la capture audio (getUserMedia) qu'une fois la
        // reconnaissance vocale confirmée active (premier résultat reçu),
        // jamais au même instant que son propre démarrage — c'est cette
        // simultanéité qui semble avoir perturbé la reconnaissance sur au
        // moins un appareil réel. On perd le tout début de la piste audio,
        // c'est un compromis délibéré pour ne jamais risquer la
        // transcription, qui reste la fonctionnalité principale de l'app.
        //
        // Le compteur de niveau en direct (startLiveMeter) N'EST PAS démarré
        // ici volontairement : sur un appareil réel, l'activer en plus de
        // AudioCapture a fait se figer la reconnaissance vocale en cours
        // d'enregistrement (aucune erreur, juste plus aucun résultat), même
        // avec ce délai. Un AudioContext + AnalyserNode branché en direct
        // sur le micro semble entrer en conflit avec le moteur de
        // reconnaissance de façon plus agressive qu'un simple MediaRecorder.
        // Le compteur live reste donc désactivé tant que ça n'est pas
        // confirmé sûr ; seule l'analyse différée (après coup, sur le blob
        // audio déjà enregistré) reste active pour les pauses/pics/Hz.
        if (isAudioTrackEnabled() && !audioTrackStarted) {
          audioTrackStarted = true;
          AudioCapture.start();
        }
      },
      onError: (err) => {
        els.recStatus.textContent = "Erreur : " + err.message;
        stopRecording();
      },
      onEnd: () => {
        // Cet événement est asynchrone : par exemple, il peut arriver après
        // qu'un clic manuel sur "Arrêter" ait déjà mis isRecording à false.
        // On finalise donc systématiquement ici (une seule fois par session),
        // plutôt que de dépendre de l'état isRecording au moment du clic.
        if (!sessionFinalized) finalizeRecording();
      },
    });
  }

  function resetRecordingUI() {
    isRecording = false;
    clearInterval(timerInterval);
    els.btnRecord.classList.remove("recording");
    els.btnRecord.setAttribute("aria-label", "Démarrer l'enregistrement");
    stopLiveMeter();
  }

  // Indicateur de niveau sonore en direct pendant l'enregistrement (comme un
  // dictaphone). Réutilise le flux déjà ouvert par AudioCapture plutôt que
  // de redemander le micro, pour ne jamais risquer de perturber à nouveau la
  // reconnaissance vocale.
  function startLiveMeter(stream) {
    if (!stream || !(window.AudioContext || window.webkitAudioContext)) return;
    try {
      const AudioContextCtor = window.AudioContext || window.webkitAudioContext;
      meterAudioCtx = new AudioContextCtor();
      const source = meterAudioCtx.createMediaStreamSource(stream);
      meterAnalyser = meterAudioCtx.createAnalyser();
      meterAnalyser.fftSize = 256;
      source.connect(meterAnalyser);
      els.liveMeter.hidden = false;
      drawLiveMeter();
    } catch (e) {
      // Purement visuel : une erreur ici ne doit jamais gêner l'enregistrement.
    }
  }

  function drawLiveMeter() {
    if (!meterAnalyser) return;
    const canvas = els.liveMeterCanvas;
    const ctx = canvas.getContext("2d");
    const width = canvas.width;
    const height = canvas.height;
    const data = new Uint8Array(meterAnalyser.fftSize);
    meterAnalyser.getByteTimeDomainData(data);

    let sumSq = 0;
    for (let i = 0; i < data.length; i++) {
      const v = (data[i] - 128) / 128;
      sumSq += v * v;
    }
    const level = Math.min(1, Math.sqrt(sumSq / data.length) * 3.5);

    ctx.clearRect(0, 0, width, height);
    ctx.fillStyle = "rgba(111, 140, 255, 0.15)";
    ctx.fillRect(0, 0, width, height);
    const barWidth = Math.max(4, level * width);
    const gradient = ctx.createLinearGradient(0, 0, barWidth, 0);
    gradient.addColorStop(0, "#6f8cff");
    gradient.addColorStop(1, "#ff7a7a");
    ctx.fillStyle = gradient;
    ctx.fillRect(0, height * 0.25, barWidth, height * 0.5);

    meterRafId = requestAnimationFrame(drawLiveMeter);
  }

  function stopLiveMeter() {
    if (meterRafId) cancelAnimationFrame(meterRafId);
    meterRafId = null;
    meterAnalyser = null;
    if (meterAudioCtx) {
      meterAudioCtx.close().catch(() => {});
      meterAudioCtx = null;
    }
    if (els.liveMeter) els.liveMeter.hidden = true;
    if (els.liveMeterCanvas) {
      const ctx = els.liveMeterCanvas.getContext("2d");
      ctx.clearRect(0, 0, els.liveMeterCanvas.width, els.liveMeterCanvas.height);
    }
  }

  function stopRecording() {
    resetRecordingUI();
    Recorder.stop();
  }

  async function finalizeRecording() {
    if (sessionFinalized) return;
    sessionFinalized = true;
    resetRecordingUI();

    const durationSec = Math.max(1, Math.round((Date.now() - startTime) / 1000));
    const transcript = Recorder.getTranscript();
    els.recStatus.textContent = "";
    els.timer.textContent = "00:00";

    // Toujours libérer le micro utilisé pour la piste audio, même si la
    // transcription est vide (l'utilisateur va probablement réessayer).
    const audioBlob = await AudioCapture.stop().catch(() => null);

    if (!transcript) {
      els.recStatus.textContent = "Aucune parole détectée, réessaie.";
      return;
    }

    // Filet de sécurité : quoi qu'il arrive à partir d'ici (bug d'analyse,
    // de rendu...), on doit soit afficher le résumé, soit prévenir
    // clairement — jamais rester bloqué sans rien afficher sur l'écran
    // d'enregistrement comme si rien ne s'était passé.
    try {
      const wordCount = transcript.split(/\s+/).filter(Boolean).length;
      const scores = Analysis.computeScores(transcript, durationSec, wordCount);

      const entry = {
        id: `${Date.now()}`,
        date: new Date().toISOString(),
        durationSec,
        transcript,
        wordCount,
        scores,
      };

      Storage.saveEntry(entry);
      lastEntry = entry;
      checkReminder();
      renderStreakBadges();
      const sessionId = renderSummary(entry);
      navigate("summary");

      if (audioBlob) processAudioProsody(entry, audioBlob, sessionId);
    } catch (err) {
      console.error("Écho: échec de la finalisation de l'enregistrement", err);
      els.recStatus.textContent = "Une erreur est survenue en traitant ton enregistrement. Réessaie.";
    }
  }

  els.btnRecord.addEventListener("click", () => {
    if (isRecording) stopRecording();
    else startRecording();
  });

  function scoreRow(label, value, color) {
    return `
      <div class="score-row">
        <div class="score-label">${label}</div>
        <div class="score-bar"><div class="score-bar-fill" style="width:${value}%; background:${color}"></div></div>
        <div class="score-value">${value}</div>
      </div>`;
  }

  const CONFETTI_COLORS = ["var(--primary)", "var(--primary-2)", "var(--energy)", "#ffd166"];

  function playConfetti() {
    const container = els.summaryConfetti;
    if (!container) return;
    container.innerHTML = "";
    const pieces = 10;
    for (let i = 0; i < pieces; i++) {
      const span = document.createElement("span");
      span.className = "confetti-piece";
      const angle = (360 / pieces) * i + (Math.random() * 20 - 10);
      const distance = 60 + Math.random() * 40;
      const dx = Math.cos((angle * Math.PI) / 180) * distance;
      const dy = Math.sin((angle * Math.PI) / 180) * distance;
      span.style.setProperty("--dx", `${dx}px`);
      span.style.setProperty("--dy", `${dy}px`);
      span.style.background = CONFETTI_COLORS[i % CONFETTI_COLORS.length];
      span.style.animationDelay = `${Math.random() * 80}ms`;
      container.appendChild(span);
    }
    // Nettoie après l'animation pour ne pas laisser de nœuds inertes.
    setTimeout(() => {
      container.innerHTML = "";
    }, 900);
  }

  let summarySessionId = 0;

  function renderSummary(entry) {
    const sessionId = ++summarySessionId;

    const isCrisis = Analysis.detectCrisisSignal(entry.transcript);
    els.crisisCard.hidden = !isCrisis;
    // Petit effet de clôture satisfaisant — jamais en cas de signal de
    // crise, où ce serait déplacé, et jamais si l'utilisateur préfère
    // réduire les animations.
    if (!isCrisis && !window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
      playConfetti();
    }

    els.companionCard.hidden = true;
    els.companionText.innerHTML = "";
    els.btnCompanionSpeak.hidden = true;
    if ("speechSynthesis" in window) speechSynthesis.cancel();
    // En cas de signal de crise, on s'en tient au message local fiable
    // ci-dessus plutôt que d'ajouter une réponse IA moins prévisible.
    if (!isCrisis) requestCompanionResponse(entry, sessionId);

    els.waveformCard.hidden = true;

    const s = entry.scores;
    let html = "";
    html += scoreRow("Énergie", s.energy, "var(--energy)");
    html += scoreRow("Stress", s.stress, "var(--stress)");
    html += scoreRow("Fatigue", s.fatigue, "var(--primary-2)");
    html += scoreRow("Humeur", s.mood, "var(--primary)");

    if (!s.hasSignal) {
      html += `<p class="transcript-quote">Peu de signaux détectés aujourd'hui — les scores restent proches de la neutralité, c'est normal.</p>`;
    }

    // En cas de signal de crise, la carte d'alerte ci-dessus suffit : un
    // conseil générique ("continue comme ça"...) juste en dessous serait
    // déplacé, donc on n'affiche pas le bloc de suggestions habituel.
    const suggestions = isCrisis ? [] : Analysis.getSuggestions(s);
    if (suggestions.length) {
      html += `<div class="suggestions-block">`;
      html += `<div class="suggestions-title">Ce que tu peux faire maintenant</div>`;
      html += `<div class="suggestions">${suggestions
        .map((sg) => {
          const isCareNote = sg.includes("professionnel de santé");
          return `<div class="suggestion-card${isCareNote ? " suggestion-card-care" : ""}">${escapeHtml(sg)}</div>`;
        })
        .join("")}</div>`;
      html += `</div>`;
    }

    if (s.keywords.length) {
      html += `<div class="tags">${s.keywords
        .slice(0, 6)
        .map((k) => `<span class="tag">${escapeHtml(k)}</span>`)
        .join("")}</div>`;
    }

    html += `<p class="transcript-quote">"${escapeHtml(truncate(entry.transcript, 220))}"</p>`;
    els.summaryContent.innerHTML = html;

    return sessionId;
  }

  // Court résumé des entrées récentes (hors l'entrée en cours), pour que le
  // compagnon puisse s'y référer explicitement ("tu disais hier que...")
  // plutôt que de traiter chaque journal comme isolé. Reste court : le
  // serveur tronque de toute façon à 500 caractères.
  function buildRecentSummary(currentEntry) {
    const recent = Storage.getEntries()
      .filter((e) => e.id !== currentEntry.id)
      .sort((a, b) => new Date(b.date) - new Date(a.date))
      .slice(0, 4);
    if (!recent.length) return "";
    return recent
      .map((e) => {
        const dayLabel = new Date(e.date).toLocaleDateString("fr-FR", { weekday: "long", day: "numeric", month: "short" });
        const s = e.scores;
        return `${dayLabel} : énergie ${s.energy}, stress ${s.stress}, humeur ${s.mood}`;
      })
      .join(" / ");
  }

  async function requestCompanionResponse(entry, sessionId) {
    const message = await Companion.getResponse({
      transcript: entry.transcript,
      scores: entry.scores,
      recentSummary: buildRecentSummary(entry),
    });
    if (!message) return;
    // Conservée sur l'entrée pour apparaître dans l'historique — sans ça,
    // la réponse du compagnon disparaissait dès qu'on quittait l'écran de
    // résumé, aucun moyen d'y revenir pour suivre l'accompagnement dans
    // le temps.
    Storage.updateEntry(entry.id, { companionResponse: message });
    // Ignore une réponse arrivée en retard si l'utilisateur a depuis lancé
    // un nouvel enregistrement (on ne veut pas afficher un message qui ne
    // correspond plus à l'entrée actuellement affichée) — mais elle reste
    // bien sauvegardée ci-dessus pour l'historique.
    if (sessionId !== summarySessionId) return;
    els.companionText.textContent = message;
    els.companionCard.hidden = false;
    els.btnCompanionSpeak.hidden = !isTTSSupported();
    els.btnCompanionSpeak.classList.remove("speak-btn-active");
  }

  function isTTSSupported() {
    return "speechSynthesis" in window;
  }

  function toggleCompanionSpeech() {
    if (!isTTSSupported()) return;
    if (speechSynthesis.speaking) {
      speechSynthesis.cancel();
      els.btnCompanionSpeak.classList.remove("speak-btn-active");
      return;
    }
    const utterance = new SpeechSynthesisUtterance(els.companionText.textContent);
    utterance.lang = "fr-FR";
    utterance.onend = () => els.btnCompanionSpeak.classList.remove("speak-btn-active");
    utterance.onerror = () => els.btnCompanionSpeak.classList.remove("speak-btn-active");
    els.btnCompanionSpeak.classList.add("speak-btn-active");
    speechSynthesis.speak(utterance);
  }

  els.btnCompanionSpeak.addEventListener("click", toggleCompanionSpeech);

  // Analyse locale de la piste audio (pauses, pics de volume) : ça prend un
  // instant (décodage audio), donc ça se fait en arrière-plan après avoir
  // déjà affiché le résumé texte, pour ne pas retarder l'écran principal.
  async function processAudioProsody(entry, audioBlob, sessionId) {
    AudioStore.saveAudio(entry.id, audioBlob);

    const metrics = await Prosody.analyze(audioBlob);
    if (!metrics) return;

    Storage.updateEntry(entry.id, { prosody: metrics });

    // Si l'utilisateur a depuis lancé un nouvel enregistrement, l'écran de
    // résumé affiché n'est plus celui de cette entrée : on n'y touche pas.
    if (sessionId !== summarySessionId) return;
    renderWaveform(metrics);
  }

  function renderWaveform(metrics) {
    Charts.drawWaveform(els.chartWaveform, metrics.envelope, metrics.pauseMask);

    const stats = [];
    stats.push(`${metrics.pauseCount} pause${metrics.pauseCount !== 1 ? "s" : ""}`);
    if (metrics.longestPauseMs > 0) {
      stats.push(`la plus longue : ${(metrics.longestPauseMs / 1000).toFixed(1)}s`);
    }
    stats.push(`${Math.round(metrics.speakingRatio * 100)}% du temps parlé activement`);
    if (metrics.peakCount > 0) {
      stats.push(`${metrics.peakCount} pic${metrics.peakCount !== 1 ? "s" : ""} de voix`);
    }
    // Mesure acoustique brute (fréquence fondamentale), pas une émotion :
    // on l'affiche comme telle, sans en tirer de conclusion sur l'humeur.
    if (metrics.pitchMeanHz) {
      const variationLabel =
        metrics.pitchVariation == null
          ? ""
          : metrics.pitchVariation < 0.18
          ? " (plutôt stable)"
          : " (assez variée)";
      stats.push(`ton moyen : ${metrics.pitchMeanHz} Hz${variationLabel}`);
    }
    els.waveformStats.innerHTML = stats.map((s) => `<span>${escapeHtml(s)}</span>`).join("");
    els.waveformCard.hidden = false;
  }

  function renderHeatmap(entries) {
    if (!entries.length) {
      els.historyHeatmap.innerHTML = "";
      return;
    }
    const daysWithEntry = new Set(entries.map((e) => new Date(e.date).toDateString()));
    const totalDays = 35;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    let html = "";
    for (let i = totalDays - 1; i >= 0; i--) {
      const d = new Date(today);
      d.setDate(d.getDate() - i);
      const active = daysWithEntry.has(d.toDateString());
      const label = d.toLocaleDateString("fr-FR", { weekday: "long", day: "numeric", month: "long" });
      html += `<div class="heatmap-cell${active ? " heatmap-cell-active" : ""}${i === 0 ? " heatmap-cell-today" : ""}" title="${label}"></div>`;
    }
    els.historyHeatmap.innerHTML = html;
  }

  function renderHistory() {
    const allEntries = [...Storage.getEntries()].sort((a, b) => new Date(b.date) - new Date(a.date));
    renderHeatmap(allEntries);
    if (!allEntries.length) {
      els.historyList.innerHTML = `<p class="history-empty">Aucun enregistrement pour l'instant. Va dans l'onglet "Parler" pour commencer.</p>`;
      return;
    }

    const query = els.historySearch.value.trim().toLocaleLowerCase("fr-FR");
    const entries = query
      ? allEntries.filter((e) => (e.transcript || "").toLocaleLowerCase("fr-FR").includes(query))
      : allEntries;

    if (!entries.length) {
      els.historyList.innerHTML = `<p class="history-empty">Aucun journal ne contient "${escapeHtml(els.historySearch.value.trim())}".</p>`;
      return;
    }

    els.historyList.innerHTML = entries
      .map((e) => {
        const d = new Date(e.date);
        const dateLabel = d.toLocaleDateString("fr-FR", { weekday: "long", day: "numeric", month: "long", hour: "2-digit", minute: "2-digit" });
        const excerpt =
          e.type === "checkin"
            ? `Check-in rapide — énergie ${e.scores.energy}, stress ${e.scores.stress}`
            : truncate(e.transcript, 140);
        const companionHtml = e.companionResponse
          ? `<div class="history-companion"><span class="history-companion-label">Ton compagnon</span>${escapeHtml(e.companionResponse)}</div>`
          : "";
        return `
          <div class="history-item">
            <div class="history-date">${dateLabel}</div>
            <div class="history-excerpt">${escapeHtml(excerpt)}</div>
            ${companionHtml}
          </div>`;
      })
      .join("");
  }

  function renderTrends() {
    const entries = [...Storage.getEntries()].sort((a, b) => new Date(a.date) - new Date(b.date));
    const insights = Analysis.generateInsights(entries);

    els.weeklySummary.hidden = true;
    els.weeklySummary.textContent = "";
    els.btnWeeklySummary.disabled = entries.length < 3;

    if (!entries.length) {
      els.insights.innerHTML = `<p class="insights-empty">Enregistre-toi quelques jours pour voir apparaître tes tendances ici.</p>`;
    } else if (!insights.length) {
      els.insights.innerHTML = `<p class="insights-empty">Continue à enregistrer régulièrement pour révéler des tendances.</p>`;
    } else {
      els.insights.innerHTML = insights.map((i) => `<div class="insight-card">${escapeHtml(i)}</div>`).join("");
    }

    const keywords = Analysis.recurringKeywords(entries, 6);
    if (keywords.length) {
      els.keywordsList.innerHTML = keywords
        .map((k) => `<span class="keyword-chip">${escapeHtml(k.word)}<span class="keyword-chip-count">×${k.count}</span></span>`)
        .join("");
      els.keywordsCard.hidden = false;
    } else {
      els.keywordsCard.hidden = true;
    }

    Charts.drawTimeline(els.chartTimeline, entries);
    Charts.drawWeekdayBars(els.chartWeekday, entries);

    // La vue mensuelle n'a de sens qu'une fois plusieurs mois de données
    // accumulés — inutile de l'afficher pour un seul mois en cours.
    const monthly = Analysis.monthlyAverages(entries);
    if (monthly.length >= 2) {
      Charts.drawMonthlyBars(els.chartMonthly, monthly);
      const best = Analysis.bestMonth(monthly);
      if (best) {
        els.bestMonthNote.textContent = `Ton meilleur mois jusqu'ici : ${best.label}.`;
        els.bestMonthNote.hidden = false;
      } else {
        els.bestMonthNote.hidden = true;
      }
      els.monthlySection.hidden = false;
    } else {
      els.monthlySection.hidden = true;
    }
  }

  // Bilan imprimable : construit une page dédiée, non stylée comme l'app,
  // affichée uniquement via @media print (voir style.css) puis imprimée /
  // exportée en PDF avec la fonction native du navigateur — pas de
  // dépendance PDF externe.
  function buildPrintReport() {
    const entries = [...Storage.getEntries()].sort((a, b) => new Date(a.date) - new Date(b.date));
    const dateLabel = new Date().toLocaleDateString("fr-FR", { day: "numeric", month: "long", year: "numeric" });

    let html = `<h1>Bilan Écho</h1><p class="print-date">Généré le ${dateLabel}</p>`;

    if (!entries.length) {
      html += `<p>Aucune donnée enregistrée pour l'instant.</p>`;
    } else {
      const weeklySummary = Analysis.generateWeeklySummary(entries);
      if (weeklySummary) {
        html += `<h2>Résumé de la semaine</h2><p>${escapeHtml(weeklySummary)}</p>`;
      }

      const insights = Analysis.generateInsights(entries);
      if (insights.length) {
        html += `<h2>Tendances</h2><ul>${insights.map((i) => `<li>${escapeHtml(i)}</li>`).join("")}</ul>`;
      }

      const monthly = Analysis.monthlyAverages(entries);
      if (monthly.length) {
        html += `<h2>Moyennes par mois</h2><table><tr><th>Mois</th><th>Énergie</th><th>Stress</th><th>Fatigue</th><th>Humeur</th><th>Entrées</th></tr>`;
        html += monthly
          .map(
            (m) =>
              `<tr><td>${escapeHtml(m.label)}</td><td>${Math.round(m.energy)}</td><td>${Math.round(m.stress)}</td><td>${Math.round(m.fatigue)}</td><td>${Math.round(m.mood)}</td><td>${m.count}</td></tr>`
          )
          .join("");
        html += `</table>`;
      }

      const keywords = Analysis.recurringKeywords(entries, 10);
      if (keywords.length) {
        html += `<h2>Mots-clés qui reviennent</h2><p>${escapeHtml(keywords.map((k) => `${k.word} (${k.count})`).join(", "))}</p>`;
      }
    }

    html += `<p class="print-disclaimer">Écho est un outil de bien-être personnel, pas un dispositif médical ni un diagnostic. Les scores viennent d'une analyse de mots-clés locale, propre à ce journal — pas un seuil clinique.</p>`;
    els.printReport.innerHTML = html;
  }

  els.btnPrintReport.addEventListener("click", () => {
    buildPrintReport();
    window.print();
  });

  function escapeHtml(str) {
    const div = document.createElement("div");
    div.textContent = str;
    return div.innerHTML;
  }

  function truncate(str, n) {
    return str.length > n ? str.slice(0, n).trim() + "…" : str;
  }

  els.btnWeeklySummary.addEventListener("click", () => {
    const entries = Storage.getEntries();
    const summary = Analysis.generateWeeklySummary(entries);
    els.weeklySummary.textContent = summary || "Enregistre-toi encore quelques jours pour débloquer ton résumé de la semaine.";
    els.weeklySummary.hidden = false;
  });

  // Check-in rapide : deux curseurs (énergie, stress) reportés directement
  // par l'utilisateur, sans passer par la voix. Contrairement à un journal
  // vocal, fatigue/humeur n'y sont pas mesurées — laissées à 50 (neutre,
  // "pas de signal") plutôt que déduites de l'énergie/stress, pour ne
  // jamais afficher une valeur inventée comme si elle avait été rapportée.
  els.checkinEnergy.addEventListener("input", () => {
    els.checkinEnergyValue.textContent = els.checkinEnergy.value;
  });
  els.checkinStress.addEventListener("input", () => {
    els.checkinStressValue.textContent = els.checkinStress.value;
  });
  els.btnCheckin.addEventListener("click", () => {
    els.checkinEnergy.value = 50;
    els.checkinEnergyValue.textContent = "50";
    els.checkinStress.value = 50;
    els.checkinStressValue.textContent = "50";
    els.checkinOverlay.hidden = false;
  });
  els.checkinCancel.addEventListener("click", () => {
    els.checkinOverlay.hidden = true;
  });
  els.checkinSave.addEventListener("click", () => {
    const energy = Number(els.checkinEnergy.value);
    const stress = Number(els.checkinStress.value);
    const entry = {
      id: `${Date.now()}`,
      date: new Date().toISOString(),
      durationSec: 0,
      transcript: "",
      wordCount: 0,
      type: "checkin",
      scores: { energy, stress, fatigue: 50, mood: 50, keywords: [], hasSignal: true, wpm: 0 },
    };
    Storage.saveEntry(entry);
    checkReminder();
    renderStreakBadges();
    els.checkinOverlay.hidden = true;
    els.recStatus.textContent = "Check-in enregistré.";
    setTimeout(() => {
      if (els.recStatus.textContent === "Check-in enregistré.") els.recStatus.textContent = "";
    }, 2500);
  });

  els.btnScaleInfo.addEventListener("click", () => {
    els.scaleInfo.hidden = !els.scaleInfo.hidden;
  });

  // Réglages : export / import / suppression.
  els.btnExport.addEventListener("click", () => {
    const blob = new Blob([Storage.exportJSON()], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `echo-export-${new Date().toISOString().slice(0, 10)}.json`;
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  });

  els.inputImport.addEventListener("change", async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    try {
      const text = await file.text();
      Storage.importJSON(text);
      alert("Import réussi.");
      renderHistory();
      renderTrends();
    } catch (err) {
      alert("Import impossible : " + err.message);
    }
    e.target.value = "";
  });

  els.btnClear.addEventListener("click", () => {
    if (confirm("Effacer définitivement tous tes enregistrements ?")) {
      Storage.clearAll();
      AudioStore.clearAll();
      renderHistory();
      renderTrends();
    }
  });

  els.audioTrackToggle.checked = false;
  els.audioTrackToggle.disabled = true;

  function updateLockButton() {
    els.btnLockToggle.textContent = Lock.isEnabled() ? "Désactiver" : "Activer";
  }
  updateLockButton();
  els.btnLockToggle.addEventListener("click", () => {
    if (Lock.isEnabled()) {
      if (confirm("Désactiver le verrouillage par code ?")) {
        Lock.disable();
        updateLockButton();
      }
      return;
    }
    Lock.showSetup(() => {
      updateLockButton();
    }, () => {});
  });

  applyAccent(localStorage.getItem(ACCENT_KEY) || "violet");
  els.accentSwatches.addEventListener("click", (e) => {
    const btn = e.target.closest(".accent-swatch");
    if (!btn) return;
    const name = btn.dataset.accent;
    localStorage.setItem(ACCENT_KEY, name);
    applyAccent(name);
  });

  els.reminderToggle.checked = isReminderEnabled();
  els.reminderTimeRow.hidden = !isReminderEnabled();
  els.reminderTime.value = getReminderTime();
  els.reminderToggle.addEventListener("change", () => {
    const enabled = els.reminderToggle.checked;
    localStorage.setItem(REMINDER_ENABLED_KEY, enabled ? "1" : "0");
    els.reminderTimeRow.hidden = !enabled;
    if (enabled && "Notification" in window && Notification.permission === "default") {
      Notification.requestPermission();
    }
    checkReminder();
  });
  els.reminderTime.addEventListener("change", () => {
    localStorage.setItem(REMINDER_TIME_KEY, els.reminderTime.value);
    checkReminder();
  });
  els.reminderBannerDismiss.addEventListener("click", () => {
    localStorage.setItem(reminderDismissKey(), "1");
    els.reminderBanner.hidden = true;
  });

  if (!Recorder.isSupported()) {
    els.unsupported.hidden = false;
    els.btnRecord.disabled = true;
  }

  if ("serviceWorker" in navigator) {
    window.addEventListener("load", () => {
      navigator.serviceWorker.register("sw.js").catch(() => {});
    });
  }

  els.btnHelp.addEventListener("click", () => Onboarding.start());

  els.recordHint.textContent = todaysPrompt();
  checkReminder();
  renderStreakBadges();

  navigate("record");
  if (Lock.needsUnlock()) {
    Lock.showUnlock(() => Onboarding.autostart());
  } else {
    Onboarding.autostart();
  }
})();
