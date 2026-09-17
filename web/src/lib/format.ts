const eurFormatter = new Intl.NumberFormat("fr-FR", {
  style: "currency",
  currency: "EUR",
  maximumFractionDigits: 0,
});

export function formatPrice(value: number) {
  return eurFormatter.format(value);
}

export const CATEGORIE_LABELS: Record<string, string> = {
  PIERRE_PRECIEUSE: "Pierre précieuse",
  PIERRE_FINE: "Pierre fine",
  METAL_PRECIEUX: "Métal précieux",
  BIJOU: "Bijou",
};

export const STATUT_LABELS: Record<string, string> = {
  DISPONIBLE: "Disponible",
  RESERVE: "Réservé",
  VENDU: "Vendu",
};
