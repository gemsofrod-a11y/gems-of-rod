package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Category
import fr.gemsofrod.gestion.data.Product
import fr.gemsofrod.gestion.data.StockUnit
import fr.gemsofrod.gestion.data.newId

private fun Category.icon(): ImageVector = when (this) {
    Category.PRECIEUSE -> Icons.Outlined.Diamond
    Category.FINE -> Icons.Outlined.Spa
    Category.METAL -> Icons.Outlined.Savings
    Category.BIJOU -> Icons.Outlined.Workspaces
    Category.AUTRE -> Icons.Outlined.Inventory2
}

private fun Category.tint(): Color = when (this) {
    Category.PRECIEUSE -> Palette.Accent
    Category.FINE -> Color(0xFF0EA5A4)
    Category.METAL -> Palette.Orange
    Category.BIJOU -> Color(0xFFDB2777)
    Category.AUTRE -> Palette.Muted
}

/** Champ de recherche arrondi, sur fond blanc. */
@Composable
fun SearchField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("Rechercher") },
        leadingIcon = { Icon(Icons.Outlined.Search, null, tint = Palette.Muted) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Palette.Card,
            unfocusedContainerColor = Palette.Card,
            unfocusedBorderColor = Color.Transparent,
        ),
    )
}

@Composable
fun StockScreen(data: AppData, onOpen: (String) -> Unit, onNew: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf<Category?>(null) }
    val list = data.products
        .filter { filter == null || it.category == filter }
        .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) }
        .sortedBy { it.name.lowercase() }
    val valueAtCost = data.products.sumOf { it.quantity.coerceAtLeast(0.0) * it.cost }
    val lowCount = data.products.count { it.isLow }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = ListPadding, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            PageHeader("Stock", "Pierres, métaux et bijoux disponibles.", "Produit", onNew)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MiniStat("Valeur d'achat", eurosRound(valueAtCost), Icons.Outlined.Inventory2, Palette.Accent, Modifier.weight(1f))
                MiniStat(
                    "Alertes", if (lowCount == 0) "Aucune" else lowCount.toString(), Icons.Outlined.WarningAmber,
                    if (lowCount == 0) Palette.Green else Palette.Orange, Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(12.dp))
            SearchField(query) { query = it }
            Spacer(Modifier.height(10.dp))
            SegmentedTabs(listOf<Category?>(null) + Category.entries, filter, { it?.label ?: "Tout" }, { filter = it })
            Spacer(Modifier.height(4.dp))
        }
        if (list.isEmpty()) {
            item { EmptyState(if (data.products.isEmpty()) "Aucun produit en stock.\nAjoutez-en avec « Produit »." else "Aucun résultat.") }
        }
        items(list, key = { it.id }) { p ->
            ItemCard(
                title = p.name,
                subtitle = "${p.category.label} · ${euros(p.price)}${if (p.unit == StockUnit.PIECE) "" else " / ${p.unit.label}"}",
                onClick = { onOpen(p.id) },
                leading = { IconBadge(p.category.icon(), p.category.tint(), 42.dp) },
                trailing = qty(p.quantity, p.unit),
                trailingColor = if (p.quantity <= 0) Palette.Red else Palette.Ink,
                chip = if (p.isLow) {
                    { StatusChip(if (p.quantity <= 0) "Rupture" else "Stock bas", if (p.quantity <= 0) ChipKind.BAD else ChipKind.WARN) }
                } else null,
            )
        }
    }
}

@Composable
fun MiniStat(label: String, value: String, icon: ImageVector, tint: Color, modifier: Modifier = Modifier) {
    SoftCard(modifier, padding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon, tint, 34.dp)
            Column(Modifier.padding(start = 10.dp)) {
                Text(label, fontSize = 12.sp, color = Palette.Muted)
                Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Palette.Ink, maxLines = 1)
            }
        }
    }
}

@Composable
fun ProductEditor(
    product: Product?,
    onSave: (Product) -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit,
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var category by remember { mutableStateOf(product?.category ?: Category.FINE) }
    var unit by remember { mutableStateOf(product?.unit ?: StockUnit.PIECE) }
    var quantity by remember { mutableStateOf(product?.quantity?.let(::editable) ?: "") }
    var cost by remember { mutableStateOf(product?.cost?.let(::editable) ?: "") }
    var price by remember { mutableStateOf(product?.price?.let(::editable) ?: "") }
    var threshold by remember { mutableStateOf(product?.threshold?.let(::editable) ?: "1") }
    var note by remember { mutableStateOf(product?.note ?: "") }

    val qtyValue = if (quantity.isBlank()) 0.0 else parseNumber(quantity)
    val costValue = if (cost.isBlank()) 0.0 else parseNumber(cost)
    val priceValue = if (price.isBlank()) 0.0 else parseNumber(price)
    val thresholdValue = if (threshold.isBlank()) 0.0 else parseNumber(threshold)
    val valid = name.isNotBlank() && qtyValue != null && costValue != null && priceValue != null && thresholdValue != null

    EditorScaffold(
        title = if (product == null) "Nouveau produit" else "Produit",
        onBack = onBack,
        onSave = if (valid) {
            {
                onSave(
                    Product(
                        id = product?.id ?: newId(),
                        name = name.trim(), category = category, unit = unit,
                        quantity = qtyValue!!, cost = costValue!!, price = priceValue!!,
                        threshold = thresholdValue!!, note = note.trim(),
                    ),
                )
            }
        } else null,
        onDelete = product?.let { p -> { onDelete(p.id) } },
        deleteMessage = "Supprimer « ${product?.name} » du stock ? Les commandes passées le conservent en clair.",
    ) {
        Field("Désignation", name, { name = it })
        ChoiceChips("Catégorie", Category.entries, category, { it.label }) { category = it }
        ChoiceChips("Unité", StockUnit.entries, unit, { it.label }) { unit = it }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Field("Quantité en stock", quantity, { quantity = it }, Modifier.weight(1f), KeyboardType.Decimal, unit.label, isError = qtyValue == null)
            if (product != null) {
                // Ajustement rapide (réception, casse, inventaire).
                OutlinedButton(onClick = { quantity = editable((qtyValue ?: 0.0) - 1).ifBlank { "0" } }) { Text("−1") }
                OutlinedButton(onClick = { quantity = editable((qtyValue ?: 0.0) + 1) }) { Text("+1") }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Field("Prix d'achat", cost, { cost = it }, Modifier.weight(1f), KeyboardType.Decimal, "€", isError = costValue == null)
            Field("Prix de vente", price, { price = it }, Modifier.weight(1f), KeyboardType.Decimal, "€", isError = priceValue == null)
        }
        if (costValue != null && priceValue != null && priceValue > 0) {
            val margin = priceValue - costValue
            SoftCard(Modifier.fillMaxWidth(), padding = 14.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Marge", fontSize = 14.sp, color = Palette.Muted, modifier = Modifier.weight(1f))
                    Text(euros(margin), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (margin >= 0) Palette.Green else Palette.Red)
                            StatusChip("${(margin / priceValue * 100).toInt()} %", if (margin >= 0) ChipKind.GOOD else ChipKind.BAD, Modifier.padding(start = 8.dp))
                }
            }
        }
        Field("Seuil d'alerte stock bas", threshold, { threshold = it }, keyboard = KeyboardType.Decimal, suffix = unit.label, isError = thresholdValue == null)
        Field("Notes (origine, certificat, traitement…)", note, { note = it }, singleLine = false)
    }
}

