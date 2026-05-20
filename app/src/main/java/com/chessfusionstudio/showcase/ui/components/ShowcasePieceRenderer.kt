package com.chessfusionstudio.showcase.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.chessfusionstudio.core.model.Piece

data class ShowcasePieceStyle(
    val whiteFillColor: Color,
    val blackFillColor: Color,
    val scale: Float
)

internal object ShowcasePieceRenderer {
    fun DrawScope.drawPiece(piece: Piece, rect: Rect, style: ShowcasePieceStyle) {
        val baseColor = if (piece.color().toFenChar() == 'w') style.whiteFillColor else style.blackFillColor
        val shadowPath = Path().apply { addPath(ShowcasePieceMask.build(piece.type(), rect), Offset(rect.width * 0.04f, rect.height * 0.05f)) }
        val mask = ShowcasePieceMask.build(piece.type(), rect)
        drawPath(shadowPath, ShowcasePieceLighting.shadowColor())
        drawPath(mask, Brush.verticalGradient(listOf(ShowcasePieceLighting.topColor(baseColor), baseColor, ShowcasePieceLighting.bottomColor(baseColor)), startY = rect.top, endY = rect.bottom))
        drawPath(mask, ShowcasePieceLighting.outlineColor(baseColor), style = Stroke(width = rect.width * 0.038f))
        drawPath(mask, ShowcasePieceLighting.highlightColor(baseColor), style = Stroke(width = rect.width * 0.018f))
        with(ShowcasePieceMask) { drawAccent(piece.type(), rect, ShowcasePieceLighting.accentColor(baseColor)) }
    }
}
