package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Choix du thème de couleurs (menu ⋮ → Thème). Le thème s'applique dès
 * qu'on le touche, pour le voir aussitôt derrière la fenêtre, et reste
 * mémorisé au prochain lancement.
 */
@Composable
fun ThemePicker(onPick: (AppTheme) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Palette.Card,
        title = { Text("Thème", fontFamily = Display) },
        text = {
            Column {
                AppTheme.entries.forEach { theme ->
                    val selected = theme == AppTheme.current
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                            .background(if (selected) Palette.AccentPale else Color.Transparent)
                            .clickable { onPick(theme) }.padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Swatch(theme)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(theme.label, fontSize = 15.sp, color = Palette.Ink, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                            Text(if (theme.dark) "Sombre" else "Clair", fontSize = 11.sp, color = Palette.Muted)
                        }
                        if (selected) Icon(Icons.Outlined.Check, null, tint = Palette.Accent)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Fermer") } },
    )
}

/** Aperçu du thème : fond, carte et pastille dorée. */
@Composable
private fun Swatch(theme: AppTheme) {
    Box(
        Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(theme.background)
            .border(1.dp, theme.line, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(theme.card), contentAlignment = Alignment.Center) {
            Box(Modifier.size(12.dp).clip(CircleShape).background(theme.accent))
        }
    }
}
