package com.example.shoppinglist.feature.auth.registration

import android.os.Parcelable
import com.example.shoppinglist.feature.auth.model.AuthFieldError
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationState(
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val isPasswordVisible: Boolean = false,
    val isPasswordRepeatVisible: Boolean = false,
    val emailError: AuthFieldError? = null,
    val passwordError: AuthFieldError? = null,
    val passwordRepeatError: AuthFieldError? = null,
    val isLoading: Boolean = false,
) : Parcelable {
    val isRegistrationEnabled: Boolean
        get() = email.isNotBlank() &&
            password.isNotBlank() &&
            passwordRepeat.isNotBlank() &&
            emailError == null &&
            passwordError == null &&
            passwordRepeatError == null &&
            !isLoading
}
