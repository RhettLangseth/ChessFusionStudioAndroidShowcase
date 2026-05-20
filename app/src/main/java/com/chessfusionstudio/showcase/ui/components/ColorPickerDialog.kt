package com.chessfusionstudio.showcase.ui.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chessfusionstudio.showcase.data.settings.normalizeOpaqueColorArgb

@Composable
fun ColorPickerDialog(
    title: String,
    initialColorArgb: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val initialHsv = remember(initialColorArgb) { hsvFromArgb(initialColorArgb) }
    var hue by remember(initialColorArgb) { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember(initialColorArgb) { mutableFloatStateOf(initialHsv[1]) }
    var value by remember(initialColorArgb) { mutableFloatStateOf(initialHsv[2]) }
    var selectedArgb by remember(initialColorArgb) { mutableIntStateOf(initialColorArgb) }
    var hexValue by remember(initialColorArgb) { mutableStateOf(formatRgbHex(initialColorArgb)) }

    fun updateFromHsv(newHue: Float = hue, newSaturation: Float = saturation, newValue: Float = value) {
        hue = normalizeHue(newHue)
        saturation = newSaturation.coerceIn(0f, 1f)
        value = newValue.coerceIn(0f, 1f)
        selectedArgb = colorFromHsvArgb(hue, saturation, value)
        hexValue = formatRgbHex(selectedArgb)
    }

    fun updateFromArgb(argb: Int) {
        val hsv = hsvFromArgb(argb)
        hue = hsv[0]
        saturation = hsv[1]
        value = hsv[2]
        selectedArgb = normalizeOpaqueColorArgb(argb)
        hexValue = formatRgbHex(selectedArgb)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(modifier = Modifier.size(42.dp), shape = CircleShape, color = Color(selectedArgb)) {}
                ColorSquare(hue = hue, value = value, onHueValueChanged = { newHue, newValue -> updateFromHsv(newHue = newHue, newValue = newValue) })
                SaturationSlider(hue = hue, value = value, saturation = saturation, onSaturationChanged = { updateFromHsv(newSaturation = it) }, modifier = Modifier.fillMaxWidth())
                HexColorField(value = hexValue, onValueChange = { hexValue = it; parseRgbHexOrNull(it)?.let(::updateFromArgb) }, modifier = Modifier.fillMaxWidth())
                ColorPresetGrid(onColorSelected = ::updateFromArgb)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        confirmButton = { Button(onClick = { onConfirm(selectedArgb) }) { Text("Apply") } }
    )
}

internal fun colorFromHsv(hue: Float, saturation: Float, value: Float): Color = Color(colorFromHsvArgb(hue, saturation, value))
private fun colorFromHsvArgb(hue: Float, saturation: Float, value: Float): Int = AndroidColor.HSVToColor(floatArrayOf(normalizeHue(hue), saturation.coerceIn(0f, 1f), value.coerceIn(0f, 1f)))
private fun hsvFromArgb(argb: Int): FloatArray = FloatArray(3).also { AndroidColor.colorToHSV(argb, it) }
internal fun normalizeHue(value: Float): Float { val normalized = value % 360f; return if (normalized < 0f) normalized + 360f else normalized }
internal fun formatRgbHex(colorArgb: Int): String = "%06X".format(colorArgb and 0x00FFFFFF)
internal fun parseRgbHexOrNull(value: String): Int? = if (value.length == 6) value.toIntOrNull(16)?.let { normalizeOpaqueColorArgb(it or (0xFF shl 24)) } else null
internal fun sanitizeHexInput(value: String): String = value.uppercase().filter { it in '0'..'9' || it in 'A'..'F' }.take(6)

