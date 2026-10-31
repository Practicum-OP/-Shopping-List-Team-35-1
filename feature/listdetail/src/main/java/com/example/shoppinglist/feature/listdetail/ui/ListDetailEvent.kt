package com.example.shoppinglist.feature.listdetail.ui

import com.example.shoppinglist.core.domain.lists.ItemsSortMode

sealed interface ListDetailEvent {
    //верхнее меню и пункты меню
    data object BackClicked : ListDetailEvent

    data class SortModeSelected(
        val mode: ItemsSortMode
    ) : ListDetailEvent

    data object DeleteAllItemsConfirmed : ListDetailEvent

    data object ClearPurchasedConfirmed : ListDetailEvent

    //действия над элементами списка
    data class PurchaseToggled(
        val itemId: String
    ) : ListDetailEvent

    data class EditItemClicked(
        val itemId: String
    ) : ListDetailEvent

    data class DeleteItemClicked(
        val itemId: String
    ) : ListDetailEvent

    data class ItemMoved(
        val fromIndex: Int,
        val toIndex: Int
    ) : ListDetailEvent

    //создание и редактирование товара
    data object AddItemClicked : ListDetailEvent

    data object FormSubmitted : ListDetailEvent

    data object FormClosed : ListDetailEvent

    data class NameChanged(
        val name: String
    ) : ListDetailEvent

    data class SuggestionSelected(
        val suggestion: String
    ) : ListDetailEvent

    data class QuantityChanged(
        val quantity: String
    ) : ListDetailEvent

    data object QuantityIncreased : ListDetailEvent

    data object QuantityDecreased : ListDetailEvent

    data object UnitPickerOpened : ListDetailEvent

    data object UnitPickerClosed : ListDetailEvent

    data class UnitSelected(
        val unit: String
    ) : ListDetailEvent
}
