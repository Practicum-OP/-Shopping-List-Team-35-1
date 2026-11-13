package com.example.shoppinglist.feature.listdetail.di

import com.example.shoppinglist.feature.listdetail.ui.ListDetailInteractors
import com.example.shoppinglist.feature.listdetail.ui.ListDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listDetailModule = module {

    viewModel { parameters ->
        ListDetailViewModel(
            listId = parameters.get(),
            interactors = ListDetailInteractors(
                observeShoppingList = get(),
                observeShoppingItems = get(),
                observeItemSuggestions = get(),
                saveShoppingItem = get(),
                toggleShoppingItemPurchased = get(),
                deleteShoppingItem = get(),
                deleteAllShoppingItems = get(),
                clearPurchasedShoppingItems = get(),
                changeItemsSortMode = get(),
                moveShoppingItem = get(),
            ),
        )
    }
}
