package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ItemHistory
import com.example.shoppinglist.core.domain.listdetail.ItemHistoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveItemSuggestions(
    private val repository: ItemHistoryRepository,
) {
    operator fun invoke(
        query: String,
        limit: Int = 8,
    ): Flow<List<ItemHistory>> {
        return repository.observeSuggestions(query, limit)
    }
}
