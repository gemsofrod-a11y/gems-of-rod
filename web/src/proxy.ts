import { NextResponse } from "next/server";
import { auth } from "@/auth";

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
