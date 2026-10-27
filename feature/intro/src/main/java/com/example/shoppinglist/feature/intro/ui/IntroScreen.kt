package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.components.Placeholder
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.intro.R

@Composable
fun IntroScreen(
    onClick: () -> Unit
) {
    val logoHorizontalPad = 37.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.intro_logo_description),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(horizontal = logoHorizontalPad)
                .padding(top = AppDimens.spacingMedium)
                .fillMaxWidth()
        )

        Placeholder(
            imageRes = R.drawable.illustration_main_screen,
            titleRes = R.string.intro_title,
            descriptionRes = R.string.intro_description,
            contentDescriptionRes = R.string.intro_image_description,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(AppDimens.spacingMedium))
    }
}
