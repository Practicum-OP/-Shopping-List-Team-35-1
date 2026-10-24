package com.example.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.lists.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    onOpenListDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.lists_title),
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppDimens.screenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(
                space = AppDimens.spacingLarge,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onOpenListDetailClick,
            ) {
                Text(
                    text = stringResource(R.string.lists_open_list),
                )
            }
        }
    }
}
