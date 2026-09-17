import { ArticleForm } from "@/components/admin/ArticleForm";
import { createArticle } from "@/lib/actions/articles";

export default function NewArticlePage() {
  return (
    <div>
      <h1 className="font-display text-2xl">Nouvel article</h1>
      <div className="mt-6 max-w-2xl">
        <ArticleForm action={createArticle} />
      </div>
    </div>
  );
}
