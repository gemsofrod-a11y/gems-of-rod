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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.data.Gem
import fr.gemsofrod.encyclopedie.data.GemImageCredit
import fr.gemsofrod.encyclopedie.data.GemImageType
import fr.gemsofrod.encyclopedie.data.GemImages
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.ui.rememberDrawableResId

/**
 * Liste de toutes les gemmes présentée sous l'angle lithothérapie (croyances
 * et usages traditionnels), sans son contenu gemmologique technique — voir
 * [LithotherapieDetailScreen] pour la fiche associée à chaque pierre.
 */
@Composable
fun LithotherapieList(onGemClick: (Gem) -> Unit) {
    val gems = GemsRepository.gems

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(gems) { gem ->
            LithotherapieRow(gem = gem, onClick = { onGemClick(gem) })
        }
    }
}

@Composable
private fun LithotherapieRow(gem: Gem, onClick: () -> Unit) {
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
                    contentDescription = gem.nom,
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
                    text = gem.nom,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = gem.lithotherapie,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LithotherapieDetailScreen(gemId: String, onBackClick: () -> Unit) {
    val gem = GemsRepository.byId(gemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gem?.nom ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
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
                        text = "Vertus en lithothérapie",
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
                text = "Ces informations relèvent de croyances et d'usages traditionnels. Elles ne constituent " +
                    "ni un avis médical, ni une allégation thérapeutique, et ne remplacent pas une consultation " +
                    "auprès d'un professionnel de santé.",
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

    Column(modifier = Modifier.width(220.dp)) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = if (credit.type == GemImageType.BRUTE) "Pierre brute" else "Pierre taillée / cabochon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = if (credit.type == GemImageType.BRUTE) "Pierre brute" else "Pierre taillée / cabochon",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
