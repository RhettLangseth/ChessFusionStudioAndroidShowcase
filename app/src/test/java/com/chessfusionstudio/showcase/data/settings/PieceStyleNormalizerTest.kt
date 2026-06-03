package com.chessfusionstudio.showcase.data.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class PieceStyleNormalizerTest {
    @Test
    fun normalizePieceStyle_acceptsKnownValues() {
        assertEquals(
            PIECE_STYLE_CHESS_CANCUN,
            normalizePieceStyle(" $PIECE_STYLE_CHESS_CANCUN ")
        )
    }

    @Test
    fun normalizePieceStyle_fallsBackForUnknownValues() {
        assertEquals(PIECE_STYLE_CHESS_CANCUN, normalizePieceStyle("unknown-style"))
    }
}

