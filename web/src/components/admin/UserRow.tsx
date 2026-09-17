"use client";

import { useTransition } from "react";
import type { User } from "@prisma/client";
import { setUserActive, setUserRole } from "@/lib/actions/users";

export function UserRow({
  user,
  isSelf,
}: {
  user: User;
  isSelf: boolean;
}) {
  const [pending, startTransition] = useTransition();

  return (
    <tr className="border-b border-border">
      <td className="py-3 pr-4">
        {user.name}
        {isSelf && <span className="ml-2 text-xs text-muted">(vous)</span>}
      </td>
      <td className="py-3 pr-4">{user.email}</td>
      <td className="py-3 pr-4">
        <select
          defaultValue={user.role}
          disabled={pending}
          onChange={(event) =>
            startTransition(() =>
              setUserRole(user.id, event.target.value as "ADMIN" | "EDITEUR")
            )
          }
          className="border border-border bg-surface px-2 py-1 text-xs uppercase tracking-widest"
        >
          <option value="ADMIN">Administrateur</option>
          <option value="EDITEUR">Éditeur</option>
        </select>
      </td>
      <td className="py-3 pr-4">
        <button
          type="button"
          disabled={pending || isSelf}
          onClick={() => startTransition(() => setUserActive(user.id, !user.active))}
          className="text-xs uppercase tracking-widest text-muted hover:text-foreground disabled:opacity-40"
        >
          {user.active ? "Désactiver" : "Réactiver"}
        </button>
      </td>
    </tr>
  );
}
