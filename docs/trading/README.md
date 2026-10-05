# Risque Clair — calculatrice de position et simulateur de stratégie

Page web autonome (`index.html`, sans dépendance ni serveur), publiée par
GitHub Pages avec le reste de `docs/` :
https://gemsofrod-a11y.github.io/gems-of-rod/trading/

Créée le 05/10/2026 à la demande de Sébastien, après analyse d'un live de
vente de formation au trading (or, « order blocks », IA). Elle garde ce qui
était utile dans ce live — la gestion du risque — et permet de vérifier les
chiffres annoncés. Elle ne prédit rien, ne passe aucun ordre et n'est reliée
à aucun broker.

## Calculatrice

Entrées : capital (€), risque par trade (%), prix d'entrée, stop, objectif,
spread ; réglages du broker (unités par lot, pas de lot, levier, EUR/USD).
Préréglages or (100 onces par lot, levier 1:20) et argent (5 000 onces).

Sorties : taille de lot arrondie **vers le bas** au pas du broker, perte si le
stop est touché (spread compris), gain si l'objectif est atteint, rapport
gain/risque réel, taux de réussite nécessaire pour ne rien perdre, marge
immobilisée, capital après 5 et 10 pertes d'affilée. Alertes : capital trop
petit pour le stop (même le plus petit lot dépasse le risque voulu), risque
au-dessus de 1 % ou 2 %, objectif plus petit que le stop, marge trop forte,
spread qui mange plus de 10 % du risque.

## Simulateur

Entrées : capital, risque par trade (€), taux de réussite, rapport
gain/risque, frais par trade, trades par mois, durée. Tire au hasard 2 000
parcours et affiche le résultat médian, les 10 % pires et meilleurs, le gain
moyen par trade, la part de parcours perdants et de mois perdants, la pire
série de pertes, la plus forte baisse et la part de comptes vidés.
Exemples : « Chiffres du live » (70 % de réussite à 3 fois le risque, sans
frais), « Bon trader » (35 %), « Débutant » (25 %).

## Données

Aucun appel réseau autre que les polices Google Fonts (repli sur les polices
du système). Les valeurs saisies sont gardées dans le navigateur
(`localStorage`, clé `risque-clair-v1`) par commodité ; la page marche sans.
