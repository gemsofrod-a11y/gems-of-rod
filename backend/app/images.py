"""Recherche d'images via Openverse (agrégateur d'images sous licence Creative
Commons, API publique gratuite, sans clé — https://openverse.org). Renvoie
des URLs directes utilisables telles quelles dans le client web, avec
l'attribution requise par les licences CC (créateur + source).
"""
import requests


def search_images(query: str, count: int = 3) -> list[dict]:
    count = max(1, min(count, 6))
    try:
        resp = requests.get(
            "https://api.openverse.org/v1/images/",
            params={"q": query, "page_size": count},
            timeout=10,
        )
        resp.raise_for_status()
        data = resp.json()
    except requests.exceptions.RequestException:
        return [{"error": "Service de recherche d'images indisponible pour le moment."}]

    results = data.get("results") or []
    if not results:
        return [{"error": f"Aucune image trouvée pour « {query} »."}]

    return [
        {
            "url": r.get("url"),
            "thumbnail": r.get("thumbnail") or r.get("url"),
            "title": r.get("title") or query,
            "creator": r.get("creator") or "",
            "source": r.get("source") or "",
            "license": r.get("license") or "",
        }
        for r in results[:count]
        if r.get("url")
    ]
