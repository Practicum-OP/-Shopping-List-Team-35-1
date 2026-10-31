package com.example.shoppinglist.feature.lists.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.feature.lists.R

@Composable
fun CreateDialog(
    onCreate: (String) -> Unit,
) {
    var textTitleList by rememberSaveable {
        mutableStateOf("")
    }

    val isCreateEnabled = textTitleList.isNotBlank()

    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_add_list),
                contentDescription = stringResource(
                    R.string.lists_create_dialog_title,
                ),
            )
        },
        title = {
            Text(
                text = stringResource(R.string.lists_create_dialog_title),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            OutlinedTextField(
                value = textTitleList,
                onValueChange = { newValue ->
                    textTitleList = newValue
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.lists_name_placeholder),
                    )
                },
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
                    onCreate(textTitleList.trim())
                },
                enabled = isCreateEnabled,
            ) {
                Text(
                    text = stringResource(R.string.lists_create),
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
fun EditDialogPreviewAdd() {
    ShoppingListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            CreateDialog {}
        }
    }
}
