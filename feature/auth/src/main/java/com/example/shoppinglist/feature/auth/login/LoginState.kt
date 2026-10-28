package com.example.shoppinglist.feature.auth.login

import com.example.shoppinglist.feature.auth.model.AuthFieldError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: AuthFieldError? = null,
    val passwordError: AuthFieldError? = null,
    val isLoading: Boolean = false,
) {
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() &&
            password.isNotBlank() &&
            emailError == null &&
            passwordError == null &&
            !isLoading
}
