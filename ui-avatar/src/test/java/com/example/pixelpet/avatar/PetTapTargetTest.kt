package com.example.pixelpet.avatar

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PetTapTargetTest {
    @Test
    fun containsTapInsideRenderedSpriteBounds() {
        assertTrue(
            PetTapTarget.containsTap(
                canvasWidth = 1_280,
                canvasHeight = 720,
                frameWidth = 64,
                frameHeight = 32,
                lookOffsetX = 0,
                lookOffsetY = 0,
                tapX = 640f,
                tapY = 360f,
            ),
        )
    }

    @Test
    fun ignoresTapOutsideRenderedSpriteBounds() {
        assertFalse(
            PetTapTarget.containsTap(
                canvasWidth = 1_280,
                canvasHeight = 720,
                frameWidth = 64,
                frameHeight = 32,
                lookOffsetX = 0,
                lookOffsetY = 0,
                tapX = 120f,
                tapY = 120f,
            ),
        )
    }

    @Test
    fun appliesCurrentGazeOffsetToTapBounds() {
        assertTrue(
            PetTapTarget.containsTap(
                canvasWidth = 1_280,
                canvasHeight = 720,
                frameWidth = 64,
                frameHeight = 32,
                lookOffsetX = 4,
                lookOffsetY = -2,
                tapX = 305f,
                tapY = 145f,
            ),
        )
        assertFalse(
            PetTapTarget.containsTap(
                canvasWidth = 1_280,
                canvasHeight = 720,
                frameWidth = 64,
                frameHeight = 32,
                lookOffsetX = 4,
                lookOffsetY = -2,
                tapX = 257f,
                tapY = 169f,
            ),
        )
    }
}
