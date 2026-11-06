package com.example.shoppinglist.feature.auth.model

import androidx.annotation.StringRes
import com.example.shoppinglist.core.domain.auth.model.AuthError
import com.example.shoppinglist.feature.auth.R

@StringRes
internal fun AuthError.toMessageRes(): Int {
    return when (this) {
        AuthError.InvalidEmail -> {
            R.string.auth_error_email_invalid
        }

        AuthError.PasswordTooShort -> {
            R.string.auth_error_password_too_short
        }

        AuthError.PasswordTooLong -> {
            R.string.auth_error_password_too_long
        }

        AuthError.UserAlreadyExists -> {
            R.string.auth_error_user_exists
        }

        AuthError.InvalidCredentials -> {
            R.string.auth_error_wrong_credentials
        }

        AuthError.InvalidRefreshToken -> {
            R.string.auth_error_session_expired
        }

        AuthError.Network -> {
            R.string.auth_error_network
        }

        is AuthError.Unknown -> {
            R.string.auth_error_unknown
        }
    }
}
