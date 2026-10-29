package com.example.shoppinglist.feature.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.shoppinglist.feature.auth.model.AuthValidator

/**
 * Пока нет ViewModel (Эпик 3), State хранится здесь: поля ввода и проверки работают,
 * а «Войти» ведёт сразу на экран списков, без запроса к API.
 */
@Composable
fun LoginRoute(
    onOpenRegistration: () -> Unit,
    onOpenRecovery: () -> Unit,
    onOpenLists: () -> Unit,
) {
    var state by rememberSaveable { mutableStateOf(LoginState()) }

    LoginScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is LoginEvent.EmailChanged -> state = state.copy(
                    email = event.email,
                    emailError = AuthValidator.validateEmail(event.email),
                )

                is LoginEvent.PasswordChanged -> state = state.copy(
                    password = event.password,
                    passwordError = AuthValidator.validatePassword(event.password),
                )

                LoginEvent.PasswordVisibilityToggled -> state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible,
                )

                LoginEvent.LoginClicked -> if (state.isLoginEnabled) onOpenLists()
                LoginEvent.RegistrationClicked -> onOpenRegistration()
                LoginEvent.RecoveryClicked -> onOpenRecovery()
            }
        },
    )
}
