import { notFound } from "next/navigation";
import { prisma } from "@/lib/prisma";
import { ArticleForm } from "@/components/admin/ArticleForm";
import { updateArticle } from "@/lib/actions/articles";

type Params = Promise<{ id: string }>;

export default async function EditArticlePage({ params }: { params: Params }) {
  const { id } = await params;
  const article = await prisma.article.findUnique({ where: { id } });
  if (!article) notFound();

  return (
    <div>
      <h1 className="font-display text-2xl">Modifier « {article.titre} »</h1>
      <div className="mt-6 max-w-2xl">
        <ArticleForm article={article} action={updateArticle.bind(null, article.id)} />
      </div>
    </div>
  );
}
