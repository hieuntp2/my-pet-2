# Eye Design System

## Purpose

Define reusable eye models so animation can evolve without rewriting rendering logic.

## Recommended Kotlin Models

```kotlin
enum class AvatarEmotion {
    IDLE,
    HAPPY,
    CURIOUS,
    SLEEPY,
    SAD,
    EXCITED,
    HUNGRY,
    ANNOYED
}

data class EyeShape(
    val width: Float,
    val height: Float,
    val openness: Float,
    val cornerRadius: Float,
    val offsetX: Float,
    val offsetY: Float,
    val highlightAlpha: Float = 0f
)

data class AvatarPose(
    val leftEye: EyeShape,
    val rightEye: EyeShape,
    val globalOffsetY: Float = 0f,
    val globalScale: Float = 1f
)
```

## Emotion Pose Guidelines

### IDLE

- medium width
- medium height
- neutral position
- calm blink

### HAPPY

- slightly squashed height
- upward feeling
- small bounce

### CURIOUS

- asymmetric eyes
- one eye slightly larger or higher
- gaze offset

### SLEEPY

- low openness
- slow blink
- lower eye position

### SAD

- smaller eyes
- lower position
- reduced movement

### EXCITED

- wider eyes
- higher openness
- faster motion

### HUNGRY

- expectant, slightly drooped
- weak movement
- occasional upward glance

### ANNOYED

- narrow eyes
- side gaze
- slow return

## Rendering Guidelines

Initial implementation can draw each eye as:

- rounded rectangle
- clipped by openness
- scaled by height
- positioned relative to center

Future implementation may add:

- pixel highlights
- glow
- stepped eyelids
- emotion particles
