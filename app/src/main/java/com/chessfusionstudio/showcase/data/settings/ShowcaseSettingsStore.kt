package com.chessfusionstudio.showcase.data.settings

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val PREFS_NAME = "showcase_settings"
private const val KEY_POSITION_ID = "position_id"
private const val KEY_BOARD_PALETTE_ID = "board_palette_id"
private const val KEY_PIECE_PALETTE_ID = "piece_palette_id"
private const val KEY_LIGHT_SQUARE = "light_square"
private const val KEY_DARK_SQUARE = "dark_square"
private const val KEY_WHITE_PIECE = "white_piece"
private const val KEY_BLACK_PIECE = "black_piece"
private const val KEY_PIECE_SCALE = "piece_scale"

const val POSITION_CLASSIC = "classic"
const val POSITION_SICILIAN = "sicilian"
const val POSITION_ENDGAME = "endgame"
const val BOARD_PALETTE_WALNUT = "walnut"
const val BOARD_PALETTE_SLATE = "slate"
const val BOARD_PALETTE_LEAF = "leaf"
const val BOARD_PALETTE_CUSTOM = "custom"
const val PIECE_PALETTE_IVORY = "ivory"
const val PIECE_PALETTE_ROSE = "rose"
const val PIECE_PALETTE_MINT = "mint"
const val PIECE_PALETTE_CUSTOM = "custom"

private const val DEFAULT_LIGHT_SQUARE = 0xFFF0D9B5.toInt()
private const val DEFAULT_DARK_SQUARE = 0xFFB58863.toInt()
private const val DEFAULT_WHITE_PIECE = 0xFFF8F4E7.toInt()
private const val DEFAULT_BLACK_PIECE = 0xFF2A3138.toInt()
private const val DEFAULT_PIECE_SCALE = 0.72f

data class ShowcaseSettingsSnapshot(
    val positionId: String = POSITION_CLASSIC,
    val boardPaletteId: String = BOARD_PALETTE_WALNUT,
    val piecePaletteId: String = PIECE_PALETTE_IVORY,
    val lightSquareArgb: Int = DEFAULT_LIGHT_SQUARE,
    val darkSquareArgb: Int = DEFAULT_DARK_SQUARE,
    val whitePieceArgb: Int = DEFAULT_WHITE_PIECE,
    val blackPieceArgb: Int = DEFAULT_BLACK_PIECE,
    val pieceScale: Float = DEFAULT_PIECE_SCALE
)

class ShowcaseSettingsStore(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _settings = MutableStateFlow(load())
    val settings: StateFlow<ShowcaseSettingsSnapshot> = _settings.asStateFlow()

    fun setPositionId(value: String) = update { it.copy(positionId = value) }
    fun applyBoardPalette(paletteId: String, lightSquareArgb: Int, darkSquareArgb: Int) = update { it.copy(boardPaletteId = paletteId, lightSquareArgb = lightSquareArgb, darkSquareArgb = darkSquareArgb) }
    fun applyPiecePalette(paletteId: String, whitePieceArgb: Int, blackPieceArgb: Int) = update { it.copy(piecePaletteId = paletteId, whitePieceArgb = whitePieceArgb, blackPieceArgb = blackPieceArgb) }
    fun setLightSquareArgb(value: Int) = update { it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, lightSquareArgb = normalizeOpaqueColorArgb(value)) }
    fun setDarkSquareArgb(value: Int) = update { it.copy(boardPaletteId = BOARD_PALETTE_CUSTOM, darkSquareArgb = normalizeOpaqueColorArgb(value)) }
    fun setWhitePieceArgb(value: Int) = update { it.copy(piecePaletteId = PIECE_PALETTE_CUSTOM, whitePieceArgb = normalizeOpaqueColorArgb(value)) }
    fun setBlackPieceArgb(value: Int) = update { it.copy(piecePaletteId = PIECE_PALETTE_CUSTOM, blackPieceArgb = normalizeOpaqueColorArgb(value)) }
    fun setPieceScale(value: Float) = update { it.copy(pieceScale = value.coerceIn(0.45f, 0.95f)) }

    private fun update(transform: (ShowcaseSettingsSnapshot) -> ShowcaseSettingsSnapshot) {
        val updated = transform(_settings.value)
        _settings.value = updated
        prefs.edit()
            .putString(KEY_POSITION_ID, updated.positionId)
            .putString(KEY_BOARD_PALETTE_ID, updated.boardPaletteId)
            .putString(KEY_PIECE_PALETTE_ID, updated.piecePaletteId)
            .putInt(KEY_LIGHT_SQUARE, updated.lightSquareArgb)
            .putInt(KEY_DARK_SQUARE, updated.darkSquareArgb)
            .putInt(KEY_WHITE_PIECE, updated.whitePieceArgb)
            .putInt(KEY_BLACK_PIECE, updated.blackPieceArgb)
            .putFloat(KEY_PIECE_SCALE, updated.pieceScale)
            .apply()
    }

    private fun load(): ShowcaseSettingsSnapshot {
        return ShowcaseSettingsSnapshot(
            positionId = prefs.getString(KEY_POSITION_ID, POSITION_CLASSIC) ?: POSITION_CLASSIC,
            boardPaletteId = prefs.getString(KEY_BOARD_PALETTE_ID, BOARD_PALETTE_WALNUT) ?: BOARD_PALETTE_WALNUT,
            piecePaletteId = prefs.getString(KEY_PIECE_PALETTE_ID, PIECE_PALETTE_IVORY) ?: PIECE_PALETTE_IVORY,
            lightSquareArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_LIGHT_SQUARE, DEFAULT_LIGHT_SQUARE)),
            darkSquareArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_DARK_SQUARE, DEFAULT_DARK_SQUARE)),
            whitePieceArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_WHITE_PIECE, DEFAULT_WHITE_PIECE)),
            blackPieceArgb = normalizeOpaqueColorArgb(prefs.getInt(KEY_BLACK_PIECE, DEFAULT_BLACK_PIECE)),
            pieceScale = prefs.getFloat(KEY_PIECE_SCALE, DEFAULT_PIECE_SCALE).coerceIn(0.45f, 0.95f)
        )
    }
}
