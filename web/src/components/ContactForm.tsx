"use client";

import { useActionState } from "react";
import { submitInquiry, type ContactState } from "@/lib/actions/inquiries";

const initialState: ContactState = null;

export function ContactForm({
  productId,
  productLabel,
}: {
  productId?: string;
  productLabel?: string;
}) {
  const [state, formAction, pending] = useActionState(
    submitInquiry,
    initialState
  );

  if (state?.success) {
    return (
      <p className="border border-border bg-surface p-4 text-sm">
        {state.message}
      </p>
    );
  }

  return (
    <form action={formAction} className="space-y-4">
      {productId && (
        <>
          <input type="hidden" name="productId" value={productId} />
          <p className="text-xs uppercase tracking-widest text-muted">
            Concernant : {productLabel}
          </p>
        </>
      )}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <input
          name="nom"
          required
          placeholder="Nom complet"
          className="border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
        />
        <input
          type="email"
          name="email"
          required
          placeholder="Email"
          className="border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
        />
      </div>
      <input
        name="telephone"
        placeholder="Téléphone (facultatif)"
        className="w-full border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
      />
      <textarea
        name="message"
        required
        rows={4}
        placeholder="Votre message"
        className="w-full border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
      />
      {state && !state.success && (
        <p className="text-sm text-red-700">{state.message}</p>
      )}
      <button type="submit" disabled={pending} className="btn-primary disabled:opacity-60">
        {pending ? "Envoi..." : "Envoyer la demande"}
      </button>
    </form>
  );
}
