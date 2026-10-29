package com.example.shoppinglist.feature.auth.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.shoppinglist.feature.auth.model.AuthValidator

/**
 * Пока нет ViewModel (Эпик 3), State хранится здесь: поля ввода и проверки работают,
 * а «Зарегистрироваться» сразу возвращает на авторизацию, без запроса к API.
 */
@Composable
fun RegistrationRoute(
    onBack: () -> Unit,
    onRegistration: () -> Unit,
) {
    var state by rememberSaveable { mutableStateOf(RegistrationState()) }

    RegistrationScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is RegistrationEvent.EmailChanged -> state = state.copy(
                    email = event.email,
                    emailError = AuthValidator.validateEmail(event.email),
                )

                // Повтор проверяется заново: он может перестать (или начать) совпадать с новым паролем.
                is RegistrationEvent.PasswordChanged -> state = state.copy(
                    password = event.password,
                    passwordError = AuthValidator.validatePassword(event.password),
                    passwordRepeatError = AuthValidator.validatePasswordRepeat(event.password, state.passwordRepeat),
                )

                is RegistrationEvent.PasswordRepeatChanged -> state = state.copy(
                    passwordRepeat = event.passwordRepeat,
                    passwordRepeatError = AuthValidator.validatePasswordRepeat(state.password, event.passwordRepeat),
                )

                RegistrationEvent.PasswordVisibilityToggled -> state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )

                RegistrationEvent.PasswordRepeatVisibilityToggled -> state = state.copy(
                    isPasswordRepeatVisible = !state.isPasswordRepeatVisible,
                )

                RegistrationEvent.RegistrationClicked -> if (state.isRegistrationEnabled) onRegistration()
                RegistrationEvent.BackClicked -> onBack()
            }
        },
    )
}
