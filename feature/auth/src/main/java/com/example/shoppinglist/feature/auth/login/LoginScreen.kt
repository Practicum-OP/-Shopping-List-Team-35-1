package com.example.shoppinglist.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.components.ActionButton
import com.example.shoppinglist.core.designsystem.components.EmailTextField
import com.example.shoppinglist.core.designsystem.components.PasswordTextField
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.auth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.auth_login_title))
                },
            )
        },
    ) { innerPadding ->
        // Прокрутка нужна в горизонтальной ориентации и на маленьких экранах:
        // если форма помещается, она остаётся по центру.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = AppDimens.screenHorizontalPadding,
                    vertical = AppDimens.spacingMedium,
                ),
            verticalArrangement = Arrangement.spacedBy(
                space = AppDimens.spacingMedium,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EmailTextField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                labelRes = R.string.auth_email_label,
                errorRes = state.emailError?.messageRes,
                enabled = !state.isLoading,
            )

            PasswordTextField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                labelRes = R.string.auth_password_label,
                isPasswordVisible = state.isPasswordVisible,
                onVisibilityToggle = { onEvent(LoginEvent.PasswordVisibilityToggled) },
                errorRes = state.passwordError?.messageRes,
                enabled = !state.isLoading,
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(LoginEvent.LoginClicked) },
                ),
            )

            ActionButton(
                textRes = R.string.auth_login_button,
                onClick = { onEvent(LoginEvent.LoginClicked) },
                enabled = state.isLoginEnabled,
                isLoading = state.isLoading,
            )

            OutlinedButton(
                onClick = { onEvent(LoginEvent.RegistrationClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
            ) {
                Text(text = stringResource(R.string.auth_registration_button))
            }

            OutlinedButton(
                onClick = { onEvent(LoginEvent.RecoveryClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
            ) {
                Text(text = stringResource(R.string.auth_recovery_button))
            }
        }
    }
}
