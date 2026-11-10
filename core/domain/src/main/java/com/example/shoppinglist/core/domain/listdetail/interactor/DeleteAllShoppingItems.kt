package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository

class DeleteAllShoppingItems(
    private val repository: ShoppingItemRepository,
) {
    suspend operator fun invoke(listId: String) {
        repository.deleteByListId(listId)
    }
}
