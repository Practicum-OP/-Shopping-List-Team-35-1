package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppinglist.core.designsystem.theme.ShoppingListTheme
import com.example.shoppinglist.core.domain.settings.ThemeMode
import com.example.shoppinglist.core.domain.settings.ThemeSettingsRepository
import com.example.shoppinglist.navigation.AppNavHost
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ShoppingListApp()
        }
    }
}

@Composable
private fun ShoppingListApp() {
    val themeRepository = koinInject<ThemeSettingsRepository>()

    val themeMode by themeRepository
        .observeThemeMode()
        .collectAsStateWithLifecycle(
            initialValue = ThemeMode.SYSTEM,
        )

    val systemDarkTheme = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> systemDarkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    ShoppingListTheme(
        darkTheme = darkTheme,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
            )
        }
    }
}
