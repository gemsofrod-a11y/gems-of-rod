import Image from "next/image";
import { notFound } from "next/navigation";
import type { Metadata } from "next";
import { prisma } from "@/lib/prisma";

type Params = Promise<{ slug: string }>;

async function getArticle(slug: string) {
  const article = await prisma.article.findUnique({ where: { slug } });
  if (!article || article.statut !== "PUBLIE") return null;
  return article;
}

export async function generateMetadata({
  params,
}: {
  params: Params;
}): Promise<Metadata> {
  const { slug } = await params;
  const article = await getArticle(slug);
  if (!article) return {};
  return { title: article.titre, description: article.extrait };
}

export default async function ArticlePage({ params }: { params: Params }) {
  const { slug } = await params;
  const article = await getArticle(slug);
  if (!article) notFound();

  const paragraphs = article.contenu
    .split(/\n\s*\n/)
    .map((p) => p.trim())
    .filter(Boolean);

  return (
    <article className="mx-auto max-w-2xl px-6 py-16">
      {article.publishedAt && (
        <p className="text-xs uppercase tracking-widest text-muted">
          {new Intl.DateTimeFormat("fr-FR", { dateStyle: "long" }).format(
            article.publishedAt
          )}
        </p>
      )}
      <h1 className="mt-2 font-display text-3xl">{article.titre}</h1>
      {article.coverImageUrl && (
        <div className="relative mt-6 aspect-[16/9] overflow-hidden bg-[#efece4]">
          <Image
            src={article.coverImageUrl}
            alt={article.titre}
            fill
            className="object-cover"
            sizes="672px"
            priority
          />
        </div>
      )}
      <div className="mt-8 space-y-5 text-muted">
        {paragraphs.map((paragraph, index) => (
          <p key={index}>{paragraph}</p>
        ))}
      </div>
    </article>
  );
}
