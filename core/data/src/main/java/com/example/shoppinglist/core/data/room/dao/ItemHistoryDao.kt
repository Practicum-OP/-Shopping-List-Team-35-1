package com.example.shoppinglist.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.shoppinglist.core.data.room.entity.ItemHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemHistoryDao {

    /**
     * Подсказки по имени товара: недавно использованные сверху.
     *
     * @param normalizedQuery строка поиска, приведённая через `toItemHistoryKey`.
     */
    @Query(
        """
        SELECT *
        FROM item_history
        WHERE instr(normalizedName, :normalizedQuery) > 0
        ORDER BY lastUsedAt DESC
        LIMIT :limit
        """,
    )
    fun observeSuggestions(
        normalizedQuery: String,
        limit: Int,
    ): Flow<List<ItemHistoryEntity>>

    @Upsert
    suspend fun upsert(entity: ItemHistoryEntity)
}
