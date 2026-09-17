import { prisma } from "@/lib/prisma";

export async function logAction(params: {
  userId: string | null | undefined;
  action: string;
  entityType: string;
  entityId: string;
  details?: Record<string, unknown>;
}) {
  await prisma.auditLog.create({
    data: {
      userId: params.userId ?? null,
      action: params.action,
      entityType: params.entityType,
      entityId: params.entityId,
      details: params.details ? JSON.stringify(params.details) : null,
    },
  });
}
