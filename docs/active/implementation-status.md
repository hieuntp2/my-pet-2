## AutoDev Run - 2026-06-15 Startup Avatar Stage Preview

**Task:** Run each current avatar stage once on app startup, then return to normal random idle behavior.
**Build:** PASSED with `.\gradlew.bat clean :ui-avatar:testDebugUnitTest assembleDebug --stacktrace`.
**Runtime check:** PASSED on `emulator-5554` with `PixelPetTrace` logcat filtering.

**Implemented:**

- Added `PetStartupPreview.DefaultStageStates` as the single ordered list for startup preview stages.
- Added startup preview playback to the active `ui-avatar` behavior controller.
- Startup preview now plays `LookingIdle`, `LookingBlink`, `Curious`, and `HappyReward` once when Home opens normally.
- After preview completion, the controller returns to normal idle/blink/gaze behavior with fresh random scheduling.
- Disabled startup preview when a debug visual state is explicitly supplied.
- Added runtime logging for `startupPreviewEnabled`.
- Added unit tests for startup preview sequencing and the default stage list.

**Runtime evidence:**

- Logcat showed `startupPreviewEnabled=true`.
- State sequence observed: `looking_idle -> looking_blink -> curious_magnifier -> happy_reward_sparkle -> looking_idle`.
- A normal random blink occurred after returning to idle.

**Autonomous decision notes:**

- Kept the future extension point as a state list, so new stages can be previewed by adding them to `PetStartupPreview.DefaultStageStates`.
- Kept the behavior in `ui-avatar`; no legacy `pixel-avatar` changes.

**Next Recommended Task:** Add a real interaction/reward path that calls `enterHappyReward(...)` outside startup preview.

---

## AutoDev Run - 2026-06-15 Happy Reward Sparkle Animation

**Task:** Add Happy Reward Sparkle animation to the active pixel pet avatar.
**Build:** PASSED with `.\gradlew.bat assembleDebug`.
**Tests:** PASSED with `.\gradlew.bat :ui-avatar:testDebugUnitTest --stacktrace`.

**Implemented:**

- Copied `resources/pet/eyes/happy_reward_sparkle.png` into `app/src/main/assets/pet/eyes/happy_reward_sparkle.png`.
- Added the `happy_reward_sparkle` animation clip with 64x32 frames and per-frame timings of 220ms, 180ms, 420ms, and 520ms.
- Added `PetAnimationState.HappyReward` and routed it to the sparkle sheet through the active `ui-avatar` runtime.
- Added a narrow `enterHappyReward(nowMs)` controller trigger that plays the reward clip once, then returns to idle with blink scheduling preserved.
- Extended existing avatar debug logging with loaded happy reward frame count.
- Added unit coverage for clip name mapping, asset sheet selection, and reward timing/completion.

**Autonomous decision notes:**

- Kept the reward animation one-shot instead of looping to match the temporary reward behavior and avoid aggressive replay.
- Did not add a new app product feature or debug screen; the active Home screen has no tap/feed/play reward hook yet.

**Next Recommended Task:** Wire positive pet interactions to `enterHappyReward(...)` when the app has an interaction/reward state surface.

---

## AutoDev Run - 2026-06-15 Runtime Diagnostics

**Task:** Add Codex-readable runtime logging and investigate app stopping during operation.
**Build:** PASSED with `.\gradlew.bat :app:testDebugUnitTest :ui-avatar:testDebugUnitTest assembleDebug --stacktrace`.
**Runtime check:** PASSED on `emulator-5554` with `PixelPetTrace` logcat filtering.

**Implemented:**

- Added `PixelPetTrace` logs for crash handler install, Activity lifecycle, trim-memory, asset loading, avatar loop start, animation state transitions, and 1-second avatar frame heartbeats.
- Added heap usage to lifecycle and avatar heartbeat logs.
- Added a hidden debug intent extra, `pixelpet.debug_visual_state=curious`, so Codex can launch the CURIOUS animation directly through adb.
- Kept normal launch unchanged and verified idle/blink logs still run.

**Root Cause Found:**

- The observed app stop on the emulator was not an app exception. Logcat showed Android low memory killer terminating `com.example.pixelpet`: `reason: min watermark is breached even after kill`.
- A separate `AndroidRuntime` crash in logcat belonged to `com.google.android.as`, not this app.

**Next Recommended Task:** Investigate emulator/device memory pressure separately if the app continues to be killed outside this run.

---

## AutoDev Run - 2026-06-15 Curious Magnifier Animation

**Task:** Add Curious Magnifier animation to the active pixel pet avatar.
**Build:** PASSED with `.\gradlew.bat assembleDebug`.
**Tests:** PASSED with `.\gradlew.bat :ui-avatar:testDebugUnitTest --stacktrace`.

**Implemented:**

- Copied `resources/pet/eyes/curious_magnifier.png` into `app/src/main/assets/pet/eyes/curious_magnifier.png`.
- Added the `curious_magnifier` animation clip with 64x32 frames and per-frame timings of 220ms, 180ms, 520ms, and 650ms.
- Added `PetAnimationState.Curious` and routed it to the magnifier clip through the active `ui-avatar` runtime.
- Added a narrow `enterCurious(nowMs)` controller trigger and a debug-only `PixelPetAvatar(debugVisualState = PetAnimationState.Curious)` hook for manual preview.
- Kept rendering through Compose Canvas with `FilterQuality.None` and integer-scaled destination sizes.
- Preserved idle/blink selection so a missing curious sheet does not disable existing idle or blink rendering.

**Next Recommended Task:** Add a proper R5 debug preview screen that can manually switch avatar emotion states.

---

## AutoDev Run - 2026-06-14 Landscape Orientation Constraint

**Task:** Make the app phone-landscape-only and document the constraint.
**Build:** PASSED with `.\gradlew.bat :app:testDebugUnitTest :ui-avatar:testDebugUnitTest assembleDebug --stacktrace`.
**Tests:** PASSED with `:app:testDebugUnitTest` and `:ui-avatar:testDebugUnitTest`.

**Implemented:**

- Locked the native Android activity to landscape orientation.
- Added a defensive phone-portrait Compose blocker that says, "Rotate your phone to landscape."
- Added unit tests for the phone orientation policy.
- Preserved the existing asset loader, blink runtime, and idle breathing/floating runtime.
- Documented the phone-landscape-only constraint in `AGENTS.md`.

**Next Recommended Task:** R5 - Add Emotion States and Debug Preview.

---

## AutoDev Run - 2026-06-14 R4 Idle Motion

**Task:** R4 - Add Subtle Idle Breathing/Floating.
**Build:** PASSED with `.\gradlew.bat :ui-avatar:testDebugUnitTest assembleDebug --stacktrace`.
**Tests:** PASSED with `:ui-avatar:testDebugUnitTest`.

**Implemented:**

- Added `IdleMotion` sine-wave helper for subtle vertical float and scaleY breathing.
- Wrapped the existing avatar Canvas render tree in an outer `graphicsLayer` transform.
- Reused the existing Compose `withFrameNanos` loop, so no extra timer or animation library was added.
- Preserved asset-loading paths and blink state/runtime behavior.
- Disabled idle motion when Android system animator duration scale is set to zero.

**Next Recommended Task:** R5 - Add Emotion States and Debug Preview.

---

## AutoDev Run - 2026-06-14 Build Sync Fix

**Task:** Fix Android Studio/Gradle source-set resolution failure.
**Build:** PASSED with `.\gradlew.bat :ui-avatar:testDebugUnitTest assembleDebug --stacktrace`.
**Regression check:** PASSED with `.\gradlew.bat :brain:compileJava --stacktrace`.

**Fixed:**

- Removed unnecessary `jvmToolchain(17)` pins from empty JVM placeholder modules.
- Resolved Gradle source-set model failure caused by missing local Java 17 toolchain.
- Kept Android and avatar animation modules unchanged.

**Next Recommended Task:** R5 - Add Emotion States and Debug Preview.

---

## AutoDev Run - 2026-06-14

**Task:** R1/R2/R3 combined first Android eye-animation milestone from explicit user request.
**Build:** PASSED with `.\gradlew.bat assembleDebug`.
**Tests:** PASSED with `:ui-avatar:testDebugUnitTest`.

**Implemented:**

- Created Gradle multi-module project: `app`, `ui-avatar`, `brain`, `memory`, `core-common`.
- Added Android app entry point and a single Compose pixel pet screen.
- Copied source eye assets from `resources/pet/eyes/` into `app/src/main/assets/pet/eyes/`.
- Loaded `pet/eyes/looking-idle.png` and `pet/eyes/looking-blink.png` through Android assets.
- Added horizontal sprite-sheet frame math for 64x32 frames.
- Added elapsed-time idle looping at 150ms per frame.
- Added randomized blink scheduling with one-shot blink playback.
- Added optional double blink scheduling with a 100ms-180ms gap.
- Added subtle randomized look offsets using weighted center/right/left/up/down choices.
- Added Compose Canvas rendering with `FilterQuality.None` for crisp pixel scaling.
- Added fallback two-eye Canvas renderer and asset warning text for missing or invalid runtime assets.
- Added focused unit tests for sprite sheet math, animation timing, blink state transitions, double blink, and look offsets.

**Autonomous decision notes:**

- Used AGP 9.2.1 built-in Kotlin Android support and applied only the Compose compiler plugin in Android modules because applying `org.jetbrains.kotlin.android` separately caused a duplicate `kotlin` extension failure.
- Kept `brain`, `memory`, and `core-common` as minimal Kotlin modules with no feature logic yet.
- Did not add persistence, personality, interaction reactions, camera, audio, cloud AI, BLE, Wi-Fi, or robot control.

**Next Recommended Task:** R5 - Add Emotion States and Debug Preview.

---

# Implementation Status - AI Pet Animation-First Rebuild

Generated: 2026-06-14

## Current State

First Android animation milestone exists with subtle idle motion.

The app opens to a single dark cyber-style pixel pet screen. The avatar runtime renders the 64x32 eye sprite sheets from Android assets when present and falls back to a simple Canvas two-eye renderer if runtime assets are missing or invalid. The avatar render tree now gently floats and breathes in code while preserving blink behavior.

## Current Active Task

R5 - Add Emotion States and Debug Preview

## Completed Tasks

- R1 - Create Android Kotlin Project Skeleton
- R2 - Render Original Pixel-Style Two-Eye Avatar
- R3 - Add Natural Blink Animation
- R4 - Add Subtle Idle Breathing/Floating

## Blocked Tasks

None.

## Important Scope Reminder

Do not implement:

- camera
- audio
- voice
- cloud AI
- robot body

Current priority is:

```text
R5
```

## Last Run Summary

Added subtle code-driven idle breathing/floating around the existing avatar render tree. Looking idle and blink still use the existing asset loader and behavior controller.

## Next Recommended Task

R5 - Add Emotion States and Debug Preview

## Notes for AutoDev

Use the first TODO task in `docs/active/backlog.md`.

Do not skip to personality or persistence until animation core is visible.
