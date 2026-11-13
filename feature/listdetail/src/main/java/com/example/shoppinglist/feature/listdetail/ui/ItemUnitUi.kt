package com.example.shoppinglist.feature.listdetail.ui

import androidx.annotation.StringRes
import com.example.shoppinglist.feature.listdetail.R

enum class ItemUnitUi(val key: String, @StringRes val labelRes: Int) {
    PIECE("PIECE", R.string.list_detail_unit_piece),
    KILOGRAM("KILOGRAM", R.string.list_detail_unit_kilogram),
    GRAM("GRAM", R.string.list_detail_unit_gram),
    LITER("LITER", R.string.list_detail_unit_liter),
    MILLILITER("MILLILITER", R.string.list_detail_unit_milliliter),
    PACKAGE("PACKAGE", R.string.list_detail_unit_package),
    PACK("PACK", R.string.list_detail_unit_pack);

    companion object {
        fun fromKey(key: String?) = entries.firstOrNull { it.key == key }
    }
}
