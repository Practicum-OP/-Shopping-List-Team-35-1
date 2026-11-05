package com.example.shoppinglist.feature.lists.ui

import androidx.annotation.DrawableRes
import com.example.shoppinglist.feature.lists.R

object ListIconCatalog {

    const val CART = "cart"

    @DrawableRes
    fun drawableRes(iconKey: String): Int {
        return R.drawable.ic_cart
    }
}
