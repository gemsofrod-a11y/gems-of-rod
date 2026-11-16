package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FacetDesign
import fr.gemsofrod.encyclopedie.data.FacetGeometry
import fr.gemsofrod.encyclopedie.data.FacetLighting
import fr.gemsofrod.encyclopedie.data.FacetMaterial
import fr.gemsofrod.encyclopedie.data.FacetRenderResult
import fr.gemsofrod.encyclopedie.data.FacetRenderSettings
import fr.gemsofrod.encyclopedie.data.FacetRenderer
import fr.gemsofrod.encyclopedie.data.hslToRgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

/**
 * Rendu réaliste de l'Atelier de facettage (étape 2, validée par Sébastien le
 * 17/11/2026) : image calculée par lancer de rayons (FacetRenderer), réglable —
 * matière, indice, dispersion, couleur, densité, inclinaison, éclairage. Un aperçu
 * rapide suit les réglages, puis l'image fine est calculée sur tous les cœurs.
 */

private val RenderGold = Color(0xFFD6B872)
private val RenderBackground = 0xFF141D17.toInt()

private fun fmt(v: Double, d: Int) = String.format(Locale.getDefault(), "%.${d}f", v)

/** Calcul réparti par bandes de lignes sur les cœurs du téléphone. */
private suspend fun renderParallel(design: FacetDesign, s: FacetRenderSettings, size: Int, samples: Int): FacetRenderResult =
    withContext(Dispatchers.Default) {
        val stone = FacetGeometry.build(design)
        val bands = 24
        val parts = coroutineScope {
            (0 until bands).map { b ->
                async {
                    val from = b * size / bands; val to = (b + 1) * size / bands
                    FacetRenderer.render(stone, s, size, samples, RenderBackground, from until to)
                }
            }.awaitAll()
        }
        val pixels = IntArray(size * size)
        parts.forEachIndexed { b, r ->
            val from = b * size / bands; val to = (b + 1) * size / bands
            System.arraycopy(r.pixels, from * size, pixels, from * size, (to - from) * size)
        }
        val entered = parts.sumOf { it.entered }
        val returned = parts.sumOf { it.returnedSum }
        FacetRenderResult(size, pixels, if (entered > 0) returned / entered else 0.0, returned, entered)
    }

private fun toBitmap(r: FacetRenderResult): Bitmap = Bitmap.createBitmap(r.pixels, r.size, r.size, Bitmap.Config.ARGB_8888)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun FacetRenderScreen(design: FacetDesign, onBack: () -> Unit, synchronous: Boolean = false) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    var settings by remember(design.material) { mutableStateOf(FacetRenderSettings.forMaterial(design.material)) }
    var material by remember { mutableStateOf(design.material) }
    // [synchronous] : image calculée tout de suite (captures de test, sans attendre les cœurs)
    val first = remember { if (synchronous) FacetRenderer.render(FacetGeometry.build(design), settings, 320, 1, RenderBackground) else null }
    var image by remember { mutableStateOf(first?.let { toBitmap(it) }) }
    var lightReturn by remember { mutableStateOf(first?.lightReturn) }
    var computing by remember { mutableStateOf(!synchronous) }
    var pickMaterial by remember { mutableStateOf(false) }
    val closed = remember(design) { FacetGeometry.build(design).closed }

    // aperçu rapide à chaque réglage, puis image fine quand on ne touche plus à rien
    LaunchedEffect(design, settings) {
        if (synchronous) return@LaunchedEffect
        computing = true
        val quick = renderParallel(design, settings, 150, 1)
        image = toBitmap(quick); lightReturn = quick.lightReturn
        delay(250)
        val fine = renderParallel(design, settings, 420, 2)
        image = toBitmap(fine); lightReturn = fine.lightReturn
        computing = false
    }

    val accentColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) Color(0xFF7E5F1A) else RenderGold
    val shareTitle = stringResource(R.string.facet_render_share)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.facet_render_title), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back)) }
                },
                actions = {
                    IconButton(onClick = {
                        val bmp = image ?: return@IconButton
                        val dir = File(context.cacheDir, "facet_renders").apply { mkdirs() }
                        val file = File(dir, design.name.replace(Regex("[^A-Za-z0-9À-ÿ _-]"), "_") + ".png")
                        FileOutputStream(file).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                        val send = Intent(Intent.ACTION_SEND).apply {
                            type = "image/png"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(send, shareTitle))
                    }, enabled = image != null) { Icon(Icons.Default.Share, contentDescription = shareTitle) }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141D17)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).aspectRatio(1f)
            ) {
                Box(Modifier.fillMaxSize()) {
                    image?.let {
                        Image(it.asImageBitmap(), contentDescription = design.name, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
                    }
                    Text(
                        stringResource(R.string.facet_render_info, lightReturn?.let { fmt(it * 100, 0) } ?: "…") +
                            if (computing) " · " + stringResource(R.string.facet_render_computing) else "",
                        style = MaterialTheme.typography.labelSmall, color = Color(0xFFB9C9BD),
                        modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
                    )
                }
            }
            if (!closed) {
                Text(
                    stringResource(R.string.facet_render_unfinished), color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            Text(
                stringResource(R.string.facet_render_hint), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Row(
                        Modifier.fillMaxWidth().clickable { pickMaterial = true }.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.facet_material_title), style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(110.dp))
                        Text(materialName(material), modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("▾", color = accentColor)
                    }
                    SettingSlider(stringResource(R.string.facet_render_ri), settings.ri, 1.3, 2.8, fmt(settings.ri, 3)) { settings = settings.copy(ri = it) }
                    SettingSlider(stringResource(R.string.facet_render_dispersion), settings.dispersion, 0.0, 0.2, fmt(settings.dispersion, 3)) { settings = settings.copy(dispersion = it) }
                    val swatch = hslToRgb(settings.hue, settings.saturation, settings.lightness).let { Color(it[0].toFloat(), it[1].toFloat(), it[2].toFloat()) }
                    SettingSlider(stringResource(R.string.facet_render_hue), settings.hue, 0.0, 360.0, fmt(settings.hue, 0) + "°", swatch) { settings = settings.copy(hue = it) }
                    SettingSlider(stringResource(R.string.facet_render_saturation), settings.saturation, 0.0, 1.0, fmt(settings.saturation * 100, 0) + " %") { settings = settings.copy(saturation = it) }
                    SettingSlider(stringResource(R.string.facet_render_lightness), settings.lightness, 0.15, 0.95, fmt(settings.lightness * 100, 0) + " %") { settings = settings.copy(lightness = it) }
                    SettingSlider(stringResource(R.string.facet_render_density), settings.density, 0.1, 3.0, fmt(settings.density, 2)) { settings = settings.copy(density = it) }
                    SettingSlider(stringResource(R.string.facet_render_tilt_x), settings.tiltX, -40.0, 40.0, fmt(settings.tiltX, 0) + "°") { settings = settings.copy(tiltX = it) }
                    SettingSlider(stringResource(R.string.facet_render_tilt_y), settings.tiltY, -40.0, 40.0, fmt(settings.tiltY, 0) + "°") { settings = settings.copy(tiltY = it) }
                    Text(stringResource(R.string.facet_render_lighting), style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(FacetLighting.STUDIO to R.string.facet_light_studio, FacetLighting.SUN to R.string.facet_light_sun, FacetLighting.SOFT to R.string.facet_light_soft).forEach { (l, label) ->
                            FilterChip(
                                selected = settings.lighting == l, onClick = { settings = settings.copy(lighting = l) },
                                label = { Text(stringResource(label)) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = RenderGold.copy(alpha = 0.35f), selectedLabelColor = MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = { settings = FacetRenderSettings.forMaterial(material) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
            ) { Text(stringResource(R.string.facet_render_reset), color = MaterialTheme.colorScheme.onSurface) }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (pickMaterial) {
        AlertDialog(
            onDismissRequest = { pickMaterial = false },
            title = { Text(stringResource(R.string.facet_material_title)) },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    FacetMaterial.entries.forEach { m ->
                        Text(
                            stringResource(R.string.facet_material_item, materialName(m), fmt(m.ri, 3), fmt(m.sg, 2)),
                            fontWeight = if (m == material) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth().clickable {
                                material = m; settings = FacetRenderSettings.forMaterial(m).copy(tiltX = settings.tiltX, tiltY = settings.tiltY, lighting = settings.lighting)
                                pickMaterial = false
                            }.padding(vertical = 10.dp)
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { pickMaterial = false }) { Text(stringResource(R.string.facet_close)) } }
        )
    }
}

@Composable
private fun SettingSlider(label: String, value: Double, min: Double, max: Double, shown: String, swatch: Color? = null, onChange: (Double) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(110.dp), maxLines = 2)
        Slider(
            value = value.toFloat().coerceIn(min.toFloat(), max.toFloat()),
            onValueChange = { onChange(it.toDouble()) },
            valueRange = min.toFloat()..max.toFloat(),
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(thumbColor = RenderGold, activeTrackColor = RenderGold, inactiveTrackColor = RenderGold.copy(alpha = 0.25f))
        )
        if (swatch != null) Box(Modifier.padding(start = 6.dp).size(16.dp).clip(CircleShape).background(swatch))
        Text(shown, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(56.dp).padding(start = 6.dp), maxLines = 1)
    }
}

@Composable
private fun materialName(m: FacetMaterial): String = stringResource(
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
