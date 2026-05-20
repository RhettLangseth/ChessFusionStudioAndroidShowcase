package com.chessfusionstudio.showcase.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.ui.components.AppSlider
import com.chessfusionstudio.showcase.ui.components.OutlinedDropdownSelector

@Composable
internal fun PieceThemeSection(
    selectedPalette: PalettePairOption,
    options: List<PalettePairOption>,
    pieceScale: Float,
    whitePieceColor: Color,
    blackPieceColor: Color,
    onPaletteSelected: (PalettePairOption) -> Unit,
    onPieceScaleChanged: (Float) -> Unit,
    onWhitePiecePressed: () -> Unit,
    onBlackPiecePressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    ThemeStudioSection(title = "Piece Theme", modifier = modifier) {
        Text(text = "Palette", style = MaterialTheme.typography.labelLarge)
        OutlinedDropdownSelector(selected = selectedPalette, options = options, optionLabel = { it.label }, onSelected = onPaletteSelected, modifier = Modifier.fillMaxWidth())
        Text(text = "Piece scale", style = MaterialTheme.typography.labelLarge)
        AppSlider(value = pieceScale, onValueChange = onPieceScaleChanged, valueRange = 0.45f..0.95f, modifier = Modifier.fillMaxWidth(), valueLabel = { value -> "${(value * 100f).toInt()}%" })
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ColorSwatchButton(label = "White pieces", color = whitePieceColor, onClick = onWhitePiecePressed)
            ColorSwatchButton(label = "Black pieces", color = blackPieceColor, onClick = onBlackPiecePressed)
        }
    }
}

