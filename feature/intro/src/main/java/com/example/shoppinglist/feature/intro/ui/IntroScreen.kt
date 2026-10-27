package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.components.Placeholder
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
        //  verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.intro_logo_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(horizontal = 37.dp)
                .padding(top = 16.dp)
                .fillMaxWidth()
            //   .wrapContentSize()
            //    .weight(1f)
            //    .,
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Placeholder(
                imageRes = R.drawable.illustration_main_screen,
                titleRes = R.string.intro_title,
                descriptionRes = R.string.intro_description,
                contentDescriptionRes = R.string.intro_image_description
            )
        }

        //   Spacer(modifier = Modifier.height(44.dp))
    }
}
