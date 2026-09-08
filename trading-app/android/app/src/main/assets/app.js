(() => {
  "use strict";

  const $ = (id) => document.getElementById(id);
  const state = { timeframe: "1m", candles: [] };

  // --- onglets ---

  $("tabbar").addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-tab]");
    if (!btn) return;
    const tab = btn.dataset.tab;
    document.querySelectorAll(".tabbar button").forEach((b) => b.classList.toggle("active", b === btn));
    document.querySelectorAll(".tab-panel").forEach((p) => { p.hidden = p.dataset.tab !== tab; });
  });

  $("bot-advanced-toggle").addEventListener("click", () => {
    const el = $("bot-advanced");
    el.hidden = !el.hidden;
    $("bot-advanced-toggle").textContent = el.hidden ? "Réglages avancés ▾" : "Réglages avancés ▴";
  });

  // --- bulles explicatives ---

  let openTipBubble = null;
  let openTipButton = null;

  function closeTip() {
    if (openTipBubble) openTipBubble.remove();
    if (openTipButton) openTipButton.classList.remove("active");
    openTipBubble = null;
    openTipButton = null;
  }

  document.querySelectorAll("button.tip[data-tip]").forEach((btn) => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      e.stopPropagation();
      const wasOpenForThis = openTipButton === btn;
      closeTip();
      if (wasOpenForThis) return;

      const bubble = document.createElement("div");
      bubble.className = "tip-bubble";
      bubble.textContent = btn.dataset.tip;
      document.body.appendChild(bubble);

      const rect = btn.getBoundingClientRect();
      const bubbleWidth = bubble.offsetWidth;
      let left = rect.left + rect.width / 2 - bubbleWidth / 2;
      left = Math.max(8, Math.min(left, window.innerWidth - bubbleWidth - 8));
      const top = rect.bottom + 8;
      bubble.style.left = `${left}px`;
      bubble.style.top = `${top}px`;

      btn.classList.add("active");
      openTipBubble = bubble;
      openTipButton = btn;
    });
  });

  document.addEventListener("click", closeTip);

  function fmtUsd(n) {
    return new Intl.NumberFormat("fr-FR", { style: "currency", currency: "USD" }).format(n);
  }
  function fmtOz(n) { return `${n.toFixed(4)} oz`; }

  // --- cours ---

  function refreshPrice() {
    try {
      const quote = JSON.parse(window.NativeBridge.getPrice());
      $("price-value").textContent = fmtUsd(quote.price);
      const badge = $("price-source");
      badge.textContent = quote.source === "live" ? `cours réel · ${quote.provider}` : "simulé (hors ligne)";
      badge.className = `badge ${quote.source}`;

      if (state.candles.length) {
        const first = state.candles[0].o;
        const changePct = ((quote.price - first) / first) * 100;
        const el = $("price-change");
        el.textContent = `${changePct >= 0 ? "+" : ""}${changePct.toFixed(2)} %`;
        el.className = `price-change ${changePct >= 0 ? "positive" : "negative"}`;
      }
    } catch (err) {
      $("price-source").textContent = "indisponible";
    }
  }

  function refreshCandles() {
    try {
      const result = JSON.parse(window.NativeBridge.getCandles(state.timeframe, 120));
      state.candles = result.candles || [];
      const providerLabel = { "yahoo-finance": "cours réel (Yahoo Finance)", "simule": "données simulées" }[result.provider] || result.provider;
      $("chart-provider").textContent = providerLabel;
      drawCandles();
    } catch (err) {
      $("chart-provider").textContent = "graphique indisponible";
    }
  }

  $("timeframes").addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-tf]");
    if (!btn) return;
    state.timeframe = btn.dataset.tf;
    document.querySelectorAll("#timeframes button").forEach((b) => b.classList.toggle("active", b === btn));
    refreshCandles();
  });

  function drawCandles() {
    const canvas = $("price-chart");
    const ctx = canvas.getContext("2d");
    const w = canvas.width, h = canvas.height;
    ctx.clearRect(0, 0, w, h);
    const candles = state.candles;
    if (candles.length < 2) return;

    const lows = candles.map((c) => c.l), highs = candles.map((c) => c.h);
    const min = Math.min(...lows), max = Math.max(...highs);
    const pad = (max - min) * 0.08 || 1;
    const yMin = min - pad, yMax = max + pad;
    const yOf = (price) => h - ((price - yMin) / (yMax - yMin)) * h;

    const slot = w / candles.length;
    const bodyWidth = Math.max(1, slot * 0.6);

    candles.forEach((c, i) => {
      const x = i * slot + slot / 2;
      const up = c.c >= c.o;
      ctx.strokeStyle = ctx.fillStyle = up ? "#3ec98b" : "#e6604f";
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.moveTo(x, yOf(c.h));
      ctx.lineTo(x, yOf(c.l));
      ctx.stroke();
      const yOpen = yOf(c.o), yClose = yOf(c.c);
      const top = Math.min(yOpen, yClose);
      const height = Math.max(1, Math.abs(yClose - yOpen));
      ctx.fillRect(x - bodyWidth / 2, top, bodyWidth, height);
    });
  }

  // --- portefeuille ---

  function refreshWallet() {
    try {
      const w = JSON.parse(window.NativeBridge.getWallet());
      const pnlClass = w.pnl >= 0 ? "positive" : "negative";
      $("wallet-summary").innerHTML = `
        <dt>Solde disponible</dt><dd>${fmtUsd(w.cash_balance)}</dd>
        <dt>Position</dt><dd>${fmtOz(w.position_oz)}</dd>
        <dt>Valeur totale</dt><dd>${fmtUsd(w.equity)}</dd>
        <dt>Performance</dt><dd class="${pnlClass}">${w.pnl >= 0 ? "+" : ""}${w.pnl_pct.toFixed(2)} %</dd>
      `;
      $("hero-equity").textContent = fmtUsd(w.equity);
      const heroPnl = $("hero-pnl");
      heroPnl.textContent = `${w.pnl >= 0 ? "+" : ""}${fmtUsd(w.pnl)} (${w.pnl >= 0 ? "+" : ""}${w.pnl_pct.toFixed(2)} %)`;
      heroPnl.className = `hero-pnl ${pnlClass}`;
    } catch (err) { /* silencieux */ }
    refreshTrades();
  }

  function refreshTrades() {
    try {
      const trades = JSON.parse(window.NativeBridge.getTrades(30));
      const tbody = document.querySelector("#trades-table tbody");
      $("trades-empty").hidden = trades.length > 0;
      tbody.innerHTML = trades.map((t) => `
        <tr>
          <td>${t.side === "buy" ? "Achat" : "Vente"}</td>
          <td>${t.qty_oz.toFixed(4)}</td>
          <td>${fmtUsd(t.price)}</td>
          <td>${t.source === "bot" ? "Bot" : "Manuel"}</td>
        </tr>
      `).join("");
    } catch (err) { /* silencieux */ }
  }

  $("cta-invest").addEventListener("click", () => {
    const panel = $("order-panel");
    panel.hidden = !panel.hidden;
    if (!panel.hidden) panel.scrollIntoView({ behavior: "smooth", block: "center" });
  });

  $("order-form").addEventListener("submit", (e) => {
    e.preventDefault();
    const errorEl = $("order-error");
    errorEl.textContent = "";
    const side = document.querySelector('input[name="side"]:checked').value;
    const mode = $("order-mode").value;
    const value = parseFloat($("order-value").value);
    const raw = mode === "amount"
      ? window.NativeBridge.placeOrderByAmount(side, value)
      : window.NativeBridge.placeOrderByQty(side, value);
    const result = JSON.parse(raw);
    if (!result.ok) {
      errorEl.textContent = result.error || "Erreur inconnue";
      return;
    }
    $("order-value").value = "";
    refreshWallet();
  });

  $("reset-wallet").addEventListener("click", () => {
    if (!confirm("Réinitialiser le portefeuille à 1000 $ ? L'historique sera effacé.")) return;
    window.NativeBridge.resetWallet();
    refreshWallet();
  });

  // --- bot ---

  $("bot-strategy").addEventListener("change", (e) => {
    $("bot-params-sma").style.display = e.target.value === "sma_crossover" ? "flex" : "none";
    $("bot-params-rsi").style.display = e.target.value === "rsi_mean_reversion" ? "flex" : "none";
    $("bot-adaptive-note").style.display = e.target.value === "adaptive" ? "block" : "none";
  });
  $("bot-strategy").dispatchEvent(new Event("change"));

  function collectBotParams() {
    const strategy = $("bot-strategy").value;
    if (strategy === "sma_crossover") {
      return { strategy, params: { fast: +$("sma-fast").value, slow: +$("sma-slow").value } };
    }
    if (strategy === "rsi_mean_reversion") {
      return { strategy, params: { period: +$("rsi-period").value, oversold: +$("rsi-oversold").value, overbought: +$("rsi-overbought").value } };
    }
    // "adaptive" : le bot choisit lui-même, pas de paramètres à saisir ici
    return { strategy, params: {} };
  }

  $("bot-form").addEventListener("submit", (e) => {
    e.preventDefault();
    const errorEl = $("bot-status");
    const { strategy, params } = collectBotParams();
    const target = $("bot-target").value;
    try {
      const raw = window.NativeBridge.startBot(
        strategy, JSON.stringify(params), +$("bot-interval").value, +$("bot-amount").value,
        +$("bot-tp").value, +$("bot-sl").value, +$("bot-hold").value,
        target ? +target : 0, +$("bot-floor").value,
      );
      const result = JSON.parse(raw);
      if (result.status === "error") {
        errorEl.textContent = `Erreur au démarrage : ${result.error}`;
        return;
      }
    } catch (err) {
      errorEl.textContent = `Erreur au démarrage : ${err.message || err}`;
      return;
    }
    refreshBotStatus();
  });

  $("bot-stop").addEventListener("click", () => {
    window.NativeBridge.stopBot();
    refreshBotStatus();
  });

  function refreshBotStatus() {
    try {
      const s = JSON.parse(window.NativeBridge.getBotStatus());
      const el = $("bot-status");
      if (!s.running) { el.textContent = "Bot arrêté."; return; }
      el.textContent = `Bot actif (${s.strategy}) · dernier signal : ${s.last_signal || "—"}` +
        (s.last_error ? ` · erreur : ${s.last_error}` : "");
    } catch (err) { /* silencieux */ }
  }

  // --- boucle ---

  function tick() {
    refreshPrice();
    refreshWallet();
    refreshBotStatus();
  }

  (function init() {
    refreshCandles();
    tick();
    setInterval(tick, 4000);
    setInterval(refreshCandles, 15000);
  })();
})();
