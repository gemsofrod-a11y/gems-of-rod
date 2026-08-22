package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Bienfaits
import fr.gemsofrod.encyclopedie.data.Chakras
import fr.gemsofrod.encyclopedie.data.FavoritesRepository
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemImageCredit
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.data.LithotherapieInfo
import fr.gemsofrod.encyclopedie.data.ZodiacSigns
import fr.gemsofrod.encyclopedie.data.BirthMonths
import fr.gemsofrod.encyclopedie.ui.localized
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import fr.gemsofrod.encyclopedie.ui.rememberDrawableResId

/** Identifiants des sous-catégories de la section Lithothérapie. */
object LithotherapieSchemes {
    const val ZODIAC = "zodiac"
    const val CHAKRA = "chakra"
    const val BIENFAIT = "bienfait"
    const val MOIS = "mois"

    @Composable
    fun title(scheme: String): String = when (scheme) {
        ZODIAC -> stringResource(R.string.scheme_zodiac_title)
        CHAKRA -> stringResource(R.string.scheme_chakra_title)
        BIENFAIT -> stringResource(R.string.scheme_bienfait_title)
        MOIS -> stringResource(R.string.scheme_mois_title)
        else -> stringResource(R.string.home_lithotherapie_title)
    }

    fun groups(scheme: String): List<Pair<String, List<Gem>>> = when (scheme) {
        ZODIAC -> ZodiacSigns.groups()
        CHAKRA -> Chakras.groups()
        BIENFAIT -> Bienfaits.groups()
        MOIS -> BirthMonths.groups()
        else -> emptyList()
    }

    fun gemsFor(scheme: String, label: String): List<Gem> = when (scheme) {
        ZODIAC -> ZodiacSigns.gemsFor(label)
        CHAKRA -> Chakras.gemsFor(label)
        BIENFAIT -> Bienfaits.gemsFor(label)
        MOIS -> BirthMonths.gemsFor(label)
        else -> emptyList()
    }
}

/**
 * Menu d'entrée de la section Lithothérapie : choix de la sous-catégorie
 * (signe astrologique, chakra, bienfait recherché, mois de naissance).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieMenuScreen(
    onSchemeClick: (String) -> Unit,
    onInfoClick: (String) -> Unit,
    onAssociationsClick: () -> Unit,
    onAllGemsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_lithotherapie_title)) },
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                SchemeCard(
                    title = stringResource(R.string.litho_all_title),
                    subtitle = stringResource(R.string.litho_all_subtitle),
                    onClick = onAllGemsClick
                )
            }
            items(
                listOf(
                    LithotherapieSchemes.ZODIAC to R.string.litho_zodiac_subtitle,
                    LithotherapieSchemes.CHAKRA to R.string.litho_chakra_subtitle,
                    LithotherapieSchemes.BIENFAIT to R.string.litho_bienfait_subtitle,
                    LithotherapieSchemes.MOIS to R.string.litho_mois_subtitle
                )
            ) { (scheme, subtitleRes) ->
                SchemeCard(
                    title = LithotherapieSchemes.title(scheme),
                    subtitle = stringResource(subtitleRes),
                    onClick = { onSchemeClick(scheme) }
                )
            }
            item {
                SchemeCard(
                    title = stringResource(R.string.litho_nettoyage_title),
                    subtitle = stringResource(R.string.litho_nettoyage_subtitle),
                    onClick = { onInfoClick(LithotherapieInfo.NETTOYAGE_RECHARGEMENT) }
                )
            }
            item {
                SchemeCard(
                    title = stringResource(R.string.litho_associations_title),
                    subtitle = stringResource(R.string.litho_associations_subtitle),
                    onClick = onAssociationsClick
                )
            }
        }
    }
}

@Composable
private fun SchemeCard(title: String, subtitle: String, onClick: () -> Unit) {
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

/** Liste des libellés (signes, chakras, bienfaits ou mois) d'une sous-catégorie. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieLabelListScreen(
    scheme: String,
    onLabelClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val groups = LithotherapieSchemes.groups(scheme)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LithotherapieSchemes.title(scheme)) },
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(groups) { (label, gems) ->
                FamilyStyleRow(name = localizedLabel(label), count = gems.size, onClick = { onLabelClick(label) })
            }
        }
    }
}

@Composable
private fun FamilyStyleRow(name: String, count: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pluralStringResource(R.plurals.gem_count, count, count),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Gemmes appartenant à un libellé donné (ex. "Bélier", "Chakra racine"). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieGemsScreen(
    scheme: String,
    label: String,
    onGemClick: (Gem) -> Unit,
    onBackClick: () -> Unit
) {
    val gems = LithotherapieSchemes.gemsFor(scheme, label)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localizedLabel(label)) },
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
        if (gems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Text(
                    text = stringResource(R.string.litho_no_gems),
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(gems) { gem ->
                    LithotherapieRow(gem = gem, onClick = { onGemClick(gem) })
                }
            }
        }
    }
}

/** Toutes les gemmes du catalogue avec leurs vertus en lithothérapie, triées par nom localisé. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieAllGemsScreen(
    onGemClick: (Gem) -> Unit,
    onBackClick: () -> Unit
) {
    val gems = GemsRepository.gems.map { it.localized() }.sortedBy { it.nom }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.litho_all_title)) },
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(gems) { gem ->
                LithotherapieRow(gem = gem, onClick = { onGemClick(gem) })
            }
        }
    }
}

@Composable
private fun LithotherapieRow(gem: Gem, onClick: () -> Unit) {
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
            val thumbnailCredit = GemImages.creditsFor(gem.id).let { credits ->
                credits.firstOrNull { it.type == GemImageType.FACETTEE } ?: credits.firstOrNull()
            }
            val imageResId = rememberDrawableResId(thumbnailCredit?.drawableName)
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = localizedGem.nom,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(gem.couleur.swatch, CircleShape)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = localizedGem.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = localizedGem.lithotherapie,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
            val isFavorite = FavoritesRepository.isFavorite(gem.id)
            IconButton(onClick = { FavoritesRepository.toggle(gem.id) }) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(if (isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite),
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieDetailScreen(gemId: String, onBackClick: () -> Unit) {
    val gem = GemsRepository.byId(gemId)?.localized()

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
                                contentDescription = stringResource(if (isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite),
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
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
                LithotherapieImageGallery(images)
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

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.litho_virtues_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = gem.lithotherapie,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = stringResource(R.string.litho_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LithotherapieImageGallery(images: List<GemImageCredit>) {
    val ordered = images.sortedBy { if (it.type == GemImageType.BRUTE) 0 else 1 }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(ordered) { credit ->
            LithotherapieImageCard(credit)
        }
    }
}

@Composable
private fun LithotherapieImageCard(credit: GemImageCredit) {
    val imageResId = rememberDrawableResId(credit.drawableName)
    if (imageResId == 0) return

    val caption = stringResource(if (credit.type == GemImageType.BRUTE) R.string.gem_photo_raw else R.string.gem_photo_cut_or_cabochon)
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
    }
}
