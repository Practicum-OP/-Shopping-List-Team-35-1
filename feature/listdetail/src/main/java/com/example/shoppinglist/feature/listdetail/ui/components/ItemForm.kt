package com.example.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.listdetail.R
import com.example.shoppinglist.feature.listdetail.ui.FormMode
import com.example.shoppinglist.feature.listdetail.ui.ItemFormState
import com.example.shoppinglist.feature.listdetail.ui.ListDetailEvent
import com.example.shoppinglist.core.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemForm(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(ListDetailEvent.FormClosed) },
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RectangleShape,
        containerColor = Color.Transparent,
        tonalElevation = 0.dp,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0) },
    ) {
        ItemFormContent(
            form = form,
            onEvent = onEvent,
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemFormContent(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = AppDimens.bottomSheetSideMargin),
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacingMedium),
        horizontalAlignment = Alignment.End,
    ) {
        SaveButton(
            mode = form.mode,
            enabled = form.name.isNotBlank(),
            onClick = { onEvent(ListDetailEvent.FormSubmitted) },
            modifier = Modifier.padding(end = AppDimens.spacingMedium),
        )
        Surface(
            modifier = Modifier.weight(1f, fill = false),
            shape = RoundedCornerShape(
                topStart = AppDimens.bottomSheetCornerRadius,
                topEnd = AppDimens.bottomSheetCornerRadius,
            ),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BottomSheetDefaults.DragHandle()
                ItemFormFields(form = form, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun SaveButton(
    mode: FormMode,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescriptionRes = when (mode) {
        FormMode.Create -> R.string.list_detail_add_item
        is FormMode.Edit -> R.string.list_detail_save_item
    }

    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(AppDimens.fabSize),
        enabled = enabled,
        shape = FloatingActionButtonDefaults.shape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        Icon(
            painter = painterResource(DesignR.drawable.ic_check),
            contentDescription = stringResource(contentDescriptionRes),
        )
    }
}
