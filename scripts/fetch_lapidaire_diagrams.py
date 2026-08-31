#!/usr/bin/env python3
"""
Récupère, pour la section Lapidaire (taille des facettes), des diagrammes et
illustrations librement réutilisables sur Wikimedia Commons, et génère :
  - les fichiers image dans android/app/src/androidMain/res/drawable-nodpi/
  - android/lapidaire_diagram_credits.json (source de vérité : crédits accumulés)
  - android/app/src/commonMain/kotlin/fr/gemsofrod/encyclopedie/data/LapidaireDiagrams.kt
    (régénéré à partir du JSON, mapping id de diagramme -> photo+crédit)

N'accepte que les licences librement réutilisables (domaine public, CC0,
CC BY, CC BY-SA) — jamais NC (non commercial) ni ND (pas de modification),
et uniquement des raster (JPG/PNG) : beaucoup de diagrammes de taille sur
Commons sont des SVG sous licence GFDL seule, non couverte par cette liste
blanche, donc automatiquement écartés — jamais de diagramme réécrit ou
redessiné par ce script, seulement des documents réels retrouvés tels quels.

Script frère de fetch_gem_images.py (mêmes règles de licence, même robustesse
réseau), volontairement séparé pour ne prendre aucun risque sur le pipeline
photo des gemmes déjà en production.

Conçu pour tourner dans GitHub Actions (accès réseau complet), pas dans le
bac à sable de développement qui bloque les hôtes d'images.
"""

import html
import json
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path
from typing import Optional

API_URL = "https://commons.wikimedia.org/w/api.php"
OPENVERSE_API_URL = "https://api.openverse.org/v1/images/"
USER_AGENT = "GemsOfRodEncyclopedieBot/1.0 (contact: gemsofrod@gmail.com)"

REPO_ROOT = Path(__file__).resolve().parent.parent
DRAWABLE_DIR = REPO_ROOT / "android/app/src/androidMain/res/drawable-nodpi"
KOTLIN_OUT = REPO_ROOT / "android/app/src/commonMain/kotlin/fr/gemsofrod/encyclopedie/data/LapidaireDiagrams.kt"
CREDITS_JSON = REPO_ROOT / "android/lapidaire_diagram_credits.json"
REPORT_OUT = REPO_ROOT / "android/LAPIDAIRE_DIAGRAM_FETCH_REPORT.md"

# diagramme_id : (termes de recherche essayés dans l'ordre, mots-clés de pertinence)
DIAGRAMS = [
    (
        "brillant_rond_proportions",
        [
            "Diamond proportions crown pavilion diagram",
            "Round brilliant cut diagram",
            "Brilliant cut diamond diagram proportions",
            "Diamond anatomy diagram",
            "Diamond cut proportions table crown pavilion girdle",
            "Round brilliant cut anatomy",
        ],
        ["brilliant", "diamond", "crown", "pavilion", "facet", "girdle", "anatomy", "proportions"],
    ),
    (
        "trajet_lumiere_pavillon",
        [
            "Diamond cut light path diagram",
            "Diamond light performance cut diagram",
            "Ideal cut shallow cut deep cut diamond diagram",
            "Diamond light reflection cut quality diagram",
            "Gemstone light path pavilion diagram",
        ],
        ["diamond", "light", "cut", "pavilion", "reflection", "brilliance"],
    ),
    (
        "moulin_taille_historique",
        ["Diamond cutting mill engraving", "Diamond cutter's wheel engraving", "Diamond polishing mill 18th century"],
        ["diamond", "mill", "wheel", "cutting", "cutter"],
    ),
    (
        "machine_facettage_moderne",
        ["Faceting machine photograph", "Gem faceting machine", "Lapidary faceting machine"],
        # Expressions composées uniquement (pas juste "facet" seul) : une
        # recherche trop large a déjà renvoyé une perle romaine à facettes au
        # lieu d'une machine, faux positif corrigé manuellement une fois
        # repéré — ces mots-clés plus stricts visent à ne pas le reproduire.
        ["faceting machine", "lapidary machine", "gem cutting machine"],
    ),
    (
        "etapes_taille_brut_facette",
        ["Rough to cut diamond stages", "Diamond cutting process stages diagram", "Gemstone cutting stages"],
        ["diamond", "cutting", "stages", "rough", "cut"],
    ),
]

ALLOWED_IMAGE_EXTENSIONS = (".jpg", ".jpeg", ".png")
# Wikimedia Commons rend aussi les fichiers .svg éligibles : l'API imageinfo,
# interrogée avec iiurlwidth, retourne pour un SVG un thumburl déjà rendu en
# PNG côté serveur (par ex. "...px-Foo.svg.png") — aucune rasterisation
# locale n'est nécessaire. Beaucoup de diagrammes techniques (coupe de
# pierre, trajet de la lumière) n'existent sur Commons qu'en SVG librement
# licenciés (CC BY/CC BY-SA/CC0) ; les exclure purement sur l'extension du
# fichier source, comme avant, les écartait à tort. Le filtre de licence
# (license_is_free) continue d'écarter les SVG en GFDL seule.
SOURCE_EXTENSIONS_ACCEPTED_FOR_SEARCH = ALLOWED_IMAGE_EXTENSIONS + (".svg",)

ALLOWED_LICENSE_RE = re.compile(
    r"^(cc0|public domain|pd|cc[\s-]?by(?:[\s-]?sa)?[\s-]?\d(?:\.\d)?)",
    re.IGNORECASE,
)
DISALLOWED_LICENSE_TOKENS = ("nc", "nd")

REQUEST_DELAY_SECONDS = 0.6
MAX_RETRIES = 4


def _sleep_for_retry(attempt: int, error: urllib.error.HTTPError) -> None:
    retry_after = error.headers.get("Retry-After") if error.headers else None
    try:
        delay = float(retry_after) if retry_after else 2.0 * (attempt + 1)
    except ValueError:
        delay = 2.0 * (attempt + 1)
    print(f"  [i] HTTP 429, nouvelle tentative dans {delay:.0f}s...", file=sys.stderr)
    time.sleep(delay)


def _urlopen_with_retry(req: urllib.request.Request):
    for attempt in range(MAX_RETRIES):
        try:
            return urllib.request.urlopen(req, timeout=30)
        except urllib.error.HTTPError as exc:
            if exc.code == 429 and attempt < MAX_RETRIES - 1:
                _sleep_for_retry(attempt, exc)
                continue
            raise
    raise RuntimeError("unreachable")


def api_get(params: dict) -> dict:
    params = {**params, "format": "json"}
    url = f"{API_URL}?{urllib.parse.urlencode(params)}"
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    time.sleep(REQUEST_DELAY_SECONDS)
    with _urlopen_with_retry(req) as resp:
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


def search_candidates(query: str, limit: int = 15) -> list:
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


def pick_image(terms: list, keywords: list, already_used_titles: set) -> Optional[dict]:
    for term in terms:
        for title in search_candidates(term):
            if title in already_used_titles:
                continue
            lower_title = title.lower()
            if not lower_title.endswith(SOURCE_EXTENSIONS_ACCEPTED_FOR_SEARCH):
                continue
            is_svg_source = lower_title.endswith(".svg")
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

            # Pour un SVG, la largeur déclarée par Commons reflète la taille
            # intrinsèque du fichier source (parfois un petit viewBox), pas
            # la résolution du thumburl demandé via iiurlwidth — ce filtre ne
            # s'applique donc qu'aux sources raster, où il évite les vignettes
            # trop petites.
            width = info.get("width", 0)
            if not is_svg_source and width and width < 300:
                continue

            description = strip_html(extmeta.get("ImageDescription", {}).get("value", ""))
            haystack = f"{lower_title} {description.lower()}"
            if not any(kw in haystack for kw in keywords):
                continue

            artist = strip_html(extmeta.get("Artist", {}).get("value", "")) or "Auteur non renseigné"
            source_url = f"https://commons.wikimedia.org/wiki/{title.replace(' ', '_')}"
            # thumburl : pour un SVG, Commons retourne déjà un rendu PNG côté
            # serveur (ex. "...px-Foo.svg.png") à la largeur iiurlwidth
            # demandée plus haut — jamais le fichier .svg brut.
            download_url = info.get("thumburl") or info.get("url")
            if is_svg_source and not (download_url or "").lower().endswith((".png", ".jpg", ".jpeg")):
                # Rendu serveur indisponible/inattendu : ne jamais retomber
                # sur le SVG brut (pas géré par le pipeline drawable-nodpi).
                continue

            return {
                "title": title,
                "download_url": download_url,
                "license": license_short,
                "artist": artist,
                "source_url": source_url,
            }
    return None


OPENVERSE_ALLOWED_LICENSES = {"cc0", "pdm", "by", "by-sa"}


def openverse_license_is_free(license_slug: str) -> bool:
    return (license_slug or "").strip().lower() in OPENVERSE_ALLOWED_LICENSES


def openverse_search(query: str, limit: int = 15) -> list:
    params = {
        "q": query,
        "license": "cc0,pdm,by,by-sa",
        "page_size": limit,
        "mature": "false",
    }
    url = f"{OPENVERSE_API_URL}?{urllib.parse.urlencode(params)}"
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT, "Accept": "application/json"})
    time.sleep(REQUEST_DELAY_SECONDS)
    with _urlopen_with_retry(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
    return data.get("results", [])


def pick_image_openverse(terms: list, keywords: list, already_used_titles: set) -> Optional[dict]:
    """Source de repli quand Wikimedia Commons n'a rien donné."""
    for term in terms:
        try:
            results = openverse_search(term)
        except Exception as exc:  # noqa: BLE001
            print(f"  [!] erreur Openverse pour « {term} »: {exc}", file=sys.stderr)
            continue
        for item in results:
            try:
                title = (item.get("title") or "").strip()
                if not title or title in already_used_titles:
                    continue
                if not openverse_license_is_free(item.get("license", "")):
                    continue
                width = item.get("width") or 0
                if width and width < 300:
                    continue
                tag_names = " ".join(
                    t.get("name", "") for t in (item.get("tags") or []) if isinstance(t, dict)
                )
                haystack = f"{title.lower()} {tag_names.lower()}"
                if not any(kw in haystack for kw in keywords):
                    continue

                download_url = item.get("url")
                if not download_url:
                    continue

                license_slug = (item.get("license") or "").strip()
                license_version = (item.get("license_version") or "").strip()
                license_label = f"{license_slug} {license_version}".strip()
                artist = (item.get("creator") or "").strip() or "Auteur non renseigné"
                source_url = item.get("foreign_landing_url") or download_url

                return {
                    "title": title,
                    "download_url": download_url,
                    "license": license_label,
                    "artist": artist,
                    "source_url": source_url,
                }
            except Exception as exc:  # noqa: BLE001
                print(f"  [!] entrée Openverse ignorée (forme inattendue): {exc}", file=sys.stderr)
                continue
    return None


def safe_resource_name(diagram_id: str) -> str:
    name = re.sub(r"[^a-z0-9_]", "_", diagram_id.lower())
    return f"lapidaire_{name}"


def kotlin_escape(value: str) -> str:
    single_line = re.sub(r"\s*[\r\n]+\s*", " ", value).strip()
    return single_line.replace("\\", "\\\\").replace("$", "\\$").replace("\"", "\\\"")


def download_image(url: str, dest: Path) -> None:
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    time.sleep(REQUEST_DELAY_SECONDS)
    with _urlopen_with_retry(req) as resp:
        dest.write_bytes(resp.read())


def load_credits() -> dict:
    if not CREDITS_JSON.exists():
        return {}
    return json.loads(CREDITS_JSON.read_text(encoding="utf-8"))


def save_credits(credits: dict) -> None:
    CREDITS_JSON.write_text(json.dumps(credits, ensure_ascii=False, indent=2, sort_keys=True), encoding="utf-8")


def write_kotlin(credits: dict) -> None:
    lines = [
        "package fr.gemsofrod.encyclopedie.data",
        "",
        "// Fichier généré automatiquement par scripts/fetch_lapidaire_diagrams.py",
        "// à partir de android/lapidaire_diagram_credits.json (Wikimedia Commons / Openverse).",
        "// Ne pas éditer à la main : relancer le workflow \"Fetch lapidaire diagrams\".",
        "",
        "data class LapidaireDiagramCredit(",
        "    val drawableName: String,",
        "    val author: String,",
        "    val license: String,",
        "    val sourceUrl: String",
        ")",
        "",
        "object LapidaireDiagrams {",
        "    private val credits: Map<String, LapidaireDiagramCredit> = mapOf(",
    ]
    for diagram_id in sorted(credits.keys()):
        c = credits[diagram_id]
        lines.append(
            "        \"{id}\" to LapidaireDiagramCredit(\"{resource_name}\", \"{artist}\", \"{license}\", \"{source_url}\"),".format(
                id=diagram_id,
                resource_name=c["resource_name"],
                artist=kotlin_escape(c["artist"]),
                license=kotlin_escape(c["license"]),
                source_url=kotlin_escape(c["source_url"]),
            )
        )
    lines.append("    )")
    lines.append("")
    lines.append("    fun creditFor(diagramId: String): LapidaireDiagramCredit? = credits[diagramId]")
    lines.append("}")
    lines.append("")
    KOTLIN_OUT.write_text("\n".join(lines), encoding="utf-8")


def main() -> None:
    DRAWABLE_DIR.mkdir(parents=True, exist_ok=True)
    credits = load_credits()
    already_used = {c["title"] for c in credits.values()}
    report_lines = ["# Rapport de récupération des diagrammes Lapidaire", ""]

    found = 0
    for diagram_id, terms, keywords in DIAGRAMS:
        if diagram_id in credits:
            print(f"[skip] {diagram_id} déjà présent")
            found += 1
            continue

        print(f"[..] {diagram_id}")
        result = pick_image(terms, keywords, already_used)
        source = "Wikimedia Commons"
        if not result:
            result = pick_image_openverse(terms, keywords, already_used)
            source = "Openverse"
        if not result:
            print(f"  [x] aucun diagramme libre de droits trouvé pour {diagram_id}")
            report_lines.append(f"- **{diagram_id}** : aucun résultat libre de droits trouvé (Commons + Openverse).")
            continue

        resource_name = safe_resource_name(diagram_id)
        extension = ".png" if result["download_url"].lower().endswith(".png") else ".jpg"
        dest = DRAWABLE_DIR / f"{resource_name}{extension}"
        try:
            download_image(result["download_url"], dest)
        except Exception as exc:  # noqa: BLE001
            print(f"  [!] échec du téléchargement pour {diagram_id}: {exc}", file=sys.stderr)
            continue

        already_used.add(result["title"])
        credits[diagram_id] = {
            "resource_name": resource_name,
            "title": result["title"],
            "artist": result["artist"],
            "license": result["license"],
            "source_url": result["source_url"],
        }
        found += 1
        print(f"  [ok] {result['title']} ({result['license']}, {result['artist']}, via {source})")
        report_lines.append(
            f"- **{diagram_id}** : *{result['title']}* — {result['artist']} — {result['license']} — {result['source_url']} (via {source})"
        )

    save_credits(credits)
    write_kotlin(credits)
    REPORT_OUT.write_text("\n".join(report_lines) + "\n", encoding="utf-8")
    print(f"\n{found}/{len(DIAGRAMS)} diagrammes disponibles.")


if __name__ == "__main__":
    main()
