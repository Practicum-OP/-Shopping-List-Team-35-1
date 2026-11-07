package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository

class ToggleShoppingItemPurchased(
    private val repository: ShoppingItemRepository,
) {
    suspend operator fun invoke(itemId: String) {
        val item = repository.getById(itemId) ?: return
        repository.updatePurchased(
            id = itemId,
            isPurchased = !item.isPurchased,
        )
    }
}
