package com.example.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.core.designsystem.components.DeleteDialog
import com.example.shoppinglist.core.designsystem.components.Placeholder
import com.example.shoppinglist.core.designsystem.theme.AppDimens
import com.example.shoppinglist.feature.lists.R
import com.example.shoppinglist.feature.lists.ui.components.CreateDialog
import com.example.shoppinglist.feature.lists.ui.components.SearchField
import com.example.shoppinglist.feature.lists.ui.components.ShoppingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    onOpenListDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }

    val topBarHeight = 64.dp
    val topBarTitleStartPadding = 16.dp

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(topBarHeight),
                title = {
                    Text(
                        text = stringResource(R.string.lists_title),
                        modifier = Modifier.padding(start = topBarTitleStartPadding),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = { /* действие 1 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.iconSize),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null, // лучше: "Удалить список"
                            modifier = Modifier.size(AppDimens.iconSize),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    IconButton(onClick = { /* действие 3 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_night_theme),
                            contentDescription = null,
                            modifier = Modifier.size(AppDimens.iconSize),
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Добавить список",
                    modifier = Modifier.size(AppDimens.iconSize),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchField(
                query = query,
                onQueryChange = { query = it },
                onBackClick = { /* навигация назад */ },
            )

            ShoppingListItem(
                name = "Тестовый список",
                listIconId = R.drawable.ic_cart,
                listIconContentDescription = "Иконка списка",
                onListIconClick = {},
                onListClick = onOpenListDetailClick,
                onDeleteClick = {},
                deleteContentDescription = "Удалить список",
            )

            Placeholder(
                imageRes = R.drawable.illustration_shopping_lists,
                titleRes = R.string.lists_empty_title,
                descriptionRes = R.string.lists_empty_description,
                contentDescriptionRes = R.string.lists_empty_image_description,
                modifier = Modifier.weight(1f),
            )
        }
    }

    if (showDeleteDialog) {
        DeleteDialog(
            onDelete = { id ->
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false },
            dialogTitle = R.string.lists_delete_all_dialog_title,
            contentDescription = R.string.lists_delete_all,
            idItem = null,
        )
    }

    if (showCreateDialog) {
        CreateDialog(
            onCreate = { name ->
                // создать список с именем name
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false },
        )
    }
}
