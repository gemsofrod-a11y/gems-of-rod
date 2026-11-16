package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FacetDesign
import fr.gemsofrod.encyclopedie.data.FacetDesignCodec
import fr.gemsofrod.encyclopedie.data.FacetGeometry
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetPresets
import fr.gemsofrod.encyclopedie.data.FacetRole
import fr.gemsofrod.encyclopedie.data.FacetRule
import fr.gemsofrod.encyclopedie.data.FacetSide
import fr.gemsofrod.encyclopedie.data.FacetTier
import fr.gemsofrod.encyclopedie.data.FacetWheels
import fr.gemsofrod.encyclopedie.data.TierIssue
import fr.gemsofrod.encyclopedie.ui.components.FacetDiagram
import fr.gemsofrod.encyclopedie.ui.components.FacetStone3DView
import java.util.Locale
import kotlin.math.abs

/**
 * Atelier de facettage (demandes de Sébastien des 16 et 17/11/2026) : on part
 * d'un cube brut et on le taille passe par passe, comme sur une machine — angle,
 * index sur la roue, symétrie et miroir, puis une règle qui dit jusqu'où couper
 * (au centre, largeur, niveler le rondiste, point de rencontre, table, ou
 * profondeur au curseur). Les instructions s'écrivent toutes seules ; la pierre
 * se voit en 3D, la passe en cours en or. Les tailles sont gardées sur le
 * téléphone (préférences privées).
 */

private const val PREFS = "facet_atelier"
private const val KEY_DESIGNS = "designs_v2"

private val Gold = Color(0xFFD6B872)

/** Or lisible : plus foncé sur fond clair (contraste), or de la marque sur fond sombre. */
@Composable
private fun accent(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) Color(0xFF7E5F1A) else Gold

@Composable
private fun cardColors() = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)

@Composable
private fun chipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Gold.copy(alpha = 0.35f),
    selectedLabelColor = MaterialTheme.colorScheme.onSurface
)

private fun parseNumber(text: String): Double? = text.trim().replace(',', '.').toDoubleOrNull()

private fun num(v: Double, decimals: Int = 2): String = String.format(Locale.getDefault(), "%.${decimals}f", v)

private fun loadDesigns(context: Context): List<FacetDesign> =
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_DESIGNS, null)
        ?.let { FacetDesignCodec.decodeAll(it) }.orEmpty()

private fun saveDesigns(context: Context, list: List<FacetDesign>) {
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        .putString(KEY_DESIGNS, FacetDesignCodec.encodeAll(list)).apply()
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

/** Instruction de taille, écrite à partir de la règle de la passe. */
@Composable
private fun instruction(t: FacetTier): String = when (t.rule) {
    FacetRule.CENTER -> stringResource(R.string.facet_ins_center)
    FacetRule.WIDTH -> if (abs(t.value - 1.0) < 1e-9) stringResource(R.string.facet_ins_width) else stringResource(R.string.facet_ins_width_value, num(t.value))
    FacetRule.LEVEL -> stringResource(R.string.facet_ins_level)
    FacetRule.GIRDLE_TOP -> stringResource(R.string.facet_ins_girdle_top)
    FacetRule.MEET -> stringResource(R.string.facet_ins_meet, t.meet.joinToString(", "))
    FacetRule.TABLE -> stringResource(R.string.facet_ins_table, num(t.value * 100, 1))
    FacetRule.VALUE -> stringResource(R.string.facet_ins_value, num(t.value, 3))
}

@Composable
private fun issueLabel(issue: TierIssue): String = stringResource(
    when (issue) {
        TierIssue.NO_CUT -> R.string.facet_issue_no_cut
        TierIssue.SOME_HIDDEN -> R.string.facet_issue_some_hidden
        TierIssue.ALL_HIDDEN -> R.string.facet_issue_all_hidden
        TierIssue.NO_MEET_POINT -> R.string.facet_issue_no_meet
        TierIssue.NEEDS_GIRDLE -> R.string.facet_issue_needs_girdle
    }
)

private fun indicesShort(t: FacetTier, wheel: Int): String {
    if (t.rule == FacetRule.TABLE || (t.side == FacetSide.CROWN && t.angleDeg == 0.0)) return "—"
    val s = t.indices.take(4).joinToString("-") { FacetWheels.display(it, wheel) }
    return if (t.indices.size > 4) "$s…" else s
}

@Composable
fun FacetAtelierScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val designs = remember { mutableStateListOf<FacetDesign>().apply { addAll(loadDesigns(context)) } }
    var open by remember { mutableIntStateOf(-1) } // index dans « Mes tailles », -1 = bibliothèque

    fun persist() = saveDesigns(context, designs.toList())

    // retour Android : de la conception vers la bibliothèque
    BackHandler(enabled = open in designs.indices) { open = -1 }
    if (open in designs.indices) {
        key(open) { FacetDesignScreen(
            initial = designs[open],
            onChange = { if (open in designs.indices) { designs[open] = it; persist() } },
            onDuplicate = { d -> designs.add(d); persist(); open = designs.lastIndex },
            onDelete = { val i = open; open = -1; designs.removeAt(i); persist() },
            onBack = { open = -1 }
        ) }
    } else {
        FacetLibraryScreen(
            designs = designs,
            onOpenPreset = { preset -> designs.add(preset); persist(); open = designs.lastIndex },
            onOpen = { open = it },
            onNew = { d -> designs.add(d); persist(); open = designs.lastIndex },
            onBack = onBackClick
        )
    }
}

// ---------- Bibliothèque ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacetLibraryScreen(
    designs: List<FacetDesign>,
    onOpenPreset: (FacetDesign) -> Unit,
    onOpen: (Int) -> Unit,
    onNew: (FacetDesign) -> Unit,
    onBack: () -> Unit
) {
    var newDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_facet_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back)) }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(stringResource(R.string.facet_lib_intro), style = MaterialTheme.typography.bodyMedium)
            SectionTitle(stringResource(R.string.facet_lib_presets))
            CardGrid(
                items = FacetPresets.all.map { p -> LibraryItem(p, stringResource(R.string.facet_lib_preset_hint, materialLabel(p.material))) { onOpenPreset(p) } } +
                    LibraryItem(null, stringResource(R.string.facet_lib_new_hint)) { newDialog = true }
            )
            if (designs.isNotEmpty()) {
                SectionTitle(stringResource(R.string.facet_lib_mine))
                CardGrid(items = designs.mapIndexed { i, d ->
                    LibraryItem(d, stringResource(R.string.facet_lib_mine_hint, d.tiers.size, materialLabel(d.material))) { onOpen(i) }
                })
            }
            Text(stringResource(R.string.facet_lib_footer), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
        }
    }
    if (newDialog) {
        val defaultName = stringResource(R.string.facet_new_default_name)
        NewDesignDialog(defaultName, onDismiss = { newDialog = false }) { d -> newDialog = false; onNew(d) }
    }
}

private class LibraryItem(val design: FacetDesign?, val hint: String, val onClick: () -> Unit)

@Composable
private fun SectionTitle(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
}

@Composable
private fun CardGrid(items: List<LibraryItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { item -> LibraryCard(item, Modifier.weight(1f)) }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun LibraryCard(item: LibraryItem, modifier: Modifier) {
    val d = item.design
    Card(
        onClick = item.onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = cardColors(),
        border = if (d == null) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    ) {
        Column(Modifier.fillMaxWidth().padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
                if (d == null) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = accent(), modifier = Modifier.size(48.dp))
                } else {
                    val stone = remember(d) { FacetGeometry.build(d) }
                    if (stone.closed) FacetDiagram(d, stone, FacetSide.CROWN, Modifier.fillMaxSize(0.92f))
                    else CubeIcon(Modifier.fillMaxSize(0.6f))
                }
            }
            Text(
                d?.name ?: stringResource(R.string.facet_lib_new),
                style = MaterialTheme.typography.titleMedium, color = accent(), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(item.hint, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

/** Petit cube en perspective : une taille encore brute. */
@Composable
private fun CubeIcon(modifier: Modifier) {
    val line = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(modifier) {
        val s = size.minDimension; val o = Offset((size.width - s) / 2, (size.height - s) / 2)
        fun p(x: Float, y: Float) = Offset(o.x + x * s, o.y + y * s)
        val pts = listOf(p(0.15f, 0.3f), p(0.55f, 0.3f), p(0.55f, 0.85f), p(0.15f, 0.85f), p(0.85f, 0.12f), p(0.85f, 0.67f), p(0.45f, 0.12f))
        val e = listOf(0 to 1, 1 to 2, 2 to 3, 3 to 0, 1 to 4, 4 to 5, 5 to 2, 0 to 6, 6 to 4)
        e.forEach { (a, b) -> drawLine(line, pts[a], pts[b], strokeWidth = 2f) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NewDesignDialog(defaultName: String, onDismiss: () -> Unit, onCreate: (FacetDesign) -> Unit) {
    var name by remember { mutableStateOf(defaultName) }
    var wheel by remember { mutableIntStateOf(96) }
    var material by remember { mutableStateOf(FacetMaterial.DIAMANT) }
    var cubeText by remember { mutableStateOf(num(8.0)) }
    val cube = parseNumber(cubeText)?.takeIf { it in 1.0..100.0 }
    var pickMaterial by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.facet_new_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.facet_new_hint), style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(value = name, onValueChange = { name = it.take(40) }, singleLine = true, label = { Text(stringResource(R.string.facet_field_name)) })
                Text(stringResource(R.string.facet_wheel_title), style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FacetWheels.sizes.forEach { w -> FilterChip(colors = chipColors(), selected = w == wheel, onClick = { wheel = w }, label = { Text(w.toString()) }) }
                }
                OutlinedButton(onClick = { pickMaterial = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.facet_chip_material, materialLabel(material), num(material.ri, 3)), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedTextField(
                    value = cubeText, onValueChange = { cubeText = it }, singleLine = true, isError = cube == null,
                    label = { Text(stringResource(R.string.facet_cube_title)) }, suffix = { Text("mm") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(enabled = cube != null && name.isNotBlank(), onClick = {
                onCreate(FacetPresets.blank(name.trim(), wheel, material).copy(cubeMm = cube!!))
            }) { Text(stringResource(R.string.facet_create)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.facet_cancel)) } }
    )
    if (pickMaterial) MaterialDialog(material, onDismiss = { pickMaterial = false }) { material = it; pickMaterial = false }
}

@Composable
private fun MaterialDialog(current: FacetMaterial, onDismiss: () -> Unit, onPick: (FacetMaterial) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.facet_material_title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                FacetMaterial.entries.forEach { m ->
                    Text(
                        stringResource(R.string.facet_material_item, materialLabel(m), num(m.ri, 3), num(m.sg)),
                        fontWeight = if (m == current) FontWeight.Bold else FontWeight.Normal,
                        color = if (m == current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth().clickable { onPick(m) }.padding(vertical = 10.dp)
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.facet_close)) } }
    )
}

// ---------- Conception ----------

/** Brouillon de passe en cours de coupe (nouvelle passe ou passe existante). */
private data class Draft(val index: Int?, val tier: FacetTier, val symmetry: Int?, val start: Int, val mirror: Int)

private fun draftFor(design: FacetDesign, index: Int?, tier: FacetTier): Draft {
    val w = design.wheel
    val sorted = tier.indices.map { it % w }.sorted()
    // l'écriture la plus simple d'abord : sans miroir, puis avec le plus petit miroir
    for (m in 0..w / 2) for (n in FacetWheels.symmetriesFor(w)) {
        if (m > w / (2 * n)) continue
        for (s in 0 until w / n) {
            if (FacetWheels.indicesFor(s, n, w, m).map { it % w }.sorted() == sorted) return Draft(index, tier, n, s, m)
        }
    }
    return Draft(index, tier, null, tier.indices.firstOrNull() ?: 0, 0)
}

/**
 * [allow3d] = false montre le dessin des facettes à la place de la vue 3D (captures
 * de test, où la WebView ne se dessine pas) ; [startEditing] ouvre directement la
 * coupe de cette passe.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacetDesignScreen(
    initial: FacetDesign,
    onChange: (FacetDesign) -> Unit,
    onDuplicate: (FacetDesign) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    allow3d: Boolean = true,
    startEditing: Int? = null
) {
    var design by remember { mutableStateOf(initial) }
    val undo = remember { mutableStateListOf<FacetDesign>() }
    val redo = remember { mutableStateListOf<FacetDesign>() }
    fun update(next: FacetDesign) {
        if (next == design) return
        undo.add(design); if (undo.size > 80) undo.removeAt(0)
        redo.clear(); design = next
    }
    LaunchedEffect(design) { onChange(design) }

    var selected by remember { mutableIntStateOf(startEditing ?: -1) }
    var draft by remember { mutableStateOf(startEditing?.let { draftFor(initial, it, initial.tiers[it]) }) }
    var symmetry by remember { mutableIntStateOf(8) }
    var mirror by remember { mutableIntStateOf(0) }
    var menu by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf<String?>(null) }
    var view3dUnavailable by remember { mutableStateOf(!allow3d) }
    // retour Android pendant une coupe : abandonner la coupe en cours
    BackHandler(enabled = draft != null) { draft = null }

    // ce qu'on montre : la taille, ou l'aperçu de la passe en cours de coupe
    val shown = remember(design, draft) {
        val d = draft ?: return@remember design
        val tiers = design.tiers.toMutableList()
        if (d.index != null) tiers[d.index] = d.tier else tiers.add(d.tier)
        design.copy(tiers = tiers)
    }
    val highlight = draft?.let { it.index ?: shown.tiers.lastIndex } ?: selected.takeIf { it in design.tiers.indices }
    val stone = remember(shown) { FacetGeometry.build(shown) }
    val metrics = remember(shown, stone) { FacetGeometry.metrics(shown, stone) }
    val meshJson = remember(stone, highlight) { FacetGeometry.meshJson(stone, highlight) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(design.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = { if (draft != null) draft = null else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { redo.add(design); design = undo.removeAt(undo.lastIndex); draft = null }, enabled = undo.isNotEmpty() && draft == null) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = stringResource(R.string.facet_undo))
                    }
                    IconButton(onClick = { undo.add(design); design = redo.removeAt(redo.lastIndex); draft = null }, enabled = redo.isNotEmpty() && draft == null) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = stringResource(R.string.facet_redo))
                    }
                    Box {
                        IconButton(onClick = { menu = true }) { Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.facet_more)) }
                        DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                            DropdownMenuItem(text = { Text(stringResource(R.string.facet_menu_rename)) }, onClick = { menu = false; dialog = "rename" })
                            DropdownMenuItem(text = { Text(stringResource(R.string.facet_menu_duplicate)) }, onClick = {
                                menu = false; onDuplicate(design.copy(name = design.name + " (2)"))
                            })
                            DropdownMenuItem(text = { Text(stringResource(R.string.facet_menu_delete)) }, onClick = { menu = false; dialog = "delete" })
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // règle d'index, symétrie, miroir
            val ruleTier = draft?.tier ?: design.tiers.getOrNull(selected)
            WheelRuler(design.wheel, ruleTier?.indices.orEmpty(), onWheelClick = { dialog = "wheel" })
            Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Stepper(stringResource(R.string.facet_symmetry), symmetry.toString(), Modifier.weight(1f),
                    onMinus = { FacetWheels.symmetriesFor(design.wheel).lastOrNull { it < symmetry }?.let { symmetry = it } },
                    onPlus = { FacetWheels.symmetriesFor(design.wheel).firstOrNull { it > symmetry }?.let { symmetry = it } })
                Stepper(stringResource(R.string.facet_mirror), mirror.toString(), Modifier.weight(1f),
                    onMinus = { if (mirror > 0) mirror-- },
                    onPlus = { if (mirror < design.wheel / (2 * symmetry)) mirror++ })
            }

            // pierre en 3D
            Card(shape = RoundedCornerShape(16.dp), colors = cardColors(), modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).height(230.dp)) {
                Box(Modifier.fillMaxSize()) {
                    if (!view3dUnavailable) {
                        FacetStone3DView(meshJson, shown.material, Modifier.fillMaxSize(), onUnavailable = { view3dUnavailable = true })
                    } else {
                        if (stone.faces.none { it.plane != null }) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CubeIcon(Modifier.size(140.dp)) }
                        } else Row(Modifier.fillMaxSize().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            FacetDiagram(shown, stone, FacetSide.PAVILION, Modifier.size(150.dp), highlightTier = highlight)
                            FacetDiagram(shown, stone, FacetSide.CROWN, Modifier.size(150.dp), highlightTier = highlight)
                        }
                    }
                    Text(
                        stringResource(R.string.facet_view_material, materialLabel(shown.material), num(shown.material.ri, 3)),
                        style = MaterialTheme.typography.labelSmall, color = Color(0xFF4A4A42),
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                    )
                    metrics?.let { m ->
                        Text(
                            stringResource(R.string.facet_view_info, num(m.lengthToWidth), m.facetCount - m.girdleFacetCount, num(m.carats), num(m.lengthMm), num(m.widthMm), num(m.heightMm), m.girdleFacetCount),
                            style = MaterialTheme.typography.labelSmall, color = Color(0xFF4A4A42),
                            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)
                        )
                    }
                }
            }

            if (draft == null) {
                // réglages de la pierre
                Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { dialog = "material" }, label = { Text(materialLabel(design.material)) })
                    AssistChip(onClick = { dialog = "cube" }, label = { Text(stringResource(R.string.facet_chip_cube, num(design.cubeMm))) })
                    AssistChip(onClick = { dialog = "girdle" }, label = { Text(stringResource(R.string.facet_chip_girdle, num(design.girdlePercent, 1))) })
                }
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    Warnings(design, stone, metrics)
                    InstructionTable(design, stone.issues, selected) { selected = it }
                    metrics?.takeIf { it.complete }?.let { m ->
                        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Metric(stringResource(R.string.facet_sum_table), num(m.tablePercent, 1) + " %", Modifier.weight(1f))
                            Metric(stringResource(R.string.facet_sum_crown), num(m.crownPercent, 1) + " %", Modifier.weight(1f))
                            Metric(stringResource(R.string.facet_sum_pavilion), num(m.pavilionPercent, 1) + " %", Modifier.weight(1f))
                            Metric(stringResource(R.string.facet_sum_total), num(m.totalPercent, 1) + " %", Modifier.weight(1f))
                        }
                    }
                    Text(
                        stringResource(R.string.facet_critical_line, num(design.material.criticalAngleDeg, 1)),
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
                // outils
                HorizontalDivider()
                Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    val sel = selected.takeIf { it in design.tiers.indices }
                    ToolButton(Icons.Default.Edit, stringResource(R.string.facet_tool_edit), enabled = sel != null) {
                        draft = draftFor(design, sel, design.tiers[sel!!])
                    }
                    ToolButton(Icons.Default.KeyboardArrowUp, stringResource(R.string.facet_tool_up), enabled = sel != null && sel > 0) {
                        val t = design.tiers.toMutableList(); val i = sel!!; t.add(i - 1, t.removeAt(i)); update(design.copy(tiers = t)); selected = i - 1
                    }
                    ToolButton(Icons.Default.KeyboardArrowDown, stringResource(R.string.facet_tool_down), enabled = sel != null && sel < design.tiers.lastIndex) {
                        val t = design.tiers.toMutableList(); val i = sel!!; t.add(i + 1, t.removeAt(i)); update(design.copy(tiers = t)); selected = i + 1
                    }
                    val hidden = sel?.let { design.tiers[it].hidden } == true
                    ToolButton(if (hidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        stringResource(if (hidden) R.string.facet_tool_show else R.string.facet_tool_hide), enabled = sel != null) {
                        val i = sel!!; update(design.copy(tiers = design.tiers.mapIndexed { k, t -> if (k == i) t.copy(hidden = !t.hidden) else t }))
                    }
                    ToolButton(Icons.Default.ContentCut, stringResource(R.string.facet_tool_cut), enabled = true) {
                        val side = when {
                            design.tiers.isEmpty() -> FacetSide.PAVILION
                            else -> design.tiers.last().side
                        }
                        val tier = FacetTier(
                            name = FacetPresets.nextName(design, side), side = side,
                            angleDeg = when (side) { FacetSide.PAVILION -> 41.0; FacetSide.GIRDLE -> 90.0; FacetSide.CROWN -> 35.0 },
                            indices = FacetWheels.indicesFor(0, symmetry, design.wheel, mirror),
                            rule = if (design.tiers.isEmpty()) FacetRule.CENTER else FacetRule.VALUE,
                            value = when (side) { FacetSide.GIRDLE -> 1.0; FacetSide.PAVILION -> 0.5; FacetSide.CROWN -> 2.0 }
                        )
                        draft = Draft(null, tier, symmetry, 0, mirror)
                    }
                }
            } else {
                CutPanel(
                    design = design,
                    draft = draft!!,
                    computedDepth = stone.depths[highlight ?: -1],
                    issue = highlight?.let { stone.issues[it] },
                    visible = highlight?.let { stone.visibleCount[it] } ?: 0,
                    onDraft = { draft = it },
                    onCancel = { draft = null },
                    onDelete = draft!!.index?.let { i -> {
                        update(design.copy(tiers = design.tiers.filterIndexed { k, _ -> k != i }.map { t -> t.copy(meet = t.meet - design.tiers[i].name) }))
                        draft = null; selected = -1
                    } },
                    onConfirm = { d ->
                        val tiers = design.tiers.toMutableList()
                        if (d.index != null) {
                            val old = tiers[d.index].name
                            tiers[d.index] = d.tier
                            // renommer une passe met à jour les rencontres qui la citent
                            if (old != d.tier.name) for (k in tiers.indices) tiers[k] = tiers[k].copy(meet = tiers[k].meet.map { if (it == old) d.tier.name else it })
                        } else tiers.add(d.tier)
                        update(design.copy(tiers = tiers))
                        selected = d.index ?: tiers.lastIndex
                        symmetry = d.symmetry ?: symmetry; mirror = d.mirror
                        draft = null
                    }
                )
            }
        }
    }

    when (dialog) {
        "material" -> MaterialDialog(design.material, onDismiss = { dialog = null }) { update(design.copy(material = it)); dialog = null }
        "wheel" -> AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(stringResource(R.string.facet_wheel_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.facet_wheel_hint), style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FacetWheels.sizes.forEach { w ->
                            FilterChip(colors = chipColors(), selected = w == design.wheel, onClick = {
                                update(FacetWheels.convert(design, w))
                                if (w % symmetry != 0) symmetry = FacetWheels.symmetriesFor(w).last { it <= 8 }
                                mirror = 0; dialog = null
                            }, label = { Text(w.toString()) })
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_close)) } }
        )
        "cube" -> NumberDialog(stringResource(R.string.facet_cube_title), design.cubeMm, 1.0, 100.0, { dialog = null }) { update(design.copy(cubeMm = it)) }
        "girdle" -> NumberDialog(stringResource(R.string.facet_girdle_title), design.girdlePercent, 0.0, 15.0, { dialog = null }) { update(design.copy(girdlePercent = it)) }
        "rename" -> {
            var name by remember { mutableStateOf(design.name) }
            AlertDialog(
                onDismissRequest = { dialog = null },
                title = { Text(stringResource(R.string.facet_rename_title)) },
                text = { OutlinedTextField(value = name, onValueChange = { name = it.take(40) }, singleLine = true) },
                confirmButton = { TextButton(onClick = { if (name.isNotBlank()) update(design.copy(name = name.trim())); dialog = null }) { Text(stringResource(R.string.facet_validate)) } },
                dismissButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_cancel)) } }
            )
        }
        "delete" -> AlertDialog(
            onDismissRequest = { dialog = null },
            title = { Text(stringResource(R.string.facet_delete_title, design.name)) },
            text = { Text(stringResource(R.string.facet_delete_text)) },
            confirmButton = { TextButton(onClick = { dialog = null; onDelete() }) { Text(stringResource(R.string.facet_menu_delete), color = MaterialTheme.colorScheme.error) } },
            dismissButton = { TextButton(onClick = { dialog = null }) { Text(stringResource(R.string.facet_cancel)) } }
        )
    }
}

/** Règle d'index : la roue déroulée autour des facettes de la passe, ses index en or. */
@Composable
private fun WheelRuler(wheel: Int, marked: List<Int>, onWheelClick: () -> Unit) {
    val measurer = rememberTextMeasurer()
    val line = MaterialTheme.colorScheme.onSurfaceVariant
    Card(shape = RoundedCornerShape(14.dp), colors = cardColors(), modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(42.dp).border(2.dp, accent(), CircleShape).clickable(onClick = onWheelClick),
                contentAlignment = Alignment.Center
            ) { Text(wheel.toString(), color = accent(), fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(10.dp))
            val center = marked.firstOrNull()?.let { it % wheel } ?: 0
            val markSet = marked.map { it % wheel }.toSet()
            Canvas(Modifier.weight(1f).height(40.dp)) {
                drawRuler(measurer, wheel, center, markSet, line, accent())
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRuler(
    measurer: TextMeasurer, wheel: Int, center: Int, marks: Set<Int>, line: Color, mark: Color
) {
    val span = 28 // dents visibles de chaque côté
    val step = size.width / (2 * span)
    for (k in -span..span) {
        val idx = ((center + k) % wheel + wheel) % wheel
        val x = size.width / 2 + k * step
        val major = idx % 4 == 0
        val isMark = idx in marks
        drawLine(
            if (isMark) mark else line,
            Offset(x, size.height), Offset(x, size.height * if (isMark) 0.35f else if (major) 0.55f else 0.72f),
            strokeWidth = if (isMark) 3.5f else 1.2f
        )
        if (major && abs(k) < span - 1) {
            val label = FacetWheels.normalize(idx, wheel).toString()
            val r = measurer.measure(label, TextStyle(fontSize = 9.sp, color = if (isMark) mark else line))
            drawText(r, topLeft = Offset(x - r.size.width / 2f, 0f))
        }
    }
}

@Composable
private fun Stepper(label: String, value: String, modifier: Modifier, onMinus: () -> Unit, onPlus: () -> Unit) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = cardColors()) {
        Row(Modifier.padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            TextButton(onClick = onMinus) { Text("−", color = accent(), fontSize = 18.sp) }
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            TextButton(onClick = onPlus) { Text("+", color = accent(), fontSize = 18.sp) }
        }
    }
}

@Composable
private fun Warnings(design: FacetDesign, stone: fr.gemsofrod.encyclopedie.data.FacetStone, metrics: fr.gemsofrod.encyclopedie.data.FacetMetrics?) {
    val list = buildList {
        if (design.tiers.isEmpty()) add(stringResource(R.string.facet_empty_hint))
        else if (!stone.closed) add(stringResource(R.string.facet_warn_raw))
        val critical = design.material.criticalAngleDeg
        design.tiers.firstOrNull { it.side == FacetSide.PAVILION && !it.hidden }?.let { p ->
            if (p.angleDeg < critical) add(stringResource(R.string.facet_warn_critical, num(p.angleDeg), num(critical, 1)))
        }
    }
    list.forEach {
        Text(it, color = if (design.tiers.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.dp))
    }
}

@Composable
private fun InstructionTable(design: FacetDesign, issues: Map<Int, TierIssue>, selected: Int, onSelect: (Int) -> Unit) {
    var group: Boolean? = null // true = couronne
    Column(Modifier.padding(horizontal = 12.dp)) {
        design.tiers.forEachIndexed { i, t ->
            val crown = t.side == FacetSide.CROWN
            if (crown != group) {
                group = crown
                Text(
                    stringResource(if (crown) R.string.facet_group_crown else R.string.facet_group_pavilion).uppercase(),
                    style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp,
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
            val bg = if (i == selected) Gold.copy(alpha = 0.22f) else Color.Transparent
            Row(
                Modifier.fillMaxWidth().background(bg).clickable { onSelect(i) }.padding(horizontal = 6.dp, vertical = 8.dp).alpha(if (t.hidden) 0.45f else 1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(t.name, Modifier.width(34.dp), fontWeight = FontWeight.SemiBold)
                Text(num(t.angleDeg) + "°", Modifier.width(64.dp), color = accent(), fontWeight = FontWeight.SemiBold)
                Text(indicesShort(t, design.wheel), Modifier.width(96.dp), style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Column(Modifier.weight(1f)) {
                    Text(
                        if (t.hidden) stringResource(R.string.facet_hidden_tier) else instruction(t),
                        style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                    roleLabel(t.role)?.let { Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    issues[i]?.let { issue ->
                        Text(issueLabel(issue), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
                if (issues[i] != null) Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun RowScope.ToolButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, enabled: Boolean, onClick: () -> Unit) {
    Column(
        Modifier.weight(1f).clickable(enabled = enabled, onClick = onClick).padding(vertical = 6.dp).alpha(if (enabled) 1f else 0.38f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = accent(), modifier = Modifier.size(22.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1)
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
                value = text, onValueChange = { text = it }, singleLine = true, isError = !valid,
                supportingText = { Text(stringResource(R.string.facet_range, num(min, 1), num(max, 1))) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        confirmButton = { TextButton(onClick = { if (valid) { onValue(parsed!!); onDismiss() } }, enabled = valid) { Text(stringResource(R.string.facet_validate)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.facet_cancel)) } }
    )
}

// ---------- Couper une passe ----------

/** Plage du curseur de profondeur selon le côté (distance du plan dans le repère du cube). */
private fun depthRange(side: FacetSide): ClosedFloatingPointRange<Float> = when (side) {
    FacetSide.PAVILION -> -0.6f..2.6f
    FacetSide.GIRDLE -> 0.1f..1.75f
    FacetSide.CROWN -> 0.0f..3.4f
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CutPanel(
    design: FacetDesign,
    draft: Draft,
    computedDepth: Double?,
    issue: TierIssue?,
    visible: Int,
    onDraft: (Draft) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)?,
    onConfirm: (Draft) -> Unit
) {
    val t = draft.tier
    val wheel = design.wheel
    var angleText by remember(draft.index, t.side) { mutableStateOf(num(t.angleDeg)) }
    var indicesText by remember(draft.index) { mutableStateOf(t.indices.joinToString(" ") { FacetWheels.display(it, wheel) }) }
    fun valueTextFor(rule: FacetRule, v: Double) = when (rule) {
        FacetRule.TABLE -> num(v * 100, 1)
        FacetRule.VALUE -> num(v, 3)
        else -> num(v)
    }
    var valueText by remember(draft.index, t.rule) { mutableStateOf(valueTextFor(t.rule, t.value)) }
    var fine by remember { mutableStateOf(false) }
    val angleOk = parseNumber(angleText)?.let { it in 0.0..90.0 } == true || t.side == FacetSide.GIRDLE
    val before = design.tiers.take(draft.index ?: design.tiers.size).map { it.name }

    fun set(tier: FacetTier) = onDraft(draft.copy(tier = tier))
    fun regen(sym: Int?, start: Int, mir: Int) {
        val idx = if (sym != null) FacetWheels.indicesFor(start, sym, wheel, mir) else t.indices
        indicesText = idx.joinToString(" ") { FacetWheels.display(it, wheel) }
        onDraft(draft.copy(tier = t.copy(indices = idx), symmetry = sym, start = start, mirror = mir))
    }

    Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            if (draft.index != null) stringResource(R.string.facet_cut_title, t.name) else stringResource(R.string.facet_cut_new),
            style = MaterialTheme.typography.titleLarge, color = accent()
        )
        Text(stringResource(R.string.facet_cut_hint), style = MaterialTheme.typography.bodySmall)

        // côté
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(FacetSide.PAVILION to R.string.facet_tab_pavilion, FacetSide.GIRDLE to R.string.facet_tab_girdle, FacetSide.CROWN to R.string.facet_tab_crown).forEach { (s, label) ->
                FilterChip(colors = chipColors(), selected = t.side == s, onClick = {
                    if (s != t.side) {
                        val rule = when {
                            s == FacetSide.GIRDLE && t.rule in listOf(FacetRule.GIRDLE_TOP, FacetRule.TABLE, FacetRule.CENTER) -> FacetRule.WIDTH
                            s != FacetSide.GIRDLE && t.rule == FacetRule.WIDTH -> FacetRule.VALUE
                            s != FacetSide.CROWN && t.rule in listOf(FacetRule.GIRDLE_TOP, FacetRule.TABLE) -> FacetRule.VALUE
                            else -> t.rule
                        }
                        val name = if (draft.index == null) FacetPresets.nextName(design, s) else t.name
                        set(t.copy(side = s, rule = rule, name = name, angleDeg = if (s == FacetSide.GIRDLE) 90.0 else if (t.side == FacetSide.GIRDLE) 41.0 else t.angleDeg,
                            value = if (rule == FacetRule.WIDTH) 1.0 else t.value))
                    }
                }, label = { Text(stringResource(label)) })
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = t.name, onValueChange = { set(t.copy(name = it.filter { c -> c.isLetterOrDigit() }.take(6))) },
                singleLine = true, label = { Text(stringResource(R.string.facet_field_name)) }, modifier = Modifier.weight(0.7f)
            )
            OutlinedTextField(
                value = if (t.side == FacetSide.GIRDLE) num(90.0) else angleText,
                onValueChange = { angleText = it; parseNumber(it)?.takeIf { a -> a in 0.0..90.0 }?.let { a -> set(t.copy(angleDeg = a)) } },
                enabled = t.side != FacetSide.GIRDLE, singleLine = true, isError = !angleOk, suffix = { Text("°") },
                label = { Text(stringResource(R.string.facet_field_angle)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.weight(1.3f)
            )
        }

        // index
        Text(stringResource(R.string.facet_symmetry), style = MaterialTheme.typography.labelMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FacetWheels.symmetriesFor(wheel).forEach { n ->
                FilterChip(colors = chipColors(), selected = draft.symmetry == n, onClick = { regen(n, draft.start, draft.mirror.coerceAtMost(wheel / (2 * n))) }, label = { Text(n.toString()) })
            }
            FilterChip(colors = chipColors(), selected = draft.symmetry == null, onClick = { onDraft(draft.copy(symmetry = null)) }, label = { Text(stringResource(R.string.facet_symmetry_free)) })
        }
        if (draft.symmetry != null) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = draft.start.toString(),
                    onValueChange = { v -> v.filter(Char::isDigit).take(3).toIntOrNull()?.let { regen(draft.symmetry, it % wheel, draft.mirror) } },
                    singleLine = true, label = { Text(stringResource(R.string.facet_field_start)) }, suffix = { Text("/ $wheel") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.weight(1f)
                )
                Stepper(stringResource(R.string.facet_mirror), draft.mirror.toString(), Modifier.weight(1.2f),
                    onMinus = { if (draft.mirror > 0) regen(draft.symmetry, draft.start, draft.mirror - 1) },
                    onPlus = { if (draft.mirror < wheel / (2 * draft.symmetry)) regen(draft.symmetry, draft.start, draft.mirror + 1) })
            }
        }
        OutlinedTextField(
            value = indicesText,
            onValueChange = { indicesText = it; onDraft(draft.copy(symmetry = null, tier = t.copy(indices = FacetWheels.parse(it, wheel)))) },
            isError = t.indices.isEmpty(),
            label = { Text(stringResource(R.string.facet_field_indices)) },
            supportingText = { Text(stringResource(R.string.facet_indices_count, t.indices.size)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()
        )

        // jusqu'où couper
        Text(stringResource(R.string.facet_rule_title), style = MaterialTheme.typography.labelMedium)
        val rules = buildList {
            if (t.side != FacetSide.GIRDLE) add(FacetRule.CENTER)
            if (t.side == FacetSide.GIRDLE) add(FacetRule.WIDTH)
            add(FacetRule.LEVEL)
            if (t.side == FacetSide.CROWN) { add(FacetRule.GIRDLE_TOP); if (t.angleDeg == 0.0) add(FacetRule.TABLE) }
            add(FacetRule.MEET)
            add(FacetRule.VALUE)
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            rules.forEach { r ->
                FilterChip(colors = chipColors(), selected = t.rule == r, onClick = {
                    val v = when (r) {
                        FacetRule.WIDTH -> 1.0
                        FacetRule.TABLE -> 0.55
                        FacetRule.VALUE -> computedDepth ?: t.value // part de la profondeur actuelle
                        else -> t.value
                    }
                    valueText = valueTextFor(r, v)
                    set(t.copy(rule = r, value = v))
                }, label = { Text(ruleLabel(r)) })
            }
        }
        when (t.rule) {
            FacetRule.WIDTH, FacetRule.TABLE -> OutlinedTextField(
                value = valueText,
                onValueChange = { valueText = it; parseNumber(it)?.let { v -> set(t.copy(value = if (t.rule == FacetRule.TABLE) v / 100 else v)) } },
                singleLine = true,
                label = { Text(stringResource(if (t.rule == FacetRule.TABLE) R.string.facet_field_table else R.string.facet_field_width)) },
                suffix = { if (t.rule == FacetRule.TABLE) Text("%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth()
            )
            FacetRule.MEET -> FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (before.isEmpty()) Text(stringResource(R.string.facet_meet_none), style = MaterialTheme.typography.bodySmall)
                before.forEach { n ->
                    FilterChip(colors = chipColors(), selected = n in t.meet, onClick = { set(t.copy(meet = if (n in t.meet) t.meet - n else t.meet + n)) }, label = { Text(n) })
                }
            }
            FacetRule.VALUE -> {
                val full = depthRange(t.side)
                val range = if (fine) (t.value.toFloat() - 0.08f)..(t.value.toFloat() + 0.08f) else full
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { val v = t.value - if (fine) 0.001 else 0.01; valueText = num(v, 3); set(t.copy(value = v)) }) { Text("−", color = accent(), fontSize = 18.sp) }
                    Slider(
                        value = t.value.toFloat().coerceIn(range.start, range.endInclusive),
                        onValueChange = { v -> valueText = num(v.toDouble(), 3); set(t.copy(value = v.toDouble())) },
                        valueRange = range, modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { val v = t.value + if (fine) 0.001 else 0.01; valueText = num(v, 3); set(t.copy(value = v)) }) { Text("+", color = accent(), fontSize = 18.sp) }
                    FilterChip(colors = chipColors(), selected = fine, onClick = { fine = !fine }, label = { Text(stringResource(R.string.facet_fine)) })
                }
                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it; parseNumber(it)?.let { v -> set(t.copy(value = v)) } },
                    singleLine = true, label = { Text(stringResource(R.string.facet_field_depth)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth()
                )
            }
            else -> {}
        }
        computedDepth?.let {
            Text(stringResource(R.string.facet_cut_depth, num(it, 3), visible, t.indices.size), style = MaterialTheme.typography.bodySmall, color = accent())
        }
        issue?.let { Text(issueLabel(it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }

        if (onDelete != null) TextButton(onClick = onDelete) { Text(stringResource(R.string.facet_delete), color = MaterialTheme.colorScheme.error) }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.facet_cancel), maxLines = 1) }
            Button(
                modifier = Modifier.weight(1f),
                enabled = angleOk && t.indices.isNotEmpty() && t.name.isNotBlank() && (t.rule != FacetRule.MEET || t.meet.isNotEmpty()) &&
                    design.tiers.withIndex().none { (k, o) -> o.name == t.name && k != draft.index },
                onClick = { onConfirm(draft) }
            ) { Text(stringResource(R.string.facet_tool_cut), maxLines = 1) }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ruleLabel(r: FacetRule): String = stringResource(
    when (r) {
        FacetRule.CENTER -> R.string.facet_rule_center
        FacetRule.WIDTH -> R.string.facet_rule_width
        FacetRule.LEVEL -> R.string.facet_rule_level
        FacetRule.GIRDLE_TOP -> R.string.facet_rule_girdle_top
        FacetRule.MEET -> R.string.facet_rule_meet
        FacetRule.TABLE -> R.string.facet_rule_table
        FacetRule.VALUE -> R.string.facet_rule_value
    }
)
