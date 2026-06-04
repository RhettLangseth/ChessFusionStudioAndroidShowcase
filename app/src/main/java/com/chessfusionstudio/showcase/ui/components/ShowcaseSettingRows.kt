package com.chessfusionstudio.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val SettingRowMinHeight = 48.dp
private val SettingRowInnerHorizontalPadding = AppSpacing.Page
private val SettingRowInputStartPadding = 12.dp
private const val SettingRowShadeAlpha = 0.42f

private class SettingRowCounter(var index: Int = 0)

private val LocalSettingRowCounter = compositionLocalOf<SettingRowCounter?> { null }

@Composable
internal fun ZebraSettingRows(content: @Composable () -> Unit) {
    val counter = remember { SettingRowCounter() }
    counter.index = 0
    CompositionLocalProvider(LocalSettingRowCounter provides counter) {
        content()
    }
}

@Composable
internal fun SettingRow(
    label: String,
    inputFill: Boolean = false,
    rowHorizontalPadding: Dp = SettingRowInnerHorizontalPadding,
    description: String? = null,
    input: @Composable () -> Unit
) {
    val rowCounter = LocalSettingRowCounter.current
    val rowIndex = if (rowCounter != null) {
        val current = rowCounter.index
        rowCounter.index = current + 1
        current
    } else {
        0
    }
    val rowContainerColor = if (rowIndex % 2 == 0) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = SettingRowShadeAlpha)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = SettingRowShadeAlpha)
    }
    val inputWidthModifier = if (inputFill) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.wrapContentWidth(Alignment.End)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AppSpacing.Section)
            .clip(RoundedCornerShape(8.dp))
            .background(rowContainerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = SettingRowMinHeight)
                .padding(horizontal = rowHorizontalPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    modifier = Modifier.weight(0.44f),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Box(
                    modifier = Modifier
                        .weight(0.56f)
                        .padding(start = SettingRowInputStartPadding)
                        .then(inputWidthModifier),
                    contentAlignment = if (inputFill) Alignment.CenterStart else Alignment.CenterEnd
                ) {
                    input()
                }
            }
            if (description != null) {
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
internal fun <T> DropdownSetting(
    selected: T,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedDropdownSelector(
        selected = selected,
        options = options,
        optionLabel = optionLabel,
        onSelected = onSelected,
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    )
}
