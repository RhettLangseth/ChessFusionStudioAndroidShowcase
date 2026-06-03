package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardPreviewState
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardRenderer

@Composable
fun ShowcaseChessBoard(
    previewState: ShowcaseBoardPreviewState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pieceTypeface = remember(context) {
        ShowcasePieceRenderer.loadTypeface(context.applicationContext)
    }
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        with(ShowcaseBoardRenderer) { draw(previewState, pieceTypeface) }
    }
}

