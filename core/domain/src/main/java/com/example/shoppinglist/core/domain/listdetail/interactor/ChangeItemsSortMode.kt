package com.example.shoppinglist.core.domain.listdetail.interactor

import com.example.shoppinglist.core.domain.listdetail.ListDetailRepository
import com.example.shoppinglist.core.domain.lists.ItemsSortMode

class ChangeItemsSortMode(
    private val repository: ListDetailRepository,
) {
    suspend operator fun invoke(
        listId: String,
        mode: ItemsSortMode,
    ) {
        repository.updateItemsSortMode(
            listId = listId,
            mode = mode,
        )
    }
}
