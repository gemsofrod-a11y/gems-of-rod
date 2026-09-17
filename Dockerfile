# Image de production pour la boutique Gems of Rod (web/).
# Construit depuis la racine du dépôt pour garder la même disposition
# relative que le développement local (web/ à côté de agent/), dont
# web/prisma/seed.ts dépend pour importer le catalogue existant.

FROM node:22-slim AS base
# openssl est requis par le moteur de requêtes Prisma
RUN apt-get update -y && apt-get install -y openssl && rm -rf /var/lib/apt/lists/*

FROM base AS deps
WORKDIR /app/web
COPY web/package.json web/package-lock.json ./
# schema.prisma doit être présent avant "npm ci" : le script "postinstall"
# du package lance "prisma generate", qui a besoin du schéma.
COPY web/prisma ./prisma
RUN npm ci

FROM base AS builder
WORKDIR /app/web
COPY --from=deps /app/web/node_modules ./node_modules
COPY web/. .
COPY agent/knowledge/products.json /app/agent/knowledge/products.json
RUN npx prisma generate
RUN npm run build

FROM base AS runner
WORKDIR /app/web
ENV NODE_ENV=production
COPY --from=builder /app/web ./
COPY --from=builder /app/agent /app/agent

EXPOSE 3000
CMD ["sh", "-c", "mkdir -p data/db data/uploads && npx prisma migrate deploy && npm run start"]
