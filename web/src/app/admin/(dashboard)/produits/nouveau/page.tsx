import { ProductForm } from "@/components/admin/ProductForm";
import { createProduct } from "@/lib/actions/products";

export default function NewProductPage() {
  return (
    <div>
      <h1 className="font-display text-2xl">Nouveau produit</h1>
      <div className="mt-6 max-w-3xl">
        <ProductForm action={createProduct} />
      </div>
    </div>
  );
}
