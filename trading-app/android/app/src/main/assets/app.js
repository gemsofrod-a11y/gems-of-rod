(() => {
  "use strict";

  const $ = (id) => document.getElementById(id);
  const state = { timeframe: "1m", candles: [], livePrice: null };

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

  function fmtTime(epochSec, timeframe) {
    const d = new Date(epochSec * 1000);
    if (timeframe === "1d" || timeframe === "4h") {
      return d.toLocaleDateString("fr-FR", { day: "2-digit", month: "2-digit" });
    }
    return d.toLocaleTimeString("fr-FR", { hour: "2-digit", minute: "2-digit" });
  }

  // --- graphique (TradingView Lightweight Charts, embarqué en local) :
  // pincer-zoomer, glisser dans l'historique, croisillon fluide et échelle
  // de prix sont tous gérés nativement par la librairie plutôt que
  // réimplémentés à la main sur un <canvas>. ---
  let chart = null;
  let candleSeries = null;
  // Ne recadre la vue (fitContent) qu'au premier chargement et lors d'un
  // changement de période — jamais lors des rafraîchissements
  // automatiques (toutes les 8s), sinon la vue "saute" en permanence et
  // annule tout pincer-zoomer/glissé fait par l'utilisateur.
  let shouldFitContent = true;

  function initChart() {
    const container = $("price-chart");
    chart = LightweightCharts.createChart(container, {
      width: container.clientWidth,
      height: container.clientHeight,
      layout: { background: { color: "#0f1115" }, textColor: "#9aa2b1" },
      grid: {
        vertLines: { color: "#1c202a" },
        horzLines: { color: "#1c202a" },
      },
      rightPriceScale: { borderColor: "#2a2e38" },
      timeScale: { borderColor: "#2a2e38", timeVisible: true, secondsVisible: false },
      crosshair: { mode: LightweightCharts.CrosshairMode.Normal },
      localization: {
        priceFormatter: (p) => p.toFixed(p >= 1000 ? 1 : 2),
      },
    });
    candleSeries = chart.addCandlestickSeries({
      upColor: "#3ec98b",
      downColor: "#e6604f",
      borderVisible: false,
      wickUpColor: "#3ec98b",
      wickDownColor: "#e6604f",
      priceLineWidth: 1,
    });
    chart.subscribeCrosshairMove(updateOhlcLegend);
    window.addEventListener("resize", () => {
      chart.resize(container.clientWidth, container.clientHeight);
    });
  }

  function updateOhlcLegend(param) {
    const legend = $("ohlc-legend");
    let bar = param && param.seriesData ? param.seriesData.get(candleSeries) : null;
    let time = param && param.time ? param.time : null;
    if (!bar) {
      const last = state.candles[state.candles.length - 1];
      bar = last ? { open: last.o, high: last.h, low: last.l, close: last.c } : null;
      time = last ? last.t : null;
    }
    if (!bar) { legend.textContent = ""; return; }
    const up = bar.close >= bar.open;
    const closeColor = up ? "var(--green)" : "var(--red)";
    legend.innerHTML = `${time ? fmtTime(time, state.timeframe) + " · " : ""}` +
      `O <b>${bar.open.toFixed(1)}</b> H <b>${bar.high.toFixed(1)}</b> L <b>${bar.low.toFixed(1)}</b> ` +
      `C <b style="color:${closeColor}">${bar.close.toFixed(1)}</b>`;
  }

  function refreshPrice() {
    try {
      const quote = JSON.parse(window.NativeBridge.getPrice());
      $("price-value").textContent = fmtUsd(quote.price);
      const badge = $("price-source");
      badge.textContent = quote.source === "live" ? `cours réel · ${quote.provider}` : "simulé (hors ligne)";
      badge.className = `badge ${quote.source}`;

      const el = $("price-change");
      if (state.candles.length) {
        const first = state.candles[0].o;
        const changePct = ((quote.price - first) / first) * 100;
        el.textContent = `${changePct >= 0 ? "+" : ""}${changePct.toFixed(2)} %`;
        el.className = `price-change ${changePct >= 0 ? "positive" : "negative"}`;
      } else {
        el.textContent = "—";
        el.className = "price-change";
      }

      // Fait "vivre" la dernière bougie entre deux rechargements complets
      // (toutes les 8s) : chaque cotation (toutes les 2s) étire son close/
      // high/low avec le dernier cours via series.update(), le mécanisme
      // officiel de la librairie pour les mises à jour temps réel — pas de
      // redessin manuel, la librairie s'occupe de tout (fluide, zoomable).
      state.livePrice = quote.price;
      if (state.candles.length && candleSeries) {
        const last = state.candles[state.candles.length - 1];
        last.c = quote.price;
        if (quote.price > last.h) last.h = quote.price;
        if (quote.price < last.l) last.l = quote.price;
        candleSeries.update({ time: last.t, open: last.o, high: last.h, low: last.l, close: last.c });
        candleSeries.applyOptions({ priceLineColor: last.c >= last.o ? "#3ec98b" : "#e6604f" });
        updateOhlcLegend(null);
      }
    } catch (err) {
      $("price-source").textContent = "indisponible";
    }
  }

  function refreshCandles() {
    try {
      const result = JSON.parse(window.NativeBridge.getCandles(state.timeframe, 120));
      state.candles = result.candles || [];
      const providerLabel = {
        "yahoo-finance": "mouvement réel · Yahoo Finance",
        "cours-reel-local": "mouvement réel · cours collectés en direct",
        "simule": "⚠ mouvement simulé (pas de connexion aux vraies données)",
      }[result.provider] || result.provider;
      const isReal = result.provider === "yahoo-finance" || result.provider === "cours-reel-local";
      const providerEl = $("chart-provider");
      providerEl.textContent = providerLabel;
      providerEl.className = `badge chart-badge ${isReal ? "live" : "simule"}`;
      if (candleSeries) {
        candleSeries.setData(state.candles.map((c) => ({ time: c.t, open: c.o, high: c.h, low: c.l, close: c.c })));
        if (shouldFitContent) {
          chart.timeScale().fitContent();
          shouldFitContent = false;
        }
      }
      updateOhlcLegend(null);
    } catch (err) {
      const providerEl = $("chart-provider");
      providerEl.textContent = "graphique indisponible";
      providerEl.className = "badge chart-badge simule";
    }
  }

  $("timeframes").addEventListener("click", (e) => {
    const btn = e.target.closest("button[data-tf]");
    if (!btn) return;
    state.timeframe = btn.dataset.tf;
    document.querySelectorAll("#timeframes button").forEach((b) => b.classList.toggle("active", b === btn));
    shouldFitContent = true;
    refreshCandles();
  });

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
      const trades = JSON.parse(window.NativeBridge.getTrades(30)); // du plus récent au plus ancien
      $("trades-count").textContent = trades.length;

      // Reconstruit les allers-retours achat → vente en ordre chronologique
      // pour calculer le gain/perte de chaque vente par rapport à l'achat
      // qui l'a précédée (un seul achat ouvert à la fois, comme le bot).
      const chronological = [...trades].reverse();
      let openBuy = null;
      for (const t of chronological) {
        if (t.side === "buy") {
          openBuy = t;
        } else if (t.side === "sell" && openBuy) {
          t._pnl = t.amount - openBuy.amount;
          openBuy = null;
        }
      }

      const tbody = document.querySelector("#trades-table tbody");
      $("trades-empty").hidden = trades.length > 0;
      tbody.innerHTML = trades.map((t) => {
        let pnlCell = "—";
        let pnlClass = "neutral";
        if (typeof t._pnl === "number") {
          pnlClass = t._pnl > 0 ? "positive" : t._pnl < 0 ? "negative" : "neutral";
          pnlCell = `${t._pnl >= 0 ? "+" : ""}${fmtUsd(t._pnl)}`;
        }
        return `
        <tr>
          <td>${t.side === "buy" ? "Achat" : "Vente"}</td>
          <td>${t.qty_oz.toFixed(4)}</td>
          <td>${fmtUsd(t.price)}</td>
          <td class="pnl-cell ${pnlClass}">${pnlCell}</td>
          <td>${t.source === "bot" ? "Bot" : "Manuel"}</td>
        </tr>
      `;
      }).join("");
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
    errorEl.className = "status stopped";
    const { strategy, params } = collectBotParams();
    const target = $("bot-target").value;
    const stake = +$("bot-amount").value;

    // L'objectif porte sur la mise de départ, pas sur le portefeuille entier :
    // un objectif déjà couvert par la mise elle-même ferait s'arrêter le bot
    // instantanément, sans qu'aucun trade n'ait lieu. On bloque ce cas ici
    // plutôt que de laisser le bot s'arrêter en silence.
    if (target && +target <= stake) {
      errorEl.textContent = `Objectif trop bas : la mise de départ est déjà de ${fmtUsd(stake)}. Mettez un objectif supérieur à la mise, ou laissez le champ vide pour qu'elle progresse sans plafond.`;
      return;
    }

    try {
      const raw = window.NativeBridge.startBot(
        strategy, JSON.stringify(params), +$("bot-interval").value, stake,
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
      const stateEl = $("bot-state");
      const stateLabel = $("bot-state-label");

      if (!s.running) {
        stateEl.classList.remove("active");
        stateLabel.textContent = "Arrêté";
        // s.last_signal porte la raison de l'arrêt (objectif atteint, seuil de
        // protection, erreur…) même quand le bot n'est plus en cours d'exécution —
        // sans ça, on ne sait jamais POURQUOI il s'est arrêté.
        el.textContent = s.last_signal ? `Bot arrêté — ${s.last_signal}` : "Bot arrêté.";
        el.className = "status stopped";
        return;
      }
      stateEl.classList.add("active");
      stateLabel.textContent = "Actif";
      el.textContent = `Bot actif (${s.strategy}) · dernier signal : ${s.last_signal || "en attente du premier cycle…"}` +
        (s.last_error ? ` · erreur : ${s.last_error}` : "");
      el.className = "status running";
    } catch (err) { /* silencieux */ }
  }

  // --- boucle ---

  function tick() {
    refreshPrice();
    refreshWallet();
    refreshBotStatus();
  }

  (function init() {
    initChart();
    refreshCandles();
    tick();
    setInterval(tick, 2000);
    setInterval(refreshCandles, 8000);
  })();
})();
