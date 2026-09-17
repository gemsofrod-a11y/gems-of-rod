import { prisma } from "@/lib/prisma";
import { InquiryStatusSelect } from "@/components/admin/InquiryStatusSelect";

export default async function AdminInquiriesPage() {
  const inquiries = await prisma.inquiry.findMany({
    orderBy: { createdAt: "desc" },
    include: { product: true },
  });

  return (
    <div>
      <h1 className="font-display text-2xl">Demandes de devis / contact</h1>

      <div className="mt-6 space-y-4">
        {inquiries.map((inquiry) => (
          <div key={inquiry.id} className="border border-border bg-surface p-5">
            <div className="flex flex-wrap items-start justify-between gap-3">
              <div>
                <p className="font-medium">{inquiry.nom}</p>
                <p className="text-sm text-muted">
                  <a href={`mailto:${inquiry.email}`} className="underline">
                    {inquiry.email}
                  </a>
                  {inquiry.telephone && ` · ${inquiry.telephone}`}
                </p>
                {inquiry.product && (
                  <p className="mt-1 text-xs uppercase tracking-widest text-muted">
                    Concerne : {inquiry.product.nom}
                  </p>
                )}
              </div>
              <InquiryStatusSelect id={inquiry.id} statut={inquiry.statut} />
            </div>
            <p className="mt-3 text-sm">{inquiry.message}</p>
            <p className="mt-2 text-xs text-muted">
              {new Intl.DateTimeFormat("fr-FR", {
                dateStyle: "long",
                timeStyle: "short",
              }).format(inquiry.createdAt)}
            </p>
          </div>
        ))}
        {inquiries.length === 0 && (
          <p className="text-muted">Aucune demande pour le moment.</p>
        )}
      </div>
    </div>
  );
}
