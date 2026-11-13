package com.example.shoppinglist.core.domain.auth.model

sealed interface AuthError {
    data object InvalidEmail : AuthError
    data object PasswordTooShort : AuthError
    data object PasswordTooLong : AuthError
    data object UserAlreadyExists : AuthError
    data object InvalidCredentials : AuthError
    data object InvalidRefreshToken : AuthError
    data object Network : AuthError
    data class Unknown(val message: String? = null) : AuthError
}
