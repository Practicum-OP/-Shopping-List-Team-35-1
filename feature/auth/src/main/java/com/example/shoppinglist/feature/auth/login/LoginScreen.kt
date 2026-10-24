package com.example.shoppinglist.feature.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.feature.auth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onRestorePassword: () -> Unit,
    onLogin: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.auth_login_title))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.auth_login_button))
            }

            OutlinedButton(
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.auth_registration_button))
            }

            OutlinedButton(
                onClick = onRestorePassword,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.auth_recovery_button))
            }
        }
    }
}
