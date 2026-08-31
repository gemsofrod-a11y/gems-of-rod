package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.LanguageRepository
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGemmologieClick: () -> Unit,
    onFamillesClick: () -> Unit,
    onLithotherapieClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onMeteoritesClick: () -> Unit,
    onFossilesClick: () -> Unit,
    onCoquillagesClick: () -> Unit,
    onQuizClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onLabClick: () -> Unit,
    onLapidaireClick: () -> Unit
) {
    val context = LocalContext.current
    val currentLanguage = remember { LanguageRepository.getLanguage(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gems of Rod") },
                actions = {
                    LanguageButton(flagEmoji = currentLanguage.flagEmoji, onClick = onLanguageClick)
                    IconButton(onClick = onAchievementsClick) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = stringResource(R.string.achievements_title))
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.favorites_title))
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeSectionCard(
                title = stringResource(R.string.home_gemmologie_title),
                subtitle = stringResource(R.string.home_gemmologie_subtitle),
                onClick = onGemmologieClick,
                backgroundDrawable = "gem_saphir_bleu"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_lapidaire_title),
                subtitle = stringResource(R.string.home_lapidaire_subtitle),
                onClick = onLapidaireClick,
                backgroundDrawable = "lapidaire_machine_facettage_moderne"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_lab_title),
                subtitle = stringResource(R.string.home_lab_subtitle),
                onClick = onLabClick,
                backgroundDrawable = "gem_quartz_rutile_inclusion"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_familles_title),
                subtitle = stringResource(R.string.home_familles_subtitle),
                onClick = onFamillesClick,
                backgroundDrawable = "gem_citrine"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_lithotherapie_title),
                subtitle = stringResource(R.string.home_lithotherapie_subtitle),
                onClick = onLithotherapieClick,
                backgroundDrawable = "gem_amethyste_brute"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_meteorites_title),
                subtitle = stringResource(R.string.home_meteorites_subtitle),
                onClick = onMeteoritesClick,
                backgroundDrawable = "gem_meteorite_gibeon_facette"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_fossiles_title),
                subtitle = stringResource(R.string.home_fossiles_subtitle),
                onClick = onFossilesClick,
                backgroundDrawable = "gem_fossile_ammonite_madagascar_facette"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_coquillages_title),
                subtitle = stringResource(R.string.home_coquillages_subtitle),
                onClick = onCoquillagesClick,
                backgroundDrawable = "gem_coquillage_ormeau_brute"
            )
            HomeSectionCard(
                title = stringResource(R.string.home_quiz_title),
                subtitle = stringResource(R.string.home_quiz_subtitle),
                onClick = onQuizClick,
                backgroundDrawable = "gem_opale_de_feu"
            )
        }
    }
}

@Composable
private fun LanguageButton(flagEmoji: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text = flagEmoji, fontSize = 20.sp)
        Text(
            text = stringResource(R.string.home_language_title),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HomeSectionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    backgroundDrawable: String?,
    leadingEmoji: String? = null
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val imagePainter = backgroundDrawable?.let { rememberSampledDrawablePainter(it, 400.dp) }
            if (imagePainter != null) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.78f),
                                    Color.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (leadingEmoji != null) {
                    Text(text = leadingEmoji, fontSize = 40.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Écran d'entrée de la Gemmologie : recherche globale, et choix du mode de
 * classement du catalogue (couleur ou pays d'origine).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GemmologieMenuScreen(
    onCouleurClick: () -> Unit,
    onPaysClick: () -> Unit,
    onAnalyseClick: () -> Unit,
    onComparerClick: () -> Unit,
    onLapidaireClick: () -> Unit,
    onGemClick: (Gem) -> Unit,
    onBackClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val results = if (query.isBlank()) emptyList() else GemsRepository.search(query)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_gemmologie_title)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_gems_placeholder)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.cd_clear))
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            if (query.isBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GemmologieMenuCard(
                        title = stringResource(R.string.gemmologie_couleur_title),
                        subtitle = stringResource(R.string.gemmologie_couleur_subtitle),
                        onClick = onCouleurClick
                    )
                    GemmologieMenuCard(
                        title = stringResource(R.string.gemmologie_pays_title),
                        subtitle = stringResource(R.string.gemmologie_pays_subtitle),
                        onClick = onPaysClick
                    )
                    GemmologieMenuCard(
                        title = stringResource(R.string.gemmologie_analyse_title),
                        subtitle = stringResource(R.string.gemmologie_analyse_subtitle),
                        onClick = onAnalyseClick
                    )
                    GemmologieMenuCard(
                        title = stringResource(R.string.gemmologie_comparer_title),
                        subtitle = stringResource(R.string.gemmologie_comparer_subtitle),
                        onClick = onComparerClick
                    )
                    GemmologieMenuCard(
                        title = stringResource(R.string.gemmologie_lapidaire_title),
                        subtitle = stringResource(R.string.gemmologie_lapidaire_subtitle),
                        onClick = onLapidaireClick
                    )
                }
            } else if (results.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(results) { gem ->
                        SearchResultRow(gem = gem, onClick = { onGemClick(gem) })
                    }
                }
            }
        }
    }
}

@Composable
private fun GemmologieMenuCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
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

/** Grille des catégories de couleur, atteinte depuis le bouton "Couleur" de la Gemmologie. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorListScreen(onCategoryClick: (GemColorCategory) -> Unit, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.gemmologie_couleur_title)) },
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(GemColorCategory.entries) { category ->
                CategoryCard(
                    category = category,
                    count = GemsRepository.countByColor(category),
                    onClick = { onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultRow(gem: Gem, onClick: () -> Unit) {
    val localizedGem = gem.localized()
    Card(
        onClick = onClick,
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
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(gem.couleur.swatch, CircleShape)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = localizedGem.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(gem.couleur.labelRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(category: GemColorCategory, count: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(category.swatch)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(category.labelRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = pluralStringResource(R.plurals.gem_count, count, count),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
