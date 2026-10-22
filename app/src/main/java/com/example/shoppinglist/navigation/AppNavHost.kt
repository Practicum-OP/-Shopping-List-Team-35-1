package com.example.shoppinglist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.feature.intro.ui.IntroRoute
import com.example.shoppinglist.feature.listdetail.ui.ListDetailRoute
import com.example.shoppinglist.feature.lists.ui.ListRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.INTRO,
        modifier = modifier,
    ) {
        composable(
            route = ScreenRoute.INTRO,
        ) {
            IntroRoute(
                onOpenList = {
                    navController.navigate(ScreenRoute.LIST) {
                        popUpTo(ScreenRoute.INTRO) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = ScreenRoute.LIST,
        ) {
            ListRoute(
                onOpenListDetail = {
                    navController.navigate(ScreenRoute.LIST_DETAIL) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = ScreenRoute.LIST_DETAIL,
        ) {
            ListDetailRoute(
                onBackToList = {
                    navController.popBackStack()
                },
            )
        }
    }
}
