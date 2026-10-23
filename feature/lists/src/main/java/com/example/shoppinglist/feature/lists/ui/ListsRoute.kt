package com.example.shoppinglist.feature.lists.ui

import androidx.compose.runtime.Composable

@Composable
fun ListsRoute(
    onOpenListDetail: () -> Unit,
) {
    ListsScreen(
        onOpenListDetailClick = onOpenListDetail,
    )
}
