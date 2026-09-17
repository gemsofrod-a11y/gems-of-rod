"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import slugify from "slugify";
import { prisma } from "@/lib/prisma";
import { requireUser } from "@/lib/session";
import { logAction } from "@/lib/audit";
import { articleSchema } from "@/lib/validation";
import { saveUploadedImage } from "@/lib/uploads";

function parseArticleForm(formData: FormData) {
  const raw = Object.fromEntries(formData.entries());
  return articleSchema.parse(raw);
}

async function uniqueSlug(titre: string, ignoreId?: string) {
  const base = slugify(titre, { lower: true, strict: true });
  let slug = base;
  let i = 1;
  while (
    await prisma.article.findFirst({
      where: { slug, ...(ignoreId ? { id: { not: ignoreId } } : {}) },
    })
  ) {
    slug = `${base}-${i++}`;
  }
  return slug;
}

async function coverImageUrl(articleId: string, formData: FormData) {
  const file = formData.get("cover");
  if (file instanceof File && file.size > 0) {
    return saveUploadedImage(file, `articles/${articleId}`);
  }
  return undefined;
}

export async function createArticle(formData: FormData) {
  const user = await requireUser();
  const data = parseArticleForm(formData);
  const slug = await uniqueSlug(data.titre);

  const article = await prisma.article.create({
    data: {
      ...data,
      slug,
      authorId: user.id,
      publishedAt: data.statut === "PUBLIE" ? new Date() : null,
    },
  });

  const cover = await coverImageUrl(article.id, formData);
  if (cover) {
    await prisma.article.update({
      where: { id: article.id },
      data: { coverImageUrl: cover },
    });
  }

  await logAction({
    userId: user.id,
    action: "article.create",
    entityType: "Article",
    entityId: article.id,
    details: { titre: article.titre },
  });

  revalidatePath("/admin/articles");
  revalidatePath("/articles");
  redirect("/admin/articles");
}

export async function updateArticle(articleId: string, formData: FormData) {
  const user = await requireUser();
  const data = parseArticleForm(formData);
  const existing = await prisma.article.findUniqueOrThrow({
    where: { id: articleId },
  });

  const slug =
    existing.titre === data.titre
      ? existing.slug
      : await uniqueSlug(data.titre, articleId);

  const cover = await coverImageUrl(articleId, formData);

  await prisma.article.update({
    where: { id: articleId },
    data: {
      ...data,
      slug,
      ...(cover ? { coverImageUrl: cover } : {}),
      publishedAt:
        data.statut === "PUBLIE" ? (existing.publishedAt ?? new Date()) : null,
    },
  });

  await logAction({
    userId: user.id,
    action: "article.update",
    entityType: "Article",
    entityId: articleId,
    details: { titre: data.titre },
  });

  revalidatePath("/admin/articles");
  revalidatePath(`/admin/articles/${articleId}`);
  revalidatePath("/articles");
  revalidatePath(`/articles/${slug}`);
  redirect("/admin/articles");
}

export async function deleteArticle(articleId: string) {
  const user = await requireUser();
  const article = await prisma.article.delete({ where: { id: articleId } });

  await logAction({
    userId: user.id,
    action: "article.delete",
    entityType: "Article",
    entityId: articleId,
    details: { titre: article.titre },
  });

  revalidatePath("/admin/articles");
  revalidatePath("/articles");
}
