import Link from "next/link";
import { prisma } from "@/lib/prisma";
import { DeleteButton } from "@/components/admin/DeleteButton";
import { deleteArticle } from "@/lib/actions/articles";

const STATUT_LABELS: Record<string, string> = {
  BROUILLON: "Brouillon",
  PUBLIE: "Publié",
};

export default async function AdminArticlesPage() {
  const articles = await prisma.article.findMany({
    orderBy: { createdAt: "desc" },
  });

  return (
    <div>
      <div className="flex items-center justify-between">
        <h1 className="font-display text-2xl">Articles</h1>
        <Link href="/admin/articles/nouveau" className="btn-primary">
          + Nouvel article
        </Link>
      </div>

      <div className="mt-6 space-y-3">
        {articles.map((article) => (
          <div
            key={article.id}
            className="flex items-center justify-between border border-border bg-surface p-4"
          >
            <div>
              <Link href={`/admin/articles/${article.id}`} className="hover:underline">
                {article.titre}
              </Link>
              <p className="text-xs text-muted">{STATUT_LABELS[article.statut]}</p>
            </div>
            <DeleteButton
              action={deleteArticle.bind(null, article.id)}
              confirmMessage={`Supprimer « ${article.titre} » ?`}
            />
          </div>
        ))}
        {articles.length === 0 && (
          <p className="text-muted">Aucun article pour le moment.</p>
        )}
      </div>
    </div>
  );
}
