package com.example.shoppinglist.feature.auth.registration

sealed interface RegistrationEvent {

    data class EmailChanged(val email: String) : RegistrationEvent

    data class PasswordChanged(val password: String) : RegistrationEvent

    data class PasswordRepeatChanged(val passwordRepeat: String) : RegistrationEvent

    data object PasswordVisibilityToggled : RegistrationEvent

    data object PasswordRepeatVisibilityToggled : RegistrationEvent

    data object RegistrationClicked : RegistrationEvent

    data object BackClicked : RegistrationEvent
}
