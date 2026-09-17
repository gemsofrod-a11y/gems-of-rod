"use client";

import { useState } from "react";
import Image from "next/image";
import type { Product, ProductImage } from "@prisma/client";
import { categorieOptions, statutProduitOptions } from "@/lib/validation";
import { deleteProductImage } from "@/lib/actions/products";

type Props = {
  product?: Product & { images: ProductImage[] };
  action: (formData: FormData) => void;
};

function Field({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <label className="block">
      <span className="text-xs uppercase tracking-widest text-muted">
        {label}
      </span>
      <div className="mt-1">{children}</div>
    </label>
  );
}

const inputClass =
  "w-full border border-border bg-surface px-3 py-2 text-sm outline-none focus:border-foreground";

export function ProductForm({ product, action }: Props) {
  const [images, setImages] = useState(product?.images ?? []);

  return (
    <form action={action} className="space-y-8">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <Field label="Référence (SKU)">
          <input
            name="sku"
            required
            defaultValue={product?.sku}
            className={inputClass}
          />
        </Field>
        <Field label="Nom">
          <input
            name="nom"
            required
            defaultValue={product?.nom}
            className={inputClass}
          />
        </Field>
        <Field label="Catégorie">
          <select
            name="categorie"
            defaultValue={product?.categorie ?? "PIERRE_PRECIEUSE"}
            className={inputClass}
          >
            {categorieOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Statut">
          <select
            name="statut"
            defaultValue={product?.statut ?? "DISPONIBLE"}
            className={inputClass}
          >
            {statutProduitOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Prix (EUR)">
          <input
            type="number"
            name="prixEur"
            required
            min={0}
            defaultValue={product?.prixEur}
            className={inputClass}
          />
        </Field>
        <Field label="Poids (carats)">
          <input
            type="number"
            step="0.01"
            name="poidsCarats"
            defaultValue={product?.poidsCarats ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Poids (grammes)">
          <input
            type="number"
            step="0.01"
            name="poidsGrammes"
            defaultValue={product?.poidsGrammes ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Origine">
          <input
            name="origine"
            defaultValue={product?.origine ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Couleur">
          <input
            name="couleur"
            defaultValue={product?.couleur ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Pureté">
          <input
            name="purete"
            defaultValue={product?.purete ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Traitement">
          <input
            name="traitement"
            defaultValue={product?.traitement ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Certification">
          <input
            name="certification"
            defaultValue={product?.certification ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="N° de certificat">
          <input
            name="numeroCertificat"
            defaultValue={product?.numeroCertificat ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Poinçon">
          <input
            name="poincon"
            defaultValue={product?.poincon ?? undefined}
            className={inputClass}
          />
        </Field>
        <Field label="Tags (séparés par des virgules)">
          <input
            name="tags"
            defaultValue={product?.tags ?? undefined}
            className={inputClass}
          />
        </Field>
      </div>

      <Field label="Description courte">
        <textarea
          name="descriptionCourte"
          required
          rows={2}
          defaultValue={product?.descriptionCourte}
          className={inputClass}
        />
      </Field>

      <Field label="Description longue">
        <textarea
          name="descriptionLongue"
          required
          rows={5}
          defaultValue={product?.descriptionLongue}
          className={inputClass}
        />
      </Field>

      <div className="border-t border-border pt-6">
        <p className="text-xs uppercase tracking-widest text-muted">Photos</p>

        {images.length > 0 && (
          <div className="mt-3 grid grid-cols-3 gap-3 sm:grid-cols-5">
            {images.map((image) => (
              <div key={image.id} className="relative aspect-square">
                <Image
                  src={image.url}
                  alt=""
                  fill
                  sizes="20vw"
                  className="border border-border object-cover"
                />
                <button
                  type="button"
                  onClick={async () => {
                    await deleteProductImage(image.id);
                    setImages((prev) => prev.filter((i) => i.id !== image.id));
                  }}
                  className="absolute right-1 top-1 bg-foreground px-2 py-0.5 text-[10px] uppercase text-surface"
                >
                  Retirer
                </button>
              </div>
            ))}
          </div>
        )}

        <div className="mt-4 grid grid-cols-1 gap-4 sm:grid-cols-2">
          <label className="flex flex-col items-center justify-center gap-2 border border-dashed border-border p-6 text-center text-sm">
            <span>📷 Prendre une photo</span>
            <span className="text-xs text-muted">
              Ouvre l&apos;appareil photo du téléphone
            </span>
            <input
              type="file"
              name="photos"
              accept="image/*"
              capture="environment"
              className="w-full text-xs"
            />
          </label>
          <label className="flex flex-col items-center justify-center gap-2 border border-dashed border-border p-6 text-center text-sm">
            <span>🖼️ Depuis la galerie</span>
            <span className="text-xs text-muted">Une ou plusieurs photos existantes</span>
            <input
              type="file"
              name="photos"
              accept="image/*"
              multiple
              className="w-full text-xs"
            />
          </label>
        </div>
      </div>

      <button type="submit" className="btn-primary">
        Enregistrer
      </button>
    </form>
  );
}
