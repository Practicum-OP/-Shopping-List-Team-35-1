package com.example.shoppinglist.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shoppinglist.core.designsystem.theme.AppDimens

/**
 * Основная кнопка действия на всю ширину. Пока идёт запрос ([isLoading]), вместо текста показывается индикатор.
 */
@Composable
fun ActionButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppDimens.buttonHeight),
        enabled = enabled,
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(AppDimens.iconSize))
        } else {
            Text(text = stringResource(textRes))
        }
    }
}
