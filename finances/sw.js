// Budget Clair : met l'appli en cache pour qu'elle s'ouvre sans connexion.
// Aucune donnée bancaire ne transite ici : le relevé est lu dans la page et reste dans le navigateur.
const CACHE = "budget-clair-v3";
const SHARE = "budget-clair-share";
const FILES = ["./", "index.html", "manifest.webmanifest", "icon.svg"];
// Lecteur de PDF (pdf.js), téléchargé à l'installation pour lire les relevés PDF hors-ligne.
const PDFJS = "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/";
const LIBS = [PDFJS + "pdf.min.js", PDFJS + "pdf.worker.min.js"];

self.addEventListener("install", e => {
  e.waitUntil(
    caches.open(CACHE)
      .then(c => c.addAll(FILES).then(() => c.addAll(LIBS.map(u => new Request(u, { mode: "cors" }))).catch(() => {})))
      .then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", e => {
  e.waitUntil(
    caches.keys()
      .then(keys => Promise.all(keys.filter(k => k !== CACHE && k !== SHARE).map(k => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});

self.addEventListener("fetch", e => {
  // Relevé partagé depuis une autre appli (menu « Partager » d'Android) : on le garde de côté
  // dans le cache du téléphone, puis la page l'importe dès son ouverture.
  if (e.request.method === "POST" && new URL(e.request.url).searchParams.has("share-target")) {
    e.respondWith((async () => {
      try {
        const form = await e.request.formData();
        const file = form.get("file");
        if (file && typeof file !== "string") {
          const c = await caches.open(SHARE);
          await c.put("shared-file", new Response(file, { headers: { "content-type": file.type || "application/octet-stream", "x-file-name": encodeURIComponent(file.name || "releve") } }));
        }
      } catch (err) { /* page ouverte sans fichier */ }
      return Response.redirect("./?shared=1", 303);
    })());
    return;
  }
  if (e.request.method !== "GET") return;
  const url = e.request.url;
  // pdf.js : version figée, donc cache d'abord.
  if (url.startsWith(PDFJS)) {
    e.respondWith(
      caches.match(url).then(hit => hit || fetch(e.request).then(res => {
        if (res.ok) { const copy = res.clone(); caches.open(CACHE).then(c => c.put(url, copy)); }
        return res;
      }))
    );
    return;
  }
  // Fichiers de l'appli : réseau d'abord (pour recevoir les mises à jour), cache en secours hors-ligne.
  e.respondWith(
    fetch(e.request)
      .then(res => {
        if (res.ok && new URL(url).origin === location.origin) {
          const copy = res.clone();
          caches.open(CACHE).then(c => c.put(e.request, copy));
        }
        return res;
      })
      .catch(() => caches.match(e.request).then(r => r || caches.match("index.html")))
  );
});
