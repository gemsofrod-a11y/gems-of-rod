import { redirect } from "next/navigation";
import { auth } from "@/auth";
import { AdminNav } from "@/components/admin/AdminNav";

export default async function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const session = await auth();
  if (!session?.user) {
    redirect("/admin/connexion");
  }

  return (
    <div className="mx-auto flex max-w-6xl flex-col gap-8 px-6 py-10 md:flex-row">
      <aside className="md:w-56 md:flex-shrink-0">
        <p className="mb-4 text-xs uppercase tracking-widest text-muted">
          {session.user.name} · {session.user.role === "ADMIN" ? "Administrateur" : "Éditeur"}
        </p>
        <AdminNav role={session.user.role} />
      </aside>
      <div className="flex-1 min-w-0">{children}</div>
    </div>
  );
}
