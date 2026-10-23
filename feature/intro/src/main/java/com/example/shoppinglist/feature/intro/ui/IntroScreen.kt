package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(
    onOpenLogin: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(4_000)
        onOpenLogin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onOpenLogin()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "СПИСОК ПОКУПОК",
            fontSize = 30.sp,
        )
        Text(
            text = "Intro",
            fontSize = 25.sp,
        )
    }
}
