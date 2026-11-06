package com.example.shoppinglist.feature.auth.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppinglist.feature.auth.model.CollectAuthEffects
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    onOpenRegistration: () -> Unit,
    onOpenRecovery: () -> Unit,
    onOpenLists: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val context = LocalContext.current

    CollectAuthEffects(
        effects = viewModel.effects,
    ) { effect ->
        when (effect) {
            LoginEffect.OpenLists -> {
                onOpenLists()
            }

            LoginEffect.OpenRegistration -> {
                onOpenRegistration()
            }

            LoginEffect.OpenRecovery -> {
                onOpenRecovery()
            }

            is LoginEffect.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(
                        effect.messageRes,
                    ),
                )
            }
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}
