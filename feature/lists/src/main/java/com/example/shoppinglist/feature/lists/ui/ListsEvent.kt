package com.example.shoppinglist.feature.lists.ui

sealed interface ListsEvent {

    // Поиск.

    data object SearchOpened : ListsEvent

    data object SearchClosed : ListsEvent

    data class SearchQueryChanged(
        val query: String,
    ) : ListsEvent

    // Создание и удаление списков.

    data class CreateListConfirmed(
        val name: String,
    ) : ListsEvent

    data object DeleteAllListsConfirmed : ListsEvent

    // Настройки оформления.

    data class ThemeChanged(
        val isDarkTheme: Boolean,
    ) : ListsEvent

    // Действия с конкретным списком.

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
