package com.example.pixelpet.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class PetAnimationStateTest {
    @Test
    fun curiousStateMapsToCuriousMagnifierClip() {
        assertEquals("curious_magnifier", PetAnimationState.Curious.clipName)
    }

    @Test
    fun happyRewardStateMapsToHappyRewardSparkleClip() {
        assertEquals("happy_reward_sparkle", PetAnimationState.HappyReward.clipName)
    }
}
