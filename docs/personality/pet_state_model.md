# Pet State Model

## Purpose

PetState is the internal condition of the pet. Animation should eventually be driven by PetState and PersonalityTraits, not only manual debug controls.

## PetMood

Recommended values: HAPPY, NEUTRAL, SAD, EXCITED, CURIOUS, SLEEPY, HUNGRY, ANNOYED.

## PetState

Recommended Kotlin model:

```kotlin
data class PetState(
    val mood: PetMood,
    val energy: Int,
    val hunger: Int,
    val sleepiness: Int,
    val social: Int,
    val bond: Int,
    val lastUpdatedAtMillis: Long
)
```

## Ranges

- energy: 0..100
- hunger: 0..100
- sleepiness: 0..100
- social: 0..100
- bond: 0..100 for MVP

## Initial State

Suggested first launch state:

- mood: CURIOUS
- energy: 70
- hunger: 35
- sleepiness: 25
- social: 60
- bond: 10

## Interaction Effects

Initial simple rules:

- tap: social +2, bond +1, mood toward HAPPY
- play: energy -8, social +8, bond +2, mood toward EXCITED
- feed: hunger -25, bond +1, mood toward HAPPY
- rest: energy +10 later, sleepiness -20, mood toward SLEEPY/NEUTRAL
- repeated fast tap: patience pressure, possible ANNOYED later
