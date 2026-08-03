package fr.gemsofrod.encyclopedie.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.gemsofrod.encyclopedie.data.GemColorCategory
import fr.gemsofrod.encyclopedie.ui.screens.CategoriesScreen
import fr.gemsofrod.encyclopedie.ui.screens.FavoritesScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemDetailScreen
import fr.gemsofrod.encyclopedie.ui.screens.GemsListScreen

private object Routes {
    const val CATEGORIES = "categories"
    const val GEMS_LIST = "gems/{colorName}"
    const val GEM_DETAIL = "gem/{gemId}"
    const val FAVORITES = "favorites"

    fun gemsList(colorName: String) = "gems/$colorName"
    fun gemDetail(gemId: String) = "gem/$gemId"
}

@Composable
fun GemsNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.CATEGORIES) {
        composable(Routes.CATEGORIES) {
            CategoriesScreen(
                onCategoryClick = { category ->
                    navController.navigate(Routes.gemsList(category.name))
                },
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) }
            )
        }
        composable(Routes.GEMS_LIST) { backStackEntry ->
            val colorName = backStackEntry.arguments?.getString("colorName").orEmpty()
            val category = GemColorCategory.valueOf(colorName)
            GemsListScreen(
                category = category,
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.GEM_DETAIL) { backStackEntry ->
            val gemId = backStackEntry.arguments?.getString("gemId").orEmpty()
            GemDetailScreen(
                gemId = gemId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onGemClick = { gem -> navController.navigate(Routes.gemDetail(gem.id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
