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
      "frustrée", "envie de rien", "envies de rien", "aucune envie", "plus goût à rien", "à quoi bon",
      "sans motivation", "démotivé", "démotivée", "vide",
    ],
  };

  function stripAccents(s) {
    return s.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
  }

  // N\u00e9gations fran\u00e7aises courantes : un mot par ailleurs positif ("bien",
  // "motiv\u00e9"...) pr\u00e9c\u00e9d\u00e9 de pr\u00e8s par l'une d'elles change de sens ("je ne
  // me sens pas bien" n'est pas un signal positif). On regarde une petite
  // fen\u00eatre de texte juste avant chaque occurrence plut\u00f4t que d'exiger une
  // grammaire compl\u00e8te \u2014 suffisant pour les tournures les plus fr\u00e9quentes.
  const NEGATION_MARKERS = ["ne", "n'", "pas", "jamais", "aucun", "aucune"];
  const NEGATION_WINDOW = 24;

  function isNegated(normalizedText, matchIndex) {
    const windowStart = Math.max(0, matchIndex - NEGATION_WINDOW);
    const window = normalizedText.slice(windowStart, matchIndex);
    return NEGATION_MARKERS.some((n) => new RegExp(`(?:^|[^a-z])${n}(?:$|[^a-z'])`).test(window));
  }

  // Rep\u00e8re les occurrences d'une liste de mots/expressions dans le texte, en
  // s\u00e9parant celles pr\u00e9c\u00e9d\u00e9es d'une n\u00e9gation (ex. "pas bien") de celles qui
  // ne le sont pas \u2014 un mot n\u00e9g\u00e9 compte pour la cat\u00e9gorie oppos\u00e9e.
  function findOccurrences(text, words) {
    const normalized = stripAccents(text.toLowerCase());
    const affirmed = [];
    const negated = [];
    for (const w of words) {
      const needle = stripAccents(w.toLowerCase());
      const re = new RegExp(`(?:^|[^a-z0-9])${needle.replace(/[.*+?^${}()|[\]\\]/g, "\\$&")}(?:$|[^a-z0-9])`, "g");
      let m;
      while ((m = re.exec(normalized)) !== null) {
        (isNegated(normalized, m.index) ? negated : affirmed).push(w);
      }
    }
    return { affirmed, negated };
  }

  function clamp(v) {
    return Math.max(0, Math.min(100, Math.round(v)));
  }

  function computeScores(transcript, durationSec, wordCount) {
    const text = transcript || "";
    const stress = findOccurrences(text, LEXICON.stress);
    const calm = findOccurrences(text, LEXICON.calm);
    const fatigue = findOccurrences(text, LEXICON.fatigue);
    const energy = findOccurrences(text, LEXICON.energy);
    const positive = findOccurrences(text, LEXICON.positive);
    const negative = findOccurrences(text, LEXICON.negative);

    // Un mot n\u00e9g\u00e9 bascule vers la cat\u00e9gorie oppos\u00e9e ("pas stress\u00e9" -> calme).
    const stressCount = stress.affirmed.length + calm.negated.length;
    const calmCount = calm.affirmed.length + stress.negated.length;
    const fatigueCount = fatigue.affirmed.length + energy.negated.length;
    const energyCount = energy.affirmed.length + fatigue.negated.length;
    const positiveCount = positive.affirmed.length + negative.negated.length;
    const negativeCount = negative.affirmed.length + positive.negated.length;

    const wpm = durationSec > 0 ? (wordCount / durationSec) * 60 : 0;
    const paceFast = wpm > 150 ? Math.min(6, (wpm - 150) / 10) : 0;
    const paceSlow = wpm > 0 && wpm < 80 ? Math.min(6, (80 - wpm) / 8) : 0;

    let stressScore = 50 + stressCount * 9 - calmCount * 7 + paceFast;
    let energyScore = 50 + energyCount * 9 - fatigueCount * 7 - paceSlow + paceFast * 0.5;
    let fatigueScore = 50 + fatigueCount * 9 - energyCount * 7 + paceSlow;
    let moodScore = 50 + positiveCount * 9 - negativeCount * 9;

    const keywordTags = [];
    [stress, calm, fatigue, energy, positive, negative].forEach((cat) => {
      keywordTags.push(...cat.affirmed);
      keywordTags.push(...cat.negated.map((w) => `pas ${w}`));
    });
    const uniqueKeywords = [...new Set(keywordTags)];
    const hasSignal = keywordTags.length > 0;

    return {
      stress: clamp(stressScore),
      energy: clamp(energyScore),
      fatigue: clamp(fatigueScore),
      mood: clamp(moodScore),
      keywords: uniqueKeywords,
      hasSignal,
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
    veryLowMood: "Si ce sentiment persiste ou devient lourd à porter, en parler à quelqu'un de confiance ou à un professionnel de santé peut vraiment aider. Écho est un outil de bien-être, pas un substitut à un accompagnement professionnel.",
  };

  function getSuggestions(scores) {
    const picks = [];
    if (scores.stress >= 65) picks.push({ priority: scores.stress, text: SUGGESTIONS.stress });
    if (scores.fatigue >= 65) picks.push({ priority: scores.fatigue, text: SUGGESTIONS.fatigue });
    if (scores.mood <= 35) picks.push({ priority: 100 - scores.mood, text: SUGGESTIONS.lowMood });
    if (scores.energy <= 35) picks.push({ priority: 100 - scores.energy, text: SUGGESTIONS.lowEnergy });

    let result;
    if (!picks.length) {
      result = [SUGGESTIONS.positive];
    } else {
      picks.sort((a, b) => b.priority - a.priority);
      result = [...new Set(picks.map((p) => p.text))].slice(0, 2);
    }

    // Note bienveillante, toujours affichée en plus (pas soumise à la limite
    // de 2 suggestions) quand l'humeur détectée est particulièrement basse.
    if (scores.mood <= 20) {
      result = [...result, SUGGESTIONS.veryLowMood];
    }
    return result;
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

  // Filet de sécurité local : quelques tournures explicites de détresse
  // aiguë. Volontairement restreint (peu de faux positifs) et volontairement
  // 100% local — ce signal doit rester fiable même sans réseau ni API.
  const CRISIS_PHRASES = [
    "envie de mourir", "je veux mourir", "en finir avec tout", "en finir avec ma vie",
    "me suicider", "plus envie de vivre", "ne plus vivre", "me faire du mal",
    "me tuer", "en finir avec la vie",
  ];

  function detectCrisisSignal(transcript) {
    const normalized = stripAccents((transcript || "").toLowerCase());
    return CRISIS_PHRASES.some((p) => normalized.includes(stripAccents(p)));
  }

  return {
    computeScores,
    generateInsights,
    generateWeeklySummary,
    getSuggestions,
    detectCrisisSignal,
    WEEKDAYS,
  };
})();
