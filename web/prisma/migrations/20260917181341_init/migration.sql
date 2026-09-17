-- CreateTable
CREATE TABLE "User" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "name" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "passwordHash" TEXT NOT NULL,
    "role" TEXT NOT NULL DEFAULT 'EDITEUR',
    "active" BOOLEAN NOT NULL DEFAULT true,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" DATETIME NOT NULL
);

-- CreateTable
CREATE TABLE "Product" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "sku" TEXT NOT NULL,
    "slug" TEXT NOT NULL,
    "nom" TEXT NOT NULL,
    "categorie" TEXT NOT NULL,
    "descriptionCourte" TEXT NOT NULL,
    "descriptionLongue" TEXT NOT NULL,
    "prixEur" INTEGER NOT NULL,
    "poidsCarats" REAL,
    "poidsGrammes" REAL,
    "origine" TEXT,
    "couleur" TEXT,
    "purete" TEXT,
    "traitement" TEXT,
    "certification" TEXT,
    "numeroCertificat" TEXT,
    "poincon" TEXT,
    "tags" TEXT NOT NULL DEFAULT '',
    "statut" TEXT NOT NULL DEFAULT 'DISPONIBLE',
    "dateAjout" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" DATETIME NOT NULL,
    "createdById" TEXT,
    "updatedById" TEXT,
    CONSTRAINT "Product_createdById_fkey" FOREIGN KEY ("createdById") REFERENCES "User" ("id") ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT "Product_updatedById_fkey" FOREIGN KEY ("updatedById") REFERENCES "User" ("id") ON DELETE SET NULL ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "ProductImage" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "productId" TEXT NOT NULL,
    "url" TEXT NOT NULL,
    "position" INTEGER NOT NULL DEFAULT 0,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "ProductImage_productId_fkey" FOREIGN KEY ("productId") REFERENCES "Product" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Article" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "slug" TEXT NOT NULL,
    "titre" TEXT NOT NULL,
    "extrait" TEXT NOT NULL,
    "contenu" TEXT NOT NULL,
    "coverImageUrl" TEXT,
    "statut" TEXT NOT NULL DEFAULT 'BROUILLON',
    "publishedAt" DATETIME,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" DATETIME NOT NULL,
    "authorId" TEXT,
    CONSTRAINT "Article_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES "User" ("id") ON DELETE SET NULL ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Inquiry" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "nom" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "telephone" TEXT,
    "message" TEXT NOT NULL,
    "statut" TEXT NOT NULL DEFAULT 'NOUVEAU',
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "productId" TEXT,
    CONSTRAINT "Inquiry_productId_fkey" FOREIGN KEY ("productId") REFERENCES "Product" ("id") ON DELETE SET NULL ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "AuditLog" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "userId" TEXT,
    "action" TEXT NOT NULL,
    "entityType" TEXT NOT NULL,
    "entityId" TEXT NOT NULL,
    "details" TEXT,
    "createdAt" DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "AuditLog_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User" ("id") ON DELETE SET NULL ON UPDATE CASCADE
);

-- CreateIndex
CREATE UNIQUE INDEX "User_email_key" ON "User"("email");

-- CreateIndex
CREATE UNIQUE INDEX "Product_sku_key" ON "Product"("sku");

-- CreateIndex
CREATE UNIQUE INDEX "Product_slug_key" ON "Product"("slug");

-- CreateIndex
CREATE UNIQUE INDEX "Article_slug_key" ON "Article"("slug");
