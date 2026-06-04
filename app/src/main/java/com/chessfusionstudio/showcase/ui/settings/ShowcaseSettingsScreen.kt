package com.chessfusionstudio.showcase.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chessfusionstudio.showcase.ui.components.AppSlider
import com.chessfusionstudio.showcase.ui.components.ColorPickerDialog
import com.chessfusionstudio.showcase.ui.components.DropdownSetting
import com.chessfusionstudio.showcase.ui.components.SettingRow
import com.chessfusionstudio.showcase.ui.components.ShowcaseChessBoard
import com.chessfusionstudio.showcase.ui.components.ShowcasePageScaffold
import com.chessfusionstudio.showcase.ui.components.SettingRows
import com.chessfusionstudio.showcase.ui.navigation.ShowcaseDestination
import com.chessfusionstudio.showcase.ui.showcase.ThemeStudioViewModel

private val SettingsPreviewBoardSize = 220.dp

private enum class EditableSettingColor(val title: String) {
    LightSquare("Light Square Color"),
    DarkSquare("Dark Square Color")
}

@Composable
internal fun ShowcaseSettingsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ThemeStudioViewModel = viewModel(factory = ThemeStudioViewModel.factory(LocalContext.current.applicationContext))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editingColor = rememberSaveable { mutableStateOf<EditableSettingColor?>(null) }
    val showRestoreDefaultsDialog = rememberSaveable { mutableStateOf(false) }

    ShowcasePageScaffold(
        destination = ShowcaseDestination.Settings,
        onBackClick = onBackClick,
        topBarActions = {
            OutlinedButton(
                onClick = { showRestoreDefaultsDialog.value = true },
                modifier = Modifier.height(40.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text("Restore Defaults", maxLines = 1, softWrap = false)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val previewSize = minOf(maxWidth, SettingsPreviewBoardSize)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ShowcaseChessBoard(
                        previewState = uiState.previewState,
                        accessibilityLabel = "${uiState.selectedPosition.label} settings preview board",
                        modifier = Modifier.width(previewSize)
                    )
                }
            }
            SettingRows {
                SettingRow(
                    label = "Board Palette",
                    inputFill = true,
                    description = "Updates both persisted square colors from a preset pair. The renderer assigns them using file-and-rank parity."
                ) {
                    DropdownSetting(
                        selected = uiState.selectedBoardPalette,
                        options = uiState.boardPaletteOptions,
                        optionLabel = { it.label },
                        onSelected = viewModel::applyBoardPalette,
                        accessibilityLabel = "Board palette",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingRow(
                    label = "Light Square",
                    description = "Persists a custom ARGB color for the parity set that includes h1, then feeds it into the renderer."
                ) {
                    ColorSettingButton(
                        label = "Light square color",
                        color = uiState.lightSquareColor,
                        onClick = { editingColor.value = EditableSettingColor.LightSquare }
                    )
                }
                SettingRow(
                    label = "Dark Square",
                    description = "Persists a custom ARGB color for the opposite parity set, then feeds it into the renderer."
                ) {
                    ColorSettingButton(
                        label = "Dark square color",
                        color = uiState.darkSquareColor,
                        onClick = { editingColor.value = EditableSettingColor.DarkSquare }
                    )
                }
                SettingRow(
                    label = "Piece Palette",
                    inputFill = true,
                    description = "Updates the persisted background and foreground ARGB pair used to rasterize and cache each ChessCancun glyph."
                ) {
                    DropdownSetting(
                        selected = uiState.selectedPiecePalette,
                        options = uiState.piecePaletteOptions,
                        optionLabel = { it.label },
                        onSelected = viewModel::applyPiecePalette,
                        accessibilityLabel = "Piece palette",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingRow(
                    label = "Piece Scale",
                    inputFill = true,
                    description = "Changes the inset calculated inside each square before its piece bitmap is rendered."
                ) {
                    AppSlider(
                        value = uiState.pieceScale,
                        onValueChange = viewModel::updatePieceScale,
                        valueRange = 0.45f..0.95f,
                        valueLabel = { value -> "${(value * 100f).toInt()}%" },
                        accessibilityLabel = "Piece scale",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    val activeColor = editingColor.value
    if (activeColor != null) {
        val initialColor = when (activeColor) {
            EditableSettingColor.LightSquare -> uiState.lightSquareColor.toArgb()
            EditableSettingColor.DarkSquare -> uiState.darkSquareColor.toArgb()
        }
        ColorPickerDialog(
            title = activeColor.title,
            initialColorArgb = initialColor,
            onDismiss = { editingColor.value = null },
            onConfirm = { colorArgb ->
                when (activeColor) {
                    EditableSettingColor.LightSquare -> viewModel.updateLightSquareColor(colorArgb)
                    EditableSettingColor.DarkSquare -> viewModel.updateDarkSquareColor(colorArgb)
                }
                editingColor.value = null
            }
        )
    }

    if (showRestoreDefaultsDialog.value) {
        AlertDialog(
            onDismissRequest = { showRestoreDefaultsDialog.value = false },
            title = { Text("Restore default settings?") },
            text = { Text("This restores the board and piece appearance settings shown on this page.") },
            confirmButton = {
                Button(
                    onClick = {
                        showRestoreDefaultsDialog.value = false
                        viewModel.restoreAppearanceDefaults()
                    }
                ) {
                    Text("Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreDefaultsDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ColorSettingButton(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    val formattedColor = formatRgbHex(color)
    Row(
        modifier = Modifier
            .heightIn(min = 48.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics {
                contentDescription = "$label, $formattedColor"
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, CircleShape)
        )
        Text(
            text = formattedColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatRgbHex(color: Color): String {
    return "#%06X".format(color.toArgb() and 0x00FFFFFF)
}
