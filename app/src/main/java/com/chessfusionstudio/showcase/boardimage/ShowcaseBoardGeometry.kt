package com.chessfusionstudio.showcase.boardimage

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

internal object ShowcaseBoardGeometry {
    data class Metrics(val boardRect: Rect, val squareSize: Float, val borderWidth: Float, val cornerRadius: Float) {
        fun squareRect(
            file: Int,
            rank: Int,
            orientation: ShowcaseBoardOrientation = ShowcaseBoardOrientation.WhiteAtBottom
        ): Rect {
            val displayFile = if (orientation == ShowcaseBoardOrientation.WhiteAtBottom) file else 7 - file
            val displayRank = if (orientation == ShowcaseBoardOrientation.WhiteAtBottom) 7 - rank else rank
            val left = boardRect.left + (displayFile * squareSize)
            val top = boardRect.top + (displayRank * squareSize)
            return Rect(left, top, left + squareSize, top + squareSize)
        }
    }

    fun resolve(size: Size, style: ShowcaseBoardStyle): Metrics {
        val boardSize = minOf(size.width, size.height)
        val left = (size.width - boardSize) / 2f
        val top = (size.height - boardSize) / 2f
        val borderWidth = (boardSize * style.borderFraction).coerceAtLeast(6f)
        val cornerRadius = boardSize * style.cornerRadiusFraction
        val innerSize = (boardSize - (borderWidth * 2f)).coerceAtLeast(8f)
        return Metrics(Rect(Offset(left + borderWidth, top + borderWidth), Size(innerSize, innerSize)), innerSize / 8f, borderWidth, cornerRadius)
    }
}
