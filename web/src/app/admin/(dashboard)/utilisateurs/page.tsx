import { redirect } from "next/navigation";
import { auth } from "@/auth";
import { prisma } from "@/lib/prisma";
import { UserRow } from "@/components/admin/UserRow";
import { NewUserForm } from "@/components/admin/NewUserForm";

export default async function AdminUsersPage() {
  const session = await auth();
  if (session?.user.role !== "ADMIN") redirect("/admin");

  const users = await prisma.user.findMany({ orderBy: { createdAt: "asc" } });

  return (
    <div>
      <h1 className="font-display text-2xl">Utilisateurs</h1>
      <p className="mt-2 text-sm text-muted">
        Un administrateur gère le catalogue, les articles, les utilisateurs et
        voit le journal d&apos;activité. Un éditeur gère le catalogue, les
        articles et les demandes.
      </p>

      <div className="mt-6 overflow-x-auto">
        <table className="w-full min-w-[560px] border-collapse text-sm">
          <thead>
            <tr className="border-b border-border text-left text-xs uppercase tracking-widest text-muted">
              <th className="py-2 pr-4">Nom</th>
              <th className="py-2 pr-4">Email</th>
              <th className="py-2 pr-4">Rôle</th>
              <th className="py-2 pr-4">Statut</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <UserRow key={user.id} user={user} isSelf={user.id === session.user.id} />
            ))}
          </tbody>
        </table>
      </div>

      <div className="mt-10">
        <h2 className="font-display text-lg">Ajouter un utilisateur</h2>
        <div className="mt-4">
          <NewUserForm />
        </div>
      </div>
    </div>
  );
}
