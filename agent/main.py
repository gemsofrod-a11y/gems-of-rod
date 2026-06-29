#!/usr/bin/env python3
"""
Gems of Rod — Agent IA autonome
Usage: python agent/main.py [COMMANDE] [OPTIONS]
"""
import os
import sys
from pathlib import Path

# Ensure repo root is on sys.path
ROOT = Path(__file__).parent.parent
sys.path.insert(0, str(ROOT))

try:
    from dotenv import load_dotenv
    load_dotenv(ROOT / ".env")
except ImportError:
    pass

import click
from rich.console import Console
from rich.table import Table

console = Console()

TASKS = {
    "run": (
        "Lance la session journalière complète : revue du catalogue, actions prioritaires, "
        "récapitulatif final. Commence par lister les produits et fournisseurs, identifie "
        "les tâches du jour (fiches à enrichir, posts Instagram à créer, emails fournisseurs "
        "à composer, articles à proposer), exécute-les, puis génère le récapitulatif journalier."
    ),
    "products": (
        "Concentre-toi uniquement sur la gestion des fiches produits. Liste le catalogue, "
        "identifie les fiches incomplètes ou à enrichir, propose des améliorations et crée "
        "les fiches manquantes. Termine par un récapitulatif des actions produits."
    ),
    "instagram": (
        "Crée des posts Instagram pour les produits Gems of Rod. Pour chaque produit phare, "
        "rédige une légende luxueuse, sélectionne les hashtags pertinents et décris le visuel "
        "souhaité pour Canva. Crée au moins 2 posts. Termine par un récapitulatif."
    ),
    "suppliers": (
        "Gère les communications fournisseurs. Passe en revue les fournisseurs, identifie "
        "les relances nécessaires, compose les emails de demande de prix ou de disponibilité "
        "pour les pierres les plus demandées. Termine par un récapitulatif."
    ),
    "articles": (
        "Propose des articles de blog pour le site Gems of Rod. Rédige au moins un article "
        "complet sur un sujet gemmologique (guides d'achat, origines des pierres, entretien, "
        "histoire d'une gemme). Soumets-le en attente de validation. Termine par un récapitulatif."
    ),
    "newsletter": (
        "Crée la newsletter mensuelle de Gems of Rod. Met en avant les nouvelles acquisitions, "
        "partage un article gemmologique court, propose une offre exclusive si pertinent. "
        "Génère le HTML complet. Termine par un récapitulatif."
    ),
    "summary": (
        "Génère uniquement le récapitulatif de la journée. Liste les actions effectuées, "
        "les éléments en attente, les questions pour l'utilisateur et les prochaines étapes."
    ),
}


@click.group()
def cli():
    """✦ Gems of Rod — Agent IA autonome"""


@cli.command()
@click.argument("commande", type=click.Choice(list(TASKS.keys())), default="run")
def run(commande: str):
    """Lance l'agent avec une commande prédéfinie."""
    _check_api_key()
    from agent.agent import run_agent
    run_agent(TASKS[commande])


@cli.command()
@click.argument("question")
def ask(question: str):
    """Pose une question libre à l'agent."""
    _check_api_key()
    from agent.agent import run_agent
    run_agent(question)


@cli.command()
def drafts():
    """Liste tous les brouillons générés (emails, Instagram, articles, newsletters)."""
    from agent import config

    table = Table(title="Brouillons Gems of Rod", show_header=True, header_style="bold magenta")
    table.add_column("Type", style="cyan")
    table.add_column("ID")
    table.add_column("Fichier")
    table.add_column("Date")

    categories = {
        "Email": config.EMAIL_DIRS["emails"],
        "Instagram": config.EMAIL_DIRS["instagram"],
        "Article": config.EMAIL_DIRS["articles"],
        "Newsletter": config.EMAIL_DIRS["newsletters"],
    }

    total = 0
    for cat, path in categories.items():
        if path.exists():
            for f in sorted(path.glob("*.json")):
                table.add_row(cat, f.stem, f.name, _file_date(f))
                total += 1

    if total == 0:
        console.print("[dim]Aucun brouillon trouvé. Lancez d'abord : python agent/main.py run[/dim]")
    else:
        console.print(table)
        console.print(f"\n[dim]{total} brouillon(s) dans data/drafts/[/dim]")


@cli.command()
def status():
    """Affiche l'état du catalogue et des données."""
    from agent import config

    console.print("\n[bold magenta]✦ Gems of Rod — État du système[/bold magenta]\n")

    prods = config.get_products()
    console.print(f"  Produits catalogue     : [bold]{len(prods)}[/bold]")
    console.print(f"  Fournisseurs           : [bold]{len(config.get_suppliers())}[/bold]")
    console.print(f"  Clients                : [bold]{len(config.get_clients())}[/bold]")

    for label, path in config.EMAIL_DIRS.items():
        count = len(list(path.glob("*.json"))) if path.exists() else 0
        console.print(f"  Brouillons {label:<12}: [bold]{count}[/bold]")

    log_dir = config.LOGS_DIR
    logs = len(list(log_dir.glob("*.json"))) if log_dir.exists() else 0
    console.print(f"  Journaux agent         : [bold]{logs}[/bold]")
    console.print()


@cli.command()
def orchestrate():
    """Affiche les brouillons prêts pour les outils MCP (Gmail, Canva, Drive)."""
    from agent.orchestrator import get_orchestration_status
    import json as _json

    status = get_orchestration_status()
    console.print("\n[bold magenta]✦ Orchestration MCP — Gems of Rod[/bold magenta]\n")
    console.print(f"  Emails à envoyer (Gmail MCP)      : [bold]{status['emails_en_attente']}[/bold]")
    console.print(f"  Posts Instagram (Canva MCP)        : [bold]{status['posts_instagram_en_attente']}[/bold]")
    console.print(f"  Articles approuvés (Drive MCP)     : [bold]{status['articles_approuves']}[/bold]")
    console.print(f"  Newsletters (Gmail MCP)            : [bold]{status['newsletters_en_attente']}[/bold]")
    console.print()

    if status["total"] == 0:
        console.print("[dim]Aucun brouillon en attente. Lancez : python agent/main.py run[/dim]")
    else:
        console.print(f"[bold green]{status['total']} brouillon(s) prêt(s) pour orchestration.[/bold green]")
        console.print("[dim]Claude Code va traiter ces brouillons via les outils MCP.[/dim]")


def _check_api_key():
    if not os.getenv("ANTHROPIC_API_KEY"):
        console.print("[bold red]Erreur : ANTHROPIC_API_KEY manquante.[/bold red]")
        console.print("Renseignez votre clé API dans le fichier .env (ANTHROPIC_API_KEY=sk-ant-...).")
        sys.exit(1)


def _file_date(path: Path) -> str:
    import datetime
    return datetime.datetime.fromtimestamp(path.stat().st_mtime).strftime("%d/%m %H:%M")


if __name__ == "__main__":
    if len(sys.argv) == 1:
        # Default: full daily run
        _check_api_key()
        from agent.agent import run_agent
        run_agent(TASKS["run"])
    else:
        cli()
