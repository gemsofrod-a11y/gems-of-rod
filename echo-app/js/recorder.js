// Reconnaissance vocale via l'API native du navigateur (Web Speech API).
// Gratuit, sans clé API, mais dépend du support navigateur (Chrome/Edge/Safari).
const Recorder = (() => {
  const SpeechRecognitionCtor = window.SpeechRecognition || window.webkitSpeechRecognition;
  let recognition = null;
  let listening = false;
  let finalTranscript = "";
  let manualStop = false;

  function isSupported() {
    return !!SpeechRecognitionCtor;
  }

  function start({ onInterim, onFinalChunk, onError, onEnd }) {
    if (!isSupported()) {
      onError && onError(new Error("Web Speech API non supportée"));
      return;
    }
    finalTranscript = "";
    manualStop = false;

    // Certains moteurs (Chrome Android notamment) n'émettent pas des
    // segments "finaux" distincts et complémentaires : ils réémettent, à
    // des index différents, le texte cumulatif reconnu jusque-là (chaque
    // nouveau bloc contient déjà tout le précédent, plus quelques mots).
    // Une simple concaténation des blocs finaux donnerait donc un texte
    // qui se répète en boucle. On fusionne donc par préfixe : si un bloc
    // contient déjà le texte accumulé, il le remplace ; s'il en est un
    // sous-ensemble, on l'ignore ; sinon (vraie nouvelle phrase), on l'ajoute.
    function mergeFinal(existing, next) {
      const e = (existing || "").trim();
      const n = (next || "").trim();
      if (!e) return n;
      if (!n) return e;
      const el = e.toLowerCase();
      const nl = n.toLowerCase();
      if (nl.startsWith(el)) return n;
      if (el.startsWith(nl)) return e;
      return `${e} ${n}`;
    }

    // Quand on relance nous-mêmes la reconnaissance après un arrêt
    // automatique du moteur (voir onend plus bas), on fige le segment qui
    // vient de se terminer dans un préfixe "committed" avant de repartir
    // sur une nouvelle liste de résultats (qui recommence à zéro).
    let committedPrefix = "";
    let currentSegmentFinal = "";

    function consolidateSegment() {
      committedPrefix = mergeFinal(committedPrefix, currentSegmentFinal);
      currentSegmentFinal = "";
    }

    function attachHandlers(rec) {
      rec.onresult = (event) => {
        let segmentFinal = "";
        let interim = "";
        for (let i = 0; i < event.results.length; i++) {
          const chunk = event.results[i][0].transcript;
          if (event.results[i].isFinal) segmentFinal = mergeFinal(segmentFinal, chunk);
          else interim += chunk;
        }
        currentSegmentFinal = segmentFinal;
        finalTranscript = mergeFinal(committedPrefix, segmentFinal);
        onFinalChunk && onFinalChunk(finalTranscript);
        onInterim && onInterim((finalTranscript + " " + interim).trim());
      };

      rec.onerror = (event) => {
        // "no-speech" et "aborted" surviennent couramment lors des redémarrages
        // automatiques (silence détecté, ou arrêt volontaire) : ce ne sont pas
        // des erreurs fatales, onend s'en charge juste après.
        if (event.error === "no-speech" || event.error === "aborted") return;
        onError && onError(new Error(event.error || "Erreur de reconnaissance vocale"));
      };

      rec.onend = () => {
        if (manualStop) {
          listening = false;
          onEnd && onEnd(finalTranscript.trim());
          return;
        }
        // Même en mode "continuous", le moteur (Chrome Android notamment)
        // s'arrête tout seul après une pause dans la parole. Tant que
        // l'utilisateur n'a pas explicitement demandé l'arrêt, on relance
        // pour que l'enregistrement reste continu de son point de vue —
        // le fold-merge ci-dessus recolle proprement les segments.
        consolidateSegment();
        startNewInstance();
      };
    }

    // Une instance fraîche à chaque redémarrage plutôt que de rappeler
    // .start() sur la même : sur au moins un moteur réel, relancer la même
    // instance semble reprendre l'écoute un peu plus lentement (état
    // interne qui traîne), ce qui élargit la fenêtre de parole non captée
    // à chaque pause — perçu comme "la reconnaissance a du mal à suivre".
    // Le merge par préfixe ci-dessus ne dépend que du texte, jamais de
    // l'identité de l'objet, donc ce changement ne modifie pas la logique
    // de recollement des segments.
    function startNewInstance() {
      recognition = new SpeechRecognitionCtor();
      recognition.lang = "fr-FR";
      recognition.continuous = true;
      recognition.interimResults = true;
      attachHandlers(recognition);
      try {
        recognition.start();
        listening = true;
      } catch (e) {
        listening = false;
        onEnd && onEnd(finalTranscript.trim());
      }
    }

    startNewInstance();
  }

  function stop() {
    manualStop = true;
    if (recognition && listening) {
      recognition.stop();
    }
  }

  function getTranscript() {
    return finalTranscript.trim();
  }

  return { isSupported, start, stop, getTranscript };
})();
