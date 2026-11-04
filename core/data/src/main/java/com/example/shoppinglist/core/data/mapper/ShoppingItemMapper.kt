package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.data.room.entity.ShoppingItemEntity
import com.example.shoppinglist.core.domain.listdetail.ItemQuantity
import com.example.shoppinglist.core.domain.listdetail.MeasurementUnit
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        listId = listId,
        name = name,
        quantity = quantity?.let(ItemQuantity::parse),
        unit = MeasurementUnit.entries.firstOrNull { measurementUnit ->
            measurementUnit.name == unit
        },
        isPurchased = isPurchased,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = id,
        listId = listId,
        name = name,
        quantity = quantity?.toString(),
        unit = unit?.name,
        isPurchased = isPurchased,
        position = position,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
