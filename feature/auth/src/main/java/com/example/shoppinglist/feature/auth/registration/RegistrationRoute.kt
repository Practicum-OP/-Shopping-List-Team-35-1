package com.example.shoppinglist.feature.auth.registration

import androidx.compose.runtime.Composable

@Composable
fun RegistrationRoute(
    onBack: () -> Unit,
    onRegistration: () -> Unit,
) {
    RegistrationScreen(
        onBack = onBack,
        onRegistration = onRegistration,
    )
}
