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

    recognition.onresult = (event) => {
      let interim = "";
      for (let i = event.resultIndex; i < event.results.length; i++) {
        const chunk = event.results[i][0].transcript;
        if (event.results[i].isFinal) {
          finalTranscript += chunk + " ";
          onFinalChunk && onFinalChunk(finalTranscript.trim());
        } else {
          interim += chunk;
        }
      }
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
