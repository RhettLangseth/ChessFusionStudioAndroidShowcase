package com.chessfusionstudio.showcase.boardimage

import androidx.compose.ui.graphics.Color

private val DefaultBoardBorder = Color(0xFF151A1F).copy(alpha = 0.26f)
private val DefaultBoardShadow = Color(0xFF050607).copy(alpha = 0.14f)

data class ShowcaseBoardStyle(
    val lightSquareColor: Color,
    val darkSquareColor: Color,
    val borderColor: Color = DefaultBoardBorder,
    val shadowColor: Color = DefaultBoardShadow,
    val borderFraction: Float = 0.055f,
    val cornerRadiusFraction: Float = 0.055f
)
