package com.example.shoppinglist.core.data.di

import androidx.room.Room
import com.example.shoppinglist.core.data.repository.ShoppingListRepositoryImpl
import com.example.shoppinglist.core.data.repository.ThemeSettingsRepositoryImpl
import com.example.shoppinglist.core.data.room.ShoppingListDatabase
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
            .fallbackToDestructiveMigration(
                dropAllTables = true,
            )
            .build()
    }

    single {
        get<ShoppingListDatabase>().shoppingListDao()
    }

    single<ShoppingListRepository> {
        ShoppingListRepositoryImpl(
            dao = get(),
        )
    }

    single<ThemeSettingsRepository> {
        ThemeSettingsRepositoryImpl(
            context = androidContext(),
        )
    }
}
