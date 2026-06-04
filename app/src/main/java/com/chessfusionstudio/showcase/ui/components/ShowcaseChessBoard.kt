package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.testTag
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardOrientation
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardPreviewState
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardRenderer

@Composable
fun ShowcaseChessBoard(
    previewState: ShowcaseBoardPreviewState,
    modifier: Modifier = Modifier,
    accessibilityLabel: String = defaultBoardAccessibilityLabel(previewState)
) {
    val context = LocalContext.current
    val pieceTypeface = remember(context) {
        ShowcasePieceRenderer.loadTypeface(context.applicationContext)
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .testTag("showcase-board")
            .semantics {
                contentDescription = accessibilityLabel
            }
    ) {
        with(ShowcaseBoardRenderer) { draw(previewState, pieceTypeface) }
    }
}

private fun defaultBoardAccessibilityLabel(previewState: ShowcaseBoardPreviewState): String {
    val sideToMove = previewState.gameState.sideToMove().name
        .lowercase()
        .replaceFirstChar(Char::uppercase)
    val orientation = when (previewState.orientation) {
        ShowcaseBoardOrientation.WhiteAtBottom -> "white pieces at the bottom"
        ShowcaseBoardOrientation.BlackAtBottom -> "black pieces at the bottom"
    }
    return "Chess board preview. $sideToMove to move, $orientation."
}
