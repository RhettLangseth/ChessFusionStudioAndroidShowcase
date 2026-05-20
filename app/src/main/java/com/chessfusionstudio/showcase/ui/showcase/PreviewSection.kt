package com.chessfusionstudio.showcase.ui.showcase

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardPreviewState
import com.chessfusionstudio.showcase.ui.components.OutlinedDropdownSelector
import com.chessfusionstudio.showcase.ui.components.ShowcaseChessBoard

@Composable
internal fun PreviewSection(
    previewState: ShowcaseBoardPreviewState,
    selectedPosition: PositionOption,
    positionOptions: List<PositionOption>,
    onPositionSelected: (PositionOption) -> Unit,
    modifier: Modifier = Modifier
) {
    ThemeStudioSection(title = "Preview", modifier = modifier) {
        ShowcaseChessBoard(previewState = previewState, modifier = Modifier.fillMaxWidth())
        Text(text = "Sample position", style = MaterialTheme.typography.labelLarge)
        OutlinedDropdownSelector(selected = selectedPosition, options = positionOptions, optionLabel = { it.label }, onSelected = onPositionSelected, modifier = Modifier.fillMaxWidth(), truncateMenuText = true)
    }
}
