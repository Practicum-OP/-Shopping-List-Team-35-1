package com.example.shoppinglist.core.domain.listdetail

data class ShoppingItem(
    val id: String,
    val listId: String,
    val name: String,
    val quantity: ItemQuantity? = null,
    val unit: MeasurementUnit? = null,
    val isPurchased: Boolean = false,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long,
)
