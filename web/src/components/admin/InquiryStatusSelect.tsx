"use client";

import { useTransition } from "react";
import { updateInquiryStatus } from "@/lib/actions/inquiries";

const OPTIONS = [
  { value: "NOUVEAU", label: "Nouveau" },
  { value: "EN_COURS", label: "En cours" },
  { value: "TRAITE", label: "Traité" },
] as const;

export function InquiryStatusSelect({
  id,
  statut,
}: {
  id: string;
  statut: string;
}) {
  const [pending, startTransition] = useTransition();

  return (
    <select
      defaultValue={statut}
      disabled={pending}
      onChange={(event) => {
        const value = event.target.value as (typeof OPTIONS)[number]["value"];
        startTransition(() => {
          updateInquiryStatus(id, value);
        });
      }}
      className="border border-border bg-surface px-3 py-1.5 text-xs uppercase tracking-widest"
    >
      {OPTIONS.map((option) => (
        <option key={option.value} value={option.value}>
          {option.label}
        </option>
      ))}
    </select>
  );
}
