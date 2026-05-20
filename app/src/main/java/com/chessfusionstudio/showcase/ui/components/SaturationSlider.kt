package com.chessfusionstudio.showcase.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun SaturationSlider(
    hue: Float,
    value: Float,
    saturation: Float,
    onSaturationChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val vividColor = colorFromHsv(hue = hue, saturation = 1f, value = value)
    AppSlider(
        value = saturation,
        onValueChange = onSaturationChanged,
        valueRange = 0f..1f,
        trackFill = AppSliderTrackFill.Gradient(listOf(Color(0xFFB9B9B9), vividColor)),
        valueLabel = { resolved -> "${(resolved * 100f).toInt()}%" },
        style = AppSliderDefaults.ColorPickerVisualStyle,
        modifier = modifier
    )
}
