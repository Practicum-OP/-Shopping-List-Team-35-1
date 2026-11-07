package com.example.shoppinglist.core.data.repository

import com.example.shoppinglist.core.data.mapper.toDomain
import com.example.shoppinglist.core.data.mapper.toEntity
import com.example.shoppinglist.core.data.room.dao.ShoppingItemDao
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingItemRepositoryImpl(
    private val dao: ShoppingItemDao,
) : ShoppingItemRepository {

    override fun observeByListId(
        listId: String,
        sortMode: ItemsSortMode,
    ): Flow<List<ShoppingItem>> {
        val flow = when (sortMode) {
            ItemsSortMode.ALPHABETICAL -> dao.observeByListIdAlphabetically(listId)
            ItemsSortMode.CUSTOM -> dao.observeByListId(listId)
        }
        return flow.map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getById(id: String): ShoppingItem? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getNextPosition(listId: String): Int {
        return dao.getNextPosition(listId)
    }

    override suspend fun upsert(item: ShoppingItem) {
        dao.upsert(item.toEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByListId(listId: String) {
        dao.deleteByListId(listId)
    }

    override suspend fun deletePurchased(listId: String) {
        dao.deletePurchased(listId)
    }

    override suspend fun updatePurchased(
        id: String,
        isPurchased: Boolean,
    ) {
        dao.updatePurchased(
            id = id,
            isPurchased = isPurchased,
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun updatePositions(orderedIds: List<String>) {
        dao.updatePositions(
            orderedIds = orderedIds,
            updatedAt = System.currentTimeMillis(),
        )
    }
}
