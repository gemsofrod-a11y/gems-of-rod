// Rendu de graphiques simples en Canvas, sans dépendance externe.
const Charts = (() => {
  function getCssVar(name) {
    return getComputedStyle(document.documentElement).getPropertyValue(name).trim();
  }

  function setupCanvas(canvas) {
    const dpr = window.devicePixelRatio || 1;
    const cssWidth = canvas.clientWidth || canvas.width;
    const cssHeight = canvas.clientHeight || canvas.height;
    canvas.width = cssWidth * dpr;
    canvas.height = cssHeight * dpr;
    const ctx = canvas.getContext("2d");
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    return { ctx, width: cssWidth, height: cssHeight };
  }

  function drawTimeline(canvas, entries) {
    const { ctx, width, height } = setupCanvas(canvas);
    ctx.clearRect(0, 0, width, height);

    const pad = { top: 16, right: 16, bottom: 24, left: 16 };
    const plotW = width - pad.left - pad.right;
    const plotH = height - pad.top - pad.bottom;

    if (entries.length === 0) {
      drawEmptyMessage(ctx, width, height, "Pas encore de données");
      return;
    }

    const points = entries.slice(-14);
    const n = points.length;
    const stepX = n > 1 ? plotW / (n - 1) : 0;
    const textColor = getCssVar("--text-muted") || "#888";

    // Lignes de repère horizontales (0 / 50 / 100).
    ctx.strokeStyle = getCssVar("--border") || "#eee";
    ctx.lineWidth = 1;
    [0, 50, 100].forEach((v) => {
      const y = pad.top + plotH - (v / 100) * plotH;
      ctx.beginPath();
      ctx.moveTo(pad.left, y);
      ctx.lineTo(pad.left + plotW, y);
      ctx.stroke();
    });

    const energyColor = getCssVar("--energy") || "#4fd1a5";
    const stressColor = getCssVar("--stress") || "#ff7a7a";

    drawLine(ctx, points, "energy", pad, plotW, plotH, stepX, energyColor);
    drawLine(ctx, points, "stress", pad, plotW, plotH, stepX, stressColor);

    // Labels de dates (premier, milieu, dernier).
    ctx.fillStyle = textColor;
    ctx.font = "11px sans-serif";
    ctx.textAlign = "center";
    [0, Math.floor((n - 1) / 2), n - 1].forEach((i) => {
      if (i < 0 || i >= n) return;
      const x = pad.left + i * stepX;
      const d = new Date(points[i].date);
      const label = `${d.getDate()}/${d.getMonth() + 1}`;
      ctx.fillText(label, x, height - 6);
    });
  }

  function drawLine(ctx, points, field, pad, plotW, plotH, stepX, color) {
    ctx.strokeStyle = color;
    ctx.lineWidth = 2.5;
    ctx.lineJoin = "round";
    ctx.beginPath();
    points.forEach((p, i) => {
      const x = pad.left + i * stepX;
      const v = p.scores[field];
      const y = pad.top + plotH - (v / 100) * plotH;
      if (i === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    });
    ctx.stroke();

    ctx.fillStyle = color;
    points.forEach((p, i) => {
      const x = pad.left + i * stepX;
      const v = p.scores[field];
      const y = pad.top + plotH - (v / 100) * plotH;
      ctx.beginPath();
      ctx.arc(x, y, 3, 0, Math.PI * 2);
      ctx.fill();
    });
  }

  function drawWeekdayBars(canvas, entries) {
    const { ctx, width, height } = setupCanvas(canvas);
    ctx.clearRect(0, 0, width, height);

    if (entries.length === 0) {
      drawEmptyMessage(ctx, width, height, "Pas encore de données");
      return;
    }

    const pad = { top: 16, right: 16, bottom: 24, left: 16 };
    const plotW = width - pad.left - pad.right;
    const plotH = height - pad.top - pad.bottom;

    const byWeekday = [[], [], [], [], [], [], []];
    entries.forEach((e) => {
      const wd = new Date(e.date).getDay();
      byWeekday[wd].push(e.scores.stress);
    });
    const order = [1, 2, 3, 4, 5, 6, 0]; // lundi -> dimanche
    const labels = ["L", "M", "M", "J", "V", "S", "D"];
    const avgs = order.map((wd) => {
      const vals = byWeekday[wd];
      return vals.length ? vals.reduce((a, b) => a + b, 0) / vals.length : null;
    });

    const barW = plotW / order.length;
    const stressColor = getCssVar("--stress") || "#ff7a7a";
    const borderColor = getCssVar("--border") || "#eee";

    order.forEach((wd, i) => {
      const x = pad.left + i * barW + barW * 0.2;
      const w = barW * 0.6;
      const v = avgs[i];
      if (v === null) {
        ctx.fillStyle = borderColor;
        ctx.fillRect(x, pad.top + plotH - 2, w, 2);
      } else {
        const h = (v / 100) * plotH;
        ctx.fillStyle = stressColor;
        ctx.fillRect(x, pad.top + plotH - h, w, h);
      }
      ctx.fillStyle = getCssVar("--text-muted") || "#888";
      ctx.font = "11px sans-serif";
      ctx.textAlign = "center";
      ctx.fillText(labels[i], x + w / 2, height - 6);
    });
  }

  function drawEmptyMessage(ctx, width, height, text) {
    ctx.fillStyle = getCssVar("--text-muted") || "#888";
    ctx.font = "13px sans-serif";
    ctx.textAlign = "center";
    ctx.fillText(text, width / 2, height / 2);
  }

  // Piste audio du jour : une barre par segment temporel, hauteur = énergie
  // sonore relative, segments de pause visuellement estompés.
  function drawWaveform(canvas, envelope, pauseMask) {
    const { ctx, width, height } = setupCanvas(canvas);
    ctx.clearRect(0, 0, width, height);

    if (!envelope || !envelope.length) {
      drawEmptyMessage(ctx, width, height, "Pas de piste audio pour cet enregistrement");
      return;
    }

    const pad = { top: 10, right: 4, bottom: 4, left: 4 };
    const plotW = width - pad.left - pad.right;
    const plotH = height - pad.top - pad.bottom;
    const n = envelope.length;
    const gap = 2;
    const barW = Math.max(1, plotW / n - gap);

    const voiceColor = getCssVar("--primary") || "#6f8cff";
    const pauseColor = getCssVar("--border") || "#e7e3f5";
    const minBarH = 3;

    for (let i = 0; i < n; i++) {
      const x = pad.left + i * (barW + gap);
      const isPause = pauseMask && pauseMask[i];
      const h = Math.max(minBarH, envelope[i] * plotH);
      const y = pad.top + plotH - h;
      ctx.fillStyle = isPause ? pauseColor : voiceColor;
      ctx.globalAlpha = isPause ? 0.6 : 1;
      const r = Math.min(2, barW / 2);
      roundedRect(ctx, x, y, barW, h, r);
      ctx.fill();
    }
    ctx.globalAlpha = 1;
  }

  function roundedRect(ctx, x, y, w, h, r) {
    ctx.beginPath();
    ctx.moveTo(x + r, y);
    ctx.arcTo(x + w, y, x + w, y + h, r);
    ctx.arcTo(x + w, y + h, x, y + h, r);
    ctx.arcTo(x, y + h, x, y, r);
    ctx.arcTo(x, y, x + w, y, r);
    ctx.closePath();
  }

  return { drawTimeline, drawWeekdayBars, drawWaveform };
})();
