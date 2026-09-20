"""Grille tarifaire complète de Gems of Rod (taille lapidaire, sertissage,
cours des métaux précieux), fournie par Sébastien le 20/09/2026.

Un seul exemplaire de ce texte, importé à la fois par classifier.py (pour
rédiger un devis indicatif dans les emails entrants) et voice_agent.py (pour
répondre à l'oral) — évite d'avoir deux copies à resynchroniser à chaque
mise à jour de tarifs. Si la grille change, modifier uniquement ce fichier.
"""

TARIFF_REFERENCE = """\
GEMS OF ROD — GRILLE TARIFAIRE (taille de pierres, sertissage, métaux précieux)

Forfaits HT par pièce (taux horaire cible 55-60 €/h). TVA non applicable —
Art. 293B du CGI (auto-entrepreneur). Un acompte de 30 % est demandé à la
commande pour toute prestation supérieure à 100 €.

FACETTAGE — TAILLE CLASSIQUE (rond, ovale, coussin, marquise, poire,
émeraude, trillion...)

Catégorie 1 — Quartz et pierres communes (cristal de roche, améthyste,
citrine, aventurine, agate...) :
  ≤ 1 ct : 35 € | 1-3 ct : 55 € | 3-10 ct : 90 € | > 10 ct : 150 €
  Supplément au-delà du seuil : +8 €/ct

Catégorie 2 — Pierres fines courantes (grenat almandin, rhodolite, topaze
bleue traitée, péridot, tourmaline courante...) :
  ≤ 1 ct : 55 € | 1-3 ct : 85 € | 3-10 ct : 140 € | > 10 ct : 220 €
  Supplément au-delà du seuil : +12 €/ct

Catégorie 3 — Pierres fines nobles (aigue-marine, topaze impériale,
spinelle, grenat tsavorite, grenat démantoïde, tourmaline de couleur...) :
  ≤ 1 ct : 85 € | 1-3 ct : 130 € | 3-10 ct : 200 € | > 10 ct : 320 €
  Supplément au-delà du seuil : +18 €/ct

Catégorie 4 — Pierres précieuses (saphir, rubis, alexandrite...) :
  ≤ 1 ct : 130 € | 1-3 ct : 200 € | 3-10 ct : 320 € | > 10 ct : 500 €
  Supplément au-delà du seuil : +28 €/ct

Catégorie 5 — Émeraude (traitement spécial, fragilité, inclusions
naturelles fréquentes, risque de casse inhérent élevé) :
  ≤ 1 ct : 160 € | 1-3 ct : 250 € | 3-10 ct : 400 € | > 10 ct : sur devis
  Supplément au-delà du seuil : +35 €/ct

TAILLES SPÉCIALES ET FANTAISIE (tarif "CAT" = tarif de la catégorie
ci-dessus correspondant à la pierre et son poids) :
  - Taille fantaisie / sur mesure (cœur, croix, trillion, bouton, formes
    libres...) : tarif CAT + 40 % — devis préalable recommandé (complexité
    accrue).
  - Retaille / recut (amélioration d'une taille existante, correction de
    proportions) : tarif CAT × 60 % — sur évaluation préalable de la pierre.
  - Préforme seulement (dégrossissage sans polissage final) : tarif CAT × 40 %.
  - Réparation et repolissage (polissage partiel, égrisage, réparation
    d'éclats superficiels) : 35 à 80 € selon l'état et la complexité.
  - Double cabochon (dessus et dessous arrondis, forme lentille, olive...) :
    tarif CAT + 25 %.

PRESTATIONS ANNEXES ET OPTIONS :
  - Pierre fournie par le client : inclus dans le tarif CAT. Clause de
    risque de casse signée obligatoire avant intervention.
  - Supplément urgence (délai < 5 jours ouvrés) : +50 % sur le tarif de
    base, sous réserve de disponibilité de l'atelier.
  - Photographie HD de la pierre taillée (fond blanc, haute résolution,
    fichier numérique) : 15 €.
  - Certificat d'authenticité pour une pierre apportée par le client : 25 €
    sur demande.
  - Envoi sécurisé (recommandé + assurance) : selon poids et valeur
    déclarée, suivi Chronopost ou Colissimo recommandé.
  - Devis et expertise de pierre brute : gratuit pour toute commande de
    taille confirmée.

CONDITIONS GÉNÉRALES :
  - Délai standard : 2 à 4 semaines selon la charge d'atelier, confirmé à
    la commande.
  - Les chutes et refus éventuels sont retournés avec la pierre taillée.

CLAUSE DE RISQUE DE CASSE (formulation contractuelle, à reproduire fidèlement,
jamais à paraphraser ou résumer de mémoire) :
"La taille lapidaire implique un risque inhérent de bris ou d'éclat,
notamment sur les pierres très incluses, fragiles ou présentant des
clivages naturels. Gems of Rod met en œuvre tous les soins nécessaires lors
de chaque intervention. Sa responsabilité ne saurait être engagée en cas de
casse liée aux caractéristiques naturelles de la pierre. Le client est
informé de ce risque et l'accepte formellement avant toute intervention,
par signature d'une clause de risque."

SERTISSAGE — bijouterie artisanale sur mesure (tarifs HT, main d'œuvre
uniquement, hors coût de la pierre et de la monture ; un devis personnalisé
est établi pour chaque projet) :
  - Serti griffes (4 ou 6 griffes, pierre ronde ou fantaisie) :
    < 5 mm : 25-40 € | 5-10 mm : 40-70 € | > 10 mm : 70-120 €
  - Serti clos / bezel (tour complet en métal enveloppant la pierre) :
    < 8 mm : 35-60 € | > 8 mm : 60-100 €

COURS DES MÉTAUX PRÉCIEUX (référence indicative relevée le 13/09/2026,
source or.fr / LBMA / agosi.de — fluctue en permanence sur les marchés
internationaux ; le tarif matière définitif est celui en vigueur au jour de
la commande, jamais celui-ci présenté comme une offre ferme) :
  - Or 24 carats / 999‰ (or pur) : 120,66 €/g
  - Or 18 carats / 750‰ : ~90,50 €/g (usage : bijouterie de luxe/joaillerie,
    l'alliage le plus utilisé en France)
  - Or 14 carats / 585‰ : ~70,59 €/g (bijouterie courante, robuste)
  - Or 9 carats / 375‰ : ~45,25 €/g (entrée de gamme, titre légal minimum
    poinçonnable "or" en France)
  - Argent 999‰ (fin) : 1,77 €/g
  - Argent 925‰ (sterling) : ~1,64 €/g (standard bijouterie/orfèvrerie,
    obligatoire pour le poinçon en France)
  - Platine 950‰ : 49,75 €/g (bijouterie de prestige, hypoallergénique)
  - Palladium 999‰ : 36,13 €/g (substitut fréquent au platine pour les
    alliages d'or blanc hypoallergéniques)

Repères utiles pour répondre à une question client sur les poinçons :
aigle = or 18 carats (750‰) ; hibou = or 9 ou 14 carats importé ; tête de
Minerve = argent 925‰ ; lion ailé = platine 950‰. 1 carat d'or = 1/24e de
métal pur (18 carats = 750‰). L'or blanc est rhodié (dépôt de rhodium) pour
sa teinte blanc éclatant, un traitement qui s'use et se renouvelle chez
Gems of Rod.\
"""
