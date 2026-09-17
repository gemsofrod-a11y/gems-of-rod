"use client";

import { useEffect } from "react";

export function ServiceWorkerRegister() {
  useEffect(() => {
    if (
      process.env.NODE_ENV === "production" &&
      "serviceWorker" in navigator
    ) {
      navigator.serviceWorker.register("/sw.js").catch(() => {
        // L'installation en app reste possible sans le service worker ; on
        // ne bloque jamais le rendu de la page pour ça.
      });
    }
  }, []);

  return null;
}
