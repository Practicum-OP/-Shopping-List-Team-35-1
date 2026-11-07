package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository

class DeleteShoppingItem(
    private val repository: ShoppingItemRepository,
) {
    suspend operator fun invoke(itemId: String) {
        repository.deleteById(itemId)
    }
}
