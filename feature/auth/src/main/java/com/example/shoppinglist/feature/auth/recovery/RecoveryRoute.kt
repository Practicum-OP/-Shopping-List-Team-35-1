package com.example.shoppinglist.feature.auth.recovery

import androidx.compose.runtime.Composable

@Composable
fun RecoveryRoute(
    onBack: () -> Unit,
    onRestorePassword: () -> Unit,
) {
    RecoveryScreen(
        onBack = onBack,
        onRestorePassword = onRestorePassword,
    )
}
