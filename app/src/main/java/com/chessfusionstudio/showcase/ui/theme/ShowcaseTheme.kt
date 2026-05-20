package com.chessfusionstudio.showcase.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun ShowcaseTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = resolveAppColorScheme(APP_THEME_COLOR_SLATE, darkTheme),
        typography = Typography(),
        content = content
    )
}
