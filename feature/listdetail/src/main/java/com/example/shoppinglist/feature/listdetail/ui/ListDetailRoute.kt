package com.example.shoppinglist.feature.listdetail.ui

import androidx.compose.runtime.Composable

@Composable
fun ListDetailRoute(
    onBackToLists: () -> Unit,
) {
    ListDetailScreen(
        onBackClick = onBackToLists,
    )
}
