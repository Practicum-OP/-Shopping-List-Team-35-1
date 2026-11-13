package com.example.shoppinglist.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val ColorScheme.listDivider: Color
    get() = LilacGray

val ColorScheme.sortLabel: Color
    get() = EmeraldGreen

val ColorScheme.sortLabelHighlighted: Color
    get() = if (isDark) MossGreen else SlateViolet

val ColorScheme.stepperIcon: Color
    get() = if (isDark) onSurface else onSecondaryContainer

val ColorScheme.swipeActionContainer: Color
    get() = if (isDark) Umber else secondaryContainer

val ColorScheme.onSwipeAction: Color
    get() = if (isDark) SoftPeach else onSecondaryContainer

private val ColorScheme.isDark: Boolean
    get() = background == BlackBrown
