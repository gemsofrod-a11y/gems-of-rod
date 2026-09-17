import type { NextAuthConfig } from "next-auth";

/**
 * Config shared with the proxy (src/proxy.ts). Deliberately has NO
 * providers here: the Credentials provider pulls in Prisma + bcrypt, and
 * Netlify's Next.js Runtime refuses to bundle their native addons into the
 * proxy/middleware function ("Usage of unsupported C++ Addon(s) found in
 * Node.js Middleware"). The full provider list lives in src/auth.ts, used
 * only by the API route and server actions/components.
 */
export const authConfig = {
  trustHost: true,
  session: { strategy: "jwt" },
  pages: { signIn: "/admin/connexion" },
  providers: [],
  callbacks: {
    jwt({ token, user }) {
      if (user) {
        token.id = user.id;
        token.role = (user as { role: string }).role;
      }
      return token;
    },
    session({ session, token }) {
      if (session.user) {
        session.user.id = token.id as string;
        session.user.role = token.role as "ADMIN" | "EDITEUR";
      }
      return session;
    },
  },
} satisfies NextAuthConfig;
