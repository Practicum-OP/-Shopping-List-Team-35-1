package com.example.shoppinglist.core.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_lists",
    indices = [
        Index(value = ["sortOrder"]),
        Index(value = ["updatedAt"])
    ]
)
data class ShoppingListEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    val iconKey: String,

    val sortOrder: Int,

    val createdAt: Long,

    val updatedAt: Long
)
