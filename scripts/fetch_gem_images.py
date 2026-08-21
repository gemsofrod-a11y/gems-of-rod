#!/usr/bin/env python3
"""
Récupère, pour chaque gemme de l'encyclopédie, une photo de la pierre BRUTE
et une photo de la pierre FACETTÉE/taillée sur Wikimedia Commons, et génère :
  - les fichiers image dans android/app/src/androidMain/res/drawable-nodpi/
  - android/gem_image_credits.json (source de vérité : crédits accumulés)
  - android/app/src/commonMain/kotlin/fr/gemsofrod/encyclopedie/data/GemImages.kt
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
OPENVERSE_API_URL = "https://api.openverse.org/v1/images/"
USER_AGENT = "GemsOfRodEncyclopedieBot/1.0 (contact: gemsofrod@gmail.com)"

REPO_ROOT = Path(__file__).resolve().parent.parent
DRAWABLE_DIR = REPO_ROOT / "android/app/src/androidMain/res/drawable-nodpi"
KOTLIN_OUT = REPO_ROOT / "android/app/src/commonMain/kotlin/fr/gemsofrod/encyclopedie/data/GemImages.kt"
CREDITS_JSON = REPO_ROOT / "android/gem_image_credits.json"
REPORT_OUT = REPO_ROOT / "android/IMAGE_FETCH_REPORT.md"

BRUTE = "BRUTE"
FACETTEE = "FACETTEE"
INCLUSION = "INCLUSION"

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
    ("emeraude", ["Emerald beryl", "Emerald gemstone faceted", "Beryl emerald green"], ["emerald"], (BRUTE, FACETTEE)),
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
    ("onyx", ["Black onyx rough", "Black onyx chalcedony", "Black banded agate rough", "Onyx nodule black rough", "Black onyx cabochon", "Onyx cabochon polished"], ["onyx", "chalcedony", "agate"], (BRUTE, FACETTEE)),
    ("spinelle-noir", ["Black spinel"], ["spinel"], (BRUTE, FACETTEE)),
    ("tourmaline-noire", ["Schorl tourmaline", "Schorl"], ["schorl", "tourmaline"], (BRUTE, FACETTEE)),
    ("obsidienne", ["Obsidian"], ["obsidian"], (BRUTE, FACETTEE)),
    ("alexandrite", ["Alexandrite"], ["alexandrite"], (BRUTE, FACETTEE)),
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
    ("saphir-auvergne", ['Auvergne sapphire', 'French sapphire Espaly', 'Basaltic sapphire France', 'Blue violet sapphire crystal basalt'], ['sapphire', 'corundum', 'auvergne', 'espaly'], (BRUTE, FACETTEE)),
    ("bixbite", ['Red beryl gemstone', 'Red beryl crystal', 'Bixbite', 'Red beryl Utah', 'Red beryl Wah Wah'], ['red beryl', 'bixbite'], (BRUTE, FACETTEE)),
    ("saphir-teal", ['Teal sapphire', 'Teal sapphire Montana', 'Parti sapphire', 'Bi-color sapphire blue yellow', 'Montana sapphire teal'], ['teal sapphire', 'parti sapphire', 'sapphire', 'corundum'], (BRUTE, FACETTEE)),
    ("ametrine", ['Ametrine', 'Ametrine crystal', 'Ametrine Bolivia', 'Ametrine gemstone faceted'], ['ametrine'], (BRUTE, FACETTEE)),
    ("aventurine", ['Green aventurine quartz', 'Aventurine gemstone', 'Aventurine cabochon'], ['aventurine'], (BRUTE, FACETTEE)),
    ("epidote", ['Epidote crystal', 'Epidote gemstone faceted'], ['epidote'], (BRUTE, FACETTEE)),
    ("gaspeite", ['Gaspeite cabochon', 'Gaspeite gemstone', 'Gaspeite rough'], ['gaspeite'], (BRUTE, FACETTEE)),
    ("hiddenite", ['Hiddenite crystal', 'Hiddenite gemstone faceted', 'Green spodumene hiddenite'], ['hiddenite'], (BRUTE, FACETTEE)),
    ("angelite", ['Angelite gemstone', 'Blue anhydrite cabochon', 'Angelite crystal'], ['angelite', 'anhydrite'], (BRUTE, FACETTEE)),
    ("oeil-de-faucon", ['Hawk eye gemstone', 'Hawks eye cabochon', 'Falcon eye stone'], ['hawk', 'falcon'], (BRUTE, FACETTEE)),
    ("howlite", ['Howlite gemstone', 'White howlite cabochon', 'Howlite mineral'], ['howlite'], (BRUTE, FACETTEE)),
    ("rhodizite", ['Rhodizite crystal', 'Rhodizite gemstone Madagascar'], ['rhodizite'], (BRUTE, FACETTEE)),
    ("sanidine", ['Sanidine crystal', 'Sanidine gemstone faceted', 'Sanidine feldspar Eifel'], ['sanidine'], (BRUTE, FACETTEE)),
    ("tremolite", ['Tremolite crystal', 'Tremolite gemstone', 'Hexagonite tremolite'], ['tremolite'], (BRUTE, FACETTEE)),
    ("oeil-de-taureau", ["Bull's eye gemstone", 'Red tiger eye cabochon', 'Bulls eye quartz'], ['bull', 'tiger eye'], (BRUTE, FACETTEE)),
    ("unakite", ['Unakite gemstone', 'Unakite cabochon', 'Unakite rough'], ['unakite'], (BRUTE, FACETTEE)),
    ("oeil-de-sainte-lucie", ['Turbo operculum shell', "Cat's eye operculum shell", 'Saint Lucia eye shell'], ['operculum'], (FACETTEE,)),
    ("jais", ['Jet gemstone', 'Whitby jet', 'Jet mineraloid polished'], ['jet'], (BRUTE, FACETTEE)),
    ("grenat-etoile", ['Star garnet Idaho', 'Star almandine garnet', 'Star garnet cabochon'], ['star garnet'], (BRUTE, FACETTEE)),
    ("opale-peruvienne", ['Peruvian opal', 'Blue opal Peru', 'Peruvian pink opal'], ['peruvian opal'], (BRUTE, FACETTEE)),
    ("opale-commune", ['Common opal', 'White opal rough', 'Milky opal gemstone'], ['common opal', 'opal'], (BRUTE, FACETTEE)),
    ("opale-noire", ['Black opal Lightning Ridge', 'Black opal gemstone'], ['black opal'], (BRUTE, FACETTEE)),
    ("opale-boulder", ['Boulder opal', 'Boulder opal Queensland'], ['boulder opal'], (BRUTE, FACETTEE)),
    ("opale-ethiopie", ['Ethiopian opal', 'Welo opal', 'Ethiopian opal gemstone'], ['ethiopian opal', 'welo opal'], (BRUTE, FACETTEE)),

    # Météorites (section dédiée, distincte du catalogue Gemmologie)
    ("meteorite-gibeon", ['Gibeon meteorite slice', 'Gibeon meteorite Widmanstatten', 'Gibeon meteorite specimen'], ['gibeon'], (BRUTE, FACETTEE)),
    ("meteorite-sikhote-alin", ['Sikhote-Alin meteorite', 'Sikhote Alin meteorite individual'], ['sikhote'], (BRUTE, FACETTEE)),
    ("meteorite-campo-del-cielo", ['Campo del Cielo meteorite', 'Campo del Cielo meteorite slice'], ['campo del cielo'], (BRUTE, FACETTEE)),
    ("meteorite-muonionalusta", ['Muonionalusta meteorite slice', 'Muonionalusta meteorite Widmanstatten'], ['muonionalusta'], (BRUTE, FACETTEE)),
    ("meteorite-nantan", ['Nantan meteorite', 'Nantan iron meteorite specimen'], ['nantan'], (BRUTE, FACETTEE)),
    ("meteorite-canyon-diablo", ['Canyon Diablo meteorite', 'Canyon Diablo meteorite slice'], ['canyon diablo'], (BRUTE, FACETTEE)),
    ("meteorite-seymchan", ['Seymchan pallasite', 'Seymchan meteorite slice'], ['seymchan'], (BRUTE, FACETTEE)),
    ("meteorite-esquel", ['Esquel pallasite', 'Esquel meteorite slice'], ['esquel'], (BRUTE, FACETTEE)),
    ("meteorite-imilac", ['Imilac pallasite', 'Imilac meteorite slice'], ['imilac'], (BRUTE, FACETTEE)),
    ("meteorite-admire", ['Admire pallasite', 'Admire pallasite meteorite'], ['admire'], (BRUTE, FACETTEE)),
    ("meteorite-fukang", ['Fukang pallasite', 'Fukang meteorite slice'], ['fukang'], (BRUTE, FACETTEE)),
    ("meteorite-chelyabinsk", ['Chelyabinsk meteorite', 'Chelyabinsk meteorite fragment'], ['chelyabinsk'], (BRUTE, FACETTEE)),
    ("meteorite-peekskill", ['Peekskill meteorite'], ['peekskill'], (BRUTE,)),
    ("meteorite-nwa-chondrite", ['Northwest Africa chondrite meteorite', 'NWA chondrite meteorite slice', 'Saharan chondrite meteorite'], ['chondrite', 'northwest africa', 'nwa'], (BRUTE, FACETTEE)),
    ("meteorite-murchison", ['Murchison meteorite', 'Murchison meteorite fragment'], ['murchison'], (BRUTE,)),
    ("meteorite-allende", ['Allende meteorite', 'Allende meteorite fragment'], ['allende'], (BRUTE,)),
    ("meteorite-hoba", ['Hoba meteorite', 'Hoba meteorite Namibia'], ['hoba'], (BRUTE,)),
    ("meteorite-willamette", ['Willamette meteorite', 'Willamette meteorite museum'], ['willamette'], (BRUTE,)),
    ("meteorite-ensisheim", ['Ensisheim meteorite', 'Ensisheim meteorite stone'], ['ensisheim'], (BRUTE,)),
    ("meteorite-nwa-7034", ['NWA 7034 meteorite', 'Black Beauty meteorite Mars'], ['nwa 7034', 'black beauty'], (BRUTE,)),
    ("meteorite-alh-84001", ['ALH 84001 meteorite', 'Allan Hills 84001 meteorite'], ['alh 84001', 'allan hills'], (BRUTE,)),
    ("meteorite-lunaire-nwa", ['Lunar meteorite NWA', 'Lunar meteorite fragment'], ['lunar meteorite'], (BRUTE,)),

    # ---------- FOSSILES ----------
    ("fossile-ammonite-madagascar", ['Ammonite fossil Madagascar', 'Ammonite fossil rough', 'Ammonite fossil polished slice'], ['ammonite'], (BRUTE, FACETTEE)),
    ("fossile-ammolite", ['Ammolite specimen', 'Ammolite gemstone', 'Ammolite polished'], ['ammolite'], (BRUTE, FACETTEE)),
    ("fossile-orthocere", ['Orthoceras fossil', 'Orthoceras fossil polished slab'], ['orthoceras', 'orthocone'], (BRUTE, FACETTEE)),
    ("fossile-goniatite", ['Goniatite fossil', 'Goniatite fossil specimen'], ['goniatite'], (BRUTE,)),
    ("fossile-trilobite-elrathia", ['Elrathia kingii fossil', 'Elrathia trilobite fossil'], ['elrathia', 'trilobite'], (BRUTE,)),
    ("fossile-trilobite-phacops", ['Phacops rana fossil', 'Phacops trilobite fossil'], ['phacops', 'trilobite'], (BRUTE,)),
    ("fossile-trilobite-calymene", ['Calymene trilobite fossil', 'Calymene fossil specimen'], ['calymene', 'trilobite'], (BRUTE,)),
    ("fossile-dent-megalodon", ['Megalodon tooth fossil', 'Megalodon shark tooth'], ['megalodon'], (BRUTE,)),
    ("fossile-os-dinosaure-gembone", ['Agatized dinosaur bone', 'Dinosaur bone jasper', 'Fossilized dinosaur bone polished slice'], ['dinosaur bone', 'agatized', 'gembone'], (BRUTE, FACETTEE)),
    ("fossile-poisson-green-river", ['Knightia fossil fish Green River', 'Fossil fish Green River Formation'], ['knightia', 'green river', 'fossil fish'], (BRUTE,)),
    ("fossile-dent-mosasaure", ['Mosasaur tooth fossil', 'Mosasaurus tooth fossil'], ['mosasaur'], (BRUTE,)),
    ("fossile-ivoire-mammouth", ['Mammoth tusk', 'Woolly mammoth ivory', 'Mammoth ivory museum', 'Mammuthus tusk fossil'], ['mammoth', 'ivory', 'tusk'], (BRUTE, FACETTEE)),
    ("fossile-bois-petrifie", ['Petrified wood specimen', 'Petrified wood rough', 'Petrified wood polished slice'], ['petrified wood'], (BRUTE, FACETTEE)),
    ("fossile-fougere", ['Fossil fern', 'Pecopteris fossil', 'Fern fossil shale imprint'], ['fern', 'pecopteris', 'fossil'], (BRUTE,)),
    ("fossile-ambre-baltique", ['Baltic amber raw', 'Baltic amber specimen', 'Baltic amber polished'], ['baltic amber', 'amber'], (BRUTE, FACETTEE)),
    ("fossile-ambre-inclusion", ['Amber insect inclusion', 'Dominican amber insect inclusion'], ['amber', 'insect', 'inclusion'], (BRUTE,)),
    ("fossile-copal", ['Copal amber resin', 'Kauri copal resin specimen', 'Copal resin nugget'], ['copal'], (BRUTE,)),
    ("fossile-corail-agatise", ['Agatized coral fossil', 'Agatized coral Florida', 'Agatized coral polished'], ['agatized coral'], (BRUTE, FACETTEE)),
    ("fossile-oursin", ['Fossil sea urchin echinoid', 'Echinoid fossil test'], ['echinoid', 'sea urchin fossil'], (BRUTE,)),
    ("fossile-crinoide", ['Crinoid stem fossil', 'Crinoidal limestone polished'], ['crinoid'], (BRUTE, FACETTEE)),
    ("fossile-stromatolite", ['Stromatolite fossil specimen', 'Stromatolite cross section polished', 'Stromatolite slab cut'], ['stromatolite'], (BRUTE, FACETTEE)),
    ("fossile-belemnite", ['Belemnite fossil rostrum', 'Belemnite fossil specimen'], ['belemnite'], (BRUTE,)),
]

# Photos dédiées aux inclusions typiques (macro/loupe), distinctes des photos
# de la pierre entière (brute/facettée) ci-dessus. Liste volontairement
# restreinte aux espèces où une inclusion précise est l'attrait visuel
# reconnaissable de la pierre (jardin, soie, chiastolite, insecte dans
# l'ambre...) : c'est là que des photos librement réutilisables existent
# réellement, contrairement à la photomicrographie de laboratoire qui est
# presque toujours sous droits.
INCLUSION_GEMS = [
    ("rubis", ["Ruby silk inclusions", "Ruby rutile silk"], ["ruby", "silk", "rutile"]),
    ("rubis-etoile", ["Star ruby asterism", "Star ruby cabochon macro"], ["star ruby", "asterism"]),
    ("saphir-etoile", ["Star sapphire asterism", "Star sapphire cabochon macro"], ["star sapphire", "asterism"]),
    ("saphir-bleu", ["Sapphire silk inclusions", "Sapphire rutile silk"], ["sapphire", "silk", "rutile"]),
    ("emeraude", ["Emerald jardin inclusions", "Emerald inclusions macro"], ["emerald"]),
    ("quartz-rutile", ["Rutilated quartz inclusions macro", "Rutile needles in quartz"], ["rutilated quartz", "rutile"]),
    ("quartz-a-inclusions", ["Lodolite quartz inclusions", "Garden quartz inclusions macro"], ["lodolite", "garden quartz", "quartz"]),
    ("quartz-super-sept", ["Super seven quartz inclusions macro"], ["super seven", "quartz"]),
    ("ambre", ["Amber insect inclusion", "Baltic amber insect inclusion"], ["amber", "insect", "inclusion"]),
    ("demantoide", ["Demantoid horsetail inclusion", "Demantoid garnet inclusions"], ["demantoid", "horsetail", "byssolite"]),
    ("peridot", ["Peridot lily pad inclusion", "Peridot chromite inclusion"], ["peridot", "lily pad", "chromite"]),
    ("tourmaline-pasteque", ["Watermelon tourmaline slice", "Watermelon tourmaline cross section"], ["watermelon tourmaline"]),
    ("oeil-de-tigre", ["Tiger's eye chatoyance macro", "Tiger eye fibers macro"], ["tiger", "chatoyance", "chatoyant"]),
    ("pierre-de-lune", ["Moonstone adularescence macro", "Moonstone schiller"], ["moonstone", "adularescence"]),
    ("labradorite", ["Labradorite labradorescence macro", "Labradorite schiller"], ["labradorite", "labradorescence"]),
    ("andalousite", ["Chiastolite cross section", "Andalusite chiastolite cross"], ["chiastolite", "andalusite", "andalousite"]),
]

# Sous-ensemble d'EXCLUDED_TITLE_TOKENS non pertinent pour une recherche
# d'inclusion : ces mots désignaient justement le type de photo qu'on
# cherche ici (on les excluait pour les photos de pierre entière BRUTE/
# FACETTEE, où une vue au microscope ou en lame mince serait hors sujet).
INCLUSION_ALLOWED_TOKENS = {
    "microscope", "thin section", "polarized light", "inclusions in a gem",
    "cathodoluminescence",
}

# gem_id -> recherche d'inclusion suspendue après vérification manuelle
# qu'aucune photo librement réutilisable n'existe (même principe que
# GIVE_UP_SLOTS ci-dessous pour BRUTE/FACETTEE).
#
# "emeraude" a été retirée de cette liste : le faux positif récurrent
# (xénolithe de lherzolite/kimberlite) est maintenant filtré à la racine
# par EXCLUDED_TITLE_TOKENS ci-dessous, la recherche peut donc être retentée.
GIVE_UP_INCLUSION_SLOTS: set = set()

EXCLUDED_TITLE_TOKENS = [
    # Toute une série de photos de xénolithes de lherzolite (roche du manteau
    # remontée par des pipes de kimberlite) sur Commons mentionne "emerald"
    # dans sa légende en passant (le kimberlite est aussi la roche hôte du
    # diamant) : un faux positif récurrent sur les recherches d'inclusions
    # d'émeraude tant que ces termes géologiques ne sont pas exclus.
    "xenolith", "lherzolite", "kimberlite",
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
    "levigating azurite", "carbonate minerals and crystals display",
    "rubis, calcite 14", "citrine x smoky quartz",
    "sapphire (corundum) (26663158752)",
    "azurite malachite", "malachite azurite", "azurite-malachite",
    "orange onyx", "jupiter's tear",
    "pyrometallury",
    "opal and gemstone studio, hahndorf", "sedimentation of azurite fraction",
    "chrysocolla.jpg", "chrysoprase brute", "red coral gemstone.jpg",
    "gemstone hearts",
    "afghanite sur gangue de calcite (afghanistan)",
    "amazonite-8819",
    "amber hg",
    "amethyst. magaliesburg, south africa",
    "anhydrite arnave",
    "antigorite (variety bowenite)-346848",
    "apatite (geodil number - 1734)",
    "aquamarine p1000141",
    "azul macauba dumortierite quartzite caillois donation mnhn",
    "barite - cerro warihuyn, miraflores, huamalies, huanuco, peru",
    "benitoite hd",
    "black spinel from sri lanka. erbil stones and gems museum, erbil citadel, hawler, iraq",
    "béryl var. émeraude sur gangue (muzo mine boyaca - colombie) 2",
    "cavansite on stilbite",
    "celestine - sakoany deposit, katsepy, mitsinjo, boeny, madagascar",
    "cerussite. tsumeb, namibia-8886",
    "charoite mfrey",
    "chrysoberyl-282796",
    "chrysobéryl var. alexandrite sous uv (brésil) 1",
    "cobaltoan calcite 8",
    "corallium rubrum (red coral) 1 (15691074776)",
    "corundum, garnet, mica, feldspar 3",
    "crocoite dundas minéraux su",
    "cronstedtite and quartz 01",
    "danburitemexique",
    "diaspore, musee de mineralogie, paris, 2025",
    "diopside 3",
    "dioptasetsumeb5",
    "euclase 7",
    "feldspar - bytownite sodium calcium aluminum silicate crystal bay minnesota 2691",
    "fenacitas",
    "fotostrecke weltraritaeten-painit-g-empiretheworldofgems",
    "garnet lherzolite - xenolith from a kimberlite pipe (precambrian, likely archean; kimberley, central south africa) 1 (15001208861)",
    "gemstone collection - labradorite (17278919981)",
    "gemstone collection - rainbow calcite (15808137826)",
    "goshenite. xuebaoding mt., mianyang city, sichuan province, china-9046",
    "grenat pyrope 1",
    "hackmanite - sw",
    "hauyne eifel",
    "hemimorphite from sardinia 02",
    "hessonite garnet 0019",
    "hyalite opal valec",
    "hypersthene with labradorite (geodil number - 1624)",
    "jasper slices (4715490113)",
    "lazulite rapid creek mnhn minéralogie n2",
    "libyan desert glass 3",
    "magnetite section",
    "maine mineral and gem museum, bethel - elbaite tourmaline - watermelon tourmaline",
    "malachite, zaire",
    "medieval finger ring (findid 564076)",
    "muséum de nantes - 244 - eudialyte (russie)",
    "opal and gemstone studio, hahndorf 20251219-122436",
    "orthoclase (geodil number - 1016)",
    "petalite bg",
    "pietra paesina val d'arno caillois donation mnhn n12",
    "pink topaz nhmla",
    "polished slice of petrified wood (large image)",
    "prehnite-epidote-178957",
    "purpurite - sandamab pegmatite, erongo region, namibia 2",
    "pyrite - huanzala mine, huallanca, bolognesi, ancash, peru",
    "quartz a rutile",
    "rhodochrosite on matrix - peru",
    "rhodonite cut",
    "sapphirine, muscovite",
    "shattuckite - conichalcite",
    "siberian-nephrite-jade-bangle-vong-kieng-ngoc-bich-kimquigems",
    "sinhalite. wet lu mine, mogok, burma-9121",
    "smithsonite 5",
    "sodalith - rohstein",
    "spinel 1",
    "spinel-structure-tetrahedra",
    "spinelle et chondrodite (chine) 2",
    "stichtite 2 on serpentine basic hydrous magnesium chromate and carbonate new amianthus mine transvaal south africa 1624",
    "sugilite 1",
    "thulite 2",
    "titanite, albite, epidote, minas gerais, brazil-8799",
    "topaze9",
    "tourmaline polychrome sur mica 2(brésil)",
    "tourmaline, feldspar",
    "uvarovite-200516",
    "variscite-crandallite-188268",
    "vaso de jaspe con neptuno y caracol (prado o-54) 01",
    "vesuvianite, musee de mineralogie, paris, 2025",
    "zircon,the oldest rock on earth",
    "zoïsite (tanzanite)",
    "onyx.jpg",
    "afghanite, pyrite, calcite 5",
    "amber shop",
    "anhydrite from sulitjelma",
    "azurite - new nevada lode, la sal, utah, usa",
    "baryte. iglesias, sardinia, italy-8963",
    "benitoite new",
    "black spinel hagi stakes 2015",
    "blue apatite from bangladesh. erbil stones and gems museum, erbil citadel, hawler, iraq",
    "blue lace agate from namibia (polished)",
    "bytownite, musee de mineralogie, paris, 2025",
    "cavansite displayed at mining museum of akita university",
    "celestite nhmla",
    "cerussite tsumeb minéraux su",
    "charoite, aegirine, tinaksite 2",
    "chromian diopside (geomuseum cologne)2",
    "chrysoberyl, quartz, tourmaline",
    "chrysoprase - brazil (2933074084)",
    "cobaltoan calcite 9",
    "corundum, garnet, mica, feldspar 1",
    "dacite obsidian-flint cross",
    "danburite 2581",
    "diaspore structure",
    "dioptase. reneville, kongo-9082",
    "euclase. galileia, brazil-9104",
    "eudialyte, albite 1",
    "garnet lherzolite - xenolith from a kimberlite pipe (precambrian, likely archean; kimberley, central south africa) 2",
    "garnet.uvarovite.500pix",
    "gemstone collection - amazonite (20819960019)",
    "gemstone collection - rainbow calcite (16265181917)",
    "hackmanite - mw",
    "hemimorphite and galena 01",
    "hermitage hall 129 - pompeian hall 11",
    "hessonite, clinochlore ",
    "hyalite opal dubnik",
    "indicolite (brésil)",
    "iziko mineral zircon (cropped)",
    "jasper slices (4716125066)",
    "labradorite detail",
    "lazulite. graves mt., georgia, usa-9164",
    "libyan desert glass (oligocene, 28.5 ma; libyan desert, egypt) 10",
    "magnetite, pfitscher joch, zillertaler alpen, austria-8795",
    "malachite-1579",
    "meandrina meandrites (maze coral)",
    "moscow state university rhodonite 2014-01 1389530950",
    "naturalis biodiversity center - chrysocolla - mineral",
    "nephrite-jade-pebble-tu-lieu-bach-ngoc-hoa-dien-kimquigems",
    "norite (geodil number - 93)",
    "nz bowenite",
    "painite crystal",
    "petrified wood img 9470",
    "pietra paesina val d'arno caillois donation mnhn n13",
    "prehnite - southbury, connecticut, usa",
    "purpurite 4",
    "pyrite from ampliación a victoria mine, navajún, la rioja, spain 2",
    "rutile (tio2) crystal habit (other views)",
    "sapphirine, mica 1",
    "sinhalite img 9802r 20160410",
    "smithsonite on cerussite",
    "sodalite badakhshan minéraux su",
    "souterroscope des ardoisières crocoite dundas tasmanie",
    "spinel-anreotti-et-al-2000",
    "spinelle et chondrodite (chine) 4",
    "stichtite with serpentine (47122029074)",
    "tanzanitebracelet",
    "thulite (geodil number - 1967)",
    "thunder bay amethyst with included hematite",
    "titanite - albite - actinolite - laumontite 01",
    "topaz, mica 4",
    "topaz, virgem de lapa, minas gerais, brazil-8781",
    "topazerose2",
    "turquoise with quartz",
    "vesuvianite fushan mnhn minéralogie",
    "watermelon tourmaline unpolished",
    "zirconusgov",
    "00021 11 cm variscite",
    "14k gembone wedding ring",
    "afghanite-t06-330c",
    "apophyllite with quartz, marcasite and hematite - apophyllite with quartz, marcasite and hematite",
    "baryte morocco",
    "bastnasite-(ce)-284046",
    "benitoite-dtn18a",
    "bytownite-mrz296a",
    "cerussite-133531",
    "chondrodite-mf30b",
    "chrysoberyl - chrysoberyl from oxford co., maine",
    "danburite-34745",
    "dundasite and crocoite",
    "elbaite-290583",
    "euclase 1",
    "eudialyte-es36b",
    "gold signet ring with an oval bezel set with a purple spinel intaglio of a crowned head and engraved in black letter 'tel il nest' ('there is none like him'). the intaglio france, late 1350-1400, the setting england, 1400-1500. gold signet ring with an oval bezel set with a purple spinel intaglio of a crowned head and engraved in black letter 'tel il nest'. the shoulders engraved with sprigs, with traces of enamel. engraved gold set with a purple spinel intaglio with traces of enamel.",
    "hackmanite, winchite 1",
    "hambergite-rare-09-15a",
    "hauyne-169904",
    "hemimorphite-165288",
    "hessonite, dolomite, clinochlore 300-4-1004",
    "jeremejevite-39096",
    "kornerupine-162065",
    "kosmochlor jade, jurassic, burma 1",
    "labradorite with hypersthene - labradorite (plagioclase) with hypersthene (orthopyroxene)",
    "larimar-ring hg",
    "lazulite-rh02-21b",
    "luminescence of diopside (violan) in ultraviolet rays",
    "malachite - malachite",
    "mawsitsit (kosmochlor jade) kachin state, burma",
    "orthoclase-titanite-dtn19a",
    "painite-266170",
    "petalite-tmu19a",
    "phosphosiderite-rockbridgeite-k330a",
    "pietra paesina di firenze 260x95 mm.",
    "polished slice of petrified wood",
    "prehnite-epidote-49136",
    "purpurite - sandanab, namibia",
    "pyrope-275003",
    "red coral gemstone",
    "sapphirine-203257",
    "sculptured sodalite gemstones 1",
    "serpentine-bowenite",
    "shattuckite-k-123a",
    "sinhalite-656999",
    "smithsonite - botryoidal smithsonite",
    "titanite (sphene) - titanite (sphene), feldspar and quartz from eganville, ontario",
    "topaz-pb39b",
    "vesuvianite asbestos",
    "zircon - zircon",
    "zircon-249203",
    "emerald cut diamond",
    "goldberyll",
    "cardinal gems",
]

# gem_id -> types pour lesquels on a renoncé à trouver une photo fiable
# (les recherches automatiques ne renvoient que des cristaux bruts, des
# collages ambigus ou des espèces voisines) ; on ne retente plus tant que
# cette liste n'est pas éditée à la main après vérification visuelle.
GIVE_UP_SLOTS = {
    ("opale-commune", BRUTE),
    ("opale-commune", FACETTEE),
    ("emeraude", FACETTEE),
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
    ("saphir-vert", BRUTE),
    # Ci-dessous : espèces dont les 2 tentatives automatiques successives
    # n'ont renvoyé que des spécimens bruts / cristaux sur gangue / bijoux
    # montés / photos hors-sujet pour le créneau FACETTEE. On arrête les
    # frais tant qu'une recherche manuelle n'aura pas été faite.
    ("afghanite", FACETTEE),
    ("amazonite", FACETTEE),
    ("ambre", FACETTEE),
    ("anhydrite", FACETTEE),
    ("apatite-bleue", FACETTEE),
    ("baryte", FACETTEE),
    ("benitoite", FACETTEE),
    ("bois-fossilise", FACETTEE),
    ("bytownite", FACETTEE),
    ("calcite", FACETTEE),
    ("cavansite", FACETTEE),
    ("celestite", FACETTEE),
    ("cerusite", FACETTEE),
    ("charoite", FACETTEE),
    ("chondrodite", FACETTEE),
    ("chrysoberyl", FACETTEE),
    ("cobaltocalcite", FACETTEE),
    ("crocoite", FACETTEE),
    ("danburite", FACETTEE),
    ("diaspore-zultanite", FACETTEE),
    ("diopside", FACETTEE),
    ("dioptase", FACETTEE),
    ("enstatite", FACETTEE),
    ("euclase", FACETTEE),
    ("eudialyte", FACETTEE),
    ("goshenite", FACETTEE),
    ("grenat-rhodolite-violet", FACETTEE),
    ("hackmanite", FACETTEE),
    ("hauyne", FACETTEE),
    ("hemimorphite", FACETTEE),
    ("hessonite", FACETTEE),
    ("hyalite", FACETTEE),
    ("hyperstene", FACETTEE),
    ("jade-nephrite", FACETTEE),
    ("jaspe", FACETTEE),
    ("labradorite", FACETTEE),
    ("larimar", FACETTEE),
    ("lazulite", FACETTEE),
    ("lepidolite", FACETTEE),
    ("magnetite", FACETTEE),
    ("malachite", FACETTEE),
    ("obsidienne", FACETTEE),
    ("oeil-de-tigre", FACETTEE),
    ("paesine", FACETTEE),
    ("painite", FACETTEE),
    ("pietersite", FACETTEE),
    ("prehnite", FACETTEE),
    ("purpurite", FACETTEE),
    ("pyrite", FACETTEE),
    ("rhodonite", FACETTEE),
    ("rutile", FACETTEE),
    ("sapphirine", FACETTEE),
    ("serpentine", FACETTEE),
    ("sinhalite", FACETTEE),
    ("smithsonite", FACETTEE),
    ("sodalite", FACETTEE),
    ("sphene", FACETTEE),
    ("spinelle-bleu", FACETTEE),
    ("spinelle-noir", FACETTEE),
    ("stichtite", FACETTEE),
    ("tanzanite", FACETTEE),
    ("thulite", FACETTEE),
    ("topaze-imperiale", FACETTEE),
    ("topaze-rose", FACETTEE),
    ("tourmaline-bleue", FACETTEE),
    ("tourmaline-pasteque", FACETTEE),
    ("turquoise", FACETTEE),
    ("uvarovite", FACETTEE),
    ("variscite", FACETTEE),
    ("verre-libyque", FACETTEE),
    ("vesuvianite", FACETTEE),
    ("zircon-dore", FACETTEE),
    ("zircon-hyacinthe", FACETTEE),
    ("ammolite", FACETTEE),
    ("azurite", FACETTEE),
    ("chrysocolle", FACETTEE),
    ("corail", FACETTEE),
    ("hematite", FACETTEE),
    ("bastnasite", FACETTEE),
    ("hambergite", FACETTEE),
    ("jeremejevite", FACETTEE),
    ("kornerupine", FACETTEE),
    ("maw-sit-sit", BRUTE),
    ("maw-sit-sit", FACETTEE),
    ("orthose", FACETTEE),
    ("os-fossilise", BRUTE),
    ("petalite", FACETTEE),
    ("phosphosiderite", FACETTEE),
    ("shattuckite", FACETTEE),
    ("spinelle-violet", FACETTEE),
    ("tourmaline-jaune", FACETTEE),    ("afghanite", BRUTE),
    ("alexandrite", BRUTE),
    ("alexandrite", FACETTEE),
    ("andalousite", FACETTEE),
    ("beryl-vert", FACETTEE),
    ("cavansite", BRUTE),
    ("chrysocolle", BRUTE),
    ("cristal-de-roche", FACETTEE),
    ("demantoide", BRUTE),
    ("diamant", FACETTEE),
    ("diamant", BRUTE),
    ("diopside", BRUTE),
    ("dioptase", BRUTE),
    ("dumortierite", BRUTE),
    ("dumortierite", FACETTEE),
    ("eudialyte", BRUTE),
    ("goshenite", BRUTE),
    ("grandidierite", FACETTEE),
    ("grenat-almandin", BRUTE),
    ("grenat-grossulaire", FACETTEE),
    ("grenat-malaya", BRUTE),
    ("grenat-pyrope", BRUTE),
    ("grenat-rhodolite", BRUTE),
    ("grenat-spessartite", BRUTE),
    ("hematite", BRUTE),
    ("hemimorphite", BRUTE),
    ("hyperstene", BRUTE),
    ("iolite", BRUTE),
    ("jade-jadeite", BRUTE),
    ("jade-nephrite", BRUTE),
    ("kornerupine", BRUTE),
    ("magnetite", BRUTE),
    ("orthose", BRUTE),
    ("padparadscha", BRUTE),
    ("pierre-de-lune", BRUTE),
    ("quartz-fume", BRUTE),
    ("rhodonite", BRUTE),
    ("rubis-etoile", BRUTE),
    ("saphir-etoile", BRUTE),
    ("saphir-rose", BRUTE),
    ("sapphirine", BRUTE),
    ("seraphinite", FACETTEE),
    ("serpentine", BRUTE),
    ("sillimanite", BRUTE),
    ("sodalite", BRUTE),
    ("spinelle-rouge", BRUTE),
    ("taaffeite", FACETTEE),
    ("topaze-rose", BRUTE),
    ("tourmaline-bleue", BRUTE),
    ("tourmaline-jaune", BRUTE),
    ("tourmaline-pasteque", BRUTE),
    ("turquoise", BRUTE),
    ("variscite", BRUTE),
    ("zircon-bleu", BRUTE),
    ("zircon-dore", BRUTE),
    ("zircon-hyacinthe", BRUTE),

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
    if image_type == INCLUSION:
        # Les termes d'INCLUSION_GEMS sont déjà des expressions de recherche
        # complètes et ciblées : pas besoin (et pas souhaitable) d'y greffer
        # les suffixes "rough crystal" / "faceted gemstone".
        return [term]
    if image_type == BRUTE:
        return [f"{term} rough crystal", f"{term} raw crystal", f"{term} crystal specimen", term]
    return [f"{term} faceted gemstone", f"{term} gemstone", f"{term} cut gem", term]


def excluded_tokens_for(image_type: str) -> list:
    if image_type == INCLUSION:
        return [tok for tok in EXCLUDED_TITLE_TOKENS if tok not in INCLUSION_ALLOWED_TOKENS]
    return EXCLUDED_TITLE_TOKENS


def pick_image(terms: list, keywords: list, image_type: str, already_used_titles: set) -> Optional[dict]:
    excluded_tokens = excluded_tokens_for(image_type)
    for term in terms:
        for query in build_queries(term, image_type):
            for title in search_candidates(query):
                if title in already_used_titles:
                    continue
                lower_title = title.lower()
                if not lower_title.endswith(ALLOWED_IMAGE_EXTENSIONS):
                    continue
                if any(tok in lower_title for tok in excluded_tokens):
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
                if any(tok in haystack for tok in excluded_tokens):
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


def pick_image_openverse(terms: list, keywords: list, image_type: str, already_used_titles: set) -> Optional[dict]:
    """Source de repli quand Wikimedia Commons n'a rien donné (ou a été
    abandonné via GIVE_UP_SLOTS). Openverse agrège Flickr, des musées, etc.
    en plus de Wikimedia — toute erreur d'API (forme de réponse inattendue,
    réseau, quota) est avalée et traitée comme "rien trouvé", jamais comme un
    échec du run entier."""
    excluded_tokens = excluded_tokens_for(image_type)
    for term in terms:
        for query in build_queries(term, image_type):
            try:
                results = openverse_search(query)
            except Exception as exc:  # noqa: BLE001
                print(f"  [!] erreur Openverse pour « {query} »: {exc}", file=sys.stderr)
                continue
            for item in results:
                try:
                    title = (item.get("title") or "").strip()
                    if not title or title in already_used_titles:
                        continue
                    lower_title = title.lower()
                    if any(tok in lower_title for tok in excluded_tokens):
                        continue
                    if not openverse_license_is_free(item.get("license", "")):
                        continue
                    width = item.get("width") or 0
                    if width and width < 300:
                        continue
                    tag_names = " ".join(
                        t.get("name", "") for t in (item.get("tags") or []) if isinstance(t, dict)
                    )
                    haystack = f"{lower_title} {tag_names.lower()}"
                    if any(tok in haystack for tok in excluded_tokens):
                        continue
                    if not any(kw in haystack for kw in keywords):
                        continue
                    if image_type == FACETTEE and any(tok in haystack for tok in ROUGH_STATE_TOKENS):
                        continue
                    if image_type == BRUTE and any(tok in haystack for tok in CUT_STATE_TOKENS):
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


def safe_resource_name(gem_id: str, image_type: str) -> str:
    name = re.sub(r"[^a-z0-9_]", "_", gem_id.lower())
    if image_type == INCLUSION:
        suffix = "inclusion"
    elif image_type == BRUTE:
        suffix = "brute"
    else:
        suffix = "facette"
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
        "enum class GemImageType { BRUTE, FACETTEE, INCLUSION }",
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

            resource_name = safe_resource_name(gem_id, image_type)
            dest = DRAWABLE_DIR / f"{resource_name}.jpg"
            already_used = {c["title"] for c in gem_credits.values()}
            result = None

            if (gem_id, image_type) in GIVE_UP_SLOTS:
                # Wikimedia ET Openverse ont déjà été essayés (et exclus)
                # pour ce créneau : on ne retente plus automatiquement à
                # chaque run (sinon chaque modif du script relance une
                # recherche complète sur tous les créneaux abandonnés).
                # Une nouvelle tentative doit être déclenchée à la main en
                # retirant l'entrée de GIVE_UP_SLOTS après vérification
                # qu'une meilleure source existe.
                print(f"-> {gem_id} [{image_type}]: recherche abandonnée (voir GIVE_UP_SLOTS)")
                status[image_type] = "⏸️ recherche suspendue"
                continue
            else:
                print(f"-> {gem_id} [{image_type}]: recherche « {' / '.join(terms)} »")
                try:
                    result = pick_image(terms, keywords, image_type, already_used)
                except Exception as exc:  # noqa: BLE001
                    print(f"  [!] erreur recherche pour {gem_id} [{image_type}]: {exc}", file=sys.stderr)
                    result = None

                if not result:
                    print(f"  [i] rien sur Wikimedia pour {gem_id} [{image_type}], tentative Openverse")
                    try:
                        result = pick_image_openverse(terms, keywords, image_type, already_used)
                    except Exception as exc:  # noqa: BLE001
                        print(f"  [!] erreur Openverse pour {gem_id} [{image_type}]: {exc}", file=sys.stderr)
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

    inclusion_report_lines = [
        "",
        "## Photos d'inclusions",
        "",
        "Photos ciblées sur l'inclusion caractéristique de la pierre (jardin, soie, "
        "chiastolite, insecte dans l'ambre...), distinctes des photos de la pierre "
        "entière ci-dessus.",
        "",
        "| Gemme | Inclusion |",
        "|---|---|",
    ]
    inclusion_found = 0

    for gem_id, terms, keywords in INCLUSION_GEMS:
        gem_credits = {c["type"]: c for c in credits.get(gem_id, [])}
        existing = gem_credits.get(INCLUSION)
        status_line = None

        if existing:
            existing_dest = DRAWABLE_DIR / f"{existing['resource_name']}.jpg"
            if existing_dest.exists():
                status_line = f"♻️ [{existing['title']}]({existing['source_url']})"
                inclusion_found += 1

        if status_line is None:
            resource_name = safe_resource_name(gem_id, INCLUSION)
            dest = DRAWABLE_DIR / f"{resource_name}.jpg"
            already_used = {c["title"] for c in gem_credits.values()}
            result = None

            if (gem_id, INCLUSION) in GIVE_UP_INCLUSION_SLOTS:
                print(f"-> {gem_id} [INCLUSION]: recherche abandonnée (voir GIVE_UP_INCLUSION_SLOTS)")
                status_line = "⏸️ recherche suspendue"
            else:
                print(f"-> {gem_id} [INCLUSION]: recherche « {' / '.join(terms)} »")
                try:
                    result = pick_image(terms, keywords, INCLUSION, already_used)
                except Exception as exc:  # noqa: BLE001
                    print(f"  [!] erreur recherche pour {gem_id} [INCLUSION]: {exc}", file=sys.stderr)
                    result = None

                if not result:
                    print(f"  [i] rien sur Wikimedia pour {gem_id} [INCLUSION], tentative Openverse")
                    try:
                        result = pick_image_openverse(terms, keywords, INCLUSION, already_used)
                    except Exception as exc:  # noqa: BLE001
                        print(f"  [!] erreur Openverse pour {gem_id} [INCLUSION]: {exc}", file=sys.stderr)
                        result = None

                if not result:
                    print(f"  [x] aucune image libre trouvée pour {gem_id} [INCLUSION]")
                    status_line = "❌ non trouvée"
                else:
                    try:
                        download_image(result["download_url"], dest)
                    except Exception as exc:  # noqa: BLE001
                        print(f"  [!] échec téléchargement pour {gem_id} [INCLUSION]: {exc}", file=sys.stderr)
                        status_line = "❌ échec téléchargement"
                    else:
                        print(f"  [ok] {result['title']} ({result['license']}, {result['artist']})")
                        gem_credits[INCLUSION] = {
                            "type": INCLUSION,
                            "resource_name": resource_name,
                            "title": result["title"],
                            "artist": result["artist"],
                            "license": result["license"],
                            "source_url": result["source_url"],
                        }
                        status_line = f"✅ [{result['title']}]({result['source_url']})"
                        inclusion_found += 1

        credits[gem_id] = list(gem_credits.values())
        inclusion_report_lines.append(f"| {gem_id} | {status_line} |")

    save_credits(credits)
    write_kotlin(credits)

    found = sum(1 for clist in credits.values() for c in clist if c["type"] != INCLUSION)
    report_lines.append("")
    report_lines.append(f"**{found} / {total_slots}** photos récupérées au total.")
    report_lines.extend(inclusion_report_lines)
    report_lines.append("")
    report_lines.append(f"**{inclusion_found} / {len(INCLUSION_GEMS)}** photos d'inclusions récupérées.")
    REPORT_OUT.write_text("\n".join(report_lines) + "\n", encoding="utf-8")

    print(f"\n{found}/{total_slots} photos récupérées.")


if __name__ == "__main__":
    main()
