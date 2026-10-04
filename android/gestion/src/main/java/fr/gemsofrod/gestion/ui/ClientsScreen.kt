package fr.gemsofrod.gestion.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.Client
import fr.gemsofrod.gestion.data.Segment
import fr.gemsofrod.gestion.data.newId
import fr.gemsofrod.gestion.data.REGULAR_MIN_ORDERS
import fr.gemsofrod.gestion.data.VIP_MIN_SPENT
import fr.gemsofrod.gestion.data.spentLastYear
import fr.gemsofrod.gestion.data.ordersLastYear
import fr.gemsofrod.gestion.data.segmentOf
import fr.gemsofrod.gestion.data.spentByClient
import java.time.LocalDate

private fun Segment.chip(): ChipKind = when (this) {
    Segment.VIP -> ChipKind.ACCENT
    Segment.REGULIER -> ChipKind.GOOD
    Segment.OCCASIONNEL, Segment.PROSPECT -> ChipKind.NEUTRAL
}

@Composable
fun ClientsScreen(data: AppData, onOpen: (String) -> Unit, onNew: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf<Segment?>(null) }
    val spent = remember(data) { data.spentByClient() }
    val list = data.clients
        .filter { filter == null || data.segmentOf(it) == filter }
        .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) || it.email.contains(query.trim(), ignoreCase = true) }
        .sortedByDescending { spent[it.id] ?: 0.0 }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = ListPadding, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            PageHeader("Clients", "Vos clients et leurs achats.", "Client", onNew)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MiniStat("Clients", data.clients.size.toString(), Icons.Outlined.Groups, Palette.Accent, Modifier.weight(1f))
                MiniStat("VIP", data.clients.count { data.segmentOf(it) == Segment.VIP }.toString(), Icons.Outlined.Star, Palette.Orange, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            SearchField(query) { query = it }
            Spacer(Modifier.height(10.dp))
            SegmentedTabs(
                listOf(null, Segment.VIP, Segment.REGULIER, Segment.OCCASIONNEL), filter, { it?.label ?: "Tous" }, { filter = it },
                count = { s -> s?.let { seg -> data.clients.count { data.segmentOf(it) == seg } } },
            )
            Spacer(Modifier.height(4.dp))
        }
        if (list.isEmpty()) {
            item { EmptyState(if (data.clients.isEmpty()) "Aucun client.\nAjoutez-en avec « Client »." else "Aucun résultat.") }
        }
        items(list, key = { it.id }) { c ->
            val count = data.orders.count { it.clientId == c.id }
            ItemCard(
                title = c.name,
                subtitle = "$count commande${if (count > 1) "s" else ""}" + (c.email.takeIf { it.isNotBlank() }?.let { " · $it" } ?: ""),
                onClick = { onOpen(c.id) },
                leading = { Avatar(c.name, 42.dp) },
                trailing = spent[c.id]?.let { eurosRound(it) },
                chip = { data.segmentOf(c).let { seg -> StatusChip(seg.label, seg.chip()) } },
            )
        }
    }
}

@Composable
fun ClientEditor(
    client: Client?,
    data: AppData,
    onSave: (Client) -> Unit,
    onDelete: (String) -> Unit,
    onOpenOrder: (String) -> Unit,
    onBack: () -> Unit,
    /** Prépare l'envoi du code de livraison offerte au client. */
    onSendCode: (Client) -> Unit = {},
    onCodeUsed: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    val today = LocalDate.now().toEpochDay()
    var name by remember { mutableStateOf(client?.name ?: "") }
    var email by remember { mutableStateOf(client?.email ?: "") }
    var phone by remember { mutableStateOf(client?.phone ?: "") }
    var note by remember { mutableStateOf(client?.note ?: "") }

    EditorScaffold(
        title = if (client == null) "Nouveau client" else "Client",
        onBack = onBack,
        onSave = if (name.isNotBlank()) {
            {
                onSave(
                    Client(
                        id = client?.id ?: newId(), name = name.trim(), email = email.trim(),
                        phone = phone.trim(), segment = Segment.OCCASIONNEL, note = note.trim(),
                        shippingCode = client?.shippingCode, shippingCodeSentAt = client?.shippingCodeSentAt,
                        shippingCodeUsed = client?.shippingCodeUsed ?: false,
                    ),
                )
            }
        } else null,
        onDelete = client?.let { c -> { onDelete(c.id) } },
        deleteMessage = "Supprimer ce client ? Ses commandes sont conservées.",
    ) {
        Field("Nom", name, { name = it })
        Field("Email", email, { email = it }, keyboard = KeyboardType.Email)
        Field("Téléphone", phone, { phone = it }, keyboard = KeyboardType.Phone)
        if (client != null && (client.phone.isNotBlank() || client.email.isNotBlank())) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (client.phone.isNotBlank()) {
                    PillButton("Appeler", Icons.Outlined.Call, {
                        runCatching { context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${client.phone}"))) }
                    })
                }
                if (client.email.isNotBlank()) {
                    PillButton("Écrire", Icons.Outlined.Email, {
                        runCatching { context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${client.email}"))) }
                    }, light = true)
                }
            }
        }
        if (client != null) {
            val seg = data.segmentOf(client)
            val n = data.ordersLastYear(client)
            val spent = data.spentLastYear(client)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Catégorie : ", fontSize = 14.sp, color = Palette.Muted)
                StatusChip(seg.label, seg.chip())
            }
            Text(
                "Sur 12 mois : ${euros(spent)} d'achats, $n commande${if (n > 1) "s" else ""}. " +
                    "VIP dès ${eurosRound(VIP_MIN_SPENT)} d'achats, Régulier dès $REGULAR_MIN_ORDERS commandes, sinon Occasionnel. " +
                    "Un ancien VIP redescend en Régulier." +
                    (if (seg == fr.gemsofrod.gestion.data.Segment.REGULIER && n < REGULAR_MIN_ORDERS) " (C'est le cas de ce client.)" else ""),
                fontSize = 12.sp, color = Palette.Muted,
            )
        }
        if (client?.shippingCode != null) ShippingCodeCard(client, onSendCode, onCodeUsed)
        Field("Notes (goûts, tailles, occasions…)", note, { note = it }, singleLine = false)

        if (client != null) {
            val orders = data.orders.filter { it.clientId == client.id }.sortedByDescending { it.date }
            if (orders.isNotEmpty()) {
                Text("Commandes", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Palette.Ink)
                orders.forEach { o ->
                    ItemCard(
                        title = "${orderRef(o.number)} · ${date(o.localDate)}",
                        subtitle = o.lines.joinToString(", ") { it.label },
                        onClick = { onOpenOrder(o.id) },
                        leading = { Avatar(o.clientName.ifBlank { "?" }, 36.dp) },
                        trailing = euros(o.total),
                        chip = { OrderChip(o, today) },
                    )
                }
            }
        }
    }
}

/** Carte « Livraison offerte » de la fiche client : code, état, envoi. */
@Composable
private fun ShippingCodeCard(client: Client, onSend: (Client) -> Unit, onUsed: (Boolean) -> Unit) {
    val code = client.shippingCode ?: return
    SoftCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(Icons.Outlined.CardGiftcard, Palette.Accent, 34.dp)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("LIVRAISON OFFERTE", fontSize = 10.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Medium, color = Palette.Muted)
                Text(code, fontFamily = Display, fontSize = 20.sp, color = Palette.Ink)
            }
            StatusChip(
                when {
                    client.shippingCodeUsed -> "Utilisé"
                    client.shippingCodeSentAt != null -> "Envoyé"
                    else -> "À envoyer"
                },
                when {
                    client.shippingCodeUsed -> ChipKind.NEUTRAL
                    client.shippingCodeSentAt != null -> ChipKind.GOOD
                    else -> ChipKind.WARN
                },
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            (client.shippingCodeSentAt?.let { "Message préparé le ${formatDay(it)}. " } ?: "Offert pour sa 3e commande sur 12 mois. ") +
                "Pensez à créer ce code dans votre boutique SumUp, ou à offrir la livraison vous-même à la commande.",
            fontSize = 12.sp, color = Palette.Muted,
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            PillButton(if (client.shippingCodeSentAt == null) "Envoyer le code" else "Renvoyer", Icons.AutoMirrored.Outlined.Send, { onSend(client) })
            Text(
                if (client.shippingCodeUsed) "Marquer non utilisé" else "Marquer utilisé",
                fontSize = 13.sp, color = Palette.Accent, fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onUsed(!client.shippingCodeUsed) }.padding(8.dp),
            )
        }
    }
}

private fun formatDay(millis: Long): String =
    date(java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.systemDefault()).toLocalDate())

/** Message de la maison accompagnant le code (envoi par email, SMS ou WhatsApp). */
fun shippingCodeMessage(client: Client): String {
    val first = client.name.trim().split(Regex("\\s+")).firstOrNull().orEmpty()
    return "Cher $first,\n\n" +
        "Pour vous remercier de votre fidélité, nous avons le plaisir de vous offrir la livraison " +
        "sur votre prochaine commande, avec votre code personnel :\n\n" +
        "${client.shippingCode}\n\n" +
        "Ce sera toujours un plaisir de vous faire découvrir nos nouvelles pierres.\n\n" +
        "Avec mes sincères salutations,\nL'équipe Gems of Rod"
}

/** Ouvre le choix d'application (email, SMS, WhatsApp…) avec le message prêt. */
fun sendShippingCode(context: android.content.Context, client: Client) {
    val send = Intent(Intent.ACTION_SEND).setType("text/plain")
        .putExtra(Intent.EXTRA_SUBJECT, "Gems of Rod — votre livraison offerte")
        .putExtra(Intent.EXTRA_TEXT, shippingCodeMessage(client))
    if (client.email.isNotBlank()) send.putExtra(Intent.EXTRA_EMAIL, arrayOf(client.email))
    runCatching { context.startActivity(Intent.createChooser(send, "Envoyer le code à ${client.name}")) }
}
