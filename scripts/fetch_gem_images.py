#!/usr/bin/env python3
"""
Récupère une photo librement réutilisable (Wikimedia Commons) pour chaque
gemme de l'encyclopédie, et génère :
  - les fichiers image dans android/app/src/main/res/drawable-nodpi/
  - android/app/src/main/java/fr/gemsofrod/encyclopedie/data/GemImages.kt
    (mapping id de gemme -> ressource + crédit photographique)
  - android/IMAGE_FETCH_REPORT.md (rapport lisible pour revue humaine)

N'accepte que les licences librement réutilisables (domaine public, CC0,
CC BY, CC BY-SA) — jamais NC (non commercial) ni ND (pas de modification).

Ce script est conçu pour tourner dans GitHub Actions (accès réseau complet),
pas dans le bac à sable de développement qui bloque les hôtes d'images.
"""

import html
import json
import re
import sys
import time
import urllib.parse
import urllib.request
from pathlib import Path
from typing import Optional

API_URL = "https://commons.wikimedia.org/w/api.php"
USER_AGENT = "GemsOfRodEncyclopedieBot/1.0 (contact: gemsofrod@gmail.com)"

REPO_ROOT = Path(__file__).resolve().parent.parent
DRAWABLE_DIR = REPO_ROOT / "android/app/src/main/res/drawable-nodpi"
KOTLIN_OUT = REPO_ROOT / "android/app/src/main/java/fr/gemsofrod/encyclopedie/data/GemImages.kt"
REPORT_OUT = REPO_ROOT / "android/IMAGE_FETCH_REPORT.md"

# id (doit correspondre à Gem.id dans GemsRepository.kt), requête de recherche, mots-clés de pertinence
GEMS = [
    ("rubis", "Ruby gemstone", ["ruby"]),
    ("grenat-almandin", "Almandine garnet gemstone", ["almandine", "garnet"]),
    ("spinelle-rouge", "Red spinel gemstone", ["spinel"]),
    ("tourmaline-rubellite", "Rubellite tourmaline gemstone", ["tourmaline", "rubellite"]),
    ("grenat-rhodolite", "Rhodolite garnet gemstone", ["rhodolite", "garnet"]),
    ("grenat-spessartite", "Spessartite garnet gemstone", ["spessartite", "garnet"]),
    ("topaze-imperiale", "Imperial topaz gemstone", ["topaz"]),
    ("hessonite", "Hessonite garnet gemstone", ["hessonite", "garnet"]),
    ("opale-de-feu", "Fire opal gemstone", ["opal"]),
    ("saphir-jaune", "Yellow sapphire gemstone", ["sapphire"]),
    ("citrine", "Citrine gemstone", ["citrine"]),
    ("heliodore", "Heliodor beryl gemstone", ["heliodor", "beryl"]),
    ("chrysoberyl", "Chrysoberyl gemstone", ["chrysoberyl"]),
    ("emeraude", "Emerald gemstone", ["emerald"]),
    ("peridot", "Peridot gemstone", ["peridot", "olivine"]),
    ("tsavorite", "Tsavorite garnet gemstone", ["tsavorite", "garnet"]),
    ("jade-jadeite", "Jadeite jade gemstone", ["jadeite", "jade"]),
    ("tourmaline-verte", "Green tourmaline gemstone", ["tourmaline"]),
    ("saphir-bleu", "Blue sapphire gemstone", ["sapphire"]),
    ("aigue-marine", "Aquamarine gemstone", ["aquamarine"]),
    ("tanzanite", "Tanzanite gemstone", ["tanzanite"]),
    ("lapis-lazuli", "Lapis lazuli mineral", ["lapis"]),
    ("spinelle-bleu", "Blue spinel gemstone", ["spinel"]),
    ("amethyste", "Amethyst gemstone", ["amethyst"]),
    ("spinelle-violet", "Purple spinel gemstone", ["spinel"]),
    ("iolite", "Iolite cordierite gemstone", ["iolite", "cordierite"]),
    ("grenat-rhodolite-violet", "Purple rhodolite garnet gemstone", ["rhodolite", "garnet"]),
    ("morganite", "Morganite beryl gemstone", ["morganite"]),
    ("kunzite", "Kunzite gemstone", ["kunzite"]),
    ("saphir-rose", "Pink sapphire gemstone", ["sapphire"]),
    ("tourmaline-rose", "Pink tourmaline gemstone", ["tourmaline"]),
    ("rhodochrosite", "Rhodochrosite mineral", ["rhodochrosite"]),
    ("diamant", "Diamond gemstone", ["diamond"]),
    ("zircon-blanc", "Colorless zircon gemstone", ["zircon"]),
    ("goshenite", "Goshenite beryl gemstone", ["goshenite", "beryl"]),
    ("cristal-de-roche", "Rock crystal quartz", ["quartz", "rock crystal"]),
    ("onyx", "Onyx gemstone", ["onyx"]),
    ("spinelle-noir", "Black spinel gemstone", ["spinel"]),
    ("tourmaline-noire", "Schorl tourmaline", ["schorl", "tourmaline"]),
    ("obsidienne", "Obsidian volcanic glass", ["obsidian"]),
    ("alexandrite", "Alexandrite gemstone", ["alexandrite"]),
    ("opale-precieuse", "Precious opal gemstone", ["opal"]),
    ("tourmaline-pasteque", "Watermelon tourmaline gemstone", ["tourmaline", "watermelon"]),
    ("labradorite", "Labradorite gemstone", ["labradorite"]),
]

EXCLUDED_TITLE_TOKENS = [
    "logo", "map", "diagram", "icon", "chart", "graph",
    "locality", "location", "mine entrance", "mining site",
    "crystal structure", "flag", "coat of arms", "stamp",
]

ALLOWED_LICENSE_RE = re.compile(
    r"^(cc0|public domain|pd|cc[\s-]?by(?:[\s-]?sa)?[\s-]?\d(?:\.\d)?)",
    re.IGNORECASE,
)
DISALLOWED_LICENSE_TOKENS = ("nc", "nd")


def api_get(params: dict) -> dict:
    params = {**params, "format": "json"}
    url = f"{API_URL}?{urllib.parse.urlencode(params)}"
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read().decode("utf-8"))


def strip_html(raw: str) -> str:
    if not raw:
        return ""
    text = re.sub(r"<[^>]+>", "", raw)
    return html.unescape(text).strip()


def license_is_free(license_short: str) -> bool:
    normalized = license_short.strip().lower()
    if not normalized:
        return False
    tokens = re.split(r"[\s-]+", normalized)
    if any(tok in DISALLOWED_LICENSE_TOKENS for tok in tokens):
        return False
    return bool(ALLOWED_LICENSE_RE.match(normalized))


def search_candidates(query: str, limit: int = 8) -> list:
    data = api_get({
        "action": "query",
        "list": "search",
        "srsearch": query,
        "srnamespace": 6,
        "srlimit": limit,
    })
    return [item["title"] for item in data.get("query", {}).get("search", [])]


def image_info(title: str) -> Optional[dict]:
    data = api_get({
        "action": "query",
        "titles": title,
        "prop": "imageinfo",
        "iiprop": "url|extmetadata|size",
        "iiurlwidth": 1024,
    })
    pages = data.get("query", {}).get("pages", {})
    for page in pages.values():
        infos = page.get("imageinfo")
        if infos:
            return infos[0]
    return None


def pick_image(gem_id: str, query: str, keywords: list) -> Optional[dict]:
    for title in search_candidates(query):
        lower_title = title.lower()
        if any(tok in lower_title for tok in EXCLUDED_TITLE_TOKENS):
            continue
        if not any(kw in lower_title for kw in keywords):
            # on tente quand même : le titre seul est parfois peu explicite,
            # on vérifiera la description ci-dessous.
            pass
        try:
            info = image_info(title)
        except Exception as exc:  # noqa: BLE001
            print(f"  [!] erreur imageinfo pour {title}: {exc}", file=sys.stderr)
            continue
        if not info:
            continue

        extmeta = info.get("extmetadata", {})
        license_short = extmeta.get("LicenseShortName", {}).get("value", "")
        if not license_is_free(license_short):
            continue

        width = info.get("width", 0)
        if width and width < 500:
            continue

        description = strip_html(extmeta.get("ImageDescription", {}).get("value", ""))
        haystack = f"{lower_title} {description.lower()}"
        if not any(kw in haystack for kw in keywords):
            continue

        artist = strip_html(extmeta.get("Artist", {}).get("value", "")) or "Auteur non renseigné"
        source_url = f"https://commons.wikimedia.org/wiki/{title.replace(' ', '_')}"
        download_url = info.get("thumburl") or info.get("url")

        return {
            "gem_id": gem_id,
            "title": title,
            "download_url": download_url,
            "license": license_short,
            "artist": artist,
            "source_url": source_url,
        }
    return None


def safe_resource_name(gem_id: str) -> str:
    name = re.sub(r"[^a-z0-9_]", "_", gem_id.lower())
    return f"gem_{name}"


def kotlin_escape(value: str) -> str:
    return value.replace("\\", "\\\\").replace("$", "\\$").replace("\"", "\\\"")


def download_image(url: str, dest: Path) -> None:
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=30) as resp:
        dest.write_bytes(resp.read())


def main() -> None:
    DRAWABLE_DIR.mkdir(parents=True, exist_ok=True)
    KOTLIN_OUT.parent.mkdir(parents=True, exist_ok=True)

    entries = []
    report_lines = [
        "# Rapport de récupération des photos (Wikimedia Commons)",
        "",
        "Licences acceptées : domaine public, CC0, CC BY, CC BY-SA uniquement.",
        "",
        "| Gemme | Statut | Fichier Commons | Licence | Auteur |",
        "|---|---|---|---|---|",
    ]

    for gem_id, query, keywords in GEMS:
        print(f"-> {gem_id}: recherche « {query} »")
        result = None
        try:
            result = pick_image(gem_id, query, keywords)
        except Exception as exc:  # noqa: BLE001
            print(f"  [!] erreur recherche pour {gem_id}: {exc}", file=sys.stderr)

        if not result:
            print(f"  [x] aucune image libre trouvée pour {gem_id}")
            report_lines.append(f"| {gem_id} | ❌ non trouvée | — | — | — |")
            time.sleep(0.3)
            continue

        resource_name = safe_resource_name(gem_id)
        dest = DRAWABLE_DIR / f"{resource_name}.jpg"
        try:
            download_image(result["download_url"], dest)
        except Exception as exc:  # noqa: BLE001
            print(f"  [!] échec téléchargement pour {gem_id}: {exc}", file=sys.stderr)
            report_lines.append(f"| {gem_id} | ❌ échec téléchargement | {result['title']} | {result['license']} | {result['artist']} |")
            time.sleep(0.3)
            continue

        print(f"  [ok] {result['title']} ({result['license']}, {result['artist']})")
        entries.append({
            "gem_id": gem_id,
            "resource_name": resource_name,
            "artist": result["artist"],
            "license": result["license"],
            "source_url": result["source_url"],
        })
        report_lines.append(
            f"| {gem_id} | ✅ | [{result['title']}]({result['source_url']}) | {result['license']} | {result['artist']} |"
        )
        time.sleep(0.3)

    kotlin_lines = [
        "package fr.gemsofrod.encyclopedie.data",
        "",
        "// Fichier généré automatiquement par scripts/fetch_gem_images.py",
        "// à partir de photos librement réutilisables de Wikimedia Commons.",
        "// Ne pas éditer à la main : relancer le workflow \"Fetch gem images\".",
        "",
        "data class GemImageCredit(",
        "    val drawableName: String,",
        "    val author: String,",
        "    val license: String,",
        "    val sourceUrl: String",
        ")",
        "",
        "object GemImages {",
        "    private val credits: Map<String, GemImageCredit> = mapOf(",
    ]
    for entry in entries:
        kotlin_lines.append(
            "        \"{gem_id}\" to GemImageCredit(\"{resource_name}\", \"{artist}\", \"{license}\", \"{source_url}\"),".format(
                gem_id=kotlin_escape(entry["gem_id"]),
                resource_name=entry["resource_name"],
                artist=kotlin_escape(entry["artist"]),
                license=kotlin_escape(entry["license"]),
                source_url=kotlin_escape(entry["source_url"]),
            )
        )
    kotlin_lines += [
        "    )",
        "",
        "    fun creditFor(gemId: String): GemImageCredit? = credits[gemId]",
        "}",
        "",
    ]
    KOTLIN_OUT.write_text("\n".join(kotlin_lines), encoding="utf-8")

    report_lines.append("")
    report_lines.append(f"**{len(entries)} / {len(GEMS)}** gemmes ont une photo.")
    REPORT_OUT.write_text("\n".join(report_lines) + "\n", encoding="utf-8")

    print(f"\n{len(entries)}/{len(GEMS)} images récupérées.")


if __name__ == "__main__":
    main()
