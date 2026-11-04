package com.example.shoppinglist.core.data.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shoppinglist.core.data.mapper.toEntity
import com.example.shoppinglist.core.data.mapper.toItemHistoryKey
import com.example.shoppinglist.core.data.room.ShoppingListDatabase
import com.example.shoppinglist.core.domain.listdetail.ItemHistory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemHistoryDaoTest {

    private lateinit var database: ShoppingListDatabase
    private lateinit var dao: ItemHistoryDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ShoppingListDatabase::class.java,
        ).build()
        dao = database.itemHistoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun suggestionsMatchCyrillicQueryInAnyCaseNewestFirst() = runTest {
        insert(name = "Молоко", lastUsedAt = 1)
        insert(name = "Сгущённое молоко", lastUsedAt = 2)
        insert(name = "Хлеб", lastUsedAt = 3)

        assertEquals(listOf("Сгущённое молоко", "Молоко"), suggestions("МОЛ"))
    }

    @Test
    fun sameNameInOtherCaseReplacesRecord() = runTest {
        insert(name = "Молоко", lastUsedAt = 1)
        insert(name = "молоко", lastUsedAt = 2)

        assertEquals(listOf("молоко"), suggestions("мол"))
    }

    @Test
    fun likeWildcardsAreMatchedLiterally() = runTest {
        insert(name = "Молоко", lastUsedAt = 1)

        assertEquals(emptyList<String>(), suggestions("%"))
    }

    @Test
    fun limitCutsOldestSuggestions() = runTest {
        insert(name = "Молоко", lastUsedAt = 1)
        insert(name = "Мука", lastUsedAt = 2)

        assertEquals(listOf("Мука"), suggestions("м", limit = 1))
    }

    private suspend fun insert(name: String, lastUsedAt: Long) {
        dao.upsert(ItemHistory(name = name, lastUsedAt = lastUsedAt).toEntity())
    }

    private suspend fun suggestions(query: String, limit: Int = 10): List<String> {
        return dao.observeSuggestions(query.toItemHistoryKey(), limit).first().map { it.name }
    }
}
