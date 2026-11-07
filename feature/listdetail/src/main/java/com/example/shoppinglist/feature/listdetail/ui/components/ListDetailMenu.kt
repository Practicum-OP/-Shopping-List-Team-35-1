package com.example.shoppinglist.feature.listdetail.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.core.designsystem.theme.sortLabel
import com.example.shoppinglist.core.designsystem.theme.sortLabelHighlighted
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.feature.listdetail.R
import com.example.shoppinglist.core.designsystem.R as DesignR

private const val SUBMENU_ARROW_ROTATION = -90f

private val sortModesInMenuOrder = listOf(ItemsSortMode.ALPHABETICAL, ItemsSortMode.CUSTOM)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailMenu(
    sortMode: ItemsSortMode,
    onSortModeSelected: (ItemsSortMode) -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier.padding(horizontal = AppDimens.bottomSheetSideMargin),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            SortMenuRow(sortMode = sortMode, onSortModeSelected = onSortModeSelected)
            MenuRow(
                iconRes = DesignR.drawable.ic_delete,
                titleRes = R.string.list_detail_menu_delete_all,
                onClick = onDeleteAllClick,
            )
            MenuRow(
                iconRes = DesignR.drawable.ic_clean,
                titleRes = R.string.list_detail_menu_clear_purchased,
                onClick = onClearPurchasedClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortMenuRow(
    sortMode: ItemsSortMode,
    onSortModeSelected: (ItemsSortMode) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box {
        MenuRow(
            iconRes = DesignR.drawable.ic_swap,
            titleRes = R.string.list_detail_menu_sort,
            onClick = { isExpanded = true },
            modifier = if (isExpanded) {
                Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            } else {
                Modifier
            },
            subtitleRes = sortMode.labelRes,
            highlighted = isExpanded,
            trailing = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = false,
                    modifier = Modifier.rotate(SUBMENU_ARROW_ROTATION),
                )
            },
        )
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                sortModesInMenuOrder.forEach { mode ->
                    SortModeItem(
                        mode = mode,
                        selected = mode == sortMode,
                        onClick = {
                            isExpanded = false
                            onSortModeSelected(mode)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SortModeItem(
    mode: ItemsSortMode,
    selected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(mode.labelRes), style = MaterialTheme.typography.bodyLarge) },
        onClick = onClick,
        leadingIcon = {
            Icon(painter = painterResource(mode.iconRes), contentDescription = null)
        },
        trailingIcon = {
            RadioButton(selected = selected, onClick = null)
        },
    )
}

@Composable
private fun MenuRow(
    @DrawableRes iconRes: Int,
    @StringRes titleRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes subtitleRes: Int? = null,
    highlighted: Boolean = false,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppDimens.listItemMinHeight)
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppDimens.listItemHorizontalPadding,
                vertical = AppDimens.listItemVerticalPadding,
            ),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(iconRes), contentDescription = null)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.bodyLarge,
            )
            subtitleRes?.let { res ->
                Text(
                    text = stringResource(res),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (highlighted) {
                        MaterialTheme.colorScheme.sortLabelHighlighted
                    } else {
                        MaterialTheme.colorScheme.sortLabel
                    },
                )
            }
        }
        trailing?.invoke()
    }
}

private val ItemsSortMode.labelRes: Int
    get() = when (this) {
        ItemsSortMode.ALPHABETICAL -> R.string.list_detail_sort_alphabetical
        ItemsSortMode.CUSTOM -> R.string.list_detail_sort_custom
    }

private val ItemsSortMode.iconRes: Int
    get() = when (this) {
        ItemsSortMode.ALPHABETICAL -> DesignR.drawable.ic_sort_alphabet
        ItemsSortMode.CUSTOM -> DesignR.drawable.ic_sort_user
    }
