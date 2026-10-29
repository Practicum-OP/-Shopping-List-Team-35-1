package com.example.shoppinglist.feature.auth.recovery

import androidx.compose.runtime.Composable
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme

@DevicePreviews
@Composable
private fun RecoveryScreenPreview() {
    ShoppingListTheme {
        RecoveryScreen(
            state = RecoveryState(email = "test@gmail.com"),
            onEvent = {},
        )
    }
}
