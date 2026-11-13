package com.example.shoppinglist.core.domain.listdetail

import kotlinx.coroutines.flow.Flow

interface ItemHistoryRepository {

    fun observeSuggestions(
        query: String,
        limit: Int = 8,
    ): Flow<List<ItemHistory>>

    suspend fun saveUsedItem(
        name: String,
    )
}
