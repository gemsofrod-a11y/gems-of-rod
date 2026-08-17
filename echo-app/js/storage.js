// Persistance locale des journaux vocaux (localStorage uniquement, rien ne sort de l'appareil).
const Storage = (() => {
  const KEY = "echo_entries_v1";

  function getEntries() {
    try {
      const raw = localStorage.getItem(KEY);
      const list = raw ? JSON.parse(raw) : [];
      return Array.isArray(list) ? list : [];
    } catch (e) {
      console.error("Écho: lecture du stockage impossible", e);
      return [];
    }
  }

  function saveEntry(entry) {
    const list = getEntries();
    list.push(entry);
    list.sort((a, b) => new Date(a.date) - new Date(b.date));
    localStorage.setItem(KEY, JSON.stringify(list));
    return list;
  }

  function clearAll() {
    localStorage.removeItem(KEY);
  }

  function exportJSON() {
    return JSON.stringify({ version: 1, exportedAt: new Date().toISOString(), entries: getEntries() }, null, 2);
  }

  function importJSON(text) {
    const parsed = JSON.parse(text);
    const entries = Array.isArray(parsed) ? parsed : parsed.entries;
    if (!Array.isArray(entries)) throw new Error("Format de fichier invalide");
    localStorage.setItem(KEY, JSON.stringify(entries));
    return entries;
  }

  return { getEntries, saveEntry, clearAll, exportJSON, importJSON };
})();
