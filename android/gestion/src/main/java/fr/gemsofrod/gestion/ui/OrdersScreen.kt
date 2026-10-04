package fr.gemsofrod.gestion.ui

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Order
import fr.gemsofrod.gestion.data.OrderLine
import fr.gemsofrod.gestion.data.OrderStatus
import fr.gemsofrod.gestion.data.newId
import java.time.LocalDate

/** « Dans 5 j », « Retard 3 j », « Payée »… pour une commande. */
fun dueLabel(o: Order, today: Long): String = when {
    o.status == OrderStatus.DEVIS -> "Devis du ${date(o.localDate)}"
    o.status == OrderStatus.ANNULEE -> "Annulée"
    o.isPaid -> o.paidDate?.let { "Payée le ${date(LocalDate.ofEpochDay(it))}" } ?: "Payée"
    o.dueDate == null -> "Sans échéance"
    else -> when (val d = o.dueDate!! - today) {
        0L -> "Échéance aujourd'hui"
        in Long.MIN_VALUE..-1L -> "Retard ${-d} j"
        else -> "Dans $d j"
    }
}

/** Pastille d'état de paiement d'une commande. */
@Composable
fun OrderChip(o: Order, today: Long) {
    when {
        o.status == OrderStatus.DEVIS -> StatusChip("Devis", ChipKind.NEUTRAL)
        o.status == OrderStatus.ANNULEE -> StatusChip("Annulée", ChipKind.NEUTRAL)
        o.isOverdue(today) -> StatusChip("En retard", ChipKind.BAD)
        o.isPaid -> StatusChip("Payée", ChipKind.GOOD)
        o.deposit > 0 -> StatusChip("Acompte", ChipKind.WARN)
        else -> StatusChip("À payer", ChipKind.ACCENT)
    }
}

private enum class OrderFilter(val label: String) {
    TOUTES("Toutes"), IMPAYEES("Impayées"), RETARD("En retard"), DEVIS("Devis"), PAYEES("Payées");

    fun matches(o: Order, today: Long) = when (this) {
        TOUTES -> true
        IMPAYEES -> o.isUnpaid
        RETARD -> o.isOverdue(today)
        DEVIS -> o.status == OrderStatus.DEVIS
        PAYEES -> o.isPaid && o.status.countsAsSale
    }
}

@Composable
fun OrdersScreen(data: AppData, onOpen: (String) -> Unit, onNew: () -> Unit) {
    val today = LocalDate.now().toEpochDay()
    var filter by rememberSaveable { mutableStateOf(OrderFilter.TOUTES) }
    val list = data.orders.filter { filter.matches(it, today) }
        .sortedWith(compareByDescending<Order> { it.date }.thenByDescending { it.number })

    LazyColumn(Modifier.fillMaxSize(), contentPadding = ListPadding, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            PageHeader("Commandes", "Suivez vos commandes et vos paiements.", "Nouvelle", onNew)
            Spacer(Modifier.height(10.dp))
            SegmentedTabs(
                OrderFilter.entries, filter, { it.label }, { filter = it },
                count = { f -> if (f == OrderFilter.TOUTES || f == OrderFilter.PAYEES) null else data.orders.count { f.matches(it, today) } },
            )
            Spacer(Modifier.height(4.dp))
        }
        if (list.isEmpty()) {
            item { EmptyState(if (data.orders.isEmpty()) "Aucune commande.\nCréez-en une avec « Nouvelle »." else "Aucune commande ici.") }
        }
        items(list, key = { it.id }) { o ->
            ItemCard(
                title = "${orderRef(o.number)} · ${o.clientName.ifBlank { "Sans client" }}",
                subtitle = dueLabel(o, today),
                onClick = { onOpen(o.id) },
                leading = { Avatar(o.clientName.ifBlank { "?" }, 42.dp) },
                trailing = euros(o.total),
                chip = { OrderChip(o, today) },
            )
        }
    }
}

/** Fiche commande en lecture, façon « facture » violette de la maquette. */
@Composable
fun OrderDetail(
    order: Order,
    data: AppData,
    onEdit: () -> Unit,
    onMarkPaid: () -> Unit,
    onStatus: (OrderStatus) -> Unit,
    onBack: () -> Unit,
    /** Crée et partage un lien de paiement SumUp (null = bouton masqué). */
    onPaymentLink: (() -> Unit)? = null,
    paymentLinkBusy: Boolean = false,
) {
    BackHandler(onBack = onBack)
    val today = LocalDate.now().toEpochDay()
    val client = order.clientId?.let { id -> data.clients.find { it.id == id } }
    Column(
        Modifier.fillMaxSize().background(Palette.Background).statusBarsPadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RoundIcon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", onBack)
            Text("Commande", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Palette.Ink, modifier = Modifier.weight(1f).padding(start = 12.dp))
            RoundIcon(Icons.Outlined.Edit, "Modifier", onEdit)
        }

        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Palette.PanelGradient).padding(20.dp)) {
            Text("Détails de la commande", fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(orderRef(order.number), fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(1f))
                OrderChip(order, today)
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(order.clientName.ifBlank { "?" }, 44.dp, ring = true)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(order.clientName.ifBlank { "Sans client" }, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    Text(client?.segment?.label ?: "Client", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(date(order.localDate), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(dueLabel(order, today), color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            // Articles en tuiles translucides, deux par ligne.
            order.lines.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                    pair.forEach { l ->
                        Column(
                            Modifier.weight(1f).clip(RoundedCornerShape(18.dp)).background(Color.White.copy(alpha = 0.16f)).padding(14.dp),
                        ) {
                            Text(euros(l.total), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                            Spacer(Modifier.height(6.dp))
                            Text(l.label, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Text("${qty(l.quantity)} × ${euros(l.unitPrice)}", color = Color.White.copy(alpha = 0.65f), fontSize = 11.sp, maxLines = 1)
                        }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            if (order.lines.isEmpty()) {
                Text("Aucun article", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp, modifier = Modifier.padding(bottom = 10.dp))
            }
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color.White.copy(alpha = 0.12f)).padding(14.dp)) {
                Row {
                    Total("Total", euros(order.total), Modifier.weight(1f))
                    Total("Reçu", euros(order.deposit), Modifier.weight(1f))
                    Total("Reste dû", euros(order.balance), Modifier.weight(1f))
                }
                if (order.isUnpaid) {
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (onPaymentLink != null) {
                            Text(
                                if (paymentLinkBusy) "Création…" else "Lien SumUp",
                                color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                                modifier = Modifier.clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.18f))
                                    .clickable(enabled = !paymentLinkBusy, onClick = onPaymentLink)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                            )
                        }
                        PillButton("Encaisser le solde", null, onMarkPaid, light = true)
                    }
                    if (order.sumupCheckoutId != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Lien SumUp envoyé : la commande sera encaissée automatiquement dès que le client aura payé (à la prochaine synchronisation).",
                            color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp,
                        )
                    }
                }
            }
        }

        SoftCard(Modifier.fillMaxWidth()) {
            Text("Statut", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Palette.Ink)
            Spacer(Modifier.height(8.dp))
            ChoiceChips(null, OrderStatus.entries, order.status, { it.label }, onStatus)
            Text(
                if (order.status.reservesStock) "Les quantités sont sorties du stock." else "Le stock n'est pas touché.",
                fontSize = 12.sp, color = Palette.Muted,
            )
        }
        if (order.note.isNotBlank()) {
            SoftCard(Modifier.fillMaxWidth()) {
                Text("Notes", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Palette.Ink)
                Spacer(Modifier.height(6.dp))
                Text(order.note, fontSize = 14.sp, color = Palette.Ink)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Total(label: String, value: String, modifier: Modifier) {
    Column(modifier) {
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
    }
}

@Composable
fun RoundIcon(icon: ImageVector, description: String, onClick: () -> Unit) {
    Box(
        Modifier.size(44.dp).clip(CircleShape).background(Palette.Card).clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { Icon(icon, description, tint = Palette.Ink) }
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
    var due by remember { mutableStateOf(order?.dueDate?.let { LocalDate.ofEpochDay(it) } ?: (order?.localDate ?: LocalDate.now()).plusDays(15)) }
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

    fun pickDate(initial: LocalDate, onPick: (LocalDate) -> Unit) {
        DatePickerDialog(context, { _, y, m, d -> onPick(LocalDate.of(y, m + 1, d)) }, initial.year, initial.monthValue - 1, initial.dayOfMonth).show()
    }

    EditorScaffold(
        title = if (order == null) "Nouvelle commande" else orderRef(number),
        onBack = onBack,
        onSave = if (valid) {
            {
                onSave(
                    Order(
                        id = order?.id ?: newId(), number = number, clientId = clientId,
                        clientName = clientName.trim(), date = day.toEpochDay(), status = status,
                        lines = parsed.filterNotNull(), deposit = depositValue!!, note = note.trim(),
                        dueDate = due.toEpochDay(), paidDate = order?.paidDate,
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
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Palette.Card,
                    unfocusedContainerColor = Palette.Card,
                    unfocusedBorderColor = Palette.Line,
                ),
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
                        leadingIcon = { Avatar(c.name, 28.dp) },
                        onClick = { clientName = c.name; clientId = c.id; clientMenu = false },
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DateButton("Commande", day, Icons.Outlined.CalendarToday, Modifier.weight(1f)) {
                pickDate(day) { picked ->
                    // L'échéance suit la date de commande si elle n'a pas été déplacée.
                    if (due == day.plusDays(15)) due = picked.plusDays(15)
                    day = picked
                }
            }
            DateButton("Échéance", due, Icons.Outlined.Event, Modifier.weight(1f)) { pickDate(due) { due = it } }
        }

        ChoiceChips("Statut", OrderStatus.entries, status, { it.label }) { status = it }
        Text(
            if (status.reservesStock) "Les quantités sont sorties du stock." else "Le stock n'est pas touché tant que la commande n'est pas confirmée.",
            fontSize = 12.sp, color = Palette.Muted,
        )

        Text("Articles", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Palette.Ink)
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

        SoftCard(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Total", fontSize = 15.sp, color = Palette.Muted, modifier = Modifier.weight(1f))
                Text(euros(total), style = AmountStyle)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Field("Montant reçu", deposit, { deposit = it }, Modifier.weight(1f), KeyboardType.Decimal, "€", isError = depositValue == null)
                OutlinedButton(onClick = { deposit = editable(Math.round(total * 30) / 100.0) }) { Text("30 %") }
                OutlinedButton(onClick = { deposit = editable(total) }) { Text("Tout") }
            }
            if (depositValue != null && total > 0) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Reste à encaisser : ${euros((total - depositValue).coerceAtLeast(0.0))}",
                    fontSize = 13.sp, color = Palette.Muted,
                )
            }
        }
        Field("Notes (gravure, délai, livraison…)", note, { note = it }, singleLine = false)
    }
}

@Composable
private fun DateButton(label: String, value: LocalDate, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    SoftCard(modifier, onClick = onClick, padding = 14.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(label, fontSize = 11.sp, color = Palette.Muted)
                Text(date(value), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Palette.Ink)
            }
            Icon(icon, null, tint = Palette.Accent, modifier = Modifier.size(20.dp))
        }
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
    SoftCard(Modifier.fillMaxWidth(), padding = 12.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (line.productId != null) {
                Text(line.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Palette.Ink, modifier = Modifier.weight(1f))
            } else {
                Field("Désignation", line.label, { onChange(line.copy(label = it)) }, Modifier.weight(1f))
            }
            IconButton(onClick = onRemove) { Icon(Icons.Outlined.Close, "Retirer", tint = Palette.Muted) }
        }
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
            Text(warning, fontSize = 12.sp, color = Palette.Red, modifier = Modifier.padding(top = 6.dp))
        }
    }
}
