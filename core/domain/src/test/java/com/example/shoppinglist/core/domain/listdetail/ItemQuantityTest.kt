package com.example.shoppinglist.core.domain.listdetail

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ItemQuantityTest {

    @Test
    fun `decimal separator can be dot or comma`() {
        assertEquals("1.5", ItemQuantity.parse("1.5").toString())
        assertEquals("1.5", ItemQuantity.parse(" 1,5 ").toString())
    }

    @Test
    fun `equal amounts are the same quantity`() {
        assertEquals(ItemQuantity.parse("1.5"), ItemQuantity.parse("1.50"))
    }

    @Test
    fun `whole numbers keep plain notation`() {
        assertEquals("100", ItemQuantity.parse("100").toString())
    }

    @Test
    fun `only positive decimals are accepted`() {
        listOf("", "abc", "0", "0.00", "-1", "1.", "1e3").forEach { raw ->
            assertNull(raw, ItemQuantity.parse(raw))
        }
    }
}
