package com.example.shoppinglist.core.data.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shoppinglist.core.data.room.ShoppingListDatabase
import com.example.shoppinglist.core.data.room.entity.ShoppingItemEntity
import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingItemDaoTest {

    private lateinit var database: ShoppingListDatabase
    private lateinit var listDao: ShoppingListDao
    private lateinit var itemDao: ShoppingItemDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ShoppingListDatabase::class.java,
        ).build()
        listDao = database.shoppingListDao()
        itemDao = database.shoppingItemDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun itemsAreObservedInManualOrderOfTheirListOnly() = runTest {
        insertList(LIST_ID)
        insertList(OTHER_LIST_ID)
        itemDao.upsertAll(
            listOf(
                item(id = "b", position = 1),
                item(id = "a", position = 0),
                item(id = "other", listId = OTHER_LIST_ID, position = 0),
            ),
        )

        assertEquals(listOf("a", "b"), itemDao.observeByListId(LIST_ID).first().map { it.id })
    }

    @Test
    fun alphabeticalOrderIgnoresCaseOfCyrillicNames() = runTest {
        insertList(LIST_ID)
        itemDao.upsertAll(
            listOf(
                item(id = "1", name = "яблоки", position = 0),
                item(id = "2", name = "Ёлка", position = 1),
                item(id = "3", name = "Бананы", position = 2),
                item(id = "4", name = "арбуз", position = 3),
            ),
        )

        assertEquals(
            listOf("арбуз", "Бананы", "Ёлка", "яблоки"),
            itemDao.observeByListIdAlphabetically(LIST_ID).first().map { it.name },
        )
    }

    @Test
    fun nextPositionFollowsLastItemOfTheList() = runTest {
        insertList(LIST_ID)
        assertEquals(0, itemDao.getNextPosition(LIST_ID))

        itemDao.upsert(item(id = "a", position = 4))

        assertEquals(5, itemDao.getNextPosition(LIST_ID))
    }

    @Test
    fun updatePositionsStoresGivenOrder() = runTest {
        insertList(LIST_ID)
        itemDao.upsertAll(listOf(item(id = "a", position = 0), item(id = "b", position = 1)))

        itemDao.updatePositions(orderedIds = listOf("b", "a"), updatedAt = 50)

        assertEquals(listOf("b", "a"), itemDao.getByListId(LIST_ID).map { it.id })
        assertEquals(50L, itemDao.getById("a")?.updatedAt)
    }

    @Test
    fun updatePurchasedChangesOnlyThatItem() = runTest {
        insertList(LIST_ID)
        itemDao.upsertAll(listOf(item(id = "a", position = 0), item(id = "b", position = 1)))

        itemDao.updatePurchased(id = "a", isPurchased = true, updatedAt = 50)

        assertTrue(itemDao.getById("a")?.isPurchased == true)
        assertFalse(itemDao.getById("b")?.isPurchased == true)
    }

    @Test
    fun deletePurchasedKeepsOtherItemsAndLists() = runTest {
        insertList(LIST_ID)
        insertList(OTHER_LIST_ID)
        itemDao.upsertAll(
            listOf(
                item(id = "bought", position = 0, isPurchased = true),
                item(id = "left", position = 1),
                item(id = "other", listId = OTHER_LIST_ID, position = 0, isPurchased = true),
            ),
        )

        itemDao.deletePurchased(LIST_ID)

        assertEquals(listOf("left"), itemDao.getByListId(LIST_ID).map { it.id })
        assertEquals(listOf("other"), itemDao.getByListId(OTHER_LIST_ID).map { it.id })
    }

    @Test
    fun deletingListDeletesItsItems() = runTest {
        insertList(LIST_ID)
        itemDao.upsert(item(id = "a", position = 0))

        listDao.deleteById(LIST_ID)

        assertNull(itemDao.getById("a"))
    }

    @Test
    fun upsertingListKeepsItsItems() = runTest {
        insertList(LIST_ID)
        itemDao.upsert(item(id = "a", position = 0))

        insertList(LIST_ID)

        assertEquals(listOf("a"), itemDao.getByListId(LIST_ID).map { it.id })
    }

    private suspend fun insertList(id: String) {
        listDao.upsert(
            ShoppingListEntity(
                id = id,
                name = id,
                iconKey = "shopping_cart",
                itemsSortMode = "CUSTOM",
                sortOrder = 0,
                createdAt = 0,
                updatedAt = 0,
            ),
        )
    }

    private fun item(
        id: String,
        position: Int,
        name: String = id,
        listId: String = LIST_ID,
        isPurchased: Boolean = false,
    ) = ShoppingItemEntity(
        id = id,
        listId = listId,
        name = name,
        quantity = null,
        unit = null,
        isPurchased = isPurchased,
        position = position,
        createdAt = 0,
        updatedAt = 0,
    )

    private companion object {
        const val LIST_ID = "list"
        const val OTHER_LIST_ID = "other-list"
    }
}
