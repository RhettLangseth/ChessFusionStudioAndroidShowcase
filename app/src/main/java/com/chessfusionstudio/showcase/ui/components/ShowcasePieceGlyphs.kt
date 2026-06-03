package com.chessfusionstudio.showcase.ui.components

import com.chessfusionstudio.core.model.Color
import com.chessfusionstudio.core.model.Piece
import com.chessfusionstudio.core.model.PieceType

internal object ShowcasePieceGlyphs {
    fun glyphFor(piece: Piece): String = glyphFor(piece.type(), piece.color()).toString()

    fun glyphFor(type: PieceType, color: Color): Char {
        val baseGlyph = when (color) {
            Color.WHITE -> when (type) {
                PieceType.KING -> 'K'
                PieceType.QUEEN -> 'Q'
                PieceType.ROOK -> 'R'
                PieceType.BISHOP -> 'B'
                PieceType.KNIGHT -> 'N'
                PieceType.PAWN -> 'P'
            }
            Color.BLACK -> when (type) {
                PieceType.KING -> 'L'
                PieceType.QUEEN -> 'W'
                PieceType.ROOK -> 'T'
                PieceType.BISHOP -> 'V'
                PieceType.KNIGHT -> 'M'
                PieceType.PAWN -> 'O'
            }
        }
        return baseGlyph.lowercaseChar()
    }
}
