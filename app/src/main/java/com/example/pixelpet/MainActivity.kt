package com.example.pixelpet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    private val screenPowerHandler = Handler(Looper.getMainLooper())
    private val dimScreenRunnable = Runnable {
        applyScreenPowerPolicy(ScreenPowerPolicy.dimmed())
        PixelPetCrashLog.lifecycle("screen_power_dimmed_after_idle")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PixelPetCrashLog.install()
        PixelPetCrashLog.lifecycle("on_create")
        applyScreenPowerPolicy(ScreenPowerPolicy.startup())
        val debugVisualState = PixelPetDebugOptions.visualStateFrom(
            intent.getStringExtra(PixelPetDebugOptions.EXTRA_VISUAL_STATE),
        )
        setContent {
            PixelPetApp(
                debugVisualState = debugVisualState,
                onPetInteraction = ::handlePetInteraction,
            )
        }
    }

    override fun onStart() {
        super.onStart()
        PixelPetCrashLog.lifecycle("on_start")
    }

    override fun onResume() {
        super.onResume()
        PixelPetCrashLog.lifecycle("on_resume")
        restoreScreenPowerAndScheduleDim(reason = "on_resume")
    }

    override fun onPause() {
        cancelScheduledScreenDim()
        PixelPetCrashLog.lifecycle("on_pause")
        super.onPause()
    }

    override fun onStop() {
        PixelPetCrashLog.lifecycle("on_stop")
        super.onStop()
    }

    override fun onDestroy() {
        cancelScheduledScreenDim()
        PixelPetCrashLog.lifecycle("on_destroy")
        super.onDestroy()
    }

    override fun onTrimMemory(level: Int) {
        PixelPetCrashLog.trimMemory(level)
        super.onTrimMemory(level)
    }

    private fun applyScreenPowerPolicy(policy: ScreenPowerSettings) {
        if (policy.keepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        val layoutParams = window.attributes
        layoutParams.screenBrightness = policy.screenBrightness
        window.attributes = layoutParams
        PixelPetCrashLog.lifecycle(
            "screen_power_policy keepScreenOn=${policy.keepScreenOn} " +
                "screenBrightness=${policy.screenBrightness}",
        )
    }

    private fun handlePetInteraction() {
        restoreScreenPowerAndScheduleDim(reason = "pet_interaction")
    }

    private fun restoreScreenPowerAndScheduleDim(reason: String) {
        applyScreenPowerPolicy(ScreenPowerPolicy.onPetInteraction())
        scheduleScreenDim()
        PixelPetCrashLog.lifecycle(
            "screen_power_restored reason=$reason nextDimMs=${ScreenPowerPolicy.DimDelayMs}",
        )
    }

    private fun scheduleScreenDim() {
        screenPowerHandler.removeCallbacks(dimScreenRunnable)
        screenPowerHandler.postDelayed(dimScreenRunnable, ScreenPowerPolicy.DimDelayMs)
    }

    private fun cancelScheduledScreenDim() {
        screenPowerHandler.removeCallbacks(dimScreenRunnable)
    }
}
