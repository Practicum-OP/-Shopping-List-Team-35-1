package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shoppinglist.core.designsystem.preview.DevicePreviews
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme

@DevicePreviews
@Composable
private fun MyComponentPreview() {
    ShoppingListTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            IntroScreen { }
        }
    }
}
