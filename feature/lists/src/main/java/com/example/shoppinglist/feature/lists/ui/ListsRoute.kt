package com.example.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListsRoute(
    onOpenListDetail: (String) -> Unit,
    viewModel: ListsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val currentOnOpenListDetail by rememberUpdatedState(
        newValue = onOpenListDetail,
    )

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListsEffect.OpenList -> {
                    currentOnOpenListDetail(effect.listId)
                }

                is ListsEffect.ShowSnackbar -> {
                    launch {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
    ) { innerPadding ->
        ListsScreen(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = androidx.compose.ui.Modifier.padding(
                innerPadding,
            ),
        )
    }
}
