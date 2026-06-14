package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpriteAnimationTest {
    @Test
    fun loopsFramesFromElapsedTime() {
        val animation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150)

        assertEquals(0, animation.frameAt(elapsedMs = 0))
        assertEquals(0, animation.frameAt(elapsedMs = 149))
        assertEquals(1, animation.frameAt(elapsedMs = 150))
        assertEquals(0, animation.frameAt(elapsedMs = 600))
    }

    @Test
    fun oneShotStopsOnLastFrameAndReportsCompletion() {
        val animation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60))

        assertEquals(0, animation.frameAt(elapsedMs = 0))
        assertEquals(2, animation.frameAt(elapsedMs = 120))
        assertEquals(4, animation.frameAt(elapsedMs = 320))
        assertFalse(animation.isComplete(elapsedMs = 319))
        assertTrue(animation.isComplete(elapsedMs = 320))
    }
}
