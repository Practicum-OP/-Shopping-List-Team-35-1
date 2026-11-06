package com.example.shoppinglist.feature.lists.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.lists.R

private val SearchFieldHeight = 64.dp

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(SearchFieldHeight),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppDimens.screenHorizontalPadding,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(
                        AppDimens.iconButtonSize,
                    ),
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_back_arrow,
                        ),
                        contentDescription = stringResource(
                            R.string.lists_back,
                        ),
                        modifier = Modifier.size(
                            AppDimens.iconSize,
                        ),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (query.isEmpty()) {
                                Text(
                                    text = stringResource(
                                        R.string.lists_search_hint,
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }

                            innerTextField()
                        }
                    },
                )

                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = onClearClick,
                        modifier = Modifier.size(
                            AppDimens.iconButtonSize,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(
                                R.drawable.ic_close,
                            ),
                            contentDescription = stringResource(
                                R.string.lists_clear_search,
                            ),
                            modifier = Modifier.size(
                                AppDimens.iconSize,
                            ),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = AppDimens.dividerThickness,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}
