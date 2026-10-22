package com.example.auth.recovery

import androidx.compose.runtime.Composable

@Composable
fun RecoveryRoute(
    onOpenLists: () -> Unit,
) {
    RecoveryScreen(
        onOpenLists = onOpenLists,
    )
}
