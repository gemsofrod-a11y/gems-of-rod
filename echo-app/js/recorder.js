// Reconnaissance vocale via l'API native du navigateur (Web Speech API).
// Gratuit, sans clé API, mais dépend du support navigateur (Chrome/Edge/Safari).
const Recorder = (() => {
  const SpeechRecognitionCtor = window.SpeechRecognition || window.webkitSpeechRecognition;
  let recognition = null;
  let listening = false;
  let finalTranscript = "";

  function isSupported() {
    return !!SpeechRecognitionCtor;
  }

  function start({ onInterim, onFinalChunk, onError, onEnd }) {
    if (!isSupported()) {
      onError && onError(new Error("Web Speech API non supportée"));
      return;
    }
    finalTranscript = "";
    recognition = new SpeechRecognitionCtor();
    recognition.lang = "fr-FR";
    recognition.continuous = true;
    recognition.interimResults = true;

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

    // Si le moteur redémarre en interne (la liste de résultats redevient
    // plus courte qu'avant), on fige le segment précédent dans un préfixe
    // "committed" avant de repartir sur une nouvelle liste.
    let committedPrefix = "";
    let currentSegmentFinal = "";
    let lastResultsLength = 0;

    recognition.onresult = (event) => {
      if (event.results.length < lastResultsLength) {
        committedPrefix = mergeFinal(committedPrefix, currentSegmentFinal);
        currentSegmentFinal = "";
      }
      lastResultsLength = event.results.length;

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

    recognition.onerror = (event) => {
      onError && onError(new Error(event.error || "Erreur de reconnaissance vocale"));
    };

    recognition.onend = () => {
      listening = false;
      onEnd && onEnd(finalTranscript.trim());
    };

    recognition.start();
    listening = true;
  }

  function stop() {
    if (recognition && listening) {
      recognition.stop();
    }
  }

  function getTranscript() {
    return finalTranscript.trim();
  }

  return { isSupported, start, stop, getTranscript };
})();
