package com.example.pixelpet.avatar

class SpriteAnimation private constructor(
    private val frameDurationsMs: List<Long>,
    private val loops: Boolean,
) {
    val frameCount: Int = frameDurationsMs.size
    val totalDurationMs: Long = frameDurationsMs.sum()

    init {
        require(frameDurationsMs.isNotEmpty()) { "Animation must have at least one frame." }
        require(frameDurationsMs.all { it > 0L }) { "Frame durations must be positive." }
    }

    fun frameAt(elapsedMs: Long): Int {
        val safeElapsed = elapsedMs.coerceAtLeast(0L)
        val playbackMs = if (loops) {
            safeElapsed % totalDurationMs
        } else {
            safeElapsed.coerceAtMost(totalDurationMs - 1L)
        }

        var accumulatedMs = 0L
        frameDurationsMs.forEachIndexed { index, durationMs ->
            accumulatedMs += durationMs
            if (playbackMs < accumulatedMs) {
                return index
            }
        }

        return frameCount - 1
    }

    fun isComplete(elapsedMs: Long): Boolean = !loops && elapsedMs >= totalDurationMs

    companion object {
        fun looping(frameCount: Int, frameDurationMs: Long): SpriteAnimation {
            require(frameCount > 0) { "Looping animation must have at least one frame." }
            return SpriteAnimation(List(frameCount) { frameDurationMs }, loops = true)
        }

        fun looping(frameDurationsMs: List<Long>): SpriteAnimation =
            SpriteAnimation(frameDurationsMs, loops = true)

        fun oneShot(frameDurationsMs: List<Long>): SpriteAnimation =
            SpriteAnimation(frameDurationsMs, loops = false)
    }
}
