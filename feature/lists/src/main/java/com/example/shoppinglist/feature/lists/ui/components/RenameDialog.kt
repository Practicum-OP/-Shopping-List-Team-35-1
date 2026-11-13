package com.example.shoppinglist.feature.lists.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.feature.lists.R

@Composable
fun RenameDialog(
    oldTitle: String,
    listId: String,
    onRename: (name: String, listId: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by rememberSaveable {
        mutableStateOf(oldTitle)
    }

    val isRenameEnabled = text.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    R.string.lists_rename_dialog_title,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(
                            R.string.lists_name_label,
                        ),
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor =
                    MaterialTheme.colorScheme.outline,
                    focusedBorderColor =
                    MaterialTheme.colorScheme.outline,
                    unfocusedLabelColor =
                    MaterialTheme.colorScheme.outline,
                    focusedLabelColor =
                    MaterialTheme.colorScheme.outline,
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onRename(
                        text.trim(),
                        listId,
                    )
                },
                enabled = isRenameEnabled,
            ) {
                Text(
                    text = stringResource(
                        R.string.lists_rename,
                    ),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(
                        R.string.lists_cancel,
                    ),
                )
            }
        },
    )
}
