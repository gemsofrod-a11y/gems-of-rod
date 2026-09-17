import Image from "next/image";
import Link from "next/link";
import { notFound } from "next/navigation";
import type { Metadata } from "next";
import { prisma } from "@/lib/prisma";
import { CATEGORIE_LABELS, formatPrice } from "@/lib/format";
import { ContactForm } from "@/components/ContactForm";

type Params = Promise<{ slug: string }>;

async function getProduct(slug: string) {
  return prisma.product.findUnique({
    where: { slug },
    include: { images: { orderBy: { position: "asc" } } },
  });
}

export async function generateMetadata({
  params,
}: {
  params: Params;
}): Promise<Metadata> {
  const { slug } = await params;
  const product = await getProduct(slug);
  if (!product) return {};
  return {
    title: product.nom,
    description: product.descriptionCourte,
  };
}

const SPECS: Array<[string, keyof NonNullable<Awaited<ReturnType<typeof getProduct>>>]> = [
  ["Origine", "origine"],
  ["Couleur", "couleur"],
  ["Pureté", "purete"],
  ["Traitement", "traitement"],
  ["Certification", "certification"],
  ["N° de certificat", "numeroCertificat"],
  ["Poinçon", "poincon"],
];

export default async function ProductPage({ params }: { params: Params }) {
  const { slug } = await params;
  const product = await getProduct(slug);
  if (!product) notFound();

  const tags = product.tags
    ? product.tags.split(",").map((t) => t.trim()).filter(Boolean)
    : [];

  return (
    <div className="mx-auto max-w-6xl px-6 py-16">
      <Link href="/boutique" className="text-xs uppercase tracking-widest text-muted hover:text-foreground">
        ← Retour à la boutique
      </Link>

      <div className="mt-6 grid grid-cols-1 gap-12 lg:grid-cols-2">
        <div className="space-y-4">
          <div className="relative aspect-square overflow-hidden bg-[#efece4]">
            {product.images[0] ? (
              <Image
                src={product.images[0].url}
                alt={product.nom}
                fill
                className="object-cover"
                sizes="(min-width: 1024px) 50vw, 100vw"
                priority
              />
            ) : (
              <div className="flex h-full items-center justify-center text-xs uppercase tracking-widest text-muted">
                Photo à venir
              </div>
            )}
          </div>
          {product.images.length > 1 && (
            <div className="grid grid-cols-4 gap-3">
              {product.images.slice(1).map((image) => (
                <div key={image.id} className="relative aspect-square overflow-hidden bg-[#efece4]">
                  <Image src={image.url} alt={product.nom} fill className="object-cover" sizes="25vw" />
                </div>
              ))}
            </div>
          )}
        </div>

        <div>
          <p className="text-xs uppercase tracking-[0.2em] text-muted">
            {CATEGORIE_LABELS[product.categorie] ?? product.categorie}
          </p>
          <h1 className="mt-2 font-display text-3xl">{product.nom}</h1>
          <p className="mt-4 text-2xl">{formatPrice(product.prixEur)}</p>
          {product.statut !== "DISPONIBLE" && (
            <p className="mt-2 inline-block bg-foreground px-3 py-1 text-xs uppercase tracking-widest text-surface">
              {product.statut === "VENDU" ? "Vendu" : "Réservé"}
            </p>
          )}

          <p className="mt-6 text-muted">{product.descriptionLongue}</p>

          <dl className="mt-8 space-y-3 border-t border-border pt-6 text-sm">
            {product.poidsCarats && (
              <div className="flex justify-between border-b border-border pb-3">
                <dt className="text-muted">Poids</dt>
                <dd>{product.poidsCarats} carats</dd>
              </div>
            )}
            {product.poidsGrammes && (
              <div className="flex justify-between border-b border-border pb-3">
                <dt className="text-muted">Poids</dt>
                <dd>{product.poidsGrammes} g</dd>
              </div>
            )}
            {SPECS.map(([label, key]) => {
              const value = product[key];
              if (!value) return null;
              return (
                <div key={key} className="flex justify-between border-b border-border pb-3">
                  <dt className="text-muted">{label}</dt>
                  <dd className="text-right">{String(value)}</dd>
                </div>
              );
            })}
          </dl>

          {tags.length > 0 && (
            <div className="mt-6 flex flex-wrap gap-2">
              {tags.map((tag) => (
                <span key={tag} className="border border-border px-3 py-1 text-xs uppercase tracking-widest text-muted">
                  {tag}
                </span>
              ))}
            </div>
          )}

          <div className="mt-10 border-t border-border pt-8">
            <h2 className="font-display text-lg">Demander un devis</h2>
            <p className="mt-2 text-sm text-muted">
              Cette pièce vous intéresse ? Laissez-nous vos coordonnées, nous
              revenons vers vous rapidement avec toutes les précisions
              souhaitées.
            </p>
            <div className="mt-4">
              <ContactForm productId={product.id} productLabel={product.nom} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
