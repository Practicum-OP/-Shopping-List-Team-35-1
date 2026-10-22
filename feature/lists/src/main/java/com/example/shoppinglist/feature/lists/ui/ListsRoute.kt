package com.example.shoppinglist.feature.lists.ui

import androidx.compose.runtime.Composable

@Composable
fun ListRoute(
    onOpenListDetail: () -> Unit,
) {
    ListScreen(
        onOpenListDetailClick = onOpenListDetail,
    )
}
