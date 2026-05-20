package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
internal fun ColorSquare(
    hue: Float,
    value: Float,
    onHueValueChanged: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    fun update(offset: Offset) {
                        val resolvedHue = ((offset.x / size.width) * 360f).coerceIn(0f, 360f)
                        val resolvedValue = (1f - (offset.y / size.height)).coerceIn(0f, 1f)
                        onHueValueChanged(resolvedHue, resolvedValue)
                    }
                    update(down.position)
                    var pointer = down
                    while (pointer.pressed) {
                        val event = awaitPointerEvent()
                        pointer = event.changes.first()
                        if (pointer.positionChanged()) {
                            update(pointer.position)
                            pointer.consume()
                        }
                    }
                }
            }
    ) {
        drawRect(Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)))
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        val marker = Offset((hue / 360f) * size.width, (1f - value) * size.height)
        drawCircle(Color.White, radius = 10.dp.toPx(), center = marker)
        drawCircle(Color.Black.copy(alpha = 0.7f), radius = 10.dp.toPx(), center = marker, style = Stroke(width = 2.dp.toPx()))
    }
}
