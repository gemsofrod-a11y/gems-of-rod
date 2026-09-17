import { notFound } from "next/navigation";
import { prisma } from "@/lib/prisma";
import { ProductForm } from "@/components/admin/ProductForm";
import { updateProduct } from "@/lib/actions/products";

type Params = Promise<{ id: string }>;

export default async function EditProductPage({ params }: { params: Params }) {
  const { id } = await params;
  const product = await prisma.product.findUnique({
    where: { id },
    include: { images: { orderBy: { position: "asc" } } },
  });

  if (!product) notFound();

  return (
    <div>
      <h1 className="font-display text-2xl">Modifier « {product.nom} »</h1>
      <div className="mt-6 max-w-3xl">
        <ProductForm product={product} action={updateProduct.bind(null, product.id)} />
      </div>
    </div>
  );
}
