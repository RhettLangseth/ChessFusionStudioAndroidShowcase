package com.chessfusionstudio.showcase.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.foundation.text.KeyboardOptions

@Composable
internal fun HexColorField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(sanitizeHexInput(it)) },
        modifier = modifier,
        singleLine = true,
        label = { Text("Hex") },
        prefix = { Text("#") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
    )
}

