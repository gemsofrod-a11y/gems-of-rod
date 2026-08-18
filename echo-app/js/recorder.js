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

    // Certains navigateurs (Chrome Android notamment) réémettent le même
    // résultat "final" plusieurs fois de suite en l'affinant, plutôt que
    // d'en émettre un nouveau à chaque fois. On reconstruit donc la
    // transcription du segment en cours à partir de la liste complète des
    // résultats à chaque événement (au lieu de l'accumuler par ajout), pour
    // éviter les doublons. Si le moteur redémarre en interne (la liste de
    // résultats redevient plus courte), on fige le segment précédent avant
    // de repartir sur un nouveau.
    let committedPrefix = "";
    let currentSegmentFinal = "";
    let lastResultsLength = 0;

    recognition.onresult = (event) => {
      if (event.results.length < lastResultsLength) {
        committedPrefix = (committedPrefix + " " + currentSegmentFinal).trim();
        currentSegmentFinal = "";
      }
      lastResultsLength = event.results.length;

      let segmentFinal = "";
      let interim = "";
      for (let i = 0; i < event.results.length; i++) {
        const chunk = event.results[i][0].transcript;
        if (event.results[i].isFinal) segmentFinal += chunk + " ";
        else interim += chunk;
      }
      currentSegmentFinal = segmentFinal;
      finalTranscript = (committedPrefix + " " + segmentFinal).trim();
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
