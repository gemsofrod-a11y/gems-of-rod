"use client";

import { useSearchParams } from "next/navigation";
import { useActionState } from "react";
import { login, type LoginState } from "@/lib/actions/auth-actions";

const initialState: LoginState = { error: null };

export function LoginForm() {
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl") ?? "/admin";
  const [state, formAction, pending] = useActionState(login, initialState);

  return (
    <form action={formAction} className="space-y-4">
      <input type="hidden" name="callbackUrl" value={callbackUrl} />
      <input
        type="email"
        name="email"
        required
        placeholder="Email"
        className="w-full border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
      />
      <input
        type="password"
        name="password"
        required
        placeholder="Mot de passe"
        className="w-full border border-border bg-surface px-4 py-3 text-sm outline-none focus:border-foreground"
      />
      {state?.error && <p className="text-sm text-red-700">{state.error}</p>}
      <button
        type="submit"
        disabled={pending}
        className="btn-primary w-full disabled:opacity-60"
      >
        {pending ? "Connexion..." : "Se connecter"}
      </button>
    </form>
  );
}
