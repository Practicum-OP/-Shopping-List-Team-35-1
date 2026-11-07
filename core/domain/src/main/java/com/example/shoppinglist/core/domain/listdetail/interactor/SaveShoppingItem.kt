package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ItemHistoryRepository
import com.example.shoppinglist.core.domain.listdetail.ItemQuantity
import com.example.shoppinglist.core.domain.listdetail.MeasurementUnit
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import com.example.shoppinglist.core.domain.listdetail.ShoppingItemRepository
import java.util.UUID

class SaveShoppingItem(
    private val itemRepository: ShoppingItemRepository,
    private val historyRepository: ItemHistoryRepository,
) {
    suspend operator fun invoke(
        listId: String,
        itemId: String?,
        name: String,
        quantityText: String,
        unitKey: String,
    ) {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) return

        val quantity = ItemQuantity.parse(quantityText) ?: return
        val unit = unitKey.toMeasurementUnit() ?: return

        val oldItem = itemId?.let { itemRepository.getById(it) }
        val now = System.currentTimeMillis()

        val item = ShoppingItem(
            id = itemId ?: UUID.randomUUID().toString(),
            listId = listId,
            name = normalizedName,
            quantity = quantity,
            unit = unit,
            isPurchased = oldItem?.isPurchased ?: false,
            position = oldItem?.position ?: itemRepository.getNextPosition(listId),
            createdAt = oldItem?.createdAt ?: now,
            updatedAt = now,
        )

        itemRepository.upsert(item)
        historyRepository.saveUsedItem(normalizedName)
    }

    private fun String.toMeasurementUnit(): MeasurementUnit? {
        return when (this) {
            "PIECE" -> MeasurementUnit.PIECES
            "KILOGRAM" -> MeasurementUnit.KILOGRAMS
            "GRAM" -> MeasurementUnit.GRAMS
            "LITER" -> MeasurementUnit.LITERS
            "MILLILITER" -> MeasurementUnit.MILLILITERS
            "PACKAGE" -> MeasurementUnit.PACKAGE
            "PACK" -> MeasurementUnit.PACK
            else -> null
        }
    }
}
