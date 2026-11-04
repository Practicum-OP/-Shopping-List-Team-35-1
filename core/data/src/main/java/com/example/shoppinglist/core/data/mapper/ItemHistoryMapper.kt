package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.data.room.entity.ItemHistoryEntity
import com.example.shoppinglist.core.domain.listdetail.ItemHistory

/**
 * Ключ истории: одно имя в разном регистре хранится одной записью.
 * Приводится в Kotlin, потому что lower() в SQLite не работает с кириллицей.
 */
fun String.toItemHistoryKey(): String = trim().lowercase()

fun ItemHistoryEntity.toDomain(): ItemHistory {
    return ItemHistory(
        name = name,
        lastUsedAt = lastUsedAt,
    )
}

fun ItemHistory.toEntity(): ItemHistoryEntity {
    return ItemHistoryEntity(
        normalizedName = name.toItemHistoryKey(),
        name = name.trim(),
        lastUsedAt = lastUsedAt,
    )
}
