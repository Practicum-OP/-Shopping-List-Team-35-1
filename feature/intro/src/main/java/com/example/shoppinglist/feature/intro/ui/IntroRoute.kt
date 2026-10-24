package com.example.shoppinglist.feature.intro.ui

import androidx.compose.runtime.Composable

@Composable
fun IntroRoute(
    onOpenLogin: () -> Unit,
) {
    IntroScreen(
        onOpenLogin = onOpenLogin,
    )
}
