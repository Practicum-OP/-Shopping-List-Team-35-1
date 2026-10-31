package com.example.shoppinglist.feature.listdetail.ui

import com.example.shoppinglist.core.domain.lists.ItemsSortMode

data class ListDetailState(
    val listId: String,
    val listName: String,
    val content: ListContent = ListContent.Loading,
    val sortMode: ItemsSortMode = ItemsSortMode.ALPHABETICAL,
    val itemForm: ItemFormState? = null,
)

//временная модель для отображения
data class ShoppingItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val unit: String?,
    val isPurchased: Boolean,
)

sealed interface ListContent {
    data object Loading : ListContent
    data object Empty : ListContent
    data class Items(val items: List<ShoppingItemUi>) : ListContent //позже заменить на настоящий список
}

data class ItemFormState(
    val mode: FormMode,
    val name: String = "",
    val quantity: String = "",
    val unit: String? = null,
    val nameError: String? = null,
    val quantityError: String? = null,
    val suggestions: List<String> = emptyList(),
    val unitPickerVisible: Boolean = false,
)

sealed interface FormMode {
    data object Create : FormMode
    data class Edit(val itemId: String) : FormMode
}
