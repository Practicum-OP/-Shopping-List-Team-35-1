package com.example.shoppinglist.core.data.repository

import com.example.shoppinglist.core.data.mapper.toDomain
import com.example.shoppinglist.core.data.room.dao.ShoppingListDao
import com.example.shoppinglist.core.domain.listdetail.ListDetailRepository
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.core.domain.lists.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListDetailRepositoryImpl(
    private val dao: ShoppingListDao,
) : ListDetailRepository {

    override fun observeList(listId: String): Flow<ShoppingList?> {
        return dao.observeById(listId).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun updateItemsSortMode(
        listId: String,
        mode: ItemsSortMode,
    ) {
        dao.updateItemsSortMode(
            id = listId,
            mode = mode.name,
            updatedAt = System.currentTimeMillis(),
        )
    }
}
