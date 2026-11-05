package com.example.shoppinglist.core.domain.di

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
}
