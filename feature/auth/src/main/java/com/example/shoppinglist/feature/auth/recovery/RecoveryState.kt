package com.example.shoppinglist.feature.auth.recovery

import android.os.Parcelable
import com.example.shoppinglist.feature.auth.model.AuthFieldError
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecoveryState(
    val email: String = "",
    val emailError: AuthFieldError? = null,
    val isLoading: Boolean = false,
) : Parcelable {
    val isSendEnabled: Boolean
        get() = email.isNotBlank() && emailError == null && !isLoading
}
