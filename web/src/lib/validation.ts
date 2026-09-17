import { z } from "zod";

const optionalNumber = z.preprocess(
  (val) => (val === "" || val === null || val === undefined ? undefined : val),
  z.coerce.number().optional()
);

const optionalString = z.preprocess(
  (val) => (val === "" || val === null || val === undefined ? undefined : val),
  z.string().optional()
);

export const categorieOptions = [
  { value: "PIERRE_PRECIEUSE", label: "Pierre précieuse" },
  { value: "PIERRE_FINE", label: "Pierre fine" },
  { value: "METAL_PRECIEUX", label: "Métal précieux" },
  { value: "BIJOU", label: "Bijou" },
] as const;

export const statutProduitOptions = [
  { value: "DISPONIBLE", label: "Disponible" },
  { value: "RESERVE", label: "Réservé" },
  { value: "VENDU", label: "Vendu" },
] as const;

export const productSchema = z.object({
  sku: z.string().min(1, "Référence requise"),
  nom: z.string().min(1, "Nom requis"),
  categorie: z.enum([
    "PIERRE_PRECIEUSE",
    "PIERRE_FINE",
    "METAL_PRECIEUX",
    "BIJOU",
  ]),
  descriptionCourte: z.string().min(1, "Description courte requise"),
  descriptionLongue: z.string().min(1, "Description longue requise"),
  prixEur: z.coerce.number().int().min(0),
  poidsCarats: optionalNumber,
  poidsGrammes: optionalNumber,
  origine: optionalString,
  couleur: optionalString,
  purete: optionalString,
  traitement: optionalString,
  certification: optionalString,
  numeroCertificat: optionalString,
  poincon: optionalString,
  tags: optionalString,
  statut: z.enum(["DISPONIBLE", "RESERVE", "VENDU"]),
});

export const articleSchema = z.object({
  titre: z.string().min(1, "Titre requis"),
  extrait: z.string().min(1, "Extrait requis"),
  contenu: z.string().min(1, "Contenu requis"),
  statut: z.enum(["BROUILLON", "PUBLIE"]),
});

export const inquirySchema = z.object({
  nom: z.string().min(1, "Votre nom est requis"),
  email: z.string().email("Email invalide"),
  telephone: z.string().optional(),
  message: z.string().min(1, "Votre message est requis"),
  productId: z.string().optional(),
});

export const userSchema = z.object({
  name: z.string().min(1, "Nom requis"),
  email: z.string().email("Email invalide"),
  role: z.enum(["ADMIN", "EDITEUR"]),
  password: z.string().min(8, "8 caractères minimum").optional(),
});
