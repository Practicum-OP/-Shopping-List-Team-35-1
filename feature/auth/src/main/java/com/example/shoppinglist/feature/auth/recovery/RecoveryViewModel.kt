package com.example.shoppinglist.feature.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.auth.interactor.RecoverPassword
import com.example.shoppinglist.core.domain.auth.model.AuthResult
import com.example.shoppinglist.feature.auth.model.AuthValidator
import com.example.shoppinglist.feature.auth.model.authOperation
import com.example.shoppinglist.feature.auth.model.toMessageRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecoveryViewModel(
    private val recoverPassword: RecoverPassword,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RecoveryState(),
    )

    val state: StateFlow<RecoveryState> =
        _state.asStateFlow()

    private val effectChannel = Channel<RecoveryEffect>(
        capacity = Channel.BUFFERED,
    )

    val effects: Flow<RecoveryEffect> =
        effectChannel.receiveAsFlow()

    fun onEvent(event: RecoveryEvent) {
        if (_state.value.isLoading) {
            return
        }

        when (event) {
            is RecoveryEvent.EmailChanged -> {
                _state.update { current ->
                    current.copy(
                        email = event.email,
                        emailError = AuthValidator.validateEmail(
                            event.email,
                        ),
                    )
                }
            }

            RecoveryEvent.SendClicked -> {
                performRecovery()
            }

            RecoveryEvent.BackClicked -> {
                viewModelScope.launch {
                    effectChannel.send(
                        RecoveryEffect.NavigateBack,
                    )
                }
            }
        }
    }

    private fun performRecovery() {
        val current = _state.value

        val validated = current.copy(
            emailError = AuthValidator.validateEmail(
                current.email,
            ),
        )

        _state.value = validated

        if (!validated.isSendEnabled) {
            return
        }

        _state.value = validated.copy(
            isLoading = true,
        )

        viewModelScope.launch {
            val result = authOperation {
                recoverPassword(
                    email = validated.email,
                )
            }

            _state.update { state ->
                state.copy(
                    isLoading = false,
                )
            }

            when (result) {
                is AuthResult.Success -> {
                    effectChannel.send(
                        RecoveryEffect.RecoveryCompleted,
                    )
                }

                is AuthResult.Failure -> {
                    effectChannel.send(
                        RecoveryEffect.ShowMessage(
                            messageRes = result.error.toMessageRes(),
                        ),
                    )
                }
            }
        }
    }
}
