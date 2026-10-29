package com.example.shoppinglist.feature.auth.registration

import androidx.compose.runtime.Composable
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.feature.auth.model.AuthFieldError

@DevicePreviews
@Composable
private fun RegistrationScreenPreview() {
    ShoppingListTheme {
        RegistrationScreen(
            state = RegistrationState(
                email = "test@gmail.com",
                password = "1234567",
                passwordRepeat = "1234",
                passwordRepeatError = AuthFieldError.PasswordsMismatch,
            ),
            onEvent = {},
        )
    }
}
