package fr.gemsofrod.encyclopedie.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.data.GemFamilies
import fr.gemsofrod.encyclopedie.data.GemsRepository
import fr.gemsofrod.encyclopedie.ui.screens.CategoriesScreen
import fr.gemsofrod.encyclopedie.ui.screens.CertificateScreen
import fr.gemsofrod.encyclopedie.ui.screens.FamillesListScreen
import fr.gemsofrod.encyclopedie.ui.screens.FavoritesScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemsListScreen
import fr.gemsofrod.encyclopedie.ui.screens.HomeScreen
import fr.gemsofrod.encyclopedie.ui.screens.LanguageScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieGemsScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieLabelListScreen
import fr.gemsofrod.encyclopedie.ui.screens.LithotherapieMenuScreen
import java.net.URLDecoder
import java.net.URLEncoder

private object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val GEMS_LIST = "gems/{colorName}"
    const val GEM_DETAIL = "gem/{gemId}"
    const val FAVORITES = "favorites"
    const val CERTIFICATE = "certificate/{gemId}"
    const val FAMILLES_LIST = "familles"
    const val FAMILLE_DETAIL = "familles/{familySlug}"
    const val LITHOTHERAPIE_MENU = "lithotherapie_menu"
    const val LITHOTHERAPIE_LABELS = "lithotherapie_labels/{scheme}"
    const val LITHOTHERAPIE_GEMS = "lithotherapie_gems/{scheme}/{label}"
    const val LITHOTHERAPIE_DETAIL = "lithotherapie_detail/{gemId}"
    const val LANGUAGE = "language"

    fun gemsList(colorName: String) = "gems/$colorName"
    fun gemDetail(gemId: String) = "gem/$gemId"
    fun certificate(gemId: String) = "certificate/$gemId"
    fun familleDetail(familyName: String) = "familles/${encode(familyName)}"
    fun lithotherapieLabels(scheme: String) = "lithotherapie_labels/$scheme"
    fun lithotherapieGems(scheme: String, label: String) = "lithotherapie_gems/$scheme/${encode(label)}"
    fun lithotherapieDetail(gemId: String) = "lithotherapie_detail/$gemId"

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
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) }
            )
        }
        composable(Routes.LANGUAGE) {
            LanguageScreen(
                onBackClick = { navController.popBackStack() },
                onLanguageSelected = { navController.popBackStack(Routes.HOME, false) }
            )
        }
        composable(Routes.CATEGORIES) {
            CategoriesScreen(
                onCategoryClick = { category ->
                    navController.navigate(Routes.gemsList(category.name))
                },
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.LITHOTHERAPIE_MENU) {
            LithotherapieMenuScreen(
                onSchemeClick = { scheme -> navController.navigate(Routes.lithotherapieLabels(scheme)) },
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
                title = familyName,
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
