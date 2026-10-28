package com.example.shoppinglist.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.theme.AppDimens

@Composable
fun Placeholder(
    @DrawableRes imageRes: Int,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier,
) {
    val imageAspectRatio = 0.926f
    val imageSpacingRatio = 6.25f
    val screenHorizontalPad = 44.dp

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = screenHorizontalPad)
            .fillMaxSize(),
    ) {
        val imageHeight: Dp

        if (maxHeight > maxWidth) {
            imageHeight = maxWidth * imageAspectRatio
        } else {
            imageHeight = (maxHeight - maxHeight / imageSpacingRatio - AppDimens.listItemTwoLineMinHeight)
                .coerceAtLeast(0.dp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = stringResource(contentDescriptionRes),
                modifier = Modifier
                    .height(imageHeight)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(imageHeight / imageSpacingRatio))

            Text(
                text = stringResource(titleRes),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(descriptionRes),
                modifier = Modifier
                    .padding(top = AppDimens.listItemVerticalPadding),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
