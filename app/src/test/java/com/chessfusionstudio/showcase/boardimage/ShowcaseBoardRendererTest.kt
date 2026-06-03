package com.chessfusionstudio.showcase.boardimage

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShowcaseBoardRendererTest {
    @Test
    fun isLightSquare_matchesStandardChessBoardColoring() {
        assertFalse(ShowcaseBoardRenderer.isLightSquare(file = 0, rank = 0)) // a1
        assertTrue(ShowcaseBoardRenderer.isLightSquare(file = 7, rank = 0)) // h1
        assertTrue(ShowcaseBoardRenderer.isLightSquare(file = 0, rank = 7)) // a8
        assertFalse(ShowcaseBoardRenderer.isLightSquare(file = 7, rank = 7)) // h8
    }
}
