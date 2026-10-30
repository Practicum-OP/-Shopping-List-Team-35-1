package com.example.shoppinglist.feature.lists.ui

sealed interface ListsEffect {

    data class OpenList(
        val listId: String,
    ) : ListsEffect

    data class ShowSnackbar(
        val message: String,
    ) : ListsEffect
}
