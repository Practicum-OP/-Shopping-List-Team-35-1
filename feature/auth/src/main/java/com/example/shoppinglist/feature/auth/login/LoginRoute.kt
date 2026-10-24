package com.example.shoppinglist.feature.auth.login

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onOpenRegistration: () -> Unit,
    onOpenRecovery: () -> Unit,
    onOpenLists: () -> Unit,
) {
    LoginScreen(
        onRegister = onOpenRegistration,
        onRestorePassword = onOpenRecovery,
        onLogin = onOpenLists,
    )
}
