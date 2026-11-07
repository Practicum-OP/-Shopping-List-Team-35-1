package com.example.shoppinglist.core.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoppinglist.core.data.room.dao.ItemHistoryDao
import com.example.shoppinglist.core.data.room.dao.ShoppingItemDao
import com.example.shoppinglist.core.data.room.dao.ShoppingListDao
import com.example.shoppinglist.core.data.room.entity.ItemHistoryEntity
import com.example.shoppinglist.core.data.room.entity.ShoppingItemEntity
import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        ShoppingItemEntity::class,
        ItemHistoryEntity::class,
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(
            from = 2,
            to = 3,
        ),
    ],
    exportSchema = true,
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun shoppingItemDao(): ShoppingItemDao

    abstract fun itemHistoryDao(): ItemHistoryDao
}
