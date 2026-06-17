package com.example.pixelpet.avatar

internal object PetTapTarget {
    fun containsTap(
        canvasWidth: Int,
        canvasHeight: Int,
        frameWidth: Int,
        frameHeight: Int,
        lookOffsetX: Int,
        lookOffsetY: Int,
        tapX: Float,
        tapY: Float,
    ): Boolean {
        if (canvasWidth <= 0 || canvasHeight <= 0) {
            return false
        }

        val layout = PixelPetRenderer.spriteLayout(
            canvasWidth = canvasWidth.toFloat(),
            canvasHeight = canvasHeight.toFloat(),
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            lookOffsetX = lookOffsetX,
            lookOffsetY = lookOffsetY,
        )

        return tapX >= layout.destinationLeft &&
            tapX <= layout.destinationLeft + layout.destinationWidth &&
            tapY >= layout.destinationTop &&
            tapY <= layout.destinationTop + layout.destinationHeight
    }
}
