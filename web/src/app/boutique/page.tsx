import Link from "next/link";
import type { Metadata } from "next";
import type { Categorie } from "@prisma/client";
import { prisma } from "@/lib/prisma";
import { ProductCard } from "@/components/ProductCard";
import { categorieOptions } from "@/lib/validation";

export const metadata: Metadata = {
  title: "Boutique",
  description:
    "Pierres précieuses, pierres fines, métaux précieux et bijoux sélectionnés par Gems of Rod.",
};

type SearchParams = Promise<{ categorie?: string }>;

export default async function BoutiquePage({
  searchParams,
}: {
  searchParams: SearchParams;
}) {
  const { categorie } = await searchParams;
  const isValidCategorie = categorieOptions.some((c) => c.value === categorie);

  const products = await prisma.product.findMany({
    where: {
      statut: { in: ["DISPONIBLE", "RESERVE"] },
      ...(isValidCategorie ? { categorie: categorie as Categorie } : {}),
    },
    orderBy: { dateAjout: "desc" },
    include: { images: { orderBy: { position: "asc" }, take: 1 } },
  });

  return (
    <div className="mx-auto max-w-6xl px-6 py-16">
      <div className="mb-10">
        <h1 className="font-display text-3xl">La collection</h1>
        <p className="mt-2 max-w-2xl text-muted">
          Pierres précieuses, pierres fines, métaux précieux et bijoux —
          chaque pièce est décrite avec son origine, sa certification et ses
          particularités.
        </p>
      </div>

      <div className="mb-10 flex flex-wrap gap-2">
        <Link
          href="/boutique"
          className={`border px-4 py-2 text-xs uppercase tracking-widest ${
            !isValidCategorie
              ? "border-foreground bg-foreground text-surface"
              : "border-border text-muted hover:border-foreground hover:text-foreground"
          }`}
        >
          Tout
        </Link>
        {categorieOptions.map((option) => (
          <Link
            key={option.value}
            href={`/boutique?categorie=${option.value}`}
            className={`border px-4 py-2 text-xs uppercase tracking-widest ${
              categorie === option.value
                ? "border-foreground bg-foreground text-surface"
                : "border-border text-muted hover:border-foreground hover:text-foreground"
            }`}
          >
            {option.label}
          </Link>
        ))}
      </div>

      {products.length === 0 ? (
        <p className="text-muted">Aucune pièce disponible dans cette catégorie pour le moment.</p>
      ) : (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {products.map((product) => (
            <ProductCard key={product.slug} product={product} />
          ))}
        </div>
      )}
    </div>
  );
}
