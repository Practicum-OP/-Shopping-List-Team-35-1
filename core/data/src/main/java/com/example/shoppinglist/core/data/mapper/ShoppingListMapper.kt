package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.core.domain.lists.ShoppingList

fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
        iconKey = iconKey,
        itemsSortMode = ItemsSortMode.entries.firstOrNull { mode ->
            mode.name == itemsSortMode
        } ?: ItemsSortMode.CUSTOM,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = id,
        name = name,
        iconKey = iconKey,
        itemsSortMode = itemsSortMode.name,
        sortOrder = sortOrder,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
