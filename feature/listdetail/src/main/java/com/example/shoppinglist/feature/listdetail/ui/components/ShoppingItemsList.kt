package com.example.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.feature.listdetail.ui.ListDetailEvent
import com.example.shoppinglist.feature.listdetail.ui.ShoppingItemUi
import kotlin.math.roundToInt
import com.example.shoppinglist.core.designsystem.R as DesignR

private class ItemDragState {
    var itemId by mutableStateOf<String?>(null)
    var offset by mutableFloatStateOf(0f)

    fun start(id: String) {
        itemId = id
        offset = 0f
    }

    fun reset() {
        itemId = null
        offset = 0f
    }
}

@Composable
fun ShoppingItemsList(
    items: List<ShoppingItemUi>,
    sortMode: ItemsSortMode,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val dragState = remember { ItemDragState() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = AppDimens.fabSize + AppDimens.spacingMedium * 2),
    ) {
        itemsIndexed(items = items, key = { _, item -> item.id }) { index, item ->
            DraggableItemRow(
                item = item,
                dragState = dragState,
                isDragEnabled = sortMode == ItemsSortMode.CUSTOM,
                onEvent = onEvent,
                onDragEnd = {
                    val target = listState.targetIndex(index, dragState.offset, items.lastIndex)
                    dragState.reset()
                    if (target != index) onEvent(ListDetailEvent.ItemMoved(index, target))
                },
            )
        }
    }
}

@Composable
private fun DraggableItemRow(
    item: ShoppingItemUi,
    dragState: ItemDragState,
    isDragEnabled: Boolean,
    onEvent: (ListDetailEvent) -> Unit,
    onDragEnd: () -> Unit,
) {
    val isDragged = item.id == dragState.itemId
    val dragHandle: @Composable () -> Unit = {
        DragHandle(
            onDragStart = { dragState.start(item.id) },
            onDrag = { delta -> dragState.offset += delta },
            onDragEnd = onDragEnd,
        )
    }

    ShoppingItemRow(
        item = item,
        onPurchaseToggle = { onEvent(ListDetailEvent.PurchaseToggled(item.id)) },
        onEditClick = { onEvent(ListDetailEvent.EditItemClicked(item.id)) },
        onDeleteClick = { onEvent(ListDetailEvent.DeleteItemClicked(item.id)) },
        modifier = Modifier
            .zIndex(if (isDragged) 1f else 0f)
            .graphicsLayer { translationY = if (isDragged) dragState.offset else 0f },
        isDragged = isDragged,
        dragHandle = dragHandle.takeIf { isDragEnabled },
    )
}

@Composable
private fun DragHandle(
    onDragStart: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
) {
    val currentOnDragStart by rememberUpdatedState(onDragStart)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)

    Icon(
        painter = painterResource(DesignR.drawable.ic_drag_handle),
        contentDescription = null,
        modifier = Modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { currentOnDragStart() },
                onDragEnd = { currentOnDragEnd() },
                onDragCancel = { currentOnDragEnd() },
                onDrag = { change, amount ->
                    change.consume()
                    currentOnDrag(amount.y)
                },
            )
        },
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private fun LazyListState.targetIndex(index: Int, offset: Float, lastIndex: Int): Int {
    val rowHeight = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }?.size ?: return index
    return (index + (offset / rowHeight).roundToInt()).coerceIn(0, lastIndex)
}
