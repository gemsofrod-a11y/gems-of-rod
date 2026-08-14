package fr.gemsofrod.encyclopedie.data

import androidx.compose.runtime.mutableIntStateOf

/**
 * Nombre d'indices déjà révélés dans l'énigme légendaire, conservé au
 * niveau du processus plutôt que de la composition — même principe que
 * [QuickAnalysisState]/[GuidedAnalysisState] — pour ne pas perdre les
 * indices déjà révélés en allant consulter la carte puis en revenant.
 * Commence à 1 : le premier indice est visible dès l'entrée sur l'écran.
 */
object LegendaryRiddleState {
    val cluesRevealed = mutableIntStateOf(1)
}
