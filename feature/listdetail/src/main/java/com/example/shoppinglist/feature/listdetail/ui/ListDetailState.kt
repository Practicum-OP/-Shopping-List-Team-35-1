package com.example.shoppinglist.feature.listdetail.ui

import android.os.Parcelable
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListDetailState(
    val listId: String,
    val listName: String,
    val content: ListContent = ListContent.Loading,
    val sortMode: ItemsSortMode = ItemsSortMode.ALPHABETICAL,
    val itemForm: ItemFormState? = null,
) : Parcelable

// временная модель для отображения
@Parcelize
data class ShoppingItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val unit: String?,
    val isPurchased: Boolean,
) : Parcelable

sealed interface ListContent : Parcelable {
    @Parcelize
    data object Loading : ListContent

    @Parcelize
    data object Empty : ListContent

    @Parcelize
    data class Items(val items: List<ShoppingItemUi>) : ListContent // позже заменить на настоящий список
}

@Parcelize
data class ItemFormState(
    val mode: FormMode,
    val name: String = "",
    val quantity: String = "",
    val unit: String? = null,
    val nameError: String? = null,
    val quantityError: String? = null,
    val suggestions: List<String> = emptyList(),
    val unitPickerVisible: Boolean = false,
) : Parcelable

sealed interface FormMode : Parcelable {
    @Parcelize
    data object Create : FormMode

    @Parcelize
    data class Edit(val itemId: String) : FormMode
}
