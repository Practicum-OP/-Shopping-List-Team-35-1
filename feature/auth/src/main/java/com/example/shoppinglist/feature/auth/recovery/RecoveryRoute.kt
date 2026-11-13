package com.example.shoppinglist.feature.auth.recovery

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppinglist.feature.auth.model.CollectAuthEffects
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecoveryRoute(
    onBack: () -> Unit,
    onRestorePassword: () -> Unit,
    viewModel: RecoveryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val resources = LocalResources.current

    CollectAuthEffects(
        effects = viewModel.effects,
    ) { effect ->
        when (effect) {
            RecoveryEffect.RecoveryCompleted -> {
                onRestorePassword()
            }

            RecoveryEffect.NavigateBack -> {
                onBack()
            }

            is RecoveryEffect.ShowMessage -> {
                launch {
                    snackbarHostState.showSnackbar(
                        message = resources.getString(
                            effect.messageRes,
                        ),
                    )
                }
            }
        }
    }

    RecoveryScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
    )
}
