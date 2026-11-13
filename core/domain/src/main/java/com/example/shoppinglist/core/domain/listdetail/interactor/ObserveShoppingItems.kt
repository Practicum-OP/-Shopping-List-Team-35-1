package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import kotlinx.coroutines.flow.Flow

class ObserveShoppingItems(
    private val repository: ShoppingItemRepository,
) {
    operator fun invoke(
        listId: String,
        sortMode: ItemsSortMode,
    ): Flow<List<ShoppingItem>> {
        return repository.observeByListId(listId, sortMode)
    }
}
