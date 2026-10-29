package com.example.shoppinglist.feature.auth.recovery

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
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.auth.R
import com.example.shoppinglist.core.designsystem.R as DesignSystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(
    state: RecoveryState,
    onEvent: (RecoveryEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.auth_recovery_title))
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(RecoveryEvent.BackClicked) }) {
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
                onValueChange = { onEvent(RecoveryEvent.EmailChanged(it)) },
                labelRes = R.string.auth_email_label,
                errorRes = state.emailError?.messageRes,
                enabled = !state.isLoading,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(RecoveryEvent.SendClicked) },
                ),
            )

            ActionButton(
                textRes = R.string.auth_recovery_send_button,
                onClick = { onEvent(RecoveryEvent.SendClicked) },
                enabled = state.isSendEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}
