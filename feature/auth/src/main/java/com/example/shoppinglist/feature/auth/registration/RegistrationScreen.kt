package com.example.shoppinglist.feature.auth.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.shoppinglist.core.designsystem.components.ActionButton
import com.example.shoppinglist.core.designsystem.components.EmailTextField
import com.example.shoppinglist.core.designsystem.components.PasswordTextField
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.auth.R
import com.example.shoppinglist.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onEvent: (RegistrationEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.auth_registration_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(RegistrationEvent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(DesignSystemR.string.common_back),
                        )
                    }
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
                onValueChange = { onEvent(RegistrationEvent.EmailChanged(it)) },
                labelRes = R.string.auth_email_label,
                errorRes = state.emailError?.messageRes,
                enabled = !state.isLoading,
            )

            PasswordTextField(
                value = state.password,
                onValueChange = { onEvent(RegistrationEvent.PasswordChanged(it)) },
                labelRes = R.string.auth_password_label,
                isPasswordVisible = state.isPasswordVisible,
                onVisibilityToggle = { onEvent(RegistrationEvent.PasswordVisibilityToggled) },
                errorRes = state.passwordError?.messageRes,
                enabled = !state.isLoading,
                imeAction = ImeAction.Next,
            )

            PasswordTextField(
                value = state.passwordRepeat,
                onValueChange = { onEvent(RegistrationEvent.PasswordRepeatChanged(it)) },
                labelRes = R.string.auth_password_repeat_label,
                isPasswordVisible = state.isPasswordRepeatVisible,
                onVisibilityToggle = { onEvent(RegistrationEvent.PasswordRepeatVisibilityToggled) },
                errorRes = state.passwordRepeatError?.messageRes,
                enabled = !state.isLoading,
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(RegistrationEvent.RegistrationClicked) },
                ),
            )

            ActionButton(
                textRes = R.string.auth_registration_button,
                onClick = { onEvent(RegistrationEvent.RegistrationClicked) },
                enabled = state.isRegistrationEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}
