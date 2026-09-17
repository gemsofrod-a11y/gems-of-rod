"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import slugify from "slugify";
import { prisma } from "@/lib/prisma";
import { requireUser } from "@/lib/session";
import { logAction } from "@/lib/audit";
import { productSchema } from "@/lib/validation";
import { saveUploadedImage } from "@/lib/uploads";

function parseProductForm(formData: FormData) {
  const raw = Object.fromEntries(formData.entries());
  const parsed = productSchema.parse(raw);
  // Undefined would make Prisma skip the field on update (keeping the old
  // value) instead of clearing it, so nullable fields are normalized to null.
  return {
    ...parsed,
    poidsCarats: parsed.poidsCarats ?? null,
    poidsGrammes: parsed.poidsGrammes ?? null,
    origine: parsed.origine ?? null,
    couleur: parsed.couleur ?? null,
    purete: parsed.purete ?? null,
    traitement: parsed.traitement ?? null,
    certification: parsed.certification ?? null,
    numeroCertificat: parsed.numeroCertificat ?? null,
    poincon: parsed.poincon ?? null,
    tags: parsed.tags ?? "",
  };
}

async function uniqueSlug(nom: string, sku: string, ignoreId?: string) {
  const base = slugify(`${nom}-${sku}`, { lower: true, strict: true });
  let slug = base;
  let i = 1;
  // Ensures the slug used in public URLs stays unique even if two products
  // share a similar name.
  while (
    await prisma.product.findFirst({
      where: { slug, ...(ignoreId ? { id: { not: ignoreId } } : {}) },
    })
  ) {
    slug = `${base}-${i++}`;
  }
  return slug;
}

async function saveProductPhotos(productId: string, formData: FormData) {
  const files = formData.getAll("photos").filter(
    (entry): entry is File => entry instanceof File && entry.size > 0
  );
  const existingCount = await prisma.productImage.count({
    where: { productId },
  });

  for (const [index, file] of files.entries()) {
    const url = await saveUploadedImage(file, `products/${productId}`);
    await prisma.productImage.create({
      data: { productId, url, position: existingCount + index },
    });
  }
}

export async function createProduct(formData: FormData) {
  const user = await requireUser();
  const data = parseProductForm(formData);
  const slug = await uniqueSlug(data.nom, data.sku);

  const product = await prisma.product.create({
    data: { ...data, slug, createdById: user.id, updatedById: user.id },
  });

  await saveProductPhotos(product.id, formData);

  await logAction({
    userId: user.id,
    action: "product.create",
    entityType: "Product",
    entityId: product.id,
    details: { nom: product.nom, sku: product.sku },
  });

  revalidatePath("/admin/produits");
  revalidatePath("/boutique");
  redirect("/admin/produits");
}

export async function updateProduct(productId: string, formData: FormData) {
  const user = await requireUser();
  const data = parseProductForm(formData);
  const existing = await prisma.product.findUniqueOrThrow({
    where: { id: productId },
  });

  const slug =
    existing.nom === data.nom && existing.sku === data.sku
      ? existing.slug
      : await uniqueSlug(data.nom, data.sku, productId);

  await prisma.product.update({
    where: { id: productId },
    data: { ...data, slug, updatedById: user.id },
  });

  await saveProductPhotos(productId, formData);

  await logAction({
    userId: user.id,
    action: "product.update",
    entityType: "Product",
    entityId: productId,
    details: { nom: data.nom, sku: data.sku },
  });

  revalidatePath("/admin/produits");
  revalidatePath(`/admin/produits/${productId}`);
  revalidatePath("/boutique");
  revalidatePath(`/boutique/${slug}`);
  redirect("/admin/produits");
}

export async function deleteProductImage(imageId: string) {
  const user = await requireUser();
  const image = await prisma.productImage.delete({ where: { id: imageId } });
  await logAction({
    userId: user.id,
    action: "product.image.delete",
    entityType: "Product",
    entityId: image.productId,
  });
  revalidatePath(`/admin/produits/${image.productId}`);
}

export async function deleteProduct(productId: string) {
  const user = await requireUser();
  const product = await prisma.product.delete({ where: { id: productId } });

  await logAction({
    userId: user.id,
    action: "product.delete",
    entityType: "Product",
    entityId: productId,
    details: { nom: product.nom, sku: product.sku },
  });

  revalidatePath("/admin/produits");
  revalidatePath("/boutique");
}
