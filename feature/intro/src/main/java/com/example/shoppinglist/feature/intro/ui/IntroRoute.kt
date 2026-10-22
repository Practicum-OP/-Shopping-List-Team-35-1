package com.example.shoppinglist.feature.intro.ui

import androidx.compose.runtime.Composable

@Composable
fun IntroRoute(
    onOpenList: () -> Unit,
) {
    IntroScreen(
        onContinueClick = onOpenList,
    )
}
