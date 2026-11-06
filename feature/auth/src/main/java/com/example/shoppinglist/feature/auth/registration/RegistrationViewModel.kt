package com.example.shoppinglist.feature.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.core.domain.auth.interactor.Register
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

class RegistrationViewModel(
    private val register: Register,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationState(),
    )

    val state: StateFlow<RegistrationState> =
        _state.asStateFlow()

    private val effectChannel = Channel<RegistrationEffect>(
        capacity = Channel.BUFFERED,
    )

    val effects: Flow<RegistrationEffect> =
        effectChannel.receiveAsFlow()

    fun onEvent(event: RegistrationEvent) {
        if (_state.value.isLoading) {
            return
        }

        when (event) {
            is RegistrationEvent.EmailChanged -> {
                _state.update { current ->
                    current.copy(
                        email = event.email,
                        emailError = AuthValidator.validateEmail(
                            event.email,
                        ),
                    )
                }
            }

            is RegistrationEvent.PasswordChanged -> {
                _state.update { current ->
                    current.copy(
                        password = event.password,
                        passwordError = AuthValidator.validatePassword(
                            event.password,
                        ),
                        passwordRepeatError =
                            AuthValidator.validatePasswordRepeat(
                                password = event.password,
                                passwordRepeat =
                                    current.passwordRepeat,
                            ),
                    )
                }
            }

            is RegistrationEvent.PasswordRepeatChanged -> {
                _state.update { current ->
                    current.copy(
                        passwordRepeat = event.passwordRepeat,
                        passwordRepeatError =
                            AuthValidator.validatePasswordRepeat(
                                password = current.password,
                                passwordRepeat =
                                    event.passwordRepeat,
                            ),
                    )
                }
            }

            RegistrationEvent.PasswordVisibilityToggled -> {
                _state.update { current ->
                    current.copy(
                        isPasswordVisible =
                            !current.isPasswordVisible,
                    )
                }
            }

            RegistrationEvent.PasswordRepeatVisibilityToggled -> {
                _state.update { current ->
                    current.copy(
                        isPasswordRepeatVisible =
                            !current.isPasswordRepeatVisible,
                    )
                }
            }

            RegistrationEvent.RegistrationClicked -> {
                performRegistration()
            }

            RegistrationEvent.BackClicked -> {
                viewModelScope.launch {
                    effectChannel.send(
                        RegistrationEffect.NavigateBack,
                    )
                }
            }
        }
    }

    private fun performRegistration() {
        val current = _state.value

        val validated = current.copy(
            emailError = AuthValidator.validateEmail(
                current.email,
            ),
            passwordError = AuthValidator.validatePassword(
                current.password,
            ),
            passwordRepeatError =
                AuthValidator.validatePasswordRepeat(
                    password = current.password,
                    passwordRepeat = current.passwordRepeat,
                ),
        )

        _state.value = validated

        if (!validated.isRegistrationEnabled) {
            return
        }

        _state.value = validated.copy(
            isLoading = true,
        )

        viewModelScope.launch {
            val result = authOperation {
                register(
                    email = validated.email,
                    password = validated.password,
                )
            }

            _state.update { state ->
                state.copy(
                    isLoading = false,
                )
            }

            when (result) {
                is AuthResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            password = "",
                            passwordRepeat = "",
                            isPasswordVisible = false,
                            isPasswordRepeatVisible = false,
                        )
                    }

                    effectChannel.send(
                        RegistrationEffect.RegistrationCompleted,
                    )
                }

                is AuthResult.Failure -> {
                    effectChannel.send(
                        RegistrationEffect.ShowMessage(
                            messageRes = result.error.toMessageRes(),
                        ),
                    )
                }
            }
        }
    }
}
