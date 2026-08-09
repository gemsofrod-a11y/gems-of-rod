package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemImageCredit
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemDiagnostics
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.googleMapsSearchUrl
import fr.gemsofrod.encyclopedie.data.priceRangePerCarat
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import fr.gemsofrod.encyclopedie.ui.rememberDrawableResId
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GemDetailScreen(gemId: String, onBackClick: () -> Unit, onCertificateClick: () -> Unit) {
    val gem = GemsRepository.byId(gemId)?.localized()
    val context = LocalContext.current
    val removeFavoriteLabel = stringResource(R.string.cd_remove_favorite)
    val addFavoriteLabel = stringResource(R.string.cd_add_favorite)
    val shareChooserTitle = stringResource(R.string.share_chooser_title)
    val shareSubject = gem?.let { stringResource(R.string.share_subject, it.nom) }
    val familyLabel = stringResource(R.string.fiche_famille)
    val hardnessLabel = stringResource(R.string.fiche_durete)
    val refractiveIndexLabel = stringResource(R.string.fiche_indice_refraction)
    val priceLabel = stringResource(R.string.fiche_prix_indicatif)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gem?.nom ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    if (gem != null) {
                        val isFavorite = FavoritesRepository.isFavorite(gem.id)
                        IconButton(onClick = { FavoritesRepository.toggle(gem.id) }) {
                            Icon(
                                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavorite) removeFavoriteLabel else addFavoriteLabel,
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                        IconButton(onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, shareSubject)
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    buildShareText(gem, familyLabel, hardnessLabel, refractiveIndexLabel, priceLabel)
                                )
                            }
                            context.startActivity(Intent.createChooser(sendIntent, shareChooserTitle))
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = shareChooserTitle)
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
        if (gem == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }

        val images = GemImages.creditsFor(gem.id)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (images.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .background(gem.couleur.swatch, RoundedCornerShape(16.dp))
                )
            } else {
                GemImageGallery(images)
            }

            Column {
                Text(
                    text = gem.nom,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = gem.nomLatin,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RareteBadge(rarete = gem.rarete)
                AssistChip(onClick = {}, label = { Text(gem.famille) })
            }

            Text(
                text = gem.descriptionLongue,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FicheRow(stringResource(R.string.fiche_categorie), stringResource(gem.couleur.labelRes))
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_famille), gem.famille)
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_formule_chimique), gem.formuleChimique)
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_systeme_cristallin), gem.systemeCristallin)
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_durete), gem.durete)
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_indice_refraction), gem.indiceRefraction)
                    val diagnostic = GemDiagnostics.data[gem.id]
                    if (diagnostic != null) {
                        FicheDivider()
                        FicheRow(stringResource(R.string.fiche_transparence), localizedLabel(diagnostic.transparence))
                        FicheDivider()
                        FicheRow(stringResource(R.string.fiche_eclat), localizedLabel(diagnostic.eclat))
                        FicheDivider()
                        FicheRow(stringResource(R.string.fiche_clivage), localizedLabel(diagnostic.clivage))
                        FicheDivider()
                        FicheRow(stringResource(R.string.fiche_densite), diagnostic.densite)
                    }
                    FicheDivider()
                    FicheRow(stringResource(R.string.fiche_prix_indicatif), gem.prixCaratEur)
                }
            }

            PriceCalculatorCard(gem)

            OutlinedButton(
                onClick = onCertificateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Description,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(stringResource(R.string.create_certificate_button))
            }

            Column {
                Text(
                    text = stringResource(R.string.particularites_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = gem.particularites,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.origins_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.origins_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val uriHandler = LocalUriHandler.current
                gem.origines.forEach { origine ->
                    OrigineRow(
                        origine = origine,
                        onClick = { uriHandler.openUri(googleMapsSearchUrl(origine)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceCalculatorCard(gem: Gem) {
    val range = gem.priceRangePerCarat() ?: return

    var caratsInput by remember(gem.id) { mutableStateOf("") }
    var prixInput by remember(gem.id) { mutableStateOf("") }

    val carats = caratsInput.replace(",", ".").toDoubleOrNull()
    val prixParCarat = prixInput.replace(",", ".").toDoubleOrNull()
    val depasseLeMaximum = prixParCarat != null && prixParCarat > range.maxEur
    val valeurs = carats != null && carats > 0 && prixParCarat != null && prixParCarat > 0 && !depasseLeMaximum
    val currency = remember { NumberFormat.getCurrencyInstance(Locale.FRANCE) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.price_calc_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.price_calc_intro, formatEur(range.maxEur)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp, top = 2.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = caratsInput,
                    onValueChange = { caratsInput = it },
                    label = { Text(stringResource(R.string.price_calc_carats_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = prixInput,
                    onValueChange = { prixInput = it },
                    label = { Text(stringResource(R.string.price_calc_price_label)) },
                    singleLine = true,
                    isError = depasseLeMaximum,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
            }
            if (depasseLeMaximum) {
                Text(
                    text = stringResource(R.string.price_calc_exceeds_max, formatEur(range.maxEur)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Button(
                onClick = {},
                enabled = valeurs,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    if (valeurs && carats != null && prixParCarat != null) {
                        stringResource(R.string.price_calc_result, currency.format(carats * prixParCarat))
                    } else {
                        stringResource(R.string.price_calc_button)
                    }
                )
            }
        }
    }
}

private fun buildShareText(
    gem: Gem,
    familyLabel: String,
    hardnessLabel: String,
    refractiveIndexLabel: String,
    priceLabel: String
): String = buildString {
    appendLine(gem.nom)
    appendLine(gem.nomLatin)
    appendLine()
    appendLine(gem.descriptionCourte)
    appendLine()
    appendLine("$familyLabel : ${gem.famille}")
    appendLine("$hardnessLabel : ${gem.durete}")
    appendLine("$refractiveIndexLabel : ${gem.indiceRefraction}")
    appendLine("$priceLabel : ${gem.prixCaratEur}")
    appendLine()
    append("Gems of Rod — gemsofrod@gmail.com")
}

private fun formatEur(value: Double): String {
    val rounded = value.toInt()
    return "${rounded} €".replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1 ")
}

@Composable
private fun GemImageGallery(images: List<GemImageCredit>) {
    val ordered = images.sortedBy { if (it.type == GemImageType.BRUTE) 0 else 1 }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ordered) { credit ->
            GemImageCard(credit)
        }
    }
}

@Composable
private fun GemImageCard(credit: GemImageCredit) {
    val imageResId = rememberDrawableResId(credit.drawableName)
    if (imageResId == 0) return

    val caption = stringResource(if (credit.type == GemImageType.BRUTE) R.string.gem_photo_raw else R.string.gem_photo_cut)
    Column(modifier = Modifier.width(220.dp)) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = caption,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = stringResource(R.string.photo_credit, credit.author, credit.license),
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RareteBadge(rarete: GemRarete) {
    val (bg, fg) = when (rarete) {
        GemRarete.COURANTE -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        GemRarete.PEU_COMMUNE -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        GemRarete.RARE -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f) to MaterialTheme.colorScheme.primary
        GemRarete.EXCEPTIONNELLE -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
    }
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Text(
            text = stringResource(rarete.labelRes),
            style = MaterialTheme.typography.labelLarge,
            color = fg,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun OrigineRow(origine: String, onClick: () -> Unit) {
    Text(
        text = "📍 $origine",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun FicheRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.42f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.58f)
        )
    }
}

@Composable
private fun FicheDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.background
    )
}
