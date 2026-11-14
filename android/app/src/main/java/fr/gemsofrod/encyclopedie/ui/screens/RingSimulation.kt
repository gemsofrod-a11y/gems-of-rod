package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import fr.gemsofrod.encyclopedie.ui.components.Ring3DView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.RingConfiguration
import fr.gemsofrod.encyclopedie.data.RingPricingResult
import fr.gemsofrod.encyclopedie.ui.components.drawRingFrontView
import fr.gemsofrod.encyclopedie.ui.components.drawRingPerspective
import fr.gemsofrod.encyclopedie.ui.components.drawRingSideView
import fr.gemsofrod.encyclopedie.ui.components.ringBandWidthMm
import fr.gemsofrod.encyclopedie.ui.components.ringOuterDiameterMm
import fr.gemsofrod.encyclopedie.ui.labelRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Fiche de simulation d'une bague (bouton « Simuler la bague » du
 * compositeur, demande de Sébastien du 14/11/2026) : vue de dessus, vue de
 * face et vue de côté cotées, composition et estimation, à partager ou à
 * enregistrer en PDF A4. Un outil de présentation, pas un devis.
 */

/** Fond des vues, repris de la palette du compositeur (vert profond). */
private val VIEW_BACKGROUND = Color(0xFF2C4134)
private val VIEW_LABEL = Color(0xFFD9CFAE)

private fun formatOneDecimal(value: Double): String = String.format(Locale.FRANCE, "%.1f", value)

private fun stoneDimensions(widthMm: Double, heightMm: Double, single: Boolean): String =
    if (single) formatOneDecimal(widthMm) else "${formatOneDecimal(widthMm)} × ${formatOneDecimal(heightMm)}"

/** Lignes (intitulé, valeur) de la composition, partagées par l'écran et le PDF. */
internal fun ringSimulationRows(context: Context, config: RingConfiguration, pricing: RingPricingResult): List<Pair<String, String>> {
    val c = config.central
    val rows = mutableListOf<Pair<String, String>>()
    rows += context.getString(R.string.ring_sim_row_metal) to context.getString(
        R.string.ring_sim_metal_value, context.getString(config.metal.labelRes), formatOneDecimal(config.gramsMetal)
    )
    rows += context.getString(R.string.ring_sim_row_central) to context.getString(
        R.string.ring_sim_central_value,
        context.getString(c.species.labelRes),
        context.getString(c.cut.labelRes),
        stoneDimensions(c.widthMm, c.heightMm, c.cut.isSingleDimension)
    )
    rows += context.getString(R.string.ring_field_orientation_label) to "${c.orientationDeg.roundToInt()}°"
    rows += context.getString(R.string.ring_sim_row_central_setting) to context.getString(c.sertissage.labelRes)
    val side = config.cote
    rows += context.getString(R.string.ring_section_cote_title) to if (side.nombre > 0) {
        context.getString(
            R.string.ring_sim_side_value,
            side.nombre * 2,
            context.getString(side.stone.species.labelRes),
            context.getString(side.stone.cut.labelRes),
            stoneDimensions(side.stone.widthMm, side.stone.heightMm, side.stone.cut.isSingleDimension),
            context.getString(side.stone.sertissage.labelRes)
        )
    } else {
        context.getString(R.string.ring_cote_none)
    }
    rows += context.getString(R.string.ring_sim_row_dimensions) to context.getString(
        R.string.ring_sim_dimensions_value,
        formatOneDecimal(ringOuterDiameterMm(config).toDouble()),
        formatOneDecimal(ringBandWidthMm(config).toDouble())
    )
    return rows
}

internal fun ringPriceRange(pricing: RingPricingResult): String {
    val currency = NumberFormat.getCurrencyInstance(Locale.FRANCE)
    return "${currency.format(pricing.rangeMinRounded)} – ${currency.format(pricing.rangeMaxRounded)}"
}

private fun renderView(widthPx: Int, heightPx: Int, block: DrawScope.() -> Unit): Bitmap {
    val image = ImageBitmap(widthPx, heightPx)
    val canvas = androidx.compose.ui.graphics.Canvas(image)
    CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, canvas, Size(widthPx.toFloat(), heightPx.toFloat())) {
        drawRect(VIEW_BACKGROUND)
        block()
    }
    return image.asAndroidBitmap()
}

/** Génère la fiche de simulation en PDF A4 (API graphiques natives d'Android). */
internal object RingSimulationPdfGenerator {
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 40f
    private const val SCALE = 3

    /** Fiche 3D (4 vues cotées) sur une page A4 paysage. */
    private fun generateFromSheet(context: Context, sheet: Bitmap, reference: String): File {
        val document = PdfDocument()
        val page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_HEIGHT, PAGE_WIDTH, 1).create())
        val canvas = page.canvas
        canvas.drawColor(android.graphics.Color.WHITE)
        val margin = 24f
        val footerH = 18f
        val availW = PAGE_HEIGHT - 2 * margin
        val availH = PAGE_WIDTH - 2 * margin - footerH
        val scale = minOf(availW / sheet.width, availH / sheet.height)
        val w = sheet.width * scale
        val h = sheet.height * scale
        val left = (PAGE_HEIGHT - w) / 2f
        canvas.drawBitmap(sheet, null, RectF(left, margin, left + w, margin + h), Paint(Paint.FILTER_BITMAP_FLAG))
        val footPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.rgb(107, 102, 90); textSize = 7.5f }
        canvas.drawText(
            context.getString(R.string.ring_sim_pdf_footer) + " Gems of Rod · gems-of-rod.fr",
            margin, PAGE_WIDTH - margin + 4f, footPaint
        )
        document.finishPage(page)
        val outDir = File(context.cacheDir, "ring_simulations").apply { mkdirs() }
        val outFile = File(outDir, "simulation-bague-$reference.pdf")
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()
        return outFile
    }

    fun generate(context: Context, config: RingConfiguration, pricing: RingPricingResult, reference: String, sheet: Bitmap? = null): File {
        if (sheet != null) return generateFromSheet(context, sheet, reference)
        val document = PdfDocument()
        val page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create())
        val canvas = page.canvas
        val gold = android.graphics.Color.rgb(122, 95, 34)
        val ink = android.graphics.Color.rgb(42, 42, 38)
        val muted = android.graphics.Color.rgb(107, 102, 90)
        val serif = Typeface.create(Typeface.SERIF, Typeface.BOLD_ITALIC)

        canvas.drawColor(android.graphics.Color.rgb(251, 248, 241))
        val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = gold; textSize = 24f; typeface = serif }
        val smallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = muted; textSize = 9.5f }
        val rightPaint = Paint(smallPaint).apply { textAlign = Paint.Align.RIGHT }
        var y = MARGIN + 20f
        canvas.drawText("Gems of Rod", MARGIN, y, brandPaint)
        canvas.drawText(context.getString(R.string.ring_sim_pdf_number, reference), PAGE_WIDTH - MARGIN, y - 10f, rightPaint)
        canvas.drawText(DateFormat.getDateInstance(DateFormat.LONG).format(Date()), PAGE_WIDTH - MARGIN, y + 3f, rightPaint)
        y += 14f
        canvas.drawText(context.getString(R.string.ring_sim_tagline), MARGIN, y, smallPaint)
        y += 8f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, Paint().apply { color = android.graphics.Color.rgb(176, 141, 60); strokeWidth = 1.5f })
        y += 24f
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ink; textSize = 16f; typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD) }
        canvas.drawText(context.getString(R.string.ring_sim_pdf_title), MARGIN, y, titlePaint)
        y += 12f

        val contentW = PAGE_WIDTH - 2 * MARGIN
        val labelTextPx = 13f * SCALE
        fun placeView(bitmap: Bitmap, left: Float, top: Float, w: Float, h: Float, caption: String) {
            val dst = RectF(left, top, left + w, top + h)
            canvas.drawBitmap(bitmap, null, dst, Paint(Paint.FILTER_BITMAP_FLAG))
            val capPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.rgb(237, 230, 210); textSize = 8.5f; textAlign = Paint.Align.CENTER; letterSpacing = 0.08f }
            canvas.drawText(caption.uppercase(Locale.getDefault()), left + w / 2f, top + h - 8f, capPaint)
        }
        val topH = 230f
        val top = renderView((contentW * SCALE).toInt(), (topH * SCALE).toInt()) { drawRingPerspective(config) }
        placeView(top, MARGIN, y, contentW, topH, context.getString(R.string.ring_view_top))
        y += topH + 8f
        val halfW = (contentW - 8f) / 2f
        val sideH = 200f
        val front = renderView((halfW * SCALE).toInt(), (sideH * SCALE).toInt()) { drawRingFrontView(config, VIEW_LABEL, labelTextPx) }
        val side = renderView((halfW * SCALE).toInt(), (sideH * SCALE).toInt()) { drawRingSideView(config, VIEW_LABEL, labelTextPx) }
        placeView(front, MARGIN, y, halfW, sideH, context.getString(R.string.ring_view_front))
        placeView(side, MARGIN + halfW + 8f, y, halfW, sideH, context.getString(R.string.ring_view_side))
        y += sideH + 26f

        val sectionPaint = Paint(titlePaint).apply { textSize = 13f }
        canvas.drawText(context.getString(R.string.ring_sim_composition), MARGIN, y, sectionPaint)
        y += 8f
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = muted; textSize = 9.5f }
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ink; textSize = 9.5f }
        val rulePaint = Paint().apply { color = android.graphics.Color.rgb(227, 220, 203); strokeWidth = 0.8f }
        for ((label, value) in ringSimulationRows(context, config, pricing)) {
            y += 16f
            canvas.drawText(label, MARGIN, y, labelPaint)
            canvas.drawText(value, MARGIN + contentW * 0.32f, y, valuePaint)
            canvas.drawLine(MARGIN, y + 5f, PAGE_WIDTH - MARGIN, y + 5f, rulePaint)
        }
        y += 26f
        canvas.drawText(context.getString(R.string.ring_sim_row_estimate), MARGIN, y, Paint(valuePaint).apply { textSize = 11.5f })
        canvas.drawText(ringPriceRange(pricing), PAGE_WIDTH - MARGIN, y, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = gold; textSize = 15f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT
        })
        y += 22f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, rulePaint)
        y += 14f
        val footer = context.getString(R.string.ring_sim_pdf_footer) + " Gems of Rod · gems-of-rod.fr"
        val footPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = muted; textSize = 8f }
        val words = footer.split(" ")
        var line = ""
        for (word in words) {
            val candidate = if (line.isEmpty()) word else "$line $word"
            if (footPaint.measureText(candidate) > contentW) {
                canvas.drawText(line, MARGIN, y, footPaint)
                y += 11f
                line = word
            } else {
                line = candidate
            }
        }
        if (line.isNotEmpty()) canvas.drawText(line, MARGIN, y, footPaint)

        document.finishPage(page)
        val outDir = File(context.cacheDir, "ring_simulations").apply { mkdirs() }
        val outFile = File(outDir, "simulation-bague-$reference.pdf")
        FileOutputStream(outFile).use { document.writeTo(it) }
        document.close()
        return outFile
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingSimulationScreen(config: RingConfiguration, pricing: RingPricingResult, onBackClick: () -> Unit) {
    BackHandler(onBack = onBackClick)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val reference = remember { "S-" + SimpleDateFormat("yyyyMMdd-HHmm", Locale.FRANCE).format(Date()) }
    val sectionTops = remember { mutableStateListOf(0, 0, 0) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var busy by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf<Bitmap?>(null) }
    var unavailable3d by remember { mutableStateOf(false) }
    val sheetLines = remember(config) {
        listOf(context.getString(R.string.ring_sim_pdf_number, reference)) +
            ringSimulationRows(context, config, pricing).map { (label, value) -> "$label : $value" } +
            "${context.getString(R.string.ring_sim_row_estimate)} : ${ringPriceRange(pricing)}"
    }
    val chooserTitle = stringResource(R.string.ring_sim_share_chooser)
    val savedMessage = stringResource(R.string.ring_sim_saved)
    val labelTextPx = with(density) { 12.dp.toPx() }

    val saveLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            withContext(Dispatchers.IO) {
                val file = RingSimulationPdfGenerator.generate(context, config, pricing, reference, sheet)
                context.contentResolver.openOutputStream(uri)?.use { out -> file.inputStream().use { it.copyTo(out) } }
            }
            Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.ring_simulation_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            val tabs = listOf(R.string.ring_view_top, R.string.ring_view_front, R.string.ring_view_side)
            if (unavailable3d) Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                tabs.forEachIndexed { index, res ->
                    FilterChip(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            scope.launch { scrollState.animateScrollTo(sectionTops[index]) }
                        },
                        label = { Text(stringResource(res), maxLines = 1) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (!unavailable3d) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECEBE7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Ring3DView(
                            config = config,
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                            sheetRequest = sheetLines,
                            onSheet = { sheet = it },
                            onUnavailable = { unavailable3d = true }
                        )
                    }
                    Text(
                        text = stringResource(R.string.ring_3d_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.ring_sim_sheet_title).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val currentSheet = sheet
                    if (currentSheet != null) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                bitmap = currentSheet.asImageBitmap(),
                                contentDescription = stringResource(R.string.ring_sim_sheet_title),
                                modifier = Modifier.fillMaxWidth().aspectRatio(currentSheet.width.toFloat() / currentSheet.height)
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.ring_sim_sheet_preparing),
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                val viewTitles = if (unavailable3d) listOf(R.string.ring_view_top, R.string.ring_view_front, R.string.ring_view_side) else emptyList()
                viewTitles.forEachIndexed { index, titleRes ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.onGloballyPositioned { sectionTops[index] = it.positionInParent().y.roundToInt() }
                    ) {
                        Text(
                            text = stringResource(titleRes).uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = VIEW_BACKGROUND),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1.3f).padding(8.dp)) {
                                when (index) {
                                    0 -> drawRingPerspective(config)
                                    1 -> drawRingFrontView(config, VIEW_LABEL, labelTextPx)
                                    else -> drawRingSideView(config, VIEW_LABEL, labelTextPx)
                                }
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.ring_sim_composition), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        ringSimulationRows(context, config, pricing).forEach { (label, value) ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.42f))
                                Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(0.58f))
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(stringResource(R.string.ring_sim_row_estimate), style = MaterialTheme.typography.bodyLarge)
                            Text(ringPriceRange(pricing), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Text(stringResource(R.string.ring_sim_pdf_footer), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (busy) return@Button
                            busy = true
                            scope.launch {
                                val file = withContext(Dispatchers.IO) { RingSimulationPdfGenerator.generate(context, config, pricing, reference, sheet) }
                                busy = false
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                                val send = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(send, chooserTitle))
                            }
                        },
                        enabled = !busy && (sheet != null || unavailable3d),
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.ring_sim_share_pdf), maxLines = 1) }
                    OutlinedButton(
                        onClick = { saveLauncher.launch("simulation-bague-$reference.pdf") },
                        enabled = sheet != null || unavailable3d,
                        modifier = Modifier.weight(1f)
                    ) { Text(stringResource(R.string.ring_sim_save_pdf), maxLines = 1) }
                }
            }
        }
    }
}
