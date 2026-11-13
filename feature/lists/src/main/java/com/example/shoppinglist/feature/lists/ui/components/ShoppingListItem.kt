package com.example.shoppinglist.feature.lists.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.feature.lists.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val ANIMATION_DURATION_MILLIS = 220
private const val ACTION_COUNT = 3

private val ItemHeight = 56.dp
private val ItemVerticalPadding = 8.dp
private val ItemCornerRadius = 12.dp

private val ItemHorizontalPadding = 8.dp
private val ItemTextEndPadding = 16.dp
private val ItemTextSpacing = 8.dp
private val ItemTextHeight = 24.dp

private val RoundIconSize = 40.dp
private val RoundIconInnerSize = 24.dp

private val ActionIconSpacing = 8.dp
private val ActionRevealSpacing = 20.dp
private val HiddenListPartWidth = 16.dp

private enum class ListSwipeState {
    Closed,
    ActionsRevealed,
    AlmostDismissed,
}

data class ShoppingListItemActions(
    val onListIconClick: () -> Unit,
    val onListClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onCopyClick: () -> Unit,
    val onDeleteClick: () -> Unit,
)

@Stable
private class ListSwipeController(
    private val scope: CoroutineScope,
    private val hiddenPartWidthPx: Float,
    private val actionsWidthPx: Float,
) {
    var containerWidthPx by mutableIntStateOf(0)

    var swipeState by mutableStateOf(ListSwipeState.Closed)
        private set

    var offsetPx by mutableFloatStateOf(0f)
        private set

    private var dragStartState = ListSwipeState.Closed
    private var animationJob: Job? = null
    private val offsetAnimation = Animatable(0f)

    private val fullDismissOffsetPx: Float
        get() = (containerWidthPx.toFloat() - hiddenPartWidthPx).coerceAtLeast(0f)

    private val actionsRevealOffsetPx: Float
        get() = actionsWidthPx.coerceAtMost(fullDismissOffsetPx)

    fun animateTo(
        targetState: ListSwipeState,
        onFinished: () -> Unit = {},
    ) {
        animationJob?.cancel()
        swipeState = targetState

        val startOffsetPx = offsetPx.coerceIn(
            minimumValue = 0f,
            maximumValue = fullDismissOffsetPx,
        )
        val targetOffsetPx = targetOffsetFor(targetState)

        animationJob = scope.launch {
            offsetAnimation.snapTo(startOffsetPx)
            offsetAnimation.animateTo(
                targetValue = targetOffsetPx,
                animationSpec = tween(durationMillis = ANIMATION_DURATION_MILLIS),
            ) {
                offsetPx = value
            }
            offsetPx = targetOffsetPx
            onFinished()
        }
    }

    fun onDragStart() {
        animationJob?.cancel()
        dragStartState = swipeState
    }

    fun onDrag(dragAmount: Float) {
        val minimumOffsetPx = if (dragStartState == ListSwipeState.AlmostDismissed) {
            actionsRevealOffsetPx
        } else {
            0f
        }
        val maximumOffsetPx = if (dragStartState == ListSwipeState.Closed) {
            actionsRevealOffsetPx
        } else {
            fullDismissOffsetPx
        }
        offsetPx = (offsetPx - dragAmount).coerceIn(
            minimumValue = minimumOffsetPx,
            maximumValue = maximumOffsetPx,
        )
    }

    fun onDragEnd() {
        animateTo(nextStateAfterDrag())
    }

    fun onDragCancel() {
        animateTo(dragStartState)
    }

    fun closeIfDismissed() {
        if (swipeState == ListSwipeState.AlmostDismissed) {
            animateTo(ListSwipeState.Closed)
        }
    }

    private fun targetOffsetFor(state: ListSwipeState): Float {
        return when (state) {
            ListSwipeState.Closed -> 0f
            ListSwipeState.ActionsRevealed -> actionsRevealOffsetPx
            ListSwipeState.AlmostDismissed -> fullDismissOffsetPx
        }
    }

    private fun nextStateAfterDrag(): ListSwipeState {
        val firstThresholdPx = actionsRevealOffsetPx / 2f
        val secondThresholdPx = (actionsRevealOffsetPx + fullDismissOffsetPx) / 2f

        return when (dragStartState) {
            ListSwipeState.Closed -> if (offsetPx >= firstThresholdPx) {
                ListSwipeState.ActionsRevealed
            } else {
                ListSwipeState.Closed
            }

            ListSwipeState.ActionsRevealed -> when {
                offsetPx < firstThresholdPx -> ListSwipeState.Closed
                offsetPx >= secondThresholdPx -> ListSwipeState.AlmostDismissed
                else -> ListSwipeState.ActionsRevealed
            }

            ListSwipeState.AlmostDismissed -> if (offsetPx < secondThresholdPx) {
                ListSwipeState.ActionsRevealed
            } else {
                ListSwipeState.AlmostDismissed
            }
        }
    }
}

@Composable
private fun rememberListSwipeController(): ListSwipeController {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    return remember(density, coroutineScope) {
        with(density) {
            ListSwipeController(
                scope = coroutineScope,
                hiddenPartWidthPx = HiddenListPartWidth.toPx(),
                actionsWidthPx = RoundIconSize.toPx() * ACTION_COUNT +
                    ActionIconSpacing.toPx() * (ACTION_COUNT - 1) +
                    ActionRevealSpacing.toPx(),
            )
        }
    }
}

private fun Modifier.swipeGestures(controller: ListSwipeController): Modifier =
    pointerInput(controller) {
        detectHorizontalDragGestures(
            onDragStart = { controller.onDragStart() },
            onHorizontalDrag = { change, dragAmount ->
                change.consume()
                controller.onDrag(dragAmount)
            },
            onDragEnd = { controller.onDragEnd() },
            onDragCancel = { controller.onDragCancel() },
        )
    }

@Composable
fun RoundIcon(
    @DrawableRes iconId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Box(
        modifier = modifier
            .size(RoundIconSize)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            modifier = Modifier.size(RoundIconInnerSize),
            tint = iconTint,
        )
    }
}

@Composable
fun ShoppingListItem(
    name: String,
    @DrawableRes listIconId: Int,
    listIconContentDescription: String,
    actions: ShoppingListItemActions,
    modifier: Modifier = Modifier,
    isDeletePending: Boolean = false,
) {
    val controller = rememberListSwipeController()
    val currentOnDeleteClick by rememberUpdatedState(actions.onDeleteClick)

    // Диалог удаления закрыли без удаления: возвращаем карточку на место.
    LaunchedEffect(isDeletePending) {
        if (!isDeletePending) {
            controller.closeIfDismissed()
        }
    }

    val onDeleteRequest = {
        controller.animateTo(ListSwipeState.AlmostDismissed) {
            currentOnDeleteClick()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ItemVerticalPadding)
            .height(ItemHeight)
            .onSizeChanged { size ->
                controller.containerWidthPx = size.width
            },
    ) {
        SwipeActions(
            swipeState = controller.swipeState,
            onEditClick = actions.onEditClick,
            onCopyClick = actions.onCopyClick,
            onDeleteClick = onDeleteRequest,
        )

        Card(
            onClick = actions.onListClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(ItemHeight)
                .offset {
                    IntOffset(
                        x = -controller.offsetPx.roundToInt(),
                        y = 0,
                    )
                }
                .swipeGestures(controller),
            shape = RoundedCornerShape(ItemCornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp,
            ),
        ) {
            ShoppingListCardContent(
                name = name,
                listIconId = listIconId,
                listIconContentDescription = listIconContentDescription,
                onListIconClick = actions.onListIconClick,
            )
        }
    }
}

@Composable
private fun BoxScope.SwipeActions(
    swipeState: ListSwipeState,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    if (swipeState == ListSwipeState.AlmostDismissed) {
        SwipeActionButton(
            iconId = R.drawable.ic_delete,
            contentDescription = stringResource(R.string.lists_delete_list),
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.Center),
            containerColor = MaterialTheme.colorScheme.primary,
            iconTint = MaterialTheme.colorScheme.onPrimary,
        )
        return
    }

    val actionsEnabled = swipeState == ListSwipeState.ActionsRevealed

    Row(
        modifier = Modifier.align(Alignment.CenterEnd),
        horizontalArrangement = Arrangement.spacedBy(ActionIconSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SwipeActionButton(
            iconId = R.drawable.ic_edit,
            contentDescription = stringResource(R.string.lists_edit_list),
            onClick = onEditClick,
            enabled = actionsEnabled,
        )

        SwipeActionButton(
            iconId = R.drawable.ic_copy,
            contentDescription = stringResource(R.string.lists_copy_list),
            onClick = onCopyClick,
            enabled = actionsEnabled,
        )

        SwipeActionButton(
            iconId = R.drawable.ic_delete,
            contentDescription = stringResource(R.string.lists_delete_list),
            onClick = onDeleteClick,
            enabled = actionsEnabled,
        )
    }
}

@Composable
private fun ShoppingListCardContent(
    name: String,
    @DrawableRes listIconId: Int,
    listIconContentDescription: String,
    onListIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = ItemHorizontalPadding,
                end = ItemTextEndPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onListIconClick,
            modifier = Modifier.size(RoundIconSize),
        ) {
            RoundIcon(
                iconId = listIconId,
                contentDescription = listIconContentDescription,
            )
        }

        Spacer(
            modifier = Modifier.width(ItemTextSpacing),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(ItemTextHeight),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SwipeActionButton(
    @DrawableRes iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    iconTint: Color = MaterialTheme.colorScheme.primary,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(RoundIconSize),
    ) {
        RoundIcon(
            iconId = iconId,
            contentDescription = contentDescription,
            containerColor = containerColor,
            iconTint = iconTint,
        )
    }
}
