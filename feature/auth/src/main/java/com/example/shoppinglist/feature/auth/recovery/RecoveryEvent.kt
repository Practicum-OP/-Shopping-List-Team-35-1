package com.example.shoppinglist.feature.auth.recovery

sealed interface RecoveryEvent {

    data class EmailChanged(val email: String) : RecoveryEvent

    data object SendClicked : RecoveryEvent

    data object BackClicked : RecoveryEvent
}
