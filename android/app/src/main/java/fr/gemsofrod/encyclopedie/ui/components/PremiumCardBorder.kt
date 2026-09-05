package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Fin liseré dégradé or -> grenat (les deux teintes "pierre précieuse" de la
 * palette de marque, voir Theme.kt) encadrant les cartes de catalogue
 * (gemmes, météorites, fossiles, coquillages). Un seul point de définition
 * pour que ces quatre sections restent visuellement cohérentes.
 */
@Composable
fun Modifier.premiumCardBorder(shape: Shape = RoundedCornerShape(14.dp)): Modifier {
    val brush = Brush.linearGradient(
        listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
    )
    return this.border(BorderStroke(1.2.dp, brush), shape)
}
