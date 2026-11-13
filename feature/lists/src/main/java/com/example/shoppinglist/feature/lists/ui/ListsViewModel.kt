package com.example.shoppinglist.feature.lists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.lists.interactor.ChangeShoppingListIcon
import com.example.shoppinglist.core.domain.lists.interactor.CopyShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.CreateShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.DeleteAllShoppingLists
import com.example.shoppinglist.core.domain.lists.interactor.DeleteShoppingList
import com.example.shoppinglist.core.domain.lists.interactor.ObserveShoppingLists
import com.example.shoppinglist.core.domain.lists.interactor.RenameShoppingList
import com.example.shoppinglist.core.domain.settings.ChangeThemeMode
import com.example.shoppinglist.core.domain.settings.ObserveThemeMode
import com.example.shoppinglist.core.domain.settings.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val STOP_TIMEOUT_MILLIS = 5_000L

data class ListsInteractors(
    val observeShoppingLists: ObserveShoppingLists,
    val createShoppingList: CreateShoppingList,
    val renameShoppingList: RenameShoppingList,
    val deleteShoppingList: DeleteShoppingList,
    val deleteAllShoppingLists: DeleteAllShoppingLists,
    val copyShoppingList: CopyShoppingList,
    val changeShoppingListIcon: ChangeShoppingListIcon,
)

class ListsViewModel(
    private val interactors: ListsInteractors,
    observeThemeMode: ObserveThemeMode,
    private val changeThemeMode: ChangeThemeMode,
) : ViewModel() {

    private val searchOpened = MutableStateFlow(false)
    private val searchQuery = MutableStateFlow("")

    private val _effect = Channel<ListsEffect>(
        capacity = Channel.BUFFERED,
    )

    val effect: Flow<ListsEffect> = _effect.receiveAsFlow()

    val state: StateFlow<ListsState> = combine(
        interactors.observeShoppingLists(),
        searchOpened,
        searchQuery,
        observeThemeMode(),
    ) { lists, isSearchOpened, query, themeMode ->
        val normalizedQuery = query.trim()

        val mainContent = when {
            isSearchOpened && normalizedQuery.isNotEmpty() -> {
                val results = lists.filter { list ->
                    list.name.contains(
                        other = normalizedQuery,
                        ignoreCase = true,
                    )
                }

                if (results.isEmpty()) {
                    MainContent.NoSearchResults
                } else {
                    MainContent.SearchResults(results)
                }
            }

            lists.isEmpty() -> {
                MainContent.NoLists
            }

            else -> {
                MainContent.Lists(lists)
            }
        }

        ListsState(
            topContent = if (isSearchOpened) {
                TopContent.SearchField(
                    searchQuery = query,
                )
            } else {
                TopContent.AppBar
            },
            mainContent = mainContent,
            themeMode = themeMode,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ListsState(),
    )

    fun onEvent(event: ListsEvent) {
        when (event) {
            ListsEvent.SearchOpened -> searchOpened.value = true
            ListsEvent.SearchClosed -> {
                searchOpened.value = false
                searchQuery.value = ""
            }
            ListsEvent.SearchCleared -> searchQuery.value = ""
            is ListsEvent.SearchQueryChanged -> searchQuery.value = event.query
            is ListsEvent.CreateListConfirmed -> createList(event.name)
            ListsEvent.DeleteAllListsConfirmed -> deleteAllLists()
            is ListsEvent.ThemeModeChanged -> changeTheme(event.mode)
            is ListsEvent.ThemeChanged -> changeTheme(
                mode = if (event.isDarkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
            )
            is ListsEvent.ListIconSelected -> changeIcon(listId = event.listId, iconKey = event.iconKey)
            is ListsEvent.RenameListConfirmed -> renameList(listId = event.listId, name = event.name)
            is ListsEvent.DeleteListConfirmed -> deleteList(event.listId)
            is ListsEvent.CopyListClicked -> copyList(event.listId)
            is ListsEvent.ListClicked -> viewModelScope.launch {
                _effect.send(ListsEffect.OpenList(listId = event.listId))
            }
        }
    }

    private fun createList(name: String) {
        executeOperation {
            interactors.createShoppingList(name)
        }
    }

    private fun renameList(
        listId: String,
        name: String,
    ) {
        executeOperation {
            interactors.renameShoppingList(
                id = listId,
                name = name,
            )
        }
    }

    private fun deleteList(listId: String) {
        executeOperation {
            interactors.deleteShoppingList(listId)
        }
    }

    private fun deleteAllLists() {
        executeOperation {
            interactors.deleteAllShoppingLists()
        }
    }

    private fun copyList(listId: String) {
        executeOperation {
            interactors.copyShoppingList(listId)
        }
    }

    private fun changeIcon(
        listId: String,
        iconKey: String,
    ) {
        executeOperation {
            interactors.changeShoppingListIcon(
                id = listId,
                iconKey = iconKey,
            )
        }
    }

    private fun changeTheme(mode: ThemeMode) {
        executeOperation {
            changeThemeMode(mode)
        }
    }

    private fun executeOperation(
        operation: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                operation()
            }.onFailure { throwable ->
                _effect.send(
                    ListsEffect.ShowSnackbar(
                        message = throwable.message
                            ?: "Не удалось выполнить операцию",
                    ),
                )
            }
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }
}
