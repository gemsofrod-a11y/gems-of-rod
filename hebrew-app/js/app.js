/* Rendu des données + interactions (onglets, clic sur mot, prononciation). */

let hebrewVoice = null;

function loadVoices() {
  const voices = window.speechSynthesis ? window.speechSynthesis.getVoices() : [];
  hebrewVoice = voices.find(v => v.lang && v.lang.toLowerCase().startsWith("he")) || null;
}

function speak(text) {
  if (!("speechSynthesis" in window)) {
    const note = document.getElementById("audio-note");
    if (note) note.hidden = false;
    return;
  }
  window.speechSynthesis.cancel();
  const utter = new SpeechSynthesisUtterance(text);
  utter.lang = "he-IL";
  utter.rate = 0.8;
  if (hebrewVoice) utter.voice = hebrewVoice;
  window.speechSynthesis.speak(utter);
}

function speakerButton(text, label) {
  const btn = document.createElement("button");
  btn.type = "button";
  btn.className = "speak-btn";
  btn.setAttribute("aria-label", label || "Écouter la prononciation");
  btn.innerHTML = '<svg viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path fill="currentColor" d="M3 9v6h4l5 5V4L7 9H3zm13.5 3a4.5 4.5 0 0 0-2.5-4.03v8.06A4.5 4.5 0 0 0 16.5 12zM14 3.23v2.06c3 .82 5 3.6 5 6.71s-2 5.89-5 6.71v2.06c4.01-.86 7-4.42 7-8.77s-2.99-7.91-7-8.77z"/></svg>';
  btn.addEventListener("click", (e) => {
    e.stopPropagation();
    speak(text);
  });
  return btn;
}

/* ---------- Alphabet ---------- */
function renderAlphabet() {
  const grid = document.getElementById("alphabet-grid");
  ALPHABET.forEach(item => {
    const card = document.createElement("div");
    card.className = "card letter-card";

    const letterEl = document.createElement("div");
    letterEl.className = "letter-big he";
    letterEl.textContent = item.letter;
    card.appendChild(letterEl);

    const name = document.createElement("div");
    name.className = "letter-name";
    name.textContent = `${item.name} — ${item.translit}`;
    card.appendChild(name);

    const desc = document.createElement("p");
    desc.className = "letter-desc";
    desc.textContent = item.desc;
    card.appendChild(desc);

    const example = document.createElement("div");
    example.className = "letter-example";
    const exHe = document.createElement("span");
    exHe.className = "he";
    exHe.textContent = item.example.he;
    const exText = document.createElement("span");
    exText.className = "example-text";
    exText.textContent = ` ${item.example.translit} — ${item.example.fr}`;
    example.appendChild(exHe);
    example.appendChild(exText);
    example.appendChild(speakerButton(item.example.he, `Écouter ${item.example.translit}`));
    card.appendChild(example);

    grid.appendChild(card);
  });
}

/* ---------- Vocabulaire de base ---------- */
function renderVocab() {
  const container = document.getElementById("vocab-container");
  VOCAB_CATEGORIES.forEach(cat => {
    const section = document.createElement("div");
    section.className = "vocab-category";

    const title = document.createElement("h3");
    title.textContent = cat.title;
    section.appendChild(title);

    const grid = document.createElement("div");
    grid.className = "grid vocab-grid";

    cat.words.forEach(w => {
      const card = document.createElement("div");
      card.className = "card word-card";

      const he = document.createElement("div");
      he.className = "he word-he";
      he.textContent = w.he;
      card.appendChild(he);

      const translit = document.createElement("div");
      translit.className = "word-translit";
      translit.textContent = w.translit;
      card.appendChild(translit);

      const fr = document.createElement("div");
      fr.className = "word-fr";
      fr.textContent = w.fr;
      card.appendChild(fr);

      card.appendChild(speakerButton(w.he, `Écouter ${w.translit}`));

      grid.appendChild(card);
    });

    section.appendChild(grid);
    container.appendChild(section);
  });
}

/* ---------- Phrases ---------- */
function renderPhrases() {
  const container = document.getElementById("phrases-container");

  PHRASES.forEach(phrase => {
    const card = document.createElement("div");
    card.className = "card phrase-card";

    const header = document.createElement("div");
    header.className = "phrase-header";

    const level = document.createElement("span");
    level.className = "badge";
    level.textContent = phrase.level;
    header.appendChild(level);

    const fullHe = phrase.words.map(w => w.he).join(" ");
    header.appendChild(speakerButton(fullHe, "Écouter la phrase"));

    card.appendChild(header);

    const sentence = document.createElement("div");
    sentence.className = "he phrase-sentence";

    phrase.words.forEach((w, idx) => {
      const span = document.createElement("span");
      span.className = "clickable-word";
      span.textContent = w.he;
      span.tabIndex = 0;
      span.setAttribute("role", "button");
      span.setAttribute("aria-expanded", "false");

      const tooltip = document.createElement("span");
      tooltip.className = "word-tooltip";
      tooltip.hidden = true;

      const translitLine = document.createElement("span");
      translitLine.className = "tooltip-translit";
      translitLine.textContent = w.translit;
      tooltip.appendChild(translitLine);

      const frLine = document.createElement("span");
      frLine.className = "tooltip-fr";
      frLine.textContent = w.fr;
      tooltip.appendChild(frLine);

      tooltip.appendChild(speakerButton(w.he, `Écouter ${w.translit}`));

      span.appendChild(tooltip);

      const toggle = () => {
        const isOpen = !tooltip.hidden;
        document.querySelectorAll(".word-tooltip").forEach(t => (t.hidden = true));
        document.querySelectorAll(".clickable-word").forEach(w2 => w2.setAttribute("aria-expanded", "false"));
        if (!isOpen) {
          tooltip.hidden = false;
          span.setAttribute("aria-expanded", "true");
        }
      };

      span.addEventListener("click", (e) => {
        e.stopPropagation();
        toggle();
      });
      span.addEventListener("keydown", (e) => {
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          toggle();
        }
      });

      sentence.appendChild(span);
      if (idx < phrase.words.length - 1) sentence.appendChild(document.createTextNode(" "));
    });

    card.appendChild(sentence);

    const fr = document.createElement("p");
    fr.className = "phrase-fr";
    fr.textContent = phrase.fr;
    card.appendChild(fr);

    const hint = document.createElement("p");
    hint.className = "phrase-hint";
    hint.textContent = "Cliquez sur un mot pour voir sa phonétique.";
    card.appendChild(hint);

    container.appendChild(card);
  });

  document.addEventListener("click", () => {
    document.querySelectorAll(".word-tooltip").forEach(t => (t.hidden = true));
    document.querySelectorAll(".clickable-word").forEach(w => w.setAttribute("aria-expanded", "false"));
  });
}

/* ---------- Onglets ---------- */
function setupTabs() {
  const tabs = document.querySelectorAll(".tab-btn");
  const panels = document.querySelectorAll(".tab-panel");

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      tabs.forEach(t => t.classList.remove("active"));
      panels.forEach(p => p.classList.remove("active"));
      tab.classList.add("active");
      document.getElementById(tab.dataset.target).classList.add("active");
    });
  });
}

document.addEventListener("DOMContentLoaded", () => {
  renderAlphabet();
  renderVocab();
  renderPhrases();
  setupTabs();

  if ("speechSynthesis" in window) {
    loadVoices();
    window.speechSynthesis.onvoiceschanged = loadVoices;
  } else {
    const note = document.getElementById("audio-note");
    if (note) note.hidden = false;
  }
});
