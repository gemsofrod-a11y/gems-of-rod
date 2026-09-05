package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import fr.gemsofrod.encyclopedie.data.Fossile
import fr.gemsofrod.encyclopedie.data.FossileClassificationInfo
import fr.gemsofrod.encyclopedie.data.FossileFamille
import fr.gemsofrod.encyclopedie.data.FossileFamilyExplainer
import fr.gemsofrod.encyclopedie.data.FossilesRepository
import fr.gemsofrod.encyclopedie.data.GemImageCredit
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.components.premiumCardBorder
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter

/**
 * Écran d'entrée de la section Fossiles, distincte du catalogue Gemmologie et
 * de la section Météorites : intro, accès à la page de classification, puis
 * la liste des fossiles regroupés par grande famille (ammonites & mollusques,
 * trilobites & arthropodes, vertébrés, végétaux, ambre & insectes, coraux &
 * échinodermes).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FossilesMenuScreen(
    onClassificationClick: () -> Unit,
    onFossileClick: (Fossile) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_fossiles_title)) },
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
        var query by remember { mutableStateOf("") }
        var anyResults = false

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CatalogSearchField(
                query = query,
                onQueryChange = { query = it },
                placeholder = stringResource(R.string.catalog_search_placeholder)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                if (query.isBlank()) {
                    Text(
                        text = stringResource(R.string.fossiles_intro),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    FossilesClassificationCard(onClick = onClassificationClick)
                }

                FossileFamille.entries.forEach { famille ->
                    val fossiles = FossilesRepository.byFamille(famille)
                        .map { it to it.localized() }
                        .filter { (_, loc) -> query.isBlank() || loc.nom.contains(query, ignoreCase = true) }
                    if (fossiles.isNotEmpty()) {
                        anyResults = true
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = stringResource(famille.labelRes),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            fossiles.forEach { (fossile, localizedFossile) ->
                                FossileRow(fossile = localizedFossile, onClick = { onFossileClick(fossile) })
                            }
                        }
                    }
                }

                if (query.isNotBlank() && !anyResults) {
                    Text(
                        text = stringResource(R.string.catalog_search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FossilesClassificationCard(onClick: () -> Unit) {
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
                    text = stringResource(R.string.fossiles_classification_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.fossiles_classification_subtitle),
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

@Composable
private fun FossileRow(fossile: Fossile, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .premiumCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fossile.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = fossile.descriptionCourte,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
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

/** Page éditoriale de classification générale des fossiles (ammonites, trilobites, vertébrés...). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FossileClassificationScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = FossileClassificationInfo.page(languageCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.fossiles_classification_title)) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = page.intro,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            page.familles.forEach { famille -> FossileFamilyExplainerCard(famille) }

            FossileClassificationDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
        }
    }
}

@Composable
private fun FossileFamilyExplainerCard(famille: FossileFamilyExplainer) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = famille.nom,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = famille.sousTypes,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = famille.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FossileClassificationDisclaimer(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

/** Fiche détaillée d'un fossile individuel. */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FossileDetailScreen(fossileId: String, onBackClick: () -> Unit) {
    val fossileIds = remember { FossilesRepository.all().map { it.id } }
    val initialPage = remember(fossileId) { fossileIds.indexOf(fossileId).coerceAtLeast(0) }
    val pagerState = rememberPagerState(initialPage = initialPage) { fossileIds.size }
    val currentFossileId = fossileIds.getOrNull(pagerState.currentPage) ?: fossileId
    val fossile = FossilesRepository.byId(currentFossileId)?.localized()
    LaunchedEffect(currentFossileId) { AchievementsRepository.recordFossileViewed(currentFossileId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fossile?.nom ?: "") },
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
        if (fossileIds.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) { page ->
            val pageFossile = FossilesRepository.byId(fossileIds[page])?.localized()
            if (pageFossile == null) {
                Box(modifier = Modifier.fillMaxSize())
            } else {
                FossileDetailBody(pageFossile)
            }
        }
    }
}

@Composable
private fun FossileDetailBody(fossile: Fossile) {
    val images = GemImages.creditsFor(fossile.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (images.isNotEmpty()) {
            FossileImageGallery(images)
        }

        Column {
            Text(
                text = fossile.nom,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = fossile.origine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RareteBadge(rarete = fossile.rarete)
            AssistChip(onClick = {}, label = { Text(stringResource(fossile.famille.labelRes)) })
        }

        Text(
            text = fossile.descriptionLongue,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FossileFicheRow(stringResource(R.string.fossile_fiche_origine), fossile.origine)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_periode), fossile.periodeGeologique)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_age), fossile.ageApprox)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_classification), fossile.classification)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_composition), fossile.compositionMinerale)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_durete), fossile.durete)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_densite), fossile.densite)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_couleur), fossile.couleur)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_taille), fossile.taillePossible)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_qualite_gemme), fossile.qualiteGemme)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_interet_joaillerie), fossile.interetJoaillerie)
                FossileFicheDivider()
                FossileFicheRow(stringResource(R.string.fossile_fiche_prix), fossile.prixApproxGramme)
            }
        }
    }
}

@Composable
private fun FossileImageGallery(images: List<GemImageCredit>) {
    val ordered = images.sortedBy { if (it.type == GemImageType.BRUTE) 0 else 1 }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ordered) { credit ->
            FossileImageCard(credit)
        }
    }
}

@Composable
private fun FossileImageCard(credit: GemImageCredit) {
    val imagePainter = rememberSampledDrawablePainter(credit.drawableName, 220.dp) ?: return

    val caption = stringResource(if (credit.type == GemImageType.BRUTE) R.string.gem_photo_raw else R.string.gem_photo_cut)
    Column(modifier = Modifier.width(220.dp)) {
        Image(
            painter = imagePainter,
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
            text = stringResource(
                if (credit.sourceUrl.contains("wikimedia.org", ignoreCase = true)) {
                    R.string.photo_credit_wikimedia
                } else {
                    R.string.photo_credit
                },
                credit.author,
                credit.license
            ),
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
private fun FossileFicheRow(label: String, value: String) {
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
private fun FossileFicheDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.background
    )
}
