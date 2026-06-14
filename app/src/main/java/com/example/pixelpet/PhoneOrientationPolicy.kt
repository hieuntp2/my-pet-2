package com.example.pixelpet

object PhoneOrientationPolicy {
    fun shouldShowRotateBlocker(
        screenWidthDp: Int,
        screenHeightDp: Int,
    ): Boolean {
        val shortestWidthDp = minOf(screenWidthDp, screenHeightDp)
        val isPhoneSized = shortestWidthDp < 600
        val isPortrait = screenHeightDp > screenWidthDp

        return isPhoneSized && isPortrait
    }
}
