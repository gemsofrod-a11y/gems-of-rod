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

  function groupByWeekday(sorted, field) {
    const byWeekday = {};
    sorted.forEach((e) => {
      const wd = new Date(e.date).getDay();
      (byWeekday[wd] = byWeekday[wd] || []).push(e.scores[field]);
    });
    return Object.entries(byWeekday)
      .filter(([, vals]) => vals.length >= 2)
      .map(([wd, vals]) => ({ wd: Number(wd), avg: average(vals) }));
  }

  function generateInsights(entries) {
    const insights = [];
    if (entries.length < 3) {
      return insights;
    }

    const sorted = [...entries].sort((a, b) => new Date(a.date) - new Date(b.date));

    // 1. Motif par jour de la semaine (stress).
    const weekdayAverages = groupByWeekday(sorted, "stress");

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

  const SUGGESTIONS = {
    stress: "Essaie une respiration 4-4-6 : 4 secondes d'inspiration, 4 de rétention, 6 d'expiration, pendant 2 minutes.",
    fatigue: "Une courte sieste (15-20 min) ou une nuit de sommeil en avance pourrait t'aider.",
    lowMood: "Une marche de 10 minutes dehors, ou appeler quelqu'un de proche, peut faire du bien.",
    lowEnergy: "Quelques étirements, un verre d'eau et une pause loin de l'écran peuvent relancer l'énergie.",
    positive: "Continue comme ça : prends un instant pour savourer ce qui a bien fonctionné aujourd'hui.",
  };

  function getSuggestions(scores) {
    const picks = [];
    if (scores.stress >= 65) picks.push({ priority: scores.stress, text: SUGGESTIONS.stress });
    if (scores.fatigue >= 65) picks.push({ priority: scores.fatigue, text: SUGGESTIONS.fatigue });
    if (scores.mood <= 35) picks.push({ priority: 100 - scores.mood, text: SUGGESTIONS.lowMood });
    if (scores.energy <= 35) picks.push({ priority: 100 - scores.energy, text: SUGGESTIONS.lowEnergy });

    if (!picks.length) {
      return [SUGGESTIONS.positive];
    }
    picks.sort((a, b) => b.priority - a.priority);
    const texts = [...new Set(picks.map((p) => p.text))];
    return texts.slice(0, 2);
  }

  function bucket(value, lowLabel, midLabel, highLabel) {
    if (value <= 40) return lowLabel;
    if (value >= 60) return highLabel;
    return midLabel;
  }

  function generateWeeklySummary(entries) {
    if (entries.length < 3) return null;

    const sorted = [...entries].sort((a, b) => new Date(a.date) - new Date(b.date));
    const week = sorted.slice(-7);
    const prior = sorted.slice(-14, -7);

    const avgEnergy = average(week.map((e) => e.scores.energy));
    const avgStress = average(week.map((e) => e.scores.stress));
    const avgFatigue = average(week.map((e) => e.scores.fatigue));
    const avgMood = average(week.map((e) => e.scores.mood));

    const sentences = [];
    sentences.push(
      `Sur ${week.length} enregistrement${week.length > 1 ? "s" : ""} cette semaine, ton énergie a été plutôt ${bucket(avgEnergy, "basse", "moyenne", "élevée")} et ton niveau de stress plutôt ${bucket(avgStress, "faible", "modéré", "élevé")}.`
    );

    if (prior.length >= 2) {
      const diffEnergy = avgEnergy - average(prior.map((e) => e.scores.energy));
      const diffStress = avgStress - average(prior.map((e) => e.scores.stress));
      const clauses = [];
      if (diffEnergy >= 7) clauses.push("ton énergie est en hausse");
      else if (diffEnergy <= -7) clauses.push("ton énergie est en baisse");
      if (diffStress >= 7) clauses.push("ton stress a augmenté");
      else if (diffStress <= -7) clauses.push("ton stress a diminué");
      if (clauses.length) {
        sentences.push(`Par rapport à la semaine précédente, ${clauses.join(" et ")}.`);
      }
    }

    if (avgFatigue >= 60) {
      sentences.push("Tu sembles avoir accumulé de la fatigue ces derniers jours.");
    }
    if (avgMood <= 40) {
      sentences.push("Ton humeur générale a semblé plus difficile que d'habitude.");
    } else if (avgMood >= 70) {
      sentences.push("Ton humeur générale est restée plutôt positive.");
    }

    return sentences.join(" ");
  }

  return { computeScores, generateInsights, generateWeeklySummary, getSuggestions, WEEKDAYS };
})();
