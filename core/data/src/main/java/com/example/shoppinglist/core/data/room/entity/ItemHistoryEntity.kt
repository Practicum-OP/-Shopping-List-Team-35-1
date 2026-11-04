package com.example.shoppinglist.core.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_history",
    indices = [
        Index(value = ["lastUsedAt"]),
    ],
)
data class ItemHistoryEntity(
    @PrimaryKey
    val normalizedName: String,
    val name: String,
    val lastUsedAt: Long,
)
