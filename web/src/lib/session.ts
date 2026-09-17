import { auth } from "@/auth";

export async function requireUser() {
  const session = await auth();
  if (!session?.user) {
    throw new Error("Non authentifié");
  }
  return session.user;
}

export async function requireAdmin() {
  const user = await requireUser();
  if (user.role !== "ADMIN") {
    throw new Error("Réservé aux administrateurs");
  }
  return user;
}
