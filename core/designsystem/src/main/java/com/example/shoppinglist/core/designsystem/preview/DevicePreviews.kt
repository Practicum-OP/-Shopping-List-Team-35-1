package com.example.shoppinglist.core.designsystem.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Retention(AnnotationRetention.BINARY)

// Маленький смартфон
@Preview(
    name = "Маленький смартфон — светлая",
    group = "Светлая тема",
    widthDp = 320,
    heightDp = 568,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Маленький смартфон — тёмная",
    group = "Тёмная тема",
    widthDp = 320,
    heightDp = 568,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)

// Маленький смартфон горизонтально
@Preview(
    name = "Маленький горизонтально — светлая",
    group = "Светлая тема",
    widthDp = 568,
    heightDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Маленький горизонтально — тёмная",
    group = "Тёмная тема",
    widthDp = 568,
    heightDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)

// Обычный смартфон
@Preview(
    name = "Обычный смартфон — светлая",
    group = "Светлая тема",
    widthDp = 393,
    heightDp = 851,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Обычный смартфон — тёмная",
    group = "Тёмная тема",
    widthDp = 393,
    heightDp = 851,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)

// Обычный смартфон горизонтально
@Preview(
    name = "Обычный горизонтально — светлая",
    group = "Светлая тема",
    widthDp = 851,
    heightDp = 393,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Обычный горизонтально — тёмная",
    group = "Тёмная тема",
    widthDp = 851,
    heightDp = 393,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)

// Планшет
@Preview(
    name = "Планшет — светлая",
    group = "Светлая тема",
    widthDp = 800,
    heightDp = 1280,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Планшет — тёмная",
    group = "Тёмная тема",
    widthDp = 800,
    heightDp = 1280,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
annotation class DevicePreviews

/*
Использование:

@DevicePreviews
@Composable
private fun MyComponentPreview() {
    ShoppingTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            // тут помещается наш экран
        }
    }
}
*/
