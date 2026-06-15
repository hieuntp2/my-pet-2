package com.example.pixelpet.avatar

class SpriteAnimation(
    private val frameSequence: List<Int>,
    private val frameDurationsMs: List<Long>,
    private val loops: Boolean,
    private val holdFinalFrame: Boolean = !loops,
) {
    val frameCount: Int = frameSequence.size
    val totalDurationMs: Long = frameDurationsMs.sum()

    init {
        require(frameSequence.isNotEmpty()) { "Animation must have at least one frame." }
        require(frameDurationsMs.isNotEmpty()) { "Frame durations must not be empty." }
        require(frameSequence.size == frameDurationsMs.size) { "Frame sequence and durations must have the same size." }
        require(frameSequence.all { it >= 0 }) { "Frame sequence indexes must be non-negative." }
        require(frameDurationsMs.all { it > 0L }) { "Frame durations must be positive." }
    }

    fun frameAt(elapsedMs: Long): Int {
        val safeElapsed = elapsedMs.coerceAtLeast(0L)
        val playbackMs = if (loops) {
            safeElapsed % totalDurationMs
        } else if (holdFinalFrame) {
            safeElapsed.coerceAtMost(totalDurationMs - 1L)
        } else {
            safeElapsed
        }

        if (!loops && !holdFinalFrame && playbackMs >= totalDurationMs) {
            return frameSequence.first()
        }

        var accumulatedMs = 0L
        frameDurationsMs.forEachIndexed { index, durationMs ->
            accumulatedMs += durationMs
            if (playbackMs < accumulatedMs) {
                return frameSequence[index]
            }
        }

        return frameSequence.last()
    }

    fun isComplete(elapsedMs: Long): Boolean = !loops && elapsedMs >= totalDurationMs

    companion object {
        fun looping(frameCount: Int, frameDurationMs: Long): SpriteAnimation {
            require(frameCount > 0) { "Looping animation must have at least one frame." }
            return SpriteAnimation(
                frameSequence = List(frameCount) { it },
                frameDurationsMs = List(frameCount) { frameDurationMs },
                loops = true,
            )
        }

        fun looping(frameDurationsMs: List<Long>): SpriteAnimation =
            SpriteAnimation(
                frameSequence = frameDurationsMs.indices.toList(),
                frameDurationsMs = frameDurationsMs,
                loops = true,
            )

        fun oneShot(frameDurationsMs: List<Long>): SpriteAnimation =
            SpriteAnimation(
                frameSequence = frameDurationsMs.indices.toList(),
                frameDurationsMs = frameDurationsMs,
                loops = false,
                holdFinalFrame = true,
            )
    }
}
