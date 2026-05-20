package com.chessfusionstudio.showcase.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chessfusionstudio.showcase.ui.components.ColorPickerDialog

enum class EditableColorSlot(val title: String) {
    LightSquare("Light Square Color"),
    DarkSquare("Dark Square Color"),
    WhitePiece("White Piece Color"),
    BlackPiece("Black Piece Color")
}

@Composable
fun ThemeStudioScreen(viewModel: ThemeStudioViewModel = viewModel(factory = ThemeStudioViewModel.factory(LocalContext.current.applicationContext))) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var editingSlot by remember { mutableStateOf<EditableColorSlot?>(null) }

    Scaffold { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "ChessFusionStudio Android Showcase", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text(text = "Curated public slice focused on Android adaptation, custom rendering, reusable controls, and persisted UI state.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            PreviewSection(previewState = uiState.previewState, selectedPosition = uiState.selectedPosition, positionOptions = uiState.positionOptions, onPositionSelected = viewModel::selectPosition)
            BoardThemeSection(selectedPalette = uiState.selectedBoardPalette, options = uiState.boardPaletteOptions, lightSquareColor = uiState.lightSquareColor, darkSquareColor = uiState.darkSquareColor, onPaletteSelected = viewModel::applyBoardPalette, onLightSquarePressed = { editingSlot = EditableColorSlot.LightSquare }, onDarkSquarePressed = { editingSlot = EditableColorSlot.DarkSquare })
            PieceThemeSection(selectedPalette = uiState.selectedPiecePalette, options = uiState.piecePaletteOptions, pieceScale = uiState.pieceScale, whitePieceColor = uiState.whitePieceColor, blackPieceColor = uiState.blackPieceColor, onPaletteSelected = viewModel::applyPiecePalette, onPieceScaleChanged = viewModel::updatePieceScale, onWhitePiecePressed = { editingSlot = EditableColorSlot.WhitePiece }, onBlackPiecePressed = { editingSlot = EditableColorSlot.BlackPiece })
        }
    }

    val activeSlot = editingSlot
    if (activeSlot != null) {
        val initialColor = when (activeSlot) {
            EditableColorSlot.LightSquare -> uiState.lightSquareColor.toArgb()
            EditableColorSlot.DarkSquare -> uiState.darkSquareColor.toArgb()
            EditableColorSlot.WhitePiece -> uiState.whitePieceColor.toArgb()
            EditableColorSlot.BlackPiece -> uiState.blackPieceColor.toArgb()
        }
        ColorPickerDialog(
            title = activeSlot.title,
            initialColorArgb = initialColor,
            onDismiss = { editingSlot = null },
            onConfirm = { colorArgb ->
                when (activeSlot) {
                    EditableColorSlot.LightSquare -> viewModel.updateLightSquareColor(colorArgb)
                    EditableColorSlot.DarkSquare -> viewModel.updateDarkSquareColor(colorArgb)
                    EditableColorSlot.WhitePiece -> viewModel.updateWhitePieceColor(colorArgb)
                    EditableColorSlot.BlackPiece -> viewModel.updateBlackPieceColor(colorArgb)
                }
                editingSlot = null
            }
        )
    }
}
