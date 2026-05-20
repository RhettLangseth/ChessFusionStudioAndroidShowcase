package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
internal fun HoldRepeatPressBox(
    enabled: Boolean,
    onStep: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    var repeatJob by remember { mutableStateOf<Job?>(null) }

    fun stopRepeating() {
        repeatJob?.cancel()
        repeatJob = null
    }

    DisposableEffect(Unit) {
        onDispose {
            stopRepeating()
        }
    }

    Box(
        modifier = modifier.pointerInput(enabled) {
            if (!enabled) {
                return@pointerInput
            }
            detectTapGestures(
                onPress = {
                    onStep()
                    stopRepeating()
                    repeatJob = scope.launch {
                        delay(300)
                        while (isActive) {
                            onStep()
                            delay(70)
                        }
                    }
                    try {
                        tryAwaitRelease()
                    } finally {
                        stopRepeating()
                    }
                }
            )
        },
        contentAlignment = contentAlignment,
        content = content
    )
}

@Composable
internal fun UnderlinedTabHeader(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 10.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val unselectedUnderlineColor = MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .drawBehind {
                if (!selected) {
                    val stroke = 1.dp.toPx()
                    val y = size.height - (stroke / 2f)
                    drawLine(
                        color = unselectedUnderlineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = stroke
                    )
                }
            }
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
internal fun <T> OutlinedDropdownSelector(
    selected: T?,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
    truncateMenuText: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = selected?.let(optionLabel)
        ?: options.firstOrNull()?.let(optionLabel).orEmpty()

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = contentPadding
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        if (truncateMenuText) {
                            Text(
                                text = optionLabel(option),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Text(text = optionLabel(option))
                        }
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

