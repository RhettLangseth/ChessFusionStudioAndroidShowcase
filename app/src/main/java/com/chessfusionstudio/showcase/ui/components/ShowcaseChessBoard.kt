package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardPreviewState
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardRenderer

@Composable
fun ShowcaseChessBoard(
    previewState: ShowcaseBoardPreviewState,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        with(ShowcaseBoardRenderer) { draw(previewState) }
    }
}

