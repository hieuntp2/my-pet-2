package com.example.pixelpet.avatar

data class SpriteFrameRect(
    val left: Int,
    val top: Int,
    val width: Int,
    val height: Int,
)

class SpriteSheet(
    val widthPx: Int,
    val heightPx: Int,
    val frameWidthPx: Int = FRAME_WIDTH_PX,
    val frameHeightPx: Int = FRAME_HEIGHT_PX,
) {
    val frameCount: Int

    init {
        require(frameWidthPx > 0) { "Frame width must be positive." }
        require(frameHeightPx > 0) { "Frame height must be positive." }
        require(widthPx >= frameWidthPx) { "Sprite sheet must contain at least one frame." }
        require(heightPx == frameHeightPx) { "Sprite sheet height must be $frameHeightPx px." }
        require(widthPx % frameWidthPx == 0) { "Sprite sheet width must be divisible by frame width." }

        frameCount = widthPx / frameWidthPx
    }

    fun frameRect(frameIndex: Int): SpriteFrameRect {
        require(frameIndex in 0 until frameCount) {
            "Frame index $frameIndex is outside 0..${frameCount - 1}."
        }

        return SpriteFrameRect(
            left = frameIndex * frameWidthPx,
            top = 0,
            width = frameWidthPx,
            height = frameHeightPx,
        )
    }

    companion object {
        const val FRAME_WIDTH_PX = 64
        const val FRAME_HEIGHT_PX = 32
    }
}
