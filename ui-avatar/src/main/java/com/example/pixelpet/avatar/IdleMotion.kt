package com.example.pixelpet.avatar

import kotlin.math.PI
import kotlin.math.sin

data class IdleMotionConfig(
    val floatAmplitudePx: Float = 2f,
    val breatheScaleYAmplitude: Float = 0.006f,
    val periodMs: Long = 3_800L,
)

data class IdleMotionFrame(
    val translationY: Float,
    val scaleY: Float,
) {
    companion object {
        val Neutral = IdleMotionFrame(
            translationY = 0f,
            scaleY = 1f,
        )
    }
}

object IdleMotion {
    fun frameAt(
        elapsedMs: Long,
        config: IdleMotionConfig = IdleMotionConfig(),
        motionEnabled: Boolean = true,
    ): IdleMotionFrame {
        if (!motionEnabled) {
            return IdleMotionFrame.Neutral
        }

        require(config.floatAmplitudePx in 0f..3f) { "Idle float amplitude must stay subtle." }
        require(config.breatheScaleYAmplitude in 0f..0.008f) { "Idle breathe scale must stay subtle." }
        require(config.periodMs > 0L) { "Idle motion period must be positive." }

        val phase = (elapsedMs.coerceAtLeast(0L) % config.periodMs).toDouble() / config.periodMs
        val wave = sin(phase * 2.0 * PI).toFloat()

        return IdleMotionFrame(
            translationY = -wave * config.floatAmplitudePx,
            scaleY = 1f + wave * config.breatheScaleYAmplitude,
        )
    }
}
