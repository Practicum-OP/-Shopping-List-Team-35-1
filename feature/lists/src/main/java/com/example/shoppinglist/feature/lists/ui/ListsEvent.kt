package com.example.shoppinglist.feature.lists.ui

import com.example.shoppinglist.core.domain.settings.ThemeMode

sealed interface ListsEvent {

    data object SearchOpened : ListsEvent

    data object SearchClosed : ListsEvent

    data object SearchCleared : ListsEvent

    data class SearchQueryChanged(
        val query: String,
    ) : ListsEvent

    data class CreateListConfirmed(
        val name: String,
    ) : ListsEvent

    data object DeleteAllListsConfirmed : ListsEvent

    data class ThemeModeChanged(
        val mode: ThemeMode,
    ) : ListsEvent

    data class ThemeChanged(
        val isDarkTheme: Boolean,
    ) : ListsEvent

    data class ListIconSelected(
        val listId: String,
        val iconKey: String,
    ) : ListsEvent

    data class RenameListConfirmed(
        val listId: String,
        val name: String,
    ) : ListsEvent

    data class DeleteListConfirmed(
        val listId: String,
    ) : ListsEvent

    data class CopyListClicked(
        val listId: String,
    ) : ListsEvent

    data class ListClicked(
        val listId: String,
    ) : ListsEvent
}
