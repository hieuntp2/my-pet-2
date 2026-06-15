package com.example.pixelpet.avatar

import kotlin.math.roundToInt

data class PetBehaviorConfig(
    val initialBlinkDelayMinMs: Long = 1_500L,
    val initialBlinkDelayMaxMs: Long = 4_000L,
    val minBlinkIntervalMs: Long = 2_500L,
    val maxBlinkIntervalMs: Long = 7_000L,
    val doubleBlinkChance: Float = 0.12f,
    val doubleBlinkGapMinMs: Long = 100L,
    val doubleBlinkGapMaxMs: Long = 180L,
    val minGazeIntervalMs: Long = 4_000L,
    val maxGazeIntervalMs: Long = 9_000L,
    val minGazeDurationMs: Long = 1_500L,
    val maxGazeDurationMs: Long = 2_500L,
)

data class PetAnimationFrame(
    val state: PetAnimationState,
    val frameIndex: Int,
    val lookOffsetX: Int,
    val lookOffsetY: Int,
    val nextBlinkInMs: Long,
)

class PetBehaviorController(
    private val idleAnimation: SpriteAnimation,
    private val blinkAnimation: SpriteAnimation,
    private val curiousAnimation: SpriteAnimation = PetAnimationClips.curiousMagnifierAnimation(),
    private val happyRewardAnimation: SpriteAnimation = PetAnimationClips.happyRewardSparkleAnimation(),
    private val startupPreviewEnabled: Boolean = false,
    private val startupPreviewStages: List<PetAnimationState> = PetStartupPreview.DefaultStageStates,
    private val random: AvatarRandom = KotlinAvatarRandom(),
    private val config: PetBehaviorConfig = PetBehaviorConfig(),
) {
    private var state = PetAnimationState.LookingIdle
    private var stateStartedAtMs = 0L
    private var nextBlinkAtMs = random.nextLong(
        config.initialBlinkDelayMinMs,
        config.initialBlinkDelayMaxMs,
    )
    private var nextGazeAtMs = random.nextLong(
        config.minGazeIntervalMs,
        config.maxGazeIntervalMs,
    )
    private var pendingDoubleBlinkAtMs: Long? = null
    private var pendingDoubleBlinkGapMs: Long? = null
    private var activeGaze: ActiveGaze? = null
    private var lastGazeDirection: LookDirection? = null
    private var startupPreviewIndex = if (startupPreviewEnabled && startupPreviewStages.isNotEmpty()) {
        0
    } else {
        STARTUP_PREVIEW_COMPLETE
    }

    fun tick(nowMs: Long): PetAnimationFrame {
        val safeNowMs = nowMs.coerceAtLeast(0L)

        val previewActive = updateStartupPreview(safeNowMs)

        if (state == PetAnimationState.HappyReward && happyRewardAnimation.isComplete(safeNowMs - stateStartedAtMs)) {
            finishHappyReward(safeNowMs)
        }

        if (!previewActive && state == PetAnimationState.LookingBlink && blinkAnimation.isComplete(safeNowMs - stateStartedAtMs)) {
            finishBlink(safeNowMs)
        }

        val doubleBlinkAtMs = pendingDoubleBlinkAtMs
        if (!previewActive && state == PetAnimationState.LookingIdle && doubleBlinkAtMs != null && safeNowMs >= doubleBlinkAtMs) {
            pendingDoubleBlinkAtMs = null
            startBlink(safeNowMs, canScheduleDoubleBlink = false)
        } else if (!previewActive && state == PetAnimationState.LookingIdle && pendingDoubleBlinkAtMs == null && safeNowMs >= nextBlinkAtMs) {
            startBlink(safeNowMs, canScheduleDoubleBlink = true)
        }

        if (!previewActive && state == PetAnimationState.LookingIdle) {
            updateGaze(safeNowMs)
        }

        val frameIndex = when (state) {
            PetAnimationState.LookingIdle -> idleAnimation.frameAt(safeNowMs)
            PetAnimationState.LookingBlink -> blinkAnimation.frameAt(safeNowMs - stateStartedAtMs)
            PetAnimationState.Curious -> curiousAnimation.frameAt(safeNowMs - stateStartedAtMs)
            PetAnimationState.HappyReward -> happyRewardAnimation.frameAt(safeNowMs - stateStartedAtMs)
        }
        val gazeOffset = activeGaze?.offsetAt(safeNowMs) ?: GazeOffset.Center

        return PetAnimationFrame(
            state = state,
            frameIndex = frameIndex,
            lookOffsetX = gazeOffset.x,
            lookOffsetY = gazeOffset.y,
            nextBlinkInMs = nextBlinkDelayFrom(safeNowMs),
        )
    }

    fun enterCurious(nowMs: Long) {
        val safeNowMs = nowMs.coerceAtLeast(0L)
        startupPreviewIndex = STARTUP_PREVIEW_COMPLETE
        if (state == PetAnimationState.Curious) {
            return
        }

        changeState(PetAnimationState.Curious, safeNowMs, reason = "enter_curious")
        stateStartedAtMs = safeNowMs
        activeGaze = null
        pendingDoubleBlinkAtMs = null
        pendingDoubleBlinkGapMs = null
    }

    fun enterHappyReward(nowMs: Long) {
        val safeNowMs = nowMs.coerceAtLeast(0L)
        startupPreviewIndex = STARTUP_PREVIEW_COMPLETE
        if (state == PetAnimationState.HappyReward) {
            return
        }

        changeState(PetAnimationState.HappyReward, safeNowMs, reason = "enter_happy_reward")
        stateStartedAtMs = safeNowMs
        activeGaze = null
        pendingDoubleBlinkAtMs = null
        pendingDoubleBlinkGapMs = null
    }

    private fun updateStartupPreview(nowMs: Long): Boolean {
        if (startupPreviewIndex == STARTUP_PREVIEW_COMPLETE) {
            return false
        }

        while (startupPreviewIndex in startupPreviewStages.indices) {
            val previewState = startupPreviewStages[startupPreviewIndex]
            if (state != previewState) {
                enterPreviewState(previewState, nowMs)
            }

            val elapsedMs = nowMs - stateStartedAtMs
            if (elapsedMs < previewDurationFor(previewState)) {
                return true
            }

            startupPreviewIndex += 1
        }

        startupPreviewIndex = STARTUP_PREVIEW_COMPLETE
        changeState(PetAnimationState.LookingIdle, nowMs, reason = "startup_preview_complete")
        stateStartedAtMs = nowMs
        activeGaze = null
        pendingDoubleBlinkAtMs = null
        pendingDoubleBlinkGapMs = null
        nextBlinkAtMs = nowMs + random.nextLong(config.minBlinkIntervalMs, config.maxBlinkIntervalMs)
        nextGazeAtMs = nowMs + random.nextLong(config.minGazeIntervalMs, config.maxGazeIntervalMs)
        return false
    }

    private fun enterPreviewState(previewState: PetAnimationState, nowMs: Long) {
        changeState(previewState, nowMs, reason = "startup_preview_stage")
        stateStartedAtMs = nowMs
        activeGaze = null
        pendingDoubleBlinkAtMs = null
        pendingDoubleBlinkGapMs = null
    }

    private fun previewDurationFor(previewState: PetAnimationState): Long = when (previewState) {
        PetAnimationState.LookingIdle -> PetStartupPreview.IDLE_STAGE_DURATION_MS
        PetAnimationState.LookingBlink -> blinkAnimation.totalDurationMs
        PetAnimationState.Curious -> curiousAnimation.totalDurationMs
        PetAnimationState.HappyReward -> happyRewardAnimation.totalDurationMs
    }

    private fun startBlink(nowMs: Long, canScheduleDoubleBlink: Boolean) {
        changeState(PetAnimationState.LookingBlink, nowMs, reason = "blink_start")
        stateStartedAtMs = nowMs
        nextBlinkAtMs = Long.MAX_VALUE

        pendingDoubleBlinkGapMs = if (canScheduleDoubleBlink && random.nextFloat() < config.doubleBlinkChance) {
            random.nextLong(config.doubleBlinkGapMinMs, config.doubleBlinkGapMaxMs)
        } else {
            null
        }
    }

    private fun finishBlink(nowMs: Long) {
        changeState(PetAnimationState.LookingIdle, nowMs, reason = "blink_complete")
        stateStartedAtMs = nowMs

        val doubleBlinkGapMs = pendingDoubleBlinkGapMs
        if (doubleBlinkGapMs != null) {
            pendingDoubleBlinkAtMs = nowMs + doubleBlinkGapMs
            pendingDoubleBlinkGapMs = null
        } else {
            nextBlinkAtMs = nowMs + random.nextLong(config.minBlinkIntervalMs, config.maxBlinkIntervalMs)
        }
    }

    private fun finishHappyReward(nowMs: Long) {
        changeState(PetAnimationState.LookingIdle, nowMs, reason = "happy_reward_complete")
        stateStartedAtMs = nowMs
        nextBlinkAtMs = nowMs + random.nextLong(config.minBlinkIntervalMs, config.maxBlinkIntervalMs)
    }

    private fun updateGaze(nowMs: Long) {
        val gaze = activeGaze
        if (gaze != null && gaze.isComplete(nowMs)) {
            activeGaze = null
            nextGazeAtMs = nowMs + random.nextLong(config.minGazeIntervalMs, config.maxGazeIntervalMs)
        }

        if (activeGaze == null && pendingDoubleBlinkAtMs == null && nowMs >= nextGazeAtMs) {
            val direction = LookDirection.fromGazeRoll(random.nextFloat()).withoutRepeating(lastGazeDirection)
            lastGazeDirection = direction
            activeGaze = ActiveGaze(
                startedAtMs = nowMs,
                durationMs = random.nextLong(config.minGazeDurationMs, config.maxGazeDurationMs),
                direction = direction,
            )
        }
    }

    private fun nextBlinkDelayFrom(nowMs: Long): Long {
        val scheduledBlinkAtMs = pendingDoubleBlinkAtMs ?: nextBlinkAtMs
        return if (scheduledBlinkAtMs == Long.MAX_VALUE) {
            0L
        } else {
            (scheduledBlinkAtMs - nowMs).coerceAtLeast(0L)
        }
    }

    private fun changeState(newState: PetAnimationState, nowMs: Long, reason: String) {
        val oldState = state
        if (oldState != newState) {
            AvatarDebugLog.stateChanged(oldState, newState, nowMs, reason)
        }
        state = newState
    }

    private companion object {
        const val STARTUP_PREVIEW_COMPLETE = -1
    }
}

private enum class LookDirection(
    val offsetX: Int,
    val offsetY: Int,
) {
    Right(offsetX = 4, offsetY = 0),
    Left(offsetX = -4, offsetY = 0),
    Up(offsetX = 0, offsetY = -2),
    Down(offsetX = 0, offsetY = 2);

    fun withoutRepeating(previous: LookDirection?): LookDirection {
        if (this != previous) {
            return this
        }

        return when (this) {
            Right -> Left
            Left -> Right
            Up -> Down
            Down -> Up
        }
    }

    companion object {
        fun fromGazeRoll(roll: Float): LookDirection = when {
            roll < 0.42f -> Right
            roll < 0.84f -> Left
            roll < 0.92f -> Up
            else -> Down
        }
    }
}

private data class GazeOffset(
    val x: Int,
    val y: Int,
) {
    companion object {
        val Center = GazeOffset(x = 0, y = 0)
    }
}

private data class ActiveGaze(
    val startedAtMs: Long,
    val durationMs: Long,
    val direction: LookDirection,
) {
    private val moveDurationMs: Long = ((durationMs * MOVE_FRACTION).toLong())
        .coerceIn(MIN_MOVE_DURATION_MS, MAX_MOVE_DURATION_MS)
    private val returnDurationMs: Long = moveDurationMs
    private val settleDurationMs: Long = SETTLE_DURATION_MS.coerceAtMost(durationMs / 4L)
    private val holdDurationMs: Long = (durationMs - moveDurationMs - returnDurationMs - settleDurationMs)
        .coerceAtLeast(0L)

    fun offsetAt(nowMs: Long): GazeOffset {
        val elapsedMs = (nowMs - startedAtMs).coerceAtLeast(0L)
        val returnStartsAtMs = moveDurationMs + holdDurationMs
        val settleStartsAtMs = returnStartsAtMs + returnDurationMs

        return when {
            elapsedMs < moveDurationMs -> scaledOffset(elapsedMs.toFloat() / moveDurationMs)
            elapsedMs < returnStartsAtMs -> GazeOffset(direction.offsetX, direction.offsetY)
            elapsedMs < settleStartsAtMs -> {
                val returnProgress = (elapsedMs - returnStartsAtMs).toFloat() / returnDurationMs
                scaledOffset(1f - returnProgress)
            }
            else -> GazeOffset.Center
        }
    }

    fun isComplete(nowMs: Long): Boolean = nowMs - startedAtMs >= durationMs

    private fun scaledOffset(progress: Float): GazeOffset {
        val easedProgress = progress.coerceIn(0f, 1f)
        return GazeOffset(
            x = (direction.offsetX * easedProgress).roundToInt(),
            y = (direction.offsetY * easedProgress).roundToInt(),
        )
    }

    private companion object {
        const val MOVE_FRACTION = 0.24f
        const val MIN_MOVE_DURATION_MS = 350L
        const val MAX_MOVE_DURATION_MS = 550L
        const val SETTLE_DURATION_MS = 200L
    }
}
