package com.example.shoppinglist.feature.auth.recovery

import com.example.shoppinglist.feature.auth.model.AuthFieldError

data class RecoveryState(
    val email: String = "",
    val emailError: AuthFieldError? = null,
    val isLoading: Boolean = false,
) {
    val isSendEnabled: Boolean
        get() = email.isNotBlank() && emailError == null && !isLoading
}
