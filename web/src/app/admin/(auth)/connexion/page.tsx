import { Suspense } from "react";
import { LoginForm } from "@/components/admin/LoginForm";

export default function ConnexionPage() {
  return (
    <div>
      <h1 className="font-display text-2xl">Espace professionnel</h1>
      <p className="mt-2 text-sm text-muted">
        Connectez-vous pour gérer le catalogue, les articles et les demandes.
      </p>

      <div className="mt-8">
        <Suspense fallback={null}>
          <LoginForm />
        </Suspense>
      </div>
    </div>
  );
}
