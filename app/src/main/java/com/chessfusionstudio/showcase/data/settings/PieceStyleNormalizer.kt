package com.chessfusionstudio.showcase.data.settings

fun normalizePieceStyle(value: String): String {
    val trimmed = value.trim()
    if (trimmed in BOARD_PIECE_STYLE_OPTIONS) {
        return trimmed
    }
    return PIECE_STYLE_CHESS_CANCUN
}

