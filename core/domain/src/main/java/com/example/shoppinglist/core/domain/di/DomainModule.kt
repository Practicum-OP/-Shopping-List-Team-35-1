package com.example.shoppinglist.core.domain.di

import com.example.shoppinglist.core.domain.listdetail.interactor.ChangeItemsSortMode
import com.example.shoppinglist.core.domain.listdetail.interactor.ClearPurchasedShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteAllShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.MoveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveItemSuggestions
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingList
import com.example.shoppinglist.core.domain.listdetail.interactor.SaveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ToggleShoppingItemPurchased
import com.example.shoppinglist.core.domain.lists.interactor.ChangeShoppingListIcon
import com.example.shoppinglist.core.domain.lists.interactor.CopyShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.CreateShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.DeleteAllShoppingLists
import com.example.shoppinglist.core.domain.lists.interactor.DeleteShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.ObserveShoppingLists
import com.example.shoppinglist.core.domain.lists.interactor.RenameShoppingList
import com.example.shoppinglist.core.domain.settings.ChangeThemeMode
import com.example.shoppinglist.core.domain.settings.ObserveThemeMode
import org.koin.dsl.module

val domainModule = module {

    factory {
        ObserveShoppingLists(
            repository = get(),
        )
    }

    factory {
        CreateShoppingList(
            repository = get(),
        )
    }

    factory {
        RenameShoppingList(
            repository = get(),
        )
    }

    factory {
        DeleteShoppingList(
            repository = get(),
        )
    }

    factory {
        DeleteAllShoppingLists(
            repository = get(),
        )
    }

    factory {
        CopyShoppingList(
            repository = get(),
        )
    }

    factory {
        ChangeShoppingListIcon(
            repository = get(),
        )
    }

    factory {
        ObserveThemeMode(
            repository = get(),
        )
    }

    factory {
        ChangeThemeMode(
            repository = get(),
        )
    }

    factory {
        ObserveShoppingList(
            repository = get(),
        )
    }

    factory {
        ObserveShoppingItems(
            repository = get(),
        )
    }

    factory {
        ObserveItemSuggestions(
            repository = get(),
        )
    }

    factory {
        SaveShoppingItem(
            itemRepository = get(),
            historyRepository = get(),
        )
    }

    factory {
        ToggleShoppingItemPurchased(
            repository = get(),
        )
    }

    factory {
        DeleteShoppingItem(
            repository = get(),
        )
    }

    factory {
        DeleteAllShoppingItems(
            repository = get(),
        )
    }

    factory {
        ClearPurchasedShoppingItems(
            repository = get(),
        )
    }

    factory {
        ChangeItemsSortMode(
            repository = get(),
        )
    }

    factory {
        MoveShoppingItem(
            repository = get(),
        )
    }
}
