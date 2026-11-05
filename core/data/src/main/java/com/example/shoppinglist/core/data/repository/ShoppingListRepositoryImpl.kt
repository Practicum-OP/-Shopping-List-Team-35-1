package com.example.shoppinglist.core.data.repository

import com.example.shoppinglist.core.data.mapper.toDomain
import com.example.shoppinglist.core.data.mapper.toEntity
import com.example.shoppinglist.core.data.room.dao.ShoppingListDao
import com.example.shoppinglist.core.domain.lists.ShoppingList
import com.example.shoppinglist.core.domain.lists.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ShoppingListRepositoryImpl(
    private val dao: ShoppingListDao,
) : ShoppingListRepository {

    override fun observeAll(): Flow<List<ShoppingList>> {
        return dao.observeAll().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getById(id: String): ShoppingList? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun create(
        name: String,
        iconKey: String,
    ) {
        val now = System.currentTimeMillis()
        val sortOrder = dao.getNextSortOrder()

        val shoppingList = ShoppingList(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            iconKey = iconKey,
            itemsSortMode = com.example.shoppinglist.core.domain.lists.ItemsSortMode.CUSTOM,
            sortOrder = sortOrder,
            createdAt = now,
            updatedAt = now,
        )

        dao.upsert(shoppingList.toEntity())
    }

    override suspend fun rename(
        id: String,
        name: String,
    ) {
        dao.rename(
            id = id,
            name = name.trim(),
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun copy(
        id: String,
        copiedName: String,
    ) {
        val source = dao.getById(id) ?: return
        val now = System.currentTimeMillis()
        val sortOrder = dao.getNextSortOrder()

        val copy = source.toDomain().copy(
            id = UUID.randomUUID().toString(),
            name = copiedName.trim(),
            sortOrder = sortOrder,
            createdAt = now,
            updatedAt = now,
        )

        dao.upsert(copy.toEntity())
    }

    override suspend fun updateIcon(
        id: String,
        iconKey: String,
    ) {
        dao.updateIcon(
            id = id,
            iconKey = iconKey,
            updatedAt = System.currentTimeMillis(),
        )
    }
}
