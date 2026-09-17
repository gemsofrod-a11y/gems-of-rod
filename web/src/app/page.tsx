import Link from "next/link";
import { prisma } from "@/lib/prisma";
import { ProductCard } from "@/components/ProductCard";

// Always read live from the database (new products must appear immediately,
// and no database is reachable at build time on platforms like Netlify).
export const dynamic = "force-dynamic";

export default async function HomePage() {
  const featured = await prisma.product.findMany({
    where: { statut: "DISPONIBLE" },
    orderBy: { dateAjout: "desc" },
    take: 6,
    include: { images: { orderBy: { position: "asc" }, take: 1 } },
  });

  return (
    <div>
      <section className="border-b border-border">
        <div className="mx-auto flex max-w-6xl flex-col items-center gap-6 px-6 py-24 text-center">
          <p className="text-xs uppercase tracking-[0.3em] text-muted">
            Maison française — depuis 2020
          </p>
          <h1 className="font-display text-4xl leading-tight md:text-5xl">
            Pierres précieuses <span className="brand-gradient-text">&amp; bijoux</span>
            <br />
            d&apos;exception
          </h1>
          <p className="max-w-xl text-muted">
            Gems of Rod sélectionne, certifie et valorise des pierres
            précieuses, pierres fines, métaux précieux et bijoux d&apos;exception,
            pour collectionneurs, investisseurs et amateurs de joaillerie fine.
          </p>
          <div className="flex flex-wrap justify-center gap-4">
            <Link href="/boutique" className="btn-primary">
              Découvrir la collection
            </Link>
            <Link href="/contact" className="btn-secondary">
              Demander conseil
            </Link>
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-6 py-20">
        <div className="mb-10 flex items-end justify-between">
          <h2 className="font-display text-2xl">Dernières acquisitions</h2>
          <Link
            href="/boutique"
            className="text-xs uppercase tracking-[0.15em] text-muted hover:text-foreground"
          >
            Toute la collection →
          </Link>
        </div>
        {featured.length === 0 ? (
          <p className="text-muted">
            De nouvelles pièces sont en cours de sélection — revenez très
            bientôt.
          </p>
        ) : (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {featured.map((product) => (
              <ProductCard key={product.slug} product={product} />
            ))}
          </div>
        )}
      </section>

      <section className="border-t border-border bg-surface">
        <div className="mx-auto grid max-w-6xl gap-10 px-6 py-20 md:grid-cols-3">
          <div>
            <h3 className="font-display text-lg">Rareté &amp; authenticité</h3>
            <p className="mt-3 text-sm text-muted">
              Chaque pierre est sélectionnée pour son origine, sa couleur et
              sa pureté, avec certification GIA, Gübelin ou GFCO.
            </p>
          </div>
          <div>
            <h3 className="font-display text-lg">Expertise &amp; transparence</h3>
            <p className="mt-3 text-sm text-muted">
              Origine, traitement et certification sont précisés pour chaque
              pièce — sans hyperbole, avec exactitude.
            </p>
          </div>
          <div>
            <h3 className="font-display text-lg">Conseil personnalisé</h3>
            <p className="mt-3 text-sm text-muted">
              Une question, un projet de bijou sur mesure ? Contactez-nous
              pour un accompagnement dédié.
            </p>
          </div>
        </div>
      </section>
    </div>
  );
}
