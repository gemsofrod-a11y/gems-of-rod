package fr.gemsofrod.gestion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Category
import fr.gemsofrod.gestion.data.Product
import fr.gemsofrod.gestion.data.StockUnit

@Composable
fun StockScreen(data: AppData, onOpen: (String) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf<Category?>(null) }
    val list = data.products
        .filter { filter == null || it.category == filter }
        .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) }
        .sortedBy { it.name.lowercase() }

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Rechercher") },
                leadingIcon = { Icon(Icons.Outlined.Search, null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            ChoiceChips(null, listOf<Category?>(null) + Category.entries, filter, { it?.label ?: "Tout" }) { filter = it }
        }
        if (list.isEmpty()) {
            EmptyState(if (data.products.isEmpty()) "Aucun produit en stock.\nAjoutez-en avec le bouton +." else "Aucun résultat.")
        } else {
            LazyColumn(contentPadding = ListPadding) {
                items(list, key = { it.id }) { p ->
                    ListRow(
                        title = p.name,
                        subtitle = "${p.category.label} · ${euros(p.price)}${if (p.unit == StockUnit.PIECE) "" else " / ${p.unit.label}"}",
                        trailing = qty(p.quantity, p.unit),
                        trailingAlert = p.isLow,
                        onClick = { onOpen(p.id) },
                        badge = if (p.isLow) { { Pill(if (p.quantity <= 0) "Rupture" else "Stock bas", strong = true) } } else null,
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
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
                        id = product?.id ?: fr.gemsofrod.gestion.data.newId(),
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
            Text(
                "Marge : ${euros(margin)} (${(margin / priceValue * 100).toInt()} % du prix de vente)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Field("Seuil d'alerte stock bas", threshold, { threshold = it }, keyboard = KeyboardType.Decimal, suffix = unit.label, isError = thresholdValue == null)
        Field("Notes (origine, certificat, traitement…)", note, { note = it }, singleLine = false)
    }
}
