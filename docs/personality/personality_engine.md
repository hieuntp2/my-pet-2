# Personality Engine — Local Behavior Learning

## Goal

The pet should slowly develop a unique behavioral tendency based on interaction.

This is not LLM chat. This is not cloud AI. This is local, explainable behavior learning.

## PersonalityTraits

Each trait ranges from 0.0 to 1.0.

Recommended model:

```kotlin
data class PersonalityTraits(
    val curiosity: Float,
    val sociability: Float,
    val playfulness: Float,
    val patience: Float,
    val boldness: Float,
    val affection: Float
)
```

## Trait Meaning

- curiosity: bias toward looking around, peeking, investigating, reacting to novelty
- sociability: bias toward greeting, happy response, attention-seeking
- playfulness: bias toward excited/play reactions
- patience: tolerance for repeated taps or long press while tired
- boldness: bias toward bigger, more expressive reactions
- affection: bias toward soft, warm, bonded responses

## Behavior Selection

A behavior should be selected by score:

```text
score = baseWeight + stateBias + traitBias + recentRewardBias - repetitionPenalty - cooldownPenalty
```

## Behavior Candidates

Initial candidates:

- idle_blink
- idle_soft_float
- look_left
- look_right
- curious_peek
- happy_bounce
- sleepy_slow_blink
- annoyed_squint
- excited_wide_eye
- hungry_expectant_look

## Reward Learning

If pet performs a behavior and user responds positively within a short window, slightly increase that behavior's preference.

Positive responses:

- tap after idle behavior
- play after curious behavior
- feed when hungry expression shown
- returning to app regularly later

Use small updates only.

Example:

```text
newPreference = oldPreference * 0.95 + reward * 0.05
```

Traits and preferences must drift slowly.
