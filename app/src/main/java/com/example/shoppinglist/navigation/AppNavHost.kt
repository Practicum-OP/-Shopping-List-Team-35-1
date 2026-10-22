package com.example.shoppinglist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.login.LoginRoute
import com.example.auth.recovery.RecoveryRoute
import com.example.auth.registration.RegistrationRoute
import com.example.shoppinglist.feature.intro.ui.IntroRoute
import com.example.shoppinglist.feature.listdetail.ui.ListDetailRoute
import com.example.shoppinglist.feature.lists.ui.ListRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val openLists: () -> Unit = {
        navController.navigate(ScreenRoute.LISTS) {
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.INTRO,
        modifier = modifier,
    ) {
        composable(ScreenRoute.INTRO) {
            IntroRoute(
                onOpenLogin = {
                    navController.navigate(ScreenRoute.LOGIN) {
                        launchSingleTop = true
                    }
                },
                onOpenRegistration = {
                    navController.navigate(ScreenRoute.REGISTRATION) {
                        launchSingleTop = true
                    }
                },
                onOpenRecovery = {
                    navController.navigate(ScreenRoute.RECOVERY) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(ScreenRoute.LOGIN) {
            LoginRoute(
                onOpenLists = openLists,
            )
        }

        composable(ScreenRoute.REGISTRATION) {
            RegistrationRoute(
                onOpenLists = openLists,
            )
        }

        composable(ScreenRoute.RECOVERY) {
            RecoveryRoute(
                onOpenLists = openLists,
            )
        }

        composable(ScreenRoute.LISTS) {
            ListRoute(
                onOpenListDetail = {
                    navController.navigate(ScreenRoute.LIST_DETAIL) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(ScreenRoute.LIST_DETAIL) {
            ListDetailRoute(
                onBackToLists = {
                    navController.popBackStack()
                },
            )
        }
    }
}
