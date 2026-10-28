package com.example.shoppinglist.core.domain.lists

data class ShoppingList(
    val id: String,
    val name: String,
    val iconKey: String = DEFAULT_ICON_KEY,
    val sortOrder: Int = 0,
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        const val DEFAULT_ICON_KEY = "shopping_cart"
    }
}
