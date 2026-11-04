package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.data.room.entity.ShoppingItemEntity
import com.example.shoppinglist.core.domain.listdetail.ItemQuantity
import com.example.shoppinglist.core.domain.listdetail.MeasurementUnit
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShoppingItemMapperTest {

    private val item = ShoppingItem(
        id = "item",
        listId = "list",
        name = "Молоко",
        quantity = ItemQuantity.parse("1.5"),
        unit = MeasurementUnit.LITERS,
        isPurchased = true,
        position = 2,
        createdAt = 10,
        updatedAt = 20,
    )

    @Test
    fun `item survives round trip through entity`() {
        assertEquals(item, item.toEntity().toDomain())
    }

    @Test
    fun `quantity and unit are stored as stable strings`() {
        val entity = item.toEntity()

        assertEquals("1.5", entity.quantity)
        assertEquals("LITERS", entity.unit)
    }

    @Test
    fun `quantity and unit are optional`() {
        val entity = item.copy(quantity = null, unit = null).toEntity()

        assertNull(entity.quantity)
        assertNull(entity.unit)
        assertEquals(item.copy(quantity = null, unit = null), entity.toDomain())
    }

    @Test
    fun `unknown stored values are dropped`() {
        val entity: ShoppingItemEntity = item.toEntity().copy(quantity = "много", unit = "BOXES")

        assertNull(entity.toDomain().quantity)
        assertNull(entity.toDomain().unit)
    }
}
