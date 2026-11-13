package com.example.shoppinglist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppinglist.feature.auth.login.LoginRoute
import com.example.shoppinglist.feature.auth.recovery.RecoveryRoute
import com.example.shoppinglist.feature.auth.registration.RegistrationRoute
import com.example.shoppinglist.feature.intro.ui.IntroRoute
import com.example.shoppinglist.feature.listdetail.ui.ListDetailRoute
import com.example.shoppinglist.feature.lists.ui.ListsRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    fun openLogin() {
        navController.navigate(ScreenRoute.LOGIN) {
            popUpTo(ScreenRoute.INTRO) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun openRegistration() {
        navController.navigate(ScreenRoute.REGISTRATION) {
            launchSingleTop = true
        }
    }

    fun openRecovery() {
        navController.navigate(ScreenRoute.RECOVERY) {
            launchSingleTop = true
        }
    }

    fun openLists() {
        navController.navigate(ScreenRoute.LISTS) {
            popUpTo(ScreenRoute.LOGIN) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun returnToLogin() {
        navController.popBackStack(
            route = ScreenRoute.LOGIN,
            inclusive = false,
        )
    }

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.INTRO,
        modifier = modifier,
    ) {
        composable(ScreenRoute.INTRO) {
            IntroRoute(
                onOpenLogin = ::openLogin,
            )
        }

        composable(ScreenRoute.LOGIN) {
            LoginRoute(
                onOpenRegistration = ::openRegistration,
                onOpenRecovery = ::openRecovery,
                onOpenLists = ::openLists,
            )
        }

        composable(ScreenRoute.REGISTRATION) {
            RegistrationRoute(
                onBack = ::returnToLogin,
                onRegistration = ::returnToLogin,
            )
        }

        composable(ScreenRoute.RECOVERY) {
            RecoveryRoute(
                onBack = ::returnToLogin,
                onRestorePassword = ::returnToLogin,
            )
        }

        composable(ScreenRoute.LISTS) {
            ListsRoute(
                onOpenListDetail = { listId ->
                    navController.navigate(ScreenRoute.listDetail(listId))
                },
            )
        }

        composable(
            route = ScreenRoute.LIST_DETAIL,
            arguments = listOf(
                navArgument("listId") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId").orEmpty()
            ListDetailRoute(
                listId = listId,
                onBackToLists = { navController.popBackStack() },
            )
        }
    }
}
