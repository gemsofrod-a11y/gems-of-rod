package fr.gemsofrod.gestion.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.SumUpUi
import fr.gemsofrod.gestion.data.AppData
import fr.gemsofrod.gestion.data.SumUpPayment
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class PayFilter(val label: String) { A_ASSOCIER("Ventes directes"), TOUS("Tous") }

/**
 * Connexion SumUp (clé API), synchronisation des paiements et rattachement
 * d'un paiement à une commande.
 */
@Composable
fun SumUpScreen(
    data: AppData,
    ui: SumUpUi,
    onConnect: (apiKey: String, merchantCode: String) -> Unit,
    onDisconnect: () -> Unit,
    onSync: () -> Unit,
    onLink: (code: String, orderId: String?) -> Unit,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)
    var filter by rememberSaveable { mutableStateOf(PayFilter.TOUS) }
    var linking by remember { mutableStateOf<SumUpPayment?>(null) }
    val payments = data.sumupPayments.filter { filter == PayFilter.TOUS || it.orderId == null }
    val today = java.time.LocalDate.now()
    val monthTotal = data.sumupPayments.filter { it.localDate.year == today.year && it.localDate.month == today.month }.sumOf { it.amount }

    Column(
        Modifier.fillMaxSize().background(Palette.Background).statusBarsPadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RoundIcon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", onBack)
            Text("SumUp", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Palette.Ink, modifier = Modifier.weight(1f).padding(start = 12.dp))
        }

        if (ui.configured) ConnectedCard(ui, monthTotal, onSync, onDisconnect) else ConnectCard(ui, onConnect)

        if (data.sumupPayments.isNotEmpty()) {
            Text("Paiements SumUp", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Palette.Ink)
            SegmentedTabs(
                PayFilter.entries, filter, { it.label }, { filter = it },
                count = { f -> if (f == PayFilter.A_ASSOCIER) data.sumupPayments.count { it.orderId == null } else null },
            )
            Text(
                "Touchez un paiement pour le rattacher à une commande (elle sera marquée comme payée). " +
                    "Sinon il reste une vente directe, comptée dans le chiffre d'affaires.",
                fontSize = 12.sp, color = Palette.Muted,
            )
            payments.forEach { p ->
                val order = p.orderId?.let { id -> data.orders.find { it.id == id } }
                ItemCard(
                    title = euros(p.amount),
                    subtitle = "${date(p.localDate)} à ${p.time} · ${p.typeLabel}",
                    onClick = { linking = p },
                    leading = { IconBadge(if (p.paymentType == "ECOM") Icons.Outlined.Link else Icons.Outlined.CreditCard, Palette.Accent, 42.dp) },
                    chip = {
                        if (order != null) StatusChip(orderRef(order.number), ChipKind.GOOD)
                        else StatusChip("Vente directe", ChipKind.NEUTRAL)
                    },
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    linking?.let { p ->
        val candidates = data.orders.filter { it.isUnpaid || it.id == p.orderId }.sortedByDescending { it.date }
        AlertDialog(
            onDismissRequest = { linking = null },
            containerColor = Palette.Card,
            title = { Text("${euros(p.amount)} du ${date(p.localDate)}") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Text("Rattacher à une commande :", fontSize = 13.sp, color = Palette.Muted)
                    Spacer(Modifier.height(6.dp))
                    if (candidates.isEmpty()) Text("Aucune commande à encaisser.", fontSize = 14.sp, color = Palette.Muted)
                    candidates.forEach { o ->
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .background(if (o.id == p.orderId) Palette.AccentPale else Color.Transparent)
                                .clickable { onLink(p.code, o.id); linking = null }.padding(vertical = 10.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Avatar(o.clientName.ifBlank { "?" }, 32.dp)
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text("${orderRef(o.number)} · ${o.clientName.ifBlank { "Sans client" }}", fontSize = 14.sp, color = Palette.Ink)
                                Text("Reste dû ${euros(o.balance)}", fontSize = 12.sp, color = Palette.Muted)
                            }
                        }
                        HorizontalDivider(color = Palette.Line)
                    }
                }
            },
            confirmButton = {
                if (p.orderId != null) TextButton(onClick = { onLink(p.code, null); linking = null }) { Text("Vente directe") }
            },
            dismissButton = { TextButton(onClick = { linking = null }) { Text("Fermer") } },
        )
    }
}

@Composable
private fun ConnectCard(ui: SumUpUi, onConnect: (String, String) -> Unit) {
    var key by rememberSaveable { mutableStateOf("") }
    var merchant by rememberSaveable { mutableStateOf("") }
    SoftCard(Modifier.fillMaxWidth()) {
        Text("Connecter votre compte SumUp", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Palette.Ink)
        Spacer(Modifier.height(6.dp))
        Text(
            "1. Sur me.sumup.com, ouvrez Paramètres → Clés API (« API keys »).\n" +
                "2. Créez une clé secrète : elle commence par sup_sk_.\n" +
                "3. Copiez-la ici. Elle reste uniquement sur ce téléphone.",
            fontSize = 13.sp, color = Palette.Muted,
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = key, onValueChange = { key = it.trim() },
            label = { Text("Clé API secrète (sup_sk_…)") },
            singleLine = true, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Palette.Line),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = merchant, onValueChange = { merchant = it.trim().uppercase() },
            label = { Text("Code marchand (facultatif)") },
            supportingText = { Text("Trouvé tout seul si vide. Sinon : app SumUp → Profil.") },
            singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Palette.Line),
        )
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            PillButton("Connecter", null, { if (!ui.busy) onConnect(key, merchant) })
            if (ui.busy) {
                Spacer(Modifier.width(12.dp))
                CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = Palette.Accent)
            }
        }
        StatusMessage(ui)
    }
}

@Composable
private fun ConnectedCard(ui: SumUpUi, monthTotal: Double, onSync: () -> Unit, onDisconnect: () -> Unit) {
    var confirm by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth().clip(CardShape).background(Palette.AccentGradient).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Connecté à SumUp", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text("Code marchand ${ui.merchantCode}", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
            }
            Box(Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFF4ADE80)))
        }
        Spacer(Modifier.height(14.dp))
        Text("Encaissé par SumUp ce mois-ci", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        Text(euros(monthTotal), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Text(
            if (ui.lastSync > 0) "Dernière synchronisation : ${formatSync(ui.lastSync)}" else "Jamais synchronisé",
            color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp,
        )
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            PillButton(if (ui.busy) "Synchronisation…" else "Synchroniser", Icons.Outlined.Sync, { if (!ui.busy) onSync() }, light = true)
            Spacer(Modifier.weight(1f))
            Text(
                "Déconnecter", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp,
                modifier = Modifier.clip(RoundedCornerShape(50)).clickable { confirm = true }.padding(horizontal = 10.dp, vertical = 8.dp),
            )
        }
        if (ui.message != null) {
            Spacer(Modifier.height(10.dp))
            Text(ui.message, color = if (ui.isError) Color(0xFFFFD1D1) else Color.White, fontSize = 13.sp)
        }
    }
    if (confirm) {
        ConfirmDialog(
            title = "Déconnecter SumUp",
            message = "La clé API sera effacée de ce téléphone. Les paiements déjà récupérés restent dans l'app.",
            confirmLabel = "Déconnecter",
            onConfirm = { confirm = false; onDisconnect() },
            onDismiss = { confirm = false },
        )
    }
}

@Composable
private fun StatusMessage(ui: SumUpUi) {
    if (ui.message == null) return
    Spacer(Modifier.height(8.dp))
    Text(ui.message, fontSize = 13.sp, color = if (ui.isError) Palette.Red else Palette.Muted)
}

private fun formatSync(millis: Long): String =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd/MM 'à' HH:mm", Locale.FRANCE))
