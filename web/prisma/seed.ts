import { PrismaClient, Categorie, StatutProduit } from "@prisma/client";
import bcrypt from "bcryptjs";
import slugify from "slugify";
import fs from "node:fs";
import path from "node:path";

const prisma = new PrismaClient();

type LegacyProduct = {
  id: string;
  nom: string;
  categorie: string;
  description_courte: string;
  description_longue: string;
  prix_eur: number;
  poids_carats?: number;
  poids_grammes?: number;
  origine?: string;
  couleur?: string;
  purete?: string;
  traitement?: string | null;
  certification?: string;
  numero_certificat?: string;
  poincon?: string;
  tags?: string[];
  statut?: string;
  date_ajout?: string;
};

const CATEGORIE_MAP: Record<string, Categorie> = {
  pierre_precieuse: Categorie.PIERRE_PRECIEUSE,
  pierre_fine: Categorie.PIERRE_FINE,
  metal_precieux: Categorie.METAL_PRECIEUX,
  bijou: Categorie.BIJOU,
};

const STATUT_MAP: Record<string, StatutProduit> = {
  disponible: StatutProduit.DISPONIBLE,
  reserve: StatutProduit.RESERVE,
  vendu: StatutProduit.VENDU,
};

async function main() {
  const adminEmail = process.env.SEED_ADMIN_EMAIL ?? "gemsofrod@gmail.com";
  const adminPassword = process.env.SEED_ADMIN_PASSWORD ?? "changez-moi-immediatement";

  const admin = await prisma.user.upsert({
    where: { email: adminEmail },
    update: {},
    create: {
      name: "Sébastien",
      email: adminEmail,
      passwordHash: await bcrypt.hash(adminPassword, 12),
      role: "ADMIN",
    },
  });

  console.log(`Compte administrateur prêt : ${admin.email}`);
  if (!process.env.SEED_ADMIN_PASSWORD) {
    console.log(
      `Mot de passe temporaire : "${adminPassword}" — changez-le dès la première connexion (Admin > Utilisateurs).`
    );
  }

  const legacyPath = path.resolve(
    __dirname,
    "..",
    "..",
    "agent",
    "knowledge",
    "products.json"
  );

  if (!fs.existsSync(legacyPath)) {
    console.log("Pas de catalogue existant trouvé (agent/knowledge/products.json), on ignore l'import.");
    return;
  }

  const legacyProducts: LegacyProduct[] = JSON.parse(
    fs.readFileSync(legacyPath, "utf-8")
  );

  for (const p of legacyProducts) {
    const slug = slugify(`${p.nom}-${p.id}`, { lower: true, strict: true });
    await prisma.product.upsert({
      where: { sku: p.id },
      update: {},
      create: {
        sku: p.id,
        slug,
        nom: p.nom,
        categorie: CATEGORIE_MAP[p.categorie] ?? Categorie.PIERRE_FINE,
        descriptionCourte: p.description_courte,
        descriptionLongue: p.description_longue,
        prixEur: p.prix_eur,
        poidsCarats: p.poids_carats ?? null,
        poidsGrammes: p.poids_grammes ?? null,
        origine: p.origine ?? null,
        couleur: p.couleur ?? null,
        purete: p.purete ?? null,
        traitement: p.traitement ?? null,
        certification: p.certification ?? null,
        numeroCertificat: p.numero_certificat ?? null,
        poincon: p.poincon ?? null,
        tags: (p.tags ?? []).join(","),
        statut: STATUT_MAP[p.statut ?? "disponible"] ?? StatutProduit.DISPONIBLE,
        dateAjout: p.date_ajout ? new Date(p.date_ajout) : new Date(),
        createdById: admin.id,
      },
    });
  }

  console.log(`Catalogue importé : ${legacyProducts.length} pierre(s)/lingot(s).`);
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
