package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository

class ClearPurchasedShoppingItems(
    private val repository: ShoppingItemRepository,
) {
    suspend operator fun invoke(listId: String) {
        repository.deletePurchased(listId)
    }
}
