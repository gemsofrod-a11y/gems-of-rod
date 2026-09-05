(() => {
  "use strict";

  const state = {
    accounts: [],
    activeAccountId: null,
    priceHistory: [],
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
      badge.textContent = quote.source === "live" ? "cours réel" : "simulé";
      badge.className = `badge ${quote.source}`;
    } catch (err) {
      $("price-source").textContent = "hors ligne";
    }
  }

  async function refreshPriceHistory() {
    try {
      state.priceHistory = await api("/api/price/history");
      drawChart();
    } catch (err) {
      /* silencieux : le graphique reste vide si l'historique n'est pas encore disponible */
    }
  }

  function drawChart() {
    const canvas = $("price-chart");
    const ctx = canvas.getContext("2d");
    const w = canvas.width, h = canvas.height;
    ctx.clearRect(0, 0, w, h);
    const points = state.priceHistory;
    if (points.length < 2) return;

    const prices = points.map((p) => p.price);
    const min = Math.min(...prices), max = Math.max(...prices);
    const pad = (max - min) * 0.1 || 1;
    const yMin = min - pad, yMax = max + pad;

    ctx.strokeStyle = "#d4af37";
    ctx.lineWidth = 2;
    ctx.beginPath();
    points.forEach((p, i) => {
      const x = (i / (points.length - 1)) * w;
      const y = h - ((p.price - yMin) / (yMax - yMin)) * h;
      if (i === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
    });
    ctx.stroke();
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

  $("bot-strategy").addEventListener("change", (e) => {
    $("bot-params-sma").style.display = e.target.value === "sma_crossover" ? "flex" : "none";
    $("bot-params-rsi").style.display = e.target.value === "rsi_mean_reversion" ? "flex" : "none";
  });

  function collectBotParams() {
    const strategy = $("bot-strategy").value;
    if (strategy === "sma_crossover") {
      return { strategy, params: { fast: +$("sma-fast").value, slow: +$("sma-slow").value } };
    }
    return {
      strategy,
      params: {
        period: +$("rsi-period").value,
        oversold: +$("rsi-oversold").value,
        overbought: +$("rsi-overbought").value,
      },
    };
  }

  $("bot-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const acc = activeAccount();
    if (!acc) return;
    const { strategy, params } = collectBotParams();
    await api("/api/bot/start", {
      method: "POST",
      body: JSON.stringify({
        account_id: acc.id,
        strategy,
        params,
        interval_sec: +$("bot-interval").value,
        risk_pct: +$("bot-risk").value,
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
    await refreshPriceHistory();
    await refreshBotStatus();
  }

  (async function init() {
    await refreshAccounts();
    await tick();
    setInterval(tick, 8000);
    setInterval(refreshAccounts, 20000);
  })();
})();
