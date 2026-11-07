package com.example.shoppinglist.core.domain.listdetail

import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.core.domain.lists.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ListDetailRepository {

    fun observeList(listId: String): Flow<ShoppingList?>

    suspend fun updateItemsSortMode(
        listId: String,
        mode: ItemsSortMode,
    )
}
