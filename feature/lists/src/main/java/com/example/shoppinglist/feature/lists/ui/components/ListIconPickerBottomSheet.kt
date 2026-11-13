package com.example.shoppinglist.feature.lists.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.feature.lists.R
import com.example.shoppinglist.feature.lists.ui.ListIconCatalog

private val IconGridMinCellSize = 64.dp
private val IconGridMaxHeight = 480.dp
private val IconGridHorizontalPadding = 16.dp
private val IconGridBottomPadding = 24.dp
private val IconGridItemSpacing = 8.dp
private val IconButtonSize = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListIconPickerBottomSheet(
    onIconSelected: (iconKey: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(
                    minSize = IconGridMinCellSize,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = IconGridMaxHeight),
                contentPadding = PaddingValues(
                    start = IconGridHorizontalPadding,
                    end = IconGridHorizontalPadding,
                    bottom = IconGridBottomPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    IconGridItemSpacing,
                ),
                verticalArrangement = Arrangement.spacedBy(
                    IconGridItemSpacing,
                ),
            ) {
                itemsIndexed(
                    items = ListIconCatalog.selectableKeys,
                    key = { _, iconKey -> iconKey },
                ) { index, iconKey ->
                    IconButton(
                        onClick = {
                            onIconSelected(iconKey)
                        },
                        modifier = Modifier.size(IconButtonSize),
                    ) {
                        RoundIcon(
                            iconId = ListIconCatalog.drawableRes(iconKey),
                            contentDescription = stringResource(
                                R.string.lists_icon_picker_item,
                                index + 1,
                            ),
                        )
                    }
                }
            }
        }
    }
}
