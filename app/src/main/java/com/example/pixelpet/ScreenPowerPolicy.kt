package com.example.pixelpet

data class ScreenPowerSettings(
    val keepScreenOn: Boolean,
    val screenBrightness: Float,
)

object ScreenPowerPolicy {
    const val SystemDefaultBrightness = -1f
    const val DimmedBrightness = 0.18f
    const val DimDelayMs = 30_000L

    fun startup(): ScreenPowerSettings = active()

    fun active(): ScreenPowerSettings = ScreenPowerSettings(
        keepScreenOn = true,
        screenBrightness = SystemDefaultBrightness,
    )

    fun dimmed(): ScreenPowerSettings = ScreenPowerSettings(
        keepScreenOn = true,
        screenBrightness = DimmedBrightness,
    )

    fun onPetInteraction(): ScreenPowerSettings = active()

    fun settingsForIdleDuration(idleDurationMs: Long): ScreenPowerSettings {
        return if (idleDurationMs >= DimDelayMs) dimmed() else active()
    }

    fun nextDimDelayMs(idleDurationMs: Long): Long {
        return (DimDelayMs - idleDurationMs).coerceAtLeast(0L)
    }
}
