package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.GemLocalization
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.StockItem
import fr.gemsofrod.encyclopedie.data.StockPhotoStorage
import fr.gemsofrod.encyclopedie.data.StockRepository
import fr.gemsofrod.encyclopedie.data.StockStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

/** Normalise la virgule décimale (clavier français) avant analyse — voir la leçon du réflectomètre. */
private fun parseDecimalOrNull(input: String): Double? = input.trim().replace(',', '.').toDoubleOrNull()

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockFormScreen(itemId: String?, onSaveComplete: () -> Unit, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val existing = remember(itemId) { itemId?.let { StockRepository.itemById(it) } }

    var nom by remember { mutableStateOf(existing?.nom ?: "") }
    var catalogGemId by remember { mutableStateOf(existing?.catalogGemId) }
    var poidsCarats by remember { mutableStateOf(existing?.poidsCarats?.toString() ?: "") }
    var taille by remember { mutableStateOf(existing?.taille ?: "") }
    var couleur by remember { mutableStateOf(existing?.couleur ?: "") }
    var purete by remember { mutableStateOf(existing?.purete ?: "") }
    var traitement by remember { mutableStateOf(existing?.traitement ?: "") }
    var certificatNumero by remember { mutableStateOf(existing?.certificatNumero ?: "") }
    var laboratoire by remember { mutableStateOf(existing?.laboratoire ?: "") }
    var fournisseur by remember { mutableStateOf(existing?.fournisseur ?: "") }
    var dateAchat by remember { mutableStateOf(existing?.dateAchatMillis?.let { dateFormat.format(it) } ?: "") }
    var coutAchat by remember { mutableStateOf(existing?.coutAchat?.toString() ?: "") }
    var prixVente by remember { mutableStateOf(existing?.prixVente?.toString() ?: "") }
    var statut by remember { mutableStateOf(existing?.statut ?: StockStatus.EN_STOCK) }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoBitmap by remember {
        mutableStateOf(StockPhotoStorage.loadSampledBitmap(context, existing?.photoFileName, 800))
    }
    var photoFileName by remember { mutableStateOf(existing?.photoFileName) }
    var photoChanged by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> photoUri = uri }

    LaunchedEffect(photoUri) {
        val uri = photoUri ?: return@LaunchedEffect
        photoBitmap = withContext(Dispatchers.IO) { runCatching { decodeSampledStockPhoto(context, uri) }.getOrNull() }
        photoChanged = true
    }

    val languageCode = LocalConfiguration.current.locales[0].language
    val catalogGems = remember(languageCode) {
        GemsRepository.gems.map { GemLocalization.localize(it, languageCode) }
    }
    val suggestions = remember(nom, catalogGems) {
        if (nom.length < 2) emptyList()
        else catalogGems
            .filter { it.nom.contains(nom, ignoreCase = true) }
            .take(5)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(if (existing == null) R.string.stock_add_title else R.string.stock_edit_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(
                        enabled = !isSaving,
                        onClick = {
                            isSaving = true
                            scope.launch {
                                val finalPhotoFileName = withContext(Dispatchers.IO) {
                                    if (photoChanged) {
                                        if (existing?.photoFileName != null) {
                                            StockPhotoStorage.deletePhoto(context, existing.photoFileName)
                                        }
                                        photoBitmap?.let { StockPhotoStorage.savePhoto(context, it) }
                                    } else {
                                        photoFileName
                                    }
                                }
                                val item = StockItem(
                                    id = existing?.id ?: "",
                                    nom = nom,
                                    catalogGemId = catalogGemId,
                                    poidsCarats = parseDecimalOrNull(poidsCarats),
                                    taille = taille,
                                    couleur = couleur,
                                    purete = purete,
                                    traitement = traitement,
                                    certificatNumero = certificatNumero,
                                    laboratoire = laboratoire,
                                    fournisseur = fournisseur,
                                    dateAchatMillis = runCatching { dateFormat.parse(dateAchat)?.time }.getOrNull(),
                                    coutAchat = parseDecimalOrNull(coutAchat),
                                    prixVente = parseDecimalOrNull(prixVente),
                                    statut = statut,
                                    notes = notes,
                                    photoFileName = finalPhotoFileName,
                                    createdAtMillis = existing?.createdAtMillis ?: 0L
                                )
                                if (existing == null) StockRepository.addItem(item) else StockRepository.updateItem(item)
                                isSaving = false
                                onSaveComplete()
                            }
                        }
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.height(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.stock_save_button))
                        }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StockPhotoPickerBox(bitmap = photoBitmap, onPickClick = { photoPicker.launch("image/*") })

            Column {
                StockField(
                    label = stringResource(R.string.stock_field_name_label),
                    value = nom,
                    onValueChange = { nom = it; catalogGemId = null }
                )
                if (suggestions.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Column {
                            suggestions.forEach { gem ->
                                Text(
                                    text = gem.nom,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { nom = gem.nom; catalogGemId = gem.id }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            StockField(
                label = stringResource(R.string.stock_field_weight_label),
                value = poidsCarats,
                onValueChange = { poidsCarats = it },
                keyboardType = KeyboardType.Decimal
            )
            StockField(label = stringResource(R.string.stock_field_cut_label), value = taille, onValueChange = { taille = it })
            StockField(label = stringResource(R.string.stock_field_color_label), value = couleur, onValueChange = { couleur = it })
            StockField(label = stringResource(R.string.stock_field_clarity_label), value = purete, onValueChange = { purete = it })
            StockField(label = stringResource(R.string.stock_field_treatment_label), value = traitement, onValueChange = { traitement = it })
            StockField(label = stringResource(R.string.stock_field_certificate_number_label), value = certificatNumero, onValueChange = { certificatNumero = it })
            StockField(label = stringResource(R.string.stock_field_laboratory_label), value = laboratoire, onValueChange = { laboratoire = it })
            StockField(label = stringResource(R.string.stock_field_supplier_label), value = fournisseur, onValueChange = { fournisseur = it })
            StockField(
                label = stringResource(R.string.stock_field_purchase_date_label),
                value = dateAchat,
                onValueChange = { dateAchat = it },
                placeholder = "JJ/MM/AAAA"
            )
            StockField(
                label = stringResource(R.string.stock_field_purchase_cost_label),
                value = coutAchat,
                onValueChange = { coutAchat = it },
                keyboardType = KeyboardType.Decimal
            )
            StockField(
                label = stringResource(R.string.stock_field_selling_price_label),
                value = prixVente,
                onValueChange = { prixVente = it },
                keyboardType = KeyboardType.Decimal
            )

            StockStatusSelector(selected = statut, onSelect = { statut = it })

            StockField(
                label = stringResource(R.string.stock_field_notes_label),
                value = notes,
                onValueChange = { notes = it },
                singleLine = false
            )
        }
    }
}

@Composable
private fun StockStatusSelector(selected: StockStatus, onSelect: (StockStatus) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.stock_field_status_label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StockStatus.entries.forEach { status ->
                val labelRes = when (status) {
                    StockStatus.EN_STOCK -> R.string.stock_status_en_stock
                    StockStatus.RESERVE -> R.string.stock_status_reserve
                    StockStatus.VENDU -> R.string.stock_status_vendu
                    StockStatus.CONSIGNATION -> R.string.stock_status_consignation
                }
                val isSelected = status == selected
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onSelect(status) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StockPhotoPickerBox(bitmap: Bitmap?, onPickClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onPickClick),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = stringResource(R.string.stock_photo_picker_hint),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.AddAPhoto, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = stringResource(R.string.stock_photo_picker_hint),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun StockField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

private const val MAX_STOCK_PHOTO_DIMENSION = 1600

/** Décode et sous-échantillonne la photo choisie, corrige l'orientation EXIF — même logique que le certificat. */
private fun decodeSampledStockPhoto(context: Context, uri: Uri): Bitmap? {
    val resolver = context.contentResolver

    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use { stream -> BitmapFactory.decodeStream(stream, null, bounds) }
    val width = bounds.outWidth
    val height = bounds.outHeight
    if (width <= 0 || height <= 0) return null

    var sampleSize = 1
    while (width / sampleSize > MAX_STOCK_PHOTO_DIMENSION || height / sampleSize > MAX_STOCK_PHOTO_DIMENSION) {
        sampleSize *= 2
    }

    val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    val bitmap = resolver.openInputStream(uri)?.use { stream -> BitmapFactory.decodeStream(stream, null, decodeOptions) } ?: return null

    val orientation = runCatching {
        resolver.openInputStream(uri)?.use { stream ->
            ExifInterface(stream).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        }
    }.getOrNull() ?: ExifInterface.ORIENTATION_NORMAL

    val rotationDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
    if (rotationDegrees == 0) return bitmap

    val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
    val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    if (rotated !== bitmap) bitmap.recycle()
    return rotated
}
