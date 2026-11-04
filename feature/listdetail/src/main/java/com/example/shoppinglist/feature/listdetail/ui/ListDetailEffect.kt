package com.example.shoppinglist.feature.listdetail.ui

import androidx.annotation.StringRes

sealed interface ListDetailEffect {

    data object NavigateBack : ListDetailEffect

    data class ShowSnackbar(
        @StringRes val messageRes: Int,
    ) : ListDetailEffect
}
