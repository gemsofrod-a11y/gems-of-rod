// Compagnon conversationnel (optionnel) : appelle une fonction serverless
// Netlify qui elle-même appelle l'API Claude. La clé API reste côté
// serveur (variable d'environnement Netlify), jamais dans ce code ni dans
// le navigateur. Si la fonction n'est pas déployée ou pas configurée
// (pas de clé), l'app continue de fonctionner avec les suggestions locales
// uniquement — cette couche est une amélioration optionnelle, pas une
// dépendance.
const Companion = (() => {
  const ENDPOINT = "/.netlify/functions/companion";
  // Simple déterrent anti-scan, pas une vraie authentification (visible
  // dans ce fichier servi au navigateur) — voir le commentaire côté
  // fonction Netlify pour le détail des limites et ce qu'il faudrait pour
  // une vraie protection par utilisateur.
  const CLIENT_SECRET = "echo-app-2026";

  async function getResponse({ transcript, scores, recentSummary }) {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 15000);
    try {
      const res = await fetch(ENDPOINT, {
        method: "POST",
        headers: { "content-type": "application/json", "x-echo-client": CLIENT_SECRET },
        body: JSON.stringify({ transcript, scores, recentSummary }),
        signal: controller.signal,
      });
      if (!res.ok) return null;
      const data = await res.json();
      return typeof data.message === "string" ? data.message : null;
    } catch (e) {
      return null;
    } finally {
      clearTimeout(timeout);
    }
  }

  return { getResponse };
})();
