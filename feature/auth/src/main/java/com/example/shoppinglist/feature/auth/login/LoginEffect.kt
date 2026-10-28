package com.example.shoppinglist.feature.auth.login

import androidx.annotation.StringRes

sealed interface LoginEffect {

    data object OpenLists : LoginEffect

    data object OpenRegistration : LoginEffect

    data object OpenRecovery : LoginEffect

    data class ShowMessage(@StringRes val messageRes: Int) : LoginEffect
}
