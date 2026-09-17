"use client";

import { useRef } from "react";
import { createUser } from "@/lib/actions/users";

const inputClass =
  "w-full border border-border bg-surface px-3 py-2 text-sm outline-none focus:border-foreground";

export function NewUserForm() {
  const formRef = useRef<HTMLFormElement>(null);

  return (
    <form
      ref={formRef}
      action={async (formData) => {
        await createUser(formData);
        formRef.current?.reset();
      }}
      className="grid grid-cols-1 gap-3 border border-border bg-surface p-5 sm:grid-cols-2"
    >
      <input name="name" required placeholder="Nom" className={inputClass} />
      <input
        type="email"
        name="email"
        required
        placeholder="Email"
        className={inputClass}
      />
      <input
        type="password"
        name="password"
        required
        minLength={8}
        placeholder="Mot de passe (8 caractères min.)"
        className={inputClass}
      />
      <select name="role" defaultValue="EDITEUR" className={inputClass}>
        <option value="EDITEUR">Éditeur</option>
        <option value="ADMIN">Administrateur</option>
      </select>
      <button type="submit" className="btn-primary sm:col-span-2">
        Créer le compte
      </button>
    </form>
  );
}
