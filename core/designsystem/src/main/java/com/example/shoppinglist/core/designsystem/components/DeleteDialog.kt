package com.example.shoppinglist.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.R
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme

@Composable
fun DeleteDialog(
    onDelete: (Int?) -> Unit,
    onCancel: () -> Unit,
    dialogTitle: String,
    contentDescription: String?,
    idItem: Int?

) {
    AlertDialog(
        icon = {
            Icon(painter = painterResource(R.drawable.ic_warning), contentDescription = contentDescription)
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        onDismissRequest = {
            onCancel()
        },
        confirmButton = {
            Button(onClick = { onDelete(idItem) }) {
                Text(
                    stringResource(R.string.common_delete),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = { onCancel() }) {
                Text(
                    stringResource(R.string.common_cancel),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    )
}

@DevicePreviews
@Composable
fun DeleteDialogPreview() {
    ShoppingListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            DeleteDialog(
                {},
                {},
                stringResource(R.string.common_delete),
                null,
                null
            )
        }
    }

}
