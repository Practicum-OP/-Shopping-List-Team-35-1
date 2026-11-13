package com.example.shoppinglist.feature.auth.login

sealed interface LoginEvent {

    data class EmailChanged(val email: String) : LoginEvent

    data class PasswordChanged(val password: String) : LoginEvent

    data object PasswordVisibilityToggled : LoginEvent

    data object LoginClicked : LoginEvent

    data object RegistrationClicked : LoginEvent

    data object RecoveryClicked : LoginEvent
}
