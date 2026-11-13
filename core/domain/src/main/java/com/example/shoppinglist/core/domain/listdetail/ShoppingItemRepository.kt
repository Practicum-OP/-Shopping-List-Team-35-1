package com.example.shoppinglist.core.domain.listdetail

import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import kotlinx.coroutines.flow.Flow

interface ShoppingItemRepository {

    fun observeByListId(
        listId: String,
        sortMode: ItemsSortMode,
    ): Flow<List<ShoppingItem>>

    suspend fun getById(id: String): ShoppingItem?

    suspend fun getNextPosition(listId: String): Int

    suspend fun upsert(item: ShoppingItem)

    suspend fun deleteById(id: String)

    suspend fun deleteByListId(listId: String)

    suspend fun deletePurchased(listId: String)

    suspend fun updatePurchased(
        id: String,
        isPurchased: Boolean,
    )

    suspend fun updatePositions(
        orderedIds: List<String>,
    )
}
