package com.example.shoppinglist.core.designsystem.theme

import androidx.compose.ui.unit.dp

/**
 * Повторяющиеся размеры дизайн-системы (значения из макета Figma).
 * Системные отступы (status bar, navigation bar, клавиатура) берутся из WindowInsets.
 */
object AppDimens {
    // Расстояния между элементами
    val spacingExtraSmall = 4.dp
    val spacingSmall = 8.dp
    val spacingMedium = 16.dp
    val spacingLarge = 24.dp
    val spacingExtraLarge = 48.dp

    // Экран
    val screenHorizontalPadding = 16.dp

    // Элементы списков (карточки списков и строки товаров)
    val listItemHorizontalPadding = 16.dp
    val listItemVerticalPadding = 8.dp
    val listItemMinHeight = 56.dp
    val listItemTwoLineMinHeight = 72.dp
    val cardCornerRadius = 12.dp

    // Иконки и кнопки
    val iconSize = 24.dp
    val iconContainerSize = 40.dp
    val iconButtonSize = 48.dp
    val buttonHeight = 48.dp

    // Диалоги и bottom sheet
    val dialogPadding = 24.dp
    val dialogCornerRadius = 28.dp
    val bottomSheetCornerRadius = 28.dp

    // Иллюстрация пустого состояния
    val emptyStateImageWidth = 324.dp
    val emptyStateImageHeight = 300.dp
}
