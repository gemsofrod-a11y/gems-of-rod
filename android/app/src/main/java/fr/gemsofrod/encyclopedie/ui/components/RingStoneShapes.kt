package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import fr.gemsofrod.encyclopedie.data.RingCutShape

/**
 * Silhouette schématique de chaque taille de pierre, dans un repère
 * normalisé -1..1 centré sur l'origine (x vers la droite, y vers le bas,
 * comme l'espace de dessin Compose). [drawStoneShape] transforme ensuite
 * ce repère normalisé vers les dimensions réelles de la pierre : un
 * même contour circulaire, par exemple, devient une ronde ou une ovale
 * selon que la mise à l'échelle x/y est identique ou non — inutile donc
 * de dupliquer la géométrie entre les deux.
 */
private fun ellipsePath(): Path = Path().apply {
    addOval(Rect(-1f, -1f, 1f, 1f))
}

private fun roundedRectPath(cornerFraction: Float): Path = Path().apply {
    val c = cornerFraction.coerceIn(0f, 0.9f)
    moveTo(-1f + c, -1f)
    lineTo(1f - c, -1f)
    quadraticBezierTo(1f, -1f, 1f, -1f + c)
    lineTo(1f, 1f - c)
    quadraticBezierTo(1f, 1f, 1f - c, 1f)
    lineTo(-1f + c, 1f)
    quadraticBezierTo(-1f, 1f, -1f, 1f - c)
    lineTo(-1f, -1f + c)
    quadraticBezierTo(-1f, -1f, -1f + c, -1f)
    close()
}

/** Rectangle à coins coupés (taille "à gradins" : asscher, émeraude, radiant). */
private fun stepCutRectPath(cornerCutFraction: Float): Path = Path().apply {
    val c = cornerCutFraction.coerceIn(0.05f, 0.45f)
    moveTo(-1f + c, -1f)
    lineTo(1f - c, -1f)
    lineTo(1f, -1f + c)
    lineTo(1f, 1f - c)
    lineTo(1f - c, 1f)
    lineTo(-1f + c, 1f)
    lineTo(-1f, 1f - c)
    lineTo(-1f, -1f + c)
    close()
}

private fun plainRectPath(): Path = Path().apply {
    addRect(Rect(-1f, -1f, 1f, 1f))
}

private fun marquisePath(): Path = Path().apply {
    moveTo(0f, -1f)
    quadraticBezierTo(1f, -0.15f, 0f, 0f)
    quadraticBezierTo(1f, 0.15f, 0f, 1f)
    quadraticBezierTo(-1f, 0.15f, 0f, 0f)
    quadraticBezierTo(-1f, -0.15f, 0f, -1f)
    close()
}

private fun poirePath(): Path = Path().apply {
    moveTo(0f, -1f)
    cubicTo(0.62f, -0.82f, 1f, -0.1f, 1f, 0.32f)
    cubicTo(1f, 0.74f, 0.5f, 1f, 0f, 1f)
    cubicTo(-0.5f, 1f, -1f, 0.74f, -1f, 0.32f)
    cubicTo(-1f, -0.1f, -0.62f, -0.82f, 0f, -1f)
    close()
}

/** Silhouette allongée et pointue aux deux extrémités — variante étirée de la poire. */
private fun brioletteHalfPath(): Path = Path().apply {
    moveTo(0f, -1f)
    cubicTo(0.42f, -1.05f, 1.05f, -0.28f, 0.72f, 0.42f)
    cubicTo(0.5f, 0.9f, -0.5f, 0.9f, -0.72f, 0.42f)
    cubicTo(-1.05f, -0.28f, -0.42f, -1.05f, 0f, -1f)
    close()
}

private fun coeurPath(): Path = Path().apply {
    moveTo(0f, 0.78f)
    cubicTo(-1f, 0.12f, -1f, -0.68f, 0f, -0.28f)
    cubicTo(1f, -0.68f, 1f, 0.12f, 0f, 0.78f)
    close()
}

private fun kitePath(): Path = Path().apply {
    moveTo(0f, -1f)
    lineTo(0.85f, -0.1f)
    lineTo(0f, 1f)
    lineTo(-0.85f, -0.1f)
    close()
}

private fun trillionPath(): Path = Path().apply {
    moveTo(0f, -1f)
    quadraticBezierTo(1.05f, 0.35f, 1f, 0.75f)
    quadraticBezierTo(0f, 0.55f, -1f, 0.75f)
    quadraticBezierTo(-1.05f, 0.35f, 0f, -1f)
    close()
}

private fun stonePathFor(shape: RingCutShape): Path = when (shape) {
    RingCutShape.RONDE, RingCutShape.OVALE -> ellipsePath()
    RingCutShape.COUSSIN -> roundedRectPath(cornerFraction = 0.42f)
    RingCutShape.PRINCESSE, RingCutShape.BAGUETTE -> plainRectPath()
    RingCutShape.ASSCHER -> stepCutRectPath(cornerCutFraction = 0.16f)
    RingCutShape.EMERAUDE_CUT -> stepCutRectPath(cornerCutFraction = 0.24f)
    RingCutShape.RADIANT -> stepCutRectPath(cornerCutFraction = 0.12f)
    RingCutShape.MARQUISE -> marquisePath()
    RingCutShape.POIRE -> poirePath()
    RingCutShape.BRIOLETTE -> brioletteHalfPath()
    RingCutShape.COEUR -> coeurPath()
    RingCutShape.KITE -> kitePath()
    RingCutShape.TRILLION -> trillionPath()
}

/**
 * Dessine la pierre [shape] dans [rect] (son rectangle englobant sur le
 * canevas, déjà à l'échelle réelle), pivotée de [rotationDeg], remplie
 * de [brush]. Le contour normalisé (-1..1) est mis à l'échelle par la
 * demi-largeur/demi-hauteur du rectangle plutôt que par un facteur
 * unique, pour que largeur et hauteur restent indépendantes (une ronde
 * étirée devient ainsi une ovale sans changer de fonction de dessin).
 */
fun DrawScope.drawStoneShape(shape: RingCutShape, rect: Rect, brush: Brush, rotationDeg: Float) {
    val path = stonePathFor(shape)
    val center = rect.center
    withTransform({
        translate(center.x, center.y)
        rotate(rotationDeg, pivot = Offset.Zero)
        scale(rect.width / 2f, rect.height / 2f, pivot = Offset.Zero)
    }) {
        drawPath(path, brush)
    }
}
