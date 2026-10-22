package com.example.auth.login

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onOpenLists: () -> Unit,
) {
    LoginScreen(
        onOpenLists = onOpenLists,
    )
}
