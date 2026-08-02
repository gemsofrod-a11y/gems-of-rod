#!/usr/bin/env python3
"""
Récupère, pour chaque gemme de l'encyclopédie, une photo de la pierre BRUTE
et une photo de la pierre FACETTÉE/taillée sur Wikimedia Commons, et génère :
  - les fichiers image dans android/app/src/main/res/drawable-nodpi/
  - android/gem_image_credits.json (source de vérité : crédits accumulés)
  - android/app/src/main/java/fr/gemsofrod/encyclopedie/data/GemImages.kt
    (régénéré à partir du JSON, mapping id de gemme -> liste de photos+crédits)
  - android/IMAGE_FETCH_REPORT.md (rapport lisible pour revue humaine)

N'accepte que les licences librement réutilisables (domaine public, CC0,
CC BY, CC BY-SA) — jamais NC (non commercial) ni ND (pas de modification).

Les images déjà présentes (fichier + entrée dans le JSON de crédits) ne sont
jamais re-téléchargées, pour limiter le nombre d'appels à l'API Commons d'un
run à l'autre.

Ce script est conçu pour tourner dans GitHub Actions (accès réseau complet),
pas dans le bac à sable de développement qui bloque les hôtes d'images.
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
USER_AGENT = "GemsOfRodEncyclopedieBot/1.0 (contact: gemsofrod@gmail.com)"

REPO_ROOT = Path(__file__).resolve().parent.parent
DRAWABLE_DIR = REPO_ROOT / "android/app/src/main/res/drawable-nodpi"
KOTLIN_OUT = REPO_ROOT / "android/app/src/main/java/fr/gemsofrod/encyclopedie/data/GemImages.kt"
CREDITS_JSON = REPO_ROOT / "android/gem_image_credits.json"
REPORT_OUT = REPO_ROOT / "android/IMAGE_FETCH_REPORT.md"

BRUTE = "BRUTE"
FACETTEE = "FACETTEE"

# gem_id : (termes de recherche essayés dans l'ordre, mots-clés de pertinence, types voulus)
GEMS = [
    ("rubis", ["Ruby"], ["ruby"], (BRUTE, FACETTEE)),
    ("grenat-almandin", ["Almandine garnet"], ["almandine", "garnet"], (BRUTE, FACETTEE)),
    ("spinelle-rouge", ["Red spinel"], ["spinel"], (BRUTE, FACETTEE)),
    ("tourmaline-rubellite", ["Rubellite tourmaline"], ["tourmaline", "rubellite"], (BRUTE, FACETTEE)),
    ("grenat-rhodolite", ["Rhodolite garnet", "Rhodolite crystal", "Rhodolite", "Pyrope almandine garnet crystal", "Garnet crystal red purple"], ["rhodolite", "garnet", "pyrope", "almandine"], (BRUTE, FACETTEE)),
    ("painite", ["Painite"], ["painite"], (BRUTE, FACETTEE)),
    ("grenat-spessartite", ["Spessartite garnet", "Spessartine garnet", "Spessartine crystal"], ["spessartite", "spessartine", "garnet"], (BRUTE, FACETTEE)),
    ("topaze-imperiale", ["Imperial topaz"], ["topaz"], (BRUTE, FACETTEE)),
    ("hessonite", ["Hessonite garnet", "Hessonite"], ["hessonite"], (BRUTE, FACETTEE)),
    ("opale-de-feu", ["Fire opal", "Fire opal rough", "Mexican fire opal", "Opal rough Queretaro", "Orange opal rough"], ["opal"], (BRUTE, FACETTEE)),
    ("padparadscha", ["Padparadscha sapphire", "Padparadscha sapphire crystal", "Padparadscha sapphire rough", "Corundum crystal pink orange", "Padparadscha corundum"], ["padparadscha", "sapphire", "corundum"], (BRUTE, FACETTEE)),
    ("cornaline", ["Carnelian"], ["carnelian", "cornelian"], (BRUTE, FACETTEE)),
    ("saphir-jaune", ["Yellow sapphire"], ["sapphire"], (BRUTE, FACETTEE)),
    ("citrine", ["Citrine"], ["citrine"], (BRUTE, FACETTEE)),
    ("heliodore", ["Heliodor beryl"], ["heliodor", "beryl"], (BRUTE, FACETTEE)),
    ("chrysoberyl", ["Chrysoberyl"], ["chrysoberyl"], (BRUTE, FACETTEE)),
    ("oeil-de-tigre", ["Tiger's eye"], ["tiger"], (BRUTE, FACETTEE)),
    ("scapolite", ["Scapolite gemstone", "Scapolite"], ["scapolite"], (BRUTE, FACETTEE)),
    ("emeraude", ["Emerald"], ["emerald"], (BRUTE, FACETTEE)),
    ("peridot", ["Peridot"], ["peridot", "olivine"], (BRUTE, FACETTEE)),
    ("tsavorite", ["Tsavorite garnet"], ["tsavorite", "garnet"], (BRUTE, FACETTEE)),
    ("jade-jadeite", ["Jadeite jade", "Jadeite"], ["jadeite", "jade"], (BRUTE, FACETTEE)),
    ("tourmaline-verte", ["Green tourmaline"], ["tourmaline"], (BRUTE, FACETTEE)),
    ("demantoide", ["Demantoid garnet"], ["demantoid", "garnet"], (BRUTE, FACETTEE)),
    ("uvarovite", ["Uvarovite garnet", "Uvarovite"], ["uvarovite"], (BRUTE, FACETTEE)),
    ("vesuvianite", ["Vesuvianite", "Idocrase", "Vesuvianite faceted gem", "Idocrase faceted", "Idocrase cut gem", "Californite cabochon"], ["vesuvianite", "idocrase", "californite"], (BRUTE, FACETTEE)),
    ("prehnite", ["Prehnite"], ["prehnite"], (BRUTE, FACETTEE)),
    ("chrysocolle", ["Chrysocolla"], ["chrysocolla"], (BRUTE, FACETTEE)),
    ("variscite", ["Variscite"], ["variscite"], (BRUTE, FACETTEE)),
    ("serpentine", ["Bowenite serpentine", "Bowenite", "Serpentine rough", "Serpentinite rough"], ["bowenite", "serpentine"], (BRUTE, FACETTEE)),
    ("grandidierite", ["Grandidierite"], ["grandidierite"], (BRUTE, FACETTEE)),
    ("saphir-bleu", ["Blue sapphire"], ["sapphire"], (BRUTE, FACETTEE)),
    ("aigue-marine", ["Aquamarine"], ["aquamarine"], (BRUTE, FACETTEE)),
    ("tanzanite", ["Tanzanite"], ["tanzanite"], (BRUTE, FACETTEE)),
    ("lapis-lazuli", ["Lapis lazuli"], ["lapis"], (BRUTE, FACETTEE)),
    ("turquoise", ["Turquoise gemstone", "Turquoise mineral"], ["turquoise"], (BRUTE, FACETTEE)),
    ("spinelle-bleu", ["Blue spinel"], ["spinel"], (BRUTE, FACETTEE)),
    ("paraiba-tourmaline", ["Paraiba tourmaline"], ["paraiba", "tourmaline"], (BRUTE, FACETTEE)),
    ("apatite-bleue", ["Neon blue apatite", "Blue apatite", "Apatite faceted gemstone", "Paraiba apatite", "Apatite cut gem", "Apatite gemstone Madagascar"], ["apatite"], (BRUTE, FACETTEE)),
    ("cyanite", ["Kyanite gemstone", "Kyanite"], ["kyanite", "cyanite"], (BRUTE, FACETTEE)),
    ("benitoite", ["Benitoite"], ["benitoite"], (BRUTE, FACETTEE)),
    ("jeremejevite", ["Jeremejevite", "Jeremejevite gem", "Jeremejevite Namibia", "Jeremejewite", "Jeremejevite faceted", "Jeremejevite cut gem"], ["jeremejevite", "jeremejewite"], (BRUTE, FACETTEE)),
    ("amethyste", ["Amethyst"], ["amethyst"], (BRUTE, FACETTEE)),
    ("spinelle-violet", ["Purple spinel"], ["spinel"], (BRUTE, FACETTEE)),
    ("iolite", ["Iolite cordierite", "Iolite"], ["iolite", "cordierite"], (BRUTE, FACETTEE)),
    (
        "grenat-rhodolite-violet",
        ["Purple rhodolite garnet", "Rhodolite garnet purple", "Rhodolite garnet", "Rhodolite crystal", "Pyrope garnet rough", "Garnet crystal purple rough", "Almandine garnet rough crystal"],
        ["rhodolite", "garnet", "pyrope", "almandine"],
        (BRUTE, FACETTEE),
    ),
    ("sugilite", ["Sugilite"], ["sugilite"], (BRUTE, FACETTEE)),
    ("charoite", ["Charoite"], ["charoite"], (BRUTE, FACETTEE)),
    ("taaffeite", ["Taaffeite"], ["taaffeite"], (BRUTE, FACETTEE)),
    ("axinite", ["Axinite gemstone", "Axinite"], ["axinite"], (BRUTE, FACETTEE)),
    ("morganite", ["Morganite beryl", "Morganite"], ["morganite"], (BRUTE, FACETTEE)),
    ("kunzite", ["Kunzite"], ["kunzite"], (BRUTE, FACETTEE)),
    ("saphir-rose", ["Pink sapphire", "Pink sapphire rough", "Pink corundum crystal", "Ceylon sapphire crystal pink", "Corundum crystal pink rough"], ["sapphire", "corundum"], (BRUTE, FACETTEE)),
    ("tourmaline-rose", ["Pink tourmaline"], ["tourmaline"], (BRUTE, FACETTEE)),
    ("rhodochrosite", ["Rhodochrosite"], ["rhodochrosite"], (BRUTE, FACETTEE)),
    ("diamant", ["Diamond"], ["diamond"], (BRUTE, FACETTEE)),
    ("zircon-blanc", ["Colorless zircon", "White zircon", "Zircon"], ["zircon"], (BRUTE, FACETTEE)),
    ("goshenite", ["Goshenite beryl", "Goshenite"], ["goshenite", "beryl"], (BRUTE, FACETTEE)),
    ("cristal-de-roche", ["Rock crystal quartz", "Rock crystal"], ["quartz", "rock crystal"], (BRUTE, FACETTEE)),
    ("danburite", ["Danburite gemstone", "Danburite"], ["danburite"], (BRUTE, FACETTEE)),
    ("quartz-fume", ["Smoky quartz"], ["smoky", "quartz"], (BRUTE, FACETTEE)),
    ("andalousite", ["Andalusite gemstone", "Andalusite"], ["andalusite"], (BRUTE, FACETTEE)),
    ("sinhalite", ["Sinhalite gemstone", "Sinhalite", "Sinhalite crystal", "Sinhalite mineral", "Sinhalite Sri Lanka", "Sinhalite specimen", "Sinhalite Mogok"], ["sinhalite"], (BRUTE, FACETTEE)),
    ("staurotide", ["Staurolite"], ["staurolite"], (BRUTE, FACETTEE)),
    ("sphene", ["Sphene titanite", "Titanite gemstone"], ["sphene", "titanite"], (BRUTE, FACETTEE)),
    ("onyx", ["Black onyx rough", "Black onyx chalcedony", "Black banded agate rough", "Onyx nodule black rough"], ["onyx", "chalcedony", "agate"], (BRUTE, FACETTEE)),
    ("spinelle-noir", ["Black spinel"], ["spinel"], (BRUTE, FACETTEE)),
    ("tourmaline-noire", ["Schorl tourmaline", "Schorl"], ["schorl", "tourmaline"], (BRUTE, FACETTEE)),
    ("obsidienne", ["Obsidian"], ["obsidian"], (BRUTE, FACETTEE)),
    ("alexandrite", ["Alexandrite"], ["alexandrite"], (BRUTE, FACETTEE)),
    ("opale-precieuse", ["Precious opal"], ["opal"], (BRUTE, FACETTEE)),
    (
        "tourmaline-pasteque",
        ["Watermelon tourmaline", "Watermelon tourmaline slice", "Bicolor tourmaline slice"],
        ["tourmaline"],
        (BRUTE, FACETTEE),
    ),
    ("labradorite", ["Labradorite"], ["labradorite"], (BRUTE, FACETTEE)),
    ("pierre-de-lune", ["Moonstone gemstone", "Moonstone feldspar", "Adularia rough", "Moonstone crystal", "Orthoclase feldspar rough", "Adularia crystal Sri Lanka"], ["moonstone", "adularia", "orthoclase"], (BRUTE, FACETTEE)),
    ("pierre-de-soleil", ["Sunstone gemstone", "Sunstone feldspar", "Oligoclase rough", "Sunstone crystal", "Andesine feldspar rough", "Oregon sunstone rough"], ["sunstone", "oligoclase", "andesine"], (BRUTE, FACETTEE)),
    ("fluorine", ["Fluorite gemstone", "Fluorite"], ["fluorite", "fluorspar"], (BRUTE, FACETTEE)),
    ("ammolite", ["Ammolite", "Ammolite rough", "Ammonite shell fragment"], ["ammolite", "ammonite"], (BRUTE, FACETTEE)),
    ("ambre", ["Amber gemstone", "Baltic amber"], ["amber"], (BRUTE, FACETTEE)),
    ("perle", ["Pearl gemstone", "Cultured pearl"], ["pearl"], (FACETTEE,)),
    ("grenat-pyrope", ['Pyrope garnet'], ['pyrope', 'garnet'], (BRUTE, FACETTEE)),
    ("eudialyte", ['Eudialyte crystal', 'Eudialyte', 'Eudialyte cut gem'], ['eudialyte'], (BRUTE, FACETTEE)),
    ("crocoite", ['Crocoite Dundas', 'Crocoite crystal', 'Crocoite'], ['crocoite'], (BRUTE, FACETTEE)),
    ("grenat-malaya", ['Malaya garnet', 'Malaya garnet faceted', 'Malaia garnet'], ['malaya', 'malaia', 'garnet'], (BRUTE, FACETTEE)),
    ("zircon-hyacinthe", ['Hyacinth zircon', 'Jacinth zircon'], ['hyacinth', 'zircon', 'jacinth'], (BRUTE, FACETTEE)),
    ("zircon-dore", ['Golden zircon', 'Yellow zircon'], ['zircon'], (BRUTE, FACETTEE)),
    ("pyrite", ['Pyrite'], ['pyrite'], (BRUTE, FACETTEE)),
    ("scheelite", ['Scheelite'], ['scheelite'], (BRUTE, FACETTEE)),
    ("tourmaline-jaune", ['Yellow tourmaline faceted', 'Tourmaline yellow Malawi', 'Elbaite yellow crystal'], ['tourmaline', 'elbaite'], (BRUTE, FACETTEE)),
    ("bastnasite", ['Bastnasite', 'Bastnäsite'], ['bastnasite', 'bastnäsite'], (BRUTE, FACETTEE)),
    ("malachite", ['Malachite specimen', 'Malachite polished', 'Malachite crystal'], ['malachite'], (BRUTE, FACETTEE)),
    ("dioptase", ['Dioptase'], ['dioptase'], (BRUTE, FACETTEE)),
    ("amazonite", ['Amazonite'], ['amazonite'], (BRUTE, FACETTEE)),
    ("chrysoprase", ['Chrysoprase'], ['chrysoprase'], (BRUTE, FACETTEE)),
    ("beryl-vert", ['Green beryl'], ['beryl'], (BRUTE, FACETTEE)),
    ("sillimanite", ['Sillimanite gemstone', 'Sillimanite'], ['sillimanite'], (BRUTE, FACETTEE)),
    ("seraphinite", ['Clinochlore Seraphinite', 'Seraphinite'], ['seraphinite', 'clinochlore'], (BRUTE, FACETTEE)),
    ("grenat-grossulaire", ['Grossular garnet'], ['grossular', 'garnet'], (BRUTE, FACETTEE)),
    ("diopside", ['Chrome diopside'], ['diopside'], (BRUTE, FACETTEE)),
    ("jade-nephrite", ['Nephrite jade', 'Nephrite jade cut', 'Nephrite jade polished'], ['nephrite'], (BRUTE, FACETTEE)),
    ("maw-sit-sit", ['Kosmochlor jade', 'Kosmochlor', 'Maw sit sit'], ['maw sit sit', 'maw-sit-sit', 'kosmochlor'], (BRUTE, FACETTEE)),
    ("tourmaline-chrome", ['Faceted chrome tourmaline', 'Chrome tourmaline', 'Chromium dravite'], ['tourmaline', 'dravite'], (BRUTE, FACETTEE)),
    ("azurite", ['Azurite specimen', 'Azurite polished', 'Azurite crystal'], ['azurite'], (BRUTE, FACETTEE)),
    ("sodalite", ['Sodalite'], ['sodalite'], (BRUTE, FACETTEE)),
    ("larimar", ['Larimar', 'Blue pectolite'], ['larimar', 'pectolite'], (BRUTE, FACETTEE)),
    ("lazulite", ['Lazulite crystal', 'Lazulite', 'Lazulite faceted'], ['lazulite'], (BRUTE, FACETTEE)),
    ("hauyne", ['Hauyne', 'Haüyne'], ['hauyne', 'haüyne'], (BRUTE, FACETTEE)),
    ("cavansite", ['Cavansite cluster', 'Cavansite specimen', 'Cavansite'], ['cavansite'], (BRUTE, FACETTEE)),
    ("celestite", ['Celestite', 'Celestine mineral'], ['celestite', 'celestine'], (BRUTE, FACETTEE)),
    ("euclase", ['Euclase Brazil', 'Euclase crystal', 'Euclase'], ['euclase'], (BRUTE, FACETTEE)),
    ("topaze-bleue", ['Blue topaz'], ['topaz'], (BRUTE, FACETTEE)),
    ("tourmaline-bleue", ['Indicolite tourmaline', 'Blue tourmaline'], ['tourmaline', 'indicolite'], (BRUTE, FACETTEE)),
    ("zircon-bleu", ['Blue zircon'], ['zircon'], (BRUTE, FACETTEE)),
    ("hemimorphite", ['Hemimorphite botryoidal', 'Hemimorphite specimen', 'Hemimorphite'], ['hemimorphite'], (BRUTE, FACETTEE)),
    ("shattuckite", ['Shattuckite Namibia', 'Shattuckite specimen', 'Shattuckite'], ['shattuckite'], (BRUTE, FACETTEE)),
    ("sapphirine", ['Sapphirine crystal', 'Sapphirine mineral', 'Sapphirine gemstone'], ['sapphirine'], (BRUTE, FACETTEE)),
    ("dumortierite", ['Dumortierite crystal', 'Dumortierite specimen', 'Dumortierite'], ['dumortierite'], (BRUTE, FACETTEE)),
    ("afghanite", ['Afghanite crystal', 'Afghanite blue', 'Afghanite'], ['afghanite'], (BRUTE, FACETTEE)),
    ("violane", ['Violane', 'Violane Praborna', 'Violan diopside'], ['violane', 'violan'], (BRUTE, FACETTEE)),
    ("saphir-violet", ['Purple sapphire crystal', 'Purple sapphire ring', 'Corundum purple faceted'], ['sapphire', 'corundum'], (BRUTE, FACETTEE)),
    ("purpurite", ['Purpurite Namibia', 'Purpurite specimen', 'Purpurite'], ['purpurite'], (BRUTE, FACETTEE)),
    ("hackmanite", ['Hackmanite Afghanistan', 'Sodalite Hackmanite', 'Hackmanite'], ['hackmanite'], (BRUTE, FACETTEE)),
    ("lepidolite", ['Lepidolite mica', 'Lepidolite specimen', 'Lepidolite'], ['lepidolite'], (BRUTE, FACETTEE)),
    ("stichtite", ['Atlantasite stichtite', 'Stichtite serpentine', 'Stichtite'], ['stichtite'], (BRUTE, FACETTEE)),
    ("cobaltocalcite", ['Cobaltoan calcite', 'Cobaltoan calcite Congo', 'Cobaltocalcite'], ['cobalt', 'calcite'], (BRUTE, FACETTEE)),
    ("thulite", ['Thulite'], ['thulite'], (BRUTE, FACETTEE)),
    ("rhodonite", ['Rhodonite'], ['rhodonite'], (BRUTE, FACETTEE)),
    ("topaze-rose", ['Pink topaz faceted', 'Pink topaz', 'Pink topaz Pakistan'], ['topaz'], (BRUTE, FACETTEE)),
    ("quartz-rose", ['Rose quartz'], ['quartz'], (BRUTE, FACETTEE)),
    ("phosphosiderite", ['Phosphosiderite'], ['phosphosiderite'], (BRUTE, FACETTEE)),
    ("saphir-blanc", ['Colourless sapphire faceted', 'White sapphire ring', 'Corundum colorless crystal'], ['sapphire', 'corundum'], (BRUTE, FACETTEE)),
    ("topaze-blanche", ['Colorless topaz ring', 'White topaz crystal', 'Topaz colorless faceted'], ['topaz'], (BRUTE, FACETTEE)),
    ("hambergite", ['Hambergite crystal', 'Hambergite Madagascar', 'Hambergite'], ['hambergite'], (BRUTE, FACETTEE)),
    ("petalite", ['Petalite crystal', 'Petalite specimen', 'Petalite'], ['petalite'], (BRUTE, FACETTEE)),
    ("phenakite", ['Phenakite', 'Phenacite'], ['phenakite', 'phenacite'], (BRUTE, FACETTEE)),
    ("bytownite", ['Bytownite crystal', 'Bytownite feldspar', 'Bytownite'], ['bytownite'], (BRUTE, FACETTEE)),
    ("hyalite", ['Hyalite opal fluorescent', 'Hyalite opal', 'Hyalite'], ['hyalite'], (BRUTE, FACETTEE)),
    ("anhydrite", ['Anhydrite crystal', 'Anhydrite specimen', 'Anhydrite'], ['anhydrite'], (BRUTE, FACETTEE)),
    ("baryte", ['Baryte', 'Barite'], ['baryte', 'barite'], (BRUTE, FACETTEE)),
    ("aragonite", ['Aragonite'], ['aragonite'], (BRUTE, FACETTEE)),
    ("calcite", ['Calcite gemstone', 'Calcite crystal'], ['calcite'], (BRUTE, FACETTEE)),
    ("cerusite", ['Cerussite crystal', 'Cerussite Tsumeb', 'Cerussite'], ['cerussite', 'cerusite'], (BRUTE, FACETTEE)),
    ("montebrasite", ['Montebrasite crystal', 'Montebrasite specimen', 'Montebrasite'], ['montebrasite'], (BRUTE, FACETTEE)),
    ("bois-fossilise", ['Petrified wood polished', 'Petrified wood slice', 'Petrified wood'], ['petrified wood'], (BRUTE, FACETTEE)),
    ("os-fossilise", ['Agatized dinosaur bone', 'Dinosaur gembone', 'Fossil bone agate slice'], ['bone', 'gembone', 'dinosaur'], (BRUTE, FACETTEE)),
    ("kornerupine", ['Kornerupine crystal', 'Kornerupine faceted', 'Kornerupine'], ['kornerupine'], (BRUTE, FACETTEE)),
    ("hyperstene", ['Hypersthene specimen', 'Hypersthene crystal', 'Hypersthene'], ['hypersthene'], (BRUTE, FACETTEE)),
    ("paesine", ['Pietra paesina', 'Ruin marble', 'Landscape marble'], ['paesina', 'paesine', 'ruin marble', 'landscape marble'], (BRUTE, FACETTEE)),
    ("hematite", ['Hematite gemstone', 'Hematite'], ['hematite'], (BRUTE, FACETTEE)),
    ("magnetite", ['Magnetite', 'Lodestone'], ['magnetite', 'lodestone'], (BRUTE, FACETTEE)),
    ("quartz-tourmaline", ['Elbaite quartz gem', 'Schorl quartz crystal', 'Tourmalinated quartz'], ['tourmalinated quartz', 'elbaite', 'schorl'], (BRUTE, FACETTEE)),
    ("rubis-etoile", ['Star ruby cabochon', 'Star ruby', 'Asterism ruby'], ['star ruby'], (BRUTE, FACETTEE)),
    ("saphir-etoile", ['Star sapphire'], ['star sapphire'], (BRUTE, FACETTEE)),
    ("diaspore-zultanite", ['Zultanite', 'Diaspore gemstone'], ['zultanite', 'diaspore'], (BRUTE, FACETTEE)),
    ("grenat-couleur-changeante", ['Color change garnet'], ['garnet'], (BRUTE, FACETTEE)),
    ("pietersite", ['Pietersite Namibia', 'Pietersite polished', 'Pietersite'], ['pietersite'], (BRUTE, FACETTEE)),
    ("agate", ['Agate gemstone', 'Agate slice'], ['agate'], (BRUTE, FACETTEE)),
    ("jaspe", ['Jasper gemstone'], ['jasper'], (BRUTE, FACETTEE)),
    ("calcedoine", ['Chalcedony gemstone', 'Blue chalcedony'], ['chalcedony'], (BRUTE, FACETTEE)),
    ("quartz-a-inclusions", ['Lodolite quartz', 'Garden quartz crystal', 'Phantom quartz chlorite'], ['quartz'], (BRUTE, FACETTEE)),
    ("quartz-rutile", ['Rutilated quartz'], ['rutilated quartz'], (BRUTE, FACETTEE)),
    ("quartz-super-sept", ['Super seven quartz', 'Melody stone quartz', 'Cacoxenite amethyst'], ['super seven', 'cacoxenite', 'quartz'], (BRUTE, FACETTEE)),
    ("smithsonite", ['Smithsonite botryoidal', 'Smithsonite pink', 'Smithsonite'], ['smithsonite'], (BRUTE, FACETTEE)),
    ("moldavite", ['Moldavite'], ['moldavite'], (BRUTE, FACETTEE)),
    ("verre-libyque", ['Libyan desert glass', 'Libyan desert glass tektite', 'Great Sand Sea glass'], ['libyan desert glass'], (BRUTE, FACETTEE)),
    ("saphir-vert", ['Green sapphire faceted', 'Green sapphire', 'Green corundum crystal'], ['sapphire', 'corundum'], (BRUTE, FACETTEE)),
    ("andesine", ['Red andesine faceted', 'Red andesine gemstone', 'Andesine red Congo'], ['andesine'], (BRUTE, FACETTEE)),
    ("chondrodite", ['Chondrodite crystal', 'Chondrodite orange', 'Chondrodite'], ['chondrodite'], (BRUTE, FACETTEE)),
    ("enstatite", ['Enstatite crystal', 'Enstatite faceted', 'Enstatite gemstone'], ['enstatite'], (BRUTE, FACETTEE)),
    ("sphalerite", ['Sphalerite gemstone'], ['sphalerite'], (BRUTE, FACETTEE)),
    ("rutile", ['Rutile crystal', 'Rutile gemstone'], ['rutile'], (BRUTE, FACETTEE)),
    ("orthose", ['Orthoclase feldspar yellow', 'Yellow orthoclase Madagascar', 'Orthoclase crystal'], ['orthoclase'], (BRUTE, FACETTEE)),
    ("corail", ['Red coral gemstone', 'Precious coral'], ['coral'], (BRUTE, FACETTEE)),
    ("perle-conque", ['Conch pearl', 'Queen conch pearl pink'], ['conch pearl'], (FACETTEE,)),
]

EXCLUDED_TITLE_TOKENS = [
    "logo", "map", "diagram", "icon", "chart", "graph",
    "locality", "location", "mine entrance", "mining site",
    "mine 1", "mine 2", "mine 3", "mine 4", "mine 5",
    "stone mine", "gem mine", "crystal structure", "flag",
    "coat of arms", "stamp",
    "synthetic", "man-made", "man made", "lab-grown", "lab grown",
    "laboratory", "imitation", "simulant", "artificial",
    "(ia ", "badge", "coin", "medal", "brooch", "manuscript",
    "catalogue", "microform", "book", "page",
    "journal", "quarterly", "proceedings of", "transactions of",
    "bulletin of", "geological society", "painting", "engraving",
    "crystal palace", "print,", "illustration",
    "butterfly", "moth", "insect", "leaf", "leaves",
    "girl with a pearl", "vermeer", "portrait",
    "necklace", "multi-gemstone", "assorted gemstones",
    "birthstone ring", "rainbow ring", "inclusions in a gem",
    "microscope", "thin section", "polarized light",
    "chameleon", "lizard", "reptile", "gecko", "frog", "bird",
    "hairstreak", "skipper", "diucon", "wing position", "wingspan",
    "specimen id", "voucher", "lepidoptera",
    "semi-precious gemstones", "gem.pebbles", "colored stones.jpg",
    "diamond age zones", "cathodoluminescence", "zircon bracelet",
    "cubic zirconia",
    "grenat tsavorite(madagascar)", "lapis lazuli - flickr - the central",
    "cordiérite var. iolite", "gemstone collection - black toumaline",
    "pezzottaïte, amazonite, zircon",
    "bulletin 426", "bowlder outcrop", "diamond cross pendant",
    "post medieval ring bezel", "soft coral", "altarkreuz",
    "strong red fluorescence art deco", "pollucite (geodil number - 2948)",
    "wiperamingaite", "zunyite", "acrostic rings", "tetrahedrite",
    "andesine (organic compound)", "eb1911", "bulletin ", "rubies encased",
    "wedding anniversary set", "tojinbo cliffs", "heliophorus androcles",
    "corundum with andesine and biotite", "01722 andesine",
    "raw azuerite malachite", "azurite malachite cabochon gemstone",
    "corallium rubrum (red coral) 2", "crisocola azurite",
    "azurite malachite", "malachite azurite", "azurite-malachite",
    "orange onyx", "jupiter's tear",
    "pyrometallury",
]

# gem_id -> types pour lesquels on a renoncé à trouver une photo fiable
# (les recherches automatiques ne renvoient que des cristaux bruts, des
# collages ambigus ou des espèces voisines) ; on ne retente plus tant que
# cette liste n'est pas éditée à la main après vérification visuelle.
GIVE_UP_SLOTS = {
    ("lapis-lazuli", FACETTEE),
    ("tsavorite", FACETTEE),
    ("iolite", FACETTEE),
    ("tourmaline-noire", FACETTEE),
    ("zircon-blanc", FACETTEE),
    ("saphir-violet", BRUTE),
    ("saphir-violet", FACETTEE),
    ("saphir-blanc", BRUTE),
    ("saphir-blanc", FACETTEE),
    ("quartz-tourmaline", BRUTE),
    ("violane", FACETTEE),
    ("onyx", BRUTE),
    ("andesine", BRUTE),
}

# Mots qui trahissent une photo de pierre BRUTE quand on cherche une FACETTÉE,
# et inversement — évite qu'une recherche "faceted gemstone" ne renvoie une
# photo de cristal brut (et vice versa).
ROUGH_STATE_TOKENS = [
    "rough", "raw crystal", "crystal specimen", "on matrix",
    "crystal cluster", "uncut", "specimen", "in matrix",
]
CUT_STATE_TOKENS = [
    "faceted", "facetted", "cut gem", "cut stone", "cabochon",
]

ALLOWED_IMAGE_EXTENSIONS = (".jpg", ".jpeg", ".png")

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


def build_queries(term: str, image_type: str) -> list:
    if image_type == BRUTE:
        return [f"{term} rough crystal", f"{term} raw crystal", f"{term} crystal specimen", term]
    return [f"{term} faceted gemstone", f"{term} gemstone", f"{term} cut gem", term]


def pick_image(terms: list, keywords: list, image_type: str, already_used_titles: set) -> Optional[dict]:
    for term in terms:
        for query in build_queries(term, image_type):
            for title in search_candidates(query):
                if title in already_used_titles:
                    continue
                lower_title = title.lower()
                if not lower_title.endswith(ALLOWED_IMAGE_EXTENSIONS):
                    continue
                if any(tok in lower_title for tok in EXCLUDED_TITLE_TOKENS):
                    continue
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
                if width and width < 300:
                    continue

                description = strip_html(extmeta.get("ImageDescription", {}).get("value", ""))
                haystack = f"{lower_title} {description.lower()}"
                if any(tok in haystack for tok in EXCLUDED_TITLE_TOKENS):
                    continue
                if not any(kw in haystack for kw in keywords):
                    continue
                if image_type == FACETTEE and any(tok in haystack for tok in ROUGH_STATE_TOKENS):
                    continue
                if image_type == BRUTE and any(tok in haystack for tok in CUT_STATE_TOKENS):
                    continue

                artist = strip_html(extmeta.get("Artist", {}).get("value", "")) or "Auteur non renseigné"
                source_url = f"https://commons.wikimedia.org/wiki/{title.replace(' ', '_')}"
                download_url = info.get("thumburl") or info.get("url")

                return {
                    "title": title,
                    "download_url": download_url,
                    "license": license_short,
                    "artist": artist,
                    "source_url": source_url,
                }
    return None


def safe_resource_name(gem_id: str, image_type: str) -> str:
    name = re.sub(r"[^a-z0-9_]", "_", gem_id.lower())
    suffix = "brute" if image_type == BRUTE else "facette"
    return f"gem_{name}_{suffix}"


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
    kotlin_lines = [
        "package fr.gemsofrod.encyclopedie.data",
        "",
        "// Fichier généré automatiquement par scripts/fetch_gem_images.py",
        "// à partir de android/gem_image_credits.json (photos Wikimedia Commons).",
        "// Ne pas éditer à la main : relancer le workflow \"Fetch gem images\".",
        "",
        "enum class GemImageType { BRUTE, FACETTEE }",
        "",
        "data class GemImageCredit(",
        "    val type: GemImageType,",
        "    val drawableName: String,",
        "    val author: String,",
        "    val license: String,",
        "    val sourceUrl: String",
        ")",
        "",
        "object GemImages {",
        "    private val credits: Map<String, List<GemImageCredit>> = mapOf(",
    ]
    for gem_id in sorted(credits.keys()):
        images = credits[gem_id]
        if not images:
            continue
        entry_lines = []
        for image in images:
            entry_lines.append(
                "GemImageCredit(GemImageType.{type}, \"{resource_name}\", \"{artist}\", \"{license}\", \"{source_url}\")".format(
                    type=image["type"],
                    resource_name=image["resource_name"],
                    artist=kotlin_escape(image["artist"]),
                    license=kotlin_escape(image["license"]),
                    source_url=kotlin_escape(image["source_url"]),
                )
            )
        kotlin_lines.append(
            f"        \"{kotlin_escape(gem_id)}\" to listOf(" + ", ".join(entry_lines) + "),"
        )
    kotlin_lines += [
        "    )",
        "",
        "    fun creditsFor(gemId: String): List<GemImageCredit> = credits[gemId].orEmpty()",
        "}",
        "",
    ]
    KOTLIN_OUT.write_text("\n".join(kotlin_lines), encoding="utf-8")


def main() -> None:
    DRAWABLE_DIR.mkdir(parents=True, exist_ok=True)
    KOTLIN_OUT.parent.mkdir(parents=True, exist_ok=True)

    credits = load_credits()
    total_slots = sum(len(types) for _, _, _, types in GEMS)
    print(f"{total_slots} photo(s) à couvrir au total (brute + facettée pour la plupart des gemmes).")

    report_lines = [
        "# Rapport de récupération des photos (Wikimedia Commons)",
        "",
        "Licences acceptées : domaine public, CC0, CC BY, CC BY-SA uniquement.",
        "Deux photos par gemme quand possible : pierre brute et pierre facettée/taillée.",
        "",
        "| Gemme | Brute | Facettée |",
        "|---|---|---|",
    ]

    for gem_id, terms, keywords, types in GEMS:
        gem_credits = {c["type"]: c for c in credits.get(gem_id, [])}
        status = {}

        for image_type in types:
            existing = gem_credits.get(image_type)

            if existing:
                existing_dest = DRAWABLE_DIR / f"{existing['resource_name']}.jpg"
                if existing_dest.exists():
                    status[image_type] = f"♻️ [{existing['title']}]({existing['source_url']})"
                    continue

            if (gem_id, image_type) in GIVE_UP_SLOTS:
                print(f"-> {gem_id} [{image_type}]: recherche abandonnée (voir GIVE_UP_SLOTS)")
                status[image_type] = "⏸️ recherche suspendue"
                continue

            resource_name = safe_resource_name(gem_id, image_type)
            dest = DRAWABLE_DIR / f"{resource_name}.jpg"

            print(f"-> {gem_id} [{image_type}]: recherche « {' / '.join(terms)} »")
            already_used = {c["title"] for c in gem_credits.values()}
            try:
                result = pick_image(terms, keywords, image_type, already_used)
            except Exception as exc:  # noqa: BLE001
                print(f"  [!] erreur recherche pour {gem_id} [{image_type}]: {exc}", file=sys.stderr)
                result = None

            if not result:
                print(f"  [x] aucune image libre trouvée pour {gem_id} [{image_type}]")
                status[image_type] = "❌ non trouvée"
                continue

            try:
                download_image(result["download_url"], dest)
            except Exception as exc:  # noqa: BLE001
                print(f"  [!] échec téléchargement pour {gem_id} [{image_type}]: {exc}", file=sys.stderr)
                status[image_type] = "❌ échec téléchargement"
                continue

            print(f"  [ok] {result['title']} ({result['license']}, {result['artist']})")
            gem_credits[image_type] = {
                "type": image_type,
                "resource_name": resource_name,
                "title": result["title"],
                "artist": result["artist"],
                "license": result["license"],
                "source_url": result["source_url"],
            }
            status[image_type] = f"✅ [{result['title']}]({result['source_url']})"

        credits[gem_id] = list(gem_credits.values())
        report_lines.append(
            f"| {gem_id} | {status.get(BRUTE, '—')} | {status.get(FACETTEE, '—')} |"
        )

    save_credits(credits)
    write_kotlin(credits)

    found = sum(1 for c in credits.values() for _ in c)
    report_lines.append("")
    report_lines.append(f"**{found} / {total_slots}** photos récupérées au total.")
    REPORT_OUT.write_text("\n".join(report_lines) + "\n", encoding="utf-8")

    print(f"\n{found}/{total_slots} photos récupérées.")


if __name__ == "__main__":
    main()
