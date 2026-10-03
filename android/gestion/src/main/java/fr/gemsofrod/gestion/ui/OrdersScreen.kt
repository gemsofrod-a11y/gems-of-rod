package fr.gemsofrod.gestion.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.OrderLine
import fr.gemsofrod.gestion.data.OrderStatus
import fr.gemsofrod.gestion.data.newId
import java.time.LocalDate

private enum class OrderFilter(val label: String) {
    EN_COURS("En cours"), DEVIS("Devis"), LIVREES("Livrées"), TOUTES("Toutes");

    fun matches(o: Order) = when (this) {
        EN_COURS -> o.isOpen
        DEVIS -> o.status == OrderStatus.DEVIS
        LIVREES -> o.status == OrderStatus.LIVREE
        TOUTES -> true
    }
}

@Composable
fun OrdersScreen(data: AppData, onOpen: (String) -> Unit) {
    var filter by rememberSaveable { mutableStateOf(OrderFilter.TOUTES) }
    val list = data.orders.filter { filter.matches(it) }.sortedWith(compareByDescending<Order> { it.date }.thenByDescending { it.number })

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            ChoiceChips(null, OrderFilter.entries, filter, { it.label }) { filter = it }
        }
        if (list.isEmpty()) {
            EmptyState(if (data.orders.isEmpty()) "Aucune commande.\nCréez-en une avec le bouton +." else "Aucune commande ici.")
        } else {
            LazyColumn(contentPadding = ListPadding) {
                items(list, key = { it.id }) { o ->
                    ListRow(
                        title = "N° ${o.number} · ${o.clientName.ifBlank { "Sans client" }}",
                        subtitle = "${date(o.localDate)} · ${o.lines.joinToString(", ") { it.label }.ifBlank { "Aucun article" }}",
                        trailing = euros(o.total),
                        onClick = { onOpen(o.id) },
                        badge = { Pill(o.status.label, strong = o.isOpen) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

/** Ligne en cours de saisie (valeurs texte, validées à l'enregistrement). */
private data class LineDraft(
    val key: String = newId(),
    val productId: String?,
    val label: String,
    val quantity: String,
    val price: String,
    val cost: Double,
)

@Composable
fun OrderEditor(
    order: Order?,
    data: AppData,
    onSave: (Order) -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val number = order?.number ?: data.nextOrderNumber
    var clientName by remember { mutableStateOf(order?.clientName ?: "") }
    var clientId by remember { mutableStateOf(order?.clientId) }
    var day by remember { mutableStateOf(order?.localDate ?: LocalDate.now()) }
    var status by remember { mutableStateOf(order?.status ?: OrderStatus.DEVIS) }
    var deposit by remember { mutableStateOf(order?.deposit?.let(::editable) ?: "") }
    var note by remember { mutableStateOf(order?.note ?: "") }
    val lines = remember {
        mutableStateListOf<LineDraft>().apply {
            order?.lines?.forEach {
                add(LineDraft(productId = it.productId, label = it.label, quantity = editable(it.quantity), price = editable(it.unitPrice), cost = it.unitCost))
            }
        }
    }
    var clientMenu by remember { mutableStateOf(false) }
    var productMenu by remember { mutableStateOf(false) }

    val parsed = lines.map { d ->
        val q = parseNumber(d.quantity)
        val p = if (d.price.isBlank()) 0.0 else parseNumber(d.price)
        if (q == null || q <= 0 || p == null || d.label.isBlank()) null
        else OrderLine(d.productId, d.label.trim(), q, p, d.cost)
    }
    val depositValue = if (deposit.isBlank()) 0.0 else parseNumber(deposit)
    val total = parsed.filterNotNull().sumOf { it.total }
    val valid = parsed.none { it == null } && depositValue != null

    EditorScaffold(
        title = if (order == null) "Nouvelle commande" else "Commande n° $number",
        onBack = onBack,
        onSave = if (valid) {
            {
                onSave(
                    Order(
                        id = order?.id ?: newId(), number = number, clientId = clientId,
                        clientName = clientName.trim(), date = day.toEpochDay(), status = status,
                        lines = parsed.filterNotNull(), deposit = depositValue!!, note = note.trim(),
                    ),
                )
            }
        } else null,
        onDelete = order?.let { o -> { onDelete(o.id) } },
        deleteMessage = "Supprimer cette commande ? Les quantités réservées reviennent en stock.",
    ) {
        // Client : choix dans la liste ou saisie libre.
        Box {
            OutlinedTextField(
                value = clientName,
                onValueChange = { text ->
                    clientName = text
                    clientId = data.clients.find { it.name.equals(text.trim(), ignoreCase = true) }?.id
                },
                label = { Text("Client") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (data.clients.isNotEmpty()) {
                        IconButton(onClick = { clientMenu = true }) { Icon(Icons.Outlined.ArrowDropDown, "Choisir un client") }
                    }
                },
                supportingText = if (clientName.isNotBlank() && clientId == null) {
                    { Text("Nouveau nom : ajoutez-le dans Clients pour suivre ses achats.") }
                } else null,
            )
            DropdownMenu(expanded = clientMenu, onDismissRequest = { clientMenu = false }) {
                data.clients.sortedBy { it.name.lowercase() }.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c.name) },
                        onClick = { clientName = c.name; clientId = c.id; clientMenu = false },
                    )
                }
            }
        }

        OutlinedButton(onClick = {
            DatePickerDialog(context, { _, y, m, d -> day = LocalDate.of(y, m + 1, d) }, day.year, day.monthValue - 1, day.dayOfMonth).show()
        }) {
            Icon(Icons.Outlined.CalendarToday, null)
            Text("  ${date(day)}")
        }

        ChoiceChips("Statut", OrderStatus.entries, status, { it.label }) { status = it }
        Text(
            if (status.reservesStock) "Les quantités sont sorties du stock." else "Le stock n'est pas touché tant que la commande n'est pas confirmée.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text("Articles", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        lines.forEachIndexed { index, line ->
            val product = line.productId?.let { id -> data.products.find { it.id == id } }
            // Disponible pour cette commande = stock actuel + ce qu'elle réservait déjà.
            val alreadyReserved = order?.takeIf { it.status.reservesStock }
                ?.lines?.filter { it.productId == line.productId }?.sumOf { it.quantity } ?: 0.0
            val available = product?.let { it.quantity + alreadyReserved }
            val wanted = parseNumber(line.quantity) ?: 0.0
            LineCard(
                line = line,
                unitLabel = product?.unit?.label,
                warning = if (status.reservesStock && available != null && wanted > available)
                    "Stock disponible : ${qty(available)} ${product?.unit?.label ?: ""}" else null,
                onChange = { lines[index] = it },
                onRemove = { lines.removeAt(index) },
            )
        }
        Box {
            TextButton(onClick = { productMenu = true }) {
                Icon(Icons.Outlined.Add, null)
                Text(" Ajouter un article")
            }
            DropdownMenu(expanded = productMenu, onDismissRequest = { productMenu = false }) {
                data.products.sortedBy { it.name.lowercase() }.forEach { p ->
                    DropdownMenuItem(
                        text = { Text("${p.name}  ·  ${qty(p.quantity, p.unit)}") },
                        onClick = {
                            lines.add(LineDraft(productId = p.id, label = p.name, quantity = "1", price = editable(p.price), cost = p.cost))
                            productMenu = false
                        },
                    )
                }
                DropdownMenuItem(
                    text = { Text("Article libre (prestation, taille…)") },
                    onClick = {
                        lines.add(LineDraft(productId = null, label = "", quantity = "1", price = "", cost = 0.0))
                        productMenu = false
                    },
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(Modifier.fillMaxWidth()) {
            Text("Total", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Text(euros(total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Field("Acompte reçu", deposit, { deposit = it }, Modifier.weight(1f), KeyboardType.Decimal, "€", isError = depositValue == null)
            OutlinedButton(onClick = { deposit = editable(Math.round(total * 30) / 100.0) }) { Text("30 %") }
        }
        if (depositValue != null && total > 0) {
            Text(
                "Reste à encaisser : ${euros((total - depositValue).coerceAtLeast(0.0))}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Field("Notes (gravure, délai, livraison…)", note, { note = it }, singleLine = false)
    }
}

@Composable
private fun LineCard(
    line: LineDraft,
    unitLabel: String?,
    warning: String?,
    onChange: (LineDraft) -> Unit,
    onRemove: () -> Unit,
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (line.productId != null) {
                    Text(line.label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                } else {
                    OutlinedTextField(
                        value = line.label,
                        onValueChange = { onChange(line.copy(label = it)) },
                        label = { Text("Désignation") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).padding(top = 8.dp),
                    )
                }
                IconButton(onClick = onRemove) { Icon(Icons.Outlined.Close, "Retirer") }
            }
            Row(Modifier.padding(end = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Field(
                    "Quantité", line.quantity, { onChange(line.copy(quantity = it)) }, Modifier.weight(1f),
                    KeyboardType.Decimal, unitLabel, isError = (parseNumber(line.quantity) ?: 0.0) <= 0,
                )
                Field(
                    "Prix unitaire", line.price, { onChange(line.copy(price = it)) }, Modifier.weight(1f),
                    KeyboardType.Decimal, "€", isError = line.price.isNotBlank() && parseNumber(line.price) == null,
                )
            }
            if (warning != null) {
                Text(warning, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
