package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter

private const val MAX_ZOOM = 5f
private const val DOUBLE_TAP_ZOOM = 2.5f

/**
 * Photo en plein écran sur fond noir, ouverte par un appui sur une photo de
 * l'app : on y zoome (pincer ou double-appui) et on s'y déplace. La version
 * plein écran est redécodée en haute résolution, la vignette affichée dans
 * la fiche n'étant qu'une version réduite.
 */
@Composable
fun FullscreenDrawablePhoto(drawableName: String, contentDescription: String?, onDismiss: () -> Unit) {
    val painter = rememberSampledDrawablePainter(drawableName, 400.dp) ?: return
    FullscreenPhoto(painter, contentDescription, onDismiss)
}

@Composable
fun FullscreenBitmapPhoto(bitmap: ImageBitmap, contentDescription: String?, onDismiss: () -> Unit) {
    val painter = remember(bitmap) { BitmapPainter(bitmap) }
    FullscreenPhoto(painter, contentDescription, onDismiss)
}

@Composable
private fun FullscreenPhoto(painter: Painter, contentDescription: String?, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var viewport by remember { mutableStateOf(IntSize.Zero) }

        // Empêche de faire glisser la photo hors de l'écran : au zoom z, le
        // contenu déborde de (z - 1) × taille / 2 de chaque côté.
        fun clamp(raw: Offset, z: Float): Offset {
            val maxX = (z - 1f) * viewport.width / 2f
            val maxY = (z - 1f) * viewport.height / 2f
            return Offset(raw.x.coerceIn(-maxX, maxX), raw.y.coerceIn(-maxY, maxY))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .onSizeChanged { viewport = it }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { tap ->
                            if (scale > 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                // Zoome sur le point touché plutôt qu'au centre.
                                val center = Offset(viewport.width / 2f, viewport.height / 2f)
                                scale = DOUBLE_TAP_ZOOM
                                offset = clamp((center - tap) * (DOUBLE_TAP_ZOOM - 1f), DOUBLE_TAP_ZOOM)
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, MAX_ZOOM)
                        offset = if (newScale == 1f) Offset.Zero else clamp(offset + pan, newScale)
                        scale = newScale
                    }
                }
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.stock_camera_close_cd), tint = Color.White)
            }
        }
    }
}
