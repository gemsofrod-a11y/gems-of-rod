"use server";

import { revalidatePath } from "next/cache";
import { prisma } from "@/lib/prisma";
import { requireUser } from "@/lib/session";
import { logAction } from "@/lib/audit";
import { inquirySchema } from "@/lib/validation";

export type ContactState = { success: boolean; message: string } | null;

export async function submitInquiry(
  _prevState: ContactState,
  formData: FormData
): Promise<ContactState> {
  const raw = Object.fromEntries(formData.entries());
  const parsed = inquirySchema.safeParse(raw);

  if (!parsed.success) {
    return {
      success: false,
      message: "Merci de vérifier les champs du formulaire.",
    };
  }

  await prisma.inquiry.create({
    data: {
      nom: parsed.data.nom,
      email: parsed.data.email,
      telephone: parsed.data.telephone || null,
      message: parsed.data.message,
      productId: parsed.data.productId || null,
    },
  });

  revalidatePath("/admin/devis");

  return {
    success: true,
    message:
      "Votre demande a bien été transmise. Nous revenons vers vous très rapidement.",
  };
}

export async function updateInquiryStatus(
  inquiryId: string,
  statut: "NOUVEAU" | "EN_COURS" | "TRAITE"
) {
  const user = await requireUser();
  await prisma.inquiry.update({ where: { id: inquiryId }, data: { statut } });

  await logAction({
    userId: user.id,
    action: "inquiry.update-status",
    entityType: "Inquiry",
    entityId: inquiryId,
    details: { statut },
  });

  revalidatePath("/admin/devis");
}
