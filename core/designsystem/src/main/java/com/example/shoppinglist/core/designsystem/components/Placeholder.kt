package com.example.shoppinglist.core.designsystem.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Placeholder(
    @DrawableRes imageRes: Int,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val boxHeight = maxHeight
        val boxWith = maxWidth

        val imageHeight: Dp
        val imageWith: Dp

        if (boxHeight > boxWith) {
            imageHeight = boxWith * 0.926f
            imageWith = boxWith
        } else {
            imageHeight = boxHeight - 30.dp
            imageWith = imageHeight / 0.926f
        }

        val pad = boxHeight / 2 + imageHeight / 2

        Column(
            modifier = modifier

                .fillMaxSize()
                .border(2.dp, color = Color.Green)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = stringResource(contentDescriptionRes),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    // .wrapContentHeight()
                    // .weight(1f)
                    // .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .height(imageHeight)
                    .width(imageWith)
                    .border(2.dp, color = Color.Red),
            )

            Text(
                text = stringResource(titleRes),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(2.dp, color = Color.Red),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(descriptionRes),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(2.dp, color = Color.Red),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }

}
