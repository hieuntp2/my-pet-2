package com.example.pixelpet.avatar

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PixelPetAssetsTest {
    @Test
    fun missingCuriousSheetDoesNotDisableIdleAndBlinkSheets() {
        val idle = spriteSheetImage()
        val blink = spriteSheetImage()
        val assets = PixelPetAssets(
            idle = idle,
            blink = blink,
            curiousMagnifier = null,
            happyRewardSparkle = null,
            missingAssets = listOf(PixelPetAssetLoader.CURIOUS_MAGNIFIER_ASSET_PATH),
        )

        assertTrue(assets.hasRequiredSheets)
        assertEquals(idle, assets.sheetFor(PetAnimationState.LookingIdle))
        assertEquals(blink, assets.sheetFor(PetAnimationState.LookingBlink))
        assertNull(assets.sheetFor(PetAnimationState.Curious))
    }

    @Test
    fun happyRewardStateSelectsSparkleSheet() {
        val happyRewardSparkle = spriteSheetImage()
        val assets = PixelPetAssets(
            idle = spriteSheetImage(),
            blink = spriteSheetImage(),
            curiousMagnifier = spriteSheetImage(),
            happyRewardSparkle = happyRewardSparkle,
            missingAssets = emptyList(),
        )

        assertEquals(happyRewardSparkle, assets.sheetFor(PetAnimationState.HappyReward))
    }

    private fun spriteSheetImage(): SpriteSheetImage =
        SpriteSheetImage(
            image = TestImageBitmap,
            sheet = SpriteSheet(widthPx = 64, heightPx = 32),
        )

    private object TestImageBitmap : ImageBitmap {
        override val width: Int = 64
        override val height: Int = 32
        override val colorSpace: ColorSpace = ColorSpaces.Srgb
        override val hasAlpha: Boolean = true
        override val config: ImageBitmapConfig = ImageBitmapConfig.Argb8888

        override fun readPixels(
            buffer: IntArray,
            startX: Int,
            startY: Int,
            width: Int,
            height: Int,
            bufferOffset: Int,
            stride: Int,
        ) = Unit

        override fun prepareToDraw() = Unit
    }
}
