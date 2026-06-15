package com.example.pixelpet.avatar

import android.util.Log

internal object AvatarDebugLog {
    const val TAG = "PixelPetTrace"

    fun assetLoaded(assetPath: String, sheet: SpriteSheet) {
        info(
            "asset_loaded path=$assetPath size=${sheet.widthPx}x${sheet.heightPx} " +
                "frame=${sheet.frameWidthPx}x${sheet.frameHeightPx} frames=${sheet.frameCount}",
        )
    }

    fun assetMissing(assetPath: String, exception: Exception) {
        warn("asset_missing path=$assetPath", exception)
    }

    fun assetInvalid(assetPath: String, widthPx: Int, heightPx: Int, exception: Exception) {
        warn("asset_invalid path=$assetPath size=${widthPx}x${heightPx}", exception)
    }

    fun avatarLoopStarted(
        idleFrames: Int?,
        blinkFrames: Int?,
        curiousFrames: Int?,
        happyRewardFrames: Int?,
        startupPreviewEnabled: Boolean,
        missingAssets: List<String>,
        debugVisualState: PetAnimationState?,
    ) {
        info(
            "avatar_loop_started idleFrames=$idleFrames blinkFrames=$blinkFrames " +
                "curiousFrames=$curiousFrames happyRewardFrames=$happyRewardFrames " +
                "startupPreviewEnabled=$startupPreviewEnabled " +
                "missingAssets=${missingAssets.joinToString(prefix = "[", postfix = "]")} " +
                "debugVisualState=${debugVisualState?.clipName ?: "none"}",
        )
    }

    fun frameHeartbeat(
        elapsedMs: Long,
        frame: PetAnimationFrame,
        sheet: SpriteSheet?,
        motionEnabled: Boolean,
    ) {
        val sheetSummary = if (sheet == null) {
            "fallback"
        } else {
            "${sheet.widthPx}x${sheet.heightPx}/${sheet.frameWidthPx}x${sheet.frameHeightPx}/${sheet.frameCount}"
        }
        val runtime = Runtime.getRuntime()
        val usedMemoryKb = (runtime.totalMemory() - runtime.freeMemory()) / 1_024L
        val maxMemoryKb = runtime.maxMemory() / 1_024L
        debug(
            "avatar_frame t=${elapsedMs}ms state=${frame.state.clipName} frame=${frame.frameIndex} " +
                "look=(${frame.lookOffsetX},${frame.lookOffsetY}) nextBlink=${frame.nextBlinkInMs}ms " +
                "sheet=$sheetSummary motionEnabled=$motionEnabled heap=${usedMemoryKb}kb/${maxMemoryKb}kb",
        )
    }

    fun stateChanged(
        from: PetAnimationState,
        to: PetAnimationState,
        nowMs: Long,
        reason: String,
    ) {
        info("state_changed from=${from.clipName} to=${to.clipName} t=${nowMs}ms reason=$reason")
    }

    private fun debug(message: String) {
        runCatching { Log.d(TAG, message) }
    }

    private fun info(message: String) {
        runCatching { Log.i(TAG, message) }
    }

    private fun warn(message: String, exception: Exception) {
        runCatching { Log.w(TAG, message, exception) }
    }
}
