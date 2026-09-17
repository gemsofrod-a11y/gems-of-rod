import type { Metadata } from "next";
import { ContactForm } from "@/components/ContactForm";

export const metadata: Metadata = {
  title: "Contact",
  description: "Contactez Gems of Rod pour toute question ou demande de devis.",
};

export default function ContactPage() {
  return (
    <div className="mx-auto max-w-2xl px-6 py-16">
      <h1 className="font-display text-3xl">Contact</h1>
      <p className="mt-3 text-muted">
        Une question sur une pierre, un projet de bijou sur mesure, une
        estimation ? Écrivez-nous, nous vous répondons personnellement.
      </p>
      <div className="mt-8">
        <ContactForm />
      </div>
      <p className="mt-8 text-sm text-muted">
        Vous pouvez aussi nous écrire directement à{" "}
        <a href="mailto:gemsofrod@gmail.com" className="underline">
          gemsofrod@gmail.com
        </a>
        .
      </p>
    </div>
  );
}
