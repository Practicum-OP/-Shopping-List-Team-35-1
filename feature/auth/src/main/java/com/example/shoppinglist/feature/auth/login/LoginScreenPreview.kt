package com.example.shoppinglist.feature.auth.login

import androidx.compose.runtime.Composable
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.feature.auth.model.AuthFieldError

@DevicePreviews
@Composable
private fun LoginScreenPreview() {
    ShoppingListTheme {
        LoginScreen(
            state = LoginState(
                email = "test@",
                password = "123",
                emailError = AuthFieldError.EmailInvalid,
                passwordError = AuthFieldError.PasswordTooShort,
            ),
            onEvent = {},
        )
    }
}
