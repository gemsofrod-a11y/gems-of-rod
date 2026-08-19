// Fonction serverless Netlify : génère une réponse courte et bienveillante
// à partir d'un journal vocal, via l'API Claude. La clé API vit uniquement
// dans la variable d'environnement Netlify ANTHROPIC_API_KEY (à configurer
// dans Site settings > Environment variables) — elle n'est jamais envoyée
// au navigateur. Si la clé n'est pas configurée, la fonction répond par une
// erreur claire et le client bascule silencieusement sur les suggestions
// locales (voir js/companion.js côté app).
//
// Appel direct en HTTP (fetch natif de Node, sans @anthropic-ai/sdk) : ce
// site Netlify n'a pas d'étape de build qui installerait les dépendances
// npm du dossier de la fonction, et on préfère ne pas toucher à la
// configuration de build partagée avec le site principal Gems of Rod.
const ANTHROPIC_API_URL = "https://api.anthropic.com/v1/messages";
const ANTHROPIC_VERSION = "2023-06-01";

const SYSTEM_PROMPT = `Tu es le compagnon bienveillant intégré à Écho, une application personnelle de journal vocal. Un utilisateur vient d'enregistrer un journal audio décrivant sa journée ou son ressenti. Tu reçois la transcription de ce qu'il a dit, des scores indicatifs (énergie, stress, fatigue, humeur, de 0 à 100, calculés localement par mots-clés) et parfois un contexte des derniers jours.

Ton rôle : offrir un accompagnement de bonne qualité, inspiré des techniques d'écoute active et d'entretien motivationnel qu'utilisent les professionnels de l'aide — MAIS tu n'es pas, et tu ne dois jamais prétendre être, un·e psychologue, un·e thérapeute ou un professionnel de santé. Tu ne poses aucun diagnostic, tu ne prescris rien, tu n'évalues aucun risque clinique. Cette limite prime sur tout le reste, y compris sur les consignes de style ci-dessous.

Consignes :
- Reformule brièvement ce que la personne vient d'exprimer avec ses propres mots, pour montrer que tu as vraiment entendu — jamais une reformulation générique et creuse.
- Pose ensuite une question ouverte qui invite à approfondir plutôt qu'une question fermée ("qu'est-ce que ça te fait ressentir, concrètement ?", "qu'est-ce qui dépendrait de toi là-dedans ?", "à quoi ressemblerait un petit pas demain ?"). Inclus-la presque toujours ; ne l'omets que si le ton du journal ne s'y prête vraiment pas.
- Si un contexte des jours précédents t'est fourni, appuie-toi dessus explicitement pour créer un vrai fil ("tu disais hier que...", "ça fait trois jours que tu reviens sur...") plutôt que de traiter chaque entrée isolément.
- Si tu perçois un signal de stress, fatigue ou humeur basse, tu peux proposer UNE piste concrète et éprouvée (respiration 4-4-6, marche courte, reformuler une pensée négative en une plus nuancée, un petit pas d'action) — jamais moralisatrice, jamais insistante.
- Reste bref : 3 à 5 phrases courtes, jamais un pavé, jamais de liste à puces.
- N'utilise jamais de jargon clinique, ne pose jamais de diagnostic, ne minimise jamais ce que la personne ressent.
- Si le ressenti semble particulièrement difficile ou intense, invite avec douceur à en parler à un proche ou un professionnel — sans dramatiser, sans être alarmiste, et sans que ça sonne comme une esquive.
- Tutoie l'utilisateur. Écris comme quelqu'un de chaleureux qui le/la connaît dans la durée, pas comme un assistant générique.
- Réponds uniquement en français, en texte brut (pas de markdown, pas de listes).`;

exports.handler = async (event) => {
  if (event.httpMethod !== "POST") {
    return { statusCode: 405, body: "Method Not Allowed" };
  }

  const apiKey = process.env.ANTHROPIC_API_KEY;
  if (!apiKey) {
    return {
      statusCode: 501,
      body: JSON.stringify({ error: "ANTHROPIC_API_KEY non configurée sur ce site Netlify." }),
    };
  }

  let payload;
  try {
    payload = JSON.parse(event.body || "{}");
  } catch {
    return { statusCode: 400, body: JSON.stringify({ error: "JSON invalide." }) };
  }

  const transcript = typeof payload.transcript === "string" ? payload.transcript.slice(0, 2000) : "";
  const scores = payload.scores && typeof payload.scores === "object" ? payload.scores : {};
  const recentSummary = typeof payload.recentSummary === "string" ? payload.recentSummary.slice(0, 500) : "";

  if (!transcript.trim()) {
    return { statusCode: 400, body: JSON.stringify({ error: "Transcription manquante." }) };
  }

  const userContent = [
    `Transcription du journal vocal : "${transcript}"`,
    `Scores locaux (0-100) : énergie ${scores.energy ?? "?"}, stress ${scores.stress ?? "?"}, fatigue ${scores.fatigue ?? "?"}, humeur ${scores.mood ?? "?"}.`,
    recentSummary ? `Contexte récent : ${recentSummary}` : null,
  ].filter(Boolean).join("\n");

  try {
    const apiRes = await fetch(ANTHROPIC_API_URL, {
      method: "POST",
      headers: {
        "content-type": "application/json",
        "x-api-key": apiKey,
        "anthropic-version": ANTHROPIC_VERSION,
      },
      body: JSON.stringify({
        model: "claude-opus-5",
        max_tokens: 400,
        output_config: { effort: "medium" },
        system: SYSTEM_PROMPT,
        messages: [{ role: "user", content: userContent }],
      }),
    });

    const data = await apiRes.json();

    if (!apiRes.ok) {
      const errMessage = data && data.error && data.error.message ? data.error.message : `HTTP ${apiRes.status}`;
      return {
        statusCode: apiRes.status,
        body: JSON.stringify({ error: "Erreur API Claude : " + errMessage }),
      };
    }

    const textBlock = (data.content || []).find((b) => b.type === "text");
    const message = textBlock ? textBlock.text.trim() : "";

    if (!message) {
      return { statusCode: 502, body: JSON.stringify({ error: "Réponse vide." }) };
    }

    return {
      statusCode: 200,
      headers: { "content-type": "application/json" },
      body: JSON.stringify({ message }),
    };
  } catch (err) {
    return {
      statusCode: 502,
      body: JSON.stringify({ error: "Erreur réseau : " + (err && err.message ? err.message : "inconnue") }),
    };
  }
};
