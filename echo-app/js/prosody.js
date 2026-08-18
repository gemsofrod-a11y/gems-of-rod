// Analyse locale du rythme de parole à partir de la piste audio : pauses,
// pics de volume, régularité du débit. Volontairement limité à des mesures
// de signal (énergie sonore dans le temps) — pas de détection d'émotion à
// partir du ton, qui ne serait pas fiable sans un modèle d'IA dédié.
const Prosody = (() => {
  const WINDOW_MS = 50;
  const PAUSE_MIN_MS = 300;
  const TARGET_BARS = 72;

  function isSupported() {
    return !!(window.AudioContext || window.webkitAudioContext);
  }

  async function analyze(blob) {
    if (!blob || !isSupported()) return null;
    try {
      const AudioContextCtor = window.AudioContext || window.webkitAudioContext;
      const ctx = new AudioContextCtor();
      const arrayBuffer = await blob.arrayBuffer();
      const audioBuffer = await ctx.decodeAudioData(arrayBuffer);
      const result = computeMetrics(audioBuffer);
      ctx.close && ctx.close();
      return result;
    } catch (e) {
      return null;
    }
  }

  function computeMetrics(audioBuffer) {
    const data = audioBuffer.getChannelData(0);
    const sampleRate = audioBuffer.sampleRate;
    const windowSize = Math.max(1, Math.round((WINDOW_MS / 1000) * sampleRate));
    const windowCount = Math.ceil(data.length / windowSize);

    const rms = new Array(windowCount);
    let maxRms = 0;
    for (let i = 0; i < windowCount; i++) {
      const start = i * windowSize;
      const end = Math.min(data.length, start + windowSize);
      let sumSq = 0;
      for (let j = start; j < end; j++) sumSq += data[j] * data[j];
      const value = Math.sqrt(sumSq / (end - start));
      rms[i] = value;
      if (value > maxRms) maxRms = value;
    }

    if (maxRms === 0) {
      return null; // piste silencieuse ou vide, rien d'exploitable
    }

    const silenceThreshold = maxRms * 0.15;
    const peakThreshold = maxRms * 0.7;
    const minPauseWindows = Math.ceil(PAUSE_MIN_MS / WINDOW_MS);

    // Pauses : suites de fenêtres sous le seuil de silence, assez longues.
    let pauseCount = 0;
    let longestPauseMs = 0;
    let totalSilenceMs = 0;
    let run = 0;
    for (let i = 0; i < windowCount; i++) {
      if (rms[i] < silenceThreshold) {
        run++;
      } else {
        if (run >= minPauseWindows) {
          pauseCount++;
          totalSilenceMs += run * WINDOW_MS;
          longestPauseMs = Math.max(longestPauseMs, run * WINDOW_MS);
        }
        run = 0;
      }
    }
    if (run >= minPauseWindows) {
      pauseCount++;
      totalSilenceMs += run * WINDOW_MS;
      longestPauseMs = Math.max(longestPauseMs, run * WINDOW_MS);
    }

    // Pics : maxima locaux au-dessus du seuil, espacés d'au moins 300ms
    // pour ne pas compter plusieurs fois la même emphase vocale.
    let peakCount = 0;
    let lastPeakIndex = -Infinity;
    for (let i = 1; i < windowCount - 1; i++) {
      if (
        rms[i] >= peakThreshold &&
        rms[i] >= rms[i - 1] &&
        rms[i] >= rms[i + 1] &&
        i - lastPeakIndex >= minPauseWindows
      ) {
        peakCount++;
        lastPeakIndex = i;
      }
    }

    const durationMs = Math.round((data.length / sampleRate) * 1000);
    const speakingRatio = durationMs > 0 ? Math.max(0, 1 - totalSilenceMs / durationMs) : 1;

    // Sous-échantillonnage pour l'affichage (moyenne par groupe de fenêtres).
    const groupSize = Math.max(1, Math.ceil(windowCount / TARGET_BARS));
    const envelope = [];
    const pauseMask = [];
    for (let i = 0; i < windowCount; i += groupSize) {
      const group = rms.slice(i, i + groupSize);
      const avg = group.reduce((a, b) => a + b, 0) / group.length;
      envelope.push(Math.min(1, avg / maxRms));
      pauseMask.push(avg < silenceThreshold);
    }

    return {
      envelope,
      pauseMask,
      pauseCount,
      longestPauseMs,
      totalSilenceMs,
      peakCount,
      speakingRatio,
      durationMs,
    };
  }

  return { isSupported, analyze };
})();
