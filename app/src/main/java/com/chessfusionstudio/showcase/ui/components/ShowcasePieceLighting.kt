package com.chessfusionstudio.showcase.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

internal object ShowcasePieceLighting {
    fun topColor(baseColor: Color): Color = lerp(baseColor, Color.White, 0.28f)
    fun bottomColor(baseColor: Color): Color = lerp(baseColor, Color.Black, 0.22f)
    fun outlineColor(baseColor: Color): Color = lerp(baseColor, Color.Black, 0.45f).copy(alpha = 0.9f)
    fun accentColor(baseColor: Color): Color = lerp(baseColor, Color.Black, 0.58f).copy(alpha = 0.75f)
    fun highlightColor(baseColor: Color): Color = lerp(baseColor, Color.White, 0.55f).copy(alpha = 0.42f)
    fun shadowColor(): Color = Color.Black.copy(alpha = 0.18f)
}
