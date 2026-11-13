package com.example.shoppinglist.core.data.mapper

import com.example.shoppinglist.core.domain.listdetail.ItemHistory
import org.junit.Assert.assertEquals
import org.junit.Test

class ItemHistoryMapperTest {

    @Test
    fun `key ignores case and surrounding spaces`() {
        assertEquals("молоко", "  МолОко ".toItemHistoryKey())
    }

    @Test
    fun `entity keeps the name as user typed it`() {
        val entity = ItemHistory(name = " Молоко ", lastUsedAt = 10).toEntity()

        assertEquals("молоко", entity.normalizedName)
        assertEquals("Молоко", entity.name)
        assertEquals(ItemHistory(name = "Молоко", lastUsedAt = 10), entity.toDomain())
    }
}
