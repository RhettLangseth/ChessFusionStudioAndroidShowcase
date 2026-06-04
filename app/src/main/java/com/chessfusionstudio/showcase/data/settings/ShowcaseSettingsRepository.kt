package com.chessfusionstudio.showcase.data.settings

import kotlinx.coroutines.flow.StateFlow

interface ShowcaseSettingsRepository {
    val settings: StateFlow<ShowcaseSettingsSnapshot>

    fun setPositionId(value: String)
    fun applyBoardPalette(paletteId: String, lightSquareArgb: Int, darkSquareArgb: Int)
    fun applyPiecePalette(paletteId: String, pieceBackgroundArgb: Int, pieceForegroundArgb: Int)
    fun setLightSquareArgb(value: Int)
    fun setDarkSquareArgb(value: Int)
    fun setPieceScale(value: Float)
    fun restoreAppearanceDefaults()
}
