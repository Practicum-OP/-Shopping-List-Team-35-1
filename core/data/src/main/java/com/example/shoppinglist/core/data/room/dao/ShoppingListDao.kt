package com.example.shoppinglist.core.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppinglist.core.data.room.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
@Dao
interface ShoppingListDao {

    @Query(
        """
        SELECT *
        FROM shopping_lists
        ORDER BY sortOrder ASC, createdAt ASC
        """
    )
    fun observeAll(): Flow<List<ShoppingListEntity>>

    @Query(
        """
        SELECT *
        FROM shopping_lists
        WHERE id = :id
        LIMIT 1
        """
    )
    fun observeById(id: String): Flow<ShoppingListEntity?>

    @Query(
        """
        SELECT *
        FROM shopping_lists
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: String): ShoppingListEntity?

    @Query(
        """
        SELECT COALESCE(MAX(sortOrder), -1) + 1
        FROM shopping_lists
        """
    )
    suspend fun getNextSortOrder(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ShoppingListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ShoppingListEntity>)

    @Delete
    suspend fun delete(entity: ShoppingListEntity)

    @Query(
        """
        DELETE FROM shopping_lists
        WHERE id = :id
        """
    )
    suspend fun deleteById(id: String)

    @Query(
        """
        DELETE FROM shopping_lists
        """
    )
    suspend fun deleteAll()

    @Query(
        """
        UPDATE shopping_lists
        SET name = :name,
            updatedAt = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun rename(
        id: String,
        name: String,
        updatedAt: Long
    )

    @Query(
        """
        UPDATE shopping_lists
        SET sortOrder = :sortOrder,
            updatedAt = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun updateSortOrder(
        id: String,
        sortOrder: Int,
        updatedAt: Long
    )
}
