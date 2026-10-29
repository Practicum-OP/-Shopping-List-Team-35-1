package com.example.shoppinglist.core.data.di

import androidx.room.Room
import com.example.shoppinglist.core.data.room.ShoppingListDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<ShoppingListDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = ShoppingListDatabase::class.java,
            name = "shopping_list.db",
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    single {
        get<ShoppingListDatabase>().shoppingListDao()
    }
}
