@file:OptIn(ExperimentalMaterial3Api::class)

package fr.gemsofrod.gestion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import fr.gemsofrod.gestion.ui.ClientEditor
import fr.gemsofrod.gestion.ui.ClientsScreen
import fr.gemsofrod.gestion.ui.ConfirmDialog
import fr.gemsofrod.gestion.ui.DashboardScreen
import fr.gemsofrod.gestion.ui.GestionTheme
import fr.gemsofrod.gestion.ui.OrderEditor
import fr.gemsofrod.gestion.ui.OrdersScreen
import fr.gemsofrod.gestion.ui.ProductEditor
import fr.gemsofrod.gestion.ui.StockScreen
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private val viewModel: GestionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    GestionApp(viewModel)
                }
            }
        }
    }
}

private enum class Tab(val title: String, val icon: ImageVector) {
    ACCUEIL("Accueil", Icons.Outlined.Diamond),
    COMMANDES("Commandes", Icons.Outlined.Receipt),
    STOCK("Stock", Icons.Outlined.Inventory2),
    CLIENTS("Clients", Icons.Outlined.People),
}

/** Écran d'édition ouvert par-dessus les onglets (id null = création). */
private sealed interface Editor {
    data class ProductE(val id: String?) : Editor
    data class ClientE(val id: String?) : Editor
    data class OrderE(val id: String?) : Editor
}

private enum class Pending { IMPORT, SAMPLE, CLEAR }

@Composable
private fun GestionApp(viewModel: GestionViewModel) {
    val data by viewModel.data.collectAsState()
    val context = LocalContext.current
    var tab by rememberSaveable { mutableStateOf(Tab.ACCUEIL) }
    // Pile d'éditeurs : une commande peut s'ouvrir depuis la fiche d'un client.
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
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            importText = runCatching {
                context.contentResolver.openInputStream(uri)!!.bufferedReader().use { it.readText() }
            }.getOrNull()
            if (importText == null) toast("Fichier illisible") else pending = Pending.IMPORT
        }
    }

    // key : chaque éditeur ouvert repart de son propre état de saisie.
    key(editors.size) { when (val e = editors.lastOrNull()) {
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
            onOpenOrder = { open(Editor.OrderE(it)) },
            onBack = ::close,
        )
        is Editor.OrderE -> OrderEditor(
            order = e.id?.let { id -> data.orders.find { it.id == id } },
            data = data,
            onSave = { viewModel.saveOrder(it); close() },
            onDelete = { viewModel.deleteOrder(it); close() },
            onBack = ::close,
        )
        null -> Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (tab == Tab.ACCUEIL) "Gems of Rod" else tab.title) },
                    actions = {
                        Box {
                            IconButton(onClick = { menu = true }) { Icon(Icons.Outlined.MoreVert, "Menu") }
                            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                    Tab.entries.forEach { t ->
                        NavigationBarItem(
                            selected = tab == t,
                            onClick = { tab = t },
                            icon = { Icon(t.icon, null) },
                            label = { Text(t.title) },
                        )
                    }
                }
            },
            floatingActionButton = {
                if (tab != Tab.ACCUEIL) {
                    FloatingActionButton(
                        onClick = {
                            when (tab) {
                                Tab.COMMANDES -> open(Editor.OrderE(null))
                                Tab.STOCK -> open(Editor.ProductE(null))
                                Tab.CLIENTS -> open(Editor.ClientE(null))
                                Tab.ACCUEIL -> Unit
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) { Icon(Icons.Outlined.Add, "Ajouter") }
                }
            },
        ) { padding ->
            Box(Modifier.padding(padding)) {
                when (tab) {
                    Tab.ACCUEIL -> DashboardScreen(
                        data = data,
                        onOpenOrder = { open(Editor.OrderE(it)) },
                        onOpenProduct = { open(Editor.ProductE(it)) },
                        onNewProduct = { open(Editor.ProductE(null)) },
                        onLoadSample = { viewModel.loadSample() },
                    )
                    Tab.COMMANDES -> OrdersScreen(data) { open(Editor.OrderE(it)) }
                    Tab.STOCK -> StockScreen(data) { open(Editor.ProductE(it)) }
                    Tab.CLIENTS -> ClientsScreen(data) { open(Editor.ClientE(it)) }
                }
            }
        }
    } }

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
