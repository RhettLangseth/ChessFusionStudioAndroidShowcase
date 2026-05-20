package com.chessfusionstudio.showcase.ui.showcase

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.chessfusionstudio.core.model.GameState
import com.chessfusionstudio.core.showcase.ShowcaseBoardFactory
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardPreviewState
import com.chessfusionstudio.showcase.boardimage.ShowcaseBoardStyle
import com.chessfusionstudio.showcase.data.settings.*
import com.chessfusionstudio.showcase.ui.components.ShowcasePieceStyle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private val sampleStates: List<GameState> = ShowcaseBoardFactory.samples()

data class PositionOption(val id: String, val label: String, val gameState: GameState)
data class PalettePairOption(val id: String, val label: String, val firstColorArgb: Int, val secondColorArgb: Int)

data class ThemeStudioUiState(
    val previewState: ShowcaseBoardPreviewState,
    val positionOptions: List<PositionOption>,
    val selectedPosition: PositionOption,
    val boardPaletteOptions: List<PalettePairOption>,
    val selectedBoardPalette: PalettePairOption,
    val piecePaletteOptions: List<PalettePairOption>,
    val selectedPiecePalette: PalettePairOption,
    val pieceScale: Float,
    val lightSquareColor: Color,
    val darkSquareColor: Color,
    val whitePieceColor: Color,
    val blackPieceColor: Color
)

private val positionOptions = listOf(
    PositionOption(POSITION_CLASSIC, "Classic Start", sampleStates[0]),
    PositionOption(POSITION_SICILIAN, "Sicilian Structure", sampleStates[1]),
    PositionOption(POSITION_ENDGAME, "Endgame Study", sampleStates[2])
)
private val boardPaletteOptions = listOf(
    PalettePairOption(BOARD_PALETTE_WALNUT, "Walnut", 0xFFF0D9B5.toInt(), 0xFFB58863.toInt()),
    PalettePairOption(BOARD_PALETTE_SLATE, "Slate", 0xFFDCE3EA.toInt(), 0xFF6C7A89.toInt()),
    PalettePairOption(BOARD_PALETTE_LEAF, "Leaf", 0xFFDDE9D5.toInt(), 0xFF739267.toInt()),
    PalettePairOption(BOARD_PALETTE_CUSTOM, "Custom", 0xFFF0D9B5.toInt(), 0xFFB58863.toInt())
)
private val piecePaletteOptions = listOf(
    PalettePairOption(PIECE_PALETTE_IVORY, "Ivory & Ebony", 0xFFF8F4E7.toInt(), 0xFF2A3138.toInt()),
    PalettePairOption(PIECE_PALETTE_ROSE, "Rose & Charcoal", 0xFFF4D8DA.toInt(), 0xFF3A2F35.toInt()),
    PalettePairOption(PIECE_PALETTE_MINT, "Mint & Graphite", 0xFFD8F0E4.toInt(), 0xFF24323A.toInt()),
    PalettePairOption(PIECE_PALETTE_CUSTOM, "Custom", 0xFFF8F4E7.toInt(), 0xFF2A3138.toInt())
)

class ThemeStudioViewModel(private val settingsStore: ShowcaseSettingsStore) : ViewModel() {
    val uiState: StateFlow<ThemeStudioUiState> = settingsStore.settings.map(::toUiState).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), toUiState(settingsStore.settings.value))

    fun selectPosition(option: PositionOption) = settingsStore.setPositionId(option.id)
    fun applyBoardPalette(option: PalettePairOption) { if (option.id != BOARD_PALETTE_CUSTOM) settingsStore.applyBoardPalette(option.id, option.firstColorArgb, option.secondColorArgb) }
    fun applyPiecePalette(option: PalettePairOption) { if (option.id != PIECE_PALETTE_CUSTOM) settingsStore.applyPiecePalette(option.id, option.firstColorArgb, option.secondColorArgb) }
    fun updateLightSquareColor(argb: Int) = settingsStore.setLightSquareArgb(argb)
    fun updateDarkSquareColor(argb: Int) = settingsStore.setDarkSquareArgb(argb)
    fun updateWhitePieceColor(argb: Int) = settingsStore.setWhitePieceArgb(argb)
    fun updateBlackPieceColor(argb: Int) = settingsStore.setBlackPieceArgb(argb)
    fun updatePieceScale(value: Float) = settingsStore.setPieceScale(value)

    private fun toUiState(snapshot: ShowcaseSettingsSnapshot): ThemeStudioUiState {
        val selectedPosition = positionOptions.firstOrNull { it.id == snapshot.positionId } ?: positionOptions.first()
        val selectedBoardPalette = boardPaletteOptions.firstOrNull { it.id == snapshot.boardPaletteId } ?: boardPaletteOptions.first()
        val selectedPiecePalette = piecePaletteOptions.firstOrNull { it.id == snapshot.piecePaletteId } ?: piecePaletteOptions.first()
        return ThemeStudioUiState(
            previewState = ShowcaseBoardPreviewState(selectedPosition.gameState, ShowcaseBoardStyle(Color(snapshot.lightSquareArgb), Color(snapshot.darkSquareArgb)), ShowcasePieceStyle(Color(snapshot.whitePieceArgb), Color(snapshot.blackPieceArgb), snapshot.pieceScale)),
            positionOptions = positionOptions,
            selectedPosition = selectedPosition,
            boardPaletteOptions = boardPaletteOptions,
            selectedBoardPalette = selectedBoardPalette,
            piecePaletteOptions = piecePaletteOptions,
            selectedPiecePalette = selectedPiecePalette,
            pieceScale = snapshot.pieceScale,
            lightSquareColor = Color(snapshot.lightSquareArgb),
            darkSquareColor = Color(snapshot.darkSquareArgb),
            whitePieceColor = Color(snapshot.whitePieceArgb),
            blackPieceColor = Color(snapshot.blackPieceArgb)
        )
    }

    companion object {
        fun factory(applicationContext: Context) = viewModelFactory {
            initializer { ThemeStudioViewModel(ShowcaseSettingsStore(applicationContext)) }
        }
    }
}
