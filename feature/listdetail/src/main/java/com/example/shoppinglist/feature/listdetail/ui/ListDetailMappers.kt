package com.example.shoppinglist.feature.listdetail.ui

import com.example.shoppinglist.core.domain.listdetail.MeasurementUnit
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import java.math.BigDecimal

internal fun sanitizeQuantityInput(input: String): String {
    val trimmed = input.trim()
    val decimal = trimmed.replace(',', '.').toBigDecimalOrNull()
    return if (decimal != null && decimal <= BigDecimal.ZERO) "" else trimmed
}

internal fun stepQuantity(currentText: String, step: Int): String {
    val current = currentText.replace(',', '.').toBigDecimalOrNull() ?: BigDecimal.ZERO
    val result = current + BigDecimal(step)
    return if (result <= BigDecimal.ZERO) {
        ""
    } else {
        result.stripTrailingZeros().toPlainString()
    }
}

internal fun ShoppingItem.toUi(): ShoppingItemUi {
    return ShoppingItemUi(
        id = id,
        name = name,
        quantity = quantity?.toString().orEmpty(),
        unit = unit?.toUnitKey(),
        isPurchased = isPurchased,
    )
}

internal fun MeasurementUnit.toUnitKey(): String {
    return when (this) {
        MeasurementUnit.PIECES -> ItemUnitUi.PIECE.key
        MeasurementUnit.KILOGRAMS -> ItemUnitUi.KILOGRAM.key
        MeasurementUnit.GRAMS -> ItemUnitUi.GRAM.key
        MeasurementUnit.LITERS -> ItemUnitUi.LITER.key
        MeasurementUnit.MILLILITERS -> ItemUnitUi.MILLILITER.key
        MeasurementUnit.PACKAGE -> ItemUnitUi.PACKAGE.key
        MeasurementUnit.PACK -> ItemUnitUi.PACK.key
    }
}
