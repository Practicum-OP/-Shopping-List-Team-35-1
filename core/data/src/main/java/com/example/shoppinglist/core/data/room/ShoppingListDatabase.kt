package com.example.shoppinglist.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglist.core.data.room.dao.ShoppingListDao
import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}
