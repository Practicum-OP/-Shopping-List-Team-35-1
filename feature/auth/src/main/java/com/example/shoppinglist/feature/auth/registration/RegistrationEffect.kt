package com.example.shoppinglist.feature.auth.registration

import androidx.annotation.StringRes

sealed interface RegistrationEffect {

    data object RegistrationCompleted : RegistrationEffect

    data object NavigateBack : RegistrationEffect

    data class ShowMessage(@StringRes val messageRes: Int) : RegistrationEffect
}
