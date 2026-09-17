import { v2 as cloudinary } from "cloudinary";
import sharp from "sharp";

cloudinary.config({
  cloud_name: process.env.CLOUDINARY_CLOUD_NAME,
  api_key: process.env.CLOUDINARY_API_KEY,
  api_secret: process.env.CLOUDINARY_API_SECRET,
});

/**
 * Persists an uploaded image (from a form, including a phone camera capture)
 * to Cloudinary, resized/compressed so multi-MB camera photos stay
 * reasonable. Returns the public HTTPS URL to store on the record.
 *
 * Cloudinary (rather than local disk) is required because the app runs on
 * serverless platforms (Netlify) with no persistent, writable filesystem.
 */
export async function saveUploadedImage(
  file: File,
  subdir: string
): Promise<string> {
  const bytes = Buffer.from(await file.arrayBuffer());

  const optimized = await sharp(bytes)
    .rotate() // respects EXIF orientation from phone cameras
    .resize(1800, 1800, { fit: "inside", withoutEnlargement: true })
    .jpeg({ quality: 82 })
    .toBuffer();

  const result = await new Promise<{ secure_url: string }>(
    (resolve, reject) => {
      const stream = cloudinary.uploader.upload_stream(
        { folder: `gems-of-rod/${subdir}`, resource_type: "image" },
        (error, uploadResult) => {
          if (error || !uploadResult) {
            reject(error ?? new Error("Échec de l'envoi vers Cloudinary"));
          } else {
            resolve(uploadResult);
          }
        }
      );
      stream.end(optimized);
    }
  );

  return result.secure_url;
}
