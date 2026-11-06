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

    private val _state = MutableStateFlow(RegistrationState())

    val state: StateFlow<RegistrationState> = _state.asStateFlow()

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
            is RegistrationEvent.EmailChanged ->
                changeEmail(event.email)

            is RegistrationEvent.PasswordChanged ->
                changePassword(event.password)

            is RegistrationEvent.PasswordRepeatChanged ->
                changePasswordRepeat(event.passwordRepeat)

            RegistrationEvent.PasswordVisibilityToggled ->
                togglePasswordVisibility()

            RegistrationEvent.PasswordRepeatVisibilityToggled ->
                togglePasswordRepeatVisibility()

            RegistrationEvent.RegistrationClicked ->
                performRegistration()

            RegistrationEvent.BackClicked ->
                navigateBack()
        }
    }

    private fun changeEmail(email: String) {
        _state.update { current ->
            current.copy(
                email = email,
                emailError = AuthValidator.validateEmail(email),
            )
        }
    }

    private fun changePassword(password: String) {
        _state.update { current ->
            current.copy(
                password = password,
                passwordError = AuthValidator.validatePassword(password),
                passwordRepeatError = AuthValidator.validatePasswordRepeat(
                    password = password,
                    passwordRepeat = current.passwordRepeat,
                ),
            )
        }
    }

    private fun changePasswordRepeat(passwordRepeat: String) {
        _state.update { current ->
            current.copy(
                passwordRepeat = passwordRepeat,
                passwordRepeatError = AuthValidator.validatePasswordRepeat(
                    password = current.password,
                    passwordRepeat = passwordRepeat,
                ),
            )
        }
    }

    private fun togglePasswordVisibility() {
        _state.update { current ->
            current.copy(
                isPasswordVisible = !current.isPasswordVisible,
            )
        }
    }

    private fun togglePasswordRepeatVisibility() {
        _state.update { current ->
            current.copy(
                isPasswordRepeatVisible = !current.isPasswordRepeatVisible,
            )
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            effectChannel.send(RegistrationEffect.NavigateBack)
        }
    }

    private fun validateState(
        current: RegistrationState,
    ): RegistrationState {
        return current.copy(
            emailError = AuthValidator.validateEmail(current.email),
            passwordError = AuthValidator.validatePassword(current.password),
            passwordRepeatError = AuthValidator.validatePasswordRepeat(
                password = current.password,
                passwordRepeat = current.passwordRepeat,
            ),
        )
    }

    private fun performRegistration() {
        val validated = validateState(_state.value)
        _state.value = validated

        if (!validated.isRegistrationEnabled) {
            return
        }

        _state.value = validated.copy(isLoading = true)

        viewModelScope.launch {
            val result = authOperation {
                register(
                    email = validated.email,
                    password = validated.password,
                )
            }

            _state.update { current ->
                current.copy(isLoading = false)
            }

            handleRegistrationResult(result)
        }
    }

    private suspend fun handleRegistrationResult(
        result: AuthResult<Unit>,
    ) {
        when (result) {
            is AuthResult.Success -> {
                clearPasswords()
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

    private fun clearPasswords() {
        _state.update { current ->
            current.copy(
                password = "",
                passwordRepeat = "",
                isPasswordVisible = false,
                isPasswordRepeatVisible = false,
            )
        }
    }
}
