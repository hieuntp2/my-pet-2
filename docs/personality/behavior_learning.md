# Behavior Learning Design

## Purpose

Create the feeling that the pet develops over time without needing cloud AI.

## What Counts as AI Here

For this MVP, AI means:

- local behavior scoring
- local preference learning
- state-based emotion
- personality trait drift
- reinforcement-like adjustment

It does not mean LLM chat.

## Behavior Loop

```text
PetState + PersonalityTraits + RecentHistory
  -> BehaviorSelector
  -> AvatarReaction / IdleBehavior
  -> User response
  -> Reward update
  -> Future behavior changes
```

## Example

Pet chooses `curious_peek`.

If user taps within 5 seconds:

- reward curious_peek +1
- curiosity preference increases slightly
- bond may increase

If user ignores:

- reward curious_peek -0.2
- preference decreases very slightly

## Constraints

- no large jumps
- no behavior should become impossible forever
- randomness must be bounded
- debug explainability is important
