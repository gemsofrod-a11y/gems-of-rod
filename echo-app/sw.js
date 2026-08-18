const CACHE_NAME = "echo-cache-v6";
const ASSETS = [
  "./",
  "./index.html",
  "./manifest.webmanifest",
  "./css/style.css",
  "./js/storage.js",
  "./js/analysis.js",
  "./js/recorder.js",
  "./js/audiocapture.js",
  "./js/audiostore.js",
  "./js/prosody.js",
  "./js/charts.js",
  "./js/companion.js",
  "./js/onboarding.js",
  "./js/app.js",
  "./icons/icon.svg",
];

self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS)).then(() => self.skipWaiting())
  );
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k)))
    ).then(() => self.clients.claim())
  );
});

// Réseau en priorité (network-first) : tant que l'app est en ligne, on sert
// toujours la dernière version déployée. Le cache ne sert que de secours
// hors-ligne, pour éviter de rester bloqué sur une version obsolète après
// une mise à jour (l'ancienne stratégie cache-first servait indéfiniment
// les fichiers JS mis en cache lors de la première visite).
self.addEventListener("fetch", (event) => {
  if (event.request.method !== "GET") return;
  event.respondWith(
    fetch(event.request)
      .then((response) => {
        const copy = response.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(event.request, copy));
        return response;
      })
      .catch(() => caches.match(event.request))
  );
});
