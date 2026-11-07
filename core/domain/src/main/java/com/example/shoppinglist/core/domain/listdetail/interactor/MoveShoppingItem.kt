package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository

class MoveShoppingItem(
    private val repository: ShoppingItemRepository,
) {
    suspend operator fun invoke(
        orderedIds: List<String>,
    ) {
        if (orderedIds.isNotEmpty()) {
            repository.updatePositions(orderedIds)
        }
    }
}
