package com.chessfusionstudio.showcase.ui.common

import kotlin.math.abs

internal fun buildPowerOfTwoMidpointOptions(minValue: Int, maxValue: Int): List<Int> {
    val safeMin = minValue.coerceAtLeast(1)
    val safeMax = maxValue.coerceAtLeast(safeMin)
    if (safeMin == safeMax) {
        return listOf(safeMin)
    }

    val generated = ArrayList<Int>()
    var current = safeMin
    while (current < safeMax) {
        val next = current shl 1
        val midpoint = (current + next) / 2
        generated.add(current)
        generated.add(midpoint)
        if (next <= current) {
            break
        }
        current = next
    }
    generated.add(safeMax)

    return generated
        .asSequence()
        .filter { it in safeMin..safeMax }
        .distinct()
        .sorted()
        .toList()
}

internal fun pickClosestOption(target: Int, options: List<Int>): Int {
    if (options.isEmpty()) {
        return target.coerceAtLeast(1)
    }
    val exact = options.firstOrNull { it == target }
    if (exact != null) {
        return exact
    }
    return options.minByOrNull { abs(it - target) } ?: options.first()
}

