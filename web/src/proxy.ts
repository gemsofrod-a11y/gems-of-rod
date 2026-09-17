import NextAuth from "next-auth";
import { NextResponse } from "next/server";
import { authConfig } from "@/auth.config";

// Uses the lightweight, provider-less config (see auth.config.ts) so the
// proxy bundle never pulls in Prisma/bcrypt — required for Netlify's
// Next.js Runtime, which rejects native addons in proxy/middleware code.
const { auth } = NextAuth(authConfig);

export default auth((req) => {
  const { pathname } = req.nextUrl;
  const isAuthRoute = pathname === "/admin/connexion";
  const isAdminRoute = pathname.startsWith("/admin");

  if (isAdminRoute && !isAuthRoute && !req.auth) {
    const url = new URL("/admin/connexion", req.nextUrl.origin);
    url.searchParams.set("callbackUrl", pathname);
    return NextResponse.redirect(url);
  }
});

export const config = {
  matcher: ["/admin/:path*"],
};
