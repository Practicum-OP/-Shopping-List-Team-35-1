package com.example.shoppinglist.feature.listdetail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ListDetailRoute(
    listId: String,
    onBackToLists: () -> Unit,
    viewModel: ListDetailViewModel = koinViewModel(
        parameters = { parametersOf(listId) },
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ListDetailScreen(
        state = state,
        onEvent = { event ->
            if (event == ListDetailEvent.BackClicked) {
                onBackToLists()
            } else {
                viewModel.onEvent(event)
            }
        },
    )
}
