@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package fr.gemsofrod.gestion.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

val CardShape = RoundedCornerShape(24.dp)

/** Carte blanche arrondie à ombre douce teintée de violet. */
@Composable
fun SoftCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    padding: Dp = 18.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .shadow(8.dp, CardShape, ambientColor = Color(0xFF6B5130).copy(alpha = 0.07f), spotColor = Color(0xFF6B5130).copy(alpha = 0.07f))
            .clip(CardShape)
            .background(Palette.Card)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(padding),
        content = content,
    )
}

/** Carte avec titre et, à droite, une petite pastille d'icône. */
@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconTint: Color = Palette.Accent,
    /** Ouvre la page de détail ; une flèche l'indique à droite du titre. */
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    SoftCard(modifier.fillMaxWidth(), onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title.uppercase(), fontSize = 11.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Medium, color = Palette.Muted, modifier = Modifier.weight(1f))
            if (icon != null) IconBadge(icon, iconTint)
            if (onClick != null) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight, "Voir le détail", tint = Palette.AccentSoft,
                    modifier = Modifier.padding(start = 4.dp).size(20.dp),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
fun IconBadge(icon: ImageVector, tint: Color = Palette.Accent, size: Dp = 30.dp) {
    Box(
        Modifier.size(size).clip(RoundedCornerShape(10.dp)).background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, null, tint = tint, modifier = Modifier.size(size * 0.55f)) }
}

/** Tons de pierres : émeraude, saphir, grenat, ambre, améthyste, tourmaline, onyx. */
private val AvatarColors = listOf(
    Color(0xFF3F6B52), Color(0xFF3E5A7E), Color(0xFF7E3B4B), Color(0xFFA9742C),
    Color(0xFF6A4C7E), Color(0xFF2F6E6A), Color(0xFF4A443C),
)

/** Pastille ronde aux initiales, couleur stable par nom. */
@Composable
fun Avatar(name: String, size: Dp = 40.dp, ring: Boolean = false) {
    val initials = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifEmpty { "?" }
    val color = AvatarColors[abs(name.trim().lowercase().hashCode()) % AvatarColors.size]
    Box(
        Modifier.size(size).clip(CircleShape).background(if (ring) Color.White.copy(alpha = 0.25f) else Color.Transparent)
            .padding(if (ring) 2.dp else 0.dp).clip(CircleShape).background(color),
        contentAlignment = Alignment.Center,
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = (size.value * 0.36f).sp)
    }
}

enum class ChipKind(val fg: Color, val bg: Color) {
    NEUTRAL(Palette.Muted, Palette.Background),
    ACCENT(Palette.Accent, Palette.AccentPale),
    GOOD(Palette.Green, Palette.GreenPale),
    WARN(Palette.Orange, Palette.OrangePale),
    BAD(Palette.Red, Palette.RedPale),
}

@Composable
fun StatusChip(text: String, kind: ChipKind = ChipKind.NEUTRAL, modifier: Modifier = Modifier) {
    Text(
        text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = kind.fg,
        maxLines = 1,
        modifier = modifier.clip(RoundedCornerShape(50)).background(kind.bg).padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

/** « ↑ 8 % vs mois dernier » en vert (ou rouge si la hausse est mauvaise). */
@Composable
fun TrendText(value: Double?, suffix: String, unit: String = " %", upIsGood: Boolean = true, fallback: String? = null) {
    if (value == null) {
        if (fallback != null) Text(fallback, fontSize = 12.sp, color = Palette.Muted)
        return
    }
    val up = value >= 0
    val good = up == upIsGood
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "${if (up) "↑" else "↓"} ${abs(value).toInt()}$unit",
            fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
            color = if (good) Palette.Green else Palette.Red,
        )
        Text(" $suffix", fontSize = 12.sp, color = Palette.Muted)
    }
}

/**
 * Onglets en pilule (« Toutes · Devis 3 · Impayées 5 »). [dark] : version
 * blanche posée sur le panneau marine.
 */
@Composable
fun <T> SegmentedTabs(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    count: (T) -> Int? = { null },
    dark: Boolean = false,
) {
    Row(
        modifier.clip(RoundedCornerShape(50)).background(if (dark) Color.White else Palette.Card)
            .horizontalScroll(rememberScrollState()).padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEach { option ->
            val on = option == selected
            Row(
                Modifier.clip(RoundedCornerShape(50)).background(if (on) Palette.Accent else Color.Transparent)
                    .clickable { onSelect(option) }.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    label(option), fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = if (on) Color.White else Palette.Ink,
                )
                val n = count(option)
                if (n != null && n > 0) {
                    Spacer(Modifier.width(6.dp))
                    Box(
                        Modifier.size(18.dp).clip(CircleShape).background(if (on) Color.White else Palette.AccentPale),
                        contentAlignment = Alignment.Center,
                    ) { Text(n.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Palette.Accent) }
                }
            }
        }
    }
}

/** Bouton pilule violet « + Nouvelle commande ». */
@Composable
fun PillButton(text: String, icon: ImageVector?, onClick: () -> Unit, modifier: Modifier = Modifier, light: Boolean = false) {
    Row(
        modifier.clip(RoundedCornerShape(50))
            .then(if (light) Modifier.background(Color.White) else Modifier.background(Palette.AccentGradient))
            .clickable(onClick = onClick).padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val fg = if (light) Palette.Ink else Color.White
        if (icon != null) {
            Icon(icon, null, tint = fg, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, color = fg, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

/** Ligne de liste en carte : visuel à gauche, titre/sous-titre, montant et pastille à droite. */
@Composable
fun ItemCard(
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    trailing: String? = null,
    trailingColor: Color = Palette.Ink,
    chip: (@Composable () -> Unit)? = null,
) {
    SoftCard(Modifier.fillMaxWidth(), onClick = onClick, padding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            leading()
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Palette.Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (subtitle != null) {
                    Text(subtitle, fontSize = 12.sp, color = Palette.Muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                if (trailing != null) Text(trailing, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = trailingColor)
                if (chip != null) {
                    Spacer(Modifier.height(4.dp))
                    chip()
                }
            }
        }
    }
}

/** Écran de saisie plein écran : retour, titre, bouton Enregistrer, suppression optionnelle. */
@Composable
fun EditorScaffold(
    title: String,
    onBack: () -> Unit,
    onSave: (() -> Unit)?,
    onDelete: (() -> Unit)? = null,
    deleteMessage: String = "Supprimer définitivement cet élément ?",
    content: @Composable ColumnScope.() -> Unit,
) {
    BackHandler(onBack = onBack)
    var confirmDelete by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Palette.Background,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour") }
                },
                actions = {
                    if (onDelete != null) {
                        IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Outlined.Delete, "Supprimer", tint = Palette.Red) }
                    }
                    TextButton(onClick = { onSave?.invoke() }, enabled = onSave != null) { Text("Enregistrer") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Palette.Background),
            )
        },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).imePadding().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
    if (confirmDelete && onDelete != null) {
        ConfirmDialog(
            title = "Supprimer",
            message = deleteMessage,
            confirmLabel = "Supprimer",
            onConfirm = { confirmDelete = false; onDelete() },
            onDismiss = { confirmDelete = false },
        )
    }
}

@Composable
fun ConfirmDialog(title: String, message: String, confirmLabel: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Palette.Card,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(confirmLabel) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } },
    )
}

@Composable
fun Field(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboard: KeyboardType = KeyboardType.Text,
    suffix: String? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        isError = isError,
        suffix = if (suffix != null) { { Text(suffix) } } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Palette.Card,
            unfocusedContainerColor = Palette.Card,
            errorContainerColor = Palette.Card,
            unfocusedBorderColor = Palette.Line,
        ),
    )
}

/** Choix unique parmi quelques options, sous forme de puces. */
@Composable
fun <T> ChoiceChips(label: String?, options: List<T>, selected: T, text: (T) -> String, onSelect: (T) -> Unit) {
    Column {
        if (label != null) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = Palette.Muted)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = { onSelect(option) },
                    label = { Text(text(option)) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Palette.Card,
                        selectedContainerColor = Palette.Accent,
                        selectedLabelColor = Color.White,
                    ),
                )
            }
        }
    }
}

@Composable
fun EmptyState(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier.fillMaxWidth().padding(32.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = Palette.Muted,
        textAlign = TextAlign.Center,
    )
}

/** Marge des listes, pour ne pas passer sous la barre de navigation flottante. */
val ListPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 120.dp)
