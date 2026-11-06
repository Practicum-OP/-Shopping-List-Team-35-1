package com.example.shoppinglist.feature.listdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.feature.listdetail.R

/**
 * Пока нет ViewModel, State хранится здесь: экран можно открыть и проверить вручную,
 * но товары не сохраняются, а подтверждения удаления ничего не меняют.
 */
@Composable
fun ListDetailRoute(
    onBackToLists: () -> Unit,
) {
    val listName = stringResource(R.string.list_detail_title)
    var state by rememberSaveable {
        mutableStateOf(
            ListDetailState(
                listId = SAMPLE_LIST_ID,
                listName = listName,
                content = ListContent.Items(sampleItems),
            ),
        )
    }

    ListDetailScreen(
        state = state,
        onEvent = { event ->
            if (event == ListDetailEvent.BackClicked) {
                onBackToLists()
            } else {
                state = state.reduceList(event).reduceForm(event)
            }
        },
    )
}

private fun ListDetailState.reduceList(event: ListDetailEvent): ListDetailState = when (event) {
    is ListDetailEvent.SortModeSelected -> copy(sortMode = event.mode)
    is ListDetailEvent.PurchaseToggled -> togglePurchased(event.itemId)
    ListDetailEvent.AddItemClicked -> copy(itemForm = ItemFormState(mode = FormMode.Create))
    is ListDetailEvent.EditItemClicked -> openEditForm(event.itemId)
    ListDetailEvent.FormSubmitted, ListDetailEvent.FormClosed -> copy(itemForm = null)
    else -> this
}

private fun ListDetailState.reduceForm(event: ListDetailEvent): ListDetailState {
    val form = itemForm ?: return this
    val updated = when (event) {
        is ListDetailEvent.NameChanged -> form.copy(name = event.name)
        is ListDetailEvent.SuggestionSelected -> form.copy(name = event.suggestion)
        is ListDetailEvent.QuantityChanged -> form.copy(quantity = event.quantity)
        ListDetailEvent.UnitPickerOpened -> form.copy(unitPickerVisible = true)
        ListDetailEvent.UnitPickerClosed -> form.copy(unitPickerVisible = false)
        is ListDetailEvent.UnitSelected -> form.copy(unit = event.unit, unitPickerVisible = false)
        else -> form
    }
    return copy(itemForm = updated)
}

private fun ListDetailState.togglePurchased(itemId: String): ListDetailState {
    val items = (content as? ListContent.Items)?.items ?: return this
    return copy(
        content = ListContent.Items(
            items.map { item ->
                if (item.id == itemId) item.copy(isPurchased = !item.isPurchased) else item
            },
        ),
    )
}

private fun ListDetailState.openEditForm(itemId: String): ListDetailState {
    val item = (content as? ListContent.Items)?.items?.firstOrNull { it.id == itemId } ?: return this
    return copy(
        itemForm = ItemFormState(
            mode = FormMode.Edit(itemId),
            name = item.name,
            quantity = item.quantity,
            unit = item.unit,
        ),
    )
}

private const val SAMPLE_LIST_ID = "sample"

private val sampleItems = listOf(
    ShoppingItemUi("1", "Молоко", "1", ItemUnitUi.LITER.key, isPurchased = false),
    ShoppingItemUi("2", "Хлеб", "", null, isPurchased = false),
    ShoppingItemUi("3", "Яблоки", "1.5", ItemUnitUi.KILOGRAM.key, isPurchased = false),
    ShoppingItemUi("4", "Яйца", "10", ItemUnitUi.PIECE.key, isPurchased = true),
    ShoppingItemUi("5", "Чай", "2", ItemUnitUi.PACK.key, isPurchased = true),
)
