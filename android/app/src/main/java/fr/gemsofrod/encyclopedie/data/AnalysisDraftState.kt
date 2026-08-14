package fr.gemsofrod.encyclopedie.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

/**
 * État de saisie de l'outil d'analyse rapide, conservé au niveau du
 * processus plutôt que de la composition : Jetpack Navigation recompose
 * l'écran depuis zéro à chaque retour depuis une fiche gemme consultée
 * depuis les résultats, ce qui effaçait les critères déjà saisis avec un
 * simple `remember`. N'est réinitialisé qu'explicitement via [reset].
 */
object QuickAnalysisState {
    val couleur = mutableStateOf<GemColorCategory?>(null)
    val transparence = mutableStateOf<String?>(null)
    val eclat = mutableStateOf<String?>(null)
    val clivage = mutableStateOf<String?>(null)
    val systemeCristallin = mutableStateOf<String?>(null)
    val dureteInput = mutableStateOf("")
    val densiteInput = mutableStateOf("")
    val indiceRefractionInput = mutableStateOf("")
    val pleochroisme = mutableStateOf<String?>(null)
    val fluorescence = mutableStateOf<String?>(null)
    val typeInclusion = mutableStateOf<String?>(null)
    val results = mutableStateOf<List<AnalysisMatch>?>(null)

    fun reset() {
        couleur.value = null
        transparence.value = null
        eclat.value = null
        clivage.value = null
        systemeCristallin.value = null
        dureteInput.value = ""
        densiteInput.value = ""
        indiceRefractionInput.value = ""
        pleochroisme.value = null
        fluorescence.value = null
        typeInclusion.value = null
        results.value = null
    }
}

/** Même principe que [QuickAnalysisState], pour l'assistant d'analyse guidée pas-à-pas. */
object GuidedAnalysisState {
    val stepIndex = mutableIntStateOf(0)
    val couleur = mutableStateOf<GemColorCategory?>(null)
    val transparence = mutableStateOf<String?>(null)
    val eclat = mutableStateOf<String?>(null)
    val clivage = mutableStateOf<String?>(null)
    val typeInclusion = mutableStateOf<String?>(null)
    val dureteInput = mutableStateOf("")
    val densiteInput = mutableStateOf("")
    val pleochroisme = mutableStateOf<String?>(null)
    val indiceRefractionInput = mutableStateOf("")
    val systemeCristallin = mutableStateOf<String?>(null)
    val fluorescence = mutableStateOf<String?>(null)
    val results = mutableStateOf<List<AnalysisMatch>?>(null)

    fun reset() {
        stepIndex.value = 0
        couleur.value = null
        transparence.value = null
        eclat.value = null
        clivage.value = null
        typeInclusion.value = null
        dureteInput.value = ""
        densiteInput.value = ""
        pleochroisme.value = null
        indiceRefractionInput.value = ""
        systemeCristallin.value = null
        fluorescence.value = null
        results.value = null
    }
}
