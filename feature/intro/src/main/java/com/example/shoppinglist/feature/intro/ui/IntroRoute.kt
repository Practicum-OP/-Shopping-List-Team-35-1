package com.example.shoppinglist.feature.intro.ui

import androidx.compose.runtime.Composable

@Composable
fun IntroRoute(
    onOpenLogin: () -> Unit,
    onOpenRegistration: () -> Unit,
    onOpenRecovery: () -> Unit,
) {
    IntroScreen(
        onLoginClick = onOpenLogin,
        onRegistrationClick = onOpenRegistration,
        onRecoveryClick = onOpenRecovery,
    )
}
