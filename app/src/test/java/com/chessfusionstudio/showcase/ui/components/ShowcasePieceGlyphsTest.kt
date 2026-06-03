package com.chessfusionstudio.showcase.ui.components

import com.chessfusionstudio.core.model.Color
import com.chessfusionstudio.core.model.PieceType
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowcasePieceGlyphsTest {
    @Test
    fun glyphFor_usesChessCancunWhitePieceMapping() {
        assertEquals('k', ShowcasePieceGlyphs.glyphFor(PieceType.KING, Color.WHITE))
        assertEquals('q', ShowcasePieceGlyphs.glyphFor(PieceType.QUEEN, Color.WHITE))
        assertEquals('r', ShowcasePieceGlyphs.glyphFor(PieceType.ROOK, Color.WHITE))
        assertEquals('b', ShowcasePieceGlyphs.glyphFor(PieceType.BISHOP, Color.WHITE))
        assertEquals('n', ShowcasePieceGlyphs.glyphFor(PieceType.KNIGHT, Color.WHITE))
        assertEquals('p', ShowcasePieceGlyphs.glyphFor(PieceType.PAWN, Color.WHITE))
    }

    @Test
    fun glyphFor_usesChessCancunBlackPieceMapping() {
        assertEquals('l', ShowcasePieceGlyphs.glyphFor(PieceType.KING, Color.BLACK))
        assertEquals('w', ShowcasePieceGlyphs.glyphFor(PieceType.QUEEN, Color.BLACK))
        assertEquals('t', ShowcasePieceGlyphs.glyphFor(PieceType.ROOK, Color.BLACK))
        assertEquals('v', ShowcasePieceGlyphs.glyphFor(PieceType.BISHOP, Color.BLACK))
        assertEquals('m', ShowcasePieceGlyphs.glyphFor(PieceType.KNIGHT, Color.BLACK))
        assertEquals('o', ShowcasePieceGlyphs.glyphFor(PieceType.PAWN, Color.BLACK))
    }
}
