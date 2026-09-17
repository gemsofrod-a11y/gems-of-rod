"use client";

export function DeleteButton({
  action,
  confirmMessage,
}: {
  action: () => Promise<void>;
  confirmMessage: string;
}) {
  return (
    <form
      action={async () => {
        if (window.confirm(confirmMessage)) {
          await action();
        }
      }}
    >
      <button
        type="submit"
        className="text-xs uppercase tracking-widest text-muted hover:text-red-700"
      >
        Supprimer
      </button>
    </form>
  );
}
