# Looking Idle & Blink Animation Spec — Android MVP

## Purpose

This document is a focused implementation spec for the first Android pixel-pet animation milestone.

It extends the existing animation-first rebuild docs. It does not replace:

- `AGENTS.md`
- `docs/product/vision.md`
- `docs/product/scope.md`
- `docs/product/roadmap.md`
- `docs/active/backlog.md`
- `docs/active/implementation-status.md`

If this document conflicts with the existing project rules, follow `AGENTS.md` first.

---

## Implementation Goal

Build the first Android version of the pixel pet using two eye animation sprite sheets:

- `looking-idle.png`
- `looking-blink.png`

The pet should feel alive through:

- continuous subtle idle animation
- random blink timing
- occasional double blink
- clean state transitions
- pixel-perfect rendering

This milestone must not implement personality, memory, camera, microphone, voice, cloud AI, BLE, Wi-Fi robot control, or full assistant behavior.

---

## Required Tech Direction

Use the existing project direction from `AGENTS.md`:

- Kotlin
- Native Android
- Jetpack Compose
- Compose Canvas for avatar rendering
- Gradle multi-module project

Do not switch to a custom Android `View` unless a later task explicitly changes the architecture.

---

## Asset Placement

Because the required file names contain hyphens, place them in Android assets instead of `res/drawable`.

Use this path:

```text
app/src/main/assets/pet/eyes/looking-idle.png
app/src/main/assets/pet/eyes/looking-blink.png
```

Reason:

- Android `res/drawable` resource names cannot contain hyphens.
- Keeping the exact filenames is useful for artist/developer workflow.
- Assets are easy to reuse later for ESP32 tooling/export scripts.

If the project later moves avatar rendering fully into the `ui-avatar` module, keep the loading API module-safe and avoid hard-coding UI logic in `app`.

---

## Sprite Sheet Convention

All eye animation frames use:

```text
Frame width: 64 px
Frame height: 32 px
```

Sprite sheets are horizontal strips:

```text
[frame0][frame1][frame2][frame3]...
```

### looking-idle.png

Recommended:

```text
Frame size: 64x32
Frame count: 4
Sheet size: 256x32
Playback: loop
Frame duration: 120–180 ms per frame
Suggested base: 150 ms per frame
```

Purpose:

- subtle glow breathing
- tiny cyber-eye life feeling
- no strong movement

### looking-blink.png

Recommended:

```text
Frame size: 64x32
Frame count: 6
Sheet size: 384x32
Playback: one-shot
```

Blink frame design, assuming each eye is normally about 16x12 px:

```text
Frame 0: open       eye height 12 px
Frame 1: closing    eye height 8 px
Frame 2: closing    eye height 4 px
Frame 3: closed     eye height 2 px
Frame 4: opening    eye height 8 px
Frame 5: open       eye height 12 px
```

Keep the eye center vertically stable while blinking.

Recommended vertical placement for each eye:

```text
12 px height: y = 10
8 px height:  y = 12
4 px height:  y = 14
2 px height:  y = 15
```

Recommended timing:

```text
Frame 0: 0–40 ms, optional if already open from idle
Frame 1: 60 ms
Frame 2: 60 ms
Frame 3: 80 ms
Frame 4: 60 ms
Frame 5: 60 ms
```

Total blink duration should feel around 300–360 ms.

---

## Rendering Requirements

Render pixel art without smoothing.

Required behavior:

- Use nearest-neighbor / no bitmap filtering.
- Keep source frame crop exact: 64x32.
- Scale up by integer multiples when possible.
- Center the pet on screen.
- Prefer a dark cyber background.

Recommended display scale:

```text
Phone preview: 4x to 8x depending on screen size
Logical sprite: 64x32
Rendered size examples:
- 4x: 256x128 px
- 6x: 384x192 px
- 8x: 512x256 px
```

Recommended palette:

```text
Background: #0B0F1A
Main cyan:  #00E5FF
Soft cyan:  #7AF7FF
Highlight:  #D9FCFF
Shadow:     #12304A
Accent:     #7A5CFF
```

---

## Runtime State Model

For this milestone, implement a small avatar animation runtime with these states:

```text
AvatarAnimationState.Idle
AvatarAnimationState.Blinking
```

Idle is the default loop.

Blinking is a one-shot event that temporarily interrupts idle.

Priority:

```text
Blinking > Idle
```

When blink finishes, return to idle.

---

## Random Blink Behavior

Blink must not happen at a fixed interval.

Use randomized scheduling:

```text
Initial blink delay: random 1.5s–4.0s
Normal blink interval: random 2.5s–7.0s
Double blink chance: 10%–15%
Double blink gap: 100–180 ms after first blink finishes
```

Recommended values:

```text
minBlinkIntervalMs = 2500
maxBlinkIntervalMs = 7000
doubleBlinkChance = 0.12
doubleBlinkGapMinMs = 100
doubleBlinkGapMaxMs = 180
```

The scheduler should use monotonic time, not wall-clock time.

---

## Timing Model

Do not depend only on a global FPS for all animations.

Use per-animation frame durations:

```text
Idle: 120–180 ms/frame
Blink: 60–80 ms/frame
```

A global update tick can run at 60 FPS in Compose, but frame selection should be based on elapsed time.

---

## Suggested Classes

Use names close to these, but keep the codebase clean and consistent with the actual project structure.

```text
ui-avatar/
  AvatarAnimationState.kt
  SpriteSheet.kt
  SpriteAnimation.kt
  PixelPetAnimator.kt
  PixelPetAvatar.kt
```

### AvatarAnimationState

```kotlin
enum class AvatarAnimationState {
    Idle,
    Blinking
}
```

### SpriteSheet

Responsible for:

- holding decoded bitmap/image
- frame width
- frame height
- frame count
- extracting frame source rect by index

### SpriteAnimation

Responsible for:

- frame count
- frame durations
- loop or one-shot mode
- current frame by elapsed time
- completed state for one-shot animation

### PixelPetAnimator

Responsible for:

- current state
- idle playback
- blink playback
- random next blink time
- double blink decision
- transition back to idle

### PixelPetAvatar

Compose UI entry:

- load assets
- own animator state
- render current frame on Canvas
- expose debug info if useful during development

---

## MVP Screen

Create one simple Android screen:

```text
HomeScreen / PixelPetScreen
```

It should show:

- dark cyber background
- centered pixel pet eyes
- optional debug text below the pet:
  - current state
  - current frame
  - next blink in ms

Debug text is allowed only for development and can be hidden behind a boolean flag.

---

## Acceptance Criteria

The task is done when:

1. The Android project builds successfully.
2. The app launches to a screen with the pixel eyes centered.
3. `looking-idle.png` loops continuously.
4. `looking-blink.png` interrupts idle at random intervals.
5. Blink timing is not fixed.
6. Double blink sometimes happens but not too often.
7. Rendering remains crisp and pixelated, not blurry.
8. The code is structured so more states can be added later.
9. No camera, audio, AI, BLE, Wi-Fi, memory, or personality features are added in this milestone.
10. `docs/active/implementation-status.md` is updated after completion.

---

## Codex Task Prompt

Use this as the task prompt for Codex:

```text
Reasoning effort: High.

Implement the first Android pixel-pet animation milestone using the existing documentation pack.

Read first:
1. AGENTS.md
2. docs/product/vision.md
3. docs/product/scope.md
4. docs/active/backlog.md
5. docs/animation/looking_idle_blink_android_spec.md

Goal:
Create or complete the Android Kotlin project skeleton and implement the first pixel-eye avatar screen using two sprite sheets:
- app/src/main/assets/pet/eyes/looking-idle.png
- app/src/main/assets/pet/eyes/looking-blink.png

Requirements:
- Use Kotlin and Jetpack Compose Canvas, following AGENTS.md.
- Do not use GIF.
- Load PNG sprite sheets from assets.
- Frame size is 64x32.
- Idle loops continuously.
- Blink is a one-shot animation that interrupts idle at randomized intervals.
- Normal blink interval should be random between 2.5s and 7s.
- Add a 10%–15% chance of double blink with a 100–180 ms gap.
- Use elapsed-time based frame durations, not a fixed global FPS only.
- Render pixel art crisp, with no bitmap smoothing.
- Keep architecture ready for future states like happy, sleepy, angry, look-left, and look-right.
- Do not implement camera, audio, cloud AI, memory, personality, BLE, Wi-Fi, or robot control.
- Update docs/active/implementation-status.md after completion.

Expected output after coding:
- Summary of changes
- Files changed
- How to verify
- Build result
- Remaining risks
- Next recommended task
```
