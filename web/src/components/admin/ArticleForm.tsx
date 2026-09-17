"use client";

import type { Article } from "@prisma/client";

type Props = {
  article?: Article;
  action: (formData: FormData) => void;
};

const inputClass =
  "w-full border border-border bg-surface px-3 py-2 text-sm outline-none focus:border-foreground";

export function ArticleForm({ article, action }: Props) {
  return (
    <form action={action} className="space-y-6">
      <label className="block">
        <span className="text-xs uppercase tracking-widest text-muted">Titre</span>
        <input
          name="titre"
          required
          defaultValue={article?.titre}
          className={`${inputClass} mt-1`}
        />
      </label>

      <label className="block">
        <span className="text-xs uppercase tracking-widest text-muted">Statut</span>
        <select
          name="statut"
          defaultValue={article?.statut ?? "BROUILLON"}
          className={`${inputClass} mt-1`}
        >
          <option value="BROUILLON">Brouillon</option>
          <option value="PUBLIE">Publié</option>
        </select>
      </label>

      <label className="block">
        <span className="text-xs uppercase tracking-widest text-muted">
          Extrait (affiché dans la liste)
        </span>
        <textarea
          name="extrait"
          required
          rows={2}
          defaultValue={article?.extrait}
          className={`${inputClass} mt-1`}
        />
      </label>

      <label className="block">
        <span className="text-xs uppercase tracking-widest text-muted">
          Contenu (un paragraphe par ligne vide)
        </span>
        <textarea
          name="contenu"
          required
          rows={12}
          defaultValue={article?.contenu}
          className={`${inputClass} mt-1`}
        />
      </label>

      <label className="block">
        <span className="text-xs uppercase tracking-widest text-muted">
          Image de couverture {article?.coverImageUrl && "(remplacer)"}
        </span>
        <input
          type="file"
          name="cover"
          accept="image/*"
          capture="environment"
          className="mt-1 w-full text-xs"
        />
      </label>

      <button type="submit" className="btn-primary">
        Enregistrer
      </button>
    </form>
  );
}
