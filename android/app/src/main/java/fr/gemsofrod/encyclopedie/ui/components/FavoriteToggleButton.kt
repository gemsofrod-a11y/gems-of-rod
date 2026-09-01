package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import kotlinx.coroutines.launch

/**
 * Bouton cœur d'ajout/retrait des favoris, avec un léger rebond à
 * l'activation (pas au chargement de l'écran — l'animation ne se déclenche
 * que sur l'action de l'utilisateur). Centralisé ici pour un rendu
 * identique sur toutes les fiches gemme (détail, lithothérapie…).
 */
@Composable
fun FavoriteToggleButton(
    gemId: String,
    modifier: Modifier = Modifier,
    activeTint: Color = MaterialTheme.colorScheme.primary,
    inactiveTint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val isFavorite = FavoritesRepository.isFavorite(gemId)
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val addLabel = stringResource(R.string.cd_add_favorite)
    val removeLabel = stringResource(R.string.cd_remove_favorite)

    IconButton(
        onClick = {
            FavoritesRepository.toggle(gemId)
            scope.launch {
                scale.snapTo(1f)
                scale.animateTo(1.35f, animationSpec = tween(100))
                scale.animateTo(
                    1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavorite) removeLabel else addLabel,
            tint = if (isFavorite) activeTint else inactiveTint,
            modifier = Modifier
                .size(24.dp)
                .scale(scale.value)
        )
    }
}
