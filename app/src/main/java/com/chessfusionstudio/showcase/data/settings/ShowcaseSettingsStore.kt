package com.chessfusionstudio.showcase.data.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_NAME = "showcase_settings"
private const val KEY_POSITION_ID = "position_id"
private const val KEY_BOARD_PALETTE_ID = "board_palette_id"
private const val KEY_PIECE_PALETTE_ID = "piece_palette_id"
private const val KEY_LIGHT_SQUARE = "light_square"
private const val KEY_DARK_SQUARE = "dark_square"
private const val KEY_PIECE_BACKGROUND = "piece_background"
private const val KEY_PIECE_FOREGROUND = "piece_foreground"
private const val KEY_PIECE_SCALE = "piece_scale"

const val POSITION_START = "start"
const val POSITION_PHILIDOR = "philidor"
const val POSITION_RUY_LOPEZ = "ruy_lopez"
const val POSITION_QUEENS_GAMBIT = "queens_gambit"
const val POSITION_SCANDINAVIAN = "scandinavian"
const val BOARD_PALETTE_WALNUT = "walnut"
const val BOARD_PALETTE_SLATE = "slate"
const val BOARD_PALETTE_LEAF = "leaf"
const val BOARD_PALETTE_CUSTOM = "custom"
const val PIECE_PALETTE_IVORY = "ivory"
const val PIECE_PALETTE_ROSE = "rose"
const val PIECE_PALETTE_MINT = "mint"

private const val DEFAULT_LIGHT_SQUARE = 0xFFF0D9B5.toInt()
private const val DEFAULT_DARK_SQUARE = 0xFFB58863.toInt()
private const val DEFAULT_PIECE_BACKGROUND = 0xFFF8F4E7.toInt()
private const val DEFAULT_PIECE_FOREGROUND = 0xFF2A3138.toInt()
private const val DEFAULT_PIECE_SCALE = 0.90f

data class ShowcaseSettingsSnapshot(
    val positionId: String = POSITION_START,
    val boardPaletteId: String = BOARD_PALETTE_WALNUT,
    val piecePaletteId: String = PIECE_PALETTE_IVORY,
    val lightSquareArgb: Int = DEFAULT_LIGHT_SQUARE,
    val darkSquareArgb: Int = DEFAULT_DARK_SQUARE,
    val pieceBackgroundArgb: Int = DEFAULT_PIECE_BACKGROUND,
    val pieceForegroundArgb: Int = DEFAULT_PIECE_FOREGROUND,
    val pieceScale: Float = DEFAULT_PIECE_SCALE
)

class ShowcaseSettingsStore(context: Context) : ShowcaseSettingsRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _settings = MutableStateFlow(load())
    override val settings: StateFlow<ShowcaseSettingsSnapshot> = _settings.asStateFlow()

    override fun setPositionId(value: String) = update { it.copy(positionId = value) }
    override fun applyBoardPalette(paletteId: String, lightSquareArgb: Int, darkSquareArgb: Int) = update { it.copy(boardPaletteId = paletteId, lightSquareArgb = lightSquareArgb, darkSquareArgb = darkSquareArgb) }
    override fun applyPiecePalette(paletteId: String, pieceBackgroundArgb: Int, pieceForegroundArgb: Int) = update { it.copy(piecePaletteId = paletteId, pieceBackgroundArgb = pieceBackgroundArgb, pieceForegroundArgb = pieceForegroundArgb) }
    override fun setLightSquareArgb(value: Int) = update { it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, lightSquareArgb = normalizeOpaqueColorArgb(value)) }
    override fun setDarkSquareArgb(value: Int) = update { it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, darkSquareArgb = normalizeOpaqueColorArgb(value)) }
    override fun setPieceScale(value: Float) = update { it.copy(pieceScale = value.coerceIn(0.45f, 0.95f)) }
    override fun restoreAppearanceDefaults() = update { current ->
        ShowcaseSettingsSnapshot(positionId = current.positionId)
    }

    private fun update(transform: (ShowcaseSettingsSnapshot) -> ShowcaseSettingsSnapshot) {
        val updated = transform(_settings.value)
        _settings.value = updated
        prefs.edit {
            putString(KEY_POSITION_ID, updated.positionId)
            putString(KEY_BOARD_PALETTE_ID, updated.boardPaletteId)
            putString(KEY_PIECE_PALETTE_ID, updated.piecePaletteId)
            putInt(KEY_LIGHT_SQUARE, updated.lightSquareArgb)
            putInt(KEY_DARK_SQUARE, updated.darkSquareArgb)
            putInt(KEY_PIECE_BACKGROUND, updated.pieceBackgroundArgb)
            putInt(KEY_PIECE_FOREGROUND, updated.pieceForegroundArgb)
            putFloat(KEY_PIECE_SCALE, updated.pieceScale)
        }
    }

    private fun load(): ShowcaseSettingsSnapshot {
        val storedPiecePaletteId = prefs.getString(KEY_PIECE_PALETTE_ID, PIECE_PALETTE_IVORY)
        val normalizedPiecePaletteId = when (storedPiecePaletteId) {
            PIECE_PALETTE_IVORY,
            PIECE_PALETTE_ROSE,
            PIECE_PALETTE_MINT -> storedPiecePaletteId
            else -> PIECE_PALETTE_IVORY
        }
        val resetLegacyCustomPiecePalette = normalizedPiecePaletteId != storedPiecePaletteId
        return ShowcaseSettingsSnapshot(
            positionId = prefs.getString(KEY_POSITION_ID, POSITION_START) ?: POSITION_START,
            boardPaletteId = prefs.getString(KEY_BOARD_PALETTE_ID, BOARD_PALETTE_WALNUT) ?: BOARD_PALETTE_WALNUT,
            piecePaletteId = normalizedPiecePaletteId,
            lightSquareArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_LIGHT_SQUARE, DEFAULT_LIGHT_SQUARE)),
            darkSquareArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_DARK_SQUARE, DEFAULT_DARK_SQUARE)),
            pieceBackgroundArgb = if (resetLegacyCustomPiecePalette) DEFAULT_PIECE_BACKGROUND else normalizeOpaqueColorArgb(prefs.getInt(KEY_PIECE_BACKGROUND, DEFAULT_PIECE_BACKGROUND)),
            pieceForegroundArgb = if (resetLegacyCustomPiecePalette) DEFAULT_PIECE_FOREGROUND else normalizeOpaqueColorArgb(prefs.getInt(KEY_PIECE_FOREGROUND, DEFAULT_PIECE_FOREGROUND)),
            pieceScale = prefs.getFloat(KEY_PIECE_SCALE, DEFAULT_PIECE_SCALE).coerceIn(0.45f, 0.95f)
        )
    }
}
