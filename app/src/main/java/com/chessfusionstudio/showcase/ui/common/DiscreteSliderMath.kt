package com.chessfusionstudio.showcase.ui.common

import kotlin.math.roundToInt

internal fun toNormalizedDiscreteValue(index: Int, maxIndex: Int): Float {
    if (maxIndex <= 0) {
        return 0f
    }
    return index.coerceIn(0, maxIndex).toFloat() / maxIndex.toFloat()
}

internal fun fromNormalizedDiscreteValue(value: Float, maxIndex: Int): Int {
    if (maxIndex <= 0) {
        return 0
    }
    return (value.coerceIn(0f, 1f) * maxIndex.toFloat()).roundToInt().coerceIn(0, maxIndex)
}

