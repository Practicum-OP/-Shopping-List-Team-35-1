package com.example.shoppinglist.feature.auth.recovery

import androidx.annotation.StringRes

sealed interface RecoveryEffect {

    data object RecoveryCompleted : RecoveryEffect

    data object NavigateBack : RecoveryEffect

    data class ShowMessage(@StringRes val messageRes: Int) : RecoveryEffect
}
