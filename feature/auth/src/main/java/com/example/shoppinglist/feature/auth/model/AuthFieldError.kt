package com.example.shoppinglist.feature.auth.model

import androidx.annotation.StringRes
import com.example.shoppinglist.feature.auth.R

/**
 * Validation error shown under an input field while the user types.
 */
enum class AuthFieldError(@StringRes val messageRes: Int) {
    EmailInvalid(R.string.auth_error_email_invalid),
    PasswordTooShort(R.string.auth_error_password_too_short),
    PasswordsMismatch(R.string.auth_error_passwords_mismatch),
}
