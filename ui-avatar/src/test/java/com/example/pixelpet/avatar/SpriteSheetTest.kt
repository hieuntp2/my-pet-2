package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class SpriteSheetTest {
    @Test
    fun computesFrameCountFromHorizontalSheetWidth() {
        val sheet = SpriteSheet(
            widthPx = 256,
            heightPx = 32,
            frameWidthPx = 64,
            frameHeightPx = 32,
        )

        assertEquals(4, sheet.frameCount)
    }

    @Test
    fun returnsExactSourceRectForFrame() {
        val sheet = SpriteSheet(
            widthPx = 384,
            heightPx = 32,
            frameWidthPx = 64,
            frameHeightPx = 32,
        )

        assertEquals(SpriteFrameRect(left = 128, top = 0, width = 64, height = 32), sheet.frameRect(2))
    }
}
