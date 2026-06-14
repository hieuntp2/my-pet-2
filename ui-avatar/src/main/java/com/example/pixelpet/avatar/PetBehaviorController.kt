package com.example.pixelpet.avatar

data class PetBehaviorConfig(
    val initialBlinkDelayMinMs: Long = 1_500L,
    val initialBlinkDelayMaxMs: Long = 4_000L,
    val minBlinkIntervalMs: Long = 2_500L,
    val maxBlinkIntervalMs: Long = 7_000L,
    val doubleBlinkChance: Float = 0.12f,
    val doubleBlinkGapMinMs: Long = 100L,
    val doubleBlinkGapMaxMs: Long = 180L,
    val minLookIntervalMs: Long = 2_000L,
    val maxLookIntervalMs: Long = 5_000L,
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
    private val random: AvatarRandom = KotlinAvatarRandom(),
    private val config: PetBehaviorConfig = PetBehaviorConfig(),
) {
    private var state = PetAnimationState.LookingIdle
    private var stateStartedAtMs = 0L
    private var nextBlinkAtMs = random.nextLong(
        config.initialBlinkDelayMinMs,
        config.initialBlinkDelayMaxMs,
    )
    private var nextLookAtMs = config.minLookIntervalMs
    private var pendingDoubleBlinkAtMs: Long? = null
    private var pendingDoubleBlinkGapMs: Long? = null
    private var currentLook = LookDirection.Center

    fun tick(nowMs: Long): PetAnimationFrame {
        val safeNowMs = nowMs.coerceAtLeast(0L)

        if (state == PetAnimationState.LookingBlink && blinkAnimation.isComplete(safeNowMs - stateStartedAtMs)) {
            finishBlink(safeNowMs)
        }

        val doubleBlinkAtMs = pendingDoubleBlinkAtMs
        if (state == PetAnimationState.LookingIdle && doubleBlinkAtMs != null && safeNowMs >= doubleBlinkAtMs) {
            pendingDoubleBlinkAtMs = null
            startBlink(safeNowMs, canScheduleDoubleBlink = false)
        } else if (state == PetAnimationState.LookingIdle && pendingDoubleBlinkAtMs == null && safeNowMs >= nextBlinkAtMs) {
            startBlink(safeNowMs, canScheduleDoubleBlink = true)
        }

        if (state == PetAnimationState.LookingIdle && safeNowMs >= nextLookAtMs) {
            updateLookDirection(safeNowMs)
        }

        val frameIndex = when (state) {
            PetAnimationState.LookingIdle -> idleAnimation.frameAt(safeNowMs)
            PetAnimationState.LookingBlink -> blinkAnimation.frameAt(safeNowMs - stateStartedAtMs)
        }

        return PetAnimationFrame(
            state = state,
            frameIndex = frameIndex,
            lookOffsetX = currentLook.offsetX,
            lookOffsetY = currentLook.offsetY,
            nextBlinkInMs = nextBlinkDelayFrom(safeNowMs),
        )
    }

    private fun startBlink(nowMs: Long, canScheduleDoubleBlink: Boolean) {
        state = PetAnimationState.LookingBlink
        stateStartedAtMs = nowMs
        nextBlinkAtMs = Long.MAX_VALUE

        pendingDoubleBlinkGapMs = if (canScheduleDoubleBlink && random.nextFloat() < config.doubleBlinkChance) {
            random.nextLong(config.doubleBlinkGapMinMs, config.doubleBlinkGapMaxMs)
        } else {
            null
        }
    }

    private fun finishBlink(nowMs: Long) {
        state = PetAnimationState.LookingIdle
        stateStartedAtMs = nowMs

        val doubleBlinkGapMs = pendingDoubleBlinkGapMs
        if (doubleBlinkGapMs != null) {
            pendingDoubleBlinkAtMs = nowMs + doubleBlinkGapMs
            pendingDoubleBlinkGapMs = null
        } else {
            nextBlinkAtMs = nowMs + random.nextLong(config.minBlinkIntervalMs, config.maxBlinkIntervalMs)
        }
    }

    private fun updateLookDirection(nowMs: Long) {
        currentLook = LookDirection.fromRoll(random.nextFloat())
        nextLookAtMs = nowMs + random.nextLong(config.minLookIntervalMs, config.maxLookIntervalMs)
    }

    private fun nextBlinkDelayFrom(nowMs: Long): Long {
        val scheduledBlinkAtMs = pendingDoubleBlinkAtMs ?: nextBlinkAtMs
        return if (scheduledBlinkAtMs == Long.MAX_VALUE) {
            0L
        } else {
            (scheduledBlinkAtMs - nowMs).coerceAtLeast(0L)
        }
    }
}

private enum class LookDirection(
    val offsetX: Int,
    val offsetY: Int,
) {
    Center(offsetX = 0, offsetY = 0),
    Right(offsetX = 2, offsetY = 0),
    Left(offsetX = -2, offsetY = 0),
    Up(offsetX = 0, offsetY = -1),
    Down(offsetX = 0, offsetY = 1);

    companion object {
        fun fromRoll(roll: Float): LookDirection = when {
            roll < 0.70f -> Center
            roll < 0.80f -> Right
            roll < 0.90f -> Left
            roll < 0.95f -> Up
            else -> Down
        }
    }
}
