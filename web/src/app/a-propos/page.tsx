import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Notre histoire",
  description:
    "Gems of Rod, maison française indépendante fondée en 2020, spécialisée dans les pierres précieuses et bijoux d'exception.",
};

export default function AProposPage() {
  return (
    <div className="mx-auto max-w-3xl px-6 py-16">
      <h1 className="font-display text-3xl">Notre histoire</h1>
      <div className="mt-6 space-y-5 text-muted">
        <p>
          Gems of Rod est une maison française indépendante fondée en 2020,
          spécialisée dans la sélection, la vente et la valorisation de
          pierres précieuses, pierres fines, métaux précieux et bijoux
          d&apos;exception.
        </p>
        <p>
          Basée en France, elle s&apos;adresse à une clientèle de
          collectionneurs, investisseurs et amateurs de joaillerie fine, en
          apportant à chaque pièce une exigence de rareté, d&apos;authenticité
          et de transparence.
        </p>
        <p>
          Chaque pierre est sélectionnée pour son origine, son traitement, sa
          couleur et sa pureté, avec certification par des laboratoires
          reconnus (GIA, Gübelin, GFCO).
        </p>
      </div>
    </div>
  );
}
