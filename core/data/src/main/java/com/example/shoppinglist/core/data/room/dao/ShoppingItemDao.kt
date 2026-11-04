package com.example.shoppinglist.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.shoppinglist.core.data.room.entity.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
@Dao
interface ShoppingItemDao {

    /**
     * Товары списка в ручном порядке.
     */
    @Query(
        """
        SELECT *
        FROM shopping_items
        WHERE listId = :listId
        ORDER BY position ASC, createdAt ASC
        """,
    )
    fun observeByListId(listId: String): Flow<List<ShoppingItemEntity>>

    /**
     * Товары списка по алфавиту.
     */
    @Query(
        """
        SELECT *
        FROM shopping_items
        WHERE listId = :listId
        ORDER BY name ASC, createdAt ASC
        """,
    )
    fun observeByListIdAlphabetically(listId: String): Flow<List<ShoppingItemEntity>>

    @Query(
        """
        SELECT *
        FROM shopping_items
        WHERE listId = :listId
        ORDER BY position ASC, createdAt ASC
        """,
    )
    suspend fun getByListId(listId: String): List<ShoppingItemEntity>

    @Query(
        """
        SELECT *
        FROM shopping_items
        WHERE id = :id
        LIMIT 1
        """,
    )
    suspend fun getById(id: String): ShoppingItemEntity?

    @Query(
        """
        SELECT COALESCE(MAX(position), -1) + 1
        FROM shopping_items
        WHERE listId = :listId
        """,
    )
    suspend fun getNextPosition(listId: String): Int

    @Upsert
    suspend fun upsert(entity: ShoppingItemEntity)

    @Upsert
    suspend fun upsertAll(entities: List<ShoppingItemEntity>)

    @Query(
        """
        DELETE FROM shopping_items
        WHERE id = :id
        """,
    )
    suspend fun deleteById(id: String)

    @Query(
        """
        DELETE FROM shopping_items
        WHERE listId = :listId
        """,
    )
    suspend fun deleteByListId(listId: String)

    @Query(
        """
        DELETE FROM shopping_items
        WHERE listId = :listId AND isPurchased = 1
        """,
    )
    suspend fun deletePurchased(listId: String)

    @Query(
        """
        UPDATE shopping_items
        SET isPurchased = :isPurchased,
            updatedAt = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updatePurchased(
        id: String,
        isPurchased: Boolean,
        updatedAt: Long,
    )

    @Query(
        """
        UPDATE shopping_items
        SET position = :position,
            updatedAt = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updatePosition(
        id: String,
        position: Int,
        updatedAt: Long,
    )

    /**
     * Сохраняет ручной порядок: позиция товара равна его индексу в [orderedIds].
     */
    @Transaction
    suspend fun updatePositions(
        orderedIds: List<String>,
        updatedAt: Long,
    ) {
        orderedIds.forEachIndexed { position, id ->
            updatePosition(id, position, updatedAt)
        }
    }
}
