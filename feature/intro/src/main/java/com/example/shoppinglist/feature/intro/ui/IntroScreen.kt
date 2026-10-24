package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.feature.intro.R
import kotlinx.coroutines.delay

private const val INTRO_DELAY_MILLIS = 4_000L

@Composable
fun IntroScreen(
    onOpenLogin: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(INTRO_DELAY_MILLIS)
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
            text = stringResource(R.string.intro_title),
            fontSize = 30.sp,
        )
        Text(
            text = stringResource(R.string.intro_description),
            fontSize = 25.sp,
        )
    }
}
