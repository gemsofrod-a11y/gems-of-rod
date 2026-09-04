package fr.gemsofrod.encyclopedie.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.gemsofrod.encyclopedie.R
import fr.gemsofrod.encyclopedie.data.Achievements
import fr.gemsofrod.encyclopedie.data.AchievementsRepository
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemFamilies
import fr.gemsofrod.encyclopedie.data.GemOrigins
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.ui.labelRes
import fr.gemsofrod.encyclopedie.ui.localizedBadgeTitle
import fr.gemsofrod.encyclopedie.ui.screens.AchievementsScreen
import fr.gemsofrod.encyclopedie.ui.screens.AnalyseScreen
import fr.gemsofrod.encyclopedie.ui.screens.CertificateScreen
import fr.gemsofrod.encyclopedie.ui.screens.ColorListScreen
import fr.gemsofrod.encyclopedie.ui.screens.FamillesListScreen
import fr.gemsofrod.encyclopedie.ui.screens.FavoritesScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemComparisonScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemmologieMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.GlobalSearchScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemsListScreen
import fr.gemsofrod.encyclopedie.ui.screens.GlossaryScreen
import fr.gemsofrod.encyclopedie.ui.screens.GuidedAnalysisScreen
import fr.gemsofrod.encyclopedie.ui.screens.HomeScreen
import fr.gemsofrod.encyclopedie.ui.screens.InstrumentsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LabMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.LabNotebookDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.LabNotebookFormScreen
import fr.gemsofrod.encyclopedie.ui.screens.LabNotebookScreen
import fr.gemsofrod.encyclopedie.ui.screens.LanguageScreen
import fr.gemsofrod.encyclopedie.ui.screens.LegendaryMapScreen
import fr.gemsofrod.encyclopedie.ui.screens.LegendaryRiddleScreen
import fr.gemsofrod.encyclopedie.ui.screens.AssociationDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.AssociationsListScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieAllGemsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieGemsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieInfoScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieLabelListScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.FossileClassificationScreen
import fr.gemsofrod.encyclopedie.ui.screens.FossileDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.FossilesMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.CoquillageClassificationScreen
import fr.gemsofrod.encyclopedie.ui.screens.CoquillageDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.CoquillagesMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoriteClassificationScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoriteDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoritesMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.PaysListScreen
import fr.gemsofrod.encyclopedie.ui.screens.QuizScreen
import fr.gemsofrod.encyclopedie.ui.screens.NuancierScreen
import fr.gemsofrod.encyclopedie.ui.screens.DiamondGradingScreen
import fr.gemsofrod.encyclopedie.ui.screens.CrystalSystemsScreen
import fr.gemsofrod.encyclopedie.ui.screens.ReflectivityMeterScreen
import fr.gemsofrod.encyclopedie.ui.screens.StockDashboardScreen
import fr.gemsofrod.encyclopedie.ui.screens.StockDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.StockFormScreen
import fr.gemsofrod.encyclopedie.ui.screens.StockListScreen
import fr.gemsofrod.encyclopedie.ui.screens.StockSalesHistoryScreen
import fr.gemsofrod.encyclopedie.ui.screens.ClientDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.ClientFormScreen
import fr.gemsofrod.encyclopedie.ui.screens.ClientListScreen
import fr.gemsofrod.encyclopedie.ui.screens.LapidaireScreen
import fr.gemsofrod.encyclopedie.ui.screens.SupplierDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.SupplierFormScreen
import fr.gemsofrod.encyclopedie.ui.screens.SupplierListScreen
import fr.gemsofrod.encyclopedie.ui.screens.TreatmentsScreen
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.net.URLEncoder

private const val NAV_TRANSITION_MS = 360
private val NAV_TRANSITION_ORIGIN = TransformOrigin(0.82f, 0.18f)

private object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val COULEUR_LIST = "couleur_list"
    const val GEMS_LIST = "gems/{colorName}"
    const val PAYS_LIST = "pays_list"
    const val PAYS_DETAIL = "pays/{country}"
    const val ANALYSE = "analyse"
    const val COMPARER = "comparer"
    const val INSTRUMENTS = "instruments"
    const val GLOSSAIRE = "glossaire"
    const val GEM_DETAIL = "gem/{gemId}"
    const val FAVORITES = "favorites"
    const val CERTIFICATE = "certificate/{gemId}"
    const val FAMILLES_LIST = "familles"
    const val FAMILLE_DETAIL = "familles/{familySlug}"
    const val LITHOTHERAPIE_MENU = "lithotherapie_menu"
    const val LITHOTHERAPIE_ALL = "lithotherapie_all"
    const val LITHOTHERAPIE_LABELS = "lithotherapie_labels/{scheme}"
    const val LITHOTHERAPIE_GEMS = "lithotherapie_gems/{scheme}/{label}"
    const val LITHOTHERAPIE_DETAIL = "lithotherapie_detail/{gemId}"
    const val LITHOTHERAPIE_INFO = "lithotherapie_info/{topic}"
    const val ASSOCIATIONS_LIST = "associations_list"
    const val ASSOCIATION_DETAIL = "association/{associationId}"
    const val LANGUAGE = "language"
    const val METEORITES = "meteorites"
    const val METEORITE_CLASSIFICATION = "meteorite_classification"
    const val METEORITE_DETAIL = "meteorite/{meteoriteId}"
    const val FOSSILES = "fossiles"
    const val FOSSILE_CLASSIFICATION = "fossile_classification"
    const val FOSSILE_DETAIL = "fossile/{fossileId}"
    const val COQUILLAGES = "coquillages"
    const val COQUILLAGE_CLASSIFICATION = "coquillage_classification"
    const val COQUILLAGE_DETAIL = "coquillage/{coquillageId}"
    const val QUIZ = "quiz"
    const val ACHIEVEMENTS = "achievements"
    const val LAB_MENU = "lab_menu"
    const val GUIDED_ANALYSE = "guided_analyse"
    const val LAB_NOTEBOOK = "lab_notebook"
    const val LAB_NOTEBOOK_NEW = "lab_notebook_new"
    const val LAB_NOTEBOOK_EDIT = "lab_notebook_edit/{sampleId}"
    const val LAB_NOTEBOOK_DETAIL = "lab_notebook_detail/{sampleId}"
    const val REFLECTIVITY_METER = "reflectivity_meter"
    const val NUANCIER = "nuancier"
    const val DIAMOND_GRADING = "diamond_grading"
    const val CRYSTAL_SYSTEMS = "crystal_systems"
    const val LEGENDARY_RIDDLE = "legendary_riddle"
    const val LEGENDARY_MAP = "legendary_map"
    const val TREATMENTS = "treatments"
    const val LAPIDAIRE = "lapidaire"
    const val STOCK_LIST = "stock_list"
    const val STOCK_NEW = "stock_new"
    const val STOCK_EDIT = "stock_edit/{itemId}"
    const val STOCK_DETAIL = "stock_detail/{itemId}"
    const val STOCK_DASHBOARD = "stock_dashboard"
    const val STOCK_SALES_HISTORY = "stock_sales_history"
    const val CLIENT_LIST = "client_list"
    const val CLIENT_NEW = "client_new"
    const val CLIENT_EDIT = "client_edit/{clientId}"
    const val CLIENT_DETAIL = "client_detail/{clientId}"
    const val SUPPLIER_LIST = "supplier_list"
    const val SUPPLIER_NEW = "supplier_new"
    const val SUPPLIER_EDIT = "supplier_edit/{supplierId}"
    const val SUPPLIER_DETAIL = "supplier_detail/{supplierId}"
    const val GLOBAL_SEARCH = "global_search"

    fun gemsList(colorName: String) = "gems/$colorName"
    fun labNotebookEdit(sampleId: String) = "lab_notebook_edit/$sampleId"
    fun labNotebookDetail(sampleId: String) = "lab_notebook_detail/$sampleId"
    fun meteoriteDetail(meteoriteId: String) = "meteorite/$meteoriteId"
    fun fossileDetail(fossileId: String) = "fossile/$fossileId"
    fun coquillageDetail(coquillageId: String) = "coquillage/$coquillageId"
    fun paysDetail(country: String) = "pays/${encode(country)}"
    fun gemDetail(gemId: String) = "gem/$gemId"
    fun certificate(gemId: String) = "certificate/$gemId"
    fun familleDetail(familyName: String) = "familles/${encode(familyName)}"
    fun lithotherapieLabels(scheme: String) = "lithotherapie_labels/$scheme"
    fun lithotherapieGems(scheme: String, label: String) = "lithotherapie_gems/$scheme/${encode(label)}"
    fun lithotherapieDetail(gemId: String) = "lithotherapie_detail/$gemId"
    fun lithotherapieInfo(topic: String) = "lithotherapie_info/$topic"
    fun associationDetail(associationId: String) = "association/$associationId"
    fun stockEdit(itemId: String) = "stock_edit/$itemId"
    fun stockDetail(itemId: String) = "stock_detail/$itemId"
    fun clientEdit(clientId: String) = "client_edit/$clientId"
    fun clientDetail(clientId: String) = "client_detail/$clientId"
    fun supplierEdit(supplierId: String) = "supplier_edit/$supplierId"
    fun supplierDetail(supplierId: String) = "supplier_detail/$supplierId"

    fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
    fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")
}

/**
 * Écoute [AchievementsRepository] pour afficher une notification (snackbar)
 * à chaque succès nouvellement débloqué, quel que soit l'écran affiché au
 * moment du déblocage (fiche gemme, quiz, favoris...). Résolution des
 * titres de succès une seule fois ici plutôt qu'à chaque notification, pour
 * rester utilisable depuis la boucle d'écoute (hors contexte @Composable).
 */
@Composable
private fun AchievementUnlockNotifier(snackbarHostState: SnackbarHostState) {
    val unlockedFormat = stringResource(R.string.achievement_unlocked_snackbar)
    val badgeTitles = Achievements.BADGES.associate { it.id to localizedBadgeTitle(it) }
    val badgeEmojis = remember { Achievements.BADGES.associate { it.id to it.emoji } }

    LaunchedEffect(Unit) {
        while (true) {
            val badgeId = AchievementsRepository.consumePendingUnlock()
            if (badgeId != null) {
                val title = badgeTitles[badgeId].orEmpty()
                val emoji = badgeEmojis[badgeId].orEmpty()
                snackbarHostState.showSnackbar(String.format(unlockedFormat, emoji, title))
            } else {
                delay(250)
            }
        }
    }
}

@Composable
fun GemsNavGraph(navController: NavHostController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    AchievementUnlockNotifier(snackbarHostState)

    Box(modifier = Modifier.fillMaxSize()) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        // Effet "écrin qui s'ouvre" : l'écran entrant apparaît en zoom-fondu
        // depuis un point ancré en haut à droite, comme un coffret qu'on
        // entrouvre, plutôt qu'un simple glissement latéral. Pas de vrai
        // clip circulaire (non disponible nativement dans les transitions
        // de Navigation Compose) : le zoom-fondu ancré donne la même
        // sensation d'ouverture.
        enterTransition = {
            fadeIn(animationSpec = tween(NAV_TRANSITION_MS)) +
                scaleIn(
                    animationSpec = tween(NAV_TRANSITION_MS),
                    initialScale = 0.86f,
                    transformOrigin = NAV_TRANSITION_ORIGIN
                )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(NAV_TRANSITION_MS))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(NAV_TRANSITION_MS))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(NAV_TRANSITION_MS)) +
                scaleOut(
                    animationSpec = tween(NAV_TRANSITION_MS),
                    targetScale = 0.86f,
                    transformOrigin = NAV_TRANSITION_ORIGIN
                )
        }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onGemmologieClick = { navController.navigate(Routes.CATEGORIES) },
                onFamillesClick = { navController.navigate(Routes.FAMILLES_LIST) },
                onLithotherapieClick = { navController.navigate(Routes.LITHOTHERAPIE_MENU) },
                onLanguageClick = { navController.navigate(Routes.LANGUAGE) },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) },
                onMeteoritesClick = { navController.navigate(Routes.METEORITES) },
                onFossilesClick = { navController.navigate(Routes.FOSSILES) },
                onCoquillagesClick = { navController.navigate(Routes.COQUILLAGES) },
                onQuizClick = { navController.navigate(Routes.QUIZ) },
                onAchievementsClick = { navController.navigate(Routes.ACHIEVEMENTS) },
                onLabClick = { navController.navigate(Routes.LAB_MENU) },
                onLapidaireClick = { navController.navigate(Routes.LAPIDAIRE) },
                onSearchClick = { navController.navigate(Routes.GLOBAL_SEARCH) }
            )
        }
        composable(Routes.GLOBAL_SEARCH) {
            GlobalSearchScreen(
                onGemClick = { gemId -> navController.navigate(Routes.gemDetail(gemId)) },
                onStockItemClick = { itemId -> navController.navigate(Routes.stockDetail(itemId)) },
                onClientClick = { clientId -> navController.navigate(Routes.clientDetail(clientId)) },
                onSupplierClick = { supplierId -> navController.navigate(Routes.supplierDetail(supplierId)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.QUIZ) {
            QuizScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ACHIEVEMENTS) {
            AchievementsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.LAB_MENU) {
            LabMenuScreen(
                onQuickAnalysisClick = { navController.navigate(Routes.ANALYSE) },
                onGuidedAnalysisClick = { navController.navigate(Routes.GUIDED_ANALYSE) },
                onNotebookClick = { navController.navigate(Routes.LAB_NOTEBOOK) },
                onInstrumentsClick = { navController.navigate(Routes.INSTRUMENTS) },
                onGlossaireClick = { navController.navigate(Routes.GLOSSAIRE) },
                onReflectivityMeterClick = { navController.navigate(Routes.REFLECTIVITY_METER) },
                onNuancierClick = { navController.navigate(Routes.NUANCIER) },
                onDiamondGradingClick = { navController.navigate(Routes.DIAMOND_GRADING) },
                onTreatmentsClick = { navController.navigate(Routes.TREATMENTS) },
                onStockClick = { navController.navigate(Routes.STOCK_LIST) },
                onClientsClick = { navController.navigate(Routes.CLIENT_LIST) },
                onSuppliersClick = { navController.navigate(Routes.SUPPLIER_LIST) },
                onLegendaryClick = { navController.navigate(Routes.LEGENDARY_RIDDLE) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.TREATMENTS) {
            TreatmentsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.LAPIDAIRE) {
            LapidaireScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.STOCK_LIST) {
            StockListScreen(
                onItemClick = { itemId -> navController.navigate(Routes.stockDetail(itemId)) },
                onAddClick = { navController.navigate(Routes.STOCK_NEW) },
                onDashboardClick = { navController.navigate(Routes.STOCK_DASHBOARD) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.STOCK_DASHBOARD) {
            StockDashboardScreen(
                onBackClick = { navController.popBackStack() },
                onSalesHistoryClick = { navController.navigate(Routes.STOCK_SALES_HISTORY) },
                onClientClick = { clientId -> navController.navigate(Routes.clientDetail(clientId)) }
            )
        }
        composable(Routes.STOCK_SALES_HISTORY) {
            StockSalesHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onItemClick = { itemId -> navController.navigate(Routes.stockDetail(itemId)) }
            )
        }
        composable(Routes.CLIENT_LIST) {
            ClientListScreen(
                onClientClick = { clientId -> navController.navigate(Routes.clientDetail(clientId)) },
                onAddClick = { navController.navigate(Routes.CLIENT_NEW) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENT_NEW) {
            ClientFormScreen(
                clientId = null,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENT_EDIT) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId").orEmpty()
            ClientFormScreen(
                clientId = clientId,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CLIENT_DETAIL) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId").orEmpty()
            ClientDetailScreen(
                clientId = clientId,
                onEditClick = { id -> navController.navigate(Routes.clientEdit(id)) },
                onDeleted = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
                onStockItemClick = { itemId -> navController.navigate(Routes.stockDetail(itemId)) }
            )
        }
        composable(Routes.SUPPLIER_LIST) {
            SupplierListScreen(
                onSupplierClick = { supplierId -> navController.navigate(Routes.supplierDetail(supplierId)) },
                onAddClick = { navController.navigate(Routes.SUPPLIER_NEW) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.SUPPLIER_NEW) {
            SupplierFormScreen(
                supplierId = null,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.SUPPLIER_EDIT) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getString("supplierId").orEmpty()
            SupplierFormScreen(
                supplierId = supplierId,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.SUPPLIER_DETAIL) { backStackEntry ->
            val supplierId = backStackEntry.arguments?.getString("supplierId").orEmpty()
            SupplierDetailScreen(
                supplierId = supplierId,
                onEditClick = { id -> navController.navigate(Routes.supplierEdit(id)) },
                onDeleted = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
                onStockItemClick = { itemId -> navController.navigate(Routes.stockDetail(itemId)) }
            )
        }
        composable(Routes.STOCK_NEW) {
            StockFormScreen(
                itemId = null,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.STOCK_EDIT) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId").orEmpty()
            StockFormScreen(
                itemId = itemId,
                onSaveComplete = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.STOCK_DETAIL) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId").orEmpty()
            StockDetailScreen(
                itemId = itemId,
                onEditClick = { id -> navController.navigate(Routes.stockEdit(id)) },
                onDeleted = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.REFLECTIVITY_METER) {
            ReflectivityMeterScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.NUANCIER) {
            NuancierScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.DIAMOND_GRADING) {
            DiamondGradingScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.LEGENDARY_RIDDLE) {
            LegendaryRiddleScreen(
                onOpenMapClick = { navController.navigate(Routes.LEGENDARY_MAP) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LEGENDARY_MAP) {
            LegendaryMapScreen(
                onGemClick = { gemId -> navController.navigate(Routes.gemDetail(gemId)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.GUIDED_ANALYSE) {
            GuidedAnalysisScreen(
                onGemClick = { gemId -> navController.navigate(Routes.gemDetail(gemId)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LAB_NOTEBOOK) {
            LabNotebookScreen(
                onSampleClick = { sampleId -> navController.navigate(Routes.labNotebookDetail(sampleId)) },
                onAddClick = { navController.navigate(Routes.LAB_NOTEBOOK_NEW) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LAB_NOTEBOOK_NEW) {
            LabNotebookFormScreen(
                sampleId = null,
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LAB_NOTEBOOK_EDIT) { backStackEntry ->
            val sampleId = backStackEntry.arguments?.getString("sampleId").orEmpty()
            LabNotebookFormScreen(
                sampleId = sampleId,
                onSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LAB_NOTEBOOK_DETAIL) { backStackEntry ->
            val sampleId = backStackEntry.arguments?.getString("sampleId").orEmpty()
            LabNotebookDetailScreen(
                sampleId = sampleId,
                onGemClick = { gemId -> navController.navigate(Routes.gemDetail(gemId)) },
                onEditClick = { id -> navController.navigate(Routes.labNotebookEdit(id)) },
                onDeleted = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.METEORITES) {
            MeteoritesMenuScreen(
                onClassificationClick = { navController.navigate(Routes.METEORITE_CLASSIFICATION) },
                onMeteoriteClick = { meteorite -> navController.navigate(Routes.meteoriteDetail(meteorite.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.METEORITE_CLASSIFICATION) {
            MeteoriteClassificationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.METEORITE_DETAIL) { backStackEntry ->
            val meteoriteId = backStackEntry.arguments?.getString("meteoriteId").orEmpty()
            MeteoriteDetailScreen(
                meteoriteId = meteoriteId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.FOSSILES) {
            FossilesMenuScreen(
                onClassificationClick = { navController.navigate(Routes.FOSSILE_CLASSIFICATION) },
                onFossileClick = { fossile -> navController.navigate(Routes.fossileDetail(fossile.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.FOSSILE_CLASSIFICATION) {
            FossileClassificationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.FOSSILE_DETAIL) { backStackEntry ->
            val fossileId = backStackEntry.arguments?.getString("fossileId").orEmpty()
            FossileDetailScreen(
                fossileId = fossileId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.COQUILLAGES) {
            CoquillagesMenuScreen(
                onClassificationClick = { navController.navigate(Routes.COQUILLAGE_CLASSIFICATION) },
                onCoquillageClick = { coquillage -> navController.navigate(Routes.coquillageDetail(coquillage.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.COQUILLAGE_CLASSIFICATION) {
            CoquillageClassificationScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.COQUILLAGE_DETAIL) { backStackEntry ->
            val coquillageId = backStackEntry.arguments?.getString("coquillageId").orEmpty()
            CoquillageDetailScreen(
                coquillageId = coquillageId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LANGUAGE) {
            LanguageScreen(
                onBackClick = { navController.popBackStack() },
                onLanguageSelected = { navController.popBackStack(Routes.HOME, false) }
            )
        }
        composable(Routes.CATEGORIES) {
            GemmologieMenuScreen(
                onCouleurClick = { navController.navigate(Routes.COULEUR_LIST) },
                onPaysClick = { navController.navigate(Routes.PAYS_LIST) },
                onComparerClick = { navController.navigate(Routes.COMPARER) },
                onCrystalSystemsClick = { navController.navigate(Routes.CRYSTAL_SYSTEMS) },
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.COMPARER) {
            GemComparisonScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.CRYSTAL_SYSTEMS) {
            CrystalSystemsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ANALYSE) {
            AnalyseScreen(
                onGemClick = { gemId -> navController.navigate(Routes.gemDetail(gemId)) },
                onInstrumentsClick = { navController.navigate(Routes.INSTRUMENTS) },
                onGlossaireClick = { navController.navigate(Routes.GLOSSAIRE) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.INSTRUMENTS) {
            InstrumentsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.GLOSSAIRE) {
            GlossaryScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.COULEUR_LIST) {
            ColorListScreen(
                onCategoryClick = { category ->
                    navController.navigate(Routes.gemsList(category.name))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.PAYS_LIST) {
            PaysListScreen(
                onPaysClick = { country -> navController.navigate(Routes.paysDetail(country)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.PAYS_DETAIL) { backStackEntry ->
            val country = Routes.decode(backStackEntry.arguments?.getString("country").orEmpty())
            GemsListScreen(
                title = localizedLabel(country),
                gems = GemOrigins.gemsFor(country),
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_MENU) {
            LithotherapieMenuScreen(
                onSchemeClick = { scheme -> navController.navigate(Routes.lithotherapieLabels(scheme)) },
                onInfoClick = { topic -> navController.navigate(Routes.lithotherapieInfo(topic)) },
                onAssociationsClick = { navController.navigate(Routes.ASSOCIATIONS_LIST) },
                onAllGemsClick = { navController.navigate(Routes.LITHOTHERAPIE_ALL) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_ALL) {
            LithotherapieAllGemsScreen(
                onGemClick = { gem -> navController.navigate(Routes.lithotherapieDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.ASSOCIATIONS_LIST) {
            AssociationsListScreen(
                onAssociationClick = { association -> navController.navigate(Routes.associationDetail(association.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.ASSOCIATION_DETAIL) { backStackEntry ->
            val associationId = backStackEntry.arguments?.getString("associationId").orEmpty()
            AssociationDetailScreen(
                associationId = associationId,
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_INFO) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            LithotherapieInfoScreen(
                topic = topic,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_LABELS) { backStackEntry ->
            val scheme = backStackEntry.arguments?.getString("scheme").orEmpty()
            LithotherapieLabelListScreen(
                scheme = scheme,
                onLabelClick = { label -> navController.navigate(Routes.lithotherapieGems(scheme, label)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_GEMS) { backStackEntry ->
            val scheme = backStackEntry.arguments?.getString("scheme").orEmpty()
            val label = Routes.decode(backStackEntry.arguments?.getString("label").orEmpty())
            LithotherapieGemsScreen(
                scheme = scheme,
                label = label,
                onGemClick = { gem -> navController.navigate(Routes.lithotherapieDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.GEMS_LIST) { backStackEntry ->
            val colorName = backStackEntry.arguments?.getString("colorName").orEmpty()
            val category = GemColorCategory.valueOf(colorName)
            GemsListScreen(
                title = stringResource(category.labelRes),
                gems = GemsRepository.byColor(category),
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.FAMILLES_LIST) {
            FamillesListScreen(
                onFamilyClick = { familyName -> navController.navigate(Routes.familleDetail(familyName)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.FAMILLE_DETAIL) { backStackEntry ->
            val familySlug = backStackEntry.arguments?.getString("familySlug").orEmpty()
            val familyName = Routes.decode(familySlug)
            GemsListScreen(
                title = localizedLabel(familyName),
                gems = GemFamilies.gemsFor(familyName),
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.GEM_DETAIL) { backStackEntry ->
            val gemId = backStackEntry.arguments?.getString("gemId").orEmpty()
            GemDetailScreen(
                gemId = gemId,
                onBackClick = { navController.popBackStack() },
                onCertificateClick = { navController.navigate(Routes.certificate(gemId)) }
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CERTIFICATE) { backStackEntry ->
            val gemId = backStackEntry.arguments?.getString("gemId").orEmpty()
            CertificateScreen(
                gemId = gemId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_DETAIL) { backStackEntry ->
            val gemId = backStackEntry.arguments?.getString("gemId").orEmpty()
            LithotherapieDetailScreen(
                gemId = gemId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}
