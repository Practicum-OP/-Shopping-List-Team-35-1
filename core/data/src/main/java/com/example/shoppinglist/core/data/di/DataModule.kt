package com.example.shoppinglist.core.data.di

import androidx.room.Room
import com.example.shoppinglist.core.data.repository.ItemHistoryRepositoryImpl
import com.example.shoppinglist.core.data.repository.ListDetailRepositoryImpl
import com.example.shoppinglist.core.data.repository.ShoppingItemRepositoryImpl
import com.example.shoppinglist.core.data.repository.ShoppingListRepositoryImpl
import com.example.shoppinglist.core.data.repository.ThemeSettingsRepositoryImpl
import com.example.shoppinglist.core.data.room.ALL_MIGRATIONS
import com.example.shoppinglist.core.data.room.ShoppingListDatabase
import com.example.shoppinglist.core.domain.listdetail.ItemHistoryRepository
import com.example.shoppinglist.core.domain.listdetail.ListDetailRepository
import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository
import com.example.shoppinglist.core.domain.lists.ShoppingListRepository
import com.example.shoppinglist.core.domain.settings.ThemeSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<ShoppingListDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ShoppingListDatabase::class.java,
            name = "shopping_list.db",
        )
            .addMigrations(*ALL_MIGRATIONS)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    single {
        get<ShoppingListDatabase>().shoppingListDao()
    }

    single {
        get<ShoppingListDatabase>().shoppingItemDao()
    }

    single {
        get<ShoppingListDatabase>().itemHistoryDao()
    }

    single<ShoppingListRepository> {
        ShoppingListRepositoryImpl(
            dao = get(),
        )
    }

    single<ListDetailRepository> {
        ListDetailRepositoryImpl(
            dao = get(),
        )
    }

    single<ShoppingItemRepository> {
        ShoppingItemRepositoryImpl(
            dao = get(),
        )
    }

    single<ItemHistoryRepository> {
        ItemHistoryRepositoryImpl(
            dao = get(),
        )
    }

    single<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(
            context = androidContext(),
        )
    }
}
