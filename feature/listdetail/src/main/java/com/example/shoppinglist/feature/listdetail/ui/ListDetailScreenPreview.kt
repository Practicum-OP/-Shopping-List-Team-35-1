package com.example.shoppinglist.feature.listdetail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.core.domain.lists.ItemsSortMode
import com.example.shoppinglist.feature.listdetail.ui.components.ItemFormContent
import com.example.shoppinglist.feature.listdetail.ui.components.ShoppingItemRow

private val previewItems = listOf(
    ShoppingItemUi("apple", "Яблоки", "1.5", ItemUnitUi.KILOGRAM.key, isPurchased = true),
    ShoppingItemUi("egg", "Яйца", "10", ItemUnitUi.PIECE.key, isPurchased = false),
    ShoppingItemUi("milk", "Молоко", "1", ItemUnitUi.LITER.key, isPurchased = false),
    ShoppingItemUi("cheese", "Сыр", "3", ItemUnitUi.PACKAGE.key, isPurchased = false),
    ShoppingItemUi("bread", "Хлеб", "", null, isPurchased = false),
    ShoppingItemUi("tea", "Чай", "2", ItemUnitUi.PACK.key, isPurchased = true),
)

private fun previewState(
    content: ListContent,
    sortMode: ItemsSortMode = ItemsSortMode.ALPHABETICAL,
) = ListDetailState(listId = "preview", listName = "Продукты", content = content, sortMode = sortMode)

@DevicePreviews
@Composable
private fun ListDetailLoadingPreview() {
    ShoppingListTheme {
        ListDetailScreen(state = previewState(ListContent.Loading), onEvent = {})
    }
}

@DevicePreviews
@Composable
private fun ListDetailEmptyPreview() {
    ShoppingListTheme {
        ListDetailScreen(state = previewState(ListContent.Empty), onEvent = {})
    }
}

@DevicePreviews
@Composable
private fun ListDetailItemsPreview() {
    ShoppingListTheme {
        ListDetailScreen(state = previewState(ListContent.Items(previewItems)), onEvent = {})
    }
}

@DevicePreviews
@Composable
private fun ListDetailCustomSortPreview() {
    ShoppingListTheme {
        ListDetailScreen(
            state = previewState(ListContent.Items(previewItems), ItemsSortMode.CUSTOM),
            onEvent = {},
        )
    }
}

@DevicePreviews
@Composable
private fun ItemFormCreatePreview() {
    ItemFormPreviewContainer(
        ItemFormState(
            mode = FormMode.Create,
            name = "Мол",
            suggestions = listOf("Кокосовое молоко", "Молоко", "Соевое молоко", "Сухое молоко"),
        ),
    )
}

@DevicePreviews
@Composable
private fun ItemFormEditErrorPreview() {
    ItemFormPreviewContainer(
        ItemFormState(
            mode = FormMode.Edit(itemId = "sausage"),
            name = "Колбаса",
            quantity = "abc",
            unit = ItemUnitUi.GRAM.key,
            quantityError = "Введите число",
        ),
    )
}

@DevicePreviews
@Composable
private fun ItemFormUnitPickerPreview() {
    ItemFormPreviewContainer(
        ItemFormState(
            mode = FormMode.Create,
            name = "Кефир",
            quantity = "2",
            unitPickerVisible = true,
        ),
    )
}

@Composable
private fun ItemFormPreviewContainer(form: ItemFormState) {
    ShoppingListTheme {
        Surface(color = MaterialTheme.colorScheme.surfaceDim) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                ItemFormContent(form = form, onEvent = {})
            }
        }
    }
}

@DevicePreviews
@Composable
private fun ShoppingItemRowPreview() {
    ShoppingListTheme {
        Surface {
            Column {
                ShoppingItemRow(
                    item = previewItems[1],
                    onPurchaseToggle = {},
                    onEditClick = {},
                    onDeleteClick = {},
                )
                ShoppingItemRow(
                    item = previewItems[0],
                    onPurchaseToggle = {},
                    onEditClick = {},
                    onDeleteClick = {},
                )
            }
        }
    }
}
