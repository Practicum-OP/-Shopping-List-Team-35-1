package com.example.shoppinglist.feature.listdetail.ui

import com.example.shoppinglist.core.domain.listdetail.interactor.ChangeItemsSortMode
import com.example.shoppinglist.core.domain.listdetail.interactor.ClearPurchasedShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteAllShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.DeleteShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.MoveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveItemSuggestions
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingItems
import com.example.shoppinglist.core.domain.listdetail.interactor.ObserveShoppingList
import com.example.shoppinglist.core.domain.listdetail.interactor.SaveShoppingItem
import com.example.shoppinglist.core.domain.listdetail.interactor.ToggleShoppingItemPurchased

data class ListDetailInteractors(
    val observeShoppingList: ObserveShoppingList,
    val observeShoppingItems: ObserveShoppingItems,
    val observeItemSuggestions: ObserveItemSuggestions,
    val saveShoppingItem: SaveShoppingItem,
    val toggleShoppingItemPurchased: ToggleShoppingItemPurchased,
    val deleteShoppingItem: DeleteShoppingItem,
    val deleteAllShoppingItems: DeleteAllShoppingItems,
    val clearPurchasedShoppingItems: ClearPurchasedShoppingItems,
    val changeItemsSortMode: ChangeItemsSortMode,
    val moveShoppingItem: MoveShoppingItem,
)
