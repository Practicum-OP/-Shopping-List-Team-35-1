package com.example.shoppinglist.feature.intro.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun IntroRoute(
    onOpenLogin: () -> Unit,
    viewModel: IntroViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                IntroEffect.OpenLogin -> onOpenLogin()
            }
        }
    }

    IntroScreen(
        onClick = {
            viewModel.onEvent(IntroEvent.ScreenClicked)
        },
    )
}
