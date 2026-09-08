(() => {
  "use strict";

  const $ = (id) => document.getElementById(id);
  const state = { timeframe: "1m" };

  function fmtUsd(n) {
    return new Intl.NumberFormat("fr-FR", { style: "currency", currency: "USD" }).format(n);
  }

  function refreshPrice() {
    try {
      const quote = JSON.parse(window.NativeBridge.getPrice());
      $("price-value").textContent = fmtUsd(quote.price);
      const badge = $("price-source");
      badge.textContent = quote.source === "live" ? `cours réel · ${quote.provider}` : "simulé (hors ligne)";
      badge.className = `badge ${quote.source}`;
    } catch (err) {
      $("price-source").textContent = "indisponible";
    }
  }

  function refreshCandles() {
    try {
      const result = JSON.parse(window.NativeBridge.getCandles(state.timeframe, 120));
      const providerLabel = {
        "yahoo-finance": "cours réel (Yahoo Finance)",
        "simule": "données simulées",
      }[result.provider] || result.provider;
      $("chart-provider").textContent = providerLabel;
      drawCandles(result.candles);
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

  function drawCandles(candles) {
    const canvas = $("price-chart");
    const ctx = canvas.getContext("2d");
    const w = canvas.width, h = canvas.height;
    ctx.clearRect(0, 0, w, h);
    if (!candles || candles.length < 2) return;

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

  function tick() {
    refreshPrice();
  }

  (function init() {
    tick();
    refreshCandles();
    setInterval(tick, 4000);
    setInterval(refreshCandles, 15000);
  })();
})();
