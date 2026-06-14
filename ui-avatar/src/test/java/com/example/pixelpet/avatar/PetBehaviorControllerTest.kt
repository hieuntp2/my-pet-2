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
                longs = listOf(2_500, 2_500, 2_500),
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
                longs = listOf(2_500, 120, 2_500, 2_000),
                floats = listOf(0.01f, 0.50f),
            ),
        )

        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 2_500).state)
        assertEquals(PetAnimationState.LookingIdle, controller.tick(nowMs = 2_820).state)
        assertEquals(PetAnimationState.LookingBlink, controller.tick(nowMs = 2_940).state)
    }

    @Test
    fun lookDirectionChangesUseSubtleOffsets() {
        val controller = PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(frameCount = 4, frameDurationMs = 150),
            blinkAnimation = SpriteAnimation.oneShot(frameDurationsMs = listOf(60, 60, 80, 60, 60)),
            random = ScriptedAvatarRandom(
                longs = listOf(4_000, 2_000),
                floats = listOf(0.75f, 0.50f),
            ),
        )

        val frame = controller.tick(nowMs = 2_000)

        assertEquals(2, frame.lookOffsetX)
        assertEquals(0, frame.lookOffsetY)
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
