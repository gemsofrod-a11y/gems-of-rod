"""Flux OAuth unique pour autoriser l'assistant à accéder à gemsofrod@gmail.com.

Prérequis : créer un client OAuth de type « Application de bureau » dans Google
Cloud Console (API Gmail activée) et enregistrer le fichier téléchargé sous
backend/credentials.json (voir backend/README.md).

Usage (depuis le dossier backend/) : python -m scripts.gmail_oauth_setup
"""
from google_auth_oauthlib.flow import InstalledAppFlow

from app import config


def main() -> None:
    if not config.CREDENTIALS_PATH.exists():
        raise SystemExit(
            f"Fichier introuvable : {config.CREDENTIALS_PATH}\n"
            "Téléchargez vos identifiants OAuth (type « Application de bureau ») depuis "
            "Google Cloud Console et enregistrez-les à cet emplacement."
        )
    flow = InstalledAppFlow.from_client_secrets_file(
        str(config.CREDENTIALS_PATH), config.GMAIL_SCOPES
    )
    creds = flow.run_local_server(port=0)
    config.TOKEN_PATH.write_text(creds.to_json(), encoding="utf-8")
    print(f"Authentification réussie. Jeton enregistré dans {config.TOKEN_PATH}")


if __name__ == "__main__":
    main()
