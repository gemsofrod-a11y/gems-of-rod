package fr.gemsofrod.gestion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.gestion.ui.ClientEditor
import fr.gemsofrod.gestion.ui.ClientsScreen
import fr.gemsofrod.gestion.ui.ConfirmDialog
import fr.gemsofrod.gestion.ui.DashboardScreen
import fr.gemsofrod.gestion.ui.GestionTheme
import fr.gemsofrod.gestion.ui.OrderDetail
import fr.gemsofrod.gestion.ui.OrderEditor
import fr.gemsofrod.gestion.ui.OrdersScreen
import fr.gemsofrod.gestion.ui.Palette
import fr.gemsofrod.gestion.ui.ProductEditor
import fr.gemsofrod.gestion.ui.RoundIcon
import fr.gemsofrod.gestion.ui.StockScreen
import fr.gemsofrod.gestion.ui.SumUpScreen
import fr.gemsofrod.gestion.ui.orderRef
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private val viewModel: GestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Thème choisi (Rubis nuit par défaut) ; GestionTheme règle les icônes de la barre d'état.
        fr.gemsofrod.gestion.ui.AppTheme.load(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GestionTheme { GestionApp(viewModel) }
        }
    }
}

enum class Tab(val title: String, val icon: ImageVector) {
    ACCUEIL("Aperçu", Icons.Outlined.GridView),
    COMMANDES("Commandes", Icons.Outlined.Receipt),
    STOCK("Stock", Icons.Outlined.Inventory2),
    CLIENTS("Clients", Icons.Outlined.People),
}

/**
 * Cadre commun des onglets : barre de marque en haut, contenu, barre de
 * navigation flottante marine en bas (pilule violette sur l'onglet actif).
 */
@Composable
fun AppFrame(tab: Tab, onTab: (Tab) -> Unit, menu: @Composable () -> Unit = {}, content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize().background(Palette.Background)) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Palette.AccentGradient),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Outlined.Diamond, null, tint = Palette.OnAccent, modifier = Modifier.size(22.dp)) }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Gems of Rod", fontFamily = fr.gemsofrod.gestion.ui.Display, fontWeight = FontWeight.Medium, fontSize = 20.sp, color = Palette.Ink)
                    Text("GESTION DE LA MAISON", fontSize = 9.sp, letterSpacing = 2.sp, color = Palette.Muted)
                }
                menu()
            }
            Box(Modifier.weight(1f)) { content() }
        }
        FloatingNav(tab, onTab, Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp))
    }
}

@Composable
private fun FloatingNav(tab: Tab, onTab: (Tab) -> Unit, modifier: Modifier) {
    Row(
        modifier.fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(50), spotColor = Palette.Navy.copy(alpha = 0.4f))
            .clip(RoundedCornerShape(50)).background(Palette.Navy).padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Tab.entries.forEach { t ->
            if (t == tab) {
                Row(
                    Modifier.clip(RoundedCornerShape(50)).background(Palette.Accent).padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(t.icon, null, tint = Palette.OnAccent, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(t.title, color = Palette.OnAccent, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            } else {
                Box(
                    Modifier.size(46.dp).clip(CircleShape).clickable { onTab(t) },
                    contentAlignment = Alignment.Center,
                ) { Icon(t.icon, t.title, tint = Palette.NavyMuted, modifier = Modifier.size(22.dp)) }
            }
        }
    }
}

/** Écran ouvert par-dessus les onglets (id null = création). */
private sealed interface Editor {
    data class ProductE(val id: String?) : Editor
    data class ClientE(val id: String?) : Editor
    data class OrderView(val id: String) : Editor
    data class OrderE(val id: String?) : Editor
    data object SumUpE : Editor
    data class InsightE(val insight: fr.gemsofrod.gestion.ui.Insight) : Editor
}

private enum class Pending { IMPORT, SAMPLE, CLEAR }

@Composable
private fun GestionApp(viewModel: GestionViewModel) {
    val data by viewModel.data.collectAsState()
    val sumup by viewModel.sumup.collectAsState()
    var linkBusy by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var tab by rememberSaveable { mutableStateOf(Tab.ACCUEIL) }
    // Pile d'écrans : une commande peut s'ouvrir depuis la fiche d'un client.
    var editors by remember { mutableStateOf(listOf<Editor>()) }
    var menu by remember { mutableStateOf(false) }
    var pending by remember { mutableStateOf<Pending?>(null) }
    var importText by remember { mutableStateOf<String?>(null) }

    fun open(e: Editor) { editors = editors + e }
    fun close() { editors = editors.dropLast(1) }
    fun toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            val ok = runCatching {
                context.contentResolver.openOutputStream(uri)!!.bufferedWriter().use { it.write(viewModel.exportJson()) }
            }.isSuccess
            toast(if (ok) "Sauvegarde enregistrée" else "Échec de l'enregistrement")
        }
    }
    var importReport by remember { mutableStateOf<String?>(null) }
    var themePicker by remember { mutableStateOf(false) }
    val sumupImportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val bytes = runCatching { context.contentResolver.openInputStream(uri)!!.use { it.readBytes() } }.getOrNull()
            importReport = if (bytes == null) "Fichier illisible." else viewModel.importSumUpExport(bytes)
        }
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            importText = runCatching {
                context.contentResolver.openInputStream(uri)!!.bufferedReader().use { it.readText() }
            }.getOrNull()
            if (importText == null) toast("Fichier illisible") else pending = Pending.IMPORT
        }
    }

    // Récupère les nouveaux paiements SumUp à l'ouverture de l'app.
    LaunchedEffect(Unit) { viewModel.syncSumUp() }

    // key : chaque écran ouvert repart de son propre état de saisie.
    key(editors.size) {
        when (val e = editors.lastOrNull()) {
            is Editor.ProductE -> ProductEditor(
                product = e.id?.let { id -> data.products.find { it.id == id } },
                onSave = { viewModel.saveProduct(it); close() },
                onDelete = { viewModel.deleteProduct(it); close() },
                onBack = ::close,
            )
            is Editor.ClientE -> ClientEditor(
                client = e.id?.let { id -> data.clients.find { it.id == id } },
                data = data,
                onSave = { viewModel.saveClient(it); close() },
                onDelete = { viewModel.deleteClient(it); close() },
                onOpenOrder = { open(Editor.OrderView(it)) },
                onBack = ::close,
                onSendCode = { c ->
                    fr.gemsofrod.gestion.ui.sendShippingCode(context, c)
                    viewModel.markShippingCodeSent(c.id)
                },
                onCodeUsed = { used -> e.id?.let { viewModel.setShippingCodeUsed(it, used) } },
            )
            is Editor.OrderView -> {
                val order = data.orders.find { it.id == e.id }
                if (order == null) LaunchedEffect(e) { close() } else OrderDetail(
                    order = order,
                    data = data,
                    onEdit = { open(Editor.OrderE(order.id)) },
                    onMarkPaid = { viewModel.markPaid(order.id) },
                    onStatus = { viewModel.saveOrder(order.copy(status = it)) },
                    onBack = ::close,
                    paymentLinkBusy = linkBusy,
                    onPaymentLink = {
                        linkBusy = true
                        viewModel.createPaymentLink(order.id) { url, error ->
                            linkBusy = false
                            if (url == null) {
                                toast(error ?: "Création du lien impossible.")
                            } else {
                                val text = "Bonjour ${order.clientName.trim()},\n\n" +
                                    "Voici le lien pour régler votre commande ${orderRef(order.number)} " +
                                    "(${fr.gemsofrod.gestion.ui.euros(order.balance)}) en toute sécurité :\n$url\n\n" +
                                    "Avec mes sincères salutations,\nL'équipe Gems of Rod"
                                val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text)
                                context.startActivity(Intent.createChooser(send, "Envoyer le lien de paiement"))
                            }
                        }
                    },
                )
            }
            is Editor.OrderE -> OrderEditor(
                order = e.id?.let { id -> data.orders.find { it.id == id } },
                data = data,
                onSave = { viewModel.saveOrder(it); close() },
                // Supprimée depuis sa fiche : on referme aussi la fiche.
                onDelete = { id ->
                    viewModel.deleteOrder(id)
                    editors = editors.dropLast(1).filterNot { it is Editor.OrderView && it.id == id }
                },
                onBack = ::close,
            )
            is Editor.InsightE -> fr.gemsofrod.gestion.ui.InsightScreen(
                insight = e.insight,
                data = data,
                onOpenOrder = { open(Editor.OrderView(it)) },
                onOpenProduct = { open(Editor.ProductE(it)) },
                onBack = ::close,
            )
            Editor.SumUpE -> SumUpScreen(
                data = data,
                ui = sumup,
                onConnect = { key, code -> viewModel.connectSumUp(key, code) },
                onDisconnect = { viewModel.disconnectSumUp() },
                onSync = { viewModel.syncSumUp() },
                onLink = { code, orderId -> viewModel.linkPayment(code, orderId) },
                onBack = ::close,
                onMapItem = { name, productId -> viewModel.mapSumUpItem(name, productId) },
                onDismissAttempt = { code -> viewModel.dismissAttempt(code) },
                onRelaunchAttempt = { a ->
                    viewModel.createAttemptLink(a.code) { url, error ->
                        if (url == null) {
                            toast(error ?: "Création du lien impossible.")
                        } else {
                            val text = "Bonjour,\n\n" +
                                "Votre paiement de ${fr.gemsofrod.gestion.ui.euros(a.amount)} sur notre boutique n'a pas pu aboutir" +
                                (if (a.summary.isNotBlank()) " (${a.summary})" else "") + ". " +
                                "Si vous le souhaitez, voici un nouveau lien pour finaliser votre commande en toute sécurité :\n$url\n\n" +
                                "Avec mes sincères salutations,\nL'équipe Gems of Rod"
                            val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, text)
                            context.startActivity(Intent.createChooser(send, "Relancer le client"))
                        }
                    }
                },
            )
            null -> AppFrame(
                tab = tab,
                onTab = { tab = it },
                menu = {
                    Box {
                        RoundIcon(Icons.Outlined.MoreVert, "Menu") { menu = true }
                        DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                            DropdownMenuItem(text = { Text("Thème") }, onClick = { menu = false; themePicker = true })
                            DropdownMenuItem(text = { Text("SumUp") }, onClick = { menu = false; open(Editor.SumUpE) })
                            DropdownMenuItem(text = { Text("Importer un export SumUp (Excel)") }, onClick = {
                                menu = false
                                sumupImportLauncher.launch(
                                    arrayOf(
                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                        "application/vnd.ms-excel", "application/octet-stream", "*/*",
                                    ),
                                )
                            })
                            DropdownMenuItem(text = { Text("Exporter une sauvegarde") }, onClick = {
                                menu = false
                                exportLauncher.launch("gems-of-rod-gestion-${LocalDate.now()}.json")
                            })
                            DropdownMenuItem(text = { Text("Importer une sauvegarde") }, onClick = {
                                menu = false
                                importLauncher.launch(arrayOf("application/json", "application/octet-stream", "text/plain"))
                            })
                            DropdownMenuItem(text = { Text("Charger l'exemple") }, onClick = { menu = false; pending = Pending.SAMPLE })
                            DropdownMenuItem(text = { Text("Tout effacer") }, onClick = { menu = false; pending = Pending.CLEAR })
                        }
                    }
                },
            ) {
                when (tab) {
                    Tab.ACCUEIL -> DashboardScreen(
                        data = data,
                        onOpenOrder = { open(Editor.OrderView(it)) },
                        onOpenProduct = { open(Editor.ProductE(it)) },
                        onNewOrder = { open(Editor.OrderE(null)) },
                        onNewProduct = { open(Editor.ProductE(null)) },
                        onLoadSample = { viewModel.loadSample() },
                        onSeeOrders = { tab = Tab.COMMANDES },
                        onOpenInsight = { open(Editor.InsightE(it)) },
                        onSendCode = { c ->
                            fr.gemsofrod.gestion.ui.sendShippingCode(context, c)
                            viewModel.markShippingCodeSent(c.id)
                        },
                        onOpenSumUp = { open(Editor.SumUpE) },
                    )
                    Tab.COMMANDES -> OrdersScreen(data, onOpen = { open(Editor.OrderView(it)) }, onNew = { open(Editor.OrderE(null)) })
                    Tab.STOCK -> StockScreen(data, onOpen = { open(Editor.ProductE(it)) }, onNew = { open(Editor.ProductE(null)) })
                    Tab.CLIENTS -> ClientsScreen(data, onOpen = { open(Editor.ClientE(it)) }, onNew = { open(Editor.ClientE(null)) })
                }
            }
        }
    }

    if (themePicker) {
        fr.gemsofrod.gestion.ui.ThemePicker(
            onPick = { fr.gemsofrod.gestion.ui.AppTheme.choose(context, it) },
            onDismiss = { themePicker = false },
        )
    }

    importReport?.let { report ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { importReport = null },
            containerColor = Palette.Card,
            title = { Text("Import SumUp") },
            text = { Text(report) },
            confirmButton = { androidx.compose.material3.TextButton(onClick = { importReport = null }) { Text("OK") } },
        )
    }

    when (pending) {
        Pending.IMPORT -> ConfirmDialog(
            title = "Importer",
            message = "Remplacer toutes les données actuelles par celles de la sauvegarde ?",
            confirmLabel = "Remplacer",
            onConfirm = {
                pending = null
                toast(if (viewModel.importJson(importText.orEmpty())) "Sauvegarde importée" else "Ce fichier n'est pas une sauvegarde valide")
                importText = null
            },
            onDismiss = { pending = null; importText = null },
        )
        Pending.SAMPLE -> ConfirmDialog(
            title = "Exemple",
            message = "Remplacer les données actuelles par un jeu d'exemple fictif ?",
            confirmLabel = "Charger",
            onConfirm = { pending = null; viewModel.loadSample() },
            onDismiss = { pending = null },
        )
        Pending.CLEAR -> ConfirmDialog(
            title = "Tout effacer",
            message = "Supprimer définitivement produits, clients et commandes ? Pensez à exporter une sauvegarde avant.",
            confirmLabel = "Effacer",
            onConfirm = { pending = null; viewModel.clearAll() },
            onDismiss = { pending = null },
        )
        null -> Unit
    }
}
