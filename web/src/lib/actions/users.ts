"use server";

import { revalidatePath } from "next/cache";
import bcrypt from "bcryptjs";
import { prisma } from "@/lib/prisma";
import { requireAdmin } from "@/lib/session";
import { logAction } from "@/lib/audit";
import { userSchema } from "@/lib/validation";

export async function createUser(formData: FormData) {
  const admin = await requireAdmin();
  const raw = Object.fromEntries(formData.entries());
  const data = userSchema.extend({
    password: userSchema.shape.password.unwrap(),
  }).parse(raw);

  const user = await prisma.user.create({
    data: {
      name: data.name,
      email: data.email,
      role: data.role,
      passwordHash: await bcrypt.hash(data.password, 12),
    },
  });

  await logAction({
    userId: admin.id,
    action: "user.create",
    entityType: "User",
    entityId: user.id,
    details: { email: user.email, role: user.role },
  });

  revalidatePath("/admin/utilisateurs");
}

export async function setUserActive(userId: string, active: boolean) {
  const admin = await requireAdmin();
  if (userId === admin.id && !active) {
    throw new Error("Vous ne pouvez pas désactiver votre propre compte.");
  }

  await prisma.user.update({ where: { id: userId }, data: { active } });

  await logAction({
    userId: admin.id,
    action: active ? "user.activate" : "user.deactivate",
    entityType: "User",
    entityId: userId,
  });

  revalidatePath("/admin/utilisateurs");
}

export async function setUserRole(userId: string, role: "ADMIN" | "EDITEUR") {
  const admin = await requireAdmin();
  await prisma.user.update({ where: { id: userId }, data: { role } });

  await logAction({
    userId: admin.id,
    action: "user.role-change",
    entityType: "User",
    entityId: userId,
    details: { role },
  });

  revalidatePath("/admin/utilisateurs");
}
