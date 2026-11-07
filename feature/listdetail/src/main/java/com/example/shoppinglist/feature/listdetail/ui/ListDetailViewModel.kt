package com.example.shoppinglist.feature.listdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ChangeItemsSortMode
import com.example.shoppinglist.core.domain.listdetail.interactor.ClearPurchasedShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteAllShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.MoveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveItemSuggestions
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingList
import com.example.shoppinglist.core.domain.listdetail.interactor.SaveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ToggleShoppingItemPurchased
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ListDetailViewModel(
    private val listId: String,
    observeShoppingList: ObserveShoppingList,
    observeShoppingItems: ObserveShoppingItems,
    observeItemSuggestions: ObserveItemSuggestions,
    private val saveShoppingItem: SaveShoppingItem,
    private val toggleShoppingItemPurchased: ToggleShoppingItemPurchased,
    private val deleteShoppingItem: DeleteShoppingItem,
    private val deleteAllShoppingItems: DeleteAllShoppingItems,
    private val clearPurchasedShoppingItems: ClearPurchasedShoppingItems,
    private val changeItemsSortMode: ChangeItemsSortMode,
    private val moveShoppingItem: MoveShoppingItem,
) : ViewModel() {

    private val listFlow = observeShoppingList(listId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val sortModeFlow = listFlow
        .map { it?.itemsSortMode ?: ItemsSortMode.ALPHABETICAL }
        .distinctUntilChanged()

    private var latestItems: List<ShoppingItem> = emptyList()

    private val itemsFlow = sortModeFlow
        .flatMapLatest { sortMode ->
            observeShoppingItems(listId, sortMode)
        }
        .onEach { items ->
            latestItems = items
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val formFlow = MutableStateFlow<ItemFormState?>(null)
    private val pendingDeleteItemIdFlow = MutableStateFlow<String?>(null)

    private val suggestionsFlow = formFlow
        .map { it?.name.orEmpty().trim() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                observeItemSuggestions(query).map { list ->
                    list.map { it.name }
                }
            }
        }

    val state: StateFlow<ListDetailState> = combine(
        listFlow,
        itemsFlow,
        formFlow,
        suggestionsFlow,
        pendingDeleteItemIdFlow,
    ) { list, items, form, suggestions, pendingDeleteId ->
        val content = when {
            list == null -> ListContent.Loading
            items.isEmpty() -> ListContent.Empty
            else -> ListContent.Items(items.map { it.toUi() })
        }

        ListDetailState(
            listId = listId,
            listName = list?.name.orEmpty(),
            content = content,
            sortMode = list?.itemsSortMode ?: ItemsSortMode.ALPHABETICAL,
            itemForm = form?.copy(suggestions = suggestions),
            pendingDeleteItemId = pendingDeleteId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListDetailState(
            listId = listId,
            listName = "",
            content = ListContent.Loading,
        ),
    )

    fun onEvent(event: ListDetailEvent) {
        when (event) {
            ListDetailEvent.AddItemClicked -> {
                formFlow.value = ItemFormState(mode = FormMode.Create)
            }

            ListDetailEvent.FormClosed -> {
                formFlow.value = null
            }

            ListDetailEvent.FormSubmitted -> {
                submitForm()
            }

            is ListDetailEvent.NameChanged -> {
                formFlow.update { it?.copy(name = event.name) }
            }

            is ListDetailEvent.SuggestionSelected -> {
                formFlow.update {
                    it?.copy(
                        name = event.suggestion,
                        suggestions = emptyList(),
                    )
                }
            }

            is ListDetailEvent.QuantityChanged -> {
                formFlow.update {
                    it?.copy(quantity = sanitizeQuantityInput(event.quantity))
                }
            }

            ListDetailEvent.QuantityIncreased -> {
                formFlow.update { current ->
                    current?.copy(quantity = stepQuantity(current.quantity, 1))
                }
            }

            ListDetailEvent.QuantityDecreased -> {
                formFlow.update { current ->
                    current?.copy(quantity = stepQuantity(current.quantity, -1))
                }
            }

            ListDetailEvent.UnitPickerOpened -> {
                formFlow.update { it?.copy(unitPickerVisible = true) }
            }

            ListDetailEvent.UnitPickerClosed -> {
                formFlow.update { it?.copy(unitPickerVisible = false) }
            }

            is ListDetailEvent.UnitSelected -> {
                formFlow.update {
                    it?.copy(
                        unit = event.unit,
                        unitPickerVisible = false,
                    )
                }
            }

            is ListDetailEvent.EditItemClicked -> {
                openEdit(event.itemId)
            }

            is ListDetailEvent.DeleteItemClicked -> {
                pendingDeleteItemIdFlow.value = event.itemId
            }

            ListDetailEvent.DeleteItemDismissed -> {
                pendingDeleteItemIdFlow.value = null
            }

            ListDetailEvent.DeleteItemConfirmed -> {
                val id = pendingDeleteItemIdFlow.value ?: return
                pendingDeleteItemIdFlow.value = null
                viewModelScope.launch {
                    deleteShoppingItem(id)
                }
            }

            is ListDetailEvent.PurchaseToggled -> {
                viewModelScope.launch {
                    toggleShoppingItemPurchased(event.itemId)
                }
            }

            is ListDetailEvent.SortModeSelected -> {
                viewModelScope.launch {
                    changeItemsSortMode(listId, event.mode)
                }
            }

            ListDetailEvent.DeleteAllItemsConfirmed -> {
                viewModelScope.launch {
                    deleteAllShoppingItems(listId)
                }
            }

            ListDetailEvent.ClearPurchasedConfirmed -> {
                viewModelScope.launch {
                    clearPurchasedShoppingItems(listId)
                }
            }

            is ListDetailEvent.ItemMoved -> {
                reorderItems(event.fromIndex, event.toIndex)
            }

            ListDetailEvent.BackClicked -> Unit
        }
    }

    private fun submitForm() {
        val form = formFlow.value ?: return
        if (!form.isSubmittable) return

        val itemId = when (val mode = form.mode) {
            FormMode.Create -> null
            is FormMode.Edit -> mode.itemId
        }

        formFlow.value = null

        viewModelScope.launch {
            saveShoppingItem(
                listId = listId,
                itemId = itemId,
                name = form.name,
                quantityText = form.quantity,
                unitKey = form.unit.orEmpty(),
            )
        }
    }

    private fun openEdit(itemId: String) {
        val item = latestItems.firstOrNull { it.id == itemId } ?: return
        formFlow.value = ItemFormState(
            mode = FormMode.Edit(itemId),
            name = item.name,
            quantity = item.quantity?.toString().orEmpty(),
            unit = item.unit?.toUnitKey(),
        )
    }

    private fun reorderItems(from: Int, to: Int) {
        if (from !in latestItems.indices || to !in latestItems.indices) return
        val list = latestItems.toMutableList()
        val item = list.removeAt(from)
        list.add(to, item)
        latestItems = list

        viewModelScope.launch {
            moveShoppingItem(list.map { it.id })
        }
    }

    private fun sanitizeQuantityInput(input: String): String {
        val trimmed = input.trim()
        val decimal = trimmed.replace(',', '.').toBigDecimalOrNull()
        if (decimal != null && decimal <= BigDecimal.ZERO) {
            return ""
        }
        return trimmed
    }

    private fun stepQuantity(currentText: String, step: Int): String {
        val current = currentText.replace(',', '.').toBigDecimalOrNull() ?: BigDecimal.ZERO
        val result = current + BigDecimal(step)
        return if (result <= BigDecimal.ZERO) {
            ""
        } else {
            result.stripTrailingZeros().toPlainString()
        }
    }

    private fun ShoppingItem.toUi(): ShoppingItemUi {
        return ShoppingItemUi(
            id = id,
            name = name,
            quantity = quantity?.toString().orEmpty(),
            unit = unit?.toUnitKey(),
            isPurchased = isPurchased,
        )
    }

    private fun com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.toUnitKey(): String {
        return when (this) {
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.PIECES -> ItemUnitUi.PIECE.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.KILOGRAMS -> ItemUnitUi.KILOGRAM.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.GRAMS -> ItemUnitUi.GRAM.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.LITERS -> ItemUnitUi.LITER.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.MILLILITERS -> ItemUnitUi.MILLILITER.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.PACKAGE -> ItemUnitUi.PACKAGE.key
            com.example.shoppinglist.core.domain.listdetail.MeasurementUnit.PACK -> ItemUnitUi.PACK.key
        }
    }
}
