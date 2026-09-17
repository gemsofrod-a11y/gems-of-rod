import Link from "next/link";
import { prisma } from "@/lib/prisma";

export default async function AdminDashboardPage() {
  const [products, articles, newInquiries, recentLogs] = await Promise.all([
    prisma.product.count(),
    prisma.article.count(),
    prisma.inquiry.count({ where: { statut: "NOUVEAU" } }),
    prisma.auditLog.findMany({
      orderBy: { createdAt: "desc" },
      take: 8,
      include: { user: true },
    }),
  ]);

  return (
    <div>
      <h1 className="font-display text-2xl">Tableau de bord</h1>

      <div className="mt-8 grid grid-cols-1 gap-4 sm:grid-cols-3">
        <Link href="/admin/produits" className="border border-border bg-surface p-6">
          <p className="text-3xl font-display">{products}</p>
          <p className="mt-1 text-sm text-muted">Produits au catalogue</p>
        </Link>
        <Link href="/admin/articles" className="border border-border bg-surface p-6">
          <p className="text-3xl font-display">{articles}</p>
          <p className="mt-1 text-sm text-muted">Articles</p>
        </Link>
        <Link href="/admin/devis" className="border border-border bg-surface p-6">
          <p className="text-3xl font-display">{newInquiries}</p>
          <p className="mt-1 text-sm text-muted">Nouvelles demandes</p>
        </Link>
      </div>

      <div className="mt-10">
        <h2 className="font-display text-lg">Activité récente</h2>
        <ul className="mt-4 space-y-2 text-sm">
          {recentLogs.map((log) => (
            <li key={log.id} className="border-b border-border pb-2 text-muted">
              <span className="text-foreground">{log.user?.name ?? "Système"}</span>{" "}
              — {log.action} ({log.entityType})
              <span className="ml-2 text-xs">
                {new Intl.DateTimeFormat("fr-FR", {
                  dateStyle: "short",
                  timeStyle: "short",
                }).format(log.createdAt)}
              </span>
            </li>
          ))}
          {recentLogs.length === 0 && (
            <li className="text-muted">Aucune activité pour le moment.</li>
          )}
        </ul>
      </div>
    </div>
  );
}
