"""Boucle continue de triage automatique des emails, à lancer en tâche de
fond (service systemd, `screen`/`tmux`, ou conteneur).

Usage (depuis le dossier backend/) : python -m scripts.run_triage_loop
"""
import time

from app import config, db, triage


def main() -> None:
    db.init_db()
    print(f"Triage automatique démarré, intervalle {config.TRIAGE_INTERVAL_SECONDS}s.")
    while True:
        summary = triage.run_triage_cycle()
        print(f"Cycle de triage : {summary}")
        time.sleep(config.TRIAGE_INTERVAL_SECONDS)


if __name__ == "__main__":
    main()
