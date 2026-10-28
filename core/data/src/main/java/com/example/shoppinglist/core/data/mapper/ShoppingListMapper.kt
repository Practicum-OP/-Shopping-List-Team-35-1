package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity
import com.example.shoppinglist.core.domain.lists.ShoppingList

fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
        iconKey = iconKey,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = id,
        name = name,
        iconKey = iconKey,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
