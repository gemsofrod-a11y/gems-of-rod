import Image from "next/image";
import Link from "next/link";
import { CATEGORIE_LABELS, formatPrice } from "@/lib/format";

export type ProductCardData = {
  slug: string;
  nom: string;
  categorie: string;
  descriptionCourte: string;
  prixEur: number;
  statut: string;
  images: { url: string }[];
};

export function ProductCard({ product }: { product: ProductCardData }) {
  const image = product.images[0]?.url;

  return (
    <Link
      href={`/boutique/${product.slug}`}
      className="group block border border-border bg-surface"
    >
      <div className="relative aspect-square overflow-hidden bg-[#efece4]">
        {image ? (
          <Image
            src={image}
            alt={product.nom}
            fill
            className="object-cover transition duration-300 group-hover:scale-[1.03]"
            sizes="(min-width: 1024px) 25vw, (min-width: 640px) 33vw, 100vw"
          />
        ) : (
          <div className="flex h-full items-center justify-center text-xs uppercase tracking-widest text-muted">
            Photo à venir
          </div>
        )}
        {product.statut !== "DISPONIBLE" && (
          <span className="absolute left-3 top-3 bg-foreground px-2 py-1 text-[10px] uppercase tracking-widest text-surface">
            {product.statut === "VENDU" ? "Vendu" : "Réservé"}
          </span>
        )}
      </div>
      <div className="p-5">
        <p className="text-[11px] uppercase tracking-[0.15em] text-muted">
          {CATEGORIE_LABELS[product.categorie] ?? product.categorie}
        </p>
        <h3 className="mt-1 font-display text-lg">{product.nom}</h3>
        <p className="mt-2 line-clamp-2 text-sm text-muted">
          {product.descriptionCourte}
        </p>
        <p className="mt-3 text-sm font-medium">{formatPrice(product.prixEur)}</p>
      </div>
    </Link>
  );
}
