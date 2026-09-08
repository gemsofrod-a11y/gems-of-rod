(() => {
  "use strict";

  const state = {
    accounts: [],
    activeAccountId: null,
    candles: [],
    timeframe: "1m",
  };

  const $ = (id) => document.getElementById(id);

  async function api(path, options) {
    const res = await fetch(path, {
      headers: { "Content-Type": "application/json" },
      ...options,
    });
    const body = await res.json().catch(() => ({}));
    if (!res.ok) {
      throw new Error(body.error || `Erreur ${res.status}`);
    }
    return body;
  }

  function fmtUsd(n) {
    return new Intl.NumberFormat("fr-FR", { style: "currency", currency: "USD" }).format(n);
  }

  function fmtOz(n) {
    return `${n.toFixed(4)} oz`;
  }

  // --- Prix ---

  async function refreshPrice() {
    try {
      const quote = await api("/api/price");
      $("price-value").textContent = fmtUsd(quote.price);
      const badge = $("price-source");
      badge.textContent = quote.source === "live" ? `cours réel · ${quote.provider}` : "simulé (hors ligne)";
      badge.className = `badge ${quote.source}`;
    } catch (err) {
      $("price-source").textContent = "hors ligne";
    }
  }

  async function refreshCandles() {
    try {
      const result = await api(`/api/candles?timeframe=${state.timeframe}&limit=180`);
      state.candles = result.candles;
      const providerLabel = { "yahoo-finance": "cours réel (Yahoo Finance)", "historique-local": "cours réel (historique local)", "simule": "données simulées" }[result.provider] || result.provider;
      $("chart-provider").textContent = providerLabel;
      drawCandles();
    } catch (err) {
      /* silencieux : le graphique reste inchangé si l'historique n'est pas encore disponible */
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

  // --- Comptes ---

  async function refreshAccounts() {
    state.accounts = await api("/api/accounts");
    if (state.activeAccountId === null && state.accounts.length) {
      state.activeAccountId = state.accounts[0].id;
    }
    renderAccountTabs();
    renderAccountSummary();
    await Promise.all([refreshTrades(), refreshBrokerStatus()]);
  }

  function renderAccountTabs() {
    const nav = $("account-tabs");
    nav.innerHTML = "";
    state.accounts.forEach((acc) => {
      const btn = document.createElement("button");
      btn.textContent = acc.name;
      btn.className = acc.id === state.activeAccountId ? "active" : "";
      btn.onclick = () => {
        state.activeAccountId = acc.id;
        renderAccountTabs();
        renderAccountSummary();
        refreshTrades();
      };
      nav.appendChild(btn);
    });
  }

  function activeAccount() {
    return state.accounts.find((a) => a.id === state.activeAccountId);
  }

  function renderAccountSummary() {
    const acc = activeAccount();
    const dl = $("account-summary");
    if (!acc) { dl.innerHTML = ""; return; }
    const pnlClass = acc.pnl >= 0 ? "positive" : "negative";
    dl.innerHTML = `
      <dt>Solde disponible</dt><dd>${fmtUsd(acc.cash_balance)}</dd>
      <dt>Position</dt><dd>${fmtOz(acc.position_oz)}</dd>
      <dt>Valeur totale</dt><dd>${fmtUsd(acc.equity)}</dd>
      <dt>Performance</dt><dd class="${pnlClass}">${acc.pnl >= 0 ? "+" : ""}${acc.pnl_pct.toFixed(2)} %</dd>
    `;
    $("broker-select").value = acc.external_broker || "none";
  }

  // --- Ordres ---

  $("order-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const acc = activeAccount();
    const errorEl = $("order-error");
    errorEl.textContent = "";
    if (!acc) return;
    const side = document.querySelector('input[name="side"]:checked').value;
    const mode = $("order-mode").value;
    const value = parseFloat($("order-value").value);
    const payload = { side };
    if (mode === "amount") payload.amount = value; else payload.qty_oz = value;
    try {
      await api(`/api/accounts/${acc.id}/order`, { method: "POST", body: JSON.stringify(payload) });
      $("order-value").value = "";
      await refreshAccounts();
    } catch (err) {
      errorEl.textContent = err.message;
    }
  });

  async function refreshTrades() {
    const acc = activeAccount();
    if (!acc) return;
    const trades = await api(`/api/accounts/${acc.id}/trades`);
    const tbody = document.querySelector("#trades-table tbody");
    tbody.innerHTML = trades.map((t) => `
      <tr>
        <td>${new Date(t.created_at * 1000).toLocaleString("fr-FR")}</td>
        <td>${t.side === "buy" ? "Achat" : "Vente"}</td>
        <td>${t.qty_oz.toFixed(4)}</td>
        <td>${fmtUsd(t.price)}</td>
        <td>${fmtUsd(t.amount)}</td>
        <td>${t.source === "bot" ? "Bot" : "Manuel"}${t.external_broker ? " · " + t.external_broker : ""}</td>
      </tr>
    `).join("");
  }

  // --- Broker externe ---

  async function refreshBrokerStatus() {
    const status = await api("/api/brokers/status");
    const acc = activeAccount();
    const el = $("broker-status");
    if (!acc) { el.textContent = ""; return; }
    const parts = [];
    if (acc.external_broker === "oanda") {
      parts.push(status.oanda.configured
        ? `OANDA connecté (${status.oanda.env})`
        : "OANDA sélectionné mais non configuré — voir trading-app/.env");
    } else if (acc.external_broker === "etoro") {
      parts.push(status.etoro.configured
        ? "eToro (démo) connecté — connecteur bêta, à vérifier"
        : "eToro sélectionné mais non configuré — voir trading-app/.env");
    } else {
      parts.push("Ordres exécutés par le broker interne (simulation).");
    }
    el.textContent = parts.join(" ");
  }

  $("broker-select").addEventListener("change", async (e) => {
    const acc = activeAccount();
    if (!acc) return;
    await api(`/api/accounts/${acc.id}/broker`, {
      method: "POST",
      body: JSON.stringify({ broker: e.target.value }),
    });
    await refreshAccounts();
  });

  // --- Bot ---

  function effectiveBaseStrategy() {
    return $("bot-strategy").value === "news_aware_trend" ? $("news-base").value : $("bot-strategy").value;
  }

  function updateBotParamVisibility() {
    const strategy = $("bot-strategy").value;
    const base = effectiveBaseStrategy();
    $("bot-params-base").style.display = strategy === "news_aware_trend" ? "flex" : "none";
    $("bot-params-sma").style.display = base === "sma_crossover" ? "flex" : "none";
    $("bot-params-rsi").style.display = base === "rsi_mean_reversion" ? "flex" : "none";
  }

  $("bot-strategy").addEventListener("change", updateBotParamVisibility);
  $("news-base").addEventListener("change", updateBotParamVisibility);
  updateBotParamVisibility();

  function collectBotParams() {
    const strategy = $("bot-strategy").value;
    const base = effectiveBaseStrategy();
    const baseParams = base === "sma_crossover"
      ? { fast: +$("sma-fast").value, slow: +$("sma-slow").value }
      : { period: +$("rsi-period").value, oversold: +$("rsi-oversold").value, overbought: +$("rsi-overbought").value };
    if (strategy === "news_aware_trend") {
      return { strategy, params: { base, ...baseParams } };
    }
    return { strategy, params: baseParams };
  }

  $("bot-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const acc = activeAccount();
    if (!acc) return;
    const { strategy, params } = collectBotParams();
    const target = $("bot-target").value;
    await api("/api/bot/start", {
      method: "POST",
      body: JSON.stringify({
        account_id: acc.id,
        strategy,
        params,
        interval_sec: +$("bot-interval").value,
        risk_pct: +$("bot-risk").value,
        take_profit_pct: +$("bot-tp").value,
        stop_loss_pct: +$("bot-sl").value,
        max_holding_sec: +$("bot-hold").value * 60,
        target_equity: target ? +target : null,
        floor_pct: +$("bot-floor").value,
      }),
    });
    refreshBotStatus();
  });

  $("bot-stop").addEventListener("click", async () => {
    const acc = activeAccount();
    if (!acc) return;
    await api("/api/bot/stop", { method: "POST", body: JSON.stringify({ account_id: acc.id }) });
    refreshBotStatus();
  });

  async function refreshBotStatus() {
    const acc = activeAccount();
    if (!acc) return;
    const statuses = await api("/api/bot/status");
    const mine = statuses.find((s) => s.account_id === acc.id);
    const el = $("bot-status");
    if (!mine || !mine.running) { el.textContent = "Bot arrêté."; return; }
    el.textContent = `Bot actif (${mine.strategy}) · dernier signal : ${mine.last_signal || "—"}` +
      (mine.last_error ? ` · erreur : ${mine.last_error}` : "");
  }

  // --- Backtest ---

  $("backtest-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const strategy = $("bt-strategy").value;
    const days = +$("bt-days").value;
    const params = strategy === "sma_crossover" ? { fast: 10, slow: 30 } : { period: 14, oversold: 30, overbought: 70 };
    const result = await api("/api/backtest", {
      method: "POST",
      body: JSON.stringify({ strategy, params, days }),
    });
    const el = $("backtest-result");
    el.innerHTML = `
      <dl class="summary">
        <dt>Rendement total</dt><dd class="${result.total_return_pct >= 0 ? "positive" : "negative"}">${result.total_return_pct.toFixed(2)} %</dd>
        <dt>Drawdown max</dt><dd>${result.max_drawdown_pct.toFixed(2)} %</dd>
        <dt>Trades</dt><dd>${result.num_trades}</dd>
        <dt>Taux de réussite</dt><dd>${result.win_rate_pct !== null ? result.win_rate_pct.toFixed(1) + " %" : "n/a"}</dd>
      </dl>
      <p class="note">${result.note}</p>
    `;
  });

  // --- Boucle de rafraîchissement ---

  async function tick() {
    await refreshPrice();
    await refreshBotStatus();
  }

  (async function init() {
    await refreshAccounts();
    await tick();
    await refreshCandles();
    setInterval(tick, 4000);
    setInterval(refreshCandles, 15000);
    setInterval(refreshAccounts, 20000);
  })();
})();
