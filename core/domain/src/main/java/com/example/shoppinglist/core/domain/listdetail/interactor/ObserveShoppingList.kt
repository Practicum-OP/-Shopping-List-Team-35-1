package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ListDetailRepository
import com.example.shoppinglist.core.domain.lists.ShoppingList
import kotlinx.coroutines.flow.Flow

class ObserveShoppingList(
    private val repository: ListDetailRepository,
) {
    operator fun invoke(listId: String): Flow<ShoppingList?> {
        return repository.observeList(listId)
    }
}
