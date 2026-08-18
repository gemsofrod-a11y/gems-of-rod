(() => {
  const MAX_RECORDING_MS = 3 * 60 * 1000;

  const els = {
    views: document.querySelectorAll(".view"),
    tabs: document.querySelectorAll(".tab-btn"),
    navButtons: document.querySelectorAll("[data-nav]"),
    btnRecord: document.getElementById("btn-record"),
    timer: document.getElementById("timer"),
    recStatus: document.getElementById("rec-status"),
    liveTranscript: document.getElementById("live-transcript"),
    unsupported: document.getElementById("unsupported"),
    summaryContent: document.getElementById("summary-content"),
    crisisCard: document.getElementById("crisis-card"),
    companionCard: document.getElementById("companion-card"),
    companionText: document.getElementById("companion-text"),
    historyList: document.getElementById("history-list"),
    insights: document.getElementById("insights"),
    chartTimeline: document.getElementById("chart-timeline"),
    chartWeekday: document.getElementById("chart-weekday"),
    btnWeeklySummary: document.getElementById("btn-weekly-summary"),
    weeklySummary: document.getElementById("weekly-summary"),
    btnExport: document.getElementById("btn-export"),
    inputImport: document.getElementById("input-import"),
    btnClear: document.getElementById("btn-clear"),
    btnHelp: document.getElementById("btn-help"),
  };

  let isRecording = false;
  let startTime = 0;
  let timerInterval = null;
  let lastEntry = null;
  let sessionFinalized = false;

  function navigate(view) {
    els.views.forEach((v) => v.classList.toggle("view-active", v.id === `view-${view}`));
    els.tabs.forEach((t) => t.classList.toggle("tab-active", t.dataset.nav === view));
    if (view === "history") renderHistory();
    if (view === "trends") renderTrends();
  }

  els.navButtons.forEach((btn) => {
    btn.addEventListener("click", () => navigate(btn.dataset.nav));
  });

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
  }

  function stopRecording() {
    resetRecordingUI();
    Recorder.stop();
  }

  function finalizeRecording() {
    if (sessionFinalized) return;
    sessionFinalized = true;
    resetRecordingUI();

    const durationSec = Math.max(1, Math.round((Date.now() - startTime) / 1000));
    const transcript = Recorder.getTranscript();
    els.recStatus.textContent = "";
    els.timer.textContent = "00:00";

    if (!transcript) {
      els.recStatus.textContent = "Aucune parole détectée, réessaie.";
      return;
    }

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
    renderSummary(entry);
    navigate("summary");
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

  function renderSummary(entry) {
    const isCrisis = Analysis.detectCrisisSignal(entry.transcript);
    els.crisisCard.hidden = !isCrisis;

    els.companionCard.hidden = true;
    els.companionText.innerHTML = "";
    // En cas de signal de crise, on s'en tient au message local fiable
    // ci-dessus plutôt que d'ajouter une réponse IA moins prévisible.
    if (!isCrisis) requestCompanionResponse(entry);

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
  }

  let companionRequestId = 0;

  async function requestCompanionResponse(entry) {
    const requestId = ++companionRequestId;
    const message = await Companion.getResponse({
      transcript: entry.transcript,
      scores: entry.scores,
    });
    // Ignore une réponse arrivée en retard si l'utilisateur a depuis lancé
    // un nouvel enregistrement (on ne veut pas afficher un message qui ne
    // correspond plus à l'entrée actuellement affichée).
    if (requestId !== companionRequestId || !message) return;
    els.companionText.textContent = message;
    els.companionCard.hidden = false;
  }

  function renderHistory() {
    const entries = [...Storage.getEntries()].sort((a, b) => new Date(b.date) - new Date(a.date));
    if (!entries.length) {
      els.historyList.innerHTML = `<p class="history-empty">Aucun enregistrement pour l'instant. Va dans l'onglet "Parler" pour commencer.</p>`;
      return;
    }
    els.historyList.innerHTML = entries
      .map((e) => {
        const d = new Date(e.date);
        const dateLabel = d.toLocaleDateString("fr-FR", { weekday: "long", day: "numeric", month: "long", hour: "2-digit", minute: "2-digit" });
        return `
          <div class="history-item">
            <div class="history-date">${dateLabel}</div>
            <div class="history-excerpt">${escapeHtml(truncate(e.transcript, 140))}</div>
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

    Charts.drawTimeline(els.chartTimeline, entries);
    Charts.drawWeekdayBars(els.chartWeekday, entries);
  }

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
      renderHistory();
      renderTrends();
    }
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

  navigate("record");
  Onboarding.autostart();
})();
