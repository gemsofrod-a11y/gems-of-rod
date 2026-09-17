import { mkdir } from "node:fs/promises";
import path from "node:path";
import sharp from "sharp";

// Deliberately outside `public/`: Next.js only serves files that existed in
// `public/` at build/start time, so runtime uploads (photos added from the
// admin, including phone camera captures) live here instead and are served
// through the /uploads route handler (src/app/uploads/[...path]/route.ts).
export const UPLOADS_ROOT = path.join(process.cwd(), "data", "uploads");

/**
 * Persists an uploaded image (from a form, including a phone camera capture)
 * to data/uploads/<subdir>/, resized/compressed so multi-MB camera photos
 * stay reasonable. Returns the public URL path to store on the record.
 */
export async function saveUploadedImage(
  file: File,
  subdir: string
): Promise<string> {
  const bytes = Buffer.from(await file.arrayBuffer());
  const dir = path.join(UPLOADS_ROOT, subdir);
  await mkdir(dir, { recursive: true });

  const filename = `${Date.now()}-${Math.random().toString(36).slice(2, 8)}.jpg`;
  const destination = path.join(dir, filename);

  await sharp(bytes)
    .rotate() // respects EXIF orientation from phone cameras
    .resize(1800, 1800, { fit: "inside", withoutEnlargement: true })
    .jpeg({ quality: 82 })
    .toFile(destination);

  return `/uploads/${subdir}/${filename}`;
}
