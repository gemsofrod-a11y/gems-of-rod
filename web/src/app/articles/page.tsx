import Image from "next/image";
import Link from "next/link";
import type { Metadata } from "next";
import { prisma } from "@/lib/prisma";

export const metadata: Metadata = {
  title: "Journal",
  description: "Articles gemmologiques et actualités de Gems of Rod.",
};

// Always read live from the database (a newly published article must
// appear immediately, and no database is reachable at build time on
// platforms like Netlify).
export const dynamic = "force-dynamic";

export default async function ArticlesPage() {
  const articles = await prisma.article.findMany({
    where: { statut: "PUBLIE" },
    orderBy: { publishedAt: "desc" },
  });

  return (
    <div className="mx-auto max-w-4xl px-6 py-16">
      <h1 className="font-display text-3xl">Le journal</h1>
      <p className="mt-2 text-muted">
        Actualités, portraits de pierres et notes gemmologiques de la maison.
      </p>

      {articles.length === 0 ? (
        <p className="mt-10 text-muted">Aucun article publié pour le moment.</p>
      ) : (
        <div className="mt-10 space-y-10">
          {articles.map((article) => (
            <Link
              key={article.slug}
              href={`/articles/${article.slug}`}
              className="flex flex-col gap-4 border-b border-border pb-10 sm:flex-row"
            >
              {article.coverImageUrl && (
                <div className="relative h-40 w-full flex-shrink-0 overflow-hidden bg-[#efece4] sm:w-56">
                  <Image
                    src={article.coverImageUrl}
                    alt={article.titre}
                    fill
                    className="object-cover"
                    sizes="224px"
                  />
                </div>
              )}
              <div>
                {article.publishedAt && (
                  <p className="text-xs uppercase tracking-widest text-muted">
                    {new Intl.DateTimeFormat("fr-FR", {
                      dateStyle: "long",
                    }).format(article.publishedAt)}
                  </p>
                )}
                <h2 className="mt-1 font-display text-xl">{article.titre}</h2>
                <p className="mt-2 text-sm text-muted">{article.extrait}</p>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
