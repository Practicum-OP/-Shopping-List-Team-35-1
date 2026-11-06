package com.example.shoppinglist.feature.listdetail.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.core.designsystem.theme.listDivider
import com.example.shoppinglist.core.designsystem.theme.onSwipeAction
import com.example.shoppinglist.core.designsystem.theme.swipeActionContainer
import com.example.shoppinglist.feature.listdetail.R
import com.example.shoppinglist.feature.listdetail.ui.ItemUnitUi
import com.example.shoppinglist.feature.listdetail.ui.ShoppingItemUi
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.example.shoppinglist.core.designsystem.R as DesignR

private const val DRAGGED_ROW_OVERLAY_ALPHA = 0.16f
private const val SWIPE_SETTLE_FRACTION = 0.5f
private const val NAME_MAX_LINES = 2

@Composable
fun ShoppingItemRow(
    item: ShoppingItemUi,
    onPurchaseToggle: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDragged: Boolean = false,
    dragHandle: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        SwipeActionsContainer(onEditClick = onEditClick, onDeleteClick = onDeleteClick) {
            ItemRowContent(
                item = item,
                onPurchaseToggle = onPurchaseToggle,
                onEditClick = onEditClick,
                isDragged = isDragged,
                dragHandle = dragHandle,
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.listDivider)
    }
}

@Composable
private fun SwipeActionsContainer(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val actionsWidth = AppDimens.iconButtonSize * 2 + AppDimens.spacingSmall + AppDimens.spacingMedium
    val revealOffset = with(LocalDensity.current) { actionsWidth.toPx() }
    val swipeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val closeAndRun: (() -> Unit) -> Unit = { action ->
        scope.launch { swipeOffset.animateTo(0f) }
        action()
    }

    Box {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(end = AppDimens.spacingMedium),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwipeAction(DesignR.drawable.ic_edit, DesignR.string.common_edit) { closeAndRun(onEditClick) }
            SwipeAction(DesignR.drawable.ic_delete, DesignR.string.common_delete) { closeAndRun(onDeleteClick) }
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeOffset.value.roundToInt(), 0) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            swipeOffset.snapTo((swipeOffset.value + delta).coerceIn(-revealOffset, 0f))
                        }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        val isRevealed = swipeOffset.value < -revealOffset * SWIPE_SETTLE_FRACTION
                        swipeOffset.animateTo(if (isRevealed) -revealOffset else 0f)
                    },
                ),
        ) {
            content()
        }
    }
}

// TODO(#51): check secondaryContainer and onSecondaryContainer against Figma after theme update
@Composable
private fun SwipeAction(
    @DrawableRes iconRes: Int,
    @StringRes contentDescriptionRes: Int,
    onClick: () -> Unit,
) {
    FilledTonalIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.swipeActionContainer,
            contentColor = MaterialTheme.colorScheme.onSwipeAction,
        ),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = stringResource(contentDescriptionRes),
        )
    }
}

@Composable
private fun ItemRowContent(
    item: ShoppingItemUi,
    onPurchaseToggle: () -> Unit,
    onEditClick: () -> Unit,
    isDragged: Boolean,
    dragHandle: (@Composable () -> Unit)?,
) {
    val surface = MaterialTheme.colorScheme.surface
    val draggedColor = MaterialTheme.colorScheme.onSurface
        .copy(alpha = DRAGGED_ROW_OVERLAY_ALPHA)
        .compositeOver(surface)

    Surface(
        color = if (isDragged) draggedColor else surface,
        shadowElevation = if (isDragged) AppDimens.draggedItemElevation else 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = AppDimens.listItemTwoLineMinHeight)
                .clickable(onClick = onEditClick)
                .padding(
                    start = AppDimens.spacingExtraSmall,
                    end = AppDimens.listItemHorizontalPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingExtraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PurchaseToggle(isPurchased = item.isPurchased, onToggle = onPurchaseToggle)
            ItemTexts(item = item, modifier = Modifier.weight(1f))
            dragHandle?.invoke()
        }
    }
}

@Composable
private fun PurchaseToggle(
    isPurchased: Boolean,
    onToggle: () -> Unit,
) {
    IconToggleButton(
        checked = isPurchased,
        onCheckedChange = { onToggle() },
    ) {
        Icon(
            painter = painterResource(
                if (isPurchased) DesignR.drawable.ic_check_circle else DesignR.drawable.ic_uncheck,
            ),
            contentDescription = stringResource(R.string.list_detail_mark_purchased),
            tint = if (isPurchased) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

@Composable
private fun ItemTexts(
    item: ShoppingItemUi,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null,
            maxLines = NAME_MAX_LINES,
            overflow = TextOverflow.Ellipsis,
        )
        quantityText(item)?.let { quantity ->
            Text(
                text = quantity,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun quantityText(item: ShoppingItemUi): String? {
    val unit = ItemUnitUi.fromKey(item.unit)?.let { stringResource(it.labelRes) }
    return when {
        item.quantity.isBlank() -> null
        unit == null -> item.quantity
        else -> stringResource(R.string.list_detail_item_quantity, item.quantity, unit)
    }
}
