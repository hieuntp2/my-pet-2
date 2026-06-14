package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class IdleMotionTest {
    @Test
    fun startsFromNeutralTransform() {
        val frame = IdleMotion.frameAt(elapsedMs = 0L)

        assertEquals(0f, frame.translationY, 0.001f)
        assertEquals(1f, frame.scaleY, 0.001f)
    }

    @Test
    fun usesSubtleBoundedSineWaveMotion() {
        val config = IdleMotionConfig(
            floatAmplitudePx = 2f,
            breatheScaleYAmplitude = 0.006f,
            periodMs = 4_000L,
        )

        val highPoint = IdleMotion.frameAt(elapsedMs = 1_000L, config = config)
        val lowPoint = IdleMotion.frameAt(elapsedMs = 3_000L, config = config)

        assertEquals(-2f, highPoint.translationY, 0.001f)
        assertEquals(1.006f, highPoint.scaleY, 0.001f)
        assertEquals(2f, lowPoint.translationY, 0.001f)
        assertEquals(0.994f, lowPoint.scaleY, 0.001f)
        assertTrue(abs(highPoint.translationY) <= 3f)
        assertTrue(abs(lowPoint.scaleY - 1f) <= 0.008f)
    }

    @Test
    fun returnsNeutralTransformWhenMotionIsDisabled() {
        val frame = IdleMotion.frameAt(elapsedMs = 1_000L, motionEnabled = false)

        assertEquals(0f, frame.translationY, 0.001f)
        assertEquals(1f, frame.scaleY, 0.001f)
    }
}
