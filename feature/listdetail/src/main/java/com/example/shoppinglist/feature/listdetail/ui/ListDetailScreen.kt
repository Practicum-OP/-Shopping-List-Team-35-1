package com.example.shoppinglist.feature.listdetail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.shoppinglist.core.designsystem.components.DeleteDialog
import com.example.shoppinglist.core.designsystem.components.Placeholder
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.feature.listdetail.R
import com.example.shoppinglist.feature.listdetail.ui.components.ItemForm
import com.example.shoppinglist.feature.listdetail.ui.components.ListDetailMenu
import com.example.shoppinglist.feature.listdetail.ui.components.ShoppingItemsList
import com.example.shoppinglist.core.designsystem.R as DesignR

private enum class PendingConfirmation {
    DELETE_ALL,
    CLEAR_PURCHASED,
}

@Composable
fun ListDetailScreen(
    state: ListDetailState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var pendingConfirmation by rememberSaveable {
        mutableStateOf<PendingConfirmation?>(null)
    }

    ListDetailScaffold(
        state = state,
        onEvent = onEvent,
        onMenuClick = {
            isMenuVisible = true
        },
        modifier = modifier,
    )

    if (isMenuVisible) {
        ListDetailMenu(
            sortMode = state.sortMode,
            onSortModeSelected = { mode ->
                isMenuVisible = false
                onEvent(ListDetailEvent.SortModeSelected(mode))
            },
            onDeleteAllClick = {
                isMenuVisible = false
                pendingConfirmation = PendingConfirmation.DELETE_ALL
            },
            onClearPurchasedClick = {
                isMenuVisible = false
                pendingConfirmation = PendingConfirmation.CLEAR_PURCHASED
            },
            onDismiss = {
                isMenuVisible = false
            },
        )
    }

    pendingConfirmation?.let { confirmation ->
        ConfirmationDialog(
            confirmation = confirmation,
            onConfirm = {
                pendingConfirmation = null
                when (confirmation) {
                    PendingConfirmation.DELETE_ALL -> onEvent(ListDetailEvent.DeleteAllItemsConfirmed)
                    PendingConfirmation.CLEAR_PURCHASED -> onEvent(ListDetailEvent.ClearPurchasedConfirmed)
                }
            },
            onDismiss = {
                pendingConfirmation = null
            },
        )
    }

    state.pendingDeleteItemId?.let { itemId ->
        val item = (state.content as? ListContent.Items)
            ?.items
            ?.firstOrNull { it.id == itemId }

        if (item != null) {
            DeleteDialog(
                onDelete = {
                    onEvent(ListDetailEvent.DeleteItemConfirmed)
                },
                onDismiss = {
                    onEvent(ListDetailEvent.DeleteItemDismissed)
                },
                dialogTitle = stringResource(
                    R.string.list_detail_delete_item_dialog_title,
                    item.name,
                ),
                contentDescription = null,
                idItem = item.id,
            )
        }
    }

    state.itemForm?.let { form ->
        ItemForm(
            form = form,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun ListDetailScaffold(
    state: ListDetailState,
    onEvent: (ListDetailEvent) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ListDetailTopBar(
                listName = state.listName,
                onBackClick = {
                    onEvent(ListDetailEvent.BackClicked)
                },
                onMenuClick = onMenuClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ListDetailEvent.AddItemClicked)
                },
            ) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_add),
                    contentDescription = stringResource(R.string.list_detail_add_item),
                )
            }
        },
    ) { innerPadding ->
        ListDetailContent(
            content = state.content,
            sortMode = state.sortMode,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListDetailTopBar(
    listName: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = listName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_back_arrow),
                    contentDescription = stringResource(DesignR.string.common_back),
                )
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_more),
                    contentDescription = stringResource(DesignR.string.common_more),
                )
            }
        },
    )
}

@Composable
private fun ListDetailContent(
    content: ListContent,
    sortMode: ItemsSortMode,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (content) {
        ListContent.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        ListContent.Empty -> {
            Placeholder(
                imageRes = DesignR.drawable.illustration_product_list,
                titleRes = R.string.list_detail_empty_title,
                descriptionRes = R.string.list_detail_empty_description,
                contentDescriptionRes = R.string.list_detail_empty_image_description,
                modifier = modifier.fillMaxSize(),
            )
        }

        is ListContent.Items -> {
            ShoppingItemsList(
                items = content.items,
                sortMode = sortMode,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ConfirmationDialog(
    confirmation: PendingConfirmation,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val titleRes = when (confirmation) {
        PendingConfirmation.DELETE_ALL -> R.string.list_detail_delete_all_dialog_title
        PendingConfirmation.CLEAR_PURCHASED -> R.string.list_detail_clear_purchased_dialog_title
    }

    DeleteDialog(
        onDelete = { onConfirm() },
        onDismiss = onDismiss,
        dialogTitle = stringResource(titleRes),
        contentDescription = null,
        idItem = null,
    )
}
