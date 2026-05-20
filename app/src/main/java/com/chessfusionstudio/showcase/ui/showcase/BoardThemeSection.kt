package com.chessfusionstudio.showcase.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.ui.components.OutlinedDropdownSelector

@Composable
internal fun BoardThemeSection(
    selectedPalette: PalettePairOption,
    options: List<PalettePairOption>,
    lightSquareColor: Color,
    darkSquareColor: Color,
    onPaletteSelected: (PalettePairOption) -> Unit,
    onLightSquarePressed: () -> Unit,
    onDarkSquarePressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    ThemeStudioSection(title = "Board Theme", modifier = modifier) {
        Text(text = "Palette", style = MaterialTheme.typography.labelLarge)
        OutlinedDropdownSelector(selected = selectedPalette, options = options, optionLabel = { it.label }, onSelected = onPaletteSelected, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ColorSwatchButton(label = "Light squares", color = lightSquareColor, onClick = onLightSquarePressed)
            ColorSwatchButton(label = "Dark squares", color = darkSquareColor, onClick = onDarkSquarePressed)
        }
    }
}

@Composable
internal fun ColorSwatchButton(label: String, color: Color, onClick: () -> Unit) {
    Row(modifier = Modifier.clickable(onClick = onClick), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.size(28.dp).background(color, CircleShape))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}
