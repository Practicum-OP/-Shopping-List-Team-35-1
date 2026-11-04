package com.example.shoppinglist.feature.lists.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglist.core.designsystem.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val ACTION_COUNT = 3
private const val SWIPE_ANIMATION_DURATION_MILLIS = 220

private val RoundIconSize = 40.dp
private val RoundIconInnerSize = 24.dp

private val ListItemHeight = 56.dp
private val ListItemCornerRadius = 12.dp

private val ListItemStartPadding = 8.dp
private val ListItemEndPadding = 16.dp
private val ListItemTextSpacing = 8.dp
private val ListItemTextHeight = 24.dp
private val ListItemTextSize = 16.sp

private val ActionIconSpacing = 8.dp
private val ActionsCardSpacing = 20.dp
private val HiddenListPartWidth = 16.dp

private enum class ListSwipeState {
    Closed,
    ActionsRevealed,
    AlmostDismissed,
}

/**
 * Иконка в круглом контейнере.
 *
 * iconId должен указывать на vector drawable,
 * например R.drawable.ic_add_list.
 */
@Composable
fun RoundIcon(
    @DrawableRes iconId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    containerColor: Color =
        MaterialTheme.colorScheme.secondaryContainer,
    iconColor: Color =
        MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = iconColor,
        )
    }
}

data class ListItemAction(
    @DrawableRes val iconId: Int,
    val contentDescription: String,
    val onClick: () -> Unit,
)

val listItemActionEdit = ListItemAction(
    iconId = R.drawable.ic_edit,
    contentDescription = "Редактировать список",
    onClick = {
        // Тестовое действие.
    },
)
val listItemActionCopy = ListItemAction(
    iconId = R.drawable.ic_copy,
    contentDescription = "Копировать список",
    onClick = {
        // Тестовое действие.
    },
)
val listItemActionDelete = ListItemAction(
    iconId = R.drawable.ic_delete,
    contentDescription = "Удалить список",
    onClick = {
        // Тестовое действие.
    },
)

@Composable
fun ShoppingListItem(
    name: String,
    @DrawableRes listIconId: Int,
    listIconContentDescription: String,
    onListIconClick: () -> Unit,
    onListClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    deleteContentDescription: String = "Удалить список",
) {
    val actions = listOf(
        listItemActionEdit,
        listItemActionCopy,
        listItemActionDelete,
    )

    require(actions.size == ACTION_COUNT) {
        "ShoppingListItem requires exactly $ACTION_COUNT actions."
    }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var containerWidthPx by remember {
        mutableIntStateOf(0)
    }

    var swipeState by remember {
        mutableStateOf(ListSwipeState.Closed)
    }

    var dragStartState by remember {
        mutableStateOf(ListSwipeState.Closed)
    }

    var offsetPx by remember {
        mutableFloatStateOf(0f)
    }

    var settleJob by remember {
        mutableStateOf<Job?>(null)
    }

    val hiddenPartWidthPx = with(density) {
        HiddenListPartWidth.toPx()
    }

    val fullDismissOffsetPx = (
        containerWidthPx.toFloat() - hiddenPartWidthPx
        ).coerceAtLeast(0f)

    val actionsWidthPx = with(density) {
        RoundIconSize.toPx() * ACTION_COUNT +
            ActionIconSpacing.toPx() * (ACTION_COUNT - 1) +
            ActionsCardSpacing.toPx()
    }

    val actionsRevealOffsetPx = actionsWidthPx
        .coerceAtMost(fullDismissOffsetPx)

    LaunchedEffect(
        fullDismissOffsetPx,
        actionsRevealOffsetPx,
    ) {
        settleJob?.cancelAndJoin()
        settleJob = null

        offsetPx = swipeState.targetOffset(
            actionsRevealOffsetPx = actionsRevealOffsetPx,
            fullDismissOffsetPx = fullDismissOffsetPx,
        )
    }

    val dragState = rememberDraggableState { dragAmount ->
        /*
         * reverseDirection = true меняет знак dragAmount.
         *
         * При движении пальца влево dragAmount становится положительным,
         * поэтому offsetPx увеличивается.
         */
        val minimumOffset = when (dragStartState) {
            ListSwipeState.AlmostDismissed -> {
                actionsRevealOffsetPx
            }

            else -> {
                0f
            }
        }

        val maximumOffset = when (dragStartState) {
            ListSwipeState.Closed -> {
                actionsRevealOffsetPx
            }

            else -> {
                fullDismissOffsetPx
            }
        }

        offsetPx = (
            offsetPx + dragAmount
            ).coerceIn(
            minimumValue = minimumOffset,
            maximumValue = maximumOffset,
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp,
            ),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(ListItemHeight)
                .onSizeChanged { size ->
                    containerWidthPx = size.width
                }
                .draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,

                    // Свайп пальцем влево.
                    reverseDirection = true,

                    enabled = fullDismissOffsetPx > 0f,
                    startDragImmediately = settleJob?.isActive == true,
                    onDragStarted = {
                        settleJob?.cancelAndJoin()
                        settleJob = null

                        dragStartState = swipeState
                    },
                    onDragStopped = { _ ->
                        val firstThreshold = actionsRevealOffsetPx / 2f

                        val secondThreshold = (
                            actionsRevealOffsetPx + fullDismissOffsetPx
                            ) / 2f

                        val nextState = when (dragStartState) {
                            ListSwipeState.Closed -> {
                                if (offsetPx >= firstThreshold) {
                                    ListSwipeState.ActionsRevealed
                                } else {
                                    ListSwipeState.Closed
                                }
                            }

                            ListSwipeState.ActionsRevealed -> {
                                when {
                                    offsetPx < firstThreshold -> {
                                        ListSwipeState.Closed
                                    }

                                    offsetPx > secondThreshold -> {
                                        ListSwipeState.AlmostDismissed
                                    }

                                    else -> {
                                        ListSwipeState.ActionsRevealed
                                    }
                                }
                            }

                            ListSwipeState.AlmostDismissed -> {
                                if (offsetPx < secondThreshold) {
                                    ListSwipeState.ActionsRevealed
                                } else {
                                    ListSwipeState.AlmostDismissed
                                }
                            }
                        }

                        swipeState = nextState

                        val targetOffsetPx = nextState.targetOffset(
                            actionsRevealOffsetPx = actionsRevealOffsetPx,
                            fullDismissOffsetPx = fullDismissOffsetPx,
                        )

                        val animationStartOffset = offsetPx

                        settleJob = scope.launch {
                            Animatable(
                                initialValue = animationStartOffset,
                            ).animateTo(
                                targetValue = targetOffsetPx,
                                animationSpec = tween(
                                    durationMillis =
                                    SWIPE_ANIMATION_DURATION_MILLIS,
                                ),
                            ) {
                                offsetPx = value
                            }
                        }
                    },
                ),
        ) {
            /*
         * Задний слой.
         *
         * При свайпе влево карточка уходит влево,
         * поэтому действия находятся справа.
         */
            if (swipeState == ListSwipeState.AlmostDismissed) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(RoundIconSize),
                    ) {
                        RoundIcon(
                            iconId = R.drawable.ic_delete,
                            contentDescription = deleteContentDescription,
                            containerColor = MaterialTheme.colorScheme.primary,
                            iconColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    horizontalArrangement = Arrangement.spacedBy(
                        ActionIconSpacing,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    actions.forEach { action ->
                        IconButton(
                            onClick = action.onClick,
                            enabled = swipeState ==
                                ListSwipeState.ActionsRevealed,
                            modifier = Modifier.size(RoundIconSize),
                        ) {
                            RoundIcon(
                                iconId = action.iconId,
                                contentDescription = action.contentDescription,
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                iconColor = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            /*
         * Передний слой.
         *
         * offsetPx всегда положительный,
         * а карточка перемещается влево через -offsetPx.
         */
            Card(
                onClick = onListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ListItemHeight)
                    .offset {
                        IntOffset(
                            x = (-offsetPx).roundToInt(),
                            y = 0,
                        )
                    },
                shape = RoundedCornerShape(ListItemCornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 6.dp,
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = ListItemStartPadding,
                            end = ListItemEndPadding,
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
                        modifier = Modifier.width(ListItemTextSpacing),
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(ListItemTextHeight),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = name,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = ListItemTextSize,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

private fun ListSwipeState.targetOffset(
    actionsRevealOffsetPx: Float,
    fullDismissOffsetPx: Float,
): Float {
    return when (this) {
        ListSwipeState.Closed -> {
            0f
        }

        ListSwipeState.ActionsRevealed -> {
            actionsRevealOffsetPx
        }

        ListSwipeState.AlmostDismissed -> {
            fullDismissOffsetPx
        }
    }
}
