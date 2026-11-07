package com.example.shoppinglist.feature.listdetail.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.core.designsystem.theme.stepperIcon
import com.example.shoppinglist.feature.listdetail.R
import com.example.shoppinglist.feature.listdetail.ui.ItemFormState
import com.example.shoppinglist.feature.listdetail.ui.ItemUnitUi
import com.example.shoppinglist.feature.listdetail.ui.ListDetailEvent
import com.example.shoppinglist.core.designsystem.R as DesignR

private const val SUGGESTIONS_WIDTH_FRACTION = 0.55f
private const val VISIBLE_UNIT_ROWS = 3

@Composable
internal fun ItemFormFields(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
) {
    var nameFieldHeight by remember { mutableIntStateOf(0) }
    val suggestionsTopPadding = with(LocalDensity.current) { nameFieldHeight.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = AppDimens.spacingMedium,
                end = AppDimens.spacingMedium,
                bottom = AppDimens.spacingMedium,
            ),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
            NameField(
                form = form,
                onEvent = onEvent,
                modifier = Modifier.onSizeChanged { nameFieldHeight = it.height },
            )
            QuantitySection(form = form, onEvent = onEvent)
        }
        if (form.suggestions.isNotEmpty()) {
            SuggestionsList(
                suggestions = form.suggestions,
                onSuggestionClick = { onEvent(ListDetailEvent.SuggestionSelected(it)) },
                modifier = Modifier
                    .padding(top = suggestionsTopPadding)
                    .fillMaxWidth(SUGGESTIONS_WIDTH_FRACTION),
            )
        }
    }
}

@Composable
private fun NameField(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = form.name,
        onValueChange = { onEvent(ListDetailEvent.NameChanged(it)) },
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = stringResource(R.string.list_detail_item_label)) },
        placeholder = { Text(text = stringResource(R.string.list_detail_item_placeholder)) },
        supportingText = form.nameError?.let { error -> { Text(text = error) } },
        isError = form.nameError != null,
        singleLine = true,
        colors = itemFormFieldColors(),
    )
}

@Composable
private fun QuantitySection(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
) {
    BoxWithConstraints {
        if (maxWidth < AppDimens.itemFormCompactWidth) {
            CompactQuantitySection(form = form, onEvent = onEvent)
        } else {
            RegularQuantitySection(form = form, onEvent = onEvent)
        }
    }
}

@Composable
private fun RegularQuantitySection(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
            QuantityField(form = form, onEvent = onEvent, modifier = Modifier.weight(1f))
            UnitField(form = form, onEvent = onEvent, modifier = Modifier.weight(1f))
            QuantityButtons(form = form, onEvent = onEvent)
        }
        if (form.unitPickerVisible) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
                Spacer(modifier = Modifier.weight(1f))
                UnitPicker(onEvent = onEvent, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(AppDimens.iconButtonSize * 2 + AppDimens.spacingSmall))
            }
        }
    }
}

@Composable
private fun CompactQuantitySection(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spacingSmall)) {
            QuantityField(form = form, onEvent = onEvent, modifier = Modifier.weight(1f))
            QuantityButtons(form = form, onEvent = onEvent)
        }
        UnitField(form = form, onEvent = onEvent, modifier = Modifier.fillMaxWidth())
        if (form.unitPickerVisible) {
            UnitPicker(onEvent = onEvent, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun QuantityField(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = form.quantity,
        onValueChange = { onEvent(ListDetailEvent.QuantityChanged(it)) },
        modifier = modifier,
        label = { FieldLabel(R.string.list_detail_quantity_label) },
        supportingText = form.quantityError?.let { error -> { Text(text = error) } },
        isError = form.quantityError != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        colors = itemFormFieldColors(),
    )
}

@Composable
private fun QuantityButtons(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
) {
    QuantityButton(
        iconRes = DesignR.drawable.ic_remove,
        contentDescriptionRes = R.string.list_detail_quantity_decrease,
        enabled = form.quantity.isNotBlank(),
        onClick = { onEvent(ListDetailEvent.QuantityDecreased) },
    )
    QuantityButton(
        iconRes = DesignR.drawable.ic_add,
        contentDescriptionRes = R.string.list_detail_quantity_increase,
        onClick = { onEvent(ListDetailEvent.QuantityIncreased) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitField(
    form: ItemFormState,
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val unitLabel = ItemUnitUi.fromKey(form.unit)?.let { stringResource(it.labelRes) }.orEmpty()
    val toggleEvent = if (form.unitPickerVisible) {
        ListDetailEvent.UnitPickerClosed
    } else {
        ListDetailEvent.UnitPickerOpened
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = unitLabel,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { FieldLabel(R.string.list_detail_unit_label) },
            singleLine = true,
            colors = itemFormFieldColors(),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { onEvent(toggleEvent) },
        )
        ExposedDropdownMenuDefaults.TrailingIcon(
            expanded = form.unitPickerVisible,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(top = AppDimens.spacingSmall, end = AppDimens.spacingSmall),
        )
    }
}

@Composable
private fun QuantityButton(
    @DrawableRes iconRes: Int,
    @StringRes contentDescriptionRes: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = Modifier.padding(top = AppDimens.spacingMedium),
        enabled = enabled,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            contentColor = MaterialTheme.colorScheme.stepperIcon,
        ),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = stringResource(contentDescriptionRes),
        )
    }
}

@Composable
private fun UnitPicker(
    onEvent: (ListDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = AppDimens.spacingExtraSmall,
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = AppDimens.listItemMinHeight * VISIBLE_UNIT_ROWS)
                .verticalScroll(rememberScrollState()),
        ) {
            ItemUnitUi.entries.forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(unit.labelRes),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    onClick = { onEvent(ListDetailEvent.UnitSelected(unit.key)) },
                )
            }
        }
    }
}

@Composable
private fun SuggestionsList(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = AppDimens.spacingExtraSmall,
    ) {
        Column {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(text = suggestion, style = MaterialTheme.typography.bodyLarge) },
                    onClick = { onSuggestionClick(suggestion) },
                )
            }
        }
    }
}

@Composable
private fun itemFormFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedLabelColor = MaterialTheme.colorScheme.outline,
    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
    focusedBorderColor = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    cursorColor = MaterialTheme.colorScheme.outline,
)

@Composable
private fun FieldLabel(@StringRes textRes: Int) {
    Text(
        text = stringResource(textRes),
        style = MaterialTheme.typography.bodySmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
