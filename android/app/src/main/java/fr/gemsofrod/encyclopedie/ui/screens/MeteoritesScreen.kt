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
import androidx.compose.foundation.layout.height
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
import fr.gemsofrod.encyclopedie.data.GemImageCredit
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemRarete
import fr.gemsofrod.encyclopedie.data.Meteorite
import fr.gemsofrod.encyclopedie.data.MeteoriteClassificationInfo
import fr.gemsofrod.encyclopedie.data.MeteoriteFamille
import fr.gemsofrod.encyclopedie.data.MeteoriteFamilyExplainer
import fr.gemsofrod.encyclopedie.data.MeteoritesRepository
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField
import fr.gemsofrod.encyclopedie.ui.components.premiumCardBorder
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.rememberSampledDrawablePainter

/**
 * Écran d'entrée de la section Météorites, distincte du catalogue Gemmologie :
 * intro, accès à la page de classification, puis la liste des météorites
 * regroupées par grande famille (ferreuses, pallasites, chondrites, pièces de
 * prestige scientifique, martiennes/lunaires).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeteoritesMenuScreen(
    onClassificationClick: () -> Unit,
    onMeteoriteClick: (Meteorite) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_meteorites_title)) },
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
                        text = stringResource(R.string.meteorites_intro),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    ClassificationCard(onClick = onClassificationClick)
                }

                MeteoriteFamille.entries.forEach { famille ->
                    val meteorites = MeteoritesRepository.byFamille(famille)
                        .map { it to it.localized() }
                        .filter { (_, loc) -> query.isBlank() || loc.nom.contains(query, ignoreCase = true) }
                    if (meteorites.isNotEmpty()) {
                        anyResults = true
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = stringResource(famille.labelRes),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            meteorites.forEach { (meteorite, localizedMeteorite) ->
                                MeteoriteRow(meteorite = localizedMeteorite, onClick = { onMeteoriteClick(meteorite) })
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
private fun ClassificationCard(onClick: () -> Unit) {
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
                    text = stringResource(R.string.meteorites_classification_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.meteorites_classification_subtitle),
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
private fun MeteoriteRow(meteorite: Meteorite, onClick: () -> Unit) {
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
                    text = meteorite.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = meteorite.descriptionCourte,
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

/** Page éditoriale de classification générale des météorites (sidérites, sidérolithes, aérolithes). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeteoriteClassificationScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = MeteoriteClassificationInfo.page(languageCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.meteorites_classification_title)) },
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

            page.familles.forEach { famille -> FamilyExplainerCard(famille) }

            ClassificationDisclaimer(title = page.disclaimerTitle, body = page.disclaimerBody)
        }
    }
}

@Composable
private fun FamilyExplainerCard(famille: MeteoriteFamilyExplainer) {
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
private fun ClassificationDisclaimer(title: String, body: String) {
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

/** Fiche détaillée d'une météorite individuelle. */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MeteoriteDetailScreen(meteoriteId: String, onBackClick: () -> Unit) {
    val meteoriteIds = remember { MeteoritesRepository.all().map { it.id } }
    val initialPage = remember(meteoriteId) { meteoriteIds.indexOf(meteoriteId).coerceAtLeast(0) }
    val pagerState = rememberPagerState(initialPage = initialPage) { meteoriteIds.size }
    val currentMeteoriteId = meteoriteIds.getOrNull(pagerState.currentPage) ?: meteoriteId
    val meteorite = MeteoritesRepository.byId(currentMeteoriteId)?.localized()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(meteorite?.nom ?: "") },
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
        if (meteoriteIds.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) { page ->
            val pageMeteorite = MeteoritesRepository.byId(meteoriteIds[page])?.localized()
            if (pageMeteorite == null) {
                Box(modifier = Modifier.fillMaxSize())
            } else {
                MeteoriteDetailBody(pageMeteorite)
            }
        }
    }
}

@Composable
private fun MeteoriteDetailBody(meteorite: Meteorite) {
    val images = GemImages.creditsFor(meteorite.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (images.isNotEmpty()) {
            MeteoriteImageGallery(images)
        }

        Column {
            Text(
                text = meteorite.nom,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = meteorite.origine,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RareteBadge(rarete = meteorite.rarete)
            AssistChip(onClick = {}, label = { Text(stringResource(meteorite.famille.labelRes)) })
        }

        Text(
            text = meteorite.descriptionLongue,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FicheRow(stringResource(R.string.meteorite_fiche_origine), meteorite.origine)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_classification), meteorite.classification)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_composition), meteorite.compositionMinerale)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_durete), meteorite.durete)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_densite), meteorite.densite)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_couleur), meteorite.couleur)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_taille), meteorite.taillePossible)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_qualite_gemme), meteorite.qualiteGemme)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_interet_joaillerie), meteorite.interetJoaillerie)
                FicheDivider()
                FicheRow(stringResource(R.string.meteorite_fiche_prix), meteorite.prixApproxGramme)
            }
        }
    }
}

@Composable
private fun MeteoriteImageGallery(images: List<GemImageCredit>) {
    val ordered = images.sortedBy { if (it.type == GemImageType.BRUTE) 0 else 1 }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ordered) { credit ->
            MeteoriteImageCard(credit)
        }
    }
}

@Composable
private fun MeteoriteImageCard(credit: GemImageCredit) {
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
