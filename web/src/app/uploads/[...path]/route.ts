import { readFile, stat } from "node:fs/promises";
import path from "node:path";
import { NextResponse } from "next/server";
import { UPLOADS_ROOT } from "@/lib/uploads";

const CONTENT_TYPES: Record<string, string> = {
  ".jpg": "image/jpeg",
  ".jpeg": "image/jpeg",
  ".png": "image/png",
  ".webp": "image/webp",
};

type Params = Promise<{ path: string[] }>;

export async function GET(_request: Request, { params }: { params: Params }) {
  const { path: segments } = await params;

  // Resolve and confirm the path stays within the uploads root to prevent
  // path traversal via a crafted "../" segment.
  const resolved = path.resolve(UPLOADS_ROOT, ...segments);
  if (!resolved.startsWith(UPLOADS_ROOT + path.sep)) {
    return new NextResponse("Not found", { status: 404 });
  }

  try {
    await stat(resolved);
    const file = await readFile(resolved);
    const contentType =
      CONTENT_TYPES[path.extname(resolved).toLowerCase()] ??
      "application/octet-stream";

    return new NextResponse(new Uint8Array(file), {
      headers: {
        "Content-Type": contentType,
        "Cache-Control": "public, max-age=31536000, immutable",
      },
    });
  } catch {
    return new NextResponse("Not found", { status: 404 });
  }
}
