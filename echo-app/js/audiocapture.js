// Capture de la piste audio brute (en plus de la transcription), pour
// permettre l'analyse locale du rythme de parole (pauses, pics de volume).
// Totalement indépendant de la reconnaissance vocale : si le micro n'est
// pas disponible pour l'enregistrement audio (permission refusée,
// MediaRecorder non supporté...), l'app continue de fonctionner normalement
// avec la seule transcription.
const AudioCapture = (() => {
  let mediaRecorder = null;
  let chunks = [];
  let stream = null;

  function isSupported() {
    return !!(navigator.mediaDevices && navigator.mediaDevices.getUserMedia && window.MediaRecorder);
  }

  async function start() {
    if (!isSupported()) return false;
    try {
      stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      chunks = [];
      mediaRecorder = new MediaRecorder(stream);
      mediaRecorder.addEventListener("dataavailable", (e) => {
        if (e.data && e.data.size > 0) chunks.push(e.data);
      });
      mediaRecorder.start();
      return true;
    } catch (e) {
      stream = null;
      mediaRecorder = null;
      return false;
    }
  }

  function stop() {
    return new Promise((resolve) => {
      if (!mediaRecorder || mediaRecorder.state === "inactive") {
        resolve(null);
        return;
      }
      mediaRecorder.addEventListener("stop", () => {
        const type = mediaRecorder.mimeType || "audio/webm";
        const blob = chunks.length ? new Blob(chunks, { type }) : null;
        if (stream) stream.getTracks().forEach((t) => t.stop());
        stream = null;
        mediaRecorder = null;
        chunks = [];
        resolve(blob);
      });
      mediaRecorder.stop();
    });
  }

  return { isSupported, start, stop };
})();
