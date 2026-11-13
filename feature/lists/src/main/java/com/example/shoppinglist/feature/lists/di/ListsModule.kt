package com.example.shoppinglist.feature.lists.di

import com.example.shoppinglist.feature.lists.ui.ListsInteractors
import com.example.shoppinglist.feature.lists.ui.ListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listsModule = module {

    viewModel {
        ListsViewModel(
            interactors = ListsInteractors(
                observeShoppingLists = get(),
                createShoppingList = get(),
                renameShoppingList = get(),
                deleteShoppingList = get(),
                deleteAllShoppingLists = get(),
                copyShoppingList = get(),
                changeShoppingListIcon = get(),
            ),
            observeThemeMode = get(),
            changeThemeMode = get(),
        )
    }
}
