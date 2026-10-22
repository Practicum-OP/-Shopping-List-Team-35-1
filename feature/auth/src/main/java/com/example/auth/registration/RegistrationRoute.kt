package com.example.auth.registration

import androidx.compose.runtime.Composable

@Composable
fun RegistrationRoute(
    onOpenLists: () -> Unit,
) {
    RegistrationScreen(
        onOpenLists = onOpenLists,
    )
}
