import Link from "next/link";

export function SiteFooter() {
  return (
    <footer className="border-t border-border">
      <div className="mx-auto flex max-w-6xl flex-col gap-4 px-6 py-10 text-sm text-muted md:flex-row md:items-center md:justify-between">
        <p>© {new Date().getFullYear()} Gems of Rod — France</p>
        <div className="flex flex-wrap gap-x-6 gap-y-2">
          <Link href="/a-propos" className="hover:text-foreground">
            Notre histoire
          </Link>
          <Link href="/contact" className="hover:text-foreground">
            Contact
          </Link>
          <a
            href="mailto:gemsofrod@gmail.com"
            className="hover:text-foreground"
          >
            gemsofrod@gmail.com
          </a>
          <a
            href="https://instagram.com/gemsofrod"
            target="_blank"
            rel="noreferrer"
            className="hover:text-foreground"
          >
            @gemsofrod
          </a>
          <Link href="/admin" className="hover:text-foreground">
            Espace pro
          </Link>
        </div>
      </div>
    </footer>
  );
}
