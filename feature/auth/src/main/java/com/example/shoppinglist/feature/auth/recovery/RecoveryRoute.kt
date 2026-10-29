package com.example.shoppinglist.feature.auth.recovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.shoppinglist.feature.auth.model.AuthValidator

/**
 * Пока нет ViewModel (Эпик 3), State хранится здесь: поле ввода и проверка работают,
 * а кнопка отправки сразу возвращает на авторизацию, без запроса к API.
 */
@Composable
fun RecoveryRoute(
    onBack: () -> Unit,
    onRestorePassword: () -> Unit,
) {
    var state by rememberSaveable { mutableStateOf(RecoveryState()) }

    RecoveryScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is RecoveryEvent.EmailChanged -> state = state.copy(
                    email = event.email,
                    emailError = AuthValidator.validateEmail(event.email),
                )

                RecoveryEvent.SendClicked -> if (state.isSendEnabled) onRestorePassword()
                RecoveryEvent.BackClicked -> onBack()
            }
        },
    )
}
