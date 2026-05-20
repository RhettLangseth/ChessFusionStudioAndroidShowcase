package com.chessfusionstudio.showcase.boardimage

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

internal object ShowcaseBoardGeometry {
    data class Metrics(val boardRect: Rect, val squareSize: Float, val borderWidth: Float, val cornerRadius: Float) {
        fun squareRect(file: Int, rank: Int): Rect {
            val left = boardRect.left + (file * squareSize)
            val top = boardRect.top + ((7 - rank) * squareSize)
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
