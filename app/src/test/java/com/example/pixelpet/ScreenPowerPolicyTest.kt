package com.example.pixelpet

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreenPowerPolicyTest {
    @Test
    fun startupPolicyKeepsScreenOnAtNormalBrightness() {
        val policy = ScreenPowerPolicy.startup()

        assertTrue(policy.keepScreenOn)
        assertEquals(ScreenPowerPolicy.SystemDefaultBrightness, policy.screenBrightness, 0.0001f)
        assertEquals(30_000L, ScreenPowerPolicy.DimDelayMs)
    }

    @Test
    fun dimsOnlyAfterThirtySecondsWithoutPetInteraction() {
        assertEquals(
            ScreenPowerPolicy.active(),
            ScreenPowerPolicy.settingsForIdleDuration(idleDurationMs = 29_999L),
        )
        assertEquals(
            ScreenPowerPolicy.dimmed(),
            ScreenPowerPolicy.settingsForIdleDuration(idleDurationMs = 30_000L),
        )
    }

    @Test
    fun petInteractionRestoresNormalBrightnessAndRestartsDimDelay() {
        assertEquals(ScreenPowerPolicy.active(), ScreenPowerPolicy.onPetInteraction())
        assertEquals(
            ScreenPowerPolicy.DimDelayMs,
            ScreenPowerPolicy.nextDimDelayMs(idleDurationMs = 0L),
        )
    }
}
