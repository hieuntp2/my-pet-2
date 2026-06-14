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

object PixelPetRenderer {
    fun DrawScope.drawSprite(
        spriteSheet: SpriteSheetImage,
        frameIndex: Int,
        lookOffsetX: Int,
        lookOffsetY: Int,
    ) {
        val sheet = spriteSheet.sheet
        val scale = integerScaleFor(
            canvasWidth = size.width,
            canvasHeight = size.height,
            frameWidth = sheet.frameWidthPx,
            frameHeight = sheet.frameHeightPx,
        )
        val destinationWidth = sheet.frameWidthPx * scale
        val destinationHeight = sheet.frameHeightPx * scale
        val destinationLeft = ((size.width - destinationWidth) / 2f).roundToInt() + lookOffsetX * scale
        val destinationTop = ((size.height - destinationHeight) / 2f).roundToInt() + lookOffsetY * scale
        val sourceFrame = sheet.frameRect(frameIndex.coerceIn(0, sheet.frameCount - 1))

        drawImage(
            image = spriteSheet.image,
            srcOffset = IntOffset(sourceFrame.left, sourceFrame.top),
            srcSize = IntSize(sourceFrame.width, sourceFrame.height),
            dstOffset = IntOffset(destinationLeft, destinationTop),
            dstSize = IntSize(destinationWidth, destinationHeight),
            filterQuality = FilterQuality.None,
        )
    }

    fun DrawScope.drawFallback(frame: PetAnimationFrame) {
        val scale = integerScaleFor(
            canvasWidth = size.width,
            canvasHeight = size.height,
            frameWidth = SpriteSheet.FRAME_WIDTH_PX,
            frameHeight = SpriteSheet.FRAME_HEIGHT_PX,
        )
        val destinationWidth = SpriteSheet.FRAME_WIDTH_PX * scale
        val destinationHeight = SpriteSheet.FRAME_HEIGHT_PX * scale
        val originX = ((size.width - destinationWidth) / 2f).roundToInt() + frame.lookOffsetX * scale
        val originY = ((size.height - destinationHeight) / 2f).roundToInt() + frame.lookOffsetY * scale
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

    private fun integerScaleFor(
        canvasWidth: Float,
        canvasHeight: Float,
        frameWidth: Int,
        frameHeight: Int,
    ): Int {
        val scale = floor(min(canvasWidth / frameWidth, canvasHeight / frameHeight)).toInt()
        return scale.coerceAtLeast(1)
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
}
