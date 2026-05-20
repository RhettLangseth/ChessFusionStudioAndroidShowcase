package com.chessfusionstudio.showcase.data.settings

enum class BoardTextureSourceMode {
    Asset,
    CustomColor
}

internal fun normalizeBoardTextureSourceMode(value: String): BoardTextureSourceMode {
    return when (value.trim()) {
        BoardTextureSourceMode.CustomColor.name -> BoardTextureSourceMode.CustomColor
        else -> BoardTextureSourceMode.Asset
    }
}

internal fun normalizeOpaqueColorArgb(value: Int): Int {
    return (value and 0x00FFFFFF) or (0xFF shl 24)
}

