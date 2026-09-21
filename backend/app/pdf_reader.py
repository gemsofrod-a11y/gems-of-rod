"""Extraction de texte depuis un PDF reçu en pièce jointe Gmail (devis
fournisseur, fiche technique, certificat...) — voir voice_agent.py, outil
read_pdf_attachment. Le texte extrait est donné à Saphir pour qu'elle puisse
en discuter avec Sébastien ; le PDF original est affiché tel quel côté client
(voir main.py /api/attachment) pour que Sébastien le lise lui-même en
parallèle.
"""
from io import BytesIO

from pypdf import PdfReader

_MAX_CHARS = 12000


def extract_text(pdf_bytes: bytes) -> str:
    try:
        reader = PdfReader(BytesIO(pdf_bytes))
    except Exception as e:
        return f"[Erreur de lecture du PDF : {e}]"

    pages = []
    for page in reader.pages:
        try:
            pages.append(page.extract_text() or "")
        except Exception:
            continue
    text = "\n".join(pages).strip()

    if not text:
        return ("[Aucun texte extrait de ce PDF — probablement un document scanné "
                "(image), sans texte sélectionnable. Il reste affiché à l'écran pour "
                "que Sébastien puisse le lire directement.]")
    if len(text) > _MAX_CHARS:
        text = text[:_MAX_CHARS] + "\n[...texte tronqué...]"
    return text
