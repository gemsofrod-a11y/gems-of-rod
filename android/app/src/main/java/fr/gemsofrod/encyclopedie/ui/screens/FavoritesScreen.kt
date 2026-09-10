package fr.gemsofrod.encyclopedie.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.CoquillagesRepository
import fr.gemsofrod.encyclopedie.data.FavoriteCategory
import fr.gemsofrod.encyclopedie.data.FavoritesPdfGenerator
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.data.FossilesRepository
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.MeteoritesRepository
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Une ligne de la liste unifiée des favoris, toutes catégories confondues. */
private data class FavoriteEntry(
    val id: String,
    val title: String,
    val subtitle: String,
    val swatchColor: Color? = null,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onGemClick: (Gem) -> Unit,
    onFossileClick: (String) -> Unit,
    onCoquillageClick: (String) -> Unit,
    onMeteoriteClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val gems = FavoritesRepository.favoriteIds(FavoriteCategory.GEM).mapNotNull { GemsRepository.byId(it) }
    val localizedGems = gems.map { it.localized() }
    val fossileLabel = stringResource(R.string.home_fossiles_title)
    val coquillageLabel = stringResource(R.string.home_coquillages_title)
    val meteoriteLabel = stringResource(R.string.home_meteorites_title)
    val entries = localizedGems.map { gem ->
        FavoriteEntry(gem.id, gem.nom, stringResource(gem.couleur.labelRes), gem.couleur.swatch) { onGemClick(gem) }
    } + FavoritesRepository.favoriteIds(FavoriteCategory.FOSSILE).mapNotNull { FossilesRepository.byId(it) }.map { it.localized() }.map { fossile ->
        FavoriteEntry(fossile.id, fossile.nom, fossileLabel) { onFossileClick(fossile.id) }
    } + FavoritesRepository.favoriteIds(FavoriteCategory.COQUILLAGE).mapNotNull { CoquillagesRepository.byId(it) }.map { it.localized() }.map { coquillage ->
        FavoriteEntry(coquillage.id, coquillage.nom, coquillageLabel) { onCoquillageClick(coquillage.id) }
    } + FavoritesRepository.favoriteIds(FavoriteCategory.METEORITE).mapNotNull { MeteoritesRepository.byId(it) }.map { it.localized() }.map { meteorite ->
        FavoriteEntry(meteorite.id, meteorite.nom, meteoriteLabel) { onMeteoriteClick(meteorite.id) }
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }
    val exportChooserTitle = stringResource(R.string.favorites_export_chooser_title)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isExporting || localizedGems.isEmpty()) return@IconButton
                            isExporting = true
                            scope.launch {
                                val file = withContext(Dispatchers.IO) {
                                    FavoritesPdfGenerator.generate(context, localizedGems)
                                }
                                isExporting = false
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(sendIntent, exportChooserTitle))
                            }
                        },
                        enabled = !isExporting && localizedGems.isNotEmpty()
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Filled.IosShare, contentDescription = stringResource(R.string.favorites_export_button))
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
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = stringResource(R.string.favorites_empty_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.favorites_empty_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            var query by remember { mutableStateOf("") }
            val displayedEntries = if (query.isBlank()) entries else entries.filter { it.title.contains(query, ignoreCase = true) }

            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                CatalogSearchField(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = stringResource(R.string.catalog_search_placeholder)
                )
                if (query.isNotBlank() && displayedEntries.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Text(
                            text = stringResource(R.string.catalog_search_no_results, query),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayedEntries, key = { it.id }) { entry ->
                            FavoriteRow(entry = entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteRow(entry: FavoriteEntry) {
    Card(
        onClick = entry.onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val thumbnailCredit = GemImages.creditsFor(entry.id).let { credits ->
                credits.firstOrNull { it.type == GemImageType.FACETTEE } ?: credits.firstOrNull()
            }
            val imagePainter = rememberSampledDrawablePainter(thumbnailCredit?.drawableName, 48.dp)
            if (imagePainter != null) {
                Image(
                    painter = imagePainter,
                    contentDescription = entry.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(entry.swatchColor ?: MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = entry.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
