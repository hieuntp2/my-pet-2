package com.example.pixelpet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PixelPetCrashLog.install()
        PixelPetCrashLog.lifecycle("on_create")
        val debugVisualState = PixelPetDebugOptions.visualStateFrom(
            intent.getStringExtra(PixelPetDebugOptions.EXTRA_VISUAL_STATE),
        )
        setContent {
            PixelPetApp(debugVisualState = debugVisualState)
        }
    }

    override fun onStart() {
        super.onStart()
        PixelPetCrashLog.lifecycle("on_start")
    }

    override fun onResume() {
        super.onResume()
        PixelPetCrashLog.lifecycle("on_resume")
    }

    override fun onPause() {
        PixelPetCrashLog.lifecycle("on_pause")
        super.onPause()
    }

    override fun onStop() {
        PixelPetCrashLog.lifecycle("on_stop")
        super.onStop()
    }

    override fun onDestroy() {
        PixelPetCrashLog.lifecycle("on_destroy")
        super.onDestroy()
    }

    override fun onTrimMemory(level: Int) {
        PixelPetCrashLog.trimMemory(level)
        super.onTrimMemory(level)
    }
}
