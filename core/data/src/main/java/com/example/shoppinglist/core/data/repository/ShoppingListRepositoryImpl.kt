package com.example.shoppinglist.core.data.repository

import com.example.shoppinglist.core.data.mapper.toDomain
import com.example.shoppinglist.core.data.mapper.toEntity
import com.example.shoppinglist.core.data.room.dao.ShoppingListDao
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
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

    override suspend fun getById(
        id: String,
    ): ShoppingList? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun create(
        name: String,
        iconKey: String,
    ) {
        val normalizedName = name.trim()

        if (normalizedName.isEmpty()) {
            return
        }

        val now = System.currentTimeMillis()
        val sortOrder = dao.getNextSortOrder()

        val shoppingList = ShoppingList(
            id = UUID.randomUUID().toString(),
            name = normalizedName,
            iconKey = iconKey,
            itemsSortMode = ItemsSortMode.ALPHABETICAL,
            sortOrder = sortOrder,
            createdAt = now,
            updatedAt = now,
        )

        dao.upsert(
            shoppingList.toEntity(),
        )
    }

    override suspend fun rename(
        id: String,
        name: String,
    ) {
        val normalizedName = name.trim()

        if (normalizedName.isEmpty()) {
            return
        }

        dao.rename(
            id = id,
            name = normalizedName,
            updatedAt = System.currentTimeMillis(),
        )
    }

    override suspend fun deleteById(
        id: String,
    ) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun copy(
        id: String,
        copiedName: String,
    ) {
        val source = dao.getById(id)
            ?: return

        val normalizedName = copiedName.trim()

        if (normalizedName.isEmpty()) {
            return
        }

        val now = System.currentTimeMillis()
        val sortOrder = dao.getNextSortOrder()

        val copy = source.toDomain().copy(
            id = UUID.randomUUID().toString(),
            name = normalizedName,
            sortOrder = sortOrder,
            createdAt = now,
            updatedAt = now,
        )

        dao.upsert(
            copy.toEntity(),
        )
    }

    override suspend fun updateIcon(
        id: String,
        iconKey: String,
    ) {
        val normalizedIconKey = iconKey.trim()

        if (normalizedIconKey.isEmpty()) {
            return
        }

        dao.updateIcon(
            id = id,
            iconKey = normalizedIconKey,
            updatedAt = System.currentTimeMillis(),
        )
    }
}
