import Link from "next/link";
import { prisma } from "@/lib/prisma";
import { CATEGORIE_LABELS, STATUT_LABELS, formatPrice } from "@/lib/format";
import { DeleteButton } from "@/components/admin/DeleteButton";
import { deleteProduct } from "@/lib/actions/products";

export default async function AdminProductsPage() {
  const products = await prisma.product.findMany({
    orderBy: { createdAt: "desc" },
  });

  return (
    <div>
      <div className="flex items-center justify-between">
        <h1 className="font-display text-2xl">Produits</h1>
        <Link href="/admin/produits/nouveau" className="btn-primary">
          + Nouveau produit
        </Link>
      </div>

      <div className="mt-6 overflow-x-auto">
        <table className="w-full min-w-[640px] border-collapse text-sm">
          <thead>
            <tr className="border-b border-border text-left text-xs uppercase tracking-widest text-muted">
              <th className="py-2 pr-4">Nom</th>
              <th className="py-2 pr-4">Catégorie</th>
              <th className="py-2 pr-4">Prix</th>
              <th className="py-2 pr-4">Statut</th>
              <th className="py-2 pr-4"></th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id} className="border-b border-border">
                <td className="py-3 pr-4">
                  <Link href={`/admin/produits/${product.id}`} className="hover:underline">
                    {product.nom}
                  </Link>
                  <p className="text-xs text-muted">{product.sku}</p>
                </td>
                <td className="py-3 pr-4">{CATEGORIE_LABELS[product.categorie]}</td>
                <td className="py-3 pr-4">{formatPrice(product.prixEur)}</td>
                <td className="py-3 pr-4">{STATUT_LABELS[product.statut]}</td>
                <td className="py-3 pr-4 text-right">
                  <DeleteButton
                    action={deleteProduct.bind(null, product.id)}
                    confirmMessage={`Supprimer « ${product.nom} » ?`}
                  />
                </td>
              </tr>
            ))}
            {products.length === 0 && (
              <tr>
                <td colSpan={5} className="py-6 text-center text-muted">
                  Aucun produit pour le moment.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
