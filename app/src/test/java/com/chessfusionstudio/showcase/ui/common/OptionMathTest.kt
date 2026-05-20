package com.chessfusionstudio.showcase.ui.common

import org.junit.Assert.assertEquals
import org.junit.Test

class OptionMathTest {
    @Test
    fun buildPowerOfTwoMidpointOptions_returnsSingleWhenRangeCollapsed() {
        assertEquals(listOf(8), buildPowerOfTwoMidpointOptions(minValue = 8, maxValue = 8))
    }

    @Test
    fun buildPowerOfTwoMidpointOptions_buildsExpectedSequence() {
        val options = buildPowerOfTwoMidpointOptions(minValue = 64, maxValue = 512)
        assertEquals(listOf(64, 96, 128, 192, 256, 384, 512), options)
    }

    @Test
    fun pickClosestOption_handlesEmptyOptions() {
        assertEquals(1, pickClosestOption(target = -3, options = emptyList()))
    }

    @Test
    fun pickClosestOption_prefersExactOrNearest() {
        val options = listOf(1, 4, 8, 16)
        assertEquals(8, pickClosestOption(target = 8, options = options))
        assertEquals(4, pickClosestOption(target = 5, options = options))
    }
}

