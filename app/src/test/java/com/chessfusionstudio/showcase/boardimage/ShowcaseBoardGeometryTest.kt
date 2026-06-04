package com.chessfusionstudio.showcase.boardimage

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowcaseBoardGeometryTest {
    private val style = ShowcaseBoardStyle(
        lightSquareColor = Color.White,
        darkSquareColor = Color.Black
    )

    @Test
    fun squareRect_blackOrientationRotatesSquareLocationsBy180Degrees() {
        val metrics = ShowcaseBoardGeometry.resolve(Size(800f, 800f), style)

        val a1WhiteOrientation = metrics.squareRect(
            file = 0,
            rank = 0,
            orientation = ShowcaseBoardOrientation.WhiteAtBottom
        )
        val h8BlackOrientation = metrics.squareRect(
            file = 7,
            rank = 7,
            orientation = ShowcaseBoardOrientation.BlackAtBottom
        )
        val h1WhiteOrientation = metrics.squareRect(
            file = 7,
            rank = 0,
            orientation = ShowcaseBoardOrientation.WhiteAtBottom
        )
        val a8BlackOrientation = metrics.squareRect(
            file = 0,
            rank = 7,
            orientation = ShowcaseBoardOrientation.BlackAtBottom
        )

        assertEquals(a1WhiteOrientation, h8BlackOrientation)
        assertEquals(h1WhiteOrientation, a8BlackOrientation)
    }

    @Test
    fun resolve_centersSquareBoardInsideWideCanvas() {
        val metrics = ShowcaseBoardGeometry.resolve(Size(1000f, 800f), style)
        val outerLeft = metrics.boardRect.left - metrics.borderWidth
        val outerRight = metrics.boardRect.right + metrics.borderWidth

        assertEquals(100f, outerLeft, 0.001f)
        assertEquals(900f, outerRight, 0.001f)
    }
}
