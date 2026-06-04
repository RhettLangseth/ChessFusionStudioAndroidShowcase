package com.chessfusionstudio.showcase.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chessfusionstudio.showcase.ui.components.AppSlider
import com.chessfusionstudio.showcase.ui.components.ColorPickerDialog
import com.chessfusionstudio.showcase.ui.components.DropdownSetting
import com.chessfusionstudio.showcase.ui.components.SettingRow
import com.chessfusionstudio.showcase.ui.components.ShowcaseChessBoard
import com.chessfusionstudio.showcase.ui.components.ShowcasePageScaffold
import com.chessfusionstudio.showcase.ui.components.ZebraSettingRows
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
    val editingColor = remember { mutableStateOf<EditableSettingColor?>(null) }

    ShowcasePageScaffold(
        destination = ShowcaseDestination.Settings,
        onBackClick = onBackClick,
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
                        modifier = Modifier.width(previewSize)
                    )
                }
            }
            ZebraSettingRows {
                SettingRow(
                    label = "Sample Position",
                    inputFill = true,
                    description = "Changes the game position displayed on the preview board by selecting a predefined FEN string parsed into a Java GameState."
                ) {
                    DropdownSetting(
                        selected = uiState.selectedPosition,
                        options = uiState.positionOptions,
                        optionLabel = { it.label },
                        onSelected = viewModel::selectPosition,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingRow(
                    label = "Board Palette",
                    inputFill = true,
                    description = "Updates both persisted square colors from a preset pair; the renderer assigns them using file-and-rank parity."
                ) {
                    DropdownSetting(
                        selected = uiState.selectedBoardPalette,
                        options = uiState.boardPaletteOptions,
                        optionLabel = { it.label },
                        onSelected = viewModel::applyBoardPalette,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingRow(
                    label = "Light Square",
                    description = "Persists a custom ARGB color for the parity set that includes h1, then feeds it into the renderer."
                ) {
                    ColorSettingButton(
                        color = uiState.lightSquareColor,
                        onClick = { editingColor.value = EditableSettingColor.LightSquare }
                    )
                }
                SettingRow(
                    label = "Dark Square",
                    description = "Persists a custom ARGB color for the opposite parity set, then feeds it into the renderer."
                ) {
                    ColorSettingButton(
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
}

@Composable
private fun ColorSettingButton(
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, CircleShape)
        )
        Text(
            text = formatRgbHex(color),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatRgbHex(color: Color): String {
    return "#%06X".format(color.toArgb() and 0x00FFFFFF)
}
