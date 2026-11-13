package com.example.shoppinglist.feature.lists.ui

import com.example.shoppinglist.core.domain.lists.ShoppingList
import com.example.shoppinglist.core.domain.settings.ThemeMode

data class ListsState(
    val topContent: TopContent = TopContent.AppBar,
    val mainContent: MainContent = MainContent.Loading,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

sealed interface TopContent {

    data object AppBar : TopContent

    data class SearchField(
        val searchQuery: String = "",
    ) : TopContent
}

sealed interface MainContent {

    data object Loading : MainContent

    data object NoLists : MainContent

    data class Lists(
        val items: List<ShoppingList>,
    ) : MainContent

    data class SearchResults(
        val items: List<ShoppingList>,
    ) : MainContent

    data object NoSearchResults : MainContent
}
