package com.example.shoppinglist.core.domain.lists.interactor

import com.example.shoppinglist.core.domain.lists.ShoppingList
import com.example.shoppinglist.core.domain.lists.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class ObserveShoppingLists(
    private val repository: ShoppingListRepository,
) {
    operator fun invoke(): Flow<List<ShoppingList>> {
        return repository.observeAll()
    }
}

class CreateShoppingList(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke(name: String) {
        val normalizedName = name.trim()

        if (normalizedName.isEmpty()) return

        repository.create(
            name = normalizedName,
        )
    }
}

class RenameShoppingList(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke(
        id: String,
        name: String,
    ) {
        val normalizedName = name.trim()

        if (normalizedName.isEmpty()) return

        repository.rename(
            id = id,
            name = normalizedName,
        )
    }
}

class DeleteShoppingList(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke(id: String) {
        repository.deleteById(id)
    }
}

class DeleteAllShoppingLists(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke() {
        repository.deleteAll()
    }
}

class CopyShoppingList(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke(id: String) {
        val source = repository.getById(id) ?: return

        repository.copy(
            id = id,
            copiedName = "${source.name} (Копия)",
        )
    }
}

class ChangeShoppingListIcon(
    private val repository: ShoppingListRepository,
) {
    suspend operator fun invoke(
        id: String,
        iconKey: String,
    ) {
        repository.updateIcon(
            id = id,
            iconKey = iconKey,
        )
    }
}
