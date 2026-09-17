import type { MetadataRoute } from "next";

export default function manifest(): MetadataRoute.Manifest {
  return {
    name: "Gems of Rod — Pierres précieuses & bijoux d'exception",
    short_name: "Gems of Rod",
    description:
      "Boutique de pierres précieuses, pierres fines, métaux précieux et bijoux d'exception.",
    start_url: "/",
    display: "standalone",
    background_color: "#faf8f4",
    theme_color: "#16181d",
    icons: [
      { src: "/icon-192.png", sizes: "192x192", type: "image/png" },
      { src: "/icon-512.png", sizes: "512x512", type: "image/png" },
      {
        src: "/icon-512.png",
        sizes: "512x512",
        type: "image/png",
        purpose: "maskable",
      },
    ],
  };
}
