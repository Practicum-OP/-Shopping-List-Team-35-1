package com.example.shoppinglist.core.domain.lists

import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    fun observeAll(): Flow<List<ShoppingList>>

    suspend fun getById(id: String): ShoppingList?

    suspend fun create(
        name: String,
        iconKey: String = DEFAULT_ICON_KEY,
    )

    suspend fun rename(
        id: String,
        name: String,
    )

    suspend fun deleteById(id: String)

    suspend fun deleteAll()

    suspend fun copy(
        id: String,
        copiedName: String,
    )

    suspend fun updateIcon(
        id: String,
        iconKey: String,
    )

    companion object {
        const val DEFAULT_ICON_KEY = "cart"
    }
}
