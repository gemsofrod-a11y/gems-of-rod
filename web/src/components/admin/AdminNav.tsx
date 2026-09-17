"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { logout } from "@/lib/actions/auth-actions";

const LINKS = [
  { href: "/admin", label: "Tableau de bord", exact: true },
  { href: "/admin/produits", label: "Produits" },
  { href: "/admin/articles", label: "Articles" },
  { href: "/admin/devis", label: "Demandes" },
  { href: "/admin/utilisateurs", label: "Utilisateurs", adminOnly: true },
  { href: "/admin/journal", label: "Journal d'activité", adminOnly: true },
];

export function AdminNav({ role }: { role: "ADMIN" | "EDITEUR" }) {
  const pathname = usePathname();

  return (
    <nav className="flex flex-col gap-1">
      {LINKS.filter((link) => !link.adminOnly || role === "ADMIN").map(
        (link) => {
          const active = link.exact
            ? pathname === link.href
            : pathname.startsWith(link.href);
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`px-3 py-2 text-sm ${
                active
                  ? "bg-foreground text-surface"
                  : "text-foreground/80 hover:bg-border/40"
              }`}
            >
              {link.label}
            </Link>
          );
        }
      )}
      <form action={logout} className="mt-4">
        <button
          type="submit"
          className="w-full px-3 py-2 text-left text-sm text-muted hover:text-foreground"
        >
          Déconnexion
        </button>
      </form>
    </nav>
  );
}
