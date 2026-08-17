// Analyse 100% locale et gratuite : détection de mots-clés + rythme de parole.
// Aucune donnée ne quitte l'appareil, aucune clé API requise.
const Analysis = (() => {
  const WEEKDAYS = ["dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"];

  const LEXICON = {
    stress: [
      "stressé", "stressée", "stress", "anxieux", "anxieuse", "angoissé", "angoissée",
      "tendu", "tendue", "sous pression", "inquiet", "inquiète", "nerveux", "nerveuse",
      "débordé", "débordée", "surchargé", "surchargée", "panique", "speed", "overbooké",
    ],
    calm: [
      "calme", "détendu", "détendue", "serein", "sereine", "tranquille", "reposé",
      "reposée", "zen", "apaisé", "apaisée",
    ],
    fatigue: [
      "fatigué", "fatiguée", "épuisé", "épuisée", "crevé", "crevée", "à plat",
      "vidé", "vidée", "sommeil", "dormi", "insomnie", "naze", "lessivé", "lessivée",
    ],
    energy: [
      "motivé", "motivée", "énergique", "en forme", "productif", "productive",
      "dynamique", "top forme", "plein d'énergie", "pleine d'énergie", "efficace",
    ],
    positive: [
      "content", "contente", "heureux", "heureuse", "bien", "super", "génial",
      "cool", "top", "satisfait", "satisfaite", "fier", "fière", "joyeux", "joyeuse",
    ],
    negative: [
      "triste", "déprimé", "déprimée", "mal", "difficile", "dur", "dure", "seul",
      "seule", "découragé", "découragée", "déçu", "déçue", "en colère", "frustré",
      "frustrée",
    ],
  };

  function stripAccents(s) {
    return s.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
  }

  function countMatches(text, words) {
    const normalized = stripAccents(text.toLowerCase());
    let hits = [];
    for (const w of words) {
      const needle = stripAccents(w.toLowerCase());
      const re = new RegExp(`(?:^|[^a-z0-9])${needle.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}(?:$|[^a-z0-9])`, "g");
      const matches = normalized.match(re);
      if (matches) hits = hits.concat(matches.map(() => w));
    }
    return hits;
  }

  function clamp(v) {
    return Math.max(0, Math.min(100, Math.round(v)));
  }

  function computeScores(transcript, durationSec, wordCount) {
    const text = transcript || "";
    const stressHits = countMatches(text, LEXICON.stress);
    const calmHits = countMatches(text, LEXICON.calm);
    const fatigueHits = countMatches(text, LEXICON.fatigue);
    const energyHits = countMatches(text, LEXICON.energy);
    const positiveHits = countMatches(text, LEXICON.positive);
    const negativeHits = countMatches(text, LEXICON.negative);

    const wpm = durationSec > 0 ? (wordCount / durationSec) * 60 : 0;
    const paceFast = wpm > 150 ? Math.min(6, (wpm - 150) / 10) : 0;
    const paceSlow = wpm > 0 && wpm < 80 ? Math.min(6, (80 - wpm) / 8) : 0;

    let stress = 50 + stressHits.length * 9 - calmHits.length * 7 + paceFast;
    let energy = 50 + energyHits.length * 9 - fatigueHits.length * 7 - paceSlow + paceFast * 0.5;
    let fatigue = 50 + fatigueHits.length * 9 - energyHits.length * 7 + paceSlow;
    let mood = 50 + positiveHits.length * 9 - negativeHits.length * 9;

    const allHits = [...stressHits, ...calmHits, ...fatigueHits, ...energyHits, ...positiveHits, ...negativeHits];
    const uniqueKeywords = [...new Set(allHits)];

    return {
      stress: clamp(stress),
      energy: clamp(energy),
      fatigue: clamp(fatigue),
      mood: clamp(mood),
      keywords: uniqueKeywords,
      hasSignal: allHits.length > 0,
      wpm: Math.round(wpm),
    };
  }

  function average(nums) {
    if (!nums.length) return null;
    return nums.reduce((a, b) => a + b, 0) / nums.length;
  }

  function generateInsights(entries) {
    const insights = [];
    if (entries.length < 3) {
      return insights;
    }

    const sorted = [...entries].sort((a, b) => new Date(a.date) - new Date(b.date));

    // 1. Motif par jour de la semaine (stress).
    const byWeekday = {};
    sorted.forEach((e) => {
      const wd = new Date(e.date).getDay();
      (byWeekday[wd] = byWeekday[wd] || []).push(e.scores.stress);
    });
    const weekdayAverages = Object.entries(byWeekday)
      .filter(([, vals]) => vals.length >= 2)
      .map(([wd, vals]) => ({ wd: Number(wd), avg: average(vals) }));

    if (weekdayAverages.length >= 2) {
      const overallAvg = average(sorted.map((e) => e.scores.stress));
      const worst = weekdayAverages.reduce((a, b) => (b.avg > a.avg ? b : a));
      if (worst.avg - overallAvg >= 8) {
        insights.push(`Tu sembles plus stressé·e le ${WEEKDAYS[worst.wd]}.`);
      }
      const best = weekdayAverages.reduce((a, b) => (b.avg < a.avg ? b : a));
      if (overallAvg - best.avg >= 8 && best.wd !== worst.wd) {
        insights.push(`Le ${WEEKDAYS[best.wd]} semble être ton jour le plus calme.`);
      }
    }

    // 2. Tendance récente de l'énergie.
    const last3 = sorted.slice(-3).map((e) => e.scores.energy);
    const prior = sorted.slice(-10, -3).map((e) => e.scores.energy);
    if (prior.length >= 2) {
      const diff = average(last3) - average(prior);
      if (diff >= 7) {
        const streak = countTrendStreak(sorted, "energy", 1);
        insights.push(`Ton énergie remonte depuis ${streak} jour${streak > 1 ? "s" : ""}.`);
      } else if (diff <= -7) {
        insights.push("Ton énergie est en baisse ces derniers jours.");
      }
    }

    // 3. Tendance récente de la fatigue.
    const last3Fatigue = sorted.slice(-3).map((e) => e.scores.fatigue);
    const priorFatigue = sorted.slice(-10, -3).map((e) => e.scores.fatigue);
    if (priorFatigue.length >= 2) {
      const diff = average(last3Fatigue) - average(priorFatigue);
      if (diff >= 7) {
        insights.push("Tu sembles plus fatigué·e que d'habitude ces derniers jours.");
      }
    }

    // 4. Régularité du journal.
    const streakDays = countJournalingStreak(sorted);
    if (streakDays >= 3) {
      insights.push(`${streakDays} jours consécutifs de journal, continue comme ça.`);
    }

    return insights.slice(0, 4);
  }

  function countTrendStreak(sorted, field, direction) {
    let streak = 1;
    for (let i = sorted.length - 1; i > 0; i--) {
      const cur = sorted[i].scores[field];
      const prev = sorted[i - 1].scores[field];
      if (direction > 0 ? cur >= prev : cur <= prev) {
        streak++;
      } else {
        break;
      }
    }
    return streak;
  }

  function countJournalingStreak(sorted) {
    const days = [...new Set(sorted.map((e) => e.date.slice(0, 10)))].sort();
    let streak = 1;
    for (let i = days.length - 1; i > 0; i--) {
      const cur = new Date(days[i]);
      const prev = new Date(days[i - 1]);
      const diffDays = Math.round((cur - prev) / 86400000);
      if (diffDays === 1) streak++;
      else break;
    }
    return streak;
  }

  return { computeScores, generateInsights, WEEKDAYS };
})();
