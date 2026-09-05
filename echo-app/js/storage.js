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

  // Fusionne `patch` dans l'entrée `id` déjà enregistrée — utilisé pour
  // attacher des données calculées après coup (ex. l'analyse audio, qui
  // prend un instant et ne doit pas retarder l'affichage du résumé).
  function updateEntry(id, patch) {
    const list = getEntries();
    const index = list.findIndex((e) => e.id === id);
    if (index === -1) return null;
    list[index] = { ...list[index], ...patch };
    localStorage.setItem(KEY, JSON.stringify(list));
    return list[index];
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

  return { getEntries, saveEntry, updateEntry, clearAll, exportJSON, importJSON };
})();
