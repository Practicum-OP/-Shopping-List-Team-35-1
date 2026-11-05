package com.example.shoppinglist.feature.lists.ui

import androidx.annotation.DrawableRes
import com.example.shoppinglist.feature.lists.R

object ListIconCatalog {

    const val BABY = "baby"
    const val BAG = "bag"
    const val BONE = "bone"
    const val BOOK = "book"
    const val CAKE = "cake"
    const val CAMERA = "camera"
    const val CAR = "car"
    const val CART = "cart"
    const val CAT = "cat"
    const val CELEBRATION = "celebration"
    const val CHILD = "child"
    const val CLOTHING = "clothing"
    const val CREDIT_CARD = "credit_card"
    const val DRINKS = "drinks"
    const val ELECTRONICS = "electronics"
    const val FLOWER = "flower"
    const val FURNITURE = "furniture"
    const val GAMING = "gaming"
    const val GIFT = "gift"
    const val HOME = "home"
    const val LIST = "list"
    const val LUGGAGE = "luggage"
    const val MEDICATION = "medication"
    const val MEDITATION = "meditation"
    const val MICROWAVE = "microwave"
    const val PALETTE = "palette"
    const val PETS = "pets"
    const val ROD = "rod"
    const val SCHOOL = "school"
    const val SELF_CARE = "self_care"
    const val SKATEBOARD = "skateboard"
    const val SKIS = "skis"
    const val SNOWBOARD = "snowboard"
    const val STROLLER = "stroller"
    const val TOOLS = "tools"
    const val TOYS = "toys"

    @DrawableRes
    fun drawableRes(iconKey: String): Int {
        return when (iconKey) {
            BABY -> R.drawable.ic_baby
            BAG -> R.drawable.ic_bag
            BONE -> R.drawable.ic_bone
            BOOK -> R.drawable.ic_book
            CAKE -> R.drawable.ic_cake
            CAMERA -> R.drawable.ic_camera
            CAR -> R.drawable.ic_car
            CART -> R.drawable.ic_cart
            CAT -> R.drawable.ic_cat
            CELEBRATION -> R.drawable.ic_celebration
            CHILD -> R.drawable.ic_child
            CLOTHING -> R.drawable.ic_clothing
            CREDIT_CARD -> R.drawable.ic_credit_card
            DRINKS -> R.drawable.ic_drinks
            ELECTRONICS -> R.drawable.ic_electronics
            FLOWER -> R.drawable.ic_flower
            FURNITURE -> R.drawable.ic_furniture
            GAMING -> R.drawable.ic_gaming
            GIFT -> R.drawable.ic_gift
            HOME -> R.drawable.ic_home
            LIST -> R.drawable.ic_list
            LUGGAGE -> R.drawable.ic_luggage
            MEDICATION -> R.drawable.ic_medication
            MEDITATION -> R.drawable.ic_meditation
            MICROWAVE -> R.drawable.ic_microwave
            PALETTE -> R.drawable.ic_palette
            PETS -> R.drawable.ic_pets
            ROD -> R.drawable.ic_rod
            SCHOOL -> R.drawable.ic_school
            SELF_CARE -> R.drawable.ic_self_care
            SKATEBOARD -> R.drawable.ic_skateboard
            SKIS -> R.drawable.ic_skis
            SNOWBOARD -> R.drawable.ic_snowboard
            STROLLER -> R.drawable.ic_stroller
            TOOLS -> R.drawable.ic_tools
            TOYS -> R.drawable.ic_toys
            else -> R.drawable.ic_list
        }
    }

    /**
     * Иконки списков, доступные для выбора пользователем.
     */
    val selectableKeys: List<String> = listOf(
        LIST,
        CART,
        BABY,
        BAG,
        BONE,
        BOOK,
        CAKE,
        CAMERA,
        CAR,
        CAT,
        CELEBRATION,
        CHILD,
        CLOTHING,
        CREDIT_CARD,
        DRINKS,
        ELECTRONICS,
        FLOWER,
        FURNITURE,
        GAMING,
        GIFT,
        HOME,
        LUGGAGE,
        MEDICATION,
        MEDITATION,
        MICROWAVE,
        PALETTE,
        PETS,
        ROD,
        SCHOOL,
        SELF_CARE,
        SKATEBOARD,
        SKIS,
        SNOWBOARD,
        STROLLER,
        TOOLS,
        TOYS,
    )
}
