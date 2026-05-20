package com.chessfusionstudio.showcase.boardimage

import com.chessfusionstudio.core.model.GameState
import com.chessfusionstudio.showcase.ui.components.ShowcasePieceStyle

data class ShowcaseBoardPreviewState(
    val gameState: GameState,
    val boardStyle: ShowcaseBoardStyle,
    val pieceStyle: ShowcasePieceStyle
)
