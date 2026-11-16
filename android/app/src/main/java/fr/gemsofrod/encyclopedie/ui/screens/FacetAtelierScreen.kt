package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FacetDepth
import fr.gemsofrod.encyclopedie.data.FacetDesign
import fr.gemsofrod.encyclopedie.data.FacetDesignCodec
import fr.gemsofrod.encyclopedie.data.FacetGeometry
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetRole
import fr.gemsofrod.encyclopedie.data.FacetSide
import fr.gemsofrod.encyclopedie.data.FacetTier
import fr.gemsofrod.encyclopedie.data.FacetWheels
import fr.gemsofrod.encyclopedie.ui.components.FacetDiagram
import fr.gemsofrod.encyclopedie.ui.components.FacetStone3DView
import java.util.Locale

/**
 * Atelier de facettage (demande de Sébastien du 16/11/2026, étape 1) : on
 * dessine une taille passe par passe comme sur une machine à facetter
 * (angle, index de la roue, symétrie), avec saisie manuelle de ses propres
 * angles, et on voit la pierre en 3D, ses proportions et son poids. La taille
 * en cours est gardée sur le téléphone (préférences privées).
 */

private const val PREFS = "facet_atelier"
private const val KEY_DESIGN = "design"

private fun parseNumber(text: String): Double? = text.trim().replace(',', '.').toDoubleOrNull()

private fun num(v: Double, decimals: Int = 2): String = String.format(Locale.getDefault(), "%.${decimals}f", v)

private fun loadDesign(context: Context): FacetDesign =
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_DESIGN, null)
        ?.let { FacetDesignCodec.decode(it) } ?: FacetPresets.brilliantRound

private fun saveDesign(context: Context, design: FacetDesign) {
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        .putString(KEY_DESIGN, FacetDesignCodec.encode(design)).apply()
}

@Composable
private fun materialLabel(m: FacetMaterial): String = stringResource(
    when (m) {
        FacetMaterial.DIAMANT -> R.string.facet_mat_diamant
        FacetMaterial.MOISSANITE -> R.string.facet_mat_moissanite
        FacetMaterial.ZIRCONE_CUBIQUE -> R.string.facet_mat_zircone_cubique
        FacetMaterial.ZIRCON -> R.string.facet_mat_zircon
        FacetMaterial.GRENAT -> R.string.facet_mat_grenat
        FacetMaterial.CORINDON -> R.string.facet_mat_corindon
        FacetMaterial.CHRYSOBERYL -> R.string.facet_mat_chrysoberyl
        FacetMaterial.SPINELLE -> R.string.facet_mat_spinelle
        FacetMaterial.TANZANITE -> R.string.facet_mat_tanzanite
        FacetMaterial.PERIDOT -> R.string.facet_mat_peridot
        FacetMaterial.TOURMALINE -> R.string.facet_mat_tourmaline
        FacetMaterial.TOPAZE -> R.string.facet_mat_topaze
        FacetMaterial.BERYL -> R.string.facet_mat_beryl
        FacetMaterial.QUARTZ -> R.string.facet_mat_quartz
    }
)

@Composable
private fun roleLabel(role: FacetRole): String? = when (role) {
    FacetRole.NONE -> null
    FacetRole.PAVILION_MAIN, FacetRole.CROWN_MAIN -> stringResource(R.string.facet_role_main)
    FacetRole.PAVILION_HALF -> stringResource(R.string.facet_role_pavilion_half)
    FacetRole.GIRDLE -> stringResource(R.string.facet_role_girdle)
    FacetRole.STAR -> stringResource(R.string.facet_role_star)
    FacetRole.UPPER_HALF -> stringResource(R.string.facet_role_upper_half)
    FacetRole.TABLE -> stringResource(R.string.facet_role_table)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacetAtelierScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var design by remember { mutableStateOf(loadDesign(context)) }
    val undo = remember { mutableStateListOf<FacetDesign>() }
    val redo = remember { mutableStateListOf<FacetDesign>() }

    fun update(next: FacetDesign) {
        if (next == design) return
        undo.add(design); if (undo.size > 50) undo.removeAt(0)
        redo.clear()
        design = next
    }
    LaunchedEffect(design) { saveDesign(context, design) }

    val stone = remember(design) { FacetGeometry.build(design) }
    val metrics = remember(design, stone) { FacetGeometry.metrics(design, stone) }
    val meshJson = remember(stone) { FacetGeometry.meshJson(stone) }

    var tab by remember { mutableIntStateOf(3) }
    var selected by remember { mutableIntStateOf(-1) }
    var editing by remember { mutableStateOf<Int?>(null) } // index de passe, ou -1 pour une nouvelle
    var menuOpen by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf<String?>(null) } // "material", "wheel", "diameter", "table", "girdle", "rename"
    var view3dUnavailable by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(design.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { redo.add(design); design = undo.removeAt(undo.lastIndex) }, enabled = undo.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = stringResource(R.string.facet_undo))
                    }
                    IconButton(onClick = { undo.add(design); design = redo.removeAt(redo.lastIndex) }, enabled = redo.isNotEmpty()) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = stringResource(R.string.facet_redo))
                    }
                    Box {
                        IconButton(onClick = { menuOpen = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.facet_more))
                        }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(text = { Text(stringResource(R.string.facet_menu_rename)) }, onClick = { menuOpen = false; dialog = "rename" })
                            DropdownMenuItem(text = { Text(stringResource(R.string.facet_menu_reset)) }, onClick = {
                                menuOpen = false
                                update(FacetPresets.brilliantRound.copy(material = design.material, diameterMm = design.diameterMm))
                                selected = -1
                            })
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // réglages de la pierre
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(onClick = { dialog = "material" }, label = {
                    Text(stringResource(R.string.facet_chip_material, materialLabel(design.material), num(design.material.ri, 3)))
                })
                AssistChip(onClick = { dialog = "wheel" }, label = { Text(stringResource(R.string.facet_chip_wheel, design.wheel)) })
                AssistChip(onClick = { dialog = "diameter" }, label = { Text(stringResource(R.string.facet_chip_diameter, num(design.diameterMm))) })
                AssistChip(onClick = { dialog = "table" }, label = { Text(stringResource(R.string.facet_chip_table, num(design.tablePercent, 1))) })
                AssistChip(onClick = { dialog = "girdle" }, label = { Text(stringResource(R.string.facet_chip_girdle, num(design.girdlePercent, 1))) })
            }

            // pierre en 3D (ou dessin des facettes si WebGL manque)
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .height(250.dp)
            ) {
                if (!view3dUnavailable) {
                    FacetStone3DView(
                        meshJson = meshJson,
                        material = design.material,
                        modifier = Modifier.fillMaxSize(),
                        onUnavailable = { view3dUnavailable = true }
                    )
                } else {
                    Column(Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FacetDiagram(design, stone, FacetSide.PAVILION, Modifier.size(180.dp))
                            FacetDiagram(design, stone, FacetSide.CROWN, Modifier.size(180.dp))
                        }
                        Text(stringResource(R.string.facet_3d_unavailable), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // avertissements
            val warnings = buildList {
                if (!stone.closed) add(stringResource(R.string.facet_warn_open))
                if (stone.hiddenTiers.isNotEmpty()) {
                    add(stringResource(R.string.facet_warn_hidden, stone.hiddenTiers.sorted().joinToString(", ") { design.tiers[it].name }))
                }
                val pavMain = design.tiers.firstOrNull { it.side == FacetSide.PAVILION && it.depth == FacetDepth.GIRDLE }
                val critical = design.material.criticalAngleDeg
                if (pavMain != null && pavMain.angleDeg < critical) {
                    add(stringResource(R.string.facet_warn_critical, num(pavMain.angleDeg), num(critical, 1)))
                }
            }
            warnings.forEach {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }

            // passes
            val tabs = listOf(
                R.string.facet_tab_pavilion to FacetSide.PAVILION,
                R.string.facet_tab_girdle to FacetSide.GIRDLE,
                R.string.facet_tab_crown to FacetSide.CROWN,
                R.string.facet_tab_all to null
            )
            TabRow(selectedTabIndex = tab, modifier = Modifier.padding(horizontal = 12.dp)) {
                tabs.forEachIndexed { i, (label, _) ->
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(stringResource(label), maxLines = 1) })
                }
            }
            val sideFilter = tabs[tab].second
            Column(Modifier.padding(horizontal = 12.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)) {
                    HeaderCell(stringResource(R.string.facet_col_tier), 1.6f)
                    HeaderCell(stringResource(R.string.facet_col_angle), 0.9f)
                    HeaderCell(stringResource(R.string.facet_col_index), 1.4f)
                    HeaderCell(stringResource(R.string.facet_col_count), 0.5f)
                    Spacer(Modifier.width(28.dp))
                }
                HorizontalDivider()
                design.tiers.forEachIndexed { i, t ->
                    if (sideFilter != null && t.side != sideFilter) return@forEachIndexed
                    val bg = if (i == selected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bg)
                            .clickable { selected = i; editing = i }
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val role = roleLabel(t.role)
                        Text(if (role != null) "${t.name} · $role" else t.name, Modifier.weight(1.6f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(num(t.angleDeg) + "°", Modifier.weight(0.9f), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Text(
                            if (t.side == FacetSide.CROWN && t.angleDeg == 0.0) "—" else t.indices.joinToString("-") { FacetWheels.display(it, design.wheel) },
                            Modifier.weight(1.4f), maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        Text(t.indices.size.toString(), Modifier.weight(0.5f))
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    HorizontalDivider()
                }
            }

            Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { editing = -1 }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.facet_add_tier), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Button(
                    onClick = { editing = if (selected in design.tiers.indices) selected else -1 },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.facet_manual), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            // proportions
            if (metrics != null) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Metric(stringResource(R.string.facet_sum_table), num(metrics.tablePercent, 1) + " %", Modifier.weight(1f))
                    Metric(stringResource(R.string.facet_sum_crown), num(metrics.crownPercent, 1) + " %", Modifier.weight(1f))
                    Metric(stringResource(R.string.facet_sum_pavilion), num(metrics.pavilionPercent, 1) + " %", Modifier.weight(1f))
                    Metric(stringResource(R.string.facet_sum_weight), num(metrics.carats) + " ct", Modifier.weight(1f))
                }
                Text(
                    stringResource(
                        R.string.facet_info_line,
                        num(design.material.criticalAngleDeg, 1),
                        metrics.facetCount,
                        num(metrics.totalPercent, 1),
                        num(metrics.lengthToWidth)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }

    // ----- dialogues -----
    editing?.let { index ->
        val existing = design.tiers.getOrNull(index)
        val defaultSide = tabs_side(tab)
        TierDialog(
            design = design,
            tierIndex = if (existing != null) index else null,
            initial = existing ?: FacetTier(
                name = FacetPresets.nextName(design, defaultSide),
                side = defaultSide,
                angleDeg = when (defaultSide) { FacetSide.PAVILION -> 41.0; FacetSide.GIRDLE -> 90.0; FacetSide.CROWN -> 35.0 },
                indices = FacetWheels.indicesFor(0, 8, design.wheel, false)
            ),
            onDismiss = { editing = null },
            onDelete = if (existing != null) ({
                update(design.copy(tiers = design.tiers.filterIndexed { i, _ -> i != index }))
                selected = -1; editing = null
            }) else null,
            onConfirm = { tier ->
                val tiers = design.tiers.toMutableList()
                if (existing != null) tiers[index] = tier else tiers.add(tier)
                update(design.copy(tiers = tiers))
                selected = if (existing != null) index else tiers.lastIndex
                editing = null
            }
        )
    }

    when (dialog) {
        "material" -> AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(stringResource(R.string.facet_material_title)) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    FacetMaterial.entries.forEach { m ->
                        Text(
                            stringResource(R.string.facet_material_item, materialLabel(m), num(m.ri, 3), num(m.sg)),
                            fontWeight = if (m == design.material) FontWeight.Bold else FontWeight.Normal,
                            color = if (m == design.material) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { update(design.copy(material = m)); dialog = null }
                                .padding(vertical = 10.dp)
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_close)) } }
        )
        "wheel" -> AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(stringResource(R.string.facet_wheel_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.facet_wheel_hint), style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    WheelChips(design.wheel) { w -> update(FacetWheels.convert(design, w)); dialog = null }
                }
            },
            confirmButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_close)) } }
        )
        "diameter" -> NumberDialog(stringResource(R.string.facet_diameter_title), design.diameterMm, 0.5, 60.0, { dialog = null }) {
            update(design.copy(diameterMm = it))
        }
        "table" -> NumberDialog(stringResource(R.string.facet_table_title), design.tablePercent, 10.0, 95.0, { dialog = null }) {
            update(design.copy(tablePercent = it))
        }
        "girdle" -> NumberDialog(stringResource(R.string.facet_girdle_title), design.girdlePercent, 0.0, 15.0, { dialog = null }) {
            update(design.copy(girdlePercent = it))
        }
        "rename" -> {
            var name by remember { mutableStateOf(design.name) }
            AlertDialog(
                onDismissRequest = { dialog = null },
                title = { Text(stringResource(R.string.facet_rename_title)) },
                text = { OutlinedTextField(value = name, onValueChange = { name = it.take(60) }, singleLine = true) },
                confirmButton = {
                    TextButton(onClick = { if (name.isNotBlank()) update(design.copy(name = name.trim())); dialog = null }) {
                        Text(stringResource(R.string.facet_validate))
                    }
                },
                dismissButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_cancel)) } }
            )
        }
    }
}

private fun tabs_side(tab: Int): FacetSide = when (tab) {
    0 -> FacetSide.PAVILION
    1 -> FacetSide.GIRDLE
    else -> FacetSide.CROWN
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.weight(weight),
        maxLines = 1
    )
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WheelChips(current: Int, onPick: (Int) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FacetWheels.sizes.forEach { w ->
            FilterChip(selected = w == current, onClick = { onPick(w) }, label = { Text(w.toString()) })
        }
    }
}

@Composable
private fun NumberDialog(title: String, value: Double, min: Double, max: Double, onDismiss: () -> Unit, onValue: (Double) -> Unit) {
    var text by remember { mutableStateOf(num(value)) }
    val parsed = parseNumber(text)
    val valid = parsed != null && parsed in min..max
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                isError = !valid,
                supportingText = { Text(stringResource(R.string.facet_range, num(min, 1), num(max, 1))) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        confirmButton = {
            TextButton(onClick = { if (valid) { onValue(parsed!!); onDismiss() } }, enabled = valid) { Text(stringResource(R.string.facet_validate)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.facet_cancel)) } }
    )
}

/**
 * Saisie manuelle d'une passe : angle au centième, index de départ, symétrie
 * (les index se calculent seuls) ou liste d'index libre, règle de profondeur.
 * L'aperçu recalcule la pierre avec la passe en cours de saisie.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TierDialog(
    design: FacetDesign,
    tierIndex: Int?,
    initial: FacetTier,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)?,
    onConfirm: (FacetTier) -> Unit
) {
    val wheel = design.wheel
    val symmetries = remember(wheel) { FacetWheels.symmetriesFor(wheel) }
    // retrouve la symétrie et le départ d'une passe existante, sinon « libre »
    val guess = remember(initial) {
        val sorted = initial.indices.map { it % wheel }.sorted()
        var found: Triple<Int, Int, Boolean>? = null
        loop@ for (mirror in listOf(false, true)) for (n in symmetries) {
            val step = wheel / n
            for (start in 0 until step) {
                if (FacetWheels.indicesFor(start, n, wheel, mirror).map { it % wheel }.sorted() == sorted) {
                    found = Triple(n, start, mirror); break@loop
                }
            }
        }
        found
    }
    var name by remember { mutableStateOf(initial.name) }
    var side by remember { mutableStateOf(initial.side) }
    var angleText by remember { mutableStateOf(num(initial.angleDeg)) }
    var symmetry by remember { mutableStateOf(guess?.first) } // null = libre
    var startText by remember { mutableStateOf(FacetWheels.display(guess?.second ?: initial.indices.firstOrNull() ?: 0, wheel)) }
    var mirror by remember { mutableStateOf(guess?.third ?: false) }
    var indicesText by remember { mutableStateOf(initial.indices.joinToString(" ") { FacetWheels.display(it, wheel) }) }
    var depth by remember { mutableStateOf(initial.depth) }
    var depthText by remember { mutableStateOf(num(initial.depthValue, 1)) }

    fun recompute() {
        val n = symmetry ?: return
        val start = startText.trim().toIntOrNull() ?: return
        indicesText = FacetWheels.indicesFor(start, n, wheel, mirror).joinToString(" ") { FacetWheels.display(it, wheel) }
    }

    val angle = parseNumber(angleText)
    val angleOk = angle != null && angle in 0.0..90.0
    val indices = FacetWheels.parse(indicesText, wheel)
    val indicesOk = indices.isNotEmpty()
    val depthValue = parseNumber(depthText)
    // « jusqu'à la table » n'a de sens que pour la couronne
    val effDepth = if (depth == FacetDepth.TABLE && side != FacetSide.CROWN) FacetDepth.GIRDLE else depth
    val depthOk = effDepth != FacetDepth.VALUE || (depthValue != null && depthValue > 0.0 && depthValue <= 300.0)
    val candidate = if (angleOk && indicesOk && depthOk) FacetTier(
        name = name.trim().ifEmpty { initial.name },
        side = side,
        angleDeg = if (side == FacetSide.GIRDLE) 90.0 else angle!!,
        indices = indices,
        depth = effDepth,
        depthValue = depthValue ?: 100.0,
        role = if (tierIndex != null && side == initial.side) initial.role else FacetRole.NONE
    ) else null

    val previewDesign = remember(candidate, design) {
        candidate?.let { c ->
            val tiers = design.tiers.toMutableList()
            if (tierIndex != null) tiers[tierIndex] = c else tiers.add(c)
            design.copy(tiers = tiers)
        }
    }
    val previewStone = remember(previewDesign) { previewDesign?.let { FacetGeometry.build(it) } }
    val previewMetrics = remember(previewDesign, previewStone) {
        if (previewDesign != null && previewStone != null) FacetGeometry.metrics(previewDesign, previewStone) else null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (tierIndex != null) stringResource(R.string.facet_dialog_edit, initial.name) else stringResource(R.string.facet_dialog_new)
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.facet_dialog_hint), style = MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name, onValueChange = { name = it.take(8) }, singleLine = true,
                        label = { Text(stringResource(R.string.facet_field_name)) }, modifier = Modifier.weight(0.8f)
                    )
                    OutlinedTextField(
                        value = if (side == FacetSide.GIRDLE) "90,00" else angleText,
                        onValueChange = { angleText = it },
                        enabled = side != FacetSide.GIRDLE,
                        singleLine = true,
                        isError = !angleOk,
                        suffix = { Text("°") },
                        label = { Text(stringResource(R.string.facet_field_angle)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1.2f)
                    )
                }
                Text(stringResource(R.string.facet_field_side), style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        FacetSide.PAVILION to R.string.facet_tab_pavilion,
                        FacetSide.GIRDLE to R.string.facet_tab_girdle,
                        FacetSide.CROWN to R.string.facet_tab_crown
                    ).forEach { (s, label) ->
                        FilterChip(selected = side == s, onClick = { side = s }, label = { Text(stringResource(label)) })
                    }
                }
                Text(stringResource(R.string.facet_symmetry), style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    symmetries.forEach { n ->
                        FilterChip(selected = symmetry == n, onClick = { symmetry = n; recompute() }, label = { Text(n.toString()) })
                    }
                    FilterChip(selected = symmetry == null, onClick = { symmetry = null }, label = { Text(stringResource(R.string.facet_symmetry_free)) })
                }
                if (symmetry != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = startText,
                            onValueChange = { startText = it.filter(Char::isDigit).take(3); recompute() },
                            singleLine = true,
                            suffix = { Text("/ $wheel") },
                            label = { Text(stringResource(R.string.facet_field_start)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Switch(checked = mirror, onCheckedChange = { mirror = it; recompute() })
                            Text(stringResource(R.string.facet_mirror), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                OutlinedTextField(
                    value = indicesText,
                    onValueChange = { indicesText = it; symmetry = null },
                    isError = !indicesOk,
                    label = { Text(stringResource(R.string.facet_field_indices)) },
                    supportingText = { Text(stringResource(R.string.facet_indices_count, indices.size)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(stringResource(R.string.facet_depth), style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(selected = effDepth == FacetDepth.GIRDLE, onClick = { depth = FacetDepth.GIRDLE }, label = { Text(stringResource(R.string.facet_depth_girdle)) })
                    if (side == FacetSide.CROWN) {
                        FilterChip(selected = effDepth == FacetDepth.TABLE, onClick = { depth = FacetDepth.TABLE }, label = { Text(stringResource(R.string.facet_depth_table)) })
                    }
                    FilterChip(selected = effDepth == FacetDepth.VALUE, onClick = { depth = FacetDepth.VALUE }, label = { Text(stringResource(R.string.facet_depth_value)) })
                }
                if (effDepth == FacetDepth.VALUE) {
                    OutlinedTextField(
                        value = depthText,
                        onValueChange = { depthText = it },
                        singleLine = true,
                        isError = !depthOk,
                        suffix = { Text("%") },
                        label = { Text(stringResource(R.string.facet_field_depth_value)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (previewDesign != null && previewStone != null) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FacetDiagram(
                            previewDesign, previewStone,
                            if (side == FacetSide.PAVILION) FacetSide.PAVILION else FacetSide.CROWN,
                            Modifier.size(120.dp),
                            highlightTier = tierIndex ?: previewDesign.tiers.lastIndex
                        )
                        Column {
                            Text(stringResource(R.string.facet_preview), style = MaterialTheme.typography.bodySmall)
                            previewMetrics?.let {
                                Text(
                                    stringResource(R.string.facet_preview_metrics, num(it.crownPercent, 1), num(it.pavilionPercent, 1), num(it.carats)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } ?: Text(stringResource(R.string.facet_warn_open), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                if (onDelete != null) {
                    TextButton(onClick = onDelete) { Text(stringResource(R.string.facet_delete), color = MaterialTheme.colorScheme.error) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { candidate?.let(onConfirm) }, enabled = candidate != null) { Text(stringResource(R.string.facet_validate)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.facet_cancel)) } }
    )
}
