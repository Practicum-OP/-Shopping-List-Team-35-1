package com.example.shoppinglist.feature.lists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.components.Placeholder
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.core.domain.lists.ShoppingList
import com.example.shoppinglist.core.domain.settings.ThemeMode
import com.example.shoppinglist.feature.lists.R
import com.example.shoppinglist.feature.lists.ui.components.CreateDialog
import com.example.shoppinglist.feature.lists.ui.components.ListItemAction
import com.example.shoppinglist.feature.lists.ui.components.RenameDialog
import com.example.shoppinglist.feature.lists.ui.components.SearchField
import com.example.shoppinglist.feature.lists.ui.components.ShoppingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    state: ListsState,
    onEvent: (ListsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCreateDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showDeleteAllDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var renameListId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var renameListName by rememberSaveable {
        mutableStateOf("")
    }

    var deleteListId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var deleteListName by rememberSaveable {
        mutableStateOf("")
    }

    val isSystemDark = isSystemInDarkTheme()

    val isDarkTheme = when (state.themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            when (val topContent = state.topContent) {
                TopContent.AppBar -> {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(
                                    R.string.lists_title,
                                ),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    onEvent(ListsEvent.SearchOpened)
                                },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.ic_search,
                                    ),
                                    contentDescription = "Поиск",
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            IconButton(
                                onClick = {
                                    showDeleteAllDialog = true
                                },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        R.drawable.ic_delete,
                                    ),
                                    contentDescription = "Удалить все списки",
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            IconButton(
                                onClick = {
                                    onEvent(
                                        ListsEvent.ThemeModeChanged(
                                            mode = if (isDarkTheme) {
                                                ThemeMode.LIGHT
                                            } else {
                                                ThemeMode.DARK
                                            },
                                        ),
                                    )
                                },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (isDarkTheme) {
                                            R.drawable.ic_day_theme
                                        } else {
                                            R.drawable.ic_night_theme
                                        },
                                    ),
                                    contentDescription = stringResource(
                                        R.string.lists_toggle_theme,
                                    ),
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        },
                    )
                }

                is TopContent.SearchField -> {
                    SearchField(
                        query = topContent.searchQuery,
                        onQueryChange = { query ->
                            onEvent(
                                ListsEvent.SearchQueryChanged(query),
                            )
                        },
                        onBackClick = {
                            onEvent(ListsEvent.SearchClosed)
                        },
                        onClearClick = {
                            onEvent(ListsEvent.SearchCleared)
                        },
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showCreateDialog = true
                },
            ) {
                Icon(
                    painter = painterResource(
                        R.drawable.ic_add,
                    ),
                    contentDescription = "Добавить список",
                )
            }
        },
    ) { innerPadding ->
        ListsContent(
            state = state,
            onEvent = onEvent,
            onRenameClick = { list ->
                renameListId = list.id
                renameListName = list.name
            },
            onDeleteClick = { list ->
                deleteListId = list.id
                deleteListName = list.name
            },
            innerPadding = innerPadding,
        )
    }

    if (showCreateDialog) {
        CreateDialog(
            onCreate = { name ->
                onEvent(
                    ListsEvent.CreateListConfirmed(name),
                )
                showCreateDialog = false
            },
            onDismiss = {
                showCreateDialog = false
            },
        )
    }

    val currentRenameListId = renameListId

    if (currentRenameListId != null) {
        RenameDialog(
            oldTitle = renameListName,
            listId = currentRenameListId,
            onRename = { name, listId ->
                onEvent(
                    ListsEvent.RenameListConfirmed(
                        listId = listId,
                        name = name,
                    ),
                )

                renameListId = null
                renameListName = ""
            },
            onDismiss = {
                renameListId = null
                renameListName = ""
            },
        )
    }

    val currentDeleteListId = deleteListId

    if (currentDeleteListId != null) {
        ConfirmDeleteDialog(
            title = "Удалить список «$deleteListName»?",
            onDelete = {
                onEvent(
                    ListsEvent.DeleteListConfirmed(
                        listId = currentDeleteListId,
                    ),
                )

                deleteListId = null
                deleteListName = ""
            },
            onDismiss = {
                deleteListId = null
                deleteListName = ""
            },
        )
    }

    if (showDeleteAllDialog) {
        ConfirmDeleteDialog(
            title = "Удалить все списки?",
            onDelete = {
                onEvent(
                    ListsEvent.DeleteAllListsConfirmed,
                )
                showDeleteAllDialog = false
            },
            onDismiss = {
                showDeleteAllDialog = false
            },
        )
    }
}

@Composable
private fun ListsContent(
    state: ListsState,
    onEvent: (ListsEvent) -> Unit,
    onRenameClick: (ShoppingList) -> Unit,
    onDeleteClick: (ShoppingList) -> Unit,
    innerPadding: PaddingValues,
) {
    val isSearchOpened = state.topContent is TopContent.SearchField
    val isSearchEmpty = (
        state.topContent as? TopContent.SearchField
        )?.searchQuery?.isBlank() == true

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        when (val mainContent = state.mainContent) {
            MainContent.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            }

            MainContent.NoLists -> {
                Placeholder(
                    imageRes = R.drawable.illustration_shopping_lists,
                    titleRes = R.string.lists_empty_title,
                    descriptionRes = R.string.lists_empty_description,
                    contentDescriptionRes = R.string.lists_empty_image_description,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            MainContent.NoSearchResults -> {
                Placeholder(
                    imageRes = R.drawable.illustration_search_screen,
                    titleRes = R.string.lists_search_not_found_title,
                    descriptionRes = R.string.lists_search_not_found_description,
                    contentDescriptionRes = R.string.lists_not_found_image_description,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is MainContent.Lists -> {
                ShoppingLists(
                    lists = mainContent.items,
                    onEvent = onEvent,
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick,
                )
            }

            is MainContent.SearchResults -> {
                ShoppingLists(
                    lists = mainContent.items,
                    onEvent = onEvent,
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick,
                )
            }
        }

        if (isSearchOpened && isSearchEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.28f),
                    ),
            )
        }
    }
}

@Composable
private fun ShoppingLists(
    lists: List<ShoppingList>,
    onEvent: (ListsEvent) -> Unit,
    onRenameClick: (ShoppingList) -> Unit,
    onDeleteClick: (ShoppingList) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = AppDimens.screenHorizontalPadding,
            vertical = AppDimens.spacingMedium,
        ),
        verticalArrangement = Arrangement.spacedBy(
            AppDimens.spacingMedium,
        ),
    ) {
        items(
            items = lists,
            key = { list ->
                list.id
            },
        ) { list ->
            ShoppingListItem(
                name = list.name,
                listIconId = ListIconCatalog.drawableRes(
                    list.iconKey,
                ),
                listIconContentDescription = "Иконка списка",
                actions = listOf(
                    ListItemAction(
                        iconId = com.example.shoppinglist.core.designsystem.R.drawable.ic_edit,
                        contentDescription = "Редактировать список",
                        onClick = {
                            onRenameClick(list)
                        },
                    ),
                    ListItemAction(
                        iconId = com.example.shoppinglist.core.designsystem.R.drawable.ic_copy,
                        contentDescription = "Копировать список",
                        onClick = {
                            onEvent(
                                ListsEvent.CopyListClicked(
                                    listId = list.id,
                                ),
                            )
                        },
                    ),
                    ListItemAction(
                        iconId = com.example.shoppinglist.core.designsystem.R.drawable.ic_delete,
                        contentDescription = "Удалить список",
                        onClick = {
                            onDeleteClick(list)
                        },
                    ),
                ),
                onListIconClick = {
                    // Здесь можно открыть выбор иконки.
                },
                onListClick = {
                    onEvent(
                        ListsEvent.ListClicked(
                            listId = list.id,
                        ),
                    )
                },
                onDeleteClick = {
                    onDeleteClick(list)
                },
            )
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    title: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDelete,
            ) {
                Text(
                    text = "Удалить",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(text = "Отмена")
            }
        },
    )
}

private fun nextThemeMode(
    current: ThemeMode,
): ThemeMode {
    return when (current) {
        ThemeMode.SYSTEM -> ThemeMode.DARK
        ThemeMode.DARK -> ThemeMode.LIGHT
        ThemeMode.LIGHT -> ThemeMode.SYSTEM
    }
}
