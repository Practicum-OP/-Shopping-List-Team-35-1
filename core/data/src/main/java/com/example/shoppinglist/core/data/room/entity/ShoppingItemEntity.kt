package com.example.shoppinglist.core.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["listId", "position"]),
    ],
)
data class ShoppingItemEntity(
    @PrimaryKey
    val id: String,
    val listId: String,
    // NOCASE в SQLite работает только с ASCII, кириллицу сортирует LOCALIZED.
    @ColumnInfo(collate = ColumnInfo.LOCALIZED)
    val name: String,
    val quantity: String?,
    val unit: String?,
    val isPurchased: Boolean,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long,
)
