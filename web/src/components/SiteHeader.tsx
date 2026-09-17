import Image from "next/image";
import Link from "next/link";

const NAV_LINKS = [
  { href: "/boutique", label: "Boutique" },
  { href: "/articles", label: "Journal" },
  { href: "/a-propos", label: "Notre histoire" },
  { href: "/contact", label: "Contact" },
];

export function SiteHeader() {
  return (
    <header className="border-b border-border">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-5">
        <Link href="/" className="flex items-center gap-3">
          <Image
            src="/icon-512.png"
            alt="Gems of Rod"
            width={40}
            height={40}
            className="h-10 w-10"
            priority
          />
          <span className="font-display text-xl tracking-wide">
            Gems of Rod
          </span>
        </Link>
        <nav className="hidden gap-8 md:flex">
          {NAV_LINKS.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className="text-xs uppercase tracking-[0.15em] text-foreground/80 hover:text-foreground"
            >
              {link.label}
            </Link>
          ))}
        </nav>
      </div>
    </header>
  );
}
