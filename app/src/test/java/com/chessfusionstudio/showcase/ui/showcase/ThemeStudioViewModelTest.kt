package com.chessfusionstudio.showcase.ui.showcase

import com.chessfusionstudio.showcase.MainDispatcherRule
import com.chessfusionstudio.showcase.data.settings.BOARD_PALETTE_CUSTOM
import com.chessfusionstudio.showcase.data.settings.BOARD_PALETTE_WALNUT
import com.chessfusionstudio.showcase.data.settings.POSITION_PHILIDOR
import com.chessfusionstudio.showcase.data.settings.POSITION_SCANDINAVIAN
import com.chessfusionstudio.showcase.data.settings.POSITION_START
import com.chessfusionstudio.showcase.data.settings.ShowcaseSettingsRepository
import com.chessfusionstudio.showcase.data.settings.ShowcaseSettingsSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeStudioViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun adjacentPositionSelection_wrapsAndPublishesMappedState() = runTest {
        val repository = FakeShowcaseSettingsRepository()
        val viewModel = ThemeStudioViewModel(repository)
        val collection = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.selectPreviousPosition()
        assertEquals(POSITION_SCANDINAVIAN, viewModel.uiState.value.selectedPosition.id)

        viewModel.selectNextPosition()
        assertEquals(POSITION_START, viewModel.uiState.value.selectedPosition.id)

        viewModel.selectNextPosition()
        assertEquals(POSITION_PHILIDOR, viewModel.uiState.value.selectedPosition.id)

        collection.cancel()
    }

    @Test
    fun customBoardPalette_onlyAppearsWhenColorsDoNotMatchPreset() = runTest {
        val repository = FakeShowcaseSettingsRepository()
        val viewModel = ThemeStudioViewModel(repository)
        val collection = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        assertFalse(viewModel.uiState.value.boardPaletteOptions.any { it.id == BOARD_PALETTE_CUSTOM })

        viewModel.updateLightSquareColor(0xFF123456.toInt())
        assertEquals(BOARD_PALETTE_CUSTOM, viewModel.uiState.value.selectedBoardPalette.id)
        assertTrue(viewModel.uiState.value.boardPaletteOptions.any { it.id == BOARD_PALETTE_CUSTOM })

        viewModel.applyBoardPalette(
            viewModel.uiState.value.boardPaletteOptions.first { it.id == BOARD_PALETTE_WALNUT }
        )
        assertEquals(BOARD_PALETTE_WALNUT, viewModel.uiState.value.selectedBoardPalette.id)
        assertFalse(viewModel.uiState.value.boardPaletteOptions.any { it.id == BOARD_PALETTE_CUSTOM })

        collection.cancel()
    }

    @Test
    fun restoreAppearanceDefaults_resetsAppearanceAndPreservesPosition() = runTest {
        val repository = FakeShowcaseSettingsRepository()
        val viewModel = ThemeStudioViewModel(repository)
        val collection = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.selectNextPosition()
        viewModel.updateLightSquareColor(0xFF123456.toInt())
        viewModel.updatePieceScale(0.60f)

        viewModel.restoreAppearanceDefaults()

        assertEquals(POSITION_PHILIDOR, viewModel.uiState.value.selectedPosition.id)
        assertEquals(BOARD_PALETTE_WALNUT, viewModel.uiState.value.selectedBoardPalette.id)
        assertEquals(0.90f, viewModel.uiState.value.pieceScale, 0.001f)

        collection.cancel()
    }
}

private class FakeShowcaseSettingsRepository : ShowcaseSettingsRepository {
    private val mutableSettings = MutableStateFlow(ShowcaseSettingsSnapshot())
    override val settings: StateFlow<ShowcaseSettingsSnapshot> = mutableSettings

    override fun setPositionId(value: String) = update { it.copy(positionId = value) }

    override fun applyBoardPalette(
        paletteId: String,
        lightSquareArgb: Int,
        darkSquareArgb: Int
    ) = update {
        it.copy(
            boardPaletteId = paletteId,
            lightSquareArgb = lightSquareArgb,
            darkSquareArgb = darkSquareArgb
        )
    }

    override fun applyPiecePalette(
        paletteId: String,
        pieceBackgroundArgb: Int,
        pieceForegroundArgb: Int
    ) = update {
        it.copy(
            piecePaletteId = paletteId,
            pieceBackgroundArgb = pieceBackgroundArgb,
            pieceForegroundArgb = pieceForegroundArgb
        )
    }

    override fun setLightSquareArgb(value: Int) = update {
        it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, lightSquareArgb = value)
    }

    override fun setDarkSquareArgb(value: Int) = update {
        it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, darkSquareArgb = value)
    }

    override fun setPieceScale(value: Float) = update { it.copy(pieceScale = value) }

    override fun restoreAppearanceDefaults() = update { current ->
        ShowcaseSettingsSnapshot(positionId = current.positionId)
    }

    private fun update(transform: (ShowcaseSettingsSnapshot) -> ShowcaseSettingsSnapshot) {
        mutableSettings.value = transform(mutableSettings.value)
    }
}
