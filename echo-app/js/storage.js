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

  // Fusionne par id plutôt que de remplacer : importer une sauvegarde plus
  // ancienne ne doit jamais effacer les journaux enregistrés depuis. En cas
  // d'id déjà présent, la version locale est conservée.
  function importJSON(text) {
    const parsed = JSON.parse(text);
    const imported = Array.isArray(parsed) ? parsed : parsed && parsed.entries;
    if (!Array.isArray(imported)) throw new Error("Format de fichier invalide");
    const list = getEntries();
    const knownIds = new Set(list.map((e) => e.id));
    const added = [];
    let skipped = 0;
    for (const e of imported) {
      if (!e || typeof e.id !== "string" || !e.date || isNaN(new Date(e.date)) || knownIds.has(e.id)) {
        skipped++;
        continue;
      }
      knownIds.add(e.id);
      list.push(e);
      added.push(e);
    }
    list.sort((a, b) => new Date(a.date) - new Date(b.date));
    localStorage.setItem(KEY, JSON.stringify(list));
    return { added, skipped };
  }

  return { getEntries, saveEntry, updateEntry, clearAll, exportJSON, importJSON };
})();
