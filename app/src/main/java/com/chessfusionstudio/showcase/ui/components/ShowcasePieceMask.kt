package com.chessfusionstudio.showcase.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.chessfusionstudio.core.model.PieceType

internal object ShowcasePieceMask {
    fun build(type: PieceType, rect: Rect): Path {
        return when (type) {
            PieceType.PAWN -> pawnPath(rect)
            PieceType.KNIGHT -> knightPath(rect)
            PieceType.BISHOP -> bishopPath(rect)
            PieceType.ROOK -> rookPath(rect)
            PieceType.QUEEN -> queenPath(rect)
            PieceType.KING -> kingPath(rect)
        }
    }

    fun DrawScope.drawAccent(type: PieceType, rect: Rect, color: androidx.compose.ui.graphics.Color) {
        val stroke = Stroke(width = rect.width * 0.045f)
        when (type) {
            PieceType.PAWN -> drawLine(color, Offset(rect.center.x, rect.top + rect.height * 0.37f), Offset(rect.center.x, rect.bottom - rect.height * 0.18f), stroke.width)
            PieceType.KNIGHT -> drawPath(Path().apply { moveTo(rect.left + rect.width * 0.44f, rect.top + rect.height * 0.28f); lineTo(rect.left + rect.width * 0.62f, rect.top + rect.height * 0.25f); lineTo(rect.left + rect.width * 0.57f, rect.top + rect.height * 0.42f) }, color, style = stroke)
            PieceType.BISHOP -> drawLine(color, Offset(rect.center.x - rect.width * 0.08f, rect.top + rect.height * 0.18f), Offset(rect.center.x + rect.width * 0.08f, rect.top + rect.height * 0.42f), stroke.width)
            PieceType.ROOK -> listOf(0.3f, 0.5f, 0.7f).forEach { x -> drawLine(color, Offset(rect.left + rect.width * x, rect.top + rect.height * 0.14f), Offset(rect.left + rect.width * x, rect.top + rect.height * 0.28f), stroke.width) }
            PieceType.QUEEN -> listOf(0.26f, 0.5f, 0.74f).forEach { x -> drawCircle(color, rect.width * 0.035f, Offset(rect.left + rect.width * x, rect.top + rect.height * 0.14f)) }
            PieceType.KING -> {
                drawLine(color, Offset(rect.center.x, rect.top + rect.height * 0.05f), Offset(rect.center.x, rect.top + rect.height * 0.23f), stroke.width)
                drawLine(color, Offset(rect.center.x - rect.width * 0.08f, rect.top + rect.height * 0.14f), Offset(rect.center.x + rect.width * 0.08f, rect.top + rect.height * 0.14f), stroke.width)
            }
        }
    }

    private fun pawnPath(rect: Rect): Path = Path().apply {
        addOval(Rect(rect.left + rect.width * 0.36f, rect.top + rect.height * 0.08f, rect.right - rect.width * 0.36f, rect.top + rect.height * 0.36f))
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.30f, rect.top + rect.height * 0.28f, rect.right - rect.width * 0.30f, rect.bottom - rect.height * 0.18f, rect.width * 0.12f, rect.width * 0.12f))
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.20f, rect.bottom - rect.height * 0.22f, rect.right - rect.width * 0.20f, rect.bottom - rect.height * 0.10f, rect.width * 0.06f, rect.width * 0.06f))
    }

    private fun rookPath(rect: Rect): Path = Path().apply {
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.24f, rect.top + rect.height * 0.18f, rect.right - rect.width * 0.24f, rect.bottom - rect.height * 0.16f, rect.width * 0.06f, rect.width * 0.06f))
        moveTo(rect.left + rect.width * 0.24f, rect.top + rect.height * 0.18f)
        lineTo(rect.left + rect.width * 0.24f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.34f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.34f, rect.top + rect.height * 0.18f)
        lineTo(rect.left + rect.width * 0.44f, rect.top + rect.height * 0.18f)
        lineTo(rect.left + rect.width * 0.44f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.56f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.56f, rect.top + rect.height * 0.18f)
        lineTo(rect.left + rect.width * 0.66f, rect.top + rect.height * 0.18f)
        lineTo(rect.left + rect.width * 0.66f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.76f, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.76f, rect.top + rect.height * 0.18f)
        close()
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.18f, rect.bottom - rect.height * 0.20f, rect.right - rect.width * 0.18f, rect.bottom - rect.height * 0.08f, rect.width * 0.06f, rect.width * 0.06f))
    }

    private fun bishopPath(rect: Rect): Path = Path().apply {
        moveTo(rect.center.x, rect.top + rect.height * 0.06f)
        cubicTo(rect.right - rect.width * 0.18f, rect.top + rect.height * 0.28f, rect.right - rect.width * 0.22f, rect.top + rect.height * 0.52f, rect.center.x, rect.bottom - rect.height * 0.22f)
        cubicTo(rect.left + rect.width * 0.22f, rect.top + rect.height * 0.52f, rect.left + rect.width * 0.18f, rect.top + rect.height * 0.28f, rect.center.x, rect.top + rect.height * 0.06f)
        close()
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.22f, rect.bottom - rect.height * 0.20f, rect.right - rect.width * 0.22f, rect.bottom - rect.height * 0.08f, rect.width * 0.06f, rect.width * 0.06f))
    }

    private fun queenPath(rect: Rect): Path = Path().apply {
        moveTo(rect.left + rect.width * 0.18f, rect.bottom - rect.height * 0.20f)
        lineTo(rect.left + rect.width * 0.28f, rect.top + rect.height * 0.16f)
        lineTo(rect.left + rect.width * 0.40f, rect.top + rect.height * 0.36f)
        lineTo(rect.center.x, rect.top + rect.height * 0.10f)
        lineTo(rect.left + rect.width * 0.60f, rect.top + rect.height * 0.36f)
        lineTo(rect.left + rect.width * 0.72f, rect.top + rect.height * 0.16f)
        lineTo(rect.right - rect.width * 0.18f, rect.bottom - rect.height * 0.20f)
        close()
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.16f, rect.bottom - rect.height * 0.24f, rect.right - rect.width * 0.16f, rect.bottom - rect.height * 0.08f, rect.width * 0.06f, rect.width * 0.06f))
    }

    private fun kingPath(rect: Rect): Path = Path().apply {
        moveTo(rect.center.x, rect.top + rect.height * 0.10f)
        lineTo(rect.center.x, rect.top + rect.height * 0.26f)
        cubicTo(rect.right - rect.width * 0.18f, rect.top + rect.height * 0.28f, rect.right - rect.width * 0.22f, rect.top + rect.height * 0.58f, rect.center.x, rect.bottom - rect.height * 0.22f)
        cubicTo(rect.left + rect.width * 0.22f, rect.top + rect.height * 0.58f, rect.left + rect.width * 0.18f, rect.top + rect.height * 0.28f, rect.center.x, rect.top + rect.height * 0.26f)
        close()
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.18f, rect.bottom - rect.height * 0.20f, rect.right - rect.width * 0.18f, rect.bottom - rect.height * 0.08f, rect.width * 0.06f, rect.width * 0.06f))
    }

    private fun knightPath(rect: Rect): Path = Path().apply {
        moveTo(rect.left + rect.width * 0.26f, rect.bottom - rect.height * 0.12f)
        lineTo(rect.left + rect.width * 0.44f, rect.top + rect.height * 0.14f)
        lineTo(rect.left + rect.width * 0.66f, rect.top + rect.height * 0.14f)
        lineTo(rect.left + rect.width * 0.60f, rect.top + rect.height * 0.32f)
        lineTo(rect.right - rect.width * 0.18f, rect.top + rect.height * 0.46f)
        lineTo(rect.left + rect.width * 0.58f, rect.bottom - rect.height * 0.16f)
        close()
        addRoundRect(androidx.compose.ui.geometry.RoundRect(rect.left + rect.width * 0.16f, rect.bottom - rect.height * 0.20f, rect.right - rect.width * 0.18f, rect.bottom - rect.height * 0.08f, rect.width * 0.06f, rect.width * 0.06f))
    }
}
