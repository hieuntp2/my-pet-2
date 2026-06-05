# Module Boundaries

## app

Allowed:

- MainActivity
- Compose app shell
- navigation state
- Home screen
- Debug screens
- manual dependency wiring

Not allowed:

- low-level avatar rendering internals
- personality algorithms
- Room DAO internals
- camera/audio/cloud logic during Phase 1

## ui-avatar

Allowed:

- PixelEyesAvatar
- eye models
- avatar pose models
- blink controller
- idle motion controller
- animation runtime
- reaction state

Not allowed:

- PetState persistence
- Room
- cloud/camera/audio
- business behavior scoring

## brain

Allowed:

- PetMood
- PetState
- PetCondition
- PetEmotionResolver
- PersonalityTraits
- BehaviorCandidate
- BehaviorSelector
- local learning models

Not allowed:

- Android UI
- Compose
- Room DAO implementation
- camera/audio/cloud

## memory

Allowed later:

- Room database
- PetState entity
- PersonalityTraits entity
- event entity
- repositories

Not allowed early:

- fake persistence
- unrelated data models

## core-common

Allowed:

- TimeProvider
- clamp helpers
- random helpers
- small math utilities

Not allowed:

- business logic
- Android UI
- persistence
