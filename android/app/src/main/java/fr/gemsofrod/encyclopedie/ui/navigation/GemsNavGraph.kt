package fr.gemsofrod.encyclopedie.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemFamilies
import fr.gemsofrod.encyclopedie.data.GemOrigins
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.ui.screens.AnalyseScreen
import fr.gemsofrod.encyclopedie.ui.screens.CertificateScreen
import fr.gemsofrod.encyclopedie.ui.screens.ColorListScreen
import fr.gemsofrod.encyclopedie.ui.screens.FamillesListScreen
import fr.gemsofrod.encyclopedie.ui.screens.FavoritesScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemmologieMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemsListScreen
import fr.gemsofrod.encyclopedie.ui.screens.GlossaryScreen
import fr.gemsofrod.encyclopedie.ui.screens.HomeScreen
import fr.gemsofrod.encyclopedie.ui.screens.InstrumentsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LanguageScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieGemsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieInfoScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieLabelListScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoriteClassificationScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoriteDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.MeteoritesMenuScreen
import fr.gemsofrod.encyclopedie.ui.screens.PaysListScreen
import fr.gemsofrod.encyclopedie.ui.screens.QuizScreen
import fr.gemsofrod.encyclopedie.ui.localizedLabel
import java.net.URLDecoder
import java.net.URLEncoder

private object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val COULEUR_LIST = "couleur_list"
    const val GEMS_LIST = "gems/{colorName}"
    const val PAYS_LIST = "pays_list"
    const val PAYS_DETAIL = "pays/{country}"
    const val ANALYSE = "analyse"
    const val INSTRUMENTS = "instruments"
    const val GLOSSAIRE = "glossaire"
    const val GEM_DETAIL = "gem/{gemId}"
    const val FAVORITES = "favorites"
    const val CERTIFICATE = "certificate/{gemId}"
    const val FAMILLES_LIST = "familles"
    const val FAMILLE_DETAIL = "familles/{familySlug}"
    const val LITHOTHERAPIE_MENU = "lithotherapie_menu"
    const val LITHOTHERAPIE_LABELS = "lithotherapie_labels/{scheme}"
    const val LITHOTHERAPIE_GEMS = "lithotherapie_gems/{scheme}/{label}"
    const val LITHOTHERAPIE_DETAIL = "lithotherapie_detail/{gemId}"
    const val LITHOTHERAPIE_INFO = "lithotherapie_info/{topic}"
    const val LANGUAGE = "language"
    const val METEORITES = "meteorites"
    const val METEORITE_CLASSIFICATION = "meteorite_classification"
    const val METEORITE_DETAIL = "meteorite/{meteoriteId}"
    const val QUIZ = "quiz"

    fun gemsList(colorName: String) = "gems/$colorName"
    fun meteoriteDetail(meteoriteId: String) = "meteorite/$meteoriteId"
    fun paysDetail(country: String) = "pays/${encode(country)}"
    fun gemDetail(gemId: String) = "gem/$gemId"
    fun certificate(gemId: String) = "certificate/$gemId"
    fun familleDetail(familyName: String) = "familles/${encode(familyName)}"
    fun lithotherapieLabels(scheme: String) = "lithotherapie_labels/$scheme"
    fun lithotherapieGems(scheme: String, label: String) = "lithotherapie_gems/$scheme/${encode(label)}"
    fun lithotherapieDetail(gemId: String) = "lithotherapie_detail/$gemId"
    fun lithotherapieInfo(topic: String) = "lithotherapie_info/$topic"

    fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
    fun decode(value: String): String = URLDecoder.decode(value, "UTF-8")
}

@Composable
fun GemsNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onGemmologieClick = { navController.navigate(Routes.CATEGORIES) },
                onFamillesClick = { navController.navigate(Routes.FAMILLES_LIST) },
                onLithotherapieClick = { navController.navigate(Routes.LITHOTHERAPIE_MENU) },
                onLanguageClick = { navController.navigate(Routes.LANGUAGE) },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) },
                onMeteoritesClick = { navController.navigate(Routes.METEORITES) },
                onQuizClick = { navController.navigate(Routes.QUIZ) }
            )
        }
        composable(Routes.QUIZ) {
            QuizScreen(onBackClick = { navController.popBackStack() })
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
                onAnalyseClick = { navController.navigate(Routes.ANALYSE) },
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
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
}
