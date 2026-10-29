package com.example.shoppinglist.feature.auth.model

private const val MIN_PASSWORD_LENGTH = 7

private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

/**
 * Field checks for auth screens. An empty field is not an error yet:
 * the button stays disabled, but nothing is shown under the field.
 */
internal object AuthValidator {

    fun validateEmail(email: String): AuthFieldError? =
        if (email.isEmpty() || EMAIL_REGEX.matches(email.trim())) null else AuthFieldError.EmailInvalid

    fun validatePassword(password: String): AuthFieldError? =
        if (password.isEmpty() || password.length >= MIN_PASSWORD_LENGTH) null else AuthFieldError.PasswordTooShort

    fun validatePasswordRepeat(password: String, passwordRepeat: String): AuthFieldError? =
        if (passwordRepeat.isEmpty() || passwordRepeat == password) null else AuthFieldError.PasswordsMismatch
}
