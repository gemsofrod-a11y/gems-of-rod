package fr.gemsofrod.encyclopedie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Diamond
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.BuyingGuideArticle
import fr.gemsofrod.encyclopedie.data.BuyingGuidesInfo
import fr.gemsofrod.encyclopedie.ui.components.CatalogSearchField

/**
 * Guides d'achat par pierre de qualité gemme (origine et couleur recherchées,
 * pureté et traitements courants du marché, précautions d'entretien) : ce que
 * Gems of Rod examine avant de sélectionner chaque gemme pour sa clientèle.
 * Même patron que [TreatmentsScreen] et [GlossaryScreen].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyingGuidesScreen(onBackClick: () -> Unit) {
    val languageCode = LocalConfiguration.current.locales[0].language
    val page = BuyingGuidesInfo.page(languageCode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.buying_guides_title)) },
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
        val displayedArticles = if (query.isBlank()) {
            page.articles
        } else {
            page.articles.filter { it.pierre.contains(query, ignoreCase = true) }
        }

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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (query.isBlank()) {
                    Text(
                        text = page.intro,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (query.isNotBlank() && displayedArticles.isEmpty()) {
                    Text(
                        text = stringResource(R.string.catalog_search_no_results, query),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    displayedArticles.forEach { article -> BuyingGuideCard(article) }
                }
            }
        }
    }
}

@Composable
private fun BuyingGuideCard(article: BuyingGuideArticle) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Diamond,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = article.pierre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = article.accroche,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            BuyingGuideDetailRow(
                label = stringResource(R.string.buying_guides_origin_label),
                value = article.origineCouleur
            )
            BuyingGuideDetailRow(
                label = stringResource(R.string.buying_guides_purity_label),
                value = article.puretTraitements
            )
            BuyingGuideDetailRow(
                label = stringResource(R.string.buying_guides_care_label),
                value = article.entretien
            )
        }
    }
}

@Composable
private fun BuyingGuideDetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
