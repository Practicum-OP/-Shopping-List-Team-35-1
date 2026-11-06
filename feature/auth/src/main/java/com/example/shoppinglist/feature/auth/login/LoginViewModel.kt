package com.example.shoppinglist.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.auth.interactor.Login
import com.example.shoppinglist.core.domain.auth.interactor.RestoreAuthSession
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

class LoginViewModel(
    private val login: Login,
    private val restoreAuthSession: RestoreAuthSession,
) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginState(
            isLoading = true,
        ),
    )

    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val effectChannel = Channel<LoginEffect>(
        capacity = Channel.BUFFERED,
    )

    val effects: Flow<LoginEffect> =
        effectChannel.receiveAsFlow()

    init {
        restoreSession()
    }

    fun onEvent(event: LoginEvent) {
        if (_state.value.isLoading) {
            return
        }

        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { current ->
                    current.copy(
                        email = event.email,
                        emailError = AuthValidator.validateEmail(
                            event.email,
                        ),
                    )
                }
            }

            is LoginEvent.PasswordChanged -> {
                _state.update { current ->
                    current.copy(
                        password = event.password,
                        passwordError = AuthValidator.validatePassword(
                            event.password,
                        ),
                    )
                }
            }

            LoginEvent.PasswordVisibilityToggled -> {
                _state.update { current ->
                    current.copy(
                        isPasswordVisible =
                        !current.isPasswordVisible,
                    )
                }
            }

            LoginEvent.LoginClicked -> {
                performLogin()
            }

            LoginEvent.RegistrationClicked -> {
                sendEffect(
                    LoginEffect.OpenRegistration,
                )
            }

            LoginEvent.RecoveryClicked -> {
                sendEffect(
                    LoginEffect.OpenRecovery,
                )
            }
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val result = authOperation {
                restoreAuthSession()
            }

            _state.update { current ->
                current.copy(
                    isLoading = false,
                )
            }

            when (result) {
                is AuthResult.Success -> {
                    if (result.data) {
                        effectChannel.send(
                            LoginEffect.OpenLists,
                        )
                    }
                }

                is AuthResult.Failure -> {
                    effectChannel.send(
                        LoginEffect.ShowMessage(
                            messageRes = result.error.toMessageRes(),
                        ),
                    )
                }
            }
        }
    }

    private fun performLogin() {
        val validated = validateLoginState(_state.value)
        _state.value = validated

        if (!validated.isLoginEnabled) {
            return
        }

        _state.value = validated.copy(isLoading = true)

        viewModelScope.launch {
            val result = authOperation {
                login(
                    email = validated.email,
                    password = validated.password,
                )
            }

            _state.update { current ->
                current.copy(isLoading = false)
            }

            handleLoginResult(result)
        }
    }

    private fun validateLoginState(
        current: LoginState,
    ): LoginState {
        return current.copy(
            emailError = AuthValidator.validateEmail(current.email),
            passwordError = AuthValidator.validatePassword(current.password),
        )
    }

    private suspend fun handleLoginResult(
        result: AuthResult<Unit>,
    ) {
        when (result) {
            is AuthResult.Success -> {
                _state.update { current ->
                    current.copy(
                        password = "",
                        isPasswordVisible = false,
                    )
                }

                effectChannel.send(LoginEffect.OpenLists)
            }

            is AuthResult.Failure -> {
                effectChannel.send(
                    LoginEffect.ShowMessage(
                        messageRes = result.error.toMessageRes(),
                    ),
                )
            }
        }
    }

    private fun sendEffect(
        effect: LoginEffect,
    ) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }
}
