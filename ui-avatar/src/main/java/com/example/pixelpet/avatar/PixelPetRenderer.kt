package com.example.pixelpet.avatar

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

internal data class PixelSpriteLayout(
    val scale: Int,
    val destinationLeft: Int,
    val destinationTop: Int,
    val destinationWidth: Int,
    val destinationHeight: Int,
)

object PixelPetRenderer {
    fun DrawScope.drawSprite(
        spriteSheet: SpriteSheetImage,
        frameIndex: Int,
        lookOffsetX: Int,
        lookOffsetY: Int,
    ) {
        val sheet = spriteSheet.sheet
        val layout = spriteLayout(
            canvasWidth = size.width,
            canvasHeight = size.height,
            frameWidth = sheet.frameWidthPx,
            frameHeight = sheet.frameHeightPx,
            lookOffsetX = lookOffsetX,
            lookOffsetY = lookOffsetY,
        )
        val sourceFrame = sheet.frameRect(frameIndex.coerceIn(0, sheet.frameCount - 1))

        drawImage(
            image = spriteSheet.image,
            srcOffset = IntOffset(sourceFrame.left, sourceFrame.top),
            srcSize = IntSize(sourceFrame.width, sourceFrame.height),
            dstOffset = IntOffset(layout.destinationLeft, layout.destinationTop),
            dstSize = IntSize(layout.destinationWidth, layout.destinationHeight),
            filterQuality = FilterQuality.None,
        )
    }

    fun DrawScope.drawFallback(frame: PetAnimationFrame) {
        val layout = spriteLayout(
            canvasWidth = size.width,
            canvasHeight = size.height,
            frameWidth = SpriteSheet.FRAME_WIDTH_PX,
            frameHeight = SpriteSheet.FRAME_HEIGHT_PX,
            lookOffsetX = frame.lookOffsetX,
            lookOffsetY = frame.lookOffsetY,
        )
        val scale = layout.scale
        val originX = layout.destinationLeft
        val originY = layout.destinationTop
        val eyeHeightPx = fallbackEyeHeight(frame)
        val eyeTopPx = 16 - eyeHeightPx / 2

        drawPixelRect(originX, originY, left = 12, top = eyeTopPx, width = 16, height = eyeHeightPx, scale = scale, color = Color(0xFF00E5FF))
        drawPixelRect(originX, originY, left = 36, top = eyeTopPx, width = 16, height = eyeHeightPx, scale = scale, color = Color(0xFF00E5FF))

        if (eyeHeightPx >= 6) {
            drawPixelRect(originX, originY, left = 15, top = eyeTopPx + 2, width = 4, height = 2, scale = scale, color = Color(0xFFD9FCFF))
            drawPixelRect(originX, originY, left = 39, top = eyeTopPx + 2, width = 4, height = 2, scale = scale, color = Color(0xFFD9FCFF))
            drawPixelRect(originX, originY, left = 12, top = eyeTopPx + eyeHeightPx, width = 16, height = 1, scale = scale, color = Color(0xFF12304A))
            drawPixelRect(originX, originY, left = 36, top = eyeTopPx + eyeHeightPx, width = 16, height = 1, scale = scale, color = Color(0xFF12304A))
        }
    }

    internal fun spriteLayout(
        canvasWidth: Float,
        canvasHeight: Float,
        frameWidth: Int,
        frameHeight: Int,
        lookOffsetX: Int,
        lookOffsetY: Int,
    ): PixelSpriteLayout {
        val scale = integerScaleFor(
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            frameWidth = frameWidth,
            frameHeight = frameHeight,
        )
        val destinationWidth = frameWidth * scale
        val destinationHeight = frameHeight * scale
        val destinationLeft = ((canvasWidth - destinationWidth) / 2f).roundToInt() + lookOffsetX * scale
        val destinationTop = ((canvasHeight - destinationHeight) / 2f).roundToInt() + lookOffsetY * scale

        return PixelSpriteLayout(
            scale = scale,
            destinationLeft = destinationLeft,
            destinationTop = destinationTop,
            destinationWidth = destinationWidth,
            destinationHeight = destinationHeight,
        )
    }

    private fun integerScaleFor(
        canvasWidth: Float,
        canvasHeight: Float,
        frameWidth: Int,
        frameHeight: Int,
    ): Int {
        val fullScale = floor(min(canvasWidth / frameWidth, canvasHeight / frameHeight)).toInt()
        return (fullScale * SPRITE_SCALE_RATIO).roundToInt().coerceAtLeast(1)
    }

    private fun DrawScope.drawPixelRect(
        originX: Int,
        originY: Int,
        left: Int,
        top: Int,
        width: Int,
        height: Int,
        scale: Int,
        color: Color,
    ) {
        drawRect(
            color = color,
            topLeft = Offset(
                x = (originX + left * scale).toFloat(),
                y = (originY + top * scale).toFloat(),
            ),
            size = Size(
                width = (width * scale).toFloat(),
                height = (height * scale).toFloat(),
            ),
        )
    }

    private fun fallbackEyeHeight(frame: PetAnimationFrame): Int {
        if (frame.state != PetAnimationState.LookingBlink) {
            return 12
        }

        return when (frame.frameIndex) {
            0 -> 12
            1 -> 8
            2 -> 4
            3 -> 2
            else -> 8
        }
    }

    private const val SPRITE_SCALE_RATIO = 0.60f
}
