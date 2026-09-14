package fr.gemsofrod.encyclopedie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

/**
 * Variante de [DropdownField] pour un champ toujours obligatoire (espèce,
 * forme, sertissage, métal du compositeur de bague) : pas de ligne
 * "Indifférent" injectée — [DropdownField] l'ajoute systématiquement,
 * ce qui n'aurait pas de sens ici puisqu'une valeur est toujours requise.
 * Même habillage visuel (champ en lecture seule + menu déroulant).
 */
@Composable
fun <T> RequiredDropdownField(
    label: String,
    selectedLabel: String,
    options: List<Pair<T, String>>,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(label) },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { (value, text) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onSelect(value)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Rangée de nuances dérivées de [baseColor] (la couleur par défaut de
 * l'espèce sélectionnée), du plus sombre au plus clair — un choix de
 * teinte simple plutôt qu'un sélecteur HSV complet, pour rester léger
 * dans un formulaire déjà riche en champs.
 */
@Composable
fun ColorSwatchRow(baseColor: Color, selected: Color, onSelect: (Color) -> Unit) {
    val shades = remember(baseColor) {
        listOf(-0.35f, -0.18f, 0f, 0.15f, 0.3f, 0.5f).map { amount -> shadeOf(baseColor, amount) }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        shades.forEach { swatch ->
            val isSelected = swatch == selected
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(swatch)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .clickable { onSelect(swatch) }
            )
        }
    }
}

/** amount < 0 assombrit vers le noir, amount > 0 éclaircit vers le blanc. */
private fun shadeOf(base: Color, amount: Float): Color =
    if (amount >= 0f) lerp(base, Color.White, amount) else lerp(base, Color.Black, -amount)
