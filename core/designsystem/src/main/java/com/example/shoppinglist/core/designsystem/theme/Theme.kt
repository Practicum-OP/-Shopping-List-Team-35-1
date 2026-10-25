package com.example.shoppinglist.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightColorScheme = lightColorScheme(
    // Основной акцент
    primary = GoldenBrown,
    onPrimary = White,
    primaryContainer = PalePeach,
    onPrimaryContainer = DarkChocolate,

    // Вторичный акцент
    secondary = GoldenBrown, // Временное соответствие
    onSecondary = White, // Временное соответствие
    secondaryContainer = SoftPeach,
    onSecondaryContainer = DarkWalnut,

    // Третичный акцент — отсутствует в выписанной палитре
    tertiary = GoldenBrown,
    onTertiary = White,
    tertiaryContainer = SoftPeach,
    onTertiaryContainer = DarkWalnut,

    // Фон
    background = WarmIvory,
    onBackground = DeepBrown, // Использован onSurface

    // Поверхности
    surface = WarmIvory, // Использован background
    onSurface = DeepBrown,
    surfaceVariant = CreamBeige, // Временное соответствие
    onSurfaceVariant = DarkTaupe,

    // Контуры
    outline = WarmTaupe,
    outlineVariant = SandBeige, // Предположение для цвета с пометкой ???

    // Инверсные роли — временные соответствия
    inverseSurface = CoffeeBrown,
    inverseOnSurface = WarmIvory,
    inversePrimary = Apricot,

    // Уровни поверхностей
    surfaceDim = LightBeige, // Временное соответствие
    surfaceBright = WarmIvory, // Временное соответствие
    surfaceContainerLowest = White, // Временное соответствие
    surfaceContainerLow = WarmIvory, // Временное соответствие
    surfaceContainer = CreamBeige, // Временное соответствие
    surfaceContainerHigh = CreamBeige,
    surfaceContainerHighest = LightBeige,

    surfaceTint = GoldenBrown,
    scrim = Black,

    // error, onError, errorContainer и onErrorContainer
    // пока используются стандартные из lightColorScheme().
)

val DarkColorScheme = darkColorScheme(
    // Основной акцент: dark-medium-contrast
    primary = Apricot,
    onPrimary = White,
    primaryContainer = Caramel,
    onPrimaryContainer = Black, // Временное соответствие

    // Вторичный акцент: dark-medium-contrast
    secondary = LightTan,
    onSecondary = Black, // Временное соответствие
    secondaryContainer = MutedBrown,
    onSecondaryContainer = Black,

    // Третичный акцент — отсутствует в выписанной палитре
    tertiary = LightTan,
    onTertiary = Black,
    tertiaryContainer = MutedBrown,
    onTertiaryContainer = Black,

    // Фон
    background = BlackBrown,
    onBackground = RoseWhite, // Использован medium-contrast onSurface

    // Поверхности: dark-medium-contrast
    surface = BlackBrown,
    onSurface = RoseWhite,
    surfaceVariant = CocoaBrown, // Временное соответствие
    onSurfaceVariant = PaleTaupe,

    // Контуры
    outline = MediumTaupe, // Временно совпадает с outlineVariant
    outlineVariant = MediumTaupe,

    // Инверсные роли
    inverseSurface = LightBeige, // Временное соответствие
    inverseOnSurface = CoffeeBrown,
    inversePrimary = GoldenBrown, // Временное соответствие

    // Уровни поверхностей
    surfaceDim = BlackBrown, // Временное соответствие
    surfaceBright = CocoaBrown, // Временное соответствие
    surfaceContainerLowest = NearBlack,
    surfaceContainerLow = BlackBrown, // Временное соответствие
    surfaceContainer = CoffeeBrown, // Временное соответствие
    surfaceContainerHigh = CoffeeBrown,
    surfaceContainerHighest = CocoaBrown,

    surfaceTint = Apricot,
    scrim = Black,

    // error, onError, errorContainer и onErrorContainer
    // пока используются стандартные из darkColorScheme().
)

/**
 * Фиксированная палитра приложения.
 * Динамические цвета на основе обоев не используются.
 */
@Composable
fun ShoppingListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: Typography = AppTypography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = typography,
        content = content,
    )
}
