package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class PixelPetRendererTest {
    @Test
    fun spriteLayoutUsesReducedIntegerScaleAndStaysCentered() {
        val layout = PixelPetRenderer.spriteLayout(
            canvasWidth = 1_280f,
            canvasHeight = 720f,
            frameWidth = 64,
            frameHeight = 32,
            lookOffsetX = 0,
            lookOffsetY = 0,
        )

        assertEquals(12, layout.scale)
        assertEquals(768, layout.destinationWidth)
        assertEquals(384, layout.destinationHeight)
        assertEquals(256, layout.destinationLeft)
        assertEquals(168, layout.destinationTop)
    }

    @Test
    fun spriteLayoutAppliesGazeOffsetInScaledPixelSteps() {
        val layout = PixelPetRenderer.spriteLayout(
            canvasWidth = 1_280f,
            canvasHeight = 720f,
            frameWidth = 64,
            frameHeight = 32,
            lookOffsetX = 4,
            lookOffsetY = -2,
        )

        assertEquals(304, layout.destinationLeft)
        assertEquals(144, layout.destinationTop)
    }
}
