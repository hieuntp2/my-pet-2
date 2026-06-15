package com.example.pixelpet

import android.util.Log

object PixelPetCrashLog {
    private const val TAG = "PixelPetTrace"
    private var installed = false

    fun install() {
        if (installed) {
            return
        }
        installed = true

        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(TAG, "uncaught_exception thread=${thread.name}", throwable)
            previousHandler?.uncaughtException(thread, throwable)
        }
        Log.i(TAG, "crash_logger_installed")
    }

    fun lifecycle(event: String) {
        val runtime = Runtime.getRuntime()
        val usedMemoryKb = (runtime.totalMemory() - runtime.freeMemory()) / 1_024L
        val maxMemoryKb = runtime.maxMemory() / 1_024L
        Log.i(TAG, "activity_$event heap=${usedMemoryKb}kb/${maxMemoryKb}kb")
    }

    fun trimMemory(level: Int) {
        val runtime = Runtime.getRuntime()
        val usedMemoryKb = (runtime.totalMemory() - runtime.freeMemory()) / 1_024L
        val maxMemoryKb = runtime.maxMemory() / 1_024L
        Log.w(TAG, "activity_trim_memory level=$level heap=${usedMemoryKb}kb/${maxMemoryKb}kb")
    }
}
