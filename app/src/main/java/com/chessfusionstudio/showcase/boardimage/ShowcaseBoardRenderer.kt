package com.chessfusionstudio.showcase.boardimage

import android.graphics.Typeface
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.chessfusionstudio.core.model.Square
import com.chessfusionstudio.showcase.ui.components.ShowcasePieceRenderer

internal object ShowcaseBoardRenderer {
    fun isLightSquare(file: Int, rank: Int): Boolean = (file + rank) % 2 != 0

    fun DrawScope.draw(previewState: ShowcaseBoardPreviewState, pieceTypeface: Typeface) {
        val metrics = ShowcaseBoardGeometry.resolve(size, previewState.boardStyle)
        val outerRect = Rect(
            left = metrics.boardRect.left - metrics.borderWidth,
            top = metrics.boardRect.top - metrics.borderWidth,
            right = metrics.boardRect.right + metrics.borderWidth,
            bottom = metrics.boardRect.bottom + metrics.borderWidth
        )
        drawRoundRect(
            color = previewState.boardStyle.borderColor,
            topLeft = outerRect.topLeft,
            size = outerRect.size,
            cornerRadius = CornerRadius(metrics.cornerRadius, metrics.cornerRadius)
        )
        drawRoundRect(
            color = previewState.boardStyle.lightSquareColor,
            topLeft = metrics.boardRect.topLeft,
            size = metrics.boardRect.size,
            cornerRadius = CornerRadius(metrics.cornerRadius * 0.72f, metrics.cornerRadius * 0.72f)
        )
        for (rank in 0 until 8) {
            for (file in 0 until 8) {
                val squareRect = metrics.squareRect(file, rank)
                val squareColor = if (isLightSquare(file, rank)) previewState.boardStyle.lightSquareColor else previewState.boardStyle.darkSquareColor
                drawRect(color = squareColor, topLeft = squareRect.topLeft, size = squareRect.size)
            }
        }
        for (index in 0 until 64) {
            val piece = previewState.gameState.pieceAt(index) ?: continue
            val square = Square.fromIndex(index)
            val squareRect = metrics.squareRect(square.file(), square.rank())
            val inset = metrics.squareSize * (1f - previewState.pieceStyle.scale) / 2f
            val pieceRect = Rect(squareRect.left + inset, squareRect.top + inset, squareRect.right - inset, squareRect.bottom - inset)
            with(ShowcasePieceRenderer) { drawPiece(piece, pieceRect, previewState.pieceStyle, pieceTypeface) }
        }
    }
}
