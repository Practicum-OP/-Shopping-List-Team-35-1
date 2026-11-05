package com.example.shoppinglist.feature.lists.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
    onListIconClick: () -> Unit,
    onListClick: () -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val currentOnDeleteClick by rememberUpdatedState(onDeleteClick)

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

    var animationJob by remember {
        mutableStateOf<Job?>(null)
    }

    val offsetAnimation = remember {
        Animatable(0f)
    }

    val hiddenListPartWidthPx = with(density) {
        HiddenListPartWidth.toPx()
    }

    val fullDismissOffsetPx = (
        containerWidthPx.toFloat() - hiddenListPartWidthPx
        ).coerceAtLeast(0f)

    val actionsWidthPx = with(density) {
        RoundIconSize.toPx() * ACTION_COUNT +
            ActionIconSpacing.toPx() * (ACTION_COUNT - 1) +
            ActionRevealSpacing.toPx()
    }

    val actionsRevealOffsetPx = actionsWidthPx
        .coerceAtMost(fullDismissOffsetPx)

    fun targetOffsetForState(
        state: ListSwipeState,
    ): Float {
        return when (state) {
            ListSwipeState.Closed -> 0f
            ListSwipeState.ActionsRevealed -> actionsRevealOffsetPx
            ListSwipeState.AlmostDismissed -> fullDismissOffsetPx
        }
    }

    fun animateToState(
        targetState: ListSwipeState,
        showDeleteDialog: Boolean = false,
    ) {
        animationJob?.cancel()

        swipeState = targetState

        val startOffsetPx = offsetPx.coerceIn(
            minimumValue = 0f,
            maximumValue = fullDismissOffsetPx,
        )

        val targetOffsetPx = targetOffsetForState(targetState)

        animationJob = coroutineScope.launch {
            offsetAnimation.snapTo(startOffsetPx)

            offsetAnimation.animateTo(
                targetValue = targetOffsetPx,
                animationSpec = tween(
                    durationMillis = ANIMATION_DURATION_MILLIS,
                ),
            ) {
                offsetPx = value
            }

            offsetPx = targetOffsetPx

            if (showDeleteDialog) {
                currentOnDeleteClick()
            }
        }
    }

    fun animateToDeleteAndShowDialog() {
        animateToState(
            targetState = ListSwipeState.AlmostDismissed,
            showDeleteDialog = true,
        )
    }

    fun settleAfterDrag() {
        val firstThresholdPx = actionsRevealOffsetPx / 2f

        val secondThresholdPx = (
            actionsRevealOffsetPx + fullDismissOffsetPx
            ) / 2f

        val nextState = when (dragStartState) {
            ListSwipeState.Closed -> {
                if (offsetPx >= firstThresholdPx) {
                    ListSwipeState.ActionsRevealed
                } else {
                    ListSwipeState.Closed
                }
            }

            ListSwipeState.ActionsRevealed -> {
                when {
                    offsetPx < firstThresholdPx -> {
                        ListSwipeState.Closed
                    }

                    offsetPx >= secondThresholdPx -> {
                        ListSwipeState.AlmostDismissed
                    }

                    else -> {
                        ListSwipeState.ActionsRevealed
                    }
                }
            }

            ListSwipeState.AlmostDismissed -> {
                if (offsetPx < secondThresholdPx) {
                    ListSwipeState.ActionsRevealed
                } else {
                    ListSwipeState.AlmostDismissed
                }
            }
        }

        animateToState(nextState)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ItemVerticalPadding)
            .height(ItemHeight)
            .onSizeChanged { size ->
                containerWidthPx = size.width
            },
    ) {
        if (swipeState == ListSwipeState.AlmostDismissed) {
            SwipeActionButton(
                iconId = R.drawable.ic_delete,
                contentDescription = stringResource(
                    R.string.lists_delete_list,
                ),
                onClick = {
                    animateToDeleteAndShowDialog()
                },
                modifier = Modifier.align(Alignment.Center),
                containerColor = MaterialTheme.colorScheme.primary,
                iconTint = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(
                    ActionIconSpacing,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val actionsEnabled =
                    swipeState == ListSwipeState.ActionsRevealed

                SwipeActionButton(
                    iconId = R.drawable.ic_edit,
                    contentDescription = stringResource(
                        R.string.lists_edit_list,
                    ),
                    onClick = onEditClick,
                    enabled = actionsEnabled,
                )

                SwipeActionButton(
                    iconId = R.drawable.ic_copy,
                    contentDescription = stringResource(
                        R.string.lists_copy_list,
                    ),
                    onClick = onCopyClick,
                    enabled = actionsEnabled,
                )

                SwipeActionButton(
                    iconId = R.drawable.ic_delete,
                    contentDescription = stringResource(
                        R.string.lists_delete_list,
                    ),
                    onClick = {
                        animateToDeleteAndShowDialog()
                    },
                    enabled = actionsEnabled,
                )
            }
        }

        Card(
            onClick = onListClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(ItemHeight)
                .offset {
                    IntOffset(
                        x = -offsetPx.roundToInt(),
                        y = 0,
                    )
                }
                .pointerInput(
                    actionsRevealOffsetPx,
                    fullDismissOffsetPx,
                ) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            animationJob?.cancel()
                            dragStartState = swipeState
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()

                            val minimumOffsetPx =
                                if (
                                    dragStartState ==
                                    ListSwipeState.AlmostDismissed
                                ) {
                                    actionsRevealOffsetPx
                                } else {
                                    0f
                                }

                            val maximumOffsetPx =
                                if (
                                    dragStartState ==
                                    ListSwipeState.Closed
                                ) {
                                    actionsRevealOffsetPx
                                } else {
                                    fullDismissOffsetPx
                                }

                            offsetPx = (
                                offsetPx - dragAmount
                                ).coerceIn(
                                minimumValue = minimumOffsetPx,
                                maximumValue = maximumOffsetPx,
                            )
                        },
                        onDragEnd = {
                            settleAfterDrag()
                        },
                        onDragCancel = {
                            animateToState(dragStartState)
                        },
                    )
                },
            shape = RoundedCornerShape(ItemCornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
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
