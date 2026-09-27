// Émotions précises choisies par l'utilisateur (1 ou 2 par entrée,
// facultatif) : nommer finement ce qu'on ressent, plutôt que de se limiter
// aux quatre scores déduits des mots-clés. Stockées par id sur l'entrée
// (`entry.emotions`), jamais déduites automatiquement.
const Emotions = (() => {
  const LIST = [
    { id: "joie", label: "Joie", emoji: "😊", tone: "pleasant" },
    { id: "fierte", label: "Fierté", emoji: "💪", tone: "pleasant" },
    { id: "gratitude", label: "Gratitude", emoji: "🙏", tone: "pleasant" },
    { id: "serenite", label: "Sérénité", emoji: "😌", tone: "pleasant" },
    { id: "soulagement", label: "Soulagement", emoji: "😮‍💨", tone: "pleasant" },
    { id: "motivation", label: "Motivation", emoji: "✨", tone: "pleasant" },
    { id: "anxiete", label: "Anxiété", emoji: "😰", tone: "difficult" },
    { id: "tristesse", label: "Tristesse", emoji: "😢", tone: "difficult" },
    { id: "colere", label: "Colère", emoji: "😠", tone: "difficult" },
    { id: "frustration", label: "Frustration", emoji: "😤", tone: "difficult" },
    { id: "solitude", label: "Solitude", emoji: "😶", tone: "difficult" },
    { id: "decouragement", label: "Découragement", emoji: "😞", tone: "difficult" },
  ];
  const MAX_SELECTED = 2;
  const BY_ID = new Map(LIST.map((e) => [e.id, e]));

  function get(id) {
    return BY_ID.get(id) || null;
  }

  function sanitize(ids) {
    if (!Array.isArray(ids)) return [];
    return [...new Set(ids.filter((id) => BY_ID.has(id)))].slice(0, MAX_SELECTED);
  }

  function labels(ids) {
    return sanitize(ids).map((id) => get(id).label.toLocaleLowerCase("fr-FR"));
  }

  // Rangée de puces cliquables. Au-delà de MAX_SELECTED, choisir une
  // nouvelle émotion remplace la plus ancienne plutôt que d'être refusé.
  function renderPicker(container, selectedIds, onChange) {
    let selected = sanitize(selectedIds);
    container.innerHTML = "";
    container.setAttribute("role", "group");
    for (const emo of LIST) {
      const btn = document.createElement("button");
      btn.type = "button";
      btn.className = `emotion-chip emotion-chip-${emo.tone}`;
      btn.dataset.emotion = emo.id;
      btn.innerHTML = `<span aria-hidden="true">${emo.emoji}</span> `;
      btn.append(emo.label);
      container.appendChild(btn);
    }
    const sync = () => {
      container.querySelectorAll(".emotion-chip").forEach((btn) => {
        const on = selected.includes(btn.dataset.emotion);
        btn.classList.toggle("emotion-chip-selected", on);
        btn.setAttribute("aria-pressed", on ? "true" : "false");
      });
    };
    container.onclick = (e) => {
      const btn = e.target.closest(".emotion-chip");
      if (!btn) return;
      const id = btn.dataset.emotion;
      if (selected.includes(id)) {
        selected = selected.filter((x) => x !== id);
      } else {
        selected = [...selected, id].slice(-MAX_SELECTED);
      }
      sync();
      onChange([...selected]);
    };
    sync();
  }

  function frequencies(entries, sinceDays) {
    const since = Date.now() - sinceDays * 24 * 60 * 60 * 1000;
    const counts = new Map();
    for (const e of entries) {
      if (new Date(e.date).getTime() < since) continue;
      for (const id of sanitize(e.emotions)) counts.set(id, (counts.get(id) || 0) + 1);
    }
    return [...counts.entries()]
      .map(([id, count]) => ({ ...get(id), count }))
      .sort((a, b) => b.count - a.count || LIST.indexOf(get(a.id)) - LIST.indexOf(get(b.id)));
  }

  return { LIST, MAX_SELECTED, get, sanitize, labels, renderPicker, frequencies };
})();
