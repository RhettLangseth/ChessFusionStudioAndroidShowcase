package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val presetColors = listOf(
    0xFFF0D9B5.toInt(), 0xFFB58863.toInt(), 0xFFDCE3EA.toInt(), 0xFF6C7A89.toInt(),
    0xFFDDE9D5.toInt(), 0xFF739267.toInt(), 0xFFF8F4E7.toInt(), 0xFF2A3138.toInt(),
    0xFFF4D8DA.toInt(), 0xFF3A2F35.toInt(), 0xFFD8F0E4.toInt(), 0xFF24323A.toInt()
)

@Composable
internal fun ColorPresetGrid(onColorSelected: (Int) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 6
    ) {
        presetColors.forEach { colorArgb ->
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(colorArgb), CircleShape)
                    .clickable { onColorSelected(colorArgb) }
            )
        }
    }
}
