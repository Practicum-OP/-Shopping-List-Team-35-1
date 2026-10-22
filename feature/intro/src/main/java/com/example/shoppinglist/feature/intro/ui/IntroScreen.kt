package com.example.shoppinglist.feature.intro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntroScreen(
    onLoginClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    onRecoveryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Добро пожаловать")

        Button(onClick = onLoginClick) {
            Text(text = "Войти")
        }

        Button(onClick = onRegistrationClick) {
            Text(text = "Зарегистрироваться")
        }

        Button(onClick = onRecoveryClick) {
            Text(text = "Восстановить пароль")
        }
    }
}
