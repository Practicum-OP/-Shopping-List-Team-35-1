package com.example.shoppinglist.feature.listdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.listdetail.ShoppingItem
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

private const val STOP_TIMEOUT_MILLIS = 5_000L

class ListDetailViewModel(
    private val listId: String,
    private val interactors: ListDetailInteractors,
) : ViewModel() {

    private val listFlow = interactors.observeShoppingList(listId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = null,
        )

    private val sortModeFlow = listFlow
        .map { it?.itemsSortMode ?: ItemsSortMode.ALPHABETICAL }
        .distinctUntilChanged()

    private var latestItems: List<ShoppingItem> = emptyList()

    private val itemsFlow = sortModeFlow
        .flatMapLatest { sortMode ->
            interactors.observeShoppingItems(listId, sortMode)
        }
        .onEach { items ->
            latestItems = items
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
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
                interactors.observeItemSuggestions(query).map { list ->
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
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ListDetailState(
            listId = listId,
            listName = "",
            content = ListContent.Loading,
        ),
    )

    fun onEvent(event: ListDetailEvent) {
        if (!handleFormEvent(event)) {
            handleItemsEvent(event)
        }
    }

    /** Обрабатывает события формы добавления/редактирования. Возвращает true, если событие обработано. */
    private fun handleFormEvent(event: ListDetailEvent): Boolean {
        when (event) {
            ListDetailEvent.AddItemClicked -> formFlow.value = ItemFormState(mode = FormMode.Create)
            ListDetailEvent.FormClosed -> formFlow.value = null
            ListDetailEvent.FormSubmitted -> submitForm()
            is ListDetailEvent.NameChanged -> formFlow.update { it?.copy(name = event.name) }
            is ListDetailEvent.SuggestionSelected -> formFlow.update {
                it?.copy(name = event.suggestion, suggestions = emptyList())
            }
            is ListDetailEvent.QuantityChanged -> formFlow.update {
                it?.copy(quantity = sanitizeQuantityInput(event.quantity))
            }
            ListDetailEvent.QuantityIncreased -> formFlow.update {
                it?.copy(quantity = stepQuantity(it.quantity, 1))
            }
            ListDetailEvent.QuantityDecreased -> formFlow.update {
                it?.copy(quantity = stepQuantity(it.quantity, -1))
            }
            ListDetailEvent.UnitPickerOpened -> formFlow.update { it?.copy(unitPickerVisible = true) }
            ListDetailEvent.UnitPickerClosed -> formFlow.update { it?.copy(unitPickerVisible = false) }
            is ListDetailEvent.UnitSelected -> formFlow.update {
                it?.copy(unit = event.unit, unitPickerVisible = false)
            }
            is ListDetailEvent.EditItemClicked -> openEdit(event.itemId)
            else -> return false
        }
        return true
    }

    private fun handleItemsEvent(event: ListDetailEvent) {
        when (event) {
            is ListDetailEvent.DeleteItemClicked -> pendingDeleteItemIdFlow.value = event.itemId
            ListDetailEvent.DeleteItemDismissed -> pendingDeleteItemIdFlow.value = null
            ListDetailEvent.DeleteItemConfirmed -> confirmDeleteItem()
            is ListDetailEvent.PurchaseToggled -> launch {
                interactors.toggleShoppingItemPurchased(event.itemId)
            }
            is ListDetailEvent.SortModeSelected -> launch {
                interactors.changeItemsSortMode(listId, event.mode)
            }
            ListDetailEvent.DeleteAllItemsConfirmed -> launch { interactors.deleteAllShoppingItems(listId) }
            ListDetailEvent.ClearPurchasedConfirmed -> launch { interactors.clearPurchasedShoppingItems(listId) }
            is ListDetailEvent.ItemMoved -> reorderItems(event.fromIndex, event.toIndex)
            else -> Unit
        }
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    private fun confirmDeleteItem() {
        val id = pendingDeleteItemIdFlow.value ?: return
        pendingDeleteItemIdFlow.value = null
        launch { interactors.deleteShoppingItem(id) }
    }

    private fun submitForm() {
        val form = formFlow.value ?: return
        if (!form.isSubmittable) return

        val itemId = when (val mode = form.mode) {
            FormMode.Create -> null
            is FormMode.Edit -> mode.itemId
        }

        formFlow.value = null

        launch {
            interactors.saveShoppingItem(
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

        launch { interactors.moveShoppingItem(list.map { it.id }) }
    }
}
