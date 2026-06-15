package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class PetBehaviorControllerTest {
    @Test
    fun startsBlinkWhenScheduledAndReturnsToIdleAfterOneShotCompletes() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(2_500, 4_000, 2_500),
                floats = listOf(0.50f, 0.50f),
            ),
        )

        assertEquals(PetAnimationState.LookingIdle, controller.tick(nowMs = 2_499).state)
        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 2_500).state)
        assertEquals(PetAnimationState.LookingIdle, controller.tick(nowMs = 2_820).state)
    }

    @Test
    fun doubleBlinkOverridesIdleAfterConfiguredGap() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(2_500, 4_000, 120, 2_500),
                floats = listOf(0.01f, 0.50f),
            ),
        )

        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 2_500).state)
        assertEquals(PetAnimationState.LookingIdle, controller.tick(nowMs = 2_820).state)
        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 2_940).state)
    }

    @Test
    fun gazeUsesMoveHoldReturnSettleTiming() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(20_000, 4_000, 1_800, 4_000, 1_800),
                floats = listOf(0.10f, 0.90f),
            ),
            config = PetBehaviorConfig(
                initialBlinkDelayMinMs = 20_000,
                initialBlinkDelayMaxMs = 20_000,
            ),
        )

        assertEquals(0, controller.tick(nowMs = 3_999).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 4_000).lookOffsetX)
        assertEquals(2, controller.tick(nowMs = 4_200).lookOffsetX)
        assertEquals(4, controller.tick(nowMs = 4_450).lookOffsetX)
        assertEquals(3, controller.tick(nowMs = 5_300).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 5_800).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 9_799).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 9_800).lookOffsetX)
    }

    @Test
    fun gazeDoesNotStartWhileBlinking() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(3_900, 4_000, 2_500, 1_800),
                floats = listOf(0.50f, 0.10f),
            ),
        )

        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 3_900).state)
        val blinkingFrame = controller.tick(nowMs = 4_100)

        assertEquals(PetAnimationState.LookingBlink, blinkingFrame.state)
        assertEquals(0, blinkingFrame.lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 4_220).lookOffsetX)
        assertEquals(2, controller.tick(nowMs = 4_420).lookOffsetX)
    }

    @Test
    fun repeatedGazeRollUsesOppositeDirection() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(20_000, 4_000, 1_800, 4_000, 1_800),
                floats = listOf(0.10f, 0.10f),
            ),
            config = PetBehaviorConfig(
                initialBlinkDelayMinMs = 20_000,
                initialBlinkDelayMaxMs = 20_000,
            ),
        )

        assertEquals(0, controller.tick(nowMs = 4_000).lookOffsetX)
        assertEquals(4, controller.tick(nowMs = 4_450).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 5_800).lookOffsetX)
        assertEquals(0, controller.tick(nowMs = 9_800).lookOffsetX)
        assertEquals(-4, controller.tick(nowMs = 10_250).lookOffsetX)
    }

    @Test
    fun curiousStateUsesMagnifierClipTimingAndLoops() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            curiousAnimation = SpriteAnimation.looping(frameDurationsMs = listOf(220, 180, 520, 650)),
            random = ScriptedAvatarRandom(
                longs = listOf(20_000, 9_000),
                floats = emptyList(),
            ),
            config = PetBehaviorConfig(
                initialBlinkDelayMinMs = 20_000,
                initialBlinkDelayMaxMs = 20_000,
            ),
        )

        controller.enterCurious(nowMs = 1_000)

        assertEquals(PetAnimationState.Curious, controller.tick(nowMs = 1_000).state)
        assertEquals(0, controller.tick(nowMs = 1_219).frameIndex)
        assertEquals(1, controller.tick(nowMs = 1_220).frameIndex)
        assertEquals(2, controller.tick(nowMs = 1_400).frameIndex)
        assertEquals(3, controller.tick(nowMs = 1_920).frameIndex)
        assertEquals(0, controller.tick(nowMs = 2_570).frameIndex)
    }

    @Test
    fun happyRewardUsesSparkleTimingAndReturnsToIdleAfterOneLoop() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            happyRewardAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(220, 180, 420, 520)),
            random = ScriptedAvatarRandom(
                longs = listOf(20_000, 9_000, 2_500),
                floats = emptyList(),
            ),
            config = PetBehaviorConfig(
                initialBlinkDelayMinMs = 20_000,
                initialBlinkDelayMaxMs = 20_000,
            ),
        )

        controller.enterHappyReward(nowMs = 1_000)

        assertEquals(PetAnimationState.HappyReward, controller.tick(nowMs = 1_000).state)
        assertEquals(0, controller.tick(nowMs = 1_219).frameIndex)
        assertEquals(1, controller.tick(nowMs = 1_220).frameIndex)
        assertEquals(2, controller.tick(nowMs = 1_400).frameIndex)
        assertEquals(3, controller.tick(nowMs = 1_820).frameIndex)
        assertEquals(PetAnimationState.LookingIdle, controller.tick(nowMs = 2_340).state)
    }
}

private class ScriptedAvatarRandom(
    private val longs: List<Long>,
    private val floats: List<Float>,
) : AvatarRandom {
    private var longIndex = 0
    private var floatIndex = 0

    override fun nextLong(fromInclusive: Long, toInclusive: Long): Long {
        val value = longs[longIndex++]
        require(value in fromInclusive..toInclusive) {
            "Scripted value $value outside requested range $fromInclusive..$toInclusive"
        }
        return value
    }

    override fun nextFloat(): Float = floats[floatIndex++]
}
