package com.example.shoppinglist.core.data.repository

import com.example.shoppinglist.core.data.mapper.toDomain
import com.example.shoppinglist.core.data.mapper.toEntity
import com.example.shoppinglist.core.data.mapper.toItemHistoryKey
import com.example.shoppinglist.core.data.room.dao.ItemHistoryDao
import com.example.shoppinglist.core.domain.listdetail.ItemHistory
import com.example.shoppinglist.core.domain.listdetail.ItemHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemHistoryRepositoryImpl(
    private val dao: ItemHistoryDao,
) : ItemHistoryRepository {

    override fun observeSuggestions(
        query: String,
        limit: Int,
    ): Flow<List<ItemHistory>> {
        return dao.observeSuggestions(
            normalizedQuery = query.toItemHistoryKey(),
            limit = limit,
        ).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun saveUsedItem(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return

        val history = ItemHistory(
            name = trimmed,
            lastUsedAt = System.currentTimeMillis(),
        )
        dao.upsert(history.toEntity())
    }
}
