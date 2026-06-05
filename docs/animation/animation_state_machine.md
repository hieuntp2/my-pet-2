# Animation State Machine

## Layers

The avatar has three layers.

### Base Emotion Layer

Represents the current long-running expression.

Examples: IDLE, HAPPY, CURIOUS, SLEEPY, SAD, EXCITED, HUNGRY, ANNOYED.

### Idle Life Layer

Runs while no reaction is active.

Examples: blink, breathing, floating, small gaze shifts, tiny asymmetry.

### Reaction Layer

Temporary animation triggered by events.

Examples: app open greeting, tap, long press, feed, play, rest, ignored repeated tap.

## Priority

```text
Reaction > Greeting > Base Emotion > Idle
```

## Runtime Rules

- Idle cannot interrupt reaction.
- Reaction can interrupt idle.
- Higher-priority reaction can interrupt lower-priority reaction if needed.
- Every reaction must return to base emotion.
- Runtime must never leave invalid eye shape values.
- Use cooldown to avoid spam.
- Use anti-repeat to avoid identical reaction loops.

## Recommended Models

```kotlin
enum class AnimationPriority {
    IDLE,
    BASE,
    GREETING,
    REACTION
}

enum class ReactionType {
    TAP,
    LONG_PRESS,
    FEED,
    PLAY,
    REST,
    IGNORED
}

data class AnimationRuntimeState(
    val baseEmotion: AvatarEmotion,
    val activeReaction: ReactionType?,
    val priority: AnimationPriority,
    val startedAtMillis: Long
)
```

## Completion

A reaction completes when:

- its duration has elapsed
- it is interrupted by higher priority
- app lifecycle stops

After completion:

- activeReaction = null
- baseEmotion remains unchanged
- idle layer resumes
