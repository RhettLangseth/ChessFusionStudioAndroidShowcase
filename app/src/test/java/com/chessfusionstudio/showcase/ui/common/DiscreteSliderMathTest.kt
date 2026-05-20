package com.chessfusionstudio.showcase.ui.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DiscreteSliderMathTest {
    @Test
    fun toNormalizedDiscreteValue_handlesInvalidMax() {
        assertEquals(0f, toNormalizedDiscreteValue(index = 3, maxIndex = 0))
        assertEquals(0f, toNormalizedDiscreteValue(index = 3, maxIndex = -1))
    }

    @Test
    fun toNormalizedDiscreteValue_clampsIndex() {
        assertEquals(0f, toNormalizedDiscreteValue(index = -5, maxIndex = 10))
        assertEquals(1f, toNormalizedDiscreteValue(index = 15, maxIndex = 10))
        assertEquals(0.5f, toNormalizedDiscreteValue(index = 5, maxIndex = 10))
    }

    @Test
    fun fromNormalizedDiscreteValue_handlesInvalidMax() {
        assertEquals(0, fromNormalizedDiscreteValue(value = 0.5f, maxIndex = 0))
        assertEquals(0, fromNormalizedDiscreteValue(value = 0.5f, maxIndex = -1))
    }

    @Test
    fun fromNormalizedDiscreteValue_clampsAndRounds() {
        assertEquals(0, fromNormalizedDiscreteValue(value = -0.5f, maxIndex = 10))
        assertEquals(10, fromNormalizedDiscreteValue(value = 1.5f, maxIndex = 10))
        assertEquals(3, fromNormalizedDiscreteValue(value = 0.26f, maxIndex = 10))
    }
}

