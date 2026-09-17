import { redirect } from "next/navigation";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";

export default async function AdminAuditLogPage() {
  const session = await auth();
  if (session?.user.role !== "ADMIN") redirect("/admin");

  const logs = await prisma.auditLog.findMany({
    orderBy: { createdAt: "desc" },
    take: 200,
    include: { user: true },
  });

  return (
    <div>
      <h1 className="font-display text-2xl">Journal d&apos;activité</h1>
      <p className="mt-2 text-sm text-muted">
        Historique des actions effectuées dans l&apos;espace professionnel
        (200 dernières entrées).
      </p>

      <div className="mt-6 overflow-x-auto">
        <table className="w-full min-w-[640px] border-collapse text-sm">
          <thead>
            <tr className="border-b border-border text-left text-xs uppercase tracking-widest text-muted">
              <th className="py-2 pr-4">Date</th>
              <th className="py-2 pr-4">Utilisateur</th>
              <th className="py-2 pr-4">Action</th>
              <th className="py-2 pr-4">Détails</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.id} className="border-b border-border align-top">
                <td className="py-2 pr-4 whitespace-nowrap text-xs text-muted">
                  {new Intl.DateTimeFormat("fr-FR", {
                    dateStyle: "short",
                    timeStyle: "short",
                  }).format(log.createdAt)}
                </td>
                <td className="py-2 pr-4">{log.user?.name ?? "Système"}</td>
                <td className="py-2 pr-4">
                  {log.action} <span className="text-muted">({log.entityType})</span>
                </td>
                <td className="py-2 pr-4 text-xs text-muted">{log.details}</td>
              </tr>
            ))}
            {logs.length === 0 && (
              <tr>
                <td colSpan={4} className="py-6 text-center text-muted">
                  Aucune activité enregistrée.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
