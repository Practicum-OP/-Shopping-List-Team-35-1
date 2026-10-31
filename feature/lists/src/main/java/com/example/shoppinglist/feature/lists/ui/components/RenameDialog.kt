package com.example.shoppinglist.feature.lists.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.feature.lists.R

@Composable
fun RenameDialog(
    onRename: (String, Int) -> Unit,
    oldTitle: String,
    idList: Int,
) {
    var textTitleList by rememberSaveable {
        mutableStateOf(oldTitle)
    }

    val isRenameEnabled = textTitleList.isNotBlank()

    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.lists_rename_dialog_title),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        text = {
            OutlinedTextField(
                value = textTitleList,
                onValueChange = { newValue ->
                    textTitleList = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                label = if (textTitleList.isNotEmpty()) {
                    {
                        Text(
                            text = stringResource(
                                R.string.lists_name_label,
                            ),
                        )
                    }
                } else {
                    null
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.outline,
                ),
            )
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onRename(textTitleList.trim(), idList)
                },
                enabled = isRenameEnabled,
            ) {
                Text(
                    text = stringResource(R.string.lists_rename),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { },
            ) {
                Text(
                    text = stringResource(R.string.lists_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
    )
}

@DevicePreviews
@Composable
fun RenameDialogPreview() {
    ShoppingListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            RenameDialog({ string, i -> }, "sdsf", 5)
        }
    }
}
